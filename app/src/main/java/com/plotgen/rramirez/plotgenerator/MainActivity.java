package com.plotgen.rramirez.plotgenerator;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.AdRequest;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.Random;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Fragment.ProfileFragment;
import com.plotgen.rramirez.plotgenerator.Fragment.SubmitStoryFragment;
import com.plotgen.rramirez.plotgenerator.Fragment.WeeklyChallengeFragment;
import com.plotgen.rramirez.plotgenerator.Model.User;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private InterstitialAd mInterstitialAd;
    private FirebaseAnalytics mFirebaseAnalytics;

    NavigationView navigationView;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Launch HOME first
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft .replace(R.id.flMain,new ProjectFragment());
//        ft .replace(R.id.flMain,new HomeFragment());
        ft.commit();
        //Set home as selected
        navigationView.setCheckedItem(R.id.nav_char);


        //Notification manager if device is OREO or below
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel =
                    new NotificationChannel(Constants.CHANNEL_ID,Constants.CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);

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

    public void updateUI() {
        if(mUser != null)
            Common.currentUser = new User(mUser.getUid(),mUser.getDisplayName(),mUser.getEmail(),mUser.getPhotoUrl());
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    public void setActionBarTitle(String title){
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
        if (mInterstitialAd.isLoaded()) {
            if(inter_chance <=5) {
                mInterstitialAd.show();
            }
        }
        else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
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
            ft .replace(R.id.flMain,new HeroJourneyFragment());
            mFirebaseAnalytics.setCurrentScreen(this, ft.getClass().getSimpleName(), ft.getClass().getSimpleName());
            ft.commit();
        } else if (id == R.id.nav_trigger) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft .replace(R.id.flMain,new TriggerFragment());
            mFirebaseAnalytics.setCurrentScreen(this, ft.getClass().getSimpleName(), ft.getClass().getSimpleName());
            ft.commit();
        }
        else if (id == R.id.nav_char) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft .replace(R.id.flMain,new ProjectFragment());
            mFirebaseAnalytics.setCurrentScreen(this, ft.getClass().getSimpleName(), ft.getClass().getSimpleName());
            ft.commit();
        }
        else if (id == R.id.nav_writting_challenge) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft .replace(R.id.flMain,new weeklyWriting());
            mFirebaseAnalytics.setCurrentScreen(this, ft.getClass().getSimpleName(), ft.getClass().getSimpleName());
            ft.commit();
        }
        else if(id == R.id.nav_profile) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flMain, new ProfileFragment());
            mFirebaseAnalytics.setCurrentScreen(this, ft.getClass().getSimpleName(), ft.getClass().getSimpleName());
            ft.commit();
            navigationView.setCheckedItem(R.id.nav_profile);
        }
        else if(id == R.id.nav_submit)
        {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flMain, new SubmitStoryFragment());
            mFirebaseAnalytics.setCurrentScreen(this, ft.getClass().getSimpleName(), ft.getClass().getSimpleName());
            ft.commit();
        }
        else if(id == R.id.nav_weekly_challenge)
        {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flMain, new WeeklyChallengeFragment());
            mFirebaseAnalytics.setCurrentScreen(this, ft.getClass().getSimpleName(), ft.getClass().getSimpleName());
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    //    @Override
//    public void onRewarded(RewardItem rewardItem) {
//        Utils.saveOnSharePreg(getApplicationContext(),"weekly_challenge_visit",rewardItem.getAmount());
//    }

}
