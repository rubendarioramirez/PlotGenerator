package com.plotgen.rramirez.plotgenerator;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Arrays;


public class GuideWeilandFragment extends Fragment {

    TextView guide_weiland_content_tv;
    private AdView mAdView;

    public GuideWeilandFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.change_arc_title));
        View myFragmentView = inflater.inflate(R.layout.fragment_guide_weiland, container, false);

        mAdView = (AdView) myFragmentView.findViewById(R.id.adView_guide_weiland);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        guide_weiland_content_tv = myFragmentView.findViewById(R.id.guide_weiland_content_tv);

        ArrayList<String> titles = new ArrayList<String>();
        ArrayList<String> descriptions = new ArrayList<String>();

        titles.addAll(Arrays.asList(myFragmentView.getResources().getStringArray(R.array.change_arc_array_titles)));
        descriptions.addAll(Arrays.asList(myFragmentView.getResources().getStringArray(R.array.change_arc_array_desc)));

        StringBuffer sb=new StringBuffer();
        for(int i =0; i<titles.size(); i++){
            sb.append("<b>" + titles.get(i) + "</b><br>");
            sb.append(descriptions.get(i) + "<br><br>");

        }
        guide_weiland_content_tv.setText(Html.fromHtml(sb.toString()));

        return myFragmentView;

    }

}
