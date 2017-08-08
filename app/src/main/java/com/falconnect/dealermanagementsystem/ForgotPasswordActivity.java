package com.falconnect.dealermanagementsystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class ForgotPasswordActivity extends AppCompatActivity {

    public ArrayList<HashMap<String, String>> ForgotList;
    ImageView back_forgot;
    Button forgot_submit;
    EditText email;
    TextView back_to_login;

    String email_id;
    HashMap<String, String> forgotpasswordlist;
    private boolean mVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        setContentView(R.layout.activity_forgot_password);

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


        //back button
        back_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgotPasswordActivity.this.finish();
            }
        });


        forgot_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_id = email.getText().toString();

                new load_forgot_password().execute();
            }
        });

    }

    public void intialize() {
        back_forgot = (ImageView) findViewById(R.id.backbtn);

        email = (EditText) findViewById(R.id.username);

        email.setTypeface(Typeface.SANS_SERIF);

        forgot_submit = (Button) findViewById(R.id.forgot_submit);

        back_to_login = (TextView) findViewById(R.id.login_back);
    }


    class load_forgot_password extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... args) {

            ServiceHandler sh = new ServiceHandler();

            String forgot_password_main_url = Constant.FORGOT_PASSWORD_API + "mailid=" + email_id.toString();

            String json = sh.makeServiceCall(forgot_password_main_url, ServiceHandler.POST);


            if (json != null) {

                ForgotList = new ArrayList<>();
                forgotpasswordlist = new HashMap<String, String>();

                try {
                    JSONObject obj = new JSONObject(json);

                    for (int i = 0; i <= obj.length(); i++) {

                        String get_email_id = obj.getString("Result");
                        String get_email_message = obj.getString("message");

                        forgotpasswordlist.put("REsult", get_email_id);
                        forgotpasswordlist.put("Message", get_email_message);

                        ForgotList.add(forgotpasswordlist);
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //  Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

        protected void onPostExecute(String file_url) {

            if (forgotpasswordlist.get("REsult").equals("1")) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPasswordActivity.this);
                builder.setTitle("Check Email");

                builder.setMessage(forgotpasswordlist.get("Message"))
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                                startActivity(intent);

                                ForgotPasswordActivity.this.finish();

                            }
                        });

                builder.show();
            } else {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPasswordActivity.this);
                builder.setTitle("Invaild User");
                builder.setMessage("Enter the valid email id")
                        .setCancelable(false)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });

                builder.show();
            }
        }
    }
}
