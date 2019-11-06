package com.plotgen.rramirez.plotgenerator.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
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
import com.plotgen.rramirez.plotgenerator.BioChallengesFragment;
import com.plotgen.rramirez.plotgenerator.BioFragment;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.Common.Utils;
import com.plotgen.rramirez.plotgenerator.Guides.GuideListFragment;
import com.plotgen.rramirez.plotgenerator.R;
import com.plotgen.rramirez.plotgenerator.TimelineFragment;

import java.util.ArrayList;

import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;
import static com.plotgen.rramirez.plotgenerator.Common.Constants.REQUEST_INVITE;


public class Container_charbio extends Fragment {


    private SectionsPageAdapter mSectionsPageAdapter;
    private FirebaseAnalytics mFirebaseAnalytics;
    private String fragmentTag = Container_charbio.class.getSimpleName();
    private ViewPager mViewPager;

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
        adapter.addFragment(new TimelineFragment(), getString(R.string.add_timeline));
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
                //Send it to the next fragment
                GuideListFragment nextFragment = new GuideListFragment();
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
                if(Common.isPAU)
                {
                    String bio = Utils.generateBIO(getContext(),Common.currentCharacter.getId());
                    StringBuffer challenges = Utils.generateChallengesDeprecated(getContext(),Common.currentCharacter.getId());
                    String body = bio + "<BR><BR>" + challenges;
                    SHARE(body, Common.currentCharacter.getName());
                } else
                {
                    Toast.makeText(getContext(),getResources().getString(R.string.premiumOnly), Toast.LENGTH_LONG).show();
                }
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
