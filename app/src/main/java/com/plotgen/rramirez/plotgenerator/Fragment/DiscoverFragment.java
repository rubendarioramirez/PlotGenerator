package com.plotgen.rramirez.plotgenerator.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Common.Utils;
import com.plotgen.rramirez.plotgenerator.MainActivity;
import com.plotgen.rramirez.plotgenerator.Model.Story;
import com.plotgen.rramirez.plotgenerator.R;
import com.plotgen.rramirez.plotgenerator.ViewHolder.OfflineStoryViewHolder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class DiscoverFragment extends Fragment {

    @BindView(R.id.rv_stories_list)
    RecyclerView rvGenre;

    @BindView(R.id.empty_project_tv)
    TextView emptyGenreTv;

    private InterstitialAd mInterstitialAd;
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
        mDatabase = FirebaseFirestore.getInstance();
        mDatabase = Common.currentDatabase;

        ButterKnife.bind(this, view);

        if (!Common.isPAU) {
            //Init the ads
            MobileAds.initialize(view.getContext(), getString(R.string.ad_account_id));
            //Interstitial
            mInterstitialAd = new InterstitialAd(view.getContext());
            mInterstitialAd.setAdUnitId(getString(R.string.interstitial_plot_gen));
            mInterstitialAd.loadAd(new AdRequest.Builder()
                    .addTestDevice("E230AE087E1D0E7FB2304943F378CD64")
                    .build());
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    // Load the next interstitial.
                    mInterstitialAd.loadAd(new AdRequest.Builder()
                            .addTestDevice("E230AE087E1D0E7FB2304943F378CD64")
                            .build());

                }

            });
        }

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        // rvGenre.setLayoutManager(mManager);
        rvGenre.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mDatabase = Common.currentDatabase;
        mReference = mDatabase.collection("stories");

        mAuth = FirebaseAuth.getInstance();

        mUser = mAuth.getCurrentUser();

        Query myTopPostsQuery = mReference.whereEqualTo("published", true).whereEqualTo("language",Common.currentLanguage).orderBy("date");
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
                final Story story = model;
                String id = mAdapter.getSnapshots().getSnapshot(position).getId();
                DocumentReference documentReference = mReference.document(id);
                holder.setIsRecyclable(false);


                //Make the likes beautiful
                if(Common.currentUser != null){
                    if(model.getLikes().containsKey(Common.currentUser.getEmail())) {
                        holder.ivLoves.setImageResource(R.drawable.ic_love_red);
                    } else {
                        holder.ivLoves.setImageResource(R.drawable.ic_love_outline);
                    }
                } else {
                    holder.ivLoves.setImageResource(R.drawable.ic_love_outline);
                }


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!Common.isPAU) {
                            if (mInterstitialAd.isLoaded()) {
                                    mInterstitialAd.show();
                            } else {
                                Log.d("TAG", "The interstitial wasn't loaded yet.");
                            }
                        }
                        Common.currentStory = model;
                        mReference.document(id).update("viewCount", FieldValue.increment(1));
                        Common.currentDocumentReference = documentReference;

                        UserStoryDetailFragment nextFragment = new UserStoryDetailFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        Utils.changeFragment(nextFragment, transaction);
                        transaction.addToBackStack(null);
                    }
                });
                View.OnClickListener likeClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onLikeClick(documentReference, holder);
                    }
                };
                holder.bindToPost(story,likeClickListener);
            }
        };
        rvGenre.setAdapter(mAdapter);
    }

public void onLikeClick(DocumentReference documentReference1, OfflineStoryViewHolder holder){
        if(Common.currentUser != null){
            mDatabase.runTransaction(new Transaction.Function<Void>() {
                @Nullable
                @Override
                public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                    Story p = transaction.get(documentReference1).toObject(Story.class);

                    if (p.likes.containsKey(Common.currentUser.getEmail())) {
                        // Unstar the post and remove self from stars
                        p.likeCount = p.likeCount - 1;
                        p.likes.remove(Common.currentUser.getEmail());
                    } else {
                        // Star the post and add self to stars
                        p.likeCount = p.likeCount + 1;
                        p.likes.put(Common.currentUser.getEmail(), true);
                    }

                    transaction.set(documentReference1,p);
                    // documentReference.set(p);
                    Common.tempStory = p;
                    return null;

                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (Common.tempStory.likes.containsKey(Common.currentUser.getEmail())) {
                                holder.ivLoves.setImageResource(R.drawable.ic_love_red);
                            } else {
                                holder.ivLoves.setImageResource(R.drawable.ic_love_outline);
                            }
                            holder.tvLoves.setText(String.valueOf(Common.tempStory.getLikeCount()));

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.v("matilda",Common.tempStory.getId());
                    Log.v("matilda",Common.tempStory.getUser().toString());
                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                    Log.e("Failed","like failed"+e);
                }
            });
        } else {
            Toast.makeText(getContext(), getString(R.string.login_first), Toast.LENGTH_LONG).show();
        }
        }

}


