package com.falconnect.dealermanagementsystem.Adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.falconnect.dealermanagementsystem.Model.AlertRightModel;
import com.falconnect.dealermanagementsystem.R;

import java.util.List;

import static com.falconnect.dealermanagementsystem.R.id.date;
import static com.falconnect.dealermanagementsystem.R.id.state;

public class Right_sidebar extends ArrayAdapter<AlertRightModel> {

    List<AlertRightModel> loanModels;
    ViewHolder holder;
    private Context context;

    public Right_sidebar(Context context, List<AlertRightModel> loanModels) {
        super(context, R.layout.right_nav_single_item, loanModels);
        this.context = context;
        this.loanModels = loanModels;
    }

    @Override
    public int getCount() {
        return loanModels.size();
    }

    @Override
    public AlertRightModel getItem(int position) {
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
            convertView = inflater.inflate(R.layout.right_nav_single_item, null);

            holder = new ViewHolder();

            holder.message = (TextView) convertView.findViewById(R.id.message);
            holder.date = (TextView) convertView.findViewById(date);
            holder.state = (TextView) convertView.findViewById(state);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        final AlertRightModel loanModel = getItem(position);

        holder.message.setText(loanModel.getMessage());
        holder.date.setText(loanModel.getDate());
        holder.state.setText(loanModel.getState());


        return convertView;
    }

    private class ViewHolder {
        TextView message, state, date;
    }

}