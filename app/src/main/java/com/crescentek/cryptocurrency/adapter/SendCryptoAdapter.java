package com.crescentek.cryptocurrency.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.model.CryptoM;
import com.crescentek.cryptocurrency.model.PriceAlertModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by R.Android on 30-10-2018.
 */

public class SendCryptoAdapter extends RecyclerView.Adapter<SendCryptoAdapter.MyViewHolder> {

    List<CryptoM> cryptoList;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_send_crypto,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

           CryptoM cryptoM=new CryptoM();
           holder.crypto_name.setText(cryptoList.get(position).getCrypto_name());
           if(cryptoList.get(position).getCrypto_code().equalsIgnoreCase("BTC"))
           {
               holder.crypto_iv.setBackgroundResource(R.drawable.bit);
           }
           else if(cryptoList.get(position).getCrypto_code().equalsIgnoreCase("ETH"))
           {
                holder.crypto_iv.setBackgroundResource(R.drawable.ethereum_icon);
           }
           else {
               holder.crypto_iv.setBackgroundResource(R.drawable.icon_no_image);
           }

    }


    public SendCryptoAdapter(List<CryptoM> cryptoList) {
        this.cryptoList =cryptoList;
    }

    @Override
    public int getItemCount() {

        //Log.d("SellSize>>", buysellExchangeList.size()+"");
        return cryptoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView crypto_name;
        private ImageView crypto_iv;

        public MyViewHolder(View view) {
            super(view);
            crypto_iv = (ImageView) view.findViewById(R.id.crypto_iv);
            crypto_name = (TextView) view.findViewById(R.id.crypto_name);

        }
    }

}
