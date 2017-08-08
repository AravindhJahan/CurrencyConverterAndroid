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

import com.falconnect.dealermanagementsystem.EMICalculatorActivity;
import com.falconnect.dealermanagementsystem.FundingActivity;
import com.falconnect.dealermanagementsystem.LoanActivity;
import com.falconnect.dealermanagementsystem.Model.FundingDataModel;
import com.falconnect.dealermanagementsystem.R;
import com.falconnect.dealermanagementsystem.SharedPreference.PlandetailsSharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;

public class FundingFooterCustomAdapter extends RecyclerView.Adapter<FundingFooterCustomAdapter.MyViewHolder> {

    private ArrayList<FundingDataModel> managedataSet;
    private Context mContext;
    PlandetailsSharedPreferences plandetailsSharedPreferences;
    HashMap<String, String> plan;

    public FundingFooterCustomAdapter(Context context, ArrayList<FundingDataModel> managedata) {
        this.mContext = context;
        this.managedataSet = managedata;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.funding_footer_list_view_single, null);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        final FundingDataModel singleItem_manage = managedataSet.get(listPosition);

        holder.text_ViewName.setText(singleItem_manage.getName());
        holder.image_ViewIcon.setImageResource(singleItem_manage.getImage());
        holder.category_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (singleItem_manage.getId() == 0) {
                    plandetailsSharedPreferences = new PlandetailsSharedPreferences(mContext.getApplicationContext());

                    plan = plandetailsSharedPreferences.getUserDetails();

//                    if (plan.get("PlanName").equals("GOLD") || plan.get("PlanName").equals("BASIC")) {
//                        Intent intent = new Intent(mContext.getApplicationContext(),BuyPlanActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//
//                        mContext.startActivity(intent);
//                        //((Activity) mContext).finish();
//                    } else {
                        Intent intent = new Intent(mContext.getApplicationContext(), FundingActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        mContext.startActivity(intent);
                        ((Activity) mContext).finish();
//                    }
                    // Toast.makeText(mContext, "Selected :" + singleItem_manage.getName(), Toast.LENGTH_SHORT).show();
                } else if (singleItem_manage.getId() == 1) {
                    Intent intent = new Intent(mContext.getApplicationContext(), LoanActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    mContext.startActivity(intent);
                    ((Activity) mContext).finish();
                    //  Toast.makeText(mContext, "Selected :" + singleItem_manage.getName(), Toast.LENGTH_SHORT).show();
                } else if (singleItem_manage.getId() == 2) {
                    Intent intent = new Intent(mContext.getApplicationContext(), EMICalculatorActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    mContext.startActivity(intent);
                    ((Activity) mContext).finish();
                    //  Toast.makeText(mContext, "Selected :" + singleItem_manage.getName(), Toast.LENGTH_SHORT).show();
                } else {

                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return (null != managedataSet ? managedataSet.size() : 0);
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