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
import com.plotgen.rramirez.plotgenerator.Adapters.Adapter_herojourney;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.items.item_herojourney;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HeroJourneyFragment extends Fragment {

    List<item_herojourney> mlist = new ArrayList<>();
    private AdView mAdView;

    public HeroJourneyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.hero_journey_tab));

        View myFragmentView =  inflater.inflate(R.layout.fragment_herojourney, container, false);

        if(!Common.isPAU) {
            mAdView = (AdView) myFragmentView.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }

        //set up the recycler view with the adapter
        RecyclerView recyclerView = myFragmentView.findViewById(R.id.rv_herojourney);
        final Adapter_herojourney adapter = new Adapter_herojourney(this.getActivity(),mlist);
        mlist.add(new item_herojourney(R.drawable.hero, (String) getString(R.string.hj_title1), (String) getString(R.string.hj_act1), (String) getString(R.string.hj_desc1)));
        mlist.add(new item_herojourney(R.drawable.hero2, (String) getString(R.string.hj_title2), (String) getString(R.string.hj_act1), (String) getString(R.string.hj_desc2)));
        mlist.add(new item_herojourney(R.drawable.hero, (String) getString(R.string.hj_title3), (String) getString(R.string.hj_act1), (String) getString(R.string.hj_desc3)));
        mlist.add(new item_herojourney(R.drawable.hero2, (String) getString(R.string.hj_title4), (String) getString(R.string.hj_act1), (String) getString(R.string.hj_desc4)));
        mlist.add(new item_herojourney(R.drawable.hero, (String) getString(R.string.hj_title5), (String) getString(R.string.hj_act2), (String) getString(R.string.hj_desc5)));
        mlist.add(new item_herojourney(R.drawable.hero2, (String) getString(R.string.hj_title6), (String) getString(R.string.hj_act2), (String) getString(R.string.hj_desc6)));
        mlist.add(new item_herojourney(R.drawable.hero, (String) getString(R.string.hj_title7), (String) (String) getString(R.string.hj_act2), (String) getString(R.string.hj_desc7)));
        mlist.add(new item_herojourney(R.drawable.hero2, (String) getString(R.string.hj_title8), (String) (String) getString(R.string.hj_act2), (String) getString(R.string.hj_desc8)));
        mlist.add(new item_herojourney(R.drawable.hero, (String) getString(R.string.hj_title9), (String) (String) getString(R.string.hj_act2), (String) getString(R.string.hj_desc9)));
        mlist.add(new item_herojourney(R.drawable.hero2, (String) getString(R.string.hj_title10), (String) (String) getString(R.string.hj_act2), (String) getString(R.string.hj_desc10)));
        mlist.add(new item_herojourney(R.drawable.hero, (String) getString(R.string.hj_title11), (String) (String) getString(R.string.hj_act2), (String) getString(R.string.hj_desc11)));
        mlist.add(new item_herojourney(R.drawable.hero2, (String) getString(R.string.hj_title12), (String) getString(R.string.hj_act3), (String) getString(R.string.hj_desc12)));
        mlist.add(new item_herojourney(R.drawable.hero, (String) getString(R.string.hj_title13), (String) getString(R.string.hj_act3), (String) getString(R.string.hj_desc13)));
        mlist.add(new item_herojourney(R.drawable.hero2, (String) getString(R.string.hj_title14), (String) getString(R.string.hj_act3), (String) getString(R.string.hj_desc14)));
        mlist.add(new item_herojourney(R.drawable.hero, (String) getString(R.string.hj_title15), (String) getString(R.string.hj_act3), (String) getString(R.string.hj_desc15)));
        mlist.add(new item_herojourney(R.drawable.hero2, (String) getString(R.string.hj_title16), (String) getString(R.string.hj_act3), (String) getString(R.string.hj_desc16)));

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        return  myFragmentView;

    }







}
