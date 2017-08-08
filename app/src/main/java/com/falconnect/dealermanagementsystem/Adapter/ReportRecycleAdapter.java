package com.falconnect.dealermanagementsystem.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.falconnect.dealermanagementsystem.Model.StaticReportModel;
import com.falconnect.dealermanagementsystem.R;

import java.util.ArrayList;

public class ReportRecycleAdapter extends RecyclerView.Adapter<ReportRecycleAdapter.MyViewHolder> {

    ArrayList<Integer> colors = new ArrayList<>();
    private ArrayList<StaticReportModel> reportdataSet;
    private Context mContext;

    public ReportRecycleAdapter(Context context, ArrayList<StaticReportModel> reportdata) {
        this.mContext = context;
        this.reportdataSet = reportdata;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_list_datas, null);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        final StaticReportModel singleItem_sell = reportdataSet.get(listPosition);

        colors.add(Color.parseColor("#f7464a"));
        colors.add(Color.parseColor("#46bfbd"));
        colors.add(Color.parseColor("#fdb45c"));
        colors.add(Color.parseColor("#949fb1"));
        colors.add(Color.parseColor("#4d5360"));

        holder.text_ViewName.setText(singleItem_sell.getCar_name());
        holder.text_view_count.setText("Total View : " + singleItem_sell.getCar_count());

        Glide.with(mContext)
                .load(singleItem_sell.getCar_image())
                .into(holder.image_ViewIcon);

        holder.linearLayout.setBackgroundColor(colors.get(listPosition));

    }


    @Override
    public int getItemCount() {
        return (null != reportdataSet ? reportdataSet.size() : 0);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView text_ViewName, text_view_count;
        ImageView image_ViewIcon;
        LinearLayout linearLayout;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.text_ViewName = (TextView) itemView.findViewById(R.id.car_name);
            this.image_ViewIcon = (ImageView) itemView.findViewById(R.id.car_image);
            this.text_view_count = (TextView) itemView.findViewById(R.id.car_view);
            this.linearLayout = (LinearLayout) itemView.findViewById(R.id.background_color);

        }
    }


}