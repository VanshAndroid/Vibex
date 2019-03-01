package com.crescentek.cryptocurrency.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.model.Trades;

import java.util.List;

/**
 * Created by R.Android on 04-07-2018.
 */

public class TrandsAdapter extends RecyclerView.Adapter<TrandsAdapter.MyViewHolder> {

    private List<Trades> tradesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_time, tv_price, tv_amt;

        public MyViewHolder(View view) {
            super(view);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_price = (TextView) view.findViewById(R.id.tv_price);
            tv_amt = (TextView) view.findViewById(R.id.tv_amt);
        }
    }

    public TrandsAdapter(List<Trades> tradesList)
    {
        this.tradesList = tradesList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_trands,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Trades tradesData = tradesList.get(position);
        holder.tv_amt.setText(tradesData.getAmount());
        holder.tv_price.setText(tradesData.getPrice());
        holder.tv_time.setText(tradesData.getTime());

    }

    @Override
    public int getItemCount() {
        Log.d("TradeListSize>>>",tradesList.size()+"");
        return tradesList.size();
    }


}
