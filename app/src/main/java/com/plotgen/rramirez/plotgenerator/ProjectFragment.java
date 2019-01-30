package com.plotgen.rramirez.plotgenerator;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Common.Utils;

import java.util.ArrayList;


public class ProjectFragment extends Fragment {
    String fragmentTag = ProjectFragment.class.getSimpleName();
    ListView project_lv;
    ArrayList<String> project_list_array;
    ArrayAdapter<String> itemsAdapter;
    TextView empty_project_tv;
    ArrayList<String> project_names;
    ArrayList<String> projects_ids;

    private AdView mAdView;

    public ProjectFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.projects_tab));
        // Inflate the layout for this fragment

        final View myFragmentView = inflater.inflate(R.layout.fragment_project, container, false);

        project_lv = myFragmentView.findViewById(R.id.project_lv);
        empty_project_tv = myFragmentView.findViewById(R.id.empty_project_tv);
        mAdView = (AdView) myFragmentView.findViewById(R.id.adView_project_frag);
        project_names = new ArrayList<String>();
        projects_ids = new ArrayList<String>();

        project_list_array = Utils.getProjects_list(myFragmentView.getContext());
        if (project_list_array != null && !project_list_array.isEmpty())
            for (int i = 0; i < project_list_array.size(); i++) {
                if (!project_list_array.get(i).isEmpty()) {
//                    projects_ids.add(String.valueOf(project_list_array.get(i).charAt(0)));
                    projects_ids.add(String.valueOf(project_list_array.get(i).split("_")[0]));
                    Log.v("matilda", project_list_array.get(i).split("_")[0]);
                    project_names.add(project_list_array.get(i).substring(2));
                }
            }

        itemsAdapter = new ArrayAdapter<String>(myFragmentView.getContext(), android.R.layout.simple_list_item_1, project_names);
        project_lv.setAdapter(itemsAdapter);
        project_lv.setEmptyView(empty_project_tv);

        if (!Common.isPAU) {
            Utils.loadAd(mAdView);
        }


        project_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                CharListFragment nextFragment = new CharListFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                boolean fragmentPopped = getFragmentManager().popBackStackImmediate(fragmentTag, 0);
                if (!fragmentPopped && getFragmentManager().findFragmentByTag(fragmentTag) == null) {
                    String values = projects_ids.get(position) + "_" + project_names.get(position);
                    Utils.changeFragment(nextFragment, transaction, "project_info", values);
                    transaction.addToBackStack(fragmentTag);
                }
            }
        });


        FloatingActionButton fab = (FloatingActionButton) myFragmentView.findViewById(R.id.add_project_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Project_detailsFragment nextFragment = new Project_detailsFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Log.e("story stack size", getFragmentManager().getBackStackEntryCount() + " " + fragmentTag);
                boolean fragmentPopped = getFragmentManager().popBackStackImmediate(fragmentTag, 0);
                if (!fragmentPopped && getFragmentManager().findFragmentByTag(fragmentTag) == null) {
                    Utils.changeFragment(nextFragment, transaction, "project_name", "");
                  //  transaction.addToBackStack(null);
                }
            }
        });

        return myFragmentView;
    }

}
