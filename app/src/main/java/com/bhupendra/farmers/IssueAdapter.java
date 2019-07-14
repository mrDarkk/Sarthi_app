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

public class IssueAdapter extends RecyclerView.Adapter<IssueAdapter.IssueViewHolder> implements Filterable
{
    private static final int TYPE_ROW = 0;
    private static final int TYPE_ROW_COLORFUL = 1;

    private List<Issue> issueList;
    private List<Issue> filteredIssueList;
    private Context context;

    public IssueAdapter(Context context, List<Issue> issueList)
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
    public IssueViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        if (viewType == TYPE_ROW)
        {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_issue, viewGroup, false);
            return new IssueViewHolder(view);
        } else
        {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_issue_colorful,
                    viewGroup, false);
            return new IssueViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(IssueViewHolder holder, int position)
    {
        Issue issue = filteredIssueList.get(position);

        holder.txtemail.setText(issue.ex_mail);
        holder.txtfName.setText(issue.f_name);
        holder.txtfpno.setText(issue.f_phone);
        holder.txtftype.setText(issue.f_type);
        holder.txttopic.setText(issue.f_topic);
        holder.txtissue.setText(issue.f_issue);
        holder.txtnoofmember.setText(issue.f_no_of_member);
        holder.txtactivity.setText(issue.f_activity);
        holder.txtadress.setText(issue.f_address);

        Glide.with(context).load(issue.f_imgUrl).into(holder.imgLogo);
    }

    @Override
    public int getItemCount()
    {
        return filteredIssueList.size();
    }

    public class IssueViewHolder extends RecyclerView.ViewHolder
    {
        public TextView txtemail , txtfName, txtfpno, txtftype, txttopic, txtissue, txtnoofmember, txtactivity,txtadress;
        public ImageView imgLogo;

        public IssueViewHolder(View view)
        {
            super(view);
            txtemail = view.findViewById(R.id.txtex_email);
            txtfName = view.findViewById(R.id.txtfarmer_name);
            txtfpno = view.findViewById(R.id.txtfarmer_phone);
            txtftype = view.findViewById(R.id.txtfarmer_type);
            txttopic = view.findViewById(R.id.txtTopic);
            txtissue = view.findViewById(R.id.txtissue);
            txtnoofmember = view.findViewById(R.id.txtnoof_member);
            txtactivity = view.findViewById(R.id.txtactivity);
            txtadress = view.findViewById(R.id.txtaddress);

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
                    List<Issue> filteredList = new ArrayList<>();
                    for (Issue issue : issueList)
                    {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name
                        if (issue.ex_mail.toLowerCase().contains(charString.toLowerCase()) )
                        {
                            filteredList.add(issue);
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
                filteredIssueList = (ArrayList<Issue>) filterResults.values;

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }
}