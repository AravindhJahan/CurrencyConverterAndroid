package com.falconnect.dealermanagementsystem.Adapter;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.falconnect.dealermanagementsystem.FilterScreenDetail;
import com.falconnect.dealermanagementsystem.Model.FilterDetailPageModel;
import com.falconnect.dealermanagementsystem.R;
import com.falconnect.dealermanagementsystem.SharedPreference.JsonValueSharedPreferences;
import com.falconnect.dealermanagementsystem.SharedPreference.SelectedListFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FilterMultiselectAdapter extends ArrayAdapter<FilterDetailPageModel> {

    ViewHolder holder;
    ArrayList<FilterDetailPageModel> data_titles;

    ArrayList<String> datas_counts;
    ArrayList<String> data_id;

    //ArrayList<String> data_titles;

    FilterDetailPageModel filterDetailPageModel;
    List<String> getSeletecte_data;

    SelectedListFilter selectedListFilter;


    ArrayList<String> select_cat = new ArrayList<String>();
    ArrayList<String> select_count = new ArrayList<String>();
    ArrayList<String> select_cat_id = new ArrayList<String>();

    JsonValueSharedPreferences jsonValueSharedPreferences;
    HashMap<String, String> json_values;
    ArrayList<String> selected_items = new ArrayList<>();
    private Context context;

    //public FilterMultiselectAdapter(Context context, ArrayList<String> datas, ArrayList<String> datacount, ArrayList<String> dataid) {

    public FilterMultiselectAdapter(Context context, ArrayList<FilterDetailPageModel> datas) {
        super(context, R.layout.filter_details_single_item, datas);
        this.context = context;
        this.data_titles = datas;

        //this.datas_counts = datacount;
        //this.data_id = dataid;

        for (int i = 0; i < data_titles.size(); i++) {
            select_cat.add(i, "0");
            select_cat_id.add(i, "0");
            select_count.add(i, "0");

            Log.i("Price", select_cat.get(i));
            Log.i("Price", select_cat_id.get(i));
        }


        selectedListFilter = new SelectedListFilter(getContext());
        getSeletecte_data = selectedListFilter.getFavorites(context);



        if (getSeletecte_data.size() == 0){

        }else {

            for (int k = 0; k < getSeletecte_data.size(); k++){
                for (int j=0;j<data_titles.size();j++) {
                    if (getSeletecte_data.get(k).equals(data_titles.get(j).getTitles())) {

                        select_cat.set(j, getSeletecte_data.get(k).toString());
                        select_cat_id.set(j,data_titles.get(j).getIds());
                        select_count.set(j,"1");
                        Log.e("true", "dalsda");

                        Log.e("select_cat--->",select_cat.toString());
                        Log.e("getSeletecte_data--->",getSeletecte_data.toString());

                    }

                }
            }

            for (int m=0;m<getSeletecte_data.size();m++)
            {
//                selected_items.add(getSeletecte_data.get(m).toString());
            }

        }

    }


    @Override
    public int getCount() {
        return data_titles.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.filter_details_single_item, null);

            jsonValueSharedPreferences = new JsonValueSharedPreferences(getContext());
            json_values = jsonValueSharedPreferences.getUserDetails();


            holder = new ViewHolder();

            holder.tvTitle = (TextView) convertView.findViewById(R.id.car_details_text);

            holder.mCheckBox = (CheckBox) convertView.findViewById(R.id.check_filter_details);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }



        filterDetailPageModel = getItem(position);

        holder.tvTitle.setText(data_titles.get(position).getTitles() + " (" + data_titles.get(position).getCount() + ")");

        holder.mCheckBox.setTag(position);


//        Log.e("selected_items---5555>",selected_items.toString());

        if (!select_cat.get(position).equals("0")) {
            holder.mCheckBox.setChecked(true);
        } else {
            holder.mCheckBox.setChecked(false);
        }




        ((FilterScreenDetail) context).passvalue(select_cat, select_cat_id);

        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String budget_values = null;

                if (json_values.get("budget") == null) {
                    budget_values = "0";
                } else {
                    budget_values = "1";
                }

                if (budget_values.equals("0")) {
                    selected_items = selectedListFilter.getFavorites(context);

                    String storyId_sms = select_count.get(position);

                    if (storyId_sms.equals("0")) {
                        select_cat.set(position, data_titles.get(position).getTitles());
                        select_cat_id.set(position, data_titles.get(position).getIds());
                        select_count.set(position, "1");
                        Log.e("Price car", select_cat.get(position));

                        selected_items.add(data_titles.get(position).getTitles());

                        Log.e("selected_items", selected_items.toString());


                        selectedListFilter.createSelectedItems(getContext(), selected_items);


                    } else {
                        select_cat.set(position, "0");
                        select_cat_id.set(position, "0");
                        select_count.set(position, "0");

                        Log.e("Price car", select_cat.get(position));

                        selected_items.remove(data_titles.get(position).getTitles());
                        Log.e("selected_items", selected_items.toString());

                        selectedListFilter.createSelectedItems(getContext(), selected_items);

                    }

                    notifyDataSetChanged();

                    ((FilterScreenDetail) context).passvalue(select_cat, select_cat_id);

                } else {
                    int sum = 0;
                    Log.i("sum=", String.valueOf(sum));

                    select_cat.clear();
                    select_cat_id.clear();

//                    for (int i = 0; i < data_titles.get(position).getTitles().length(); i++) {
//                        int m = Integer.parseInt(select_count.get(i));
//                        sum = sum + m;
//                        Log.i("sum=", String.valueOf(sum));
//                    }

                    for (int i = 0; i < data_titles.get(position).getTitles().length(); i++) {
                        select_cat.add(i, "0");
                        select_cat_id.add(i, "0");
                    }

                    select_cat.set(position, data_titles.get(position).getTitles());
                    select_cat_id.set(position, data_titles.get(position).getIds());

                    selected_items.add(data_titles.get(position).getTitles());

                    select_count.set(position, "1");

                    Log.e("selected_items", selected_items.toString());

                    notifyDataSetChanged();

                    ((FilterScreenDetail) context).passvalue(select_cat, select_cat_id);

                }
            }
        });


        return convertView;

    }

    private class ViewHolder {

        CheckBox mCheckBox;
        TextView tvTitle;


    }


}