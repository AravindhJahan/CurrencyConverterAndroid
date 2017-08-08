package com.falconnect.dealermanagementsystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import com.bumptech.glide.Glide;
import com.falconnect.dealermanagementsystem.Adapter.CustomList;
import com.falconnect.dealermanagementsystem.Adapter.SellFooterCustomAdapter;
import com.falconnect.dealermanagementsystem.FontAdapter.RoundImageTransform;
import com.falconnect.dealermanagementsystem.Model.SellFooterDataModel;
import com.falconnect.dealermanagementsystem.NavigationDrawer.BuyPageNavigation;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;
import com.freshdesk.hotline.FaqOptions;
import com.freshdesk.hotline.Hotline;
import com.navdrawer.SimpleSideDrawer;

import java.util.ArrayList;
import java.util.HashMap;

public class AuctionActivity extends AppCompatActivity {

    private static RecyclerView sellacution_footer;
    private static ArrayList<SellFooterDataModel> sellfooterdata;
    private static RecyclerView.Adapter selladapter;
    ImageView auction_mnav;
    SessionManager session_auction;
    ImageView imageView_auction;
    TextView profile_name_auction;
    TextView profile_address_auction;
    String saved_name_auction, saved_address_auction;
    BuyPageNavigation auction_buypagenavigation;
    HashMap<String, String> user;
    private boolean mVisible;
    private SimpleSideDrawer mNav_auction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_acution);
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

        sellacution_footer = (RecyclerView) findViewById(R.id.auction_recycle_view);
        sellacution_footer.setHasFixedSize(true);
        sellacution_footer.setLayoutManager(new LinearLayoutManager(AuctionActivity.this, LinearLayoutManager.HORIZONTAL, false));
        sellfooterdata = new ArrayList<SellFooterDataModel>();
        for (int i = 0; i < MyFooterSellData.sellnameArray.length; i++) {

            sellfooterdata.add(new SellFooterDataModel(
                    MyFooterSellData.sellnameArray[i],
                    MyFooterSellData.selldrawableArrayWhite2[i],
                    MyFooterSellData.sellid_[i]
            ));
        }
        selladapter = new SellFooterCustomAdapter(AuctionActivity.this, sellfooterdata);
        sellacution_footer.setAdapter(selladapter);

        mNav_auction = new SimpleSideDrawer(this);
        mNav_auction.setLeftBehindContentView(R.layout.activity_behind_left_simple);

        imageView_auction = (ImageView) mNav_auction.findViewById(R.id.profile_avatar);
        profile_name_auction = (TextView) mNav_auction.findViewById(R.id.profile_name);
        profile_address_auction = (TextView) mNav_auction.findViewById(R.id.profile_address);

        session_auction = new SessionManager(AuctionActivity.this);
        user = session_auction.getUserDetails();
        saved_name_auction = user.get("dealer_name");
        saved_address_auction = user.get("dealershipname");
        profile_name_auction.setText(saved_name_auction);

        RelativeLayout call_layout = (RelativeLayout) mNav_auction.findViewById(R.id.call_layout);
        RelativeLayout chat_layout = (RelativeLayout) mNav_auction.findViewById(R.id.chat_layout);
        RelativeLayout logout_layout = (RelativeLayout) mNav_auction.findViewById(R.id.logout_layout);

        logout_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session_auction.logoutUser();
                mNav_auction.closeLeftSide();
                AuctionActivity.this.finish();
            }
        });

        call_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertbox = new AlertDialog.Builder(AuctionActivity.this)
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

        chat_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chat();
                mNav_auction.closeLeftSide();
            }
        });

        if (user.get("dealer_img").isEmpty()) {
            Glide.with(getApplicationContext())
                    .load(R.drawable.default_avatar)
                    .transform(new RoundImageTransform(AuctionActivity.this))
                    .into(imageView_auction);
        } else {
            Glide.with(getApplicationContext())
                    .load(user.get("dealer_img"))
                    .transform(new RoundImageTransform(AuctionActivity.this))
                    .into(imageView_auction);
        }
        profile_address_auction.setText(saved_address_auction);
        auction_mnav  = (ImageView) findViewById(R.id.auction_mnav);
        auction_mnav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNav_auction.toggleLeftDrawer();
            }
        });

        //NAVIGATION DRAWER LIST VIEW
        auction_buypagenavigation = new BuyPageNavigation();
        CustomList adapter = new CustomList(AuctionActivity.this, BuyPageNavigation.web, BuyPageNavigation.imageId);
        ListView list = (ListView) findViewById(R.id.nav_list_view);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (BuyPageNavigation.web[position] == "Dashboard") {
                    Intent intent = new Intent(AuctionActivity.this, MainDashBoardActivity.class);
                    startActivity(intent);
                    mNav_auction.closeLeftSide();
                    AuctionActivity.this.finish();
                    // Toast.makeText(AuctionActivity.this, auction_buypagenavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Buy") {
                    Intent intent = new Intent(AuctionActivity.this, DashBoard.class);
                    startActivity(intent);
                    mNav_auction.closeLeftSide();
                    AuctionActivity.this.finish();
                    //  Toast.makeText(AuctionActivity.this, auction_buypagenavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Sell") {
                    mNav_auction.closeLeftSide();
                    //  Toast.makeText(AuctionActivity.this, auction_buypagenavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Manage") {
                    Intent intent = new Intent(AuctionActivity.this, ManageDashBoardActivity.class);
                    startActivity(intent);
                    AuctionActivity.this.finish();
                    mNav_auction.closeLeftSide();
                    //  Toast.makeText(AuctionActivity.this, auction_buypagenavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Funding") {
                    Intent intent = new Intent(AuctionActivity.this, FundingActivity.class);
                    startActivity(intent);
                    mNav_auction.closeLeftSide();
                    //  Toast.makeText(AuctionActivity.this, auction_buypagenavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Networks") {
                    String url = "http://dev.dealerplus.in/dev/public/user_login_cometchat?session_user_id=" + user.get("user_id");
                    Intent intet = new Intent(AuctionActivity.this, NetworksWebviewActivity.class);
                    //intet.putExtra("title", BuyPageNavigation.web[position]);
                    intet.putExtra("url", url);
                    startActivity(intet);
                    mNav_auction.closeLeftSide();
                    //  Toast.makeText(AuctionActivity.this, auction_buypagenavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Reports") {
                    mNav_auction.closeLeftSide();
                    //  Toast.makeText(AuctionActivity.this, auction_buypagenavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "FAQs") {
                    faqs();
                    mNav_auction.closeLeftSide();
                } else {
                    //Toast.makeText(DashBoard.this, web[position], Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to exit application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        AuctionActivity.this.finish();
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

        Hotline.showFAQs(AuctionActivity.this, faqOptions);
    }

    public void chat() {
        Hotline.showConversations(getApplicationContext());
        // Hotline.clearUserData(getApplicationContext());
    }
}
