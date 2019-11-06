package com.plotgen.rramirez.plotgenerator;


import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.plotgen.rramirez.plotgenerator.Common.dBHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Chapter_creation extends Fragment {

    //region Declare elements
    private static final int PERMISSION_REQUEST_GALLERY = 101;
    private static final int REQUEST_CODE_GALLERY = 102;
    public Boolean updateMode;
    String project_name_text, projectID;
    ArrayList<String> project_description;
    private FirebaseAnalytics mFirebaseAnalytics;
    private View myFragmentView;
    private String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private Uri uri;
    private String filepath = "";
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ArrayList<String> project_list_array = new ArrayList<>();

    @BindView(R.id.chapter_name_et)
    EditText chapter_name_et;
    @BindView(R.id.chapter_summary_et)
    EditText chapter_summary_et;
    @BindView(R.id.project_add_submit)
    FloatingActionButton fab_save;
    @BindView(R.id.project_detail_delete)
    FloatingActionButton fab_delete;
    //endregion

    public Chapter_creation() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.chapter_creation, container, false);
        ButterKnife.bind(this, myFragmentView);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(myFragmentView.getContext());


        return myFragmentView;
    }


    private void saveToDB() {
        SQLiteDatabase database = new dBHelper(this.getContext()).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(dBHelper.PROJECT_COLUMN_PROJECT, chapter_name_et.getText().toString());
        values.put(dBHelper.PROJECT_COLUMN_PLOT, chapter_summary_et.getText().toString());
        database.insert(dBHelper.TABLE_PROJECT, null, values);
        database.close();

    }

    private void updateDB() {
        SQLiteDatabase database = new dBHelper(this.getContext()).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(dBHelper.PROJECT_COLUMN_PROJECT, chapter_name_et.getText().toString());
        values.put(dBHelper.PROJECT_COLUMN_PLOT, chapter_summary_et.getText().toString());
        values.put(dBHelper.PROJECT_COLUMN_IMAGE, filepath);
        database.update(dBHelper.TABLE_PROJECT, values, "_id = ?", new String[]{projectID});
        database.close();
    }


    public ArrayList<String> getProject(Context context) {
        dBHelper myhelper = new dBHelper(context);
        SQLiteDatabase sqLiteDatabase = myhelper.getWritableDatabase();
        String query = "SELECT * FROM  " + dBHelper.TABLE_PROJECT + " WHERE _id = ?";
        ArrayList<String> char_list = new ArrayList<String>();
        try {
            Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{projectID});
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                char_list.add(cursor.getString(cursor.getColumnIndex("project")));
                char_list.add(cursor.getString(cursor.getColumnIndex("genre")));
                char_list.add(cursor.getString(cursor.getColumnIndex("plot")));
                if (cursor.getColumnIndex("image") != -1 && cursor.getString(cursor.getColumnIndex("image")) != null)
                    char_list.add(cursor.getString(cursor.getColumnIndex("image")));
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e){
            Log.v("matilda", e.toString());
        }
        return char_list;
    }


    private void fragmentTransaction(){
        ProjectFragment nextFragment = new ProjectFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);
        transaction.replace(R.id.flMain, nextFragment);
        getActivity().getSupportFragmentManager().popBackStack();
        transaction.commit();
    }


}
