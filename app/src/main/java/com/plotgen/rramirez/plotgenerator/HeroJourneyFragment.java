package com.plotgen.rramirez.plotgenerator;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class HeroJourneyFragment extends Fragment {

    List<item_herojourney> mlist = new ArrayList<>();
    private AdView mAdView;

    public HeroJourneyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).setActionBarTitle("Hero Journey");

        View myFragmentView =  inflater.inflate(R.layout.fragment_herojourney, container, false);
        mAdView = (AdView) myFragmentView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //Get a firebase reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("hero_journey");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {;

                for (DataSnapshot childSnap : dataSnapshot.getChildren()){
                    Log.v("tmz",""+ childSnap.getValue(String.class)); //displays the key for the node
                    String data = childSnap.getValue(String.class);
                    String[] data_split = data.split(",");
                    Log.v("tmz",""+ data_split[0] + data_split[2]);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });



        //set up the recycler view with the adapter
        RecyclerView recyclerView = myFragmentView.findViewById(R.id.rv_herojourney);
        final Adapter_herojourney adapter = new Adapter_herojourney(this.getActivity(),mlist);
        mlist.add(new item_herojourney(R.drawable.hero, (String) "The call to adventure", (String) "Act 1: The Departure", (String)"The hero is presented with a problem, challenge or adventure. \nThe hero must face beginning of change.  This call must be accepted, this can happen willingly, \nreluctantly, consciously or accidentally"));
        mlist.add(new item_herojourney(R.drawable.hero2, (String) "Refusal of the call", (String) "Act 1: The Departure", (String)"The hero doesn’t accept the call right away. This can happen for all sorts of reasons, such as being unwilling, being in denial or being kept from being able to respond. \nThe hero finds motivation and or opportunity to answer the call."));
        mlist.add(new item_herojourney(R.drawable.hero, (String) "Supernatural Aid", (String) "Act 1: The Departure", (String)"The hero is now committed to the quest (consciously or unconsciously) and his guide/ helper becomes known. \nOften, the mentor will present the hero with a talisman or artifact to aid him later in the quest."));
        mlist.add(new item_herojourney(R.drawable.hero2, (String) "The crossing of the First Threshold", (String) "Act 1: The Departure", (String)"The hero crosses into the field of adventure, leaving behind all that is known and stepping into the unknown. Here, the limits, rules and limits are not yet known. \nThere is no going back, this is where the adventure gets going."));
        mlist.add(new item_herojourney(R.drawable.hero, (String) "Belly of the whale", (String) "Act 2: Initiation", (String)"The belly of the whale represents the final separation from the known world of the hero. In this stage, he shows willingness to undergo change and adaptation."));
        mlist.add(new item_herojourney(R.drawable.hero2, (String) "The road of Trails", (String) "Act 2: Initiation", (String)"This is a series of tests, tasks and ordeals that the hero must undergo in order to begin his transformation. Often the tests occur in threes and the hero usually fails one ore more. \nSometimes, the heroes lose their mentor in this stage (e.g. LOTR and Star Wars)."));
        mlist.add(new item_herojourney(R.drawable.hero, (String) "The meeting with the Goddess", (String) "Act 2: Initiation", (String)"The hero experiences a love that is all-powerful, encompassing and unconditional."));
        mlist.add(new item_herojourney(R.drawable.hero2, (String) "Woman as Temptress", (String) "Act 2: Initiation", (String)"The hero faces temptation (physical or pleasurable) which make lead him to stray from his mission or even abandon it. This temptation does not necessary come from a woman."));
        mlist.add(new item_herojourney(R.drawable.hero, (String) "Atonement with the father", (String) "Act 2: Initiation", (String)"The hero confronts the ultimate power in his life. This is the centre point of the story. All previous steps have brought him here and the steps to come move him away."));
        mlist.add(new item_herojourney(R.drawable.hero2, (String) "Apotheosis", (String) "Act 2: Initiation", (String)"There is a period of rest, peace and fulfilment before the hero begins his journey home."));
        mlist.add(new item_herojourney(R.drawable.hero, (String) "The Ultimate Boon", (String) "Act 2: Initiation", (String)"The achievement of the goal or quest. The hero gets what he came for."));
        mlist.add(new item_herojourney(R.drawable.hero2, (String) "Refusal of the return", (String) "Act 3: The return", (String)"The hero may not want to return to the ordinary world."));
        mlist.add(new item_herojourney(R.drawable.hero, (String) "The magic flight", (String) "Act 3: The return", (String)"Sometimes the hero must escape with the artifact he came for (e.g. the holy grail) and others come after him to claim it back. In this case, the journey back can be just as dangerous and full of adventure."));
        mlist.add(new item_herojourney(R.drawable.hero2, (String) "Rescue from without", (String) "Act 3: The return", (String)"The hero often has (and needs) guides and helpers, sometimes the helpers are the one that need to bring him back to every day life. Especially if the hero has been severely wounded."));
        mlist.add(new item_herojourney(R.drawable.hero, (String) "The crossing of the return threshold", (String) "Act 3: The return", (String)"Now that he gained all this knowledge and experience, he needs to retain it and integrate it into his human life and or share it with the world."));
        mlist.add(new item_herojourney(R.drawable.hero2, (String) "Master of two worlds", (String) "Act 3: The return", (String)"This step occurs if the hero is a transcendental hero such as Jesus or Buddha. If the hero is human, this may be simply achieving a balance between the material and spiritual world. The hero becomes comfortable and competent in both the inner and outer world."));

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        return  myFragmentView;

    }







}
