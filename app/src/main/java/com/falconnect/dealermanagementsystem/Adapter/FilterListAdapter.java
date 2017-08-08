package com.falconnect.dealermanagementsystem.Adapter;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.falconnect.dealermanagementsystem.FilterScreen;
import com.falconnect.dealermanagementsystem.FilterScreenDetail;
import com.falconnect.dealermanagementsystem.R;
import com.falconnect.dealermanagementsystem.SharedPreference.JsonValueSharedPreferences;
import com.falconnect.dealermanagementsystem.SharedPreference.SelectedListFilter;

import java.util.ArrayList;
import java.util.HashMap;

public class FilterListAdapter extends ArrayAdapter<String> {

    ArrayList<String> filter_titles;
    ArrayList<String> filter_sites;
    TextView title_list, car_site_text;
    RelativeLayout reset;
    JsonValueSharedPreferences jsonValueSharedPreferences;
    HashMap<String, String> json_vals;
    private Context context;
    String resetValue;
    SelectedListFilter selectedListFilter;
    ArrayList<String> getSeletecte_data;
    ArrayList<String> selected_items = new ArrayList<>();


    public FilterListAdapter(Context context, ArrayList<String> filter_titles, ArrayList<String> filter_sites) {
        super(context, R.layout.filter_single_item, filter_titles);
        this.context = context;
        this.filter_titles = filter_titles;
        this.filter_sites = filter_sites;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.filter_single_item, null);

        title_list = (TextView) convertView.findViewById(R.id.car_sites_text);
        car_site_text = (TextView) convertView.findViewById(R.id.select_car_sites_text);

        title_list.setText(filter_titles.get(position));

        car_site_text.setText(filter_sites.get(position));

        reset = (RelativeLayout) convertView.findViewById(R.id.reset);

        jsonValueSharedPreferences = new JsonValueSharedPreferences(getContext());
        json_vals = jsonValueSharedPreferences.getUserDetails();


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "Clicked reset is::"+ filter_titles.get(position).toString(), Toast.LENGTH_SHORT).show();

                selectedListFilter = new SelectedListFilter(getContext());
                getSeletecte_data = selectedListFilter.getFavorites(context);

                Log.e("getSeletecte_data",getSeletecte_data.toString());

                resetValue=filter_sites.get(position);

                Log.e("reset value",resetValue);

                if (getSeletecte_data.size() == 0)
                {

                }
                else {

                    for (int m = 0; m < getSeletecte_data.size(); m++) {
                        if (resetValue.contains(getSeletecte_data.get(m).toString()))
                        {
                            getSeletecte_data.remove(m);
                        } else {

                        }
                    }
                }
                Log.e("Removed_getSeletecte_da",getSeletecte_data.toString());

                if (getSeletecte_data.size() == 0)
                {

                }
                else {
                        selected_items=getSeletecte_data;
                }

                Log.e("getdataassd",selected_items.toString());

                selectedListFilter.createSelectedItems(getContext(), selected_items);

                if (filter_titles.get(position).equals("Car Sites")) {

                    resetValue=filter_sites.get(position);

                    Log.e("reset value",resetValue);

                    filter_sites.set(position, "Select Car Sites");
                    notifyDataSetChanged();
                } else if (filter_titles.get(position).equals("Listing Types")) {
                    filter_sites.set(position, "Select Listing Types");
                    notifyDataSetChanged();
                } else if (filter_titles.get(position).equals("Car Make")) {
                    filter_sites.set(position, "Select Make");
                    notifyDataSetChanged();
                } else if (filter_titles.get(position).equals("Car Model")) {
                    filter_sites.set(position, "Select Model");
                    notifyDataSetChanged();
                } else if (filter_titles.get(position).equals("Car Year")) {
                    filter_sites.set(position, "Select Year");
                    notifyDataSetChanged();
                } else if (filter_titles.get(position).equals("Transmission")) {
                    filter_sites.set(position, "Select Transmission");
                    notifyDataSetChanged();
                } else if (filter_titles.get(position).equals("Fuel Type")) {
                    filter_sites.set(position, "Select Fuel Type");
                    notifyDataSetChanged();
                } else if (filter_titles.get(position).equals("Price Range")) {
                    filter_sites.set(position, "Select Price Range");
                    notifyDataSetChanged();
                } else if (filter_titles.get(position).equals("Body Type")) {
                    filter_sites.set(position, "Select Body Type");
                    notifyDataSetChanged();
                }

            }
        });

        ((FilterScreen) context).passvalue(filter_sites);

        return convertView;
    }

}