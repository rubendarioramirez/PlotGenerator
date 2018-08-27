package com.plotgen.rramirez.plotgenerator;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    TextView textDescription, textTitle;
    private AdView mAdView;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).setActionBarTitle("Home");
        View myFragmentView =   inflater.inflate(R.layout.fragment_home, container, false);

        textDescription = myFragmentView.findViewById(R.id.homeDescription);
        //textTitle = myFragmentView.findViewById(R.id.main_title_home);
        mAdView = (AdView) myFragmentView.findViewById(R.id.adView);

        //textTitle.setText("Your writing \ncompanion");
        textDescription.setText("If you love to write, then you're in the right place.\nI made this app because I also love writing as well but I know the struggle of being block, being lost or just losing focus.\nActually, I did this app for me but I believe it can also help you. \nThere are two things at the moment that you may find interesting.\n\nTriggers, whenever you're lost or you don't know how to start or how to continue, check them out. A simple sentence that will let your imagination fly away. And don't worry I'll post new ones weekly.\n\nThe Hero Journey. If you didn't read it, go running to get it! That's Joseph Campbell legacy for writers. However if you did read it and you want a refreshment, or you just want to go to the meat, check out The Hero Journey section, it contains all you have to know for your hero and it's journey. \n\nHave a great writing!");

        //Display the ad
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        return myFragmentView;
    }

}
