package com.plotgen.rramirez.plotgenerator;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.plotgen.rramirez.plotgenerator.Adapters.Adapter_trigger;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Common.Utils;
import com.plotgen.rramirez.plotgenerator.Fragment.SubmitTriggerFragment;
import com.plotgen.rramirez.plotgenerator.items.item_trigger;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class TriggerFragment extends Fragment {


    ArrayList trigger_list,trigger_backgrounds,promptsList, authorsList;
    List<item_trigger> mlist = new ArrayList<>();
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
        final View myFragmentView = inflater.inflate(R.layout.fragment_trigger, container, false);
        ButterKnife.bind(this, myFragmentView);

        if (!Common.isPAU) {
            //Add the ads
            mAdView = (AdView) myFragmentView.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }

        //Get the array data
        CollectionReference mCollectionRef;
        if (Locale.getDefault().getDisplayLanguage().equals("español")) {
            mCollectionRef = FirebaseFirestore.getInstance().collection("triggers_es");
        } else {
            mCollectionRef = FirebaseFirestore.getInstance().collection("triggers");
        }
        promptsList = new ArrayList();
        authorsList = new ArrayList();
        mCollectionRef.document("0").collection("special").whereEqualTo("selected",true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                promptsList.add(Objects.requireNonNull(document.getData().get("story")).toString());
                                HashMap map = (HashMap) document.getData().get("user");
                                authorsList.add(map.get("name").toString());
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }

                        populateTriggers(myFragmentView);
                    }

                });



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

    public void populateTriggers(View view)
    {

        //set up the recycler view with the adapter
        RecyclerView recyclerView = view.findViewById(R.id.rv_list);
        final Adapter_trigger adapter = new Adapter_trigger(this.getActivity(), mlist);

        //The arrays
        trigger_list = new ArrayList();
        trigger_backgrounds = new ArrayList();

        //Fill up the background trigger list
        trigger_backgrounds.addAll(Arrays.asList(R.color.color_trigger_1, R.color.color_trigger_2, R.color.color_trigger_3, R.color.color_trigger_4, R.color.color_trigger_5));
        int x = 0;
        for (int i = 0; i < promptsList.size(); i++) {
            mlist.add(new item_trigger((Integer) trigger_backgrounds.get(x), (String) promptsList.get(i), "By " + (String) authorsList.get(i)));
            adapter.notifyDataSetChanged();
            x++;
            if (x >= trigger_backgrounds.size()) {
                x = 0;
            }
        }

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

    }
}
