package com.falconnect.dealermanagementsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;
import com.freshdesk.hotline.Hotline;
import com.freshdesk.hotline.HotlineConfig;
import com.freshdesk.hotline.HotlineUser;

import java.util.HashMap;

public class SplashScreen extends Activity {

    String token = "";
    public static final String HOTLINE_APP_ID = "76d60240-e80b-40ec-8f59-e27c29a2c9e8";
    public static final String HOTLINE_APP_KEY = "d5fe0c9e-13d9-472c-87eb-2ea909047520";
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;

    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        HotlineConfig hlConfig = new HotlineConfig(HOTLINE_APP_ID, HOTLINE_APP_KEY);
        Hotline.getInstance(getApplicationContext()).init(hlConfig);


        // Update user info with Hotline
        HotlineUser users = Hotline.getInstance(this).getUser();
        users.setName("Guest");
        users.setEmail("user_" + System.currentTimeMillis() + "@users.users");
        Hotline.getInstance(this).updateUser(users);


        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();

        if (user.get("user_id") == null) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    //open activity
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);
                    // activity finish
                    SplashScreen.this.finish();
                }
            }, SPLASH_TIME_OUT);
        }
        else {
            //open activity
            Intent i = new Intent(SplashScreen.this, MainDashBoardActivity.class);
            startActivity(i);
            SplashScreen.this.finish();
        }
    }


}
