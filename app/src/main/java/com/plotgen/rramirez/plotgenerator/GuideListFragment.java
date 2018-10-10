package com.plotgen.rramirez.plotgenerator;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

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

        mAdView = (AdView) myFragmentView.findViewById(R.id.adView_guide_list);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        RecyclerView recyclerView = myFragmentView.findViewById(R.id.rv_guide_list_fragment);
        final Adapter_herojourney adapter = new Adapter_herojourney(this.getActivity(),mlist);
        mlist.clear();
        mlist.add(new item_herojourney(R.drawable.typewriter, (String) getString(R.string.roles_title), (String) getString(R.string.roles_desc), (String) getString(R.string.roles_desc_long),(String) "dumi", (String) "dumi"));
        mlist.add(new item_herojourney(R.drawable.typewriter, (String) getString(R.string.lajos_character_title), (String) getString(R.string.lajos_character_desc), (String) getString(R.string.lajos_character_long),(String) "dumi", (String) "dumi"));
        mlist.add(new item_herojourney(R.drawable.typewriter, (String) getString(R.string.change_arc_title), (String) getString(R.string.change_arc_desc), (String) getString(R.string.change_arc_desc_long),(String) "dumi", (String) "dumi"));
        mlist.add(new item_herojourney(R.drawable.typewriter, (String) getString(R.string.antagonist_guide_title), (String) getString(R.string.antagonist_guide_desc), (String) getString(R.string.antagonist_guide_desc_long),(String) "dumi", (String) "dumi"));

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));


        return myFragmentView;
    }

}
