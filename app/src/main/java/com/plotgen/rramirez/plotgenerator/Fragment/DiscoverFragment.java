package com.plotgen.rramirez.plotgenerator.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Common.Utils;
import com.plotgen.rramirez.plotgenerator.MainActivity;
import com.plotgen.rramirez.plotgenerator.Model.Story;
import com.plotgen.rramirez.plotgenerator.R;
import com.plotgen.rramirez.plotgenerator.ViewHolder.OfflineStoryViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DiscoverFragment extends Fragment {

    @BindView(R.id.rv_stories_list)
    RecyclerView rvGenre;

    @BindView(R.id.empty_project_tv)
    TextView emptyGenreTv;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private LinearLayoutManager mManager;
    private FirebaseFirestore mDatabase;
    private CollectionReference mReference;
    private FirestoreRecyclerOptions<Story> options;
    private FirestoreRecyclerAdapter<Story, OfflineStoryViewHolder> mAdapter;

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

    @SuppressLint("WrongConstant")
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

        mDatabase = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        mUser = mAuth.getCurrentUser();

        mReference = mDatabase.collection("stories");

        Query myTopPostsQuery = mReference.orderBy("date").limit(5);
        options = new FirestoreRecyclerOptions.Builder<Story>()
                .setQuery(myTopPostsQuery, Story.class)
                .build();
        populateDiscoverGenre();

        return view;
    }

    private void populateDiscoverGenre() {
        mAdapter = new FirestoreRecyclerAdapter<Story, OfflineStoryViewHolder>(options) {

            @NonNull
            @Override
            public OfflineStoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                return new OfflineStoryViewHolder(inflater.inflate(
                        R.layout.item_user_story, parent, false));
            }
            @Override
            protected void onBindViewHolder(@NonNull OfflineStoryViewHolder holder, int position, @NonNull final Story model) {
              //  final DatabaseReference postRef = getRef(position);
                final Story story = model;
                holder.setIsRecyclable(false);
                //Log.e("reff", mReference.document("genre").collection(model.getGenre()).getId() + "  ");
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Common.currentUserStory = model;
                        Common.currentStory = model;
                        UserStoryDetailFragment nextFragment = new UserStoryDetailFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        Utils.changeFragment(nextFragment, transaction);
                        transaction.addToBackStack(null);
                    }
                });
                holder.bindToPost(mReference.document(model.getTitle()), story);
            }
        };
        rvGenre.setAdapter(mAdapter);
    }
}
