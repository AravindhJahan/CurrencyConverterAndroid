package com.falconnect.dealermanagementsystem.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.falconnect.dealermanagementsystem.Model.SellFooterDataModel;
import com.falconnect.dealermanagementsystem.R;
import java.util.ArrayList;

public class SellCustomAdapter extends RecyclerView.Adapter<SellCustomAdapter.MyViewHolder> {

    private ArrayList<SellFooterDataModel> selldataSet;
    private Context mContext;

    public SellCustomAdapter(Context context, ArrayList<SellFooterDataModel> selldata) {
        this.mContext = context;
        this.selldataSet = selldata;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myinventry_recycle_content, null);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        final SellFooterDataModel sell_singleItem = selldataSet.get(listPosition);

        holder.sell_text_ViewName.setText(sell_singleItem.getSellname());
        holder.sell_image_ViewIcon.setImageResource(sell_singleItem.getSellimage());

    }


    @Override
    public int getItemCount() {
        return (null != selldataSet ? selldataSet.size() : 0);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView sell_text_ViewName;
        ImageView sell_image_ViewIcon;
        LinearLayout sell_category_item;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.sell_text_ViewName = (TextView) itemView.findViewById(R.id.sell_mtitle);
            this.sell_image_ViewIcon = (ImageView) itemView.findViewById(R.id.sell_image_footer);
            this.sell_category_item = (LinearLayout) itemView.findViewById(R.id.sell_category_item);

        }
    }


}