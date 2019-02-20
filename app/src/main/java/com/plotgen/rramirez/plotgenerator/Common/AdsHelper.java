package com.plotgen.rramirez.plotgenerator.Common;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class AdsHelper {


    public static void loadAd(AdView mAdView) {
        if (!Common.isPAU) {
            //Display the ad
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice("E230AE087E1D0E7FB2304943F378CD64")
                    .build();
            mAdView.loadAd(adRequest);
        }
    }
}
