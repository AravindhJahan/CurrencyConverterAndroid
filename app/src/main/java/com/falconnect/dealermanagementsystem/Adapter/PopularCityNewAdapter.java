package com.falconnect.dealermanagementsystem.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.falconnect.dealermanagementsystem.Model.PopularCityDataModel;
import com.falconnect.dealermanagementsystem.R;
import com.falconnect.dealermanagementsystem.SearchResultActivity;

import java.util.ArrayList;

public class PopularCityNewAdapter extends RecyclerView.Adapter<PopularCityNewAdapter.MyViewHolder> {

    private ArrayList<PopularCityDataModel> dataSetdata;
    private Context mContext;

    public PopularCityNewAdapter(Context context, ArrayList<PopularCityDataModel> data) {
        this.mContext = context;
        this.dataSetdata = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_city_single_item, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        final PopularCityDataModel singleItemdata = dataSetdata.get(listPosition);

        holder.textView.setText(singleItemdata.getName());

        holder.imageView.setImageResource(singleItemdata.getImage());

        holder.category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Current screen to next screen navigate
                // Toast.makeText(mContext, "Selected Item : " + singleItemdata.getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, SearchResultActivity.class);
                intent.putExtra("City", singleItemdata.getName());
                mContext.startActivity(intent);

                //current screen finish
                ((Activity) mContext).finish();


            }
        });

    }


    @Override
    public int getItemCount() {
        return (null != dataSetdata ? dataSetdata.size() : 0);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;
        LinearLayout category;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.popular_city_title);
            this.imageView = (ImageView) itemView.findViewById(R.id.popular_city_image);
            this.category = (LinearLayout) itemView.findViewById(R.id.layout);
        }
    }


}