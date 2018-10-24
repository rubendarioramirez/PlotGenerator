package com.plotgen.rramirez.plotgenerator;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.gms.ads.AdView;
import java.util.ArrayList;


public class ProjectFragment extends Fragment {

    ListView project_lv;
    ArrayList<String> project_list_array;
    ArrayAdapter<String> itemsAdapter;
    TextView empty_project_tv;

    private AdView mAdView;

    public ProjectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.projects_tab));
        // Inflate the layout for this fragment

        final View myFragmentView = inflater.inflate(R.layout.fragment_project, container, false);

        project_lv  = myFragmentView.findViewById(R.id.project_lv);
        empty_project_tv = myFragmentView.findViewById(R.id.empty_project_tv);
        mAdView = (AdView) myFragmentView.findViewById(R.id.adView_project_frag);
        project_list_array = Utils.getProjects_list(myFragmentView.getContext());
        itemsAdapter = new ArrayAdapter<String>(myFragmentView.getContext(), android.R.layout.simple_list_item_1, project_list_array);
        project_lv.setAdapter(itemsAdapter);
        project_lv.setEmptyView(empty_project_tv);


        Utils.loadAd(mAdView);


        project_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                CharListFragment nextFragment = new CharListFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Utils.changeFragment(nextFragment,transaction,"project_name",itemsAdapter.getItem(position).toString());
            }
        });


        FloatingActionButton fab = (FloatingActionButton) myFragmentView.findViewById(R.id.add_project_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Project_detailsFragment nextFragment = new Project_detailsFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Utils.changeFragment(nextFragment,transaction,"project_name","");
            }
        });


        return myFragmentView;

    }

}
