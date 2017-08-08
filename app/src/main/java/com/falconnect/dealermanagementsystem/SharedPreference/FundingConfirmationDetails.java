package com.falconnect.dealermanagementsystem.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;


public class FundingConfirmationDetails {

    private static final String PREF_NAME = "ConfirmationDetails";
    private static final String IS_DETAILS = "IsConfirmationDetailsIn";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    String json1, json2, json3, json;
    int PRIVATE_MODE = 0;

    public FundingConfirmationDetails(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createConfirmationDetails(ArrayList<String> car_id, ArrayList<String> car_name, ArrayList<String> car_image, ArrayList<String> car_price) {
        editor.putBoolean(IS_DETAILS, true);

        Gson gson = new Gson();

        json = gson.toJson(car_id);
        editor.putString("CAR_ID", json);

        json1 = gson.toJson(car_name);
        editor.putString("CAR_NAME", json1);

        json2 = gson.toJson(car_image);
        editor.putString("CAR_IMAGE", json2);

        json3 = gson.toJson(car_price);
        editor.putString("CAR_PRICE", json3);


        Log.e("name", json);
        Log.e("name", json1);
        Log.e("name", json2);
        Log.e("name", json2);

        editor.commit();
    }


    public void confirmationDetailscleat() {
        editor.clear();
        editor.commit();


    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_DETAILS, false);
    }

}
