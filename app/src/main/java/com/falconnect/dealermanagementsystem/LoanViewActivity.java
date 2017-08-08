package com.falconnect.dealermanagementsystem;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.falconnect.dealermanagementsystem.Adapter.LoanListView;
import com.falconnect.dealermanagementsystem.Model.FundingDataModel;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class LoanViewActivity extends AppCompatActivity {


    private static ArrayList<FundingDataModel> sellfooterdata;
    private static RecyclerView.Adapter selladapter;
    public ArrayList<HashMap<String, String>> revoke_list;
    ImageView loan_view_back_btn;
    Button revoke;
    SessionManager session;
    HashMap<String, String> user;
    HashMap<String, String> revokelist;
    String result, message;
    ListView list_view_loan;

    String list_image;
    LoanListView loanListView;
    ArrayList<String> list_details = new ArrayList<>();
    ArrayList<String> list_details_title = new ArrayList<>();
    String status;
    private boolean mVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_loan_view);
        mVisible = true;
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
        session = new SessionManager(LoanViewActivity.this);
        user = session.getUserDetails();

        list_details = getIntent().getStringArrayListExtra("list_details");
        list_image = getIntent().getStringExtra("list_image");
        status = getIntent().getStringExtra("status");


        loan_view_back_btn = (ImageView) findViewById(R.id.loan_view_back_btn);
        loan_view_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoanViewActivity.this.finish();
            }
        });


        list_details_title.add("Loan ID");
        list_details_title.add("Name");
        list_details_title.add("Mobile Number");
        list_details_title.add("PAN Number");
        list_details_title.add("Email ID");
        list_details_title.add("Vehicle Details");
        list_details_title.add("Date");
        list_details_title.add("Amount");


        list_view_loan = (ListView) findViewById(R.id.loan_details_view);
        loanListView = new LoanListView(LoanViewActivity.this, list_details_title, list_details);
        list_view_loan.setAdapter(loanListView);


        revoke = (Button) findViewById(R.id.revoke_button);


        if (status.equals("In Progress")) {

        } else if (status.equals("Completed")) {
            revoke.setVisibility(View.GONE);
        } else {
            revoke.setVisibility(View.GONE);
        }

        revoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new revoke_sell().execute();
            }
        });
    }

    @Override
    public void onRestart() {
        super.onRestart();
        startActivity(getIntent());
        finish();
    }


    private class revoke_sell extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();
            String newqueriesurl = Constant.REVOKE_SELL
                    + "session_user_id=" + user.get("user_id")
                    + "&loanid=" + list_details.get(0).toString();
            Log.e("Queriesurl" , newqueriesurl);
            String json = sh.makeServiceCall(newqueriesurl, ServiceHandler.POST );
            if (json != null) {
                revoke_list = new ArrayList<>();
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    for (int k = 0; k <= jsonObj.length(); k++) {
                        result = jsonObj.getString("Result");
                        message = jsonObj.getString("message");
                        revokelist = new HashMap<>();
                        revokelist.put("Result", result);
                        revokelist.put("message", message);
                        revoke_list.add(revokelist);
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
            if (revokelist.get("Result").equals("1")) {
                //Toast.makeText(LoanViewActivity.this, revokelist.get("message"), Toast.LENGTH_SHORT).show();
                LoanViewActivity.this.finish();
            } else {
                // Toast.makeText(LoanViewActivity.this, revokelist.get("message"), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
