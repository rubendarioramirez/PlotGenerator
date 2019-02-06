package com.plotgen.rramirez.plotgenerator;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.plotgen.rramirez.plotgenerator.Common.Common;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.content.ContentValues.TAG;


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
        mlist.add(new item_herojourney(R.drawable.hero, (String) getString(R.string.hj_title1), (String) getString(R.string.hj_act1), (String) getString(R.string.hj_desc1),(String) "dumi", (String) "dumi"));
        mlist.add(new item_herojourney(R.drawable.hero2, (String) getString(R.string.hj_title2), (String) getString(R.string.hj_act1), (String) getString(R.string.hj_desc2),(String) "dumi", (String) "dumi"));
        mlist.add(new item_herojourney(R.drawable.hero, (String) getString(R.string.hj_title3), (String) getString(R.string.hj_act1), (String) getString(R.string.hj_desc3),(String) "dumi", (String) "dumi"));
        mlist.add(new item_herojourney(R.drawable.hero2, (String) getString(R.string.hj_title4), (String) getString(R.string.hj_act1), (String) getString(R.string.hj_desc4),(String) "dumi", (String) "dumi"));
        mlist.add(new item_herojourney(R.drawable.hero, (String) getString(R.string.hj_title5), (String) getString(R.string.hj_act2), (String) getString(R.string.hj_desc5),(String) "dumi", (String) "dumi"));
        mlist.add(new item_herojourney(R.drawable.hero2, (String) getString(R.string.hj_title6), (String) getString(R.string.hj_act2), (String) getString(R.string.hj_desc6),(String) "dumi", (String) "dumi"));
        mlist.add(new item_herojourney(R.drawable.hero, (String) getString(R.string.hj_title7), (String) (String) getString(R.string.hj_act2), (String) getString(R.string.hj_desc7),(String) "dumi", (String) "dumi"));
        mlist.add(new item_herojourney(R.drawable.hero2, (String) getString(R.string.hj_title8), (String) (String) getString(R.string.hj_act2), (String) getString(R.string.hj_desc8),(String) "dumi", (String) "dumi"));
        mlist.add(new item_herojourney(R.drawable.hero, (String) getString(R.string.hj_title9), (String) (String) getString(R.string.hj_act2), (String) getString(R.string.hj_desc9),(String) "dumi", (String) "dumi"));
        mlist.add(new item_herojourney(R.drawable.hero2, (String) getString(R.string.hj_title10), (String) (String) getString(R.string.hj_act2), (String) getString(R.string.hj_desc10),(String) "dumi", (String) "dumi"));
        mlist.add(new item_herojourney(R.drawable.hero, (String) getString(R.string.hj_title11), (String) (String) getString(R.string.hj_act2), (String) getString(R.string.hj_desc11),(String) "dumi", (String) "dumi"));
        mlist.add(new item_herojourney(R.drawable.hero2, (String) getString(R.string.hj_title12), (String) getString(R.string.hj_act3), (String) getString(R.string.hj_desc12),(String) "dumi", (String) "dumi"));
        mlist.add(new item_herojourney(R.drawable.hero, (String) getString(R.string.hj_title13), (String) getString(R.string.hj_act3), (String) getString(R.string.hj_desc13),(String) "dumi", (String) "dumi"));
        mlist.add(new item_herojourney(R.drawable.hero2, (String) getString(R.string.hj_title14), (String) getString(R.string.hj_act3), (String) getString(R.string.hj_desc14),(String) "dumi", (String) "dumi"));
        mlist.add(new item_herojourney(R.drawable.hero, (String) getString(R.string.hj_title15), (String) getString(R.string.hj_act3), (String) getString(R.string.hj_desc15),(String) "dumi", (String) "dumi"));
        mlist.add(new item_herojourney(R.drawable.hero2, (String) getString(R.string.hj_title16), (String) getString(R.string.hj_act3), (String) getString(R.string.hj_desc16),(String) "dumi", (String) "dumi"));

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        return  myFragmentView;

    }







}
