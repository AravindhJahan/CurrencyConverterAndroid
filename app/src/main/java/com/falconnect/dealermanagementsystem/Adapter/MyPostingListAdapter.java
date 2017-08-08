package com.falconnect.dealermanagementsystem.Adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.falconnect.dealermanagementsystem.FontAdapter.RoundImageTransform;
import com.falconnect.dealermanagementsystem.Model.MyPostingListModel;
import com.falconnect.dealermanagementsystem.R;

import java.util.List;

public class MyPostingListAdapter extends ArrayAdapter<MyPostingListModel> {

    List<MyPostingListModel> loanModels;
    ViewHolder holder;
    private Context context;

    public MyPostingListAdapter(Context context, List<MyPostingListModel> loanModels) {
        super(context, R.layout.my_posting_single_item, loanModels);
        this.context = context;
        this.loanModels = loanModels;
    }

    @Override
    public int getCount() {
        return loanModels.size();
    }

    @Override
    public MyPostingListModel getItem(int position) {
        return loanModels.get(position);
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
            convertView = inflater.inflate(R.layout.my_posting_single_item, null);

            holder = new ViewHolder();

            holder.car_details = (TextView) convertView.findViewById(R.id.mypostion_kms_fuel_year_owner);
            holder.model = (TextView) convertView.findViewById(R.id.myposting_name1);
            holder.myposting_rate1 = (TextView) convertView.findViewById(R.id.myposting_rate1);
            holder.imageurl = (ImageView) convertView.findViewById(R.id.myposting_carimage1);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        final MyPostingListModel myPostingListModel = getItem(position);

        Glide.with(context)
                .load(myPostingListModel.getImageurl())
                .transform(new RoundImageTransform(getContext()))
                .placeholder(R.drawable.carimageplaceholder)
                .into(holder.imageurl);

        holder.car_details.setText(myPostingListModel.getKms() + " KMS" + "|"  + " "  + myPostingListModel.getFuel_type() + " "+ "|"
                + " " + myPostingListModel.getYear() + " " + "|"  + " "  + myPostingListModel.getOwner() + " owner");

        //holder.mongopushdate.setText(myPostingListModel.getMongopushdate());

        holder.model.setText(myPostingListModel.getModel());
        holder.myposting_rate1.setText(myPostingListModel.getPrice());

        return convertView;
    }

    private class ViewHolder {
        TextView car_details, model, myposting_rate1;
        ImageView imageurl;
    }

}