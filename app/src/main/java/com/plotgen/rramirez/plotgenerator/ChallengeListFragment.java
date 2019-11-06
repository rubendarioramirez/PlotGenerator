package com.plotgen.rramirez.plotgenerator;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.plotgen.rramirez.plotgenerator.Adapters.Adapter_challengeList;
import com.plotgen.rramirez.plotgenerator.Common.AdsHelper;
import com.plotgen.rramirez.plotgenerator.Common.CharacterUtils;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Adapters.Adapter_challenges;
import com.plotgen.rramirez.plotgenerator.Common.Utils;
import com.plotgen.rramirez.plotgenerator.items.item_challenge_list;
import com.plotgen.rramirez.plotgenerator.items.item_herojourney;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChallengeListFragment extends Fragment {

    List<item_challenge_list> mlist = new ArrayList<>();
    private AdView mAdView;
    public int challenge_unlocked = 0;
    private RewardedVideoAd mRewardedVideoAd;
    String char_name, project_name, role;
    public ChallengeListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.challenge_action_bar));
        View myFragmentView =  inflater.inflate(R.layout.fragment_challenge, container, false);

        role = "";
        if(Common.currentCharacter != null) {
            char_name = Common.currentCharacter.getName();
            project_name = Common.currentCharacter.getProject_name();
            role = Common.currentCharacter.getRole();
        }


        //Load ad
        mAdView = myFragmentView.findViewById(R.id.adView_challenge_list);
        AdsHelper.loadAd(mAdView);

        //Set list and fill it up
        RecyclerView recyclerView = myFragmentView.findViewById(R.id.rv_challenge_fragment);
        final Adapter_challengeList adapter = new Adapter_challengeList(this.getActivity(),mlist);
        mlist.clear();


        switch (role){
            case "Mentor":
                addNewChallenge("mentor_challenge","recommendedAct1");
                break;
            case "Sidekick":
            case "Escudero":
                addNewChallenge("sidekick_challenge","recommendedAct1");
                break;
            case "Antagonist":
            case "Antagonista":
                addNewChallenge("antagonist_challenge","recommendedAct1");
                break;
            case "Protagonist":
            case "Protagonista":
                addNewChallenge("protagonist_challenge","recommendedAct1");
                break;
        }
        //Generic list
        addNewChallenge("challenge_1","recommendedAct1");
        addNewChallenge("challenge_9","recommendedAct1");
        addNewChallenge("challenge_4","recommendedAct1");
        addNewChallenge("challenge_6","recommendedAct1");
        addNewChallenge("challenge_10","recommendedAct1");
        addNewChallenge("challenge_2","recommendedAct2");
        addNewChallenge("challenge_3","recommendedAct2");
        addNewChallenge("challenge_5","recommendedAct2");
        addNewChallenge("challenge_7","recommendedAct2");
        addNewChallenge("challenge_8","recommendedAct3");

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        int tutorialDisplayed = Utils.getSharePref(getContext(),"challengeTutorial",0);
        if(tutorialDisplayed != 1){
            displayDialog(myFragmentView.getContext(), getContext().getString(R.string.challengeFirstTimePopUpTitle), getContext().getString(R.string.challengeFirstTimePopUpBody), "Got it!");
        }
        return myFragmentView;
    }

    public static void displayDialog(Context context, String title, String body, String positiveBTN){
        new AlertDialog.Builder(context, R.style.AlertDialogCustom)
                .setTitle(title)
                .setMessage(body)
                .setPositiveButton(positiveBTN, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
        Utils.saveOnSharePreg(context, "challengeTutorial", 1);
    }


    public void addNewChallenge(String challengeNumber, String actNumber){
        int actResID = this.getResources().getIdentifier(actNumber, "string", Objects.requireNonNull(getActivity()).getPackageName());
        int resourceID = this.getResources().getIdentifier(challengeNumber, "array", Objects.requireNonNull(getActivity()).getPackageName());
        String[] myResArray = getResources().getStringArray(resourceID);
        List<String> myResArrayList = Arrays.asList(myResArray);
        mlist.add(new item_challenge_list(myResArrayList.get(0), myResArrayList.get(2), getString(actResID),  CharacterUtils.addCharNameOnChallenge(myResArrayList.get(3),getContext())));
    }
}
