package com.falconnect.dealermanagementsystem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.falconnect.dealermanagementsystem.Adapter.BussinessProfileListAdapter;
import com.falconnect.dealermanagementsystem.Model.BussinessProfileModel;
import com.falconnect.dealermanagementsystem.SharedPreference.PlandetailsSharedPreferences;
import com.falconnect.dealermanagementsystem.SharedPreference.ProfileManagerSession;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DealerBussinessProfile extends Activity {

    public ArrayList<HashMap<String, String>> bussiness_profile_list;
    public ArrayList<HashMap<String, String>> bussiness_profile_car_list;
    ImageView backs;
    String dealer_id;
    String dealer_name;
    TextView dealer_name_text;
    ArrayList<String> bussiness_details = new ArrayList<>();
    HashMap<String, String> bussinessprofilelist;
    HashMap<String, String> bussinessprofilecarlist;
    SessionManager session;
    List<NameValuePair> params;
    JSONArray business_index = null;
    HashMap<String, String> user;
    ImageView cover_photo, profile_photo;
    ImageView facebook_share_image, twitter_share_image, lkendin_share_image, lkendin_web_image;
    TextView dealer_plus_manager, dealer_plus_manager_address;

    ImageView verified_img;

    TextView landline;


    BussinessProfileListAdapter bussinessProfileListAdapter;
    ListView bussiness_profile_car_details;
    ProgressDialog barProgressDialog;
    ProfileManagerSession profileManagerSession;
    PlandetailsSharedPreferences plandetailsSharedPreferences;
    HashMap<String, String> plan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dealer_bussiness_profile);

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        dealer_id = getIntent().getStringExtra("dealer_id");
        //dealer_name = getIntent().getStringExtra("dealet_name");

        session = new SessionManager(DealerBussinessProfile.this);
        user = session.getUserDetails();
        bussiness_profile_car_details = (ListView) findViewById(R.id.bussiness_profile_car_details);
        backs = (ImageView) findViewById(R.id.bussiness_profile_mnav);

        View header = getLayoutInflater().inflate(R.layout.dealerbussiness_header, null);
        bussiness_profile_car_details.addHeaderView(header);


        cover_photo = (ImageView) header.findViewById(R.id.cover_photo);
        profile_photo = (ImageView) header.findViewById(R.id.profile_photo);

        facebook_share_image = (ImageView) header.findViewById(R.id.facebook_share_image);
        twitter_share_image = (ImageView) header.findViewById(R.id.twitter_share_image);
        lkendin_share_image = (ImageView) header.findViewById(R.id.lkendin_share_image);
        lkendin_web_image = (ImageView) header.findViewById(R.id.lkendin_web_image);

        dealer_plus_manager = (TextView) header.findViewById(R.id.dealer_plus_manager);
        dealer_plus_manager_address = (TextView) header.findViewById(R.id.dealer_plus_manager_address);

        verified_img =(ImageView) header.findViewById(R.id.verified_img);
        landline=(TextView) header.findViewById(R.id.dealer_plus_manager_landline);

        backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DealerBussinessProfile.this.finish();
            }
        });

        if (isNetworkAvailable()) {
            new bussiness_profile().execute();
        } else {

            Toast.makeText(DealerBussinessProfile.this, "Network Problem", Toast.LENGTH_SHORT).show();
            DealerBussinessProfile.this.finish();
        }

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

   /* @Override
    public void onBackPressed() {

        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to exit application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        DealerBussinessProfile.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();

    }*/

    private ArrayList<BussinessProfileModel> getbidsdata() {
        final ArrayList<BussinessProfileModel> biddata = new ArrayList<>();
        for (int i = 0; i < bussiness_profile_car_list.size(); i++) {

            String image = bussiness_profile_car_list.get(i).get("image");
            String car_id = bussiness_profile_car_list.get(i).get("car_id");
            String listing_id = bussiness_profile_car_list.get(i).get("listing_id");
            String inventory_type = bussiness_profile_car_list.get(i).get("inventory_type");
            String car_locality = bussiness_profile_car_list.get(i).get("car_locality");
            String dealer_id = bussiness_profile_car_list.get(i).get("dealer_id");
            String price = bussiness_profile_car_list.get(i).get("price");
            String kms_done = bussiness_profile_car_list.get(i).get("kms_done");
            String registration_year = bussiness_profile_car_list.get(i).get("registration_year");
            String owner_type = bussiness_profile_car_list.get(i).get("owner_type");
            String make = bussiness_profile_car_list.get(i).get("make");
            String model = bussiness_profile_car_list.get(i).get("model");
            String varient = bussiness_profile_car_list.get(i).get("varient");
            String fuel_type = bussiness_profile_car_list.get(i).get("fuel_type");
            //String statuc_number = bussiness_profile_car_list.get(i).get("statuc_number");
            String imagecount = bussiness_profile_car_list.get(i).get("imagecount");
            String days = bussiness_profile_car_list.get(i).get("days");

            biddata.add(new BussinessProfileModel(image, car_id, listing_id, inventory_type, car_locality, dealer_id, price, kms_done, registration_year,
                    owner_type, make, model, varient, fuel_type, imagecount, days));
        }
        return biddata;
    }

    private class bussiness_profile extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = ProgressDialog.show(DealerBussinessProfile.this, "Loading...", "Please Wait ...", true);
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();

            String url = Constant.BUSSINESSPROFILE + "session_user_id=" + dealer_id + "&page_name=viewbusinessprofile";

            String json = sh.makeServiceCall(url, ServiceHandler.POST, params);

            Log.e("json", json);

            if (json != null) {

                bussiness_profile_list = new ArrayList<>();

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    business_index = jsonObj.getJSONArray("business_index");
                    for (int i = 0; i < business_index.length(); i++) {
                        String d_id = business_index.getJSONObject(i).getString("d_id");
                        String parent_id = business_index.getJSONObject(i).getString("parent_id");
                        String dealer_name = business_index.getJSONObject(i).getString("dealer_name");
                        String dealership_name = business_index.getJSONObject(i).getString("dealership_name");
                        String d_email = business_index.getJSONObject(i).getString("d_email");
                        String d_mobile = business_index.getJSONObject(i).getString("d_mobile");
                        String line_of_business = business_index.getJSONObject(i).getString("line_of_business");
                        String dealership_website = business_index.getJSONObject(i).getString("dealership_website");
                        String facebook_link = business_index.getJSONObject(i).getString("facebook_link");
                        String twitter_link = business_index.getJSONObject(i).getString("twitter_link");
                        String linkedin_link = business_index.getJSONObject(i).getString("linkedin_link");
                        String company_logo = business_index.getJSONObject(i).getString("company_logo");
                        String coverphoto_logo = business_index.getJSONObject(i).getString("coverphoto_logo");
                        String company_doc = business_index.getJSONObject(i).getString("company_doc");
                        String profile_name = business_index.getJSONObject(i).getString("profile_name");
                        String business_domain = business_index.getJSONObject(i).getString("business_domain");
                        String about_us = business_index.getJSONObject(i).getString("about_us");
                        String landline_no = business_index.getJSONObject(i).getString("landline_no");
                        String fax_no = business_index.getJSONObject(i).getString("fax_no");
                        String company_status = business_index.getJSONObject(i).getString("company_status");
                        String dealership_started = business_index.getJSONObject(i).getString("dealership_started");
                        String pan_no = business_index.getJSONObject(i).getString("pan_no");
                        String verified = business_index.getJSONObject(i).getString("verified");

                        bussinessprofilelist = new HashMap<>();

                        bussinessprofilelist.put("d_id", d_id);
                        bussinessprofilelist.put("parent_id", parent_id);
                        bussinessprofilelist.put("dealer_name", dealer_name);
                        bussinessprofilelist.put("dealership_name", dealership_name);
                        bussinessprofilelist.put("d_email", d_email);
                        bussinessprofilelist.put("d_mobile", d_mobile);
                        bussinessprofilelist.put("line_of_business", line_of_business);
                        bussinessprofilelist.put("dealership_website", dealership_website);
                        bussinessprofilelist.put("facebook_link", facebook_link);
                        bussinessprofilelist.put("twitter_link", twitter_link);
                        bussinessprofilelist.put("linkedin_link", linkedin_link);
                        bussinessprofilelist.put("company_logo", company_logo);
                        bussinessprofilelist.put("coverphoto_logo", coverphoto_logo);
                        bussinessprofilelist.put("company_doc", company_doc);
                        bussinessprofilelist.put("profile_name", profile_name);
                        bussinessprofilelist.put("business_domain", business_domain);
                        bussinessprofilelist.put("about_us", about_us);
                        bussinessprofilelist.put("landline_no", landline_no);
                        bussinessprofilelist.put("fax_no", fax_no);
                        bussinessprofilelist.put("company_status", company_status);
                        bussinessprofilelist.put("dealership_started", dealership_started);
                        bussinessprofilelist.put("pan_no", pan_no);
                        bussinessprofilelist.put("verified", verified);

                        bussiness_profile_list.add(bussinessprofilelist);

                        bussiness_details.add(dealership_name);
                        bussiness_details.add(dealer_name);
                        bussiness_details.add(d_mobile);
                        bussiness_details.add(d_email);
                    }

                    JSONArray city = jsonObj.getJSONArray("my_cardetails");

                    bussiness_profile_car_list = new ArrayList<>();

                    for (int k = 0; k <= city.length(); k++) {

                        String image = city.getJSONObject(k).getString("image");
                        String car_id = city.getJSONObject(k).getString("car_id");
                        String listing_id = city.getJSONObject(k).getString("listing_id");
                        String inventory_type = city.getJSONObject(k).getString("inventory_type");
                        String car_locality = city.getJSONObject(k).getString("car_locality");
                        String dealer_id = city.getJSONObject(k).getString("dealer_id");
                        String price = city.getJSONObject(k).getString("price");
                        String kms_done = city.getJSONObject(k).getString("kms_done");
                        String registration_year = city.getJSONObject(k).getString("registration_year");
                        String owner_type = city.getJSONObject(k).getString("owner_type");
                        String make = city.getJSONObject(k).getString("make");
                        String model = city.getJSONObject(k).getString("model");
                        String varient = city.getJSONObject(k).getString("varient");
                        String fuel_type = city.getJSONObject(k).getString("fuel_type");
                        //String statuc_number = city.getJSONObject(k).getString("statuc_number");
                        String imagecount = city.getJSONObject(k).getString("imagecount");
                        String days = city.getJSONObject(k).getString("days");

                        bussinessprofilecarlist = new HashMap<>();

                        bussinessprofilecarlist.put("image", image);
                        bussinessprofilecarlist.put("car_id", car_id);
                        bussinessprofilecarlist.put("listing_id", listing_id);
                        bussinessprofilecarlist.put("inventory_type", inventory_type);
                        bussinessprofilecarlist.put("car_locality", car_locality);
                        bussinessprofilecarlist.put("dealer_id", dealer_id);
                        bussinessprofilecarlist.put("price", price);
                        bussinessprofilecarlist.put("kms_done", kms_done);
                        bussinessprofilecarlist.put("registration_year", registration_year);
                        bussinessprofilecarlist.put("owner_type", owner_type);
                        bussinessprofilecarlist.put("make", make);
                        bussinessprofilecarlist.put("model", model);
                        bussinessprofilecarlist.put("varient", varient);
                        bussinessprofilecarlist.put("fuel_type", fuel_type);
                        //bussinessprofilecarlist.put("statuc_number", statuc_number);
                        bussinessprofilecarlist.put("imagecount", imagecount);
                        bussinessprofilecarlist.put("days", days);

                        bussiness_profile_car_list.add(bussinessprofilecarlist);


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

            bussinessProfileListAdapter = new BussinessProfileListAdapter(DealerBussinessProfile.this, getbidsdata());
            bussiness_profile_car_details.setAdapter(bussinessProfileListAdapter);

            String length = String.valueOf(bussinessProfileListAdapter.getCount());

            Glide.with(getApplication())
                    .load(bussinessprofilelist.get("company_logo"))
                    .placeholder(R.drawable.carimageplaceholder)
                    .into(profile_photo);

            Glide.with(getApplication())
                    .load(bussinessprofilelist.get("coverphoto_logo"))
                    .placeholder(R.drawable.carimageplaceholder)
                    .into(cover_photo);

            dealer_plus_manager.setText(bussinessprofilelist.get("dealer_name") + " (" + length.toString() + " Cars)");
            dealer_plus_manager_address.setText(bussinessprofilelist.get("d_mobile") + " - " +
                    bussinessprofilelist.get("d_email"));

            if (bussinessprofilelist.get("verified").toString() == "0")
            {
                verified_img.setVisibility(View.GONE);
            }
            else
            {
                verified_img.setVisibility(View.VISIBLE);
            }

            if (bussinessprofilelist.get("landline_no").toString()==null) {
                landline.setVisibility(View.GONE);
            }else
            {
                landline.setVisibility(View.VISIBLE);
                landline.setText(bussinessprofilelist.get("landline_no").toString());
            }

            facebook_share_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = bussinessprofilelist.get("facebook_link").toString();
                    Intent intent = new Intent(DealerBussinessProfile.this, WebViewActivity.class);
                    intent.putExtra("title", "Facebook");
                    intent.putExtra("url", url);
                    startActivity(intent);
                }
            });

            twitter_share_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = bussinessprofilelist.get("twitter_link").toString();
                    Intent intent = new Intent(DealerBussinessProfile.this, WebViewActivity.class);
                    intent.putExtra("title", "Twitter");
                    intent.putExtra("url", url);
                    startActivity(intent);


                }
            });

            lkendin_share_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = bussinessprofilelist.get("linkedin_link").toString();
                    Intent intent = new Intent(DealerBussinessProfile.this, WebViewActivity.class);
                    intent.putExtra("title", "LinkedIn");
                    intent.putExtra("url", url);
                    startActivity(intent);

                }
            });

            lkendin_web_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = bussinessprofilelist.get("dealership_website").toString();
                    Intent intent = new Intent(DealerBussinessProfile.this, WebViewActivity.class);
                    intent.putExtra("title", "Dealer Website");
                    intent.putExtra("url", url);
                    startActivity(intent);
                }
            });


            bussiness_profile_car_details.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    BussinessProfileModel item = (BussinessProfileModel) parent.getItemAtPosition(position);
                    if (item == null) {

                    } else {
                        String url = ConstantIP.IP + "mobileweb/carview/index.html#/carview/" + user.get("user_id") + "/" + item.getCar_id() + "/0";
                        Intent intent = new Intent(DealerBussinessProfile.this, SellWebViewActivity.class);
                        intent.putExtra("title", "Car Details");
                        intent.putExtra("url", url);
                        startActivity(intent);
                    }
                    // Toast.makeText(BussinessProfileActivity.this, "Selected Car Name :" + item.getMake(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}
