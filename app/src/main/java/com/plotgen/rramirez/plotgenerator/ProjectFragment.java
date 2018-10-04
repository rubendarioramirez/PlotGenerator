package com.plotgen.rramirez.plotgenerator;


import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class ProjectFragment extends Fragment {

    ListView project_lv;
    ArrayList<String> project_list_array;
    ArrayAdapter<String> itemsAdapter;
    TextView empty_project_tv;

    private AdView mAdView;

    public ProjectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.projects_tab));
        // Inflate the layout for this fragment

        final View myFragmentView = inflater.inflate(R.layout.fragment_project, container, false);

        project_lv  = myFragmentView.findViewById(R.id.project_lv);
        empty_project_tv = myFragmentView.findViewById(R.id.empty_project_tv);
        final EditText et = new EditText(myFragmentView.getContext());
        project_list_array = getProjects(myFragmentView.getContext());
        itemsAdapter = new ArrayAdapter<String>(myFragmentView.getContext(), android.R.layout.simple_list_item_1, project_list_array);
        project_lv.setAdapter(itemsAdapter);
        project_lv.setEmptyView(empty_project_tv);

        project_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                //Bundle the project INFO
                Bundle bundle = new Bundle();
                bundle.putString("project_name",itemsAdapter.getItem(position).toString());
                //Send it to the next fragment
                changeFragment(bundle);
            }
        });



        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(myFragmentView.getContext());
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setTitle(getString(R.string.add_projects_btn));
        alertDialogBuilder.setView(et);

        // set dialog message
        alertDialogBuilder.setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(et.getText().toString() != " ") {
                    saveToDB(et);
                    itemsAdapter.notifyDataSetChanged();
                }
            }
        });
        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();
        FloatingActionButton fab = (FloatingActionButton) myFragmentView.findViewById(R.id.add_project_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });




    mAdView = (AdView) myFragmentView.findViewById(R.id.adView_project_frag);
        //Display the ad
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("E230AE087E1D0E7FB2304943F378CD64")
                .build();
        mAdView.loadAd(adRequest);



        return myFragmentView;

    }



    private void saveToDB(TextView et) {
        SQLiteDatabase database = new mySQLiteDBHelper(this.getContext()).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(mySQLiteDBHelper.CHARACTER_COLUMN_PROJECT, et.getText().toString());
        long newRowId = database.insert(mySQLiteDBHelper.CHARACTER_TABLE_PROJECT, null, values);

        project_list_array.add(et.getText().toString());

    }

    public ArrayList<String> getProjects(Context context){
        mySQLiteDBHelper myhelper = new mySQLiteDBHelper(context);
        SQLiteDatabase sqLiteDatabase = myhelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(mySQLiteDBHelper.CHARACTER_TABLE_PROJECT, null, null, null, null, null, null);
        cursor.moveToFirst();
        ArrayList<String> projects_list = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            projects_list.add(cursor.getString(cursor.getColumnIndex("project")));
            cursor.moveToNext();
        }
        cursor.close();
        return projects_list;
    }



    private void changeFragment(Bundle bundle){
        CharListFragment nextFragment = new CharListFragment();
        nextFragment.setArguments(bundle);
        //Make the transaction
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);
        transaction.replace(R.id.flMain,nextFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }





}
