package com.falconnect.dealermanagementsystem.Adapter;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.falconnect.dealermanagementsystem.AlertActivity;
import com.falconnect.dealermanagementsystem.Constant;
import com.falconnect.dealermanagementsystem.Model.AlertPageModel;
import com.falconnect.dealermanagementsystem.Model.SingleProductModel;
import com.falconnect.dealermanagementsystem.R;
import com.falconnect.dealermanagementsystem.ServiceHandler;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AlertPageListAdapter extends ArrayAdapter<AlertPageModel> {

    public ArrayList<HashMap<String, String>> reload_arraylist;
    List<AlertPageModel> loanModels;
    ViewHolder holder;
    ArrayList<String> arrayList_alert;
    ArrayList<String> arrayList_sms;
    ArrayList<String> arrayList_email;

    String storyId_alert, storyId_email, storyId_sms;
    String pos_alert;
    List<SingleProductModel> products;
    HashMap<String, String> user;
    SessionManager sessionManager = new SessionManager(getContext());
    HashMap<String, String> editmap;
    String result, message;
    String user_id;
    String alert_status;
    int position_value;
    private Context context;


    public AlertPageListAdapter(Context context, List<AlertPageModel> loanModels, ArrayList<String> alert, ArrayList<String> sms, ArrayList<String> email) {
        super(context, R.layout.alert_singlelist, loanModels);
        this.context = context;
        this.loanModels = loanModels;
        this.arrayList_alert = alert;
        this.arrayList_email = email;
        this.arrayList_sms = sms;
    }

    @Override
    public int getCount() {
        return loanModels.size();
    }

    @Override
    public AlertPageModel getItem(int position) {
        return loanModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.alert_singlelist, null);

            holder = new ViewHolder();

            holder.type = (TextView) convertView.findViewById(R.id.type);
            holder.dealername = (TextView) convertView.findViewById(R.id.name);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.product = (TextView) convertView.findViewById(R.id.product);
            holder.city = (TextView) convertView.findViewById(R.id.city);

            holder.alertId=(TextView) convertView.findViewById(R.id.alert_id);

            holder.email_status = (CheckBox) convertView.findViewById(R.id.email);
            holder.sms_status = (CheckBox) convertView.findViewById(R.id.sms);
            holder.alert_status = (CheckBox) convertView.findViewById(R.id.alertcheck);
            holder.delete = (TextView) convertView.findViewById(R.id.delete);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        final AlertPageModel loanModel = getItem(position);

        holder.type.setText(loanModel.getType());
        holder.dealername.setText(loanModel.getDealername());
        holder.date.setText(loanModel.getDate());
        holder.product.setText(loanModel.getProduct());
        holder.city.setText(loanModel.getCity());
        holder.alertId.setText(loanModel.getAlertid());

        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/sanz.ttf");
        holder.email_status.setTypeface(font);
        holder.sms_status.setTypeface(font);
        holder.alert_status.setTypeface(font);

        if (arrayList_alert.get(position).equals("1")) {
            holder.alert_status.setChecked(true);
        } else {
            holder.alert_status.setChecked(false);
        }
        if (arrayList_email.get(position).equals("1")) {
            holder.email_status.setChecked(true);
        } else {
            holder.email_status.setChecked(false);
        }
        if (arrayList_sms.get(position).equals("1")) {
            holder.sms_status.setChecked(true);
        } else {
            holder.sms_status.setChecked(false);
        }

        holder.alert_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storyId_alert = arrayList_alert.get(position);

                alert_status = "0";

                pos_alert = loanModels.get(position).getAlertid();

                String name = loanModels.get(position).getDealername();

                Log.e("car_name", name);
                if (storyId_alert.equals("0")) {
                    arrayList_alert.set(position, "1");
                } else {
                    arrayList_alert.set(position, "0");
                }
                Log.e("alert", arrayList_alert.get(position));

                new AlertButton().execute();

            }
        });

        holder.email_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storyId_email = arrayList_email.get(position);

                pos_alert = loanModels.get(position).getAlertid();

                alert_status = "1";

                String name = loanModels.get(position).getDealername();

                Log.e("car_name", name);
                if (storyId_email.equals("0")) {
                    arrayList_email.set(position, "1");
                } else {
                    arrayList_email.set(position, "0");
                }
                Log.e("alert", arrayList_alert.get(position));

                new AlertButton().execute();

            }
        });

        holder.sms_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storyId_sms = arrayList_sms.get(position);

                pos_alert = loanModels.get(position).getAlertid();

                alert_status = "2";

                String name = loanModels.get(position).getDealername();

                Log.e("car_name", name);
                if (storyId_sms.equals("0")) {
                    arrayList_sms.set(position, "1");
                } else {
                    arrayList_sms.set(position, "0");
                }
                Log.e("alert", arrayList_alert.get(position));

                new AlertButton().execute();
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertbox = new AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogNewStyle)
                        .setMessage("Do you want to delete the Alert?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                notifyDataSetChanged();
                                position_value = position;
                                pos_alert = loanModels.get(position).getAlertid();

                                new LikeButton().execute();
                            }
                        })
                        .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        })
                        .show();


            }
        });


        return convertView;
    }

    class LikeButton extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            ServiceHandler sh = new ServiceHandler();
            user = sessionManager.getUserDetails();
            user_id = user.get("user_id");
            String fav_url = Constant.ALERT_DELETE
                    + "session_user_id=" + user_id
                    + "&alertid=" + pos_alert;

            Log.e("stringurl",fav_url);
            String json = sh.makeServiceCall(fav_url, ServiceHandler.POST);

            if (json != null) {

                reload_arraylist = new ArrayList<>();
                editmap = new HashMap<String, String>();

                try {
                    JSONObject obj = new JSONObject(json);

                    for (int i = 0; i <= obj.length(); i++) {

                        result = obj.getString("Result");
                        message = obj.getString("message");
                        editmap.put("REsult", result);
                        editmap.put("Message", message);
                        reload_arraylist.add(editmap);

                    }
                } catch (final JSONException e) {
                    // Toast.makeText(getContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                // Toast.makeText(getContext(), "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            ((AlertActivity) context).refresh();
            // Toast.makeText(getContext(), editmap.get("Message"), Toast.LENGTH_SHORT).show();
        }
    }


    class AlertButton extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            ServiceHandler sh = new ServiceHandler();
            user = sessionManager.getUserDetails();
            user_id = user.get("user_id");

            String fav_url = Constant.ALERT_REVOKE
                    + "session_user_id=" + user_id
                    + "&alertid=" + pos_alert
                    + "&alertstatus=" + alert_status;

            Log.e("fav_url", fav_url);

            String json = sh.makeServiceCall(fav_url, ServiceHandler.POST);

            if (json != null) {

                reload_arraylist = new ArrayList<>();
                editmap = new HashMap<String, String>();

                try {
                    JSONObject obj = new JSONObject(json);

                    for (int i = 0; i <= obj.length(); i++) {

                        result = obj.getString("Result");
                        message = obj.getString("message");
                        editmap.put("Result", result);
                        editmap.put("Message", message);
                        reload_arraylist.add(editmap);

                    }
                } catch (final JSONException e) {
                    //Toast.makeText(getContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                // Toast.makeText(getContext(), "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            notifyDataSetChanged();

            // Toast.makeText(getContext(), editmap.get("Message"), Toast.LENGTH_SHORT).show();
        }
    }


    private class ViewHolder {
        TextView city, date, product, type, dealername, delete,alertId;

        CheckBox email_status, sms_status, alert_status;
    }

}