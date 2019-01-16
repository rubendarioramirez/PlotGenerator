package com.plotgen.rramirez.plotgenerator.Fragment;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.MainActivity;
import com.plotgen.rramirez.plotgenerator.ProjectFragment;
import com.plotgen.rramirez.plotgenerator.R;
import com.plotgen.rramirez.plotgenerator.Utils;
import com.plotgen.rramirez.plotgenerator.mySQLiteDBHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.richeditor.RichEditor;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class OfflineStoryFragment extends Fragment {

    @BindView(R.id.editor)
    RichEditor mEditor;
    @BindView(R.id.formatBold)
    ImageView ivBold;
    @BindView(R.id.formatItalic)
    ImageView ivItalic;
    @BindView(R.id.formatAlignLeft)
    ImageView ivAlignLeft;
    @BindView(R.id.formatAlignCenter)
    ImageView ivAlignCenter;
    @BindView(R.id.formatAlignRight)
    ImageView ivAlignRight;

    private AdView mAdView;


    public void saveStoryToDB(View v) {
        SQLiteDatabase database = new mySQLiteDBHelper(this.getContext()).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(mySQLiteDBHelper.STORY_COLUMN_PROJECT, project_name);
        values.put(mySQLiteDBHelper.STORY_COLUMN_PROJECT_ID, project_id);
        values.put(mySQLiteDBHelper.STORY_COLUMN_STORIES, mStory);

        if (isUpdate)
            database.update(mySQLiteDBHelper.CHARACTER_TABLE_STORY, values, "project = ?", new String[]{project_name});
        else
            database.insert(mySQLiteDBHelper.CHARACTER_TABLE_STORY, null, values);

        ProjectFragment nextFragment = new ProjectFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Utils.changeFragment(nextFragment, transaction, "", "");
        getFragmentManager().popBackStack();

    }


    @OnClick(R.id.formatBold)
    public void setBold(View v) {
        mEditor.setBold();

    }

    @OnClick(R.id.formatItalic)
    public void setItalic(View v) {
        mEditor.setItalic();

    }

    @OnClick(R.id.formatAlignLeft)
    public void setAlignLeft(View v) {
        mEditor.setAlignLeft();

    }

    @OnClick(R.id.formatAlignCenter)
    public void setAlignCenter(View v) {
        mEditor.setAlignCenter();

    }

    @OnClick(R.id.formatAlignRight)
    public void setAlignRight(View v) {
        mEditor.setAlignRight();

    }


    private boolean isBold = false;
    private boolean isItalic = false;

    private boolean isUpdate = false;

    private String project_name, project_id;
    private String mStory;


    public OfflineStoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity)getActivity()).setActionBarTitle("My Story");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_offline_story, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        ButterKnife.bind(this, view);

        final String project_info = this.getArguments().getString("project_info");
        project_name = project_info.substring(2);
        project_id = String.valueOf(project_info.charAt(0));

        mStory =  getStoryFromDB(view.getContext(), project_name);
        //Save button
        FloatingActionButton fab = view.findViewById(R.id.btnSaveStory);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveStoryToDB(view);
            }
        });

        if(!Common.isPAU) {
            mAdView = (AdView) view.findViewById(R.id.adView_offile_story);
            Utils.loadAd(mAdView);
        }



        mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(16);
        int selectedTheme = Utils.getSharePref(view.getContext(), "selectedTheme", 0);
        if (selectedTheme == 0) {
            mEditor.setEditorFontColor(Color.BLACK);
            mEditor.setBackgroundColor(getResources().getColor(R.color.background));
            mEditor.setBackgroundColor(getResources().getColor(R.color.background));
        } else if (selectedTheme == 1) {
            //DARK THEME
            mEditor.setEditorFontColor(Color.WHITE);
            mEditor.setBackgroundColor(getResources().getColor(R.color.background_2));
            mEditor.setTextBackgroundColor(getResources().getColor(R.color.background_2));
        } else if (selectedTheme == 2) {
            //ROMANCE THEME
            mEditor.setEditorFontColor(Color.BLACK);
            mEditor.setBackgroundColor(getResources().getColor(R.color.background_3));
            mEditor.setTextBackgroundColor(getResources().getColor(R.color.background_3));
        } else if (selectedTheme == 3) {
            //AUTUMN THEME
            mEditor.setEditorFontColor(Color.BLACK);
            mEditor.setBackgroundColor(getResources().getColor(R.color.background_4));
            mEditor.setTextBackgroundColor(getResources().getColor(R.color.background_4));
        }



        mEditor.setPadding(10, 10, 10, 10);
//        mEditor.setScrollY(400);
//        mEditor.setScrollBarFadeDuration(0);
        if(mStory.equals(""))
        {
            mEditor.setPlaceholder("Insert text here...");

        }
        else
        {
            isUpdate = true;
            mEditor.setHtml(mStory);
        }

        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override public void onTextChange(String text) {

                mStory = text;
//                mEditor.scrollBy(0,500);
            }
        });



        return view;
    }



    public static String getStoryFromDB(Context context, String project_name){
        mySQLiteDBHelper myhelper = new mySQLiteDBHelper(context);
        SQLiteDatabase sqLiteDatabase = myhelper.getWritableDatabase();
        String query = "SELECT * FROM  " + mySQLiteDBHelper.CHARACTER_TABLE_STORY  + " WHERE project = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(query,new String[]{project_name});
        cursor.moveToFirst();
        String s = "";
        ArrayList<String> story_list = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            story_list.add(cursor.getString(cursor.getColumnIndex("project")));
            story_list.add(cursor.getString(cursor.getColumnIndex("project_id")));
            story_list.add(cursor.getString(cursor.getColumnIndex("stories")));

            s = cursor.getString(cursor.getColumnIndex("stories"));
            cursor.moveToNext();
        }
        cursor.close();
        sqLiteDatabase.close();
        return s;
    }

}
