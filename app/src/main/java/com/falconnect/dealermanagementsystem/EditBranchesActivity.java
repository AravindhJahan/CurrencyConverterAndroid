package com.falconnect.dealermanagementsystem;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.falconnect.dealermanagementsystem.Model.ManageFooterDataModel;
import com.falconnect.dealermanagementsystem.SharedPreference.AddressSavedSharedPreferences;
import com.falconnect.dealermanagementsystem.SharedPreference.SavedDetailsBranch;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;


public class EditBranchesActivity extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private static RecyclerView editbranchrecycleview;
    private static ArrayList<ManageFooterDataModel> editbranchdata;
    public ArrayList<HashMap<String, String>> editbranch_list;
    ImageView edit_back, branch_edit_btn;
    ArrayList<String> list = new ArrayList<String>();
    SessionManager session;

    SavedDetailsBranch savedDetailsBranch;

    HashMap<String, String> user;
    String name, email, phonenumber, branchaddress, city, pincode, branch_id, mainbranch;
    EditText dealername_editbranch, email_editbranch, phone_num_editbranch;
    TextView address_editbranch;
    CheckBox editbranchcheckBox, editbranchhead_checkbox;
    Button update_btn;
    ImageView map_view;
    String result, message;
    int value_dealer_service, value_head_quater;
    AddressSavedSharedPreferences addressSavedSharedPreferences;
    HashMap<String, String> user_address;
    HashMap<String, String> editbranchlist;
    private boolean mVisible;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_branches);

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
        session = new SessionManager(EditBranchesActivity.this);
        user = session.getUserDetails();

        list = getIntent().getStringArrayListExtra("branchlist");

        dealername_editbranch = (EditText) findViewById(R.id.dealername_editbranch);
        email_editbranch = (EditText) findViewById(R.id.email_editbranch);
        phone_num_editbranch = (EditText) findViewById(R.id.phone_num_editbranch);
        address_editbranch = (TextView) findViewById(R.id.address_editbranch);
        map_view = (ImageView) findViewById(R.id.map_view);
        update_btn = (Button) findViewById(R.id.editbranch_submit_button);

        editbranchrecycleview = (RecyclerView) findViewById(R.id.editbranch_recycle_view);
        editbranchrecycleview.setHasFixedSize(true);
        editbranchrecycleview.setLayoutManager(new LinearLayoutManager(EditBranchesActivity.this, LinearLayoutManager.HORIZONTAL, false));

       /* editbranchdata = new ArrayList<ManageFooterDataModel>();

        for (int i = 0; i < MyFooterManageData.managenameArray.length; i++) {

            editbranchdata.add(new ManageFooterDataModel(
                    MyFooterManageData.managenameArray[i],
                    MyFooterManageData.managedrawableArrayWhite1[i],
                    MyFooterManageData.manageid_[i]
            ));
        }
        adapter = new ManageFooterCustomAdapter(EditBranchesActivity.this, editbranchdata);
        editbranchrecycleview.setAdapter(adapter);*/

        edit_back = (ImageView) findViewById(R.id.edit_back);
        edit_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                EditBranchesActivity.this.finish();
            }
        });

        String headquater = list.get(0).toString();
        String branchid = list.get(1).toString();
        String dealer_name = list.get(2).toString();
        String dealer_contact_no = list.get(3).toString();
        String branch_address = list.get(4).toString();
        String dealer_mail = list.get(5).toString();
        String dealer_state = list.get(6).toString();
        String dealer_city = list.get(7).toString();
        String dealer_pincode = list.get(8).toString();
        String dealer_status = list.get(9).toString();
        String dealer_service = list.get(10).toString();
        String headquaters = list.get(11).toString();

        branch_id = branchid.toString();
        addressSavedSharedPreferences = new AddressSavedSharedPreferences(EditBranchesActivity.this);

        editbranchcheckBox = (CheckBox) findViewById(R.id.editbranchdealer_service_checkbox);
        editbranchhead_checkbox = (CheckBox) findViewById(R.id.editbranchhead_quaters_checkbox);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/sanz.ttf");

        editbranchcheckBox.setTypeface(font);
        editbranchhead_checkbox.setTypeface(font);

        address_editbranch.setText(branch_address.toString());

        savedDetailsBranch = new SavedDetailsBranch(EditBranchesActivity.this);

        HashMap<String, String> user_branch = savedDetailsBranch.getUserDetails();

        if (user_branch.get("branch_name") == null) {
            dealername_editbranch.setText(dealer_name.toString());
        } else {
            dealername_editbranch.setText(user_branch.get("branch_name").toString());
        }
        if (user_branch.get("branch_email") == null) {
            email_editbranch.setText(dealer_mail.toString());
        } else {
            email_editbranch.setText(user_branch.get("branch_email").toString());
        }
        if (user_branch.get("branch_number") == null) {
            phone_num_editbranch.setText(dealer_contact_no.toString());
        } else {
            phone_num_editbranch.setText(user_branch.get("branch_number").toString());
        }


        map_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = dealername_editbranch.getText().toString();
                email = email_editbranch.getText().toString();
                phonenumber = phone_num_editbranch.getText().toString();
                savedDetailsBranch.createLoginSession(name, phonenumber, email);

                Intent intent = new Intent(EditBranchesActivity.this, LocationSelection.class);
                startActivity(intent);
                addressSavedSharedPreferences.clear_address();
            }
        });

        user_address = addressSavedSharedPreferences.getAddress_details();

        if (user_address.get("map_address") == null) {
            address_editbranch.setText(branch_address.toString());
        } else {
            address_editbranch.setText(user_address.get("map_address"));
        }



        if (headquater.equals("1")) {
            editbranchhead_checkbox.setVisibility(View.GONE);
        } else {
            editbranchhead_checkbox.setVisibility(View.VISIBLE);
        }


        if (dealer_service.equals("1")) {
            editbranchcheckBox.setChecked(true);
        } else {
            editbranchcheckBox.setChecked(false);
        }

        if (headquaters.equals("1")) {
            editbranchhead_checkbox.setVisibility(View.VISIBLE);
            editbranchhead_checkbox.setChecked(true);
        } else {
            editbranchhead_checkbox.setChecked(false);
        }

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = dealername_editbranch.getText().toString().trim();
                email = email_editbranch.getText().toString().trim();
                phonenumber = phone_num_editbranch.getText().toString().trim();
                branchaddress = address_editbranch.getText().toString().trim();

                if (editbranchcheckBox.isChecked()) {
                    value_dealer_service = 1;
                    String value1 = "DealerService 1";
                    Log.e("Value", value1);
                } else {
                    value_dealer_service = 0;
                    String value1 = "DealerService 0";
                    Log.e("Value", value1);
                }

                if (editbranchhead_checkbox.isChecked()) {
                    value_head_quater = 1;
                    String value1 = "Headquater 1";
                    Log.e("Value", value1);
                } else {
                    value_head_quater = 0;
                    String value1 = "Headquater 0";
                    Log.e("Value", value1);
                }
                new edit_branches().execute();
            }
        });


    }

    @Override
    public void onRestart() {
        super.onRestart();
        startActivity(getIntent());
        finish();
    }

    private class edit_branches extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();
            String newurl = null;

            String newqueriesurl = Constant.EDITBRANCHES
                    + "session_user_id=" + user.get("user_id")
                    + "&dealer_name=" + name
                    + "&dealer_mail=" + email
                    + "&branchid=" + branch_id
                    + "&mobilenumber=" + phonenumber
                    + "&branch_address=" + branchaddress
                    + "&dealer_service=" + value_dealer_service
                    + "&head_quater=" + value_head_quater
                    + "&page_name=editbranch";


            try {
                URI uri = new URI(newqueriesurl.replace(" ", "%20"));
                newurl = uri.toString();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            Log.e("newurl", newurl);

            String json = sh.makeServiceCall(newurl, ServiceHandler.POST);

            if (json != null) {

                editbranch_list = new ArrayList<>();

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    for (int k = 0; k <= jsonObj.length(); k++) {

                        result = jsonObj.getString("Result");
                        message = jsonObj.getString("message");

                        editbranchlist = new HashMap<>();
                        editbranchlist.put("Result", result);
                        editbranchlist.put("message", message);

                        editbranch_list.add(editbranchlist);
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

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (editbranchlist.get("Result").equals("1")) {
                // Toast.makeText(EditBranchesActivity.this, editbranchlist.get("message"), Toast.LENGTH_SHORT).show();
                EditBranchesActivity.this.finish();
            } else {
                // Toast.makeText(EditBranchesActivity.this, editbranchlist.get("message"), Toast.LENGTH_SHORT).show();
            }

        }
    }
}
