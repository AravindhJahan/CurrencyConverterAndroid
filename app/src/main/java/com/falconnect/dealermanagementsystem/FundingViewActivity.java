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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.falconnect.dealermanagementsystem.Adapter.FundingDetailListViewAdapter;
import com.falconnect.dealermanagementsystem.FontAdapter.RoundImageTransform;
import com.falconnect.dealermanagementsystem.Model.FundingDataModel;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FundingViewActivity extends AppCompatActivity {


    private static RecyclerView.Adapter adapter;
    private static ArrayList<FundingDataModel> data;
    public ArrayList<HashMap<String, String>> revoke_fund_list;
    TextView header;
    Button revoke_fund;
    ImageView fund_image;
    SessionManager session;
    HashMap<String, String> user;
    HashMap<String, String> revokefundlist;
    String result, message;
    ListView funding_details;
    String list_funding1, list_funding2, list_funding3, list_funding4, list_funding5, list_funding6;
    ArrayList<String> getList;
    List<String> titles;
    String ticket_id;
    FundingDetailListViewAdapter fundingDetailListViewAdapter;
    private boolean mVisible;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_funding_view);

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

        session = new SessionManager(FundingViewActivity.this);
        user = session.getUserDetails();


        ticket_id = getIntent().getStringExtra("ticket_id");

        list_funding1 = getIntent().getStringExtra("list_funding1");
        list_funding2 = getIntent().getStringExtra("list_funding2");
        list_funding3 = getIntent().getStringExtra("list_funding3");
        list_funding4 = getIntent().getStringExtra("list_funding4");
        list_funding5 = getIntent().getStringExtra("list_funding5");
        list_funding6 = getIntent().getStringExtra("list_funding6");

        getList = new ArrayList<>();
        getList.add(list_funding1);
        getList.add(list_funding2);
        getList.add(list_funding3);
        getList.add(list_funding4);
        getList.add(list_funding5);
        getList.add(list_funding6);

        titles = new ArrayList<>();

        titles.add("Branch");
        titles.add("Ticket ID");
        titles.add("Mobile No");
        titles.add("City");
        titles.add("Status");
        titles.add("Funding ID");


        revoke_fund = (Button) findViewById(R.id.revoke_funing);


        funding_details = (ListView) findViewById(R.id.funding_details);

        fundingDetailListViewAdapter = new FundingDetailListViewAdapter(FundingViewActivity.this, getList, titles);
        funding_details.setAdapter(fundingDetailListViewAdapter);

        ImageView funding_view_back = (ImageView) findViewById(R.id.funding_view_back);

        funding_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FundingViewActivity.this.finish();
            }
        });


        fund_image = (ImageView) findViewById(R.id.cholo_images);


        Glide.with(getApplicationContext())
                .load(R.drawable.chola)
                .transform(new RoundImageTransform(FundingViewActivity.this))
                .into(fund_image);

        revoke_fund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new revoke_funding_buy().execute();
            }
        });

        if (list_funding5.equals("Revoked")) {
            revoke_fund.setVisibility(View.GONE);

        } else if (list_funding5.equals("Decline")) {
            revoke_fund.setVisibility(View.GONE);

        } else if (list_funding5.equals("Approved")) {

            revoke_fund.setVisibility(View.GONE);
        } else {

            revoke_fund.setVisibility(View.VISIBLE);
        }
    }


    private class revoke_funding_buy extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();

            String revoke_buy_funding = Constant.REVOKE_FUNDING_BUY
                    + "session_user_id=" + user.get("user_id")
                    + "&fundingid=" + ticket_id;

            Log.e("revoke_buy_funding", revoke_buy_funding);

            String json = sh.makeServiceCall(revoke_buy_funding, ServiceHandler.POST);

            if (json != null) {

                revoke_fund_list = new ArrayList<>();

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    for (int k = 0; k <= jsonObj.length(); k++) {

                        result = jsonObj.getString("Result");
                        message = jsonObj.getString("message");

                        revokefundlist = new HashMap<>();

                        revokefundlist.put("Result", result);
                        revokefundlist.put("message", message);

                        revoke_fund_list.add(revokefundlist);

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

            if (revokefundlist.get("Result").equals("1")) {
                Toast.makeText(FundingViewActivity.this, revokefundlist.get("message"), Toast.LENGTH_SHORT).show();
                FundingViewActivity.this.finish();

            } else {
                Toast.makeText(FundingViewActivity.this, revokefundlist.get("message"), Toast.LENGTH_SHORT).show();

            }
        }
    }
}
