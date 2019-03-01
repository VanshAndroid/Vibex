package com.crescentek.cryptocurrency.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.model.NavDrawerItem;

import java.util.Collections;
import java.util.List;


/**
 * Created by R.Android on 9/30/2016.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder>{
    List<NavDrawerItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    public NavigationDrawerAdapter(Context context, List<NavDrawerItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NavDrawerItem current = data.get(position);
        holder.title.setText(current.getTitle());
        if(position==0)
        {
            holder.navicon.setBackgroundResource(R.mipmap.home);
            holder._view.setVisibility(View.GONE);
        }
        if(position==1)
        {
            holder.navicon.setBackgroundResource(R.mipmap.wallet);
            holder._view.setVisibility(View.GONE);
        }
        if(position==2)
        {
            holder.navicon.setBackgroundResource(R.mipmap.transaction);
            holder._view.setVisibility(View.GONE);
        }
        if(position==3)
        {
            holder.navicon.setBackgroundResource(R.mipmap.price);
            holder._view.setVisibility(View.GONE);
        }
        if(position==4)
        {
            holder.navicon.setBackgroundResource(R.mipmap.promotions);
            holder._view.setVisibility(View.GONE);
        }
        if(position==5)
        {
            holder.navicon.setBackgroundResource(R.mipmap.exchange);
            holder._view.setVisibility(View.VISIBLE);
        }
        if(position==6)
        {
            holder.navicon.setBackgroundResource(R.mipmap.settings);
            holder._view.setVisibility(View.GONE);
        }
        if(position==7)
        {
            holder.navicon.setBackgroundResource(R.mipmap.help);
            holder._view.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView navicon;
        View _view;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            navicon=(ImageView)itemView.findViewById(R.id.nav_icon);
            _view=itemView.findViewById(R.id._view);
        }
    }

}
