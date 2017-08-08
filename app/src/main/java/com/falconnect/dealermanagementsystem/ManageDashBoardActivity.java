package com.falconnect.dealermanagementsystem;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.falconnect.dealermanagementsystem.Adapter.CustomList;
import com.falconnect.dealermanagementsystem.Adapter.ManageFooterCustomAdapter;
import com.falconnect.dealermanagementsystem.FontAdapter.RoundImageTransform;
import com.falconnect.dealermanagementsystem.Model.ManageFooterDataModel;
import com.falconnect.dealermanagementsystem.NavigationDrawer.BuyPageNavigation;
import com.falconnect.dealermanagementsystem.SharedPreference.AddressSavedSharedPreferences;
import com.falconnect.dealermanagementsystem.SharedPreference.PlandetailsSharedPreferences;
import com.falconnect.dealermanagementsystem.SharedPreference.ProfileManagerSession;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;
import com.freshdesk.hotline.FaqOptions;
import com.freshdesk.hotline.Hotline;
import com.navdrawer.SimpleSideDrawer;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ManageDashBoardActivity extends AppCompatActivity {

    public static String encodedImage;
    public static String imagebase64;
    public static HashMap<String, String> user;
    private static RecyclerView.Adapter adapter;
    private static RecyclerView managerecycleview;
    private static ArrayList<ManageFooterDataModel> managedata;
    public ArrayList<HashMap<String, String>> updateprofile_list;
    List<NameValuePair> params;
    String newqueriesurl;
    AddressSavedSharedPreferences sessionManagerAddress;
    EditText profile_name, profile_email, profile_password, profile_phonenumber;
    TextView profile_address;
    ImageView map_nav;
    TextView profile_password_change;
    Button button_send_manage_profile;
    ImageView manage_edit_profile_btn, manage_edit_cancel_btn, manage_mnav;
    SessionManager session;
    HashMap<String, String> user_shared;
    ImageView imageView_manage;

    TextView profile_name_manage;
    TextView profile_address_manage;
    String saved_name_manage, saved_address_manage;
    RelativeLayout call_layout, chat_layout, logout_layout;
    BuyPageNavigation manage_buypagenavigation;
    ImageView profile_edit_manage;
    boolean clicked = false;

    ProfileManagerSession profileManagerSession;

    HashMap<String, String> updateprofilelist;
    String update_dealer_name, update_dealer_number, update_address;
    String result, dealer_address, dealer_email, dealer_img, dealer_mobile, dealer_name, message, user_id, city_news, pincode, dealershipname, parent_id;
    ProgressDialog barProgressDialog;
    String location;
    AddressSavedSharedPreferences addressSavedSharedPreferences;
    HashMap<String, String> user_address;
    String image_value;
    PlandetailsSharedPreferences plandetailsSharedPreferences;
    HashMap<String, String> plan;
    private int PICK_IMAGE_REQUEST = 1;
    private boolean mVisible;
    private RecyclerView.LayoutManager layoutManager;
    private SimpleSideDrawer mNav_manage;

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter) {
        float ratio = Math.min(maxImageSize / realImage.getWidth(),
                maxImageSize / realImage.getHeight());
        int width = Math.round(ratio * realImage.getWidth());
        int height = Math.round(ratio * realImage.getHeight());
        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width, height, filter);
        convert(newBitmap);
        return newBitmap;
    }

    public static String convert(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

        encodedImage = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);

        return encodedImage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_manage_dash_board);

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mVisible = true;

        if (mVisible) {
            android.support.v7.app.ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
            mVisible = false;

        } else {
        }


        addressSavedSharedPreferences = new AddressSavedSharedPreferences(ManageDashBoardActivity.this);
        profileManagerSession = new ProfileManagerSession(ManageDashBoardActivity.this);
        user_shared = profileManagerSession.getProfileManagerDetails();


        profile_name = (EditText) findViewById(R.id.profile_name);
        profile_email = (EditText) findViewById(R.id.profile_email);
        profile_password = (EditText) findViewById(R.id.profile_password);
        profile_password_change = (TextView) findViewById(R.id.profile_password_change);
        profile_phonenumber = (EditText) findViewById(R.id.profile_phone_number);
        profile_address = (TextView) findViewById(R.id.profile_address);
        map_nav = (ImageView) findViewById(R.id.map_button);
        profile_edit_manage = (ImageView) findViewById(R.id.profile_edit_manage);

        button_send_manage_profile = (Button) findViewById(R.id.button_send_manage_profile);

        manage_edit_profile_btn = (ImageView) findViewById(R.id.manage_edit_btn);

        manage_edit_cancel_btn = (ImageView) findViewById(R.id.manage_edit_cancel_btn);

        manage_mnav = (ImageView) findViewById(R.id.manage_profile_mnav);
        //add_location = (ImageView) findViewById(R.id.add_location);
        managerecycleview = (RecyclerView) findViewById(R.id.profile_recyclerview);
        managerecycleview.setHasFixedSize(true);
        managerecycleview.setLayoutManager(new LinearLayoutManager(ManageDashBoardActivity.this, LinearLayoutManager.HORIZONTAL, false));

        managedata = new ArrayList<ManageFooterDataModel>();

        for (int i = 0; i < MyFooterManageData.managenameArray.length; i++) {

            managedata.add(new ManageFooterDataModel(
                    MyFooterManageData.managenameArray[i],
                    MyFooterManageData.managedrawableArrayWhite0[i],
                    MyFooterManageData.manageid_[i]
            ));
        }
        adapter = new ManageFooterCustomAdapter(ManageDashBoardActivity.this, managedata);
        managerecycleview.setAdapter(adapter);


        profile_name.setEnabled(false);
        profile_email.setEnabled(false);
        profile_password.setEnabled(false);
        profile_phonenumber.setEnabled(false);
        profile_address.setEnabled(false);
        map_nav.setEnabled(false);
        profile_edit_manage.setEnabled(false);
        button_send_manage_profile.setVisibility(View.GONE);

        user_address = profileManagerSession.getProfileManagerDetails();

        if (user_address.get("key_vale") == null) {
            manage_edit_profile_btn.setVisibility(View.VISIBLE);
            manage_edit_cancel_btn.setVisibility(View.GONE);
        } else {
            manage_edit_profile_btn.setVisibility(View.GONE);
            manage_edit_cancel_btn.setVisibility(View.VISIBLE);
            button_send_manage_profile.setVisibility(View.VISIBLE);

        }

        manage_edit_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                profile_name.setEnabled(true);
                profile_phonenumber.setEnabled(true);
                profile_address.setEnabled(true);
                map_nav.setEnabled(true);
                profile_edit_manage.setEnabled(true);
                manage_edit_profile_btn.setVisibility(View.GONE);
                manage_edit_cancel_btn.setVisibility(View.VISIBLE);
                button_send_manage_profile.setVisibility(View.VISIBLE);

            }
        });

        profile_password_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageDashBoardActivity.this, ChangePassword.class);
                startActivity(intent);
            }
        });


        session = new SessionManager(ManageDashBoardActivity.this);
        user = session.getUserDetails();

        ImageView manage_nav = (ImageView) findViewById(R.id.manage_profile_mnav);
        mNav_manage = new SimpleSideDrawer(this);
        mNav_manage.setLeftBehindContentView(R.layout.activity_behind_left_simple);

        imageView_manage = (ImageView) mNav_manage.findViewById(R.id.profile_avatar);
        profile_name_manage = (TextView) mNav_manage.findViewById(R.id.profile_name);
        profile_address_manage = (TextView) mNav_manage.findViewById(R.id.profile_address);

        saved_name_manage = user.get("dealer_name");
        saved_address_manage = user.get("dealershipname");

        call_layout = (RelativeLayout) mNav_manage.findViewById(R.id.call_layout);
        chat_layout = (RelativeLayout) mNav_manage.findViewById(R.id.chat_layout);
        logout_layout = (RelativeLayout) mNav_manage.findViewById(R.id.logout_layout);

        call_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertbox = new AlertDialog.Builder(ManageDashBoardActivity.this)
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
                mNav_manage.closeLeftSide();
                ManageDashBoardActivity.this.finish();
            }
        });

        chat_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chat();
                mNav_manage.closeLeftSide();
            }
        });

        profile_name_manage.setText(saved_name_manage);
        if (user.get("dealer_img").isEmpty()) {
            Glide.with(getApplicationContext())
                    .load(R.drawable.default_avatar)
                    .asBitmap()
                    .transform(new RoundImageTransform(ManageDashBoardActivity.this))
                    .into(imageView_manage);
        } else {
            Glide.with(getApplicationContext())
                    .load(user.get("dealer_img"))
                    .asBitmap()
                    .transform(new RoundImageTransform(ManageDashBoardActivity.this))
                    .into(imageView_manage);
        }
        profile_address_manage.setText(saved_address_manage);

        manage_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNav_manage.toggleLeftDrawer();
            }
        });

        //NAVIGATION DRAWER LIST VIEW
        manage_buypagenavigation = new BuyPageNavigation();
        CustomList adapter = new CustomList(ManageDashBoardActivity.this, BuyPageNavigation.web, BuyPageNavigation.imageId);
        ListView list = (ListView) findViewById(R.id.nav_list_view);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (BuyPageNavigation.web[position] == "Dashboard") {
                    Intent intent = new Intent(ManageDashBoardActivity.this, MainDashBoardActivity.class);
                    startActivity(intent);
                    ManageDashBoardActivity.this.finish();
                    mNav_manage.closeLeftSide();
                    //  Toast.makeText(ManageDashBoardActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();

                } else if (BuyPageNavigation.web[position] == "Buy") {
                    Intent intent = new Intent(ManageDashBoardActivity.this, DashBoard.class);
                    startActivity(intent);
                    ManageDashBoardActivity.this.finish();
                    mNav_manage.closeLeftSide();
                    //  Toast.makeText(ManageDashBoardActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();

                } else if (BuyPageNavigation.web[position] == "Sell") {
                    Intent intent = new Intent(ManageDashBoardActivity.this, SellDashBoardActivity.class);
                    startActivity(intent);
                    ManageDashBoardActivity.this.finish();
                    mNav_manage.closeLeftSide();
                    //   Toast.makeText(ManageDashBoardActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Manage") {
                    mNav_manage.closeLeftSide();
                    Toast.makeText(ManageDashBoardActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Funding") {
                    plandetailsSharedPreferences = new PlandetailsSharedPreferences(ManageDashBoardActivity.this);
                    plan = plandetailsSharedPreferences.getUserDetails();

//                    if ( plan.get("PlanName").equals("BASIC")) {
//
//                        Intent intent = new Intent(ManageDashBoardActivity.this, LoanActivity.class);
//                        startActivity(intent);
//                        mNav_manage.closeLeftSide();
//                        ManageDashBoardActivity.this.finish();
//                    } else {
                        Intent intent = new Intent(ManageDashBoardActivity.this, FundingActivity.class);
                        startActivity(intent);
                        mNav_manage.closeLeftSide();
                        ManageDashBoardActivity.this.finish();
//                    }
                    //   Toast.makeText(ManageDashBoardActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Networks") {
                    String url = "http://app.dealerplus.in/user_login_cometchat?session_user_id=" + user.get("user_id");
                    Intent intet = new Intent(ManageDashBoardActivity.this, NetworksWebviewActivity.class);
                    //intet.putExtra("title", BuyPageNavigation.web[position]);
                    intet.putExtra("url", url);
                    startActivity(intet);
                    mNav_manage.closeLeftSide();
                    //   Toast.makeText(ManageDashBoardActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Reports") {
                    plandetailsSharedPreferences = new PlandetailsSharedPreferences(ManageDashBoardActivity.this);

                    plan = plandetailsSharedPreferences.getUserDetails();

                    if (plan.get("PlanName").equals("BASIC")) {

                        Intent intent = new Intent(ManageDashBoardActivity.this, BuyPlanActivity.class);
                        startActivity(intent);
                        //MainDashBoardActivity.this.finish();
                        mNav_manage.closeLeftSide();
                    } else {
                        Intent intent = new Intent(ManageDashBoardActivity.this, ReportSalesActivity.class);
                        startActivity(intent);
                        ManageDashBoardActivity.this.finish();
                        mNav_manage.closeLeftSide();
                    }
                    //    Toast.makeText(ManageDashBoardActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "FAQs") {
                    faqs();
                    mNav_manage.closeLeftSide();
                    // Toast.makeText(ManageDashBoardActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(DashBoard.this, web[position], Toast.LENGTH_SHORT).show();
                }
            }
        });


        if (user.get("dealer_img").isEmpty()) {
            Glide.with(getApplicationContext())
                    .load(R.drawable.default_avatar)
                    .asBitmap()
                    .transform(new RoundImageTransform(ManageDashBoardActivity.this))
                    .into(profile_edit_manage);
        } else {
            Glide.with(getApplicationContext())
                    .load(user.get("dealer_img"))
                    .asBitmap()
                    .transform(new RoundImageTransform(ManageDashBoardActivity.this))
                    .into(profile_edit_manage);
        }

        profile_name.setText(user.get("dealer_name"));
        profile_email.setText(user.get("dealer_email"));
        profile_phonenumber.setText(user.get("dealer_mobile"));

        location = getIntent().getStringExtra("Location");

        profile_edit_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final List<String> photo_list = new ArrayList<>();
                photo_list.add("Gallery");
                photo_list.add("Remove Photo");
                final AlertDialog.Builder builder = new AlertDialog.Builder(ManageDashBoardActivity.this);
                final ArrayAdapter<String> aa1 = new ArrayAdapter<String>(ManageDashBoardActivity.this, R.layout.sort_single_item, R.id.list, photo_list);
                builder.setSingleChoiceItems(aa1, 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (photo_list.get(item) == "Gallery") {
                            clicked = true;
                            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(i, PICK_IMAGE_REQUEST);
                            dialog.dismiss();
                        } else if (photo_list.get(item) == "Remove Photo") {
                            clicked = true;

                            Glide.with(ManageDashBoardActivity.this)
                                    .load(R.drawable.default_avatar)
                                    .into(profile_edit_manage);

                            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.default_avatar);
                            scaleDown(largeIcon, 200, true);
                            imagebase64 = encodedImage;
                            Log.e("imagebase64", imagebase64);


                            dialog.dismiss();
                        }
                    }
                });
                builder.show();

            }
        });


        if (location == null) {
            profile_address.setText(user.get("dealer_address"));
        } else {
            profile_address.setText(location);
        }

        manage_edit_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user = session.getUserDetails();

                profileManagerSession.clear_ProfileManage();
                profile_name.setText(user.get("dealer_name"));
                profile_email.setText(user.get("dealer_email"));
                profile_phonenumber.setText(user.get("dealer_mobile"));
                profile_address.setText(user.get("dealer_address"));
                if (user.get("dealer_img").isEmpty()) {
                    Glide.with(getApplicationContext())
                            .load(R.drawable.default_avatar)
                            .asBitmap()
                            .transform(new RoundImageTransform(ManageDashBoardActivity.this))
                            .into(profile_edit_manage);
                } else {
                    Glide.with(getApplicationContext())
                            .load(user.get("dealer_img"))
                            .asBitmap()
                            .transform(new RoundImageTransform(ManageDashBoardActivity.this))
                            .into(profile_edit_manage);
                }

                profile_name.setEnabled(false);
                profile_phonenumber.setEnabled(false);
                profile_address.setEnabled(false);
                map_nav.setEnabled(false);
                profile_edit_manage.setEnabled(false);
                manage_edit_profile_btn.setVisibility(View.VISIBLE);
                manage_edit_cancel_btn.setVisibility(View.GONE);
                button_send_manage_profile.setVisibility(View.GONE);

            }
        });

        update_dealer_name = profile_name.getText().toString();
        update_dealer_name = update_dealer_name.replaceAll(" ", "%20");

        update_dealer_number = profile_phonenumber.getText().toString();
        update_dealer_number = update_dealer_number.replaceAll(" ", "%20");

        update_address = profile_address.getText().toString();
        update_address = update_address.replaceAll(" ", "%20");


        button_send_manage_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_dealer_name = profile_name.getText().toString();
                update_dealer_name = update_dealer_name.replaceAll(" ", "%20");

                update_dealer_number = profile_phonenumber.getText().toString();
                update_dealer_number = update_dealer_number.replaceAll(" ", "%20");

                update_address = profile_address.getText().toString();
                update_address = update_address.replaceAll(" ", "%20");

                new update_profile().execute();
            }
        });

        map_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (user_shared.get("dealer_img") == null) {
                    /*Intent intent = new Intent(ManageDashBoardActivity.this, LocationSelectionAddress.class);
                    startActivity(intent);*/
                    profileManagerSession.createProfileManageSession(update_dealer_name, user_shared.get("dealer_img"), user.get("dealer_email"), update_dealer_number, "1");
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    Intent intent = new Intent(ManageDashBoardActivity.this, LocationSelectionAddress.class);
                    startActivity(intent);
                } else {

                }
            }
        });
    }



    @Override
    public void onBackPressed() {

        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to exit application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        ManageDashBoardActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();

    }

    public void faqs() {
        FaqOptions faqOptions = new FaqOptions()
                .showFaqCategoriesAsGrid(true)
                .showContactUsOnAppBar(true)
                .showContactUsOnFaqScreens(false)
                .showContactUsOnFaqNotHelpful(false);

        Hotline.showFAQs(ManageDashBoardActivity.this, faqOptions);
    }

    public void chat() {
        Hotline.showConversations(getApplicationContext());
        // Hotline.clearUserData(getApplicationContext());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && null != data) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor != null)
            {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                Glide.with(getApplicationContext())
                        .load(selectedImage)
                        .asBitmap()
                        .transform(new RoundImageTransform(ManageDashBoardActivity.this))
                        .into(profile_edit_manage);

                profileManagerSession.createProfileManageSession(null, picturePath, null, null, "1");

                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                scaleDown(bitmap, 200, true);

            }
            else {

                Toast.makeText(ManageDashBoardActivity.this,"Image cannot be upload",Toast.LENGTH_SHORT);
            }


        } else {
            Toast.makeText(ManageDashBoardActivity.this,"Image cannot be upload",Toast.LENGTH_SHORT);

        }
    }

    private class update_profile extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = ProgressDialog.show(ManageDashBoardActivity.this, "Loading...", "Please Wait ...", true);

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();

            params = new ArrayList<NameValuePair>();

            if (encodedImage != null) {
                imagebase64 = encodedImage;
            } else {

                URL url = null;
                Bitmap bmp = null;

                try {
                    url = new URL(user.get("dealer_img").toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                scaleDown(bmp, 200, true);
                imagebase64 = encodedImage;
            }


            params.add(new BasicNameValuePair("profile_image", imagebase64));

            newqueriesurl = Constant.EDITACCOUNT +
                    "session_user_id=" + user.get("user_id") +
                    "&delaer_name=" + update_dealer_name +
                    "&mobile_number=" + update_dealer_number +
                    "&dealer_address=" + update_address +
                    "&page_name=updateprofile";

            Log.e("Queriesurl", newqueriesurl);

            String json = sh.makeServiceCall(newqueriesurl, ServiceHandler.POST, params);
            if (json != null) {
                updateprofile_list = new ArrayList<>();
                updateprofilelist = new HashMap<>();
                try {
                    JSONObject obj = new JSONObject(json);
                    for (int k = 0; k <= obj.length(); k++) {
                        result = obj.getString("Result");
                        message = obj.getString("message");
                        if (result.equals("1")) {
                            user_id = obj.getString("user_id");
                            dealer_name = obj.getString("dealer_name");
                            dealer_mobile = obj.getString("dealer_mobile");
                            dealer_email = obj.getString("dealer_email");
                            dealer_img = obj.getString("dealer_img");
                            dealer_address = obj.getString("dealer_address");
                            city_news = obj.getString("city");
                            pincode = obj.getString("pincode");
                            dealershipname = obj.getString("dealershipname");
                            parent_id = obj.getString("parent_id");

                            updateprofilelist.put("Result", result);
                            updateprofilelist.put("message", message);
                            updateprofilelist.put("user_id", user_id);
                            updateprofilelist.put("dealer_name", dealer_name);
                            updateprofilelist.put("dealer_mobile", dealer_mobile);
                            updateprofilelist.put("dealer_email", dealer_email);
                            updateprofilelist.put("dealer_img", dealer_img);
                            updateprofilelist.put("dealer_address", dealer_address);
                            updateprofilelist.put("city", city_news);
                            updateprofilelist.put("pincode", pincode);
                            updateprofilelist.put("parent_id", parent_id);
                            updateprofilelist.put("dealershipname", dealershipname);

                            updateprofile_list.add(updateprofilelist);

                        } else {
                            updateprofilelist.put("Result", result);
                            updateprofilelist.put("message", message);
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
                        // Toast.makeText(getApplicationContext(), "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            barProgressDialog.dismiss();

            session.clear_user();

            session.createLoginSession(dealer_name, user_id, dealer_img, dealer_address, dealer_email, dealer_mobile, city_news, pincode, parent_id, dealershipname);

            profile_name.setText(updateprofilelist.get("dealer_name"));
            profile_phonenumber.setText(updateprofilelist.get("dealer_mobile"));

            Glide.with(getApplicationContext())
                    .load(updateprofilelist.get("dealer_img"))
                    .transform(new RoundImageTransform(ManageDashBoardActivity.this))
                    .into(profile_edit_manage);

            Glide.with(getApplicationContext())
                    .load(updateprofilelist.get("dealer_img"))
                    .transform(new RoundImageTransform(ManageDashBoardActivity.this))
                    .into(imageView_manage);

            profile_address.setText(updateprofilelist.get("dealer_address"));

            encodedImage = null;

            manage_edit_profile_btn.setVisibility(View.VISIBLE);
            manage_edit_cancel_btn.setVisibility(View.GONE);
            button_send_manage_profile.setVisibility(View.GONE);

            profileManagerSession.clear_ProfileManage();

            // Toast.makeText(ManageDashBoardActivity.this, updateprofilelist.get("message"), Toast.LENGTH_SHORT).show();

        }
    }
}

