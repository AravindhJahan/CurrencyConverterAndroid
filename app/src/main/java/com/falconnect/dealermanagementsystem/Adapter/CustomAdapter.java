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

import com.falconnect.dealermanagementsystem.AlertActivity;
import com.falconnect.dealermanagementsystem.DashBoard;
import com.falconnect.dealermanagementsystem.Model.DataModel;
import com.falconnect.dealermanagementsystem.MyQueriesActivity;
import com.falconnect.dealermanagementsystem.R;
import com.falconnect.dealermanagementsystem.SavedCarActivity;
import com.falconnect.dealermanagementsystem.SharedPreference.PlandetailsSharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    PlandetailsSharedPreferences plandetailsSharedPreferences;
    HashMap<String, String> plan;
    private ArrayList<DataModel> dataSet;
    private Context mContext;

    public CustomAdapter(Context context, ArrayList<DataModel> data) {
        this.mContext = context;
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_list_view_single, null);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        final DataModel singleItem = dataSet.get(listPosition);

        holder.textViewName.setText(singleItem.getName());

        holder.imageViewIcon.setImageResource(singleItem.getImage());

        holder.category_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (singleItem.getId() == 0) {
                    //Current screen to next screen navigate
                    Intent intent = new Intent(mContext.getApplicationContext(), DashBoard.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    mContext.startActivity(intent);

                    //current screen finish
                    ((Activity)mContext).finish();

                    // Toast.makeText(mContext, "Selected :" + singleItem.getName(), Toast.LENGTH_SHORT).show();
                } else if (singleItem.getId() == 1) {
                    //Current screen to next screen navigate
                    plandetailsSharedPreferences = new PlandetailsSharedPreferences(mContext.getApplicationContext());
                    plan = plandetailsSharedPreferences.getUserDetails();
//                    if (plan.get("PlanName").equals("BASIC")) {
//                        Intent intent = new Intent(mContext.getApplicationContext(), BuyPlanActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                        mContext.startActivity(intent);
//
//                        //mContext.startActivity(intent);
//                        //((Activity) mContext).finish();
//                    } else {
                        Intent intent = new Intent(mContext.getApplicationContext(), SavedCarActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        mContext.startActivity(intent);

                        //current screen finish
                        ((Activity) mContext).finish();
//                    }
                    //   Toast.makeText(mContext, "Selected :" + singleItem.getName(), Toast.LENGTH_SHORT).show();
                } else if (singleItem.getId() == 2) {
                    //Current screen to next screen navigate
                    Intent intent = new Intent(mContext.getApplicationContext(), MyQueriesActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    mContext.startActivity(intent);

                    //current screen finish
                    ((Activity)mContext).finish();

                    // Toast.makeText(mContext, "Selected :" + singleItem.getName(), Toast.LENGTH_SHORT).show();
                } else if (singleItem.getId() == 3) {

                    //Current screen to next screen navigate
                    Intent intent = new Intent(mContext.getApplicationContext(), AlertActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    mContext.startActivity(intent);

                    //current screen finish
                    ((Activity)mContext).finish();


                    //  Toast.makeText(mContext, "Selected :" + singleItem.getName(), Toast.LENGTH_SHORT).show();
                } else if (singleItem.getId() == 4) {
                   /* //Current screen to next screen navigate
                    Intent intent = new Intent(mContext.getApplicationContext(), FundingActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    mContext.startActivity(intent);

                    //current screen finish
                    ((Activity)mContext).finish();

                    Toast.makeText(mContext, "Selected :" + singleItem.getName(), Toast.LENGTH_SHORT).show();*/
                } else {

                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return (null != dataSet ? dataSet.size() : 0);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        ImageView imageViewIcon;
        LinearLayout category_item;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.mtitle);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.image_footer);
            this.category_item = (LinearLayout) itemView.findViewById(R.id.category_item);


        }
    }


}