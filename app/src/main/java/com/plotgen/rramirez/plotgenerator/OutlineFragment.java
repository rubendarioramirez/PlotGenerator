package com.plotgen.rramirez.plotgenerator;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.plotgen.rramirez.plotgenerator.Adapters.Adapter_outline;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Common.SQLUtils;
import com.plotgen.rramirez.plotgenerator.Common.Utils;
import com.plotgen.rramirez.plotgenerator.items.item_outline;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class OutlineFragment extends Fragment {


    ArrayList outline_list;
    List<item_outline> mlist = new ArrayList<>();
    private String fragmentTag = OutlineFragment.class.getSimpleName();

    //    private String databaseToUse;
    private AdView mAdView;

    public OutlineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //TODO change the name
        ((MainActivity) getActivity()).setActionBarTitle("Outline");
        final View myFragmentView = inflater.inflate(R.layout.fragment_outline, container, false);
        ButterKnife.bind(this, myFragmentView);
        setHasOptionsMenu(true);

        if (!Common.isPAU) {
            //Add the ads
            mAdView = (AdView) myFragmentView.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }


        outline_list = new ArrayList();
        populateOutline(myFragmentView);

        return myFragmentView;
    }


    public void onStart() {
        super.onStart();
    }

    public void populateOutline(View view)
    {
        outline_list = SQLUtils.getOutlineByID(getContext(), Common.currentProject.getId());
        //set up the recycler view with the adapter
        RecyclerView recyclerView = view.findViewById(R.id.outline_list);
        final Adapter_outline adapter = new Adapter_outline(this.getActivity(), mlist);
        mlist.clear();
        for (int i = 0; i < outline_list.size(); i++) {
            String title = outline_list.get(i).toString().split("/&&/")[1];
            String description = outline_list.get(i).toString().split("/&&/")[2];
            String characters = outline_list.get(i).toString().split("/&&/")[3];
            mlist.add(new item_outline(title, description,characters));
            adapter.notifyDataSetChanged();
        }

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_outlinelist, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_outline_add) {
            try {
                Outline_detail nextFragment = new Outline_detail();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                boolean fragmentPopped = getFragmentManager().popBackStackImmediate(fragmentTag, 0);
                if (!fragmentPopped && getFragmentManager().findFragmentByTag(fragmentTag) == null) {
                    transaction.addToBackStack(null);
                }
                Utils.changeFragment(nextFragment, transaction);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
