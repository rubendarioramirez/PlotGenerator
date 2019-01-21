package com.plotgen.rramirez.plotgenerator;

import android.app.NotificationChannel;
import android.app.NotificationManager;
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
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Fragment.ProfileFragment;
import com.plotgen.rramirez.plotgenerator.Fragment.StoryDetailFragment;
import com.plotgen.rramirez.plotgenerator.Fragment.weekly_challenge_container;
import com.plotgen.rramirez.plotgenerator.Model.Story;
import com.plotgen.rramirez.plotgenerator.Model.User;

import java.util.Random;


public class MainActivity extends AppCompatActivity
        implements BillingProcessor.IBillingHandler, NavigationView.OnNavigationItemSelectedListener {

    private InterstitialAd mInterstitialAd;
    private FirebaseAnalytics mFirebaseAnalytics;

    NavigationView navigationView;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    public BillingProcessor bp;
    private FirebaseDatabase mDatabase;
    private String firebase_token;
    private String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int selectedTheme = Utils.getSharePref(getApplicationContext(), "selectedTheme", 0);
        if (selectedTheme == 0) {
            setTheme(R.style.AppTheme);
        } else if (selectedTheme == 1) {
            setTheme(R.style.DarkTheme);
        } else if (selectedTheme == 2) {
            setTheme(R.style.OpaqueTheme);
        } else if (selectedTheme == 3) {
            setTheme(R.style.AutumnTheme);
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Common.isPAU = Utils.getSPIAP(this);
        FirebaseApp.initializeApp(this);

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

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        if (FirebaseInstanceId.getInstance() != null) {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(MainActivity.this, new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    if (instanceIdResult != null) {
                        firebase_token = instanceIdResult.getToken();
                        if (firebase_token != null && !firebase_token.equals(""))
                            Utils.saveOnSharePreg(getApplicationContext(), "firebase_token", firebase_token);
                        Log.d("This app", "Refreshed token: " + firebase_token);
                    }
                }
            });
        }

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference mUserDatabase = mDatabase.getReference().child("users");

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


        //handle notification on post like and comment
        if (Utils.getStringSharePref(getApplicationContext(), "notifications").equalsIgnoreCase("")) {
            Utils.saveOnSharePreg(getApplicationContext(), "notifications", "true");
        }


        //if (Utils.getStringSharePref(getApplicationContext(), "notifications").equalsIgnoreCase("true")) {
        if (getIntent() != null && getIntent().getStringExtra("tag") != null && getIntent().getStringExtra("tag").equalsIgnoreCase("post")) {
            id = getIntent().getStringExtra("post_id");
            if (id == null) {
                id = getIntent().getStringExtra("id");
            }
            if (id != null) {
                openStoryFragment(id);
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

    }

    private void openStoryFragment(final String id) {
        final DatabaseReference mPostReference = mDatabase.getReference().child(getString(R.string.weekly_challenge_db_name)).child("posts");
        Query query = mPostReference.child(id).orderByChild("id");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getKey().equals(id)) {
                    String photoUrl = null;
                    if (mUser.getPhotoUrl() != null)
                        photoUrl = mUser.getPhotoUrl().toString();
                    Common.currentUser = new User(mUser.getUid(), mUser.getDisplayName(), mUser.getEmail(), photoUrl, mUser.getPhotoUrl());
                    Common.currentStory = dataSnapshot.getValue(Story.class);
                    StoryDetailFragment nextFragment = new StoryDetailFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    Utils.changeFragment(nextFragment, transaction, "", "");
                    getSupportFragmentManager().popBackStack();
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
        int inter_chance = r.nextInt(20);
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
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_genre) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flMain, new HeroJourneyFragment());
            mFirebaseAnalytics.setCurrentScreen(this, ft.getClass().getSimpleName(), ft.getClass().getSimpleName());
            ft.commit();
        } else if (id == R.id.nav_trigger) {
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
//            ft.replace(R.id.flMain, new weeklyWriting());
            ft.replace(R.id.flMain, new weekly_challenge_container());
            mFirebaseAnalytics.setCurrentScreen(this, ft.getClass().getSimpleName(), ft.getClass().getSimpleName());
            ft.commit();
        }
//        else if (id == R.id.discover) {
//                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                ft.replace(R.id.flMain, new discoverFragment());
//                mFirebaseAnalytics.setCurrentScreen(this, ft.getClass().getSimpleName(), ft.getClass().getSimpleName());
//                ft.commit();
//        }
         else if (id == R.id.nav_profile) {
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

}
