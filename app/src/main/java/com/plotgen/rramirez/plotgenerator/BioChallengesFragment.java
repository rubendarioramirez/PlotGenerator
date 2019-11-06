package com.plotgen.rramirez.plotgenerator;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.plotgen.rramirez.plotgenerator.Common.CharacterUtils;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Common.SQLUtils;
import com.plotgen.rramirez.plotgenerator.Common.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.opencensus.metrics.LongGauge;


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


        //Get answers and titles
        ArrayList challengeTitles = Utils.getAllChallengesTitlesbyID(getContext(),charID);
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i<challengeTitles.size();i++){
            int resourceID = this.getResources().getIdentifier(challengeTitles.get(i).toString(), "array",getActivity().getPackageName());
            String[] myResArray = getResources().getStringArray(resourceID);
            List<String> questionTitles = Arrays.asList(myResArray);
            //Get answers
            ArrayList challengeAnswers = Utils.getChallengeByID(getContext(),challengeTitles.get(i).toString(),charID);
            //Get the title of the challenge.
            sb.append("<br><b><font color='red'>" + questionTitles.get(2) + "</font></b>");
            for (int x = 4; x < questionTitles.size(); x++) {
            sb.append("<br><b>" + CharacterUtils.addCharNameOnChallenge(questionTitles.get(x), getContext()) + "</b>");
            sb.append("<br><br>" + challengeAnswers.get(x - 4) + "<br>");
            }

        }

            if(sb.toString().equals("")){
                biochallenge_body.setText(getString(R.string.biocontainer_challenge_empty));
            } else {
                biochallenge_body.setText(Html.fromHtml(sb.toString()));
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
