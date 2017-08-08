package com.falconnect.dealermanagementsystem.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.falconnect.dealermanagementsystem.BussinessProfileActivity;
import com.falconnect.dealermanagementsystem.ContactActivity;
import com.falconnect.dealermanagementsystem.ManageDashBoardActivity;
import com.falconnect.dealermanagementsystem.Model.ManageFooterDataModel;
import com.falconnect.dealermanagementsystem.MyBranchesActivity;
import com.falconnect.dealermanagementsystem.MyEmployeeActivity;
import com.falconnect.dealermanagementsystem.MyUserActivity;
import com.falconnect.dealermanagementsystem.R;
import com.falconnect.dealermanagementsystem.SharedPreference.ProfileManagerSession;
import com.falconnect.dealermanagementsystem.TranscationWebviewActivity;

import java.util.ArrayList;

public class ManageFooterCustomAdapter extends RecyclerView.Adapter<ManageFooterCustomAdapter.MyViewHolder> {

    private ArrayList<ManageFooterDataModel> managedataSet;
    private Context mContext;
    ProfileManagerSession profileManagerSession;

    public ManageFooterCustomAdapter(Context context, ArrayList<ManageFooterDataModel> managedata) {
        this.mContext = context;
        this.managedataSet = managedata;
        profileManagerSession = new ProfileManagerSession(context);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_footer_list_view_single, null);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;



    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        final ManageFooterDataModel singleItem_manage = managedataSet.get(listPosition);

        holder.text_ViewName.setText(singleItem_manage.getManagefootername());
        holder.image_ViewIcon.setImageResource(singleItem_manage.getManagefooterimage());
        holder.category_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                profileManagerSession.clear_ProfileManage();

                if (singleItem_manage.getManagefooterid_() == 0) {
                    //Current screen to next screen navigate
                    Intent intent = new Intent(mContext.getApplicationContext(), ManageDashBoardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    mContext.startActivity(intent);
                    //current screen finish
                    ((Activity) mContext).finish();
                    // Toast.makeText(mContext, "Selected :" + singleItem_manage.getManagefootername(), Toast.LENGTH_SHORT).show();
                } else if (singleItem_manage.getManagefooterid_() == 1) {
                    //Current screen to next screen navigate
                    Intent intent = new Intent(mContext.getApplicationContext(), BussinessProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    mContext.startActivity(intent);
                    //current screen finish
                    ((Activity) mContext).finish();
                    //Toast.makeText(mContext, "Selected :" + singleItem_manage.getManagefootername(), Toast.LENGTH_SHORT).show();
                } else if (singleItem_manage.getManagefooterid_() == 2) {
                    //Current screen to next screen navigate
                    Intent intent = new Intent(mContext.getApplicationContext(), MyBranchesActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    mContext.startActivity(intent);
                    //current screen finish
                    ((Activity) mContext).finish();
                    //  Toast.makeText(mContext, "Selected :" + singleItem_manage.getManagefootername(), Toast.LENGTH_SHORT).show();
                } else if (singleItem_manage.getManagefooterid_() == 3) {
                    //Current screen to next screen navigate
                    Intent intent = new Intent(mContext.getApplicationContext(), ContactActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    mContext.startActivity(intent);
                    //current screen finish
                    ((Activity) mContext).finish();
                    //   Toast.makeText(mContext, "Selected :" + singleItem_manage.getManagefootername(), Toast.LENGTH_SHORT).show();
                } else if (singleItem_manage.getManagefooterid_() == 4) {
                    //Current screen to next screen navigate
                    Intent intent = new Intent(mContext.getApplicationContext(), MyEmployeeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    mContext.startActivity(intent);
                    //current screen finish
                    ((Activity) mContext).finish();
                    //  Toast.makeText(mContext, "Selected :" + singleItem_manage.getManagefootername(), Toast.LENGTH_SHORT).show();
                } else if (singleItem_manage.getManagefooterid_() == 5) {
                    Intent intent = new Intent(mContext.getApplicationContext(), MyUserActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    mContext.startActivity(intent);
                    //current screen finish
                    ((Activity) mContext).finish();
//                    Toast.makeText(mContext, "Selected :" + singleItem_manage.getManagefootername(), Toast.LENGTH_SHORT).show();
                } else if (singleItem_manage.getManagefooterid_() == 6) {
                    //Current screen to next screen navigate
                    Intent intent = new Intent(mContext.getApplicationContext(), TranscationWebviewActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    mContext.startActivity(intent);
                    //current screen finish
                    ((Activity) mContext).finish();
                   // Toast.makeText(mContext, "Selected :" + singleItem_manage.getManagefootername(), Toast.LENGTH_SHORT).show();
                } else {

                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return (null != managedataSet ? managedataSet.size() : 0);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView text_ViewName;
        ImageView image_ViewIcon;
        LinearLayout category_item;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.text_ViewName = (TextView) itemView.findViewById(R.id.mtitle);
            this.image_ViewIcon = (ImageView) itemView.findViewById(R.id.image_footer);
            this.category_item = (LinearLayout) itemView.findViewById(R.id.category_item);

        }
    }


}