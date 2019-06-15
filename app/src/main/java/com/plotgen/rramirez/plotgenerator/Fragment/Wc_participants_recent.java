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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Common.Notify;
import com.plotgen.rramirez.plotgenerator.Common.Utils;
import com.plotgen.rramirez.plotgenerator.MainActivity;
import com.plotgen.rramirez.plotgenerator.Model.Story;
import com.plotgen.rramirez.plotgenerator.Model.User;
import com.plotgen.rramirez.plotgenerator.R;
import com.plotgen.rramirez.plotgenerator.ViewHolder.StoryViewHolder;

import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class Wc_participants_recent extends Fragment {

    @BindView(R.id.rvWeeklyChalenge)
    RecyclerView rvWeeklyChallenge;

    FirestoreRecyclerOptions<Story> optionsMostRecent;
    private FirestoreRecyclerAdapter<Story, StoryViewHolder> mRecentAdapter;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private FirebaseFirestore mDatabase_recent;
    private CollectionReference mReference_recent;
    private CollectionReference mCommentReference_recent;
    private CollectionReference mUserReference_recent;

    private LinearLayoutManager mManager;

    public Wc_participants_recent() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle("Weekly Challenge Participant");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weekly_challenge, container, false);

        ButterKnife.bind(this, view);
        mDatabase_recent = FirebaseFirestore.getInstance();

        mDatabase_recent = Common.currentDatabase;
        mAuth = Common.currentAuth;
        mUser = Common.currentFirebaseUser;
        mCommentReference_recent = Common.currentCommentReference;
        mUserReference_recent = Common.currentUserReference;

        com.google.firebase.firestore.Query query_recents = Common.currentQuery.orderBy("date");

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


         mReference_recent = mDatabase_recent.collection(getString(R.string.weekly_challenge_db_name)).document("posts").collection("posts");
        //  DocumentReference  mReference_recent = mDatabase_recent.collection(getString(R.string.weekly_challenge_db_name)).document("posts").collection("posts").document();
//        mCommentReference_recent = mDatabase_recent.getReference().child(getString(R.string.weekly_challenge_db_name)).child("post-comments");
//        mUserReference_recent = mDatabase_recent.getReference().child("users");
//      Query query_recents = mDatabase_recent.getReference().child(getString(R.string.weekly_challenge_db_name)).child("posts").orderByChild("date");
        Query query = mReference_recent.orderBy("date");

        //  Query query1 = mDatabase_recent.collection(getString(R.string.weekly_challenge_db_name)).document("posts").collection("posts").orderBy("date");
//        mReference_recent = Common.currentReference;
//        mCommentReference_recent = Common.currentCommentReference;
//        mUserReference_recent = Common.currentUserReference;
//        Query query_recents = Common.currentQuery.orderByChild("date");
        optionsMostRecent = new FirestoreRecyclerOptions.Builder<Story>()
                .setQuery(query, Story.class)
                .build();

        populateWeeklyChallenge();
        rvWeeklyChallenge.setAdapter(mRecentAdapter);
        mRecentAdapter.notifyDataSetChanged();

        return view;
    }

    private void populateWeeklyChallenge() {

        mRecentAdapter = new FirestoreRecyclerAdapter<Story, StoryViewHolder>(optionsMostRecent) {

            @Override
            public StoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new StoryViewHolder(inflater.inflate(R.layout.item_story, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(StoryViewHolder viewHolder, int position, final Story model) {
                //     final DocumentReference postRef = mRecentAdapter.getRef(position);
                final Story currentStory = model;
                /*viewHolder.tvGenre.setText(model.getGenre());
                viewHolder.tvTitle.setText(model.getTitle());
                viewHolder.ivTemplatePic.setImageResource(R.drawable.typewriter);
                viewHolder.tvUser.setText(model.getUser().getName());
                viewHolder.tvTitle.setText(model.getTitle());
                viewHolder.tvGenre.setText(model.getGenre());
                viewHolder.tvStory.setText(model.getChalenge());
                viewHolder.tvLoves.setText(String.valueOf(model.getLikeCount()));
                Glide.with(viewHolder.itemView.getContext())
                        .load(model.getUser().getUriString())
                        .apply(RequestOptions.circleCropTransform())
                        .into(viewHolder.ivUser);*/
                //  viewHolder.tvComments.setText();

                viewHolder.setIsRecyclable(false);


                //  if (model.getTitle().contains(Common.currentChallenge.getName())) {

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
                        DocumentReference documentReference = mReference_recent.document(model.getId());
                          onLikeClicked(documentReference);
                    }
                };


                mCommentReference_recent = mDatabase_recent.collection(getString(R.string.weekly_challenge_db_name)).document("post-comments").collection("post-comments").document(model.getId()).collection("Comments");

                viewHolder.bindToPost(model, likeClickListener, mCommentReference_recent);
                // } else {
                // viewHolder.removeItem();
                // }

            }

        };
        mRecentAdapter.notifyDataSetChanged();
    }


    private void onLikeClicked(final DocumentReference postRef) {

        final DocumentReference documentReference = postRef.collection("likes").document();

        mDatabase_recent.runTransaction(new com.google.firebase.firestore.Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                Story p = transaction.get(postRef).toObject(Story.class);

                if (p.likes.containsKey(Common.currentUser.getUid())) {
                    // Unstar the post and remove self from stars
                    p.likeCount = p.likeCount - 1;
                    p.likes.remove(Common.currentUser.getUid());
                } else {
                    // Star the post and add self to stars
                    p.likeCount = p.likeCount + 1;
                    p.likes.put(Common.currentUser.getUid(), true);
                    // sendNotification(Common.currentUser.getName() + " liked your post", p.getUser(), p.getId());
                }

                // Set value and report transaction success

                transaction.set(postRef,p);
               // postRef.set(p);
                Common.tempStory = p;
                return null;

            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                Log.e("Failed","failed"+e);
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
        if (mRecentAdapter != null) {
            mRecentAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRecentAdapter != null) {
            mRecentAdapter.stopListening();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    public void sendNotification(final String message, User user, final String id) {
        //String firebase_token = mUserReference.child(user.getUid()).child("token");

        mUserReference_recent.document(user.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    String token = documentSnapshot.getString("token");
                    String to = token; // the notification key
                    AtomicInteger msgId = new AtomicInteger();
                    new Notify(to, message, id, "Weekly Challenge").execute();
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
    }

   /* public void sendNotification(final String message, User user, final String id) {
        //String firebase_token = mUserReference.child(user.getUid()).child("token");
        Query query = mUserReference_recent.child(user.getUid()).orderByChild("token");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("token")) {
                    String token = dataSnapshot.child("token").getValue().toString();

                    String to = token; // the notification key
                    AtomicInteger msgId = new AtomicInteger();
                    new Notify(to, message, id, "Weekly Challenge").execute();
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

    }*/

}
