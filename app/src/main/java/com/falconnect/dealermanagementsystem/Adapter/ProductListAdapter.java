package com.falconnect.dealermanagementsystem.Adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.falconnect.dealermanagementsystem.Constant;
import com.falconnect.dealermanagementsystem.ConstantIP;
import com.falconnect.dealermanagementsystem.Model.SingleProductModel;
import com.falconnect.dealermanagementsystem.R;
import com.falconnect.dealermanagementsystem.SellWebViewActivity;
import com.falconnect.dealermanagementsystem.ServiceHandler;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ProductListAdapter extends ArrayAdapter<SingleProductModel> {

    public MyBounceInterpolator interpolator;
    public ArrayList<HashMap<String, String>> savecarList;
    public ArrayList<HashMap<String, String>> editcarList;
    List<SingleProductModel> products;
    String storyId, storyId_alert, storyId_compare;
    SingleProductModel product;
    SessionManager sessionManager = new SessionManager(getContext());
    HashMap<String, String> user;
    String user_id;
    String result, message;
    HashMap<String, String> savemap;
    HashMap<String, String> editmap;
    ViewHolder holder;
    SingleProductModel m;
    String pos, pos_alert, pos_compare;
    ArrayList<String> arrayListnew;
    ArrayList<String> arrayList_alert;
    ArrayList<String> arrayList_compare;
    ArrayList<String> new_compare;
    int sum;
    private Context context;
    private int lastPosition = -1;


    public ProductListAdapter(Context context, ArrayList<SingleProductModel> products, ArrayList<String> arrayList, ArrayList<String> alert, ArrayList<String> compare) {
        super(context, R.layout.search_list_single_item, products);
        this.context = context;
        this.products = products;
        this.arrayListnew = arrayList;
        this.arrayList_alert = alert;
        this.arrayList_compare = compare;

        new_compare = new ArrayList<String>();
        for (int i = 0; i < arrayList_compare.size(); i++) {
            new_compare.add(i, "0");
        }
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public SingleProductModel getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.search_list_single_item, null);

            m = products.get(position);

            holder = new ViewHolder();

            holder.car_image = (ImageView) convertView.findViewById(R.id.car_image);
            holder.car_name = (TextView) convertView.findViewById(R.id.car_name);
            holder.car_rate = (TextView) convertView.findViewById(R.id.car_rate);
            holder.car_date = (TextView) convertView.findViewById(R.id.posted_day);
            holder.car_kms = (TextView) convertView.findViewById(R.id.car_details_kms);

            holder.like_text = (TextView) convertView.findViewById(R.id.like_text);
            holder.alert_text = (TextView) convertView.findViewById(R.id.alert_text);
            holder.compare_text = (TextView) convertView.findViewById(R.id.compare_text);

            holder.no_of_images = (TextView) convertView.findViewById(R.id.noofimages);
            holder.favoriteImg = (ImageView) convertView.findViewById(R.id.chola);
            holder.saved_car = (ImageView) convertView.findViewById(R.id.car_saved);
            holder.bid_image = (ImageView) convertView.findViewById(R.id.like);
            holder.alert_image = (ImageView) convertView.findViewById(R.id.car_alert);
            holder.compare = (ImageView) convertView.findViewById(R.id.car_compare);

            holder.like_layout = (LinearLayout) convertView.findViewById(R.id.like_layout);
            holder.compare_layout = (LinearLayout) convertView.findViewById(R.id.compare_layout);
            holder.alert_layout = (LinearLayout) convertView.findViewById(R.id.alert_layout);



            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        product = getItem(position);
        user = sessionManager.getUserDetails();
        Glide.with(getContext())
                .load(product.getImagelinks())
                .placeholder(R.drawable.carimageplaceholder)
                .into(holder.car_image);

        holder.car_name.setText(product.getModel() + " - " + product.getVariant());
        holder.car_rate.setText(product.getPrice() + " - " + product.getCar_locality());
        holder.car_date.setText(product.getDaysstmt());

        holder.car_kms.setText(product.getKilometer_run() + " Km" + " | " + product.getFuel_type() + " | " +
                product.getRegistration_year() + " | " + product.getOwner_type() + " Owners");

        holder.no_of_images.setText(product.getNo_images());

        Glide.with(getContext())
                .load(product.getSite_image())
                .into(holder.favoriteImg);

        String likearray = arrayListnew.get(position).toString();
        if (likearray.equals("0")) {
            Glide.with(getContext())
                    .load(R.drawable.like_white)
                    .into(holder.saved_car);
        } else {
            Glide.with(getContext())
                    .load(R.drawable.like_red)
                    .into(holder.saved_car);

        }

        //Alert ICons
        String alertarray = arrayList_alert.get(position).toString();
        if (alertarray.equals("0")) {
            Glide.with(getContext())
                    .load(R.drawable.alert_white)
                    .into(holder.alert_image);
        } else {
            Glide.with(getContext())
                    .load(R.drawable.alert_red)
                    .into(holder.alert_image);

        }

        String comparearray = new_compare.get(position).toString();
        if (comparearray.equals("0")) {
            Glide.with(getContext())
                    .load(R.drawable.compare_car_gray)
                    .into(holder.compare);

        } else {
            Glide.with(getContext())
                    .load(R.drawable.compare_blue)
                    .into(holder.compare);

        }

        if (product.getSite_image().isEmpty()) {
            Glide.with(getContext())
                    .load(R.drawable.carimageplaceholder)
                    .into(holder.favoriteImg);

        } else {
            Glide.with(getContext())
                    .load(product.getSite_image())
                    .into(holder.favoriteImg);

        }

       /* Animation animation = AnimationUtils.loadAnimation(getContext(), (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        convertView.startAnimation(animation);
        lastPosition = position;*/

        holder.like_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storyId = arrayListnew.get(position);
                pos = products.get(position).getCar_id();
                String name = products.get(position).getMake();
                Log.e("car_name", name);
                if (storyId.equals("0")) {
                    arrayListnew.set(position, "1");
                } else {
                    arrayListnew.set(position, "0");
                }
                Log.e("Saved_Cars", arrayListnew.get(position));

                new FavLikeButton().execute();

            }
        });

        holder.alert_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storyId_alert = arrayList_alert.get(position);
                pos_alert = products.get(position).getCar_id();
                String name = products.get(position).getMake();
                Log.e("car_name", name);
                if (storyId_alert.equals("0")) {
                    arrayList_alert.set(position, "1");
                } else {
                    arrayList_alert.set(position, "0");
                }
                Log.e("alert", arrayList_alert.get(position));

                new AlertButton().execute();

            }
        });

        holder.compare_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sum = 0;
                for (int i = 0; i < new_compare.size(); i++) {
                    sum = sum + Integer.parseInt(new_compare.get(i).toString());
                    Log.e("sum", new_compare.get(i));

                }
                storyId_compare = arrayList_compare.get(position);
                pos_compare = products.get(position).getCar_id();
                String name = products.get(position).getMake();
                Log.e("car_name", name);

                if (sum < 2 && new_compare.get(position).equals("0")) {
                    new_compare.set(position, "1");

                    arrayList_compare.set(position, pos_compare);
                    Log.e("New_Array", arrayList_compare.get(position));
                    holder.compare.setImageResource(R.drawable.compare_blue);
                    notifyDataSetChanged();
//                    Toast.makeText(context, "selected blue", Toast.LENGTH_SHORT).show();

                } else if (new_compare.get(position).equals("1")) {
                    new_compare.set(position, "0");
                    arrayList_compare.set(position, "1");
                    holder.compare.setImageResource(R.drawable.compare_car_gray);
                    notifyDataSetChanged();
//                    Toast.makeText(context, "selected gray", Toast.LENGTH_SHORT).show();

                } else {

                }

                sum = 0;
                for (int i = 0; i < new_compare.size(); i++) {
                    sum = sum + Integer.parseInt(new_compare.get(i).toString());
                    Log.e("sum", new_compare.get(i));

                }
                Log.e("sum", new_compare.toString());

                Log.e("Summmm", String.valueOf(sum));

                Log.e("newarraylist", arrayList_compare.toString());

                if (sum == 2) {
                    arrayList_compare.removeAll(Collections.singleton("1"));
                    Log.e("newarraylist", arrayList_compare.toString());
                    String url = ConstantIP.IP + "mobileweb/comparecar/www/index.html#/app/comparecar/"
                            + user.get("user_id") + "/"
                            + arrayList_compare.get(0) + "/"
                            + arrayList_compare.get(1);

                    Intent intent = new Intent(context, SellWebViewActivity.class);
                    intent.putExtra("title", "Compare Car");
                    intent.putExtra("url", url);
                    Log.e("url", url);
                    context.startActivity(intent);

                }

            }
        });


        return convertView;
    }


    class FavLikeButton extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            ServiceHandler sh = new ServiceHandler();

            user_id = user.get("user_id");
            String fav_url = Constant.SAVE_CAR_API
                    + "session_user_id=" + user_id
                    + "&carid=" + pos;

            Log.e("car_name", pos);

            String json = sh.makeServiceCall(fav_url, ServiceHandler.POST);

            if (json != null) {

                savecarList = new ArrayList<>();
                savemap = new HashMap<String, String>();

                try {
                    JSONObject obj = new JSONObject(json);

                    for (int i = 0; i <= obj.length(); i++) {

                        result = obj.getString("Result");
                        message = obj.getString("message");
                        savemap.put("REsult", result);
                        savemap.put("Message", message);
                        savecarList.add(savemap);

                    }
                } catch (final JSONException e) {
//                    Toast.makeText(getContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
//                Toast.makeText(getContext(), "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            notifyDataSetChanged();

//            Toast.makeText(getContext(), savemap.get("Message"), Toast.LENGTH_SHORT).show();
            Log.e("CarId", product.getCar_id());
        }
    }

    class AlertButton extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            ServiceHandler sh = new ServiceHandler();
            user_id = user.get("user_id");

            String fav_url = Constant.ADD_ALERT_API
                    + "session_user_id=" + user_id
                    + "&car_id=" + pos_alert
                    + "&page_name=alertcarpage";

            Log.e("fav_url", fav_url);

            String json = sh.makeServiceCall(fav_url, ServiceHandler.POST);

            if (json != null) {

                editcarList = new ArrayList<>();
                editmap = new HashMap<String, String>();

                try {
                    JSONObject obj = new JSONObject(json);

                    for (int i = 0; i <= obj.length(); i++) {

                        result = obj.getString("Result");
                        message = obj.getString("message");
                        editmap.put("REsult", result);
                        editmap.put("Message", message);
                        editcarList.add(editmap);

                    }
                } catch (final JSONException e) {
//                    Toast.makeText(getContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
//                Toast.makeText(getContext(), "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            notifyDataSetChanged();

//            Toast.makeText(getContext(), editmap.get("Message"), Toast.LENGTH_SHORT).show();
            Log.e("CarId", product.getCar_id());
        }
    }

    private class ViewHolder {
        ImageView car_image;
        TextView car_name;
        TextView car_rate, car_date;
        TextView car_kms, no_of_images;
        ImageView favoriteImg;
        ImageView saved_car;
        ImageView bid_image, alert_image;
        ImageView compare;
        LinearLayout like_layout, compare_layout, alert_layout;
        TextView like_text, alert_text, compare_text;
    }

}

