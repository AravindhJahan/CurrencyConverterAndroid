package com.falconnect.dealermanagementsystem.Adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.falconnect.dealermanagementsystem.FontAdapter.RoundImageTransform;
import com.falconnect.dealermanagementsystem.Model.ContactModel;
import com.falconnect.dealermanagementsystem.R;

import java.util.List;

public class ContactListAdapter extends ArrayAdapter<ContactModel> {

    List<ContactModel> contactModels;
    private Context context;
    ViewHolder holder;

    public ContactListAdapter(Context context, List<ContactModel> contactModels) {
        super(context, R.layout.contact_listview_single_item, contactModels);
        this.context = context;
        this.contactModels = contactModels;
    }

    @Override
    public int getCount() {
        return contactModels.size();
    }

    @Override
    public ContactModel getItem(int position) {
        return contactModels.get(position);
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
            convertView = inflater.inflate(R.layout.contact_listview_single_item, null);

            holder = new ViewHolder();

            holder.name = (TextView) convertView.findViewById(R.id.contact_name);
            holder.email = (TextView) convertView.findViewById(R.id.contact_email);
            holder.mobilenum = (TextView) convertView.findViewById(R.id.contact_mobilenum);
            holder.address = (TextView) convertView.findViewById(R.id.contact_address);

            holder.contact_image = (ImageView) convertView.findViewById(R.id.contact_image);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ContactModel contactModel = getItem(position);

        holder.name.setText(contactModel.getName());
        holder.email.setText(contactModel.getEmail());
        holder.mobilenum.setText(contactModel.getMobilenum());
        holder.address.setText(contactModel.getAddress());

        Glide.with(context)
                .load(contactModel.getContactimage())
                .transform(new RoundImageTransform(getContext()))
                .placeholder(R.drawable.carimageplaceholder)
                .into(holder.contact_image);


        return convertView;
    }

    private class ViewHolder {
        ImageView contact_image;
        TextView name, email, mobilenum, address;

    }

}