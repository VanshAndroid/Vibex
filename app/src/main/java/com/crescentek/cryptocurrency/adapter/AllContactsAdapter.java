package com.crescentek.cryptocurrency.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.activity.send.SendBitcoin_1;
import com.crescentek.cryptocurrency.model.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by R.Android on 05-09-2018.
 */

public class AllContactsAdapter extends RecyclerView.Adapter<AllContactsAdapter.ContactViewHolder> implements Filterable {


    private List<Contact> contactFilterList;
    private List<Contact> contactVOList;
    private Context mContext;

    SendBitcoin_1 sendBitcoin_1;

    public AllContactsAdapter(List<Contact> contactVOList, Context mContext, SendBitcoin_1 sendBitcoin_1){
        this.contactVOList = contactVOList;
        contactFilterList = contactVOList;
        this.mContext = mContext;
        this.sendBitcoin_1 = sendBitcoin_1;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_contact_view, null);
        ContactViewHolder contactViewHolder = new ContactViewHolder(view);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        final Contact contactVO = contactVOList.get(position);
        holder.tvContactName.setText(contactVO.getContactName());
        holder.tvPhoneNumber.setText(contactVO.getContactNumber());
        holder.tv_contact_start.setText(""+contactVO.getContactName().toUpperCase().charAt(0));

        holder.tvContactName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendBitcoin_1.callcheckUserApiMethod(contactVO);
            }
        });

    }

    @Override
    public int getItemCount() {
        return contactVOList.size();
    }


    public static class ContactViewHolder extends RecyclerView.ViewHolder{

        TextView tv_contact_start;
        TextView tvContactName;
        TextView tvPhoneNumber;

        public ContactViewHolder(View itemView) {
            super(itemView);
            tv_contact_start = (TextView) itemView.findViewById(R.id.tv_contact_start);
            tvContactName = (TextView) itemView.findViewById(R.id.tvContactName);
            tvPhoneNumber = (TextView) itemView.findViewById(R.id.tvPhoneNumber);
        }
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    contactVOList = contactFilterList;
                } else {

                    ArrayList<Contact> filteredList = new ArrayList<>();

                    for (Contact data : contactFilterList) {

                        if (data.getContactName().toLowerCase().contains(charString) || data.getContactNumber().toLowerCase().contains(charString)) {

                            filteredList.add(data);
                        }
                    }

                    contactVOList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactVOList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactVOList = (ArrayList<Contact>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


}
