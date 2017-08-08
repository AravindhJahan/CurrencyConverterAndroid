package com.falconnect.dealermanagementsystem.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.falconnect.dealermanagementsystem.Model.MyPostingDetailsDataModel;
import com.falconnect.dealermanagementsystem.R;

import java.util.ArrayList;

public class MyPostingDetailsCustomAdapter extends RecyclerView.Adapter<MyPostingDetailsCustomAdapter.MyViewHolder> {

    private ArrayList<MyPostingDetailsDataModel> dataSet;
    private Context mContext;

    public MyPostingDetailsCustomAdapter(Context context, ArrayList<MyPostingDetailsDataModel> data) {
        this.mContext = context;
        this.dataSet = data;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tab_layout, null);

        MyPostingDetailsCustomAdapter.MyViewHolder myViewHolder = new MyPostingDetailsCustomAdapter.MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        final MyPostingDetailsDataModel singleItem_manage = dataSet.get(listPosition);




        holder.title.setText(singleItem_manage.getMypostingname());
        holder.icons.setImageResource(singleItem_manage.getMypostingimage());

    }


    @Override
    public int getItemCount() {
        return (null != dataSet ? dataSet.size() : 0);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView icons;
        TextView title;

        public MyViewHolder(View itemView) {
            super(itemView);

            icons = (ImageView) itemView.findViewById(R.id.tab_icon_petrol);
            title = (TextView) itemView.findViewById(R.id.tab_title);
        }
    }


}
