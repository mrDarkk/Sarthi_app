package com.bhupendra.farmers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ExecutiveAdapter extends RecyclerView.Adapter<ExecutiveAdapter.ExecutiveViewHolder> implements Filterable
{
    private static final int TYPE_ROW = 0;
    private static final int TYPE_ROW_COLORFUL = 1;

    private List<Executive> issueList;
    private List<Executive> filteredIssueList;
    private Context context;

    public ExecutiveAdapter(Context context, List<Executive> issueList)
    {
        this.context = context;
        this.issueList = issueList;
        this.filteredIssueList = issueList;
    }

    @Override
    public int getItemViewType(int position)
    {
        if (position % 2 == 0)
        {
            return TYPE_ROW_COLORFUL;
        }

        return TYPE_ROW;
    }

    @Override
    public ExecutiveViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        if (viewType == TYPE_ROW)
        {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_executive, viewGroup, false);
            return new ExecutiveViewHolder(view);
        } else
        {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_executive_colorful,
                    viewGroup, false);
            return new ExecutiveViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(ExecutiveViewHolder holder, int position)
    {
        Executive executive = filteredIssueList.get(position);

        holder.txtemail.setText(executive.e_email);
        holder.txteName.setText(executive.e_name);
        holder.txtepno.setText(executive.e_phone);
        holder.txteallocatedarea.setText(executive.e_allocated_area);
        holder.txteadress.setText(executive.e_address);


        Glide.with(context).load(executive.e_imgUrl).into(holder.imgLogo);
    }

    @Override
    public int getItemCount()
    {
        return filteredIssueList.size();
    }

    public class ExecutiveViewHolder extends RecyclerView.ViewHolder
    {
        public TextView txtemail , txteName, txtepno, txteallocatedarea,txteadress;
        public ImageView imgLogo;

        public ExecutiveViewHolder(View view)
        {
            super(view);
            txtemail = view.findViewById(R.id.txtex_email);
            txteName = view.findViewById(R.id.txtex_name);
            txtepno = view.findViewById(R.id.txtex_phone);
            txteallocatedarea = view.findViewById(R.id.txtex_allocated_area);
            txteadress = view.findViewById(R.id.txtex_address);


            imgLogo = view.findViewById(R.id.imgLogo);
        }
    }

    @Override
    public Filter getFilter()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence)
            {
                String charString = charSequence.toString();
                if (charString.isEmpty())
                {
                    filteredIssueList = issueList;
                } else
                {
                    List<Executive> filteredList = new ArrayList<>();
                    for (Executive executive : issueList)
                    {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name
                        if (executive.e_name.toLowerCase().contains(charString.toLowerCase()) )
                        {
                            filteredList.add(executive);
                        }
                    }

                    filteredIssueList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredIssueList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults)
            {
                filteredIssueList = (ArrayList<Executive>) filterResults.values;

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }
}