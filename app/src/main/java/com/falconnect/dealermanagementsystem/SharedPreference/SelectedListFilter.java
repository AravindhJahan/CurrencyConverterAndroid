package com.falconnect.dealermanagementsystem.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SelectedListFilter {
    public static final String FAVORITES = "Product_Favorite";
    private static final String PREF_NAME = "FILTERLIST";
    private static final String IS_JSON = "ISSELECTEDLISTIN";
    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    HashMap<String, String> user;
    ArrayList<String> selected_datas = new ArrayList<>();

    public SelectedListFilter(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createSelectedItems(Context context, ArrayList<String> selecte_items) {
        /*editor.putBoolean(IS_JSON, true);

        for (int i = 0; i < selecte_items.size(); i ++){
            selected_datas.add(selecte_items.get(i).toString());
            editor.putString("set" + i, selecte_items.get(i).toString());
        }

        Log.e("selected_list", selecte_items.toString());

        editor.commit();*/

        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(selecte_items);

        editor.putString(FAVORITES, jsonFavorites);

        Log.e("selected_list", jsonFavorites.toString());

        editor.commit();
    }

   /* public HashMap<String, String> getSelectedDatas() {

         StringBuilder sb = new StringBuilder();
        for (int i = 0; i < selected_datas.size(); i++) {
            sb.append(selected_datas.get(i).toString());
        }
        editor.putString("SELECTEDDATASS", sb.toString());

        Log.e("SELECTEDDATASS", selected_datas.toString());

        return user;

     }*/

    public ArrayList<String> getFavorites(Context context) {
        SharedPreferences settings;
        List<String> favorites;

        settings = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            String[] favoriteItems = gson.fromJson(jsonFavorites,
                    String[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<String>(favorites);
        } else
            return null;

        return (ArrayList<String>) favorites;
    }


    public boolean isLoggedIn() {
        return pref.getBoolean(IS_JSON, false);
    }

}
