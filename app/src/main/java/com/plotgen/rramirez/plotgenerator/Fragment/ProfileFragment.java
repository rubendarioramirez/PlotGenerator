package com.plotgen.rramirez.plotgenerator.Fragment;


import android.animation.ValueAnimator;
import androidx.appcompat.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.storage.StorageReference;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.MainActivity;
import com.plotgen.rramirez.plotgenerator.Model.User;
import com.plotgen.rramirez.plotgenerator.ProjectFragment;
import com.plotgen.rramirez.plotgenerator.R;
import com.plotgen.rramirez.plotgenerator.Common.Utils;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements BillingProcessor.IBillingHandler {


    private FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();


    String selection = "";
    String[] themes;

    @BindView(R.id.loginLayout)
    LinearLayout loginLayout;

    @BindView(R.id.profileLayout)
    LinearLayout profileLayout;

    @BindView(R.id.txtEmailProfile)
    MaterialEditText etEmailProfile;

    @BindView(R.id.txtNamaProfile)
    MaterialEditText etNamaProfile;

    @BindView(R.id.txtChoosetheme)
    MaterialEditText etChooseTheme;

    @BindView(R.id.ivProfilePic)
    ImageView ivProfilePic;

    @BindView(R.id.txtProjectsCreated)
    MaterialEditText etProjectsCreated;

    @BindView(R.id.txtCharCreated)
    MaterialEditText etCharCreated;

    @BindView(R.id.notification)
    CheckBox cbNotification;

    @BindView(R.id.btnSignOut)
    Button btnSignOut;

    @BindView(R.id.btnIAP)
    Button btnIAP;
    private String firebase_token;
    private FirebaseFirestore mDatabase;
    private CollectionReference mUserDatabase;

    @OnClick(R.id.btnIAP)
    public void buyIAP(View v) {
        ((MainActivity) getActivity()).bp.purchase(this.getActivity(), getString(R.string.remove_ads_product_id));

        //Log clicked in IAP updated
        Bundle params = new Bundle();
        params.putString("user_email", Common.currentUser.getEmail());
        params.putString("from", "profile");
        mFirebaseAnalytics.logEvent("Click_IAP_Purchase", params);
    }


    @OnClick(R.id.btnSignOut)
    public void signOut(View v) {
        AuthUI.getInstance()
                .signOut(this.getContext())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        mDatabase.collection("users_1").document(Common.currentUser.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {

                                if (documentSnapshot != null && documentSnapshot.exists()) {
                                    Log.d(TAG, "Current data: " + documentSnapshot.getData());
                                } else {
                                    Log.d(TAG, "Current data: null");
                                }

                            }
                        });
                        // user is now signed out
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.flMain, new ProjectFragment());
                        ft.commit();
                        Snackbar.make(getActivity().findViewById(R.id.flMain), "Sign Out Success.", Snackbar.LENGTH_SHORT).show();

                    }
                });
    }

    @OnClick(R.id.btnLogin)
    public void login(View v) {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.GoogleBuilder().build()))
                        .setIsSmartLockEnabled(false)
                        .build(),
                RC_SIGN_IN);
    }

    private FirebaseAnalytics mFirebaseAnalytics;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;


    private static final int RC_SIGN_IN = 123;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle("My Profile");
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ButterKnife.bind(this, view);
        themes = view.getContext().getResources().getStringArray(R.array.themes);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(view.getContext());
        firebase_token = Utils.getStringSharePref((MainActivity) getActivity(), "firebase_token");
        mDatabase = FirebaseFirestore.getInstance();
        mUserDatabase = mDatabase.collection("users");

        //handle notification
        if (Utils.getStringSharePref(getActivity(), "notifications").equalsIgnoreCase("true")) {
            cbNotification.setChecked(true);
            cbNotification.setButtonDrawable(R.drawable.ic_check_box);
        } else {
            cbNotification.setChecked(false);
            cbNotification.setButtonDrawable(R.drawable.ic_uncheck_box);
        }

        //handle notification checkbox click
        cbNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cbNotification.isChecked()) {
                    //cbNotification.setChecked(false);
                    cbNotification.setButtonDrawable(R.drawable.ic_check_box);
                    final DocumentReference documentReference = mDatabase.collection("users_1").document(Common.currentUser.getUid());
                    mDatabase.runTransaction(new com.google.firebase.firestore.Transaction.Function<Void>() {
                        @Nullable
                        @Override
                        public Void apply(@NonNull com.google.firebase.firestore.Transaction transaction) throws FirebaseFirestoreException {
                            DocumentSnapshot snapshot = transaction.get(documentReference);
                          //  snapshot.getData().put("new token",firebase_token);



                            return null;
                        }
                    });

                    Utils.saveOnSharePreg(getActivity(), "notifications", "true");
                } else {
                    final DocumentReference documentReference = mDatabase.collection("users_1").document(Common.currentUser.getUid());
                    mDatabase.runTransaction(new com.google.firebase.firestore.Transaction.Function<Void>() {
                        @Nullable
                        @Override
                        public Void apply(@NonNull com.google.firebase.firestore.Transaction transaction) throws FirebaseFirestoreException {
                            DocumentSnapshot snapshot = transaction.get(documentReference);
                          //  snapshot.getData().put("new token",firebase_token);



                            return null;
                        }
                    });

                    cbNotification.setButtonDrawable(R.drawable.ic_uncheck_box);
                    Utils.saveOnSharePreg(getActivity(), "notifications", "false");
                }
            }
        });


        remoteConfig.setConfigSettings(new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(false)
                .build());

        HashMap<String, Object> defaults = new HashMap<>();
        defaults.put("iap_button_text", "Go no ads!");
        remoteConfig.setDefaults(defaults);
        final Task<Void> fetch = remoteConfig.fetch(0);
        fetch.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                remoteConfig.activateFetched();
                updateIAPText();
            }
        });

        ((MainActivity) getActivity()).bp = new BillingProcessor(getContext(), null, this);
        ((MainActivity) getActivity()).bp.initialize();

        updateUI();


        etChooseTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext())
                            .setTitle("Choose your theme")
                            .setSingleChoiceItems(R.array.themes, -1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    selection = themes[i];
                                }
                            })
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (!selection.equals("")) {
                                        if (selection.equals("Dark Theme")) {
                                            if (Common.isPAU) {
                                                Utils.saveOnSharePreg(getContext(), "selectedTheme", 1);
                                                Bundle params = new Bundle();
                                                params.putString("theme_chosen", "dark_theme");
                                                mFirebaseAnalytics.logEvent("theme_selected", params);
                                                getActivity().recreate();
                                            } else {
                                                displayPayDulu();
                                            }
                                        } else if (selection.equals("Romance Theme")) {
                                            if (Common.isPAU) {
                                                Utils.saveOnSharePreg(getContext(), "selectedTheme", 2);
                                                Bundle params = new Bundle();
                                                params.putString("theme_chosen", "romance_theme");
                                                mFirebaseAnalytics.logEvent("theme_selected", params);
                                                getActivity().recreate();
                                            } else {
                                                displayPayDulu();
                                            }
                                        } else if (selection.equals("Autumn Theme")) {
                                            if (Common.isPAU) {
                                                Utils.saveOnSharePreg(getContext(), "selectedTheme", 3);
                                                Bundle params = new Bundle();
                                                params.putString("theme_chosen", "autumn_theme");
                                                mFirebaseAnalytics.logEvent("theme_selected", params);
                                                getActivity().recreate();
                                            } else {
                                                displayPayDulu();
                                            }

                                        } else if (selection.equals("Light Theme")) {
                                            Utils.saveOnSharePreg(getContext(), "selectedTheme", 0);
                                            Bundle params = new Bundle();
                                            params.putString("theme_chosen", "light_theme");
                                            mFirebaseAnalytics.logEvent("theme_selected", params);
                                            getActivity().recreate();

                                        } else if (selection.equals("Metal Theme")) {
                                            Utils.saveOnSharePreg(getContext(), "selectedTheme", 4);
                                            Bundle params = new Bundle();
                                            params.putString("theme_chosen", "metal_theme");
                                            mFirebaseAnalytics.logEvent("theme_selected", params);
                                            getActivity().recreate();

                                        }else if (selection.equals("Black & White Theme")) {
                                            Utils.saveOnSharePreg(getContext(), "selectedTheme", 5);
                                            Bundle params = new Bundle();
                                            params.putString("theme_chosen", "white_theme");
                                            mFirebaseAnalytics.logEvent("theme_selected", params);
                                            getActivity().recreate();
                                        }
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                    builder.show();
                }
        });


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
        } else
            return s;
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        Toast.makeText(getContext(), "You are now a premium user", Toast.LENGTH_SHORT).show();
        Utils.setSPIAP(this.getContext(), true);
        Common.isPAU = true;

        btnIAP.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {

    }

    @Override
    public void onBillingInitialized() {

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
            final IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {

                mUser = mAuth.getCurrentUser();
                final DocumentReference documentReference = mDatabase.collection("users_1").document(Objects.requireNonNull(mAuth.getUid()));
                mDatabase.runTransaction(new com.google.firebase.firestore.Transaction.Function<Void>() {
                    @Nullable
                    @Override
                    public Void apply(@NonNull com.google.firebase.firestore.Transaction transaction) throws FirebaseFirestoreException {
                        DocumentSnapshot snapshot = transaction.get(documentReference);
                        snapshot.getData().put("new token",firebase_token);



                        return null;
                    }
                });

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.flMain, new ProfileFragment());
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

    private void updateIAPText() {
        String text = (String) remoteConfig.getString("iap_button_text");
        btnIAP.setText(text);
    }


    private void updateUI() {
        if (mUser != null) {
            Common.currentUser = new User(mUser.getUid(), mUser.getDisplayName(), mUser.getEmail(), mUser.getPhotoUrl());

            loginLayout.setVisibility(View.INVISIBLE);
            profileLayout.setVisibility(View.VISIBLE);

            etEmailProfile.setText(Common.currentUser.getEmail());
            etNamaProfile.setText(Common.currentUser.getName());

            etEmailProfile.setFocusable(false);
            etNamaProfile.setFocusable(false);
            etChooseTheme.setFocusable(false);
            int selectedTheme = Utils.getSharePref(getContext(), "selectedTheme", 0);
            if (selectedTheme == 0) {
                etChooseTheme.setText("Light Theme");
            } else if (selectedTheme == 1) {
                etChooseTheme.setText("Dark Theme");
            } else if (selectedTheme == 2) {
                etChooseTheme.setText("Romance Theme");
            } else if (selectedTheme == 3) {
                etChooseTheme.setText("Autumn Theme");
            }else if (selectedTheme == 4) {
                etChooseTheme.setText("Metal Theme");
            }else if (selectedTheme == 5) {
                etChooseTheme.setText("White Theme");
            }


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

            if (!Common.isPAU) {
                btnIAP.setVisibility(View.VISIBLE);
            }
            updateIAPText();
            btnSignOut.setVisibility(View.VISIBLE);
        } else {
            loginLayout.setVisibility(View.VISIBLE);
            profileLayout.setVisibility(View.INVISIBLE);
            btnSignOut.setVisibility(View.INVISIBLE);
            btnIAP.setVisibility(View.INVISIBLE);
        }
    }

    private int getProjectCreated() {
        ArrayList<String> projects = Utils.getProjects_list(getContext());
        return projects.size();
    }

    private int getCharCreated() {
        ArrayList<String> projects_id = new ArrayList<String>();
        ArrayList<String> projects = Utils.getProjects_list(getContext());
        for(int i = 0; i < projects.size(); i++)
        {
            projects_id.add(projects.get(i).split("/")[0]);
        }

        ArrayList<String> chars;
        int totalChar = 0;
        if (projects != null && !projects.isEmpty())
            for (int i = 0; i < projects.size(); i++) {
                if (!projects.get(i).isEmpty()) {
                    chars = Utils.getCharListByID(getContext(), projects_id.get(i));
                    totalChar = totalChar + chars.size();
                }
            }
        return totalChar;
    }

    public void displayPayDulu(){
        Toast.makeText(getContext(), getResources().getString(R.string.themes_disclaimer), Toast.LENGTH_LONG).show();
    }
}
