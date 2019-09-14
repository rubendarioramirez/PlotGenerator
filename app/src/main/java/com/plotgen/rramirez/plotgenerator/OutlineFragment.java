package com.plotgen.rramirez.plotgenerator;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
    @BindView(R.id.outline_add_btn)
    com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton outline_add_btn;

    @BindView(R.id.outline_empty_tv)
    TextView outline_empty_tv;

    //    private String databaseToUse;
    private AdView mAdView;

    public OutlineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.add_outline));
        final View myFragmentView = inflater.inflate(R.layout.fragment_outline, container, false);
        ButterKnife.bind(this, myFragmentView);


        if (!Common.isPAU) {
            //Add the ads
            mAdView = (AdView) myFragmentView.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }


        outline_list = new ArrayList();
        try {
            outline_list = SQLUtils.getOutlineByProjectID(getContext(), Common.currentProject.getId());
        } catch (Exception e){
            Toast.makeText(getContext(),"Error: Please relaunch the app or contact the developer", Toast.LENGTH_LONG).show();
        }
        if (outline_list.size() > 0){
            outline_empty_tv.setVisibility(View.INVISIBLE);
        }
        populateOutline(myFragmentView);
        setHasOptionsMenu(true);

        outline_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Outline_detail nextFragment = new Outline_detail();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    boolean fragmentPopped = getFragmentManager().popBackStackImmediate(fragmentTag, 0);
                    if (!fragmentPopped && getFragmentManager().findFragmentByTag(fragmentTag) == null) {
                        transaction.addToBackStack(null);
                    }
                    Common.outlineCreationMode = true;
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

    public void populateOutline(View view)
    {
        //set up the recycler view with the adapter
        RecyclerView recyclerView = view.findViewById(R.id.outline_list);
        final Adapter_outline adapter = new Adapter_outline(this.getActivity(), mlist);
        mlist.clear();
        for (int i = 0; i < outline_list.size(); i++) {
            String id =  outline_list.get(i).toString().split("/&&/")[0];
            String title = outline_list.get(i).toString().split("/&&/")[1];
            String description = outline_list.get(i).toString().split("/&&/")[2];
            String characters = outline_list.get(i).toString().split("/&&/")[3];
            String position = outline_list.get(i).toString().split("/&&/")[4];
            mlist.add(new item_outline(id,title, description,characters,position));
            adapter.notifyDataSetChanged();
        }

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

    }


    public void SHARE() {
        StringBuffer body = new StringBuffer();
        for (int i = 0; i < outline_list.size(); i++) {
            String title = outline_list.get(i).toString().split("/&&/")[1];
            String description = outline_list.get(i).toString().split("/&&/")[2];
            String characters = outline_list.get(i).toString().split("/&&/")[3];
            body.append("<font color=\"red\">Title: </font>" + title + "<br><br>" + description + "<br><br>Characters: " + characters + "<br><br>");

        }

        //Do something in response to button
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Auctor: " + Common.currentProject.getName() + " outline");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(body.toString()));
        startActivity(Intent.createChooser(sharingIntent, "share"));
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_outlinelist, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_outline_share) {
            if(Common.isPAU){
                SHARE();
            } else {
                Toast.makeText(getContext(),getString(R.string.premiumOnly),Toast.LENGTH_LONG).show();
            }

            return true;
                }


        return super.onOptionsItemSelected(item);
        }

}
