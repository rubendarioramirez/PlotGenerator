package com.plotgen.rramirez.plotgenerator;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CharacterFragment extends Fragment {

    public EditText nameEditText, profession_edit_text, desire_edit_text, age_edit_text, gender_edit_text, placebirth_edit_text, role_edit_text, defmoment_edit_text,need_edit_text;
    public TextView project_name_tv;
    public Button submit;
    ArrayList<String> char_description;

    public CharacterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.character_tab));
        View myFragmentView = inflater.inflate(R.layout.fragment_character, container, false);

        final String project_name_text = this.getArguments().getString("project_name");
        final String name_text = this.getArguments().getString("char_name");


        //Declare all the elements
        project_name_tv = myFragmentView.findViewById(R.id.project_name_tv);
        nameEditText = myFragmentView.findViewById(R.id.nameEditText);
        age_edit_text = myFragmentView.findViewById(R.id.age_edit_text);
        gender_edit_text = myFragmentView.findViewById(R.id.gender_edit_text);
        profession_edit_text = myFragmentView.findViewById(R.id.profession_edit_text);
        placebirth_edit_text = myFragmentView.findViewById(R.id.placebirth_edit_text);
        role_edit_text= myFragmentView.findViewById(R.id.role_edit_text);
        defmoment_edit_text= myFragmentView.findViewById(R.id.defmoment_edit_text);
        desire_edit_text = myFragmentView.findViewById(R.id.desire_edit_text);
        need_edit_text= myFragmentView.findViewById(R.id.need_edit_text);
        //Save button action
        submit =  myFragmentView.findViewById(R.id.submit);


        //Set the title
        project_name_tv.setText(project_name_text);


        if (name_text == null){
            Log.v("this app", "creation mode");
            submit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click
                    saveToDB();
                }
            });
        } else { //If it's in update mode
            //database getter
            char_description = getDescription(myFragmentView.getContext(), name_text.toString());
            nameEditText.setText(char_description.get(0));
            age_edit_text.setText(char_description.get(1));
            gender_edit_text.setText(char_description.get(2));
            placebirth_edit_text.setText(char_description.get(3));
            profession_edit_text.setText(char_description.get(4));
            desire_edit_text.setText(char_description.get(5));
            role_edit_text.setText(char_description.get(6));
            defmoment_edit_text.setText(char_description.get(7));
            need_edit_text.setText(char_description.get(8));

            submit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click
                    updateDB(name_text.toString(), project_name_text.toString());
                }
            });


        }





        return myFragmentView;
    }

    private void saveToDB() {
        SQLiteDatabase database = new mySQLiteDBHelper(this.getContext()).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_NAME, nameEditText.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_JOB, profession_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_DESIRE, desire_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_AGE, age_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_GENDER, gender_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_ROLE, role_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_DEFMOMENT, defmoment_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_NEED, need_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_PLACEBIRTH, placebirth_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_PROJECT, project_name_tv.getText().toString());


        long newRowId = database.insert(mySQLiteDBHelper.CHARACTER_TABLE_CHARACTER, null, values);

        //Come back to previous fragment
        fragmentTransition();

    }


    private void updateDB(String name_text, String project_name){
        SQLiteDatabase database = new mySQLiteDBHelper(this.getContext()).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_NAME, nameEditText.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_JOB, profession_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_DESIRE, desire_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_AGE, age_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_GENDER, gender_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_ROLE, role_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_DEFMOMENT, defmoment_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_NEED, need_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_PLACEBIRTH, placebirth_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_PROJECT, project_name);
        database.update(mySQLiteDBHelper.CHARACTER_TABLE_CHARACTER, values,   "name = ?", new String[]{name_text.toString()});

        //Come back to previous fragment
        fragmentTransition();

    }


    public ArrayList<String> getDescription(Context context, String char_name){
        mySQLiteDBHelper myhelper = new mySQLiteDBHelper(context);
        SQLiteDatabase sqLiteDatabase = myhelper.getWritableDatabase();
        String query = "SELECT * FROM " + mySQLiteDBHelper.CHARACTER_TABLE_CHARACTER + " WHERE name = " + "'" + char_name + "'";
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        cursor.moveToFirst();
        ArrayList<String> char_list = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            char_list.add(cursor.getString(cursor.getColumnIndex("name")));
            char_list.add(cursor.getString(cursor.getColumnIndex("age")));
            char_list.add(cursor.getString(cursor.getColumnIndex("gender")));
            char_list.add(cursor.getString(cursor.getColumnIndex("placebirth")));
            char_list.add(cursor.getString(cursor.getColumnIndex("profession")));
            char_list.add(cursor.getString(cursor.getColumnIndex("desire")));
            char_list.add(cursor.getString(cursor.getColumnIndex("role")));
            char_list.add(cursor.getString(cursor.getColumnIndex("defmoment")));
            char_list.add(cursor.getString(cursor.getColumnIndex("need")));
            cursor.moveToNext();
        }
        cursor.close();
        return char_list;
    }



    public void fragmentTransition(){
        Bundle bundle = new Bundle();
        bundle.putString("project_name",project_name_tv.getText().toString());
        //Send it to the next fragment
        CharListFragment nextFragment = new CharListFragment();
        nextFragment.setArguments(bundle);
        //Make the transaction
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.flMain,nextFragment);
        transaction.commit();

    }


}
