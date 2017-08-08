package com.falconnect.dealermanagementsystem.Adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.falconnect.dealermanagementsystem.R;
import com.falconnect.dealermanagementsystem.SharedPreference.JsonValueSharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;


public class MultiSelectionDetailAdapter<T> extends BaseAdapter {
    Context mContext;
    LayoutInflater mInflater;
    ArrayList<T> mList;
    ArrayList<T> mList_count;
    ArrayList<T> mList_id;
    SparseBooleanArray mSparseBooleanArray;
    int selected_position = -1;
    JsonValueSharedPreferences jsonValueSharedPreferences;
    HashMap<String, String> values_json;

    ArrayList<String> single_select = new ArrayList<>();

    /*

    OnCheckedChangeListener mCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            String budget_value;
            if (values_json.get("budget") != null) {
                budget_value = values_json.get("budget");
            } else {
                budget_value = "0";
            }
            if (budget_value == "1") {



            } else {
                mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
            }

        }
    };
*/

    public MultiSelectionDetailAdapter(Context context, ArrayList<T> list, ArrayList<T> list_count, ArrayList<T> list_id) {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mSparseBooleanArray = new SparseBooleanArray();
        mList = new ArrayList<T>();
        mList_count = new ArrayList<T>();
        mList_id = new ArrayList<T>();
        this.mList = list;
        this.mList_count = list_count;
        this.mList_id = list_id;

    }

    public ArrayList<T> getCheckedItems() {
        ArrayList<T> mTempArry = new ArrayList<T>();
        for (int i = 0; i < mList.size(); i++) {
            if (mSparseBooleanArray.get(i)) {
                mTempArry.add(mList.get(i));

            }
        }
        return mTempArry;
    }


    public ArrayList<T> getCheckedID() {
        ArrayList<T> mTempArry_id = new ArrayList<T>();
        for (int i = 0; i < mList_id.size(); i++) {
            if (mSparseBooleanArray.get(i)) {
                mTempArry_id.add(mList_id.get(i));
            }
        }
        return mTempArry_id;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.filter_details_single_item, null);
        }
        TextView tvTitle = (TextView) convertView.findViewById(R.id.car_details_text);

        tvTitle.setText(mList.get(position).toString() + " (" + mList_count.get(position).toString() + ")");

        final CheckBox mCheckBox = (CheckBox) convertView.findViewById(R.id.check_filter_details);
        mCheckBox.setTag(position);
        mCheckBox.setChecked(mSparseBooleanArray.get(position));

        jsonValueSharedPreferences = new JsonValueSharedPreferences(mContext);
        values_json = jsonValueSharedPreferences.getUserDetails();

        mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String budget_value;
                if (values_json.get("budget") != null) {
                    budget_value = values_json.get("budget");
                } else {
                    budget_value = "0";
                }
                if (budget_value == "1") {

                    if (isChecked) {
                        mCheckBox.setChecked(position == selected_position);
                        selected_position = position;
                        mCheckBox.setSelected(false);
                    } else {
                        mCheckBox.setSelected(true);
                        selected_position = -1;
                    }
                    notifyDataSetChanged();

                } else {
                    mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
                }

            }
        });


        return convertView;
    }
}