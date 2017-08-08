package com.falconnect.dealermanagementsystem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.falconnect.dealermanagementsystem.Adapter.CustomList;
import com.falconnect.dealermanagementsystem.Adapter.GridViewAdapter;
import com.falconnect.dealermanagementsystem.Adapter.GridViewAdapterFirst;
import com.falconnect.dealermanagementsystem.FontAdapter.ExpandedGridView;
import com.falconnect.dealermanagementsystem.FontAdapter.RoundImageTransform;
import com.falconnect.dealermanagementsystem.NavigationDrawer.BuyPageNavigation;
import com.falconnect.dealermanagementsystem.SharedPreference.AddressSavedSharedPreferences;
import com.falconnect.dealermanagementsystem.SharedPreference.InventorySavedDetails;
import com.falconnect.dealermanagementsystem.SharedPreference.LoanFundingSharedPreferences;
import com.falconnect.dealermanagementsystem.SharedPreference.PlandetailsSharedPreferences;
import com.falconnect.dealermanagementsystem.SharedPreference.ProfileManagerSession;
import com.falconnect.dealermanagementsystem.SharedPreference.SavedDetailsContact;
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

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainDashBoardActivity extends Activity {

    public ArrayList<HashMap<String, String>> tab_contact_listview;
    public ArrayList<HashMap<String, String>> count_list;
    BuyPageNavigation maindashboard_navigation;
    SessionManager sessionManager;
    HashMap<String, String> user;
    ImageView imageView_maindashboard;
    TextView profile_name_maindashboard;
    RelativeLayout call_layout, chat_layout, logout_layout;
    TextView profile_address_maindashboard;
    String saved_name_maindashboard, saved_address_maindashboard;
    List<String> list, list_first;
    HashMap<String, String> tabcontactlistview;
    List<String> values, values_first;
    HashMap<String, String> countlist;
    ExpandedGridView gridView, gridView1;
    ArrayList<String> list_new = new ArrayList<String>();
    ArrayList<String> list_new_id_contact = new ArrayList<String>();
    ArrayList<String> list_sample = new ArrayList<String>();
    GridViewAdapter gridViewAdapter;
    GridViewAdapterFirst gridViewAdapterFirst;
    TextView inventory_count, lead_count, customer_count, fund_count;
    TextView date_details, totalamount, amount;
    ImageView nav_refresh;
    ProgressDialog barProgressDialog;
    LoanFundingSharedPreferences loanFundingSharedPreferences;
    InventorySavedDetails inventorySavedDetails;
    HashMap<String, String> user_save;
    HashMap<String, String> user_details;
    SessionManager session;
    ProfileManagerSession profileManagerSession;
    EditText search_textbox;
    RelativeLayout relativeLayout1, relativeLayout2;
    RelativeLayout result_found;
    ImageView search, search_close;
    HashMap<String, String> plan;
    PlandetailsSharedPreferences plandetailsSharedPreferences;
    RelativeLayout logic;
    ScrollView scroll_view;
    private SimpleSideDrawer mNav_maindashboard;
    private Animation slideRight, slideLeft;
    //Permission
    public static final int RequestPermissionCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        setContentView(R.layout.activity_main_dash_board);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActivityCompat.requestPermissions(MainDashBoardActivity.this, new String[]
                {
                        WRITE_EXTERNAL_STORAGE,
                        ACCESS_FINE_LOCATION,
                        ACCESS_COARSE_LOCATION,
                        CALL_PHONE,
                        READ_CONTACTS,
                        READ_EXTERNAL_STORAGE,
                        CAMERA
                }, RequestPermissionCode);


        plandetailsSharedPreferences = new PlandetailsSharedPreferences(MainDashBoardActivity.this);

        ImageView nav_funding = (ImageView) findViewById(R.id.main_dashboard_mnav);
        mNav_maindashboard = new SimpleSideDrawer(this);
        mNav_maindashboard.setLeftBehindContentView(R.layout.activity_behind_left_simple);

        imageView_maindashboard = (ImageView) mNav_maindashboard.findViewById(R.id.profile_avatar);
        profile_name_maindashboard = (TextView) mNav_maindashboard.findViewById(R.id.profile_name);
        profile_address_maindashboard = (TextView) mNav_maindashboard.findViewById(R.id.profile_address);

        // scroll_view = (ScrollView) findViewById(R.id.scroll_view);

        session = new SessionManager(MainDashBoardActivity.this);
        user = session.getUserDetails();
        saved_name_maindashboard = user.get("dealer_name");
        saved_address_maindashboard = user.get("dealershipname");
        profile_name_maindashboard.setText(saved_name_maindashboard);


        if (user.get("dealer_img").isEmpty()) {
            Glide.with(getApplicationContext())
                    .load(R.drawable.default_avatar)
                    .transform(new RoundImageTransform(MainDashBoardActivity.this))
                    .into(imageView_maindashboard);
        } else {
            Glide.with(getApplicationContext())
                    .load(user.get("dealer_img"))
                    .transform(new RoundImageTransform(MainDashBoardActivity.this))
                    .into(imageView_maindashboard);
        }
        profile_address_maindashboard.setText(saved_address_maindashboard);

        nav_funding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNav_maindashboard.toggleLeftDrawer();
            }
        });

        call_layout = (RelativeLayout) mNav_maindashboard.findViewById(R.id.call_layout);
        chat_layout = (RelativeLayout) mNav_maindashboard.findViewById(R.id.chat_layout);
        logout_layout = (RelativeLayout) mNav_maindashboard.findViewById(R.id.logout_layout);

        call_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertbox = new AlertDialog.Builder(MainDashBoardActivity.this)
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
                mNav_maindashboard.closeLeftSide();
                MainDashBoardActivity.this.finish();
            }
        });

        chat_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chat();
                mNav_maindashboard.closeLeftSide();
            }
        });

        slideRight = AnimationUtils.loadAnimation(this, R.anim.slide_right);
        slideLeft = AnimationUtils.loadAnimation(this, R.anim.slide_left);

        relativeLayout1 = (RelativeLayout) findViewById(R.id.header_name);
        relativeLayout2 = (RelativeLayout) findViewById(R.id.search_name);
        logic = (RelativeLayout) findViewById(R.id.logic);
        logic.setVisibility(View.GONE);

        // result_found = (RelativeLayout) findViewById(R.id.result_found);

        search = (ImageView) findViewById(R.id.search_button_header);
        //search_close = (ImageView) findViewById(R.id.close_button);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*relativeLayout2.startAnimation(slideRight);
                relativeLayout2.setVisibility(View.VISIBLE);
                relativeLayout1.setVisibility(View.GONE);
                //scroll_view.isScrollContainer(  );
                *//*InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);*/


              /*  String url = Constant.SEARCH_TEXTBOX + "session_user_id=" + user.get("user_id")
                        + "&city_name=" + ""
                        + "&search_listing=" + decode
                        + "&sorting_category=" + 0
                        + "&page_name=detail_searchpage";*/

                Intent intent = new Intent(MainDashBoardActivity.this, DealerSearchActivity.class);
                startActivity(intent);


            }
        });

        /*search_close.setOnClickListener(new View.OnClickListener() {
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
        });*/

        search_textbox = (EditText) findViewById(R.id.searchview_edit_text);

        /*search_textbox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    String decode = search_textbox.getText().toString();

                    try {
                        decode = URLEncoder.encode(decode, "UTF-8");
                    } catch (Exception e) {

                    }
                    String url = Constant.SEARCH_TEXTBOX + "session_user_id=" + user.get("user_id")
                            + "&city_name=" + ""
                            + "&search_listing=" + decode
                            + "&sorting_category=" + 0
                            + "&page_name=detail_searchpage";
                    Intent intent = new Intent(MainDashBoardActivity.this, SearchResultActivity.class);
                    intent.putExtra("search_word", decode);
                    intent.putExtra("url", url);
                    startActivity(intent);

                    CitySavedSharedPreferences citySavedSharedPreferences = new CitySavedSharedPreferences(MainDashBoardActivity.this);
                    citySavedSharedPreferences.createCitySession(null);

                    search_textbox.clearFocus();
                    InputMethodManager in = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(search_textbox.getWindowToken(), 0);
                    relativeLayout1.setVisibility(View.VISIBLE);
                    relativeLayout1.startAnimation(slideRight);
                    relativeLayout2.startAnimation(slideRight);
                    relativeLayout2.setVisibility(View.GONE);
                    search_textbox.setText("");
                    search_textbox.clearFocus();

                    return true;

                }
                return false;
            }
        });*/

        session = new SessionManager(MainDashBoardActivity.this);

        //NAVIGATION DRAWER LIST VIEW
        maindashboard_navigation = new BuyPageNavigation();
        CustomList adapter = new CustomList(MainDashBoardActivity.this, BuyPageNavigation.web, BuyPageNavigation.imageId);
        ListView listnew = (ListView) findViewById(R.id.nav_list_view);
        listnew.setAdapter(adapter);

        listnew.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (BuyPageNavigation.web[position] == "Home") {
                    mNav_maindashboard.closeLeftSide();
                    //Toast.makeText(MainDashBoardActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Buy") {
                    Intent intent = new Intent(MainDashBoardActivity.this, DashBoard.class);
                    startActivity(intent);
                    mNav_maindashboard.closeLeftSide();
                    MainDashBoardActivity.this.finish();
                    //Toast.makeText(MainDashBoardActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Sell") {
                    Intent intent = new Intent(MainDashBoardActivity.this, SellDashBoardActivity.class);
                    startActivity(intent);
                    MainDashBoardActivity.this.finish();
                    mNav_maindashboard.closeLeftSide();
                    // Toast.makeText(MainDashBoardActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Manage") {
                    Intent intent = new Intent(MainDashBoardActivity.this, ManageDashBoardActivity.class);
                    startActivity(intent);
                    MainDashBoardActivity.this.finish();
                    profileManagerSession = new ProfileManagerSession(MainDashBoardActivity.this);
                    profileManagerSession.clear_ProfileManage();
                    mNav_maindashboard.closeLeftSide();
                    //  Toast.makeText(MainDashBoardActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Funding") {

                    plan = plandetailsSharedPreferences.getUserDetails();

//                    if ( plan.get("PlanName").equals("BASIC")) {
//                        Intent intent = new Intent(MainDashBoardActivity.this, LoanActivity.class);
//                        startActivity(intent);
//                        mNav_maindashboard.closeLeftSide();
//                        MainDashBoardActivity.this.finish();
//                    } else {
                        Intent intent = new Intent(MainDashBoardActivity.this, FundingActivity.class);
                        startActivity(intent);
                        mNav_maindashboard.closeLeftSide();
                        MainDashBoardActivity.this.finish();
//                    }
                    //  Toast.makeText(MainDashBoardActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Networks") {
                    String url = "http://app.dealerplus.in/user_login_cometchat?session_user_id=" + user.get("user_id");
                    Intent intet = new Intent(MainDashBoardActivity.this, NetworksWebviewActivity.class);
                    //intet.putExtra("title", BuyPageNavigation.web[position]);
                    intet.putExtra("url", url);
                    startActivity(intet);
                    mNav_maindashboard.closeLeftSide();
                    MainDashBoardActivity.this.finish();
                    // Toast.makeText(MainDashBoardActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "FAQs") {
                    faqs();
                    mNav_maindashboard.closeLeftSide();
                    //  Toast.makeText(MainDashBoardActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Reports") {

                    plan = plandetailsSharedPreferences.getUserDetails();

                    if (plan.get("PlanName").equals("BASIC")) {
                        Intent intent = new Intent(MainDashBoardActivity.this, BuyPlanActivity.class);
                        startActivity(intent);
                        //MainDashBoardActivity.this.finish();
                        mNav_maindashboard.closeLeftSide();
                    } else {
                        Intent intent = new Intent(MainDashBoardActivity.this, ReportSalesActivity.class);
                        startActivity(intent);
                        MainDashBoardActivity.this.finish();
                        mNav_maindashboard.closeLeftSide();
                    }


                    // Toast.makeText(MainDashBoardActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(DashBoard.this, web[position], Toast.LENGTH_SHORT).show();
                }
            }
        });


        values = new ArrayList<>();
        values_first = new ArrayList<>();

        date_details = (TextView) findViewById(R.id.dates);
        amount = (TextView) findViewById(R.id.amount);
        totalamount = (TextView) findViewById(R.id.noodtext);

        nav_refresh = (ImageView) findViewById(R.id.nav_refresh);
        nav_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Grid_Datas().execute();
            }
        });

        if (isNetworkAvailable()) {
            new Grid_Datas().execute();
            new Plan_Datas().execute();
            new tab_my_user_view().execute();
        } else {
            Toast.makeText(MainDashBoardActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            MainDashBoardActivity.this.finish();
        }

    }

    public void faqs() {
        FaqOptions faqOptions = new FaqOptions()
                .showFaqCategoriesAsGrid(true)
                .showContactUsOnAppBar(true)
                .showContactUsOnFaqScreens(false)
                .showContactUsOnFaqNotHelpful(false);

        Hotline.showFAQs(MainDashBoardActivity.this, faqOptions);
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
                        MainDashBoardActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();

    }

    // Check Internet Connection!!!
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean write = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadPhoneStatePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean Network_state = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean Phone_state = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean contact = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                    boolean Read = grantResults[5] == PackageManager.PERMISSION_GRANTED;
                    boolean camera = grantResults[6] == PackageManager.PERMISSION_GRANTED;
                }

                break;
        }
    }

    private class tab_my_user_view extends AsyncTask<Void, Void, Void> {
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

                tab_contact_listview = new ArrayList<>();

                list_sample.add("Select Contact Type");

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    JSONArray loan = jsonObj.getJSONArray("dealer_contact_type");

                    for (int k = 0; k <= loan.length(); k++) {

                        String contact_type_id = loan.getJSONObject(k).getString("contact_type_id");
                        String contact_type = loan.getJSONObject(k).getString("contact_type");


                        tabcontactlistview = new HashMap<>();

                        tabcontactlistview.put("contact_type_id", contact_type_id);
                        tabcontactlistview.put("contact_type", contact_type);

                        tab_contact_listview.add(tabcontactlistview);

                        list_new.add(contact_type.toString());

                        list_sample.add(contact_type.toString());

                        list_new_id_contact.add(contact_type_id.toString());

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

        }

    }

    private class Plan_Datas extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();

            String queriesurl = Constant.MY_USER_TAB_VIEW + "session_user_id=" + user.get("user_id");

            String json = sh.makeServiceCall(queriesurl, ServiceHandler.POST);

            if (json != null) {

                count_list = new ArrayList<>();


                Log.e("dashboard json", json);

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    JSONArray plan_detail = jsonObj.getJSONArray("planDetails");

                    for (int k = 0; k <= plan_detail.length(); k++) {

                        String plandetail = plan_detail.getJSONObject(k).getString("PlanName");
                        String expired = plan_detail.getJSONObject(k).getString("PlanTypeAllow");

                        Log.e("plan detial---->", plandetail);
                        Log.e("expired--->", expired);

                        plandetailsSharedPreferences.createLoginSession(plandetail, expired);

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


        }
    }

    private class Grid_Datas extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = ProgressDialog.show(MainDashBoardActivity.this, "Loading...", "Please Wait ...", true);

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();

            String queriesurl = Constant.MY_USER_TAB_VIEW + "session_user_id=" + user.get("user_id");

            String json = sh.makeServiceCall(queriesurl, ServiceHandler.POST);

            if (json != null) {

                count_list = new ArrayList<>();


                Log.e("dashboard json", json);

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    JSONArray apply_fund = jsonObj.getJSONArray("dashboard");

                    for (int k = 0; k <= apply_fund.length(); k++) {

                        String leadcount = apply_fund.getJSONObject(k).getString("leadcount");
                        String custmoercount = apply_fund.getJSONObject(k).getString("custmoercount");
                        String inventorycount = apply_fund.getJSONObject(k).getString("inventorycount");
                        String fundingcount = apply_fund.getJSONObject(k).getString("fundingcount");
                        String livecount = apply_fund.getJSONObject(k).getString("livecount");
                        String draftcount = apply_fund.getJSONObject(k).getString("draftcount");
                        String soldcount = apply_fund.getJSONObject(k).getString("countsold");
                        String quriescount = apply_fund.getJSONObject(k).getString("quriescount");
                        String soldamount = apply_fund.getJSONObject(k).getString("soldamount");
                        String currentdate = apply_fund.getJSONObject(k).getString("currentdate");
                        String totalsold = apply_fund.getJSONObject(k).getString("totalsold");

                        countlist = new HashMap<>();

                        countlist.put("leadcount", leadcount);
                        countlist.put("custmoercount", custmoercount);
                        countlist.put("inventorycount", inventorycount);
                        countlist.put("fundingcount", fundingcount);
                        countlist.put("livecount", livecount);
                        countlist.put("draftcount", draftcount);
                        countlist.put("soldcount", soldcount);
                        countlist.put("quriescount", quriescount);
                        countlist.put("soldamount", soldamount);
                        countlist.put("currentdate", currentdate);
                        countlist.put("totalsold", totalsold);

                        count_list.add(countlist);

                        values.add(livecount);
                        values.add(draftcount);
                        values.add(soldcount);
                        values.add(quriescount);

                        values_first.add(inventorycount);
                        values_first.add(leadcount);
                        values_first.add(custmoercount);
                        values_first.add(fundingcount);
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

            logic.setVisibility(View.VISIBLE);

            list = new ArrayList<>();
            list_first = new ArrayList<>();

            list_first.add("Inventory");
            list_first.add("Leads");
            list_first.add("Customer");
            list_first.add("Fund Request");

            list.add("In Stock (Live)");
            list.add("In Progress");
            list.add("Sold This Month");
            list.add("Queries Received");

            gridView1 = (ExpandedGridView) findViewById(R.id.buttons);

            if (values_first.size() == 0) {

                values_first.add("0");
                values_first.add("0");
                values_first.add("0");
                values_first.add("0");

                gridViewAdapterFirst = new GridViewAdapterFirst(MainDashBoardActivity.this, list_first, values_first);
                gridView1.setExpanded(true);
                gridView1.setAdapter(gridViewAdapterFirst);

            } else {

                gridViewAdapterFirst = new GridViewAdapterFirst(MainDashBoardActivity.this, list_first, values_first);
                gridView1.setExpanded(true);
                gridView1.setAdapter(gridViewAdapterFirst);
            }

            gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                    if (position == 0) {
                        String url = ConstantIP.IP + "mobileweb/inventorynew/index.html#/basicInfo/" + user.get("user_id");
                        Intent intent = new Intent(MainDashBoardActivity.this, SellWebViewActivity.class);
                        intent.putExtra("url", url);
                        startActivity(intent);
                        //   Toast.makeText(MainDashBoardActivity.this, "Selected Car Name :", Toast.LENGTH_SHORT).show();

                    } else if (position == 1) {
                        list_sample.remove(1);
                        SavedDetailsContact savedDetailsContact = new SavedDetailsContact(MainDashBoardActivity.this);
                        AddressSavedSharedPreferences addressSavedSharedPreferences = new AddressSavedSharedPreferences(MainDashBoardActivity.this);
                        savedDetailsContact.clear_contact_datas();
                        addressSavedSharedPreferences.clear_address();
                        Intent intent = new Intent(MainDashBoardActivity.this, AddContactActivity.class);
                        intent.putExtra("contact_list", list_sample);
                        startActivity(intent);

                    } else if (position == 2) {
                        SavedDetailsContact savedDetailsContact = new SavedDetailsContact(MainDashBoardActivity.this);
                        AddressSavedSharedPreferences addressSavedSharedPreferences = new AddressSavedSharedPreferences(MainDashBoardActivity.this);
                        savedDetailsContact.clear_contact_datas();
                        addressSavedSharedPreferences.clear_address();
                        Intent intent = new Intent(MainDashBoardActivity.this, AddContactActivity.class);
                        intent.putExtra("contact_list", list_sample);
                        startActivity(intent);

                    } else if (position == 3) {

                        plandetailsSharedPreferences = new PlandetailsSharedPreferences(MainDashBoardActivity.this);

                        plan = plandetailsSharedPreferences.getUserDetails();

//                        || plan.get("PlanName").equals("GOLD")

//                        if (plan.get("PlanName").equals("BASIC")) {
//                            Intent intent = new Intent(MainDashBoardActivity.this, BuyPlanActivity.class);
//                            startActivity(intent);
//                        } else {

                            String values_save = "0";
                            loanFundingSharedPreferences = new LoanFundingSharedPreferences(MainDashBoardActivity.this);
                            loanFundingSharedPreferences.createAddressSession(values_save);
                            user_save = loanFundingSharedPreferences.getAddress_details();

                            //Session Manager
                            sessionManager = new SessionManager(MainDashBoardActivity.this);
                            user_details = sessionManager.getUserDetails();

                            inventorySavedDetails = new InventorySavedDetails(MainDashBoardActivity.this);
                            inventorySavedDetails.createInventoryDetailsSession(user_details.get("dealer_img"), user_details.get("dealer_name"), user_details.get("dealershipname"), user_details.get("dealer_email"), user_details.get("dealer_mobile"), null, null, null, null);

                            Log.e("Loan", user_save.get("loan"));

                            Intent intent = new Intent(MainDashBoardActivity.this, ApplyFundingActivity.class);
                            startActivity(intent);
//                        }
                    }
                }
            });

            gridView = (ExpandedGridView) findViewById(R.id.fourcolumns);

            if (values.size() == 0) {

                countlist = new HashMap<>();

                values.add("0");
                values.add("0");
                values.add("0");
                values.add("0");

                gridViewAdapter = new GridViewAdapter(MainDashBoardActivity.this, list, values, countlist.get("currentdate"));
                gridView.setExpanded(true);
                gridView.setAdapter(gridViewAdapter);

                date_details.setText("null");
                amount.setText("\u20B9" + " " + "null");
                totalamount.setText("No of sales for last six month " + " " + "null" + " " + "cars");


            } else {

                gridViewAdapter = new GridViewAdapter(MainDashBoardActivity.this, list, values, countlist.get("currentdate"));
                gridView.setExpanded(true);
                gridView.setAdapter(gridViewAdapter);

                date_details.setText(countlist.get("currentdate"));
                amount.setText("\u20B9" + " " + countlist.get("soldamount"));
                totalamount.setText("No of sales for last six month " + " " + countlist.get("totalsold") + " " + "cars");

            }


//            plan = plandetailsSharedPreferences.getUserDetails();
//
//            if (plan.get("PlanName") == null) {
//
//            } else {
//
//                if (plan.get("PlanName").equals("BASIC")) {
//                    search.setVisibility(View.GONE);
//                } else {
//                    search.setVisibility(View.VISIBLE);
//                }
//            }

        }
    }
}