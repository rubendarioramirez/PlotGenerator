package com.plotgen.rramirez.plotgenerator.Fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.plotgen.rramirez.plotgenerator.Common.Common;
import com.plotgen.rramirez.plotgenerator.MainActivity;
import com.plotgen.rramirez.plotgenerator.R;
import com.plotgen.rramirez.plotgenerator.Common.Utils;

import java.util.HashMap;


public class PremiumFragment extends Fragment implements BillingProcessor.IBillingHandler{

    public PremiumFragment() {
        // Required empty public constructor
    }

    private FirebaseRemoteConfig remoteConfig_premium;
    private FirebaseAnalytics mFirebaseAnalytics;
    TextView title_tv, body_tv;
    Button premium_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_premium, container, false);

        title_tv = view.findViewById(R.id.premium_title_tv);
        body_tv = view.findViewById(R.id.premium_body_tv);
        premium_btn = view.findViewById(R.id.premium_btn);
        remoteConfig_premium = FirebaseRemoteConfig.getInstance();

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true)
                .build();
        remoteConfig_premium.setConfigSettings(configSettings);

        HashMap<String, Object> defaults = new HashMap<>();
        defaults.put("premium_text_title", "We're the premiums!");
        defaults.put("premium_body_text", "La la la pun");
        remoteConfig_premium.setDefaults(defaults);

        //IAP Fields
        ((MainActivity) getActivity()).bp = new BillingProcessor(getContext(), null, this);
        ((MainActivity) getActivity()).bp.initialize();

        premium_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buyIAP(view);
            }
        });

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        fetchTexts();
    }

    private void fetchTexts(){

        long cacheExpiration = 3600; // 1 hour in seconds.
        // If your app is using developer mode, cacheExpiration is set to 0, so each fetch will
        // retrieve values from the service.
        if (remoteConfig_premium.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }
        remoteConfig_premium.fetch(0)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // After config data is successfully fetched, it must be activated before newly fetched
                            // values are returned.
                            remoteConfig_premium.activateFetched();
                            updateTexts();
                        } else {

                            Log.v("matilda", task.getException().toString());
                        }
                        updateTexts();
                    }
                });

    }

    private void updateTexts() {
        String title_text = (String) remoteConfig_premium.getString("premium_text_title");
        String body_text = (String) remoteConfig_premium.getString("premium_body_text");
        String button_text = (String) remoteConfig_premium.getString("premium_text_btn");
        int button_bg_color = Color.parseColor(remoteConfig_premium.getString("button_bg_color"));
        int button_text_color = Color.parseColor(remoteConfig_premium.getString("button_text_color"));


        title_tv.setText(title_text);
        body_tv.setText(body_text);
        premium_btn.setText(button_text);
        premium_btn.setBackgroundColor(button_bg_color);
        premium_btn.setTextColor(button_text_color);
    }

    public void buyIAP(View v) {
        ((MainActivity) getActivity()).bp.purchase(this.getActivity(), getString(R.string.remove_ads_product_id));
        //Log clicked in IAP updated
//        Bundle params = new Bundle();
//        params.putString("user_email", Common.currentUser.getEmail());
//        params.putString("from", "premium section");
//        mFirebaseAnalytics.logEvent("Click_IAP_Purchase", params);

        Log.v("matilda", "this was clicked");
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @android.support.annotation.Nullable TransactionDetails details) {
        Toast.makeText(getContext(), "You are now a premium user", Toast.LENGTH_SHORT).show();
        Utils.setSPIAP(this.getContext(), true);
        Common.isPAU = true;
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @android.support.annotation.Nullable Throwable error) {

    }

    @Override
    public void onBillingInitialized() {

    }



}
