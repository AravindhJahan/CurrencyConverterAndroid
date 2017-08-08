package com.falconnect.dealermanagementsystem.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import java.util.HashMap;

public class SavedDetailsContact {
    public static final String CONTACT_TYPE = "contact_type";
    public static final String CONTACT_TYPE_ID = "contact_type_id";
    public static final String CONTACT_MAIN_NAME = "contact_main_name";
    public static final String CONTACT_NAME = "contact_name";
    public static final String CONTACT_EMAIL = "contact_email";
    public static final String CONTACT_NUMBER = "contact_number";
    public static final String CONTACT_IMAGE = "contact_image";
    public static final String CONTACT_PAN_NUMBER = "contact_pan_number";
    private static final String PREF_NAME = "ContactDetails";
    private static final String IS_LOGIN = "IsContactIn";
    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    HashMap<String, String> user;

    public SavedDetailsContact(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String type, String id, String main_name, String contact_name, String contact_number, String contact_email, String image, String pan_number) {
        editor.putBoolean(IS_LOGIN, true);

        editor.putString(CONTACT_TYPE, type);
        editor.putString(CONTACT_TYPE_ID, id);
        editor.putString(CONTACT_MAIN_NAME, main_name);
        editor.putString(CONTACT_NAME, contact_name);
        editor.putString(CONTACT_EMAIL, contact_email);
        editor.putString(CONTACT_NUMBER, contact_number);
        editor.putString(CONTACT_IMAGE, image);
        editor.putString(CONTACT_PAN_NUMBER, pan_number);

        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {

        user = new HashMap<String, String>();

        user.put(CONTACT_TYPE, pref.getString(CONTACT_TYPE, null));
        user.put(CONTACT_TYPE_ID, pref.getString(CONTACT_TYPE_ID, null));
        user.put(CONTACT_MAIN_NAME, pref.getString(CONTACT_MAIN_NAME, null));
        user.put(CONTACT_NAME, pref.getString(CONTACT_NAME, null));
        user.put(CONTACT_EMAIL, pref.getString(CONTACT_EMAIL, null));
        user.put(CONTACT_NUMBER, pref.getString(CONTACT_NUMBER, null));
        user.put(CONTACT_IMAGE, pref.getString(CONTACT_IMAGE, null));
        user.put(CONTACT_PAN_NUMBER, pref.getString(CONTACT_PAN_NUMBER, null));


        Log.e("username", CONTACT_NAME);

        return user;
    }

    public void clear_contact_datas() {
        editor.clear();
        editor.commit();

    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

}
