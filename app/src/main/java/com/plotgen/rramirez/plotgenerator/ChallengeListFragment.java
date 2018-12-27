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
import com.plotgen.rramirez.plotgenerator.Common.Common;

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

    public ChallengeListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.challenge_action_bar));
        final String char_name = this.getArguments().getString("char_name");
        final String project_name = this.getArguments().getString("project_name");
        final String role = this.getArguments().getString("role");
        // Inflate the layout for this fragment
        View myFragmentView =  inflater.inflate(R.layout.fragment_challenge, container, false);

        if(!Common.isPAU) {
            mAdView = (AdView) myFragmentView.findViewById(R.id.adView_challenge_list);
            Utils.loadAd(mAdView);
        }

        RecyclerView recyclerView = myFragmentView.findViewById(R.id.rv_challenge_fragment);
        final Adapter_herojourney adapter = new Adapter_herojourney(this.getActivity(),mlist);
        mlist.clear();
        switch (role){
            case "Mentor":
                mlist.add(new item_herojourney(R.drawable.typewriter, getString(R.string.c1_mentor_title), getString(R.string.c1_mentor_desc), getString(R.string.c1_mentor_desc_long),(String) char_name, (String) project_name));
                break;
            case "Sidekick":
            case "Escudero":
                mlist.add(new item_herojourney(R.drawable.typewriter, getString(R.string.c1_sidekick_title), getString(R.string.c1_sidekick_desc), getString(R.string.c1_sidekick_desc_long), char_name, project_name));
                break;
            case "Antagonist":
            case "Antagonista":
                mlist.add(new item_herojourney(R.drawable.typewriter, getString(R.string.c1_antagonist_title), getString(R.string.c1_antagonist_desc), getString(R.string.c1_antagonist_desc_long), char_name, project_name));
                break;
        }
        //Generic list
        mlist.add(new item_herojourney(R.drawable.typewriter, getString(R.string.challenge_1_title), (String) getString(R.string.challenge_1_desc), (String) getString(R.string.challenge_1_desc_long),(String) char_name, (String) project_name));
        mlist.add(new item_herojourney(R.drawable.typewriter, getString(R.string.challenge_2_title), (String) getString(R.string.challenge_2_desc), (String) getString(R.string.challenge_2_desc_long),(String) char_name, (String) project_name));
        mlist.add(new item_herojourney(R.drawable.typewriter, getString(R.string.challenge_3_title), (String) getString(R.string.challenge_3_desc), (String) getString(R.string.challenge_3_desc_long),(String) char_name, (String) project_name));
        mlist.add(new item_herojourney(R.drawable.typewriter, getString(R.string.challenge_4_title), (String) getString(R.string.challenge_4_desc), (String) getString(R.string.challenge_4_desc_long),(String) char_name, (String) project_name));
        mlist.add(new item_herojourney(R.drawable.typewriter, getString(R.string.challenge_5_title), (String) getString(R.string.challenge_5_desc), (String) getString(R.string.challenge_5_desc_long),(String) char_name, (String) project_name));
        mlist.add(new item_herojourney(R.drawable.typewriter, getString(R.string.challenge_6_title), (String) getString(R.string.challenge_6_desc), (String) getString(R.string.challenge_6_desc_long),(String) char_name, (String) project_name));
        mlist.add(new item_herojourney(R.drawable.typewriter, getString(R.string.challenge_7_title), (String) getString(R.string.challenge_7_desc), (String) getString(R.string.challenge_7_desc_long),(String) char_name, (String) project_name));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        return myFragmentView;
    }

}
