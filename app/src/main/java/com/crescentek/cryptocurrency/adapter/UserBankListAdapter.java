package com.crescentek.cryptocurrency.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.model.BankList;
import com.crescentek.cryptocurrency.model.Trades;

import java.util.List;

/**
 * Created by R.Android on 16-10-2018.
 */

public class UserBankListAdapter extends RecyclerView.Adapter<UserBankListAdapter.MyViewHolder> {

    private List<BankList> bankLists;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView bank_tv;

        public MyViewHolder(View view) {
            super(view);
            bank_tv = (TextView) view.findViewById(R.id.bank_tv);

        }
    }

    public UserBankListAdapter(List<BankList> bankLists)
    {
        this.bankLists = bankLists;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_bank_user,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        BankList bList=bankLists.get(position);
        holder.bank_tv.setText(bList.getBank_name());


    }

    @Override
    public int getItemCount() {
        Log.d("TradeListSize>>>",bankLists.size()+"");
        return bankLists.size();
    }


}
