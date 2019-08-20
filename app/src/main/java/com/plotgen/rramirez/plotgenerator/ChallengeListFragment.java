package com.plotgen.rramirez.plotgenerator;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.plotgen.rramirez.plotgenerator.Common.AdsHelper;
import com.plotgen.rramirez.plotgenerator.Common.CharacterUtils;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Adapters.Adapter_challenges;
import com.plotgen.rramirez.plotgenerator.items.item_herojourney;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChallengeListFragment extends Fragment {

    List<item_herojourney> mlist = new ArrayList<>();
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

        if(Common.currentCharacter != null) {
            char_name = Common.currentCharacter.getName();
            project_name = Common.currentCharacter.getProject_name();
        }
        View myFragmentView =  inflater.inflate(R.layout.fragment_challenge, container, false);

        if(!Common.isPAU) {
            mAdView = (AdView) myFragmentView.findViewById(R.id.adView_challenge_list);
            AdsHelper.loadAd(mAdView);
        }

        RecyclerView recyclerView = myFragmentView.findViewById(R.id.rv_challenge_fragment);
        final Adapter_challenges adapter = new Adapter_challenges(this.getActivity(),mlist);
        mlist.clear();


        if(Common.currentCharacter != null){
            role = Common.currentCharacter.getRole();
        } else {
            role = "";
        }


        switch (role){
            case "Mentor":
                mlist.add(new item_herojourney(R.drawable.typewriter, getString(R.string.c1_mentor_title), getString(R.string.c1_mentor_desc), getString(R.string.c1_mentor_desc_long)));
                break;
            case "Sidekick":
            case "Escudero":
                mlist.add(new item_herojourney(R.drawable.typewriter, getString(R.string.c1_sidekick_title), getString(R.string.c1_sidekick_desc), getString(R.string.c1_sidekick_desc_long)));
                break;
            case "Antagonist":
            case "Antagonista":
                mlist.add(new item_herojourney(R.drawable.typewriter, getString(R.string.c1_antagonist_title), getString(R.string.c1_antagonist_desc), getString(R.string.c1_antagonist_desc_long)));
                break;
        }
        //Generic list
        mlist.add(new item_herojourney(R.color.color_trigger_1, getString(R.string.challenge_1_title), getString(R.string.challenge_1_desc), CharacterUtils.addCharNameOnChallenge(getString(R.string.challenge_1_desc_long),getContext())));
        mlist.add(new item_herojourney(R.color.color_trigger_2, getString(R.string.challenge_2_title),  getString(R.string.challenge_2_desc),  CharacterUtils.addCharNameOnChallenge(getString(R.string.challenge_2_desc_long),getContext())));
        mlist.add(new item_herojourney(R.color.color_trigger_3, getString(R.string.challenge_3_title),  getString(R.string.challenge_3_desc),  CharacterUtils.addCharNameOnChallenge(getString(R.string.challenge_3_desc_long),getContext())));
        mlist.add(new item_herojourney(R.color.color_trigger_4, getString(R.string.challenge_4_title),  getString(R.string.challenge_4_desc), CharacterUtils.addCharNameOnChallenge(getString(R.string.challenge_4_desc_long),getContext())));
        mlist.add(new item_herojourney(R.color.color_trigger_5, getString(R.string.challenge_5_title),  getString(R.string.challenge_5_desc),  CharacterUtils.addCharNameOnChallenge(getString(R.string.challenge_5_desc_long),getContext())));
        mlist.add(new item_herojourney(R.color.color_trigger_1, getString(R.string.challenge_6_title), getString(R.string.challenge_6_desc),  CharacterUtils.addCharNameOnChallenge(getString(R.string.challenge_6_desc_long),getContext())));
        mlist.add(new item_herojourney(R.color.color_trigger_2, getString(R.string.challenge_7_title), getString(R.string.challenge_7_desc),  CharacterUtils.addCharNameOnChallenge(getString(R.string.challenge_7_desc_long),getContext())));
        mlist.add(new item_herojourney(R.color.color_trigger_3, getString(R.string.challenge_8_title), getString(R.string.challenge_8_desc),  CharacterUtils.addCharNameOnChallenge(getString(R.string.challenge_8_desc_long),getContext())));

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        return myFragmentView;
    }

}
