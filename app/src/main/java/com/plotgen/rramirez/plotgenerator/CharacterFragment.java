package com.plotgen.rramirez.plotgenerator;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class CharacterFragment extends Fragment {

    public TextView nameEditText;
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

        project_name_tv.setText(project_name_text);


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
        long newRowId = database.insert(mySQLiteDBHelper.CHARACTER_TABLE_NAME, null, values);

        Toast.makeText(this.getContext(), "The new Row Id is " + newRowId, Toast.LENGTH_LONG).show();
    }



}
