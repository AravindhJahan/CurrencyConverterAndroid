package com.falconnect.dealermanagementsystem.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.falconnect.dealermanagementsystem.FontAdapter.AnimateCounter;
import com.falconnect.dealermanagementsystem.R;

import java.util.List;


public class GridViewAdapter extends BaseAdapter {

    private final List<String> datas;
    private final List<String> values;
    String date;
    View gridView;
    int pos;
    private Context context;

    public GridViewAdapter(Context context, List<String> datas, List<String> values, String date) {
        this.context = context;
        this.datas = datas;
        this.values = values;
        this.date = date;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);



        if (convertView == null) {

            gridView = new View(context);

            gridView = inflater.inflate(R.layout.grid_view_single_item, null);

            //ArrayList String
            TextView textView = (TextView) gridView.findViewById(R.id.instock_live);
            textView.setText(datas.get(position));

            //Dates
            TextView instock_date = (TextView) gridView.findViewById(R.id.instock_date);
            instock_date.setText(date);

            //Values
            TextView instock_count = (TextView) gridView.findViewById(R.id.instock_count);
            instock_count.setText(values.get(position));

            if (instock_count.getText().toString().equals("0")){

            }else{
                AnimateCounter animateCounter = new AnimateCounter.Builder(instock_count)
                        .setCount(0, Integer.parseInt(values.get(position)))
                        .setDuration(2000)
                        .build();

                animateCounter.execute();
            }


        } else {
            gridView = convertView;
        }

        return gridView;
    }

    public int getCount() {
        return datas.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

}
