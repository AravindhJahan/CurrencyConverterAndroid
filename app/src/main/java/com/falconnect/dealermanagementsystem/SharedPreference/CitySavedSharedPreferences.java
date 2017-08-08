package com.falconnect.dealermanagementsystem.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import java.util.HashMap;

public class CitySavedSharedPreferences {

    public static final String MAP_ADDRESS = "city";

    private static final String PREF_ADDRESS = "MapAddressSave";
    private static final String IS_ADDRESS = "IsMapAddressIn";
    SharedPreferences preferences;
    Editor editor_address;
    Context _context;
    int PRIVATE_MODE = 0;
    HashMap<String, String> user_address;


    public CitySavedSharedPreferences(Context context) {
        this._context = context;
        preferences = _context.getSharedPreferences(PREF_ADDRESS, PRIVATE_MODE);
        editor_address = preferences.edit();
    }

    public void createCitySession(String city) {

        editor_address.putBoolean(IS_ADDRESS, true);

        editor_address.putString(MAP_ADDRESS, city);

        editor_address.commit();
    }

    public HashMap<String, String> getAddress_details() {
        user_address = new HashMap<String, String>();

        user_address.put(MAP_ADDRESS, preferences.getString(MAP_ADDRESS, null));

        Log.e("map_address", MAP_ADDRESS);

        return user_address;
    }

    public void clear_address() {
        editor_address.clear();
        editor_address.commit();
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean(IS_ADDRESS, false);
    }

}
