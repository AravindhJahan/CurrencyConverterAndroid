package com.falconnect.dealermanagementsystem.Adapter;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.falconnect.dealermanagementsystem.Model.ApplyFundingListModel;
import com.falconnect.dealermanagementsystem.R;

import java.util.List;

public class ApplyFundingListAdapter extends ArrayAdapter<ApplyFundingListModel> {

    List<ApplyFundingListModel> products;
    ViewHolder holder;
    private Context context;

    public ApplyFundingListAdapter(Context context, List<ApplyFundingListModel> products) {
        super(context, R.layout.apply_fundind_single_item, products);
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public ApplyFundingListModel getItem(int position) {
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
            convertView = inflater.inflate(R.layout.apply_fundind_single_item, null);

            holder = new ViewHolder();

            holder.fund_name = (TextView) convertView.findViewById(R.id.funding_name);
            holder.fund_address = (TextView) convertView.findViewById(R.id.funding_address);
            holder.fund_rate = (TextView) convertView.findViewById(R.id.funding_rate);
            holder.fund_date = (TextView) convertView.findViewById(R.id.funding_date);
            holder.listing_id = (TextView) convertView.findViewById(R.id.funding_listing_id);
            holder.fund_status = (TextView) convertView.findViewById(R.id.funding_status);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ApplyFundingListModel applyFundingListModel = getItem(position);

        holder.fund_name.setText(applyFundingListModel.getDealer_funding_ticket_id());
        holder.listing_id.setText(applyFundingListModel.getDealer_listing_id());
        holder.fund_address.setText(applyFundingListModel.getDealercity());
        holder.fund_date.setText(applyFundingListModel.getCreated_date());
        holder.fund_rate.setText(Html.fromHtml("&#x20B9;" + " " + applyFundingListModel.getRequested_amount()));

        if (applyFundingListModel.getStatus().equals("Approved")) {
            holder.fund_status.setTextColor(Color.GREEN);
            holder.fund_status.setText(applyFundingListModel.getStatus());
        } else if (applyFundingListModel.getStatus().equals("Decline")) {
            holder.fund_status.setTextColor(Color.RED);
            holder.fund_status.setText(applyFundingListModel.getStatus());
        } else if (applyFundingListModel.getStatus().equals("Revoked")) {
            holder.fund_status.setTextColor(Color.RED);
            holder.fund_status.setText(applyFundingListModel.getStatus());
        } else if (applyFundingListModel.getStatus().equals("In Progress")) {
            holder.fund_status.setTextColor(Color.parseColor("#173E84"));
            holder.fund_status.setText(applyFundingListModel.getStatus());
        }



        return convertView;
    }

    private class ViewHolder {
        TextView fund_name, fund_address, fund_rate, fund_date, fund_status, listing_id;
    }

}