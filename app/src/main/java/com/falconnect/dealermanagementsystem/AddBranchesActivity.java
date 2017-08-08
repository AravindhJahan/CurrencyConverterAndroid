package com.falconnect.dealermanagementsystem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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


public class AddBranchesActivity extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private static RecyclerView addbranchrecycleview;
    private static ArrayList<ManageFooterDataModel> addbranchdata;
    public ArrayList<HashMap<String, String>> addbranch_list;
    ImageView addbranch_back;
    SavedDetailsBranch savedDetailsBranch;
    HashMap<String, String> user_branch;
    EditText dealername_addbranch, email_addbranch, phone_num_addbranch;
    EditText address_addbranch;
    CheckBox checkBox, head_checkbox;
    int value_dealer_service, value_head_quater;
    SessionManager session;
    ImageView map_views;
    HashMap<String, String> user;
    String name, email, phonenumber, branchaddress, city;
    Button addbranch_btn;
    String result, message;
    HashMap<String, String> user_address;
    AddressSavedSharedPreferences addressSavedSharedPreferences;
    ArrayList<String> headquater = new ArrayList<>();
    HashMap<String, String> addbranchlist;
    ProgressDialog barProgressDialog;
    private boolean mVisible;
    private RecyclerView.LayoutManager layoutManager;

    private String blockCharacterSet = "~#^|$%&*!()@':.;?/+-_%{[}],";

    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_branches);

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

        headquater = getIntent().getStringArrayListExtra("headquater");

        session = new SessionManager(AddBranchesActivity.this);
        user = session.getUserDetails();

        dealername_addbranch = (EditText) findViewById(R.id.dealername_addbranch);
        dealername_addbranch.setFilters(new InputFilter[]{filter});

        email_addbranch = (EditText) findViewById(R.id.email_addbranch);
        phone_num_addbranch = (EditText) findViewById(R.id.phone_num_addbranch);

        address_addbranch = (EditText) findViewById(R.id.address_addbranch);
        map_views = (ImageView) findViewById(R.id.map_views);

        addbranch_btn = (Button) findViewById(R.id.branch_submit_button);

        addbranchrecycleview = (RecyclerView) findViewById(R.id.addbranch_recycle_view);
        addbranchrecycleview.setHasFixedSize(true);
        addbranchrecycleview.setLayoutManager(new LinearLayoutManager(AddBranchesActivity.this, LinearLayoutManager.HORIZONTAL, false));

        addressSavedSharedPreferences = new AddressSavedSharedPreferences(AddBranchesActivity.this);
        savedDetailsBranch = new SavedDetailsBranch(AddBranchesActivity.this);

       /* addbranchdata = new ArrayList<ManageFooterDataModel>();

        for (int i = 0; i < MyFooterManageData.managenameArray.length; i++) {

            addbranchdata.add(new ManageFooterDataModel(
                    MyFooterManageData.managenameArray[i],
                    MyFooterManageData.managedrawableArrayWhite1[i],
                    MyFooterManageData.manageid_[i]
            ));
        }
        adapter = new ManageFooterCustomAdapter(AddBranchesActivity.this, addbranchdata);
        addbranchrecycleview.setAdapter(adapter);
*/
        addbranch_back = (ImageView) findViewById(R.id.addbranch_back);

        map_views.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = dealername_addbranch.getText().toString();
                email = email_addbranch.getText().toString();
                phonenumber = phone_num_addbranch.getText().toString();
                savedDetailsBranch.createLoginSession(name, phonenumber, email);
                Intent inten = new Intent(AddBranchesActivity.this, LocationSelection.class);
                startActivity(inten);
            }
        });

        user_address = addressSavedSharedPreferences.getAddress_details();

        user_branch = savedDetailsBranch.getUserDetails();

        if (user_branch.get("branch_name") == null) {
            dealername_addbranch.setText("");
        } else {
            dealername_addbranch.setText(user_branch.get("branch_name").toString());
        }
        if (user_branch.get("branch_email") == null) {
            email_addbranch.setText("");
        } else {
            email_addbranch.setText(user_branch.get("branch_email").toString());
        }
        if (user_branch.get("branch_number") == null) {
            phone_num_addbranch.setText("");
        } else {
            phone_num_addbranch.setText(user_branch.get("branch_number").toString());
        }

        if (user_address.get("map_address") == null) {
            address_addbranch.setText("");
        } else {
            address_addbranch.setText(user_address.get("map_address"));
        }


        addbranch_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                AddBranchesActivity.this.finish();
            }
        });

        checkBox = (CheckBox) findViewById(R.id.dealer_service_checkbox);
        head_checkbox = (CheckBox) findViewById(R.id.head_quaters_checkbox);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/sanz.ttf");

        checkBox.setTypeface(font);
        head_checkbox.setTypeface(font);

        for (int i = 0; i < headquater.size(); i++) {
            if (headquater.get(i).equals("1")) {
                head_checkbox.setVisibility(View.GONE);
            } else {
            }
        }

        addbranch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkBox.isChecked()) {
                    value_dealer_service = 1;
                    String value1 = "DealerService 1";
                    Log.e("Value", value1);
                } else {
                    value_dealer_service = 0;
                    String value1 = "DealerService 0";
                    Log.e("Value", value1);
                }

                if (head_checkbox.isChecked()) {
                    value_head_quater = 1;
                    String value1 = "Headquater 1";
                    Log.e("Value", value1);
                } else {
                    value_head_quater = 0;
                    String value1 = "Headquater 0";
                    Log.e("Value", value1);
                }


                name = dealername_addbranch.getText().toString();
                email = email_addbranch.getText().toString();
                phonenumber = phone_num_addbranch.getText().toString();
                branchaddress = address_addbranch.getText().toString();

                new add_branches().execute();
            }
        });

    }

    @Override
    public void onRestart() {
        super.onRestart();
        startActivity(getIntent());
        finish();
    }

    private class add_branches extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = ProgressDialog.show(AddBranchesActivity.this, "Loading...", "Please Wait ...", true);
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();
            String newurl = null;
            String newqueriesurl = Constant.ADDBRANCHES
                    + "session_user_id=" + user.get("user_id")
                    + "&dealer_name=" + name
                    + "&dealer_mail=" + email
                    + "&mobilenumber=" + phonenumber
                    + "&branch_address=" + branchaddress
                    + "&dealer_service=" + value_dealer_service
                    + "&head_quater=" + value_head_quater
                    + "&page_name=addbranch";

            try {
                URI uri = new URI(newqueriesurl.replace(" ", "%20"));
                newurl = uri.toString();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            Log.e("newurl", newurl);

            String json = sh.makeServiceCall(newurl, ServiceHandler.POST);

            if (json != null) {

                addbranch_list = new ArrayList<>();

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    for (int k = 0; k <= jsonObj.length(); k++) {

                        result = jsonObj.getString("Result");
                        message = jsonObj.getString("message");

                        addbranchlist = new HashMap<>();
                        addbranchlist.put("Result", result);
                        addbranchlist.put("message", message);

                        addbranch_list.add(addbranchlist);
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
                        //   Toast.makeText(getApplicationContext(), "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            barProgressDialog.dismiss();
            if (addbranchlist.get("Result").equals("1")) {
                final AlertDialog alertbox = new AlertDialog.Builder(AddBranchesActivity.this)
                        .setTitle("message")
                        .setMessage(addbranchlist.get("Branch Added Sucessfully"))
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                AddBranchesActivity.this.finish();
                            }
                        })
                        .show();
            } else {
                final AlertDialog alertbox = new AlertDialog.Builder(AddBranchesActivity.this)
                        .setTitle("Error")
                        .setMessage(addbranchlist.get("message"))
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        })
                        .show();
            }
        }
    }

}
