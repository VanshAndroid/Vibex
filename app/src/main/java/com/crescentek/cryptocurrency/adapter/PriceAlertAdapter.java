package com.crescentek.cryptocurrency.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.model.PriceAlertModel;

import java.util.ArrayList;

/**
 * Created by R.Android on 03-10-2018.
 */

public class PriceAlertAdapter extends RecyclerView.Adapter<PriceAlertAdapter.MyViewHolder> {

    ArrayList<PriceAlertModel> priceAlertArrayList;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_price_alert,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        PriceAlertModel priceAlert=priceAlertArrayList.get(position);
        holder.crypto_tv.setText(priceAlert.getCurrency_code()+" "+priceAlert.getAmount());
        if(priceAlert.getAmount_less().trim().equalsIgnoreCase("1"))
        {
            holder.info_tv.setText("Less than - "+priceAlert.getAlert_type());
            holder.up_down_iv.setImageResource(R.drawable.down_arrow);
        }
        else {
            holder.info_tv.setText("More than - "+priceAlert.getAlert_type());
            holder.up_down_iv.setImageResource(R.drawable.up_arrow);
        }


    }


    public PriceAlertAdapter(ArrayList<PriceAlertModel> priceAlertArrayList) {
        this.priceAlertArrayList =priceAlertArrayList;
    }

    @Override
    public int getItemCount() {

        //Log.d("SellSize>>", buysellExchangeList.size()+"");
        return priceAlertArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView crypto_tv, info_tv;
        private ImageView up_down_iv;

        public MyViewHolder(View view) {
            super(view);
            up_down_iv = (ImageView) view.findViewById(R.id.up_down_iv);
            crypto_tv = (TextView) view.findViewById(R.id.crypto_tv);
            info_tv = (TextView) view.findViewById(R.id.info_tv);
        }
    }

}