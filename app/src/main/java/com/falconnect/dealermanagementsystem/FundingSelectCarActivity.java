package com.falconnect.dealermanagementsystem;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.falconnect.dealermanagementsystem.Adapter.FundingCarSelectListAdapter;
import com.falconnect.dealermanagementsystem.Model.FundingCarSelectModel;
import com.falconnect.dealermanagementsystem.SharedPreference.FundingConfirmationDetails;
import com.falconnect.dealermanagementsystem.SharedPreference.LoanFundingSharedPreferences;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class FundingSelectCarActivity extends AppCompatActivity {

    public ArrayList<HashMap<String, String>> carselect_listview;
    public ArrayList<String> select_car = new ArrayList<String>();
    public ArrayList<String> car_price = new ArrayList<String>();
    ImageView funding_car_list_back;
    SessionManager session;
    HashMap<String, String> user;
    HashMap<String, String> carselectlistview;
    ListView multipleselectcarlistview;
    FundingCarSelectListAdapter myUserListAdapter;
    RelativeLayout apply;
    EditText totalAmount;
    FundingConfirmationDetails fundingConfirmationDetails;
    String city_id, branch_id;
    LoanFundingSharedPreferences loanFundingSharedPreferences;
    HashMap<String, String> user_save;
    ArrayList<String> car_id_list = new ArrayList<>();
    ArrayList<String> car_name_list = new ArrayList<>();
    ArrayList<String> car_image_list = new ArrayList<>();
    ArrayList<String> car_price_list = new ArrayList<>();
    String total_amount;
    ProgressDialog barProgressDialog;

    private boolean mVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_funding_select_car);

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

        city_id = getIntent().getStringExtra("city_id");
        branch_id = getIntent().getStringExtra("branch_id");

        session = new SessionManager(FundingSelectCarActivity.this);
        user = session.getUserDetails();


        funding_car_list_back = (ImageView) findViewById(R.id.funding_car_list_back);
        totalAmount = (EditText) findViewById(R.id.total_amount);

        loanFundingSharedPreferences = new LoanFundingSharedPreferences(FundingSelectCarActivity.this);
        user_save = loanFundingSharedPreferences.getAddress_details();

        funding_car_list_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FundingSelectCarActivity.this.finish();
            }
        });

        apply = (RelativeLayout) findViewById(R.id.apply_funding_amount_button);

        new multi_car_list().execute();

    }

    private ArrayList<FundingCarSelectModel> getUserData() {
        final ArrayList<FundingCarSelectModel> inventorydata = new ArrayList<>();
        for (int i = 0; i < carselect_listview.size(); i++) {

            String image = carselect_listview.get(i).get("image");
            String car_id = carselect_listview.get(i).get("car_id");
            String listing_id = carselect_listview.get(i).get("listing_id");
            String inventory_type = carselect_listview.get(i).get("inventory_type");
            String dealer_id = carselect_listview.get(i).get("dealer_id");
            String price = carselect_listview.get(i).get("price");
            String kms_done = carselect_listview.get(i).get("kms_done");
            String registration_year = carselect_listview.get(i).get("registration_year");
            String owner_type = carselect_listview.get(i).get("owner_type");
            String make = carselect_listview.get(i).get("make");
            String model = carselect_listview.get(i).get("model");
            String varient = carselect_listview.get(i).get("varient");
            String fuel_type = carselect_listview.get(i).get("fuel_type");
            String carstatus = carselect_listview.get(i).get("carstatus");


            inventorydata.add(new FundingCarSelectModel(image, car_id, listing_id, inventory_type, dealer_id,
                    price, kms_done, registration_year, owner_type, make, model, varient, fuel_type, carstatus, select_car, false));
        }
        return inventorydata;
    }

    public void passvalue(String amount, ArrayList<String> car_id, ArrayList<String> car_name, ArrayList<String> car_image, ArrayList<String> car_price) {

        totalAmount.setText("Rs. " + amount);
        total_amount = amount;
        car_id_list = car_id;
        car_name_list = car_name;
        car_image_list = car_image;
        car_price_list = car_price;

    }

    private class multi_car_list extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = ProgressDialog.show(FundingSelectCarActivity.this, "Loading...", "Please Wait ...", true);
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();
            String queriesurl;

            if (user_save.get("loan").equals("0")) {

                queriesurl = Constant.APPLY_FUNDING_SELECT_CAR
                        + "session_user_id=" + user.get("user_id")
                        + "&page_name=viewallfundinglist"
                        + "&city_id=" + city_id
                        + "&branch_id=" + branch_id;
            } else {
                queriesurl = Constant.APPLY_LOAN_SELECT_CAR
                        + "session_user_id=" + user.get("user_id")
                        + "&page_name=viewallloanlist"
                        + "&city_id=" + city_id
                        + "&branch_id=" + branch_id;
            }


            Log.e("queriesurl", queriesurl);

            String json = sh.makeServiceCall(queriesurl, ServiceHandler.POST);

            if (json != null) {

                carselect_listview = new ArrayList<>();

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    JSONArray loan = jsonObj.getJSONArray("funding_list");

                    for (int k = 0; k <= loan.length(); k++) {

                        String image = loan.getJSONObject(k).getString("image");
                        String car_id = loan.getJSONObject(k).getString("car_id");
                        String listing_id = loan.getJSONObject(k).getString("listing_id");
                        String inventory_type = loan.getJSONObject(k).getString("inventory_type");
                        String dealer_id = loan.getJSONObject(k).getString("dealer_id");
                        String price = loan.getJSONObject(k).getString("price");
                        String kms_done = loan.getJSONObject(k).getString("kms_done");
                        String registration_year = loan.getJSONObject(k).getString("registration_year");
                        String owner_type = loan.getJSONObject(k).getString("owner_type");
                        String make = loan.getJSONObject(k).getString("make");
                        String model = loan.getJSONObject(k).getString("model");
                        String varient = loan.getJSONObject(k).getString("varient");
                        String fuel_type = loan.getJSONObject(k).getString("fuel_type");
                        String carstatus = loan.getJSONObject(k).getString("carstatus");

                        carselectlistview = new HashMap<>();

                        carselectlistview.put("image", image);
                        carselectlistview.put("car_id", car_id);
                        carselectlistview.put("listing_id", listing_id);
                        carselectlistview.put("inventory_type", inventory_type);
                        carselectlistview.put("dealer_id", dealer_id);
                        carselectlistview.put("price", price);
                        carselectlistview.put("kms_done", kms_done);
                        carselectlistview.put("registration_year", registration_year);
                        carselectlistview.put("owner_type", owner_type);
                        carselectlistview.put("make", make);
                        carselectlistview.put("model", model);
                        carselectlistview.put("varient", varient);
                        carselectlistview.put("fuel_type", fuel_type);
                        carselectlistview.put("carstatus", carstatus);

                        select_car.add("0");
                        car_price.add(price);

                        carselect_listview.add(carselectlistview);

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

            multipleselectcarlistview = (ListView) findViewById(R.id.multipleselectcarlistview);
            myUserListAdapter = new FundingCarSelectListAdapter(FundingSelectCarActivity.this, getUserData(), select_car, car_price);
            multipleselectcarlistview.setAdapter(myUserListAdapter);


            apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    fundingConfirmationDetails = new FundingConfirmationDetails(FundingSelectCarActivity.this);
                    fundingConfirmationDetails.createConfirmationDetails(car_id_list, car_name_list, car_image_list, car_price_list);

                    if (total_amount != null) {

                        Intent push = new Intent(FundingSelectCarActivity.this, ConfirmationFundingActivity.class);
                        push.putExtra("total_amount", total_amount);
                        push.putExtra("car_id_list", car_id_list);
                        push.putExtra("car_name_list", car_name_list);
                        push.putExtra("car_image_list", car_image_list);
                        push.putExtra("car_price_list", car_price_list);
                        //push.putExtra("title", "Apply Loan");
                        startActivity(push);

                        FundingSelectCarActivity.this.finish();

                    } else {

                        AlertDialog alertbox = new AlertDialog.Builder(FundingSelectCarActivity.this)
                                .setMessage("Please select the Car")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                    }
                                })

                                .show();

                    }
                }
            });
        }
    }

}

