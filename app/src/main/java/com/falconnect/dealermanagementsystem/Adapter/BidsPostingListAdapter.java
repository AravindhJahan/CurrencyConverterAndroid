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
import com.falconnect.dealermanagementsystem.Model.BidsPostedListModel;
import com.falconnect.dealermanagementsystem.Model.BidsPostingListModel;
import com.falconnect.dealermanagementsystem.R;

import java.util.List;

public class BidsPostingListAdapter extends ArrayAdapter<BidsPostingListModel> {

    List<BidsPostingListModel> products;
    private Context context;

    ViewHolder holder;

    public BidsPostingListAdapter(Context context, List<BidsPostingListModel> products) {
        super(context, R.layout.bids_posting_listview_position_single_item, products);
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public BidsPostingListModel getItem(int position) {
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
            convertView = inflater.inflate(R.layout.bids_posting_listview_position_single_item, null);

            holder = new ViewHolder();

            holder.bid_name = (TextView) convertView.findViewById(R.id.bid_posting_name);
            holder.bid_rate = (TextView) convertView.findViewById(R.id.bids_posting_rate);
            holder.bid_date = (TextView) convertView.findViewById(R.id.bids_posting_date);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final BidsPostingListModel bidsPostingListModel = getItem(position);

        holder.bid_name.setText(bidsPostingListModel.getDealername());
        holder.bid_rate.setText(bidsPostingListModel.getAmount());
        holder.bid_date.setText(bidsPostingListModel.getDate());

        return convertView;
    }


    private class ViewHolder {
        TextView bid_name, bid_rate, bid_date;
    }

}