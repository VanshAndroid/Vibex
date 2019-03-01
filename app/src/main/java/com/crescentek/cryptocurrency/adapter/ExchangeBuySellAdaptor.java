package com.crescentek.cryptocurrency.adapter;


import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.model.BuySell;

import java.util.ArrayList;

public class ExchangeBuySellAdaptor extends RecyclerView.Adapter<ExchangeBuySellAdaptor.MyViewHolder> {

    ArrayList<BuySell> buysellExchangeList;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_buy_exchange,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        BuySell buySell= buysellExchangeList.get(position);
        //Log.d("type>>>",""+buySell.getType());
        if(buySell.getType().equalsIgnoreCase("buy"))
        {
          holder.linear_bg.setBackgroundColor(Color.parseColor("#FAE6E5"));
        }
        else {
            holder.linear_bg.setBackgroundColor(Color.parseColor("#ECF6ED"));
        }
        holder.tv_currency.setText(buySell.getCrypto_code());
        holder.tv_amt.setText(buySell.getCrypto_val());

        holder.tv_price.setText(buySell.getCryptoRate());
        holder.tv_fill.setText(buySell.getCrypto_closing_traded());

    }


    public ExchangeBuySellAdaptor(ArrayList<BuySell> buyExchangeList) {
        this.buysellExchangeList =buyExchangeList;
    }

    @Override
    public int getItemCount() {

        //Log.d("SellSize>>", buysellExchangeList.size()+"");
        return buysellExchangeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_currency, tv_amt,tv_price,tv_fill;
        private LinearLayout linear_bg;

        public MyViewHolder(View view) {
            super(view);
            tv_currency = (TextView) view.findViewById(R.id.tv_currency);
            tv_amt = (TextView) view.findViewById(R.id.tv_amt);
            tv_price = (TextView) view.findViewById(R.id.tv_price);
            tv_fill = (TextView) view.findViewById(R.id.tv_fill);
            linear_bg=(LinearLayout) view.findViewById(R.id.linear_bg);
        }
    }

}