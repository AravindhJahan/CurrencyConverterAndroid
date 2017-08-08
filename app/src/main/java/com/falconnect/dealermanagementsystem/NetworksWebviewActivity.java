package com.falconnect.dealermanagementsystem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.falconnect.dealermanagementsystem.Adapter.CustomList;
import com.falconnect.dealermanagementsystem.FontAdapter.RoundImageTransform;
import com.falconnect.dealermanagementsystem.NavigationDrawer.BuyPageNavigation;
import com.falconnect.dealermanagementsystem.SharedPreference.PlandetailsSharedPreferences;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;
import com.freshdesk.hotline.FaqOptions;
import com.freshdesk.hotline.Hotline;
import com.navdrawer.SimpleSideDrawer;

import java.util.HashMap;

public class NetworksWebviewActivity extends Activity {

    String title, url;
    WebView webView;
    ImageView my_queries_back;
    BuyPageNavigation queries_buypagenavigation;
    SessionManager session_queries;
    ImageView imageView_queries;
    TextView profile_name_queries;
    TextView profile_address_queries;
    HashMap<String, String> user;
    String saved_name_queries, saved_address_queries;
    ProgressDialog barProgressDialog;
    RelativeLayout result_found;
    PlandetailsSharedPreferences plandetailsSharedPreferences;
    HashMap<String, String> plan;
    private SimpleSideDrawer mNav_queries;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_networks_webview);

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        url = getIntent().getStringExtra("url");

        webView = (WebView) findViewById(R.id.networks_webview);
        my_queries_back = (ImageView) findViewById(R.id.networks_back);
        barProgressDialog = ProgressDialog.show(NetworksWebviewActivity.this, "Loading...", "Please Wait ...", true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                barProgressDialog.dismiss();
            }

        });

        my_queries_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mNav_queries = new SimpleSideDrawer(this);
        mNav_queries.setLeftBehindContentView(R.layout.activity_behind_left_simple);

        imageView_queries = (ImageView) mNav_queries.findViewById(R.id.profile_avatar);
        profile_name_queries = (TextView) mNav_queries.findViewById(R.id.profile_name);
        profile_address_queries = (TextView) mNav_queries.findViewById(R.id.profile_address);

        session_queries = new SessionManager(NetworksWebviewActivity.this);
        user = session_queries.getUserDetails();
        saved_name_queries = user.get("dealer_name");
        saved_address_queries = user.get("dealershipname");
        profile_name_queries.setText(saved_name_queries);
        if (user.get("dealer_img").isEmpty()) {
            Glide.with(getApplicationContext())
                    .load(R.drawable.default_avatar)
                    .transform(new RoundImageTransform(NetworksWebviewActivity.this))
                    .into(imageView_queries);
        } else {
            Glide.with(getApplicationContext())
                    .load(user.get("dealer_img"))
                    .transform(new RoundImageTransform(NetworksWebviewActivity.this))
                    .into(imageView_queries);
        }
        profile_address_queries.setText(saved_address_queries);

        my_queries_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNav_queries.toggleLeftDrawer();
            }
        });

        RelativeLayout call_layout = (RelativeLayout) mNav_queries.findViewById(R.id.call_layout);
        RelativeLayout chat_layout = (RelativeLayout) mNav_queries.findViewById(R.id.chat_layout);
        RelativeLayout logout_layout = (RelativeLayout) mNav_queries.findViewById(R.id.logout_layout);
        result_found = (RelativeLayout) findViewById(R.id.result_found);

        call_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertbox = new AlertDialog.Builder(NetworksWebviewActivity.this)
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
                session_queries.logoutUser();
                mNav_queries.closeLeftSide();
                NetworksWebviewActivity.this.finish();
            }
        });

        chat_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chat();
                mNav_queries.closeLeftSide();
            }
        });

        //NAVIGATION DRAWER LIST VIEW
        queries_buypagenavigation = new BuyPageNavigation();
        CustomList adapter = new CustomList(NetworksWebviewActivity.this, BuyPageNavigation.web, BuyPageNavigation.imageId);

        ListView list = (ListView) findViewById(R.id.nav_list_view);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (BuyPageNavigation.web[position] == "Dashboard") {
                    Intent intent = new Intent(NetworksWebviewActivity.this, MainDashBoardActivity.class);
                    startActivity(intent);
                    NetworksWebviewActivity.this.finish();
                    mNav_queries.closeLeftSide();
                    //Toast.makeText(MyQueriesActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Buy") {
                    Intent intent = new Intent(NetworksWebviewActivity.this, DashBoard.class);
                    startActivity(intent);
                    NetworksWebviewActivity.this.finish();
                    mNav_queries.closeLeftSide();
                    // Toast.makeText(MyQueriesActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Sell") {
                    Intent intent = new Intent(NetworksWebviewActivity.this, SellDashBoardActivity.class);
                    startActivity(intent);
                    NetworksWebviewActivity.this.finish();
                    mNav_queries.closeLeftSide();
                    //  Toast.makeText(MyQueriesActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Manage") {
                    Intent intent = new Intent(NetworksWebviewActivity.this, ManageDashBoardActivity.class);
                    startActivity(intent);
                    NetworksWebviewActivity.this.finish();
                    mNav_queries.closeLeftSide();
                    //Toast.makeText(NetworksWebviewActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Funding") {
                    plandetailsSharedPreferences = new PlandetailsSharedPreferences(NetworksWebviewActivity.this);
                    plan = plandetailsSharedPreferences.getUserDetails();

//                    if ( plan.get("PlanName").equals("BASIC")) {
//
//                        Intent intent = new Intent(NetworksWebviewActivity.this, LoanActivity.class);
//                        startActivity(intent);
//                        mNav_queries.closeLeftSide();
//                        NetworksWebviewActivity.this.finish();
//                    } else {
                        Intent intent = new Intent(NetworksWebviewActivity.this, FundingActivity.class);
                        startActivity(intent);
                        mNav_queries.closeLeftSide();
                        NetworksWebviewActivity.this.finish();
//                    }
                    //   Toast.makeText(MyQueriesActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Networks") {
                    mNav_queries.closeLeftSide();
                    // Toast.makeText(MyQueriesActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Reports") {
                    plandetailsSharedPreferences = new PlandetailsSharedPreferences(NetworksWebviewActivity.this);
                    plan = plandetailsSharedPreferences.getUserDetails();
                    if (plan.get("PlanName").equals("BASIC")) {
                        Intent intent = new Intent(NetworksWebviewActivity.this, BuyPlanActivity.class);
                        startActivity(intent);
                        //MainDashBoardActivity.this.finish();
                        mNav_queries.closeLeftSide();
                    } else {
                        Intent intent = new Intent(NetworksWebviewActivity.this, ReportSalesActivity.class);
                        startActivity(intent);
                        NetworksWebviewActivity.this.finish();
                        mNav_queries.closeLeftSide();
                    }
                } else if (BuyPageNavigation.web[position] == "FAQs") {
                    faqs();
                    mNav_queries.closeLeftSide();
                } else {
                    //Toast.makeText(DashBoard.this, web[position], Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void faqs() {
        FaqOptions faqOptions = new FaqOptions()
                .showFaqCategoriesAsGrid(true)
                .showContactUsOnAppBar(true)
                .showContactUsOnFaqScreens(false)
                .showContactUsOnFaqNotHelpful(false);

        Hotline.showFAQs(NetworksWebviewActivity.this, faqOptions);
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
                        NetworksWebviewActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();

    }

}
