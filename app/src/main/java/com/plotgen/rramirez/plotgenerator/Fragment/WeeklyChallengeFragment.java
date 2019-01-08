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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Common.Notify;
import com.plotgen.rramirez.plotgenerator.MainActivity;
import com.plotgen.rramirez.plotgenerator.Model.Story;
import com.plotgen.rramirez.plotgenerator.Model.User;
import com.plotgen.rramirez.plotgenerator.R;
import com.plotgen.rramirez.plotgenerator.Utils;
import com.plotgen.rramirez.plotgenerator.ViewHolder.StoryViewHolder;

import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeeklyChallengeFragment extends Fragment {

    @BindView(R.id.rvWeeklyChalenge)
    RecyclerView rvWeeklyChallenge;

    private FirebaseRecyclerAdapter<Story, StoryViewHolder> mAdapter;
    private FirebaseRecyclerAdapter<Story, StoryViewHolder> mMyPostAdapter;
    private FirebaseRecyclerAdapter<Story, StoryViewHolder> mRecentAdapter;
    FirebaseRecyclerOptions<Story> options;
    FirebaseRecyclerOptions<Story> optionsMyPost;
    FirebaseRecyclerOptions<Story> optionsMostRecent;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private DatabaseReference mCommentReference;
    private DatabaseReference mUserReference;

    private LinearLayoutManager mManager;

    private int toggleMyPost = 1;

    public WeeklyChallengeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle("Weekly Challenge Participant");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weekly_challenge, container, false);

        ButterKnife.bind(this, view);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                updateUI();
            }
        });

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        rvWeeklyChallenge.setLayoutManager(mManager);

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference().child(getString(R.string.weekly_challenge_db_name)).child("posts");
        mCommentReference = mDatabase.getReference().child(getString(R.string.weekly_challenge_db_name)).child("post-comments");
        mUserReference = mDatabase.getReference().child("users");

        Query query = mDatabase.getReference().child(getString(R.string.weekly_challenge_db_name)).child("posts").orderByChild("likeCount");
        Query query_recents = mDatabase.getReference().child(getString(R.string.weekly_challenge_db_name)).child("posts").orderByChild("date");


        options = new FirebaseRecyclerOptions.Builder<Story>()
                .setQuery(query, Story.class)
                .build();

        optionsMyPost = new FirebaseRecyclerOptions.Builder<Story>()
                .setQuery(query, Story.class)
                .build();

        optionsMostRecent = new FirebaseRecyclerOptions.Builder<Story>()
                .setQuery(query_recents, Story.class)
                .build();


        populateWeeklyChallenge();
        rvWeeklyChallenge.setAdapter(mAdapter);

        return view;
    }

    private void populateWeeklyChallenge() {

        mAdapter = new FirebaseRecyclerAdapter<Story, StoryViewHolder>(options) {

            @Override
            public StoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new StoryViewHolder(inflater.inflate(R.layout.item_story, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(StoryViewHolder viewHolder, int position, final Story model) {
                final DatabaseReference postRef = getRef(position);
                final Story currentStory = model;

                viewHolder.setIsRecyclable(false);

                if (model.getTitle().contains(Common.currentChallenge.getName())) {

                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Common.currentStory = currentStory;
                            StoryDetailFragment nextFragment = new StoryDetailFragment();
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

                    viewHolder.bindToPost(model, likeClickListener, mCommentReference.child(postRef.getKey()));
                } else {
                    viewHolder.removeItem();
                }

            }
        };
        mMyPostAdapter = new FirebaseRecyclerAdapter<Story, StoryViewHolder>(optionsMyPost) {

            @Override
            public StoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new StoryViewHolder(inflater.inflate(R.layout.item_story, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(StoryViewHolder viewHolder, int position, final Story model) {
                final DatabaseReference postRef = getRef(position);
                final Story currentStory = model;

                viewHolder.setIsRecyclable(false);

                if (model.getUser().getUid().equals(Common.currentUser.getUid())) {

                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Common.currentStory = currentStory;
                            StoryDetailFragment nextFragment = new StoryDetailFragment();
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

                    viewHolder.bindToPost(model, likeClickListener, mCommentReference.child(postRef.getKey()));
                } else {
                    viewHolder.removeItem();
                }

            }
        };
        mRecentAdapter = new FirebaseRecyclerAdapter<Story, StoryViewHolder>(optionsMostRecent) {

            @Override
            public StoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new StoryViewHolder(inflater.inflate(R.layout.item_story, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(StoryViewHolder viewHolder, int position, final Story model) {
                final DatabaseReference postRef = getRef(position);
                final Story currentStory = model;

                viewHolder.setIsRecyclable(false);

                if (model.getTitle().contains(Common.currentChallenge.getName())) {

                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Common.currentStory = currentStory;
                            StoryDetailFragment nextFragment = new StoryDetailFragment();
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

                    viewHolder.bindToPost(model, likeClickListener, mCommentReference.child(postRef.getKey()));
                } else {
                    viewHolder.removeItem();
                }

            }
        };
    }

    private void onLikeClicked(final DatabaseReference postRef) {

        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                final Story p = mutableData.getValue(Story.class);
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
                    sendNotification(Common.currentUser.getName() + " liked your post", p.getUser(), p.getId());
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

    private void updateUI() {
        if (mUser != null) {
            Common.currentUser = new User(mUser.getUid(), mUser.getDisplayName(), mUser.getEmail(), mUser.getPhotoUrl().toString(), mUser.getPhotoUrl());
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startListening();
            mMyPostAdapter.startListening();
            mRecentAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
            mMyPostAdapter.stopListening();
            mRecentAdapter.stopListening();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_goto, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_goto) {
            if (toggleMyPost == 1) {
                rvWeeklyChallenge.swapAdapter(mMyPostAdapter, true);
                toggleMyPost = 0;
            } else if (toggleMyPost == 0) {
                rvWeeklyChallenge.swapAdapter(mAdapter, true);
                toggleMyPost = 1;
            }
            return true;
        } else if (id == R.id.menu_recent) {
            rvWeeklyChallenge.swapAdapter(mRecentAdapter, true);
            return true;
        } else if (id == R.id.menu_likes) {
            rvWeeklyChallenge.swapAdapter(mAdapter, true);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void sendNotification(final String message, User user, final String id) {
        //String firebase_token = mUserReference.child(user.getUid()).child("token");
        Query query = mUserReference.child(user.getUid()).orderByChild("token");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("token")) {
                    String token = dataSnapshot.child("token").getValue().toString();

                    String to = token; // the notification key
                    AtomicInteger msgId = new AtomicInteger();
                    new Notify(to, message, id).execute();
                    //notifyMessage(to,message);
                    FirebaseMessaging.getInstance().send(new RemoteMessage.Builder(to)
                            .setMessageId(String.valueOf(msgId.get()))
                            .addData("title", "Weekly Challenge")
                            .addData("body", message)
                            .build());
                    Log.e("message", new RemoteMessage.Builder(to).setMessageId(String.valueOf(msgId.get()))
                            .addData("title", "Weekly Challenge")
                            .addData("body", message).toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("notification action", databaseError.getDetails());
            }
        });

    }

}
