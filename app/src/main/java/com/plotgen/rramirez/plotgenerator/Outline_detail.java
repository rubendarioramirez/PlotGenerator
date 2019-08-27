package com.plotgen.rramirez.plotgenerator;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Common.SQLUtils;
import com.plotgen.rramirez.plotgenerator.Common.mySQLiteDBHelper;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Outline_detail extends Fragment {

    //region Declare elements
    public Boolean updateMode;
    String outlineID;
    ArrayList<String> characters;
    private FirebaseAnalytics mFirebaseAnalytics;
    private View myFragmentView;

    //Popup Multiple choice
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();

    @BindView(R.id.outline_title_et)
    EditText outline_title_et;
    @BindView(R.id.outline_description_et)
    EditText outline_description_et;
    @BindView(R.id.outline_submit_btn)
    FloatingActionButton outline_submit_btn;
    @BindView(R.id.outline_delete_btn)
    FloatingActionButton outline_delete_btn;

    @BindView(R.id.outline_characters_selected_tv)
    TextView outline_characters_selected_tv;

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
        if(!Common.outlineCreationMode){
            updateUI();
            outlineID = Common.currentOutlineID;
        }


        //region Choose characters
        outline_characters_selected_tv.setOnClickListener(new View.OnClickListener() {
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
        //endregion


        outline_submit_btn.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            try {
                if(Common.outlineCreationMode){
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

        if(Common.outlineCreationMode){
            outline_delete_btn.setVisibility(View.INVISIBLE);
        }
        outline_delete_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    SQLUtils.deleteOutlineFromDB(getContext(),outlineID);
                    fragmentTransaction();
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
        database.insert(mySQLiteDBHelper.TABLE_OUTLINE, null, values);
        database.close();
        fragmentTransaction();
    }

    //Update DB for the selected OUTLINE
    private void updateDB() {
        SQLiteDatabase database = new mySQLiteDBHelper(this.getContext()).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(mySQLiteDBHelper.OUTLINE_COLUMN_NAME, outline_title_et.getText().toString());
        values.put(mySQLiteDBHelper.OUTLINE_COLUMN_DESCRIPTION, outline_description_et.getText().toString());
        values.put(mySQLiteDBHelper.OUTLINE_COLUMN_PROJECT_ID, Common.currentProject.getId());
        values.put(mySQLiteDBHelper.OUTLINE_COLUMN_POSITION, "0");
        values.put(mySQLiteDBHelper.OUTLINE_COLUMN_CHARACTERS, outline_characters_selected_tv.getText().toString());
        database.update(mySQLiteDBHelper.TABLE_OUTLINE, values, "_id = ?", new String[]{outlineID});
        database.close();
        fragmentTransaction();
    }


    //Get outline selected
    private void updateUI(){
        ArrayList<String> currentOutline = new ArrayList<String>();
        currentOutline = SQLUtils.getOutlineByID(getContext(),Common.currentOutlineID);
        outline_title_et.setText(currentOutline.get(0).split("/&&/")[1]);
        outline_description_et.setText(currentOutline.get(0).split("/&&/")[2]);
        outline_characters_selected_tv.setText(currentOutline.get(0).split("/&&/")[3]);
    }



    private void fragmentTransaction(){
        OutlineFragment nextFragment = new OutlineFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);
        transaction.replace(R.id.flMain, nextFragment);
        getActivity().getSupportFragmentManager().popBackStack();
        transaction.commit();
    }


}
