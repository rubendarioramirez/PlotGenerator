package com.plotgen.rramirez.plotgenerator.Guides;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.MainActivity;
import com.plotgen.rramirez.plotgenerator.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class GuideLajosFragment extends Fragment {

    TextView guide_lajos_content_tv;
    private AdView mAdView;


    public GuideLajosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.lajos_character_title));
        View myFragmentView = inflater.inflate(R.layout.fragment_guide_lajos, container, false);

        if(!Common.isPAU) {
            mAdView = (AdView) myFragmentView.findViewById(R.id.adView_guide_lajos);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }


        guide_lajos_content_tv = myFragmentView.findViewById(R.id.guide_lajos_content_tv);
        guide_lajos_content_tv.setText(getString(R.string.lajos_text));

        return myFragmentView;
    }

}
