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
import com.falconnect.dealermanagementsystem.Model.BussinessProfileModel;
import com.falconnect.dealermanagementsystem.R;

import java.util.List;

public class BussinessProfileListAdapter extends ArrayAdapter<BussinessProfileModel> {

    List<BussinessProfileModel> products;
    ViewHolder holder;
    private Context context;

    public BussinessProfileListAdapter(Context context, List<BussinessProfileModel> products) {
        super(context, R.layout.bussiness_profile_single_item, products);
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public BussinessProfileModel getItem(int position) {
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
            convertView = inflater.inflate(R.layout.bussiness_profile_single_item, null);

            holder = new ViewHolder();


            holder.car = (ImageView) convertView.findViewById(R.id.car_image_bussiness_profile);

            holder.car_name = (TextView) convertView.findViewById(R.id.car_name_bussiness_profile);
            holder.address = (TextView) convertView.findViewById(R.id.address_bussiness_profile);
            holder.details = (TextView) convertView.findViewById(R.id.details_bussiness_profile);
            holder.rate = (TextView) convertView.findViewById(R.id.rate_bussiness_profile);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final BussinessProfileModel bussinessProfileModel = getItem(position);

        Glide.with(getContext())
                .load(bussinessProfileModel.getImage())
                .placeholder(R.drawable.carimageplaceholder)
                .transform(new RoundImageTransform(getContext()))
                .into(holder.car);

        holder.car_name.setText(bussinessProfileModel.getModel());

        holder.address.setText(bussinessProfileModel.getCar_locality() + " " + "-" + " " + bussinessProfileModel.getDays());

        holder.rate.setText("\u20B9" + " " + bussinessProfileModel.getPrice());

        holder.details.setText(bussinessProfileModel.getKms_done() +"Km" + " | " +
                        bussinessProfileModel.getFuel_type() + " | " + bussinessProfileModel.getRegistration_year()+ " | "
                + bussinessProfileModel.getOwner_type() + " " + "Owner");

        return convertView;
    }

    private class ViewHolder{
        ImageView car;
        TextView car_name, address, details, rate;
    }

}