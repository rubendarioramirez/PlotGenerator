package com.plotgen.rramirez.plotgenerator;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.gms.ads.AdView;
import com.plotgen.rramirez.plotgenerator.Adapters.Adapter_projectList;
import com.plotgen.rramirez.plotgenerator.Common.AdsHelper;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Common.Tutorial;
import com.plotgen.rramirez.plotgenerator.Common.Utils;
import com.plotgen.rramirez.plotgenerator.items.item_project_list;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.plotgen.rramirez.plotgenerator.Common.Constants.debugMode;


public class ProjectFragment extends Fragment {
    //region Declare elements
    String fragmentTag = ProjectFragment.class.getSimpleName();
    ArrayList<String> project_list_array;
    boolean alreadyCalled;
    List<item_project_list> mlist = new ArrayList<>();

    @BindView(R.id.project_lv)
    RecyclerView project_lv;
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

        project_list_array = Utils.getProjects_list(myFragmentView.getContext());
        final Adapter_projectList adapter = new Adapter_projectList(this.getActivity(),mlist);
        mlist.clear();

        if(!project_list_array.isEmpty()){
            empty_project_tv.setVisibility(View.INVISIBLE);
            for (int i = 0; i<project_list_array.size();i++) {
                String id = project_list_array.get(i).split("/&&/")[0];
                String name = project_list_array.get(i).split("/&&/")[1];
                String genre = project_list_array.get(i).split("/&&/")[2];
                String characters = String.valueOf(Utils.getCharListByID(getContext(),id).size());
                mlist.add(new item_project_list(id, name, genre, characters));
                }
            }

        project_lv.setAdapter(adapter);
        project_lv.setLayoutManager(new LinearLayoutManager(this.getContext()));


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
