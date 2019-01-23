package com.plotgen.rramirez.plotgenerator.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.plotgen.rramirez.plotgenerator.MainActivity;
import com.plotgen.rramirez.plotgenerator.Model.Genre;
import com.plotgen.rramirez.plotgenerator.R;
import com.plotgen.rramirez.plotgenerator.ViewHolder.GenreViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DiscoverFragment extends Fragment {

    @BindView(R.id.rv_genre_list)
    RecyclerView rvGenre;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private LinearLayoutManager mManager;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private FirebaseRecyclerOptions<Genre> options;
    private FirebaseRecyclerAdapter mAdapter;
    private Query query;

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

        mReference = mDatabase.getReference().child("stories").child("genre");
        query = mDatabase.getReference().child("stories").child("genre");
        options = new FirebaseRecyclerOptions.Builder<Genre>()
                .setQuery(query, Genre.class)
                .build();

        populateDiscoverGenre();
        rvGenre.setAdapter(mAdapter);

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
            protected void onBindViewHolder(@NonNull GenreViewHolder holder, int position, @NonNull Genre model) {
                final DatabaseReference postRef = getRef(position);
                final Genre genre = model;

                holder.setIsRecyclable(false);
                Log.e("reff", mReference.child(postRef.getKey()).getKey());
                holder.bindToPost(genre, mReference.child(postRef.getKey()));
            }
        };


    }
}
