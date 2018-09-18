package com.plotgen.rramirez.plotgenerator;

import android.os.Bundle;
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
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.AdRequest;
import com.google.firebase.analytics.FirebaseAnalytics;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private InterstitialAd mInterstitialAd;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        //Init the ads
        MobileAds.initialize(this, "ca-app-pub-6696437403163667~6953226633");

        //Interstitial
        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId("ca-app-pub-6696437403163667/3046586854");
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_plot_gen));

        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Launch HOME first
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft .replace(R.id.flMain,new ProjectFragment());
        ft.commit();
        //Set home as selected
        navigationView.setCheckedItem(R.id.nav_char);
    }

    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
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

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_home) {
//            // Handle the home action
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.addToBackStack(null);
//            ft .replace(R.id.flMain,new HomeFragment());
//            mFirebaseAnalytics.setCurrentScreen(this, ft.getClass().getSimpleName(), ft.getClass().getSimpleName());
//            ft.commit();
//
//        } else
            if (id == R.id.nav_genre) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft .replace(R.id.flMain,new HeroJourneyFragment());
            ft.addToBackStack(null);
            mFirebaseAnalytics.setCurrentScreen(this, ft.getClass().getSimpleName(), ft.getClass().getSimpleName());
            ft.commit();
        } else if (id == R.id.nav_trigger) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft .replace(R.id.flMain,new TriggerFragment());
            ft.addToBackStack(null);
            mFirebaseAnalytics.setCurrentScreen(this, ft.getClass().getSimpleName(), ft.getClass().getSimpleName());
            ft.commit();
        }
        else if (id == R.id.nav_char) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft .replace(R.id.flMain,new ProjectFragment());
            ft.addToBackStack(null);
            mFirebaseAnalytics.setCurrentScreen(this, ft.getClass().getSimpleName(), ft.getClass().getSimpleName());
            ft.commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
