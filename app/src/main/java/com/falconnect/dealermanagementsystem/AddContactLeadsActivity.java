package com.falconnect.dealermanagementsystem;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AddContactLeadsActivity extends AppCompatActivity {

    SessionManager sessionManager;
    HashMap<String, String> user;
    ProgressDialog barProgressDialog;
    TextView region, make, model, price, timeline;
    ActionBar actionBar;
    String Contactid;
    ArrayList<String> city_id = new ArrayList<>();
    ArrayList<String> make_id = new ArrayList<>();
    ArrayList<String> model_id = new ArrayList<>();
    ArrayList<String> price_id = new ArrayList<>();
    ArrayList<String> timeline_id = new ArrayList<>();
    HashMap<String, String> resultmap;
    String id_city, id_model, id_price, id_timeline;
    String region_name, make_name, model_name, price_name, time_name;
    public ArrayList<HashMap<String, String>> result_hashmap;

    //City
    String get_city_id, get_city_name;
    HashMap<String, String> citylist;
    public ArrayList<HashMap<String, String>> city_spinner_list;
    private ArrayList<String> spinner_datas = new ArrayList<>();
    private ArrayList<String> spinner_datas_id = new ArrayList<>();

    //Make
    public ArrayList<HashMap<String, String>> make_spinner_list;
    HashMap<String, String> makelist;
    String get_brand_id, get_brand_name;
    private ArrayList<String> make_datas = new ArrayList<>();
    ArrayList<String> make_ids = new ArrayList<>();
    String idss;
    String selectedItem;

    ///Model
    public ArrayList<HashMap<String, String>> model_spinner_list;
    ArrayList<String> model_datas = new ArrayList<>();
    ArrayList<String> model_datas_id = new ArrayList<>();
    String get_model_id, get_model_name;
    HashMap<String, String> modelist;


    //Price
    public ArrayList<HashMap<String, String>> budget_spinner_list;
    String budget_id, budget_name, int_bud_id;
    HashMap<String, String> budgetlist;
    ArrayList<String> budget_datas = new ArrayList<>();
    ArrayList<String> budget_datas_ids = new ArrayList<>();

    //
    ArrayList<String> timeline_datas = new ArrayList<>();

    Button save_btn_leads, back_leads, bnext_leads;
    ImageView leads_details_back;
    ArrayList<String> makes_idss = new ArrayList<>();

    ArrayList<String> contact_list = new ArrayList<>();
    String regi, make_select, model_select, time_line, price_select;

    String add_contact;

    String regi_id, make_select_id, model_select_id, time_line_id, price_select_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact_leads);

        actionBar = getSupportActionBar();
        actionBar.hide();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sessionManager = new SessionManager(AddContactLeadsActivity.this);
        user = sessionManager.getUserDetails();

        initilize();

        Contactid = getIntent().getStringExtra("Contactid");


        add_contact = getIntent().getStringExtra("add_contact");
        contact_list = getIntent().getStringArrayListExtra("contact_list");

        if (add_contact == "0") {


            contact_list = getIntent().getStringArrayListExtra("contact_list");

            regi = contact_list.get(13).toString();
            regi = regi.replace("[", "");
            regi = regi.replace("]", "");
            regi = regi.replace("\"", "");

            make_select = contact_list.get(14).toString();
            make_select = make_select.replace("[", "");
            make_select = make_select.replace("]", "");
            make_select = make_select.replace("\"", "");

            model_select = contact_list.get(15).toString();
            model_select = model_select.replace("[", "");
            model_select = model_select.replace("]", "");
            model_select = model_select.replace("\"", "");

            price_select = contact_list.get(18).toString();
            price_select = price_select.replace("[", "");
            price_select = price_select.replace("]", "");
            price_select = price_select.replace("\"", "");

            time_line = contact_list.get(19).toString();
            time_line = time_line.replace("[", "");
            time_line = time_line.replace("]", "");
            time_line = time_line.replace("\"", "");

            if (contact_list.get(13) == null || contact_list.get(13) == "" || contact_list.get(13).length() == 0) {
                region.setText("Select Region");
            } else {
                region.setText(regi);
            }

            if (contact_list.get(14) == null || contact_list.get(14) == "" || contact_list.get(14).length() == 0) {
                make.setText("Select Make");
            } else {
                make.setText(make_select);
            }

            if (contact_list.get(15) == null || contact_list.get(15) == "" || contact_list.get(15).length() == 0) {
                model.setText("Select Model");
            } else {
                model.setText(model_select);
            }

            if (contact_list.get(18) == null || contact_list.get(18) == "" || contact_list.get(18).length() == 0) {
                price.setText("Select Price Range");
            } else {
                price.setText(price_select);
            }

            if (contact_list.get(19) == null || contact_list.get(19) == "" || contact_list.get(19).length() == 0) {
                timeline.setText("Select TimeLine");
            } else {
                timeline.setText(time_line);
            }

        }

        region.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner_datas.clear();
                spinner_datas_id.clear();
                city_id.clear();
                new City_Datas().execute();
            }
        });

        make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                make_datas.clear();
                make_ids.clear();
                make_id.clear();
                new Make_Datas().execute();
            }
        });

        timeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeline_datas.clear();
                timeline_id.clear();
                timeline_data();
            }
        });

        model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model_datas.clear();
                model_id.clear();
                model_datas_id.clear();
                new Sub_model().execute();
            }
        });

        price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                budget_datas.clear();
                budget_datas_ids.clear();
                price_id.clear();
                new Budget_Datas().execute();
            }
        });

        bnext_leads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new document_lead_details().execute();
            }
        });

        leads_details_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddContactLeadsActivity.this.finish();
            }
        });

        back_leads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddContactLeadsActivity.this.finish();
            }
        });

        save_btn_leads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new save_lead_details().execute();
            }
        });
    }

    public void initilize() {
        region = (TextView) findViewById(R.id.region);
        make = (TextView) findViewById(R.id.manufacture_make);
        model = (TextView) findViewById(R.id.model);
        price = (TextView) findViewById(R.id.price);
        timeline = (TextView) findViewById(R.id.timeline);

        bnext_leads = (Button) findViewById(R.id.bnext_leads);
        back_leads = (Button) findViewById(R.id.back_leads);
        save_btn_leads = (Button) findViewById(R.id.save_btn_leads);

        leads_details_back = (ImageView) findViewById(R.id.leads_details_back);
    }

    private class City_Datas extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = ProgressDialog.show(AddContactLeadsActivity.this, "Loading...", "Please Wait ...", true);

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();
            String city_url = Constant.DASH_BOARD_SPINNER_API;
            String json = sh.makeServiceCall(city_url, ServiceHandler.GET);
            if (json != null) {
                city_spinner_list = new ArrayList<>();
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    JSONArray city = jsonObj.getJSONArray("model_city");
                    for (int k = 0; k <= city.length(); k++) {
                        get_city_id = city.getJSONObject(k).getString("city_id");
                        get_city_name = city.getJSONObject(k).getString("city_name");
                        citylist = new HashMap<>();
                        citylist.put("city_id", get_city_id);
                        citylist.put("city_name", get_city_name);
                        city_spinner_list.add(citylist);
                        spinner_datas.add(get_city_name);
                        spinner_datas_id.add(get_city_id);
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
            region.setText("Select Region");

            AlertDialog.Builder builder = new AlertDialog.Builder(AddContactLeadsActivity.this);

            String[] str = new String[spinner_datas.size()];

            for (int f = 0; f < spinner_datas.size(); f++) {
                str[f] = spinner_datas.get(f);
            }

            String[] str_id = new String[spinner_datas_id.size()];

            for (int q = 0; q < spinner_datas_id.size(); q++) {
                str_id[q] = spinner_datas_id.get(q);
            }

            Log.e("str", str.toString());

            final boolean[] checkedColors = new boolean[spinner_datas.size()];
            for (int m = 0; m < spinner_datas.size(); m++) {
                checkedColors[m] = false;
            }

            final boolean[] checkedColors_id = new boolean[spinner_datas_id.size()];
            for (int m = 0; m < spinner_datas_id.size(); m++) {
                checkedColors_id[m] = false;
            }

            final List<String> colorsList = Arrays.asList(str);
            final List<String> colorsList_id = Arrays.asList(str_id);

            builder.setMultiChoiceItems(str, checkedColors, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    checkedColors[which] = isChecked;
                    String currentItem = colorsList.get(which);
                    Toast.makeText(getApplicationContext(), currentItem + " " + isChecked, Toast.LENGTH_SHORT).show();
                }
            });

            builder.setCancelable(false);
            builder.setTitle("Select Region");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    for (int i = 0; i < checkedColors.length; i++) {
                        boolean checked = checkedColors[i];
                        if (checked) {
                            String values = region.getText() + colorsList.get(i) + ",";
                            values = values.replace("Select Region", "");
                            region.setText(values);

                            city_id.add(colorsList_id.get(i));
                            id_city = city_id.toString();
                            id_city = id_city.replace("[", "");
                            id_city = id_city.replace("]", "");
                            id_city = id_city.replace(" ", "");
                            Log.e("city_id", id_city);
                        }
                    }
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (contact_list.get(13) == null) {
                        region.setText("Select Region");
                    } else {
                        String regi = contact_list.get(13).toString();
                        regi = regi.replace("[", "");
                        regi = regi.replace("]", "");
                        regi = regi.replace("\"", "");

                        region.setText(regi);
                    }

                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }

    private class Make_Datas extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = ProgressDialog.show(AddContactLeadsActivity.this, "Loading...", "Please Wait ...", true);
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();

            String city_url = Constant.DASH_BOARD_SPINNER_API;

            String json = sh.makeServiceCall(city_url, ServiceHandler.GET);

            if (json != null) {

                make_spinner_list = new ArrayList<>();
                make_datas = new ArrayList<>();
                try {
                    JSONObject jsonObj = new JSONObject(json);

                    JSONArray make = jsonObj.getJSONArray("model_make");

                    for (int j = 0; j <= make.length(); j++) {
                        get_brand_id = make.getJSONObject(j).getString("make_id");
                        get_brand_name = make.getJSONObject(j).getString("makename");

                        makelist = new HashMap<>();

                        makelist.put("make_id", get_brand_id);
                        makelist.put("makename", get_brand_name);

                        make_spinner_list.add(makelist);

                        make_datas.add(get_brand_name);
                        make_ids.add(get_brand_id);
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
            make.setText("Select Manufacturing Make");
            AlertDialog.Builder builder = new AlertDialog.Builder(AddContactLeadsActivity.this);

            String[] str = new String[make_datas.size()];
            for (int f = 0; f < make_datas.size(); f++) {
                str[f] = make_datas.get(f);
            }

            String[] str_id = new String[make_ids.size()];
            for (int r = 0; r < make_ids.size(); r++) {
                str_id[r] = make_ids.get(r);
            }

            Log.e("str", str_id.toString());

            final boolean[] checkedColors = new boolean[make_datas.size()];
            for (int m = 0; m < make_datas.size(); m++) {
                checkedColors[m] = false;
            }

            final boolean[] checkedColors_id = new boolean[make_ids.size()];
            for (int m = 0; m < make_ids.size(); m++) {
                checkedColors_id[m] = false;
            }
            final List<String> colorsList = Arrays.asList(str);
            final List<String> colorsList_id = Arrays.asList(str_id);

            builder.setMultiChoiceItems(str, checkedColors, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    checkedColors[which] = isChecked;
                    String currentItem = colorsList.get(which);
                    Toast.makeText(getApplicationContext(), currentItem + " " + isChecked, Toast.LENGTH_SHORT).show();
                }
            });
            builder.setCancelable(false);
            builder.setTitle("Select Manufacturing Make");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    for (int i = 0; i < checkedColors.length; i++) {
                        boolean checked = checkedColors[i];
                        if (checked) {
                            String values = make.getText() + colorsList.get(i) + ",";
                            values = values.replace("Select Manufacturing Make", "");
                            make.setText(values);

                            make_id.add(colorsList_id.get(i));
                            idss = make_id.toString();
                            idss = idss.replace("[", "");
                            idss = idss.replace("]", "");
                            idss = idss.replace(" ", "");
                            Log.e("city_id", idss);

                        }
                    }
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (contact_list.get(14) == null) {
                        make.setText("Select Make");
                    } else {
                        String make_select = contact_list.get(14).toString();
                        make_select = make_select.replace("[", "");
                        make_select = make_select.replace("]", "");
                        make_select = make_select.replace("\"", "");
                        make.setText(make_select);
                    }
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

        }
    }

    public void timeline_data() {

        timeline.setText("Select TimeLine");

        timeline_datas.add("Within 1 Month");
        timeline_datas.add("Within 2 Month");
        timeline_datas.add("Within 3 Month");


        ArrayList<String> timelineid = new ArrayList<>();
        timelineid.add("9");
        timelineid.add("10");
        timelineid.add("11");

        final AlertDialog.Builder builder = new AlertDialog.Builder(AddContactLeadsActivity.this);

        // Set the alert dialog title
        builder.setTitle("Select TimeLine");

        // Initializing an array of flowers
        final String[] strsss = new String[timeline_datas.size()];
        for (int f = 0; f < timeline_datas.size(); f++) {
            strsss[f] = timeline_datas.get(f);
        }

        final String[] strsss_ids = new String[timelineid.size()];
        for (int f = 0; f < timelineid.size(); f++) {
            strsss_ids[f] = timelineid.get(f);
        }

        final List<String> colorsList_id = Arrays.asList(strsss_ids);

        builder.setSingleChoiceItems(strsss, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectedItem = Arrays.asList(strsss).get(i);
                timeline_id.add(Arrays.asList(strsss_ids).get(i));
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                timeline.setText(selectedItem);

                id_timeline = timeline_id.toString();
                id_timeline = id_timeline.replace("[", "");
                id_timeline = id_timeline.replace("]", "");
                id_timeline = id_timeline.replace(" ", "");
                Log.e("city_id", id_timeline);

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private class Sub_model extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = ProgressDialog.show(AddContactLeadsActivity.this, "Loading...", "Please Wait ...", true);

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();
            String sub_make = Constant.FILTER_DETAIL_API
                    + "session_user_id=" + user.get("user_id")
                    + "&filtertype=Car%20Model"
                    + "&make_id=" + idss;
            String json = sh.makeServiceCall(sub_make, ServiceHandler.POST);
            Log.e("jsonjsonjsonjson", sub_make);
            if (json != null) {
                model_spinner_list = new ArrayList<>();
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    JSONArray model = jsonObj.getJSONArray("detail_filter");
                    for (int j = 0; j <= model.length(); j++) {
                        get_model_id = model.getJSONObject(j).getString("id");
                        get_model_name = model.getJSONObject(j).getString("name");
                        modelist = new HashMap<>();
                        modelist.put("model_id", get_model_id);
                        modelist.put("model_name", get_model_name);
                        model_spinner_list.add(modelist);
                        model_datas.add(get_model_name);
                        model_datas_id.add(get_model_id);
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

            model.setText("Select Model");
            AlertDialog.Builder builder = new AlertDialog.Builder(AddContactLeadsActivity.this);

            String[] str = new String[model_datas.size()];
            for (int f = 0; f < model_datas.size(); f++) {
                str[f] = model_datas.get(f);
            }

            String[] strs_ids = new String[model_datas_id.size()];
            for (int f = 0; f < model_datas_id.size(); f++) {
                strs_ids[f] = model_datas_id.get(f);
            }


            Log.e("str", str.toString());

            final boolean[] checkedColors = new boolean[model_datas.size()];
            for (int m = 0; m < model_datas.size(); m++) {
                checkedColors[m] = false;
            }

            final boolean[] checkedColors_ids = new boolean[model_datas_id.size()];
            for (int m = 0; m < model_datas_id.size(); m++) {
                checkedColors_ids[m] = false;
            }


            final List<String> colorsList = Arrays.asList(str);
            final List<String> colorsList_ids = Arrays.asList(strs_ids);

            builder.setMultiChoiceItems(str, checkedColors, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    checkedColors[which] = isChecked;
                    String currentItem = colorsList.get(which);
                    Toast.makeText(getApplicationContext(), currentItem + " " + isChecked, Toast.LENGTH_SHORT).show();
                }
            });
            builder.setCancelable(false);
            builder.setTitle("Select Model");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    for (int i = 0; i < checkedColors.length; i++) {
                        boolean checked = checkedColors[i];
                        if (checked) {
                            String values = model.getText() + colorsList.get(i) + ",";
                            values = values.replace("Select Model", "");
                            model.setText(values);

                            model_id.add(colorsList_ids.get(i));
                            id_model = model_id.toString();
                            id_model = id_model.replace("[", "");
                            id_model = id_model.replace("]", "");
                            id_model = id_model.replace(" ", "");
                            Log.e("city_id", id_model);

                        }
                    }
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (contact_list.get(15) == null) {
                        model.setText("Select Model");
                    } else {
                        String model_select = contact_list.get(15).toString();
                        model_select = model_select.replace("[", "");
                        model_select = model_select.replace("]", "");
                        model_select = model_select.replace("\"", "");
                        model.setText(model_select);
                    }
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();


        }
    }

    private class Budget_Datas extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = ProgressDialog.show(AddContactLeadsActivity.this, "Loading...", "Please Wait ...", true);

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();

            String budget_url = Constant.DASH_BOARD_SPINNER_API;

            String json = sh.makeServiceCall(budget_url, ServiceHandler.GET);

            if (json != null) {

                budget_spinner_list = new ArrayList<>();

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    JSONArray budget = jsonObj.getJSONArray("car_budget");

                    for (int j = 0; j <= budget.length(); j++) {
                        budget_id = budget.getJSONObject(j).getString("id");
                        int_bud_id = budget.getJSONObject(j).getString("id");
                        budget_name = budget.getJSONObject(j).getString("budget_varient_name");

                        budgetlist = new HashMap<>();

                        int addvalue = 3;
                        int sum = Integer.parseInt(int_bud_id) + addvalue;

                        budgetlist.put("ID", budget_id);
                        budgetlist.put("BUDGET_ID", String.valueOf(sum));
                        budgetlist.put("BUDGET", budget_name);

                        budget_spinner_list.add(budgetlist);

                        budget_datas.add(budget_name);
                        budget_datas_ids.add(String.valueOf(sum));

                        Log.e("budget_datas_ids", budget_datas_ids.toString());
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

            price.setText("Select Price");

            AlertDialog.Builder builder = new AlertDialog.Builder(AddContactLeadsActivity.this);
            String[] str = new String[budget_datas.size()];
            for (int f = 0; f < budget_datas.size(); f++) {
                str[f] = budget_datas.get(f);
            }

            String[] strs_idss = new String[budget_datas_ids.size()];
            for (int f = 0; f < budget_datas_ids.size(); f++) {
                strs_idss[f] = budget_datas_ids.get(f);
            }

            Log.e("str", str.toString());

            final boolean[] checkedColors = new boolean[budget_datas.size()];
            for (int m = 0; m < budget_datas.size(); m++) {
                checkedColors[m] = false;
            }

            final boolean[] checkedColorsids = new boolean[budget_datas_ids.size()];
            for (int m = 0; m < budget_datas_ids.size(); m++) {
                checkedColorsids[m] = false;
            }

            final List<String> colorsList = Arrays.asList(str);
            final List<String> colorsListID = Arrays.asList(strs_idss);

            builder.setMultiChoiceItems(str, checkedColors, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    checkedColors[which] = isChecked;
                    String currentItem = colorsList.get(which);
                    Toast.makeText(getApplicationContext(), currentItem + " " + isChecked, Toast.LENGTH_SHORT).show();
                }
            });

            builder.setCancelable(false);
            builder.setTitle("Select Price");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    for (int i = 0; i < checkedColors.length; i++) {
                        boolean checked = checkedColors[i];
                        if (checked) {
                            String values = price.getText() + colorsList.get(i) + ",";
                            values = values.replace("Select Price", "");
                            price.setText(values);

                            price_id.add(colorsListID.get(i));
                            id_price = price_id.toString();
                            id_price = id_price.replace("[", "");
                            id_price = id_price.replace("]", "");
                            id_price = id_price.replace(" ", "");
                            Log.e("city_id", id_price);

                        }
                    }
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (contact_list.get(18) == null) {
                        price.setText("Select Price Range");
                    } else {
                        String price_select = contact_list.get(18).toString();
                        price_select = price_select.replace("[", "");
                        price_select = price_select.replace("]", "");
                        price_select = price_select.replace("\"", "");
                        price.setText(price_select);
                    }
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }

    private class save_lead_details extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = ProgressDialog.show(AddContactLeadsActivity.this, "Loading...", "Please Wait ...", true);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();

        /*    region_name = region.getText().toString();
            make_name = make.getText().toString();
            model_name = model.getText().toString();
            price_name = price.getText().toString();
            time_name = timeline.getText().toString();

            try {
                region_name = URLEncoder.encode(region_name, "UTF-8");
                make_name = URLEncoder.encode(make_name, "UTF-8");
                model_name = URLEncoder.encode(model_name, "UTF-8");
                price_name = URLEncoder.encode(price_name, "UTF-8");
                time_name = URLEncoder.encode(time_name, "UTF-8");
            } catch (Exception e) {

            }*/

            /*String city_ids = null;

            if (contact_list.get(20) == null) {
                city_ids = id_city;
            } else {
                city_ids = regi_id;
            }

            String make_value_ids = null;

            if (contact_list.get(21) == null) {
                make_value_ids = idss;
            } else {
                make_value_ids = make_select_id;
            }

            String model_ids = null;

            if (contact_list.get(22) == null) {
                model_ids = id_model;
            } else {
                model_ids = model_select_id;
            }

            String price_ids = null;

            if (contact_list.get(23) == null) {
                price_ids = id_price;
            } else {
                price_ids = price_select_id;
            }
            String time_ids = null;

            if (contact_list.get(24) == null) {
                time_ids = id_timeline;
            } else {
                time_ids = time_line_id;
            }*/

            String newqueriesurl = Constant.LEADS_SAVE +
                    "session_user_id=" + user.get("user_id") +
                    "&contact_id=" + Contactid +
                    "&buycity=" + id_city +
                    "&buymake=" + idss +
                    "&buymodel=" + id_model +
                    "&pricefliter=" + id_price +
                    "&timeline=" + id_timeline;

            Log.e("Queriesurl", newqueriesurl);

            String json = sh.makeServiceCall(newqueriesurl, ServiceHandler.POST);
            Log.e("Queriesurl", json);

            if (json != null) {
                result_hashmap = new ArrayList<>();
                resultmap = new HashMap<String, String>();
                try {
                    JSONObject obj = new JSONObject(json);

                    for (int i = 0; i <= obj.length(); i++) {

                        String result = obj.getString("Result");
                        String message = obj.getString("message");

                        resultmap.put("REsult", result);
                        resultmap.put("Message", message);
                        result_hashmap.add(resultmap);

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

            barProgressDialog.dismiss();

            if (resultmap.get("REsult").equals("1")) {
                Log.e("Sdadas", "dasdas");
                AddContactLeadsActivity.this.finish();
            } else {
                Log.e("Sdadas", "11111");
            }
        }
    }

    private class document_lead_details extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = ProgressDialog.show(AddContactLeadsActivity.this, "Loading...", "Please Wait ...", true);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();

        /*    region_name = region.getText().toString();
            make_name = make.getText().toString();
            model_name = model.getText().toString();
            price_name = price.getText().toString();
            time_name = timeline.getText().toString();

            try {
                region_name = URLEncoder.encode(region_name, "UTF-8");
                make_name = URLEncoder.encode(make_name, "UTF-8");
                model_name = URLEncoder.encode(model_name, "UTF-8");
                price_name = URLEncoder.encode(price_name, "UTF-8");
                time_name = URLEncoder.encode(time_name, "UTF-8");
            } catch (Exception e) {

            }*/

            /*String city_ids = null;

            if (contact_list.get(20) == null) {
                city_ids = id_city;
            } else {
                city_ids = regi_id;
            }

            String make_value_ids = null;

            if (contact_list.get(21) == null) {
                make_value_ids = idss;
            } else {
                make_value_ids = make_select_id;
            }

            String model_ids = null;

            if (contact_list.get(22) == null) {
                model_ids = id_model;
            } else {
                model_ids = model_select_id;
            }

            String price_ids = null;

            if (contact_list.get(23) == null) {
                price_ids = id_price;
            } else {
                price_ids = price_select_id;
            }
            String time_ids = null;

            if (contact_list.get(24) == null) {
                time_ids = id_timeline;
            } else {
                time_ids = time_line_id;
            }*/

            String newqueriesurl = Constant.LEADS_SAVE +
                    "session_user_id=" + user.get("user_id") +
                    "&contact_id=" + Contactid +
                    "&buycity=" + id_city +
                    "&buymake=" + idss +
                    "&buymodel=" + id_model +
                    "&pricefliter=" + id_price +
                    "&timeline=" + id_timeline;

            Log.e("Queriesurl", newqueriesurl);

            String json = sh.makeServiceCall(newqueriesurl, ServiceHandler.POST);
            Log.e("Queriesurl", json);

            if (json != null) {
                result_hashmap = new ArrayList<>();
                resultmap = new HashMap<String, String>();
                try {
                    JSONObject obj = new JSONObject(json);

                    for (int i = 0; i <= obj.length(); i++) {

                        String result = obj.getString("Result");
                        String message = obj.getString("message");

                        resultmap.put("REsult", result);
                        resultmap.put("Message", message);
                        result_hashmap.add(resultmap);

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

            barProgressDialog.dismiss();

            if (resultmap.get("REsult").equals("1")) {
                Log.e("Sdadas", "dasdas");
                AddContactLeadsActivity.this.finish();

                Intent intet = new Intent(AddContactLeadsActivity.this, Contact_Document.class);
                intet.putExtra("Contactid", Contactid);
                startActivity(intet);

            } else {
                Log.e("Sdadas", "11111");
            }
        }
    }
}

