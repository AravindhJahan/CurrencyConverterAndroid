package com.falconnect.dealermanagementsystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.bumptech.glide.Glide;
import com.falconnect.dealermanagementsystem.Adapter.ContactListAdapter;
import com.falconnect.dealermanagementsystem.Adapter.CustomList;
import com.falconnect.dealermanagementsystem.Adapter.ManageFooterCustomAdapter;
import com.falconnect.dealermanagementsystem.FontAdapter.RoundImageTransform;
import com.falconnect.dealermanagementsystem.Model.ContactModel;
import com.falconnect.dealermanagementsystem.Model.ManageFooterDataModel;
import com.falconnect.dealermanagementsystem.NavigationDrawer.BuyPageNavigation;
import com.falconnect.dealermanagementsystem.SharedPreference.AddressSavedSharedPreferences;
import com.falconnect.dealermanagementsystem.SharedPreference.PlandetailsSharedPreferences;
import com.falconnect.dealermanagementsystem.SharedPreference.SavedDetailsContact;
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

public class ContactActivity extends FragmentActivity implements TabHost.OnTabChangeListener {

    private static RecyclerView.Adapter adapter;
    private static RecyclerView contactrecycleview;
    private static ArrayList<ManageFooterDataModel> contactdatas;
    public ArrayList<HashMap<String, String>> contact_listview;
    public ArrayList<HashMap<String, String>> tab_contact_listview;
    public ArrayList<HashMap<String, String>> delete_branch_list;
    ImageView contact_nav;
    SessionManager session;
    ImageView imageView_contact;
    HashMap<String, String> user;
    TextView profile_name_contact;
    TextView profile_address_contact;
    String saved_name_contact, saved_address_contact;
    ContactListAdapter contactListAdapter;
    RelativeLayout result_found;
    HashMap<String, String> contactlistview;
    HashMap<String, String> deletebranchlist;
    int value = 0;
    AddressSavedSharedPreferences sessionManagerAddress;
    ArrayList<String> contact_list;
    HashMap<String, String> tabcontactlistview;
    ArrayList<String> list_new = new ArrayList<String>();
    ArrayList<String> list_new_id_contact = new ArrayList<String>();
    ArrayList<String> list = new ArrayList<String>();
    BuyPageNavigation contact_buypagenavigation;
    ImageView plus_contact;
    ArrayList<ContactModel> contactdata;
    ContactModel contactModel;
    ProgressDialog barProgressDialog;
    String Contact_id;
    boolean doubleBackToExitPressedOnce = false;
    PlandetailsSharedPreferences plandetailsSharedPreferences;
    HashMap<String, String> plan;
    SavedDetailsContact savedDetailsContact;
    private boolean mVisible;
    private RecyclerView.LayoutManager layoutManager;
    private SimpleSideDrawer mNav_contact;
    private SwipeMenuListView contact_listView;
    private TabHost mTabHost;
    private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, TabInfo>();


    ImageView search, search_close;
    RelativeLayout relativeLayout1, relativeLayout2;
    private Animation slideRight, slideLeft;
    EditText search_textbox;



    private static void AddTab(ContactActivity activity, TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {
        tabSpec.setContent(activity.new TabFactory(activity));
        tabHost.addTab(tabSpec);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact);
        // this.initialiseTabHost(savedInstanceState);

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mVisible = true;

        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }

        plandetailsSharedPreferences = new PlandetailsSharedPreferences(ContactActivity.this);

        contact_nav = (ImageView) findViewById(R.id.main_dashboard_mnav);
        contactrecycleview = (RecyclerView) findViewById(R.id.contact_recyclerview);
        contactrecycleview.setHasFixedSize(true);
        contactrecycleview.setLayoutManager(new LinearLayoutManager(ContactActivity.this, LinearLayoutManager.HORIZONTAL, false));
        contactdatas = new ArrayList<ManageFooterDataModel>();
        for (int i = 0; i < MyFooterManageData.managenameArray.length; i++) {

            contactdatas.add(new ManageFooterDataModel(
                    MyFooterManageData.managenameArray[i],
                    MyFooterManageData.managedrawableArrayWhite3[i],
                    MyFooterManageData.manageid_[i]
            ));
        }
        adapter = new ManageFooterCustomAdapter(ContactActivity.this, contactdatas);
        contactrecycleview.setAdapter(adapter);

        session = new SessionManager(ContactActivity.this);
        user = session.getUserDetails();

        mNav_contact = new SimpleSideDrawer(this);
        mNav_contact.setLeftBehindContentView(R.layout.activity_behind_left_simple);
        result_found = (RelativeLayout) findViewById(R.id.result_found);
        imageView_contact = (ImageView) mNav_contact.findViewById(R.id.profile_avatar);
        profile_name_contact = (TextView) mNav_contact.findViewById(R.id.profile_name);
        profile_address_contact = (TextView) mNav_contact.findViewById(R.id.profile_address);
        user = session.getUserDetails();
        saved_name_contact = user.get("dealer_name");
        saved_address_contact = user.get("dealershipname");
        profile_name_contact.setText(saved_name_contact);

        if (user.get("dealer_img").isEmpty()) {
            Glide.with(getApplicationContext())
                    .load(R.drawable.default_avatar)
                    .transform(new RoundImageTransform(ContactActivity.this))
                    .into(imageView_contact);
        } else {
            Glide.with(getApplicationContext())
                    .load(user.get("dealer_img"))
                    .transform(new RoundImageTransform(ContactActivity.this))
                    .into(imageView_contact);
        }
        profile_address_contact.setText(saved_address_contact);

        contact_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNav_contact.toggleLeftDrawer();
            }
        });

        plus_contact = (ImageView) findViewById(R.id.plus_contact);


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

                new my_user_view().execute();

            }
        });


        RelativeLayout call_layout = (RelativeLayout) mNav_contact.findViewById(R.id.call_layout);
        RelativeLayout chat_layout = (RelativeLayout) mNav_contact.findViewById(R.id.chat_layout);
        RelativeLayout logout_layout = (RelativeLayout) mNav_contact.findViewById(R.id.logout_layout);

        call_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertbox = new AlertDialog.Builder(ContactActivity.this)
                        .setMessage("Do you want to Call?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:9790928569"));
                                if (ActivityCompat.checkSelfPermission(ContactActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling

                                    return;
                                }
                                if (ActivityCompat.checkSelfPermission(ContactActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling

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
                mNav_contact.closeLeftSide();
                ContactActivity.this.finish();
            }
        });

        chat_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chat();
                mNav_contact.closeLeftSide();
            }
        });

        //NAVIGATION DRAWER LIST VIEW
        contact_buypagenavigation = new BuyPageNavigation();
        CustomList adapter = new CustomList(ContactActivity.this, BuyPageNavigation.web, BuyPageNavigation.imageId);
        ListView list = (ListView) findViewById(R.id.nav_list_view);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (BuyPageNavigation.web[position] == "Dashboard") {
                    Intent intent = new Intent(ContactActivity.this, MainDashBoardActivity.class);
                    startActivity(intent);
                    ContactActivity.this.finish();
                    mNav_contact.closeLeftSide();
                    Toast.makeText(ContactActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();

                } else if (BuyPageNavigation.web[position] == "Buy") {
                    Intent intent = new Intent(ContactActivity.this, DashBoard.class);
                    startActivity(intent);
                    ContactActivity.this.finish();
                    mNav_contact.closeLeftSide();
                    Toast.makeText(ContactActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();

                } else if (BuyPageNavigation.web[position] == "Sell") {
                    Intent intent = new Intent(ContactActivity.this, SellDashBoardActivity.class);
                    startActivity(intent);
                    ContactActivity.this.finish();
                    mNav_contact.closeLeftSide();
                 } else if (BuyPageNavigation.web[position] == "Manage") {
                    mNav_contact.closeLeftSide();
                    Toast.makeText(ContactActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Funding") {
                    plan = plandetailsSharedPreferences.getUserDetails();

//                    if ( plan.get("PlanName").equals("BASIC")) {
//                        Intent intent = new Intent(ContactActivity.this, LoanActivity.class);
//                        startActivity(intent);
//                        mNav_contact.closeLeftSide();
//                        ContactActivity.this.finish();
//                    } else {
                        Intent intent = new Intent(ContactActivity.this, FundingActivity.class);
                        startActivity(intent);
                        mNav_contact.closeLeftSide();
                        ContactActivity.this.finish();
//                    }
                } else if (BuyPageNavigation.web[position] == "Networks") {
                    String url = "http://dev.dealerplus.in/dev/public/user_login_cometchat?session_user_id=" + user.get("user_id");
                    Intent intet = new Intent(ContactActivity.this, NetworksWebviewActivity.class);
                    //intet.putExtra("title", BuyPageNavigation.web[position]);
                    intet.putExtra("url", url);
                    startActivity(intet);
                    mNav_contact.closeLeftSide();
                    Toast.makeText(ContactActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Reports") {
                    plandetailsSharedPreferences = new PlandetailsSharedPreferences(ContactActivity.this);

                    plan = plandetailsSharedPreferences.getUserDetails();

                    if (plan.get("PlanName").equals("BASIC")) {
                        Intent intent = new Intent(ContactActivity.this, BuyPlanActivity.class);
                        startActivity(intent);
                        //MainDashBoardActivity.this.finish();
                        mNav_contact.closeLeftSide();
                    } else {
                        Intent intent = new Intent(ContactActivity.this, ReportSalesActivity.class);
                        startActivity(intent);
                        ContactActivity.this.finish();
                        mNav_contact.closeLeftSide();
                    }
                } else if (BuyPageNavigation.web[position] == "FAQs") {
                    faqs();
                    mNav_contact.closeLeftSide();
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

                    new search_user_view().execute();

                    return true;

                }
                return false;
            }
        });

        // XML Parsing Using AsyncTask...
        if (isNetworkAvailable()) {

        new tab_my_user_view().execute();
        new my_user_view().execute();
        } else {
            Toast.makeText(ContactActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            this.finish();
        }

    }

    public void faqs() {
        FaqOptions faqOptions = new FaqOptions()
                .showFaqCategoriesAsGrid(true)
                .showContactUsOnAppBar(true)
                .showContactUsOnFaqScreens(false)
                .showContactUsOnFaqNotHelpful(false);

        Hotline.showFAQs(ContactActivity.this, faqOptions);
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
        mTabHost.setup();
        TabInfo tabInfo = null;
        int j;
        for (j = 0; j < list_new.size(); j++) {
            AddTab(this, this.mTabHost,
                    this.mTabHost.newTabSpec(list_new.get(j).toString()).setIndicator(list_new.get(j).toString()),
                    (tabInfo = new TabInfo("Tab", ContactActivity.class)));
            this.mapTabInfo.put(tabInfo.tag, tabInfo);

            Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/sanz.ttf");
            TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(j).findViewById(android.R.id.title);
            tv.setTypeface(myTypeface, Typeface.BOLD);
            tv.setTextColor(Color.WHITE);
            tv.setAllCaps(false);
        }
        mTabHost.setOnTabChangedListener(this);

    }

    private ArrayList<ContactModel> getContactData() {
        contactdata = new ArrayList<>();
        for (int i = 0; i < contact_listview.size(); i++) {

            String name = contact_listview.get(i).get("name");
            String mobilenum = contact_listview.get(i).get("mobilenum");
            String email = contact_listview.get(i).get("email");
            String contactimage = contact_listview.get(i).get("contactimage");
            String address = contact_listview.get(i).get("address");
            String contact_id = contact_listview.get(i).get("contact_id");
            String contact_owner = contact_listview.get(i).get("contact_owner");
            String contact_type_id = contact_listview.get(i).get("contact_type_id");
            String contact_type_name = contact_listview.get(i).get("contact_type_name");
            String pan_number = contact_listview.get(i).get("pan_number");
            String contact_gender = contact_listview.get(i).get("contact_gender");
            String contact_email = contact_listview.get(i).get("contact_email");
            String contact_sms = contact_listview.get(i).get("contact_sms");
            String lead_makeid = contact_listview.get(i).get("lead_makeid");
            String lead_cityid = contact_listview.get(i).get("lead_cityid");
            String lead_modelid = contact_listview.get(i).get("lead_modelid");
            String lead_prcie = contact_listview.get(i).get("lead_prcie");
            String lead_time = contact_listview.get(i).get("lead_time");
            String lead_cityname = contact_listview.get(i).get("lead_cityname");
            String lead_makename = contact_listview.get(i).get("lead_makename");
            String lead_modelname = contact_listview.get(i).get("lead_modelname");
            String lead_timename = contact_listview.get(i).get("lead_timename");
            String lead_pricename = contact_listview.get(i).get("lead_pricename");
            String dealer_document_management_id = contact_listview.get(i).get("dealer_document_management_id");
            String contact_management_id = contact_listview.get(i).get("contact_management_id");
            String document_id_type = contact_listview.get(i).get("document_id_type");
            String document_id_number = contact_listview.get(i).get("document_id_number");
            String document_dob = contact_listview.get(i).get("document_dob");
            String doc_link_fullpath = contact_listview.get(i).get("doc_link_fullpath");
            String document_name = contact_listview.get(i).get("document_name");



            contactdata.add(new ContactModel(name, mobilenum, email, contactimage, address, contact_id, contact_owner,
                    contact_type_id, contact_type_name, pan_number, contact_gender, contact_email, contact_sms, lead_cityname,
                    lead_makename, lead_modelname, lead_prcie, lead_time, lead_makeid,lead_cityid,lead_modelid,dealer_document_management_id, contact_management_id, document_id_type, document_id_number,
                    document_dob, doc_link_fullpath, document_name, lead_timename, lead_pricename));
        }
        return contactdata;
    }

    @Override
    public void onBackPressed() {

        AlertDialog alertbox = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogNewStyle)
                .setMessage("Do you want to exit application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        ContactActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();


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

                tab_contact_listview = new ArrayList<>();

                list.add("Select Contact Type");

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

                        list.add(contact_type.toString());

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

            ContactActivity.this.initialiseTabHost();
            barProgressDialog.dismiss();
            plus_contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    sessionManagerAddress = new AddressSavedSharedPreferences(ContactActivity.this);
                    sessionManagerAddress.clear_address();

                    savedDetailsContact = new SavedDetailsContact(ContactActivity.this);
                    savedDetailsContact.clear_contact_datas();

                    list.remove(1);

                    list.remove("ALL");
                    Intent intent = new Intent(ContactActivity.this, AddContactActivity.class);
                    intent.putExtra("contact_list", list);
                    startActivity(intent);
                }
            });

        }

    }

    private class my_user_view extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = ProgressDialog.show(ContactActivity.this, "Loading...", "Please Wait ...", true);
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();
            String queriesurl = Constant.CONTACTLIST +
                    "session_user_id=" + user.get("user_id")
                    + "&page_name=viewcontactlist"
                    + "&contact_type=" + value;

            Log.e("queriesurl", queriesurl);

            String json = sh.makeServiceCall(queriesurl, ServiceHandler.POST);

            Log.e("json object",json.toString());


            if (json != null) {

                contact_listview = new ArrayList<>();

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    JSONArray loan = jsonObj.getJSONArray("contact_list");

                    for (int k = 0; k <= loan.length(); k++) {

                        String name = loan.getJSONObject(k).getString("name");
                        String mobilenum = loan.getJSONObject(k).getString("mobilenum");
                        String email = loan.getJSONObject(k).getString("email");
                        String contactimage = loan.getJSONObject(k).getString("contactimage");
                        String address = loan.getJSONObject(k).getString("address");
                        String contact_id = loan.getJSONObject(k).getString("contact_id");
                        String contact_owner = loan.getJSONObject(k).getString("contact_owner");
                        String contact_type_id = loan.getJSONObject(k).getString("contact_type_id");
                        String contact_type_name = loan.getJSONObject(k).getString("contact_type_name");
                        String pan_number = loan.getJSONObject(k).getString("pan_number");
                        String contact_gender = loan.getJSONObject(k).getString("contact_gender");
                        String contact_email = loan.getJSONObject(k).getString("contact_email");
                        String contact_sms = loan.getJSONObject(k).getString("contact_sms");
                        String lead_city = loan.getJSONObject(k).getString("lead_cityid");
                        String lead_make = loan.getJSONObject(k).getString("lead_makeid");
                        String lead_model = loan.getJSONObject(k).getString("lead_modelid");
                        String lead_prcie = loan.getJSONObject(k).getString("lead_prcie");
                        String lead_time = loan.getJSONObject(k).getString("lead_time");
                        String lead_cityname = loan.getJSONObject(k).getString("lead_cityname");
                        String lead_makename = loan.getJSONObject(k).getString("lead_makename");
                        String lead_modelname = loan.getJSONObject(k).getString("lead_modelname");
                        String lead_timename = loan.getJSONObject(k).getString("lead_timename");
                        String lead_pricename = loan.getJSONObject(k).getString("lead_pricename");
                        String dealer_document_management_id = loan.getJSONObject(k).getString("dealer_document_management_id");
                        String contact_management_id = loan.getJSONObject(k).getString("contact_management_id");
                        String document_id_type = loan.getJSONObject(k).getString("document_id_type");
                        String document_id_number = loan.getJSONObject(k).getString("document_id_number");
                        String document_dob = loan.getJSONObject(k).getString("document_dob");
                        String doc_link_fullpath = loan.getJSONObject(k).getString("doc_link_fullpath");
                        String document_name = loan.getJSONObject(k).getString("document_name");

                        contactlistview = new HashMap<>();

                        contactlistview.put("name", name);
                        contactlistview.put("mobilenum", mobilenum);
                        contactlistview.put("email", email);
                        contactlistview.put("contactimage", contactimage);
                        contactlistview.put("address", address);
                        contactlistview.put("contact_id", contact_id);
                        contactlistview.put("contact_owner", contact_owner);
                        contactlistview.put("contact_type_id", contact_type_id);
                        contactlistview.put("contact_type_name", contact_type_name);
                        contactlistview.put("pan_number", pan_number);
                        contactlistview.put("contact_gender", contact_gender);
                        contactlistview.put("contact_email", contact_email);
                        contactlistview.put("contact_sms", contact_sms);
                        contactlistview.put("lead_city", lead_city);
                        contactlistview.put("lead_make", lead_make);
                        contactlistview.put("lead_model", lead_model);
                        contactlistview.put("lead_prcie", lead_prcie);
                        contactlistview.put("lead_time", lead_time);
                        contactlistview.put("lead_cityname", lead_cityname);
                        contactlistview.put("lead_makename", lead_makename);
                        contactlistview.put("lead_modelname", lead_modelname);
                        contactlistview.put("lead_timename", lead_timename);
                        contactlistview.put("lead_pricename", lead_pricename);
                        contactlistview.put("dealer_document_management_id", dealer_document_management_id);
                        contactlistview.put("contact_management_id", contact_management_id);
                        contactlistview.put("document_id_type", document_id_type);
                        contactlistview.put("document_id_number", document_id_number);
                        contactlistview.put("document_dob", document_dob);
                        contactlistview.put("doc_link_fullpath", doc_link_fullpath);
                        contactlistview.put("document_name", document_name);


                        contact_listview.add(contactlistview);

                    }

                } catch (final JSONException e) {

                }

            } else {

                Toast.makeText(ContactActivity.this, "No data is there!!!", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            barProgressDialog.dismiss();

            contact_listView = (SwipeMenuListView) findViewById(R.id.contact_listView);

            contactListAdapter = new ContactListAdapter(ContactActivity.this, getContactData());
            contact_listView.setAdapter(contactListAdapter);

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
            contact_listView.setMenuCreator(creator);

            // step 2. listener item click event
            contact_listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                    if (contactdata == null || contactdata.size() <= 0) {

                    } else {


                        switch (index) {

                            case 0:
                                contactModel = contactdata.get(position);

                                contact_list = new ArrayList<>();

                                String Contact_ids = contactModel.getContact_id();

                                Log.e("Contact_ids", Contact_ids);

                                contact_list.add(contactModel.getContactimage());
                                contact_list.add(contactModel.getContact_owner());
                                contact_list.add(contactModel.getName());
                                contact_list.add(contactModel.getMobilenum());
                                contact_list.add(contactModel.getEmail());
                                contact_list.add(contactModel.getAddress());
                                contact_list.add(contactModel.getContact_id());
                                contact_list.add(contactModel.getContact_type_id());
                                contact_list.add(contactModel.getContact_type_name());
                                contact_list.add(contactModel.getPan_number());
                                contact_list.add(contactModel.getContact_gender());
                                contact_list.add(contactModel.getContact_email());
                                contact_list.add(contactModel.getContact_sms());
                                contact_list.add(contactModel.getLead_city());
                                contact_list.add(contactModel.getLead_make());
                                contact_list.add(contactModel.getLead_model());
                                contact_list.add(contactModel.getLead_prcie());
                                contact_list.add(contactModel.getLead_time());
                                contact_list.add(contactModel.getLead_pricename());
                                contact_list.add(contactModel.getLead_timename());
                                contact_list.add(contactModel.getLead_cityid());
                                contact_list.add(contactModel.getLead_makeid());
                                contact_list.add(contactModel.getLead_modelid());
                                contact_list.add(contactModel.getLead_prcie());
                                contact_list.add(contactModel.getLead_time());


                                list.remove(1);

                                Intent intent = new Intent(ContactActivity.this, EditContact.class);
                                intent.putExtra("list_spine", list);
                                intent.putExtra("listid", list_new_id_contact);
                                intent.putExtra("contact_list", contact_list);
                                intent.putExtra("contact_id", Contact_ids);
                                Log.e("contact_listkkkkkkk", contact_list.toString());
                                startActivity(intent);
                                sessionManagerAddress = new AddressSavedSharedPreferences(ContactActivity.this);
                                sessionManagerAddress.clear_address();

                                break;
                            case 1:

                                contactModel = contactdata.get(position);
                                Contact_id = contactModel.getContact_id();

                                AlertDialog alertbox = new AlertDialog.Builder(ContactActivity.this, R.style.AppCompatAlertDialogNewStyle)
                                        .setMessage("Do you want to Delete this Contact?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                new contact_api_delete().execute();
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
            contact_listView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

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
            contact_listView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
                @Override
                public void onMenuOpen(int position) {
                }

                @Override
                public void onMenuClose(int position) {
                }
            });
        }
    }

    private class search_user_view extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = ProgressDialog.show(ContactActivity.this, "Loading...", "Please Wait ...", true);
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();
            String decode = search_textbox.getText().toString();

            try {
                decode = URLEncoder.encode(decode, "UTF-8");
            } catch (Exception e) {

            }
            String queriesurl = Constant.CONTACT_SEARCH +
                    "session_user_id=" + user.get("user_id")
                    + "&searchcontact="+ decode
                    + "&contact_type=" + value;

            Log.e("queriesurl", queriesurl);

            String json = sh.makeServiceCall(queriesurl, ServiceHandler.POST);

            if (json != null) {

                contact_listview = new ArrayList<>();


                Log.e("json object",json.toString());

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    JSONArray loan = jsonObj.getJSONArray("contact_list");

                    for (int k = 0; k <= loan.length(); k++) {
                        String name = loan.getJSONObject(k).getString("name");
                        String mobilenum = loan.getJSONObject(k).getString("mobilenum");
                        String email = loan.getJSONObject(k).getString("email");
                        String contactimage = loan.getJSONObject(k).getString("contactimage");
                        String address = loan.getJSONObject(k).getString("address");
                        String contact_id = loan.getJSONObject(k).getString("contact_id");
                        String contact_owner = loan.getJSONObject(k).getString("contact_owner");
                        String contact_type_id = loan.getJSONObject(k).getString("contact_type_id");
                        String contact_type_name = loan.getJSONObject(k).getString("contact_type_name");
                        String pan_number = loan.getJSONObject(k).getString("pan_number");
                        String contact_gender = loan.getJSONObject(k).getString("contact_gender");
                        String contact_email = loan.getJSONObject(k).getString("contact_email");
                        String contact_sms = loan.getJSONObject(k).getString("contact_sms");
                        String lead_city = loan.getJSONObject(k).getString("lead_cityid");
                        String lead_make = loan.getJSONObject(k).getString("lead_makeid");
                        String lead_model = loan.getJSONObject(k).getString("lead_modelid");
                        String lead_prcie = loan.getJSONObject(k).getString("lead_prcie");
                        String lead_time = loan.getJSONObject(k).getString("lead_time");
                        String lead_cityname = loan.getJSONObject(k).getString("lead_cityname");
                        String lead_makename = loan.getJSONObject(k).getString("lead_makename");
                        String lead_modelname = loan.getJSONObject(k).getString("lead_modelname");
                        String lead_timename = loan.getJSONObject(k).getString("lead_timename");
                        String lead_pricename = loan.getJSONObject(k).getString("lead_pricename");
                        String dealer_document_management_id = loan.getJSONObject(k).getString("dealer_document_management_id");
                        String contact_management_id = loan.getJSONObject(k).getString("contact_management_id");
                        String document_id_type = loan.getJSONObject(k).getString("document_id_type");
                        String document_id_number = loan.getJSONObject(k).getString("document_id_number");
                        String document_dob = loan.getJSONObject(k).getString("document_dob");
                        String doc_link_fullpath = loan.getJSONObject(k).getString("doc_link_fullpath");
                        String document_name = loan.getJSONObject(k).getString("document_name");

                        contactlistview = new HashMap<>();

                        contactlistview.put("name", name);
                        contactlistview.put("mobilenum", mobilenum);
                        contactlistview.put("email", email);
                        contactlistview.put("contactimage", contactimage);
                        contactlistview.put("address", address);
                        contactlistview.put("contact_id", contact_id);
                        contactlistview.put("contact_owner", contact_owner);
                        contactlistview.put("contact_type_id", contact_type_id);
                        contactlistview.put("contact_type_name", contact_type_name);
                        contactlistview.put("pan_number", pan_number);
                        contactlistview.put("contact_gender", contact_gender);
                        contactlistview.put("contact_email", contact_email);
                        contactlistview.put("contact_sms", contact_sms);
                        contactlistview.put("lead_city", lead_city);
                        contactlistview.put("lead_make", lead_make);
                        contactlistview.put("lead_model", lead_model);
                        contactlistview.put("lead_prcie", lead_prcie);
                        contactlistview.put("lead_time", lead_time);
                        contactlistview.put("lead_cityname", lead_cityname);
                        contactlistview.put("lead_makename", lead_makename);
                        contactlistview.put("lead_modelname", lead_modelname);
                        contactlistview.put("lead_timename", lead_timename);
                        contactlistview.put("lead_pricename", lead_pricename);
                        contactlistview.put("dealer_document_management_id", dealer_document_management_id);
                        contactlistview.put("contact_management_id", contact_management_id);
                        contactlistview.put("document_id_type", document_id_type);
                        contactlistview.put("document_id_number", document_id_number);
                        contactlistview.put("document_dob", document_dob);
                        contactlistview.put("doc_link_fullpath", doc_link_fullpath);
                        contactlistview.put("document_name", document_name);


                        contact_listview.add(contactlistview);

                    }

                } catch (final JSONException e) {

                }

            } else {

                Toast.makeText(ContactActivity.this, "No data is there!!!", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            barProgressDialog.dismiss();

            contact_listView = (SwipeMenuListView) findViewById(R.id.contact_listView);

            contactListAdapter = new ContactListAdapter(ContactActivity.this, getContactData());
            contact_listView.setAdapter(contactListAdapter);

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
            contact_listView.setMenuCreator(creator);

            // step 2. listener item click event
            contact_listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                    if (contactdata == null || contactdata.size() <= 0) {

                    } else {


                        switch (index) {

                            case 0:
                                contactModel = contactdata.get(position);

                                contact_list = new ArrayList<>();

                                contact_list.add(contactModel.getContactimage());
                                contact_list.add(contactModel.getContact_owner());
                                contact_list.add(contactModel.getName());
                                contact_list.add(contactModel.getMobilenum());
                                contact_list.add(contactModel.getEmail());
                                contact_list.add(contactModel.getAddress());
                                contact_list.add(contactModel.getContact_id());
                                contact_list.add(contactModel.getContact_type_id());
                                contact_list.add(contactModel.getContact_type_name());
                                contact_list.add(contactModel.getPan_number());
                                contact_list.add(contactModel.getContact_gender());
                                contact_list.add(contactModel.getContact_email());
                                contact_list.add(contactModel.getContact_sms());
                                contact_list.add(contactModel.getLead_city());
                                contact_list.add(contactModel.getLead_make());
                                contact_list.add(contactModel.getLead_model());
                                contact_list.add(contactModel.getLead_prcie());
                                contact_list.add(contactModel.getLead_time());
                                contact_list.add(contactModel.getLead_pricename());
                                contact_list.add(contactModel.getLead_timename());
                                contact_list.add(contactModel.getLead_cityid());
                                contact_list.add(contactModel.getLead_makeid());
                                contact_list.add(contactModel.getLead_modelid());
                                contact_list.add(contactModel.getLead_prcie());
                                contact_list.add(contactModel.getLead_time());

                                list.remove(0);

                                Intent intent = new Intent(ContactActivity.this, EditContact.class);
                                intent.putExtra("list_spine", list);
                                intent.putExtra("listid", list_new_id_contact);
                                intent.putExtra("contact_list", contact_list);
                                startActivity(intent);
                                sessionManagerAddress = new AddressSavedSharedPreferences(ContactActivity.this);
                                sessionManagerAddress.clear_address();

                                break;
                            case 1:

                                contactModel = contactdata.get(position);
                                Contact_id = contactModel.getContact_id();

                                AlertDialog alertbox = new AlertDialog.Builder(ContactActivity.this, R.style.AppCompatAlertDialogNewStyle)
                                        .setMessage("Do you want to Delete this Contact?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                new contact_api_delete().execute();
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
            contact_listView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

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
            contact_listView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
                @Override
                public void onMenuOpen(int position) {
                }

                @Override
                public void onMenuClose(int position) {
                }
            });
        }
    }

    private class contact_api_delete extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();

            String queriesurl = Constant.DELETE_CONTACT +
                    "session_user_id=" + user.get("user_id")
                    + "&page_name=deletecontact"
                    + "&contact_id=" + Contact_id;

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
                new my_user_view().execute();
            } else {

            }
        }
    }

}
