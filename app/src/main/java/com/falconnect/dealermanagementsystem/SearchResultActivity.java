package com.falconnect.dealermanagementsystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.falconnect.dealermanagementsystem.Adapter.ProductListAdapter;
import com.falconnect.dealermanagementsystem.Adapter.SearchListAdapter;
import com.falconnect.dealermanagementsystem.Adapter.TopNoteAdapter;
import com.falconnect.dealermanagementsystem.Model.DataModel;
import com.falconnect.dealermanagementsystem.Model.SingleProductModel;
import com.falconnect.dealermanagementsystem.SharedPreference.CitySavedSharedPreferences;
import com.falconnect.dealermanagementsystem.SharedPreference.Filter_API;
import com.falconnect.dealermanagementsystem.SharedPreference.JsonValueSharedPreferences;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private static RecyclerView recyclerView_search;
    private static ArrayList<DataModel> data;
    public ListView listView;
    public ArrayList<String> car_name = new ArrayList<>();
    public ArrayList<HashMap<String, String>> city_search_list;

    public ArrayList<HashMap<String, String>> city_search_list_new;

    public ArrayList<HashMap<String, String>> topnote_list;
    public ArrayList<HashMap<String, String>> LoginList;
    public ArrayList<HashMap<String, String>> topnoteList;
    Context context;
    String City, Make, Model, RadioInline, Budget, Site, Type;
    SessionManager session;
    HashMap<String, String> user;
    HashMap<String, String> get_city;


    String SearchCity;

    String search_word;
    String rls;

    ProductListAdapter listAdapter;
    String City_Search_List_Url;
    HashMap<String, String> citysearchlist;
    HashMap<String, String> citysearchnotelist;

    HashMap<String, String> citysearchnotelist_new;


    HashMap<String, String> topnotelist;
    ImageView return_btn;
    RelativeLayout relativeLayout1, relativeLayout2;
    LinearLayout sort, filter;
    RecyclerView my_recycler_top_notes;
    RelativeLayout relativeLayout;
    RecyclerView.Adapter recyclerViewAdapter;
    EditText search_textbox;
    int value_sort;
    ImageView search, search_close;
    JSONArray topnotes = null;
    JSONArray topnotes_new = null;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> arrayList_alert = new ArrayList<>();
    ArrayList<String> arrayList_compare = new ArrayList<>();
    ArrayList<String> list_topnotes = new ArrayList<>();
    TextView title_city;
    ProgressDialog barProgressDialog;
    HashMap<String, String> loginlistmap;
    RelativeLayout result_found;
    ArrayList<String> new_top_notes = new ArrayList<>();
    HashMap<String, String> topnotemap;
    JSONArray city;
    JsonValueSharedPreferences jsonValueSharedPreferences;
    Filter_API filter_api;
    HashMap<String, String> filter_url;
    CitySavedSharedPreferences citySavedSharedPreferences;
    private boolean mVisible;
    private Animation slideRight, slideLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_result);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mVisible = true;
        if (mVisible) {
            android.support.v7.app.ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
            mVisible = false;

        } else {
        }

        mVisible = true;
        context = this;

        Make = getIntent().getStringExtra("vehicle_make");
        Model = getIntent().getStringExtra("vehicle_model");
        Site = getIntent().getStringExtra("car_sites");
        RadioInline = getIntent().getStringExtra("radioInline");
        Budget = getIntent().getStringExtra("car_budget");
        Type = getIntent().getStringExtra("vehicle_type");

        filter_api = new Filter_API(SearchResultActivity.this);
        filter_url = filter_api.getURL();

        search_word = getIntent().getStringExtra("search_word");
        rls = getIntent().getStringExtra("url");

        session = new SessionManager(SearchResultActivity.this);
        user = session.getUserDetails();

        citySavedSharedPreferences = new CitySavedSharedPreferences(SearchResultActivity.this);
        get_city = citySavedSharedPreferences.getAddress_details();

        City = get_city.get("city");

        title_city = (TextView) findViewById(R.id.title_city);

        if (City == null) {
            title_city.setText("Select City");
            SearchCity = "";
        }else{
            title_city.setText(City);
            SearchCity = title_city.getText().toString();
        }

        topnotelist = new HashMap<>();

        //Back Arrow
        return_btn = (ImageView) findViewById(R.id.nav_back_drawer);
        return_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchResultActivity.this.finish();
            }
        });

        my_recycler_top_notes = (RecyclerView) findViewById(R.id.my_recycler_top_notes);
        City_Search_List_Url = getIntent().getStringExtra("City_Url");
        listView = (ListView) findViewById(R.id.list_view);

        slideRight = AnimationUtils.loadAnimation(this, R.anim.slide_right);
        slideLeft = AnimationUtils.loadAnimation(this, R.anim.slide_left);

        relativeLayout1 = (RelativeLayout) findViewById(R.id.header_name);
        relativeLayout2 = (RelativeLayout) findViewById(R.id.search_name);
        result_found = (RelativeLayout) findViewById(R.id.result_found);

        search = (ImageView) findViewById(R.id.search_button_header);
        search_close = (ImageView) findViewById(R.id.close_button);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout2.startAnimation(slideRight);
                relativeLayout2.setVisibility(View.VISIBLE);
                relativeLayout1.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });

        search_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout1.setVisibility(View.VISIBLE);
                relativeLayout1.startAnimation(slideLeft);
                relativeLayout2.startAnimation(slideLeft);
                relativeLayout2.setVisibility(View.GONE);
                search_textbox.setText("");
                search_textbox.clearFocus();
                InputMethodManager in = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(search_textbox.getWindowToken(), 0);

            }
        });

        search_textbox = (EditText) findViewById(R.id.searchview_edit_text);

        search_textbox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    list_topnotes.clear();
                    new search_edit_text().execute();
                    search_textbox.clearFocus();
                    InputMethodManager in = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(search_textbox.getWindowToken(), 0);
                    return true;

                }
                return false;
            }
        });

        sort = (LinearLayout) findViewById(R.id.sort);
        filter = (LinearLayout) findViewById(R.id.filter);
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sorting();
            }
        });
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = list_topnotes.toString().replace("[", "").replace("]", "");

                try {
                    text = URLEncoder.encode(text, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                jsonValueSharedPreferences = new JsonValueSharedPreferences(SearchResultActivity.this);
                jsonValueSharedPreferences.createLoginSession(null, "0", null, null);
                Intent inte = new Intent(SearchResultActivity.this, FilterScreen.class);
                inte.putExtra("City", City);
                inte.putExtra("topnote", text);

                startActivity(inte);
            }
        });

        if (filter_url.get("url") == null) {
            new Search_List().execute();
        } else {
            new Filter_Search_List().execute();
        }

        if (search_word == null && rls == null)  {
        } else {
            list_topnotes.clear();
            new search_edit_text_new().execute();
        }
    }

    private ArrayList<SingleProductModel> getData() {
        final ArrayList<SingleProductModel> imageItems = new ArrayList<>();
        for (int i = 0; i < city_search_list.size(); i++) {

            String make = city_search_list.get(i).get("make");
            String make_id = city_search_list.get(i).get("make_id");
            String model = city_search_list.get(i).get("model");
            String variant = city_search_list.get(i).get("variant");
            String car_locality = city_search_list.get(i).get("car_locality");
            String registration_year = city_search_list.get(i).get("registration_year");
            String kilometer_run = city_search_list.get(i).get("kilometer_run");
            String fuel_type = city_search_list.get(i).get("fuel_type");
            String owner_type = city_search_list.get(i).get("owner_type");
            String price = city_search_list.get(i).get("price");
            String daysstmt = city_search_list.get(i).get("daysstmt");
            String car_id = city_search_list.get(i).get("car_id");
            String dealer_id = city_search_list.get(i).get("dealer_id");
            String bid_image = city_search_list.get(i).get("bid_image");
            String no_images = city_search_list.get(i).get("no_images");
            String imagelinks = city_search_list.get(i).get("imagelinks");
            String saved_car = city_search_list.get(i).get("saved_car");
            String compare_car = city_search_list.get(i).get("compare_car");
            String notify_car = city_search_list.get(i).get("notify_car");
            String view_car = city_search_list.get(i).get("view_car");
            String auction = city_search_list.get(i).get("auction");
            String site_id = city_search_list.get(i).get("site_id");
            String site_image = city_search_list.get(i).get("site_image");

            imageItems.add(new SingleProductModel(make, make_id, model, variant, car_locality, registration_year,
                    kilometer_run, fuel_type, owner_type, price, daysstmt, car_id,
                    dealer_id, bid_image, no_images, imagelinks, saved_car, compare_car,
                    notify_car, view_car, auction, site_id, site_image));
        }
        return imageItems;
    }

    private ArrayList<SingleProductModel> getData_new() {
        final ArrayList<SingleProductModel> imageItems_new = new ArrayList<>();
        for (int i = 0; i < city_search_list_new.size(); i++) {

            String make = city_search_list_new.get(i).get("make");
            String make_id = city_search_list_new.get(i).get("make_id");
            String model = city_search_list_new.get(i).get("model");
            String variant = city_search_list_new.get(i).get("variant");
            String car_locality = city_search_list_new.get(i).get("car_locality");
            String registration_year = city_search_list_new.get(i).get("registration_year");
            String kilometer_run = city_search_list_new.get(i).get("kilometer_run");
            String fuel_type = city_search_list_new.get(i).get("fuel_type");
            String owner_type = city_search_list_new.get(i).get("owner_type");
            String price = city_search_list_new.get(i).get("price");
            String daysstmt = city_search_list_new.get(i).get("daysstmt");
            String car_id = city_search_list_new.get(i).get("car_id");
            String dealer_id = city_search_list_new.get(i).get("dealer_id");
            String bid_image = city_search_list_new.get(i).get("bid_image");
            String no_images = city_search_list_new.get(i).get("no_images");
            String imagelinks = city_search_list_new.get(i).get("imagelinks");
            String saved_car = city_search_list_new.get(i).get("saved_car");
            String compare_car = city_search_list_new.get(i).get("compare_car");
            String notify_car = city_search_list_new.get(i).get("notify_car");
            String view_car = city_search_list_new.get(i).get("view_car");
            String auction = city_search_list_new.get(i).get("auction");
            String site_id = city_search_list_new.get(i).get("site_id");
            String site_image = city_search_list_new.get(i).get("site_image");

            imageItems_new.add(new SingleProductModel(make, make_id, model, variant, car_locality, registration_year,
                    kilometer_run, fuel_type, owner_type, price, daysstmt, car_id,
                    dealer_id, bid_image, no_images, imagelinks, saved_car, compare_car,
                    notify_car, view_car, auction, site_id, site_image));
        }
        return imageItems_new;
    }

    public void sorting() {

        final List<String> sortinglist = new ArrayList<>();
        sortinglist.add("Price -- High to Low");
        sortinglist.add("Price -- Low to High");
        sortinglist.add("Milage -- High to Low");
        sortinglist.add("Milage -- Low to High");
        sortinglist.add("Year -- New to Old");
        sortinglist.add("Year -- Old to New");
        sortinglist.add("Relavance -- Recent to Old");
        sortinglist.add("Relavance -- Old to Recent");


        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort By");
        final ArrayAdapter<String> aa1 = new ArrayAdapter<String>(SearchResultActivity.this, R.layout.sort_single_item, R.id.list,
                sortinglist);
        builder.setSingleChoiceItems(aa1, 0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (sortinglist.get(item) == "Price -- High to Low") {
                    value_sort = 1;
                    arrayList.clear();
                    new sorting_function().execute();
                    dialog.dismiss();
                } else if (sortinglist.get(item) == "Price -- Low to High") {
                    value_sort = 2;
                    arrayList.clear();
                    new sorting_function().execute();
                    dialog.dismiss();
                } else if (sortinglist.get(item) == "Milage -- High to Low") {
                    value_sort = 3;
                    arrayList.clear();
                    new sorting_function().execute();
                    dialog.dismiss();
                } else if (sortinglist.get(item) == "Milage -- Low to High") {
                    value_sort = 4;
                    arrayList.clear();
                    new sorting_function().execute();
                    dialog.dismiss();
                } else if (sortinglist.get(item) == "Year -- New to Old") {
                    value_sort = 5;
                    arrayList.clear();
                    new sorting_function().execute();
                    dialog.dismiss();
                } else if (sortinglist.get(item) == "Year -- Old to New") {
                    value_sort = 6;
                    new sorting_function().execute();
                    dialog.dismiss();

                }else if (sortinglist.get(item) == "Relavance -- Recent to Old") {
                    value_sort = 7;
                    new sorting_function().execute();
                    dialog.dismiss();

                }else if (sortinglist.get(item) == "Relavance -- Old to Recent") {
                    value_sort = 8;
                    new sorting_function().execute();
                    dialog.dismiss();

                }
            }
        });
        builder.show();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        startActivity(getIntent());
        finish();
    }

    public void listview(ArrayList<String> array) {

        new_top_notes = array;

        my_recycler_top_notes.setHasFixedSize(true);
        my_recycler_top_notes.setLayoutManager(new LinearLayoutManager(SearchResultActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewAdapter = new TopNoteAdapter(SearchResultActivity.this, new_top_notes);
        my_recycler_top_notes.setAdapter(recyclerViewAdapter);

        new top_note().execute();

    }

    private class top_note extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = ProgressDialog.show(SearchResultActivity.this, "Loading...", "Please Wait ...", true);
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();

            String text = new_top_notes.toString().replace("[", "").replace("]", "");

            try {
                text = URLEncoder.encode(text, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String fav_url = Constant.TOP_NOTE_API
                    + "session_user_id=" + user.get("user_id")
                    + "&tag_values=" + text.toString()
                    + "&page_name=tagsearchpage"
                    + "&sorting_category=";

            Log.e("fav_url", fav_url);


            String json = sh.makeServiceCall(fav_url, ServiceHandler.POST);

            if (json != null) {
                topnoteList = new ArrayList<>();
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    city = jsonObj.getJSONArray("car_listing");
                    for (int k = 0; k <= city.length(); k++) {
                        String make = city.getJSONObject(k).getString("make");
                        String make_id = city.getJSONObject(k).getString("make_id");
                        String model = city.getJSONObject(k).getString("model");
                        String variant = city.getJSONObject(k).getString("variant");
                        String car_locality = city.getJSONObject(k).getString("car_locality");
                        String registration_year = city.getJSONObject(k).getString("registration_year");
                        String kilometer_run = city.getJSONObject(k).getString("kilometer_run");
                        String fuel_type = city.getJSONObject(k).getString("fuel_type");
                        String owner_type = city.getJSONObject(k).getString("owner_type");
                        String price = city.getJSONObject(k).getString("price");
                        String daysstmt = city.getJSONObject(k).getString("daysstmt");
                        String car_id = city.getJSONObject(k).getString("car_id");
                        String dealer_id = city.getJSONObject(k).getString("dealer_id");
                        String bid_image = city.getJSONObject(k).getString("bid_image");
                        String no_images = city.getJSONObject(k).getString("no_images");
                        String imagelinks = city.getJSONObject(k).getString("imagelinks");
                        String saved_car = city.getJSONObject(k).getString("saved_car");
                        String compare_car = city.getJSONObject(k).getString("compare_car");
                        String notify_car = city.getJSONObject(k).getString("notify_car");
                        String view_car = city.getJSONObject(k).getString("view_car");
                        String auction = city.getJSONObject(k).getString("auction");
                        String site_id = city.getJSONObject(k).getString("site_id");
                        String site_image = city.getJSONObject(k).getString("site_image");

                        topnotemap = new HashMap<>();

                        topnotemap.put("make", make);
                        topnotemap.put("make_id", make_id);
                        topnotemap.put("model", model);
                        topnotemap.put("variant", variant);
                        topnotemap.put("car_locality", car_locality);
                        topnotemap.put("registration_year", registration_year);
                        topnotemap.put("kilometer_run", kilometer_run);
                        topnotemap.put("fuel_type", fuel_type);
                        topnotemap.put("owner_type", owner_type);
                        topnotemap.put("price", price);
                        topnotemap.put("daysstmt", daysstmt);
                        topnotemap.put("car_id", car_id);
                        topnotemap.put("dealer_id", dealer_id);
                        topnotemap.put("bid_image", bid_image);
                        topnotemap.put("no_images", no_images);
                        topnotemap.put("imagelinks", imagelinks);
                        topnotemap.put("saved_car", saved_car);
                        topnotemap.put("compare_car", compare_car);
                        topnotemap.put("notify_car", notify_car);
                        topnotemap.put("view_car", view_car);
                        topnotemap.put("auction", auction);
                        topnotemap.put("site_id", site_id);
                        topnotemap.put("site_image", site_image);

                        city_search_list.add(topnotemap);

                        arrayList.add(saved_car);
                        arrayList_alert.add(notify_car);
                        arrayList_compare.add(compare_car);
                    }
                } catch (final JSONException e) {

                }
            } else {

            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            barProgressDialog.dismiss();

            listAdapter = new ProductListAdapter(SearchResultActivity.this, getData(), arrayList, arrayList_alert, arrayList_compare);
            //listAdapter.notifyDataSetChanged();
            //listView.setAdapter(listAdapter);

            if (listAdapter.getCount() != 0) {
                listView.setAdapter(listAdapter);
            } else {
                listView.setVisibility(View.GONE);
                result_found.setVisibility(View.VISIBLE);
            }

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    SingleProductModel item = (SingleProductModel) parent.getItemAtPosition(position);
                    String url = ConstantIP.IP + "mobileweb/carview/index.html#/carview/" + user.get("user_id") + "/" + item.getCar_id() + "/0";
                    Intent intent = new Intent(SearchResultActivity.this, SellWebViewActivity.class);
                    intent.putExtra("title", "Car Details");
                    intent.putExtra("url", url);
                    startActivity(intent);

                    //Toast.makeText(SearchResultActivity.this, "Selected Car Name :" + item.getMake(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private class Search_List extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = ProgressDialog.show(SearchResultActivity.this, "Loading...", "Please Wait ...", true);
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();

            String city_search_url = Constant.SEARCH_CAR_LISTING_API +
                    "session_user_id=" + user.get("user_id") +
                    "&car_sites=" + Site +
                    "&city_name=" + City +
                    "&radioInline=" + RadioInline +
                    "&car_budget=" + Budget +
                    "&vehicle_type=" + Type +
                    "&vehicle_model=" + Model +
                    "&vehicle_make=" + Make +
                    "&page_name=searchpage";

            Log.e("city_search_url", city_search_url);

            city_search_url = city_search_url.replace(" ", "%20");


            String json = sh.makeServiceCall(city_search_url, ServiceHandler.POST);

            if (json != null) {
                city_search_list = new ArrayList<>();
                loginlistmap = new HashMap<>();
                LoginList = new ArrayList<>();
                try {
                    JSONObject jsonObj = new JSONObject(json);

                    topnotes = jsonObj.getJSONArray("top_note");
                    for (int i = 0; i < topnotes.length(); i++) {
                        list_topnotes.add(topnotes.get(i).toString());
                    }

                    JSONArray city = jsonObj.getJSONArray("car_listing");
                    for (int k = 0; k <= city.length(); k++) {
                        String make = city.getJSONObject(k).getString("make");
                        String make_id = city.getJSONObject(k).getString("make_id");
                        String model = city.getJSONObject(k).getString("model");
                        String variant = city.getJSONObject(k).getString("variant");
                        String car_locality = city.getJSONObject(k).getString("car_locality");
                        String registration_year = city.getJSONObject(k).getString("registration_year");
                        String kilometer_run = city.getJSONObject(k).getString("kilometer_run");
                        String fuel_type = city.getJSONObject(k).getString("fuel_type");
                        String owner_type = city.getJSONObject(k).getString("owner_type");
                        String price = city.getJSONObject(k).getString("price");
                        String daysstmt = city.getJSONObject(k).getString("daysstmt");
                        String car_id = city.getJSONObject(k).getString("car_id");
                        String dealer_id = city.getJSONObject(k).getString("dealer_id");
                        String bid_image = city.getJSONObject(k).getString("bid_image");
                        String no_images = city.getJSONObject(k).getString("no_images");
                        String imagelinks = city.getJSONObject(k).getString("imagelinks");
                        String saved_car = city.getJSONObject(k).getString("saved_car");
                        String compare_car = city.getJSONObject(k).getString("compare_car");
                        String notify_car = city.getJSONObject(k).getString("notify_car");
                        String view_car = city.getJSONObject(k).getString("view_car");
                        String auction = city.getJSONObject(k).getString("auction");
                        String site_id = city.getJSONObject(k).getString("site_id");
                        String site_image = city.getJSONObject(k).getString("site_image");

                        citysearchlist = new HashMap<>();

                        citysearchlist.put("make", make);
                        citysearchlist.put("make_id", make_id);
                        citysearchlist.put("model", model);
                        citysearchlist.put("variant", variant);
                        citysearchlist.put("car_locality", car_locality);
                        citysearchlist.put("registration_year", registration_year);
                        citysearchlist.put("kilometer_run", kilometer_run);
                        citysearchlist.put("fuel_type", fuel_type);
                        citysearchlist.put("owner_type", owner_type);
                        citysearchlist.put("price", price);
                        citysearchlist.put("daysstmt", daysstmt);
                        citysearchlist.put("car_id", car_id);
                        citysearchlist.put("dealer_id", dealer_id);
                        citysearchlist.put("bid_image", bid_image);
                        citysearchlist.put("no_images", no_images);
                        citysearchlist.put("imagelinks", imagelinks);
                        citysearchlist.put("saved_car", saved_car);
                        citysearchlist.put("compare_car", compare_car);
                        citysearchlist.put("notify_car", notify_car);
                        citysearchlist.put("view_car", view_car);
                        citysearchlist.put("auction", auction);
                        citysearchlist.put("site_id", site_id);
                        citysearchlist.put("site_image", site_image);

                        city_search_list.add(citysearchlist);

                        arrayList.add(saved_car);
                        arrayList_alert.add(notify_car);
                        arrayList_compare.add(compare_car);
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

            my_recycler_top_notes.setHasFixedSize(true);
            my_recycler_top_notes.setLayoutManager(new LinearLayoutManager(SearchResultActivity.this, LinearLayoutManager.HORIZONTAL, false));
            recyclerViewAdapter = new TopNoteAdapter(SearchResultActivity.this, list_topnotes);
            my_recycler_top_notes.setAdapter(recyclerViewAdapter);

            listAdapter = new ProductListAdapter(SearchResultActivity.this, getData(), arrayList, arrayList_alert, arrayList_compare);
            //listAdapter.notifyDataSetChanged();
            //listView.setAdapter(listAdapter);

            if (listAdapter.getCount() != 0) {
                listView.setAdapter(listAdapter);
            } else {
                listView.setVisibility(View.GONE);
                result_found.setVisibility(View.VISIBLE);
            }

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    SingleProductModel item = (SingleProductModel) parent.getItemAtPosition(position);
                    String url = ConstantIP.IP + "mobileweb/carview/index.html#/carview/" + user.get("user_id") + "/" + item.getCar_id() + "/0";
                    Intent intent = new Intent(SearchResultActivity.this, SellWebViewActivity.class);
                    intent.putExtra("title", "Car Details");
                    intent.putExtra("url", url);
                    startActivity(intent);
                }
            });

            title_city.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SearchResultActivity.this, CityResultActivity.class);
                    startActivity(intent);

                }
            });


        }
    }

    private class search_edit_text extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = ProgressDialog.show(SearchResultActivity.this, "Loading...", "Please Wait ...", true);
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();

            String decode = search_textbox.getText().toString();

            try {
                decode = URLEncoder.encode(decode, "UTF-8");
            } catch (Exception e) {

            }

            String url = Constant.SEARCH_TEXTBOX + "session_user_id=" + user.get("user_id")
                    + "&city_name=" + City
                    + "&search_listing=" + decode
                    + "&sorting_category=" + value_sort
                    + "&page_name=detail_searchpage";

            String json = sh.makeServiceCall(url, ServiceHandler.POST);

            if (json != null) {

                city_search_list = new ArrayList<>();

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    topnotes = jsonObj.getJSONArray("top_note");
                    for (int i = 0; i < topnotes.length(); i++) {
                        list_topnotes.add(topnotes.get(i).toString());
                    }

                    JSONArray city = jsonObj.getJSONArray("car_listing");

                    for (int k = 0; k <= city.length(); k++) {

                        String make = city.getJSONObject(k).getString("make");
                        String make_id = city.getJSONObject(k).getString("make_id");
                        String model = city.getJSONObject(k).getString("model");
                        String variant = city.getJSONObject(k).getString("variant");
                        String car_locality = city.getJSONObject(k).getString("car_locality");
                        String registration_year = city.getJSONObject(k).getString("registration_year");
                        String kilometer_run = city.getJSONObject(k).getString("kilometer_run");
                        String fuel_type = city.getJSONObject(k).getString("fuel_type");
                        String owner_type = city.getJSONObject(k).getString("owner_type");
                        String price = city.getJSONObject(k).getString("price");
                        String daysstmt = city.getJSONObject(k).getString("daysstmt");
                        String car_id = city.getJSONObject(k).getString("car_id");
                        String dealer_id = city.getJSONObject(k).getString("dealer_id");
                        String bid_image = city.getJSONObject(k).getString("bid_image");
                        String no_images = city.getJSONObject(k).getString("no_images");
                        String imagelinks = city.getJSONObject(k).getString("imagelinks");
                        String saved_car = city.getJSONObject(k).getString("saved_car");
                        String compare_car = city.getJSONObject(k).getString("compare_car");
                        String notify_car = city.getJSONObject(k).getString("notify_car");
                        String view_car = city.getJSONObject(k).getString("view_car");
                        String auction = city.getJSONObject(k).getString("auction");
                        String site_id = city.getJSONObject(k).getString("site_id");
                        String site_image = city.getJSONObject(k).getString("site_image");

                        citysearchlist = new HashMap<>();

                        citysearchlist.put("make", make);
                        citysearchlist.put("make_id", make_id);
                        citysearchlist.put("model", model);
                        citysearchlist.put("variant", variant);
                        citysearchlist.put("car_locality", car_locality);
                        citysearchlist.put("registration_year", registration_year);
                        citysearchlist.put("kilometer_run", kilometer_run);
                        citysearchlist.put("fuel_type", fuel_type);
                        citysearchlist.put("owner_type", owner_type);
                        citysearchlist.put("price", price);
                        citysearchlist.put("daysstmt", daysstmt);
                        citysearchlist.put("car_id", car_id);
                        citysearchlist.put("dealer_id", dealer_id);
                        citysearchlist.put("bid_image", bid_image);
                        citysearchlist.put("no_images", no_images);
                        citysearchlist.put("imagelinks", imagelinks);
                        citysearchlist.put("saved_car", saved_car);
                        citysearchlist.put("compare_car", compare_car);
                        citysearchlist.put("notify_car", notify_car);
                        citysearchlist.put("view_car", view_car);
                        citysearchlist.put("auction", auction);
                        citysearchlist.put("site_id", site_id);
                        citysearchlist.put("site_image", site_image);

                        city_search_list.add(citysearchlist);

                        arrayList.add(saved_car);
                        arrayList_alert.add(notify_car);
                        arrayList_compare.add(compare_car);
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

            relativeLayout1.setVisibility(View.VISIBLE);
            relativeLayout1.startAnimation(slideRight);
            relativeLayout2.startAnimation(slideRight);
            relativeLayout2.setVisibility(View.GONE);
            search_textbox.setText("");
            search_textbox.clearFocus();

            my_recycler_top_notes.setHasFixedSize(true);
            my_recycler_top_notes.setLayoutManager(new LinearLayoutManager(SearchResultActivity.this, LinearLayoutManager.HORIZONTAL, false));
            recyclerViewAdapter = new TopNoteAdapter(SearchResultActivity.this, list_topnotes);
            my_recycler_top_notes.setAdapter(recyclerViewAdapter);

            listAdapter = new ProductListAdapter(SearchResultActivity.this, getData(), arrayList, arrayList_alert, arrayList_compare);

            if (listAdapter.getCount() != 0) {
                listView.setAdapter(listAdapter);
            } else {
                listView.setVisibility(View.GONE);
                result_found.setVisibility(View.VISIBLE);
            }

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    SingleProductModel item = (SingleProductModel) parent.getItemAtPosition(position);
                    String url = ConstantIP.IP + "mobileweb/carview/index.html#/carview/" + user.get("user_id") + "/" + item.getCar_id() + "/0";
                    Intent intent = new Intent(SearchResultActivity.this, SellWebViewActivity.class);
                    intent.putExtra("title", "Car Details");
                    intent.putExtra("url", url);
                    startActivity(intent);
                }
            });

        }
    }

    private class sorting_function extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = ProgressDialog.show(SearchResultActivity.this, "Loading...", "Please Wait ...", true);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();

            /*String decode = search_textbox.getText().toString();
            try {
                decode = URLEncoder.encode(decode, "UTF-8");
            } catch (Exception e) {
            }*/

            String top_values = list_topnotes.toString();

            top_values = top_values.replace("[", "");
            top_values = top_values.replace("]", "");
            top_values = top_values.replace(" ", "%20");

            String url = Constant.TOP_NOTE_API + "session_user_id=" + user.get("user_id")

                    + "&sorting_category=" + value_sort
                    + "&page_name=tagsearchpage"
                    + "&tag_values=" + top_values;

            Log.e("urlurlurlurl", url);

            String json = sh.makeServiceCall(url, ServiceHandler.POST);

            if (json != null) {
                city_search_list = new ArrayList<>();
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    JSONArray city = jsonObj.getJSONArray("car_listing");

                    for (int k = 0; k <= city.length(); k++) {

                        String make = city.getJSONObject(k).getString("make");
                        String make_id = city.getJSONObject(k).getString("make_id");
                        String model = city.getJSONObject(k).getString("model");
                        String variant = city.getJSONObject(k).getString("variant");
                        String car_locality = city.getJSONObject(k).getString("car_locality");
                        String registration_year = city.getJSONObject(k).getString("registration_year");
                        String kilometer_run = city.getJSONObject(k).getString("kilometer_run");
                        String fuel_type = city.getJSONObject(k).getString("fuel_type");
                        String owner_type = city.getJSONObject(k).getString("owner_type");
                        String price = city.getJSONObject(k).getString("price");
                        String daysstmt = city.getJSONObject(k).getString("daysstmt");
                        String car_id = city.getJSONObject(k).getString("car_id");
                        String dealer_id = city.getJSONObject(k).getString("dealer_id");
                        String bid_image = city.getJSONObject(k).getString("bid_image");
                        String no_images = city.getJSONObject(k).getString("no_images");
                        String imagelinks = city.getJSONObject(k).getString("imagelinks");
                        String saved_car = city.getJSONObject(k).getString("saved_car");
                        String compare_car = city.getJSONObject(k).getString("compare_car");
                        String notify_car = city.getJSONObject(k).getString("notify_car");
                        String view_car = city.getJSONObject(k).getString("view_car");
                        String auction = city.getJSONObject(k).getString("auction");
                        String site_id = city.getJSONObject(k).getString("site_id");
                        String site_image = city.getJSONObject(k).getString("site_image");

                        citysearchlist = new HashMap<>();

                        citysearchlist.put("make", make);
                        citysearchlist.put("make_id", make_id);
                        citysearchlist.put("model", model);
                        citysearchlist.put("variant", variant);
                        citysearchlist.put("car_locality", car_locality);
                        citysearchlist.put("registration_year", registration_year);
                        citysearchlist.put("kilometer_run", kilometer_run);
                        citysearchlist.put("fuel_type", fuel_type);
                        citysearchlist.put("owner_type", owner_type);
                        citysearchlist.put("price", price);
                        citysearchlist.put("daysstmt", daysstmt);
                        citysearchlist.put("car_id", car_id);
                        citysearchlist.put("dealer_id", dealer_id);
                        citysearchlist.put("bid_image", bid_image);
                        citysearchlist.put("no_images", no_images);
                        citysearchlist.put("imagelinks", imagelinks);
                        citysearchlist.put("saved_car", saved_car);
                        citysearchlist.put("compare_car", compare_car);
                        citysearchlist.put("notify_car", notify_car);
                        citysearchlist.put("view_car", view_car);
                        citysearchlist.put("auction", auction);
                        citysearchlist.put("site_id", site_id);
                        citysearchlist.put("site_image", site_image);

                        city_search_list.add(citysearchlist);

                        arrayList.add(saved_car);
                        arrayList_alert.add(notify_car);
                        arrayList_compare.add(compare_car);
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
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            barProgressDialog.dismiss();

            listAdapter = new ProductListAdapter(SearchResultActivity.this, getData(), arrayList, arrayList_alert, arrayList_compare);
            //listAdapter.notifyDataSetChanged();
            //listView.setAdapter(listAdapter);

            if (listAdapter.getCount() != 0) {
                listView.setAdapter(listAdapter);
            } else {
                listView.setVisibility(View.GONE);
                result_found.setVisibility(View.VISIBLE);
            }

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    SingleProductModel item = (SingleProductModel) parent.getItemAtPosition(position);
                    String url = ConstantIP.IP + "mobileweb/carview/index.html#/carview/" + user.get("user_id") + "/" + item.getCar_id() + "/0";
                    Intent intent = new Intent(SearchResultActivity.this, SellWebViewActivity.class);
                    intent.putExtra("title", "Car Details");
                    intent.putExtra("url", url);
                    startActivity(intent);
                }
            });

        }
    }

    private class Filter_Search_List extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = ProgressDialog.show(SearchResultActivity.this, "Loading...", "Please Wait ...", true);
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();

            String json = sh.makeServiceCall(filter_url.get("url"), ServiceHandler.POST);

            Log.e("filter_url.get", filter_url.get("url").toString());

            Log.e("json", json);

            if (json != null) {
                city_search_list = new ArrayList<>();
                loginlistmap = new HashMap<>();
                LoginList = new ArrayList<>();
                try {
                    JSONObject jsonObj = new JSONObject(json);

                    topnotes = jsonObj.getJSONArray("top_note");
                    for (int i = 0; i < topnotes.length(); i++) {
                        list_topnotes.add(topnotes.get(i).toString());
                    }

                    JSONArray city = jsonObj.getJSONArray("car_listing");
                    for (int k = 0; k <= city.length(); k++) {
                        String make = city.getJSONObject(k).getString("make");
                        String make_id = city.getJSONObject(k).getString("make_id");
                        String model = city.getJSONObject(k).getString("model");
                        String variant = city.getJSONObject(k).getString("variant");
                        String car_locality = city.getJSONObject(k).getString("car_locality");
                        String registration_year = city.getJSONObject(k).getString("registration_year");
                        String kilometer_run = city.getJSONObject(k).getString("kilometer_run");
                        String fuel_type = city.getJSONObject(k).getString("fuel_type");
                        String owner_type = city.getJSONObject(k).getString("owner_type");
                        String price = city.getJSONObject(k).getString("price");
                        String daysstmt = city.getJSONObject(k).getString("daysstmt");
                        String car_id = city.getJSONObject(k).getString("car_id");
                        String dealer_id = city.getJSONObject(k).getString("dealer_id");
                        String bid_image = city.getJSONObject(k).getString("bid_image");
                        String no_images = city.getJSONObject(k).getString("no_images");
                        String imagelinks = city.getJSONObject(k).getString("imagelinks");
                        String saved_car = city.getJSONObject(k).getString("saved_car");
                        String compare_car = city.getJSONObject(k).getString("compare_car");
                        String notify_car = city.getJSONObject(k).getString("notify_car");
                        String view_car = city.getJSONObject(k).getString("view_car");
                        String auction = city.getJSONObject(k).getString("auction");
                        String site_id = city.getJSONObject(k).getString("site_id");
                        String site_image = city.getJSONObject(k).getString("site_image");

                        citysearchlist = new HashMap<>();

                        citysearchlist.put("make", make);
                        citysearchlist.put("make_id", make_id);
                        citysearchlist.put("model", model);
                        citysearchlist.put("variant", variant);
                        citysearchlist.put("car_locality", car_locality);
                        citysearchlist.put("registration_year", registration_year);
                        citysearchlist.put("kilometer_run", kilometer_run);
                        citysearchlist.put("fuel_type", fuel_type);
                        citysearchlist.put("owner_type", owner_type);
                        citysearchlist.put("price", price);
                        citysearchlist.put("daysstmt", daysstmt);
                        citysearchlist.put("car_id", car_id);
                        citysearchlist.put("dealer_id", dealer_id);
                        citysearchlist.put("bid_image", bid_image);
                        citysearchlist.put("no_images", no_images);
                        citysearchlist.put("imagelinks", imagelinks);
                        citysearchlist.put("saved_car", saved_car);
                        citysearchlist.put("compare_car", compare_car);
                        citysearchlist.put("notify_car", notify_car);
                        citysearchlist.put("view_car", view_car);
                        citysearchlist.put("auction", auction);
                        citysearchlist.put("site_id", site_id);
                        citysearchlist.put("site_image", site_image);

                        city_search_list.add(citysearchlist);

                        arrayList.add(saved_car);
                        arrayList_alert.add(notify_car);
                        arrayList_compare.add(compare_car);
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

            my_recycler_top_notes.setHasFixedSize(true);
            my_recycler_top_notes.setLayoutManager(new LinearLayoutManager(SearchResultActivity.this, LinearLayoutManager.HORIZONTAL, false));
            recyclerViewAdapter = new TopNoteAdapter(SearchResultActivity.this, list_topnotes);
            my_recycler_top_notes.setAdapter(recyclerViewAdapter);

            listAdapter = new ProductListAdapter(SearchResultActivity.this, getData(), arrayList, arrayList_alert, arrayList_compare);
            //listAdapter.notifyDataSetChanged();
            //listView.setAdapter(listAdapter);

            if (listAdapter.getCount() != 0) {
                listView.setAdapter(listAdapter);
            } else {
                listView.setVisibility(View.GONE);
                result_found.setVisibility(View.VISIBLE);
            }

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    SingleProductModel item = (SingleProductModel) parent.getItemAtPosition(position);
                    String url = ConstantIP.IP + "mobileweb/carview/index.html#/carview/" + user.get("user_id") + "/" + item.getCar_id() + "/0";
                    Intent intent = new Intent(SearchResultActivity.this, SellWebViewActivity.class);
                    intent.putExtra("title", "Car Details");
                    intent.putExtra("url", url);
                    startActivity(intent);
                    // Toast.makeText(SearchResultActivity.this, "Selected Car Name :" + item.getMake(), Toast.LENGTH_SHORT).show();
                }
            });

            title_city.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SearchResultActivity.this, CityResultActivity.class);
                    startActivity(intent);

                }
            });


        }
    }

    private class search_edit_text_new extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //barProgressDialog = ProgressDialog.show(SearchResultActivity.this, "Loading...", "Please Wait ...", true);
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();

            String json = sh.makeServiceCall(rls, ServiceHandler.POST);

            if (json != null) {

                city_search_list_new = new ArrayList<>();
                list_topnotes.clear();
                try {
                    JSONObject jsonObj = new JSONObject(json);

                    topnotes_new = jsonObj.getJSONArray("top_note");
                    for (int i = 0; i < topnotes_new.length(); i++) {
                        list_topnotes.add(topnotes_new.get(i).toString());
                    }

                    JSONArray carss = jsonObj.getJSONArray("car_listing");

                    for (int k = 0; k <= carss.length(); k++) {

                        String make = carss.getJSONObject(k).getString("make");
                        String make_id = carss.getJSONObject(k).getString("make_id");
                        String model = carss.getJSONObject(k).getString("model");
                        String variant = carss.getJSONObject(k).getString("variant");
                        String car_locality = carss.getJSONObject(k).getString("car_locality");
                        String registration_year = carss.getJSONObject(k).getString("registration_year");
                        String kilometer_run = carss.getJSONObject(k).getString("kilometer_run");
                        String fuel_type = carss.getJSONObject(k).getString("fuel_type");
                        String owner_type = carss.getJSONObject(k).getString("owner_type");
                        String price = carss.getJSONObject(k).getString("price");
                        String daysstmt = carss.getJSONObject(k).getString("daysstmt");
                        String car_id = carss.getJSONObject(k).getString("car_id");
                        String dealer_id = carss.getJSONObject(k).getString("dealer_id");
                        String bid_image = carss.getJSONObject(k).getString("bid_image");
                        String no_images = carss.getJSONObject(k).getString("no_images");
                        String imagelinks = carss.getJSONObject(k).getString("imagelinks");
                        String saved_car = carss.getJSONObject(k).getString("saved_car");
                        String compare_car = carss.getJSONObject(k).getString("compare_car");
                        String notify_car = carss.getJSONObject(k).getString("notify_car");
                        String view_car = carss.getJSONObject(k).getString("view_car");
                        String auction = carss.getJSONObject(k).getString("auction");
                        String site_id = carss.getJSONObject(k).getString("site_id");
                        String site_image = carss.getJSONObject(k).getString("site_image");

                        citysearchnotelist_new = new HashMap<>();

                        citysearchnotelist_new.put("make", make);
                        citysearchnotelist_new.put("make_id", make_id);
                        citysearchnotelist_new.put("model", model);
                        citysearchnotelist_new.put("variant", variant);
                        citysearchnotelist_new.put("car_locality", car_locality);
                        citysearchnotelist_new.put("registration_year", registration_year);
                        citysearchnotelist_new.put("kilometer_run", kilometer_run);
                        citysearchnotelist_new.put("fuel_type", fuel_type);
                        citysearchnotelist_new.put("owner_type", owner_type);
                        citysearchnotelist_new.put("price", price);
                        citysearchnotelist_new.put("daysstmt", daysstmt);
                        citysearchnotelist_new.put("car_id", car_id);
                        citysearchnotelist_new.put("dealer_id", dealer_id);
                        citysearchnotelist_new.put("bid_image", bid_image);
                        citysearchnotelist_new.put("no_images", no_images);
                        citysearchnotelist_new.put("imagelinks", imagelinks);
                        citysearchnotelist_new.put("saved_car", saved_car);
                        citysearchnotelist_new.put("compare_car", compare_car);
                        citysearchnotelist_new.put("notify_car", notify_car);
                        citysearchnotelist_new.put("view_car", view_car);
                        citysearchnotelist_new.put("auction", auction);
                        citysearchnotelist_new.put("site_id", site_id);
                        citysearchnotelist_new.put("site_image", site_image);

                        city_search_list_new.add(citysearchnotelist_new);

                        arrayList.add(saved_car);
                        arrayList_alert.add(notify_car);
                        arrayList_compare.add(compare_car);
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

            //barProgressDialog.dismiss();

            my_recycler_top_notes.setHasFixedSize(true);
            my_recycler_top_notes.setLayoutManager(new LinearLayoutManager(SearchResultActivity.this, LinearLayoutManager.HORIZONTAL, false));
            recyclerViewAdapter = new TopNoteAdapter(SearchResultActivity.this, list_topnotes);
            my_recycler_top_notes.setAdapter(recyclerViewAdapter);

            SearchListAdapter listAdapter = new SearchListAdapter(SearchResultActivity.this, getData_new(), arrayList, arrayList_alert, arrayList_compare);

            if (listAdapter.getCount() != 0) {
                listView.setAdapter(listAdapter);
                listView.setVisibility(View.VISIBLE);
                result_found.setVisibility(View.GONE);

            } else {
                listView.setVisibility(View.VISIBLE);
                result_found.setVisibility(View.GONE);
            }

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    SingleProductModel item = (SingleProductModel) parent.getItemAtPosition(position);
                    String url = ConstantIP.IP + "mobileweb/carview/index.html#/carview/" + user.get("user_id") + "/" + item.getCar_id() + "/0";
                    Intent intent = new Intent(SearchResultActivity.this, SellWebViewActivity.class);
                    intent.putExtra("title", "Car Details");
                    intent.putExtra("url", url);
                    startActivity(intent);
                }
            });

        }
    }
}