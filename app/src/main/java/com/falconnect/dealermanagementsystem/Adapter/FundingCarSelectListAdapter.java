package com.falconnect.dealermanagementsystem.Adapter;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.falconnect.dealermanagementsystem.FontAdapter.RoundImageTransform;
import com.falconnect.dealermanagementsystem.FundingSelectCarActivity;
import com.falconnect.dealermanagementsystem.Model.FundingCarSelectModel;
import com.falconnect.dealermanagementsystem.R;
import com.falconnect.dealermanagementsystem.SharedPreference.LoanFundingSharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;

public class FundingCarSelectListAdapter extends ArrayAdapter<FundingCarSelectModel> {

    ArrayList<FundingCarSelectModel> userModels;
    ViewHolder holder;
    FundingCarSelectModel myInventoryModel;
    TextView totalamount;
    ArrayList<String> car_id = new ArrayList<>();
    ArrayList<String> car_name = new ArrayList<>();
    ArrayList<String> car_image = new ArrayList<>();
    ArrayList<String> car_price_new = new ArrayList<>();
    String amount;
    ArrayList<String> select_car;
    ArrayList<String> price = new ArrayList<String>();
    ArrayList<String> car_price;
    FundingCarSelectModel p;
    LoanFundingSharedPreferences loanFundingSharedPreferences;
    HashMap<String, String> loan_datas;
    private Context context;

    public FundingCarSelectListAdapter(Context context, ArrayList<FundingCarSelectModel> userModels, ArrayList<String> selct, ArrayList<String> car_price) {
        super(context, R.layout.multiple_select_car_single_item, userModels);
        this.context = context;
        this.userModels = userModels;
        this.select_car = selct;
        this.car_price = car_price;

        for (int i = 0; i < select_car.size(); i++) {
            price.add(i, "0");
            car_id.add(i, "0");
            car_image.add(i, "0");
            car_name.add(i, "0");
            car_price_new.add(i, "0");
            Log.i("Price", price.get(i));
        }
    }


    @Override
    public int getCount() {
        return userModels.size();
    }

    @Override
    public FundingCarSelectModel getItem(int position) {
        return userModels.get(position);
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
            convertView = inflater.inflate(R.layout.multiple_select_car_single_item, null);


            holder = new ViewHolder();

            holder.car_image = (ImageView) convertView.findViewById(R.id.car_list_image);

            holder.car_name = (TextView) convertView.findViewById(R.id.fundingcar_name);
            holder.price = (TextView) convertView.findViewById(R.id.fundingcar_rate);
            holder.car_details = (TextView) convertView.findViewById(R.id.funding_car_details);
            holder.ready_for_sale = (TextView) convertView.findViewById(R.id.funding_ready_for_sale);


            holder.car_select = (CheckBox) convertView.findViewById(R.id.car_select);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        loanFundingSharedPreferences = new LoanFundingSharedPreferences(getContext());
        loan_datas = loanFundingSharedPreferences.getAddress_details();

        myInventoryModel = getItem(position);

        p = getProduct(position);

        Glide.with(context)
                .load(myInventoryModel.getImage())
                .transform(new RoundImageTransform(getContext()))
                .placeholder(R.drawable.carimageplaceholder)
                .into(holder.car_image);


        holder.car_name.setText(myInventoryModel.getModel());

        holder.price.setText(myInventoryModel.getPrice() + " - " + myInventoryModel.getCarstatus());

        holder.car_details.setText(
                myInventoryModel.getKms_done() + " Kms" + " | " +
                        myInventoryModel.getFuel_type() + " | " +
                        myInventoryModel.getRegistration_year() + " | " +
                        myInventoryModel.getOwner_type() + "Owner");

        holder.ready_for_sale.setText(myInventoryModel.getCarstatus());

        holder.car_select.setTag(position);

        if (select_car.get(position).equals("1")) {
            holder.car_select.setChecked(true);
        } else {
            holder.car_select.setChecked(false);
        }

        holder.car_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (loan_datas.get("loan").equals("0")) {
                    String storyId_sms = select_car.get(position);

                    if (storyId_sms.equals("0")) {
                        select_car.set(position, "1");
                        price.set(position, car_price.get(position));
                        Log.e("Price car", price.get(position));

                        car_id.set(position, userModels.get(position).getCar_id());
                        String car_names = userModels.get(position).getModel() + userModels.get(position).getVarient();
                        car_name.set(position, car_names);
                        car_image.set(position, userModels.get(position).getImage());
                        car_price_new.set(position, userModels.get(position).getPrice());


                    } else {
                        select_car.set(position, "0");
                        price.set(position, "0");
                        Log.e("Price car", price.get(position));

                        car_id.set(position, "0");
                        car_name.set(position, "0");
                        car_image.set(position, "0");
                        car_price_new.set(position, "0");
                    }


                    // Toast.makeText(getContext(), "outSide for loop", Toast.LENGTH_SHORT).show();

                    int total = 0;
                    for (int i = 0; i < price.size(); i++) {
                        total = total + Integer.parseInt(price.get(i));
                        Log.i("total amount =", String.valueOf(total));
                    }
                    amount = String.valueOf(total);

                    notifyDataSetChanged();


                    ((FundingSelectCarActivity) context).passvalue(amount, car_id, car_name, car_image, car_price_new);

                } else {
                    int sum = 0;
                    Log.i("sum=", String.valueOf(sum));

                    car_id.clear();
                    car_name.clear();
                    car_image.clear();
                    car_price_new.clear();


                    for (int i = 0; i < select_car.size(); i++) {
                        int m = Integer.parseInt(select_car.get(i));
                        sum = sum + m;
                        Log.i("sum=", String.valueOf(sum));
                    }

                    for (int i = 0; i < select_car.size(); i++) {
                        select_car.set(i, "0");
                        price.set(i, "0");
                        car_id.add(i, "0");
                        car_name.add(i, "0");
                        car_image.add(i, "0");
                        car_price_new.add(i, "0");

                        Log.i("Price", price.get(i));

                    }

                    select_car.set(position, "1");
                    price.set(position, car_price.get(position));
                    Log.e("Price car", price.get(position));

                    car_id.set(position, userModels.get(position).getCar_id());
                    String car_names = userModels.get(position).getModel() + userModels.get(position).getVarient();
                    car_name.set(position, car_names);
                    car_image.set(position, userModels.get(position).getImage());
                    car_price_new.set(position, userModels.get(position).getPrice());

                    //Toast.makeText(getContext(), "outSide for loop", Toast.LENGTH_SHORT).show();

                    int total = 0;
                    for (int i = 0; i < price.size(); i++) {
                        total = total + Integer.parseInt(price.get(i));
                        Log.i("total amount =", String.valueOf(total));
                    }
                    amount = String.valueOf(total);

                    notifyDataSetChanged();

                    ((FundingSelectCarActivity) context).passvalue(amount, car_id, car_name, car_image, car_price_new);

                }
            }
        });

        return convertView;
    }


    FundingCarSelectModel getProduct(int position) {
        return getItem(position);
    }

    public ArrayList<FundingCarSelectModel> getBox() {
        ArrayList<FundingCarSelectModel> box = new ArrayList<FundingCarSelectModel>();
        for (FundingCarSelectModel p : userModels) {
            if (p.box)
                box.add(p);
        }
        return box;
    }

    private class ViewHolder {

        CheckBox car_select;
        ImageView car_image;
        TextView car_name, price, car_details, ready_for_sale;

    }


}