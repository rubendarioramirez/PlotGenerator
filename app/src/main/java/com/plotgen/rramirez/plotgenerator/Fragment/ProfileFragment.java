package com.plotgen.rramirez.plotgenerator.Fragment;


import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.MainActivity;
import com.plotgen.rramirez.plotgenerator.Model.User;
import com.plotgen.rramirez.plotgenerator.ProjectFragment;
import com.plotgen.rramirez.plotgenerator.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    @BindView(R.id.txtEmailProfile)
    MaterialEditText etEmailProfile;

    @BindView(R.id.txtNamaProfile)
    MaterialEditText etNamaProfile;

    @BindView(R.id.ivProfilePic)
    ImageView ivProfilePic;

    @BindView(R.id.txtProjectsCreated)
    MaterialEditText etProjectsCreated;

    @BindView(R.id.txtCharCreated)
    MaterialEditText etCharCreated;

    @OnClick(R.id.btnSignOut)
    public void onClick(View v) {
        AuthUI.getInstance()
                .signOut(this.getContext())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // user is now signed out
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft .replace(R.id.flMain,new ProjectFragment());
                        ft.commit();

                        Common.isProfileUnlocked = false;
                    }
                });
    }

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity)getActivity()).setActionBarTitle("My Profile");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ButterKnife.bind(this, view);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        Common.currentUser = new User(mUser.getUid(),mUser.getDisplayName(),mUser.getEmail(),mUser.getPhotoUrl());

        etEmailProfile.setText(Common.currentUser.getEmail());
        etNamaProfile.setText(Common.currentUser.getName());

        etEmailProfile.setFocusable(false);
        etNamaProfile.setFocusable(false);
        etProjectsCreated.setFocusable(false);
        etCharCreated.setFocusable(false);

        Glide.with(this.getContext())
                .load(getHiResUrl(Common.currentUser.getPicUrl().toString()))
                .into(ivProfilePic);

        ValueAnimator projectsAnimator = new ValueAnimator();
        projectsAnimator.setObjectValues(0, 99);// here you set the range, from 0 to "count" value
        projectsAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                etProjectsCreated.setText(String.valueOf(animation.getAnimatedValue()));
            }
        });
        projectsAnimator.setDuration(1000); // here you set the duration of the anim
        projectsAnimator.start();

        ValueAnimator charAnimator = new ValueAnimator();
        charAnimator.setObjectValues(0, 99);// here you set the range, from 0 to "count" value
        charAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                etCharCreated.setText(String.valueOf(animation.getAnimatedValue()));
            }
        });
        charAnimator.setDuration(1000); // here you set the duration of the anim
        charAnimator.start();

        return view;
    }

    private String getHiResUrl(String s) {

        // Variable holding the original String portion of the url that will be replaced
        String originalPieceOfUrl = "s96-c/photo.jpg";

        // Variable holding the new String portion of the url that does the replacing, to improve image quality
        String newPieceOfUrlToAdd = "s400-c/photo.jpg";

        // Check if the Url path is null
        if (s != null) {

            // Convert the Url to a String and store into a variable
            String photoPath = s.toString();

            // Replace the original part of the Url with the new part
            String newString = photoPath.replace(originalPieceOfUrl, newPieceOfUrlToAdd);

            return newString;
        }
        else
            return s;
    }

    @GlideModule
    public class MyAppGlideModule extends AppGlideModule {

        @Override
        public void registerComponents(Context context, Glide glide, Registry registry) {
            // Register FirebaseImageLoader to handle StorageReference
            registry.append(StorageReference.class, InputStream.class,
                    new FirebaseImageLoader.Factory());
        }
    }

}
