package com.falconnect.dealermanagementsystem.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class JsonValueSharedPreferences {
    public static final String JSON = "json";
    public static final String MAKE = "make";
    public static final String BUDGET = "budget";
    public static final String JSON_STRING = "json_string";
    private static final String PREF_NAME = "JSONVALUES";
    private static final String IS_JSON = "ISJSONIn";

    ArrayList<String> selected_array_list = new ArrayList<>();

    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    HashMap<String, String> user;

    public JsonValueSharedPreferences(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String json, String json_string, String make, String budget) {
        editor.putBoolean(IS_JSON, true);
        editor.putString(JSON, json);
        editor.putString(JSON_STRING, json_string);
        editor.putString(MAKE, make);
        editor.putString(BUDGET, budget);

        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {

        user = new HashMap<String, String>();

        user.put(JSON, pref.getString(JSON, null));
        user.put(JSON_STRING, pref.getString(JSON_STRING, null));
        user.put(MAKE, pref.getString(MAKE, null));
        user.put(BUDGET, pref.getString(BUDGET, null));

        Log.e("json", JSON);
        Log.e("json_string", JSON_STRING);
        Log.e("make", MAKE);
        Log.e("budget", BUDGET);


        return user;
    }

    public void clear_json() {
        editor.clear();
        editor.commit();

    }


}
