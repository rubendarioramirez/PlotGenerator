package com.plotgen.rramirez.plotgenerator.Fragment;


import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
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
import com.plotgen.rramirez.plotgenerator.Utils;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    @BindView(R.id.loginLayout)
    LinearLayout loginLayout;

    @BindView(R.id.profileLayout)
    LinearLayout profileLayout;

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

    @BindView(R.id.btnSignOut)
    Button btnSignOut;

    @OnClick(R.id.btnSignOut)
    public void signOut(View v) {
        AuthUI.getInstance()
                .signOut(this.getContext())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // user is now signed out
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft .replace(R.id.flMain,new ProjectFragment());
                        ft.commit();

                        Snackbar.make(getActivity().findViewById(R.id.flMain), "Sign Out Success.", Snackbar.LENGTH_SHORT).show();

                    }
                });
    }

    @OnClick(R.id.btnLogin)
    public void login(View v)
    {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.GoogleBuilder().build()))
                        .setIsSmartLockEnabled(false)
                        .build(),
                RC_SIGN_IN);
    }

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private static final int RC_SIGN_IN = 123;

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

        updateUI();

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft .replace(R.id.flMain,new ProfileFragment());
                ft.commit();

                Snackbar.make(getActivity().findViewById(R.id.flMain), "Sign In Success.", Snackbar.LENGTH_SHORT).show();
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Snackbar.make(getActivity().findViewById(R.id.flMain), "Failed, you pressed back.", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Snackbar.make(getActivity().findViewById(R.id.flMain), "Failed, No Internet Connection.", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                Snackbar.make(getActivity().findViewById(R.id.flMain), "Unknown Error.", Snackbar.LENGTH_SHORT).show();
                Log.e(TAG, "Sign-in error: ", response.getError());
            }
        }
    }

    private void updateUI() {
        if(mUser != null) {
            Common.currentUser = new User(mUser.getUid(),mUser.getDisplayName(),mUser.getEmail(),mUser.getPhotoUrl());

            loginLayout.setVisibility(View.INVISIBLE);
            profileLayout.setVisibility(View.VISIBLE);

            etEmailProfile.setText(Common.currentUser.getEmail());
            etNamaProfile.setText(Common.currentUser.getName());

            etEmailProfile.setFocusable(false);
            etNamaProfile.setFocusable(false);
            etProjectsCreated.setFocusable(false);
            etCharCreated.setFocusable(false);

            Glide.with(getActivity().getApplicationContext())
                    .load(getHiResUrl(Common.currentUser.getPicUrl().toString()))
                    .apply(RequestOptions.circleCropTransform())
                    .into(ivProfilePic);

            ValueAnimator projectsAnimator = new ValueAnimator();
            projectsAnimator.setObjectValues(0, getProjectCreated());// here you set the range, from 0 to "count" value
            projectsAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    etProjectsCreated.setText(String.valueOf(animation.getAnimatedValue()));
                }
            });
            projectsAnimator.setDuration(1000); // here you set the duration of the anim
            projectsAnimator.start();

            ValueAnimator charAnimator = new ValueAnimator();
            charAnimator.setObjectValues(0, getCharCreated());// here you set the range, from 0 to "count" value
            charAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    etCharCreated.setText(String.valueOf(animation.getAnimatedValue()));
                }
            });
            charAnimator.setDuration(1000); // here you set the duration of the anim
            charAnimator.start();

            btnSignOut.setVisibility(View.VISIBLE);
        }
        else
        {
            loginLayout.setVisibility(View.VISIBLE);
            profileLayout.setVisibility(View.INVISIBLE);
            btnSignOut.setVisibility(View.INVISIBLE);
        }
    }

    private int getProjectCreated() {
        ArrayList<String> projects = Utils.getProjects_list(getContext());
        return projects.size();
    }

    private int getCharCreated() {
        ArrayList<String> projects = Utils.getProjects_list(getContext());
        ArrayList<String> chars;
        int totalChar = 0;
        if(projects != null && !projects.isEmpty())
            for (int i=0;i<projects.size();i++) {
                if(!projects.get(i).isEmpty()) {
                    chars = Utils.getCharList(getContext(),projects.get(i).substring(2));
                    totalChar = totalChar + chars.size();
                }
            }
        return totalChar;
    }
}
