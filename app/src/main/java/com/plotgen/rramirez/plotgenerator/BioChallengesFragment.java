package com.plotgen.rramirez.plotgenerator;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Common.Utils;
import com.plotgen.rramirez.plotgenerator.Common.mySQLiteDBHelper;
import com.plotgen.rramirez.plotgenerator.Fragment.Container_charbio;
import com.plotgen.rramirez.plotgenerator.Guides.GuideListFragment;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class BioChallengesFragment extends Fragment {

//    TextView char_role_challenge;
    ArrayList<String> char_description;
//    ImageButton guide_btn, character_bio_challenge_btn;
    private FirebaseAnalytics mFirebaseAnalytics;
    private String fragmentTag = BioChallengesFragment.class.getSimpleName();
    String char_name, charID, project_name, charRole;
    @BindView(R.id.fab_bio_challenge)
    com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton fabAddChallenge;
    @BindView(R.id.biochallenge_body)
    TextView biochallenge_body;

    public BioChallengesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View myFragmentView = inflater.inflate(R.layout.fragment_bio_challenges, container, false);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(myFragmentView.getContext());
        ButterKnife.bind(this, myFragmentView);

        char_description = new ArrayList<String>();
        if(Common.currentCharacter !=null) {
            try {
                char_name = Common.currentCharacter.getName();
                charID = Common.currentCharacter.getId();
                charRole = Common.currentCharacter.getRole();
            } catch (Exception e) {
                Log.v("matilda", e.toString());
            }
        }


        StringBuffer challenges = Utils.generateChallenges(getContext(),charID);
            if(Html.fromHtml(challenges.toString()).toString().equals("")){
                biochallenge_body.setText(getString(R.string.biocontainer_challenge_empty));
            } else {
                biochallenge_body.setText(Html.fromHtml(challenges.toString()));
            }


        fabAddChallenge.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fragmentTransaction();
            }
        });

        return myFragmentView;
    }


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) Objects.requireNonNull(getActivity())).setActionBarTitle("");
    }


    private void fragmentTransaction(){
        ChallengeListFragment nextFragment = new ChallengeListFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);
        transaction.replace(R.id.flMain, nextFragment);
        getActivity().getSupportFragmentManager().popBackStack();
        transaction.commit();
    }

}
