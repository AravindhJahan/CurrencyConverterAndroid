package com.falconnect.dealermanagementsystem;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.falconnect.dealermanagementsystem.Model.ManageFooterDataModel;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class EditUserActivity extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private static RecyclerView my_recycler_edituser;
    private static ArrayList<ManageFooterDataModel> adduserdata;
    public ArrayList<HashMap<String, String>> tab_user_listview;
    public ArrayList<HashMap<String, String>> edituser_list;
    EditText user_name, user_email, user_mobilenum;
    ArrayList<String> role_list = new ArrayList<>();
    String result, message;
    Button submit_edituser;
    ImageView edit_user_back;
    HashMap<String, String> tabuserlistview;
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> list_new = new ArrayList<String>();
    ArrayList<String> list_new_id = new ArrayList<String>();
    String username, phonenum;
    SessionManager session;
    HashMap<String, String> edituserlist;
    HashMap<String, String> user;
    TextView role_edituser;
    String names = null;
    String edit_user_id;
    private boolean mVisible;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_user);
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
        role_list = intent.getStringArrayListExtra("users");
        list_new = intent.getStringArrayListExtra("list");
        list_new_id = intent.getStringArrayListExtra("listid");

        session = new SessionManager(EditUserActivity.this);
        user = session.getUserDetails();


        my_recycler_edituser = (RecyclerView) findViewById(R.id.edituser_footer);
        my_recycler_edituser.setHasFixedSize(true);
        my_recycler_edituser.setLayoutManager(new LinearLayoutManager(EditUserActivity.this, LinearLayoutManager.HORIZONTAL, false));
        /*adduserdata = new ArrayList<ManageFooterDataModel>();
        for (int i = 0; i < MyFooterManageData.managenameArray.length; i++) {
            adduserdata.add(new ManageFooterDataModel(
                    MyFooterManageData.managenameArray[i],
                    MyFooterManageData.managedrawableArrayWhite4[i],
                    MyFooterManageData.manageid_[i]
            ));
        }

        adapter = new ManageFooterCustomAdapter(EditUserActivity.this, adduserdata);
        my_recycler_edituser.setAdapter(adapter);*/

        user_name = (EditText) findViewById(R.id.username_edituser);
        user_email = (EditText) findViewById(R.id.user_email_edituser);
        user_mobilenum = (EditText) findViewById(R.id.phone_num_edituser);

        submit_edituser = (Button) findViewById(R.id.submit_edituser);
        edit_user_back = (ImageView) findViewById(R.id.edit_user_back);

        role_edituser = (TextView) findViewById(R.id.role_edituser);

        session = new SessionManager(EditUserActivity.this);
        user = session.getUserDetails();


        user_name.setText(role_list.get(0));

        user_email.setText(role_list.get(1));

        user_email.setEnabled(false);

        user_mobilenum.setText(role_list.get(2));

        list_new.remove(0);

        for (int m = 0; m < list_new.size(); m++) {
            String id = role_list.get(3).toString();
            if (id.equals(list_new_id.get(m).toString())) {
                names = list_new.get(m).toString();
                Log.e("name", names);
            } else {

            }
        }

        role_edituser.setText(names.toString());

        edit_user_id = role_list.get(4).toString();

        edit_user_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                EditUserActivity.this.finish();
            }
        });

        submit_edituser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = user_name.getText().toString();
                phonenum = user_mobilenum.getText().toString();
                new edit_user_data().execute();
            }
        });
    }

    private class edit_user_data extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();

            String newqueriesurl = Constant.EDITUSER
                    + "session_user_id=" + user.get("user_id")
                    + "&user_name=" + username
                    + "&user_moblie_no=" + phonenum
                    + "&user_id=" + edit_user_id
                    + "&page_name=updateuser";

            Log.e("Queriesurl", newqueriesurl);

            String json = sh.makeServiceCall(newqueriesurl, ServiceHandler.POST);

            if (json != null) {

                edituser_list = new ArrayList<>();

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    for (int k = 0; k <= jsonObj.length(); k++) {

                        result = jsonObj.getString("Result");
                        message = jsonObj.getString("message");

                        edituserlist = new HashMap<>();
                        edituserlist.put("Result", result);
                        edituserlist.put("message", message);

                        edituser_list.add(edituserlist);

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

            if (edituserlist.get("Result").equals("1")) {
                //Toast.makeText(EditUserActivity.this, edituserlist.get("message"), Toast.LENGTH_SHORT).show();
                EditUserActivity.this.finish();

            } else {
                final AlertDialog alertbox = new AlertDialog.Builder(EditUserActivity.this)
                        .setTitle("Error")
                        .setMessage(edituserlist.get("message"))
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        })
                        .show();
            }

        }
    }

}
