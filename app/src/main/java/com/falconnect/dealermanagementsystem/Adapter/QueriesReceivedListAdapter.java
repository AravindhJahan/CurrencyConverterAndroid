package com.falconnect.dealermanagementsystem.Adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.falconnect.dealermanagementsystem.ConstantIP;
import com.falconnect.dealermanagementsystem.FontAdapter.RoundImageTransform;
import com.falconnect.dealermanagementsystem.Model.QueriesReceivedListModel;
import com.falconnect.dealermanagementsystem.R;
import com.falconnect.dealermanagementsystem.SellWebViewActivity;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;

import java.util.HashMap;
import java.util.List;

public class QueriesReceivedListAdapter extends ArrayAdapter<QueriesReceivedListModel> {

    List<QueriesReceivedListModel> products;
    ViewHolder holder;
    QueriesReceivedListModel queriesReceivedListModel;
    HashMap<String, String> user;
    SessionManager session_query_receive;
    private Context context;
    private int lastPosition = -1;

    public QueriesReceivedListAdapter(Context context, List<QueriesReceivedListModel> products) {
        super(context, R.layout.quereis_received_list_single, products);
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public QueriesReceivedListModel getItem(int position) {
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

            convertView = inflater.inflate(R.layout.quereis_received_list_single, null);

            holder = new ViewHolder();

            holder.car_image = (ImageView) convertView.findViewById(R.id.qreceived_list_image);
            holder.car_owner_make = (TextView) convertView.findViewById(R.id.quereies_recieved_name);
            holder.car_name = (TextView) convertView.findViewById(R.id.queries_recieved_car_name);
            holder.car_message = (TextView) convertView.findViewById(R.id.queries_revieved_message);
            holder.car_days = (TextView) convertView.findViewById(R.id.quereies_recieved_date);
            holder.new_card = (RelativeLayout) convertView.findViewById(R.id.category_item);
            holder.listing =(Button) convertView.findViewById(R.id.listing_id);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        session_query_receive = new SessionManager(getContext());
        user = session_query_receive.getUserDetails();

        queriesReceivedListModel = getItem(position);

        Glide.with(context)
                .load(queriesReceivedListModel.getImagelink())
                .placeholder(R.drawable.default_avatar)
                .transform(new RoundImageTransform(getContext()))
                .into(holder.car_image);

        holder.car_owner_make.setText(queriesReceivedListModel.getFrom_dealer_id() + " (" + queriesReceivedListModel.getTitle() + ")");
        holder.car_name.setText(queriesReceivedListModel.getMake());
        holder.car_days.setText(queriesReceivedListModel.getDays());
        holder.car_message.setText(queriesReceivedListModel.getMessage());

       /*Animation animation = AnimationUtils.loadAnimation(getContext(), (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        convertView.startAnimation(animation);
        lastPosition = position;*/

        if (queriesReceivedListModel.getStatus().equals("0")) {
            holder.new_card.setBackgroundColor(Color.WHITE);
        } else {
            holder.new_card.setBackgroundColor(Color.parseColor("#E6E6E6"));
        }

        holder.listing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = ConstantIP.IP + "mobileweb/carview/index.html#/carview/" + user.get("user_id") + "/" + products.get(position).getCar_id() + "/0";
                Intent intent = new Intent(getContext(), SellWebViewActivity.class);
                intent.putExtra("url", url);
                context.startActivity(intent);
                Log.e("url", url);
            }
        });

        holder.new_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = "Queries Detail";
                String url = ConstantIP.IP + "mobileweb/mobilechat/#/sell/" + products.get(position).getContact_transactioncode() + "/" + user.get("user_id");
                Intent intent = new Intent(getContext(), SellWebViewActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("url", url);
                context.startActivity(intent);
            }
        });


        return convertView;
    }

    private class ViewHolder {

        ImageView car_image;
        TextView car_owner_make, car_name, car_days, car_message;
        RelativeLayout new_card;
        Button listing;
    }

}