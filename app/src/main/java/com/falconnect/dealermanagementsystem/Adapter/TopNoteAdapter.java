package com.falconnect.dealermanagementsystem.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.falconnect.dealermanagementsystem.R;
import com.falconnect.dealermanagementsystem.SearchResultActivity;

import java.util.ArrayList;

public class TopNoteAdapter extends RecyclerView.Adapter<TopNoteAdapter.ViewHolder> {

    ArrayList<String> SubjectValues;
    Context context;
    View view1;
    ViewHolder viewHolder1;



    public TopNoteAdapter(Context context1, ArrayList<String> SubjectValues1) {
        SubjectValues = SubjectValues1;
        context = context1;
    }

    @Override
    public TopNoteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view1 = LayoutInflater.from(context).inflate(R.layout.top_note_tag, parent, false);
        viewHolder1 = new ViewHolder(view1);

       /* View bookRow = LayoutInflater.from(context).inflate(R.layout.activity_search_result, parent, false);
        viewHolder1 = new ViewHolder(bookRow);*/

        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.textView.setText(SubjectValues.get(position));

        holder.top_note_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String itemLabel = SubjectValues.get(position);

                SubjectValues.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, SubjectValues.size());
//                Toast.makeText(context, "Removed : " + itemLabel, Toast.LENGTH_SHORT).show();

                Log.e("SubjectValues", SubjectValues.toString());

                ((SearchResultActivity) context).listview(SubjectValues);


            }
        });
    }

    @Override
    public int getItemCount() {
        return SubjectValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout top_note_layout;
        public TextView textView;


        public ViewHolder(View v) {
            super(v);
            top_note_layout = (RelativeLayout) v.findViewById(R.id.top_note_layout);
            textView = (TextView) v.findViewById(R.id.tag_name);

            //listView = (ListView) v.findViewById(R.id.list_view);
        }
    }


}

