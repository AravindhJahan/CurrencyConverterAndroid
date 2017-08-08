package com.falconnect.dealermanagementsystem.Adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.falconnect.dealermanagementsystem.DealerBussinessProfile;
import com.falconnect.dealermanagementsystem.FontAdapter.RoundImageTransform;
import com.falconnect.dealermanagementsystem.Model.DealerSearchModel;
import com.falconnect.dealermanagementsystem.R;

import java.util.List;

public class DealerSearchAdapter extends ArrayAdapter<DealerSearchModel> {

    List<DealerSearchModel> products;
    ViewHolder holder;
    DealerSearchModel queriesReceivedListModel;
    private Context context;
    private int lastPosition = -1;

    public DealerSearchAdapter(Context context, List<DealerSearchModel> products) {
        super(context, R.layout.quereis_received_list_single, products);
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public DealerSearchModel getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.dealer_list, null);

            holder = new ViewHolder();

            holder.dealer_image = (ImageView) convertView.findViewById(R.id.queryimage1);

            holder.dealer_name = (TextView) convertView.findViewById(R.id.dealer_name);
            holder.dealer_car = (TextView) convertView.findViewById(R.id.dealer_cars_name);
            holder.phone = (TextView) convertView.findViewById(R.id.dealer_mobile_number);
            holder.email = (TextView) convertView.findViewById(R.id.dealer_email);
            holder.address = (TextView) convertView.findViewById(R.id.dealer_address);

            holder.car = (Button) convertView.findViewById(R.id.car_count);

            holder.item = (RelativeLayout) convertView.findViewById(R.id.category_item);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        queriesReceivedListModel = getItem(position);

        Glide.with(getContext())
                .load(queriesReceivedListModel.getLogo())
                .transform(new RoundImageTransform(getContext()))
                .into(holder.dealer_image);

        holder.dealer_name.setText(queriesReceivedListModel.getDealer_name());
        holder.dealer_car.setText(queriesReceivedListModel.getDealership_name());
        holder.phone.setText(queriesReceivedListModel.getD_mobile());
        holder.email.setText(queriesReceivedListModel.getD_email());
        holder.address.setText(queriesReceivedListModel.getCity());

        holder.car.setText(queriesReceivedListModel.getDealercarno() + " Cars");

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DealerBussinessProfile.class);
                intent.putExtra("dealer_id", products.get(position).getId());
                //intent.putExtra("dealet_name", products.get(position).getDealer_name());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    private class ViewHolder {

        ImageView dealer_image;
        TextView dealer_name, phone, email, address, dealer_car;
        Button car;
        RelativeLayout item;
    }

}