package com.falconnect.dealermanagementsystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.falconnect.dealermanagementsystem.Adapter.CustomList;
import com.falconnect.dealermanagementsystem.Adapter.MyInventoryListAdapter;
import com.falconnect.dealermanagementsystem.Adapter.SellFooterCustomAdapter;
import com.falconnect.dealermanagementsystem.FontAdapter.RoundImageTransform;
import com.falconnect.dealermanagementsystem.Model.MyInventoryModel;
import com.falconnect.dealermanagementsystem.Model.SellFooterDataModel;
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

public class SellDashBoardActivity extends FragmentActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {

    private static RecyclerView.Adapter adapter;
    private static RecyclerView inventoryrecycleview;
    private static ArrayList<SellFooterDataModel> inventorydata;
    public ArrayList<HashMap<String, String>> user_listview;
    public ArrayList<HashMap<String, String>> tab_user_listview;
    ImageView inventory_nav;
    SessionManager session;
    ImageView imageView_inventory;
    HashMap<String, String> user;
    ProgressDialog barProgressDialog;
    TextView profile_name_inventory;
    TextView profile_address_inventory;
    String saved_name_user, saved_address_user;
    BuyPageNavigation inventory_buypagenavigation;
    HashMap<String, String> userlistview;
    MyInventoryListAdapter myUserListAdapter;
    RelativeLayout call_layout, chat_layout, logout_layout;
    int value = 0;
    ArrayList<String> arrayList = new ArrayList<>();

    HashMap<String, String> tabuserlistview;
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> list_new = new ArrayList<String>();
    ImageView plus_inventory;
    boolean doubleBackToExitPressedOnce = false;
    RelativeLayout sort;
    PlandetailsSharedPreferences plandetailsSharedPreferences;
    HashMap<String, String> plan;
    private boolean mVisible;
    private RecyclerView.LayoutManager layoutManager;
    private SimpleSideDrawer mNav_inventory;
    private ListView user_listView;
    private TabHost mTabHost;
    private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, TabInfo>();
    int value_sort;


    private static void AddTab(SellDashBoardActivity activity, TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {
        tabSpec.setContent(activity.new TabFactory(activity));
        tabHost.addTab(tabSpec);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sell_dash_board);

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mVisible = true;

        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }

        inventory_nav = (ImageView) findViewById(R.id.inventory_mnav);
        inventoryrecycleview = (RecyclerView) findViewById(R.id.inventory_recyclerview);
        inventoryrecycleview.setHasFixedSize(true);
        inventoryrecycleview.setLayoutManager(new LinearLayoutManager(SellDashBoardActivity.this, LinearLayoutManager.HORIZONTAL, false));
        inventorydata = new ArrayList<SellFooterDataModel>();
        for (int i = 0; i < MyFooterSellData.sellnameArray.length; i++) {
            inventorydata.add(new SellFooterDataModel(
                    MyFooterSellData.sellnameArray[i],
                    MyFooterSellData.selldrawableArrayWhite0[i],
                    MyFooterSellData.sellid_[i]
            ));
        }
        adapter = new SellFooterCustomAdapter(SellDashBoardActivity.this, inventorydata);
        inventoryrecycleview.setAdapter(adapter);

        session = new SessionManager(SellDashBoardActivity.this);
        user = session.getUserDetails();

        mNav_inventory = new SimpleSideDrawer(this);
        mNav_inventory.setLeftBehindContentView(R.layout.activity_behind_left_simple);

        imageView_inventory = (ImageView) mNav_inventory.findViewById(R.id.profile_avatar);
        profile_name_inventory = (TextView) mNav_inventory.findViewById(R.id.profile_name);
        profile_address_inventory = (TextView) mNav_inventory.findViewById(R.id.profile_address);
        user = session.getUserDetails();
        saved_name_user = user.get("dealer_name");
        saved_address_user = user.get("dealershipname");
        profile_name_inventory.setText(saved_name_user);
        if (user.get("dealer_img").isEmpty()) {
            Glide.with(getApplicationContext())
                    .load(R.drawable.default_avatar)
                    .transform(new RoundImageTransform(SellDashBoardActivity.this))
                    .into(imageView_inventory);
        } else {
            Glide.with(getApplicationContext())
                    .load(user.get("dealer_img"))
                    .transform(new RoundImageTransform(SellDashBoardActivity.this))
                    .into(imageView_inventory);
        }
        profile_address_inventory.setText(saved_address_user);

        inventory_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNav_inventory.toggleLeftDrawer();
            }
        });


        call_layout = (RelativeLayout) mNav_inventory.findViewById(R.id.call_layout);
        chat_layout = (RelativeLayout) mNav_inventory.findViewById(R.id.chat_layout);
        logout_layout = (RelativeLayout) mNav_inventory.findViewById(R.id.logout_layout);

        call_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertbox = new AlertDialog.Builder(SellDashBoardActivity.this)
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
                session.logoutUser();
                mNav_inventory.closeLeftSide();
                SellDashBoardActivity.this.finish();
            }
        });

        chat_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chat();
                mNav_inventory.closeLeftSide();
            }
        });

        //NAVIGATION DRAWER LIST VIEW
        inventory_buypagenavigation = new BuyPageNavigation();
        CustomList adapter = new CustomList(SellDashBoardActivity.this, BuyPageNavigation.web, BuyPageNavigation.imageId);
        ListView list = (ListView) findViewById(R.id.nav_list_view);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (BuyPageNavigation.web[position] == "Dashboard") {
                    Intent intent = new Intent(SellDashBoardActivity.this, MainDashBoardActivity.class);
                    startActivity(intent);
                    SellDashBoardActivity.this.finish();
                    mNav_inventory.closeLeftSide();
                    //   Toast.makeText(SellDashBoardActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Buy") {
                    Intent intent = new Intent(SellDashBoardActivity.this, DashBoard.class);
                    startActivity(intent);
                    SellDashBoardActivity.this.finish();
                    mNav_inventory.closeLeftSide();
                    //    Toast.makeText(SellDashBoardActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Sell") {
                    mNav_inventory.closeLeftSide();
                    //  Toast.makeText(SellDashBoardActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Manage") {
                    Intent intent = new Intent(SellDashBoardActivity.this, ManageDashBoardActivity.class);
                    startActivity(intent);
                    SellDashBoardActivity.this.finish();
                    mNav_inventory.closeLeftSide();
                    //   Toast.makeText(SellDashBoardActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Funding") {
                    plandetailsSharedPreferences = new PlandetailsSharedPreferences(SellDashBoardActivity.this);
                    plan = plandetailsSharedPreferences.getUserDetails();

//                    if ( plan.get("PlanName").equals("BASIC")) {
//
//                        Intent intent = new Intent(SellDashBoardActivity.this, LoanActivity.class);
//                        startActivity(intent);
//                        mNav_inventory.closeLeftSide();
//                        SellDashBoardActivity.this.finish();
//                    } else {
                        Intent intent = new Intent(SellDashBoardActivity.this, FundingActivity.class);
                        startActivity(intent);
                        mNav_inventory.closeLeftSide();
                        SellDashBoardActivity.this.finish();
//                    }
                    //  Toast.makeText(SellDashBoardActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Networks") {
                    String url = "http://app.dealerplus.in/user_login_cometchat?session_user_id=" + user.get("user_id");
                    Intent intet = new Intent(SellDashBoardActivity.this, NetworksWebviewActivity.class);
                    //intet.putExtra("title", BuyPageNavigation.web[position]);
                    intet.putExtra("url", url);
                    startActivity(intet);
                    mNav_inventory.closeLeftSide();
                    //   Toast.makeText(SellDashBoardActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Reports") {
                    plandetailsSharedPreferences = new PlandetailsSharedPreferences(SellDashBoardActivity.this);
                    plan = plandetailsSharedPreferences.getUserDetails();
                    if (plan.get("PlanName").equals("BASIC")) {
                        Intent intent = new Intent(SellDashBoardActivity.this, BuyPlanActivity.class);
                        startActivity(intent);
                        //MainDashBoardActivity.this.finish();
                        mNav_inventory.closeLeftSide();
                    } else {
                        Intent intent = new Intent(SellDashBoardActivity.this, ReportSalesActivity.class);
                        startActivity(intent);
                        SellDashBoardActivity.this.finish();
                        mNav_inventory.closeLeftSide();
                    }

                    //  Toast.makeText(SellDashBoardActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "FAQs") {
                    faqs();
                    mNav_inventory.closeLeftSide();
                    // Toast.makeText(SellDashBoardActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(DashBoard.this, web[position], Toast.LENGTH_SHORT).show();
                }
            }
        });

        plus_inventory = (ImageView) findViewById(R.id.plus_inventory);

        sort = (RelativeLayout) findViewById(R.id.inventory_sort);

        new tab_my_inventory_view().execute();
        new my_inventory_view().execute();

        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sorting();
            }
        });

    }

    public void sorting() {

        final List<String> sortinglist = new ArrayList<>();
        sortinglist.add("Price -- High to Low");
        sortinglist.add("Price -- Low to High");
        sortinglist.add("Milage -- High to Low");
        sortinglist.add("Milage -- Low to High");
        sortinglist.add("Year -- New to Old");
        sortinglist.add("Year -- Old to New");

        AlertDialog.Builder alertbox = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogNewStyle)
                .setTitle("Sort By");
        final ArrayAdapter<String> aa1 = new ArrayAdapter<String>(SellDashBoardActivity.this, R.layout.sort_single_item, R.id.list,
                sortinglist);
        alertbox.setSingleChoiceItems(aa1, 0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (sortinglist.get(item) == "Price -- High to Low") {
                    value_sort = 2;
                    arrayList.clear();
                    new my_inventory_view().execute();
                    dialog.dismiss();
                } else if (sortinglist.get(item) == "Price -- Low to High") {
                    value_sort = 1;
                    arrayList.clear();
                    new my_inventory_view().execute();
                    dialog.dismiss();
                } else if (sortinglist.get(item) == "Milage -- High to Low") {
                    value_sort = 3;
                    arrayList.clear();
                    new my_inventory_view().execute();
                    dialog.dismiss();
                } else if (sortinglist.get(item) == "Milage -- Low to High") {
                    value_sort = 4;
                    arrayList.clear();
                    new my_inventory_view().execute();
                    dialog.dismiss();
                } else if (sortinglist.get(item) == "Year -- New to Old") {
                    value_sort = 5;
                    arrayList.clear();
                    new my_inventory_view().execute();
                    dialog.dismiss();
                } else if (sortinglist.get(item) == "Year -- Old to New") {
                    value_sort = 6;
                    new my_inventory_view().execute();
                    dialog.dismiss();

                }
            }
        });
        alertbox.show();


    }


    public void onBackPressed() {


        AlertDialog alertbox = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogNewStyle)
                .setMessage("Do you want to exit application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        SellDashBoardActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();


    }

    public void faqs() {
        FaqOptions faqOptions = new FaqOptions()
                .showFaqCategoriesAsGrid(true)
                .showContactUsOnAppBar(true)
                .showContactUsOnFaqScreens(false)
                .showContactUsOnFaqNotHelpful(false);

        Hotline.showFAQs(SellDashBoardActivity.this, faqOptions);
    }


    public void chat() {
        Hotline.showConversations(getApplicationContext());
        // Hotline.clearUserData(getApplicationContext());
    }

    protected void onSaveInstanceState(Bundle outState) {
        if (mTabHost.getCurrentTabTag() == null) {

        } else {
            outState.putString("tab", mTabHost.getCurrentTabTag()); //save the tab selected
        }
        super.onSaveInstanceState(outState);
    }

    public void onTabChanged(String tag) {

        if (mTabHost.getCurrentTabTag() == null) {

        } else {
            int pos = mTabHost.getCurrentTab();
            value = pos ;
            new my_inventory_view().execute();
        }

    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // TODO Auto-generated method stub

    }

    public void onPageSelected(int position) {
        // TODO Auto-generated method stub
        this.mTabHost.setCurrentTab(position);
    }

    public void onPageScrollStateChanged(int state) {
        // TODO Auto-generated method stub

    }

    private void initialiseTabHost() {
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();
        TabInfo tabInfo = null;
        int j;
        for (j = 0; j < list_new.size(); j++) {
            AddTab(this, this.mTabHost,
                    this.mTabHost.newTabSpec(list_new.get(j).toString()).setIndicator(list_new.get(j).toString()),
                    (tabInfo = new TabInfo("Tab", SellDashBoardActivity.class)));
            this.mapTabInfo.put(tabInfo.tag, tabInfo);

            Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/sanz.ttf");
            TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(j).findViewById(android.R.id.title);
            tv.setTypeface(myTypeface, Typeface.BOLD);
            tv.setTextColor(Color.WHITE);
            tv.setAllCaps(false);
        }

        mTabHost.setOnTabChangedListener(this);

    }

    private ArrayList<MyInventoryModel> getUserData() {
        final ArrayList<MyInventoryModel> inventorydata = new ArrayList<>();
        for (int i = 0; i < user_listview.size(); i++) {

            String image = user_listview.get(i).get("image");
            String car_id = user_listview.get(i).get("car_id");
            String listing_id = user_listview.get(i).get("listing_id");
            String inventory_type = user_listview.get(i).get("inventory_type");
            String dealer_id = user_listview.get(i).get("dealer_id");
            String price = user_listview.get(i).get("price");
            String kms_done = user_listview.get(i).get("kms_done");
            String registration_year = user_listview.get(i).get("registration_year");
            String owner_type = user_listview.get(i).get("owner_type");
            String make = user_listview.get(i).get("make");
            String model = user_listview.get(i).get("model");
            String varient = user_listview.get(i).get("varient");
            String colors = user_listview.get(i).get("colors");
            String fuel_type = user_listview.get(i).get("fuel_type");
            String statuc_number = user_listview.get(i).get("statuc_number");
            String imagecount = user_listview.get(i).get("imagecount");
            String videoscount = user_listview.get(i).get("videoscount");
            String documentcount = user_listview.get(i).get("documentcount");
            String viewscount = user_listview.get(i).get("viewscount");
            String carstatus = user_listview.get(i).get("carstatus");
            String millege = user_listview.get(i).get("millege");


            inventorydata.add(new MyInventoryModel(image, car_id, listing_id, inventory_type, dealer_id,
                    price, kms_done, registration_year, owner_type, make, model, varient, colors, fuel_type,
                    statuc_number, imagecount, videoscount, documentcount, viewscount, carstatus,millege));
        }
        return inventorydata;
    }

    public void listview() {
        new my_inventory_view().execute();
    }

    private class TabInfo {
        private String tag;
        private Class<?> clss;
        private Fragment fragment;

        TabInfo(String tag, Class<?> clazz) {
            this.tag = tag;
            this.clss = clazz;
        }
    }

    class TabFactory implements TabHost.TabContentFactory {

        private final Context mContext;

        public TabFactory(Context context) {
            mContext = context;
        }

        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }

    }

    private class tab_my_inventory_view extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();

            String queriesurl = Constant.MY_USER_TAB_VIEW + "session_user_id=" + user.get("user_id");

            Log.e("queriesurl", queriesurl);

            String json = sh.makeServiceCall(queriesurl, ServiceHandler.POST);

            if (json != null) {

                tab_user_listview = new ArrayList<>();

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    JSONArray loan = jsonObj.getJSONArray("inventory_type");

                    for (int k = 0; k <= loan.length(); k++) {

                        String inventory_type = loan.getJSONObject(k).getString("inventory_type");
                        tabuserlistview = new HashMap<>();

                        tabuserlistview.put("inventory_type", inventory_type);

                        tab_user_listview.add(tabuserlistview);

                        list.add(inventory_type.toString());
                        list_new.add(inventory_type.toString());
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

            SellDashBoardActivity.this.initialiseTabHost();

            plus_inventory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = ConstantIP.IP + "mobileweb/inventorynew/index.html#/basicInfo/" + user.get("user_id");
                    Intent intent = new Intent(SellDashBoardActivity.this, SellWebViewActivity.class);
                    intent.putExtra("url", url);
                    startActivity(intent);
                    //Toast.makeText(SellDashBoardActivity.this, "Selected Car Name :", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private class my_inventory_view extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = ProgressDialog.show(SellDashBoardActivity.this, "Loading...", "Please Wait ...", true);
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();
            String queriesurl = Constant.MY_INVENTORY +
                    "session_user_id=" + user.get("user_id")
                    + "&page_name=viewinventorydashboard"
                    + "&inventory_type_id=" + value
                    + "&sorting_id=" +  value_sort;

            Log.e("queriesurl", queriesurl);

            String json = sh.makeServiceCall(queriesurl, ServiceHandler.POST);

            if (json != null) {

                user_listview = new ArrayList<>();

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    JSONArray loan = jsonObj.getJSONArray("inventory_dashboard");

                    for (int k = 0; k <= loan.length(); k++) {

                        String image = loan.getJSONObject(k).getString("image");
                        String car_id = loan.getJSONObject(k).getString("car_id");
                        String listing_id = loan.getJSONObject(k).getString("listing_id");
                        String inventory_type = loan.getJSONObject(k).getString("inventory_type");
                        String dealer_id = loan.getJSONObject(k).getString("dealer_id");
                        String price = loan.getJSONObject(k).getString("price");
                        String kms_done = loan.getJSONObject(k).getString("kms_done");
                        String registration_year = loan.getJSONObject(k).getString("registration_year");
                        String owner_type = loan.getJSONObject(k).getString("owner_type");
                        String make = loan.getJSONObject(k).getString("make");
                        String model = loan.getJSONObject(k).getString("model");
                        String varient = loan.getJSONObject(k).getString("varient");
                        String colors = loan.getJSONObject(k).getString("colors");
                        String fuel_type = loan.getJSONObject(k).getString("fuel_type");
                        String statuc_number = loan.getJSONObject(k).getString("statuc_number");
                        String imagecount = loan.getJSONObject(k).getString("imagecount");
                        String videoscount = loan.getJSONObject(k).getString("videoscount");
                        String documentcount = loan.getJSONObject(k).getString("documentcount");
                        String viewscount = loan.getJSONObject(k).getString("viewscount");
                        String carstatus = loan.getJSONObject(k).getString("carstatus");
                        String millege = loan.getJSONObject(k).getString("millege");


                        userlistview = new HashMap<>();

                        userlistview.put("image", image);
                        userlistview.put("car_id", car_id);
                        userlistview.put("listing_id", listing_id);
                        userlistview.put("inventory_type", inventory_type);
                        userlistview.put("dealer_id", dealer_id);
                        userlistview.put("price", price);
                        userlistview.put("kms_done", kms_done);
                        userlistview.put("registration_year", registration_year);
                        userlistview.put("owner_type", owner_type);
                        userlistview.put("make", make);
                        userlistview.put("model", model);
                        userlistview.put("varient", varient);
                        userlistview.put("colors", colors);
                        userlistview.put("fuel_type", fuel_type);
                        userlistview.put("statuc_number", statuc_number);
                        userlistview.put("imagecount", imagecount);
                        userlistview.put("videoscount", videoscount);
                        userlistview.put("documentcount", documentcount);
                        userlistview.put("viewscount", viewscount);
                        userlistview.put("carstatus", carstatus);
                        userlistview.put("millege", millege);

                        user_listview.add(userlistview);

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

            user_listView = (ListView) findViewById(R.id.inventory_listView);

            myUserListAdapter = new MyInventoryListAdapter(SellDashBoardActivity.this, getUserData(), value);
            user_listView.setAdapter(myUserListAdapter);
        }

    }


}
