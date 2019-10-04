package com.plotgen.rramirez.plotgenerator.Guides;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.MainActivity;
import com.plotgen.rramirez.plotgenerator.R;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class AntagonistFragment extends Fragment {

    TextView guide_antagonist_content_tv;
    private AdView mAdView;

    public AntagonistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.antagonist_guide_title));
        View myFragmentView = inflater.inflate(R.layout.fragment_guide_antagonist, container, false);

        if(!Common.isPAU) {
            mAdView = (AdView) myFragmentView.findViewById(R.id.adView_guide_antagonist);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }

        guide_antagonist_content_tv = myFragmentView.findViewById(R.id.guide_antagonist_content_tv);

        ArrayList<String> titles = new ArrayList<String>();
        ArrayList<String> descriptions = new ArrayList<String>();

        titles.addAll(Arrays.asList(myFragmentView.getResources().getStringArray(R.array.antagonist_guide_array_titles)));
        descriptions.addAll(Arrays.asList(myFragmentView.getResources().getStringArray(R.array.antagonist_guide_array_desc)));

        StringBuffer sb=new StringBuffer();
        for(int i =0; i<titles.size(); i++){
            sb.append("<b>" + titles.get(i) + "</b><br>");
            sb.append(descriptions.get(i) + "<br><br>");

        }
        guide_antagonist_content_tv.setText(Html.fromHtml(sb.toString()));


        return myFragmentView;
    }

}
