package com.falconnect.dealermanagementsystem;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.falconnect.dealermanagementsystem.Adapter.CustomList;
import com.falconnect.dealermanagementsystem.Adapter.ReportFooterCustomAdapter;
import com.falconnect.dealermanagementsystem.FontAdapter.MyMarkerView;
import com.falconnect.dealermanagementsystem.FontAdapter.RoundImageTransform;
import com.falconnect.dealermanagementsystem.Model.ReportFooterDataModel;
import com.falconnect.dealermanagementsystem.NavigationDrawer.BuyPageNavigation;
import com.falconnect.dealermanagementsystem.SharedPreference.PlandetailsSharedPreferences;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;
import com.freshdesk.hotline.FaqOptions;
import com.freshdesk.hotline.Hotline;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.navdrawer.SimpleSideDrawer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class ReportSalesActivity extends Activity {

    private static RecyclerView.Adapter adapter;
    private static RecyclerView inventoryrecycleview;
    private static ArrayList<ReportFooterDataModel> inventorydata;
    ImageView inventory_nav;
    SessionManager session;
    ImageView imageView_inventory;
    String max_value, min_value, total_array_value;
    HashMap<String, String> user;
    ProgressDialog barProgressDialog;
    TextView profile_name_inventory;
    TextView profile_address_inventory;
    String saved_name_user, saved_address_user;
    BuyPageNavigation inventory_buypagenavigation;
    RelativeLayout call_layout, chat_layout, logout_layout;
    int value = 1;
    LineChart chart;
    TextView price;
    LineDataSet dataset;
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> linevalues = new ArrayList<>();
    ArrayList<String> linemonthvalues = new ArrayList<>();
    PlandetailsSharedPreferences plandetailsSharedPreferences;
    HashMap<String, String> plan;
    private SimpleSideDrawer mNav_inventory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_report_sales);

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        inventory_nav = (ImageView) findViewById(R.id.inventory_mnav);
        inventoryrecycleview = (RecyclerView) findViewById(R.id.inventory_recyclerview);
        inventoryrecycleview.setHasFixedSize(true);
        inventoryrecycleview.setLayoutManager(new LinearLayoutManager(ReportSalesActivity.this, LinearLayoutManager.HORIZONTAL, false));
        inventorydata = new ArrayList<ReportFooterDataModel>();
        for (int i = 0; i < MyFooterReportData.reportnameArray.length; i++) {
            inventorydata.add(new ReportFooterDataModel(
                    MyFooterReportData.reportnameArray[i],
                    MyFooterReportData.reportdrawableArrayWhite0[i],
                    MyFooterReportData.reportid_[i]
            ));
        }
        adapter = new ReportFooterCustomAdapter(ReportSalesActivity.this, inventorydata);
        inventoryrecycleview.setAdapter(adapter);

        session = new SessionManager(ReportSalesActivity.this);
        user = session.getUserDetails();

        mNav_inventory = new SimpleSideDrawer(this);
        mNav_inventory.setLeftBehindContentView(R.layout.activity_behind_left_simple);

        imageView_inventory = (ImageView) mNav_inventory.findViewById(R.id.profile_avatar);
        profile_name_inventory = (TextView) mNav_inventory.findViewById(R.id.profile_name);
        profile_address_inventory = (TextView) mNav_inventory.findViewById(R.id.profile_address);
        user = session.getUserDetails();
        saved_name_user = user.get("dealer_name");
        saved_address_user = user.get("dealershipname");
        profile_name_inventory.setText(saved_name_user);
        if (user.get("dealer_img").isEmpty()) {
            Glide.with(getApplicationContext())
                    .load(R.drawable.default_avatar)
                    .transform(new RoundImageTransform(ReportSalesActivity.this))
                    .into(imageView_inventory);
        } else {
            Glide.with(getApplicationContext())
                    .load(user.get("dealer_img"))
                    .transform(new RoundImageTransform(ReportSalesActivity.this))
                    .into(imageView_inventory);
        }
        profile_address_inventory.setText(saved_address_user);

        inventory_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNav_inventory.toggleLeftDrawer();
            }
        });

        price = (TextView) findViewById(R.id.price_sales);


        call_layout = (RelativeLayout) mNav_inventory.findViewById(R.id.call_layout);
        chat_layout = (RelativeLayout) mNav_inventory.findViewById(R.id.chat_layout);
        logout_layout = (RelativeLayout) mNav_inventory.findViewById(R.id.logout_layout);

        call_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertbox = new AlertDialog.Builder(ReportSalesActivity.this)
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
                mNav_inventory.closeLeftSide();
                ReportSalesActivity.this.finish();
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
        inventory_buypagenavigation = new BuyPageNavigation();
        CustomList adapter = new CustomList(ReportSalesActivity.this, BuyPageNavigation.web, BuyPageNavigation.imageId);
        ListView list = (ListView) findViewById(R.id.nav_list_view);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (BuyPageNavigation.web[position] == "Dashboard") {
                    Intent intent = new Intent(ReportSalesActivity.this, MainDashBoardActivity.class);
                    startActivity(intent);
                    ReportSalesActivity.this.finish();
                    mNav_inventory.closeLeftSide();
                    //  Toast.makeText(ReportSalesActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Buy") {
                    Intent intent = new Intent(ReportSalesActivity.this, DashBoard.class);
                    startActivity(intent);
                    ReportSalesActivity.this.finish();
                    mNav_inventory.closeLeftSide();
                    //  Toast.makeText(ReportSalesActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Sell") {
                    Intent intent = new Intent(ReportSalesActivity.this, SellDashBoardActivity.class);
                    startActivity(intent);
                    ReportSalesActivity.this.finish();
                    mNav_inventory.closeLeftSide();
                    // Toast.makeText(ReportSalesActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Manage") {
                    Intent intent = new Intent(ReportSalesActivity.this, ManageDashBoardActivity.class);
                    startActivity(intent);
                    ReportSalesActivity.this.finish();
                    mNav_inventory.closeLeftSide();
                    // Toast.makeText(ReportSalesActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Funding") {
                    plandetailsSharedPreferences = new PlandetailsSharedPreferences(ReportSalesActivity.this);
                    plan = plandetailsSharedPreferences.getUserDetails();
//
//                    if (plan.get("PlanName") == "BASIC" ) {
//                        Intent intent = new Intent(ReportSalesActivity.this, LoanActivity.class);
//                        startActivity(intent);
//                        mNav_inventory.closeLeftSide();
//                        ReportSalesActivity.this.finish();
//                    } else {
                        Intent intent = new Intent(ReportSalesActivity.this, FundingActivity.class);
                        startActivity(intent);
                        mNav_inventory.closeLeftSide();
                        ReportSalesActivity.this.finish();
//                    }
                    // Toast.makeText(ReportSalesActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Networks") {
                    String url = "http://app.dealerplus.in/user_login_cometchat?session_user_id=" + user.get("user_id");
                    Intent intet = new Intent(ReportSalesActivity.this, NetworksWebviewActivity.class);
                    //intet.putExtra("title", BuyPageNavigation.web[position]);
                    intet.putExtra("url", url);
                    startActivity(intet);
                    mNav_inventory.closeLeftSide();
                    //Toast.makeText(ReportSalesActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Reports") {
                    mNav_inventory.closeLeftSide();
                    Toast.makeText(ReportSalesActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "FAQs") {
                    faqs();
                    mNav_inventory.closeLeftSide();
                    // Toast.makeText(ReportSalesActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(DashBoard.this, web[position], Toast.LENGTH_SHORT).show();
                }
            }
        });

        chart = (LineChart) findViewById(R.id.smoothChartES);

        //chartES = (SmoothLineChartEquallySpaced) findViewById(R.id.smoothChartES);

        new value_get_data().execute();

    }

    public void faqs() {
        FaqOptions faqOptions = new FaqOptions()
                .showFaqCategoriesAsGrid(true)
                .showContactUsOnAppBar(true)
                .showContactUsOnFaqScreens(false)
                .showContactUsOnFaqNotHelpful(false);

        Hotline.showFAQs(ReportSalesActivity.this, faqOptions);
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
                        ReportSalesActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();

    }

     private class value_get_data extends AsyncTask<Void, Void, Void> {
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

                        max_value = city.getJSONObject(k).getString("maxsoldamount");
                        min_value = city.getJSONObject(k).getString("minsoldamount");

                        JSONArray drawgraphsoldamount = new JSONArray(city.getJSONObject(k).getString("drawgraphsoldamount"));

                        for (int i = 0; i <= drawgraphsoldamount.length(); i++) {

                            linevalues.add(drawgraphsoldamount.get(i).toString());

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

            new sixmonth().execute();

        }
    }

    private class sixmonth extends AsyncTask<Void, Void, Void> {
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

                        JSONArray showlastsixmonth = new JSONArray(city.getJSONObject(k).getString("showlastsixmonth"));

                        for (int m = 0; m <= showlastsixmonth.length(); m++) {

                            linemonthvalues.add(showlastsixmonth.get(m).toString());

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

            float[] floatValues = new float[linevalues.size()];

            for (int i = 0; i < linevalues.size(); i++) {
                floatValues[i] = Float.parseFloat(linevalues.get(i).toString());
            }

           /* String[] args = new String[linemonthvalues.size()];
            for (int b = 0; b < linemonthvalues.size(); b++) {
                args[b] = String.valueOf(linemonthvalues.get(b).toString());
            }
            Log.e("args", args.toString());*/

            MyMarkerView mv = new MyMarkerView(ReportSalesActivity.this, R.layout.custom_marker_view);
            mv.setChartView(chart);
            chart.setMarker(mv);


            ArrayList<Entry> values = new ArrayList<Entry>();
            for (int i = 0; i < linevalues.size(); i++) {
                float val = floatValues[i];
                values.add(new Entry(i, val));
            }


            if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
                dataset = (LineDataSet) chart.getData().getDataSetByIndex(0);
                dataset.setValues(values);
                chart.getData().notifyDataChanged();
                chart.notifyDataSetChanged();
            } else {
                // create a dataset and give it a type
                dataset = new LineDataSet(values, "");
                dataset.setColor(Color.parseColor("#173E84"));
                dataset.setCircleColor(Color.parseColor("#173E84"));
                dataset.setCircleRadius(3f);
                dataset.setDrawFilled(false);
                dataset.setLineWidth(4f);
                dataset.setDrawCircles(true);
            }

            LineData data = new LineData(dataset);
            chart.setData(data);

            chart.disableScroll();
            chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            chart.getAxis(YAxis.AxisDependency.RIGHT).setEnabled(false);

            chart.setScrollContainer(false);
            chart.setDrawGridBackground(false);
            chart.getAxisLeft().setDrawGridLines(false);
            chart.getXAxis().setDrawGridLines(false);

            Description des = chart.getDescription();
            des.setEnabled(false);

            chart.getLegend().setEnabled(false);
            chart.invalidate();

            //Month X Axis
            final HashMap<Integer, String> numMap = new HashMap<>();

            for (int f = 0; f < linemonthvalues.size(); f++) {
                numMap.put(f, linemonthvalues.get(f).toString());
            }

            Log.e("lineMonthValuesss", numMap.toString());

            XAxis xAxis = chart.getXAxis();
            int size = linemonthvalues.size();
            Log.e("size", String.valueOf(size));
            xAxis.setLabelCount(size, true);
            xAxis.setTextColor(Color.parseColor("#173E84"));
            xAxis.setTextSize(7f);

            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return numMap.get((int) value);
                }
            });

            Float maxvalue_floet = Float.valueOf(max_value);

            YAxis yAxis = chart.getAxisLeft();
            yAxis.setAxisMaximum(maxvalue_floet);
            yAxis.setAxisMinValue(0);
            yAxis.setTextColor(Color.parseColor("#173E84"));

            chart.animateX(2500, Easing.EasingOption.EaseInOutSine);

            int total = 0;
            for (int i = 0; i < linevalues.size(); i++) {
                total = total + Integer.parseInt(linevalues.get(i));
                Log.i("total amount =", String.valueOf(total));
            }

            total_array_value = String.valueOf(total);

            price.setText("Rs. " + total_array_value);
            Log.e("MaxVALUES", total_array_value);

        }
    }
}
