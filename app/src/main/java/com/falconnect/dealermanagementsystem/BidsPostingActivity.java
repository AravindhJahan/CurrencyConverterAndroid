package com.falconnect.dealermanagementsystem;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.falconnect.dealermanagementsystem.Adapter.BidsPostingListAdapter;
import com.falconnect.dealermanagementsystem.Adapter.CustomAdapter;
import com.falconnect.dealermanagementsystem.FontAdapter.RoundImageTransform;
import com.falconnect.dealermanagementsystem.Model.BidsPostingListModel;
import com.falconnect.dealermanagementsystem.Model.DataModel;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class BidsPostingActivity extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private static RecyclerView applyfundingrecyclerView;
    private static ArrayList<DataModel> data;
    public ArrayList<HashMap<String, String>> bids_posting_list;
    HashMap<String, String> user;
    SessionManager session;
    ImageView bids_posting_back;
    BidsPostingListAdapter bidsPostingListAdapter;
    String car_id_get, car_image_get;
    ListView bidsposting_listview;
    HashMap<String, String> bidspositinglist;
    TextView rank_postion;
    ImageView carimage, profileimage;
    String car_name, dealer_id;
    EditText bids_posting_textbox;
    Button bids_posting_button;
    TextView car_name_display, owner_name;
    String amount;
    private boolean mVisible;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bids_posting);


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

        applyfundingrecyclerView = (RecyclerView) findViewById(R.id.my_recycler_bids_posting);
        applyfundingrecyclerView.setHasFixedSize(true);
        applyfundingrecyclerView.setLayoutManager(new LinearLayoutManager(BidsPostingActivity.this, LinearLayoutManager.HORIZONTAL, false));

        data = new ArrayList<DataModel>();
        for (int i = 0; i < MyData.nameArray.length; i++) {
            data.add(new DataModel(
                    MyData.nameArray[i],
                    MyData.id_[i],
                    MyData.drawableArrayWhite3[i]
            ));
        }

        adapter = new CustomAdapter(BidsPostingActivity.this, data);
        applyfundingrecyclerView.setAdapter(adapter);

        bids_posting_back = (ImageView) findViewById(R.id.bids_posting_back);

        bids_posting_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BidsPostingActivity.this.finish();
            }
        });


        car_id_get = getIntent().getStringExtra("car_id");
        car_image_get = getIntent().getStringExtra("car_image");
        car_name = getIntent().getStringExtra("car_name");
        dealer_id = getIntent().getStringExtra("dealer_id");

        carimage = (ImageView) findViewById(R.id.bids_posted_car_image);
        profileimage = (ImageView) findViewById(R.id.user_profile_image);
        car_name_display = (TextView) findViewById(R.id.car_name_above_line);
        owner_name = (TextView) findViewById(R.id.car_owner_name_above_line2);

        session = new SessionManager(BidsPostingActivity.this);
        user = session.getUserDetails();

        Glide.with(getApplicationContext())
                .load(user.get("dealer_img"))
                .transform(new RoundImageTransform(BidsPostingActivity.this))
                .placeholder(R.drawable.default_avatar)
                .into(profileimage);

        Glide.with(getApplicationContext())
                .load(car_image_get)
                .transform(new RoundImageTransform(BidsPostingActivity.this))
                .placeholder(R.drawable.defaultcar)
                .into(carimage);

        car_name_display.setText(car_name.toString());
        owner_name.setText(user.get("dealer_name"));

        bids_posting_textbox = (EditText) findViewById(R.id.bids_posting_textbox);
        bids_posting_button = (Button) findViewById(R.id.bids_posting_button);


        bids_posting_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                amount = bids_posting_textbox.getText().toString();
                new Bid_Amount_new().execute();

            }
        });
        new BidsPosting_Data().execute();
    }
    private ArrayList<BidsPostingListModel> getbidspostingdata() {
        final ArrayList<BidsPostingListModel> bidposteddata = new ArrayList<>();
        for (int i = 0; i < bids_posting_list.size(); i++) {
            String result = bids_posting_list.get(i).get("result");
            String message = bids_posting_list.get(i).get("message");
            String position = bids_posting_list.get(i).get("Position");
            String dealername = bids_posting_list.get(i).get("Dealername");
            String amount = bids_posting_list.get(i).get("Amount");
            String date = bids_posting_list.get(i).get("Date");
            bidposteddata.add(new BidsPostingListModel(result, message, position, dealername, amount, date));
        }
        return bidposteddata;
    }

    private class BidsPosting_Data extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();

            String bids_postingurl = Constant.BIDS_POSTING_LISTVIEW_API + "session_user_id=" + user.get("user_id") +
                    "&car_id=" + car_id_get.toString();

            String json = sh.makeServiceCall(bids_postingurl, ServiceHandler.POST);

            if (json != null) {

                bids_posting_list = new ArrayList<>();

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    String result = jsonObj.getString("Result");
                    String message = jsonObj.getString("message");
                    if (result.equals("0")) {
                        bidspositinglist = new HashMap<>();
                        bidspositinglist.put("result", result);
                        bidspositinglist.put("message", message);
                        bids_posting_list.add(bidspositinglist);
                    } else {
                        String position = jsonObj.getString("Position");
                        JSONArray bids = jsonObj.getJSONArray("dealer_bid_list");
                        for (int k = 0; k <= bids.length(); k++) {
                            String dealer_id = bids.getJSONObject(k).getString("Dealername");
                            String car_id = bids.getJSONObject(k).getString("Amount");
                            String noimages = bids.getJSONObject(k).getString("Date");

                            bidspositinglist = new HashMap<>();

                            bidspositinglist.put("result", result);
                            bidspositinglist.put("message", message);
                            bidspositinglist.put("Position", position);
                            bidspositinglist.put("Dealername", dealer_id);
                            bidspositinglist.put("Amount", car_id);
                            bidspositinglist.put("Date", noimages);

                            bids_posting_list.add(bidspositinglist);

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
            bidsposting_listview = (ListView) findViewById(R.id.bids_posting_list);
            bidsPostingListAdapter = new BidsPostingListAdapter(BidsPostingActivity.this, getbidspostingdata());
            bidsposting_listview.setAdapter(bidsPostingListAdapter);
            bidsposting_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    BidsPostingListModel bidsPostingListModelnew = (BidsPostingListModel) parent.getItemAtPosition(position);

                    Toast.makeText(BidsPostingActivity.this, bidsPostingListModelnew.getAmount(), Toast.LENGTH_SHORT).show();

                }
            });

            rank_postion = (TextView) findViewById(R.id.rank_postion);
            rank_postion.setText(bidspositinglist.get("Position"));

        }

    }


    private class Bid_Amount_new extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();

            String bids_postingurl = Constant.NEW_BID_POSTING_AMOUNT_API +
                    "session_user_id=" + user.get("user_id") +
                    "&biddingamount=" + amount +
                    "&car_id=" + car_id_get+
                    "&dealerid=" + dealer_id;

            Log.e("bids_postingurl", bids_postingurl);

            String json = sh.makeServiceCall(bids_postingurl, ServiceHandler.POST);

            if (json != null) {

                bids_posting_list = new ArrayList<>();

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    String result = jsonObj.getString("Result");
                    String message = jsonObj.getString("message");
                    if (result.equals("0")) {
                        bidspositinglist = new HashMap<>();
                        bidspositinglist.put("result", result);
                        bidspositinglist.put("message", message);
                        bids_posting_list.add(bidspositinglist);
                    } else {
                        bidspositinglist = new HashMap<>();
                        bidspositinglist.put("result", result);
                        bidspositinglist.put("message", message);
                        String position = jsonObj.getString("Position");
                        JSONArray bids = jsonObj.getJSONArray("dealer_bid_list");
                        for (int k = 0; k <= bids.length(); k++) {
                            String dealer_id = bids.getJSONObject(k).getString("Dealername");
                            String car_id = bids.getJSONObject(k).getString("Amount");
                            String noimages = bids.getJSONObject(k).getString("Date");

                            bidspositinglist = new HashMap<>();

                            bidspositinglist.put("result", result);
                            bidspositinglist.put("message", message);
                            bidspositinglist.put("Position", position);
                            bidspositinglist.put("Dealername", dealer_id);
                            bidspositinglist.put("Amount", car_id);
                            bidspositinglist.put("Date", noimages);

                            bids_posting_list.add(bidspositinglist);

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
            bidsposting_listview = (ListView) findViewById(R.id.bids_posting_list);

            if (bidspositinglist.get("result").equals("0")) {
               Toast.makeText(BidsPostingActivity.this,
                       bidspositinglist.get("message") + " " + amount,
                       Toast.LENGTH_SHORT).show();
            } else {
                bidsPostingListAdapter = new BidsPostingListAdapter(BidsPostingActivity.this, getbidspostingdata());
                bidsposting_listview.setAdapter(bidsPostingListAdapter);
                bidsposting_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        BidsPostingListModel bidsPostingListModelnew = (BidsPostingListModel) parent.getItemAtPosition(position);
                        //  Toast.makeText(BidsPostingActivity.this, bidsPostingListModelnew.getAmount(), Toast.LENGTH_SHORT).show();

                    }
                });

                rank_postion = (TextView) findViewById(R.id.rank_postion);
                rank_postion.setText(bidspositinglist.get("Position"));
            }
        }

    }


}
