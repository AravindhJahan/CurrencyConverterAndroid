package com.falconnect.dealermanagementsystem;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.falconnect.dealermanagementsystem.Adapter.CustomList;
import com.falconnect.dealermanagementsystem.Adapter.FundingFooterCustomAdapter;
import com.falconnect.dealermanagementsystem.FontAdapter.RoundImageTransform;
import com.falconnect.dealermanagementsystem.Model.FundingDataModel;
import com.falconnect.dealermanagementsystem.NavigationDrawer.BuyPageNavigation;
import com.falconnect.dealermanagementsystem.SharedPreference.PlandetailsSharedPreferences;
import com.falconnect.dealermanagementsystem.SharedPreference.ProfileManagerSession;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;
import com.freshdesk.hotline.FaqOptions;
import com.freshdesk.hotline.Hotline;
import com.navdrawer.SimpleSideDrawer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class EMICalculatorActivity extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private static RecyclerView my_emi_funding;
    private static ArrayList<FundingDataModel> data;
    BuyPageNavigation emi_buypagenavigation;
    SessionManager session_emi;
    ImageView imageView_emi;
    TextView profile_name_emi;
    TextView profile_address_emi;
    String saved_name_emi, saved_address_emi;
    SessionManager session;
    HashMap<String, String> user;
    EditText loan_amount;
    Button month, year;
    Button plus_button, minus_button;
    TextView increment_button, intrest_percent;
    int value = 0;
    ProfileManagerSession profileManagerSession;
    Button nav_tick;
    int value_smple = 0;
    TextView no_of_month, no_of_month_yaers;
    TextView increment_month, increment_button_month_year;
    Button plus_button_month_year, minus_button_month_year;
    TextView total_payment_amount, monthly_payment_amount;
    double loanAmount, interestRate, loanPeriod, r, r1, monthlyPayment, totalPayment;
    CardView cardddds;
    PlandetailsSharedPreferences plandetailsSharedPreferences;
    HashMap<String, String> plan;
    private boolean mVisible;
    private SimpleSideDrawer mNav_emi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_emi_calculate);

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

        my_emi_funding = (RecyclerView) findViewById(R.id.my_emi_funding);
        my_emi_funding.setHasFixedSize(true);
        my_emi_funding.setLayoutManager(new LinearLayoutManager(EMICalculatorActivity.this, LinearLayoutManager.HORIZONTAL, false));

        data = new ArrayList<FundingDataModel>();
        for (int i = 0; i < MyFundingFooterData.nameArrayFunding.length; i++) {
            data.add(new FundingDataModel(
                    MyFundingFooterData.nameArrayFunding[i],
                    MyFundingFooterData.id_[i],
                    MyFundingFooterData.drawableArrayWhite2[i]
            ));
        }

        adapter = new FundingFooterCustomAdapter(EMICalculatorActivity.this, data);
        my_emi_funding.setAdapter(adapter);

        ImageView nav_funding = (ImageView) findViewById(R.id.nav_emi);
        mNav_emi = new SimpleSideDrawer(this);
        mNav_emi.setLeftBehindContentView(R.layout.activity_behind_left_simple);

        imageView_emi = (ImageView) mNav_emi.findViewById(R.id.profile_avatar);
        profile_name_emi = (TextView) mNav_emi.findViewById(R.id.profile_name);
        profile_address_emi = (TextView) mNav_emi.findViewById(R.id.profile_address);
        nav_tick = (Button) findViewById(R.id.nav_tick);
        session_emi = new SessionManager(EMICalculatorActivity.this);
        user = session_emi.getUserDetails();
        saved_name_emi = user.get("dealer_name");
        saved_address_emi = user.get("dealershipname");
        profile_name_emi.setText(saved_name_emi);

        if (user.get("dealer_img").isEmpty()) {
            Glide.with(getApplicationContext())
                    .load(R.drawable.default_avatar)
                    .transform(new RoundImageTransform(EMICalculatorActivity.this))
                    .into(imageView_emi);
        } else {
            Glide.with(getApplicationContext())
                    .load(user.get("dealer_img"))
                    .transform(new RoundImageTransform(EMICalculatorActivity.this))
                    .into(imageView_emi);
        }
        profile_address_emi.setText(saved_address_emi);

        nav_funding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNav_emi.toggleLeftDrawer();
            }
        });

        cardddds = (CardView) findViewById(R.id.cardddds);

        session = new SessionManager(EMICalculatorActivity.this);

        //NAVIGATION DRAWER LIST VIEW
        emi_buypagenavigation = new BuyPageNavigation();
        CustomList adapter = new CustomList(EMICalculatorActivity.this, BuyPageNavigation.web, BuyPageNavigation.imageId);
        ListView list = (ListView) findViewById(R.id.nav_list_view);
        list.setAdapter(adapter);

        RelativeLayout call_layout = (RelativeLayout) mNav_emi.findViewById(R.id.call_layout);
        RelativeLayout chat_layout = (RelativeLayout) mNav_emi.findViewById(R.id.chat_layout);
        RelativeLayout logout_layout = (RelativeLayout) mNav_emi.findViewById(R.id.logout_layout);

        call_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertbox = new AlertDialog.Builder(EMICalculatorActivity.this)
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
                mNav_emi.closeLeftSide();
                EMICalculatorActivity.this.finish();
            }
        });

        cardddds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(EMICalculatorActivity.this, "nullll", Toast.LENGTH_SHORT).show();
            }
        });

        chat_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chat();
                mNav_emi.closeLeftSide();
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (BuyPageNavigation.web[position] == "Dashboard") {
                    Intent intent = new Intent(EMICalculatorActivity.this, MainDashBoardActivity.class);
                    startActivity(intent);
                    EMICalculatorActivity.this.finish();
                    mNav_emi.closeLeftSide();
                } else if (BuyPageNavigation.web[position] == "Buy") {
                    Intent intent = new Intent(EMICalculatorActivity.this, DashBoard.class);
                    startActivity(intent);
                    EMICalculatorActivity.this.finish();
                    mNav_emi.closeLeftSide();
                } else if (BuyPageNavigation.web[position] == "Sell") {
                    Intent intent = new Intent(EMICalculatorActivity.this, SellDashBoardActivity.class);
                    startActivity(intent);
                    EMICalculatorActivity.this.finish();
                    mNav_emi.closeLeftSide();
                } else if (BuyPageNavigation.web[position] == "Manage") {
                    Intent intent = new Intent(EMICalculatorActivity.this, ManageDashBoardActivity.class);
                    startActivity(intent);
                    EMICalculatorActivity.this.finish();
                    mNav_emi.closeLeftSide();
                    profileManagerSession = new ProfileManagerSession(EMICalculatorActivity.this);
                    profileManagerSession.clear_ProfileManage();
                } else if (BuyPageNavigation.web[position] == "Funding") {
                    plandetailsSharedPreferences = new PlandetailsSharedPreferences(EMICalculatorActivity.this);
                    plan = plandetailsSharedPreferences.getUserDetails();

//                    if ( plan.get("PlanName").equals("BASIC")) {
//
//                        Intent intent = new Intent(EMICalculatorActivity.this, LoanActivity.class);
//                        startActivity(intent);
//                        mNav_emi.closeLeftSide();
//                        EMICalculatorActivity.this.finish();
//                    } else {
                        Intent intent = new Intent(EMICalculatorActivity.this, FundingActivity.class);
                        startActivity(intent);
                        mNav_emi.closeLeftSide();
                        EMICalculatorActivity.this.finish();
//                    }
                } else if (BuyPageNavigation.web[position] == "Networks") {
                    String url = "http://app.dealerplus.in/user_login_cometchat?session_user_id=" + user.get("user_id");
                    Intent intet = new Intent(EMICalculatorActivity.this, NetworksWebviewActivity.class);
                    //intet.putExtra("title", BuyPageNavigation.web[position]);
                    intet.putExtra("url", url);
                    startActivity(intet);
                    mNav_emi.closeLeftSide();
                } else if (BuyPageNavigation.web[position] == "Reports") {
                    plandetailsSharedPreferences = new PlandetailsSharedPreferences(EMICalculatorActivity.this);

                    plan = plandetailsSharedPreferences.getUserDetails();

                    if (plan.get("PlanName").equals("BASIC")) {

                        Intent intent = new Intent(EMICalculatorActivity.this, BuyPlanActivity.class);
                        startActivity(intent);
                        //MainDashBoardActivity.this.finish();
                        mNav_emi.closeLeftSide();
                    } else {
                        Intent intent = new Intent(EMICalculatorActivity.this, ReportSalesActivity.class);
                        startActivity(intent);
                        EMICalculatorActivity.this.finish();
                        mNav_emi.closeLeftSide();
                    }
                } else if (BuyPageNavigation.web[position] == "FAQs") {
                    faqs();
                    mNav_emi.closeLeftSide();
                } else {
                    //Toast.makeText(DashBoard.this, web[position], Toast.LENGTH_SHORT).show();
                }
            }
        });


        nav_tick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emicalculation();
            }
        });

        month = (Button) findViewById(R.id.month);
        year = (Button) findViewById(R.id.year);
        plus_button = (Button) findViewById(R.id.plus_button);
        minus_button = (Button) findViewById(R.id.minus_button);
        increment_button = (TextView) findViewById(R.id.increment_button);
        intrest_percent = (TextView) findViewById(R.id.intrest_percent);

        no_of_month = (TextView) findViewById(R.id.no_of_month);
        no_of_month_yaers = (TextView) findViewById(R.id.no_of_month_yaers);

        increment_month = (TextView) findViewById(R.id.increment_month);
        increment_button_month_year = (TextView) findViewById(R.id.increment_button_month_year);

        plus_button_month_year = (Button) findViewById(R.id.plus_button_month_year);
        minus_button_month_year = (Button) findViewById(R.id.minus_button_month_year);

        monthly_payment_amount = (TextView) findViewById(R.id.monthly_payment_amount);

        loan_amount = (EditText) findViewById(R.id.loan_amount);

        total_payment_amount = (TextView) findViewById(R.id.total_payment_amount);

        plus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value_new = Integer.parseInt(increment_button.getText().toString());
                if (value_new < 60) {
                    value_new = value_new + 1;
                    increment_button.setText(value_new + "");
                    intrest_percent.setText(value_new + " %");
                } else {

                }

            }
        });

        minus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value_new = Integer.parseInt(increment_button.getText().toString());
                if (value_new < 0) {
                    value_new = 0;
                    increment_button.setText(value_new + "");
                    intrest_percent.setText(value_new + " %");
                } else if (value_new > 0) {
                    value_new = value_new - 1;
                    increment_button.setText(value_new + "");
                    intrest_percent.setText(value_new + " %");

                }
            }
        });


        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month.setBackgroundResource(R.drawable.budget_model);
                year.setBackgroundResource(R.drawable.by_model);
                month.setTextColor(Color.WHITE);
                year.setTextColor(Color.BLACK);
                no_of_month.setText("Months");
                increment_month.setText("Months:");
                increment_button_month_year.setText("0");
                no_of_month_yaers.setText("0");
                value_smple = 0;
                monthly_payment_amount.setText("0");
            }
        });

        year.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                month.setBackgroundResource(R.drawable.by_model);
                year.setBackgroundResource(R.drawable.budget_model);
                month.setTextColor(Color.BLACK);
                year.setTextColor(Color.WHITE);
                no_of_month.setText("Years");
                increment_month.setText("Years:");
                increment_button_month_year.setText("0");
                no_of_month_yaers.setText("0");
                value_smple = 0;
                monthly_payment_amount.setText("0");
            }
        });

        plus_button_month_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value_news = Integer.parseInt(increment_button_month_year.getText().toString());

                if (value_news < 100) {
                    value_news = value_news + 1;
                    increment_button_month_year.setText(value_news + "");
                    no_of_month_yaers.setText(value_news + "");
                } else {

                }

            }
        });

        minus_button_month_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value1 = Integer.parseInt(increment_button_month_year.getText().toString());
                if (value1 < 0) {
                    value1 = 0;
                    increment_button_month_year.setText(value1 + "");
                    no_of_month_yaers.setText(value1 + "");
                } else if (value1 > 0) {
                    value1 = value1 - 1;
                    increment_button_month_year.setText(value1 + "");
                    no_of_month_yaers.setText(value1 + "");
                }
            }
        });


        loan_amount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loan_amount.clearFocus();
                    InputMethodManager in = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(loan_amount.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
    }

    public void emicalculation() {

        String amount = loan_amount.getText().toString();

        //total_payment_amount.setText(amount);

        if (amount.equals("")) {

            final AlertDialog alertbox = new AlertDialog.Builder(EMICalculatorActivity.this)
                    .setTitle("Error")
                    .setMessage("Enter All the Values")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    })
                    .show();

        } else {
            loanAmount = Double.parseDouble(amount);
            interestRate = Integer.parseInt(increment_button.getText().toString());
            if (no_of_month.getText().toString().equals("Years")) {
                loanPeriod = Integer.parseInt(no_of_month_yaers.getText().toString()) * 12;
            } else {
                loanPeriod = Integer.parseInt(no_of_month_yaers.getText().toString());
            }
            r = interestRate / 1200;
            r1 = Math.pow(r + 1, loanPeriod);

            Log.e("r1", String.valueOf(r1));
            Log.e("r1", String.valueOf(loanPeriod));

            monthlyPayment = (r + (r / (r1 - 1))) * loanAmount;

            if (interestRate == 0) {
                if (loanPeriod == 0) {
                    monthly_payment_amount.setText(new DecimalFormat("##.##").format(loanAmount));
                } else {
                    loanAmount = loanAmount / loanPeriod;
                    monthly_payment_amount.setText(new DecimalFormat("##.##").format(loanAmount));
                }
            } else if (increment_button_month_year.getText().equals("0")) {
                monthly_payment_amount.setText(new DecimalFormat("##.##").format(loanAmount));
            } else {
                totalPayment = monthlyPayment * loanPeriod;
                monthly_payment_amount.setText(new DecimalFormat("##.##").format(monthlyPayment));
            }
            if (no_of_month.getText().toString().equals("Years")) {
                double month_values = monthlyPayment * Double.parseDouble(increment_button_month_year.getText().toString()) * 12;
                String nan_values = String.valueOf(month_values);
                if (nan_values == "NaN") {
                    total_payment_amount.setText(new DecimalFormat("##.##").format(loanAmount));
                } else {
                    total_payment_amount.setText(new DecimalFormat("##.##").format(month_values));
                }
            } else {
                double month_values = monthlyPayment * Double.parseDouble(increment_button_month_year.getText().toString());
                String nan_values = String.valueOf(month_values);
                if (nan_values == "NaN") {
                    total_payment_amount.setText(new DecimalFormat("##.##").format(loanAmount));
                } else {
                    total_payment_amount.setText(new DecimalFormat("##.##").format(month_values));
                }
            }
        }
    }

    public void faqs() {
        FaqOptions faqOptions = new FaqOptions()
                .showFaqCategoriesAsGrid(true)
                .showContactUsOnAppBar(true)
                .showContactUsOnFaqScreens(false)
                .showContactUsOnFaqNotHelpful(false);

        Hotline.showFAQs(EMICalculatorActivity.this, faqOptions);
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
                        EMICalculatorActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();

    }

}
