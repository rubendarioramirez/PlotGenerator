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
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;

import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Common.Notify;
import com.plotgen.rramirez.plotgenerator.MainActivity;
import com.plotgen.rramirez.plotgenerator.Model.Story;
import com.plotgen.rramirez.plotgenerator.Model.User;
import com.plotgen.rramirez.plotgenerator.R;
import com.plotgen.rramirez.plotgenerator.Common.Utils;
import com.plotgen.rramirez.plotgenerator.ViewHolder.StoryViewHolder;

import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class Wc_participants_mystory extends Fragment {

    @BindView(R.id.rvWeeklyChalenge)
    RecyclerView rvWeeklyChallenge;

    //private FirebaseRecyclerAdapter<Story, StoryViewHolder> mMyPostAdapter;
    private FirestoreRecyclerAdapter<Story, StoryViewHolder> mMyPostAdapter;
    //FirebaseRecyclerOptions<Story> optionsMyPost;
    FirestoreRecyclerOptions optionsMyPost;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    //private FirebaseDatabase mDatabase;

    FirebaseFirestore mDatabase;

    //private DatabaseReference mReference;
    private CollectionReference mReference;
    //private DatabaseReference mCommentReference;
    CollectionReference mCommentReference;
    //private DatabaseReference mUserReference;
    CollectionReference mUserReference;

    private LinearLayoutManager mManager;

    public Wc_participants_mystory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle("Weekly Challenge Participant");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weekly_challenge, container, false);

        ButterKnife.bind(this, view);

        mDatabase = Common.currentDatabase;
        mAuth = Common.currentAuth;
        mUser = Common.currentFirebaseUser;
        mCommentReference = Common.currentCommentReference;
        mUserReference = Common.currentUserReference;


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


        //mReference = mDatabase.getReference().child(getString(R.string.weekly_challenge_db_name)).child("posts");
        mReference = mDatabase.collection(getString(R.string.weekly_challenge_db_name))
                .document("posts").collection("posts");

        //Below are old comments
//        mCommentReference = mDatabase.getReference().child(getString(R.string.weekly_challenge_db_name)).child("post-comments");
//        mUserReference = mDatabase.getReference().child("users");

//        Query query = mDatabase.getReference().child(getString(R.string.weekly_challenge_db_name)).child("posts").orderByChild("likeCount");


//        mReference = Common.currentReference;
//        mCommentReference = Common.currentCommentReference;
//        mUserReference = Common.currentUserReference;
//        Query query = Common.currentQuery.orderByChild("date");
        // old comments ended here
        Query query = mReference;

        optionsMyPost = new FirestoreRecyclerOptions.Builder<Story>()
                .setQuery(query, Story.class)
                .build();



        populateWeeklyChallenge();
        rvWeeklyChallenge.setAdapter(mMyPostAdapter);

        return view;
    }

    private void populateWeeklyChallenge() {

       /* mMyPostAdapter = new FirebaseRecyclerAdapter<Story, StoryViewHolder>(optionsMyPost) {

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
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            Utils.changeFragment(nextFragment, transaction);
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
        };*/
        mMyPostAdapter = new FirestoreRecyclerAdapter<Story, StoryViewHolder>(optionsMyPost) {
            @Override
            protected void onBindViewHolder(@NonNull StoryViewHolder viewHolder, int position, @NonNull final Story model) {
                //final DatabaseReference postRef = getRef(position);
                final Story currentStory = model;

                viewHolder.setIsRecyclable(false);

                if (model.getUser().getUid().equals(Common.currentUser.getUid())) {

                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Common.currentStory = currentStory;
                            StoryDetailFragment nextFragment = new StoryDetailFragment();
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            Utils.changeFragment(nextFragment, transaction);
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
                            DocumentReference globalPostRef = mReference.document(model.getId());
                            onLikeClicked(globalPostRef);
                        }
                    };
                    mCommentReference = mDatabase.collection(getString(R.string.weekly_challenge_db_name)).document("post-comments").collection("post-comments").document(model.getId()).collection("Comments");

                    viewHolder.bindToPost(model, likeClickListener, mCommentReference);
                } else {
                    viewHolder.removeItem();
                }

            }

            @NonNull
            @Override
            public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                return new StoryViewHolder(inflater.inflate(R.layout.item_story, parent, false));
            }
        };
    }


    private void onLikeClicked(final DocumentReference postRef) {

    }
    /*private void onLikeClicked(final DatabaseReference postRef) {

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
    }*/

    private void updateUI() {
        if (mUser != null) {
            Common.currentUser = new User(mUser.getUid(), mUser.getDisplayName(), mUser.getEmail(), mUser.getPhotoUrl().toString(), mUser.getPhotoUrl());
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        if (mMyPostAdapter != null) {
            mMyPostAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mMyPostAdapter != null) {
            mMyPostAdapter.stopListening();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    public void sendNotification(final String message, User user, final String id) {
        //String firebase_token = mUserReference.child(user.getUid()).child("token");

        mUserReference.document(user.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()){
                    String token = documentSnapshot.getString("token");
                    String to = token; // the notification key
                    AtomicInteger msgId = new AtomicInteger();
                    new Notify(to, message, id,"Weekly Challenge").execute();
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
        });

       /* Query query = mUserReference.child(user.getUid()).orderByChild("token");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("token")) {
                    String token = dataSnapshot.child("token").getValue().toString();

                    String to = token; // the notification key
                    AtomicInteger msgId = new AtomicInteger();
                    new Notify(to, message, id,"Weekly Challenge").execute();
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
        });*/

    }

}
