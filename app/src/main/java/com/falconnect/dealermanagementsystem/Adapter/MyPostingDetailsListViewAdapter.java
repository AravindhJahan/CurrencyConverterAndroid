package com.falconnect.dealermanagementsystem.Adapter;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.falconnect.dealermanagementsystem.Constant;
import com.falconnect.dealermanagementsystem.Model.MyPostingDetailsListViewModel;
import com.falconnect.dealermanagementsystem.MyPostingDetailsActivity;
import com.falconnect.dealermanagementsystem.R;
import com.falconnect.dealermanagementsystem.ServiceHandler;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyPostingDetailsListViewAdapter extends ArrayAdapter<MyPostingDetailsListViewModel> {

    public ArrayList<HashMap<String, String>> deletelist;
    public ArrayList<HashMap<String, String>> repostlist;
    List<MyPostingDetailsListViewModel> loanModels;
    ViewHolder holder;
    MyPostingDetailsListViewModel myPostingListModel;
    SessionManager sessionManager = new SessionManager(getContext());
    HashMap<String, String> user = new HashMap<>();
    HashMap<String, String> deletemap;
    HashMap<String, String> repostmap;
    String result, message;
    ProgressDialog barProgressDialog;
    private Context context;

    public MyPostingDetailsListViewAdapter(Context context, List<MyPostingDetailsListViewModel> loanModels) {
        super(context, R.layout.myposting_details_list_single_item, loanModels);
        this.context = context;
        this.loanModels = loanModels;
    }

    @Override
    public int getCount() {
        return loanModels.size();
    }

    @Override
    public MyPostingDetailsListViewModel getItem(int position) {
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
            convertView = inflater.inflate(R.layout.myposting_details_list_single_item, null);

            holder = new ViewHolder();

            holder.car_name = (TextView) convertView.findViewById(R.id.car_name);
            holder.myposting_details_car_token = (TextView) convertView.findViewById(R.id.myposting_details_car_token);
            holder.myposting_details_car_rate = (TextView) convertView.findViewById(R.id.myposting_details_car_rate);
            holder.myposting_details_car_details = (TextView) convertView.findViewById(R.id.myposting_details_car_details);
            holder.myposting_details_car_status = (TextView) convertView.findViewById(R.id.myposting_details_car_status);
            holder.myposting_details_car_post_date = (TextView) convertView.findViewById(R.id.myposting_details_car_post_date);

            holder.imageurl = (ImageView) convertView.findViewById(R.id.car_image);
            holder.imagesite = (ImageView) convertView.findViewById(R.id.my_posting_site_image);

            holder.delete = (Button) convertView.findViewById(R.id.my_psting_delete_btn);
            holder.repost = (Button) convertView.findViewById(R.id.my_psting_repost_btn);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        myPostingListModel = getItem(position);

        Glide.with(context)
                .load(myPostingListModel.getImageurl())
                .placeholder(R.drawable.carimageplaceholder)
                .centerCrop()
                .into(holder.imageurl);

        holder.car_name.setText(myPostingListModel.getModel());
        holder.myposting_details_car_token.setText(myPostingListModel.getDuplicate_id()
                + " " + "-" + " " + myPostingListModel.getPlan());

        holder.myposting_details_car_rate.setText("Rs." + myPostingListModel.getPrice());

        holder.myposting_details_car_details.setText(myPostingListModel.getKms() + " Km" + " | " + " " + myPostingListModel.getFuel_type() + " | " +
                myPostingListModel.getYear() + " | " + myPostingListModel.getOwner() + " Owner");

        if (myPostingListModel.getListing_status().equals("0")) {
            holder.myposting_details_car_status.setText("InActive");
            holder.delete.setVisibility(View.GONE);
            holder.repost.setVisibility(View.VISIBLE);
        } else {
            holder.delete.setVisibility(View.VISIBLE);
            holder.repost.setVisibility(View.GONE);
            holder.myposting_details_car_status.setText("Active");
        }

        holder.myposting_details_car_post_date.setText(myPostingListModel.getMongopushdate());

        Glide.with(context)
                .load(myPostingListModel.getList_image())
                .placeholder(R.drawable.carimageplaceholder)
                .into(holder.imagesite);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new delete_posting().execute();
            }
        });

        holder.repost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new re_posting().execute();
            }
        });

        return convertView;
    }

    class delete_posting extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = ProgressDialog.show(getContext(), "Loading...", "Please Wait ...", true);

        }

        @Override
        protected String doInBackground(String... params) {

            ServiceHandler sh = new ServiceHandler();
            user = sessionManager.getUserDetails();
            String user_id = user.get("user_id");
            String fav_url = Constant.MY_POSTING_DETAIL_DELETE
                    + "session_user_id=" + user_id
                    + "&page_name=mypostdetailsdelete"
                    + "&listing_id=" + myPostingListModel.getListing_id();

            String json = sh.makeServiceCall(fav_url, ServiceHandler.POST);

            if (json != null) {

                deletelist = new ArrayList<>();
                deletemap = new HashMap<String, String>();

                try {
                    JSONObject obj = new JSONObject(json);

                    for (int i = 0; i <= obj.length(); i++) {

                        result = obj.getString("Result");
                        message = obj.getString("message");
                        deletemap.put("REsult", result);
                        deletemap.put("Message", message);
                        deletelist.add(deletemap);

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
            barProgressDialog.dismiss();
            if (deletemap.get("REsult").equals("2")) {
                ((MyPostingDetailsActivity) context).listview();
            } else {

            }
        }
    }

    class re_posting extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            ServiceHandler sh = new ServiceHandler();
            user = sessionManager.getUserDetails();
            String user_id = user.get("user_id");
            String fav_url = Constant.MY_POSTING_DETAIL_REPOST
                    + "session_user_id=" + user_id
                    + "&page_name=mypostdetailsdelete"
                    + "&listing_id=" + myPostingListModel.getListing_id();

            String json = sh.makeServiceCall(fav_url, ServiceHandler.POST);

            if (json != null) {

                repostlist = new ArrayList<>();
                repostmap = new HashMap<String, String>();

                try {
                    JSONObject obj = new JSONObject(json);

                    for (int i = 0; i <= obj.length(); i++) {

                        result = obj.getString("Result");
                        message = obj.getString("message");
                        repostmap.put("REsult", result);
                        repostmap.put("Message", message);
                        repostlist.add(repostmap);

                    }
                } catch (final JSONException e) {
                    //  Toast.makeText(getContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                // Toast.makeText(getContext(), "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            if (repostmap.get("REsult").equals("2")) {
                ((MyPostingDetailsActivity) context).listview();
            } else {
            }
        }
    }

    private class ViewHolder {

        TextView car_name, myposting_details_car_token, myposting_details_car_rate, myposting_details_car_details;
        TextView myposting_details_car_status, myposting_details_car_post_date;
        ImageView imageurl, imagesite;
        Button delete, repost;
    }


}