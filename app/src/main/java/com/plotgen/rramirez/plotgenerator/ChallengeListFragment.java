package com.plotgen.rramirez.plotgenerator;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChallengeListFragment extends Fragment {

    List<item_herojourney> mlist = new ArrayList<>();

    public ChallengeListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.challenge_action_bar));
        final String char_name = this.getArguments().getString("char_name");
        final String project_name = this.getArguments().getString("project_name");
        // Inflate the layout for this fragment
        View myFragmentView =  inflater.inflate(R.layout.fragment_challenge, container, false);


        RecyclerView recyclerView = myFragmentView.findViewById(R.id.rv_challenge_fragment);
        final Adapter_herojourney adapter = new Adapter_herojourney(this.getActivity(),mlist);
        mlist.add(new item_herojourney(R.drawable.typewriter, (String) getString(R.string.challenge_1_title), (String) getString(R.string.challenge_1_desc), (String) getString(R.string.challenge_1_desc_long),(String) char_name, (String) project_name));
        mlist.add(new item_herojourney(R.drawable.typewriter, (String) getString(R.string.challenge_2_title), (String) getString(R.string.challenge_2_desc), (String) getString(R.string.challenge_2_desc_long),(String) char_name, (String) project_name));
        mlist.add(new item_herojourney(R.drawable.typewriter, (String) getString(R.string.challenge_3_title), (String) getString(R.string.challenge_3_desc), (String) getString(R.string.challenge_3_desc_long),(String) char_name, (String) project_name));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        return myFragmentView;
    }

}
