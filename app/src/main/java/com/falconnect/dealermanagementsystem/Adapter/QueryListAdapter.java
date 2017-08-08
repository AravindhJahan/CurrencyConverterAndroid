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
import com.falconnect.dealermanagementsystem.Model.QueryListModel;
import com.falconnect.dealermanagementsystem.R;
import com.falconnect.dealermanagementsystem.SellWebViewActivity;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;

import java.util.HashMap;
import java.util.List;

public class QueryListAdapter extends ArrayAdapter<QueryListModel> {

    List<QueryListModel> products;
    ViewHolder holder;
    SessionManager sessionManager = new SessionManager(getContext());
    HashMap<String, String> user;
    private Context context;
    private int lastPosition = -1;

    public QueryListAdapter(Context context, List<QueryListModel> products) {
        super(context, R.layout.queries_list_single, products);
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public QueryListModel getItem(int position) {
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
            convertView = inflater.inflate(R.layout.queries_list_single, null);

            holder = new ViewHolder();

            holder.image1 = (ImageView) convertView.findViewById(R.id.queryimage1);
            holder.name = (TextView) convertView.findViewById(R.id.query_owner_name);
            holder.car_name = (TextView) convertView.findViewById(R.id.query_car_name);
            holder.car_message = (TextView) convertView.findViewById(R.id.query_car_details);
            holder.car_date = (TextView) convertView.findViewById(R.id.query_time);
            holder.new_card = (RelativeLayout) convertView.findViewById(R.id.category_item);

            holder.listing_id = (Button) convertView.findViewById(R.id.listing_id);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final QueryListModel queryListModel = getItem(position);

        user = sessionManager.getUserDetails();

        Log.e("Status", queryListModel.getStatus());

        if (queryListModel.getStatus().equals("0")) {
            holder.new_card.setBackgroundColor(Color.WHITE);
        } else {
            holder.new_card.setBackgroundColor(Color.parseColor("#E6E6E6"));
        }

        Glide.with(context)
                .load(queryListModel.getImagelink())
                .transform(new RoundImageTransform(getContext()))
                .placeholder(R.drawable.carimageplaceholder)
                .into(holder.image1);

        holder.name.setText(queryListModel.getTo_dealer_name() + "(" + queryListModel.getHeadertitle() + ")");

        holder.car_name.setText(queryListModel.getTitle());
        holder.car_message.setText(queryListModel.getMessage());
        holder.car_date.setText(queryListModel.getTime());

        holder.listing_id.setOnClickListener(new View.OnClickListener() {
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
                String url = ConstantIP.IP + "mobileweb/mobilechat/#/buy/"
                        + products.get(position).getContact_transactioncode()
                        + "/" + user.get("user_id");
                Intent intent = new Intent(getContext(), SellWebViewActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("url", url);
                context.startActivity(intent);
            }
        });

       /*Animation animation = AnimationUtils.loadAnimation(getContext(), (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        convertView.startAnimation(animation);
        lastPosition = position;*/

        return convertView;
    }

    private class ViewHolder {
        ImageView image1;
        TextView name, car_name, car_message, car_date;
        RelativeLayout new_card;
        Button listing_id;
    }

}