package com.falconnect.dealermanagementsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.falconnect.dealermanagementsystem.Adapter.FilterMultiselectAdapter;
import com.falconnect.dealermanagementsystem.Model.FilterDetailPageModel;
import com.falconnect.dealermanagementsystem.SharedPreference.JsonValueSharedPreferences;
import com.falconnect.dealermanagementsystem.SharedPreference.SelectedListFilter;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class FilterScreenDetail extends Activity {


    public ArrayList<HashMap<String, String>> filter_detail_list;
    public ArrayList<HashMap<String, String>> filter_detail_list1;
    String postion;
    String text;
    SessionManager sessionManager;
    HashMap<String, String> user_datss;
    HashMap<String, String> filter_detail;
    JSONArray detail_filter = null;
    ListView filter_details;
    RelativeLayout apply_icon, back_buton;
    ArrayList<String> mArrayProducts = new ArrayList<>();
    ArrayList<String> mArrayProducts_ID = new ArrayList<>();


    SelectedListFilter selectedListFilter;

    ArrayList<String> data = new ArrayList<>();
    ArrayList<String> data_count = new ArrayList<>();
    ArrayList<String> data_id = new ArrayList<>();
    String select_values, selected_values_id;
    ArrayList<String> new_array = new ArrayList<>();
    String pos, make_id;
    JsonValueSharedPreferences jsonValue;
    ArrayList<String> values_filter = new ArrayList<>();
    HashMap<String, String> json_values;

    FilterMultiselectAdapter filterMultiselectAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_filter_screen_detail);

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        postion = getIntent().getStringExtra("pos");
        pos = getIntent().getStringExtra("pos1");
        values_filter = getIntent().getStringArrayListExtra("pos2");

        sessionManager = new SessionManager(FilterScreenDetail.this);
        user_datss = sessionManager.getUserDetails();

        jsonValue = new JsonValueSharedPreferences(FilterScreenDetail.this);
        json_values = jsonValue.getUserDetails();

        // selectedListFilter = new SelectedListFilter(FilterScreenDetail.this);

        filter_details = (ListView) findViewById(R.id.filter_details);
        apply_icon = (RelativeLayout) findViewById(R.id.apply_icon);
        back_buton = (RelativeLayout) findViewById(R.id.close_icon);

        if (json_values.get("make") != null) {
            make_id = json_values.get("make");
        }


        new details_api().execute();

    }

    public void passvalue(ArrayList<String> select_cat, ArrayList<String> select_cat_id) {

        mArrayProducts = select_cat;
        mArrayProducts_ID = select_cat_id;
    }

    private ArrayList<FilterDetailPageModel> getfilter_title() {
        final ArrayList<FilterDetailPageModel> biddata = new ArrayList<>();
        for (int i = 0; i < filter_detail_list.size(); i++) {

            String sitename = filter_detail_list.get(i).get("sitename");
            String count = filter_detail_list.get(i).get("count");
            String name = filter_detail_list.get(i).get("name");

            biddata.add(new FilterDetailPageModel(name, count, sitename));
        }
        return biddata;
    }



    private class details_api extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();

            postion = postion.replace(" ", "%20");

             /*String decode = make_id.toString();
            try {
                decode = URLEncoder.encode(decode, "UTF-8");
            } catch (Exception e) {
            }*/

            if (make_id == null) {

            } else {

                make_id = make_id.replace(" ", "");
            }
            String city_url = Constant.FILTER_DETAIL_API +
                    "session_user_id=" + user_datss.get("user_id")
                    + "&filtertype=" + postion
                    + "&make_id=" + make_id;

            String json_detail = sh.makeServiceCall(city_url, ServiceHandler.POST);

            Log.e("json", json_detail);
            if (json_detail != null) {

                filter_detail_list = new ArrayList<>();

                try {

                    JSONObject jsonObj = new JSONObject(json_detail);

                    detail_filter = jsonObj.getJSONArray("detail_filter");
                    Log.e("Detail_Filter", detail_filter.toString());
                    String sitename;
                    for (int i = 0; i < detail_filter.length(); i++) {
                        if (postion.equals("Car%20Sites")) {
                            sitename = detail_filter.getJSONObject(i).getString("sitename");
                            new_array.add(sitename);
                        } else if (postion.equals("Car%20Year")) {
                            sitename = detail_filter.getJSONObject(i).getString("year");
                        } else if (postion.equals("Price%20Range")) {
                            sitename = detail_filter.getJSONObject(i).getString("value");
                        } else if (postion.equals("Body%20Type")) {
                            sitename = "";
                        } else {
                            sitename = detail_filter.getJSONObject(i).getString("id");
                        }

                        String count = detail_filter.getJSONObject(i).getString("count");
                        String name = detail_filter.getJSONObject(i).getString("name");

                        filter_detail = new HashMap<>();

                        filter_detail.put("sitename", sitename);
                        filter_detail.put("count", count);
                        filter_detail.put("name", name);

                        filter_detail_list.add(filter_detail);

                        data.add(name);
                        data_count.add(count);
                        data_id.add(sitename);

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

            //filterMultiselectAdapter = new FilterMultiselectAdapter(FilterScreenDetail.this, data, data_count, data_id);

            filterMultiselectAdapter = new FilterMultiselectAdapter(FilterScreenDetail.this, getfilter_title());
            filter_details.setAdapter(filterMultiselectAdapter);
            Log.e("Before", data.toString());

            postion = postion.replace("%20", " ");

            back_buton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FilterScreenDetail.this.finish();
                }
            });


            apply_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mArrayProducts.removeAll(Collections.singleton("0"));

                    if (mArrayProducts.size() == 0) {
//                        Toast.makeText(FilterScreenDetail.this, "Please Select Site", Toast.LENGTH_SHORT).show();

                        FilterScreenDetail.this.finish();

                    } else {

                        select_values = mArrayProducts.toString();
                        select_values = select_values.replace("[", "");
                        select_values = select_values.replace("]", "");

                        mArrayProducts_ID.removeAll(Collections.singleton("0"));

                        selected_values_id = mArrayProducts_ID.toString();

                        selected_values_id = selected_values_id.replace("[", "");
                        selected_values_id = selected_values_id.replace("]", "");

                        Log.e("mArray", filter_detail.get("sitename").toString());

                        //Toast.makeText(FilterScreenDetail.this, "Selected Items: " + select_values, Toast.LENGTH_LONG).show();

                        int p = Integer.parseInt(pos);
                        values_filter.set(p, select_values);

                        Log.e("After", values_filter.toString());

                        if (postion.equals("Car Make")) {
                            // new  select_api().execute();
                            jsonValue.createLoginSession(json_values.get("json"), "1", selected_values_id, null);
                        } else if (postion.equals("Price Range")) {
                            // new  select_api().execute();
                            jsonValue.createLoginSession(json_values.get("json"), "1", json_values.get("make"), "1");
                        }

                        Intent intent = new Intent(FilterScreenDetail.this, FilterScreen.class);
                        intent.putExtra("detail_filter", values_filter);
                        startActivity(intent);

                        FilterScreenDetail.this.finish();
                    }

                }
            });
        }

    }

    private class select_api extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();
            postion = postion.replace(" ", "%20");

            text = select_values.replace("[", "");
            text = text.replace("]", "");
            text = text.replace(" ", "%20");

            String city_url = Constant.FILTER_SELECT_API + "session_user_id=" + user_datss.get("user_id") + "&filtertype=" + postion + "&selected_filter=" + text;

            String json_detail = sh.makeServiceCall(city_url, ServiceHandler.POST);

            Log.e("json", json_detail);

            if (json_detail != null) {

                filter_detail_list1 = new ArrayList<>();

                try {

                    JSONObject jsonObj = new JSONObject(json_detail);

                    postion = postion.replace("%20", " ");

                    JSONArray detail_filter1 = jsonObj.getJSONArray(postion);

                    for (int i = 0; i < detail_filter1.length(); i++) {

                        String id = detail_filter1.getJSONObject(i).getString("id");


                        filter_detail_list.add(filter_detail);


                        data_id.add(id);
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
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Log.e("poosssss", data_id.toString());


        }
    }

}

