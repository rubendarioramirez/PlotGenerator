package com.plotgen.rramirez.plotgenerator;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class CharacterFragment extends Fragment {

    public EditText nameEditText, profession_edit_text, desire_edit_text, age_edit_text, placebirth_edit_text, role_edit_text, defmoment_edit_text,need_edit_text;
    public TextView project_name_tv;
    public Button submit;

    public CharacterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.character_tab));
        View myFragmentView = inflater.inflate(R.layout.fragment_character, container, false);

        String project_name_text = this.getArguments().getString("project_name");

        //Declare all the elements
        project_name_tv = myFragmentView.findViewById(R.id.project_name_tv);
        nameEditText = myFragmentView.findViewById(R.id.nameEditText);
        profession_edit_text = myFragmentView.findViewById(R.id.profession_edit_text);
        desire_edit_text = myFragmentView.findViewById(R.id.desire_edit_text);
        age_edit_text = myFragmentView.findViewById(R.id.age_edit_text);
        placebirth_edit_text = myFragmentView.findViewById(R.id.placebirth_edit_text);
        role_edit_text= myFragmentView.findViewById(R.id.role_edit_text);
        defmoment_edit_text= myFragmentView.findViewById(R.id.defmoment_edit_text);
        need_edit_text= myFragmentView.findViewById(R.id.need_edit_text);


        //Set the title
        project_name_tv.setText(project_name_text);


        //Save button action
        submit =  myFragmentView.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                saveToDB();
            }
        });

        return myFragmentView;
    }

    private void saveToDB() {
        SQLiteDatabase database = new mySQLiteDBHelper(this.getContext()).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_NAME, nameEditText.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_JOB, profession_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_DESIRE, desire_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_AGE, age_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_ROLE, role_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_DEFMOMENT, defmoment_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_NEED, need_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_PLACEBIRTH, placebirth_edit_text.getText().toString());
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_PROJECT, project_name_tv.getText().toString());


        long newRowId = database.insert(mySQLiteDBHelper.CHARACTER_TABLE_CHARACTER, null, values);

        Toast.makeText(this.getContext(), "The new Row Id is " + newRowId, Toast.LENGTH_LONG).show();

    }



}
