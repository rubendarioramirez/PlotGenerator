package com.plotgen.rramirez.plotgenerator.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plotgen.rramirez.plotgenerator.R;
import com.plotgen.rramirez.plotgenerator.weeklyWriting;
//import com.plotgen.rramirez.plotgenerator.weeklyWriting;


public class weekly_challenge_container extends Fragment {

    private SectionsPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;


    public weekly_challenge_container() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_weekly_challenge_container, container, false);
        return view;


    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSectionsPageAdapter = new SectionsPageAdapter(getChildFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = this.getActivity().findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = this.getActivity().findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }


    @Override
    public void onResume() {
        super.onResume();
        mSectionsPageAdapter = new SectionsPageAdapter(getChildFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = this.getActivity().findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = this.getActivity().findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }



    private void setupViewPager(ViewPager viewPager) {
        //To fix already executing transactions.
        SectionsPageAdapter adapter = new SectionsPageAdapter(getChildFragmentManager());
        adapter.addFragment(new weeklyWriting(), getString(R.string.weekly_challenge_tab1));
        adapter.addFragment(new weekly_winners(), getString(R.string.weekly_challenge_tab2));
        viewPager.setAdapter(adapter);

    }

}
