package com.falconnect.dealermanagementsystem;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.bumptech.glide.Glide;
import com.falconnect.dealermanagementsystem.Adapter.CustomList;
import com.falconnect.dealermanagementsystem.Adapter.ManageFooterCustomAdapter;
import com.falconnect.dealermanagementsystem.Adapter.MyEmployeeListAdapter;
import com.falconnect.dealermanagementsystem.FontAdapter.RoundImageTransform;
import com.falconnect.dealermanagementsystem.Model.ManageFooterDataModel;
import com.falconnect.dealermanagementsystem.Model.MyEmployeeModel;
import com.falconnect.dealermanagementsystem.NavigationDrawer.BuyPageNavigation;
import com.falconnect.dealermanagementsystem.SharedPreference.PlandetailsSharedPreferences;
import com.falconnect.dealermanagementsystem.SharedPreference.ProfileManagerSession;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;
import com.freshdesk.hotline.FaqOptions;
import com.freshdesk.hotline.Hotline;
import com.navdrawer.SimpleSideDrawer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class MyEmployeeActivity extends Activity implements TabHost.OnTabChangeListener {

    private static RecyclerView.Adapter adapter;
    private static RecyclerView employeerecycleview;
    private static ArrayList<ManageFooterDataModel> employeedata;
    public ArrayList<HashMap<String, String>> user_listview;
    public ArrayList<HashMap<String, String>> tab_user_listview;
    public ArrayList<HashMap<String, String>> delete_branch_list;
    ImageView employee_nav;
    SessionManager session;
    ImageView imageView_employee;
    HashMap<String, String> user;
    TextView profile_name_employee;
    ProfileManagerSession profileManagerSession;
    TextView profile_address_employee;
    String saved_name_employee, saved_address_employee;
    HashMap<String, String> userlistview;
    MyEmployeeListAdapter myUserListAdapter;
    int value = 0;
    TabWidget widget;
    ArrayList<String> users;
    BuyPageNavigation employee_buypagenavigation;
    HashMap<String, String> tabuserlistview;
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> lists = new ArrayList<String>();

    ArrayList<String> list_new = new ArrayList<String>();
    ArrayList<String> list_new_id = new ArrayList<String>();
    ImageView plus_employee;
    MyEmployeeModel item;
    String employee_ids;
    HashMap<String, String> deletebranchlist;
    boolean doubleBackToExitPressedOnce = false;
    RelativeLayout result_found;
    PlandetailsSharedPreferences plandetailsSharedPreferences;
    HashMap<String, String> plan;
    private boolean mVisible;
    private RecyclerView.LayoutManager layoutManager;
    private SimpleSideDrawer mNav_employee;
    private SwipeMenuListView employee_listView;
    private ArrayList<MyEmployeeModel> employeedatanew;
    private TabHost mTabHost;
    private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, TabInfo>();

    ImageView search, search_close;
    RelativeLayout relativeLayout1, relativeLayout2;
    private Animation slideRight, slideLeft;
    EditText search_textbox;

    private static void AddTab(MyEmployeeActivity activity, TabHost tabHost, TabHost.TabSpec tabSpec, MyEmployeeActivity.TabInfo tabInfo) {
        tabSpec.setContent(activity.new TabFactory(activity));
        tabHost.addTab(tabSpec);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_employee);
        // this.initialiseTabHost(savedInstanceState);

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mVisible = true;

        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }

        slideRight = AnimationUtils.loadAnimation(this, R.anim.slide_right);
        slideLeft = AnimationUtils.loadAnimation(this, R.anim.slide_left);

        relativeLayout1 = (RelativeLayout) findViewById(R.id.header_name);
        relativeLayout2 = (RelativeLayout) findViewById(R.id.search_name);


        search = (ImageView) findViewById(R.id.search_button_header);

        search_close = (ImageView) findViewById(R.id.close_button);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout2.startAnimation(slideRight);
                relativeLayout2.setVisibility(View.VISIBLE);
                relativeLayout1.setVisibility(View.GONE);
                /*InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);*/
            }
        });

        search_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager in = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(search_textbox.getWindowToken(), 0);

                relativeLayout1.setVisibility(View.VISIBLE);
                relativeLayout1.startAnimation(slideLeft);
                relativeLayout2.startAnimation(slideLeft);
                relativeLayout2.setVisibility(View.GONE);
                search_textbox.setText("");
                search_textbox.clearFocus();

                new my_employee_view().execute();

            }
        });

        //this.intialiseViewPager();

        employee_nav = (ImageView) findViewById(R.id.main_dashboard_mnav);
        employeerecycleview = (RecyclerView) findViewById(R.id.employee_recyclerview);
        employeerecycleview.setHasFixedSize(true);
        employeerecycleview.setLayoutManager(new LinearLayoutManager(MyEmployeeActivity.this, LinearLayoutManager.HORIZONTAL, false));
        employeedata = new ArrayList<ManageFooterDataModel>();
        for (int i = 0; i < MyFooterManageData.managenameArray.length; i++) {

            employeedata.add(new ManageFooterDataModel(
                    MyFooterManageData.managenameArray[i],
                    MyFooterManageData.managedrawableArrayWhite41[i],
                    MyFooterManageData.manageid_[i]
            ));
        }
        adapter = new ManageFooterCustomAdapter(MyEmployeeActivity.this, employeedata);
        employeerecycleview.setAdapter(adapter);

        session = new SessionManager(MyEmployeeActivity.this);
        user = session.getUserDetails();

        mNav_employee = new SimpleSideDrawer(this);
        mNav_employee.setLeftBehindContentView(R.layout.activity_behind_left_simple);

        imageView_employee = (ImageView) mNav_employee.findViewById(R.id.profile_avatar);
        profile_name_employee = (TextView) mNav_employee.findViewById(R.id.profile_name);
        profile_address_employee = (TextView) mNav_employee.findViewById(R.id.profile_address);
        user = session.getUserDetails();

        RelativeLayout call_layout = (RelativeLayout) mNav_employee.findViewById(R.id.call_layout);
        RelativeLayout chat_layout = (RelativeLayout) mNav_employee.findViewById(R.id.chat_layout);
        RelativeLayout logout_layout = (RelativeLayout) mNav_employee.findViewById(R.id.logout_layout);

        call_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertbox = new AlertDialog.Builder(MyEmployeeActivity.this)
                        .setMessage("Do you want to Call?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:9790928569"));
                                if (ActivityCompat.checkSelfPermission(MyEmployeeActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                if (ActivityCompat.checkSelfPermission(MyEmployeeActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
                mNav_employee.closeLeftSide();
                MyEmployeeActivity.this.finish();
            }
        });

        chat_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chat();
                mNav_employee.closeLeftSide();
            }
        });

        saved_name_employee = user.get("dealer_name");
        saved_address_employee = user.get("dealershipname");
        profile_name_employee.setText(saved_name_employee);
        if (user.get("dealer_img").isEmpty()) {
            Glide.with(getApplicationContext())
                    .load(R.drawable.default_avatar)
                    .transform(new RoundImageTransform(MyEmployeeActivity.this))
                    .into(imageView_employee);
        } else {
            Glide.with(getApplicationContext())
                    .load(user.get("dealer_img"))
                    .transform(new RoundImageTransform(MyEmployeeActivity.this))
                    .into(imageView_employee);
        }
        profile_address_employee.setText(saved_address_employee);

        employee_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNav_employee.toggleLeftDrawer();
            }
        });

        plus_employee = (ImageView) findViewById(R.id.plus_employee);

        //NAVIGATION DRAWER LIST VIEW
        employee_buypagenavigation = new BuyPageNavigation();
        CustomList adapter = new CustomList(MyEmployeeActivity.this, BuyPageNavigation.web, BuyPageNavigation.imageId);
        ListView list = (ListView) findViewById(R.id.nav_list_view);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (BuyPageNavigation.web[position] == "Dashboard") {
                    Intent intent = new Intent(MyEmployeeActivity.this, MainDashBoardActivity.class);
                    startActivity(intent);
                    MyEmployeeActivity.this.finish();
                    mNav_employee.closeLeftSide();
                    //  Toast.makeText(MyEmployeeActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();

                } else if (BuyPageNavigation.web[position] == "Buy") {
                    Intent intent = new Intent(MyEmployeeActivity.this, DashBoard.class);
                    startActivity(intent);
                    MyEmployeeActivity.this.finish();
                    mNav_employee.closeLeftSide();
                    //   Toast.makeText(MyEmployeeActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();

                } else if (BuyPageNavigation.web[position] == "Sell") {
                    Intent intent = new Intent(MyEmployeeActivity.this, SellDashBoardActivity.class);
                    startActivity(intent);
                    MyEmployeeActivity.this.finish();
                    mNav_employee.closeLeftSide();
                    //    Toast.makeText(MyEmployeeActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Manage") {
                    profileManagerSession = new ProfileManagerSession(MyEmployeeActivity.this);
                    profileManagerSession.clear_ProfileManage();
                    Intent intent = new Intent(MyEmployeeActivity.this, ManageDashBoardActivity.class);
                    startActivity(intent);
                    mNav_employee.closeLeftSide();
                    //    Toast.makeText(MyEmployeeActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Funding") {
                    plandetailsSharedPreferences = new PlandetailsSharedPreferences(MyEmployeeActivity.this);
                    plan = plandetailsSharedPreferences.getUserDetails();

//                    if ( plan.get("PlanName").equals("BASIC")) {
//
//                        Intent intent = new Intent(MyEmployeeActivity.this, LoanActivity.class);
//                        startActivity(intent);
//                        mNav_employee.closeLeftSide();
//                        MyEmployeeActivity.this.finish();
//                    } else {
                        Intent intent = new Intent(MyEmployeeActivity.this, FundingActivity.class);
                        startActivity(intent);
                        mNav_employee.closeLeftSide();
                        MyEmployeeActivity.this.finish();
//                    }
                    //    Toast.makeText(MyEmployeeActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Networks") {
                    String url = "http://app.dealerplus.in/user_login_cometchat?session_user_id=" + user.get("user_id");
                    Intent intet = new Intent(MyEmployeeActivity.this, NetworksWebviewActivity.class);
                    //intet.putExtra("title", BuyPageNavigation.web[position]);
                    intet.putExtra("url", url);
                    startActivity(intet);
                    mNav_employee.closeLeftSide();
                    //    Toast.makeText(MyEmployeeActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Reports") {
                    plandetailsSharedPreferences = new PlandetailsSharedPreferences(MyEmployeeActivity.this);
                    plan = plandetailsSharedPreferences.getUserDetails();
                    if (plan.get("PlanName").equals("BASIC")) {
                        Intent intent = new Intent(MyEmployeeActivity.this, BuyPlanActivity.class);
                        startActivity(intent);
                        //MainDashBoardActivity.this.finish();
                        mNav_employee.closeLeftSide();
                    } else {
                        Intent intent = new Intent(MyEmployeeActivity.this, ReportSalesActivity.class);
                        startActivity(intent);
                        MyEmployeeActivity.this.finish();
                        mNav_employee.closeLeftSide();
                    }
                    //   Toast.makeText(MyEmployeeActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "FAQs") {
                    faqs();
                    mNav_employee.closeLeftSide();
                } else {
                    //Toast.makeText(DashBoard.this, web[position], Toast.LENGTH_SHORT).show();
                }
            }
        });

        search_textbox = (EditText) findViewById(R.id.searchview_edit_text);

        search_textbox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    search_textbox.clearFocus();
                    InputMethodManager in = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(search_textbox.getWindowToken(), 0);

                    new employee_search().execute();

                    return true;

                }
                return false;
            }
        });

        new tab_my_user_view().execute();

        new my_employee_view().execute();

    }


    @Override
    public void onBackPressed() {

        AlertDialog alertbox = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogNewStyle)
                .setMessage("Do you want to exit application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        MyEmployeeActivity.this.finish();
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

        Hotline.showFAQs(MyEmployeeActivity.this, faqOptions);
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
            value = pos;
            new my_employee_view().execute();
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
        widget = mTabHost.getTabWidget();
        mTabHost.setup();
        MyEmployeeActivity.TabInfo tabInfo = null;
        int j;
        for (j = 0; j < list_new.size(); j++) {
            AddTab(this, this.mTabHost,
                    this.mTabHost.newTabSpec(list_new.get(j).toString()).setIndicator(list_new.get(j).toString()),
                    (tabInfo = new MyEmployeeActivity.TabInfo("Tab", MyEmployeeActivity.class)));
            this.mapTabInfo.put(tabInfo.tag, tabInfo);

            Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/sanz.ttf");
            TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(j).findViewById(android.R.id.title);
            tv.setTypeface(myTypeface, Typeface.BOLD);
            tv.setTextColor(Color.WHITE);
            tv.setAllCaps(false);
        }
        //mTabHost.getTabWidget().setStripEnabled(true);

        mTabHost.setOnTabChangedListener(this);

    }

    private ArrayList<MyEmployeeModel> getUserData() {
        employeedatanew = new ArrayList<>();
        for (int i = 0; i < user_listview.size(); i++) {

            String employee_id = user_listview.get(i).get("employee_id");
            String employee_type = user_listview.get(i).get("employee_type");
            String employee_name = user_listview.get(i).get("employee_name");
            String employee_gender = user_listview.get(i).get("employee_gender");
            String employee_mobile = user_listview.get(i).get("employee_mobile");
            String employee_landline = user_listview.get(i).get("employee_landline");
            String employee_email = user_listview.get(i).get("employee_email");
            String employee_address = user_listview.get(i).get("employee_address");
            String employee_location = user_listview.get(i).get("employee_location");
            String employee_pincode = user_listview.get(i).get("employee_pincode");
            String contactimage = user_listview.get(i).get("contactimage");
            String status = user_listview.get(i).get("status");
            String employee_designation = user_listview.get(i).get("employee_designation");
            //String employee_document = user_listview.get(i).get("employee_document");

            employeedatanew.add(new MyEmployeeModel(employee_id, employee_type, employee_name, employee_gender,
                    employee_mobile, employee_landline,
                    employee_email, employee_address, employee_location,
                    employee_pincode, contactimage, status, employee_designation));
            //, employee_document));
        }
        return employeedatanew;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    public void onRestart() {
        super.onRestart();
        startActivity(getIntent());
        finish();
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

            list.add("Select Employee");
            lists.add("Select Employee");

            if (json != null) {

                tab_user_listview = new ArrayList<>();

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    JSONArray loan = jsonObj.getJSONArray("employee_types");

                    for (int k = 0; k <= loan.length(); k++) {

                        String employee_type_id = loan.getJSONObject(k).getString("employee_type_id");
                        String employee_type = loan.getJSONObject(k).getString("employee_type");

                        tabuserlistview = new HashMap<>();

                        tabuserlistview.put("employee_type_id", employee_type_id);
                        tabuserlistview.put("employee_type", employee_type);

                        tab_user_listview.add(tabuserlistview);

                        list.add(employee_type.toString());
                        lists.add(employee_type.toString());
                        list_new.add(employee_type.toString());
                        list_new_id.add(employee_type_id.toString());
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

            MyEmployeeActivity.this.initialiseTabHost();

            plus_employee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(1);
                    Intent intent = new Intent(MyEmployeeActivity.this, AddEmployeeActivity.class);
                    intent.putExtra("role_list", list);
                    startActivity(intent);
                }
            });

        }

    }

    private class my_employee_view extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();
            String queriesurl = Constant.EMPLOYEE_VIEW +
                    "session_user_id=" + user.get("user_id")
                    + "&page_name=viewemployeelist"
                    + "&employee_type=" + value;

            Log.e("queriesurl", queriesurl);

            String json = sh.makeServiceCall(queriesurl, ServiceHandler.POST);

            if (json != null) {

                user_listview = new ArrayList<>();

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    JSONArray loan = jsonObj.getJSONArray("employee_list");

                    for (int k = 0; k <= loan.length(); k++) {

                        String employee_id = loan.getJSONObject(k).getString("employee_id");
                        String employee_type = loan.getJSONObject(k).getString("employee_type");
                        String employee_name = loan.getJSONObject(k).getString("employee_name");
                        String employee_gender = loan.getJSONObject(k).getString("employee_gender");
                        String employee_mobile = loan.getJSONObject(k).getString("employee_mobile");
                        String employee_landline = loan.getJSONObject(k).getString("employee_landline");
                        String employee_email = loan.getJSONObject(k).getString("employee_email");
                        String employee_address = loan.getJSONObject(k).getString("employee_address");
                        String employee_location = loan.getJSONObject(k).getString("employee_location");
                        String employee_pincode = loan.getJSONObject(k).getString("employee_pincode");
                        String contactimage = loan.getJSONObject(k).getString("contactimage");
                        String status = loan.getJSONObject(k).getString("status");
                        String employee_designation = loan.getJSONObject(k).getString("employee_designation");
                        //String employee_document = loan.getJSONObject(k).getString("employee_document");

                        userlistview = new HashMap<>();

                        userlistview.put("employee_id", employee_id);
                        userlistview.put("employee_type", employee_type);
                        userlistview.put("employee_name", employee_name);
                        userlistview.put("employee_gender", employee_gender);
                        userlistview.put("employee_mobile", employee_mobile);
                        userlistview.put("employee_landline", employee_landline);
                        userlistview.put("employee_email", employee_email);
                        userlistview.put("employee_address", employee_address);
                        userlistview.put("employee_location", employee_location);
                        userlistview.put("employee_pincode", employee_pincode);
                        userlistview.put("contactimage", contactimage);
                        userlistview.put("employee_designation", employee_designation);
                        //userlistview.put("employee_document", employee_document);

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
            employee_listView = (SwipeMenuListView) findViewById(R.id.employee_listView);
            myUserListAdapter = new MyEmployeeListAdapter(MyEmployeeActivity.this, getUserData());
            employee_listView.setAdapter(myUserListAdapter);

           /* result_found = (RelativeLayout) findViewById(R.id.result_found);
            if(myUserListAdapter.getCount()!=0){
                employee_listView.setAdapter(myUserListAdapter);
            }else{
                employee_listView.setVisibility(View.GONE);
                result_found.setVisibility(View.VISIBLE);
            }*/

            // step 1. create a MenuCreator

            SwipeMenuCreator creator = new SwipeMenuCreator() {

                @Override
                public void create(SwipeMenu menu) {
                    SwipeMenuItem edit = new SwipeMenuItem(getApplicationContext());
                    edit.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                    edit.setWidth(dp2px(90));
                    edit.setIcon(R.drawable.swipe_edit);
                    menu.addMenuItem(edit);

                    SwipeMenuItem delete = new SwipeMenuItem(getApplicationContext());
                    delete.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                    delete.setWidth(dp2px(90));
                    delete.setIcon(R.drawable.swipe_delete);
                    menu.addMenuItem(delete);

                }
            };
            // set creator
            employee_listView.setMenuCreator(creator);

            // step 2. listener item click event
            employee_listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                    if (employeedatanew == null || employeedatanew.size() <= 0) {

                    } else {


                        switch (index) {
                            case 0:
                                item = employeedatanew.get(position);
                                users = new ArrayList<String>();
                                users.add(item.getContactimage());
                                users.add(item.getEmployee_name());
                                users.add(item.getEmployee_mobile());
                                users.add(item.getEmployee_email());
                                users.add(item.getEmployee_id());
                                users.add(item.getEmployee_type());
                                users.add(item.getEmployee_gender());
                                users.add(item.getEmployee_designation());

                                lists.remove(1);

                                Intent intent = new Intent(MyEmployeeActivity.this, EditEmployeeActivity.class);
                                intent.putExtra("employee_list", users);
                                intent.putExtra("role_list", lists);
                                startActivity(intent);
                                break;
                            case 1:
                                item = employeedatanew.get(position);
                                employee_ids = item.getEmployee_id();
                                AlertDialog alertbox = new AlertDialog.Builder(MyEmployeeActivity.this, R.style.AppCompatAlertDialogNewStyle).setMessage("Do you want to Delete this Employee?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                new employee_api_delete().execute();
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface arg0, int arg1) {
                                            }
                                        })
                                        .show();

                                break;
                        }
                    }
                    return false;
                }
            });

            // set SwipeListener
            employee_listView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

                @Override
                public void onSwipeStart(int position) {
                    // swipe start
                }

                @Override
                public void onSwipeEnd(int position) {
                    // swipe end
                }
            });

            // set MenuStateChangeListener
            employee_listView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
                @Override
                public void onMenuOpen(int position) {
                }

                @Override
                public void onMenuClose(int position) {
                }
            });
        }
    }

    private class employee_api_delete extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();
            String queriesurl = Constant.DELETE_EMPLOYEE +
                    "session_user_id=" + user.get("user_id")
                    + "&page_name=deleteemployee"
                    + "&employee_id=" + employee_ids;

            Log.e("queriesurl", queriesurl);

            String json = sh.makeServiceCall(queriesurl, ServiceHandler.POST);

            if (json != null) {

                delete_branch_list = new ArrayList<>();
                try {
                    JSONObject jsonObj = new JSONObject(json);

                    for (int k = 0; k <= jsonObj.length(); k++) {

                        String Result = jsonObj.getString("Result");
                        String Message = jsonObj.getString("message");

                        deletebranchlist = new HashMap<>();

                        deletebranchlist.put("Result", Result);
                        deletebranchlist.put("Message", Message);

                        delete_branch_list.add(deletebranchlist);

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

            if (deletebranchlist.get("Result").equals("3")) {
                new my_employee_view().execute();
            } else {

            }
        }
    }

    private class employee_search extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();
            String decode = search_textbox.getText().toString();

            try {
                decode = URLEncoder.encode(decode, "UTF-8");
            } catch (Exception e) {

            }
            String queriesurl = Constant.EMPLOYEE_SEARCH +
                    "session_user_id=" + user.get("user_id")
                    + "&searchemployee="+ decode
                    + "&employee_type=" + value;

            Log.e("queriesurl", queriesurl);

            String json = sh.makeServiceCall(queriesurl, ServiceHandler.POST);

            if (json != null) {

                user_listview = new ArrayList<>();

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    JSONArray loan = jsonObj.getJSONArray("employee_list");

                    for (int k = 0; k <= loan.length(); k++) {

                        String employee_id = loan.getJSONObject(k).getString("employee_id");
                        String employee_type = loan.getJSONObject(k).getString("employee_type");
                        String employee_name = loan.getJSONObject(k).getString("employee_name");
                        String employee_gender = loan.getJSONObject(k).getString("employee_gender");
                        String employee_mobile = loan.getJSONObject(k).getString("employee_mobile");
                        String employee_landline = loan.getJSONObject(k).getString("employee_landline");
                        String employee_email = loan.getJSONObject(k).getString("employee_email");
                        String employee_address = loan.getJSONObject(k).getString("employee_address");
                        String employee_location = loan.getJSONObject(k).getString("employee_location");
                        String employee_pincode = loan.getJSONObject(k).getString("employee_pincode");
                        String contactimage = loan.getJSONObject(k).getString("contactimage");
                        String status = loan.getJSONObject(k).getString("status");
                        String employee_designation = loan.getJSONObject(k).getString("employee_designation");
                        //String employee_document = loan.getJSONObject(k).getString("employee_document");

                        userlistview = new HashMap<>();

                        userlistview.put("employee_id", employee_id);
                        userlistview.put("employee_type", employee_type);
                        userlistview.put("employee_name", employee_name);
                        userlistview.put("employee_gender", employee_gender);
                        userlistview.put("employee_mobile", employee_mobile);
                        userlistview.put("employee_landline", employee_landline);
                        userlistview.put("employee_email", employee_email);
                        userlistview.put("employee_address", employee_address);
                        userlistview.put("employee_location", employee_location);
                        userlistview.put("employee_pincode", employee_pincode);
                        userlistview.put("contactimage", contactimage);
                        userlistview.put("employee_designation", employee_designation);
                        //userlistview.put("employee_document", employee_document);

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
            employee_listView = (SwipeMenuListView) findViewById(R.id.employee_listView);
            myUserListAdapter = new MyEmployeeListAdapter(MyEmployeeActivity.this, getUserData());
            employee_listView.setAdapter(myUserListAdapter);

           /* result_found = (RelativeLayout) findViewById(R.id.result_found);
            if(myUserListAdapter.getCount()!=0){
                employee_listView.setAdapter(myUserListAdapter);
            }else{
                employee_listView.setVisibility(View.GONE);
                result_found.setVisibility(View.VISIBLE);
            }*/

            // step 1. create a MenuCreator

            SwipeMenuCreator creator = new SwipeMenuCreator() {

                @Override
                public void create(SwipeMenu menu) {
                    SwipeMenuItem edit = new SwipeMenuItem(getApplicationContext());
                    edit.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                    edit.setWidth(dp2px(90));
                    edit.setIcon(R.drawable.swipe_edit);
                    menu.addMenuItem(edit);

                    SwipeMenuItem delete = new SwipeMenuItem(getApplicationContext());
                    delete.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                    delete.setWidth(dp2px(90));
                    delete.setIcon(R.drawable.swipe_delete);
                    menu.addMenuItem(delete);

                }
            };
            // set creator
            employee_listView.setMenuCreator(creator);

            // step 2. listener item click event
            employee_listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                    if (employeedatanew == null || employeedatanew.size() <= 0) {

                    } else {


                        switch (index) {
                            case 0:
                                item = employeedatanew.get(position);
                                users = new ArrayList<String>();
                                users.add(item.getContactimage());
                                users.add(item.getEmployee_name());
                                users.add(item.getEmployee_mobile());
                                users.add(item.getEmployee_email());
                                users.add(item.getEmployee_id());
                                users.add(item.getEmployee_type());
                                users.add(item.getEmployee_gender());
                                users.add(item.getEmployee_designation());

                                lists.remove(1);

                                Intent intent = new Intent(MyEmployeeActivity.this, EditEmployeeActivity.class);
                                intent.putExtra("employee_list", users);
                                intent.putExtra("role_list", lists);
                                startActivity(intent);
                                break;
                            case 1:
                                item = employeedatanew.get(position);
                                employee_ids = item.getEmployee_id();
                                AlertDialog alertbox = new AlertDialog.Builder(MyEmployeeActivity.this, R.style.AppCompatAlertDialogNewStyle).setMessage("Do you want to Delete this Employee?")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                new employee_api_delete().execute();
                                            }
                                        })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface arg0, int arg1) {
                                            }
                                        })
                                        .show();

                                break;
                        }
                    }
                    return false;
                }
            });

            // set SwipeListener
            employee_listView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

                @Override
                public void onSwipeStart(int position) {
                    // swipe start
                }

                @Override
                public void onSwipeEnd(int position) {
                    // swipe end
                }
            });

            // set MenuStateChangeListener
            employee_listView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
                @Override
                public void onMenuOpen(int position) {
                }

                @Override
                public void onMenuClose(int position) {
                }
            });
        }
    }

}