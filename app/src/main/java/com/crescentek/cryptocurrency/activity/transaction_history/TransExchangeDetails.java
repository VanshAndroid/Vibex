package com.crescentek.cryptocurrency.activity.transaction_history;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.utility.BaseActivity;

public class TransExchangeDetails extends BaseActivity {

    ImageView back_iv,icon_exchange;
    TextView headerText_tv,exchange_title_tv,amount_tv,trx_fees,trx_timestamp,crypto_closing_balance,crypto_closing_traded,trx_status,trx_ref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trans_exchange_detail);
        setStatusBarGradiant(TransExchangeDetails.this);
        initViews();
        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initViews() {
        back_iv=findViewById(R.id.back_iv);
        headerText_tv=findViewById(R.id.headerText_tv);
        trx_ref=findViewById(R.id.trx_ref);
        exchange_title_tv=findViewById(R.id.exchange_title_tv);
        amount_tv=findViewById(R.id.amount_tv);
        trx_fees=findViewById(R.id.trx_fees);
        trx_timestamp=findViewById(R.id.trx_timestamp);
        crypto_closing_balance=findViewById(R.id.crypto_closing_balance);
        crypto_closing_traded=findViewById(R.id.crypto_closing_traded);
        trx_status=findViewById(R.id.trx_status);
        icon_exchange=findViewById(R.id.icon_exchange);

        setValues();
    }

    private void setValues() {

        headerText_tv.setText("Summary");
        trx_ref.setText(getIntent().getStringExtra("trx_log_id"));
        exchange_title_tv.setText(""+getIntent().getStringExtra("exchange"));
        amount_tv.setText(""+getIntent().getStringExtra("amount")+" "+getIntent().getStringExtra("currency_code"));
        trx_fees.setText(""+getIntent().getStringExtra("trx_fees")+" "+getIntent().getStringExtra("crypto_code"));
        trx_timestamp.setText(""+getIntent().getStringExtra("trx_timestamp"));
        crypto_closing_balance.setText(""+getIntent().getStringExtra("crypto_closing_balance")+" "+getIntent().getStringExtra("crypto_code"));
        crypto_closing_traded.setText(""+getIntent().getStringExtra("crypto_closing_traded")+" "+getIntent().getStringExtra("crypto_code"));
        trx_status.setText(""+getIntent().getStringExtra("trx_status"));

        if (getIntent().getStringExtra("exchange").equalsIgnoreCase("Exchange buy"))
        {
            icon_exchange.setImageResource(R.drawable.t_buy);
        }else {
            icon_exchange.setImageResource(R.drawable.t_sell);
        }

    }

}