package com.falconnect.dealermanagementsystem.SharedPreference;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.falconnect.dealermanagementsystem.LoginActivity;

import java.util.HashMap;

public class SessionManager {
    public static final String KEY_DEALER_NAME = "dealer_name";
    public static final String KEY_ID = "user_id";
    public static final String KEY_IMAGE = "dealer_img";
    public static final String KEY_ADDRESS = "dealer_address";
    public static final String KEY_MOBILE = "dealer_mobile";
    public static final String KEY_EMAIL = "dealer_email";
    public static final String KEY_CITY = "city";
    public static final String KEY_PINCODE = "pincode";
    public static final String KEY_PARENTID = "parentid";
    public static final String KEY_DEALERSHIPNAME = "dealershipname";
    private static final String PREF_NAME = "AndroidHivePref";
    private static final String IS_LOGIN = "IsLoggedIn";
    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    HashMap<String, String> user;

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String name, String id, String image, String address, String email, String mobile
                                    ,String city, String pincode, String parentid, String dealershipname) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_DEALER_NAME, name);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_IMAGE, image);
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_CITY, city);
        editor.putString(KEY_PINCODE, pincode);
        editor.putString(KEY_PARENTID, parentid);
        editor.putString(KEY_DEALERSHIPNAME, dealershipname);
        editor.commit();
    }

    public void checkLogin() {
        if (!this.isLoggedIn()) {
            Intent i = new Intent(_context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }
    }

    public HashMap<String, String> getUserDetails() {

        user = new HashMap<String, String>();
        user.put(KEY_DEALER_NAME, pref.getString(KEY_DEALER_NAME, null));
        user.put(KEY_ID, pref.getString(KEY_ID, null));
        user.put(KEY_IMAGE, pref.getString(KEY_IMAGE, null));
        user.put(KEY_ADDRESS, pref.getString(KEY_ADDRESS, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_MOBILE, pref.getString(KEY_MOBILE, null));
        user.put(KEY_CITY, pref.getString(KEY_CITY, null));
        user.put(KEY_PINCODE, pref.getString(KEY_PINCODE, null));
        user.put(KEY_PARENTID, pref.getString(KEY_PARENTID, null));
        user.put(KEY_DEALERSHIPNAME, pref.getString(KEY_DEALERSHIPNAME, null));

        Log.e("dealer_name", KEY_DEALER_NAME);
        Log.e("user_id", KEY_ID);
        Log.e("dealer_img", KEY_IMAGE);
        Log.e("dealer_address", KEY_ADDRESS);
        Log.e("dealer_email", KEY_EMAIL);
        Log.e("dealer_mobile", KEY_MOBILE);
        Log.e("city", KEY_CITY);
        Log.e("pincode", KEY_PINCODE);
        Log.e("parentid", KEY_PARENTID);
        Log.e("dealershipname", KEY_DEALERSHIPNAME);

        return user;
    }

    public void clear_user() {
        editor.clear();
        editor.commit();

    }

    public void logoutUser() {
        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

}
