package com.falconnect.dealermanagementsystem.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.falconnect.dealermanagementsystem.R;

import java.util.List;


public class LoanListView extends ArrayAdapter<String> {

    List<String> loanModels_title;
    List<String> loanModels;
    ViewHolder holder;
    private Context context;

    public LoanListView(Context context, List<String> loanModels_title, List<String> loanModels) {
        super(context, R.layout.loan_list_single, loanModels);
        this.context = context;
        this.loanModels_title = loanModels_title;
        this.loanModels = loanModels;
    }

    @Override
    public int getCount() {
        return loanModels.size();
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
            convertView = inflater.inflate(R.layout.loan_list_single, null);

            holder = new ViewHolder();

            holder.loan_cust_title = (TextView) convertView.findViewById(R.id.title);
            holder.loan_title_name = (TextView) convertView.findViewById(R.id.title_ans);

            holder.loan_cust_title.setText(loanModels_title.get(position));
            holder.loan_title_name.setText(loanModels.get(position));

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        return convertView;
    }

    private class ViewHolder {
        TextView loan_cust_title, loan_title_name;
    }


}
