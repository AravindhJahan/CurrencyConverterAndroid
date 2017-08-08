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

import com.falconnect.dealermanagementsystem.Model.ReportFooterDataModel;
import com.falconnect.dealermanagementsystem.R;
import com.falconnect.dealermanagementsystem.ReportSalesActivity;
import com.falconnect.dealermanagementsystem.Report_Inventory;
import com.falconnect.dealermanagementsystem.Staticts_Report;

import java.util.ArrayList;

public class ReportFooterCustomAdapter extends RecyclerView.Adapter<ReportFooterCustomAdapter.MyViewHolder> {

    private ArrayList<ReportFooterDataModel> reportdataSet;
    private Context mContext;

    public ReportFooterCustomAdapter(Context context, ArrayList<ReportFooterDataModel> reportdata) {
        this.mContext = context;
        this.reportdataSet = reportdata;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_footer_list_view_single, null);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        final ReportFooterDataModel singleItem_sell = reportdataSet.get(listPosition);

        holder.text_ViewName.setText(singleItem_sell.getReportname());
        holder.image_ViewIcon.setImageResource(singleItem_sell.getReportimage());
        holder.category_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (singleItem_sell.getReportfooterid_() == 0) {
                    //Current screen to next screen navigate
                    Intent intent = new Intent(mContext.getApplicationContext(), ReportSalesActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    mContext.startActivity(intent);
                    //current screen finish
                    ((Activity) mContext).finish();
                    //Toast.makeText(mContext, "Selected :" + singleItem_sell.getReportname(), Toast.LENGTH_SHORT).show();
                } else if (singleItem_sell.getReportfooterid_() == 1) {
                    Intent intent = new Intent(mContext.getApplicationContext(), Report_Inventory.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    mContext.startActivity(intent);
                    ((Activity) mContext).finish();
                    // Toast.makeText(mContext, "Selected :" + singleItem_sell.getReportname(), Toast.LENGTH_SHORT).show();
                } else if (singleItem_sell.getReportfooterid_() == 2) {
                    Intent intent = new Intent(mContext.getApplicationContext(), Staticts_Report.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    mContext.startActivity(intent);
                    ((Activity) mContext).finish();
                    //Toast.makeText(mContext, "Selected :" + singleItem_sell.getReportname(), Toast.LENGTH_SHORT).show();
                } else {

                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return (null != reportdataSet ? reportdataSet.size() : 0);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView text_ViewName;
        ImageView image_ViewIcon;
        LinearLayout category_item;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.text_ViewName = (TextView) itemView.findViewById(R.id.mtitle);
            this.image_ViewIcon = (ImageView) itemView.findViewById(R.id.image_footer);
            this.category_item = (LinearLayout) itemView.findViewById(R.id.category_item);

        }
    }


}