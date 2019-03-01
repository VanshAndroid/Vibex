package com.crescentek.cryptocurrency.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.activity.deposit.AmountDeposit;
import com.crescentek.cryptocurrency.activity.login_signup.ReciveBitCoin;
import com.crescentek.cryptocurrency.activity.send.SendCrypto;
import com.crescentek.cryptocurrency.activity.withdraw.WithDrawActivity;
import com.crescentek.cryptocurrency.model.WalletN;
import com.crescentek.cryptocurrency.utility.NetworkUtility;

import java.util.List;

/**
 * Created by R.Android on 25-09-2018.
 */

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.MyViewHolder> {

    private List<WalletN> walletList;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView btc_txt, balance_tv, available_amt,withdraw_buy,recive_sell;
        public ImageView currencyLogo;

        public MyViewHolder(View view) {
            super(view);
            btc_txt = (TextView) view.findViewById(R.id.btc_txt);
            balance_tv = (TextView) view.findViewById(R.id.balance_tv);
            available_amt = (TextView) view.findViewById(R.id.available_amt);
            withdraw_buy = (TextView) view.findViewById(R.id.withdraw_buy);
            recive_sell = (TextView) view.findViewById(R.id.recive_sell);
            currencyLogo = (ImageView) view.findViewById(R.id.currencyLogo);
        }
    }

    public AccountAdapter(List<WalletN> walletList, Context context)
    {
        this.walletList=walletList;
        this.context=context;
        Log.d("WalletSize>>>",this.walletList.size()+"");
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account,parent,false);

        return new AccountAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        WalletN walletN=walletList.get(position);

        String crypto_code=walletList.get(position).getCrypto_code();
        String currency_code=walletList.get(position).getCurrency_code();

        Log.d("code>>",crypto_code+position+":::"+currency_code);
        if(walletN.getCrypto_code().equalsIgnoreCase("null"))
        {
            holder.withdraw_buy.setText("Deposit");
            holder.recive_sell.setText("Withdraw");
            //holder.currencyLogo.setImageResource(R.drawable.ngn_icon);

            Glide.with(context).load(NetworkUtility.IMAGE_URL+walletN.getCurrency_logo())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.currencyLogo);

            holder.btc_txt.setText(walletN.getCurrency_code()+" Account");
            //holder.balance_tv.setText(walletN.getCurrency_code()+" "+walletN.getCurrency_value());
            holder.balance_tv.setText(walletN.getCurrency_value());
            holder.available_amt.setText(walletN.getCurrency_code()+" "+walletN.getCurrency_value());

            holder.withdraw_buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(context, AmountDeposit.class);
                    context.startActivity(intent);

                }
            });
            holder.recive_sell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(context, WithDrawActivity.class);
                    context.startActivity(intent);

                }
            });

        }
        else  {
            holder.withdraw_buy.setText("Send");
            holder.recive_sell.setText("Receive");

            holder.btc_txt.setText(walletN.getCrypto_code()+" Account");
            //holder.balance_tv.setText(walletN.getCrypto_code()+" "+walletN.getCrypto_value());
            if(walletN.getCrypto_code().equalsIgnoreCase("BTC"))
            {
                //holder.currencyLogo.setImageResource(R.drawable.bit);
                Glide.with(context).load(NetworkUtility.IMAGE_URL+walletN.getCrypto_logo())
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.currencyLogo);
            }
            else {
                //holder.currencyLogo.setImageResource(R.drawable.ethereum_icon);

                Glide.with(context).load(NetworkUtility.IMAGE_URL+walletN.getCrypto_logo())
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.currencyLogo);
            }
            holder.balance_tv.setText(walletN.getCrypto_value());
            holder.available_amt.setText(walletN.getCrypto_code()+" "+walletN.getCrypto_value());

            holder.withdraw_buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(context, SendCrypto.class);
                    /*intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
                    context.startActivity(intent);

                }
            });
            holder.recive_sell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(context, ReciveBitCoin.class);
                    /*intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
                    context.startActivity(intent);

                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return walletList.size();
    }
}
