package com.plotgen.rramirez.plotgenerator;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Common.mySQLiteDBHelper;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChallengeTemplateFragment extends Fragment  {


    TextView charTemplateTitle, question1Title, question1, question2Title, question2, question3Title, question3, question4Title, question4;
    private FirebaseAnalytics mFirebaseAnalytics;
    private InterstitialAd mInterstitialAd_challenge;
    ArrayList<String> challenge_info;


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

        //Save button
        FloatingActionButton fab = myFragmentView.findViewById(R.id.challenge_template_submit);

        charTemplateTitle = myFragmentView.findViewById(R.id.char_template_title);
        question1Title = myFragmentView.findViewById(R.id.char_template_question1_title);
        question1 = myFragmentView.findViewById(R.id.char_template_q1_et);
        question2Title = myFragmentView.findViewById(R.id.char_template_question2_title);
        question2 = myFragmentView.findViewById(R.id.char_template_q2_et);
        question3Title = myFragmentView.findViewById(R.id.char_template_question3_title);
        question3 = myFragmentView.findViewById(R.id.char_template_q3_et);
        question4Title = myFragmentView.findViewById(R.id.char_template_question4_title);
        question4 = myFragmentView.findViewById(R.id.char_template_q4_et);

        ((MainActivity)getActivity()).setActionBarTitle(project_name);
        challenge_info = getDescription(myFragmentView.getContext(),char_name);
        charTemplateTitle.setText(char_name);
        String short_name = char_name.split(" ")[0];
        //Set titles for challenge one
        if(challenge_number.equals(getContext().getResources().getString(R.string.challenge_1_title))) {
            question1Title.setText(getString(R.string.challenge_1_q1));
            question2Title.setText(getString(R.string.challenge_1_q2));
            question3Title.setText(getString(R.string.challenge_1_q3));
            question4Title.setText(getString(R.string.challenge_1_q4));
            if(challenge_info.size()>0) {
                question1.setText(challenge_info.get(0));
                question2.setText(challenge_info.get(1));
                question3.setText(challenge_info.get(2));
                question4.setText(challenge_info.get(3));
            } else {
                default_questions_text();
            }
        } else if(challenge_number.equals(getContext().getResources().getString(R.string.challenge_2_title))){
            question1Title.setText(getString(R.string.challenge_2_q1));
            question2Title.setText(getString(R.string.challenge_2_q2));
            question3Title.setText(getString(R.string.challenge_2_q3));
            question4Title.setText(getString(R.string.challenge_2_q4));
            if(challenge_info.size()>0) {
                question1.setText(challenge_info.get(4));
                question2.setText(challenge_info.get(5));
                question3.setText(challenge_info.get(6));
                question4.setText(challenge_info.get(7));
            }else {
                default_questions_text();
            }
        }else if(challenge_number.equals(getContext().getResources().getString(R.string.challenge_3_title))){
            question1Title.setText(getString(R.string.challenge_3_q1));
            question2Title.setText(getString(R.string.challenge_3_q2));
            question3Title.setText(getString(R.string.challenge_3_q3));
            question4Title.setText(getString(R.string.challenge_3_q4));
            if(challenge_info.size()>0) {
                question1.setText(challenge_info.get(8));
                question2.setText(challenge_info.get(9));
                question3.setText(challenge_info.get(10));
                question4.setText(challenge_info.get(11));
            }else {
                default_questions_text();
            }
        }else if(challenge_number.equals(getContext().getResources().getString(R.string.challenge_4_title))){
            question1Title.setText(getString(R.string.challenge_4_q1));
            question2Title.setText(getString(R.string.challenge_4_q2));
            question3Title.setText(getString(R.string.challenge_4_q3));
            question4Title.setText(getString(R.string.challenge_4_q4));
            if(challenge_info.size()>0) {
            question1.setText(challenge_info.get(12));
            question2.setText(challenge_info.get(13));
            question3.setText(challenge_info.get(14));
            question4.setText(challenge_info.get(15));
            }else {
                default_questions_text();
            }
        }else if(challenge_number.equals(getContext().getResources().getString(R.string.challenge_5_title))){
            question1Title.setText(getString(R.string.challenge_5_q1));
            question2Title.setText(getString(R.string.challenge_5_q2));
            question3Title.setText(getString(R.string.challenge_5_q3));
            question4Title.setText(getString(R.string.challenge_5_q4));
            if(challenge_info.size()>0) {
                question1.setText(challenge_info.get(16));
                question2.setText(challenge_info.get(17));
                question3.setText(challenge_info.get(18));
                question4.setText(challenge_info.get(19));
            }else {
                default_questions_text();
            }
        } else if(challenge_number.equals(getContext().getResources().getString(R.string.challenge_6_title))){
            question1Title.setText(getString(R.string.challenge_6_q1));
            question2Title.setText(getString(R.string.challenge_6_q2));
            question3Title.setText(getString(R.string.challenge_6_q3));
            question4Title.setText(getString(R.string.challenge_6_q4));
            if(challenge_info.size()>0) {
                question1.setText(challenge_info.get(20));
                question2.setText(challenge_info.get(21));
                question3.setText(challenge_info.get(22));
                question4.setText(challenge_info.get(23));
            }else {
                default_questions_text();
            }
        } else if(challenge_number.equals(getContext().getResources().getString(R.string.challenge_7_title))){
            question1Title.setText(getString(R.string.challenge_7_q1).replace("$char_name$", short_name));
            question2Title.setText(getString(R.string.challenge_7_q2).replace("$char_name$", short_name));
            question3Title.setText(getString(R.string.challenge_7_q3).replace("$char_name$", short_name));
            question4Title.setText(getString(R.string.challenge_7_q4).replace("$char_name$", short_name));
            if(challenge_info.size()>0) {
                question1.setText(challenge_info.get(24));
                question2.setText(challenge_info.get(25));
                question3.setText(challenge_info.get(26));
                question4.setText(challenge_info.get(27));
            }else {
                default_questions_text();
            }
        }else if(challenge_number.equals(getContext().getResources().getString(R.string.challenge_8_title))){
            question1Title.setText(getString(R.string.challenge_8_q1).replace("$char_name$", short_name));
            question2Title.setText(getString(R.string.challenge_8_q2).replace("$char_name$", short_name));
            question3Title.setText(getString(R.string.challenge_8_q3).replace("$char_name$", short_name));
            question4Title.setText(getString(R.string.challenge_8_q4).replace("$char_name$", short_name));
            if(challenge_info.size()>0) {
                question1.setText(challenge_info.get(28));
                question2.setText(challenge_info.get(29));
                question3.setText(challenge_info.get(30));
                question4.setText(challenge_info.get(31));
            }else {
                default_questions_text();
            }
        }
        else if(challenge_number.equals(getContext().getResources().getString(R.string.c1_mentor_title))){
            question1Title.setText(getString(R.string.c1_mentor_q1));
            question2Title.setText(getString(R.string.c1_mentor_q2));
            question3Title.setText(getString(R.string.c1_mentor_q3));
            question4Title.setText(getString(R.string.c1_mentor_q4));
            question1.setText(challenge_info.get(32));
            question2.setText(challenge_info.get(33));
            question3.setText(challenge_info.get(34));
            question4.setText(challenge_info.get(35));
        } else if(challenge_number.equals(getContext().getResources().getString(R.string.c1_antagonist_title))){
            question1Title.setText(getString(R.string.c1_antagonist_q1));
            question2Title.setText(getString(R.string.c1_antagonist_q2));
            question3Title.setText(getString(R.string.c1_antagonist_q3));
            question4Title.setText(getString(R.string.c1_antagonist_q4));
            question1.setText(challenge_info.get(36));
            question2.setText(challenge_info.get(37));
            question3.setText(challenge_info.get(38));
            question4.setText(challenge_info.get(39));
        }else if(challenge_number.equals(getContext().getResources().getString(R.string.c1_sidekick_title))){
            question1Title.setText(getString(R.string.c1_sidekick_q1));
            question2Title.setText(getString(R.string.c1_sidekick_q2));
            question3Title.setText(getString(R.string.c1_sidekick_q3));
            question4Title.setText(getString(R.string.c1_sidekick_q4));
            question1.setText(challenge_info.get(40));
            question2.setText(challenge_info.get(41));
            question3.setText(challenge_info.get(42));
            question4.setText(challenge_info.get(43));
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform action on click
                updateDB(char_name, project_name, challenge_number);


            }
        });


        if(!Common.isPAU) {
            //Interstitial
            mInterstitialAd_challenge = new InterstitialAd(this.getContext());
            mInterstitialAd_challenge.setAdUnitId(getString(R.string.interstitial_plot_gen));
            mInterstitialAd_challenge.loadAd(new AdRequest.Builder()
                    .addTestDevice("E230AE087E1D0E7FB2304943F378CD64")
                    .build());
            mInterstitialAd_challenge.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    mInterstitialAd_challenge.show();
                }

            });
        }

        return myFragmentView;
    }

    private void updateDB(String char_name, String project_name, String challenge_number){
        SQLiteDatabase database = new mySQLiteDBHelper(this.getContext()).getWritableDatabase();
        ContentValues values = new ContentValues();
        //Log challenges updated
        Bundle params = new Bundle();
        if(challenge_number.equals(getContext().getResources().getString(R.string.challenge_1_title))) {
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_EIR, question1.getText().toString());
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_EWR, question2.getText().toString());
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_EHP, question3.getText().toString());
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_EEF, question4.getText().toString());
            params.putString("Challenge", "challenge_1");
        }
        else if(challenge_number.equals(getContext().getResources().getString(R.string.challenge_2_title))){
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C2Q1, question1.getText().toString());
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C2Q2, question2.getText().toString());
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C2Q3, question3.getText().toString());
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C2Q4, question4.getText().toString());
            params.putString("Challenge", "challenge_2");
        }else if(challenge_number.equals(getContext().getResources().getString(R.string.challenge_3_title))){
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C3Q1, question1.getText().toString());
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C3Q2, question2.getText().toString());
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C3Q3, question3.getText().toString());
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C3Q4, question4.getText().toString());
            params.putString("Challenge", "challenge_3");
        }else if(challenge_number.equals(getContext().getResources().getString(R.string.challenge_4_title))){
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C4Q1, question1.getText().toString());
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C4Q2, question2.getText().toString());
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C4Q3, question3.getText().toString());
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C4Q4, question4.getText().toString());
            params.putString("Challenge", "challenge_4");
        }else if(challenge_number.equals(getContext().getResources().getString(R.string.challenge_5_title))){
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C5Q1, question1.getText().toString());
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C5Q2, question2.getText().toString());
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C5Q3, question3.getText().toString());
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C5Q4, question4.getText().toString());
            params.putString("Challenge", "challenge_5");
        }else if(challenge_number.equals(getContext().getResources().getString(R.string.challenge_6_title))){
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C6Q1, question1.getText().toString());
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C6Q2, question2.getText().toString());
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C6Q3, question3.getText().toString());
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C6Q4, question4.getText().toString());
            params.putString("Challenge", "challenge_6");
        }else if(challenge_number.equals(getContext().getResources().getString(R.string.challenge_7_title))){
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C7Q1, question1.getText().toString());
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C7Q2, question2.getText().toString());
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C7Q3, question3.getText().toString());
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C7Q4, question4.getText().toString());
            params.putString("Challenge", "challenge_7");
        }else if(challenge_number.equals(getContext().getResources().getString(R.string.challenge_8_title))){
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C8Q1, question1.getText().toString());
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C8Q2, question2.getText().toString());
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C8Q3, question3.getText().toString());
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C8Q4, question4.getText().toString());
            params.putString("Challenge", "challenge_8");
        }
        else if(challenge_number.equals(getContext().getResources().getString(R.string.c1_mentor_title))){
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C1_MENTOR_Q1, question1.getText().toString());
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C1_MENTOR_Q2, question2.getText().toString());
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C1_MENTOR_Q3, question3.getText().toString());
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C1_MENTOR_Q4, question4.getText().toString());
            params.putString("Challenge", "challenge_mentor");
        } else if(challenge_number.equals(getContext().getResources().getString(R.string.c1_antagonist_title))){
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C1_ANTAGONIST_Q1, question1.getText().toString());
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C1_ANTAGONIST_Q2, question2.getText().toString());
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C1_ANTAGONIST_Q3, question3.getText().toString());
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C1_ANTAGONIST_Q4, question4.getText().toString());
            params.putString("Challenge", "challenge_antagonist");
        }else if(challenge_number.equals(getContext().getResources().getString(R.string.c1_sidekick_title))){
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C1_sidekick_Q1, question1.getText().toString());
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C1_sidekick_Q2, question2.getText().toString());
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C1_sidekick_Q3, question3.getText().toString());
            values.put(mySQLiteDBHelper.CHARACTER_COLUMN_C1_sidekick_Q4, question4.getText().toString());
            params.putString("Challenge", "challenge_sidekick");
        }
        mFirebaseAnalytics.logEvent("challenge_completed",params);
        database.update(mySQLiteDBHelper.CHARACTER_TABLE_CHARACTER, values,   "name = ?", new String[]{char_name});
        //Come back to previous fragment
        fragmentTransition(project_name,char_name);
    }


    public ArrayList<String> getDescription(Context context, String char_name){
        mySQLiteDBHelper myhelper = new mySQLiteDBHelper(context);
        SQLiteDatabase sqLiteDatabase = myhelper.getWritableDatabase();
        String query = "SELECT * FROM  " + mySQLiteDBHelper.CHARACTER_TABLE_CHARACTER  + " WHERE name = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(query,new String[]{char_name});
        cursor.moveToFirst();
        ArrayList<String> char_list = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            //First challenge
            char_list.add(cursor.getString(cursor.getColumnIndex("elevator_initial_reaction")));
            char_list.add(cursor.getString(cursor.getColumnIndex("elevator_wait_rescue")));
            char_list.add(cursor.getString(cursor.getColumnIndex("elevator_help_partner")));
            char_list.add(cursor.getString(cursor.getColumnIndex("elevator_escape_first")));
            //Second Challenge
            char_list.add(cursor.getString(cursor.getColumnIndex("challenge_2_q1")));
            char_list.add(cursor.getString(cursor.getColumnIndex("challenge_2_q2")));
            char_list.add(cursor.getString(cursor.getColumnIndex("challenge_2_q3")));
            char_list.add(cursor.getString(cursor.getColumnIndex("challenge_2_q4")));
            //Third Challenge
            char_list.add(cursor.getString(cursor.getColumnIndex("challenge_3_q1")));
            char_list.add(cursor.getString(cursor.getColumnIndex("challenge_3_q2")));
            char_list.add(cursor.getString(cursor.getColumnIndex("challenge_3_q3")));
            char_list.add(cursor.getString(cursor.getColumnIndex("challenge_3_q4")));
            //Fourth Challenge
            char_list.add(cursor.getString(cursor.getColumnIndex("challenge_4_q1")));
            char_list.add(cursor.getString(cursor.getColumnIndex("challenge_4_q2")));
            char_list.add(cursor.getString(cursor.getColumnIndex("challenge_4_q3")));
            char_list.add(cursor.getString(cursor.getColumnIndex("challenge_4_q4")));
            //Fifth Challenge
            char_list.add(cursor.getString(cursor.getColumnIndex("challenge_5_q1")));
            char_list.add(cursor.getString(cursor.getColumnIndex("challenge_5_q2")));
            char_list.add(cursor.getString(cursor.getColumnIndex("challenge_5_q3")));
            char_list.add(cursor.getString(cursor.getColumnIndex("challenge_5_q4")));
            //Sixth Challenge
            char_list.add(cursor.getString(cursor.getColumnIndex("challenge_6_q1")));
            char_list.add(cursor.getString(cursor.getColumnIndex("challenge_6_q2")));
            char_list.add(cursor.getString(cursor.getColumnIndex("challenge_6_q3")));
            char_list.add(cursor.getString(cursor.getColumnIndex("challenge_6_q4")));
            //Seventh Challenge
            char_list.add(cursor.getString(cursor.getColumnIndex("challenge_7_q1")));
            char_list.add(cursor.getString(cursor.getColumnIndex("challenge_7_q2")));
            char_list.add(cursor.getString(cursor.getColumnIndex("challenge_7_q3")));
            char_list.add(cursor.getString(cursor.getColumnIndex("challenge_7_q4")));
            //Eight Challenge
            char_list.add(cursor.getString(cursor.getColumnIndex("challenge_8_q1")));
            char_list.add(cursor.getString(cursor.getColumnIndex("challenge_8_q2")));
            char_list.add(cursor.getString(cursor.getColumnIndex("challenge_8_q3")));
            char_list.add(cursor.getString(cursor.getColumnIndex("challenge_8_q4")));
            //Mentor Challenge
            char_list.add(cursor.getString(cursor.getColumnIndex("c1_mentor_q1")));
            char_list.add(cursor.getString(cursor.getColumnIndex("c1_mentor_q2")));
            char_list.add(cursor.getString(cursor.getColumnIndex("c1_mentor_q3")));
            char_list.add(cursor.getString(cursor.getColumnIndex("c1_mentor_q4")));
            //Antagonist Challenge
            char_list.add(cursor.getString(cursor.getColumnIndex("c1_antagonist_q1")));
            char_list.add(cursor.getString(cursor.getColumnIndex("c1_antagonist_q2")));
            char_list.add(cursor.getString(cursor.getColumnIndex("c1_antagonist_q3")));
            char_list.add(cursor.getString(cursor.getColumnIndex("c1_antagonist_q4")));
            //Sidekick Challenge
            char_list.add(cursor.getString(cursor.getColumnIndex("c1_sidekick_q1")));
            char_list.add(cursor.getString(cursor.getColumnIndex("c1_sidekick_q2")));
            char_list.add(cursor.getString(cursor.getColumnIndex("c1_sidekick_q3")));
            char_list.add(cursor.getString(cursor.getColumnIndex("c1_sidekick_q4")));

            cursor.moveToNext();
        }
        cursor.close();
        return char_list;
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
        getFragmentManager().popBackStack();

    }

    private void default_questions_text(){
        question1.setText("Please refresh the challenge");
        question2.setText("Please refresh the challenge");
        question3.setText("Please refresh the challenge");
        question4.setText("Please refresh the challenge");
    }

}