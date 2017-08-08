package com.falconnect.currencyconverter.Session;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

public class SessionManager {

    public static final String KEY_EURO_AMOUNT = "euro";
    public static final String KEY_USD_AMOUNT = "usd";
    public static final String KEY_JPY_AMOUNT = "jpy";

    private static final String PREF_NAME = "AmountPref";
    private static final String IS_AMOUNT = "IsAmountIn";

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

    public void createAmountDetails(String euro, String usd, String jpy) {
        editor.putBoolean(IS_AMOUNT, true);

        editor.putString(KEY_EURO_AMOUNT, euro);
        editor.putString(KEY_USD_AMOUNT, usd);
        editor.putString(KEY_JPY_AMOUNT, jpy);

        editor.commit();
    }

    public HashMap<String, String> getAmountDetails() {

        user = new HashMap<String, String>();

        user.put(KEY_EURO_AMOUNT, pref.getString(KEY_EURO_AMOUNT, null));
        user.put(KEY_USD_AMOUNT, pref.getString(KEY_USD_AMOUNT, null));
        user.put(KEY_JPY_AMOUNT, pref.getString(KEY_JPY_AMOUNT, null));

        return user;
    }

    public void clear_amount() {
        editor.clear();
        editor.commit();
    }


    public boolean IsAmountIn() {
        return pref.getBoolean(IS_AMOUNT, false);
    }

}
