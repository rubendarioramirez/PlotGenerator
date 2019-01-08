package com.plotgen.rramirez.plotgenerator;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Fragment.SubmitStoryFragment;
import com.plotgen.rramirez.plotgenerator.Fragment.WeeklyChallengeFragment;
import com.plotgen.rramirez.plotgenerator.Model.Challenge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class weeklyWriting extends Fragment implements RewardedVideoAdListener {

    private TextView title, description, ad_desc;
    private Button ad_submit_btn;
    private Button btViewParticipant;
    private String databaseToUse;
    ArrayList data_list;
    private FirebaseAnalytics mFirebaseAnalytics;
    public int can_submit = 0;
    private RewardedVideoAd mRewardedVideoAd;

    private static final int RC_SIGN_IN = 123;
    private static final int RC_SIGN_IN_View_Participant = 124;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;


    public weeklyWriting() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.writing_challenge_tab));
        // Inflate the layout for this fragment
        View myFragmentView = inflater.inflate(R.layout.fragment_weekly_writing, container, false);


        if (!Common.isPAU) {
            //Rewarded ad
            mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(myFragmentView.getContext());
            mRewardedVideoAd.setRewardedVideoAdListener(this);
            loadRewardedVideoAd();
        } else
            can_submit = 1;


        ad_desc = myFragmentView.findViewById(R.id.weekly_challenge_ad_desc);
        ad_submit_btn = myFragmentView.findViewById(R.id.weekly_challenge_btn);
        btViewParticipant = myFragmentView.findViewById(R.id.weekly_challenge_list_btn);

        ad_submit_btn.setVisibility(View.INVISIBLE);
        btViewParticipant.setVisibility(View.INVISIBLE);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(myFragmentView.getContext());
        //Log challenges updated
        Bundle params = new Bundle();
        params.putString("WeeklyWriting", "opened");
        mFirebaseAnalytics.logEvent("weekly_writing", params);


        //Define elements
        title = myFragmentView.findViewById(R.id.writing_challenge_title);
        description = myFragmentView.findViewById(R.id.writing_challenge_desc);

        //Get a firebase reference
        //Get device lang
        if (Locale.getDefault().getDisplayLanguage().equals("español")) {
            databaseToUse = "writing_challenge_es";
        } else {
            databaseToUse = "writing_challenge";
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(databaseToUse);
        data_list = new ArrayList();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnap : dataSnapshot.getChildren()) {
                    data_list.add(childSnap.getValue(String.class));

                }
                title.setText(data_list.get(0).toString());
                description.setText(data_list.get(1).toString());
                String s = data_list.get(0).toString();
                Challenge challenge = new Challenge("", s);
                Common.currentChallenge = challenge;
                ad_submit_btn.setVisibility(View.VISIBLE);
                btViewParticipant.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        //Check if should watch ad or can submit
        if (can_submit == 1) {
            ad_submit_btn.setText(getString(R.string.weekly_challenge_submit_btn));
            ad_desc.setText("");
        } else {
            ad_submit_btn.setText(getString(R.string.weekly_challenge_view_ad_btn));
            ad_desc.setText(getString(R.string.weekly_challenge_view_ad_desc));
        }

        ad_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (can_submit == 1) {
                    /*Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto","ramirez.ruben.dario10@gmail.com", null));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT,
                            title.getText().toString() + " - " + getString(R.string.writing_challenge_tab)
                            );
                    emailIntent.putExtra(Intent.EXTRA_TEXT, description.getText().toString()
                            + "\n\n"
                            + getString(R.string.weekly_challenge_email_desc)

                        );
                    startActivity(Intent.createChooser(emailIntent, "Send your story by email to me..."));
                    */
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build()))
                                    .setIsSmartLockEnabled(false)
                                    .build(),
                            RC_SIGN_IN);
                } else {
                    if (mRewardedVideoAd.isLoaded()) {
                        mRewardedVideoAd.show();
                    }
                }
            }

        });

        btViewParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(Arrays.asList(
                                        new AuthUI.IdpConfig.GoogleBuilder().build()))
                                .setIsSmartLockEnabled(false)
                                .build(),
                        RC_SIGN_IN_View_Participant);
            }
        });


        return myFragmentView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                mDatabase = FirebaseDatabase.getInstance();
                mAuth = FirebaseAuth.getInstance();

                final DatabaseReference mUserDatabase = mDatabase.getReference().child("users");
                mUser = mAuth.getCurrentUser();
                final String firebase_token = Utils.getSharePref((MainActivity) getActivity(), "firebase_token");


                if (mUser != null) {
                    mUserDatabase.runTransaction(new Transaction.Handler() {
                        @NonNull
                        @Override
                        public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                            if (mutableData.hasChild(mUser.getUid())) {
                                mutableData.child(mUser.getUid()).child("token").setValue(firebase_token);
                            } else {
                                mutableData.child(mUser.getUid());
                                mutableData.child(mUser.getUid()).setValue(mUser.getUid());
                                mutableData.child(mUser.getUid()).child("token").setValue(firebase_token);
                            }
                            return Transaction.success(mutableData);
                        }

                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                            Log.d("Updated token", "postTransaction:onComplete:" + databaseError);

                        }
                    });
                }
                //
                // change to submit fragment in main activity
                SubmitStoryFragment nextFragment = new SubmitStoryFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Utils.changeFragment(nextFragment, transaction, "", "");
                transaction.addToBackStack(null);
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(getContext(), "You cancelled", Toast.LENGTH_LONG).show();
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(getContext(), "Unknown error, please try again", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Sign-in error: ", response.getError());
            }
        } else if (requestCode == RC_SIGN_IN_View_Participant) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                //
                // change to submit fragment in main activity
                WeeklyChallengeFragment nextFragment = new WeeklyChallengeFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Utils.changeFragment(nextFragment, transaction, "", "");
                transaction.addToBackStack(null);
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(getContext(), "You cancelled", Toast.LENGTH_LONG).show();
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(getContext(), "Unknown error, please try again", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Sign-in error: ", response.getError());
            }
        }
    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd(getString(R.string.reward_ad_plot_gen),
                new AdRequest.Builder()
                        .addTestDevice("E230AE087E1D0E7FB2304943F378CD64")
                        .build());
    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        loadRewardedVideoAd();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        //Get the reward
        can_submit = 1;
        ad_submit_btn.setText(getString(R.string.weekly_challenge_submit_btn));
        ad_desc.setText("");
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }
}
