package com.plotgen.rramirez.plotgenerator.Fragment;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.plotgen.rramirez.plotgenerator.Common.AdsHelper;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Common.Utils;
import com.plotgen.rramirez.plotgenerator.MainActivity;
import com.plotgen.rramirez.plotgenerator.Model.Prompt;
import com.plotgen.rramirez.plotgenerator.Model.User;
import com.plotgen.rramirez.plotgenerator.R;
import com.plotgen.rramirez.plotgenerator.TriggerFragment;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;





/**
 * A simple {@link Fragment} subclass.
 */
public class SubmitTriggerFragment extends Fragment implements RewardedVideoAdListener {


    @BindView(R.id.etTriggerStory)
    EditText etTriggerStory;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    String id = "";

    private FirebaseFirestore mDatabase;
    private CollectionReference mReference;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private RewardedVideoAd mRewardedVideoAd;
    public int can_submit = 0;

    @OnClick(R.id.btnTriggerSubmit)
    public void submitStory(View view) {
        if (Common.currentUser != null) {
            final String s = etTriggerStory.getText().toString();
            mDatabase = FirebaseFirestore.getInstance();
            mUser = Common.currentFirebaseUser;

            // Title is required
            if (TextUtils.isEmpty(s)) {
                etTriggerStory.setError("Required");
                return;
            }

            if(s.length() > 120){
                etTriggerStory.setError(s.length() + "/120. Please use less than 120 characters");
                return;
            }

            if(can_submit == 0){
                if (mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
                    }
                can_submit = 1;
                return;
            }
                    CollectionReference collectionReference = mReference.document("0").collection("special");
                    String key = mReference.document().getId();
                    Long tsLong = System.currentTimeMillis() / 1000;

                    final Prompt prompt = new Prompt(key, etTriggerStory.getText().toString(), tsLong,
                            new User(Common.currentUser.getUid(),
                                    Common.currentUser.getName(),
                                    Common.currentUser.getEmail(),
                                    Common.currentUser.getPicUrl().toString()),
                            false);

                    Map<String, Object> postValues = prompt.toMap();

                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put(key, postValues);
                    collectionReference.document(key).update(childUpdates);

                    collectionReference.document(key).set(postValues).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getContext(), " Prompt Added", Toast.LENGTH_SHORT).show();

                        }
                    });

                    etTriggerStory.setText("");
                    Utils.saveOnSharePreg(getContext(), "can_submit_trigger", 0);

                    //Come back to Triggers
                    TriggerFragment nextFragment = new TriggerFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    Utils.changeFragment(nextFragment, transaction);
                }
        else {
            Toast.makeText(getContext(),"Please login in Profile section", Toast.LENGTH_LONG).show();
        }
    }
    public SubmitTriggerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle("Submit Your Prompt!");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.submit_trigger, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        ButterKnife.bind(this, view);

        mAuth = Common.currentAuth;
        mUser = Common.currentFirebaseUser;
        can_submit = Utils.getSharePref(getContext(), "can_submit_trigger", 0);

        if (!Common.isPAU) {
            //Rewarded ad
            mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getContext());
            mRewardedVideoAd.setRewardedVideoAdListener(this);
            AdsHelper.loadRewardedVideoAd(mRewardedVideoAd, Objects.requireNonNull(getContext()));
        } else {
            Utils.saveOnSharePreg(getContext(), "can_submit_trigger", 1);
            can_submit = 1;
        }


        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                updateUI();
            }
        });

        mDatabase = Common.currentDatabase;
        if (Locale.getDefault().getDisplayLanguage().equals("español")) {
            mReference = FirebaseFirestore.getInstance().collection("triggers_es");
        } else {
            mReference = FirebaseFirestore.getInstance().collection("triggers");
        }

        return view;
    }

    private void updateUI() {
        if (mUser != null) {
            Common.currentUser = new User(mUser.getUid(), mUser.getDisplayName(), mUser.getEmail(), mUser.getPhotoUrl().toString(), mUser.getPhotoUrl());
        }
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
        AdsHelper.loadRewardedVideoAd(mRewardedVideoAd,getContext());
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        //Get the reward
        Utils.saveOnSharePreg(getContext(), "can_submit_trigger", 1);

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
