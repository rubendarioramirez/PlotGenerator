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
import com.google.firebase.analytics.FirebaseAnalytics;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Fragment.OfflineStoryFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class CharListFragment extends Fragment {

    TextView project_list_tv, empty_character_tv;
    ImageButton charlist_project_edit_btn;
    ListView character_list_lv;
    ArrayList<String> char_list_array;
    ArrayAdapter<String> itemsAdapter;
    String project_info;
    private FirebaseAnalytics mFirebaseAnalytics;


    @BindView(R.id.fab_add_char)
    com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton fabAddChar;
    @BindView(R.id.fab_add_story)
    com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton fabAddStory;
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
        project_info = this.getArguments().getString("project_info");
        final String project_name_text = project_info.substring(2);
        final String project_id = String.valueOf(project_info.charAt(0));

        final View myFragmentView =   inflater.inflate(R.layout.fragment_char_list, container, false);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(myFragmentView.getContext());

        ButterKnife.bind(this, myFragmentView);

        //Declare elements
        //FloatingActionButton fab_add = (FloatingActionButton) myFragmentView.findViewById(R.id.add_character_btn);
        project_list_tv = myFragmentView.findViewById(R.id.project_list_tv);
        character_list_lv = myFragmentView.findViewById(R.id.character_list_lv);
        empty_character_tv= myFragmentView.findViewById(R.id.empty_character_tv);
        charlist_project_edit_btn = myFragmentView.findViewById(R.id.charlist_project_edit_btn);

//      project_list_array = Utils.getCharList(myFragmentView.getContext(), project_id); TO SEARCH BY PROJECT ID
        char_list_array = Utils.getCharList(myFragmentView.getContext(), project_name_text);

        project_list_tv.setText(project_name_text);

        if(!Common.isPAU) {
            //Display the ad
            mAdView = (AdView) myFragmentView.findViewById(R.id.adView_char_list);
            Utils.loadAd(mAdView);
        }

        //Display the adapter
        itemsAdapter = new ArrayAdapter<String>(myFragmentView.getContext(), android.R.layout.simple_list_item_1, char_list_array);

        character_list_lv.setAdapter(itemsAdapter);
        character_list_lv.setEmptyView(empty_character_tv);
        //When the item is click
        character_list_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                //Get the name that was touched
                //String[] charNameRaw = itemsAdapter.getItem(position).split(" - ");
                String charName = itemsAdapter.getItem(position);
                String[] arrayList = getResources().getStringArray(R.array.char_guide_types_titles_for_comparison);
                for(String s : arrayList)
                {
                    if(charName.contains(s))
                    {
                        charName = charName.replace( " - " + s,"");
                    }
                }

                Bundle bundle = new Bundle();
                bundle.putString("char_name",charName);
                bundle.putString("project_name",project_name_text);
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


        fabAddChar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharacterFragment nextFragment = new CharacterFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Utils.changeFragment(nextFragment,transaction,"project_info",project_info);
            }
        });

        if(Utils.isHaveStoryFromDB(myFragmentView.getContext(),project_name_text))
            fabAddStory.setFabText(getString(R.string.edit_story));

        fabAddStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OfflineStoryFragment nextFragment = new OfflineStoryFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Utils.changeFragment(nextFragment,transaction,"project_info",project_info);
                Bundle params = new Bundle();
                params.putString("created_story", "started");
                mFirebaseAnalytics.logEvent("created_story", params);
            }
        });


        //Click on Delete Button
        charlist_project_edit_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Send it to the next fragment
                Project_detailsFragment nextFragment = new Project_detailsFragment();
                //Make the transaction
                Bundle bundle = new Bundle();
                bundle.putString("project_name",project_name_text);
                //Send it to the next fragment
                nextFragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.flMain,nextFragment);
                transaction.commit();
            }
        });


        return myFragmentView;
    }



}
