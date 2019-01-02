package com.plotgen.rramirez.plotgenerator.Fragment;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
    @BindView(R.id.btnSaveStory)
    Button btnSaveStory;

    @OnClick(R.id.btnSaveStory)
    public void saveStoryToDB(View v)
    {
        SQLiteDatabase database = new mySQLiteDBHelper(this.getContext()).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(mySQLiteDBHelper.STORY_COLUMN_PROJECT, project_name);
        values.put(mySQLiteDBHelper.STORY_COLUMN_PROJECT_ID, project_id);
        values.put(mySQLiteDBHelper.STORY_COLUMN_STORIES, mStory);

        if(isUpdate)
            database.update(mySQLiteDBHelper.CHARACTER_TABLE_STORY, values,   "project = ?", new String[]{project_name});
        else
            database.insert(mySQLiteDBHelper.CHARACTER_TABLE_STORY, null, values);

        ProjectFragment nextFragment = new ProjectFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Utils.changeFragment(nextFragment,transaction,"","");
        getFragmentManager().popBackStack();

    }



    @OnClick(R.id.formatBold)
    public void setBold(View v)
    {
        mEditor.setBold();

    }

    @OnClick(R.id.formatItalic)
    public void setItalic(View v)
    {
        mEditor.setItalic();

    }

    @OnClick(R.id.formatAlignLeft)
    public void setAlignLeft(View v)
    {
        mEditor.setAlignLeft();
        //ivAlignLeft.setBackgroundColor(Color.GRAY);
        //ivAlignCenter.setBackgroundColor(Color.WHITE);
        //ivAlignRight.setBackgroundColor(Color.WHITE);

    }

    @OnClick(R.id.formatAlignCenter)
    public void setAlignCenter(View v)
    {
        mEditor.setAlignCenter();
        //ivAlignLeft.setBackgroundColor(Color.WHITE);
        //ivAlignCenter.setBackgroundColor(Color.GRAY);
        //ivAlignRight.setBackgroundColor(Color.WHITE);

    }

    @OnClick(R.id.formatAlignRight)
    public void setAlignRight(View v)
    {
        mEditor.setAlignRight();
        //ivAlignLeft.setBackgroundColor(Color.WHITE);
        //ivAlignCenter.setBackgroundColor(Color.WHITE);
        //ivAlignRight.setBackgroundColor(Color.GRAY);

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
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ButterKnife.bind(this, view);

        final String project_info = this.getArguments().getString("project_info");
        project_name = project_info.substring(2);
        project_id = String.valueOf(project_info.charAt(0));

        mStory =  getStoryFromDB(view.getContext(), project_name);
        btnSaveStory.setVisibility(View.INVISIBLE);

        mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(16);
        mEditor.setPadding(10, 10, 10, 10);
        if(mStory.equals(""))
        {
            mEditor.setPlaceholder("Insert text here...");

        }
        else
        {
            isUpdate = true;
            mEditor.setHtml(mStory);
            btnSaveStory.setText("Update");
        }


        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override public void onTextChange(String text) {
                mStory = text;
                btnSaveStory.setVisibility(View.VISIBLE);
            }
        });

        //ivAlignLeft.setBackgroundColor(Color.GRAY);

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
