package com.falconnect.currencyconverter;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.falconnect.currencyconverter.JsonServiceHandler.ServiceHandler;
import com.falconnect.currencyconverter.Session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    EditText amount_textbox;
    Spinner from_spinner, to_spinner;
    Button convert_button;
    TextView converted_amount;

    TextView euro_balance, usd_balance, jpy_balance;

    ArrayList<String> currency_values = new ArrayList<>();
    String get_fromString, get_toString, get_amount;

    ProgressDialog barProgressDialog;

    public static ArrayAdapter<String> spinnercontactArrayAdapter;

    //API URL
    String URL = "http://api.evp.lt/currency/commercial/exchange/";
    String get_amounts, currency;

    //Session
    SessionManager sessionManager;
    HashMap<String, String> user;

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initlize();
        converted_amount.setVisibility(View.GONE);
        user = sessionManager.getAmountDetails();

        if (user.get("euro") == null || user.get("usd") == null || user.get("jpy") == null) {
            sessionManager.createAmountDetails("1000.00", "0.00", "0.00");
            euro_balance.setText(user.get("euro"));
            usd_balance.setText(user.get("usd"));
            jpy_balance.setText(user.get("jpy"));
        } else {
            sessionManager.createAmountDetails(user.get("euro"), user.get("usd"), user.get("jpy"));
            euro_balance.setText(user.get("euro"));
            usd_balance.setText(user.get("usd"));
            jpy_balance.setText(user.get("jpy"));
        }


        //List Values for Spinner
        currency_values.add("Select Your Currency");
        currency_values.add("EUR");
        currency_values.add("USD");
        currency_values.add("JPY");

        spinnercontactArrayAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.spinner_single_item, currency_values) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                //zeroth postion values text color
                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                }
                //remaining position values
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        //Dropdown Resource and layouts
        spinnercontactArrayAdapter.setDropDownViewResource(R.layout.spinner_single_item);
        from_spinner.setAdapter(spinnercontactArrayAdapter);

        //From spinner Selection from spinner
        from_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText_from = (String) parent.getItemAtPosition(position);
                if (position > 0) {
                    get_fromString = selectedItemText_from;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //To spinner Selection from spinner
        to_spinner.setAdapter(spinnercontactArrayAdapter);
        to_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText_to = (String) parent.getItemAtPosition(position);
                if (position > 0) {
                    get_toString = selectedItemText_to;
                    //new euro_rate_api().execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //Button On Click API CAll
        convert_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_amount = amount_textbox.getText().toString().trim();
                //Select Spinner datas if null
                if (get_toString == null || get_toString == null) {
                    final AlertDialog alertbox = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Error")
                            .setMessage("Please Select the Currency")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            })
                            .show();
                }
                //Textbox empty value
                else if (get_amount.equals("")) {
                    final AlertDialog alertbox = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Error")
                            .setMessage("Please Enter the Amount")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            })
                            .show();
                }
                //API CALL
                else {
                    i = i + 1;
                    if (i < 6) {
                        new call_api_function().execute();
                    }else{
                        final AlertDialog alertboxs = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Commission Fee 0.7%")
                                .setMessage("More than 5 Transaction. if you want to transact amount again?")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        new call_api_function().execute();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .show();
                    }

                }
            }
        });

    }

    public void initlize() {
        amount_textbox = (EditText) findViewById(R.id.amount_textbox);
        from_spinner = (Spinner) findViewById(R.id.from_spinner);
        to_spinner = (Spinner) findViewById(R.id.to_spinner);

        converted_amount = (TextView) findViewById(R.id.converted_amount);
        euro_balance = (TextView) findViewById(R.id.euro_balance);
        usd_balance = (TextView) findViewById(R.id.usd_balance);
        jpy_balance = (TextView) findViewById(R.id.jpy_balance);

        convert_button = (Button) findViewById(R.id.convert_button);

        sessionManager = new SessionManager(MainActivity.this);


    }

    public class call_api_function extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Progress Bar
            barProgressDialog = ProgressDialog.show(MainActivity.this, "Loading...", "Please Wait...", true);
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();

            String new_URL = URL + get_amount + "-" + get_fromString + "/" + get_toString + "/latest";

            Log.e("url", new_URL);

            String json = sh.makeServiceCall(new_URL, ServiceHandler.GET);
            if (json != null) {

                JSONObject obj = null;
                try {
                    obj = new JSONObject(json);
                    for (int k = 0; k <= obj.length(); k++) {
                        get_amounts = obj.getString("amount");
                        currency = obj.getString("currency");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            //progressbar dismiss
            barProgressDialog.dismiss();

            //TextView Visibility
            converted_amount.setVisibility(View.VISIBLE);

            //check currency for set text
            if (currency.equals("EUR")) {
                converted_amount.setText("€" + " " + get_amounts);
            } else if (currency.equals("USD")) {
                converted_amount.setText("$" + " " + get_amounts);
            } else if (currency.equals("JPY")) {
                converted_amount.setText("¥" + " " + get_amounts);
            }

            if (i < 6) {
                // From - EUR && To - USD
                if (get_fromString.equals("EUR") && get_toString.equals("USD")) {
                    user = sessionManager.getAmountDetails();
                    //EURO Value 0
                    if (user.get("euro").equals("0.0")) {
                        final AlertDialog alertbox = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Error")
                                .setMessage("Value is 0")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {

                                    }
                                })
                                .show();
                    }
                    // Euro value greater than textbox value
                    else if (Double.valueOf(user.get("euro")) < Double.valueOf(get_amount)) {
                        final AlertDialog alertbox = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Error")
                                .setMessage("Entered values is greater than EURO Value")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {

                                    }
                                })
                                .show();
                    }
                    // calculation
                    else {
                        user = sessionManager.getAmountDetails();

                        double euro_bal = Double.valueOf(user.get("euro").toString()) - Integer.parseInt(get_amount);
                        double usd_bal = Double.valueOf(user.get("usd").toString()) + Double.valueOf(get_amounts);

                        sessionManager.createAmountDetails(String.valueOf(euro_bal), String.valueOf(usd_bal), user.get("jpy"));
                    }
                }
                // From - EUR && To - JPY
                else if (get_fromString.equals("EUR") && get_toString.equals("JPY")) {
                    //EURO Value 0
                    if (user.get("euro").equals("0.0")) {
                        final AlertDialog alertbox = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Error")
                                .setMessage("Value is 0")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {

                                    }
                                })
                                .show();
                    }
                    //Greater value
                    else if (Double.valueOf(user.get("euro")) < Double.valueOf(get_amount)) {
                        final AlertDialog alertbox = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Error")
                                .setMessage("Entered values is greater than EURO Value")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {

                                    }
                                })
                                .show();
                    }
                    // calculation
                    else {
                        user = sessionManager.getAmountDetails();

                        double euro_bal = Double.valueOf(user.get("euro").toString()) - Integer.parseInt(get_amount);
                        double jpy_bal = Double.valueOf(user.get("jpy").toString()) + Double.valueOf(get_amounts);

                        sessionManager.createAmountDetails(String.valueOf(euro_bal), user.get("usd"), String.valueOf(jpy_bal));
                    }
                } else if (get_fromString.equals("USD") && get_toString.equals("EUR")) {
                    //EURO Value 0
                    if (user.get("usd").equals("0.0")) {
                        final AlertDialog alertbox = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Error")
                                .setMessage("Value is 0")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {

                                    }
                                })
                                .show();
                    }
                    //Greater value
                    else if (Double.valueOf(user.get("usd")) < Double.valueOf(get_amount)) {
                        final AlertDialog alertbox = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Error")
                                .setMessage("Entered values is greater than USD Value")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {

                                    }
                                })
                                .show();
                    } else {
                        user = sessionManager.getAmountDetails();

                        double usd_bal = Double.valueOf(user.get("usd").toString()) - Integer.parseInt(get_amount);
                        double add_euro_Val = Double.valueOf(user.get("euro").toString()) + Double.valueOf(get_amounts);

                        sessionManager.createAmountDetails(String.valueOf(add_euro_Val), String.valueOf(usd_bal), user.get("jpy").toString());
                    }
                } else if (get_fromString.equals("USD") && get_toString.equals("JPY")) {
                    //EURO Value 0
                    if (user.get("usd").equals("0.0")) {
                        final AlertDialog alertbox = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Error")
                                .setMessage("Value is 0")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {

                                    }
                                })
                                .show();
                    }
                    //Greater value
                    else if (Double.valueOf(user.get("usd")) < Double.valueOf(get_amount)) {
                        final AlertDialog alertbox = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Error")
                                .setMessage("Entered values is greater than USD Value")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {

                                    }
                                })
                                .show();
                    } else {
                        user = sessionManager.getAmountDetails();

                        double usd_bal = Double.valueOf(user.get("usd").toString()) - Integer.parseInt(get_amount);
                        double add_euro_Val = Double.valueOf(user.get("jpy").toString()) + Double.valueOf(get_amounts);

                        sessionManager.createAmountDetails(user.get("euro").toString(), String.valueOf(usd_bal), String.valueOf(add_euro_Val));
                    }
                } else if (get_fromString.equals("JPY") && get_toString.equals("EUR")) {
                    //EURO Value 0
                    if (user.get("jpy").equals("0.0")) {
                        final AlertDialog alertbox = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Error")
                                .setMessage("Value is 0")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {

                                    }
                                })
                                .show();
                    }
                    //Greater value
                    else if (Double.valueOf(user.get("jpy")) < Double.valueOf(get_amount)) {
                        final AlertDialog alertbox = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Error")
                                .setMessage("Entered values is greater than JPY Value")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {

                                    }
                                })
                                .show();
                    } else {
                        user = sessionManager.getAmountDetails();

                        double usd_bal = Double.valueOf(user.get("jpy").toString()) - Integer.parseInt(get_amount);
                        double add_euro_Val = Double.valueOf(user.get("euro").toString()) + Double.valueOf(get_amounts);

                        sessionManager.createAmountDetails(String.valueOf(add_euro_Val), user.get("usd").toString(), String.valueOf(usd_bal));

                    }
                } else if (get_fromString.equals("JPY") && get_toString.equals("USD")) {
                    //EURO Value 0
                    if (user.get("jpy").equals("0.0")) {
                        final AlertDialog alertbox = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Error")
                                .setMessage("Value is 0")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {

                                    }
                                })
                                .show();
                    }
                    //Greater value
                    else if (Double.valueOf(user.get("jpy")) < Double.valueOf(get_amount)) {
                        final AlertDialog alertbox = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Error")
                                .setMessage("Entered values is greater than JPY Value")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {

                                    }
                                })
                                .show();
                    } else {
                        user = sessionManager.getAmountDetails();

                        double usd_bal = Double.valueOf(user.get("jpy").toString()) - Integer.parseInt(get_amount);
                        double add_euro_Val = Double.valueOf(user.get("usd").toString()) + Double.valueOf(get_amounts);

                        sessionManager.createAmountDetails(user.get("euro").toString(), String.valueOf(add_euro_Val), String.valueOf(usd_bal));
                    }
                }

                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            } else {
                if (get_fromString.equals("EUR") && get_toString.equals("USD")) {
                    //EURO Value 0
                    if (user.get("euro").equals("0.0")) {
                        final AlertDialog alertbox = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Error")
                                .setMessage("Value is 0")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {

                                    }
                                })
                                .show();
                    }
                    //Greater value
                    else if (Double.valueOf(user.get("euro")) < Double.valueOf(get_amount)) {
                        final AlertDialog alertbox = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Error")
                                .setMessage("Entered values is greater than EURO Value")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {

                                    }
                                })
                                .show();
                    } else {
                        user = sessionManager.getAmountDetails();

                        //percentage
                        double percentage = Double.valueOf(user.get("euro")) * 0.70;
                        double final_percentage = percentage / 100;

                        double euro_bal = Double.valueOf(user.get("euro").toString()) - Integer.parseInt(get_amount) - final_percentage;
                        double usd_bal = Double.valueOf(user.get("usd").toString()) + Double.valueOf(get_amounts);

                        sessionManager.createAmountDetails(String.valueOf(euro_bal), String.valueOf(usd_bal), user.get("jpy"));
                    }

                } else if (get_fromString.equals("EUR") && get_toString.equals("JPY")) {
                    //EURO Value 0
                    if (user.get("euro").equals("0.0")) {
                        final AlertDialog alertbox = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Error")
                                .setMessage("Value is 0")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {

                                    }
                                })
                                .show();
                    }
                    //Greater value
                    else if (Double.valueOf(user.get("euro")) < Double.valueOf(get_amount)) {
                        final AlertDialog alertbox = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Error")
                                .setMessage("Entered values is greater than EURO Value")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {

                                    }
                                })
                                .show();
                    } else {
                        user = sessionManager.getAmountDetails();
                        //percentage
                        double percentage = Double.valueOf(user.get("euro")) * 0.70;
                        double final_percentage = percentage / 100;

                        double euro_bal = Double.valueOf(user.get("euro").toString()) - Integer.parseInt(get_amount) - final_percentage;
                        double jpy_bal = Double.valueOf(user.get("jpy").toString()) + Double.valueOf(get_amounts);

                        sessionManager.createAmountDetails(String.valueOf(euro_bal), user.get("usd"), String.valueOf(jpy_bal));
                    }
                } else if (get_fromString.equals("USD") && get_toString.equals("EUR")) {
                    //USD Value 0
                    if (user.get("usd").equals("0.0")) {
                        final AlertDialog alertbox = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Error")
                                .setMessage("Value is 0")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {

                                    }
                                })
                                .show();
                    }
                    //Greater value
                    else if (Double.valueOf(user.get("usd")) < Double.valueOf(get_amount)) {
                        final AlertDialog alertbox = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Error")
                                .setMessage("Entered values is greater than USD Value")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {

                                    }
                                })
                                .show();
                    } else {
                        user = sessionManager.getAmountDetails();
                        //percentage
                        double percentage = Double.valueOf(user.get("usd")) * 0.70;
                        double final_percentage = percentage / 100;

                        double usd_bal = Double.valueOf(user.get("usd").toString()) - Integer.parseInt(get_amount) - final_percentage;
                        double add_euro_Val = Double.valueOf(user.get("euro").toString()) + Double.valueOf(get_amounts);

                        sessionManager.createAmountDetails(String.valueOf(add_euro_Val), String.valueOf(usd_bal), user.get("jpy").toString());
                    }
                } else if (get_fromString.equals("USD") && get_toString.equals("JPY")) {
                    //USD Value 0
                    if (user.get("usd").equals("0.0")) {
                        final AlertDialog alertbox = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Error")
                                .setMessage("Value is 0")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {

                                    }
                                })
                                .show();
                    }
                    //Greater value
                    else if (Double.valueOf(user.get("usd")) < Double.valueOf(get_amount)) {
                        final AlertDialog alertbox = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Error")
                                .setMessage("Entered values is greater than USD Value")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {

                                    }
                                })
                                .show();

                    } else {
                        user = sessionManager.getAmountDetails();
                        //percentage
                        double percentage = Double.valueOf(user.get("usd")) * 0.70;
                        double final_percentage = percentage / 100;

                        double usd_bal = Double.valueOf(user.get("usd").toString()) - Integer.parseInt(get_amount) - final_percentage;
                        double add_euro_Val = Double.valueOf(user.get("jpy").toString()) + Double.valueOf(get_amounts);

                        sessionManager.createAmountDetails(user.get("euro").toString(), String.valueOf(usd_bal), String.valueOf(add_euro_Val));
                    }
                } else if (get_fromString.equals("JPY") && get_toString.equals("EUR")) {
                    //JPY Value 0
                    if (user.get("jpy").equals("0.0")) {
                        final AlertDialog alertbox = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Error")
                                .setMessage("Value is 0")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {

                                    }
                                })
                                .show();
                    }
                    //Greater value
                    else if (Double.valueOf(user.get("jpy")) < Double.valueOf(get_amount)) {
                        final AlertDialog alertbox = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Error")
                                .setMessage("Entered values is greater than JPY Value")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {

                                    }
                                })
                                .show();

                    } else {
                        user = sessionManager.getAmountDetails();
                        //percentage
                        double percentage = Double.valueOf(user.get("jpy")) * 0.70;
                        double final_percentage = percentage / 100;

                        double usd_bal = Double.valueOf(user.get("jpy").toString()) - Integer.parseInt(get_amount) - final_percentage;
                        double add_euro_Val = Double.valueOf(user.get("euro").toString()) + Double.valueOf(get_amounts);

                        sessionManager.createAmountDetails(String.valueOf(add_euro_Val), user.get("usd").toString(), String.valueOf(usd_bal));
                    }
                } else if (get_fromString.equals("JPY") && get_toString.equals("USD")) {
                    //JPY Value 0
                    if (user.get("jpy").equals("0.0")) {
                        final AlertDialog alertbox = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Error")
                                .setMessage("Value is 0")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {

                                    }
                                })
                                .show();
                    }
                    //Greater value
                    else if (Double.valueOf(user.get("jpy")) < Double.valueOf(get_amount)) {
                        final AlertDialog alertbox = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Error")
                                .setMessage("Entered values is greater than JPY Value")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {

                                    }
                                })
                                .show();

                    } else {
                        user = sessionManager.getAmountDetails();
                        //percentage
                        double percentage = Double.valueOf(user.get("jpy")) * 0.70;
                        double final_percentage = percentage / 100;

                        double usd_bal = Double.valueOf(user.get("jpy").toString()) - Integer.parseInt(get_amount) - final_percentage;
                        double add_euro_Val = Double.valueOf(user.get("usd").toString()) + Double.valueOf(get_amounts);

                        sessionManager.createAmountDetails(user.get("euro").toString(), String.valueOf(add_euro_Val), String.valueOf(usd_bal));
                    }
                }

                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            }

            user = sessionManager.getAmountDetails();

            euro_balance.setText(user.get("euro"));
            usd_balance.setText(user.get("usd"));
            jpy_balance.setText(user.get("jpy"));

        }
    }
}
