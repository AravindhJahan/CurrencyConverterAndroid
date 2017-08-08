package com.falconnect.dealermanagementsystem;

import android.app.ProgressDialog;
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
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.falconnect.dealermanagementsystem.Adapter.CustomList;
import com.falconnect.dealermanagementsystem.Adapter.FundingFooterCustomAdapter;
import com.falconnect.dealermanagementsystem.Adapter.LoanListAdapter;
import com.falconnect.dealermanagementsystem.FontAdapter.RoundImageTransform;
import com.falconnect.dealermanagementsystem.Model.FundingDataModel;
import com.falconnect.dealermanagementsystem.Model.LoanModel;
import com.falconnect.dealermanagementsystem.NavigationDrawer.BuyPageNavigation;
import com.falconnect.dealermanagementsystem.SharedPreference.LoanFundingSharedPreferences;
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

public class LoanActivity extends AppCompatActivity {

    private static RecyclerView sellloan_footer;
    private static RecyclerView.Adapter adapter;
    private static ArrayList<FundingDataModel> data;
    public ArrayList<HashMap<String, String>> loan_listview;
    ImageView loan_mnav;
    SessionManager session_loan;
    ImageView imageView_loan;
    TextView profile_name_loan;
    TextView profile_address_loan;
    ImageView add;
    BuyPageNavigation loan_buypagenavigation;
    HashMap<String, String> user;
    ListView loan_listView;
    String saved_name_loan, saved_address_loan;
    HashMap<String, String> loanlistview;
    LoanListAdapter loanListAdapter;
    ImageView plus_loan;
    ArrayList<String> list_details = new ArrayList<>();
    LoanFundingSharedPreferences loanFundingSharedPreferences;
    HashMap<String, String> user_save;
    ProgressDialog barProgressDialog;
    ProfileManagerSession profileManagerSession;
    private boolean mVisible;
    private SimpleSideDrawer mNav_loan;
    private SwipeRefreshLayout mSwipeRefreshLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_loan);

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

        loan_listView = (ListView) findViewById(R.id.loan_list);

        sellloan_footer = (RecyclerView) findViewById(R.id.my_recycler_apply_loan);
        sellloan_footer.setHasFixedSize(true);
        sellloan_footer.setLayoutManager(new LinearLayoutManager(LoanActivity.this, LinearLayoutManager.HORIZONTAL, false));

        data = new ArrayList<FundingDataModel>();
        for (int i = 0; i < MyFundingFooterData.nameArrayFunding.length; i++) {
            data.add(new FundingDataModel(
                    MyFundingFooterData.nameArrayFunding[i],
                    MyFundingFooterData.id_[i],
                    MyFundingFooterData.drawableArrayWhite1[i]
            ));
        }

        adapter = new FundingFooterCustomAdapter(LoanActivity.this, data);
        sellloan_footer.setAdapter(adapter);

        mNav_loan = new SimpleSideDrawer(this);
        mNav_loan.setLeftBehindContentView(R.layout.activity_behind_left_simple);

        imageView_loan = (ImageView) mNav_loan.findViewById(R.id.profile_avatar);
        profile_name_loan = (TextView) mNav_loan.findViewById(R.id.profile_name);
        profile_address_loan = (TextView) mNav_loan.findViewById(R.id.profile_address);

        session_loan = new SessionManager(LoanActivity.this);
        user = session_loan.getUserDetails();
        saved_name_loan = user.get("dealer_name");
        saved_address_loan = user.get("dealershipname");
        profile_name_loan.setText(saved_name_loan);

        plus_loan = (ImageView) findViewById(R.id.plus_loan);

        if (user.get("dealer_img").isEmpty()) {
            Glide.with(getApplicationContext())
                    .load(R.drawable.default_avatar)
                    .transform(new RoundImageTransform(LoanActivity.this))
                    .into(imageView_loan);
        } else {
            Glide.with(getApplicationContext())
                    .load(user.get("dealer_img"))
                    .transform(new RoundImageTransform(LoanActivity.this))
                    .into(imageView_loan);
        }
        profile_address_loan.setText(saved_address_loan);
        loan_mnav = (ImageView) findViewById(R.id.loan_mnav);
        loan_mnav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNav_loan.toggleLeftDrawer();
            }
        });

        RelativeLayout call_layout = (RelativeLayout) mNav_loan.findViewById(R.id.call_layout);
        RelativeLayout chat_layout = (RelativeLayout) mNav_loan.findViewById(R.id.chat_layout);
        RelativeLayout logout_layout = (RelativeLayout) mNav_loan.findViewById(R.id.logout_layout);

        call_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertbox = new AlertDialog.Builder(LoanActivity.this)
                        .setMessage("Do you want to call this 9790928569?")
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
                session_loan.logoutUser();
                mNav_loan.closeLeftSide();
                LoanActivity.this.finish();
            }
        });

        chat_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chat();
                mNav_loan.closeLeftSide();
            }
        });


        //NAVIGATION DRAWER LIST VIEW
        loan_buypagenavigation = new BuyPageNavigation();
        CustomList adapter = new CustomList(LoanActivity.this, BuyPageNavigation.web, BuyPageNavigation.imageId);
        ListView list = (ListView) findViewById(R.id.nav_list_view);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (BuyPageNavigation.web[position] == "Dashboard") {
                    Intent intent = new Intent(LoanActivity.this, MainDashBoardActivity.class);
                    startActivity(intent);
                    mNav_loan.closeLeftSide();
                    LoanActivity.this.finish();
                    // Toast.makeText(LoanActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Buy") {
                    Intent intent = new Intent(LoanActivity.this, DashBoard.class);
                    startActivity(intent);
                    mNav_loan.closeLeftSide();
                    LoanActivity.this.finish();
                    // Toast.makeText(LoanActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Sell") {
                    Intent intent = new Intent(LoanActivity.this, SellDashBoardActivity.class);
                    startActivity(intent);
                    mNav_loan.closeLeftSide();
                    //  Toast.makeText(LoanActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Manage") {
                    Intent intent = new Intent(LoanActivity.this, ManageDashBoardActivity.class);
                    startActivity(intent);
                    LoanActivity.this.finish();
                    mNav_loan.closeLeftSide();
                    profileManagerSession = new ProfileManagerSession(LoanActivity.this);
                    profileManagerSession.clear_ProfileManage();
                    // Toast.makeText(LoanActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Funding") {
                    Intent intent = new Intent(LoanActivity.this, FundingActivity.class);
                    startActivity(intent);
                    mNav_loan.closeLeftSide();
                    //  Toast.makeText(LoanActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Networks") {
                    String url = "http://dev.dealerplus.in/dev/public/user_login_cometchat?session_user_id=" + user.get("user_id");
                    Intent intet = new Intent(LoanActivity.this, NetworksWebviewActivity.class);
                    //intet.putExtra("title", BuyPageNavigation.web[position]);
                    intet.putExtra("url", url);
                    startActivity(intet);
                    mNav_loan.closeLeftSide();
                    //  Toast.makeText(LoanActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Reports") {
                    Intent intent = new Intent(LoanActivity.this, ReportSalesActivity.class);
                    startActivity(intent);
                    LoanActivity.this.finish();
                    mNav_loan.closeLeftSide();
                    //  Toast.makeText(LoanActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(DashBoard.this, web[position], Toast.LENGTH_SHORT).show();
                }
            }
        });

        new loan_list().execute();

        //Initialize swipe to refresh view
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Refreshing data on server
                new loan_list().execute();
            }
        });

        add = (ImageView) findViewById(R.id.plus_loan);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String values_save = "1";
                loanFundingSharedPreferences = new LoanFundingSharedPreferences(LoanActivity.this);
                loanFundingSharedPreferences.createAddressSession(values_save);
                user_save = loanFundingSharedPreferences.getAddress_details();

                Log.e("Loan", user_save.get("loan"));

                Intent intent = new Intent(LoanActivity.this, SelectContactActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onBackPressed() {

        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to exit application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        LoanActivity.this.finish();
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

        Hotline.showFAQs(LoanActivity.this, faqOptions);
    }

    public void chat() {
        Hotline.showConversations(getApplicationContext());
        // Hotline.clearUserData(getApplicationContext());
    }

    private ArrayList<LoanModel> getLoanData() {
        final ArrayList<LoanModel> loandata = new ArrayList<>();
        for (int i = 0; i < loan_listview.size(); i++) {

            String dealer_customer_loan_id = loan_listview.get(i).get("dealer_customer_loan_id");
            String customername = loan_listview.get(i).get("customername");
            String user_image = loan_listview.get(i).get("user_image");
            String customermobileno = loan_listview.get(i).get("customermobileno");
            String customerpannumber = loan_listview.get(i).get("customerpannumber");
            String dealer_loan_ticket_id = loan_listview.get(i).get("dealer_loan_ticket_id");
            String customermailid = loan_listview.get(i).get("customermailid");
            String customercity = loan_listview.get(i).get("customercity");
            String branchname = loan_listview.get(i).get("customercity");
            String vehicle_details = loan_listview.get(i).get("vehicle_details");
            String status = loan_listview.get(i).get("status");
            String created_date = loan_listview.get(i).get("created_date");
            String requested_amount = loan_listview.get(i).get("requested_amount");

            loandata.add(new LoanModel(dealer_customer_loan_id, customername, user_image, customermobileno,
                    customerpannumber, dealer_loan_ticket_id, customermailid, customercity, branchname, vehicle_details,
                    status, created_date, requested_amount));
        }
        return loandata;
    }

    @Override
    public void onRestart() {
        super.onRestart();
        startActivity(getIntent());
        finish();
    }

    private class loan_list extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();

            String queriesurl = Constant.APPLY_LOAN
                    + "session_user_id=" + user.get("user_id")
                    + "&page_name=viewloandetails";

            Log.e("queriesurl", queriesurl);

            String json = sh.makeServiceCall(queriesurl, ServiceHandler.POST);

            if (json != null) {

                loan_listview = new ArrayList<>();

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    JSONArray loan = jsonObj.getJSONArray("viewloanlist");

                    for (int k = 0; k <= loan.length(); k++) {

                        String dealer_customer_loan_id = loan.getJSONObject(k).getString("dealer_customer_loan_id");
                        String customername = loan.getJSONObject(k).getString("customername");
                        String user_image = loan.getJSONObject(k).getString("user_image");
                        String customermobileno = loan.getJSONObject(k).getString("customermobileno");
                        String customerpannumber = loan.getJSONObject(k).getString("customerpannumber");
                        String dealer_loan_ticket_id = loan.getJSONObject(k).getString("dealer_loan_ticket_id");
                        String customermailid = loan.getJSONObject(k).getString("customermailid");
                        String customercity = loan.getJSONObject(k).getString("customercity");
                        String branchname = loan.getJSONObject(k).getString("branchname");
                        String vehicle_details = loan.getJSONObject(k).getString("vehicle_details");
                        String status = loan.getJSONObject(k).getString("status");
                        String created_date = loan.getJSONObject(k).getString("created_date");
                        String requested_amount = loan.getJSONObject(k).getString("requested_amount");

                        loanlistview = new HashMap<>();

                        loanlistview.put("dealer_customer_loan_id", dealer_customer_loan_id);
                        loanlistview.put("customername", customername);
                        loanlistview.put("user_image", user_image);
                        loanlistview.put("customermobileno", customermobileno);
                        loanlistview.put("customerpannumber", customerpannumber);
                        loanlistview.put("dealer_loan_ticket_id", dealer_loan_ticket_id);
                        loanlistview.put("customermailid", customermailid);
                        loanlistview.put("customercity", customercity);
                        loanlistview.put("branchname", branchname);
                        loanlistview.put("vehicle_details", vehicle_details);
                        loanlistview.put("status", status);
                        loanlistview.put("created_date", created_date);
                        loanlistview.put("requested_amount", requested_amount);

                        loan_listview.add(loanlistview);

                    }

                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

            loanListAdapter = new LoanListAdapter(LoanActivity.this, getLoanData());
            loan_listView.setAdapter(loanListAdapter);

            loan_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    LoanModel item = (LoanModel) parent.getItemAtPosition(position);

                    list_details.add(item.getDealer_customer_loan_id());
                    list_details.add(item.getCustomername());
                    list_details.add(item.getCustomermobileno());
                    list_details.add(item.getCustomerpannumber());
                    list_details.add(item.getCustomermailid());
                    list_details.add(item.getVehicle_details());
                    list_details.add(item.getCreated_date());
                    list_details.add(item.getRequested_amount());


                    Intent intent = new Intent(LoanActivity.this, LoanViewActivity.class);
                    intent.putExtra("list_details", list_details);
                    intent.putExtra("list_image", item.getUser_image());
                    intent.putExtra("status", item.getStatus());
                    startActivity(intent);

                    list_details.clear();
                }
            });


            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }

        }

    }

}