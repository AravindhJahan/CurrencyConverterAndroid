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
import com.falconnect.dealermanagementsystem.FontAdapter.RoundImageTransform;
import com.falconnect.dealermanagementsystem.Model.ReportFooterDataModel;
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
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.navdrawer.SimpleSideDrawer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Report_Inventory extends Activity {

    private static RecyclerView.Adapter adapter;
    private static RecyclerView inventory_sales_recyclerview;
    private static ArrayList<ReportFooterDataModel> inventorydata;
    BuyPageNavigation report_buynavigation;
    SessionManager sessionManager;
    HashMap<String, String> user;
    ImageView imageView_inventory;
    TextView profile_name_inventory;
    RelativeLayout call_layout, chat_layout, logout_layout;
    TextView profile_address_inventory;
    String saved_name, saved_address;
    ArrayList<String> piechart_value1 = new ArrayList<>();
    ArrayList<String> piechart_value2 = new ArrayList<>();
    ArrayList<String> piechart_value3 = new ArrayList<>();
    ArrayList<String> piechart_value4 = new ArrayList<>();
    ImageView inventory_nav;
    TextView parkandsell_value, selfown_value, sold_value;
    TextView with_images, with_documents, num_cars;
    PieChart piechart1;
    PieChart piechart2;
    PieChart piechart3;
    PieChart piechart4;
    String parkandsell, parkandsell_precent, self, self_percent, sold, sold_percent, inventorycount;
    String percent_photos, documentpercentage;
    TextView view_cars;
    PlandetailsSharedPreferences plandetailsSharedPreferences;
    HashMap<String, String> plan;
    private SimpleSideDrawer mNav_inventory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_report__inventory);

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        sessionManager = new SessionManager(Report_Inventory.this);
        user = sessionManager.getUserDetails();

        piechart1 = (PieChart) findViewById(R.id.chart1);
        piechart2 = (PieChart) findViewById(R.id.chart2);
        piechart3 = (PieChart) findViewById(R.id.chart3);
        piechart4 = (PieChart) findViewById(R.id.chart4);

        with_images = (TextView) findViewById(R.id.with_images);
        with_documents = (TextView) findViewById(R.id.with_documents);
        num_cars = (TextView) findViewById(R.id.num_cars);

        parkandsell_value = (TextView) findViewById(R.id.parkandsell_value);
        selfown_value = (TextView) findViewById(R.id.selfown_value);
        sold_value = (TextView) findViewById(R.id.sold_value);

        inventory_sales_recyclerview = (RecyclerView) findViewById(R.id.inventory_sales_recyclerview);
        inventory_sales_recyclerview.setHasFixedSize(true);
        inventory_sales_recyclerview.setLayoutManager(new LinearLayoutManager(Report_Inventory.this, LinearLayoutManager.HORIZONTAL, false));
        inventorydata = new ArrayList<ReportFooterDataModel>();
        for (int i = 0; i < MyFooterReportData.reportnameArray.length; i++) {
            inventorydata.add(new ReportFooterDataModel(
                    MyFooterReportData.reportnameArray[i],
                    MyFooterReportData.reportdrawableArrayWhite1[i],
                    MyFooterReportData.reportid_[i]
            ));
        }
        adapter = new ReportFooterCustomAdapter(Report_Inventory.this, inventorydata);
        inventory_sales_recyclerview.setAdapter(adapter);

        mNav_inventory = new SimpleSideDrawer(this);
        mNav_inventory.setLeftBehindContentView(R.layout.activity_behind_left_simple);

        imageView_inventory = (ImageView) mNav_inventory.findViewById(R.id.profile_avatar);
        profile_name_inventory = (TextView) mNav_inventory.findViewById(R.id.profile_name);
        profile_address_inventory = (TextView) mNav_inventory.findViewById(R.id.profile_address);
        saved_name = user.get("dealer_name");
        saved_address = user.get("dealershipname");
        profile_name_inventory.setText(saved_name);
        if (user.get("dealer_img").isEmpty()) {
            Glide.with(getApplicationContext())
                    .load(R.drawable.default_avatar)
                    .transform(new RoundImageTransform(Report_Inventory.this))
                    .into(imageView_inventory);
        } else {
            Glide.with(getApplicationContext())
                    .load(user.get("dealer_img"))
                    .transform(new RoundImageTransform(Report_Inventory.this))
                    .into(imageView_inventory);
        }
        profile_address_inventory.setText(saved_address);
        inventory_nav = (ImageView) findViewById(R.id.inventory_nav);
        inventory_nav.setOnClickListener(new View.OnClickListener() {
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

                AlertDialog alertbox = new AlertDialog.Builder(Report_Inventory.this)
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
                sessionManager.logoutUser();
                mNav_inventory.closeLeftSide();
                Report_Inventory.this.finish();
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
        report_buynavigation = new BuyPageNavigation();
        CustomList adapter = new CustomList(Report_Inventory.this, BuyPageNavigation.web, BuyPageNavigation.imageId);
        ListView list = (ListView) findViewById(R.id.nav_list_view);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (BuyPageNavigation.web[position] == "Dashboard") {
                    Intent intent = new Intent(Report_Inventory.this, MainDashBoardActivity.class);
                    startActivity(intent);
                    Report_Inventory.this.finish();
                    mNav_inventory.closeLeftSide();
                    //Toast.makeText(Report_Inventory.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Buy") {
                    Intent intent = new Intent(Report_Inventory.this, DashBoard.class);
                    startActivity(intent);
                    Report_Inventory.this.finish();
                    mNav_inventory.closeLeftSide();
                    //  Toast.makeText(Report_Inventory.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Sell") {
                    Intent intent = new Intent(Report_Inventory.this, SellDashBoardActivity.class);
                    startActivity(intent);
                    Report_Inventory.this.finish();
                    mNav_inventory.closeLeftSide();
                    // Toast.makeText(Report_Inventory.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Manage") {
                    Intent intent = new Intent(Report_Inventory.this, ManageDashBoardActivity.class);
                    startActivity(intent);
                    Report_Inventory.this.finish();
                    mNav_inventory.closeLeftSide();
                    // Toast.makeText(Report_Inventory.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Funding") {
                    plandetailsSharedPreferences = new PlandetailsSharedPreferences(Report_Inventory.this);
                    plan = plandetailsSharedPreferences.getUserDetails();

//                    if (plan.get("PlanName") == "BASIC" ) {
//                        Intent intent = new Intent(Report_Inventory.this, LoanActivity.class);
//                        startActivity(intent);
//                        mNav_inventory.closeLeftSide();
//                        Report_Inventory.this.finish();
//                    } else {
                        Intent intent = new Intent(Report_Inventory.this, FundingActivity.class);
                        startActivity(intent);
                        mNav_inventory.closeLeftSide();
                        Report_Inventory.this.finish();
//                    }
                    // Toast.makeText(Report_Inventory.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Networks") {
                    String url = "http://app.dealerplus.in/user_login_cometchat?session_user_id=" + user.get("user_id");
                    Intent intet = new Intent(Report_Inventory.this, NetworksWebviewActivity.class);
                    //intet.putExtra("title", BuyPageNavigation.web[position]);
                    intet.putExtra("url", url);
                    startActivity(intet);
                    mNav_inventory.closeLeftSide();
                    // Toast.makeText(Report_Inventory.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Reports") {
                    mNav_inventory.closeLeftSide();
                    // Toast.makeText(Report_Inventory.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "FAQs") {
                    faqs();
                    mNav_inventory.closeLeftSide();
                    // Toast.makeText(Report_Inventory.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(DashBoard.this, web[position], Toast.LENGTH_SHORT).show();
                }
            }
        });

        view_cars = (TextView) findViewById(R.id.view_cars);

        view_cars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Report_Inventory.this, SellDashBoardActivity.class);
                startActivity(intent);
                Report_Inventory.this.finish();
            }
        });


        new pie_chart1().execute();
        new pie_chart2().execute();
        new pie_chart3().execute();
        new pie_chart4().execute();


    }

    public void faqs() {
        FaqOptions faqOptions = new FaqOptions()
                .showFaqCategoriesAsGrid(true)
                .showContactUsOnAppBar(true)
                .showContactUsOnFaqScreens(false)
                .showContactUsOnFaqNotHelpful(false);

        Hotline.showFAQs(Report_Inventory.this, faqOptions);
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
                        Report_Inventory.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();

    }

    private class pie_chart1 extends AsyncTask<Void, Void, Void> {
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

                    for (int k = 0; k <= city.length(); k++) {

                        percent_photos = city.getJSONObject(k).getString("photospercentage");

                        JSONArray photosgraph = new JSONArray(city.getJSONObject(k).getString("photosgraph"));

                        for (int i = 0; i <= photosgraph.length(); i++) {

                            piechart_value1.add(photosgraph.get(i).toString());
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

            Log.e("linemonthvalues", piechart_value1.toString());

            piechart_value1.removeAll(Collections.singleton("0"));

            with_images.setText(percent_photos + "% with Images");

            ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();

            for (int i = 0; i < piechart_value1.size(); i++) {
                float val = Float.parseFloat(piechart_value1.get(i));

                yvalues.add(new PieEntry(val, i));

            }

            ArrayList<String> labels = new ArrayList<>();
            labels.add("Without Images");
            labels.add("All Images");
            labels.add("Less than 5  Images");

            ArrayList<Integer> colors = new ArrayList<Integer>();
            colors.add(Color.parseColor("#f7464a"));
            colors.add(Color.parseColor("#fdb45c"));
            colors.add(Color.parseColor("#46bfbd"));
            colors.add(Color.parseColor("#949fb1"));
            colors.add(Color.parseColor("#4d5360"));


            PieDataSet dataSet = new PieDataSet(yvalues, "");

            PieData data = new PieData(dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(11f);
            data.setValueTextColor(Color.WHITE);
            dataSet.setColors(colors);

            piechart1.getLegend().setEnabled(false);
            piechart1.setDrawHoleEnabled(false);
            piechart1.setRotationEnabled(false);
            piechart1.setUsePercentValues(true);
            piechart1.setHighlightPerTapEnabled(true);
            piechart1.animateY(1400, Easing.EasingOption.EaseInOutQuad);
            piechart1.setEntryLabelColor(Color.WHITE);
            piechart1.setEntryLabelTextSize(12f);
            Description des = piechart1.getDescription();
            des.setEnabled(false);
            piechart1.setData(data);
            piechart1.invalidate();

        }
    }

    private class pie_chart2 extends AsyncTask<Void, Void, Void> {
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

                    for (int k = 0; k <= city.length(); k++) {

                        documentpercentage = city.getJSONObject(k).getString("documentpercentage");

                        JSONArray documentsgraph = new JSONArray(city.getJSONObject(k).getString("documentsgraph"));

                        for (int i = 0; i <= documentsgraph.length(); i++) {

                            piechart_value2.add(documentsgraph.get(i).toString());
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

            Log.e("linemonthvalues", piechart_value2.toString());

            piechart_value2.removeAll(Collections.singleton("0"));

            with_documents.setText(documentpercentage + "% With Documents");

            ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();

            for (int i = 0; i < piechart_value2.size(); i++) {
                float val = Float.parseFloat(piechart_value2.get(i));
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
            data.setValueFormatter(new PercentFormatter());
            piechart2.getLegend().setEnabled(false);
            piechart2.setDrawHoleEnabled(false);
            piechart2.setRotationEnabled(false);
            piechart2.setUsePercentValues(true);
            piechart2.setHighlightPerTapEnabled(true);
            piechart2.animateY(1400, Easing.EasingOption.EaseInOutQuad);
            piechart2.setEntryLabelColor(Color.WHITE);
            piechart2.setEntryLabelTextSize(12f);
            Description des = piechart2.getDescription();
            des.setEnabled(false);
            piechart2.setData(data);
            piechart2.invalidate();
        }
    }

    private class pie_chart3 extends AsyncTask<Void, Void, Void> {
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

                    for (int k = 0; k <= city.length(); k++) {

                        JSONArray pricinggraph = new JSONArray(city.getJSONObject(k).getString("pricinggraph"));

                        for (int i = 0; i <= pricinggraph.length(); i++) {

                            piechart_value3.add(pricinggraph.get(i).toString());
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

            Log.e("linemonthvalues", piechart_value3.toString());

            piechart_value3.removeAll(Collections.singleton("0"));


            ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();

            for (int i = 0; i < piechart_value3.size(); i++) {
                float val = Float.parseFloat(piechart_value3.get(i));
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
            data.setValueFormatter(new PercentFormatter());
            dataSet.setColors(colors);

            piechart3.getLegend().setEnabled(false);
            piechart3.setDrawHoleEnabled(false);
            piechart3.setRotationEnabled(false);
            piechart3.setUsePercentValues(true);
            piechart3.setHighlightPerTapEnabled(true);
            piechart3.animateY(1400, Easing.EasingOption.EaseInOutQuad);
            piechart3.setEntryLabelColor(Color.WHITE);
            piechart3.setEntryLabelTextSize(12f);
            piechart3.setData(data);

            Description des = piechart3.getDescription();
            des.setEnabled(false);

            piechart3.invalidate();

        }
    }

    private class pie_chart4 extends AsyncTask<Void, Void, Void> {
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

                    for (int k = 0; k <= city.length(); k++) {

                        inventorycount = city.getJSONObject(k).getString("inventorycount");
                        parkandsell = city.getJSONObject(k).getString("parksellcount");
                        parkandsell_precent = city.getJSONObject(k).getString("parksellpercentage");
                        self = city.getJSONObject(k).getString("owncount");
                        self_percent = city.getJSONObject(k).getString("ownpercentage");
                        sold = city.getJSONObject(k).getString("soldcount");
                        sold_percent = city.getJSONObject(k).getString("soldpercentage");


                        JSONArray inventorygraph = new JSONArray(city.getJSONObject(k).getString("inventorygraph"));

                        for (int i = 0; i <= inventorygraph.length(); i++) {

                            piechart_value4.add(inventorygraph.get(i).toString());
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

            Log.e("linemonthvalues", piechart_value4.toString());

            piechart_value4.removeAll(Collections.singleton("0"));

            parkandsell_value.setText(parkandsell + " (" + parkandsell_precent + ")%");
            selfown_value.setText(self + " (" + self_percent + ")%");
            sold_value.setText(sold + " (" + sold_percent + ")%");
            num_cars.setText(inventorycount + " Cars");
            ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();

            for (int i = 0; i < piechart_value4.size(); i++) {
                float val = Float.parseFloat(piechart_value4.get(i));
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
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextColor(Color.WHITE);
            dataSet.setColors(colors);

            piechart4.getLegend().setEnabled(false);
            piechart4.setDrawHoleEnabled(false);
            piechart4.setRotationEnabled(false);
            piechart4.setUsePercentValues(true);
            piechart4.setHighlightPerTapEnabled(true);
            piechart4.animateY(1400, Easing.EasingOption.EaseInOutQuad);
            piechart4.setEntryLabelColor(Color.WHITE);
            piechart4.setEntryLabelTextSize(12f);

            Description des = piechart4.getDescription();
            des.setEnabled(false);

            piechart4.setData(data);
            piechart4.invalidate();

        }
    }

}
