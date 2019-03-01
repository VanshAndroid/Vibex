package com.crescentek.cryptocurrency.activity.transaction_history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.utility.BaseActivity;

/**
 * Created by R.Android on 16-10-2018.
 */

public class TransBuySellDetails extends BaseActivity {

    private TextView trx_type, trx_ref, amount, crypto, fees, headerText_tv, trx_status,trx_timestamp;
    private ImageView back_iv,icon_buy_sell;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(TransBuySellDetails.this);
        setContentView(R.layout.trans_buy_sell_details);
        initViews();
        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void initViews() {

        back_iv = findViewById(R.id.back_iv);
        icon_buy_sell=findViewById(R.id.icon_buy_sell);
        trx_type = findViewById(R.id.trx_type);
        trx_ref = findViewById(R.id.trx_ref);
        amount = findViewById(R.id.amount);
        crypto = findViewById(R.id.crypto);
        fees = findViewById(R.id.fees);
        trx_status = findViewById(R.id.trx_status);
        trx_timestamp=findViewById(R.id.trx_timestamp);
        headerText_tv = findViewById(R.id.headerText_tv);

        setValues();

    }

    private void setValues() {

        headerText_tv.setText("Summary");
        trx_ref.setText(getIntent().getStringExtra("trx_log_id"));
        amount.setText(getIntent().getStringExtra("amount") + " " + getIntent().getStringExtra("currency_code"));
        crypto.setText( getIntent().getStringExtra("crypto_val") + " " + getIntent().getStringExtra("crypto_code"));
        double trx_fees = Double.parseDouble(getIntent().getStringExtra("trx_fees"));
        double credit_market_rate = Double.parseDouble(getIntent().getStringExtra("credit_market_rate"));
        double fee = trx_fees * credit_market_rate;
        fees.setText("" + fee + " " + getIntent().getStringExtra("currency_code"));
        trx_status.setText("" + getIntent().getStringExtra("trx_status"));
        trx_timestamp.setText(""+getIntent().getStringExtra("trx_timestamp"));

        if(getIntent().getStringExtra("type").equalsIgnoreCase("buy"))
        {
            String content="Bought "+ getIntent().getStringExtra("crypto_code")+" "+ getIntent().getStringExtra("crypto_val")+" for \n"+ getIntent().getStringExtra("currency_code")+" "+ getIntent().getStringExtra("amount") ;
            trx_type.setText(content);
        }
        else {
            String content="Sold "+ getIntent().getStringExtra("crypto_code")+" "+ getIntent().getStringExtra("crypto_val")+" for \n"+ getIntent().getStringExtra("currency_code")+" "+ getIntent().getStringExtra("amount") ;
            trx_type.setText(content);
        }

        if(getIntent().getStringExtra("type").equalsIgnoreCase("buy")){
            icon_buy_sell.setImageResource(R.drawable.t_buy);
        }
        else {
            icon_buy_sell.setImageResource(R.drawable.t_sell);
        }

    }
}