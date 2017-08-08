package com.falconnect.dealermanagementsystem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;

import com.falconnect.dealermanagementsystem.Adapter.MyPostingDetailsCustomAdapter;
import com.falconnect.dealermanagementsystem.Adapter.MyPostingDetailsListViewAdapter;
import com.falconnect.dealermanagementsystem.Model.MyPostingDetailsDataModel;
import com.falconnect.dealermanagementsystem.Model.MyPostingDetailsListViewModel;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MyPostingDetailsActivity extends Activity {

    private static RecyclerView.Adapter adapter;
    private static ArrayList<MyPostingDetailsDataModel> data;
    public ArrayList<HashMap<String, String>> header_list;
    public ArrayList<HashMap<String, String>> listviewheader_list;
    RecyclerView my_recycler_posting_header;
    ArrayList<String> ar = new ArrayList<String>();
    SessionManager session;
    HashMap<String, String> user;
    String carid;
    HashMap<String, String> headerlist;
    HashMap<String, String> listviewheaderlist;
    ListView listView;
    ImageView posting_details_back;
    ProgressDialog barProgressDialog;
    MyPostingDetailsListViewAdapter myPostingListviewAdapter;
    private boolean mVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_posting_details);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mVisible = true;

        posting_details_back = (ImageView) findViewById(R.id.posting_details_back);

        session = new SessionManager(MyPostingDetailsActivity.this);
        user = session.getUserDetails();
        carid = getIntent().getStringExtra("carID");
        new listview_data().execute();

        new header_recycle_data().execute();

        posting_details_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyPostingDetailsActivity.this.finish();
            }
        });

    }

    private ArrayList<MyPostingDetailsListViewModel> getmypostingData() {
        final ArrayList<MyPostingDetailsListViewModel> mypostdata = new ArrayList<>();

        for (int i = 0; i < listviewheader_list.size(); i++) {

            String imageurl = listviewheader_list.get(i).get("imageurl");
            String year = listviewheader_list.get(i).get("year");
            String duplicate_id = listviewheader_list.get(i).get("duplicate_id");
            String price = listviewheader_list.get(i).get("price");
            String kms = listviewheader_list.get(i).get("kms");
            String owner = listviewheader_list.get(i).get("owner");
            String fuel_type = listviewheader_list.get(i).get("fuel_type");
            String plan = listviewheader_list.get(i).get("plan");
            String mileage = listviewheader_list.get(i).get("mileage");
            String place = listviewheader_list.get(i).get("place");
            String make = listviewheader_list.get(i).get("make");
            String model = listviewheader_list.get(i).get("model");
            String varient = listviewheader_list.get(i).get("varient");
            String colors = listviewheader_list.get(i).get("colors");
            String seat = listviewheader_list.get(i).get("seat");
            String imagecount = listviewheader_list.get(i).get("imagecount");
            String videoscount = listviewheader_list.get(i).get("videoscount");
            String documentcount = listviewheader_list.get(i).get("documentcount");
            String viewscount = listviewheader_list.get(i).get("viewscount");
            String mongopushdate = listviewheader_list.get(i).get("mongopushdate");
            String listing_id = listviewheader_list.get(i).get("listing_id");
            String car_id = listviewheader_list.get(i).get("car_id");
            String listing_site = listviewheader_list.get(i).get("listing_site");
            String listing_status = listviewheader_list.get(i).get("listing_status");
            String list_image = listviewheader_list.get(i).get("list_image");
            String createddate = listviewheader_list.get(i).get("createddate");

            mypostdata.add(new MyPostingDetailsListViewModel(imageurl, year, duplicate_id, price, kms,
                    owner, fuel_type, plan, mileage, place, make, model, varient, colors, seat, imagecount, videoscount,
                    documentcount, viewscount, mongopushdate, listing_id, car_id, listing_site, listing_status,
                    list_image, createddate));
        }

        return mypostdata;
    }

    public void listview() {

        new listview_data().execute();
    }


    private class header_recycle_data extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();

            String queriesurl = Constant.MY_POSTING_DETAIL_HEADER
                    + "session_user_id=" + user.get("user_id")
                    + "&car_id=" + carid
                    + "&page_name=viewmypostdetails";

            Log.e("queriesurl", queriesurl);

            String json = sh.makeServiceCall(queriesurl, ServiceHandler.POST);

            if (json != null) {

                header_list = new ArrayList<>();
                try {
                    JSONObject jsonObj = new JSONObject(json);

                    JSONArray loan = jsonObj.getJSONArray("myposting_list");

                    for (int k = 0; k <= loan.length(); k++) {

                        String kms = loan.getJSONObject(k).getString("kms");
                        String fuel_type = loan.getJSONObject(k).getString("fuel_type");
                        String mileage = loan.getJSONObject(k).getString("mileage");
                        String place = loan.getJSONObject(k).getString("place");
                        String colors = loan.getJSONObject(k).getString("colors");
                        String seat = loan.getJSONObject(k).getString("seat");

                        headerlist = new HashMap<>();

                        headerlist.put("kms", kms);
                        headerlist.put("fuel_type", fuel_type);
                        headerlist.put("mileage", mileage);
                        headerlist.put("place", place);
                        headerlist.put("colors", colors);
                        headerlist.put("seat", seat);

                        header_list.add(headerlist);

                        ar.add(fuel_type);
                        ar.add(kms);
                        ar.add(mileage);
                        ar.add(seat);
                        ar.add(colors);
                        ar.add(place);

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

            my_recycler_posting_header = (RecyclerView) findViewById(R.id.my_recycler_posting_header);

            my_recycler_posting_header.setHasFixedSize(true);
            my_recycler_posting_header.setLayoutManager(new LinearLayoutManager(MyPostingDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));
            data = new ArrayList<MyPostingDetailsDataModel>();
            for (int i = 0; i < MyPostingHeaderData.headernameArray.length; i++) {

                data.add(new MyPostingDetailsDataModel(
                        ar.get(i),
                        MyPostingHeaderData.headerimageArray[i],
                        MyPostingHeaderData.headerid_[i]
                ));
            }


            adapter = new MyPostingDetailsCustomAdapter(MyPostingDetailsActivity.this, data);
            my_recycler_posting_header.setAdapter(adapter);
        }
    }

    private class listview_data extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = ProgressDialog.show(MyPostingDetailsActivity.this, "Loading...", "Please Wait ...", true);
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();

            String queriesurl = Constant.MY_POSTING_DETAIL_HEADER
                    + "session_user_id=" + user.get("user_id")
                    + "&car_id=" + carid
                    + "&page_name=viewmypostdetails";

            Log.e("queriesurl", queriesurl);

            String json = sh.makeServiceCall(queriesurl, ServiceHandler.POST);

            if (json != null) {

                listviewheader_list = new ArrayList<>();

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    JSONArray loan = jsonObj.getJSONArray("myposting_details");

                    for (int k = 0; k <= loan.length(); k++) {

                        String imageurl = loan.getJSONObject(k).getString("imageurl");
                        String year = loan.getJSONObject(k).getString("year");
                        String duplicate_id = loan.getJSONObject(k).getString("duplicate_id");
                        String price = loan.getJSONObject(k).getString("price");
                        String kms = loan.getJSONObject(k).getString("kms");
                        String owner = loan.getJSONObject(k).getString("owner");
                        String fuel_type = loan.getJSONObject(k).getString("fuel_type");
                        String plan = loan.getJSONObject(k).getString("plan");
                        String mileage = loan.getJSONObject(k).getString("mileage");
                        String place = loan.getJSONObject(k).getString("place");
                        String make = loan.getJSONObject(k).getString("make");
                        String model = loan.getJSONObject(k).getString("model");
                        String varient = loan.getJSONObject(k).getString("varient");
                        String colors = loan.getJSONObject(k).getString("colors");
                        String seat = loan.getJSONObject(k).getString("seat");
                        String imagecount = loan.getJSONObject(k).getString("imagecount");
                        String videoscount = loan.getJSONObject(k).getString("videoscount");
                        String documentcount = loan.getJSONObject(k).getString("documentcount");
                        String viewscount = loan.getJSONObject(k).getString("viewscount");
                        String mongopushdate = loan.getJSONObject(k).getString("mongopushdate");
                        String listing_id = loan.getJSONObject(k).getString("listing_id");
                        String car_id = loan.getJSONObject(k).getString("car_id");
                        String listing_site = loan.getJSONObject(k).getString("listing_site");
                        String listing_status = loan.getJSONObject(k).getString("listing_status");
                        String list_image = loan.getJSONObject(k).getString("list_image");
                        String createddate = loan.getJSONObject(k).getString("createddate");

                        listviewheaderlist = new HashMap<>();

                        listviewheaderlist.put("imageurl", imageurl);
                        listviewheaderlist.put("year", year);
                        listviewheaderlist.put("duplicate_id", duplicate_id);
                        listviewheaderlist.put("price", price);
                        listviewheaderlist.put("kms", kms);
                        listviewheaderlist.put("owner", owner);
                        listviewheaderlist.put("fuel_type", fuel_type);
                        listviewheaderlist.put("plan", plan);
                        listviewheaderlist.put("mileage", mileage);
                        listviewheaderlist.put("place", place);
                        listviewheaderlist.put("make", make);
                        listviewheaderlist.put("model", model);
                        listviewheaderlist.put("varient", varient);
                        listviewheaderlist.put("colors", colors);
                        listviewheaderlist.put("seat", seat);
                        listviewheaderlist.put("imagecount", imagecount);
                        listviewheaderlist.put("videoscount", videoscount);
                        listviewheaderlist.put("documentcount", documentcount);
                        listviewheaderlist.put("viewscount", viewscount);
                        listviewheaderlist.put("mongopushdate", mongopushdate);
                        listviewheaderlist.put("listing_id", listing_id);
                        listviewheaderlist.put("car_id", car_id);
                        listviewheaderlist.put("listing_site", listing_site);
                        listviewheaderlist.put("listing_status", listing_status);
                        listviewheaderlist.put("list_image", list_image);
                        listviewheaderlist.put("createddate", createddate);

                        listviewheader_list.add(listviewheaderlist);


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

            listView = (ListView) findViewById(R.id.posting_details);

            myPostingListviewAdapter = new MyPostingDetailsListViewAdapter(MyPostingDetailsActivity.this, getmypostingData());
            listView.setAdapter(myPostingListviewAdapter);


        }
    }


}
