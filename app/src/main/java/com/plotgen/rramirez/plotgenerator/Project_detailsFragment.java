package com.plotgen.rramirez.plotgenerator;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
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

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Project_detailsFragment extends Fragment {

    TextView project_name_et, project_plot_et;
    Spinner project_genre_spinner;
    public Boolean updateMode;
    String project_name_text;

    public Project_detailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myFragmentView =  inflater.inflate(R.layout.fragment_project_details, container, false);
        project_name_text = this.getArguments().getString("project_name");

        project_name_et = myFragmentView.findViewById(R.id.project_name_et);
        project_plot_et = myFragmentView.findViewById(R.id.project_plot_et);
        FloatingActionButton fab_save = myFragmentView.findViewById(R.id.project_add_submit);
        final FloatingActionButton fab_delete = myFragmentView.findViewById(R.id.project_detail_delete);

        //Role spinner functions
        project_genre_spinner = myFragmentView.findViewById(R.id.project_genre_spinner);
        ArrayAdapter<CharSequence> genre_adapter = ArrayAdapter.createFromResource(myFragmentView.getContext(), R.array.genres_array, android.R.layout.simple_spinner_item);
        genre_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        project_genre_spinner.setAdapter(genre_adapter);

        if(project_name_text != ""){
            //Update mode
            ArrayList<String> project_list_array = Utils.getProject(this.getContext(), project_name_text);
            project_name_et.setText(project_list_array.get(0));
            project_plot_et.setText(project_list_array.get(2));
            //TODO UPDATE THE SPINNER IN THE SAME POSITION
            updateMode = true;
        } else {
            updateMode = false;
        }


        fab_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(updateMode)
                {
                    updateDB();
                    fab_delete.setVisibility(View.INVISIBLE);
                }
                else {
                    saveToDB(project_name_et, project_plot_et, project_genre_spinner);
                    fab_delete.setVisibility(View.VISIBLE);
                }
                ProjectFragment nextFragment = new ProjectFragment();
                //Make the transaction
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Utils.changeFragment(nextFragment,transaction,"","");
            }
        });


        fab_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(getContext());
                }
                builder.setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                Utils.deleteFromDB(view.getContext(),project_name_text);
                                ProjectFragment nextFragment = new ProjectFragment();
                                //Make the transaction
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                Utils.changeFragment(nextFragment,transaction,"","");
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
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
        database.close();

    }

    private void updateDB(){
        SQLiteDatabase database = new mySQLiteDBHelper(this.getContext()).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(mySQLiteDBHelper.PROJECT_COLUMN_PROJECT, project_name_et.getText().toString());
        values.put(mySQLiteDBHelper.PROJECT_COLUMN_GENRE, project_genre_spinner.getSelectedItem().toString());
        values.put(mySQLiteDBHelper.PROJECT_COLUMN_PLOT, project_plot_et.getText().toString());
        database.update(mySQLiteDBHelper.CHARACTER_TABLE_PROJECT, values,   "project = ?", new String[]{project_name_text.toString()});
        database.close();
        //Come back to previous fragment
        ProjectFragment nextFragment = new ProjectFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Utils.changeFragment(nextFragment,transaction,"","");


    }


}
