package com.crescentek.cryptocurrency.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.model.CryptoM;

import java.util.List;

/**
 * Created by R.Android on 29-10-2018.
 */

public class CryptoAdapter extends BaseAdapter {

    LayoutInflater flater;
    List<CryptoM> cryptoMList;
    String showCrypto="";

    public CryptoAdapter(Activity context, int resouceId, int textviewId, List<CryptoM> list){

        flater = context.getLayoutInflater();
        cryptoMList=list;
    }

    public CryptoAdapter(Activity context, int resouceId, int textviewId, List<CryptoM> list,String showCrypto){
        flater = context.getLayoutInflater();
        cryptoMList=list;
        this.showCrypto=showCrypto;
    }

    @Override
    public int getCount() {
        return cryptoMList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowview;
        if (showCrypto.equalsIgnoreCase("Crypto"))
        {
            rowview = flater.inflate(R.layout.list_item_crypto,null,true);

            TextView cryptoName = (TextView) rowview.findViewById(R.id.cryptoName);
            TextView cryptoValue = (TextView) rowview.findViewById(R.id.cryptoValue);

            cryptoName.setText(cryptoMList.get(position).getCrypto_code()+"    ");
            //cryptoValue.setText(cryptoMList.get(position).getCrypto_value());
            cryptoValue.setVisibility(View.GONE);

        }
        else {
            rowview = flater.inflate(R.layout.list_item_crypto,null,true);
            TextView cryptoName = (TextView) rowview.findViewById(R.id.cryptoName);
            TextView cryptoValue = (TextView) rowview.findViewById(R.id.cryptoValue);

            cryptoName.setText(cryptoMList.get(position).getCrypto_code());
            cryptoValue.setText(cryptoMList.get(position).getCrypto_value());
        }



        return rowview;
    }


}
