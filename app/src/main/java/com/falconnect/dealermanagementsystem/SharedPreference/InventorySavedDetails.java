package com.falconnect.dealermanagementsystem.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;


public class InventorySavedDetails {

    public static final String KEY_DEALER_NAME = "name";
    public static final String KEY_DEALER_DEALERSHIPNAME = "dealershipname";
    public static final String KEY_DEALER_EMAIL_ID = "email";
    public static final String KEY_DEALER_PHONE_NUM = "phone_num";
    public static final String KEY_DEALER_DATE = "date";
    public static final String KEY_DEALER_CITY = "city";
    public static final String KEY_DEALER_BRANCH = "branch";
    public static final String KEY_DEALER_IMAGE = "image";
    public static final String KEY_CONTACT_ID = "contactid";



    private static final String PREF_NAME = "InventoryDetails";
    private static final String IS_DETAILS = "IsDetailsIn";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    HashMap<String, String> user;

    public InventorySavedDetails(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createInventoryDetailsSession(String image, String name, String dealershipname, String email, String phone_num, String date, String city, String branch, String contactid) {
        editor.putBoolean(IS_DETAILS, true);
        editor.putString(KEY_DEALER_NAME, name);
        editor.putString(KEY_DEALER_DEALERSHIPNAME, dealershipname);
        editor.putString(KEY_DEALER_EMAIL_ID, email);
        editor.putString(KEY_DEALER_PHONE_NUM, phone_num);
        editor.putString(KEY_DEALER_DATE, date);
        editor.putString(KEY_DEALER_CITY, city);
        editor.putString(KEY_DEALER_BRANCH, branch);
        editor.putString(KEY_DEALER_IMAGE, image);
        editor.putString(KEY_CONTACT_ID, contactid);

        Log.e("name", KEY_DEALER_NAME);
        Log.e("dealershipname", KEY_DEALER_DEALERSHIPNAME);
        Log.e("email", KEY_DEALER_EMAIL_ID);
        Log.e("phone_num", KEY_DEALER_PHONE_NUM);
        Log.e("date", KEY_DEALER_DATE);
        Log.e("city", KEY_DEALER_CITY);
        Log.e("branch", KEY_DEALER_BRANCH);
        Log.e("Image", KEY_DEALER_IMAGE);
        Log.e("contactid", KEY_CONTACT_ID);


        editor.commit();
    }


    public HashMap<String, String> getInventoryDetails() {

        user = new HashMap<String, String>();
        user.put(KEY_DEALER_NAME, pref.getString(KEY_DEALER_NAME, null));
        user.put(KEY_DEALER_DEALERSHIPNAME, pref.getString(KEY_DEALER_DEALERSHIPNAME, null));
        user.put(KEY_DEALER_EMAIL_ID, pref.getString(KEY_DEALER_EMAIL_ID, null));
        user.put(KEY_DEALER_PHONE_NUM, pref.getString(KEY_DEALER_PHONE_NUM, null));
        user.put(KEY_DEALER_DATE, pref.getString(KEY_DEALER_DATE, null));
        user.put(KEY_DEALER_CITY, pref.getString(KEY_DEALER_CITY, null));
        user.put(KEY_DEALER_BRANCH, pref.getString(KEY_DEALER_BRANCH, null));
        user.put(KEY_DEALER_IMAGE, pref.getString(KEY_DEALER_IMAGE, null));
        user.put(KEY_CONTACT_ID, pref.getString(KEY_CONTACT_ID, null));

        return user;
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();


    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_DETAILS, false);
    }

}
