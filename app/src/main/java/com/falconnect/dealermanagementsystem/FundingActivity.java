package com.falconnect.dealermanagementsystem;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.falconnect.dealermanagementsystem.Adapter.ApplyFundingListAdapter;
import com.falconnect.dealermanagementsystem.Adapter.CustomList;
import com.falconnect.dealermanagementsystem.Adapter.FundingFooterCustomAdapter;
import com.falconnect.dealermanagementsystem.FontAdapter.RoundImageTransform;
import com.falconnect.dealermanagementsystem.Model.ApplyFundingListModel;
import com.falconnect.dealermanagementsystem.Model.FundingDataModel;
import com.falconnect.dealermanagementsystem.NavigationDrawer.BuyPageNavigation;
import com.falconnect.dealermanagementsystem.SharedPreference.InventorySavedDetails;
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
import java.util.List;

public class FundingActivity extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private static RecyclerView fundingrecyclerView;
    private static ArrayList<FundingDataModel> data;
    public ArrayList<HashMap<String, String>> applyfund_list;
    ImageView plus;
    BuyPageNavigation funding_buypagenavigation;
    SessionManager session_fund;
    ImageView imageView_fund;
    TextView profile_name_fund;
    TextView profile_address_fund;
    String saved_name_fund, saved_address_fund;
    SessionManager session;
    HashMap<String, String> user;
    ListView applyfunding_listview;
    HashMap<String, String> applyfundlist;
    ArrayList<String> datas = new ArrayList<String>();
    ApplyFundingListAdapter applyFundingListAdapter;
    List<String> new_list = new ArrayList<>();
    HashMap<String, String> user_save;
    HashMap<String, String> user_details;
    LoanFundingSharedPreferences loanFundingSharedPreferences;
    InventorySavedDetails inventorySavedDetails;
    SessionManager sessionManager;
    ProgressDialog barProgressDialog;
    ProfileManagerSession profileManagerSession;
    private boolean mVisible;
    private RecyclerView.LayoutManager layoutManager;
    private SimpleSideDrawer mNav_funding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_funding);

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

        fundingrecyclerView = (RecyclerView) findViewById(R.id.my_recycler_funding);
        fundingrecyclerView.setHasFixedSize(true);
        fundingrecyclerView.setLayoutManager(new LinearLayoutManager(FundingActivity.this, LinearLayoutManager.HORIZONTAL, false));

        data = new ArrayList<FundingDataModel>();
        for (int i = 0; i < MyFundingFooterData.nameArrayFunding.length; i++) {
            data.add(new FundingDataModel(
                    MyFundingFooterData.nameArrayFunding[i],
                    MyFundingFooterData.id_[i],
                    MyFundingFooterData.drawableArrayWhite0[i]
            ));
        }

        adapter = new FundingFooterCustomAdapter(FundingActivity.this, data);
        fundingrecyclerView.setAdapter(adapter);

        ImageView nav_funding = (ImageView) findViewById(R.id.nav_funding);
        mNav_funding = new SimpleSideDrawer(this);
        mNav_funding.setLeftBehindContentView(R.layout.activity_behind_left_simple);

        imageView_fund = (ImageView) mNav_funding.findViewById(R.id.profile_avatar);
        profile_name_fund = (TextView) mNav_funding.findViewById(R.id.profile_name);
        profile_address_fund = (TextView) mNav_funding.findViewById(R.id.profile_address);

        session_fund = new SessionManager(FundingActivity.this);
        user = session_fund.getUserDetails();
        saved_name_fund = user.get("dealer_name");
        saved_address_fund = user.get("dealershipname");
        profile_name_fund.setText(saved_name_fund);
        if (user.get("dealer_img").isEmpty()) {
            Glide.with(getApplicationContext())
                    .load(R.drawable.default_avatar)
                    .transform(new RoundImageTransform(FundingActivity.this))
                    .into(imageView_fund);
        } else {
            Glide.with(getApplicationContext())
                    .load(user.get("dealer_img"))
                    .transform(new RoundImageTransform(FundingActivity.this))
                    .into(imageView_fund);
        }
        profile_address_fund.setText(saved_address_fund);

        nav_funding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNav_funding.toggleLeftDrawer();
            }
        });

        plus = (ImageView) findViewById(R.id.plus);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String values_save = "0";
                loanFundingSharedPreferences = new LoanFundingSharedPreferences(FundingActivity.this);
                loanFundingSharedPreferences.createAddressSession(values_save);
                user_save = loanFundingSharedPreferences.getAddress_details();

                //Session Manager
                sessionManager = new SessionManager(FundingActivity.this);
                user_details = sessionManager.getUserDetails();

                inventorySavedDetails = new InventorySavedDetails(FundingActivity.this);
                inventorySavedDetails.createInventoryDetailsSession(user_details.get("dealer_img"), user_details.get("dealer_name"), user_details.get("dealershipname"), user_details.get("dealer_email"), user_details.get("dealer_mobile"), null, null, null, null);

                Log.e("Loan", user_save.get("loan"));

                Intent intent = new Intent(FundingActivity.this, ApplyFundingActivity.class);
                startActivity(intent);
            }
        });

        session = new SessionManager(FundingActivity.this);

        //NAVIGATION DRAWER LIST VIEW
        funding_buypagenavigation = new BuyPageNavigation();
        CustomList adapter = new CustomList(FundingActivity.this, BuyPageNavigation.web, BuyPageNavigation.imageId);
        ListView list = (ListView) findViewById(R.id.nav_list_view);
        list.setAdapter(adapter);

        RelativeLayout call_layout = (RelativeLayout) mNav_funding.findViewById(R.id.call_layout);
        RelativeLayout chat_layout = (RelativeLayout) mNav_funding.findViewById(R.id.chat_layout);
        RelativeLayout logout_layout = (RelativeLayout) mNav_funding.findViewById(R.id.logout_layout);

        call_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertbox = new AlertDialog.Builder(FundingActivity.this)
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
                mNav_funding.closeLeftSide();
                FundingActivity.this.finish();
            }
        });

        chat_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chat();
                mNav_funding.closeLeftSide();
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (BuyPageNavigation.web[position] == "Dashboard") {
                    Intent intent = new Intent(FundingActivity.this, MainDashBoardActivity.class);
                    startActivity(intent);
                    FundingActivity.this.finish();
                    mNav_funding.closeLeftSide();
                    //  Toast.makeText(FundingActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Buy") {
                    Intent intent = new Intent(FundingActivity.this, DashBoard.class);
                    startActivity(intent);
                    mNav_funding.closeLeftSide();
                    //   Toast.makeText(FundingActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Sell") {
                    Intent intent = new Intent(FundingActivity.this, SellDashBoardActivity.class);
                    startActivity(intent);
                    FundingActivity.this.finish();
                    mNav_funding.closeLeftSide();
                    //   Toast.makeText(FundingActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Manage") {
                    Intent intent = new Intent(FundingActivity.this, ManageDashBoardActivity.class);
                    startActivity(intent);
                    FundingActivity.this.finish();
                    profileManagerSession = new ProfileManagerSession(FundingActivity.this);
                    profileManagerSession.clear_ProfileManage();
                    mNav_funding.closeLeftSide();
                    //  Toast.makeText(FundingActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Networks") {
                    String url = "http://dev.dealerplus.in/dev/public/user_login_cometchat?session_user_id=" + user.get("user_id");
                    Intent intet = new Intent(FundingActivity.this, NetworksWebviewActivity.class);
                    //intet.putExtra("title", BuyPageNavigation.web[position]);
                    intet.putExtra("url", url);
                    startActivity(intet);
                    mNav_funding.closeLeftSide();
                    //  Toast.makeText(FundingActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Reports") {
                    Intent intent = new Intent(FundingActivity.this, ReportSalesActivity.class);
                    startActivity(intent);
                    FundingActivity.this.finish();
                    mNav_funding.closeLeftSide();
                    //  Toast.makeText(FundingActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "FAQs") {
                    faqs();
                    mNav_funding.closeLeftSide();
                } else {
                    //Toast.makeText(DashBoard.this, web[position], Toast.LENGTH_SHORT).show();
                }
            }
        });

        new Apply_fund().execute();

    }


    public void faqs() {
        FaqOptions faqOptions = new FaqOptions()
                .showFaqCategoriesAsGrid(true)
                .showContactUsOnAppBar(true)
                .showContactUsOnFaqScreens(false)
                .showContactUsOnFaqNotHelpful(false);

        Hotline.showFAQs(FundingActivity.this, faqOptions);
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
                        FundingActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();

    }

    private ArrayList<ApplyFundingListModel> getFundData() {
        final ArrayList<ApplyFundingListModel> funddata = new ArrayList<>();
        for (int i = 0; i < applyfund_list.size(); i++) {
            String fundingid = applyfund_list.get(i).get("fundingid");
            String dealername = applyfund_list.get(i).get("dealername");
            String dealermobileno = applyfund_list.get(i).get("dealermobileno");
            String dealermailid = applyfund_list.get(i).get("dealermailid");
            String requested_amount = applyfund_list.get(i).get("requested_amount");
            String created_date = applyfund_list.get(i).get("created_date");
            String dealercity = applyfund_list.get(i).get("dealercity");
            String branchname = applyfund_list.get(i).get("branchname");
            String dealer_listing_id = applyfund_list.get(i).get("dealer_listing_id");
            String status = applyfund_list.get(i).get("status");
            String dealer_funding_ticket_id = applyfund_list.get(i).get("dealer_funding_ticket_id");

            funddata.add(new ApplyFundingListModel(fundingid, dealername, dealermobileno, dealermailid,
                    requested_amount, created_date, dealercity, branchname, dealer_listing_id, status, dealer_funding_ticket_id));
        }
        return funddata;
    }

    @Override
    public void onRestart() {
        super.onRestart();
        startActivity(getIntent());
        finish();
    }

    private class Apply_fund extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = ProgressDialog.show(FundingActivity.this, "Loading...", "Please Wait ...", true);
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();

            String queriesurl = Constant.APPLY_FUNDING_APT
                    + "session_user_id=" + user.get("user_id")
                    + "&page_name=viewfundingdetails";

            String json = sh.makeServiceCall(queriesurl, ServiceHandler.POST);

            if (json != null) {

                applyfund_list = new ArrayList<>();

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    JSONArray apply_fund = jsonObj.getJSONArray("viewfundinglist");

                    for (int k = 0; k <= apply_fund.length(); k++) {

                        String fundingid = apply_fund.getJSONObject(k).getString("fundingid");
                        String dealername = apply_fund.getJSONObject(k).getString("dealername");
                        String dealermobileno = apply_fund.getJSONObject(k).getString("dealermobileno");
                        String dealermailid = apply_fund.getJSONObject(k).getString("dealermailid");
                        String requested_amount = apply_fund.getJSONObject(k).getString("requested_amount");
                        String created_date = apply_fund.getJSONObject(k).getString("created_date");
                        String dealercity = apply_fund.getJSONObject(k).getString("dealercity");
                        String branchname = apply_fund.getJSONObject(k).getString("branchname");
                        String dealer_listing_id = apply_fund.getJSONObject(k).getString("dealer_listing_id");
                        String status = apply_fund.getJSONObject(k).getString("status");
                        String dealer_funding_ticket_id = apply_fund.getJSONObject(k).getString("dealer_funding_ticket_id");

                        applyfundlist = new HashMap<>();

                        applyfundlist.put("fundingid", fundingid);
                        applyfundlist.put("dealername", dealername);
                        applyfundlist.put("dealermobileno", dealermobileno);
                        applyfundlist.put("dealermailid", dealermailid);
                        applyfundlist.put("requested_amount", requested_amount);
                        applyfundlist.put("created_date", created_date);
                        applyfundlist.put("dealercity", dealercity);
                        applyfundlist.put("branchname", branchname);
                        applyfundlist.put("dealer_listing_id", dealer_listing_id);
                        applyfundlist.put("status", status);
                        applyfundlist.put("dealer_funding_ticket_id", dealer_funding_ticket_id);

                        applyfund_list.add(applyfundlist);


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

            applyfunding_listview = (ListView) findViewById(R.id.funding_listview);

            applyFundingListAdapter = new ApplyFundingListAdapter(FundingActivity.this, getFundData());
            applyfunding_listview.setAdapter(applyFundingListAdapter);

            applyfunding_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ApplyFundingListModel applyFundingListModel = (ApplyFundingListModel) parent.getItemAtPosition(position);
                    Intent intent = new Intent(FundingActivity.this, FundingViewActivity.class);
                    intent.putExtra("list_funding1", applyFundingListModel.getBranchname());
                    intent.putExtra("list_funding2", applyFundingListModel.getDealer_funding_ticket_id());
                    intent.putExtra("list_funding3", applyFundingListModel.getDealermobileno());
                    intent.putExtra("list_funding4", applyFundingListModel.getDealercity());
                    intent.putExtra("list_funding5", applyFundingListModel.getStatus());
                    intent.putExtra("list_funding6", applyFundingListModel.getFundingid());

                    intent.putExtra("ticket_id", applyfundlist.get("fundingid"));
                    startActivity(intent);

                }
            });


        }

    }


}
