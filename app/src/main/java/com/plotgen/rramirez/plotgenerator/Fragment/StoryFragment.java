package com.plotgen.rramirez.plotgenerator.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Common.Notify;
import com.plotgen.rramirez.plotgenerator.Common.Utils;
import com.plotgen.rramirez.plotgenerator.MainActivity;
import com.plotgen.rramirez.plotgenerator.Model.User;
import com.plotgen.rramirez.plotgenerator.Model.UserStory;
import com.plotgen.rramirez.plotgenerator.R;
import com.plotgen.rramirez.plotgenerator.ViewHolder.UserStoryViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StoryFragment extends Fragment {
    @BindView(R.id.rvWeeklyChalenge)
    RecyclerView rvWeeklyChalenge;

    private DatabaseReference mReference;
    private FirebaseRecyclerOptions<UserStory> options;
    private FirebaseRecyclerAdapter<UserStory, UserStoryViewHolder> mAdapter;
    private DatabaseReference mCommentReference;
    private DatabaseReference mUserReference;

    public StoryFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        /*mAdapter.stopListening();*/
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle("Discover");
        View view = inflater.inflate(R.layout.fragment_weekly_challenge, container, false);

        ButterKnife.bind(this, view);
        // Set up Layout Manager, reverse layout
        LinearLayoutManager mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        rvWeeklyChalenge.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

        mReference = mDatabase.getReference().child("stories");
        mUserReference = mDatabase.getReference().child("users");

        mCommentReference = mDatabase.getReference().child("stories").child("post-comments");
        Query myTopPostsQuery = mDatabase.getReference().child("stories").orderByChild(Common.currentGenre);

        options = new FirebaseRecyclerOptions.Builder<UserStory>()
                .setQuery(myTopPostsQuery, UserStory.class)
                .build();

        populateStories();
        rvWeeklyChalenge.setAdapter(mAdapter);
        return view;
    }

    private void populateStories() {
        mAdapter = new FirebaseRecyclerAdapter<UserStory, UserStoryViewHolder>(options) {
            @NonNull
            @Override
            public UserStoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new UserStoryViewHolder(inflater.inflate(R.layout.item_story, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull UserStoryViewHolder viewHolder, int position, @NonNull final UserStory model) {
                final DatabaseReference postRef = getRef(position);
                viewHolder.setIsRecyclable(false);
                Log.e("reff model", mReference.child(postRef.getKey()).getKey() + "  " + model.getGenre());

                if (model.getGenre() != null) {
                    if (model.getGenre().equals(Common.currentGenre)) {
                        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Common.currentUserStory = model;
                                UserStoryDetailFragment nextFragment = new UserStoryDetailFragment();
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                Utils.changeFragment(nextFragment, transaction, "", "");
                                transaction.addToBackStack(null);
                            }
                        });

                        if (model.likes.containsKey(Common.currentUser.getUid())) {
                            viewHolder.ivLoves.setImageResource(R.drawable.ic_love_red);
                        } else {
                            viewHolder.ivLoves.setImageResource(R.drawable.ic_love_outline);
                        }

                        View.OnClickListener likeClickListener = new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DatabaseReference globalPostRef = mReference.child(postRef.getKey());
                                onLikeClicked(globalPostRef);
                            }
                        };

                        viewHolder.bindToPost(model, likeClickListener, mCommentReference.child(model.getId()));
                    } else {
                        viewHolder.removeItem();
                    }
                } else {
                    viewHolder.removeItem();
                }
            }
        };
    }

    private void onLikeClicked(final DatabaseReference postRef) {

        postRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                final UserStory p = mutableData.getValue(UserStory.class);
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
                    //  sendNotification(Common.currentUser.getName() + " liked your post", p.getUser(), postRef.getKey());
                }

                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {

                Log.d("UpdateLikeCount", "postTransaction:onComplete:" + databaseError);
            }
        });
    }

    public void sendNotification(final String message, User user, final String id) {
        //String firebase_token = mUserReference.child(user.getUid()).child("token");
        Query query = mUserReference.child(user.getUid()).orderByChild("token");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("token")) {
                    String token = (String) dataSnapshot.child("token").getValue();

                    new Notify(token, message, id, "Stories").execute();
                    //notifyMessage(to,message);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("notification action", databaseError.getDetails());
            }
        });

    }


}


