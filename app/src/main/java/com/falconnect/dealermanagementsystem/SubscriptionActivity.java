package com.falconnect.dealermanagementsystem;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.falconnect.dealermanagementsystem.Adapter.CustomList;
import com.falconnect.dealermanagementsystem.FontAdapter.RoundImageTransform;
import com.falconnect.dealermanagementsystem.Model.ManageFooterDataModel;
import com.falconnect.dealermanagementsystem.NavigationDrawer.BuyPageNavigation;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;
import com.freshdesk.hotline.FaqOptions;
import com.freshdesk.hotline.Hotline;
import com.navdrawer.SimpleSideDrawer;

import java.util.ArrayList;
import java.util.HashMap;

public class SubscriptionActivity extends Activity {

    private static RecyclerView.Adapter subadapter;
    private static RecyclerView subrecycleview;
    private static ArrayList<ManageFooterDataModel> subdata;
    ImageView sub_Mnav;
    ImageView imageView_subscription;
    TextView profile_name_subscription;
    TextView profile_address_subscription;
    String saved_name_branch, saved_address_branch;
    BuyPageNavigation subscription_buypagenavigation;
    SessionManager session;
    HashMap<String, String> user;
    private boolean mVisible;
    private RecyclerView.LayoutManager layoutManager;
    private SimpleSideDrawer mNav_subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_subscription);

        mVisible = true;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        subrecycleview = (RecyclerView) findViewById(R.id.subscription_my_recycler);
        subrecycleview.setHasFixedSize(true);
        subrecycleview.setLayoutManager(new LinearLayoutManager(SubscriptionActivity.this, LinearLayoutManager.HORIZONTAL, false));

        subdata = new ArrayList<ManageFooterDataModel>();

       /* for (int i = 0; i < MyFooterManageData.managenameArray.length; i++) {

            subdata.add(new ManageFooterDataModel(
                    MyFooterManageData.managenameArray[i],
                    MyFooterManageData.managedrawableArrayWhite5[i],
                    MyFooterManageData.manageid_[i]
            ));
        }
        subadapter = new ManageFooterCustomAdapter(SubscriptionActivity.this, subdata);
        subrecycleview.setAdapter(subadapter);*/

        session = new SessionManager(SubscriptionActivity.this);
        user = session.getUserDetails();

        sub_Mnav = (ImageView) findViewById(R.id.subscription_nav);
        mNav_subscription = new SimpleSideDrawer(this);
        mNav_subscription.setLeftBehindContentView(R.layout.activity_behind_left_simple);

        imageView_subscription = (ImageView) mNav_subscription.findViewById(R.id.profile_avatar);
        profile_name_subscription = (TextView) mNav_subscription.findViewById(R.id.profile_name);
        profile_address_subscription = (TextView) mNav_subscription.findViewById(R.id.profile_address);
        user = session.getUserDetails();

        RelativeLayout call_layout = (RelativeLayout) mNav_subscription.findViewById(R.id.call_layout);
        RelativeLayout chat_layout = (RelativeLayout) mNav_subscription.findViewById(R.id.chat_layout);
        RelativeLayout logout_layout = (RelativeLayout) mNav_subscription.findViewById(R.id.logout_layout);


        call_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:9790928569"));
                startActivity(callIntent);
            }
        });

        logout_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
                mNav_subscription.closeLeftSide();
                SubscriptionActivity.this.finish();
            }
        });

        chat_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chat();
                mNav_subscription.closeLeftSide();
            }
        });

        saved_name_branch = user.get("dealer_name");
        saved_address_branch = user.get("dealershipname");
        profile_name_subscription.setText(saved_name_branch);
        if (user.get("dealer_img").isEmpty()) {
            Glide.with(getApplicationContext())
                    .load(R.drawable.default_avatar)
                    .transform(new RoundImageTransform(SubscriptionActivity.this))
                    .into(imageView_subscription);
        } else {
            Glide.with(getApplicationContext())
                    .load(user.get("dealer_img"))
                    .transform(new RoundImageTransform(SubscriptionActivity.this))
                    .into(imageView_subscription);
        }
        profile_address_subscription.setText(saved_address_branch);

        sub_Mnav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNav_subscription.toggleLeftDrawer();
            }
        });

        //NAVIGATION DRAWER LIST VIEW
        subscription_buypagenavigation = new BuyPageNavigation();
        CustomList adapter = new CustomList(SubscriptionActivity.this, BuyPageNavigation.web, BuyPageNavigation.imageId);
        ListView list = (ListView) findViewById(R.id.nav_list_view);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (BuyPageNavigation.web[position] == "Dashboard") {
                    Intent intent = new Intent(SubscriptionActivity.this, MainDashBoardActivity.class);
                    startActivity(intent);
                    SubscriptionActivity.this.finish();
                    mNav_subscription.closeLeftSide();
                    Toast.makeText(SubscriptionActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Buy") {
                    Intent intent = new Intent(SubscriptionActivity.this, DashBoard.class);
                    startActivity(intent);
                    SubscriptionActivity.this.finish();
                    mNav_subscription.closeLeftSide();
                    Toast.makeText(SubscriptionActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Sell") {
                    Intent intent = new Intent(SubscriptionActivity.this, SellDashBoardActivity.class);
                    startActivity(intent);
                    SubscriptionActivity.this.finish();
                    mNav_subscription.closeLeftSide();
                    Toast.makeText(SubscriptionActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Manage") {
                    mNav_subscription.closeLeftSide();
                    Toast.makeText(SubscriptionActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Funding") {
                    Intent intent = new Intent(SubscriptionActivity.this, FundingActivity.class);
                    startActivity(intent);
                    mNav_subscription.closeLeftSide();
                    Toast.makeText(SubscriptionActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Networks") {
                    String url = "http://app.dealerplus.in/user_login_cometchat?session_user_id=" + user.get("user_id");
                    Intent intet = new Intent(SubscriptionActivity.this, WebViewActivity.class);
                    intet.putExtra("title", BuyPageNavigation.web[position]);
                    intet.putExtra("url", url);
                    startActivity(intet);
                    mNav_subscription.closeLeftSide();
                    Toast.makeText(SubscriptionActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Reports") {
                    mNav_subscription.closeLeftSide();
                    Toast.makeText(SubscriptionActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "FAQs") {
                    faqs();
                    mNav_subscription.closeLeftSide();
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

        Hotline.showFAQs(SubscriptionActivity.this, faqOptions);
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
                        SubscriptionActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();

    }
}
