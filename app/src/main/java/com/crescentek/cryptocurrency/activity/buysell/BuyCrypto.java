package com.crescentek.cryptocurrency.activity.buysell;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.activity.send.SendBitcoin_1;
import com.crescentek.cryptocurrency.activity.send.SendCrypto;
import com.crescentek.cryptocurrency.adapter.SendCryptoAdapter;
import com.crescentek.cryptocurrency.comman.WalletRecord;
import com.crescentek.cryptocurrency.interfaces.WalletListener;
import com.crescentek.cryptocurrency.model.CryptoM;
import com.crescentek.cryptocurrency.model.CurreencyM;
import com.crescentek.cryptocurrency.model.WalletN;
import com.crescentek.cryptocurrency.utility.BaseActivity;
import com.crescentek.cryptocurrency.utility.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by R.Android on 31-10-2018.
 */

public class BuyCrypto extends BaseActivity implements WalletListener {

    private ImageView back_iv, info;
    private TextView headerText_tv;
    private LinearLayout bitcoin_layout, ethereum_layout;
    List<CryptoM> cryptoList;
    RecyclerView crypto_recycler;

    SendCryptoAdapter adapter;
    String cryptoTag;
    String localCurrancy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(BuyCrypto.this);
        setContentView(R.layout.send_bitcoin);
        showCustomProgrssDialog(BuyCrypto.this);

        new WalletRecord(BuyCrypto.this, BuyCrypto.this);

        bitcoin_layout = findViewById(R.id.bitcoin_layout);
        ethereum_layout = findViewById(R.id.ethereum_layout);
        back_iv = findViewById(R.id.back_iv);
        info = findViewById(R.id.info);
        headerText_tv = findViewById(R.id.headerText_tv);
        crypto_recycler = findViewById(R.id.crypto_list);


        headerText_tv.setText("What would you like to Buy");
        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BuyCrypto.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        crypto_recycler.setLayoutManager(linearLayoutManager);

        crypto_recycler.addOnItemTouchListener(new RecyclerTouchListener(BuyCrypto.this, crypto_recycler, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Log.d("crypto_name_id", cryptoList.get(position).getCrypto_name() + ">>>>" + cryptoList.get(position).getCrypto_id());
                cryptoTag=cryptoList.get(position).getCrypto_code();

                Intent intent=new Intent(BuyCrypto.this, BuyBitCoin.class);
                intent.putExtra("crypto_id",cryptoList.get(position).getCrypto_id());
                intent.putExtra("cryptoName",cryptoList.get(position).getCrypto_name());
                if(cryptoTag.equalsIgnoreCase("BTC"))
                {
                    intent.putExtra("cryptoTag",cryptoTag);
                }else {
                    intent.putExtra("cryptoTag",cryptoTag);
                }
                intent.putExtra("localCurrancy",localCurrancy);

                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {

                Log.d("Position>>", position + "");

            }
        }));


    }


    @Override
    public void walletList(List<WalletN> walletList) throws Exception {

        hideCustomProgrssDialog();

    }

    @Override
    public void cryptoList(List<CryptoM> cryptoList) throws Exception {

        this.cryptoList = cryptoList;
        adapter = new SendCryptoAdapter(cryptoList);
        crypto_recycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void curreencyList(List<CurreencyM> curreencyList) throws Exception {

        localCurrancy=curreencyList.get(0).getCurrency_code();

    }
}
