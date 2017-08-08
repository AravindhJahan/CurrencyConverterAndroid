package com.falconnect.dealermanagementsystem;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.falconnect.dealermanagementsystem.Adapter.FundingFooterCustomAdapter;
import com.falconnect.dealermanagementsystem.FontAdapter.RoundImageTransform;
import com.falconnect.dealermanagementsystem.Model.FundingDataModel;
import com.falconnect.dealermanagementsystem.SharedPreference.InventorySavedDetails;
import com.falconnect.dealermanagementsystem.SharedPreference.LoanFundingSharedPreferences;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ApplyFundingActivity extends AppCompatActivity implements View.OnClickListener {

    static final int DATE_PICKER_ID = 999;
    private static RecyclerView.Adapter adapter;
    private static RecyclerView applyfundingrecyclerView;
    private static ArrayList<FundingDataModel> data;
    public ArrayList<HashMap<String, String>> city_list;
    public ArrayList<HashMap<String, String>> dealer_list;
    ImageView applyfundingback;
    TextView name_inventory, dealership_inventory, dealership_mail_inventory, dealership_number_inventory;
    TextView applying_date, applying_city, applying_branch;
    Button next_inventory;
    InventorySavedDetails inventorySavedDetails;
    ImageView apply_funding_ownerimage;
    SessionManager sessionManager;
    HashMap<String, String> city_list_map;
    HashMap<String, String> dealer_list_map;
    List<String> list = new ArrayList<>();
    List<String> list_id = new ArrayList<>();
    List<String> list_dealer = new ArrayList<>();
    List<String> list_dealer_id = new ArrayList<>();
    String city_postition, branch_postition;
    HashMap<String, String> user_details;
    String img;
    ProgressDialog barProgressDialog;
    LoanFundingSharedPreferences loanFundingSharedPreferences;
    HashMap<String, String> user_save;
    HashMap<String, String> saved_detail;
    TextView title, info;
    private boolean mVisible;
    private int year;
    private int month;
    private int date;
    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDate) {
            year = selectedYear;
            month = selectedMonth;
            date = selectedDate;
            applying_date.setText(new StringBuilder().append(year).append("/").append(month + 1).append("/").append(date));

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_apply_funding);

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

        //Session Manager
        sessionManager = new SessionManager(ApplyFundingActivity.this);
        user_details = sessionManager.getUserDetails();

        loanFundingSharedPreferences = new LoanFundingSharedPreferences(ApplyFundingActivity.this);
        user_save = loanFundingSharedPreferences.getAddress_details();

        title = (TextView) findViewById(R.id.title);
        info = (TextView) findViewById(R.id.dealer_info);


        if (user_save.get("loan").equals("0")) {
            title.setText("Apply Funding");
            info.setText("Dealer Information");
        } else {
            info.setText("Customer Information");
            title.setText("Apply Loan");
        }

        applyfundingrecyclerView = (RecyclerView) findViewById(R.id.my_recycler_apply_funding);
        applyfundingrecyclerView.setHasFixedSize(true);
        applyfundingrecyclerView.setLayoutManager(new LinearLayoutManager(ApplyFundingActivity.this, LinearLayoutManager.HORIZONTAL, false));

        data = new ArrayList<FundingDataModel>();
        for (int i = 0; i < MyFundingFooterData.nameArrayFunding.length; i++) {
            data.add(new FundingDataModel(
                    MyFundingFooterData.nameArrayFunding[i],
                    MyFundingFooterData.id_[i],
                    MyFundingFooterData.drawableArrayWhite0[i]
            ));
        }

        adapter = new FundingFooterCustomAdapter(ApplyFundingActivity.this, data);
        applyfundingrecyclerView.setAdapter(adapter);


        applyfundingback = (ImageView) findViewById(R.id.apply_funding_back);
        apply_funding_ownerimage = (ImageView) findViewById(R.id.apply_funding_ownerimage);

        name_inventory = (TextView) findViewById(R.id.name_inventory);
        dealership_inventory = (TextView) findViewById(R.id.dealership_inventory);
        dealership_mail_inventory = (TextView) findViewById(R.id.dealership_mail_inventory);
        dealership_number_inventory = (TextView) findViewById(R.id.dealership_number_inventory);
        applying_date = (TextView) findViewById(R.id.applying_date);
        applying_city = (TextView) findViewById(R.id.applying_city);
        applying_branch = (TextView) findViewById(R.id.applying_branch);

        next_inventory = (Button) findViewById(R.id.next_inventory);


        inventorySavedDetails = new InventorySavedDetails(ApplyFundingActivity.this);
        saved_detail = inventorySavedDetails.getInventoryDetails();

        img = saved_detail.get("image");
        Glide.with(getApplicationContext())
                .load(saved_detail.get("image"))
                .transform(new RoundImageTransform(ApplyFundingActivity.this))
                .into(apply_funding_ownerimage);

        name_inventory.setText(saved_detail.get("name"));
        dealership_inventory.setText(saved_detail.get("dealershipname"));
        dealership_mail_inventory.setText(saved_detail.get("email"));
        dealership_number_inventory.setText(saved_detail.get("phone_num"));

        final Calendar c = Calendar.getInstance();
        date = c.get(Calendar.DATE);
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);

        applying_date.setText(new StringBuilder().append(year).append("/").append(month + 1).append("/").append(date));

        applying_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_PICKER_ID);
            }
        });

        applyfundingback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplyFundingActivity.this.finish();
            }
        });

        applying_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new dealer_city().execute();
                list.clear();
            }
        });

        applying_branch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog alertbox = new AlertDialog.Builder(ApplyFundingActivity.this)
                        .setMessage("Please select the City")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        })
                        .show();

            }
        });

        next_inventory.setVisibility(View.GONE);


        next_inventory.setOnClickListener(this);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:

                return new DatePickerDialog(ApplyFundingActivity.this, pickerListener, year, month, date);
        }
        return null;
    }


    @Override
    public void onClick(View v) {

        String str = applying_city.getText().toString();
        Log.e("city==", applying_branch.getText().toString());
        Log.e("str==", str);
        Log.e("date==", applying_date.getText().toString());


        if (applying_city.getText().toString().equals("City")) {
            AlertDialog alertbox = new AlertDialog.Builder(this)
                    .setMessage("Please select the City")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    })
                    .show();
        } else if (applying_branch.getText().equals("Branch")) {
            AlertDialog alertbox = new AlertDialog.Builder(this)
                    .setMessage("Please select the Branch")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    })

                    .show();
        } else {

            inventorySavedDetails.createInventoryDetailsSession(
                    img,
                    name_inventory.getText().toString(),
                    dealership_inventory.getText().toString(),
                    dealership_mail_inventory.getText().toString(),
                    dealership_number_inventory.getText().toString(),
                    applying_date.getText().toString(),
                    city_postition,
                    branch_postition,
                    saved_detail.get("contactid"));

            Intent intent = new Intent(ApplyFundingActivity.this, FundingSelectCarActivity.class);
            intent.putExtra("city_id", city_postition);
            intent.putExtra("branch_id", branch_postition);
            startActivity(intent);

            ApplyFundingActivity.this.finish();
        }
    }

    private class dealer_city extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = ProgressDialog.show(ApplyFundingActivity.this, "Loading...", "Please Wait ...", true);
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();

            String url = Constant.MY_USER_TAB_VIEW + "session_user_id=" + user_details.get("user_id");

            Log.e("queriesurl", url);

            String json = sh.makeServiceCall(url, ServiceHandler.POST);

            if (json != null) {

                city_list = new ArrayList<>();

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    JSONArray loan = jsonObj.getJSONArray("dealer_city");

                    for (int k = 0; k <= loan.length(); k++) {

                        String country_id = loan.getJSONObject(k).getString("country_id");
                        String state_id = loan.getJSONObject(k).getString("state_id");
                        String city_id = loan.getJSONObject(k).getString("city_id");
                        String city_name = loan.getJSONObject(k).getString("city_name");
                        String popular_status = loan.getJSONObject(k).getString("popular_status");

                        city_list_map = new HashMap<>();

                        city_list_map.put("country_id", country_id);
                        city_list_map.put("state_id", state_id);
                        city_list_map.put("city_id", city_id);
                        city_list_map.put("city_name", city_name);
                        city_list_map.put("popular_status", popular_status);

                        city_list.add(city_list_map);

                        list.add(city_list_map.get("city_name"));
                        list_id.add(city_list_map.get("city_id"));
                    }

                } catch (final JSONException e) {

                }

            } else {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            barProgressDialog.dismiss();
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(ApplyFundingActivity.this);
            builderSingle.setTitle("Select City");
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ApplyFundingActivity.this, android.R.layout.simple_list_item_1);
            for (int i = 0; i < list.size(); i++) {
                arrayAdapter.add(list.get(i));
            }

            builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String strName = arrayAdapter.getItem(which);
                    applying_city.setText(strName);
                    city_postition = list_id.get(which);
                }
            });
            builderSingle.show();

            applying_branch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new get_dealer().execute();
                    list_dealer.clear();
                }
            });
        }
    }

    private class get_dealer extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = ProgressDialog.show(ApplyFundingActivity.this, "Loading...", "Please Wait ...", true);
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();

            String url = Constant.GET_DEALER_BRANCH
                    + "session_user_id=" + user_details.get("user_id")
                    + "&city_id=" + city_postition;

            Log.e("queriesurl", url);

            String json = sh.makeServiceCall(url, ServiceHandler.POST);

            if (json != null) {

                dealer_list = new ArrayList<>();

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    JSONArray loan = jsonObj.getJSONArray("branchname");

                    for (int k = 0; k <= loan.length(); k++) {

                        String branch_id = loan.getJSONObject(k).getString("branch_id");
                        String dealer_id = loan.getJSONObject(k).getString("dealer_id");
                        String dealer_name = loan.getJSONObject(k).getString("dealer_name");
                        String dealer_contact_no = loan.getJSONObject(k).getString("dealer_contact_no");
                        String branch_address = loan.getJSONObject(k).getString("branch_address");
                        String dealer_state = loan.getJSONObject(k).getString("dealer_state");
                        String dealer_city = loan.getJSONObject(k).getString("dealer_city");
                        String dealer_pincode = loan.getJSONObject(k).getString("dealer_pincode");
                        String dealer_mail = loan.getJSONObject(k).getString("dealer_mail");
                        String dealer_service = loan.getJSONObject(k).getString("dealer_service");
                        String headquarter = loan.getJSONObject(k).getString("headquarter");
                        String latitude = loan.getJSONObject(k).getString("latitude");
                        String longitude = loan.getJSONObject(k).getString("longitude");
                        String dealer_status = loan.getJSONObject(k).getString("dealer_status");

                        dealer_list_map = new HashMap<>();

                        dealer_list_map.put("branch_id", branch_id);
                        dealer_list_map.put("dealer_id", dealer_id);
                        dealer_list_map.put("dealer_name", dealer_name);
                        dealer_list_map.put("dealer_contact_no", dealer_contact_no);
                        dealer_list_map.put("branch_address", branch_address);
                        dealer_list_map.put("dealer_state", dealer_state);
                        dealer_list_map.put("dealer_city", dealer_city);
                        dealer_list_map.put("dealer_pincode", dealer_pincode);
                        dealer_list_map.put("dealer_mail", dealer_mail);
                        dealer_list_map.put("dealer_service", dealer_service);
                        dealer_list_map.put("headquarter", headquarter);
                        dealer_list_map.put("latitude", latitude);
                        dealer_list_map.put("longitude", longitude);
                        dealer_list_map.put("dealer_status", dealer_status);

                        dealer_list.add(dealer_list_map);

                        list_dealer.add(dealer_list_map.get("dealer_name"));
                        list_dealer_id.add(dealer_list_map.get("branch_id"));
                    }

                } catch (final JSONException e) {

                }

            } else {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            barProgressDialog.dismiss();
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(ApplyFundingActivity.this);
            builderSingle.setTitle("Select Dealer");
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ApplyFundingActivity.this, android.R.layout.simple_list_item_1);
            for (int i = 0; i < list_dealer.size(); i++) {
                arrayAdapter.add(list_dealer.get(i));
            }

            builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String strName = arrayAdapter.getItem(which);
                    applying_branch.setText(strName);
                    branch_postition = list_dealer_id.get(which);
                    next_inventory.setVisibility(View.VISIBLE);
                }
            });
            builderSingle.show();

        }
    }

}