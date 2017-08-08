package com.falconnect.dealermanagementsystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.falconnect.dealermanagementsystem.Adapter.CustomAdapter;
import com.falconnect.dealermanagementsystem.Adapter.CustomList;
import com.falconnect.dealermanagementsystem.Adapter.SavedCarAdapter;
import com.falconnect.dealermanagementsystem.FontAdapter.RoundImageTransform;
import com.falconnect.dealermanagementsystem.Model.DataModel;
import com.falconnect.dealermanagementsystem.Model.SingleProductModel;
import com.falconnect.dealermanagementsystem.NavigationDrawer.BuyPageNavigation;
import com.falconnect.dealermanagementsystem.SharedPreference.PlandetailsSharedPreferences;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;
import com.freshdesk.hotline.FaqOptions;
import com.freshdesk.hotline.Hotline;
import com.navdrawer.SimpleSideDrawer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SavedCarActivity extends AppCompatActivity {


    private static RecyclerView.Adapter adapter;
    private static RecyclerView recyclerView_search;
    private static ArrayList<DataModel> data;
    public ArrayList<HashMap<String, String>> saved_car_list;
    Context context;
    ListView listView;
    SavedCarAdapter listAdapter;
    SessionManager sessionManager;
    HashMap<String, String> user;
    String Saved_Car_url;
    ImageView savedcar_back;
    ProgressDialog barProgressDialog;
    BuyPageNavigation savedcar_buypagenavigation;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> arrayList_alert = new ArrayList<>();
    ArrayList<String> arrayList_compare = new ArrayList<>();
    TextView title;
    HashMap<String, String> savedcarlist;
    SessionManager session_savedcar;
    ImageView imageView_savedcar;
    TextView profile_name_savedcar;
    TextView profile_address_savedcar;
    String saved_name_savedcar, saved_address_savedcar, saved_user_id;
    RelativeLayout result_found;
    PlandetailsSharedPreferences plandetailsSharedPreferences;
    HashMap<String, String> plan;
    private boolean mVisible;
    private SwipeRefreshLayout mSwipeRefreshLayout_savedcar = null;
    private SimpleSideDrawer mNav_savedcar;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_saved_car);

        //Keyboard Hide
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //Status Bar Hide
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mVisible = true;
        context = this;

        //Full Screen Activity
        if (mVisible) {
            android.support.v7.app.ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
            mVisible = false;

        } else {
        }
        barProgressDialog = ProgressDialog.show(SavedCarActivity.this, "Loading...", "Please Wait ...", true);

        //Footer List View
        recyclerView_search = (RecyclerView) findViewById(R.id.my_recycler);
        //List View Fixed size
        recyclerView_search.setHasFixedSize(true);
        //List View Horizontal
        recyclerView_search.setLayoutManager(new LinearLayoutManager(SavedCarActivity.this, LinearLayoutManager.HORIZONTAL, false));

        //Footer List View Get Data
        data = new ArrayList<DataModel>();

        for (int i = 0; i < MyData.nameArray.length; i++) {

            data.add(new DataModel(
                    MyData.nameArray[i],
                    MyData.id_[i],
                    MyData.drawableArrayWhite1[i]
            ));

        }

        session_savedcar = new SessionManager(SavedCarActivity.this);
        user = session_savedcar.getUserDetails();
        //Footer List View Adapter
        adapter = new CustomAdapter(SavedCarActivity.this, data);
        recyclerView_search.setAdapter(adapter);

        //get city url
        title = (TextView) findViewById(R.id.title_saved_cars);

        listView = (ListView) findViewById(R.id.saved_car_list);

        savedcar_back = (ImageView) findViewById(R.id.savedcar_back);
        mNav_savedcar = new SimpleSideDrawer(this);
        mNav_savedcar.setLeftBehindContentView(R.layout.activity_behind_left_simple);

        imageView_savedcar = (ImageView) mNav_savedcar.findViewById(R.id.profile_avatar);
        profile_name_savedcar= (TextView) mNav_savedcar.findViewById(R.id.profile_name);
        profile_address_savedcar = (TextView) mNav_savedcar.findViewById(R.id.profile_address);

        session_savedcar = new SessionManager(SavedCarActivity.this);
        user = session_savedcar.getUserDetails();
        saved_user_id = user.get("user_id");
        saved_name_savedcar = user.get("dealer_name");
        saved_address_savedcar = user.get("dealershipname");
        profile_name_savedcar.setText(saved_name_savedcar);

        if (user.get("dealer_img").isEmpty()) {
            Glide.with(getApplicationContext()).load(R.drawable.default_avatar)
                    .transform(new RoundImageTransform(SavedCarActivity.this))
                    .into(imageView_savedcar);
        } else {
            Glide.with(getApplicationContext())
                    .load(user.get("dealer_img"))
                    .transform(new RoundImageTransform(SavedCarActivity.this))
                    .into(imageView_savedcar);
        }
        profile_address_savedcar.setText(saved_address_savedcar);

        savedcar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNav_savedcar.toggleLeftDrawer();
            }
        });

        Saved_Car_url = Constant.SAVED_CAR_API + "&session_user_id=" + saved_user_id;

        RelativeLayout call_layout = (RelativeLayout) mNav_savedcar.findViewById(R.id.call_layout);
        RelativeLayout chat_layout = (RelativeLayout) mNav_savedcar.findViewById(R.id.chat_layout);
        RelativeLayout logout_layout = (RelativeLayout) mNav_savedcar.findViewById(R.id.logout_layout);

        call_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertbox = new AlertDialog.Builder(SavedCarActivity.this)
                        .setMessage("Do you want to Call?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:9790928569"));
                                startActivity(callIntent);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        })
                        .show();
            }
        });

        logout_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session_savedcar.logoutUser();
                mNav_savedcar.closeLeftSide();
                SavedCarActivity.this.finish();
            }
        });

        chat_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chat();
                mNav_savedcar.closeLeftSide();
            }
        });

        result_found = (RelativeLayout) findViewById(R.id.result_found);

        //NAVIGATION DRAWER LIST VIEW
        savedcar_buypagenavigation = new BuyPageNavigation();
        CustomList adapter = new CustomList(SavedCarActivity.this, BuyPageNavigation.web, BuyPageNavigation.imageId);
        ListView list = (ListView) findViewById(R.id.nav_list_view);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (BuyPageNavigation.web[position] == "Dashboard") {
                    Intent intent = new Intent(SavedCarActivity.this, MainDashBoardActivity.class);
                    startActivity(intent);
                    SavedCarActivity.this.finish();
                    mNav_savedcar.closeLeftSide();
                    // Toast.makeText(SavedCarActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Buy") {
                    mNav_savedcar.closeLeftSide();
                    // Toast.makeText(SavedCarActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Sell") {
                    Intent intent = new Intent(SavedCarActivity.this, SellDashBoardActivity.class);
                    startActivity(intent);
                    SavedCarActivity.this.finish();
                    mNav_savedcar.closeLeftSide();
                    // Toast.makeText(SavedCarActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Manage") {
                    Intent intent = new Intent(SavedCarActivity.this, ManageDashBoardActivity.class);
                    startActivity(intent);
                    SavedCarActivity.this.finish();
                    mNav_savedcar.closeLeftSide();
                    //  Toast.makeText(SavedCarActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Funding") {
                    plandetailsSharedPreferences = new PlandetailsSharedPreferences(SavedCarActivity.this);
                    plan = plandetailsSharedPreferences.getUserDetails();

//                    if (plan.get("PlanName") == "BASIC" ) {
//                        Intent intent = new Intent(SavedCarActivity.this, LoanActivity.class);
//                        startActivity(intent);
//                        mNav_savedcar.closeLeftSide();
//                        SavedCarActivity.this.finish();
//                    } else {
                        Intent intent = new Intent(SavedCarActivity.this, FundingActivity.class);
                        startActivity(intent);
                        mNav_savedcar.closeLeftSide();
                        SavedCarActivity.this.finish();
//                    }
                    // Toast.makeText(SavedCarActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Networks") {
                    String url = "http://app.dealerplus.in/user_login_cometchat?session_user_id=" + user.get("user_id");
                    Intent intet = new Intent(SavedCarActivity.this, NetworksWebviewActivity.class);
                    //intet.putExtra("title", BuyPageNavigation.web[position]);
                    intet.putExtra("url", url);
                    startActivity(intet);
                    mNav_savedcar.closeLeftSide();
                    //  Toast.makeText(SavedCarActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Reports") {
                    plandetailsSharedPreferences = new PlandetailsSharedPreferences(SavedCarActivity.this);
                    plan = plandetailsSharedPreferences.getUserDetails();
                    if (plan.get("PlanName") == "BASIC") {
                        Intent intent = new Intent(SavedCarActivity.this, BuyPlanActivity.class);
                        startActivity(intent);
                        //MainDashBoardActivity.this.finish();
                        mNav_savedcar.closeLeftSide();
                    } else {
                        Intent intent = new Intent(SavedCarActivity.this, ReportSalesActivity.class);
                        startActivity(intent);
                        SavedCarActivity.this.finish();
                        mNav_savedcar.closeLeftSide();
                    }

                    //Toast.makeText(SavedCarActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();

                } else if (BuyPageNavigation.web[position] == "FAQs") {
                    faqs();
                    mNav_savedcar.closeLeftSide();
                } else {
                    //Toast.makeText(DashBoard.this, web[position], Toast.LENGTH_SHORT).show();
                }

            }
        });

        new Saved_Car().execute();

        //Initialize swipe to refresh view
        mSwipeRefreshLayout_savedcar = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout_savedcar);
        mSwipeRefreshLayout_savedcar.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Refreshing data on server
                new Saved_Car().execute();
            }
        });

    }

    @Override
    public void onRestart() {
        super.onRestart();
        startActivity(getIntent());
        finish();
    }

    public void faqs() {
        FaqOptions faqOptions = new FaqOptions()
                .showFaqCategoriesAsGrid(true)
                .showContactUsOnAppBar(true)
                .showContactUsOnFaqScreens(false)
                .showContactUsOnFaqNotHelpful(false);

        Hotline.showFAQs(SavedCarActivity.this, faqOptions);
    }

    public void chat() {
        Hotline.showConversations(getApplicationContext());
        // Hotline.clearUserData(getApplicationContext());
    }

    @Override
    public void onBackPressed() {

        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to exit application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        SavedCarActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();

    }

    private ArrayList<SingleProductModel> getData() {
        final ArrayList<SingleProductModel> imageItems = new ArrayList<>();
        for (int i = 0; i < saved_car_list.size(); i++) {
            String make = saved_car_list.get(i).get("make");
            String make_id = saved_car_list.get(i).get("make_id");
            String model = saved_car_list.get(i).get("model");
            String variant = saved_car_list.get(i).get("variant");
            String car_locality = saved_car_list.get(i).get("car_locality");
            String registration_year = saved_car_list.get(i).get("registration_year");
            String kilometer_run = saved_car_list.get(i).get("kilometer_run");
            String fuel_type = saved_car_list.get(i).get("fuel_type");
            String owner_type = saved_car_list.get(i).get("owner_type");
            String price = saved_car_list.get(i).get("price");
            String daysstmt = saved_car_list.get(i).get("daysstmt");
            String car_id = saved_car_list.get(i).get("car_id");
            String dealer_id = saved_car_list.get(i).get("dealer_id");
            String bid_image = saved_car_list.get(i).get("bid_image");
            String no_images = saved_car_list.get(i).get("no_images");
            String imagelinks = saved_car_list.get(i).get("imagelinks");
            String saved_car = saved_car_list.get(i).get("saved_car");
            String compare_car = saved_car_list.get(i).get("compare_car");
            String notify_car = saved_car_list.get(i).get("notify_car");
            String view_car = saved_car_list.get(i).get("view_car");
            String auction = saved_car_list.get(i).get("auction");
            String site_id = saved_car_list.get(i).get("site_id");
            String site_image = saved_car_list.get(i).get("site_image");

            imageItems.add(new SingleProductModel(make,make_id,model,variant,car_locality,registration_year,
                    kilometer_run,fuel_type,owner_type,price,daysstmt,car_id,
                    dealer_id,bid_image,no_images,imagelinks,saved_car,compare_car,
                    notify_car,view_car,auction,site_id,site_image));
        }
        return imageItems;
    }

    private void reloadAllData() {
        // get new modified random data
        List<SingleProductModel> objects = getData();
        // update data in our adapter
        listAdapter.getData().clear();
        listAdapter.getData().addAll(objects);
        // fire the event
        listAdapter.notifyDataSetChanged();
    }

    private class Saved_Car extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();

            String json = sh.makeServiceCall(Saved_Car_url, ServiceHandler.POST );

            if (json != null) {

                saved_car_list = new ArrayList<>();

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

                        savedcarlist = new HashMap<>();

                        savedcarlist.put("make", make);
                        savedcarlist.put("make_id", make_id);
                        savedcarlist.put("model", model);
                        savedcarlist.put("variant", variant);
                        savedcarlist.put("car_locality", car_locality);
                        savedcarlist.put("registration_year", registration_year);
                        savedcarlist.put("kilometer_run", kilometer_run);
                        savedcarlist.put("fuel_type", fuel_type);
                        savedcarlist.put("owner_type", owner_type);
                        savedcarlist.put("price", price);
                        savedcarlist.put("daysstmt", daysstmt);
                        savedcarlist.put("car_id", car_id);
                        savedcarlist.put("dealer_id", dealer_id);
                        savedcarlist.put("bid_image", bid_image);
                        savedcarlist.put("no_images", no_images);
                        savedcarlist.put("imagelinks", imagelinks);
                        savedcarlist.put("saved_car", saved_car);
                        savedcarlist.put("compare_car", compare_car);
                        savedcarlist.put("notify_car", notify_car);
                        savedcarlist.put("view_car", view_car);
                        savedcarlist.put("auction", auction);
                        savedcarlist.put("site_id", site_id);
                        savedcarlist.put("site_image", site_image);

                        saved_car_list.add(savedcarlist);
                        arrayList_alert.add(notify_car);
                        arrayList.add(saved_car);
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

            listAdapter = new SavedCarAdapter(SavedCarActivity.this, getData(), arrayList, arrayList_alert, arrayList_compare);

            if (listAdapter.getCount() != 0) {
                listView.setAdapter(listAdapter);
                listView.invalidateViews();
                reloadAllData();
                listAdapter.notifyDataSetChanged();
            } else {
                listView.setVisibility(View.GONE);
                result_found.setVisibility(View.VISIBLE);
            }

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    SingleProductModel item = (SingleProductModel) parent.getItemAtPosition(position);
                    String url = ConstantIP.IP + "mobileweb/carview/index.html#/carview/" + user.get("user_id") + "/" + item.getCar_id() + "/0";
                    Intent intent = new Intent(SavedCarActivity.this, SellWebViewActivity.class);
                    intent.putExtra("title", "Car Details");
                    intent.putExtra("url", url);
                    startActivity(intent);
                    //Toast.makeText(SavedCarActivity.this, "Selected Car Name :" + item.getMake(), Toast.LENGTH_SHORT).show();
                }
            });

            if (mSwipeRefreshLayout_savedcar.isRefreshing()) {
                mSwipeRefreshLayout_savedcar.setRefreshing(false);
            }

            barProgressDialog.dismiss();

        }
    }

}