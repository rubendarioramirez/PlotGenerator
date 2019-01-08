package com.plotgen.rramirez.plotgenerator;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

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
