package com.falconnect.dealermanagementsystem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.falconnect.dealermanagementsystem.Model.ManageFooterDataModel;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AddUserActivity extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private static RecyclerView my_recycler_adduser;
    private static ArrayList<ManageFooterDataModel> adduserdata;
    public ArrayList<HashMap<String, String>> adduser_list;
    Spinner role_adduser;
    SessionManager session;
    HashMap<String, String> user;
    EditText user_name, user_email, user_mobilenum;
    ArrayList<String> role_list = new ArrayList<>();
    ArrayAdapter<String> spinnerroleArrayAdapter;
    String selected_role;
    String result, message;
    int position_role;
    HashMap<String, String> adduserlist;
    ProgressDialog barProgressDialog;

    Button submit_adduser;
    ImageView add_user_back;
    String username, useremail, phonenum;
    private boolean mVisible;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_user);
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

        Intent intent = getIntent();
        role_list = intent.getStringArrayListExtra("role_list");

        my_recycler_adduser = (RecyclerView) findViewById(R.id.adduser_footer);
        my_recycler_adduser.setHasFixedSize(true);
        my_recycler_adduser.setLayoutManager(new LinearLayoutManager(AddUserActivity.this, LinearLayoutManager.HORIZONTAL, false));

        role_adduser = (Spinner) findViewById(R.id.role_adduser);

        user_name = (EditText) findViewById(R.id.username_adduser);
        user_email = (EditText) findViewById(R.id.user_email_adduser);
        user_mobilenum = (EditText) findViewById(R.id.phone_num_adduser);

        submit_adduser = (Button) findViewById(R.id.submit_adduser);
        add_user_back = (ImageView) findViewById(R.id.add_user_back);

        session = new SessionManager(AddUserActivity.this);
        user = session.getUserDetails();

        spinnerroleArrayAdapter = new ArrayAdapter<String>(AddUserActivity.this,
                R.layout.spinner_single_item, role_list) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerroleArrayAdapter.setDropDownViewResource(R.layout.spinner_single_item);
        role_adduser.setAdapter(spinnerroleArrayAdapter);
        role_adduser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                if (position > 0) {
                    //Toast.makeText(getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT).show();
                    selected_role = selectedItemText;
                    position_role = position;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        add_user_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                AddUserActivity.this.finish();
            }
        });

        submit_adduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = user_name.getText().toString();
                useremail = user_email.getText().toString();
                phonenum = user_mobilenum.getText().toString();

                new add_user_data().execute();
            }
        });
    }

    private class add_user_data extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = ProgressDialog.show(AddUserActivity.this, "Loading...", "Please Wait ...", true);
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();

            String newqueriesurl = Constant.ADD_USER_VIEW
                    + "session_user_id=" + user.get("user_id")
                    + "&dealer_name=" + username
                    + "&dealer_mail=" + useremail
                    + "&mobilenumber=" + phonenum
                    + "&user_role=" + position_role
                    + "&page_name=addnewuser";

            Log.e("Queriesurl", newqueriesurl);

            String json = sh.makeServiceCall(newqueriesurl, ServiceHandler.POST);

            if (json != null) {

                adduser_list = new ArrayList<>();

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    for (int k = 0; k <= jsonObj.length(); k++) {

                        result = jsonObj.getString("Result");
                        message = jsonObj.getString("message");

                        adduserlist = new HashMap<>();
                        adduserlist.put("Result", result);
                        adduserlist.put("message", message);

                        adduser_list.add(adduserlist);

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
            if (adduserlist.get("Result").equals("1")) {
                //  Toast.makeText(AddUserActivity.this, adduserlist.get("message"), Toast.LENGTH_SHORT).show();
                AddUserActivity.this.finish();

            } else {
                final AlertDialog alertbox = new AlertDialog.Builder(AddUserActivity.this)
                        .setTitle("Error")
                        .setMessage(adduserlist.get("message"))
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        })
                        .show();
            }

        }
    }

}
