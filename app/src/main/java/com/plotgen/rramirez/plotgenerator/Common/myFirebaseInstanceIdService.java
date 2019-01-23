package com.plotgen.rramirez.plotgenerator.Common;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.plotgen.rramirez.plotgenerator.Common.Utils;

import static android.widget.Toast.makeText;

public class myFirebaseInstanceIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Utils.saveOnSharePreg(getApplicationContext(),"firebase_token",refreshedToken);
        Log.d("This app", "Refreshed token: " + refreshedToken);
    }
}
