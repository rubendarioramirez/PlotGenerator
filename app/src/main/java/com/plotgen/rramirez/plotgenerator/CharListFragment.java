package com.plotgen.rramirez.plotgenerator;


import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.constraint.solver.widgets.Guideline;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CharListFragment extends Fragment {

    TextView project_list_tv, empty_character_tv;
    ImageButton charlist_project_delete_btn;
    ListView character_list_lv;
    ArrayList<String> project_list_array;
    ArrayAdapter<String> itemsAdapter;
    private AdView mAdView;

    public CharListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.character_list_tab));
        //Get the data from the previous fragment
        final String project_name_text = this.getArguments().getString("project_name");
        final View myFragmentView =   inflater.inflate(R.layout.fragment_char_list, container, false);

        //Declare elements
        project_list_tv = myFragmentView.findViewById(R.id.project_list_tv);
        character_list_lv = myFragmentView.findViewById(R.id.character_list_lv);
        empty_character_tv= myFragmentView.findViewById(R.id.empty_character_tv);
        charlist_project_delete_btn = myFragmentView.findViewById(R.id.charlist_project_delete_btn);
        project_list_tv.setText(project_name_text);



        //Display the ad
        mAdView = (AdView) myFragmentView.findViewById(R.id.adView_char_list);
        //Display the ad
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("E230AE087E1D0E7FB2304943F378CD64")
                .build();
        mAdView.loadAd(adRequest);


        //Display the adapter
        project_list_array = getProjects(myFragmentView.getContext(), project_name_text.toString());
        itemsAdapter = new ArrayAdapter<String>(myFragmentView.getContext(), android.R.layout.simple_list_item_1, project_list_array);

        character_list_lv.setAdapter(itemsAdapter);
        character_list_lv.setEmptyView(empty_character_tv);
        //When the item is click
        character_list_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                //Get the name that was touched
                String[] charNameRaw = itemsAdapter.getItem(position).split(" - ");
                String charName = charNameRaw[0];

                Bundle bundle = new Bundle();
                bundle.putString("char_name",charName);
                bundle.putString("project_name",project_name_text.toString());
                //Send it to the next fragment
                BioFragment nextFragment = new BioFragment();
                nextFragment.setArguments(bundle);
                //Make the transaction
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);
                transaction.addToBackStack(null);
                transaction.replace(R.id.flMain,nextFragment);
                transaction.commit();

            }
        });


        FloatingActionButton fab = (FloatingActionButton) myFragmentView.findViewById(R.id.add_character_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("project_name",project_list_tv.getText().toString());
                //Send it to the next fragment
                CharacterFragment nextFragment = new CharacterFragment();
                nextFragment.setArguments(bundle);
                //Make the transaction
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.flMain,nextFragment);
                transaction.commit();
            }
        });


        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(myFragmentView.getContext());
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setTitle(getString(R.string.delete_project_btn));
        alertDialogBuilder.setMessage(getString(R.string.delete_project_btn_message));
        // set dialog message
        alertDialogBuilder.setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteFromDB(project_name_text);
            }
        });
        // create alert dialog
        final android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();

        //Click on Delete Button
        charlist_project_delete_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alertDialog.show();
            }
        });


        return myFragmentView;
    }

    public ArrayList<String> getProjects(Context context, String project_name){
        mySQLiteDBHelper myhelper = new mySQLiteDBHelper(context);
        SQLiteDatabase sqLiteDatabase = myhelper.getWritableDatabase();
//        String query = "SELECT * FROM " + mySQLiteDBHelper.CHARACTER_TABLE_CHARACTER + " WHERE project = " + "'" + project_name + "'";
        String query = "SELECT * FROM  " + mySQLiteDBHelper.CHARACTER_TABLE_CHARACTER  + " WHERE project = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(query,new String[]{project_name});
        cursor.moveToFirst();
        ArrayList<String> projects_list = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            String charname = cursor.getString(cursor.getColumnIndex("name"));
            String charRole = cursor.getString(cursor.getColumnIndex("role"));
            projects_list.add(charname + " - " + charRole);
            cursor.moveToNext();
        }
        cursor.close();
        return projects_list;
    }

    private void deleteFromDB(String project_name) {
        SQLiteDatabase database = new mySQLiteDBHelper(this.getContext()).getWritableDatabase();
        database.delete(mySQLiteDBHelper.CHARACTER_TABLE_PROJECT,  "project = ?", new String[]{project_name});
        //Come back to previous fragment
        fragmentTransition();

    }

    public void fragmentTransition(){
        //Send it to the next fragment
        ProjectFragment nextFragment = new ProjectFragment();
        //Make the transaction
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.flMain,nextFragment);
        transaction.commit();

    }



}
