package com.plotgen.rramirez.plotgenerator;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class weeklyWriting extends Fragment {

    private TextView title,description;
    private AdView mAdView;
    private String databaseToUse;
    ArrayList data_list;
    private FirebaseAnalytics mFirebaseAnalytics;


    public weeklyWriting() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.writing_challenge_tab));
        // Inflate the layout for this fragment
        View myFragmentView = inflater.inflate(R.layout.fragment_weekly_writing, container, false);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(myFragmentView.getContext());
        //Log challenges updated
        Bundle params = new Bundle();
        params.putString("WeeklyWriting", "opened");
        mFirebaseAnalytics.logEvent("weekly_writing",params);


        //Define ads
        mAdView = (AdView) myFragmentView.findViewById(R.id.adView_writing_challenge);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //Define elements
        title = myFragmentView.findViewById(R.id.writing_challenge_title);
        description = myFragmentView.findViewById(R.id.writing_challenge_desc);

        //Get a firebase reference
        //Get device lang
        if (Locale.getDefault().getLanguage()=="es"){
            databaseToUse = "writing_challenge_es";
        }
        else {
            databaseToUse = "writing_challenge";
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(databaseToUse);
        data_list = new ArrayList();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {;
                for (DataSnapshot childSnap : dataSnapshot.getChildren()){
                    data_list.add(childSnap.getValue(String.class));

                }
                title.setText(data_list.get(0).toString());
                description.setText(data_list.get(1).toString());

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });



        return myFragmentView;
    }

}
