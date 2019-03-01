package com.crescentek.cryptocurrency.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.model.TransactionDetails;

import java.util.List;

/**
 * Created by R.Android on 11-10-2018.
 */

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {

    List<TransactionDetails> transactionList;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_trans,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        TransactionDetails details=transactionList.get(position);

        if(details.getTrx_type().equalsIgnoreCase("Buy")) {
            double credit_market_rate=Double.parseDouble(details.getCredit_market_rate());
            double currency_val=Double.parseDouble(details.getCurrency_val());
            double currency_val_local=credit_market_rate*currency_val;
            double fees_usd=Double.parseDouble(details.getTrx_fees());
            double fees_local=fees_usd*credit_market_rate;

            holder.payment_type_tv.setText("Bought "+details.getCrypto_code()+" "+details.getCrypto_val()+" for "+details.getCurrency_code()+" "+currency_val_local);
            holder.trans_amt.setText(details.getCurrency_code()+" - "+currency_val_local);
            holder.trans_date_tv.setText(details.getTrx_timestamp());
            holder.trx_id.setText("Reference "+details.getTrx_log_id());
            holder.transaction_fee.setText("Fee for "+details.getCrypto_code()+" "+details.getCrypto_val()+" Bought "+details.getCurrency_code()+" - "+fees_local);

        }
        else if(details.getTrx_type().equalsIgnoreCase("Sell")) {

            double credit_market_rate=Double.parseDouble(details.getCredit_market_rate());
            double currency_val=Double.parseDouble(details.getCurrency_val());
            double currency_val_local=credit_market_rate*currency_val;
            double fees_usd=Double.parseDouble(details.getTrx_fees());
            double fees_local=fees_usd*credit_market_rate;

            holder.payment_type_tv.setText("Sold "+details.getCrypto_code()+" "+details.getCrypto_val()+" for "+details.getCurrency_code()+" "+currency_val_local);
            holder.trans_amt.setText(details.getCurrency_code()+" "+currency_val_local);
            holder.trans_date_tv.setText(details.getTrx_timestamp());
            holder.trx_id.setText("Reference "+details.getTrx_log_id());
            holder.transaction_fee.setText("Fee for "+details.getCrypto_code()+" "+details.getCrypto_val()+" Sold "+details.getCurrency_code()+" - "+fees_local);

        }
        else if(details.getTrx_type().equalsIgnoreCase("Send")) {

            holder.payment_type_tv.setText("Sent "+details.getCrypto_code()+" "+details.getCrypto_val());
            holder.trans_amt.setText(details.getCrypto_code()+" - "+details.getCrypto_val());
            holder.trans_date_tv.setText(details.getTrx_timestamp());
            holder.trx_id.setText("Reference "+details.getTrx_log_id());
            holder.transaction_fee.setText("Fee for "+details.getCrypto_code()+" "+details.getCrypto_val()+" Sent "+"  "+details.getCrypto_code()+" - "+details.getTrx_fees());

        }
        else if(details.getTrx_type().equalsIgnoreCase("Receive")) {

            holder.payment_type_tv.setText("Recived "+details.getCrypto_code()+" "+details.getCrypto_val());
            holder.trans_amt.setText(details.getCrypto_code()+" "+details.getCrypto_val());
            holder.trans_date_tv.setText(details.getTrx_timestamp());
            holder.trx_id.setText("Reference "+details.getTrx_log_id());
            holder.transaction_fee.setText("Fee for "+details.getCrypto_code()+" "+details.getCrypto_val()+"  "+details.getCrypto_code()+" - "+details.getTrx_fees());


        }
        else if(details.getTrx_type().equalsIgnoreCase("Deposit")) {

            holder.payment_type_tv.setText("Deposit");
            holder.trans_amt.setText(details.getCurrency_code()+" "+details.getCurrency_val());
            holder.trans_date_tv.setText(details.getTrx_timestamp());
            holder.trx_id.setText("Reference "+details.getTrx_log_id());
            holder.transaction_fee.setText("Deposit fee "+details.getCurrency_code()+" "+details.getCurrency_val()+" for "+details.getCurrency_code()+" - "+details.getTrx_fees());

        }
        else if(details.getTrx_type().equalsIgnoreCase("withdraw")) {

            holder.payment_type_tv.setText("Withdraw");
            holder.trans_amt.setText(details.getCurrency_code()+" - "+details.getCurrency_val());
            holder.trans_date_tv.setText(details.getTrx_timestamp());
            holder.trx_id.setText("Reference "+details.getTrx_log_id());
            holder.transaction_fee.setText("Withdraw fee "+details.getCurrency_code()+" "+details.getCurrency_val()+" for "+details.getCurrency_code()+" - "+details.getTrx_fees());

        }
        else if(details.getTrx_type().equalsIgnoreCase("Exchange Buy")) {

            double credit_market_rate=Double.parseDouble(details.getCredit_market_rate());
            double trx_fees=Double.parseDouble(details.getTrx_fees());
            double fees=trx_fees*credit_market_rate;


            holder.payment_type_tv.setText("Exchange Buy  "+details.getCrypto_code()+" "+details.getCrypto_closing_traded()+" at "+details.getCurrency_code()+" "+details.getAmount());
            holder.trans_amt.setText(details.getCrypto_code()+" "+details.getCrypto_closing_traded());
            holder.trans_date_tv.setText(details.getTrx_timestamp());
            holder.trx_id.setText("Reference "+details.getTrx_log_id());
            holder.transaction_fee.setText("Fee for Exchange Buy "+details.getCrypto_code()+" "+details.getCrypto_closing_traded()+" for "+details.getCurrency_code()+" - "+fees);

        }else {

            double credit_market_rate=Double.parseDouble(details.getCredit_market_rate());
            double trx_fees=Double.parseDouble(details.getTrx_fees());
            double fees=trx_fees*credit_market_rate;

            holder.payment_type_tv.setText("Exchange Bid for "+details.getCrypto_code()+" "+details.getCrypto_closing_traded()+" at "+details.getCurrency_code()+" "+details.getAmount());
            holder.trans_amt.setText(details.getCrypto_code()+" "+details.getCrypto_closing_traded());
            holder.trans_date_tv.setText(details.getTrx_timestamp());
            holder.trx_id.setText("Reference "+details.getTrx_log_id());
            holder.transaction_fee.setText("Fee for Exchange Sell "+details.getCrypto_code()+" "+details.getCrypto_closing_traded()+" for "+details.getCurrency_code()+" - "+fees);

        }


    }

    public TransactionAdapter(List<TransactionDetails> transactionList) {
        this.transactionList=transactionList;
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView payment_type_tv, trans_date_tv, trans_amt,transaction_fee,trx_id;
        public MyViewHolder(View view) {
            super(view);
            payment_type_tv = (TextView) view.findViewById(R.id.payment_type_tv);
            trans_date_tv = (TextView) view.findViewById(R.id.trans_date_tv);
            trans_amt = (TextView) view.findViewById(R.id.trans_amt);
            trx_id=(TextView) view.findViewById(R.id.trx_id);
            transaction_fee = (TextView) view.findViewById(R.id.transaction_fee);
        }
    }

}
