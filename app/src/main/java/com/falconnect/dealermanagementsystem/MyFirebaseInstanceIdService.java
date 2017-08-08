package com.falconnect.dealermanagementsystem;


import android.util.Log;

import com.freshdesk.hotline.Hotline;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = "fcmdemo";

    @Override
    public void onTokenRefresh() {
         String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token:" + refreshedToken);

        sendRegistrationToServer(refreshedToken);
    }

    public void sendRegistrationToServer(String token) {
        Hotline.getInstance(this).updateGcmRegistrationToken(token);
    }
}