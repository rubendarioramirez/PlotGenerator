package com.plotgen.rramirez.plotgenerator;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class TriggerFragment extends Fragment {


    ArrayList trigger_list;
    ArrayList trigger_backgrounds;
    List<item> mlist = new ArrayList<>();
    private String databaseToUse;
    private AdView mAdView;

    public TriggerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.trigger_tab));

        View myFragmentView = inflater.inflate(R.layout.fragment_trigger, container, false);


        //Get device lang
        if (Locale.getDefault().getLanguage()=="es"){
            databaseToUse = "triggers_es";
        }
        else {
            databaseToUse = "triggers";
        }



        //Get a firebase reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(databaseToUse);



        //Add the ads
        mAdView = (AdView) myFragmentView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        //set up the recycler view with the adapter
        RecyclerView recyclerView = myFragmentView.findViewById(R.id.rv_list);
        final Adapter adapter = new Adapter(this.getActivity(),mlist);




                //The arrays
        trigger_list = new ArrayList();
        trigger_backgrounds = new ArrayList();

        //Fill up the background trigger list
        trigger_backgrounds.addAll(Arrays.asList(R.drawable.writting, R.drawable.writting1, R.drawable.writting2, R.drawable.writting3, R.drawable.writting4,R.drawable.writting6,R.drawable.writing7,R.drawable.writing8));


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {;

                for (DataSnapshot childSnap : dataSnapshot.getChildren()){
                    trigger_list.add(childSnap.getValue(String.class));
                }
                for(int i=0;i<trigger_list.size();i++){
                    int randomNumber = new Random().nextInt(8);
                    mlist.add(new item((Integer) trigger_backgrounds.get(randomNumber), (String) trigger_list.get(i)));


                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });



        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        return  myFragmentView;
    }




    public void onStart(){
        super.onStart();


    }
}
