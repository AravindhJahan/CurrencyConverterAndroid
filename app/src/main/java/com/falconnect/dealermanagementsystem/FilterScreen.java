package com.falconnect.dealermanagementsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.falconnect.dealermanagementsystem.Adapter.FilterListAdapter;
import com.falconnect.dealermanagementsystem.SharedPreference.Filter_API;
import com.falconnect.dealermanagementsystem.SharedPreference.JsonValueSharedPreferences;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class FilterScreen extends Activity {

    public RelativeLayout back;
    public ListView filert_listview;
    public ArrayList<String> filter_detail_list;
    public ArrayList<HashMap<String, String>> city_search_list;
    Filter_API filter_api;
    HashMap<String, String> filter_datas;
    SessionManager sessionManager;
    HashMap<String, String> user_datas;
    ArrayList<String> filter_titles = new ArrayList<>();
    ArrayList<String> filter_sites = new ArrayList<>();
    FilterListAdapter filterListAdapter;
    ArrayList<String> valuess = new ArrayList<>();
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> arrayList_alert = new ArrayList<>();
    ArrayList<String> arrayList_compare = new ArrayList<>();
    ArrayList<String> list_topnotes = new ArrayList<>();
    JSONArray filter = null;
    HashMap<String, String> citysearchlist;
    String make_id;
    String name;
    ArrayList<String> list = new ArrayList<>();
    String json = null;
    JSONArray topnotes = null;
    JsonValueSharedPreferences jsonValue;
    String text_new;
    ArrayList<String> valus_name_list = new ArrayList<>();
    HashMap<String, String> json_values;
    boolean check = true;
    LinearLayout apply_filter, reset_filter;
    String text;
    ArrayList<String> new_Array_List = new ArrayList<>();
    String City;
    String topnote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_filter_screen);

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        initialize();

        jsonValue = new JsonValueSharedPreferences(FilterScreen.this);
        json_values = jsonValue.getUserDetails();

        text = getIntent().getStringExtra("text");

        City = getIntent().getStringExtra("City");

        topnote = getIntent().getStringExtra("topnote");

        if (text == null) {

        } else {
            Log.e("Details_Filte_datas", text.toString());
        }

        sessionManager = new SessionManager(FilterScreen.this);
        user_datas = sessionManager.getUserDetails();

        if (json_values.get("json_string").equals("0")) {
            new filter_api_list().execute();
            Log.e("On Data", json_values.get("json_string").toString());
        } else {
            new_Array_List = getIntent().getStringArrayListExtra("detail_filter");
            if (new_Array_List == null) {

            } else {
                Log.e("new_Array_List", new_Array_List.toString());

                try {
                    JSONObject jsonObj = new JSONObject(json_values.get("json").toString());
                    filter = jsonObj.getJSONArray("filter_index");
                    for (int i = 0; i < filter.length(); i++) {
                        filter_titles.add(filter.get(i).toString());
                    }

                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
            filterListAdapter = new FilterListAdapter(FilterScreen.this, filter_titles, new_Array_List);
            filert_listview.setAdapter(filterListAdapter);

            filert_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {


                    if (filter_titles.get(position).equals("Price Range")) {

                        jsonValue.createLoginSession(json_values.get("json"), "1", json_values.get("make"), "1");
                    }

                    Intent intent = new Intent(FilterScreen.this, FilterScreenDetail.class);
                    intent.putExtra("pos", filter_titles.get(position));
                    intent.putExtra("pos1", String.valueOf(position));
                    intent.putExtra("pos2", new_Array_List);
                    startActivity(intent);

                    Log.e("pos2", String.valueOf(position));

                    // Toast.makeText(FilterScreen.this, new_Array_List.get(position).toString(), Toast.LENGTH_SHORT).show();
                    FilterScreen.this.finish();
                }
            });

            ////Back On Click Listener
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FilterScreen.this.finish();
                }
            });
        }


        ////Back On Click Listener
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilterScreen.this.finish();
            }
        });

        reset_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                valus_name_list.clear();
                filter_titles.clear();
                topnote="";

                Log.e("hello",topnote);
                new filter_api_list().execute();

            }
        });

        apply_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (json_values.get("make") != null) {
                    make_id = json_values.get("make");
                } else {
                    make_id = new_Array_List.get(2);
                }



                String filterurl = Constant.FILTER_FINAL_API
                        + "session_user_id=" + user_datas.get("user_id")
                        + "&page_name=finalfilter"
                        + "&car_sites=" + new_Array_List.get(0)
                        + "&listing_types=" + new_Array_List.get(1)
                        + "&vehicle_make=" + make_id
                        + "&vehicle_model=" + new_Array_List.get(3)
                        + "&register_year=" + new_Array_List.get(4)
                        + "&transmission_type=" + new_Array_List.get(5)
                        + "&fuel_type=" + new_Array_List.get(6)
                        + "&car_budget=" + new_Array_List.get(7)
                        + "&vehicle_type=" + new_Array_List.get(8)
                        + "&sorting_category=1";

                filterurl = filterurl.replace(" ", "%20");

                Log.e("filterurl", filterurl);
                Log.e("arraylist-->",new_Array_List.toString());

                filter_api.createURL(filterurl);



                // Toast.makeText(FilterScreen.this, "Closrrsdfasdasdas", Toast.LENGTH_SHORT).show();

               /* Intent inte = new Intent(FilterScreen.this, SearchResultActivity.class);
                startActivity(inte);*/

                FilterScreen.this.finish();
            }
        });

    }

    public void initialize() {
        back = (RelativeLayout) findViewById(R.id.close_icon);
        filert_listview = (ListView) findViewById(R.id.filter_list_view);

        apply_filter = (LinearLayout) findViewById(R.id.apply_filter);
        reset_filter = (LinearLayout) findViewById(R.id.reset_filter);

        filter_api = new Filter_API(FilterScreen.this);
        filter_datas = filter_api.getURL();


    }

    public void passvalue(ArrayList<String> filter_sites) {

        new_Array_List=filter_sites;
    }

    private class filter_api_list extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            sessionManager = new SessionManager(FilterScreen.this);
            user_datas = sessionManager.getUserDetails();

            ServiceHandler sh = new ServiceHandler();

            String city_url = Constant.FILTER_API + "session_user_id=" + user_datas.get("user_id") +"&tag_values=" +topnote;
            json = sh.makeServiceCall(city_url, ServiceHandler.POST);


            Log.e("city_url",city_url);

            json_values = jsonValue.getUserDetails();
            jsonValue.clear_json();


            if (json != null) {
                try {
                    jsonValue.createLoginSession(json.toString(), "1", null, null);
                    json_values = jsonValue.getUserDetails();

                    Log.e("json", json_values.get("json"));

                    JSONObject jsonObj = new JSONObject(json);

                    filter = jsonObj.getJSONArray("filter_index");

                    for (int i = 0; i < filter.length(); i++) {
                        filter_titles.add(filter.get(i).toString());
                    }

                    for (int m = 0; m < filter_titles.size(); m++) {

                        filter_sites.add(m, filter_titles.get(m));

                        JSONArray sites_new = jsonObj.getJSONArray(filter_titles.get(m));

                        Log.e("Json array", jsonObj.getJSONArray(filter_titles.get(m)).toString());

                        for (int n = 0; n < sites_new.length(); n++) {

                            String name = sites_new.getJSONObject(n).getString("name");

                            Log.e("Name--->", name);

                            valus_name_list.add(name);


                        }

                        Log.e("valus_name_list",valus_name_list.toString());
//                        selectedListFilter = new SelectedListFilter(FilterScreen.this);
//
//                        selectedListFilter.createSelectedItems(FilterScreen.this,filter_titles);

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

            filterListAdapter = new FilterListAdapter(FilterScreen.this, filter_titles, valus_name_list);
            filert_listview.setAdapter(filterListAdapter);

            filert_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                    if (filter_titles.get(position).equals("Price Range")) {

                        jsonValue.createLoginSession(json_values.get("json"), "1", json_values.get("make"), "1");
                    }

                    Intent intent = new Intent(FilterScreen.this, FilterScreenDetail.class);
                    intent.putExtra("pos", filter_titles.get(position));
                    intent.putExtra("pos1", String.valueOf(position));
                    intent.putExtra("pos2", valus_name_list);
                    startActivity(intent);

                    Log.e("pos2", String.valueOf(position));

                    //Toast.makeText(FilterScreen.this, valus_name_list.get(position).toString(), Toast.LENGTH_SHORT).show();
                    FilterScreen.this.finish();
                }
            });

            apply_filter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (new_Array_List.size() == 0) {

                        Log.e("arraylist-->",new_Array_List.toString());

                        Intent inte = new Intent(FilterScreen.this, SearchResultActivity.class);
                        inte.putExtra("City", City);
                        FilterScreen.this.finish();

                    } else {

                        Log.e("arraylist-->",new_Array_List.toString());

                        String filterurl = Constant.FILTER_FINAL_API
                                + "session_user_id=" + user_datas.get("user_id")
                                + "&page_name=finalfilter"
                                + "&car_sites=" + new_Array_List.get(0)
                                + "&listing_types=" + new_Array_List.get(1)
                                + "&vehicle_make=" + new_Array_List.get(2)
                                + "&vehicle_model=" + new_Array_List.get(3)
                                + "&register_year=" + new_Array_List.get(4)
                                + "&transmission_type=" + new_Array_List.get(5)
                                + "&fuel_type=" + new_Array_List.get(6)
                                + "&car_budget=" + new_Array_List.get(7)
                                + "&vehicle_type=" + new_Array_List.get(8)
                                + "&sorting_category=1";

                        filterurl = filterurl.replace(" ", "%20");

                        Log.e("arraylist-->",new_Array_List.toString());


                        Log.e("filterurl", filterurl);

                        filter_api.createURL(filterurl);

                       /* Intent inte = new Intent(FilterScreen.this, SearchResultActivity.class);
                        inte.putExtra("City", City);
*/

                    /*Intent inte = new Intent(FilterScreen.this, SearchResultActivity.class);
                    startActivity(inte);*/

                        FilterScreen.this.finish();
                    }
                }
            });

            ////Back On Click Listener
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FilterScreen.this.finish();
                }
            });
        }
    }


}

