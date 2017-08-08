package com.falconnect.dealermanagementsystem.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import java.util.HashMap;

public class Filter_API {
    public static final String JSON = "url";

    private static final String PREF_NAME = "FILTERURL";
    private static final String IS_JSON = "ISFILTERIn";
    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    HashMap<String, String> user;

    public Filter_API(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createURL(String url) {
        editor.putBoolean(IS_JSON, true);
        editor.putString(JSON, url);

        editor.commit();
    }

    public HashMap<String, String> getURL() {

        user = new HashMap<String, String>();
        user.put(JSON, pref.getString(JSON, null));


        Log.e("url", JSON);


        return user;
    }

    public void clear_URL() {
        editor.clear();
        editor.commit();

    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_JSON, false);
    }

}
