package com.plotgen.rramirez.plotgenerator.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Model.Story;
import com.plotgen.rramirez.plotgenerator.Model.User;
import com.plotgen.rramirez.plotgenerator.R;
import com.plotgen.rramirez.plotgenerator.Utils;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoryEditFragment extends Fragment {

    @BindView(R.id.etEditStory)
    MaterialEditText etEditStory;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private DatabaseReference mNewStoryReference;

    @OnClick(R.id.btnSaveEdit)
    public void saveEdit(View v)
    {
        final String newStory = etEditStory.getText().toString();

        // Title is required
        if (TextUtils.isEmpty(newStory)) {
            etEditStory.setError("Required");
            return;
        }

        String key = Common.currentStory.getId();
        mNewStoryReference = mReference.child(key);
        mNewStoryReference.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Story s = mutableData.getValue(Story.class);
                if (s == null) {
                    return Transaction.success(mutableData);
                }
                s.setChalenge(newStory);
                // Set value and report transaction success
                mutableData.setValue(s);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                Log.d("UpdateStory", "postTransaction:onComplete:" + databaseError);
            }
        });


        WeeklyChallengeFragment nextFragment = new WeeklyChallengeFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Utils.changeFragment(nextFragment,transaction,"","");
    }


    public StoryEditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_story_edit, container, false);

        ButterKnife.bind(this, view);

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference().child("Weekly_Challenge_Beta").child("posts");

        etEditStory.setText(Common.currentStory.getChalenge());


        return view;
    }

}
