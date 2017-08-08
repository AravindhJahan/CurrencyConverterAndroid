package com.falconnect.dealermanagementsystem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.falconnect.dealermanagementsystem.Adapter.FundingCarConfirmationAdapter;
import com.falconnect.dealermanagementsystem.FontAdapter.RoundImageTransform;
import com.falconnect.dealermanagementsystem.SharedPreference.FundingConfirmationDetails;
import com.falconnect.dealermanagementsystem.SharedPreference.InventorySavedDetails;
import com.falconnect.dealermanagementsystem.SharedPreference.LoanFundingSharedPreferences;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ConfirmationFundingActivity extends Activity {


    public ArrayList<HashMap<String, String>> resultsss;
    ImageView apply_funding_confrm_back;
    ImageView profile_image;
    TextView name, pan_number, mobile_number, email_id;
    TextView date;
    EditText amount;
    InventorySavedDetails inventorySavedDetails;
    HashMap<String, String> inventory_details;
    ListView listView;
    String total_amount;
    FundingCarConfirmationAdapter fundingCarConfirmationAdapter;
    FundingConfirmationDetails fundingConfirmationDetails;
    HashMap<String, String> user_car_id;
    ArrayList<String> car_id_list = new ArrayList<>();
    ArrayList<String> car_name_list = new ArrayList<>();
    ArrayList<String> car_image_list = new ArrayList<>();
    ArrayList<String> car_price_list = new ArrayList<>();
    Button submit_confirm;
    LoanFundingSharedPreferences loanFundingSharedPreferences;
    HashMap<String, String> user_save;
    ProgressDialog barProgressDialog;
    SessionManager sessionManager;
    HashMap<String, String> user;
    ArrayList<String> car_id_list_new = new ArrayList<>();
    ArrayList<String> car_name_list_new = new ArrayList<>();
    ArrayList<String> car_image_list_new = new ArrayList<>();
    ArrayList<String> car_price_list_new = new ArrayList<>();
    HashMap<String, String> resultmap;

    TextView title_saved_cars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_confirmation_funding);

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        total_amount = getIntent().getStringExtra("total_amount");

        car_id_list = getIntent().getStringArrayListExtra("car_id_list");
        car_name_list = getIntent().getStringArrayListExtra("car_name_list");
        car_image_list = getIntent().getStringArrayListExtra("car_image_list");
        car_price_list = getIntent().getStringArrayListExtra("car_price_list");


        for (int i = 0; i < car_id_list.size(); i++) {
            if (!car_id_list.get(i).equals("0")) {
                car_id_list_new.add(car_id_list.get(i));
                car_name_list_new.add(car_name_list.get(i));
                car_image_list_new.add(car_image_list.get(i));
                car_price_list_new.add(car_price_list.get(i));
            } else {

            }
        }


        apply_funding_confrm_back = (ImageView) findViewById(R.id.apply_funding_confrm_back);
        profile_image = (ImageView) findViewById(R.id.apply_funding_ownerimage);
        name = (TextView) findViewById(R.id.name);
        pan_number = (TextView) findViewById(R.id.pan_number);
        mobile_number = (TextView) findViewById(R.id.mobile_number);
        email_id = (TextView) findViewById(R.id.email_id);
        date = (TextView) findViewById(R.id.date);

        submit_confirm = (Button) findViewById(R.id.submit_confirm);
        title_saved_cars = (TextView) findViewById(R.id.title_saved_cars);
        amount = (EditText) findViewById(R.id.amount);

        listView = (ListView) findViewById(R.id.car_list_fund);


        apply_funding_confrm_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmationFundingActivity.this.finish();
            }
        });

        sessionManager = new SessionManager(ConfirmationFundingActivity.this);
        user = sessionManager.getUserDetails();

        inventorySavedDetails = new InventorySavedDetails(ConfirmationFundingActivity.this);
        inventory_details = inventorySavedDetails.getInventoryDetails();

        loanFundingSharedPreferences = new LoanFundingSharedPreferences(ConfirmationFundingActivity.this);
        user_save = loanFundingSharedPreferences.getAddress_details();

        if (inventory_details.get("image").equals(null)) {
            Glide.with(ConfirmationFundingActivity.this)
                    .load(R.drawable.carimageplaceholder)
                    .transform(new RoundImageTransform(ConfirmationFundingActivity.this))
                    .into(profile_image);
        } else {
            Glide.with(ConfirmationFundingActivity.this)
                    .load(inventory_details.get("image"))
                    .transform(new RoundImageTransform(ConfirmationFundingActivity.this))
                    .into(profile_image);
        }

        if (user_save.get("loan").equals("0")) {
            title_saved_cars.setText("Apply Fund");
        } else {
            title_saved_cars.setText("Apply Loan");
        }


        if (total_amount.equals(null)) {
            amount.setText("NULL");
        } else {
            amount.setText("Rs." + " " + total_amount.toString());
        }

        // Name
        if (inventory_details.get("name").equals(null)) {
            name.setText("NULL");
        } else {
            name.setText(inventory_details.get("name"));
        }
        //PAN Number
        if (inventory_details.get("dealershipname").equals(null)) {
            pan_number.setText("NULL");
        } else {
            pan_number.setText(inventory_details.get("dealershipname"));
        }
        //Email ID
        if (inventory_details.get("email").equals(null)) {
            email_id.setText("NULL");
        } else {
            email_id.setText(inventory_details.get("email"));
        }
        //Phone Number
        if (inventory_details.get("phone_num").equals(null)) {
            mobile_number.setText("NULL");
        } else {
            mobile_number.setText(inventory_details.get("phone_num"));
        }

        //Dateeeee
        if (inventory_details.get("date").equals(null)) {
            date.setText("NULL");
        } else {
            date.setText(inventory_details.get("date"));
        }


        fundingCarConfirmationAdapter = new FundingCarConfirmationAdapter(ConfirmationFundingActivity.this, car_id_list_new, car_name_list_new, car_image_list_new, car_price_list_new);
        listView.setAdapter(fundingCarConfirmationAdapter);

        submit_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // XML Parsing Using AsyncTask...
                if (isNetworkAvailable()) {
                    new submit_api().execute();
                } else {
                    Toast.makeText(ConfirmationFundingActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    ConfirmationFundingActivity.this.finish();
                }
            }
        });

    }

    // Check Internet Connection!!!
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    private class submit_api extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = ProgressDialog.show(ConfirmationFundingActivity.this, "Loading...", "Please Wait ...", true);
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();

            String url;

            String dealershipname = inventory_details.get("dealershipname").replace(" ", "%20");
            String name = inventory_details.get("name").replace(" ", "%20");

            StringBuilder builder = new StringBuilder();
            for (String value : car_id_list_new) {
                builder.append(value);
                builder.append(",");
            }
            String text = builder.toString();


            if (user_save.get("loan").equals("0")) {

                url = Constant.CONFIRMATION_API_FUND
                        + "session_user_id=" + user.get("user_id")
                        + "&page_name=applyfunding"
                        + "&dealername=" + name
                        + "&mobilenumber=" + inventory_details.get("phone_num")
                        + "&place=" + inventory_details.get("city")
                        + "&emailid=" + inventory_details.get("email")
                        + "&date=" + inventory_details.get("date")
                        + "&branch=" + inventory_details.get("branch")
                        + "&dealershipname=" + dealershipname
                        + "&appendcarid=" + text
                        + "&fundingamount=" + total_amount.toString();
            } else {

                String id = text.replace(",", "");

                url = Constant.CONFIRMATION_API_LOAN
                        + "session_user_id=" + user.get("user_id")
                        + "&page_name=applyloan"
                        + "&customername=" + inventory_details.get("contactid")
                        + "&mobilenumber=" + inventory_details.get("phone_num")
                        + "&place=" + inventory_details.get("city")
                        + "&emailid=" + inventory_details.get("email")
                        + "&date=" + inventory_details.get("date")
                        + "&branch=" + inventory_details.get("branch")
                        + "&pannumber=" + dealershipname
                        + "&carid=" + id
                        + "&loanamount=" + total_amount;
            }


            String json = sh.makeServiceCall(url, ServiceHandler.POST);

            if (json != null) {
                resultsss = new ArrayList<>();
                try {
                    JSONObject jsonObj = new JSONObject(json);

                    for (int i = 0; i < jsonObj.length(); i++) {

                        String result = jsonObj.getString("Result");
                        String message = jsonObj.getString("message");

                        resultmap = new HashMap<>();

                        resultmap.put("Result", result);
                        resultmap.put("message", message);

                        resultsss.add(resultmap);
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

            if (resultmap.get("Result").equals("0")) {
                // Toast.makeText(ConfirmationFundingActivity.this, resultmap.get("message"), Toast.LENGTH_SHORT).show();
            } else {
                if (user_save.get("loan").equals("0")) {
                    Intent intent = new Intent(ConfirmationFundingActivity.this, FundingActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ConfirmationFundingActivity.this, LoanActivity.class);
                    startActivity(intent);
                }
                //  Toast.makeText(ConfirmationFundingActivity.this, resultmap.get("message"), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
