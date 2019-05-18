package com.plotgen.rramirez.plotgenerator.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Common.Utils;
import com.plotgen.rramirez.plotgenerator.MainActivity;
import com.plotgen.rramirez.plotgenerator.Model.Genre;
import com.plotgen.rramirez.plotgenerator.R;
import com.plotgen.rramirez.plotgenerator.ViewHolder.GenreViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DiscoverFragment extends Fragment {

    @BindView(R.id.rv_genre_list)
    RecyclerView rvGenre;

    @BindView(R.id.empty_project_tv)
    TextView emptyGenreTv;

    private InterstitialAd mInterstitialAd;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private LinearLayoutManager mManager;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private FirebaseRecyclerOptions<Genre> options;
    private FirebaseRecyclerAdapter mAdapter;

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle("Discover");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        ButterKnife.bind(this, view);

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        // rvGenre.setLayoutManager(mManager);
        rvGenre.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        mUser = mAuth.getCurrentUser();

        MobileAds.initialize(this.getContext(), getString(R.string.ad_account_id));


//        if (!Common.isPAU) {
//        //Init the ads
//        // Interstitial
//            mInterstitialAd = new InterstitialAd(this.getContext());
//            mInterstitialAd.setAdUnitId(getString(R.string.interstitial_plot_gen));
//            mInterstitialAd.loadAd(new AdRequest.Builder()
//                    .addTestDevice("E230AE087E1D0E7FB2304943F378CD64")
//                    .build());
//            mInterstitialAd.setAdListener(new AdListener() {
//                @Override
//                public void onAdClosed() {
//                    // Load the next interstitial.
//                    mInterstitialAd.loadAd(new AdRequest.Builder()
//                            .addTestDevice("E230AE087E1D0E7FB2304943F378CD64")
//                            .build());
//                }
//            });
//        }

        mReference = mDatabase.getReference().child("stories");

        Query myTopPostsQuery = mReference.child("genre");
        options = new FirebaseRecyclerOptions.Builder<Genre>()
                .setQuery(myTopPostsQuery, Genre.class)
                .build();
        populateDiscoverGenre();


        Utils.popUp(this.getContext());



        return view;
    }


    private void populateDiscoverGenre() {

        mAdapter = new FirebaseRecyclerAdapter<Genre, GenreViewHolder>(options) {
            @NonNull
            @Override
            public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                return new GenreViewHolder(inflater.inflate(
                        R.layout.story_genre, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull GenreViewHolder holder, int position, @NonNull final Genre model) {
                final DatabaseReference postRef = getRef(position);
                holder.setIsRecyclable(false);

                Log.e("reff", mReference.child("genre").child(postRef.getKey()).getKey() + "  ");
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Common.currentGenre = mReference.child("genre").child(postRef.getKey()).getKey();
                        StoryFragment nextFragment = new StoryFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        Utils.changeFragment(nextFragment, transaction);
                        transaction.addToBackStack(null);
                    }
                });
                holder.bindToPost(mReference.child("genre").child(postRef.getKey()));
            }

        };
//        if (!Common.isPAU) {
//            if (mInterstitialAd.isLoaded()) {
//                mInterstitialAd.show();
//
//            } else {
//                Log.d("TAG", "The interstitial wasn't loaded yet.");
//            }
//        }
        rvGenre.setAdapter(mAdapter);
    }
}
