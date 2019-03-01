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
import com.crescentek.cryptocurrency.model.AllBuyOrder;
import com.crescentek.cryptocurrency.model.AllSellOrder;
import com.crescentek.cryptocurrency.model.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by R.Android on 09-07-2018.
 */

public class Order_Book_Adapter extends RecyclerView.Adapter<Order_Book_Adapter.MyViewHolder> {

    List<AllSellOrder> allSellOrders;
    List<AllBuyOrder> allBuyOrders;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_order_book,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if(position<allSellOrders.size())
        {
            AllSellOrder sellOrder=allSellOrders.get(position);
            holder.sell_view.setBackgroundColor(Color.parseColor("#ECF6ED"));
            holder.tv_sell.setText(sellOrder.getCrypto_val()+" "+sellOrder.getCrypto_code());
            holder.tv_sell_amt.setText(sellOrder.getCryptoRate());
        }
        if(position<allBuyOrders.size())
        {
            AllBuyOrder buyOrder=allBuyOrders.get(position);
            holder.buy_view.setBackgroundColor(Color.parseColor("#FAE6E5"));
            holder.tv_buy.setText(buyOrder.getCrypto_val()+" "+buyOrder.getCrypto_code());
            holder.tv_buy_amt.setText(buyOrder.getCryptoRate());
        }

    }


    public Order_Book_Adapter(List<AllSellOrder> allSellOrders, List<AllBuyOrder>allBuyOrders) {
        this.allSellOrders=allSellOrders;
        this.allBuyOrders=allBuyOrders;
    }

    @Override
    public int getItemCount() {

        int viewSize=0;
        if(allSellOrders.size()>allBuyOrders.size())
        {
            viewSize=allSellOrders.size();
        }
        else {
            viewSize=allBuyOrders.size();
        }
        Log.d("Size>>",viewSize+"");
        return viewSize;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_buy, tv_buy_amt, tv_sell,tv_sell_amt;
        public LinearLayout buy_view,sell_view;

        public MyViewHolder(View view) {
            super(view);
            tv_buy = (TextView) view.findViewById(R.id.tv_buy);
            tv_buy_amt = (TextView) view.findViewById(R.id.tv_buy_amt);
            tv_sell = (TextView) view.findViewById(R.id.tv_sell);
            tv_sell_amt = (TextView) view.findViewById(R.id.tv_sell_amt);
            buy_view=view.findViewById(R.id.buy_view);
            sell_view=view.findViewById(R.id.sell_view);
        }
    }

}
