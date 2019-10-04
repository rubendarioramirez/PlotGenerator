package com.plotgen.rramirez.plotgenerator.Guides;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.plotgen.rramirez.plotgenerator.Adapters.Adapter_challenges;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.MainActivity;
import com.plotgen.rramirez.plotgenerator.R;
import com.plotgen.rramirez.plotgenerator.items.item_herojourney;

import java.util.ArrayList;
import java.util.List;


public class GuideListFragment extends Fragment {

    List<item_herojourney> mlist = new ArrayList<>();
    private AdView mAdView;

    public GuideListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.guide_character_btn));
        View myFragmentView =  inflater.inflate(R.layout.fragment_guide_list, container, false);

        if(!Common.isPAU) {
            mAdView = (AdView) myFragmentView.findViewById(R.id.adView_guide_list);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }

        RecyclerView recyclerView = myFragmentView.findViewById(R.id.rv_guide_list_fragment);
        final Adapter_challenges adapter = new Adapter_challenges(this.getActivity(),mlist);
        mlist.clear();
        mlist.add(new item_herojourney(R.color.color_trigger_1, (String) getString(R.string.roles_title), (String) getString(R.string.roles_desc), (String) getString(R.string.roles_desc_long)));
        mlist.add(new item_herojourney(R.color.color_trigger_2, (String) getString(R.string.lajos_character_title), (String) getString(R.string.lajos_character_desc), (String) getString(R.string.lajos_character_long)));
        mlist.add(new item_herojourney(R.color.color_trigger_3, (String) getString(R.string.change_arc_title), (String) getString(R.string.change_arc_desc), (String) getString(R.string.change_arc_desc_long)));
        mlist.add(new item_herojourney(R.color.color_trigger_4, (String) getString(R.string.antagonist_guide_title), (String) getString(R.string.antagonist_guide_desc), (String) getString(R.string.antagonist_guide_desc_long)));
        mlist.add(new item_herojourney(R.color.color_trigger_5, getString(R.string.herojourney_guide_title), getString(R.string.herojourney_guide_desc), getString(R.string.herojourney_guide_desc_long)));

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));


        return myFragmentView;
    }

}
