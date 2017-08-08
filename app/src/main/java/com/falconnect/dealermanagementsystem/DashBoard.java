package com.falconnect.dealermanagementsystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.falconnect.dealermanagementsystem.Adapter.CustomAdapter;
import com.falconnect.dealermanagementsystem.Adapter.CustomList;
import com.falconnect.dealermanagementsystem.Adapter.Right_sidebar;
import com.falconnect.dealermanagementsystem.FontAdapter.RoundImageTransform;
import com.falconnect.dealermanagementsystem.Model.AlertRightModel;
import com.falconnect.dealermanagementsystem.Model.City_Make_Spinner_Model;
import com.falconnect.dealermanagementsystem.Model.DataModel;
import com.falconnect.dealermanagementsystem.NavigationDrawer.BuyPageNavigation;
import com.falconnect.dealermanagementsystem.SharedPreference.CitySavedSharedPreferences;
import com.falconnect.dealermanagementsystem.SharedPreference.Filter_API;
import com.falconnect.dealermanagementsystem.SharedPreference.JsonValueSharedPreferences;
import com.falconnect.dealermanagementsystem.SharedPreference.PlandetailsSharedPreferences;
import com.falconnect.dealermanagementsystem.SharedPreference.ProfileManagerSession;
import com.falconnect.dealermanagementsystem.SharedPreference.SelectedListFilter;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;
import com.freshdesk.hotline.FaqOptions;
import com.freshdesk.hotline.Hotline;
import com.navdrawer.SimpleSideDrawer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class DashBoard extends AppCompatActivity {

    public static final String PREFS_NAME = "AOP_PREFS";
    public static final String SITE_KEY = "sitekey";
    public static final String CITY_KEY = "citykey";
    private static RecyclerView.Adapter adapter;
    private static RecyclerView recyclerView;
    private static ArrayList<DataModel> data;
    public ArrayList<HashMap<String, String>> make_spinner_list;
    public ArrayList<HashMap<String, String>> model_spinner_list;
    public ArrayList<HashMap<String, String>> budget_spinner_list;
    public ArrayList<HashMap<String, String>> vehi_spinner_list;
    public ArrayList<HashMap<String, String>> site_spinner_list;
    public ArrayList<HashMap<String, String>> alert_spinner_list;
    Context context;
    ImageView nav;
    RelativeLayout nav_refresh_icon;
    //List<String> vehilist;
    Intent intent;
    ArrayList<String> stringArray;
    String selected_city, selected_make, selected_model, selected_budget, selected_vehicle_type;
    HashMap<String, String> makelist;
    HashMap<String, String> modelist;
    HashMap<String, String> budgetlist;
    HashMap<String, String> vehilist;
    ArrayList<City_Make_Spinner_Model> datas;
    Button search;
    RelativeLayout call_layout, chat_layout, logout_layout;
    TextView spinner;
    Spinner bud_spinner, vehi_spinner, mod_spinner, bran_spinner;
    Button bud_mod, by_mod;
    Filter_API filter_api;
    LinearLayout clciknew;
    JsonValueSharedPreferences jsonValue;
    ImageView horn_image;
    ArrayAdapter<String> spinnerArrayAdapter;
    TextView sites;
    String encodedUrl = null;
    int value = 0;
    String upperString;
    ProfileManagerSession profileManagerSession;
    PlandetailsSharedPreferences plandetailsSharedPreferences;
    HashMap<String, String> plan;
    // Initializing a Vehicle Spinner Array
   /* String[] vehi = new String[]{
            "Select Vehicle type",
            "Sedan",
            "Coupe",
            "Hatchback",
            "Minivan",
            "SUV",
            "Wagon"
    };*/
    HashMap<String, String> sitelist;
    String site_id, site_name;
    String get_brand_id, get_brand_name, get_model_id, get_model_name;
    String budget_id, budget_name;
    String category_id, category_description, status;
    ListView list;
    String selectedcity;
    SessionManager session;
    ImageView imageView;
    TextView profile_name;
    HashMap<String, String> user;
    TextView profile_address;
    String saved_name, saved_address, user_id;
    //int bud_mod_value, by_mod_value;
    String sitekey;
    //String sites_get;
    BuyPageNavigation mnavgation;
    SharedPreferences settings;
    ProgressDialog barProgressDialog;
    SharedPreferences.Editor editor;
    String radioinline = String.valueOf(0);
    Button nav_alert, nav_queries;
    RelativeLayout nav_alertIcon;
    String side_url;
    Right_sidebar rightSidebar;
    ListView list_right;
    HashMap<String, String> Alertlist;

    int sideno;
    CitySavedSharedPreferences citySavedSharedPreferences;
    private boolean mVisible;
    private SimpleSideDrawer mNav;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> make_datas, model_datas, budget_datas, vehi_datas;
    private ArrayList<String> site_datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dash_board);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mVisible = true;
        context = this;

        if (mVisible) {
            android.support.v7.app.ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
            mVisible = false;

        } else {
        }

        intialize();

        jsonValue = new JsonValueSharedPreferences(DashBoard.this);
        jsonValue.clear_json();

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        sitekey = getIntent().getStringExtra("sites_array");

        if (sitekey == null) {
            sites.setText("Select Websites");
        } else {
            sitekey = sitekey.replace("]", "").replace("[", "");
            sites.setText(sitekey);
            editor.putString(SITE_KEY, sitekey);
        }

        selectedcity = getIntent().getStringExtra("selected_item");
        if (selectedcity == null) {
            spinner.setText("Select City");
        } else {
            spinner.setText(selectedcity);
            editor.putString(CITY_KEY, selectedcity);
        }

        editor.commit();
        Log.e("Site_TAF", settings.getAll().toString());

        sites.setText(settings.getString(SITE_KEY, "Select Websites"));
        spinner.setText(settings.getString(CITY_KEY, "Select City"));

        make_datas = new ArrayList<String>();
        model_datas = new ArrayList<String>();
        budget_datas = new ArrayList<String>();
        site_datas = new ArrayList<String>();
        vehi_datas = new ArrayList<String>();

        budget_datas.add("Select Budget");
        make_datas.add("Select Brand");
        model_datas.add("Select Model");
        model_datas.add("Select Model");
        vehi_datas.add("All Body Type");

        spinnerArrayAdapter = new ArrayAdapter<String>(DashBoard.this, R.layout.spinner_single_item, budget_datas);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_single_item);
        bud_spinner.setAdapter(spinnerArrayAdapter);

        spinnerArrayAdapter = new ArrayAdapter<String>(DashBoard.this, R.layout.spinner_single_item, make_datas);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_single_item);
        bran_spinner.setAdapter(spinnerArrayAdapter);

        spinnerArrayAdapter = new ArrayAdapter<String>(DashBoard.this, R.layout.spinner_single_item, model_datas);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_single_item);
        mod_spinner.setAdapter(spinnerArrayAdapter);

        spinnerArrayAdapter = new ArrayAdapter<String>(DashBoard.this, R.layout.spinner_single_item, vehi_datas);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_single_item);
        vehi_spinner.setAdapter(spinnerArrayAdapter);

        nav = (ImageView) findViewById(R.id.nav_icon_drawer);

        nav_alertIcon = (RelativeLayout) findViewById(R.id.nav_alert_icon);
        nav_refresh_icon = (RelativeLayout) findViewById(R.id.nav_refresh_icon);

        mNav = new SimpleSideDrawer(this);
        mNav.setLeftBehindContentView(R.layout.activity_behind_left_simple);
        mNav.setRightBehindContentView(R.layout.activity_behind_right_simple);

        plandetailsSharedPreferences = new PlandetailsSharedPreferences(DashBoard.this);
        plan = plandetailsSharedPreferences.getUserDetails();

        imageView = (ImageView) mNav.findViewById(R.id.profile_avatar);
        profile_name = (TextView) mNav.findViewById(R.id.profile_name);
        profile_address = (TextView) mNav.findViewById(R.id.profile_address);

        session = new SessionManager(DashBoard.this);
        user = session.getUserDetails();
        saved_name = user.get("dealer_name");
        saved_address = user.get("dealershipname");
        profile_name.setText(saved_name);

        if (user.get("dealer_img").isEmpty()) {
            Glide.with(getApplicationContext())
                    .load(R.drawable.default_avatar)
                    .transform(new RoundImageTransform(DashBoard.this))
                    .into(imageView);
        } else {
            Glide.with(getApplicationContext())
                    .load(user.get("dealer_img"))
                    .transform(new RoundImageTransform(DashBoard.this))
                    .into(imageView);
        }
        profile_address.setText(saved_address);

        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNav.toggleLeftDrawer();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(DashBoard.this, LinearLayoutManager.HORIZONTAL, false));

        data = new ArrayList<DataModel>();
        for (int i = 0; i < MyData.nameArray.length; i++) {
            data.add(new DataModel(
                    MyData.nameArray[i],
                    MyData.id_[i],
                    MyData.drawableArrayWhite0[i]
            ));
        }

        adapter = new CustomAdapter(DashBoard.this, data);
        recyclerView.setAdapter(adapter);

        nav_alert = (Button) mNav.findViewById(R.id.alert_btn);
        nav_queries = (Button) mNav.findViewById(R.id.queries_btn);


        nav_alertIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                side_url = Constant.ALERT_SIDEBAR + "session_user_id="
                        + user.get("user_id");
                sideno = 0;
                barProgressDialog = ProgressDialog.show(DashBoard.this, "Loading...", "Please Wait ...", true);
                new Alert_Datas().execute();

                mNav.toggleRightDrawer();

            }
        });


        nav_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nav_queries.setBackgroundResource(R.drawable.alert);
                nav_alert.setBackgroundResource(R.drawable.chat);
                nav_queries.setTextColor(Color.WHITE);
                nav_alert.setTextColor(Color.parseColor("#173E84"));

                side_url = Constant.ALERT_SIDEBAR + "session_user_id="
                        + user.get("user_id");
                sideno = 0;
                barProgressDialog = ProgressDialog.show(DashBoard.this, "Loading...", "Please Wait ...", true);
                new Alert_Datas().execute();
            }
        });

        nav_queries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nav_alert.setBackgroundResource(R.drawable.alert);
                nav_queries.setBackgroundResource(R.drawable.chat);
                nav_queries.setTextColor(Color.parseColor("#173E84"));
                nav_alert.setTextColor(Color.WHITE);
                side_url = Constant.QUERIES_SIDEBAR + "session_user_id="
                        + user.get("user_id");
                sideno = 1;
                barProgressDialog = ProgressDialog.show(DashBoard.this, "Loading...", "Please Wait ...", true);
                new Alert_Datas().execute();
            }
        });

        //Button By Model
        by_mod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioinline = String.valueOf(1);
                by_mod.setBackgroundResource(R.drawable.budget_model);
                bud_mod.setBackgroundResource(R.drawable.by_model);
                by_mod.setTextColor(Color.WHITE);
                bud_mod.setTextColor(Color.BLACK);

                //Visibility GONE spinner
                bud_spinner.setVisibility(View.GONE);
                vehi_spinner.setVisibility(View.GONE);

                //VISIBLE SPINNER
                mod_spinner.setVisibility(View.VISIBLE);
                bran_spinner.setVisibility(View.VISIBLE);

            }
        });

        clciknew = (LinearLayout) findViewById(R.id.clciknew);
        clciknew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        horn_image = (ImageView) findViewById(R.id.horn_image);
        horn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //Button Budget Modesession_query_receivel
        bud_mod.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                radioinline = String.valueOf(0);
                by_mod.setBackgroundResource(R.drawable.by_model);
                bud_mod.setBackgroundResource(R.drawable.budget_model);
                by_mod.setTextColor(Color.BLACK);
                bud_mod.setTextColor(Color.WHITE);

                //Visibility GONE spinner
                mod_spinner.setVisibility(View.GONE);
                bran_spinner.setVisibility(View.GONE);

                //VISIBLE SPINNER
                bud_spinner.setVisibility(View.VISIBLE);
                vehi_spinner.setVisibility(View.VISIBLE);
            }
        });


        call_layout = (RelativeLayout) mNav.findViewById(R.id.call_layout);
        chat_layout = (RelativeLayout) mNav.findViewById(R.id.chat_layout);
        logout_layout = (RelativeLayout) mNav.findViewById(R.id.logout_layout);

        call_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertbox = new AlertDialog.Builder(DashBoard.this)
                        .setMessage("Do you want to Call?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:9790928569"));
                                if (ActivityCompat.checkSelfPermission(DashBoard.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
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
                editor.clear();
                editor.commit();
                mNav.closeLeftSide();
                DashBoard.this.finish();
            }
        });

        chat_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chat();
                mNav.closeLeftSide();
            }
        });


        //NAVIGATION DRAWER LIST VIEW
        mnavgation = new BuyPageNavigation();
        CustomList adapter = new CustomList(DashBoard.this, BuyPageNavigation.web, BuyPageNavigation.imageId);
        list = (ListView) findViewById(R.id.nav_list_view);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (BuyPageNavigation.web[position] == "Dashboard") {
                    Intent intent = new Intent(DashBoard.this, MainDashBoardActivity.class);
                    startActivity(intent);
                    DashBoard.this.finish();
                    mNav.closeLeftSide();
                } else if (BuyPageNavigation.web[position] == "Buy") {
                    mNav.closeLeftSide();
                    Toast.makeText(DashBoard.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Sell") {
                    Intent intent = new Intent(DashBoard.this, SellDashBoardActivity.class);
                    startActivity(intent);
                    DashBoard.this.finish();
                    mNav.closeLeftSide();
                } else if (BuyPageNavigation.web[position] == "Manage") {
                    mNav.closeLeftSide();
                    Intent intent = new Intent(DashBoard.this, ManageDashBoardActivity.class);
                    startActivity(intent);
                    DashBoard.this.finish();
                    profileManagerSession = new ProfileManagerSession(DashBoard.this);
                    profileManagerSession.clear_ProfileManage();
                } else if (BuyPageNavigation.web[position] == "Funding") {
                    plandetailsSharedPreferences = new PlandetailsSharedPreferences(DashBoard.this);
                    plan = plandetailsSharedPreferences.getUserDetails();

//                    if (plan.get("PlanName").equals("BASIC")) {
//                        Intent intent = new Intent(DashBoard.this, LoanActivity.class);
//                        startActivity(intent);
//                        mNav.closeLeftSide();
//                        DashBoard.this.finish();
//                    } else {
                        Intent intent = new Intent(DashBoard.this, FundingActivity.class);
                        startActivity(intent);
                        mNav.closeLeftSide();
                        DashBoard.this.finish();
//                    }
                } else if (BuyPageNavigation.web[position] == "Networks") {
                    String url = "http://app.dealerplus.in/user_login_cometchat?session_user_id=" + user.get("user_id");
                    Intent intet = new Intent(DashBoard.this, NetworksWebviewActivity.class);
                    //intet.putExtra("title", BuyPageNavigation.web[position]);
                    intet.putExtra("url", url);
                    startActivity(intet);
                    mNav.closeLeftSide();
                } else if (BuyPageNavigation.web[position] == "Reports") {
                    plandetailsSharedPreferences = new PlandetailsSharedPreferences(DashBoard.this);

                    plan = plandetailsSharedPreferences.getUserDetails();

                    if (plan.get("PlanName").equals("BASIC")) {
                        Intent intent = new Intent(DashBoard.this, BuyPlanActivity.class);
                        startActivity(intent);
                        //MainDashBoardActivity.this.finish();
                        mNav.closeLeftSide();
                    } else {
                        Intent intent = new Intent(DashBoard.this, ReportSalesActivity.class);
                        startActivity(intent);
                        DashBoard.this.finish();
                        mNav.closeLeftSide();
                    }
                } else if (BuyPageNavigation.web[position] == "FAQs") {
                    faqs();
                    mNav.closeLeftSide();
                } else {

                    //Toast.makeText(DashBoard.this, web[position], Toast.LENGTH_SHORT).show();
                }
            }
        });

        spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashBoard.this, CityActivity.class);
                startActivity(i);
                DashBoard.this.finish();
            }
        });

        // XML Parsing Using AsyncTask...
        if (isNetworkAvailable()) {
            new Site_Datas().execute();
            new Budget_Datas().execute();
            new Make_Datas().execute();
            new Vechile_Datas().execute();
        } else {
            Toast.makeText(DashBoard.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            this.finish();
        }

        nav_refresh_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                editor = settings.edit();
                editor.clear();
                editor.commit();
                sites.setText("Select Websites");
                spinner.setText("Select City");

                spinnerArrayAdapter = new ArrayAdapter<String>(DashBoard.this, R.layout.spinner_single_item, make_datas) {
                    @Override
                    public boolean isEnabled(int position) {
                        return position != 0;
                    }

                    @Override
                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        if (position == 0) {
                            tv.setTextColor(Color.GRAY);
                        } else {
                            tv.setTextColor(Color.BLACK);
                        }
                        return view;
                    }
                };
                spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_single_item);
                bran_spinner.setAdapter(spinnerArrayAdapter);
                bran_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedItem_Text = (String) parent.getItemAtPosition(position);
                        if (position == 0) {
                            value = position;
                            selected_make = selectedItem_Text;
                            model_datas.clear();
                            model_datas.add("Select Model");
                            spinnerArrayAdapter = new ArrayAdapter<String>(DashBoard.this, R.layout.spinner_single_item, model_datas);
                            spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_single_item);
                            mod_spinner.setAdapter(spinnerArrayAdapter);
                        } else if (position > 0) {
                            value = position;
                            selected_make = selectedItem_Text;

                            model_datas.clear();
                            new Sub_model().execute();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                spinnerArrayAdapter = new ArrayAdapter<String>(DashBoard.this, R.layout.spinner_single_item, budget_datas) {
                    @Override
                    public boolean isEnabled(int position) {
                        return position != 0;
                    }

                    @Override
                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        if (position == 0) {
                            tv.setTextColor(Color.GRAY);
                        } else {
                            tv.setTextColor(Color.BLACK);
                        }
                        return view;
                    }
                };
                spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_single_item);
                bud_spinner.setAdapter(spinnerArrayAdapter);

                spinnerArrayAdapter = new ArrayAdapter<String>(DashBoard.this, R.layout.spinner_single_item, vehi_datas) {
                    @Override
                    public boolean isEnabled(int position) {
                        return position != 0;
                    }

                    @Override
                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        if (position == 0) {
                            tv.setTextColor(Color.GRAY);
                        } else {
                            tv.setTextColor(Color.BLACK);
                        }
                        return view;
                    }
                };
                spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_single_item);
                vehi_spinner.setAdapter(spinnerArrayAdapter);

            }
        });

        //Button Event
        search_button();

    }

    private ArrayList<AlertRightModel> getContactData() {
        ArrayList<AlertRightModel> contactdata = new ArrayList<>();
        for (int i = 0; i < alert_spinner_list.size(); i++) {

            String message = alert_spinner_list.get(i).get("message");
            String state = alert_spinner_list.get(i).get("state");
            String date = alert_spinner_list.get(i).get("date");

            contactdata.add(new AlertRightModel(message, state, date));
        }
        return contactdata;
    }

    public void intialize() {
        //Spinners
        spinner = (TextView) findViewById(R.id.search_city_spinner);
        bud_spinner = (Spinner) findViewById(R.id.budget_spinner);
        vehi_spinner = (Spinner) findViewById(R.id.vehi_type);
        bran_spinner = (Spinner) findViewById(R.id.brand_spinner);
        mod_spinner = (Spinner) findViewById(R.id.model_spinner);

        //TextView
        sites = (TextView) findViewById(R.id.search_sites);

        //Buttons
        bud_mod = (Button) findViewById(R.id.budget_model);
        by_mod = (Button) findViewById(R.id.by_model);
        search = (Button) findViewById(R.id.search_btn);
    }

    public void faqs() {
        FaqOptions faqOptions = new FaqOptions()
                .showFaqCategoriesAsGrid(true)
                .showContactUsOnAppBar(true)
                .showContactUsOnFaqScreens(false)
                .showContactUsOnFaqNotHelpful(false);

        Hotline.showFAQs(DashBoard.this, faqOptions);
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
                        DashBoard.this.finish();
                        System.exit(0);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();

    }

    public void search_button() {
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                plan = plandetailsSharedPreferences.getUserDetails();
//
//                if (plan.get("PlanName").equals("BASIC")) {
//
//                    Intent inte = new Intent(DashBoard.this, BuyPlanActivity.class);
//                    startActivity(inte);
//
//                } else {

                    filter_api = new Filter_API(DashBoard.this);

                    filter_api.clear_URL();



                    selected_city = spinner.getText().toString();
                    sitekey = sites.getText().toString();
                    user_id = user.get("user_id");
//                    if (selected_city == "Select City" || sitekey == "Select Site") {
//                        AlertDialog alertbox = new AlertDialog.Builder(DashBoard.this)
//                                .setMessage("You Must Select Your Site and City")
//                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface arg0, int arg1) {
//
//                                    }
//                                })
//                                .show();
//
//                    } else {


                        upperString = sitekey.substring(0, 1).toUpperCase() + sitekey.substring(1);
                        try {
                            upperString = upperString.replace(" ", "");
                            upperString = URLEncoder.encode(upperString, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        if (selected_budget == null) {
                            selected_budget = "";
                        } else {

                        }
                        if (selected_make == null) {
                            selected_make = "";
                        } else {

                        }
                        if (selected_model == null) {
                            selected_model = "";
                        } else {

                        }
                        if (selected_vehicle_type == null) {
                            selected_vehicle_type = "All Body Type";

                            try {
                                selected_vehicle_type = selected_vehicle_type.replace(" ", "%20");
                                selected_vehicle_type = URLEncoder.encode(selected_vehicle_type, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        } else {

                        }

                        Intent j = new Intent(DashBoard.this, SearchResultActivity.class);

                        citySavedSharedPreferences = new CitySavedSharedPreferences(DashBoard.this);
                        citySavedSharedPreferences.createCitySession(selected_city);

                        j.putExtra("City", selected_city);
                        j.putExtra("car_sites", upperString);
                        j.putExtra("radioInline", radioinline);
                        j.putExtra("car_budget", selected_budget);
                        j.putExtra("vehicle_type", selected_vehicle_type);
                        j.putExtra("vehicle_model", selected_model);
                        j.putExtra("vehicle_make", String.valueOf(value));
                        startActivity(j);

                        Log.e("radioInline", radioinline);

                        ArrayList<String> values = new ArrayList<String>();

                        values.add(upperString);
                        values.add(selected_budget);
                        values.add(selected_vehicle_type);
                        values.add(selected_model);
                        values.add(String.valueOf(value));

                        SelectedListFilter selectedListFilter = new SelectedListFilter(DashBoard.this);
                        selectedListFilter.createSelectedItems(DashBoard.this, values);

                        Log.e("dasjkdbasdaskjfkad", values.toString());
//                    }
//                }
            }
        });

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

    private class Alert_Datas extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();


            String json = sh.makeServiceCall(side_url, ServiceHandler.POST);

            if (json != null) {

                alert_spinner_list = new ArrayList<>();

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    if (sideno == 0) {

                        JSONArray city = jsonObj.getJSONArray("alert_history");

                        for (int k = 0; k <= city.length(); k++) {

                            String message, state, date;

                            message = city.getJSONObject(k).getString("alert_title");
                            state = city.getJSONObject(k).getString("alert_type");
                            date = city.getJSONObject(k).getString("alert_listingid");


                            Alertlist = new HashMap<>();

                            Alertlist.put("message", message);
                            Alertlist.put("date", date);
                            Alertlist.put("state", "");

                            alert_spinner_list.add(Alertlist);
                        }
                    } else {

                        JSONArray city = jsonObj.getJSONArray("notification_history");

                        for (int k = 0; k <= city.length(); k++) {

                            String message, state, date;

                            message = city.getJSONObject(k).getString("title");
                            state = city.getJSONObject(k).getString("notification_type");
                            date = city.getJSONObject(k).getString("created_at");


                            Alertlist = new HashMap<>();

                            Alertlist.put("message", message);
                            Alertlist.put("date", date);
                            Alertlist.put("state", state);

                            alert_spinner_list.add(Alertlist);
                        }
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

            list_right = (ListView) findViewById(R.id.nav_list_view_right);
            rightSidebar = new Right_sidebar(DashBoard.this, getContactData());
            list_right.setAdapter(rightSidebar);

        }
    }

    private class Site_Datas extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();

            String city_url = Constant.DASH_BOARD_SPINNER_API;

            String json = sh.makeServiceCall(city_url, ServiceHandler.GET);

            if (json != null) {

                site_spinner_list = new ArrayList<>();

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    JSONArray city = jsonObj.getJSONArray("site_names");

                    for (int k = 0; k <= city.length(); k++) {

                        site_id = city.getJSONObject(k).getString("id");
                        site_name = city.getJSONObject(k).getString("sitename");

                        sitelist = new HashMap<>();

                        sitelist.put("id", site_id);
                        sitelist.put("sitename", site_name);

                        site_spinner_list.add(sitelist);

                        site_datas.add(site_name);
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
            sites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(DashBoard.this, SitesActivity.class);
                    i.putExtra("string_array", site_datas);
                    startActivity(i);
                    //Toast.makeText(DashBoard.this, "Sites", Toast.LENGTH_SHORT).show();
                    DashBoard.this.finish();
                }
            });
        }
    }

    private class Make_Datas extends AsyncTask<Void, Void, Void> {
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

                make_spinner_list = new ArrayList<>();

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    JSONArray make = jsonObj.getJSONArray("model_make");

                    for (int j = 0; j <= make.length(); j++) {
                        get_brand_id = make.getJSONObject(j).getString("make_id");
                        get_brand_name = make.getJSONObject(j).getString("makename");

                        makelist = new HashMap<>();

                        makelist.put("make_id", get_brand_id);
                        makelist.put("makename", get_brand_name);

                        make_spinner_list.add(makelist);

                        make_datas.add(get_brand_name);
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

            //Brand Data Get
            spinnerArrayAdapter = new ArrayAdapter<String>(DashBoard.this, R.layout.spinner_single_item, make_datas) {
                @Override
                public boolean isEnabled(int position) {
                    return position != 0;
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };
            spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_single_item);
            bran_spinner.setAdapter(spinnerArrayAdapter);
            bran_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedItem_Text = (String) parent.getItemAtPosition(position);
                    if (position > 0) {
                        value = position;
                        selected_make = selectedItem_Text;
                        new Sub_model().execute();
                    }
                    if (bran_spinner.isClickable()) {
                        model_datas.clear();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    private class Sub_model extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();
            String sub_make = Constant.DASH_BOARD_SUB_SPINNER_API + "make=" + value;
            String json = sh.makeServiceCall(sub_make, ServiceHandler.POST);
            Log.e("jsonjsonjsonjson", json);
            if (json != null) {
                model_spinner_list = new ArrayList<>();
                model_datas.add("Select Model");
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    JSONArray model = jsonObj.getJSONArray("model_makeid");
                    for (int j = 0; j <= model.length(); j++) {
                        get_model_id = model.getJSONObject(j).getString("model_id");
                        get_model_name = model.getJSONObject(j).getString("model_name");
                        modelist = new HashMap<>();
                        modelist.put("model_id", get_model_id);
                        modelist.put("model_name", get_model_name);
                        model_spinner_list.add(modelist);
                        model_datas.add(get_model_name);
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

            //Brand Data Get
            spinnerArrayAdapter = new ArrayAdapter<String>(DashBoard.this, R.layout.spinner_single_item, model_datas) {
                @Override
                public boolean isEnabled(int position) {
                    return position != 0;
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };
            spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_single_item);
            mod_spinner.setAdapter(spinnerArrayAdapter);
            mod_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selected_Item_Text = (String) parent.getItemAtPosition(position);
                    selected_Item_Text = selected_Item_Text.replaceAll(" ", "%20");
                    if (position > 0) {
                        selected_model = selected_Item_Text;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
    }

    private class Budget_Datas extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();

            String budget_url = Constant.DASH_BOARD_SPINNER_API;

            String json = sh.makeServiceCall(budget_url, ServiceHandler.GET);

            datas = new ArrayList<City_Make_Spinner_Model>();

            if (json != null) {

                budget_spinner_list = new ArrayList<>();

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    JSONArray budget = jsonObj.getJSONArray("car_budget");

                    for (int j = 0; j <= budget.length(); j++) {
                        budget_id = budget.getJSONObject(j).getString("id");
                        budget_name = budget.getJSONObject(j).getString("budget_varient_name");

                        budgetlist = new HashMap<>();

                        budgetlist.put("ID", budget_id);
                        budgetlist.put("BUDGET", budget_name);

                        budget_spinner_list.add(budgetlist);

                        budget_datas.add(budget_name);
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

            spinnerArrayAdapter = new ArrayAdapter<String>(DashBoard.this, R.layout.spinner_single_item, budget_datas) {
                @Override
                public boolean isEnabled(int position) {
                    return position != 0;
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };
            spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_single_item);

            bud_spinner.setAdapter(spinnerArrayAdapter);

            bud_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedItemText = (String) parent.getItemAtPosition(position);
                    selectedItemText = selectedItemText.replaceAll(" ", "%20");
                    if (position > 0) {
                        // Toast.makeText(getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT).show();
                        selected_budget = selectedItemText;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }

    }

    private class Vechile_Datas extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();

            String budget_url = Constant.DASH_BOARD_SPINNER_API;

            String json = sh.makeServiceCall(budget_url, ServiceHandler.GET);

            datas = new ArrayList<City_Make_Spinner_Model>();

            if (json != null) {

                vehi_spinner_list = new ArrayList<>();

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    JSONArray Vehicle_type = jsonObj.getJSONArray("Vehicle_type");

                    for (int j = 0; j <= Vehicle_type.length(); j++) {
                        category_id = Vehicle_type.getJSONObject(j).getString("category_id");
                        category_description = Vehicle_type.getJSONObject(j).getString("category_description");

                        vehilist = new HashMap<>();

                        vehilist.put("category_id", category_id);
                        vehilist.put("category_description", category_description);

                        vehi_spinner_list.add(vehilist);

                        vehi_datas.add(category_description);
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
            Log.e("vehi_datas", vehi_datas.toString());
            Log.e("selected_vechi", vehi_datas.toString());

            spinnerArrayAdapter = new ArrayAdapter<String>(DashBoard.this, R.layout.spinner_single_item, vehi_datas) {
                @Override
                public boolean isEnabled(int position) {
                    return position != 0;
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };
            spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_single_item);
            vehi_spinner.setAdapter(spinnerArrayAdapter);

            vehi_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedItemText1 = (String) parent.getItemAtPosition(position);

                    //Toast.makeText(getApplicationContext(), "Selected : " + selectedItemText1, Toast.LENGTH_SHORT).show();
                    selected_vehicle_type = selectedItemText1;
                    try {
                        selected_vehicle_type = selected_vehicle_type.replace(" ", "%20");
                        selected_vehicle_type = URLEncoder.encode(selected_vehicle_type, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }

    }

}
