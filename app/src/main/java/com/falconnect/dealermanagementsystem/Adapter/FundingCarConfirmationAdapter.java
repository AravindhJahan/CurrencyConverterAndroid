package com.falconnect.dealermanagementsystem.Adapter;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.falconnect.dealermanagementsystem.FontAdapter.RoundImageTransform;
import com.falconnect.dealermanagementsystem.R;

import java.util.ArrayList;

public class FundingCarConfirmationAdapter extends ArrayAdapter<String> {

    ViewHolder holder;
    ArrayList<String> car_id;
    ArrayList<String> car_names;
    ArrayList<String> car_image;
    ArrayList<String> car_price;
    private Context context;

    public FundingCarConfirmationAdapter(Context context, ArrayList<String> car_id, ArrayList<String> car_names, ArrayList<String> car_image, ArrayList<String> car_price) {
        super(context, R.layout.fund_confirmation_car_single_item, car_id);
        this.context = context;
        this.car_id = car_id;
        this.car_names = car_names;
        this.car_image = car_image;
        this.car_price = car_price;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fund_confirmation_car_single_item, null, true);

            holder = new ViewHolder();

            holder.car_image = (ImageView) convertView.findViewById(R.id.car_list_image1);
            holder.car_name = (TextView) convertView.findViewById(R.id.fundingcar_name1);
            holder.price = (TextView) convertView.findViewById(R.id.fundingcar_rate1);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Glide.with(getContext())
                .load(car_image.get(position))
                .transform(new RoundImageTransform(getContext()))
                .into(holder.car_image);

        holder.car_name.setText(car_names.get(position));

        holder.price.setText("Rs: " + car_price.get(position));

        Log.e("car_namesasad", "namessssas");


        return convertView;
    }


    private class ViewHolder {
        ImageView car_image;
        TextView car_name, price;

    }


}