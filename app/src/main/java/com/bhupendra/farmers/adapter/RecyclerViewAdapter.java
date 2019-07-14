package com.bhupendra.farmers.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bhupendra.farmers.R;
import com.bhupendra.farmers.listViewData;

import java.util.ArrayList;



public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    Context context;
    ArrayList<listViewData> list;

    public RecyclerViewAdapter(Context context, ArrayList<listViewData> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.listview_layout, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        listViewData data = list.get(position);

        holder.executive_email.setText(data.executive_email);
        holder.farmer_name.setText(data.farmer_name);
        holder.issue.setText(data.issue);
        holder.farmer_contact_no.setText(data.farmer_contact_no);
        holder.location.setText(data.location);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView executive_email, farmer_name, farmer_contact_no, issue,location;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            executive_email = itemView.findViewById(R.id.email);
            farmer_contact_no = itemView.findViewById(R.id.phone);
            farmer_name = itemView.findViewById(R.id.name);
            issue = itemView.findViewById(R.id.msg);
            location = itemView.findViewById(R.id.location);
        }
    }
}
