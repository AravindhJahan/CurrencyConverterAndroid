package com.falconnect.dealermanagementsystem.Adapter;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.falconnect.dealermanagementsystem.Constant;
import com.falconnect.dealermanagementsystem.ConstantIP;
import com.falconnect.dealermanagementsystem.FontAdapter.RoundImageTransform;
import com.falconnect.dealermanagementsystem.Model.MyInventoryModel;
import com.falconnect.dealermanagementsystem.R;
import com.falconnect.dealermanagementsystem.SellDashBoardActivity;
import com.falconnect.dealermanagementsystem.SellWebViewActivity;
import com.falconnect.dealermanagementsystem.ServiceHandler;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyInventoryListAdapter extends ArrayAdapter<MyInventoryModel> {

    public ArrayList<HashMap<String, String>> savecarList;
    List<MyInventoryModel> userModels;
    ViewHolder holder;
    SessionManager sessionManager = new SessionManager(getContext());
    HashMap<String, String> user;
    String pos;
    String result, message;
    String status;
    HashMap<String, String> savemap;
    MyInventoryModel myInventoryModel;
    ProgressDialog barProgressDialog;
    int list;
    private Context context;

    public MyInventoryListAdapter(Context context, List<MyInventoryModel> userModels, int list) {
        super(context, R.layout.fragment_first_page_sell, userModels);
        this.context = context;
        this.userModels = userModels;
        this.list = list;
    }

    @Override
    public int getCount() {
        return userModels.size();
    }

    @Override
    public MyInventoryModel getItem(int position) {
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
            convertView = inflater.inflate(R.layout.fragment_first_page_sell, null);

            holder = new ViewHolder();

            holder.car_image = (ImageView) convertView.findViewById(R.id.car_image_myinventry);

            holder.post_image = (ImageView) convertView.findViewById(R.id.post_image);
            holder.edit_image = (ImageView) convertView.findViewById(R.id.edit_image);
            holder.preview_image = (ImageView) convertView.findViewById(R.id.preview_Image);
            holder.market_image = (ImageView) convertView.findViewById(R.id.market_Image);

            holder.car_name = (TextView) convertView.findViewById(R.id.myinventry_car_name);
            holder.price = (TextView) convertView.findViewById(R.id.myinventry_car_rate);
            holder.car_details = (TextView) convertView.findViewById(R.id.myinventory_kms);
            holder.image_count = (TextView) convertView.findViewById(R.id.photo_count);
            holder.document_count = (TextView) convertView.findViewById(R.id.document_count);
            holder.video_count = (TextView) convertView.findViewById(R.id.video_count);
            holder.viewcount = (TextView) convertView.findViewById(R.id.view_count);

            holder.draft = (Button) convertView.findViewById(R.id.draft);
            holder.for_sale = (Button) convertView.findViewById(R.id.for_sale);
            holder.live = (Button) convertView.findViewById(R.id.live);
            holder.sold = (Button) convertView.findViewById(R.id.sold);
            holder.delete = (Button) convertView.findViewById(R.id.delete);

            holder.sold_full = (Button) convertView.findViewById(R.id.sold_full);
            holder.delete_full = (Button) convertView.findViewById(R.id.delete_full);

            holder.buttons = (LinearLayout) convertView.findViewById(R.id.button);

            holder.post = (LinearLayout) convertView.findViewById(R.id.post);
            holder.edit = (LinearLayout) convertView.findViewById(R.id.edit);
            holder.preview = (LinearLayout) convertView.findViewById(R.id.preview);
            holder.market = (LinearLayout) convertView.findViewById(R.id.market);

            holder.post_text = (TextView) convertView.findViewById(R.id.post_text);
            holder.edit_text = (TextView) convertView.findViewById(R.id.edit_text);
            holder.preview_text = (TextView) convertView.findViewById(R.id.preview_text);
            holder.market_text = (TextView) convertView.findViewById(R.id.market_text);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        myInventoryModel = getItem(position);
        user = sessionManager.getUserDetails();

        Glide.with(context)
                .load(myInventoryModel.getImage())
                .transform(new RoundImageTransform(getContext()))
                .placeholder(R.drawable.carimageplaceholder)
                .into(holder.car_image);


        holder.car_name.setText(myInventoryModel.getModel() + " " + myInventoryModel.getVarient());

        holder.price.setText(myInventoryModel.getPrice() + " - " + myInventoryModel.getCarstatus());

        holder.car_details.setText(myInventoryModel.getKms_done() + " KM" + " | " + myInventoryModel.getMillege() + " KMPL" + " | " + myInventoryModel.getFuel_type());

        holder.image_count.setText(myInventoryModel.getImagecount());
        holder.document_count.setText(myInventoryModel.getDocumentcount());
        holder.video_count.setText(myInventoryModel.getVideoscount());
        holder.video_count.setText(myInventoryModel.getViewscount());

        holder.preview.setEnabled(false);
        holder.post.setEnabled(false);
        holder.edit.setEnabled(false);
        holder.market.setEnabled(false);


        if (myInventoryModel.getCarstatus().equals("Draft")) {
            holder.delete_full.setVisibility(View.GONE);
            holder.sold_full.setVisibility(View.GONE);
            holder.buttons.setVisibility(View.VISIBLE);

            int background = Color.parseColor("#173E84");
            holder.draft.setBackgroundColor(background);
            holder.draft.setTextColor(Color.WHITE);

            holder.sold.setEnabled(false);
            holder.live.setEnabled(false);

            holder.sold.setTextColor(Color.GRAY);
            holder.live.setTextColor(Color.GRAY);


            holder.for_sale.setBackgroundResource(R.drawable.buttons_chage);
            holder.for_sale.setTextColor(background);
            Glide.with(getContext())
                    .load(R.drawable.sharegray)
                    .into(holder.post_image);

            holder.post_text.setTextColor(Color.GRAY);
            holder.preview_text.setTextColor(Color.GRAY);

            Glide.with(getContext())
                    .load(R.drawable.editblue)
                    .into(holder.edit_image);
            holder.edit.setEnabled(true);

            holder.edit_text.setTextColor(Color.parseColor("#173E84"));

            Glide.with(getContext())
                    .load(R.drawable.previewgray)
                    .into(holder.preview_image);

            holder.market_text.setTextColor(Color.parseColor("#C0C0C0"));

            Glide.with(getContext())
                    .load(R.drawable.marketgray)
                    .into(holder.market_image);


            holder.live.setBackgroundResource(R.drawable.buttons_chage);

        } else if (myInventoryModel.getCarstatus().equals("Live")) {
            holder.delete_full.setVisibility(View.GONE);
            holder.sold_full.setVisibility(View.GONE);
            holder.buttons.setVisibility(View.VISIBLE);

            int background = Color.parseColor("#173E84");
            holder.live.setBackgroundColor(background);
            holder.live.setTextColor(Color.WHITE);
            holder.delete.setTextColor(background);
            holder.sold.setTextColor(background);

            holder.for_sale.setEnabled(false);
            holder.draft.setEnabled(false);

            holder.draft.setBackgroundResource(R.drawable.buttons_chage);
            holder.for_sale.setBackgroundResource(R.drawable.buttons_chage);
            holder.draft.setTextColor(Color.GRAY);
            holder.for_sale.setTextColor(Color.GRAY);

            holder.post_text.setTextColor(Color.parseColor("#173E84"));
            holder.preview_text.setTextColor(Color.parseColor("#173E84"));
            holder.market_text.setTextColor(Color.parseColor("#173E84"));

            Glide.with(getContext())
                    .load(R.drawable.shareblue)
                    .into(holder.post_image);

            Glide.with(getContext())
                    .load(R.drawable.marketblue)
                    .into(holder.market_image);

            Glide.with(getContext())
                    .load(R.drawable.editgray)
                    .into(holder.edit_image);
            holder.preview.setEnabled(true);
            holder.post.setEnabled(true);
            holder.market.setEnabled(true);

            holder.edit_text.setTextColor(Color.GRAY);

            Glide.with(getContext())
                    .load(R.drawable.previewblue)
                    .into(holder.preview_image);


        } else if (myInventoryModel.getCarstatus().equals("Ready for Sale")) {
            holder.delete_full.setVisibility(View.GONE);
            holder.sold_full.setVisibility(View.GONE);
            holder.buttons.setVisibility(View.VISIBLE);

            int background = Color.parseColor("#173E84");
            holder.for_sale.setBackgroundColor(background);
            holder.for_sale.setTextColor(Color.WHITE);
            holder.delete.setTextColor(background);
            holder.sold.setTextColor(background);

            holder.draft.setBackgroundResource(R.drawable.buttons_chage);
            holder.live.setBackgroundResource(R.drawable.buttons_chage);
            holder.draft.setTextColor(Color.GRAY);
            holder.live.setTextColor(Color.GRAY);

            holder.live.setEnabled(false);
            holder.draft.setEnabled(false);

            holder.market_text.setTextColor(Color.parseColor("#C0C0C0"));

            Glide.with(getContext())
                    .load(R.drawable.marketgray)
                    .into(holder.market_image);


            holder.preview.setEnabled(true);
            holder.post.setEnabled(true);

            holder.post_text.setTextColor(Color.parseColor("#173E84"));
            holder.preview_text.setTextColor(Color.parseColor("#173E84"));

            Glide.with(getContext())
                    .load(R.drawable.shareblue)
                    .into(holder.post_image);

            Glide.with(getContext())
                    .load(R.drawable.editgray)
                    .into(holder.edit_image);
            holder.edit_text.setTextColor(Color.GRAY);

            Glide.with(getContext())
                    .load(R.drawable.previewblue)
                    .into(holder.preview_image);


        }else if (myInventoryModel.getCarstatus().equals("Sold")) {

            holder.sold_full.setVisibility(View.VISIBLE);
            holder.delete_full.setVisibility(View.GONE);
            holder.buttons.setVisibility(View.GONE);

            Glide.with(getContext())
                    .load(R.drawable.editgray)
                    .into(holder.edit_image);

            holder.market_text.setTextColor(Color.parseColor("#C0C0C0"));

            Glide.with(getContext())
                    .load(R.drawable.marketgray)
                    .into(holder.market_image);


            Glide.with(getContext())
                    .load(R.drawable.sharegray)
                    .into(holder.post_image);

            Glide.with(getContext())
                    .load(R.drawable.previewgray)
                    .into(holder.preview_image);

            holder.edit.setEnabled(false);
            holder.preview.setEnabled(false);
            holder.post.setEnabled(false);

            holder.edit_text.setTextColor(Color.GRAY);
            holder.post_text.setTextColor(Color.GRAY);
            holder.preview_text.setTextColor(Color.GRAY);

        }else if (myInventoryModel.getCarstatus().equals("Deleted")) {
            holder.delete_full.setVisibility(View.VISIBLE);
            holder.sold_full.setVisibility(View.GONE);
            holder.buttons.setVisibility(View.GONE);

            Glide.with(getContext())
                    .load(R.drawable.editgray)
                    .into(holder.edit_image);

            Glide.with(getContext())
                    .load(R.drawable.sharegray)
                    .into(holder.post_image);

            holder.market_text.setTextColor(Color.parseColor("#C0C0C0"));

            Glide.with(getContext())
                    .load(R.drawable.marketgray)
                    .into(holder.market_image);


            Glide.with(getContext())
                    .load(R.drawable.previewgray)
                    .into(holder.preview_image);

            holder.edit_text.setTextColor(Color.GRAY);
            holder.post_text.setTextColor(Color.GRAY);
            holder.preview_text.setTextColor(Color.GRAY);

            holder.edit.setEnabled(false);
            holder.preview.setEnabled(false);
            holder.post.setEnabled(false);
        }else {

        }

        holder.sold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pos = userModels.get(position).getCar_id();

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogNewStyle);
                builder1.setMessage("Are you sure want to Sold this car?");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        status = "3";
                        new car_sold().execute();
                    }
                });
                builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pos = userModels.get(position).getCar_id();

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogNewStyle);
                builder1.setMessage("Are you sure want to Delete this car?");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                status = "4";
                                new car_delete().execute();
                            }
                        });
                builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });

        holder.for_sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pos = userModels.get(position).getCar_id();

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogNewStyle);
                builder1.setMessage("Are you sure want to Ready for Sale this car?");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        status = "1";
                        new car_ready_sale().execute();
                    }
                });
                builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });

        holder.preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = ConstantIP.IP + "mobileweb/carview/index.html#/carview/" + user.get("user_id") + "/" + userModels.get(position).getCar_id() + "/0";
                Intent intent = new Intent(getContext(), SellWebViewActivity.class);
                intent.putExtra("url", url);
                context.startActivity(intent);
                Log.e("url", url);
            }
        });

        holder.market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = ConstantIP.IP + "mobileweb/inventorynew/index.html#/market/" + user.get("user_id") + "/" + userModels.get(position).getCar_id() ;
                Intent intent = new Intent(getContext(), SellWebViewActivity.class);
                intent.putExtra("url", url);
                context.startActivity(intent);
                Log.e("url", url);
            }
        });

        holder.post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = ConstantIP.IP + "mobileweb/inventorynew/index.html#/onlinePortal/" + user.get("user_id") + "/" + myInventoryModel.getCar_id() + "/0";
                Intent intent = new Intent(getContext(), SellWebViewActivity.class);
                intent.putExtra("url", url);
                context.startActivity(intent);
                // Toast.makeText(getContext(), "Selected Car Name :" + myInventoryModel.getMake(), Toast.LENGTH_SHORT).show();

            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = ConstantIP.IP + "mobileweb/inventorynew/index.html#/EditBasicInfo/" + user.get("user_id") + "/" + myInventoryModel.getCar_id();
                Intent intent = new Intent(getContext(), SellWebViewActivity.class);
                intent.putExtra("url", url);
                context.startActivity(intent);
                // Toast.makeText(getContext(), "Selected Car Name :" + myInventoryModel.getMake(), Toast.LENGTH_SHORT).show();

            }
        });

        return convertView;
    }

    class car_delete extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = ProgressDialog.show(getContext(), "Loading...", "Please Wait ...", true);
        }

        @Override
        protected String doInBackground(String... params) {

            ServiceHandler sh = new ServiceHandler();
            String fav_url = Constant.MY_INVENTORY_DELETE
                    + "session_user_id=" + user.get("user_id")
                    + "&car_id=" + pos
                    + "&status=" + status
                    + "&inventory_type_id=" + list
                    +"&page_name=deleteinventory";

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
                    //    Toast.makeText(getContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                // Toast.makeText(getContext(), "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            barProgressDialog.dismiss();

            ((SellDashBoardActivity) context).listview();

            // Toast.makeText(getContext(), savemap.get("Message"), Toast.LENGTH_SHORT).show();

        }
    }

    class car_sold extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = ProgressDialog.show(getContext(), "Loading...", "Please Wait ...", true);
        }

        @Override
        protected String doInBackground(String... params) {

            ServiceHandler sh = new ServiceHandler();
            String fav_url = Constant.MY_INVENTORY_DELETE
                    + "session_user_id=" + user.get("user_id")
                    + "&car_id=" + pos
                    + "&status=" + status
                    + "&inventory_type_id=" + list
                    + "&page_name=deleteinventory";

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
                    // Toast.makeText(getContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                // Toast.makeText(getContext(), "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            barProgressDialog.dismiss();

            ((SellDashBoardActivity) context).listview();

            // Toast.makeText(getContext(), savemap.get("Message"), Toast.LENGTH_SHORT).show();

        }
    }

    class car_ready_sale extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = ProgressDialog.show(getContext(), "Loading...", "Please Wait ...", true);
        }

        @Override
        protected String doInBackground(String... params) {

            ServiceHandler sh = new ServiceHandler();
            String fav_url = Constant.MY_INVENTORY_DELETE
                    + "session_user_id=" + user.get("user_id")
                    + "&car_id=" + pos
                    + "&status=" + status
                    + "&inventory_type_id=" + list
                    + "&page_name=deleteinventory";

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
                    //   Toast.makeText(getContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                // Toast.makeText(getContext(), "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            barProgressDialog.dismiss();

            ((SellDashBoardActivity) context).listview();

            // Toast.makeText(getContext(), savemap.get("Message"), Toast.LENGTH_SHORT).show();

        }
    }

    private class ViewHolder {

        ImageView post_image, edit_image, preview_image, market_image;
        LinearLayout post, edit, preview, market;
        LinearLayout buttons;
        Button sold_full, delete_full;
        Button draft, for_sale, live, sold, delete;
        ImageView car_image;
        TextView post_text, edit_text, preview_text, market_text;
        TextView car_name, price, car_details, image_count, document_count, video_count, viewcount;

    }

}