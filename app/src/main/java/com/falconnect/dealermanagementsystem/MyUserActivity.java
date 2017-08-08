package com.falconnect.dealermanagementsystem;

import android.app.ProgressDialog;
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
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
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
import com.falconnect.dealermanagementsystem.Adapter.MyUserListAdapter;
import com.falconnect.dealermanagementsystem.FontAdapter.RoundImageTransform;
import com.falconnect.dealermanagementsystem.Model.ManageFooterDataModel;
import com.falconnect.dealermanagementsystem.Model.MyUserModel;
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

import static com.falconnect.dealermanagementsystem.R.drawable.search_close;

public class MyUserActivity extends FragmentActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {

    private static RecyclerView.Adapter adapter;
    private static RecyclerView userrecycleview;
    private static ArrayList<ManageFooterDataModel> userdata;
    public ArrayList<HashMap<String, String>> user_listview;
    public ArrayList<HashMap<String, String>> tab_user_listview;
    public ArrayList<HashMap<String, String>> delete_branch_list;
    ImageView user_nav;
    SessionManager session;
    ImageView imageView_user;
    HashMap<String, String> user;
    TextView profile_name_user;
    TextView profile_address_user;
    HashMap<String, String> deletebranchlist;
    String saved_name_user, saved_address_user;
    HashMap<String, String> userlistview;
    MyUserListAdapter myUserListAdapter;
    int value = 0;
    String user_count;
    TabWidget widget;
    ArrayList<String> users;
    String id_branch;
    MyUserModel item;
    String User_id;
    BuyPageNavigation user_buypagenavigation;
    HashMap<String, String> tabuserlistview;
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> list_new = new ArrayList<String>();
    ArrayList<String> list_new_id = new ArrayList<String>();
    ImageView plus_myuser;
    ProgressDialog barProgressDialog;
    ProfileManagerSession profileManagerSession;
    boolean doubleBackToExitPressedOnce = false;
    PlandetailsSharedPreferences plandetailsSharedPreferences;
    HashMap<String, String> plan;
    private boolean mVisible;
    private RecyclerView.LayoutManager layoutManager;
    private SimpleSideDrawer mNav_user;
    private SwipeMenuListView user_listView;
    private ArrayList<MyUserModel> userdatanew;
    private TabHost mTabHost;
    private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, TabInfo>();

    ArrayList<String>statusList=new ArrayList<String>();


    ImageView search, search_close;
    RelativeLayout relativeLayout1, relativeLayout2;
    private Animation slideRight, slideLeft;
    EditText search_textbox;

    private static void AddTab(MyUserActivity activity, TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {
        tabSpec.setContent(activity.new TabFactory(activity));
        tabHost.addTab(tabSpec);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_user);
        // this.initialiseTabHost(savedInstanceState);

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mVisible = true;

        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }

        //this.intialiseViewPager();

        user_nav = (ImageView) findViewById(R.id.main_dashboard_mnav);
        userrecycleview = (RecyclerView) findViewById(R.id.user_recyclerview);
        userrecycleview.setHasFixedSize(true);
        userrecycleview.setLayoutManager(new LinearLayoutManager(MyUserActivity.this, LinearLayoutManager.HORIZONTAL, false));
        userdata = new ArrayList<ManageFooterDataModel>();
        for (int i = 0; i < MyFooterManageData.managenameArray.length; i++) {

            userdata.add(new ManageFooterDataModel(
                    MyFooterManageData.managenameArray[i],
                    MyFooterManageData.managedrawableArrayWhite4[i],
                    MyFooterManageData.manageid_[i]
            ));
        }
        adapter = new ManageFooterCustomAdapter(MyUserActivity.this, userdata);
        userrecycleview.setAdapter(adapter);
        barProgressDialog = ProgressDialog.show(MyUserActivity.this, "Loading...", "Please Wait ...", true);

        session = new SessionManager(MyUserActivity.this);
        user = session.getUserDetails();

        mNav_user = new SimpleSideDrawer(this);
        mNav_user.setLeftBehindContentView(R.layout.activity_behind_left_simple);

        imageView_user = (ImageView) mNav_user.findViewById(R.id.profile_avatar);
        profile_name_user = (TextView) mNav_user.findViewById(R.id.profile_name);
        profile_address_user = (TextView) mNav_user.findViewById(R.id.profile_address);
        user = session.getUserDetails();

        RelativeLayout call_layout = (RelativeLayout) mNav_user.findViewById(R.id.call_layout);
        RelativeLayout chat_layout = (RelativeLayout) mNav_user.findViewById(R.id.chat_layout);
        RelativeLayout logout_layout = (RelativeLayout) mNav_user.findViewById(R.id.logout_layout);

        call_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertbox = new AlertDialog.Builder(MyUserActivity.this)
                        .setMessage("Do you want to Call?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:9790928569"));
                                if (ActivityCompat.checkSelfPermission(MyUserActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                if (ActivityCompat.checkSelfPermission(MyUserActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
                mNav_user.closeLeftSide();
                MyUserActivity.this.finish();
            }
        });

        chat_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chat();
                mNav_user.closeLeftSide();
            }
        });

        saved_name_user = user.get("dealer_name");
        saved_address_user = user.get("dealershipname");
        profile_name_user.setText(saved_name_user);
        if (user.get("dealer_img").isEmpty()) {
            Glide.with(getApplicationContext())
                    .load(R.drawable.default_avatar)
                    .transform(new RoundImageTransform(MyUserActivity.this))
                    .into(imageView_user);
        } else {
            Glide.with(getApplicationContext())
                    .load(user.get("dealer_img"))
                    .transform(new RoundImageTransform(MyUserActivity.this))
                    .into(imageView_user);
        }
        profile_address_user.setText(saved_address_user);

        user_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNav_user.toggleLeftDrawer();
            }
        });

        plus_myuser = (ImageView) findViewById(R.id.plus_myuser);


        slideRight = AnimationUtils.loadAnimation(this, R.anim.slide_right);
        slideLeft = AnimationUtils.loadAnimation(this, R.anim.slide_left);

        relativeLayout1 = (RelativeLayout) findViewById(R.id.header_name);
        relativeLayout2 = (RelativeLayout) findViewById(R.id.search_name);


        search = (ImageView) findViewById(R.id.search_button_header);


        search_textbox = (EditText) findViewById(R.id.searchview_edit_text);

        search_textbox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    search_textbox.clearFocus();
                    InputMethodManager in = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(search_textbox.getWindowToken(), 0);

                    new user_search().execute();

                    return true;

                }
                return false;
            }
        });


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

                new my_user_view().execute();

            }
        });

        //NAVIGATION DRAWER LIST VIEW
        user_buypagenavigation = new BuyPageNavigation();
        CustomList adapter = new CustomList(MyUserActivity.this, BuyPageNavigation.web, BuyPageNavigation.imageId);
        ListView list = (ListView) findViewById(R.id.nav_list_view);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (BuyPageNavigation.web[position] == "Dashboard") {
                    Intent intent = new Intent(MyUserActivity.this, MainDashBoardActivity.class);
                    startActivity(intent);
                    MyUserActivity.this.finish();
                    mNav_user.closeLeftSide();
                    //Toast.makeText(MyUserActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();

                } else if (BuyPageNavigation.web[position] == "Buy") {
                    Intent intent = new Intent(MyUserActivity.this, DashBoard.class);
                    startActivity(intent);
                    MyUserActivity.this.finish();
                    mNav_user.closeLeftSide();
                    //Toast.makeText(MyUserActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();

                } else if (BuyPageNavigation.web[position] == "Sell") {
                    Intent intent = new Intent(MyUserActivity.this, SellDashBoardActivity.class);
                    startActivity(intent);
                    MyUserActivity.this.finish();
                    mNav_user.closeLeftSide();
                    //  Toast.makeText(MyUserActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Manage") {
                    mNav_user.closeLeftSide();
                    profileManagerSession = new ProfileManagerSession(MyUserActivity.this);
                    profileManagerSession.clear_ProfileManage();
                    Intent intet = new Intent(MyUserActivity.this, ManageDashBoardActivity.class);
                    startActivity(intet);
                    //   Toast.makeText(MyUserActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Funding") {
                    plandetailsSharedPreferences = new PlandetailsSharedPreferences(MyUserActivity.this);
                    plan = plandetailsSharedPreferences.getUserDetails();

//                    if ( plan.get("PlanName").equals("BASIC")) {
//                        Intent intent = new Intent(MyUserActivity.this, LoanActivity.class);
//                        startActivity(intent);
//                        mNav_user.closeLeftSide();
//                        MyUserActivity.this.finish();
//                    } else {
                        Intent intent = new Intent(MyUserActivity.this, FundingActivity.class);
                        startActivity(intent);
                        mNav_user.closeLeftSide();
                        MyUserActivity.this.finish();
//                    }
                    //  Toast.makeText(MyUserActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Networks") {
                    String url = "http://app.dealerplus.in/user_login_cometchat?session_user_id=" + user.get("user_id");
                    Intent intet = new Intent(MyUserActivity.this, NetworksWebviewActivity.class);
                    //intet.putExtra("title", BuyPageNavigation.web[position]);
                    intet.putExtra("url", url);
                    startActivity(intet);
                    mNav_user.closeLeftSide();
                    // Toast.makeText(MyUserActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Reports") {
                    plandetailsSharedPreferences = new PlandetailsSharedPreferences(MyUserActivity.this);
                    plan = plandetailsSharedPreferences.getUserDetails();

                    if (plan.get("PlanName").equals("BASIC")) {

                        Intent intent = new Intent(MyUserActivity.this, BuyPlanActivity.class);
                        startActivity(intent);
                        //MainDashBoardActivity.this.finish();
                        mNav_user.closeLeftSide();
                    } else {
                        Intent intent = new Intent(MyUserActivity.this, ReportSalesActivity.class);
                        startActivity(intent);
                        MyUserActivity.this.finish();
                        mNav_user.closeLeftSide();
                    }
                } else if (BuyPageNavigation.web[position] == "FAQs") {
                    faqs();
                    mNav_user.closeLeftSide();
                } else {
                    //Toast.makeText(DashBoard.this, web[position], Toast.LENGTH_SHORT).show();
                }
            }
        });

        new tab_my_user_view().execute();
        new my_user_view().execute();

    }


    @Override
    public void onBackPressed() {
        AlertDialog alertbox = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogNewStyle)
                .setMessage("Do you want to exit application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        MyUserActivity.this.finish();
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

        Hotline.showFAQs(MyUserActivity.this, faqOptions);
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
            new my_user_view().execute();
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
        TabInfo tabInfo = null;
        int j;
        for (j = 0; j < list_new.size(); j++) {

            AddTab(this, this.mTabHost,
                    this.mTabHost.newTabSpec(list_new.get(j).toString()).setIndicator(list_new.get(j).toString()),
                    (tabInfo = new TabInfo("Tab", MyUserActivity.class)));
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

    private ArrayList<MyUserModel> getUserData() {
        userdatanew = new ArrayList<>();
        for (int i = 0; i < user_listview.size(); i++) {

            String branch_id = user_listview.get(i).get("branch_id");
            String user_email = user_listview.get(i).get("user_email");
            String user_role = user_listview.get(i).get("user_role");
            String user_id = user_listview.get(i).get("user_id");
            String user_name = user_listview.get(i).get("user_name");
            String role_name = user_listview.get(i).get("role_name");
            String user_moblie_no = user_listview.get(i).get("user_moblie_no_new");
            String status = user_listview.get(i).get("status");

            userdatanew.add(new MyUserModel(branch_id, user_email, user_role, user_id, user_name, role_name, user_moblie_no, status));
        }
        return userdatanew;
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

            if (json != null) {

                tab_user_listview = new ArrayList<>();
                list.add("Select Role");
                try {
                    JSONObject jsonObj = new JSONObject(json);

                    JSONArray loan = jsonObj.getJSONArray("User_role_list");

                    for (int k = 0; k <= loan.length(); k++) {

                        String master_role_id = loan.getJSONObject(k).getString("master_role_id");
                        String master_role_name = loan.getJSONObject(k).getString("master_role_name");

                        tabuserlistview = new HashMap<>();

                        tabuserlistview.put("master_role_id", master_role_id);
                        tabuserlistview.put("master_role_name", master_role_name);

                        tab_user_listview.add(tabuserlistview);

                        list.add(master_role_name.toString());
                        list_new.add(master_role_name.toString());
                        list_new_id.add(master_role_id.toString());
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

            MyUserActivity.this.initialiseTabHost();

            plus_myuser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(user_count.equals("1")) {
                        list.remove(1);
                        Intent intent = new Intent(MyUserActivity.this, AddUserActivity.class);
                        intent.putExtra("role_list", list);
                        startActivity(intent);
                    }
                    else {
                        AlertDialog alertbox = new AlertDialog.Builder(MyUserActivity.this, R.style.AppCompatAlertDialogNewStyle)
                                .setMessage("Your Plan User Limit is Exceeded")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {

                                    }
                                })
                                .show();

                    }

                }
            });

        }

    }

    private class my_user_view extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();
            String queriesurl = Constant.USER_VIEW +
                    "session_user_id=" + user.get("user_id")
                    + "&page_name=viewuserlist"
                    + "&role_id=" + value;

            Log.e("queriesurl", queriesurl);

            String json = sh.makeServiceCall(queriesurl, ServiceHandler.POST);

            if (json != null) {

                user_listview = new ArrayList<>();

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    user_count = jsonObj.getString("user_count");

                    Log.e("user_count",user_count);

                    JSONArray loan = jsonObj.getJSONArray("user_list");

                    for (int k = 0; k <= loan.length(); k++) {

                        String branch_id = loan.getJSONObject(k).getString("branch_id");
                        String user_email = loan.getJSONObject(k).getString("user_email");
                        String user_role = loan.getJSONObject(k).getString("user_role");
                        String user_id = loan.getJSONObject(k).getString("user_id");
                        String user_name = loan.getJSONObject(k).getString("user_name");
                        String role_name = loan.getJSONObject(k).getString("role_name");
                        String user_moblie_no_new = loan.getJSONObject(k).getString("user_moblie_no");
                        String status = loan.getJSONObject(k).getString("status");

                        userlistview = new HashMap<>();

                        userlistview.put("branch_id", branch_id);
                        userlistview.put("user_email", user_email);
                        userlistview.put("user_role", user_role);
                        userlistview.put("user_id", user_id);
                        userlistview.put("user_name", user_name);
                        userlistview.put("role_name", role_name);
                        userlistview.put("user_moblie_no_new", user_moblie_no_new);
                        userlistview.put("status", status);


                        statusList.add(status);


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

            user_listView = (SwipeMenuListView) findViewById(R.id.user_listView);
            myUserListAdapter = new MyUserListAdapter(MyUserActivity.this, getUserData(),statusList);
            user_listView.setAdapter(myUserListAdapter);

           /* RelativeLayout result_found = (RelativeLayout) findViewById(R.id.result_found);
            if(myUserListAdapter.getCount()!=0){
                user_listView.setAdapter(myUserListAdapter);
            }else{
                user_listView.setVisibility(View.GONE);
                result_found.setVisibility(View.VISIBLE);
            }
*/
            // step 1. create a MenuCreator

            SwipeMenuCreator creator = new SwipeMenuCreator() {

                @Override
                public void create(SwipeMenu menu) {
                    /*SwipeMenuItem edit = new SwipeMenuItem(getApplicationContext());
                    edit.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                    edit.setWidth(dp2px(90));
                    edit.setIcon(R.drawable.swipe_edit);
                    menu.addMenuItem(edit);
*/
                    SwipeMenuItem delete = new SwipeMenuItem(getApplicationContext());
                    delete.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                    delete.setWidth(dp2px(90));
                    delete.setIcon(R.drawable.swipe_delete);
                    menu.addMenuItem(delete);

                }
            };
            // set creator
            user_listView.setMenuCreator(creator);

            // step 2. listener item click event
            user_listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                    if (userdatanew == null || userdatanew.size() <= 0) {

                    } else {
                        switch (index) {
                           /* case 0:
                                item = userdatanew.get(position);
                                users = new ArrayList<String>();
                                users.add(item.getUser_name());
                                users.add(item.getUser_email());
                                users.add(item.getUser_moblie_no());
                                users.add(item.getUser_role());
                                users.add(item.getUser_id());

                                Intent intent = new Intent(MyUserActivity.this, EditUserActivity.class);
                                intent.putExtra("listid", list_new_id);
                                intent.putExtra("list", list);
                                intent.putExtra("users", users);
                                startActivity(intent);
                                break;*/
                            case 0:
                                item = userdatanew.get(position);
                                User_id = item.getUser_id();
                                AlertDialog alertbox = new AlertDialog.Builder(MyUserActivity.this, R.style.AppCompatAlertDialogNewStyle)
                                        .setMessage("Do you want delete this user?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                new user_api_delete().execute();
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
            user_listView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

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
            user_listView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
                @Override
                public void onMenuOpen(int position) {
                }

                @Override
                public void onMenuClose(int position) {
                }
            });

        }

    }

    private class user_search extends AsyncTask<Void, Void, Void> {
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
            String queriesurl = Constant.USER_SEARCH +
                    "session_user_id=" + user.get("user_id")
                    + "&searcheuser="+ decode
                    + "&role_id=" + value;

            Log.e("queriesurl", queriesurl);

            String json = sh.makeServiceCall(queriesurl, ServiceHandler.POST);

            if (json != null) {

                user_listview = new ArrayList<>();

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    JSONArray loan = jsonObj.getJSONArray("user_list");

                    for (int k = 0; k <= loan.length(); k++) {

                        String branch_id = loan.getJSONObject(k).getString("branch_id");
                        String user_email = loan.getJSONObject(k).getString("user_email");
                        String user_role = loan.getJSONObject(k).getString("user_role");
                        String user_id = loan.getJSONObject(k).getString("user_id");
                        String user_name = loan.getJSONObject(k).getString("user_name");
                        String role_name = loan.getJSONObject(k).getString("role_name");
                        String user_moblie_no_new = loan.getJSONObject(k).getString("user_moblie_no");
                        String status = loan.getJSONObject(k).getString("status");

                        userlistview = new HashMap<>();

                        userlistview.put("branch_id", branch_id);
                        userlistview.put("user_email", user_email);
                        userlistview.put("user_role", user_role);
                        userlistview.put("user_id", user_id);
                        userlistview.put("user_name", user_name);
                        userlistview.put("role_name", role_name);
                        userlistview.put("user_moblie_no_new", user_moblie_no_new);
                        userlistview.put("status", status);

                        statusList.add(status);

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


            user_listView = (SwipeMenuListView) findViewById(R.id.user_listView);
            myUserListAdapter = new MyUserListAdapter(MyUserActivity.this, getUserData(),statusList);
            user_listView.setAdapter(myUserListAdapter);

           /* RelativeLayout result_found = (RelativeLayout) findViewById(R.id.result_found);
            if(myUserListAdapter.getCount()!=0){
                user_listView.setAdapter(myUserListAdapter);
            }else{
                user_listView.setVisibility(View.GONE);
                result_found.setVisibility(View.VISIBLE);
            }
*/
            // step 1. create a MenuCreator

            SwipeMenuCreator creator = new SwipeMenuCreator() {

                @Override
                public void create(SwipeMenu menu) {
                    /*SwipeMenuItem edit = new SwipeMenuItem(getApplicationContext());
                    edit.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                    edit.setWidth(dp2px(90));
                    edit.setIcon(R.drawable.swipe_edit);
                    menu.addMenuItem(edit);
*/
                    SwipeMenuItem delete = new SwipeMenuItem(getApplicationContext());
                    delete.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                    delete.setWidth(dp2px(90));
                    delete.setIcon(R.drawable.swipe_delete);
                    menu.addMenuItem(delete);

                }
            };
            // set creator
            user_listView.setMenuCreator(creator);

            // step 2. listener item click event
            user_listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                    if (userdatanew == null || userdatanew.size() <= 0) {

                    } else {
                        switch (index) {
                           /* case 0:
                                item = userdatanew.get(position);
                                users = new ArrayList<String>();
                                users.add(item.getUser_name());
                                users.add(item.getUser_email());
                                users.add(item.getUser_moblie_no());
                                users.add(item.getUser_role());
                                users.add(item.getUser_id());

                                Intent intent = new Intent(MyUserActivity.this, EditUserActivity.class);
                                intent.putExtra("listid", list_new_id);
                                intent.putExtra("list", list);
                                intent.putExtra("users", users);
                                startActivity(intent);
                                break;*/
                            case 0:
                                item = userdatanew.get(position);
                                User_id = item.getUser_id();
                                AlertDialog alertbox = new AlertDialog.Builder(MyUserActivity.this, R.style.AppCompatAlertDialogNewStyle)
                                        .setMessage("Do you want delete this user?")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                new user_api_delete().execute();
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
            user_listView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

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
            user_listView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
                @Override
                public void onMenuOpen(int position) {
                }

                @Override
                public void onMenuClose(int position) {
                }
            });

        }

    }

    private class user_api_delete extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();

            String queriesurl = Constant.DELETE_USER +
                    "session_user_id=" + user.get("user_id")
                    + "&page_name=deleteuser"
                    + "&user_id=" + User_id;

            Log.e("queriel", queriesurl);

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
                new my_user_view().execute();
            } else {

            }
        }
    }

}
