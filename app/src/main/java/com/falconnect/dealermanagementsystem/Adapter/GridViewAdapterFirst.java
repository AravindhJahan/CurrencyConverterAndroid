package com.falconnect.dealermanagementsystem.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.falconnect.dealermanagementsystem.R;

import java.util.List;


public class GridViewAdapterFirst extends BaseAdapter {

    private final List<String> datasfirst;
    private final List<String> valuesfirst;
    private Context context;

    public GridViewAdapterFirst(Context context, List<String> datasfirst, List<String> valuesfirst) {
        this.context = context;
        this.datasfirst = datasfirst;
        this.valuesfirst = valuesfirst;
     }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridViewfirst;

        if (convertView == null) {

            gridViewfirst = new View(context);

            gridViewfirst = inflater.inflate(R.layout.grid_view_first_single_item, null);

            TextView add_fund_dashboard = (TextView) gridViewfirst.findViewById(R.id.add_fund_dashboard);

            add_fund_dashboard.setText(datasfirst.get(position) + " (" + valuesfirst.get(position) + ")");

        } else {
            gridViewfirst = convertView;
        }

        return gridViewfirst;
    }

    public int getCount() {
        return datasfirst.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

}
