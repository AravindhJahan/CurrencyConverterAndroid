package com.falconnect.dealermanagementsystem.SharedPreference;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.falconnect.dealermanagementsystem.LoginActivity;

import java.util.HashMap;

public class ProfileManagerSession {
    public static final String KEY_DEALER_NAME = "dealer_name";
    public static final String KEY_IMAGE = "dealer_img";
    public static final String KEY_MOBILE = "dealer_mobile";
    public static final String KEY_EMAIL = "dealer_email";
    public static final String KEY_VALUE = "key_vale";
    private static final String PREF_NAME = "ProfileManage";
    private static final String IS_LOGIN = "IsProfileManageIn";
    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    HashMap<String, String> user;

    public ProfileManagerSession(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createProfileManageSession(String name, String image, String email, String mobile, String value) {
        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_DEALER_NAME, name);
        editor.putString(KEY_IMAGE, image);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_VALUE, value);
        editor.commit();
    }

    public void checkProfileManage() {
        if (!this.isLoggedIn()) {
            Intent i = new Intent(_context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }
    }

    public HashMap<String, String> getProfileManagerDetails() {

        user = new HashMap<String, String>();
        user.put(KEY_DEALER_NAME, pref.getString(KEY_DEALER_NAME, null));
        user.put(KEY_IMAGE, pref.getString(KEY_IMAGE, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_MOBILE, pref.getString(KEY_MOBILE, null));
        user.put(KEY_VALUE, pref.getString(KEY_VALUE, null));

        Log.e("dealer_name", KEY_DEALER_NAME);
        Log.e("dealer_img", KEY_IMAGE);
        Log.e("dealer_email", KEY_EMAIL);
        Log.e("dealer_mobile", KEY_MOBILE);

        return user;
    }


    public void clear_ProfileManage() {
        editor.clear();
        editor.commit();
    }


    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

}
