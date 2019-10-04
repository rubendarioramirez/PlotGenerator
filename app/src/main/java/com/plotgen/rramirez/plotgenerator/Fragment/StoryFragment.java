package com.plotgen.rramirez.plotgenerator.Fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.plotgen.rramirez.plotgenerator.Common.Common;
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

    private CollectionReference mReference;
    private FirestoreRecyclerOptions<UserStory> options;
    private FirestoreRecyclerAdapter<UserStory, UserStoryViewHolder> mAdapter;
    private CollectionReference mCommentReference;
    private CollectionReference mUserReference;

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

        FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();

        mReference = mDatabase.collection("stories");
        mUserReference = mDatabase.collection("users");

        mCommentReference = mDatabase.document("stories").collection("post-comments");
      //  Query myTopPostsQuery = mDatabase.getReference().child("stories").orderByChild(Common.currentGenre);
        com.google.firebase.firestore.Query myTopPostsQuery = Common.currentQuery.orderBy("stories");

        options = new FirestoreRecyclerOptions.Builder<UserStory>()
                .setQuery(myTopPostsQuery, UserStory.class)
                .build();

        populateStories();
        rvWeeklyChalenge.setAdapter(mAdapter);
        return view;
    }

    private void populateStories() {
        mAdapter = new FirestoreRecyclerAdapter<UserStory, UserStoryViewHolder>(options) {
            @NonNull
            @Override
            public UserStoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new UserStoryViewHolder(inflater.inflate(R.layout.item_story, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull UserStoryViewHolder viewHolder, int position, @NonNull final UserStory model) {
             //   final DatabaseReference postRef = getRef(position);
                final UserStory userStory = model;
                viewHolder.setIsRecyclable(false);
                Log.e("reff model", String.valueOf(mReference.document(userStory.getId() + "  " + model.getGenre())));

                if (model.getGenre() != null) {
                    if (model.getGenre().equals(Common.currentGenre)) {
                        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Common.currentUserStory = model;
                                UserStoryDetailFragment nextFragment = new UserStoryDetailFragment();
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
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
                                DocumentReference globalPostRef = mReference.document(userStory.getId());
                                onLikeClicked(globalPostRef);
                            }
                        };

                        viewHolder.bindToPost(model, likeClickListener, mCommentReference.document(model.getId()));
                    } else {
                        viewHolder.removeItem();
                    }
                } else {
                    viewHolder.removeItem();
                }
            }
        };
    }

    private void onLikeClicked(final DocumentReference postRef) {

       /* postRef.runTransacation(new Transaction.Handler() {
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
        });*/
    }

    public void sendNotification(final String message, User user, final String id) {
      /*  //String firebase_token = mUserReference.child(user.getUid()).child("token");
        Query query = mUserReference.document(user.getUid()).collection("token");
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
        });*/

    }


}


