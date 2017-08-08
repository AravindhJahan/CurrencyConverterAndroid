package com.falconnect.dealermanagementsystem;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class EditBussinessProfileActivity extends Activity {

    public ArrayList<HashMap<String, String>> editList;
    ArrayList<String> get_details = new ArrayList<>();
    ImageView edit_bussiness_close, edit_bussiness_apply_icon;
    EditText dealership_name, dealer_names, dealer_num, dealer_email;
    String dealer_ship_name, dealers_name, dealers_num;
    String Result, Message;
    HashMap<String, String> editlistmap;

    SessionManager session;
    HashMap<String, String> user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_bussiness_profile);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        get_details = getIntent().getStringArrayListExtra("bussiness_details");

        session = new SessionManager(EditBussinessProfileActivity.this);
        user = session.getUserDetails();

        edit_bussiness_close = (ImageView) findViewById(R.id.edit_bussiness_close);
        edit_bussiness_apply_icon = (ImageView) findViewById(R.id.edit_bussiness_apply_icon);

        dealership_name = (EditText) findViewById(R.id.dealerplus_edit_text);
        dealer_names = (EditText) findViewById(R.id.dealer_edit_text);
        dealer_num = (EditText) findViewById(R.id.dealer_num_edit_text);
        dealer_email = (EditText) findViewById(R.id.dealer_email_edit_text);

        dealership_name.setText(get_details.get(0));
        dealer_names.setText(get_details.get(1));
        dealer_num.setText(get_details.get(2));
        dealer_email.setText(get_details.get(3));
        dealer_email.setTextColor(Color.GRAY);
        dealer_email.setEnabled(false);


        edit_bussiness_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                EditBussinessProfileActivity.this.finish();
            }
        });

        edit_bussiness_apply_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dealer_ship_name = dealership_name.getText().toString();
                dealers_name = dealer_names.getText().toString();
                dealers_num = dealer_num.getText().toString();

                if (dealership_name.getText().toString().equals("")) {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(EditBussinessProfileActivity.this)
                            .setMessage("Please Enter Dealership Name")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });
                    alertbox.show();
                } else if (dealer_names.getText().toString().equals("")) {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(EditBussinessProfileActivity.this)
                            .setMessage("Please Enter Dealer Name")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });
                    alertbox.show();
                } else if (dealer_num.getText().toString().equals("")) {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(EditBussinessProfileActivity.this)
                            .setMessage("Please Enter Dealer Number")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });
                    alertbox.show();
                }else{
                    new edit_bussiness_profile().execute();
                }
            }
        });

    }

    class edit_bussiness_profile extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... args) {

            ServiceHandler sh = new ServiceHandler();

            String main_url = Constant.EDITBUSSINESSPROFILE + "session_user_id=" + user.get("user_id")
                    + "&page_name=updatebusinessprofile"
                    + "&dealership_name=" + dealer_ship_name
                    + "&dealer_name=" + dealers_name
                    + "&d_mobile=" + dealers_num;

            String urlString = main_url.replaceAll(" ", "%20");

            String json = sh.makeServiceCall(urlString, ServiceHandler.POST);


            if (json != null) {

                editList = new ArrayList<>();

                try {
                    JSONObject obj = new JSONObject(json);

                    for (int i = 0; i <= obj.length(); i++) {
                        Result = obj.getString("Result");
                        Message = obj.getString("message");

                        editlistmap = new HashMap<>();
                        editlistmap.put("Result", Result);
                        editlistmap.put("Message", Message);

                        editList.add(editlistmap);
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }
            } else

            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        protected void onPostExecute(String file_url) {

            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            // Toast.makeText(EditBussinessProfileActivity.this, editlistmap.get("Message"), Toast.LENGTH_SHORT).show();



            EditBussinessProfileActivity.this.finish();


        }
    }
}

