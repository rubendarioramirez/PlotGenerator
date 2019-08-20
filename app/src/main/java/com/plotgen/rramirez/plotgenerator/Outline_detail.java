package com.plotgen.rramirez.plotgenerator;


import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Common.SQLUtils;
import com.plotgen.rramirez.plotgenerator.Common.Utils;
import com.plotgen.rramirez.plotgenerator.Common.mySQLiteDBHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Outline_detail extends Fragment {

    //region Declare elements
    public Boolean updateMode;
    String projectID;
    ArrayList<String> project_description, characters;
    private FirebaseAnalytics mFirebaseAnalytics;
    private View myFragmentView;

    //Popup Multiple choice
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();

    private ArrayList<String> project_list_array = new ArrayList<>();

    @BindView(R.id.outline_title_et)
    EditText outline_title_et;
    @BindView(R.id.outline_description_et)
    EditText outline_description_et;
    @BindView(R.id.outline_submit_btn)
    FloatingActionButton outline_submit_btn;
    @BindView(R.id.outline_delete_btn)
    FloatingActionButton outline_delete_btn;

    @BindView(R.id.outline_characters_btn)
    Button outline_characters_btn;

    @BindView(R.id.outline_characters_selected_tv)
    EditText outline_characters_selected_tv;

    //endregion

    public Outline_detail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.outline_detail, container, false);
        ButterKnife.bind(this, myFragmentView);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(myFragmentView.getContext());

        characters = new ArrayList<String>();
        characters = SQLUtils.getCharNamesByID(getContext(),Common.currentProject.getId());
        listItems = characters.toArray(new String[characters.size()]);
        checkedItems = new boolean[characters.size()];

        outline_characters_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                mBuilder.setTitle("Choose a character");
                mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                        if(isChecked){
                            mUserItems.add(position);
                        }else{
                            mUserItems.remove((Integer.valueOf(position)));
                        }
                    }
                });

                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String item = "";
                        for (int i = 0; i < mUserItems.size(); i++) {
                            item = item + listItems[mUserItems.get(i)];
                            if (i != mUserItems.size() - 1) {
                                item = item + ", ";
                            }
                        }
                        outline_characters_selected_tv.setText(item);
                    }
                });

                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                mBuilder.setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for (int i = 0; i < checkedItems.length; i++) {
                            checkedItems[i] = false;
                            mUserItems.clear();
                            outline_characters_selected_tv.setText("");
                        }
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });


        outline_submit_btn.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            try {
                saveToDB();
                Toast.makeText(getContext(),"Saved", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        });


        outline_delete_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Toast.makeText(getContext(),"Delete button pressed", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        return myFragmentView;
    }


    //Save DB for the selected OUTLINE
    private void saveToDB() {
        SQLiteDatabase database = new mySQLiteDBHelper(this.getContext()).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(mySQLiteDBHelper.OUTLINE_COLUMN_NAME, outline_title_et.getText().toString());
        values.put(mySQLiteDBHelper.OUTLINE_COLUMN_DESCRIPTION, outline_description_et.getText().toString());
        values.put(mySQLiteDBHelper.OUTLINE_COLUMN_PROJECT_ID, Common.currentProject.getId());
        values.put(mySQLiteDBHelper.OUTLINE_COLUMN_POSITION, "0");
        values.put(mySQLiteDBHelper.OUTLINE_COLUMN_CHARACTERS, outline_characters_selected_tv.getText().toString());
        database.insert(mySQLiteDBHelper.CHARACTER_TABLE_OUTLINE, null, values);
        database.close();
    }

    //Update DB for the selected OUTLINE
    private void updateDB() {
        SQLiteDatabase database = new mySQLiteDBHelper(this.getContext()).getWritableDatabase();
        ContentValues values = new ContentValues();
      // values.put(mySQLiteDBHelper.PROJECT_COLUMN_PROJECT, chapter_name_et.getText().toString());
      //  values.put(mySQLiteDBHelper.PROJECT_COLUMN_PLOT, chapter_summary_et.getText().toString());
        database.update(mySQLiteDBHelper.CHARACTER_TABLE_PROJECT, values, "_id = ?", new String[]{projectID});
        database.close();
    }


    //Get outline selected



    private void fragmentTransaction(){
        ProjectFragment nextFragment = new ProjectFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);
        transaction.replace(R.id.flMain, nextFragment);
        getActivity().getSupportFragmentManager().popBackStack();
        transaction.commit();
    }


}
