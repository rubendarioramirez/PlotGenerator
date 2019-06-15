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
import com.plotgen.rramirez.plotgenerator.Common.AdsHelper;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Common.Tutorial;
import com.plotgen.rramirez.plotgenerator.Common.Utils;
import com.plotgen.rramirez.plotgenerator.Model.Project;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.plotgen.rramirez.plotgenerator.Common.Constants.debugMode;


public class ProjectFragment extends Fragment {
    //region Declare elements
    String fragmentTag = ProjectFragment.class.getSimpleName();
    ArrayList<String> project_list_array;
    ArrayAdapter<String> itemsAdapter;
    ArrayList<String> project_names;
    ArrayList<String> projects_ids;
    boolean alreadyCalled;

    @BindView(R.id.project_lv)
    ListView project_lv;
    @BindView(R.id.empty_project_tv)
    TextView empty_project_tv;
    @BindView(R.id.adView_project_frag)
    AdView mAdView;
    //endregion

    public ProjectFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.projects_tab));
        final View myFragmentView = inflater.inflate(R.layout.fragment_project, container, false);
        ButterKnife.bind(this, myFragmentView);

        //region Populate Project list
        project_names = new ArrayList<String>();
        projects_ids = new ArrayList<String>();
        project_list_array = Utils.getProjects_list(myFragmentView.getContext());
        if (project_list_array != null && !project_list_array.isEmpty())
            for (int i = 0; i < project_list_array.size(); i++) {
                if (!project_list_array.get(i).isEmpty()) {
                    projects_ids.add(String.valueOf(project_list_array.get(i).split("/&&/")[0]));
                    project_names.add(project_list_array.get(i).split("/&&/")[1]);
                }
            }
        itemsAdapter = new ArrayAdapter<String>(myFragmentView.getContext(), android.R.layout.simple_list_item_1, project_names);
        project_lv.setAdapter(itemsAdapter);
        project_lv.setEmptyView(empty_project_tv);
        //endregion

        //region Tutorial
        if(project_list_array.isEmpty()){
            Common.onBoarding = 1;
            Common.tutorialMODE = true;
        }

        if(!alreadyCalled){
        Tutorial.checkTutorial(myFragmentView,getActivity());
        alreadyCalled=true;
        }
        //endregion Tutorial

        //Load ad
        AdsHelper.loadAd(mAdView);

        //region handle Click on projects
        project_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                CharListFragment nextFragment = new CharListFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                boolean fragmentPopped = getFragmentManager().popBackStackImmediate(fragmentTag, 0);
//                if (!fragmentPopped && getFragmentManager().findFragmentByTag(fragmentTag) == null) {
                Project project = new Project(projects_ids.get(position),project_names.get(position));
                Common.currentProject = project;
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);
                transaction.replace(R.id.flMain, nextFragment);
                transaction.addToBackStack(null);
                transaction.commit();
//                }
            }
        });
        //endregion

        //region handle Add project button
        FloatingActionButton fab = (FloatingActionButton) myFragmentView.findViewById(R.id.add_project_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.projectCreationMode = true;

                if(debugMode)
                Log.i("matilda", "Creation mode is: " + Common.projectCreationMode + " at Project ProjectList");

                Project_detailsFragment nextFragment = new Project_detailsFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//  Log.e("story stack size", getFragmentManager().getBackStackEntryCount() + " " + fragmentTag);
//                boolean fragmentPopped = getFragmentManager().popBackStackImmediate(fragmentTag, 0);
//                if (!fragmentPopped && getFragmentManager().findFragmentByTag(fragmentTag) == null) {
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);
                transaction.replace(R.id.flMain, nextFragment);
                transaction.addToBackStack(null);
                transaction.commit();

//                }
            }
        });
        //endregion

        return myFragmentView;
    }





}
