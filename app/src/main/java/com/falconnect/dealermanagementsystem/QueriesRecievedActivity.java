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
import com.falconnect.dealermanagementsystem.Adapter.QueriesReceivedListAdapter;
import com.falconnect.dealermanagementsystem.Adapter.SellFooterCustomAdapter;
import com.falconnect.dealermanagementsystem.FontAdapter.RoundImageTransform;
import com.falconnect.dealermanagementsystem.Model.QueriesReceivedListModel;
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

public class QueriesRecievedActivity extends AppCompatActivity {

    private static RecyclerView sellqueries_footer;
    private static ArrayList<SellFooterDataModel> sellfooterdata;
    private static RecyclerView.Adapter selladapter;
    public ArrayList<HashMap<String, String>> qrecieved_list;
    HashMap<String, String> user;
    HashMap<String, String> qrecievedlist;
    ImageView query_recieve_mNav;
    SessionManager session_query_receive;
    ImageView imageView_query_receive;
    TextView profile_name_query_receive;
    TextView profile_address_query_receive;
    String saved_name_query_receive, saved_address_query_receive;
    BuyPageNavigation query_receive_buypagenavigation;
    ListView qresived_listview;
    QueriesReceivedListAdapter queriesReceivedListAdapter;
    ProgressDialog barProgressDialog;
    PlandetailsSharedPreferences plandetailsSharedPreferences;
    HashMap<String, String> plan;
    private boolean mVisible;
    private SimpleSideDrawer mNav_query_receive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_queries_recieved);
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

        sellqueries_footer = (RecyclerView) findViewById(R.id.my_recycler_queries_recieved);
        sellqueries_footer.setHasFixedSize(true);
        sellqueries_footer.setLayoutManager(new LinearLayoutManager(QueriesRecievedActivity.this, LinearLayoutManager.HORIZONTAL, false));
        sellfooterdata = new ArrayList<SellFooterDataModel>();
        for (int i = 0; i < MyFooterSellData.sellnameArray.length; i++) {

            sellfooterdata.add(new SellFooterDataModel(
                    MyFooterSellData.sellnameArray[i],
                    MyFooterSellData.selldrawableArrayWhite2[i],
                    MyFooterSellData.sellid_[i]
            ));
        }
        selladapter = new SellFooterCustomAdapter(QueriesRecievedActivity.this, sellfooterdata);
        sellqueries_footer.setAdapter(selladapter);

        mNav_query_receive = new SimpleSideDrawer(this);
        mNav_query_receive.setLeftBehindContentView(R.layout.activity_behind_left_simple);

        barProgressDialog = ProgressDialog.show(QueriesRecievedActivity.this, "Loading...", "Please Wait ...", true);

        RelativeLayout call_layout = (RelativeLayout) mNav_query_receive.findViewById(R.id.call_layout);
        RelativeLayout chat_layout = (RelativeLayout) mNav_query_receive.findViewById(R.id.chat_layout);
        RelativeLayout logout_layout = (RelativeLayout) mNav_query_receive.findViewById(R.id.logout_layout);

        call_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertbox = new AlertDialog.Builder(QueriesRecievedActivity.this)
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
                session_query_receive.logoutUser();
                mNav_query_receive.closeLeftSide();
                QueriesRecievedActivity.this.finish();
            }
        });

        chat_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chat();
                mNav_query_receive.closeLeftSide();
            }
        });

        imageView_query_receive = (ImageView) mNav_query_receive.findViewById(R.id.profile_avatar);
        profile_name_query_receive = (TextView) mNav_query_receive.findViewById(R.id.profile_name);
        profile_address_query_receive = (TextView) mNav_query_receive.findViewById(R.id.profile_address);

        session_query_receive = new SessionManager(QueriesRecievedActivity.this);
        user = session_query_receive.getUserDetails();
        saved_name_query_receive = user.get("dealer_name");
        saved_address_query_receive = user.get("dealershipname");
        profile_name_query_receive.setText(saved_name_query_receive);
        if (user.get("dealer_img").isEmpty()) {
            Glide.with(getApplicationContext())
                    .load(R.drawable.default_avatar)
                    .transform(new RoundImageTransform(QueriesRecievedActivity.this))
                    .into(imageView_query_receive);
        } else {
            Glide.with(getApplicationContext())
                    .load(user.get("dealer_img"))
                    .transform(new RoundImageTransform(QueriesRecievedActivity.this))
                    .into(imageView_query_receive);
        }
        profile_address_query_receive.setText(saved_address_query_receive);
        query_recieve_mNav = (ImageView) findViewById(R.id.query_recived_mnav);
        query_recieve_mNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNav_query_receive.toggleLeftDrawer();
            }
        });

        //NAVIGATION DRAWER LIST VIEW
        query_receive_buypagenavigation = new BuyPageNavigation();
        CustomList adapter = new CustomList(QueriesRecievedActivity.this, BuyPageNavigation.web, BuyPageNavigation.imageId);
        ListView list = (ListView) findViewById(R.id.nav_list_view);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (BuyPageNavigation.web[position] == "Dashboard") {
                    Intent intent = new Intent(QueriesRecievedActivity.this, MainDashBoardActivity.class);
                    startActivity(intent);
                    mNav_query_receive.closeLeftSide();
                    QueriesRecievedActivity.this.finish();
                    // Toast.makeText(QueriesRecievedActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Buy") {
                    Intent intent = new Intent(QueriesRecievedActivity.this, DashBoard.class);
                    startActivity(intent);
                    mNav_query_receive.closeLeftSide();
                    QueriesRecievedActivity.this.finish();
                    // Toast.makeText(QueriesRecievedActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Sell") {
                    mNav_query_receive.closeLeftSide();
                    // Toast.makeText(QueriesRecievedActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Manage") {
                    Intent intent = new Intent(QueriesRecievedActivity.this, ManageDashBoardActivity.class);
                    startActivity(intent);
                    QueriesRecievedActivity.this.finish();
                    mNav_query_receive.closeLeftSide();
                    //  Toast.makeText(QueriesRecievedActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Funding") {
                    plandetailsSharedPreferences = new PlandetailsSharedPreferences(QueriesRecievedActivity.this);
                    plan = plandetailsSharedPreferences.getUserDetails();

//                    if ( plan.get("PlanName").equals("BASIC")) {
//
//                        Intent intent = new Intent(QueriesRecievedActivity.this, LoanActivity.class);
//                        startActivity(intent);
//                        mNav_query_receive.closeLeftSide();
//                        QueriesRecievedActivity.this.finish();
//                    } else {
                        Intent intent = new Intent(QueriesRecievedActivity.this, FundingActivity.class);
                        startActivity(intent);
                        mNav_query_receive.closeLeftSide();
                        QueriesRecievedActivity.this.finish();
//                    }
                    // Toast.makeText(QueriesRecievedActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Networks") {
                    String url = "http://app.dealerplus.in/user_login_cometchat?session_user_id=" + user.get("user_id");
                    Intent intet = new Intent(QueriesRecievedActivity.this, NetworksWebviewActivity.class);
                    //intet.putExtra("title", BuyPageNavigation.web[position]);
                    intet.putExtra("url", url);
                    startActivity(intet);
                    mNav_query_receive.closeLeftSide();
                    // Toast.makeText(QueriesRecievedActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Reports") {
                    plandetailsSharedPreferences = new PlandetailsSharedPreferences(QueriesRecievedActivity.this);
                    plan = plandetailsSharedPreferences.getUserDetails();
                    if (plan.get("PlanName").equals("BASIC")) {
                        Intent intent = new Intent(QueriesRecievedActivity.this, BuyPlanActivity.class);
                        startActivity(intent);
                        //MainDashBoardActivity.this.finish();
                        mNav_query_receive.closeLeftSide();
                    } else {
                        Intent intent = new Intent(QueriesRecievedActivity.this, ReportSalesActivity.class);
                        startActivity(intent);
                        QueriesRecievedActivity.this.finish();
                        mNav_query_receive.closeLeftSide();
                    }

                } else if (BuyPageNavigation.web[position] == "FAQs") {
                    faqs();
                    mNav_query_receive.closeLeftSide();
                } else {
                    //Toast.makeText(DashBoard.this, web[position], Toast.LENGTH_SHORT).show();
                }
            }
        });

        new queries_recieved().execute();
    }

    public void faqs() {
        FaqOptions faqOptions = new FaqOptions()
                .showFaqCategoriesAsGrid(true)
                .showContactUsOnAppBar(true)
                .showContactUsOnFaqScreens(false)
                .showContactUsOnFaqNotHelpful(false);

        Hotline.showFAQs(QueriesRecievedActivity.this, faqOptions);
    }

    public void chat() {
        Hotline.showConversations(getApplicationContext());
        // Hotline.clearUserData(getApplicationContext());
    }

    @Override
    public void onRestart() {
        super.onRestart();
        startActivity(getIntent());
        finish();
    }

    @Override
    public void onBackPressed() {

        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to exit application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        QueriesRecievedActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();

    }


    private ArrayList<QueriesReceivedListModel> getqdata() {
        final ArrayList<QueriesReceivedListModel> qdata = new ArrayList<>();
        for (int i = 0; i < qrecieved_list.size(); i++) {
            String noimages = qrecieved_list.get(i).get("noimages");
            String imagelink = qrecieved_list.get(i).get("imagelink");
            String listing_type = qrecieved_list.get(i).get("listing_type");
            String price = qrecieved_list.get(i).get("price");
            String car_id = qrecieved_list.get(i).get("car_id");
            String status = qrecieved_list.get(i).get("status");
            String from_dealer_id = qrecieved_list.get(i).get("from_dealer_id");
            String to_dealer_name = qrecieved_list.get(i).get("to_dealer_name");
            String to_dealer_id = qrecieved_list.get(i).get("to_dealer_id");
            String make = qrecieved_list.get(i).get("make");
            String title = qrecieved_list.get(i).get("title");
            String dealer_name = qrecieved_list.get(i).get("dealer_name");
            String dealer_email = qrecieved_list.get(i).get("dealer_email");
            String message = qrecieved_list.get(i).get("message");
            String contact_transactioncode = qrecieved_list.get(i).get("contact_transactioncode");
            String days = qrecieved_list.get(i).get("days");

            qdata.add(new QueriesReceivedListModel(noimages, imagelink, listing_type, price, car_id, status, from_dealer_id, to_dealer_name, to_dealer_id, make, title, dealer_name, dealer_email, message, contact_transactioncode, days));
        }
        return qdata;
    }

    private class queries_recieved extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();

            String mysellqueries_url = Constant.QUERIES_RECEIVED
                    + "session_user_id=" + user.get("user_id");

            String json = sh.makeServiceCall(mysellqueries_url, ServiceHandler.POST );

            if (json != null) {

                qrecieved_list = new ArrayList<>();

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    JSONArray mysellqueries = jsonObj.getJSONArray("mysellqueries");

                    for (int k = 0; k <= mysellqueries.length(); k++) {

                        String noimages = mysellqueries.getJSONObject(k).getString("noimages");
                        String imagelink = mysellqueries.getJSONObject(k).getString("imagelink");
                        String listing_type = mysellqueries.getJSONObject(k).getString("listing_type");
                        String price = mysellqueries.getJSONObject(k).getString("price");
                        String car_id = mysellqueries.getJSONObject(k).getString("car_id");
                        String status = mysellqueries.getJSONObject(k).getString("status");
                        String from_dealer_id = mysellqueries.getJSONObject(k).getString("from_dealer_id");
                        String to_dealer_name = mysellqueries.getJSONObject(k).getString("to_dealer_name");
                        String to_dealer_id = mysellqueries.getJSONObject(k).getString("to_dealer_id");
                        String make = mysellqueries.getJSONObject(k).getString("make");
                        String title = mysellqueries.getJSONObject(k).getString("title");
                        String dealer_name = mysellqueries.getJSONObject(k).getString("dealer_name");
                        String dealer_email = mysellqueries.getJSONObject(k).getString("dealer_email");
                        String message = mysellqueries.getJSONObject(k).getString("message");
                        String contact_transactioncode = mysellqueries.getJSONObject(k).getString("contact_transactioncode");
                        String days = mysellqueries.getJSONObject(k).getString("days");

                        qrecievedlist = new HashMap<>();


                        qrecievedlist.put("noimages", noimages);
                        qrecievedlist.put("imagelink", imagelink);
                        qrecievedlist.put("listing_type", listing_type);
                        qrecievedlist.put("price", price);
                        qrecievedlist.put("car_id", car_id);
                        qrecievedlist.put("status", status);
                        qrecievedlist.put("from_dealer_id", from_dealer_id);
                        qrecievedlist.put("to_dealer_name", to_dealer_name);
                        qrecievedlist.put("to_dealer_id", to_dealer_id);
                        qrecievedlist.put("make", make);
                        qrecievedlist.put("title", title);
                        qrecievedlist.put("dealer_name", dealer_name);
                        qrecievedlist.put("dealer_email", dealer_email);
                        qrecievedlist.put("message", message);
                        qrecievedlist.put("contact_transactioncode", contact_transactioncode);
                        qrecievedlist.put("days", days);

                        qrecieved_list.add(qrecievedlist);

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
            barProgressDialog.dismiss();
            qresived_listview = (ListView) findViewById(R.id.qresived_listview);

            queriesReceivedListAdapter = new QueriesReceivedListAdapter(QueriesRecievedActivity.this, getqdata());
            //qresived_listview.setAdapter(queriesReceivedListAdapter);

            RelativeLayout result_found = (RelativeLayout) findViewById(R.id.result_found);
            if (queriesReceivedListAdapter.getCount() != 0) {
                qresived_listview.setAdapter(queriesReceivedListAdapter);
            } else {
                qresived_listview.setVisibility(View.GONE);
                result_found.setVisibility(View.VISIBLE);
            }

            /*qresived_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    QueriesReceivedListModel queryListModel = (QueriesReceivedListModel) parent.getItemAtPosition(position);
                    String title = "Queries Detail";
                    String url = ConstantIP.IP + "mobileweb/mobilechat/#/sell/" + queryListModel.getContact_transactioncode() + "/" + user.get("user_id");
                    Intent intent = new Intent(QueriesRecievedActivity.this, SellDashBoardActivity.class);
                    intent.putExtra("title", title);
                    intent.putExtra("url", url);
                    startActivity(intent);
                }
            });*/

        }

    }

}
