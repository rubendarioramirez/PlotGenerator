package com.plotgen.rramirez.plotgenerator;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Common.SQLUtils;
import com.plotgen.rramirez.plotgenerator.Common.Utils;
import com.plotgen.rramirez.plotgenerator.Common.mySQLiteDBHelper;
import com.plotgen.rramirez.plotgenerator.Fragment.Container_charbio;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Timeline_detail extends Fragment {

    //region Declare elements
    public Boolean updateMode;
    String timelineID;
    private FirebaseAnalytics mFirebaseAnalytics;
    private View myFragmentView;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Date unixDate;
    private String unixUpdate;

    @BindView(R.id.timeline_title_et)
    EditText timeline_title_et;
    @BindView(R.id.timeline_description_et)
    EditText timeline_description_et;
    @BindView(R.id.timeline_datepicker)
    EditText timeline_datepicker;
    @BindView(R.id.timeline_submit_btn)
    FloatingActionButton timeline_submit_btn;
    @BindView(R.id.timeline_delete_btn)
    FloatingActionButton timeline_delete_btn;
    //endregion
    final Calendar myCalendar = Calendar.getInstance();

    public Timeline_detail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.timeline_detail, container, false);
        ButterKnife.bind(this, myFragmentView);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(myFragmentView.getContext());

        if(!Common.timelineCreationMode){
            updateUI();
            timelineID = Common.currentTimelineID;
        }


        timeline_submit_btn.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            try {
                if(Common.timelineCreationMode){
                    saveToDB();
                } else {
                    updateDB();
                }
                Toast.makeText(getContext(),"Saved", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        });

        if(Common.timelineCreationMode){
            timeline_delete_btn.setVisibility(View.INVISIBLE);
        }
        timeline_delete_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(myFragmentView.getContext());
                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setTitle("Are you sure? This can't be undone");
                    // set dialog message
                    alertDialogBuilder.setCancelable(true).setPositiveButton("YES, DELETE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            SQLUtils.deleteTimelineFromDB(getContext(),timelineID);
                            fragmentTransaction();
                        }
                    });
                    // create alert dialog
                    final android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        timeline_datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    unixDate = formatter.parse(date);
                    unixUpdate = String.valueOf(formatter.parse(date).getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                timeline_datepicker.setText(date);
            }
        };



        return myFragmentView;
    }


    //Save DB for the selected OUTLINE
    private void saveToDB() {
        SQLiteDatabase database = new mySQLiteDBHelper(this.getContext()).getWritableDatabase();
        ContentValues values = new ContentValues();
        String title = timeline_title_et.getText().toString();
        values.put(mySQLiteDBHelper.TIMELINE_COLUMN_TITLE, title);
        values.put(mySQLiteDBHelper.TIMELINE_COLUMN_DESCRIPTION, timeline_description_et.getText().toString());
        values.put(mySQLiteDBHelper.TIMELINE_COLUMN_CHARACTER_ID, Common.currentCharacter.getId());
        values.put(mySQLiteDBHelper.TIMELINE_COLUMN_POSITION, "0");
        values.put(mySQLiteDBHelper.TIMELINE_COLUMN_DATE, unixDate.getTime());
        database.insert(mySQLiteDBHelper.TABLE_TIMELINE, null, values);
        database.close();
        fragmentTransaction();
    }

    //Update DB for the selected OUTLINE
    private void updateDB() {
        SQLiteDatabase database = new mySQLiteDBHelper(this.getContext()).getWritableDatabase();
        ContentValues values = new ContentValues();
        String title = timeline_title_et.getText().toString();
        values.put(mySQLiteDBHelper.TIMELINE_COLUMN_TITLE, title);
        values.put(mySQLiteDBHelper.TIMELINE_COLUMN_DESCRIPTION, timeline_description_et.getText().toString());
        values.put(mySQLiteDBHelper.TIMELINE_COLUMN_CHARACTER_ID, Common.currentCharacter.getId());
        values.put(mySQLiteDBHelper.TIMELINE_COLUMN_POSITION, "0");
        values.put(mySQLiteDBHelper.TIMELINE_COLUMN_DATE, unixUpdate);
        database.update(mySQLiteDBHelper.TABLE_TIMELINE, values, "_id = ?", new String[]{timelineID});
        database.close();
        Toast.makeText(getContext(),"Updated", Toast.LENGTH_LONG).show();
        fragmentTransaction();
    }


    //Get outline selected
    private void updateUI(){
        ArrayList<String> currentTimeline = new ArrayList<String>();
        currentTimeline = SQLUtils.getTimelineByID(getContext(),Common.currentTimelineID);
        timeline_title_et.setText(currentTimeline.get(0).split("/&&/")[1]);
        timeline_description_et.setText(currentTimeline.get(0).split("/&&/")[2]);
        String date = Utils.unixToString(currentTimeline.get(0).split("/&&/")[4]);
        timeline_datepicker.setText(date);
        unixUpdate = currentTimeline.get(0).split("/&&/")[4];
    }


    private void fragmentTransaction(){
        Container_charbio nextFragment = new Container_charbio();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);
        transaction.replace(R.id.flMain, nextFragment);
        //getActivity().getSupportFragmentManager().popBackStack();
        transaction.commit();
    }

}
