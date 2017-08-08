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

import com.falconnect.dealermanagementsystem.Model.SellFooterDataModel;
import com.falconnect.dealermanagementsystem.MyPostingActivity;
import com.falconnect.dealermanagementsystem.QueriesRecievedActivity;
import com.falconnect.dealermanagementsystem.R;
import com.falconnect.dealermanagementsystem.SellDashBoardActivity;

import java.util.ArrayList;

public class SellFooterCustomAdapter extends RecyclerView.Adapter<SellFooterCustomAdapter.MyViewHolder> {

    private ArrayList<SellFooterDataModel> selldataSet;
    private Context mContext;

    public SellFooterCustomAdapter(Context context, ArrayList<SellFooterDataModel> selldata) {
        this.mContext = context;
        this.selldataSet = selldata;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sell_footer_list_view_single, null);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        final SellFooterDataModel singleItem_sell = selldataSet.get(listPosition);

        holder.text_ViewName.setText(singleItem_sell.getSellname());
        holder.image_ViewIcon.setImageResource(singleItem_sell.getSellimage());
        holder.category_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (singleItem_sell.getSellfooterid_() == 0) {
                    //Current screen to next screen navigate
                    Intent intent = new Intent(mContext.getApplicationContext(), SellDashBoardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    mContext.startActivity(intent);
                    //current screen finish
                    ((Activity)mContext).finish();
//                    Toast.makeText(mContext, "Selected :" + singleItem_sell.getSellname(), Toast.LENGTH_SHORT).show();
                } else if (singleItem_sell.getSellfooterid_() == 1) {
                    //Current screen to next screen navigate
                    Intent intent = new Intent(mContext.getApplicationContext(), MyPostingActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    mContext.startActivity(intent);
                    //current screen finish
                    ((Activity)mContext).finish();
//                    Toast.makeText(mContext, "Selected :" + singleItem_sell.getSellname(), Toast.LENGTH_SHORT).show();
                } /*else if (singleItem_sell.getSellfooterid_() == 2) {
                    //Current screen to next screen navigate
                    Intent intent = new Intent(mContext.getApplicationContext(), AuctionActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    mContext.startActivity(intent);
                    //current screen finish
                    ((Activity)mContext).finish();
//                    Toast.makeText(mContext, "Selected :" + singleItem_sell.getSellname(), Toast.LENGTH_SHORT).show();
                } */else if (singleItem_sell.getSellfooterid_() == 2) {
                    Intent intent = new Intent(mContext.getApplicationContext(), QueriesRecievedActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    mContext.startActivity(intent);
                    //current screen finish
                    ((Activity)mContext).finish();
//                    Toast.makeText(mContext, "Selected :" + singleItem_sell.getSellname(), Toast.LENGTH_SHORT).show();
                }/* else if (singleItem_sell.getSellfooterid_() == 4) {
                    //Current screen to next screen navigate
                    Intent intent = new Intent(mContext.getApplicationContext(), LoanActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    mContext.startActivity(intent);
                    //current screen finish
                    ((Activity)mContext).finish();
//                    Toast.makeText(mContext, "Selected :" + singleItem_sell.getSellname(), Toast.LENGTH_SHORT).show();
                }*/ else {

                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return (null != selldataSet ? selldataSet.size() : 0);
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