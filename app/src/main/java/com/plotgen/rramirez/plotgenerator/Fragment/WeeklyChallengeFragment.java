package com.plotgen.rramirez.plotgenerator.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.MainActivity;
import com.plotgen.rramirez.plotgenerator.Model.Like;
import com.plotgen.rramirez.plotgenerator.Model.Story;
import com.plotgen.rramirez.plotgenerator.R;
import com.plotgen.rramirez.plotgenerator.Utils;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeeklyChallengeFragment extends Fragment {

    @BindView(R.id.lvWeeklyChalenge)
    ListView lvWeeklyChallenge;

    FirebaseListAdapter<Story> adapter;
    FirebaseListOptions<Story> options;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;


    public WeeklyChallengeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity)getActivity()).setActionBarTitle("Weekly Challenge Participant");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weekly_challenge, container, false);

        ButterKnife.bind(this, view);

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference().child("Weekly_Challenge_test").child("posts");

        Query query = mDatabase.getReference().child("Weekly_Challenge_test").child("posts").orderByChild("likeCount");
        options = new FirebaseListOptions.Builder<Story>()
                .setQuery(query, Story.class)
                .setLayout(R.layout.item_story)
                .build();

        populateWeeklyChallenge();

        return view;
    }

    private void populateWeeklyChallenge() {

        adapter = new FirebaseListAdapter<Story>(options) {
            @Override
            public Story getItem(int position) {
                return super.getItem(super.getCount() - position - 1);
            }
            @Override
            protected void populateView(@NonNull View v, @NonNull final Story model, int position) {
                final DatabaseReference postRef = getRef(position);

                final TextView tvTitle, tvGenre, tvStory, tvUser, tvLoves;
                final ImageView ivTemplatePic, ivUser, ivLoves;

                final boolean canLike;

                tvTitle = v.findViewById(R.id.tvTitle);
                tvGenre = v.findViewById(R.id.tvGenre);
                tvStory = v.findViewById(R.id.tvStory);
                tvUser = v.findViewById(R.id.tvUser);
                tvLoves = v.findViewById(R.id.tvLoves);
                ivUser = v.findViewById(R.id.ivUser);
                ivLoves = v.findViewById(R.id.ivLoves);

                Glide.with(v.getContext())
                        .load(model.getUser().getUriString())
                        .apply(RequestOptions.circleCropTransform())
                        .into(ivUser);

                ivTemplatePic = v.findViewById(R.id.ivTemplatePic);

                ivTemplatePic.setImageResource(R.drawable.typewriter);

                tvUser.setText(model.getUser().getName());
                tvTitle.setText(model.getTitle());
                tvGenre.setText(model.getGenre());
                tvStory.setText(model.getChalenge());
                tvLoves.setText(String.valueOf(model.getLikeCount()));

                final Story currentStory = model;

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Common.currentStory = currentStory;
                        StoryDetailFragment nextFragment = new StoryDetailFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        Utils.changeFragment(nextFragment,transaction,"","");
                    }
                });

                // Determine if the current user has liked this post and set UI accordingly
                if (model.likes.containsKey(Common.currentUser.getUid())) {
                    ivLoves.setImageResource(R.drawable.ic_love_red);
                } else {
                    ivLoves.setImageResource(R.drawable.ic_love_outline);
                }

                ivLoves.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference globalPostRef = mReference.child(postRef.getKey());

                        // Run two transactions
                        onStarClicked(globalPostRef);
                    }
                });
            }
        };

        lvWeeklyChallenge.setAdapter(adapter);
    }

    private void onStarClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Story p = mutableData.getValue(Story.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.likes.containsKey(Common.currentUser.getUid())) {
                    // Unstar the post and remove self from stars
                    p.likeCount = p.likeCount - 1;
                    p.likes.remove(Common.currentUser.getUid());
                } else {
                    // Star the post and add self to stars
                    p.likeCount = p.likeCount + 1;
                    p.likes.put(Common.currentUser.getUid(), true);
                }

                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {

            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

}
