package com.falconnect.dealermanagementsystem;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.falconnect.dealermanagementsystem.Adapter.ContactListAdapter;
import com.falconnect.dealermanagementsystem.Model.ContactModel;
import com.falconnect.dealermanagementsystem.Model.ManageFooterDataModel;
import com.falconnect.dealermanagementsystem.NavigationDrawer.BuyPageNavigation;
import com.falconnect.dealermanagementsystem.SharedPreference.AddressSavedSharedPreferences;
import com.falconnect.dealermanagementsystem.SharedPreference.InventorySavedDetails;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;
import com.navdrawer.SimpleSideDrawer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SelectContactActivity extends FragmentActivity {

    private static RecyclerView.Adapter adapter;
    private static RecyclerView contactrecycleview;
    private static ArrayList<ManageFooterDataModel> contactdatas;
    public ArrayList<HashMap<String, String>> contact_listview;
    public ArrayList<HashMap<String, String>> delete_branch_list;
    ImageView contact_nav;
    SessionManager session;
    ImageView imageView_contact;
    HashMap<String, String> user;
    TextView profile_name_contact;
    TextView profile_address_contact;
    String saved_name_contact, saved_address_contact;
    ContactListAdapter contactListAdapter;
    HashMap<String, String> contactlistview;
    InventorySavedDetails inventorySavedDetails;
    HashMap<String, String> deletebranchlist;
    int value = 0;
    AddressSavedSharedPreferences sessionManagerAddress;
    ArrayList<String> contact_list;
    HashMap<String, String> tabcontactlistview;
    ArrayList<String> list_new = new ArrayList<String>();
    ArrayList<String> list_new_id_contact = new ArrayList<String>();
    ArrayList<String> list = new ArrayList<String>();
    BuyPageNavigation contact_buypagenavigation;
    ImageView plus_contact;
    ArrayList<ContactModel> contactdata;
    ContactModel contactModel;
    ProgressDialog barProgressDialog;
    String Contact_id;
    private boolean mVisible;
    private RecyclerView.LayoutManager layoutManager;
    private SimpleSideDrawer mNav_contact;
    private ListView contact_listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_contact);

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mVisible = true;


        barProgressDialog = ProgressDialog.show(SelectContactActivity.this, "Loading...", "Please Wait ...", true);

        contact_nav = (ImageView) findViewById(R.id.contact_nav);

        session = new SessionManager(SelectContactActivity.this);
        user = session.getUserDetails();

        new my_user_view().execute();

        contact_nav.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               SelectContactActivity.this.finish();
                                           }
                                       }
        );

    }


    private ArrayList<ContactModel> getContactData() {
        contactdata = new ArrayList<>();
        for (int i = 0; i < contact_listview.size(); i++) {

            String name = contact_listview.get(i).get("name");
            String mobilenum = contact_listview.get(i).get("mobilenum");
            String email = contact_listview.get(i).get("email");
            String contactimage = contact_listview.get(i).get("contactimage");
            String address = contact_listview.get(i).get("address");
            String contact_id = contact_listview.get(i).get("contact_id");
            String contact_owner = contact_listview.get(i).get("contact_owner");
            String contact_type_id = contact_listview.get(i).get("contact_type_id");
            String contact_type_name = contact_listview.get(i).get("contact_type_name");
            String pan_number = contact_listview.get(i).get("pan_number");
            String contact_gender = contact_listview.get(i).get("contact_gender");
            String contact_email = contact_listview.get(i).get("contact_email");
            String contact_sms = contact_listview.get(i).get("contact_sms");
            String lead_makeid = contact_listview.get(i).get("lead_makeid");
            String lead_cityid = contact_listview.get(i).get("lead_cityid");
            String lead_modelid = contact_listview.get(i).get("lead_modelid");
            String lead_prcie = contact_listview.get(i).get("lead_prcie");
            String lead_time = contact_listview.get(i).get("lead_time");
            String lead_cityname = contact_listview.get(i).get("lead_cityname");
            String lead_makename = contact_listview.get(i).get("lead_makename");
            String lead_modelname = contact_listview.get(i).get("lead_modelname");
            String lead_timename = contact_listview.get(i).get("lead_timename");
            String lead_pricename = contact_listview.get(i).get("lead_pricename");
            String dealer_document_management_id = contact_listview.get(i).get("dealer_document_management_id");
            String contact_management_id = contact_listview.get(i).get("contact_management_id");
            String document_id_type = contact_listview.get(i).get("document_id_type");
            String document_id_number = contact_listview.get(i).get("document_id_number");
            String document_dob = contact_listview.get(i).get("document_dob");
            String doc_link_fullpath = contact_listview.get(i).get("doc_link_fullpath");
            String document_name = contact_listview.get(i).get("document_name");



            contactdata.add(new ContactModel(name, mobilenum, email, contactimage, address, contact_id, contact_owner,
                    contact_type_id, contact_type_name, pan_number, contact_gender, contact_email, contact_sms, lead_cityname,
                    lead_makename, lead_modelname, lead_prcie, lead_time, lead_makeid,lead_cityid,lead_modelid,dealer_document_management_id, contact_management_id, document_id_type, document_id_number,
                    document_dob, doc_link_fullpath, document_name, lead_timename, lead_pricename));
        }
        return contactdata;
    }

    @Override
    public void onBackPressed() {

        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to exit application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        SelectContactActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();

    }


    @Override
    public void onRestart() {
        super.onRestart();
        startActivity(getIntent());
        finish();
    }


    private class my_user_view extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();
            String queriesurl = Constant.CONTACTLIST +
                    "session_user_id=" + user.get("user_id")
                    + "&page_name=viewcontactlist"
                    + "&contact_type=1";

            Log.e("queriesurl", queriesurl);

            String json = sh.makeServiceCall(queriesurl, ServiceHandler.POST);

            if (json != null) {

                contact_listview = new ArrayList<>();

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    JSONArray loan = jsonObj.getJSONArray("contact_list");

                    for (int k = 0; k <= loan.length(); k++) {

                        String name = loan.getJSONObject(k).getString("name");
                        String mobilenum = loan.getJSONObject(k).getString("mobilenum");
                        String email = loan.getJSONObject(k).getString("email");
                        String contactimage = loan.getJSONObject(k).getString("contactimage");
                        String address = loan.getJSONObject(k).getString("address");
                        String contact_id = loan.getJSONObject(k).getString("contact_id");
                        String contact_owner = loan.getJSONObject(k).getString("contact_owner");
                        String contact_type_id = loan.getJSONObject(k).getString("contact_type_id");
                        String contact_type_name = loan.getJSONObject(k).getString("contact_type_name");
                        String pan_number = loan.getJSONObject(k).getString("pan_number");


                        contactlistview = new HashMap<>();

                        contactlistview.put("name", name);
                        contactlistview.put("mobilenum", mobilenum);
                        contactlistview.put("email", email);
                        contactlistview.put("contactimage", contactimage);
                        contactlistview.put("address", address);
                        contactlistview.put("contact_id", contact_id);
                        contactlistview.put("contact_owner", contact_owner);
                        contactlistview.put("contact_type_id", contact_type_id);
                        contactlistview.put("contact_type_name", contact_type_name);
                        contactlistview.put("pan_number", pan_number);


                        contact_listview.add(contactlistview);

                    }

                } catch (final JSONException e) {

                }

            } else {

                // Toast.makeText(SelectContactActivity.this, "No data is there!!!", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            barProgressDialog.dismiss();


            contact_listView = (ListView) findViewById(R.id.contact_listView);

            contactListAdapter = new ContactListAdapter(SelectContactActivity.this, getContactData());
            contact_listView.setAdapter(contactListAdapter);


            contact_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    contactModel = (ContactModel) parent.getItemAtPosition(position);

                    contact_list = new ArrayList<String>();

                    contact_list.add(contactModel.getContactimage());
                    contact_list.add(contactModel.getContact_owner());
                    contact_list.add(contactModel.getName());
                    contact_list.add(contactModel.getMobilenum());
                    contact_list.add(contactModel.getEmail());
                    contact_list.add(contactModel.getAddress());
                    contact_list.remove(contactModel.getContact_id());
                    contact_list.add(contactModel.getContact_type_id());
                    contact_list.add(contactModel.getContact_type_name());
                    contact_list.add(contactModel.getPan_number());

                    inventorySavedDetails = new InventorySavedDetails(SelectContactActivity.this);
                    inventorySavedDetails.createInventoryDetailsSession(contactModel.getContactimage(), contactModel.getName(), contactModel.getPan_number(), contactModel.getEmail(), contactModel.getMobilenum(), null, null, null, contactModel.getContact_id());

                    Intent intent = new Intent(SelectContactActivity.this, ApplyFundingActivity.class);
                    intent.putExtra("contact_list", contact_list);
                    startActivity(intent);

                    SelectContactActivity.this.finish();

                }
            });

        }
    }

}
