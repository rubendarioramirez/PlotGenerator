package com.plotgen.rramirez.plotgenerator;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class Project_detailsFragment extends Fragment {

    TextView project_name_et, project_plot_et;
    Spinner project_genre_spinner;

    public Project_detailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myFragmentView =  inflater.inflate(R.layout.fragment_project_details, container, false);

        project_name_et = myFragmentView.findViewById(R.id.project_name_et);
        project_plot_et = myFragmentView.findViewById(R.id.project_plot_et);

        //Role spinner functions
        project_genre_spinner = myFragmentView.findViewById(R.id.project_genre_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> genre_adapter = ArrayAdapter.createFromResource(myFragmentView.getContext(),
                R.array.genres_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        genre_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        project_genre_spinner.setAdapter(genre_adapter);

        FloatingActionButton fab = (FloatingActionButton) myFragmentView.findViewById(R.id.project_add_submit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToDB(project_name_et, project_plot_et, project_genre_spinner);
                ProjectFragment nextFragment = new ProjectFragment();
                //Make the transaction
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_from_right);
                transaction.replace(R.id.flMain,nextFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return myFragmentView;

    }


    private void saveToDB(TextView project_name_et, TextView project_plot_et, Spinner project_genre_spinner ) {
        SQLiteDatabase database = new mySQLiteDBHelper(this.getContext()).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(mySQLiteDBHelper.PROJECT_COLUMN_PROJECT, project_name_et.getText().toString());
        values.put(mySQLiteDBHelper.PROJECT_COLUMN_GENRE, project_genre_spinner.getSelectedItem().toString());
        values.put(mySQLiteDBHelper.PROJECT_COLUMN_PLOT, project_plot_et.getText().toString());
        long newRowId = database.insert(mySQLiteDBHelper.CHARACTER_TABLE_PROJECT, null, values);

    }

}
