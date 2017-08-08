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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.falconnect.dealermanagementsystem.Adapter.CustomList;
import com.falconnect.dealermanagementsystem.Adapter.MyPostingListAdapter;
import com.falconnect.dealermanagementsystem.Adapter.SellFooterCustomAdapter;
import com.falconnect.dealermanagementsystem.FontAdapter.RoundImageTransform;
import com.falconnect.dealermanagementsystem.Model.MyPostingListModel;
import com.falconnect.dealermanagementsystem.Model.SellFooterDataModel;
import com.falconnect.dealermanagementsystem.NavigationDrawer.BuyPageNavigation;
import com.falconnect.dealermanagementsystem.SharedPreference.PlandetailsSharedPreferences;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;
import com.freshdesk.hotline.FaqOptions;
import com.freshdesk.hotline.Hotline;
import com.navdrawer.SimpleSideDrawer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MyPostingActivity extends AppCompatActivity {

    private static RecyclerView sellmyposting_footer;
    private static ArrayList<SellFooterDataModel> sellfooterdata;
    private static RecyclerView.Adapter selladapter;
    public ArrayList<HashMap<String, String>> my_posting_list;
    ImageView myposting_mnav;
    SessionManager session_myposting;
    ImageView imageView_myposting;
    TextView profile_name_myposting;
    TextView profile_address_myposting;
    String saved_name_myposting, saved_address_myposting;
    BuyPageNavigation myposting_buypagenavigation;
    HashMap<String, String> user;
    HashMap<String, String> mypositinglist;
    MyPostingListAdapter myPostingListAdapter;
    ListView myposting;
    ProgressDialog barProgressDialog;
    PlandetailsSharedPreferences plandetailsSharedPreferences;
    HashMap<String, String> plan;
    private boolean mVisible;
    private SimpleSideDrawer mNav_myposting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_posting);

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

        sellmyposting_footer = (RecyclerView) findViewById(R.id.my_recycler_my_posting);
        sellmyposting_footer.setHasFixedSize(true);
        sellmyposting_footer.setLayoutManager(new LinearLayoutManager(MyPostingActivity.this, LinearLayoutManager.HORIZONTAL, false));
        sellfooterdata = new ArrayList<SellFooterDataModel>();
        for (int i = 0; i < MyFooterSellData.sellnameArray.length; i++) {

            sellfooterdata.add(new SellFooterDataModel(
                    MyFooterSellData.sellnameArray[i],
                    MyFooterSellData.selldrawableArrayWhite1[i],
                    MyFooterSellData.sellid_[i]
            ));
        }
        selladapter = new SellFooterCustomAdapter(MyPostingActivity.this, sellfooterdata);
        sellmyposting_footer.setAdapter(selladapter);

        mNav_myposting = new SimpleSideDrawer(this);
        mNav_myposting.setLeftBehindContentView(R.layout.activity_behind_left_simple);

        imageView_myposting = (ImageView) mNav_myposting.findViewById(R.id.profile_avatar);
        profile_name_myposting = (TextView) mNav_myposting.findViewById(R.id.profile_name);
        profile_address_myposting = (TextView) mNav_myposting.findViewById(R.id.profile_address);

        session_myposting = new SessionManager(MyPostingActivity.this);
        user = session_myposting.getUserDetails();
        saved_name_myposting = user.get("dealer_name");
        saved_address_myposting = user.get("dealershipname");
        profile_name_myposting.setText(saved_name_myposting);

        RelativeLayout call_layout = (RelativeLayout) mNav_myposting.findViewById(R.id.call_layout);
        RelativeLayout chat_layout = (RelativeLayout) mNav_myposting.findViewById(R.id.chat_layout);
        RelativeLayout logout_layout = (RelativeLayout) mNav_myposting.findViewById(R.id.logout_layout);

        call_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertbox = new AlertDialog.Builder(MyPostingActivity.this)
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
                session_myposting.logoutUser();
                mNav_myposting.closeLeftSide();
                MyPostingActivity.this.finish();
            }
        });

        chat_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chat();
                mNav_myposting.closeLeftSide();
            }
        });
        if (user.get("dealer_img").isEmpty()) {
            Glide.with(getApplicationContext())
                    .load(R.drawable.default_avatar)
                    .transform(new RoundImageTransform(MyPostingActivity.this))
                    .into(imageView_myposting);
        } else {
            Glide.with(getApplicationContext())
                    .load(user.get("dealer_img"))
                    .transform(new RoundImageTransform(MyPostingActivity.this))
                    .into(imageView_myposting);
        }
        profile_address_myposting.setText(saved_address_myposting);
        myposting_mnav  = (ImageView) findViewById(R.id.myposting_mnav);
        myposting_mnav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNav_myposting.toggleLeftDrawer();
            }
        });

        //NAVIGATION DRAWER LIST VIEW
        myposting_buypagenavigation = new BuyPageNavigation();
        CustomList adapter = new CustomList(MyPostingActivity.this, BuyPageNavigation.web, BuyPageNavigation.imageId);
        ListView list = (ListView) findViewById(R.id.nav_list_view);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (BuyPageNavigation.web[position] == "Dashboard") {
                    Intent intent = new Intent(MyPostingActivity.this, MainDashBoardActivity.class);
                    startActivity(intent);
                    mNav_myposting.closeLeftSide();
                    MyPostingActivity.this.finish();
                    //  Toast.makeText(MyPostingActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Buy") {
                    Intent intent = new Intent(MyPostingActivity.this, DashBoard.class);
                    startActivity(intent);
                    mNav_myposting.closeLeftSide();
                    MyPostingActivity.this.finish();
                    //  Toast.makeText(MyPostingActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Sell") {
                    mNav_myposting.closeLeftSide();
                    Toast.makeText(MyPostingActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Manage") {
                    Intent intent = new Intent(MyPostingActivity.this, ManageDashBoardActivity.class);
                    startActivity(intent);
                    MyPostingActivity.this.finish();
                    mNav_myposting.closeLeftSide();
                    //  Toast.makeText(MyPostingActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Funding") {
                    plan = plandetailsSharedPreferences.getUserDetails();

//                    if ( plan.get("PlanName").equals("BASIC")) {
//                        Intent intent = new Intent(MyPostingActivity.this, LoanActivity.class);
//                        startActivity(intent);
//                        mNav_myposting.closeLeftSide();
//                        MyPostingActivity.this.finish();
//                    } else {
                        Intent intent = new Intent(MyPostingActivity.this, FundingActivity.class);
                        startActivity(intent);
                        mNav_myposting.closeLeftSide();
                        MyPostingActivity.this.finish();
//                    }
                    //   Toast.makeText(MyPostingActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Networks") {
                    String url = "http://app.dealerplus.in/user_login_cometchat?session_user_id=" + user.get("user_id");
                    Intent intet = new Intent(MyPostingActivity.this, NetworksWebviewActivity.class);
                    //intet.putExtra("title", BuyPageNavigation.web[position]);
                    intet.putExtra("url", url);
                    startActivity(intet);
                    mNav_myposting.closeLeftSide();
                    //  Toast.makeText(MyPostingActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Reports") {
                    plandetailsSharedPreferences = new PlandetailsSharedPreferences(MyPostingActivity.this);
                    plan = plandetailsSharedPreferences.getUserDetails();
                    if (plan.get("PlanName").equals("BASIC")) {

                        Intent intent = new Intent(MyPostingActivity.this, BuyPlanActivity.class);
                        startActivity(intent);
                        //MainDashBoardActivity.this.finish();
                        mNav_myposting.closeLeftSide();
                    } else {
                        Intent intent = new Intent(MyPostingActivity.this, ReportSalesActivity.class);
                        startActivity(intent);
                        MyPostingActivity.this.finish();
                        mNav_myposting.closeLeftSide();
                    }
                    //   Toast.makeText(MyPostingActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "FAQs") {
                    faqs();
                    mNav_myposting.closeLeftSide();
                } else {
                    //Toast.makeText(DashBoard.this, web[position], Toast.LENGTH_SHORT).show();
                }
            }
        });

        new my_posting().execute();

    }


    @Override
    public void onBackPressed() {

        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to exit application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        MyPostingActivity.this.finish();
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

        Hotline.showFAQs(MyPostingActivity.this, faqOptions);
    }

    public void chat() {
        Hotline.showConversations(getApplicationContext());
        // Hotline.clearUserData(getApplicationContext());
    }

    private ArrayList<MyPostingListModel> getmypostingData() {
        final ArrayList<MyPostingListModel> mypostdata = new ArrayList<>();
        for (int i = 0; i < my_posting_list.size(); i++) {

            String imageurl = my_posting_list.get(i).get("imageurl");
            String car_id = my_posting_list.get(i).get("car_id");
            String year = my_posting_list.get(i).get("year");
            String price = my_posting_list.get(i).get("price");
            String kms = my_posting_list.get(i).get("kms");
            String owner_type = my_posting_list.get(i).get("owner_type");
            String fuel_type = my_posting_list.get(i).get("fuel_type");
            String make = my_posting_list.get(i).get("make");
            String model = my_posting_list.get(i).get("model");
            String varient = my_posting_list.get(i).get("varient");
            String colors = my_posting_list.get(i).get("colors");

            String mongopushdate = my_posting_list.get(i).get("mongopushdate");

            mypostdata.add(new MyPostingListModel(imageurl, car_id, year, price, kms, owner_type,
                    fuel_type, make, model, varient,colors,  mongopushdate));
        }
        return mypostdata;
    }

    private class my_posting extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = ProgressDialog.show(MyPostingActivity.this, "Loading...", "Please Wait ...", true);
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();

            String queriesurl = Constant.MY_POSTING
                    + "session_user_id=" + user.get("user_id")
                    +"&page_name=viewmypost";

            Log.e("queriesurl",queriesurl);

            String json = sh.makeServiceCall(queriesurl, ServiceHandler.POST );

            if (json != null) {

                my_posting_list = new ArrayList<>();

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    JSONArray loan = jsonObj.getJSONArray("myposting_list");

                    for (int k = 0; k <= loan.length(); k++) {

                        String imageurl = loan.getJSONObject(k).getString("imageurl");
                        String car_id  = loan.getJSONObject(k).getString("car_id");
                        String year = loan.getJSONObject(k).getString("year");
                        String price = loan.getJSONObject(k).getString("price");
                        String kms = loan.getJSONObject(k).getString("kms");
                        String owner_type = loan.getJSONObject(k).getString("owner_type");
                        String fuel_type = loan.getJSONObject(k).getString("fuel_type");
                        String make = loan.getJSONObject(k).getString("make");
                        String model = loan.getJSONObject(k).getString("model");
                        String varient = loan.getJSONObject(k).getString("varient");
                        String colors = loan.getJSONObject(k).getString("colors");

                       /* String imagecount = loan.getJSONObject(k).getString("imagecount");
                        String videoscount = loan.getJSONObject(k).getString("videoscount");
                        String documentcount = loan.getJSONObject(k).getString("documentcount");
                        String viewscount = loan.getJSONObject(k).getString("viewscount");*/
                        String mongopushdate = loan.getJSONObject(k).getString("mongopushdate");

                        mypositinglist = new HashMap<>();

                        mypositinglist.put("imageurl", imageurl);
                        mypositinglist.put("car_id", car_id);
                        mypositinglist.put("year", year);
                        mypositinglist.put("price", price);
                        mypositinglist.put("kms", kms);
                        mypositinglist.put("owner_type", owner_type);
                        mypositinglist.put("fuel_type", fuel_type);
                        mypositinglist.put("make", make);
                        mypositinglist.put("model", model);
                        mypositinglist.put("varient", varient);
                        mypositinglist.put("colors", colors);
               /*       mypositinglist.put("imagecount", imagecount);
                        mypositinglist.put("videoscount", videoscount);
                        mypositinglist.put("documentcount", documentcount);
                        mypositinglist.put("viewscount", viewscount);*/
                        mypositinglist.put("mongopushdate", mongopushdate);

                        my_posting_list.add(mypositinglist);

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

            myposting = (ListView) findViewById(R.id.my_posting_listview);
            myPostingListAdapter = new MyPostingListAdapter(MyPostingActivity.this, getmypostingData());
            //myposting.setAdapter(myPostingListAdapter);

            RelativeLayout result_found = (RelativeLayout) findViewById(R.id.result_found);
            if (myPostingListAdapter.getCount() != 0) {
                myposting.setAdapter(myPostingListAdapter);
            } else {
                myposting.setVisibility(View.GONE);
                result_found.setVisibility(View.VISIBLE);
            }

            myposting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    MyPostingListModel item = (MyPostingListModel) parent.getItemAtPosition(position);
                    Intent intent = new Intent(MyPostingActivity.this, MyPostingDetailsActivity.class);
                    intent.putExtra("carID", item.getCar_id());
                    startActivity(intent);
                }
            });
        }

    }

}
