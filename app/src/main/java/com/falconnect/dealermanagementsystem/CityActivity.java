package com.falconnect.dealermanagementsystem;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.falconnect.dealermanagementsystem.Adapter.PopularCityAdapter;
import com.falconnect.dealermanagementsystem.Model.City_Make_Spinner_Model;
import com.falconnect.dealermanagementsystem.Model.PopularCityDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class CityActivity extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private static RecyclerView popular_city_recycleview;
    private static ArrayList<PopularCityDataModel> data;
    public ArrayList<HashMap<String, String>> city_spinner_list;
    TextView searcheditlist;
    ListView searchlistview;
    ImageView back_close;
    String get_city_id, get_city_name;
    ArrayList<City_Make_Spinner_Model> datas;
    HashMap<String, String> citylist;
    private boolean mVisible;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> spinner_datas;
    private ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mVisible = true;
        if (mVisible) {
            android.support.v7.app.ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
            mVisible = false;

        } else {

        }
        intialize();
        spinner_datas = new ArrayList<String>();
        popular_city_recycleview = (RecyclerView) findViewById(R.id.search_list_view);
        popular_city_recycleview.setHasFixedSize(true);
        popular_city_recycleview.setLayoutManager(new LinearLayoutManager(CityActivity.this, LinearLayoutManager.HORIZONTAL, false));
        data = new ArrayList<PopularCityDataModel>();
        for (int i = 0; i < PopularCityData.popularcityArray.length; i++) {
            data.add(new PopularCityDataModel(
                    PopularCityData.popularcityArray[i],
                    PopularCityData.popularcityId[i],
                    PopularCityData.drawableArrayImage[i]
            ));
        }
        adapter = new PopularCityAdapter(CityActivity.this, data);
        popular_city_recycleview.setAdapter(adapter);
        back_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CityActivity.this, DashBoard.class);
                startActivity(intent);
                CityActivity.this.finish();
            }
        });

        new City_Datas().execute();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(CityActivity.this, "Please select any one city", Toast.LENGTH_SHORT).show();
    }

    public void intialize() {
        searcheditlist = (TextView) findViewById(R.id.searchlistedittext);
        searchlistview = (ListView) findViewById(R.id.citylistview);
        back_close = (ImageView) findViewById(R.id.close_icon);

    }

    private class City_Datas extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();
            String city_url = Constant.DASH_BOARD_SPINNER_API;
            String json = sh.makeServiceCall(city_url, ServiceHandler.GET);
            datas = new ArrayList<City_Make_Spinner_Model>();
            if (json != null) {
                city_spinner_list = new ArrayList<>();
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    JSONArray city = jsonObj.getJSONArray("model_city");
                    for (int k = 0; k <= city.length(); k++) {
                        get_city_id = city.getJSONObject(k).getString("city_id");
                        get_city_name = city.getJSONObject(k).getString("city_name");
                        citylist = new HashMap<>();
                        citylist.put("city_id", get_city_id);
                        citylist.put("city_name", get_city_name);
                        city_spinner_list.add(citylist);
                        spinner_datas.add(get_city_name);
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

            ////City Data Get
            listAdapter = new ArrayAdapter<String>(CityActivity.this, R.layout.search_list_view_single_item, R.id.text1, spinner_datas);
            searchlistview.setAdapter(listAdapter);
            searchlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // make Toast when click
                    String selected_city = (parent.getItemAtPosition(position).toString());
                    //  Toast.makeText(getApplicationContext(), "Position " + selected_city, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CityActivity.this, DashBoard.class);
                    intent.putExtra("selected_item", selected_city);
                    startActivity(intent);
                    CityActivity.this.finish();

                }
            });

            searcheditlist.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    CityActivity.this.listAdapter.getFilter().filter(s);
                }
                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
        ////End City Data Get
    }

}

