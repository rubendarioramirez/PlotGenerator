package com.plotgen.rramirez.plotgenerator.Common;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.plotgen.rramirez.plotgenerator.R;

public class AdsHelper {


    public static void loadAd(AdView mAdView) {
        if (!Common.isPAU) {
            //Display the ad
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice("DF4C0FAF4578BA24E48A081749037E54")
                    .build();
            mAdView.loadAd(adRequest);
        }
    }

    public static void loadRewardedVideoAd(RewardedVideoAd mRewardedVideoAd, Context mContext) {
        mRewardedVideoAd.loadAd(mContext.getString(R.string.reward_ad_plot_gen),
                new AdRequest.Builder()
                        .addTestDevice("DF4C0FAF4578BA24E48A081749037E54")
                        .build());
    }

}
