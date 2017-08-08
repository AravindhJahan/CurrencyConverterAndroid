package com.falconnect.dealermanagementsystem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.falconnect.dealermanagementsystem.Adapter.AlertPageListAdapter;
import com.falconnect.dealermanagementsystem.Adapter.CustomAdapter;
import com.falconnect.dealermanagementsystem.Adapter.CustomList;
import com.falconnect.dealermanagementsystem.FontAdapter.RoundImageTransform;
import com.falconnect.dealermanagementsystem.Model.AlertPageModel;
import com.falconnect.dealermanagementsystem.Model.DataModel;
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

import java.util.ArrayList;
import java.util.HashMap;


public class AlertActivity extends Activity {

    private static RecyclerView alertrecyclerView;
    private static ArrayList<DataModel> data;
    private static RecyclerView.Adapter adapter;
    public ArrayList<HashMap<String, String>> alert_list;
    HashMap<String, String> Alertlist;
    HashMap<String, String> user;
    ListView alertView;
    AlertPageListAdapter alertAdapter;
    SessionManager session;
    ImageView sidebarbtn;
    ProgressDialog barProgressDialog;
    BuyPageNavigation bids_buypagenavigation;
    SessionManager session_bids;
    ImageView imageView_bids;
    TextView profile_name_bids;
    TextView profile_address_bids;
    String saved_name_bids, saved_address_bids;
    ArrayList<String> arrayList_alert = new ArrayList<>();
    ArrayList<String> arrayList_sms = new ArrayList<>();
    ArrayList<String> arrayList_email = new ArrayList<>();
    ProfileManagerSession profileManagerSession;
    RelativeLayout result_found;
    PlandetailsSharedPreferences plandetailsSharedPreferences;
    HashMap<String, String> plan;
    private SimpleSideDrawer mNav;
    private boolean mVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activty_alertpage);

        session = new SessionManager(AlertActivity.this);
        user = session.getUserDetails();

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mVisible = true;

        plandetailsSharedPreferences = new PlandetailsSharedPreferences(AlertActivity.this);

        alertrecyclerView = (RecyclerView) findViewById(R.id.my_recycler_bids);
        alertrecyclerView.setHasFixedSize(true);
        alertrecyclerView.setLayoutManager(new LinearLayoutManager(AlertActivity.this, LinearLayoutManager.HORIZONTAL, false));

        data = new ArrayList<DataModel>();
        for (int i = 0; i < MyData.nameArray.length; i++) {
            data.add(new DataModel(
                    MyData.nameArray[i],
                    MyData.id_[i],
                    MyData.drawableArrayWhite3[i]
            ));
        }

        adapter = new CustomAdapter(AlertActivity.this, data);
        alertrecyclerView.setAdapter(adapter);

        mNav = new SimpleSideDrawer(this);
        mNav.setLeftBehindContentView(R.layout.activity_behind_left_simple);

        imageView_bids = (ImageView) mNav.findViewById(R.id.profile_avatar);
        profile_name_bids = (TextView) mNav.findViewById(R.id.profile_name);
        profile_address_bids = (TextView) mNav.findViewById(R.id.profile_address);

        RelativeLayout call_layout = (RelativeLayout) mNav.findViewById(R.id.call_layout);
        RelativeLayout chat_layout = (RelativeLayout) mNav.findViewById(R.id.chat_layout);
        RelativeLayout logout_layout = (RelativeLayout) mNav.findViewById(R.id.logout_layout);
        result_found = (RelativeLayout) findViewById(R.id.result_found);
        session_bids = new SessionManager(AlertActivity.this);
        user = session_bids.getUserDetails();
        saved_name_bids = user.get("dealer_name");
        saved_address_bids = user.get("dealershipname");
        profile_name_bids.setText(saved_name_bids);
        if (user.get("dealer_img").isEmpty()) {
            Glide.with(getApplicationContext())
                    .load(R.drawable.default_avatar)
                    .transform(new RoundImageTransform(AlertActivity.this))
                    .into(imageView_bids);
        } else {
            Glide.with(getApplicationContext())
                    .load(user.get("dealer_img"))
                    .transform(new RoundImageTransform(AlertActivity.this))
                    .into(imageView_bids);
        }
        profile_address_bids.setText(saved_address_bids);

        call_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertbox = new AlertDialog.Builder(AlertActivity.this)
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
                mNav.closeLeftSide();
                AlertActivity.this.finish();
            }
        });

        chat_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chat();
                mNav.closeLeftSide();
            }
        });


        session = new SessionManager(AlertActivity.this);

        //NAVIGATION DRAWER LIST VIEW
        bids_buypagenavigation = new BuyPageNavigation();
        CustomList adapter = new CustomList(AlertActivity.this, BuyPageNavigation.web, BuyPageNavigation.imageId);
        ListView list = (ListView) findViewById(R.id.nav_list_view);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (BuyPageNavigation.web[position] == "Dashboard") {
                    Intent intent = new Intent(AlertActivity.this, MainDashBoardActivity.class);
                    startActivity(intent);
                    AlertActivity.this.finish();
                    mNav.closeLeftSide();
                    //Toast.makeText(AlertActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Buy") {
                    mNav.closeLeftSide();
                    Toast.makeText(AlertActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Sell") {
                    Intent intent = new Intent(AlertActivity.this, SellDashBoardActivity.class);
                    startActivity(intent);
                    AlertActivity.this.finish();
                    mNav.closeLeftSide();
                    //  Toast.makeText(AlertActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Manage") {
                    profileManagerSession = new ProfileManagerSession(AlertActivity.this);
                    profileManagerSession.clear_ProfileManage();
                    Intent intent = new Intent(AlertActivity.this, ManageDashBoardActivity.class);
                    startActivity(intent);
                    AlertActivity.this.finish();
                    mNav.closeLeftSide();
                    //   Toast.makeText(AlertActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Funding") {

                    plan = plandetailsSharedPreferences.getUserDetails();

//                    if (plan.get("PlanName").equals("BASIC")) {
//                        Intent intent = new Intent(AlertActivity.this, LoanActivity.class);
//                        startActivity(intent);
//                        mNav.closeLeftSide();
//                        AlertActivity.this.finish();
//                    } else {
                        Intent intent = new Intent(AlertActivity.this, FundingActivity.class);
                        startActivity(intent);
                        mNav.closeLeftSide();
                        AlertActivity.this.finish();
//                    }
                    //   Toast.makeText(AlertActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Networks") {
                    String url = "http://app.dealerplus.in/user_login_cometchat?session_user_id=" + user.get("user_id");
                    Intent intet = new Intent(AlertActivity.this, NetworksWebviewActivity.class);
                    //intet.putExtra("title", BuyPageNavigation.web[position]);
                    intet.putExtra("url", url);
                    startActivity(intet);
                    mNav.closeLeftSide();
                    //   Toast.makeText(AlertActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Reports") {
                    plandetailsSharedPreferences = new PlandetailsSharedPreferences(AlertActivity.this);
                    plan = plandetailsSharedPreferences.getUserDetails();
                    if (plan.get("PlanName").equals("BASIC")) {
                        Intent intent = new Intent(AlertActivity.this, BuyPlanActivity.class);
                        startActivity(intent);
                        //MainDashBoardActivity.this.finish();
                        mNav.closeLeftSide();
                    } else {
                        Intent intent = new Intent(AlertActivity.this, ReportSalesActivity.class);
                        startActivity(intent);
                        AlertActivity.this.finish();
                        mNav.closeLeftSide();
                    }
                    //   Toast.makeText(AlertActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "FAQs") {
                    faqs();
                    mNav.closeLeftSide();
                } else {
                    //Toast.makeText(DashBoard.this, web[position], Toast.LENGTH_SHORT).show();
                }
            }
        });


        sidebarbtn = (ImageView) findViewById(R.id.alert_back);

        new my_employee_view().execute();

        sidebarbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNav.toggleLeftDrawer();
            }
        });

    }

    @Override
    public void onBackPressed() {

        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to exit application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        AlertActivity.this.finish();
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

        Hotline.showFAQs(AlertActivity.this, faqOptions);
    }

    public void chat() {
        Hotline.showConversations(getApplicationContext());
        // Hotline.clearUserData(getApplicationContext());
    }


    public void refresh() {
        arrayList_alert.clear();
        arrayList_sms.clear();
        arrayList_email.clear();
        new my_employee_view().execute();
    }

    private ArrayList<AlertPageModel> getUserData() {
        ArrayList<AlertPageModel> employeedatanew = new ArrayList<>();
        for (int i = 0; i < alert_list.size(); i++) {
            String city = alert_list.get(i).get("city");
            String date = alert_list.get(i).get("date");
            String email_status = alert_list.get(i).get("email_status");
            String listingid = alert_list.get(i).get("listingid");
            String mobileno = alert_list.get(i).get("mobileno");
            String product = alert_list.get(i).get("product");
            String sms_status = alert_list.get(i).get("sms_status");
            String alert_status = alert_list.get(i).get("alert_status");
            String type = alert_list.get(i).get("type");
            String email = alert_list.get(i).get("email");
            String alertid = alert_list.get(i).get("alertid");
            String dealername = alert_list.get(i).get("dealername");

            employeedatanew.add(new AlertPageModel(city, email_status, date, listingid, mobileno,
                    product, sms_status, alert_status,
                    type, email, alertid, dealername));
        }
        return employeedatanew;
    }

    private class my_employee_view extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = ProgressDialog.show(AlertActivity.this, "Loading...", "Please Wait ...", true);
        }


        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();
            String queriesurl = Constant.ALERT_PAGEAPI +
                    "session_user_id=" + user.get("user_id");

            Log.e("queriesurl", queriesurl);

            String json = sh.makeServiceCall(queriesurl, ServiceHandler.POST);

            if (json != null) {

                alert_list = new ArrayList<>();

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    JSONArray loan = jsonObj.getJSONArray("alert_history");

                    for (int k = 0; k <= loan.length(); k++) {


                        String city = loan.getJSONObject(k).getString("alert_city");
                        String date = loan.getJSONObject(k).getString("alert_date");
                        String email_status = loan.getJSONObject(k).getString("alert_email_status");
                        String listingid = loan.getJSONObject(k).getString("alert_listingid");
                        String mobileno = loan.getJSONObject(k).getString("alert_mobileno");
                        String product = loan.getJSONObject(k).getString("alert_product");
                        String sms_status = loan.getJSONObject(k).getString("alert_sms_status");
                        String alert_status = loan.getJSONObject(k).getString("alert_status");
                        String type = loan.getJSONObject(k).getString("alert_type");
                        String email = loan.getJSONObject(k).getString("alert_usermailid");
                        String alertid = loan.getJSONObject(k).getString("alertid");
                        String dealername = loan.getJSONObject(k).getString("dealername");

                        Alertlist = new HashMap<>();

                        Alertlist.put("city", city);
                        Alertlist.put("date", date);
                        Alertlist.put("email_status", email_status);
                        Alertlist.put("listingid", listingid);
                        Alertlist.put("mobileno", mobileno);
                        Alertlist.put("product", product);
                        Alertlist.put("sms_status", sms_status);
                        Alertlist.put("alert_status", alert_status);
                        Alertlist.put("type", type);
                        Alertlist.put("email", email);
                        Alertlist.put("alertid", alertid);
                        Alertlist.put("dealername", dealername);

                        arrayList_alert.add(alert_status);
                        arrayList_sms.add(sms_status);
                        arrayList_email.add(email_status);

                        alert_list.add(Alertlist);

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

            alertView = (ListView) findViewById(R.id.alert_listview);
            alertAdapter = new AlertPageListAdapter(AlertActivity.this, getUserData(), arrayList_alert, arrayList_sms, arrayList_email);

            if (alertAdapter.getCount() != 0) {
                alertView.setAdapter(alertAdapter);
            } else {
                alertView.setVisibility(View.GONE);
                result_found.setVisibility(View.VISIBLE);
            }
        }
    }


}
