package com.falconnect.dealermanagementsystem.Adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.falconnect.dealermanagementsystem.Model.MyBranchListModel;
import com.falconnect.dealermanagementsystem.R;

import java.util.List;

public class MyBranchListAdapter extends ArrayAdapter<MyBranchListModel> {

    List<MyBranchListModel> products;
    private Context context;
    ViewHolder holder;

    public MyBranchListAdapter(Context context, List<MyBranchListModel> products) {
        super(context, R.layout.branch_single_item, products);
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public MyBranchListModel getItem(int position) {
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
            convertView = inflater.inflate(R.layout.branch_single_item, null);

            holder = new ViewHolder();

            holder.name = (TextView) convertView.findViewById(R.id.main_name);
            holder.location = (TextView) convertView.findViewById(R.id.location_branch);
            holder.phone_num = (TextView) convertView.findViewById(R.id.call_branch);
            holder.email = (TextView) convertView.findViewById(R.id.email_branch);
            holder.service_center = (ImageView) convertView.findViewById(R.id.service_center);
            holder.text = (TextView) convertView.findViewById(R.id.textviwe);

            holder.head_quaters = (ImageView) convertView.findViewById(R.id.head_quaters);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final MyBranchListModel myBranchListModel = getItem(position);

        holder.name.setText(myBranchListModel.getDealer_name());
        holder.location.setText(myBranchListModel.getBranch_address());
        holder.phone_num.setText(myBranchListModel.getDealer_contact_no());
        holder.email.setText(myBranchListModel.getDealer_mail());


        if (myBranchListModel.getHead_quater().equals("1")) {
            holder.head_quaters.setVisibility(View.VISIBLE);
        } else {
            holder.head_quaters.setVisibility(View.GONE);
        }

        if (myBranchListModel.getDealer_service().equals("1")) {
            holder.service_center.setVisibility(View.VISIBLE);
            holder.text.setVisibility(View.VISIBLE);
        } else {
            holder.service_center.setVisibility(View.GONE);
            holder.text.setVisibility(View.GONE);
        }

        return convertView;
    }

    private class ViewHolder {
        ImageView head_quaters, service_center;
        TextView name, location, phone_num, email;
        TextView text;
    }

}