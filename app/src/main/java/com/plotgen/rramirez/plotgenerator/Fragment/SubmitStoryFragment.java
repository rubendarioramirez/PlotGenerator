package com.plotgen.rramirez.plotgenerator.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @OnClick(R.id.btnSubmit)
    public void submitStory(View view) {
        final String s = etStory.getText().toString();

        // Title is required
        if (TextUtils.isEmpty(s)) {
            etStory.setError("Required");
            return;
        }

        String key = mReference.child("posts").push().getKey();
        Long tsLong = System.currentTimeMillis() / 1000;


        Story story = new Story(key, etTitle.getText().toString(),
                "",
                etStory.getText().toString(), tsLong,
                new User(Common.currentUser.getUid(),
                        Common.currentUser.getName(),
                        Common.currentUser.getEmail(),
                        Common.currentUser.getPicUrl().toString()));

        Map<String, Object> postValues = story.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);

        mReference.updateChildren(childUpdates);

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
        mReference = mDatabase.getReference().child(getString(R.string.weekly_challenge_db_name));

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
