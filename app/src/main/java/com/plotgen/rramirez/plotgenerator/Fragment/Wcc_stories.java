package com.plotgen.rramirez.plotgenerator.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Model.User;
import com.plotgen.rramirez.plotgenerator.R;


public class Wcc_stories extends Fragment {

    private SectionsPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;

    public Wcc_stories() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.wcc_stories, container, false);

        FirebaseDatabase mDatabase = Common.currentDatabase;
        FirebaseAuth mAuth = Common.currentAuth;
        final FirebaseUser mUser = Common.currentFirebaseUser;
        DatabaseReference mReference = mDatabase.getReference().child(getString(R.string.weekly_challenge_db_name)).child("posts");
        DatabaseReference mCommentReference = mDatabase.getReference().child(getString(R.string.weekly_challenge_db_name)).child("post-comments");
        DatabaseReference mUserReference = mDatabase.getReference().child("users");
       /* mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mUser != null) {
                    Common.currentUser = new User(mUser.getUid(), mUser.getDisplayName(), mUser.getEmail(), mUser.getPhotoUrl().toString(), mUser.getPhotoUrl());
                }
            }
        });
*/
        Query query = mDatabase.getReference().child(getString(R.string.weekly_challenge_db_name)).child("posts");
        Common.currentQuery = query;
        Common.currentUserReference = mUserReference;
        Common.currentCommentReference = mCommentReference;


        return view;


    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSectionsPageAdapter = new SectionsPageAdapter(getChildFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = this.getActivity().findViewById(R.id.wcc_stories_container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = this.getActivity().findViewById(R.id.wcc_stories_tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }


    @Override
    public void onResume() {
        super.onResume();
        mSectionsPageAdapter = new SectionsPageAdapter(getChildFragmentManager());


        // Set up the ViewPager with the sections adapter.
        mViewPager = this.getActivity().findViewById(R.id.wcc_stories_container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = this.getActivity().findViewById(R.id.wcc_stories_tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }


    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getChildFragmentManager());
        adapter.addFragment(new WeeklyChallengeFragment(), getString(R.string.wc_most_voted));
        adapter.addFragment(new Wc_participants_recent(), getString(R.string.wc_recent));
        adapter.addFragment(new Wc_participants_mystory(), getString(R.string.wc_mystory));
        viewPager.setAdapter(adapter);
    }

}
