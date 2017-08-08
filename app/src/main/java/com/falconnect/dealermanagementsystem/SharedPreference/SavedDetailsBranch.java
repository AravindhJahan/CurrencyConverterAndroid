package com.falconnect.dealermanagementsystem.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import java.util.HashMap;

public class SavedDetailsBranch {
    public static final String BRANCH_NAME = "branch_name";
    public static final String BRANCH_EMAIL = "branch_email";
    public static final String BRANCH_NUMBER = "branch_number";
    private static final String PREF_NAME = "BranchDetails";
    private static final String IS_LOGIN = "IsBranchIn";
    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    HashMap<String, String> user;

    public SavedDetailsBranch(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String branch_name, String branch_number, String branch_email) {
        editor.putBoolean(IS_LOGIN, true);

        editor.putString(BRANCH_NAME, branch_name);
        editor.putString(BRANCH_NUMBER, branch_number);
        editor.putString(BRANCH_EMAIL, branch_email);

        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {

        user = new HashMap<String, String>();

        user.put(BRANCH_NAME, pref.getString(BRANCH_NAME, null));
        user.put(BRANCH_EMAIL, pref.getString(BRANCH_EMAIL, null));
        user.put(BRANCH_NUMBER, pref.getString(BRANCH_NUMBER, null));

        Log.e("dealer_name", BRANCH_NAME);
        Log.e("user_id", BRANCH_EMAIL);
        Log.e("dealer_img", BRANCH_NUMBER);


        return user;
    }

    public void clear_details_brach_Datas() {
        editor.clear();
        editor.commit();

    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

}
