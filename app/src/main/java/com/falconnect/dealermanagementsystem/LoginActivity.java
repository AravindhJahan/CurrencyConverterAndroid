package com.falconnect.dealermanagementsystem;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    public ArrayList<HashMap<String, String>> LoginList;
    RelativeLayout forgot_password;
    // TextView donthaveaccount, signup,;
    Button submit;
    EditText username, pass_word;
    ProgressDialog barProgressDialog;

    SessionManager session;

    //JSON DATAS
    String user, pass;
    String result, message, user_id, dealer_name, dealer_img, dealer_address, parent_id, dealershipname, dealer_mobile, dealer_email, city_new, pincode;
    HashMap<String, String> loginlistmap;
    private boolean mVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        setContentView(R.layout.activity_login);

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mVisible = true;

        if (mVisible) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
            mVisible = false;

        } else {
        }

        intialize();

        session = new SessionManager(getApplicationContext());

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                pass = pass_word.getText().toString();

                if (user.equals("") && pass.equals("")) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("Invalid User");
                    builder.setMessage("Enter the valid username and password")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });

                    builder.show();
                } else {
                    if (isNetworkAvailable()) {

                        new GetLoginData().execute();

                    } else {
                        Toast.makeText(LoginActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

       /* signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent j = new Intent(LoginActivity.this, RegisterActivity.class);
                j.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(j);

                LoginActivity.this.finish();
            }
        });*/

    }

    public void intialize() {
        // signup = (TextView) findViewById(R.id.sigin_up);
        //donthaveaccount = (TextView) findViewById(R.id.dont_have_account);
        forgot_password = (RelativeLayout) findViewById(R.id.forgot_password);
        submit = (Button) findViewById(R.id.submit_btn);


        //Edittext
        username = (EditText) findViewById(R.id.username);
        pass_word = (EditText) findViewById(R.id.password);

    }


    /////ONBackPressed Button Dialog Box
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
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

    class GetLoginData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = ProgressDialog.show(LoginActivity.this, "Loading...", "Please Wait ...", true);

        }

        protected String doInBackground(String... args) {

            ServiceHandler sh = new ServiceHandler();

            String main_url = Constant.LOGIN_API + "email=" + user.toString() + "&password=" + pass.toString();

            String json = sh.makeServiceCall(main_url, ServiceHandler.POST );

            if (json != null) {
                LoginList = new ArrayList<>();
                loginlistmap = new HashMap<String, String>();
                try {
                    JSONObject obj = new JSONObject(json);

                    for (int i = 0; i <= obj.length(); i++) {

                        result = obj.getString("Result");
                        message = obj.getString("message");

                        if (result.equals("0")) {
                            loginlistmap.put("REsult", result);
                            loginlistmap.put("Message", message);
                            LoginList.add(loginlistmap);
                        } else {

                            user_id = obj.getString("user_id");
                            dealer_name = obj.getString("dealer_name");
                            dealer_img = obj.getString("dealer_img");
                            dealer_address = obj.getString("dealer_address");
                            dealer_mobile = obj.getString("dealer_mobile");
                            dealer_email = obj.getString("dealer_email");
                            dealershipname = obj.getString("dealershipname");
                            parent_id = obj.getString("parent_id");
                            city_new = obj.getString("city");
                            pincode = obj.getString("pincode");

                            loginlistmap.put("REsult", result);
                            loginlistmap.put("Message", message);
                            loginlistmap.put("UserID", user_id);
                            loginlistmap.put("DealerName", dealer_name);
                            loginlistmap.put("DealerImage", dealer_img);
                            loginlistmap.put("DealerAddress", dealer_address);
                            loginlistmap.put("DealerMobile", dealer_mobile);
                            loginlistmap.put("DealerEmail", dealer_email);
                            loginlistmap.put("parent_id", parent_id);
                            loginlistmap.put("DealerShipName", dealershipname);

                            LoginList.add(loginlistmap);
                        }
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }
            } else {
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
            session.createLoginSession(dealer_name, user_id, dealer_img, dealer_address, dealer_email, dealer_mobile, city_new, pincode, parent_id, dealershipname);

            Log.e("name", session.getUserDetails().toString());

            if (loginlistmap.get("REsult").equals("1")) {
                Intent i = new Intent(LoginActivity.this, MainDashBoardActivity.class);
                startActivity(i);
                LoginActivity.this.finish();

            } else if (loginlistmap.get("REsult").equals("0")) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Login Incorrect");
                builder.setMessage(loginlistmap.get("Message"))
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                username.setText("");
                                pass_word.setText("");
                            }
                        });

                builder.show();
            }
            barProgressDialog.dismiss();
        }
    }

}

