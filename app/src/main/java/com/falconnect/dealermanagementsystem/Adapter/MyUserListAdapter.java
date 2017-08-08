package com.falconnect.dealermanagementsystem.Adapter;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.falconnect.dealermanagementsystem.Constant;
import com.falconnect.dealermanagementsystem.Model.MyUserModel;
import com.falconnect.dealermanagementsystem.R;
import com.falconnect.dealermanagementsystem.ServiceHandler;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyUserListAdapter extends ArrayAdapter<MyUserModel> {

    List<MyUserModel> userModels;
    private Context context;
    ViewHolder holder;
    HashMap<String, String> user;
    String result, message;
    String user_id;
    int pos;
    public ArrayList<HashMap<String, String>> reload_arraylist;
    HashMap<String, String> editmap;

    ArrayList<String> status=new ArrayList<String>();

    ArrayList<String> statusId=new ArrayList<String>();


    String check = "0";

    String user_ids;


    SessionManager sessionManager = new SessionManager(getContext());

    public MyUserListAdapter(Context context, List<MyUserModel> userModels,ArrayList<String> status) {
        super(context, R.layout.myuser_single_item, userModels);
        this.context = context;
        this.userModels = userModels;
        this.status=status;

        for(int i =0; i<status.size();i++)
        {
            if (status.get(i).equals("Active"))
            {
                statusId.add("1");
            }
            else
            {
                statusId.add("0");
            }
        }

    }

    @Override
    public int getCount() {
        return userModels.size();
    }

    @Override
    public MyUserModel getItem(int position) {
        return userModels.get(position);
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
            convertView = inflater.inflate(R.layout.myuser_single_item, null);

            holder = new ViewHolder();

            holder.branch_id = (TextView) convertView.findViewById(R.id.user_branches);
            holder.user_email = (TextView) convertView.findViewById(R.id.user_id);
            holder.role = (TextView) convertView.findViewById(R.id.user_role);
            holder.toggleButton = (ToggleButton) convertView.findViewById(R.id.toggleButton1);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        final MyUserModel userModelssss = getItem(position);




            if (userModelssss.getBranch_id().equals("null")) {
            holder.branch_id.setText("All Branches");
        } else {
            holder.branch_id.setText("All Branches");
        }

        holder.user_email.setText(userModelssss.getUser_name());

        if (userModelssss.getUser_role().equals("0")) {
            holder.role.setText("Admin (Primary User)");
        } else {
            holder.role.setText(userModelssss.getRole_name());
        }

        Log.e("Status", userModelssss.getStatus());


        if (statusId.get(position).equals("1"))
        {
            holder.toggleButton.setChecked(true);

        } else {
            holder.toggleButton.setChecked(false);
        }

        holder.toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_ids = userModels.get(position).getUser_id();
                Log.e("TRUE", user_ids);

                new alert_status().execute();

                pos=position;

            }
        });


        return convertView;
    }

    class alert_status extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            ServiceHandler sh = new ServiceHandler();

            user = sessionManager.getUserDetails();

            user_id = user.get("user_id");
            String fav_url = Constant.USER_STATUS
                    + "session_user_id=" + user_id
                    + "&user_id=" + user_ids
                    + "&page_name=invokestatususer";
            String json = sh.makeServiceCall(fav_url, ServiceHandler.POST);

            Log.e("jsonjsonjsonjson", fav_url);

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

            if (result.equals("3")) {
                Log.e("Failure", message);

                if (statusId.get(pos).equals("1"))
                {
                    statusId.set(pos,"0");
                }
                else
                {
                    statusId.set(pos,"1");
                }

                notifyDataSetChanged();


            } else {
                AlertDialog alertbox = new AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogNewStyle)
                        .setMessage(message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                                if(holder.toggleButton.isChecked() == false){
                                    Log.e("truee", "True");
                                    holder.toggleButton.setChecked(true);
                                }
                            }
                        })
                        .show();

                notifyDataSetChanged();

            }

        }
    }

    private class ViewHolder {
        TextView branch_id, user_email, role;
        ToggleButton toggleButton;

    }

}
