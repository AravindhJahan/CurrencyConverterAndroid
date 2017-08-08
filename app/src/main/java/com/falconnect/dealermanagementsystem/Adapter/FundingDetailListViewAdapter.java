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


public class FundingDetailListViewAdapter extends ArrayAdapter<String> {
    List<String> products;
    List<String> titles;
    ViewHolder holder;
    private Context context;


    public FundingDetailListViewAdapter(Context context, List<String> products, List<String> titles) {
        super(context, R.layout.queries_list_single, products);
        this.context = context;
        this.products = products;
        this.titles = titles;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public String getItem(int position) {
        return products.get(position);
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
            convertView = inflater.inflate(R.layout.funding_details_list, null);
            holder = new ViewHolder();

            holder.tokent_id = (TextView) convertView.findViewById(R.id.tokent_id);
            holder.tokent_id_text = (TextView) convertView.findViewById(R.id.tokent_id_text);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tokent_id.setText(products.get(position));
        holder.tokent_id_text.setText(titles.get(position));

        return convertView;
    }

    private class ViewHolder {
        TextView tokent_id_text, tokent_id;
    }

}