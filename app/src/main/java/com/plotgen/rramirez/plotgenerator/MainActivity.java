package com.plotgen.rramirez.plotgenerator;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdLoadListener;
import com.applovin.sdk.AppLovinAdSize;
import com.applovin.sdk.AppLovinSdk;
import com.bumptech.glide.util.Util;
import com.google.android.ads.mediationtestsuite.MediationTestSuite;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.integration.IntegrationHelper;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Common.Constants;
import com.plotgen.rramirez.plotgenerator.Common.Utils;
import com.plotgen.rramirez.plotgenerator.Fragment.DiscoverFragment;
import com.plotgen.rramirez.plotgenerator.Fragment.PremiumFragment;
import com.plotgen.rramirez.plotgenerator.Fragment.ProfileFragment;
import com.plotgen.rramirez.plotgenerator.Fragment.StoryDetailFragment;
import com.plotgen.rramirez.plotgenerator.Fragment.UserStoryDetailFragment;
import com.plotgen.rramirez.plotgenerator.Fragment.weekly_challenge_container;
import com.plotgen.rramirez.plotgenerator.Model.Story;
import com.plotgen.rramirez.plotgenerator.Model.User;
import com.plotgen.rramirez.plotgenerator.Model.UserStory;

import java.util.HashMap;
import java.util.Locale;
import java.util.Random;



public class MainActivity extends AppCompatActivity
        implements BillingProcessor.IBillingHandler, NavigationView.OnNavigationItemSelectedListener {

    static boolean calledAlready = false;
    public BillingProcessor bp;
    NavigationView navigationView;
    private InterstitialAd mInterstitialAd;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mUserDatabase;
    private String firebase_token;
    private FirebaseAuth mAuth;
    private boolean firstTime;
    private FirebaseRemoteConfig remoteConfig_main;
    private AppLovinAd loadedAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppLovinSdk.initializeSdk(this.getApplicationContext());
        IronSource.init(this, "92412865");

        //IntegrationHelper.validateIntegration(this);

        //MediationTestSuite.launch(MainActivity.this, "ca-app-pub-6696437403163667~6953226633");



        // Load an Interstitial Ad
        AppLovinSdk.getInstance( this ).getAdService().loadNextAd( AppLovinAdSize.INTERSTITIAL, new AppLovinAdLoadListener()
        {
            @Override
            public void adReceived(AppLovinAd ad)
            {
                loadedAd = ad;
            }

            @Override
            public void failedToReceiveAd(int errorCode)
            {
                // Look at AppLovinErrorCodes.java for list of error codes.
            }
        } );



        Common.isPAU = Utils.getSPIAP(this);
        firstTime = Utils.checkFirstTime(getApplicationContext());
        if (firstTime) {
            remoteConfig_main = FirebaseRemoteConfig.getInstance();
            FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                    .setDeveloperModeEnabled(false)
                    .build();
            remoteConfig_main.setConfigSettings(configSettings);

            HashMap<String, Object> defaults = new HashMap<>();
            defaults.put("default_theme", "4");
            remoteConfig_main.setDefaults(defaults);
            fetchTheme();
        } else {
            updateThemes(firstTime);
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Set all databases
        mAuth = FirebaseAuth.getInstance();
        Common.currentAuth = mAuth;

        mUser = mAuth.getCurrentUser();
        Common.currentFirebaseUser = mUser;



        if (!calledAlready) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
            calledAlready = true;
        }
        mDatabase = FirebaseDatabase.getInstance();
        mUserDatabase = mDatabase.getReference().child("users");
        Common.currentDatabase = mDatabase;



        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        FirebaseApp.initializeApp(this);


        DocumentReference mDocRef;
        if(mUser != null){
            String email = mUser.getEmail();
            mDocRef = FirebaseFirestore.getInstance().document("premium_users/" + email);
            mDocRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot.exists()) {
                        Boolean premium = documentSnapshot.getBoolean("premium");
                        if(premium)
                            Common.isPAU = true;
                        Log.v("matilda", "Im here and pau is " + Common.isPAU);
                    }
                }
            });
        }




        if (!Common.isPAU) {
            //Init the ads
            MobileAds.initialize(this, getString(R.string.ad_account_id));

            //Interstitial
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId(getString(R.string.interstitial_plot_gen));
            mInterstitialAd.loadAd(new AdRequest.Builder()
                    .addTestDevice("E230AE087E1D0E7FB2304943F378CD64")
                    .build());
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    // Load the next interstitial.
                    mInterstitialAd.loadAd(new AdRequest.Builder()
                            .addTestDevice("E230AE087E1D0E7FB2304943F378CD64")
                            .build());

                }

            });
        }

        if (FirebaseInstanceId.getInstance() != null) {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(MainActivity.this, new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    if (instanceIdResult != null) {
                        firebase_token = instanceIdResult.getToken();
                        if (!firebase_token.equals(""))
                            Utils.saveOnSharePreg(getApplicationContext(), "firebase_token", firebase_token);
                        Log.d("This app", "Refreshed token: " + firebase_token);
                    }
                }
            });
        }

        // Obtain the FirebaseAnalytics instance.
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (Common.isPAU)
            hideItem();




        //handle notification on post like and comment
        if (Utils.getStringSharePref(getApplicationContext(), "notifications").equalsIgnoreCase("")) {
            Utils.saveOnSharePreg(getApplicationContext(), "notifications", "true");
        }


        //if (Utils.getStringSharePref(getApplicationContext(), "notifications").equalsIgnoreCase("true")) {
        if (getIntent() != null && getIntent().getStringExtra("tag") != null && getIntent().getStringExtra("tag").equalsIgnoreCase("post")) {
            String id = getIntent().getStringExtra("post_id");
            String title = getIntent().getStringExtra("title");
            if (id == null) {
                id = getIntent().getStringExtra("id");
            }
            Log.e("post_id", id);

            if (id != null) {
                if (title.equalsIgnoreCase("Weekly Challenge")) {
                    Log.v("story fragment", id);
                    openStoryFragment(id);
                } else if (title.equalsIgnoreCase("Stories")) {
                    openUserStoryFragment(id);
                }
            } else {
                openHomeFragment();
            }
        } else {
            openHomeFragment();
        }


        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData data) {
                        if (data == null) {
                            Log.d("invite_link", "getInvitation: no data");
                            return;
                        }
                        // Get the deep link
                        Uri deepLink = data.getLink();
                        String id = deepLink.getQueryParameter("id");
                        Log.v("story fragment", id + " " + deepLink);
                        openStoryFragment(id);
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("invite_link", "getDynamicLink:onFailure", e);
                    }
                });


        //Notification manager if device is OREO or below
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel =
                    new NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);

            mChannel.setDescription(Constants.CHANNEL_DESCRIPTION);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);

            mNotificationManager.createNotificationChannel(mChannel);

        }

        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                updateUI();
            }
        });

        updateUI();

        System.gc();

    }



    private void openUserStoryFragment(final String id) {
        final DatabaseReference mPostReference = mDatabase.getReference().child("stories");
        Query query = mPostReference.child(id);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getKey().equals(id)) {
                    String photoUrl = null;
                    if (mUser.getPhotoUrl() != null)
                        photoUrl = mUser.getPhotoUrl().toString();
                    Common.currentUser = new User(mUser.getUid(), mUser.getDisplayName(), mUser.getEmail(), photoUrl, mUser.getPhotoUrl());
                    Common.currentUserStory = dataSnapshot.getValue(UserStory.class);
                    UserStoryDetailFragment nextFragment = new UserStoryDetailFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    Utils.changeFragment(nextFragment, transaction);
                    getSupportFragmentManager().popBackStack();
//                    navigationView.setCheckedItem(R.id.nav_discover);

                } else {
                    openHomeFragment();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("error", databaseError.getDetails());
            }
        });
    }

    private void openStoryFragment(final String id) {
        final DatabaseReference mPostReference = mDatabase.getReference().child(getString(R.string.weekly_challenge_db_name)).child("posts");
        Query query = mPostReference.child(id).orderByChild("id");
        Log.e("story fragments", id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getKey().equals(id)) {
                    String photoUrl = null;
                    if (mUser.getPhotoUrl() != null)
                        photoUrl = mUser.getPhotoUrl().toString();
                    Common.currentUser = new User(mUser.getUid(), mUser.getDisplayName(), mUser.getEmail(), photoUrl, mUser.getPhotoUrl());
                    Common.currentStory = dataSnapshot.getValue(Story.class);
                    Log.e("story fragments", dataSnapshot.getKey());
                    StoryDetailFragment nextFragment = new StoryDetailFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    Utils.changeFragment(nextFragment, transaction);
                    //getSupportFragmentManager().popBackStack();
                    navigationView.setCheckedItem(R.id.nav_writting_challenge);

                } else {
                    openHomeFragment();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("error", databaseError.getDetails());
            }
        });
    }

    public void openHomeFragment() {
        //Launch HOME first
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flMain, new ProjectFragment());
        getSupportFragmentManager().popBackStack();
//        ft .replace(R.id.flMain,new HomeFragment());
        ft.commit();
        //Set home as selected
        navigationView.setCheckedItem(R.id.nav_char);
    }

    public void updateUI() {
        if (mUser != null) {
            String photoUrl = null;
            if (mUser.getPhotoUrl() != null)
                photoUrl = mUser.getPhotoUrl().toString();
            Common.currentUser = new User(mUser.getUid(), mUser.getDisplayName(), mUser.getEmail(), photoUrl, mUser.getPhotoUrl());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onBackPressed() {
        Random r = new Random();
        int inter_chance = r.nextInt(30);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        if (!Common.isPAU) {
            if (mInterstitialAd.isLoaded()) {
                if (inter_chance <= 5) {
                    mInterstitialAd.show();
                }
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_discover) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flMain, new DiscoverFragment());
            mFirebaseAnalytics.setCurrentScreen(this, ft.getClass().getSimpleName(), ft.getClass().getSimpleName());
            ft.commit();
        }
        if (id == R.id.nav_trigger) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flMain, new TriggerFragment());
            mFirebaseAnalytics.setCurrentScreen(this, ft.getClass().getSimpleName(), ft.getClass().getSimpleName());
            ft.commit();
        } else if (id == R.id.nav_char) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flMain, new ProjectFragment());
            getSupportFragmentManager().popBackStack();
            mFirebaseAnalytics.setCurrentScreen(this, ft.getClass().getSimpleName(), ft.getClass().getSimpleName());
            ft.commit();
        } else if (id == R.id.nav_writting_challenge) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flMain, new weekly_challenge_container());
            mFirebaseAnalytics.setCurrentScreen(this, ft.getClass().getSimpleName(), ft.getClass().getSimpleName());
            ft.commit();
        } else if (id == R.id.premium) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flMain, new PremiumFragment());
            mFirebaseAnalytics.setCurrentScreen(this, ft.getClass().getSimpleName(), ft.getClass().getSimpleName());
            ft.commit();
        } else if (id == R.id.nav_profile) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flMain, new ProfileFragment());
            mFirebaseAnalytics.setCurrentScreen(this, ft.getClass().getSimpleName(), ft.getClass().getSimpleName());
            ft.commit();
            navigationView.setCheckedItem(R.id.nav_profile);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        updateUI();
        if (bp != null) {
            bp.handleActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        Toast.makeText(this, "You are a premium user now", Toast.LENGTH_SHORT).show();
        Utils.setSPIAP(this, true);
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {
        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBillingInitialized() {

    }

    @Override
    public void onDestroy() {
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
    }


    private void fetchTheme(){

        long cacheExpiration = 3600; // 1 hour in seconds.
        // If your app is using developer mode, cacheExpiration is set to 0, so each fetch will
        // retrieve values from the service.
        if (remoteConfig_main.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }
        remoteConfig_main.fetch(cacheExpiration)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // After config data is successfully fetched, it must be activated before newly fetched
                            // values are returned.
                            remoteConfig_main.activateFetched();
                            updateThemes(firstTime);
                        } else {
                            Log.v("matilda", task.getException().toString());
                        }
                    }
                });

    }


    private void updateThemes(boolean firstTime) {
        int selectedTheme;
        if(firstTime) {
            selectedTheme = (int) remoteConfig_main.getLong("default_theme");
            Utils.saveOnSharePreg(getApplicationContext(), "selectedTheme", selectedTheme);
            MainActivity.this.recreate();
            Log.v("matilda", "value is " + selectedTheme);
        } else {
            selectedTheme = Utils.getSharePref(getApplicationContext(), "selectedTheme", 0);
            Log.v("matilda", "Theme selected is " + selectedTheme);
        }

        if (selectedTheme == 0) {
            setTheme(R.style.AppTheme);
        } else if (selectedTheme == 1) {
            setTheme(R.style.DarkTheme);
        } else if (selectedTheme == 2) {
            setTheme(R.style.OpaqueTheme);
        } else if (selectedTheme == 3) {
            setTheme(R.style.AutumnTheme);
        } else if (selectedTheme == 4) {
            setTheme(R.style.MetalTheme);
        }else if (selectedTheme == 5) {
            setTheme(R.style.whiteTheme);
        }

    }

    private void hideItem()
    {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.premium).setVisible(false);
    }

    protected void onResume() {
        super.onResume();
        IronSource.onResume(this);
    }
    protected void onPause() {
        super.onPause();
        IronSource.onPause(this);
    }

}
