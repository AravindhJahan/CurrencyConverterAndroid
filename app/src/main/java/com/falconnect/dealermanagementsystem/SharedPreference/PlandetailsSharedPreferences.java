package com.falconnect.dealermanagementsystem.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import java.util.HashMap;

public class PlandetailsSharedPreferences {
    public static final String PLANNAME = "PlanName";
    public static final String EXPIRED = "PlanTypeAllow";
    private static final String PREF_NAME = "JSONPLANVALUES";
    private static final String IS_NAMED = "ISJSON";

    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    HashMap<String, String> user;

    public PlandetailsSharedPreferences(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String PlanName, String PlanTypeAllow) {
        editor.putBoolean(IS_NAMED, true);

        editor.putString(PLANNAME, PlanName);
        editor.putString(EXPIRED, PlanTypeAllow);

        Log.e("json", PLANNAME);
        Log.e("json_string", EXPIRED);

        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {

        user = new HashMap<String, String>();

        user.put(PLANNAME, pref.getString(PLANNAME, null));
        user.put(EXPIRED, pref.getString(EXPIRED, null));

        Log.e("json", PLANNAME);
        Log.e("json_string", EXPIRED);
        return user;
    }

}
