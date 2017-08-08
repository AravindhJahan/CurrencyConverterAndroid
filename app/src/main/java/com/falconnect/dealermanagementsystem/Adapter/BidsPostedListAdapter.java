package com.falconnect.dealermanagementsystem.Adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.falconnect.dealermanagementsystem.FontAdapter.RoundImageTransform;
import com.falconnect.dealermanagementsystem.Model.BidsPostedListModel;
import com.falconnect.dealermanagementsystem.Model.QueryListModel;
import com.falconnect.dealermanagementsystem.R;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;

import java.util.HashMap;
import java.util.List;

public class BidsPostedListAdapter extends ArrayAdapter<BidsPostedListModel> {

    List<BidsPostedListModel> products;
    private Context context;

    ViewHolder holder;

    public BidsPostedListAdapter(Context context, List<BidsPostedListModel> products) {
        super(context, R.layout.bids_list_single_item, products);
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public BidsPostedListModel getItem(int position) {
        return products.get(position);
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
            convertView = inflater.inflate(R.layout.bids_list_single_item, null);

            holder = new ViewHolder();

            holder.car_image = (ImageView) convertView.findViewById(R.id.bids_car_image);
            holder.site_image = (ImageView) convertView.findViewById(R.id.bids_site_image);
            holder.car_name = (TextView) convertView.findViewById(R.id.bids_name);
            holder.car_rate = (TextView) convertView.findViewById(R.id.bids_posted_rate);
            holder.car_tim_left = (TextView) convertView.findViewById(R.id.bids_time_left);
            holder.car_posted_date = (TextView) convertView.findViewById(R.id.bids_aution);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final BidsPostedListModel bidsPostedListModel = getItem(position);


        Glide.with(context)
                .load(bidsPostedListModel.getCar_image())
                .placeholder(R.drawable.defaultcar)
                .into(holder.car_image);

        Glide.with(context)
                .load(bidsPostedListModel.getSite_image())
                .placeholder(R.drawable.chola)
                .into(holder.site_image);

        holder.car_name.setText(bidsPostedListModel.getCar_name());
        holder.car_rate.setText(bidsPostedListModel.getCar_rate());
        holder.car_tim_left.setText(bidsPostedListModel.getLeftdate_time());
        holder.car_posted_date.setText(bidsPostedListModel.getCar_posted_ago());


        return convertView;
    }


    private class ViewHolder {

        ImageView car_image, site_image;
        TextView car_name, car_rate, car_tim_left, car_posted_date;
    }

}