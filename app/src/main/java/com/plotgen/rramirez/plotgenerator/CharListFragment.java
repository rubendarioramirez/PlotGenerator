package com.plotgen.rramirez.plotgenerator;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CharListFragment extends Fragment {

    TextView project_list_tv;
    Button add_character_btn;
    ListView character_list_lv;
    ArrayList<String> project_list_array;
    ArrayAdapter<String> itemsAdapter;

    public CharListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.character_list_tab));
        //Get the data from the previous fragment
        String project_name_text = this.getArguments().getString("project_name");
        final View myFragmentView =   inflater.inflate(R.layout.fragment_char_list, container, false);


        //Declare elements
        project_list_tv = myFragmentView.findViewById(R.id.project_list_tv);
        add_character_btn = myFragmentView.findViewById(R.id.add_character_btn);
        character_list_lv = myFragmentView.findViewById(R.id.character_list_lv);


        project_list_tv.setText(project_name_text);


        //Display the adapter
        project_list_array = getProjects(myFragmentView.getContext(), project_name_text.toString());
        itemsAdapter = new ArrayAdapter<String>(myFragmentView.getContext(), android.R.layout.simple_list_item_1, project_list_array);
        character_list_lv.setAdapter(itemsAdapter);
        character_list_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Log.v("THIS APP", "ITEM CLICK AT POSITION " + itemsAdapter.getItem(position));
            }
        });





        //Click on Add character
        add_character_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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

        return myFragmentView;
    }

    public ArrayList<String> getProjects(Context context, String project_name){
        mySQLiteDBHelper myhelper = new mySQLiteDBHelper(context);
        SQLiteDatabase sqLiteDatabase = myhelper.getWritableDatabase();
        String query = "SELECT * FROM " + mySQLiteDBHelper.CHARACTER_TABLE_CHARACTER + " WHERE project = " + "'" + project_name + "'";
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        cursor.moveToFirst();
        ArrayList<String> projects_list = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            projects_list.add(cursor.getString(cursor.getColumnIndex("name")));
            cursor.moveToNext();
        }
        cursor.close();
        return projects_list;
    }



}
