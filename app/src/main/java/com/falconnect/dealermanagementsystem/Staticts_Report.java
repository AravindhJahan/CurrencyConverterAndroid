package com.falconnect.dealermanagementsystem;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

import com.bumptech.glide.Glide;
import com.falconnect.dealermanagementsystem.Adapter.CustomList;
import com.falconnect.dealermanagementsystem.Adapter.ReportFooterCustomAdapter;
import com.falconnect.dealermanagementsystem.Adapter.ReportRecycleAdapter;
import com.falconnect.dealermanagementsystem.FontAdapter.RoundImageTransform;
import com.falconnect.dealermanagementsystem.Model.ReportFooterDataModel;
import com.falconnect.dealermanagementsystem.Model.StaticReportModel;
import com.falconnect.dealermanagementsystem.NavigationDrawer.BuyPageNavigation;
import com.falconnect.dealermanagementsystem.SharedPreference.PlandetailsSharedPreferences;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;
import com.freshdesk.hotline.FaqOptions;
import com.freshdesk.hotline.Hotline;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.navdrawer.SimpleSideDrawer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class Staticts_Report extends Activity {

    private static RecyclerView.Adapter adapter;
    private static RecyclerView static_recyclerview;
    private static ArrayList<ReportFooterDataModel> inventorydata;
    public ArrayList<HashMap<String, String>> gridvalues;
    PieChart singlechart;
    ArrayList<String> char_values = new ArrayList<>();
    HashMap<String, String> grid_values;
    RecyclerView grid_recycler;
    SessionManager sessionManager;
    HashMap<String, String> user;

    PlandetailsSharedPreferences plandetailsSharedPreferences;
    HashMap<String, String> plan;

    String viewcarscount;

    ReportRecycleAdapter reportRecycleAdapter;


    TextView total_view_counts;

    BuyPageNavigation buyPageNavigation;
    ImageView imageView_inventory;
    ImageView inventory_mnav;
    TextView profile_name_inventory;
    RelativeLayout call_layout, chat_layout, logout_layout;
    TextView profile_address_inventory;
    String saved_name, saved_address;
    private SimpleSideDrawer mNav_inventory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_staticts__report);

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        singlechart = (PieChart) findViewById(R.id.singlechart);

        grid_recycler = (RecyclerView) findViewById(R.id.griddatas);

        sessionManager = new SessionManager(Staticts_Report.this);
        user = sessionManager.getUserDetails();

        static_recyclerview = (RecyclerView) findViewById(R.id.static_recyclerview);
        static_recyclerview.setHasFixedSize(true);
        static_recyclerview.setLayoutManager(new LinearLayoutManager(Staticts_Report.this, LinearLayoutManager.HORIZONTAL, false));
        inventorydata = new ArrayList<ReportFooterDataModel>();
        for (int i = 0; i < MyFooterReportData.reportnameArray.length; i++) {
            inventorydata.add(new ReportFooterDataModel(
                    MyFooterReportData.reportnameArray[i],
                    MyFooterReportData.reportdrawableArrayWhite2[i],
                    MyFooterReportData.reportid_[i]
            ));
        }
        adapter = new ReportFooterCustomAdapter(Staticts_Report.this, inventorydata);
        static_recyclerview.setAdapter(adapter);

        mNav_inventory = new SimpleSideDrawer(this);
        mNav_inventory.setLeftBehindContentView(R.layout.activity_behind_left_simple);

        imageView_inventory = (ImageView) mNav_inventory.findViewById(R.id.profile_avatar);
        profile_name_inventory = (TextView) mNav_inventory.findViewById(R.id.profile_name);
        profile_address_inventory = (TextView) mNav_inventory.findViewById(R.id.profile_address);
        user = sessionManager.getUserDetails();
        saved_name = user.get("dealer_name");
        saved_address = user.get("dealershipname");
        profile_name_inventory.setText(saved_name);
        if (user.get("dealer_img").isEmpty()) {
            Glide.with(getApplicationContext())
                    .load(R.drawable.default_avatar)
                    .transform(new RoundImageTransform(Staticts_Report.this))
                    .into(imageView_inventory);
        } else {
            Glide.with(getApplicationContext())
                    .load(user.get("dealer_img"))
                    .transform(new RoundImageTransform(Staticts_Report.this))
                    .into(imageView_inventory);
        }
        profile_address_inventory.setText(saved_address);

        inventory_mnav = (ImageView) findViewById(R.id.inventory_mnav);

        inventory_mnav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNav_inventory.toggleLeftDrawer();
            }
        });


        call_layout = (RelativeLayout) mNav_inventory.findViewById(R.id.call_layout);
        chat_layout = (RelativeLayout) mNav_inventory.findViewById(R.id.chat_layout);
        logout_layout = (RelativeLayout) mNav_inventory.findViewById(R.id.logout_layout);

        call_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertbox = new AlertDialog.Builder(Staticts_Report.this)
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

        total_view_counts = (TextView) findViewById(R.id.total_view_counts);

        logout_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logoutUser();
                mNav_inventory.closeLeftSide();
                Staticts_Report.this.finish();
            }
        });

        chat_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chat();
                mNav_inventory.closeLeftSide();
            }
        });

        //NAVIGATION DRAWER LIST VIEW
        buyPageNavigation = new BuyPageNavigation();
        CustomList adapter = new CustomList(Staticts_Report.this, BuyPageNavigation.web, BuyPageNavigation.imageId);
        ListView list = (ListView) findViewById(R.id.nav_list_view);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (BuyPageNavigation.web[position] == "Dashboard") {
                    Intent intent = new Intent(Staticts_Report.this, MainDashBoardActivity.class);
                    startActivity(intent);
                    Staticts_Report.this.finish();
                    mNav_inventory.closeLeftSide();
                    //  Toast.makeText(Staticts_Report.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Buy") {
                    Intent intent = new Intent(Staticts_Report.this, DashBoard.class);
                    startActivity(intent);
                    Staticts_Report.this.finish();
                    mNav_inventory.closeLeftSide();
                    // Toast.makeText(Staticts_Report.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Sell") {
                    Intent intent = new Intent(Staticts_Report.this, SellDashBoardActivity.class);
                    startActivity(intent);
                    Staticts_Report.this.finish();
                    mNav_inventory.closeLeftSide();
                    //   Toast.makeText(Staticts_Report.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Manage") {
                    Intent intent = new Intent(Staticts_Report.this, ManageDashBoardActivity.class);
                    startActivity(intent);
                    Staticts_Report.this.finish();
                    mNav_inventory.closeLeftSide();
                    //  Toast.makeText(Staticts_Report.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Funding") {
                    plandetailsSharedPreferences = new PlandetailsSharedPreferences(Staticts_Report.this);
                    plan = plandetailsSharedPreferences.getUserDetails();

//                    if ( plan.get("PlanName").equals("BASIC")) {
//
//                        Intent intent = new Intent(Staticts_Report.this, LoanActivity.class);
//                        startActivity(intent);
//                        mNav_inventory.closeLeftSide();
//                        Staticts_Report.this.finish();
//                    } else {
                        Intent intent = new Intent(Staticts_Report.this, FundingActivity.class);
                        startActivity(intent);
                        mNav_inventory.closeLeftSide();
                        Staticts_Report.this.finish();
//                    }   // Toast.makeText(Staticts_Report.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Networks") {
                    String url = "http://app.dealerplus.in/user_login_cometchat?session_user_id=" + user.get("user_id");
                    Intent intet = new Intent(Staticts_Report.this, WebViewActivity.class);
                    intet.putExtra("title", BuyPageNavigation.web[position]);
                    intet.putExtra("url", url);
                    startActivity(intet);
                    mNav_inventory.closeLeftSide();
                    //   Toast.makeText(Staticts_Report.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Reports") {
                    mNav_inventory.closeLeftSide();
                    // Toast.makeText(Staticts_Report.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "FAQs") {
                    faqs();
                    mNav_inventory.closeLeftSide();
                    // Toast.makeText(Staticts_Report.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(DashBoard.this, web[position], Toast.LENGTH_SHORT).show();
                }

            }
        });

        new datas().execute();

    }

    public void faqs() {
        FaqOptions faqOptions = new FaqOptions()
                .showFaqCategoriesAsGrid(true)
                .showContactUsOnAppBar(true)
                .showContactUsOnFaqScreens(false)
                .showContactUsOnFaqNotHelpful(false);

        Hotline.showFAQs(Staticts_Report.this, faqOptions);
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
                        Staticts_Report.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();

    }

    private ArrayList<StaticReportModel> getData() {
        final ArrayList<StaticReportModel> imageItems = new ArrayList<>();
        for (int i = 0; i < gridvalues.size(); i++) {

            String nameofcar = gridvalues.get(i).get("nameofcar");
            String image = gridvalues.get(i).get("image");
            String individualcount = gridvalues.get(i).get("individualcount");

            imageItems.add(new StaticReportModel(nameofcar, image, individualcount));
        }
        return imageItems;
    }

    private class datas extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();

            String Saved_Car_url = Constant.MY_USER_TAB_VIEW + "session_user_id=" + user.get("user_id");

            String json = sh.makeServiceCall(Saved_Car_url, ServiceHandler.POST);

            if (json != null) {

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    JSONArray city = jsonObj.getJSONArray("dashboard");

                    gridvalues = new ArrayList<>();
                    for (int k = 0; k <= city.length(); k++) {

                        viewcarscount = city.getJSONObject(k).getString("viewcarscount");


                        JSONArray cardetails = new JSONArray(city.getJSONObject(k).getString("cardetails"));

                        for (int i = 0; i <= cardetails.length(); i++) {

                            String nameofcar = cardetails.getJSONObject(i).getString("nameofcar");
                            String image = cardetails.getJSONObject(i).getString("image");
                            String individualcount = cardetails.getJSONObject(i).getString("individualcount");

                            grid_values = new HashMap<>();

                            grid_values.put("nameofcar", nameofcar);
                            grid_values.put("image", image);
                            grid_values.put("individualcount", individualcount);

                            gridvalues.add(grid_values);

                            char_values.add(individualcount);

                        }


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


            Log.e("`", char_values.toString());

            /////Grid Datassss
            reportRecycleAdapter = new ReportRecycleAdapter(Staticts_Report.this, getData());

            grid_recycler.setHasFixedSize(true);
            grid_recycler.setLayoutManager(new LinearLayoutManager(Staticts_Report.this, LinearLayoutManager.HORIZONTAL, false));
            grid_recycler.setAdapter(reportRecycleAdapter);


            float[] floatValues = new float[char_values.size()];
            for (int i = 0; i < char_values.size(); i++) {
                floatValues[i] = Float.parseFloat(char_values.get(i).toString());
            }

            ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();

            for (int i = 0; i < char_values.size(); i++) {
                float val = Float.parseFloat(char_values.get(i));
                yvalues.add(new PieEntry(val, i));

            }

            ArrayList<Integer> colors = new ArrayList<Integer>();

            colors.add(Color.parseColor("#f7464a"));
            colors.add(Color.parseColor("#46bfbd"));
            colors.add(Color.parseColor("#fdb45c"));
            colors.add(Color.parseColor("#949fb1"));
            colors.add(Color.parseColor("#4d5360"));

            PieDataSet dataSet = new PieDataSet(yvalues, "");

            PieData data = new PieData(dataSet);

            data.setValueTextSize(11f);
            data.setValueTextColor(Color.WHITE);
            dataSet.setColors(colors);

            singlechart.setRotationEnabled(false);
            singlechart.setHighlightPerTapEnabled(true);
            singlechart.setDrawHoleEnabled(false);

            //singlechart.setDescription(Description);

            singlechart.setDrawHoleEnabled(true);
            singlechart.setTransparentCircleRadius(30f);
            singlechart.setHoleRadius(30f);

            singlechart.getLegend().setEnabled(false);

            singlechart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

            singlechart.setEntryLabelColor(Color.WHITE);
            singlechart.setEntryLabelTextSize(12f);

            Description des = singlechart.getDescription();
            des.setEnabled(false);

            singlechart.setData(data);
            singlechart.invalidate();

            Log.e("viewcarscount", viewcarscount);

            total_view_counts.setText("Total Cars Views" + " " + ":" + " " + viewcarscount);

        }
    }
}
