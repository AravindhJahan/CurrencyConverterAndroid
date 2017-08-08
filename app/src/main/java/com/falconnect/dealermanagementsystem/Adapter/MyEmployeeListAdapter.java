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
import com.falconnect.dealermanagementsystem.Model.MyEmployeeModel;
import com.falconnect.dealermanagementsystem.R;

import java.util.List;

public class MyEmployeeListAdapter extends ArrayAdapter<MyEmployeeModel> {

    List<MyEmployeeModel> userModels;
    ViewHolder holder;
    private Context context;

    public MyEmployeeListAdapter(Context context, List<MyEmployeeModel> userModels) {
        super(context, R.layout.employee_single_item, userModels);
        this.context = context;
        this.userModels = userModels;
    }

    @Override
    public int getCount() {
        return userModels.size();
    }

    @Override
    public MyEmployeeModel getItem(int position) {
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
            convertView = inflater.inflate(R.layout.employee_single_item, null);

            holder = new ViewHolder();


            holder.employee_image = (ImageView) convertView.findViewById(R.id.employee_image);

            holder.employee_name = (TextView) convertView.findViewById(R.id.employee_name);
            holder.employee_mobilenum = (TextView) convertView.findViewById(R.id.employee_mobilenum);
            holder.employee_email = (TextView) convertView.findViewById(R.id.employee_email);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        final MyEmployeeModel userModels = getItem(position);


        if (userModels.getContactimage().isEmpty()) {
            Glide.with(context)
                    .load(R.drawable.carimageplaceholder)
                    .transform(new RoundImageTransform(getContext()))
                    .into(holder.employee_image);

        } else {
            Glide.with(context)
                    .load(userModels.getContactimage())
                    .transform(new RoundImageTransform(getContext()))
                    .into(holder.employee_image);
        }

        holder.employee_name.setText(userModels.getEmployee_name());

        holder.employee_mobilenum.setText(userModels.getEmployee_mobile());

        holder.employee_email.setText(userModels.getEmployee_email());

        return convertView;
    }

    private class ViewHolder {
        TextView employee_name, employee_mobilenum, employee_email;

        ImageView employee_image;

    }

}