package com.plotgen.rramirez.plotgenerator.Fragment;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.MainActivity;
import com.plotgen.rramirez.plotgenerator.Model.Story;
import com.plotgen.rramirez.plotgenerator.Model.User;
import com.plotgen.rramirez.plotgenerator.R;
import com.plotgen.rramirez.plotgenerator.Common.Utils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubmitStoryFragment extends Fragment {


    @BindView(R.id.tvEmail)
    TextView tvEmail;

    @BindView(R.id.etTitle)
    EditText etTitle;
    @BindView(R.id.etStory)
    EditText etStory;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    String id = "";

    private FirebaseFirestore mDatabase;
    private CollectionReference mReference;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @OnClick(R.id.btnSubmit)
    public void submitStory(View view) {
        final String s = etStory.getText().toString();
        mDatabase = FirebaseFirestore.getInstance();
        mUser = Common.currentFirebaseUser;

        // Title is required
        if (TextUtils.isEmpty(s)) {
            etStory.setError("Required");
            return;
        }

        //CollectionReference collectionReference = mReference.document(Common.currentWeeklyStoryTitle).collection("posts");
        CollectionReference collectionReference = mReference;
        String key = mReference.document().getId();
        Long tsLong = System.currentTimeMillis() / 1000;

        final Story story = new Story(key, etTitle.getText().toString(),etStory.getText().toString(),
                "",
                etStory.getText().toString(), tsLong,
                new User(Common.currentUser.getUid(),
                        Common.currentUser.getName(),
                        Common.currentUser.getEmail(),
                        Common.currentUser.getPicUrl().toString()),0, false);

        Map<String, Object> postValues = story.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put( key, postValues);
       collectionReference.document(key).update(childUpdates);

       collectionReference.document(key).set(postValues).addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {
               Toast.makeText(getContext(), " Story Added", Toast.LENGTH_SHORT).show();

           }
       });

        etStory.setText("");


        //Make sure that on return to Weekly writing user has to watch an ad again.
        Utils.saveOnSharePreg(getContext(), "can_submit", 0);

        weekly_challenge_container nextFragment = new weekly_challenge_container();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Utils.changeFragment(nextFragment, transaction);
    }


    public SubmitStoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle("Submit Your Challenge!");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_submit_story, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        ButterKnife.bind(this, view);

        mAuth = Common.currentAuth;
        mUser = Common.currentFirebaseUser;

        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                updateUI();
            }
        });

        mDatabase = Common.currentDatabase;
        mReference = mDatabase.collection(getString(R.string.weekly_challenge_db_name)).document(Common.currentWeeklyStoryTitle).collection("posts");

        if (Common.currentChallenge != null) {
            etTitle.setText(Common.currentChallenge.getName());
            etTitle.setFocusable(false);
        }

        return view;
    }

    private void updateUI() {
        if (mUser != null) {
            Common.currentUser = new User(mUser.getUid(), mUser.getDisplayName(), mUser.getEmail(), mUser.getPhotoUrl().toString(), mUser.getPhotoUrl());
            tvEmail.setText(mUser.getDisplayName());
        }
    }

}
