package com.plotgen.rramirez.plotgenerator;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.plotgen.rramirez.plotgenerator.Common.AdsHelper;
import com.plotgen.rramirez.plotgenerator.Common.CharacterUtils;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Common.SQLUtils;
import com.plotgen.rramirez.plotgenerator.Common.Utils;
import com.plotgen.rramirez.plotgenerator.Common.dBHelper;
import com.plotgen.rramirez.plotgenerator.Fragment.Container_charbio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChallengeTemplateFragment extends Fragment  {

    private FirebaseAnalytics mFirebaseAnalytics;
    private String charID, project_name, challenge_number;
    ArrayList<String> answersList;

    @BindView(R.id.challenge_template_submit)
    FloatingActionButton fab;
    @BindView(R.id.char_template_title)
    TextView charTemplateTitle;
    @BindView(R.id.char_template_question1_title)
    TextView question1Title;
    @BindView(R.id.char_template_q1_et)
    TextView question1;
    @BindView(R.id.char_template_question2_title)
    TextView question2Title;
    @BindView(R.id.char_template_q2_et)
    TextView question2;
    @BindView(R.id.char_template_question3_title)
    TextView question3Title;
    @BindView(R.id.char_template_q3_et)
    TextView question3;
    @BindView(R.id.char_template_question4_title)
    TextView question4Title;
    @BindView(R.id.char_template_q4_et)
    TextView question4;

    public ChallengeTemplateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).setActionBarTitle(project_name);
        View myFragmentView = inflater.inflate(R.layout.fragment_challenge_template, container, false);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(myFragmentView.getContext());
        ButterKnife.bind(this, myFragmentView);

        project_name = Common.characterProject();
        charID = Common.characterID();
        challenge_number = Common.currentChallenge.getId();

        charTemplateTitle.setText(Common.characterName());
        //New description answersList
        answersList = Utils.getChallengeByID(getContext(),Common.currentChallenge.getId(),Common.currentCharacter.getId());
        //SetUI
        setQuestionTitles();

        if(!setCreationMode()){
            question1.setText(answersList.get(0));
            question2.setText(answersList.get(1));
            question3.setText(answersList.get(2));
            question4.setText(answersList.get(3));
        }
        //endregion


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(setCreationMode())
                    insertDB();
                else {
                    updateDB(charID, challenge_number);
                }
            }
        });

        //Show ads
        AdsHelper.displayInterstitial(getContext());

        return myFragmentView;
    }


    private void insertDB(){
        SQLiteDatabase database = new dBHelper(this.getContext()).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(dBHelper.CHALLENGE_ID, challenge_number);
        values.put(dBHelper.CHALLENGE_CHAR_ID, charID);
        values.put(dBHelper.CHALLENGE_Q1, question1.getText().toString());
        values.put(dBHelper.CHALLENGE_Q2, question2.getText().toString());
        values.put(dBHelper.CHALLENGE_Q3, question3.getText().toString());
        values.put(dBHelper.CHALLENGE_Q4, question4.getText().toString());
        // Insert the new row, returning the primary key value of the new row
        database.insert(dBHelper.TABLE_CHALLENGE, null, values);
        database.close();
        //Update Challenges
        //Trackingevents
        Bundle params = new Bundle();
        params.putString("Challenge", Common.currentChallenge.getId());
        mFirebaseAnalytics.logEvent("challenge_completed",params);
        //Update challenges
        updateChallengeCompletion();
        //Come back to previous fragment
        fragmentTransition();
    }

    private void updateDB(String characterID, String challengeID){
        SQLiteDatabase database = new dBHelper(this.getContext()).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(dBHelper.CHALLENGE_Q1, question1.getText().toString());
        values.put(dBHelper.CHALLENGE_Q2, question2.getText().toString());
        values.put(dBHelper.CHALLENGE_Q3, question3.getText().toString());
        values.put(dBHelper.CHALLENGE_Q4, question4.getText().toString());
        database.update(dBHelper.TABLE_CHALLENGE, values,   "challengeID = ? AND characterID = ?", new String[]{challengeID, characterID});
        database.close();
        //Come back to previous fragment
        fragmentTransition();
    }


    public void fragmentTransition(){
        Container_charbio nextFragment = new Container_charbio();
        //Make the transaction
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.flMain,nextFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    private Boolean setCreationMode(){
        if(answersList.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    private void setQuestionTitles(){
        String value = Common.currentChallenge.getId();
        int resourceID = this.getResources().getIdentifier(value, "array",getActivity().getPackageName());
        String[] myResArray = getResources().getStringArray(resourceID);
        List<String> myResArrayList = Arrays.asList(myResArray);
        question1Title.setText(CharacterUtils.addCharNameOnChallenge(myResArrayList.get(3),getContext()));
        question2Title.setText(CharacterUtils.addCharNameOnChallenge(myResArrayList.get(4),getContext()));
        question3Title.setText(CharacterUtils.addCharNameOnChallenge(myResArrayList.get(5),getContext()));
        question4Title.setText(CharacterUtils.addCharNameOnChallenge(myResArrayList.get(6),getContext()));
    }

    private void updateChallengeCompletion(){
        //Only if project creation is true. Do not add if its in update.
        SQLiteDatabase database = new dBHelper(this.getContext()).getWritableDatabase();
        ContentValues CharacterValues = new ContentValues();
        int current = SQLUtils.getCompletionByCHARID(getContext(),charID);
        Log.v("matilda", "The value of currentChallenge is " + SQLUtils.getCompletionByCHARID(getContext(),charID));
        int result = current + 1;
        Log.v("matilda", "The value is " + result);
        CharacterValues.put(dBHelper.CHAR_challengesCompleted, String.valueOf(result));
        database.update(dBHelper.TABLE_CHARACTER, CharacterValues,   "_id = ?", new String[]{charID});
        database.close();
        Common.currentCharacter.setChallengesDone(Common.currentCharacter.getChallengesDone()+1);
        Log.v("matilda", "Challenges done " + SQLUtils.getCompletionByCHARID(getContext(),charID));

    }

}