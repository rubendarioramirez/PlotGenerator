package com.plotgen.rramirez.plotgenerator;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChallengeTemplateFragment extends Fragment {


    TextView charTemplateTitle, question1Title, question1, question2Title, question2, question3Title, question3, question4Title, question4;
    Button charTemplateSubmit;
    private AdView mAdView;
    private FirebaseAnalytics mFirebaseAnalytics;

    public ChallengeTemplateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View myFragmentView = inflater.inflate(R.layout.fragment_challenge_template, container, false);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(myFragmentView.getContext());


        final String project_name  = this.getArguments().getString("char_name");
        final String char_name = this.getArguments().getString("project_name");
        final String challenge_number = this.getArguments().getString("challenge_number");

        charTemplateTitle = myFragmentView.findViewById(R.id.char_template_title);
        question1Title = myFragmentView.findViewById(R.id.char_template_question1_title);
        question1 = myFragmentView.findViewById(R.id.char_template_q1_et);
        question2Title = myFragmentView.findViewById(R.id.char_template_question2_title);
        question2 = myFragmentView.findViewById(R.id.char_template_q2_et);
        question3Title = myFragmentView.findViewById(R.id.char_template_question3_title);
        question3 = myFragmentView.findViewById(R.id.char_template_q3_et);
        question4Title = myFragmentView.findViewById(R.id.char_template_question4_title);
        question4 = myFragmentView.findViewById(R.id.char_template_q4_et);
        charTemplateSubmit = myFragmentView.findViewById(R.id.char_template_submit);
        ((MainActivity)getActivity()).setActionBarTitle(project_name);

        charTemplateTitle.setText(char_name);
        //Set titles for challenge one
        if(challenge_number.equals("Challenge I") || challenge_number.equals("Desafio I")) {
            question1Title.setText(getString(R.string.challenge_1_q1));
            question2Title.setText(getString(R.string.challenge_1_q2));
            question3Title.setText(getString(R.string.challenge_1_q3));
            question4Title.setText(getString(R.string.challenge_1_q4));
        } else if(challenge_number.equals("Challenge II") || challenge_number.equals("Desafio II")){
            question1Title.setText(getString(R.string.challenge_2_q1));
            question2Title.setText(getString(R.string.challenge_2_q2));
            question3Title.setText(getString(R.string.challenge_2_q3));
            question4Title.setText(getString(R.string.challenge_2_q4));
        }
            charTemplateSubmit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click
                    updateDB(char_name.toString(), project_name.toString(), challenge_number.toString());

                    //Log challenges updated
                    Bundle params = new Bundle();
                    params.putString("Challenge", "completed");
                    mFirebaseAnalytics.logEvent("challenge_completed",params);
                }
            });

        //Init the ad
        mAdView = (AdView) myFragmentView.findViewById(R.id.adView_challenge_template);
        //Display the ad
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        return myFragmentView;
    }

    private void updateDB(String char_name, String project_name, String challenge_number){
        SQLiteDatabase database = new mySQLiteDBHelper(this.getContext()).getWritableDatabase();
        ContentValues values = new ContentValues();
        if(challenge_number.equals("Challenge I") || challenge_number.equals("Desafio I")) {
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_EIR, question1.getText().toString());
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_EWR, question2.getText().toString());
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_EHP, question3.getText().toString());
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_EEF, question4.getText().toString());
        }
        else if(challenge_number.equals("Challenge II") || challenge_number.equals("Desafio II")){
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C2Q1, question1.getText().toString());
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C2Q2, question2.getText().toString());
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C2Q3, question3.getText().toString());
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C2Q4, question4.getText().toString());
        }
        database.update(mySQLiteDBHelper.CHARACTER_TABLE_CHARACTER, values,   "name = ?", new String[]{char_name});

        //Come back to previous fragment
        fragmentTransition(project_name,char_name);
    }

    public void fragmentTransition(String project_name, String char_name){
        Bundle bundle = new Bundle();
        bundle.putString("project_name",project_name);
        bundle.putString("char_name",char_name);
        //Send it to the next fragment
        BioFragment nextFragment = new BioFragment();
        nextFragment.setArguments(bundle);
        //Make the transaction
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.flMain,nextFragment);
        transaction.commit();

    }
}
