package com.falconnect.dealermanagementsystem;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.falconnect.dealermanagementsystem.Adapter.BidsPostedListAdapter;
import com.falconnect.dealermanagementsystem.Adapter.CustomAdapter;
import com.falconnect.dealermanagementsystem.Adapter.CustomList;
import com.falconnect.dealermanagementsystem.FontAdapter.RoundImageTransform;
import com.falconnect.dealermanagementsystem.Model.BidsPostedListModel;
import com.falconnect.dealermanagementsystem.Model.DataModel;
import com.falconnect.dealermanagementsystem.NavigationDrawer.BuyPageNavigation;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;
import com.freshdesk.hotline.FaqOptions;
import com.freshdesk.hotline.Hotline;
import com.navdrawer.SimpleSideDrawer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class BidsPostedActivity extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private static RecyclerView bidsrecyclerView;
    private static ArrayList<DataModel> data;
    public ArrayList<HashMap<String, String>> bids_list;
    SessionManager session;
    ImageView bids_back;
    HashMap<String, String> user;
    BuyPageNavigation bids_buypagenavigation;
    SessionManager session_bids;
    ImageView imageView_bids;
    TextView profile_name_bids;
    TextView profile_address_bids;
    String saved_name_bids, saved_address_bids;
    ListView bids_listview;
    HashMap<String, String> bidslist;
    BidsPostedListAdapter bidsPostedListAdapter;
    private boolean mVisible;
    private RecyclerView.LayoutManager layoutManager;
    private SimpleSideDrawer mNav_bids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bids_posted);

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

        bidsrecyclerView = (RecyclerView) findViewById(R.id.my_recycler_bids);
        bidsrecyclerView.setHasFixedSize(true);
        bidsrecyclerView.setLayoutManager(new LinearLayoutManager(BidsPostedActivity.this, LinearLayoutManager.HORIZONTAL, false));

        data = new ArrayList<DataModel>();
        for (int i = 0; i < MyData.nameArray.length; i++) {
            data.add(new DataModel(
                    MyData.nameArray[i],
                    MyData.id_[i],
                    MyData.drawableArrayWhite3[i]
            ));
        }

        adapter = new CustomAdapter(BidsPostedActivity.this, data);
        bidsrecyclerView.setAdapter(adapter);

        bids_back = (ImageView) findViewById(R.id.bids_back);

        mNav_bids = new SimpleSideDrawer(this);
        mNav_bids.setLeftBehindContentView(R.layout.activity_behind_left_simple);

        imageView_bids = (ImageView) mNav_bids.findViewById(R.id.profile_avatar);
        profile_name_bids = (TextView) mNav_bids.findViewById(R.id.profile_name);
        profile_address_bids = (TextView) mNav_bids.findViewById(R.id.profile_address);

        session_bids = new SessionManager(BidsPostedActivity.this);
        user = session_bids.getUserDetails();
        saved_name_bids = user.get("dealer_name");
        saved_address_bids = user.get("dealershipname");
        profile_name_bids.setText(saved_name_bids);
        if (user.get("dealer_img").isEmpty()) {
            Glide.with(getApplicationContext())
                    .load(R.drawable.default_avatar)
                    .transform(new RoundImageTransform(BidsPostedActivity.this))
                    .into(imageView_bids);
        } else {
            Glide.with(getApplicationContext())
                    .load(user.get("dealer_img"))
                    .transform(new RoundImageTransform(BidsPostedActivity.this))
                    .into(imageView_bids);
        }
        profile_address_bids.setText(saved_address_bids);

        bids_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNav_bids.toggleLeftDrawer();
            }
        });

        session = new SessionManager(BidsPostedActivity.this);

        RelativeLayout call_layout = (RelativeLayout) mNav_bids.findViewById(R.id.call_layout);
        RelativeLayout chat_layout = (RelativeLayout) mNav_bids.findViewById(R.id.chat_layout);
        RelativeLayout logout_layout = (RelativeLayout) mNav_bids.findViewById(R.id.logout_layout);

        logout_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
                mNav_bids.closeLeftSide();
                BidsPostedActivity.this.finish();
            }
        });

        chat_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chat();
                mNav_bids.closeLeftSide();
            }
        });
        //NAVIGATION DRAWER LIST VIEW
        bids_buypagenavigation = new BuyPageNavigation();
        CustomList adapter = new CustomList(BidsPostedActivity.this, BuyPageNavigation.web, BuyPageNavigation.imageId);
        ListView list = (ListView) findViewById(R.id.nav_list_view);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (BuyPageNavigation.web[position] == "Dashboard") {
                    Intent intent = new Intent(BidsPostedActivity.this, MainDashBoardActivity.class);
                    startActivity(intent);
                    BidsPostedActivity.this.finish();
                    mNav_bids.closeLeftSide();
                    //  Toast.makeText(BidsPostedActivity.this, bids_buypagenavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Buy") {
                    mNav_bids.closeLeftSide();
                    Toast.makeText(BidsPostedActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Sell") {
                    Intent intent = new Intent(BidsPostedActivity.this, SellDashBoardActivity.class);
                    startActivity(intent);
                    BidsPostedActivity.this.finish();
                    mNav_bids.closeLeftSide();
                    //  Toast.makeText(BidsPostedActivity.this, bids_buypagenavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Manage") {
                    Toast.makeText(BidsPostedActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                    mNav_bids.closeLeftSide();
                } else if (BuyPageNavigation.web[position] == "Funding") {
                    Intent intent = new Intent(BidsPostedActivity.this, FundingActivity.class);
                    startActivity(intent);
                    mNav_bids.closeLeftSide();
                    //  Toast.makeText(BidsPostedActivity.this, bids_buypagenavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Networks") {
                    String url = "http://dev.dealerplus.in/dev/public/user_login_cometchat?session_user_id=" + user.get("user_id");
                    Intent intet = new Intent(BidsPostedActivity.this, WebViewActivity.class);
                    intet.putExtra("title", BuyPageNavigation.web[position]);
                    intet.putExtra("url", url);
                    startActivity(intet);
                    // Toast.makeText(BidsPostedActivity.this, bids_buypagenavigation.web[position], Toast.LENGTH_SHORT).show();
                    mNav_bids.closeLeftSide();
                } else if (BuyPageNavigation.web[position] == "Reports") {
                    // Toast.makeText(BidsPostedActivity.this, bids_buypagenavigation.web[position], Toast.LENGTH_SHORT).show();
                    mNav_bids.closeLeftSide();
                } else if (BuyPageNavigation.web[position] == "FAQs") {
                    faqs();
                    mNav_bids.closeLeftSide();
                } else {
                    //Toast.makeText(DashBoard.this, web[position], Toast.LENGTH_SHORT).show();
                }
            }
        });

        bids_listview = (ListView) findViewById(R.id.bids_listview);

        new Bids_posted_data().execute();

    }

    public void faqs() {
        FaqOptions faqOptions = new FaqOptions()
                .showFaqCategoriesAsGrid(true)
                .showContactUsOnAppBar(true)
                .showContactUsOnFaqScreens(false)
                .showContactUsOnFaqNotHelpful(false);

        Hotline.showFAQs(BidsPostedActivity.this, faqOptions);
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
                        BidsPostedActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();

    }

    private ArrayList<BidsPostedListModel> getbidsdata() {
        final ArrayList<BidsPostedListModel> biddata = new ArrayList<>();
        for (int i = 0; i < bids_list.size(); i++) {
            String image = bids_list.get(i).get("imagelink");
            String bidded_amount = bids_list.get(i).get("bidded_amount");
            String make = bids_list.get(i).get("make");
            String posted = bids_list.get(i).get("posted");
            String closing_time = bids_list.get(i).get("closing_time");
            String site_image = bids_list.get(i).get("site_image");
            String bid_image = bids_list.get(i).get("bid_image");
            String car_id = bids_list.get(i).get("car_id");
            String dealer_id = bids_list.get(i).get("dealer_id");
            biddata.add(new BidsPostedListModel(image, bidded_amount, make, posted, closing_time, site_image, bid_image, car_id, dealer_id));
        }
        return biddata;
    }

    private class Bids_posted_data extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();

            String queriesurl = Constant.BIDS_POSTED_API
                    + "session_user_id="
                    + user.get("user_id");

            String json = sh.makeServiceCall(queriesurl, ServiceHandler.POST);

            if (json != null) {

                bids_list = new ArrayList<>();

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    JSONArray bids = jsonObj.getJSONArray("bidding_list");

                    for (int k = 0; k <= bids.length(); k++) {

                        String dealer_id = bids.getJSONObject(k).getString("dealer_id");
                        String car_id = bids.getJSONObject(k).getString("car_id");
                        String noimages = bids.getJSONObject(k).getString("noimages");
                        String imagelink = bids.getJSONObject(k).getString("imagelink");
                        String site_id = bids.getJSONObject(k).getString("site_id");
                        String bid_image = bids.getJSONObject(k).getString("bid_image");
                        String site_image = bids.getJSONObject(k).getString("site_image");
                        String posted = bids.getJSONObject(k).getString("posted");
                        String closing_time = bids.getJSONObject(k).getString("closing_time");
                        String make = bids.getJSONObject(k).getString("make");
                        String model = bids.getJSONObject(k).getString("model");
                        String bidded_amount = bids.getJSONObject(k).getString("bidded_amount");

                        bidslist = new HashMap<>();

                        bidslist.put("dealer_id", dealer_id);
                        bidslist.put("car_id", car_id);
                        bidslist.put("noimages", noimages);
                        bidslist.put("imagelink", imagelink);
                        bidslist.put("site_id", site_id);
                        bidslist.put("bid_image", bid_image);
                        bidslist.put("site_image", site_image);
                        bidslist.put("posted", posted);
                        bidslist.put("closing_time", closing_time);
                        bidslist.put("make", make);
                        bidslist.put("model", model);
                        bidslist.put("bidded_amount", bidded_amount);

                        bids_list.add(bidslist);

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

            bidsPostedListAdapter = new BidsPostedListAdapter(BidsPostedActivity.this, getbidsdata());
            bids_listview.setAdapter(bidsPostedListAdapter);
            bids_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    BidsPostedListModel bidsPostedListModel = (BidsPostedListModel) parent.getItemAtPosition(position);
                    Intent intent = new Intent(BidsPostedActivity.this, BidsPostingActivity.class);
                    intent.putExtra("car_id", bidsPostedListModel.getCar_id());
                    intent.putExtra("car_name", bidsPostedListModel.getCar_name());
                    intent.putExtra("dealer_id", bidsPostedListModel.getDealer_id());
                    intent.putExtra("car_image", bidsPostedListModel.getCar_image());
                    startActivity(intent);
                    Toast.makeText(BidsPostedActivity.this, bidsPostedListModel.getCar_name(), Toast.LENGTH_SHORT).show();

                }
            });

        }

    }

}
