package com.plotgen.rramirez.plotgenerator;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Common.Utils;
import com.plotgen.rramirez.plotgenerator.Fragment.SubmitTriggerFragment;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class TriggerFragment extends Fragment {


    ArrayList trigger_list;
    ArrayList trigger_backgrounds;
    List<item> mlist = new ArrayList<>();
    private String fragmentTag = TriggerFragment.class.getSimpleName();

    //    private String databaseToUse;
    private AdView mAdView;

    @BindView(R.id.fab_addTrigger)
    FloatingActionButton fab_addTrigger;

    public TriggerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.trigger_tab));
        View myFragmentView = inflater.inflate(R.layout.fragment_trigger, container, false);
        ButterKnife.bind(this, myFragmentView);

        if (!Common.isPAU) {
            //Add the ads
            mAdView = (AdView) myFragmentView.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }

        //set up the recycler view with the adapter
        RecyclerView recyclerView = myFragmentView.findViewById(R.id.rv_list);
        final Adapter adapter = new Adapter(this.getActivity(), mlist);

        //The arrays
        trigger_list = new ArrayList();
        trigger_backgrounds = new ArrayList();

        //Fill up the background trigger list
        trigger_backgrounds.addAll(Arrays.asList(R.color.color_trigger_1, R.color.color_trigger_2, R.color.color_trigger_3, R.color.color_trigger_4, R.color.color_trigger_5));
        String[] stringArray = getResources().getStringArray(R.array.triggers_list);
        int x = 0;

        for (int i = 0; i < stringArray.length; i++) {
            mlist.add(new item((Integer) trigger_backgrounds.get(x), (String) stringArray[i]));
            adapter.notifyDataSetChanged();
            x++;
            if (x >= trigger_backgrounds.size()) {
                x = 0;
            }
        }

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));


        fab_addTrigger.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    SubmitTriggerFragment nextFragment = new SubmitTriggerFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    boolean fragmentPopped = getFragmentManager().popBackStackImmediate(fragmentTag, 0);
                    if (!fragmentPopped && getFragmentManager().findFragmentByTag(fragmentTag) == null) {
                        transaction.addToBackStack(null);
                    }
                    Utils.changeFragment(nextFragment, transaction);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



        return myFragmentView;
    }


    public void onStart() {
        super.onStart();


    }
}
