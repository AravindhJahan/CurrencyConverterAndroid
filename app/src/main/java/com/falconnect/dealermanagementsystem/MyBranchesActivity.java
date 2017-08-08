package com.falconnect.dealermanagementsystem;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.bumptech.glide.Glide;
import com.falconnect.dealermanagementsystem.Adapter.CustomList;
import com.falconnect.dealermanagementsystem.Adapter.ManageFooterCustomAdapter;
import com.falconnect.dealermanagementsystem.Adapter.MyBranchListAdapter;
import com.falconnect.dealermanagementsystem.FontAdapter.RoundImageTransform;
import com.falconnect.dealermanagementsystem.Model.ManageFooterDataModel;
import com.falconnect.dealermanagementsystem.Model.MyBranchListModel;
import com.falconnect.dealermanagementsystem.NavigationDrawer.BuyPageNavigation;
import com.falconnect.dealermanagementsystem.SharedPreference.AddressSavedSharedPreferences;
import com.falconnect.dealermanagementsystem.SharedPreference.PlandetailsSharedPreferences;
import com.falconnect.dealermanagementsystem.SharedPreference.ProfileManagerSession;
import com.falconnect.dealermanagementsystem.SharedPreference.SavedDetailsBranch;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;
import com.freshdesk.hotline.FaqOptions;
import com.freshdesk.hotline.Hotline;
import com.navdrawer.SimpleSideDrawer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MyBranchesActivity extends AppCompatActivity {

    static SwipeMenuListView branch_listview;
    private static RecyclerView.Adapter adapter;
    private static RecyclerView branchrecycleview;
    private static ArrayList<ManageFooterDataModel> branchdata;
    public ArrayList<HashMap<String, String>> branch_list;
    public ArrayList<HashMap<String, String>> delete_branch_list;
    AddressSavedSharedPreferences sessionManagerAddress;
    SavedDetailsBranch savedDetailsBranch;
    SessionManager session;
    HashMap<String, String> user;
    ImageView imageView_branch;
    TextView profile_name_branch;
    TextView profile_address_manage;
    String saved_name_branch, saved_address_branch;
    BuyPageNavigation branch_buypagenavigation;
    MyBranchListAdapter myBranchListAdapter;
    ImageView plus_mybranch;
    HashMap<String, String> branchlist;
    HashMap<String, String> deletebranchlist;
    ArrayList<String> branch;
    MyBranchListModel item;
    ArrayList<String> headquater = new ArrayList<>();
    String headquarter;
    String branch_ids;
    JSONArray apply_fund;
    ProgressDialog barProgressDialog;
    ProfileManagerSession profileManagerSession;
    RelativeLayout result_found;
    PlandetailsSharedPreferences plandetailsSharedPreferences;
    HashMap<String, String> plan;
    private boolean mVisible;
    private RecyclerView.LayoutManager layoutManager;
    private SimpleSideDrawer mNav_branch;
    private ArrayList<MyBranchListModel> branchdatanew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_branches);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mVisible = true;
        if (mVisible) {
            android.support.v7.app.ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
            mVisible = false;

        } else {
        }

        branchrecycleview = (RecyclerView) findViewById(R.id.branch_recyclerview);
        branchrecycleview.setHasFixedSize(true);
        branchrecycleview.setLayoutManager(new LinearLayoutManager(MyBranchesActivity.this, LinearLayoutManager.HORIZONTAL, false));

        branchdata = new ArrayList<ManageFooterDataModel>();

        for (int i = 0; i < MyFooterManageData.managenameArray.length; i++) {

            branchdata.add(new ManageFooterDataModel(
                    MyFooterManageData.managenameArray[i],
                    MyFooterManageData.managedrawableArrayWhite2[i],
                    MyFooterManageData.manageid_[i]
            ));
        }
        adapter = new ManageFooterCustomAdapter(MyBranchesActivity.this, branchdata);
        branchrecycleview.setAdapter(adapter);

        session = new SessionManager(MyBranchesActivity.this);
        user = session.getUserDetails();

        ImageView branch_nav = (ImageView) findViewById(R.id.manage_branches_mnav);
        mNav_branch = new SimpleSideDrawer(this);
        mNav_branch.setLeftBehindContentView(R.layout.activity_behind_left_simple);

        imageView_branch = (ImageView) mNav_branch.findViewById(R.id.profile_avatar);
        profile_name_branch = (TextView) mNav_branch.findViewById(R.id.profile_name);
        profile_address_manage = (TextView) mNav_branch.findViewById(R.id.profile_address);
        user = session.getUserDetails();
        saved_name_branch = user.get("dealer_name");
        saved_address_branch = user.get("dealershipname");
        profile_name_branch.setText(saved_name_branch);

        result_found = (RelativeLayout) findViewById(R.id.result_found);

        RelativeLayout call_layout = (RelativeLayout) mNav_branch.findViewById(R.id.call_layout);
        RelativeLayout chat_layout = (RelativeLayout) mNav_branch.findViewById(R.id.chat_layout);
        RelativeLayout logout_layout = (RelativeLayout) mNav_branch.findViewById(R.id.logout_layout);

        call_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertbox = new AlertDialog.Builder(MyBranchesActivity.this)
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
                mNav_branch.closeLeftSide();
                MyBranchesActivity.this.finish();
            }
        });

        chat_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chat();
                mNav_branch.closeLeftSide();
            }
        });

        if (user.get("dealer_img").isEmpty()) {
            Glide.with(getApplicationContext())
                    .load(R.drawable.default_avatar)
                    .transform(new RoundImageTransform(MyBranchesActivity.this))
                    .into(imageView_branch);
        } else {
            Glide.with(getApplicationContext())
                    .load(user.get("dealer_img"))
                    .transform(new RoundImageTransform(MyBranchesActivity.this))
                    .into(imageView_branch);
        }
        profile_address_manage.setText(saved_address_branch);

        branch_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNav_branch.toggleLeftDrawer();
            }
        });

        //NAVIGATION DRAWER LIST VIEW
        branch_buypagenavigation = new BuyPageNavigation();
        CustomList adapter = new CustomList(MyBranchesActivity.this, BuyPageNavigation.web, BuyPageNavigation.imageId);
        ListView list = (ListView) findViewById(R.id.nav_list_view);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (BuyPageNavigation.web[position] == "Dashboard") {
                    Intent intent = new Intent(MyBranchesActivity.this, MainDashBoardActivity.class);
                    startActivity(intent);
                    MyBranchesActivity.this.finish();
                    mNav_branch.closeLeftSide();
                    //  Toast.makeText(MyBranchesActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();

                } else if (BuyPageNavigation.web[position] == "Buy") {
                    Intent intent = new Intent(MyBranchesActivity.this, DashBoard.class);
                    startActivity(intent);
                    MyBranchesActivity.this.finish();
                    mNav_branch.closeLeftSide();
                    //  Toast.makeText(MyBranchesActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();

                } else if (BuyPageNavigation.web[position] == "Sell") {
                    Intent intent = new Intent(MyBranchesActivity.this, SellDashBoardActivity.class);
                    startActivity(intent);
                    MyBranchesActivity.this.finish();
                    mNav_branch.closeLeftSide();
                    // Toast.makeText(MyBranchesActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Manage") {
                    mNav_branch.closeLeftSide();
                    profileManagerSession = new ProfileManagerSession(MyBranchesActivity.this);
                    profileManagerSession.clear_ProfileManage();
                    Intent intet = new Intent(MyBranchesActivity.this, ManageDashBoardActivity.class);
                    startActivity(intet);
                 } else if (BuyPageNavigation.web[position] == "Funding") {
                    plandetailsSharedPreferences = new PlandetailsSharedPreferences(MyBranchesActivity.this);
                    plan = plandetailsSharedPreferences.getUserDetails();

//                    if ( plan.get("PlanName").equals("BASIC")) {
//
//                        Intent intent = new Intent(MyBranchesActivity.this, LoanActivity.class);
//                        startActivity(intent);
//                        mNav_branch.closeLeftSide();
//                        MyBranchesActivity.this.finish();
//                    } else {
                        Intent intent = new Intent(MyBranchesActivity.this, FundingActivity.class);
                        startActivity(intent);
                        mNav_branch.closeLeftSide();
                        MyBranchesActivity.this.finish();
//                    }
                 } else if (BuyPageNavigation.web[position] == "Networks") {
                    String url = "http://app.dealerplus.in/user_login_cometchat?session_user_id=" + user.get("user_id");
                    Intent intet = new Intent(MyBranchesActivity.this, NetworksWebviewActivity.class);
                    //intet.putExtra("title", BuyPageNavigation.web[position]);
                    intet.putExtra("url", url);
                    startActivity(intet);
                    mNav_branch.closeLeftSide();
                 } else if (BuyPageNavigation.web[position] == "Reports") {
                    plandetailsSharedPreferences = new PlandetailsSharedPreferences(MyBranchesActivity.this);
                    plan = plandetailsSharedPreferences.getUserDetails();
                    if (plan.get("PlanName").equals("BASIC")) {
                        Intent intent = new Intent(MyBranchesActivity.this, BuyPlanActivity.class);
                        startActivity(intent);
                        //MainDashBoardActivity.this.finish();
                        mNav_branch.closeLeftSide();
                    } else {
                        Intent intent = new Intent(MyBranchesActivity.this, ReportSalesActivity.class);
                        startActivity(intent);
                        MyBranchesActivity.this.finish();
                        mNav_branch.closeLeftSide();
                    }
                 } else if (BuyPageNavigation.web[position] == "FAQs") {
                    faqs();
                    mNav_branch.closeLeftSide();
                } else {
                 }
            }
        });

        plus_mybranch = (ImageView) findViewById(R.id.plus_mybranch);

        new branch_list().execute();
    }

    public void faqs() {
        FaqOptions faqOptions = new FaqOptions()
                .showFaqCategoriesAsGrid(true)
                .showContactUsOnAppBar(true)
                .showContactUsOnFaqScreens(false)
                .showContactUsOnFaqNotHelpful(false);

        Hotline.showFAQs(MyBranchesActivity.this, faqOptions);
    }

    public void chat() {
        Hotline.showConversations(getApplicationContext());
        // Hotline.clearUserData(getApplicationContext());
    }

    private ArrayList<MyBranchListModel> getbranchlist() {
        branchdatanew = new ArrayList<>();
        for (int i = 0; i < branch_list.size(); i++) {
            String branch_id = branch_list.get(i).get("branch_id");
            String dealer_name = branch_list.get(i).get("dealer_name");
            String dealer_contact_no = branch_list.get(i).get("dealer_contact_no");
            String branch_address = branch_list.get(i).get("branch_address");
            String dealer_mail = branch_list.get(i).get("dealer_mail");
            String dealer_state = branch_list.get(i).get("dealer_state");
            String dealer_city = branch_list.get(i).get("dealer_city");
            String dealer_pincode = branch_list.get(i).get("dealer_pincode");
            String dealer_status = branch_list.get(i).get("dealer_status");
            String dealer_service = branch_list.get(i).get("dealer_service");
            String head_quater = branch_list.get(i).get("headquater");

            branchdatanew.add(new MyBranchListModel(branch_id, dealer_name, dealer_contact_no, branch_address,
                    dealer_mail, dealer_state, dealer_city, dealer_pincode, dealer_status, dealer_service, head_quater));
        }
        return branchdatanew;
    }


    @Override
    public void onBackPressed() {

        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to exit application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        MyBranchesActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();

    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    public void onRestart() {
        super.onRestart();
        startActivity(getIntent());
        finish();
    }

    private class branch_list extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = ProgressDialog.show(MyBranchesActivity.this, "Loading...", "Please Wait ...", true);

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();

            String queriesurl = Constant.MYBRANCHESLIST
                    + "session_user_id=" + user.get("user_id")
                    + "&page_name=viewbranchlist";

            String json = sh.makeServiceCall(queriesurl, ServiceHandler.POST);

            if (json != null) {

                branch_list = new ArrayList<>();

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    headquarter = jsonObj.getString("headquarter");

                    apply_fund = jsonObj.getJSONArray("branch_list");

                    if (apply_fund != null) {

                        for (int k = 0; k <= apply_fund.length(); k++) {

                            String branch_id = apply_fund.getJSONObject(k).getString("branch_id");
                            String dealer_name = apply_fund.getJSONObject(k).getString("dealer_name");
                            String dealer_contact_no = apply_fund.getJSONObject(k).getString("dealer_contact_no");
                            String branch_address = apply_fund.getJSONObject(k).getString("branch_address");
                            String dealer_mail = apply_fund.getJSONObject(k).getString("dealer_mail");
                            String dealer_state = apply_fund.getJSONObject(k).getString("dealer_state");
                            String dealer_city = apply_fund.getJSONObject(k).getString("dealer_city");
                            String dealer_pincode = apply_fund.getJSONObject(k).getString("dealer_pincode");
                            String dealer_status = apply_fund.getJSONObject(k).getString("dealer_status");
                            String dealer_service = apply_fund.getJSONObject(k).getString("dealer_service");
                            String head_quater = apply_fund.getJSONObject(k).getString("head_quater");

                            branchlist = new HashMap<>();

                            branchlist.put("branch_id", branch_id);
                            branchlist.put("dealer_name", dealer_name);
                            branchlist.put("dealer_contact_no", dealer_contact_no);
                            branchlist.put("branch_address", branch_address);
                            branchlist.put("dealer_mail", dealer_mail);
                            branchlist.put("dealer_state", dealer_state);
                            branchlist.put("dealer_city", dealer_city);
                            branchlist.put("dealer_pincode", dealer_pincode);
                            branchlist.put("dealer_status", dealer_status);
                            branchlist.put("dealer_service", dealer_service);
                            branchlist.put("headquater", head_quater);

                            branch_list.add(branchlist);

                            headquater.add(head_quater);

                        }
                    } else {
                        Toast.makeText(MyBranchesActivity.this, "Null Pointer", Toast.LENGTH_SHORT).show();
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

            branch_listview = (SwipeMenuListView) findViewById(R.id.branch_listview);
            myBranchListAdapter = new MyBranchListAdapter(MyBranchesActivity.this, getbranchlist());
            //branch_listview.setAdapter(myBranchListAdapter);

            if (myBranchListAdapter.getCount() != 0) {
                branch_listview.setAdapter(myBranchListAdapter);
            } else {
                branch_listview.setVisibility(View.GONE);
                result_found.setVisibility(View.VISIBLE);
            }

            plus_mybranch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    sessionManagerAddress = new AddressSavedSharedPreferences(MyBranchesActivity.this);
                    sessionManagerAddress.clear_address();

                    savedDetailsBranch = new SavedDetailsBranch(MyBranchesActivity.this);
                    savedDetailsBranch.clear_details_brach_Datas();

                    Intent intet = new Intent(MyBranchesActivity.this, AddBranchesActivity.class);
                    intet.putExtra("headquater", headquater);
                    startActivity(intet);
                }
            });

            // step 1. create a MenuCreator
            SwipeMenuCreator creator = new SwipeMenuCreator() {

                @Override
                public void create(SwipeMenu menu) {
                    SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
                    openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                    openItem.setWidth(dp2px(90));
                    openItem.setIcon(R.drawable.swipe_edit);
                    menu.addMenuItem(openItem);

                    SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                    deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                    deleteItem.setWidth(dp2px(90));
                    deleteItem.setIcon(R.drawable.swipe_delete);
                    menu.addMenuItem(deleteItem);

                }
            };
            // set creator
            branch_listview.setMenuCreator(creator);

            // step 2. listener item click event
            branch_listview.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                    if (branchdatanew == null || branchdatanew.size() <= 0) {

                    } else {

                        switch (index) {
                            case 0:

                                sessionManagerAddress = new AddressSavedSharedPreferences(MyBranchesActivity.this);
                                sessionManagerAddress.clear_address();

                                item = branchdatanew.get(position);
                                branch = new ArrayList<String>();
                                branch.add(headquarter);
                                branch.add(item.getBranch_id());
                                branch.add(item.getDealer_name());
                                branch.add(item.getDealer_contact_no());
                                branch.add(item.getBranch_address());
                                branch.add(item.getDealer_mail());
                                branch.add(item.getDealer_state());
                                branch.add(item.getDealer_city());
                                branch.add(item.getDealer_pincode());
                                branch.add(item.getDealer_status());
                                branch.add(item.getDealer_service());
                                branch.add(item.getHead_quater());

                                Intent intent = new Intent(MyBranchesActivity.this, EditBranchesActivity.class);
                                intent.putExtra("branchlist", branch);
                                startActivity(intent);

                                SavedDetailsBranch savedDetailsBranch = new SavedDetailsBranch(MyBranchesActivity.this);
                                savedDetailsBranch.clear_details_brach_Datas();


                                break;
                            case 1:
                                item = branchdatanew.get(position);
                                branch_ids = item.getBranch_id();
                                AlertDialog alertbox = new AlertDialog.Builder(MyBranchesActivity.this)
                                        .setMessage("Do you want to Delete this Branch?")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                new branch_api_delete().execute();
                                            }
                                        })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface arg0, int arg1) {
                                            }
                                        })
                                        .show();

                                break;
                        }
                    }
                    return false;
                }
            });

            // set SwipeListener
            branch_listview.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

                @Override
                public void onSwipeStart(int position) {
                    // swipe start
                }

                @Override
                public void onSwipeEnd(int position) {
                    // swipe end
                }
            });

            // set MenuStateChangeListener
            branch_listview.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
                @Override
                public void onMenuOpen(int position) {
                }

                @Override
                public void onMenuClose(int position) {
                }
            });
        }
    }

    private class branch_api_delete extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();
            String queriesurl = Constant.DELETE_BRANCH +
                    "session_user_id=" + user.get("user_id")
                    + "&page_name=deletebranch"
                    + "&branchid=" + branch_ids;

            Log.e("queriesurl", queriesurl);

            String json = sh.makeServiceCall(queriesurl, ServiceHandler.POST);

            if (json != null) {

                delete_branch_list = new ArrayList<>();
                try {
                    JSONObject jsonObj = new JSONObject(json);

                    for (int k = 0; k <= jsonObj.length(); k++) {

                        String Result = jsonObj.getString("Result");
                        String Message = jsonObj.getString("message");

                        deletebranchlist = new HashMap<>();

                        deletebranchlist.put("Result", Result);
                        deletebranchlist.put("Message", Message);

                        delete_branch_list.add(deletebranchlist);

                    }

                } catch (final JSONException e) {

                }

            } else {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (deletebranchlist.get("Result").equals("3")) {
                //myBranchListAdapter.notifyDataSetChanged();
                new branch_list().execute();
            } else {

            }
        }
    }


}
