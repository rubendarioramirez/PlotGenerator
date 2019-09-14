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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Common.Utils;
import com.plotgen.rramirez.plotgenerator.MainActivity;
import com.plotgen.rramirez.plotgenerator.Model.Prompt;
import com.plotgen.rramirez.plotgenerator.Model.Story;
import com.plotgen.rramirez.plotgenerator.Model.User;
import com.plotgen.rramirez.plotgenerator.R;
import com.plotgen.rramirez.plotgenerator.TriggerFragment;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubmitTriggerFragment extends Fragment {


    @BindView(R.id.etTriggerStory)
    EditText etTriggerStory;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    String id = "";

    private FirebaseFirestore mDatabase;
    private CollectionReference mReference;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

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

            //Come back to Triggers
            TriggerFragment nextFragment = new TriggerFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            Utils.changeFragment(nextFragment, transaction);
        }else {
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

}
