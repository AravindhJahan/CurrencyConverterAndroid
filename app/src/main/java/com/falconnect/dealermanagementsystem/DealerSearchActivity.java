package com.falconnect.dealermanagementsystem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.falconnect.dealermanagementsystem.Adapter.DealerSearchAdapter;
import com.falconnect.dealermanagementsystem.Model.DealerSearchModel;
import com.falconnect.dealermanagementsystem.SharedPreference.CitySavedSharedPreferences;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;


public class DealerSearchActivity extends Activity {

    public ArrayList<HashMap<String, String>> my_posting_list;
    Button car_search, dealer_search;
    EditText search_word;
    ListView dealer_search_listview;
    ImageView back;
    SessionManager sessionManager;
    ProgressDialog barProgressDialog;
    HashMap<String, String> user;
    HashMap<String, String> mypositinglist;
    String queriesurl;
    DealerSearchAdapter dealerSearchAdapter;
    int values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dealer_search);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initialize();

        sessionManager = new SessionManager(DealerSearchActivity.this);
        user = sessionManager.getUserDetails();

        //Car Search
        car_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                values = 0;
                car_search.setBackgroundResource(R.drawable.budget_model);
                dealer_search.setBackgroundResource(R.drawable.by_model);
                car_search.setTextColor(Color.WHITE);
                dealer_search.setTextColor(Color.BLACK);

                search_word.setHint("Search Make/Model/Variant");

            }
        });

        //Dealer Search
        dealer_search.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                values = 1;
                car_search.setBackgroundResource(R.drawable.by_model);
                dealer_search.setBackgroundResource(R.drawable.budget_model);
                car_search.setTextColor(Color.BLACK);
                dealer_search.setTextColor(Color.WHITE);

                search_word.setHint("Search Dealer");
            }
        });

        ////Back Button
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager in = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(search_word.getWindowToken(), 0);

                DealerSearchActivity.this.finish();
            }
        });



            search_word.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    String decode = search_word.getText().toString();

                    try {
                        decode = URLEncoder.encode(decode, "UTF-8");
                    } catch (Exception e) {

                    }

                    if (values == 0) {
                        String url = Constant.SEARCH_TEXTBOX + "session_user_id=" + user.get("user_id")
                                + "&city_name=" + ""
                                + "&search_listing=" + decode
                                + "&sorting_category=" + 0
                                + "&page_name=detail_searchpage";

                        Intent intent = new Intent(DealerSearchActivity.this, SearchResultActivity.class);
                        intent.putExtra("search_word", decode);
                        intent.putExtra("url", url);
                        startActivity(intent);

                        CitySavedSharedPreferences citySavedSharedPreferences = new CitySavedSharedPreferences(DealerSearchActivity.this);
                        citySavedSharedPreferences.createCitySession(null);
                    } else {
                        //Toast.makeText(DealerSearchActivity.this, "Dealer_searasasdas", Toast.LENGTH_SHORT).show();

                        queriesurl = Constant.DEALER_SEARCH
                                + "session_user_id=" + user.get("user_id")
                                + "&page_name=searchdealers"
                                + "&search_dealer=" + decode;

                        new dealer_list().execute();

                    }

                    search_word.clearFocus();
                    InputMethodManager in = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(search_word.getWindowToken(), 0);

                    return true;

                }
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {

        InputMethodManager in = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(search_word.getWindowToken(), 0);

        DealerSearchActivity.this.finish();

    }

    public void initialize() {
        back = (ImageView) findViewById(R.id.nav_back_drawer);

        car_search = (Button) findViewById(R.id.car_search);
        dealer_search = (Button) findViewById(R.id.dealer_search);

        dealer_search_listview = (ListView) findViewById(R.id.dealer_search_listview);

        search_word = (EditText) findViewById(R.id.searchtext_word);
    }

    private ArrayList<DealerSearchModel> getbidsdata() {
        final ArrayList<DealerSearchModel> biddata = new ArrayList<>();
        for (int i = 0; i < my_posting_list.size(); i++) {

            String dealer_name = my_posting_list.get(i).get("dealer_name");
            String dealership_name = my_posting_list.get(i).get("dealership_name");
            String id = my_posting_list.get(i).get("id");
            String logo = my_posting_list.get(i).get("logo");
            String d_mobile = my_posting_list.get(i).get("d_mobile");
            String d_email = my_posting_list.get(i).get("d_email");
            String city = my_posting_list.get(i).get("city");
            String dealercarno = my_posting_list.get(i).get("dealercarno");

            biddata.add(new DealerSearchModel(dealer_name, dealership_name, id, logo, d_mobile, d_email, city, dealercarno));
        }
        return biddata;
    }

    private class dealer_list extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = ProgressDialog.show(DealerSearchActivity.this, "Loading...", "Please Wait ...", true);
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();

            Log.e("queriesurl", queriesurl);

            String json = sh.makeServiceCall(queriesurl, ServiceHandler.POST);

            if (json != null) {

                my_posting_list = new ArrayList<>();

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    JSONArray loan = jsonObj.getJSONArray("dealerdetails");

                    for (int k = 0; k <= loan.length(); k++) {

                        String dealer_name = loan.getJSONObject(k).getString("dealer_name");
                        String dealership_name = loan.getJSONObject(k).getString("dealership_name");
                        String id = loan.getJSONObject(k).getString("id");
                        String logo = loan.getJSONObject(k).getString("logo");
                        String d_mobile = loan.getJSONObject(k).getString("d_mobile");
                        String d_email = loan.getJSONObject(k).getString("d_email");
                        String city = loan.getJSONObject(k).getString("city");
                        String dealercarno = loan.getJSONObject(k).getString("dealercarno");

                        mypositinglist = new HashMap<>();

                        mypositinglist.put("dealer_name", dealer_name);
                        mypositinglist.put("dealership_name", dealership_name);
                        mypositinglist.put("id", id);
                        mypositinglist.put("logo", logo);
                        mypositinglist.put("d_mobile", d_mobile);
                        mypositinglist.put("d_email", d_email);
                        mypositinglist.put("city", city);
                        mypositinglist.put("dealercarno", dealercarno);

                        my_posting_list.add(mypositinglist);

                    }

                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }


            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(), "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            barProgressDialog.dismiss();

            dealerSearchAdapter = new DealerSearchAdapter(DealerSearchActivity.this, getbidsdata());
            dealer_search_listview.setAdapter(dealerSearchAdapter);

        }

    }

}
