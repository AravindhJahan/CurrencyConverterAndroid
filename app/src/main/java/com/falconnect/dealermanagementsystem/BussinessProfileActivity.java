package com.falconnect.dealermanagementsystem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.falconnect.dealermanagementsystem.Adapter.BussinessProfileListAdapter;
import com.falconnect.dealermanagementsystem.Adapter.CustomList;
import com.falconnect.dealermanagementsystem.Adapter.ManageFooterCustomAdapter;
import com.falconnect.dealermanagementsystem.FontAdapter.RoundImageTransform;
import com.falconnect.dealermanagementsystem.Model.BussinessProfileModel;
import com.falconnect.dealermanagementsystem.Model.ManageFooterDataModel;
import com.falconnect.dealermanagementsystem.NavigationDrawer.BuyPageNavigation;
import com.falconnect.dealermanagementsystem.SharedPreference.PlandetailsSharedPreferences;
import com.falconnect.dealermanagementsystem.SharedPreference.ProfileManagerSession;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;
import com.freshdesk.hotline.FaqOptions;
import com.freshdesk.hotline.Hotline;
import com.navdrawer.SimpleSideDrawer;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BussinessProfileActivity extends Activity {

    public static String encodedImage, encodedImage_profile;
    public static String imagebase64, imagebase64_profile;
    private static RecyclerView.Adapter bussinessadapter;
    private static RecyclerView bussinessrecycleview;
    private static ArrayList<ManageFooterDataModel> bussinessdata;
    public ArrayList<HashMap<String, String>> bussiness_profile_list;
    public ArrayList<HashMap<String, String>> bussiness_profile_car_list;
    TextView edit_bussiness;
    ArrayList<String> bussiness_details = new ArrayList<>();
    ImageView imageView_branch;
    ImageView verified_img;

    TextView landline;

    TextView dealership_plus_manager;
    TextView profile_name_branch;
    TextView profile_address_manage;
    String saved_name_branch, saved_address_branch;
    BuyPageNavigation bussiness_buypagenavigation;
    HashMap<String, String> bussinessprofilelist;
    HashMap<String, String> bussinessprofilecarlist;
    SessionManager session;
    List<NameValuePair> mainparams;
    List<NameValuePair> params;
    JSONArray business_index = null;
    HashMap<String, String> user;
    ImageView cover_photo, profile_photo;
    ImageView facebook_share_image, twitter_share_image, lkendin_share_image, lkendin_web_image;
    TextView dealer_plus_manager, dealer_plus_manager_address;
    BussinessProfileListAdapter bussinessProfileListAdapter;
    ListView bussiness_profile_car_details;
    boolean clicked = false;
    boolean clicked_profile = false;
    ProgressDialog barProgressDialog;
    ProfileManagerSession profileManagerSession;
    PlandetailsSharedPreferences plandetailsSharedPreferences;
    HashMap<String, String> plan;
    private boolean mVisible;
    private RecyclerView.LayoutManager layoutManager;
    private SimpleSideDrawer mNav_bussiness;
    private int PICK_IMAGE_REQUEST = 1;

    public static String convert(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 10, outputStream);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, outputStream);
        encodedImage = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);

        imagebase64 = encodedImage.toString();

        return encodedImage;
    }

    public static String convert_profile(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 4, outputStream);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 4, outputStream);
        encodedImage_profile = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);

        imagebase64_profile = encodedImage_profile.toString();

        return encodedImage;
    }

    public static Bitmap scaleDown_profile(Bitmap realImage, float maxImageSize, boolean filter) {
        float ratio = Math.min(maxImageSize / realImage.getWidth(),
                maxImageSize / realImage.getHeight());
        int width = Math.round(ratio * realImage.getWidth());
        int height = Math.round(ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);

        convert_profile(newBitmap);

        return newBitmap;
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter) {
        float ratio = Math.min(maxImageSize / realImage.getWidth(),
                maxImageSize / realImage.getHeight());
        int width = Math.round(ratio * realImage.getWidth());
        int height = Math.round(ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);

        convert(newBitmap);

        return newBitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bussiness_profile);

        mVisible = true;
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        plandetailsSharedPreferences = new PlandetailsSharedPreferences(BussinessProfileActivity.this);

        barProgressDialog = ProgressDialog.show(BussinessProfileActivity.this, "Loading...", "Please Wait ...", true);

        bussinessrecycleview = (RecyclerView) findViewById(R.id.my_recycler_bussiness_profile);
        bussinessrecycleview.setHasFixedSize(true);
        bussinessrecycleview.setLayoutManager(new LinearLayoutManager(BussinessProfileActivity.this, LinearLayoutManager.HORIZONTAL, false));

        bussinessdata = new ArrayList<ManageFooterDataModel>();

        for (int i = 0; i < MyFooterManageData.managenameArray.length; i++) {

            bussinessdata.add(new ManageFooterDataModel(
                    MyFooterManageData.managenameArray[i],
                    MyFooterManageData.managedrawableArrayWhite1[i],
                    MyFooterManageData.manageid_[i]
            ));
        }
        bussinessadapter = new ManageFooterCustomAdapter(BussinessProfileActivity.this, bussinessdata);
        bussinessrecycleview.setAdapter(bussinessadapter);

        bussiness_profile_car_details = (ListView) findViewById(R.id.bussiness_profile_car_details);

        session = new SessionManager(BussinessProfileActivity.this);
        user = session.getUserDetails();

        ImageView branch_nav = (ImageView) findViewById(R.id.bussiness_profile_mnav);
        mNav_bussiness = new SimpleSideDrawer(this);
        mNav_bussiness.setLeftBehindContentView(R.layout.activity_behind_left_simple);

        imageView_branch = (ImageView) mNav_bussiness.findViewById(R.id.profile_avatar);
        profile_name_branch = (TextView) mNav_bussiness.findViewById(R.id.profile_name);
        profile_address_manage = (TextView) mNav_bussiness.findViewById(R.id.profile_address);



        user = session.getUserDetails();
        saved_name_branch = user.get("dealer_name");
        saved_address_branch = user.get("dealershipname");

        profile_name_branch.setText(saved_name_branch);
        if (user.get("dealer_img").isEmpty()) {
            Glide.with(getApplicationContext())
                    .load(R.drawable.default_avatar)
                    .transform(new RoundImageTransform(BussinessProfileActivity.this))
                    .into(imageView_branch);
        } else {
            Glide.with(getApplicationContext())
                    .load(user.get("dealer_img"))
                    .transform(new RoundImageTransform(BussinessProfileActivity.this))
                    .into(imageView_branch);
        }
        profile_address_manage.setText(saved_address_branch);

        branch_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNav_bussiness.toggleLeftDrawer();
            }
        });

        RelativeLayout call_layout = (RelativeLayout) mNav_bussiness.findViewById(R.id.call_layout);
        RelativeLayout chat_layout = (RelativeLayout) mNav_bussiness.findViewById(R.id.chat_layout);
        RelativeLayout logout_layout = (RelativeLayout) mNav_bussiness.findViewById(R.id.logout_layout);

        call_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertbox = new AlertDialog.Builder(BussinessProfileActivity.this)
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
                mNav_bussiness.closeLeftSide();
                BussinessProfileActivity.this.finish();
            }
        });

        chat_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chat();
                mNav_bussiness.closeLeftSide();
            }
        });


        //NAVIGATION DRAWER LIST VIEW
        bussiness_buypagenavigation = new BuyPageNavigation();
        CustomList adapter = new CustomList(BussinessProfileActivity.this, BuyPageNavigation.web, BuyPageNavigation.imageId);
        ListView list = (ListView) findViewById(R.id.nav_list_view);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (BuyPageNavigation.web[position] == "Dashboard") {
                    Intent intent = new Intent(BussinessProfileActivity.this, MainDashBoardActivity.class);
                    startActivity(intent);
                    BussinessProfileActivity.this.finish();
                    mNav_bussiness.closeLeftSide();
                    // Toast.makeText(BussinessProfileActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Buy") {
                    Intent intent = new Intent(BussinessProfileActivity.this, DashBoard.class);
                    startActivity(intent);
                    BussinessProfileActivity.this.finish();
                    mNav_bussiness.closeLeftSide();
                    Toast.makeText(BussinessProfileActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Sell") {
                    Intent intent = new Intent(BussinessProfileActivity.this, SellDashBoardActivity.class);
                    startActivity(intent);
                    BussinessProfileActivity.this.finish();
                    mNav_bussiness.closeLeftSide();
                    // Toast.makeText(BussinessProfileActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Manage") {
                    mNav_bussiness.closeLeftSide();
                    profileManagerSession = new ProfileManagerSession(BussinessProfileActivity.this);
                    profileManagerSession.clear_ProfileManage();
                    Intent intent = new Intent(BussinessProfileActivity.this, ManageDashBoardActivity.class);
                    startActivity(intent);
                    //  Toast.makeText(BussinessProfileActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Funding") {

                    plan = plandetailsSharedPreferences.getUserDetails();

//                    if (plan.get("PlanName").equals("BASIC")) {
//                        Intent intent = new Intent(BussinessProfileActivity.this, LoanActivity.class);
//                        startActivity(intent);
//                        mNav_bussiness.closeLeftSide();
//                        BussinessProfileActivity.this.finish();
//                    } else {
                        Intent intent = new Intent(BussinessProfileActivity.this, FundingActivity.class);
                        startActivity(intent);
                        mNav_bussiness.closeLeftSide();
                        BussinessProfileActivity.this.finish();
//                    }
                    // Toast.makeText(BussinessProfileActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Networks") {
                    String url = "http://app.dealerplus.in/user_login_cometchat?session_user_id=" + user.get("user_id");
                    Intent intet = new Intent(BussinessProfileActivity.this, NetworksWebviewActivity.class);
                    //intet.putExtra("title", BuyPageNavigation.web[position]);
                    intet.putExtra("url", url);
                    startActivity(intet);
                    mNav_bussiness.closeLeftSide();
                    // Toast.makeText(BussinessProfileActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Reports") {
                    plandetailsSharedPreferences = new PlandetailsSharedPreferences(BussinessProfileActivity.this);
                    plan = plandetailsSharedPreferences.getUserDetails();
                    if (plan.get("PlanName").equals("BASIC")) {
                        Intent intent = new Intent(BussinessProfileActivity.this, BuyPlanActivity.class);
                        startActivity(intent);
                        //MainDashBoardActivity.this.finish();
                        mNav_bussiness.closeLeftSide();
                    } else {
                        Intent intent = new Intent(BussinessProfileActivity.this, ReportSalesActivity.class);
                        startActivity(intent);
                        BussinessProfileActivity.this.finish();
                        mNav_bussiness.closeLeftSide();
                    }
                    // Toast.makeText(BussinessProfileActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "FAQs") {
                    faqs();
                    mNav_bussiness.closeLeftSide();
                } else {
                    //Toast.makeText(DashBoard.this, web[position], Toast.LENGTH_SHORT).show();
                }
            }
        });

        View header = getLayoutInflater().inflate(R.layout.bussinessheader, null);
        bussiness_profile_car_details.addHeaderView(header);

        cover_photo = (ImageView) header.findViewById(R.id.cover_photo);
        profile_photo = (ImageView) header.findViewById(R.id.profile_photo);

        facebook_share_image = (ImageView) header.findViewById(R.id.facebook_share_image);
        twitter_share_image = (ImageView) header.findViewById(R.id.twitter_share_image);
        lkendin_share_image = (ImageView) header.findViewById(R.id.lkendin_share_image);
        lkendin_web_image = (ImageView) header.findViewById(R.id.lkendin_web_image);
        verified_img =(ImageView) header.findViewById(R.id.verified_img);
        landline=(TextView) header.findViewById(R.id.dealer_plus_manager_landline);
        dealership_plus_manager=(TextView) header.findViewById(R.id.dealership_plus_manager);
        dealer_plus_manager = (TextView) header.findViewById(R.id.dealer_plus_manager);
        dealer_plus_manager_address = (TextView) header.findViewById(R.id.dealer_plus_manager_address);


        edit_bussiness = (TextView) header.findViewById(R.id.edit_button);

        profile_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked_profile = true;
                imagebase64 = null;
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, PICK_IMAGE_REQUEST);
            }
        });

        cover_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final List<String> photo_list = new ArrayList<>();
                photo_list.add("Gallery");
                photo_list.add("Remove Photo");
                final AlertDialog.Builder builder = new AlertDialog.Builder(BussinessProfileActivity.this);
                final ArrayAdapter<String> aa1 = new ArrayAdapter<String>(BussinessProfileActivity.this, R.layout.sort_single_item, R.id.list, photo_list);
                builder.setSingleChoiceItems(aa1, 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (photo_list.get(item) == "Gallery") {
                            clicked = true;
                            imagebase64_profile = null;
                            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(i, PICK_IMAGE_REQUEST);
                        } else if (photo_list.get(item) == "Remove Photo") {
                            clicked = true;
                            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.carimageplaceholder);
                            scaleDown(largeIcon, 200, true);
                            imagebase64 = encodedImage;

                            new bussiness_profile().execute();

                            dialog.dismiss();
                        }
                    }
                });
                builder.show();


            }
        });


        if (isNetworkAvailable()) {
            new bussiness_profile().execute();
        } else {

            Toast.makeText(BussinessProfileActivity.this, "Network Problem", Toast.LENGTH_SHORT).show();
            BussinessProfileActivity.this.finish();
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

    @Override
    public void onBackPressed() {

        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to exit application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        BussinessProfileActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && null != data) {

            if (clicked == true) {

                clicked = false;

                Uri selectedImage = data.getData();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                if (cursor == null) {

                } else {
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    Glide.with(getApplicationContext())
                            .load(selectedImage)
                            .asBitmap()
                            .into(cover_photo);

                    Bitmap bitmap = null;

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //convert(bitmap);
                    scaleDown(bitmap, 400, true);
                    if (isNetworkAvailable()) {
                        new bussiness_profile().execute();
                    } else {
                        Toast.makeText(BussinessProfileActivity.this, "Network Problem", Toast.LENGTH_SHORT).show();
                        BussinessProfileActivity.this.finish();
                    }
                }

            } else if (clicked_profile == true) {
                clicked_profile = false;
                Uri selectedImage1 = data.getData();

                String[] filePathColumn1 = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage1, filePathColumn1, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn1[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                Glide.with(getApplicationContext())
                        .load(selectedImage1)
                        .asBitmap()
                        .into(profile_photo);

                Bitmap bitmap = null;

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //convert(bitmap);
                scaleDown_profile(bitmap, 400, true);

                if (isNetworkAvailable()) {
                    new bussiness_profile().execute();
                } else {

                    Toast.makeText(BussinessProfileActivity.this, "Network Problem", Toast.LENGTH_SHORT).show();
                    BussinessProfileActivity.this.finish();
                }
            }

        } else {
            Log.i("SonaSys", "resultCode: " + resultCode);
        }
    }

    public void faqs() {
        FaqOptions faqOptions = new FaqOptions()
                .showFaqCategoriesAsGrid(true)
                .showContactUsOnAppBar(true)
                .showContactUsOnFaqScreens(false)
                .showContactUsOnFaqNotHelpful(false);

        Hotline.showFAQs(BussinessProfileActivity.this, faqOptions);
    }

    public void chat() {
        Hotline.showConversations(getApplicationContext());
        // Hotline.clearUserData(getApplicationContext());
    }

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

    @Override
    public void onRestart() {
        super.onRestart();
        startActivity(getIntent());
        finish();
    }

    private class bussiness_profile extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();
            mainparams = new ArrayList<NameValuePair>();
            params = new ArrayList<NameValuePair>();
            if (imagebase64 == null && imagebase64_profile == null) {

            } else if (imagebase64_profile != null) {
                params.add(new BasicNameValuePair("profile_image", imagebase64_profile.toString()));
                //params.add(new BasicNameValuePair("cover_image", null));
            } else if (imagebase64 != null) {
                params.add(new BasicNameValuePair("cover_image", imagebase64.toString()));
            }
            String url = Constant.BUSSINESSPROFILE + "session_user_id=" + user.get("user_id") + "&page_name=viewbusinessprofile";

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

            bussinessProfileListAdapter = new BussinessProfileListAdapter(BussinessProfileActivity.this, getbidsdata());
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



            dealer_plus_manager.setText(bussinessprofilelist.get("dealer_name") + " (" + length.toString() + " Cars)");
            dealer_plus_manager_address.setText(bussinessprofilelist.get("d_mobile") + " - " +
                    bussinessprofilelist.get("d_email"));
            dealership_plus_manager.setText(bussinessprofilelist.get("dealership_name").toString());


            facebook_share_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = bussinessprofilelist.get("facebook_link").toString();
                    Intent intent = new Intent(BussinessProfileActivity.this, WebViewActivity.class);
                    intent.putExtra("title", "Facebook");
                    intent.putExtra("url", url);
                    startActivity(intent);
                }
            });

            twitter_share_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = bussinessprofilelist.get("twitter_link").toString();
                    Intent intent = new Intent(BussinessProfileActivity.this, WebViewActivity.class);
                    intent.putExtra("title", "Twitter");
                    intent.putExtra("url", url);
                    startActivity(intent);


                }
            });

            lkendin_share_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = bussinessprofilelist.get("linkedin_link").toString();
                    Intent intent = new Intent(BussinessProfileActivity.this, WebViewActivity.class);
                    intent.putExtra("title", "LinkedIn");
                    intent.putExtra("url", url);
                    startActivity(intent);

                }
            });

            lkendin_web_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = bussinessprofilelist.get("dealership_website").toString();
                    Intent intent = new Intent(BussinessProfileActivity.this, WebViewActivity.class);
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
                        Intent intent = new Intent(BussinessProfileActivity.this, SellWebViewActivity.class);
                        intent.putExtra("title", "Car Details");
                        intent.putExtra("url", url);
                        startActivity(intent);
                    }
                    // Toast.makeText(BussinessProfileActivity.this, "Selected Car Name :" + item.getMake(), Toast.LENGTH_SHORT).show();
                }
            });


            edit_bussiness.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BussinessProfileActivity.this, EditBussinessProfileActivity.class);
                    intent.putExtra("bussiness_details", bussiness_details);
                    startActivity(intent);
                }
            });
        }
    }

}
