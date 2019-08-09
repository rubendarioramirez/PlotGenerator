package com.plotgen.rramirez.plotgenerator.Fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.plotgen.rramirez.plotgenerator.BioChallengesFragment;
import com.plotgen.rramirez.plotgenerator.BioFragment;
import com.plotgen.rramirez.plotgenerator.ChallengeListFragment;
import com.plotgen.rramirez.plotgenerator.CharacterFragment;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Common.Constants;
import com.plotgen.rramirez.plotgenerator.Common.Utils;
import com.plotgen.rramirez.plotgenerator.Common.mySQLiteDBHelper;
import com.plotgen.rramirez.plotgenerator.Guides.GuideListFragment;
import com.plotgen.rramirez.plotgenerator.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;
import static com.plotgen.rramirez.plotgenerator.Common.Constants.REQUEST_INVITE;


public class Container_charbio extends Fragment {


    private SectionsPageAdapter mSectionsPageAdapter;
    private FirebaseAnalytics mFirebaseAnalytics;
    private String fragmentTag = Container_charbio.class.getSimpleName();
    private ViewPager mViewPager;
    @BindView(R.id.fab_add_challenge)
    com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton fabAddChallenge;
    @BindView(R.id.fab_guide)
    com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton fabCheckGuide;
    ArrayList<String> char_description;
    public Container_charbio() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.container_charbio, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        char_description = new ArrayList<String>();

        fabCheckGuide.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GuideListFragment nextFragment = new GuideListFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                boolean fragmentPopped = getFragmentManager().popBackStackImmediate(fragmentTag, 0);
                if (!fragmentPopped && getFragmentManager().findFragmentByTag(fragmentTag) == null) {
                    transaction.addToBackStack(null);
                }
                Utils.changeFragment(nextFragment, transaction);
            }
        });

        fabAddChallenge.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    ChallengeListFragment nextFragment = new ChallengeListFragment();
                    //Make the transaction
                    FragmentTransaction transaction = getFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.slide_up, 0, 0, R.anim.slide_down);
                    boolean fragmentPopped = getFragmentManager().popBackStackImmediate(fragmentTag, 0);
                    if (!fragmentPopped && getFragmentManager().findFragmentByTag(fragmentTag) == null) {
                        transaction.addToBackStack(null);
                    }
                    transaction.replace(R.id.flMain, nextFragment);
                    transaction.commit();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



        return view;


    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSectionsPageAdapter = new SectionsPageAdapter(getChildFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = this.getActivity().findViewById(R.id.container_charbio);
        setupViewPager(mViewPager);

        TabLayout tabLayout = this.getActivity().findViewById(R.id.container_charbio_tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }


    @Override
    public void onResume() {
        super.onResume();
        mSectionsPageAdapter = new SectionsPageAdapter(getChildFragmentManager());


        // Set up the ViewPager with the sections adapter.
        mViewPager = this.getActivity().findViewById(R.id.container_charbio);
        setupViewPager(mViewPager);

        TabLayout tabLayout = this.getActivity().findViewById(R.id.container_charbio_tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }


    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getChildFragmentManager());
        adapter.addFragment(new BioFragment(), getString(R.string.biocontainer_character));
        adapter.addFragment(new BioChallengesFragment(), getString(R.string.biocontainer_challenge));
        viewPager.setAdapter(adapter);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_bio, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_bio_edit) {
            //Send it to the next fragment
            try {
                Common.charCreationMode = false;
                //Send it to the next fragment
                CharacterFragment nextFragment = new CharacterFragment();
                //Make the transaction
                FragmentTransaction transaction = getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.slide_up, 0, 0, R.anim.slide_down);
                transaction.replace(R.id.flMain, nextFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        else if (id == R.id.menu_bio_share){
            try {
                    String bio = Utils.generateBIO(getContext(),Common.currentCharacter.getId());
                    StringBuffer challenges = Utils.generateChallenges(getContext(),Common.currentCharacter.getId());
                    String body = bio + "<BR><BR>" + challenges;
                    SHARE(body, Common.currentCharacter.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public void SHARE(String body, String char_name) {

        // Do something in response to button
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Auctor:" + char_name);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(body));
        startActivity(Intent.createChooser(sharingIntent, "share"));

        //Log challenges updated
        Bundle params = new Bundle();
        params.putString("Share", "completed");
        mFirebaseAnalytics.logEvent("share_completed", params);

    }


    private void onInviteClicked() {
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("matilda", "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                    Log.d("matilda", "onActivityResult: sent invitation " + id);
                }
            } else {
                // Sending failed or it was canceled, show failure message to the user
                // ...
            }
        }
    }

}
