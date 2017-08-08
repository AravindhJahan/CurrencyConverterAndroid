package com.falconnect.dealermanagementsystem;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ChangePassword extends AppCompatActivity {

    public ArrayList<HashMap<String, String>> changepasswordlist;
    ImageView back_new;
    EditText oldpass, newpass, confirmpass;
    String old_pass, new_pass, confirm_pass;
    Button change_btn, cancel;
    SessionManager session;
    HashMap<String, String> user;
    HashMap<String, String> changepassmaplist;
    private boolean mVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_change_password);
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
        session = new SessionManager(ChangePassword.this);
        user = session.getUserDetails();
        intialize();
        //back
        back_new = (ImageView) findViewById(R.id.backbtn);
        back_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePassword.this.finish();
            }
        });

        change_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                old_pass = oldpass.getText().toString();
                new_pass = newpass.getText().toString();
                confirm_pass = confirmpass.getText().toString();

                new load_change_password().execute();
            }
        });

    }

    public void intialize() {

        oldpass = (EditText) findViewById(R.id.old_password);
        newpass = (EditText) findViewById(R.id.new_password);
        confirmpass = (EditText) findViewById(R.id.confirm_password);
        //Button Id
        change_btn = (Button) findViewById(R.id.change_submit);
        cancel = (Button) findViewById(R.id.change_cancel);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePassword.this.finish();
            }
        });
    }


    class load_change_password extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... args) {

            ServiceHandler sh = new ServiceHandler();

            String change_password_url = Constant.CHANGE_PASSWORD_API + "oldpassword="
                    + old_pass.toString() + "&newpassword="
                    + new_pass.toString() + "&confirm_password="
                    + confirm_pass.toString() + "&id=" +user.get("user_id") ;

            String json = sh.makeServiceCall(change_password_url, ServiceHandler.POST);

            if (json != null) {

                changepasswordlist = new ArrayList<>();

                changepassmaplist = new HashMap<String, String>();

                try {
                    JSONObject obj = new JSONObject(json);

                    for (int i = 0; i <= obj.length(); i++) {

                        String result = obj.getString("Result");
                        String message = obj.getString("message");

                        changepassmaplist.put("REsult", result);
                        changepassmaplist.put("Message", message);

                        changepasswordlist.add(changepassmaplist);
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
                        //Toast.makeText(getApplicationContext(), "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }

        protected void onPostExecute(String file_url) {

            if (changepassmaplist.get("REsult").equals("1")) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ChangePassword.this);
                builder.setTitle("Password Changed");
                builder.setMessage(changepassmaplist.get("Message"));
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        ChangePassword.this.finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChangePassword.this);
                builder.setTitle("Error");
                builder.setMessage(changepassmaplist.get("Message"));
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        }
    }
}
