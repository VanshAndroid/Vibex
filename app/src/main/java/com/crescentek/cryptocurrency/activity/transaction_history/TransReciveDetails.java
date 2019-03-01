package com.crescentek.cryptocurrency.activity.transaction_history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.utility.BaseActivity;

public class TransReciveDetails extends BaseActivity {

    ImageView back_iv;
    private TextView headerText_tv,trx_type,trx_ref,amount_tv,credit_users_wallets,trx_fees,trx_timestamp,trx_status;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trans_recive_details);
        setStatusBarGradiant(TransReciveDetails.this);

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

        trx_type=findViewById(R.id.trx_type);
        trx_ref=findViewById(R.id.trx_ref);
        amount_tv=findViewById(R.id.amount_tv);
        credit_users_wallets=findViewById(R.id.credit_users_wallets);
        trx_fees=findViewById(R.id.trx_fees);
        trx_timestamp=findViewById(R.id.trx_timestamp);
        trx_status=findViewById(R.id.trx_status);

        setValues();

    }

    private void setValues() {

        Log.d("Crypto>>>","");

        headerText_tv.setText("Summary");

        trx_ref.setText(getIntent().getStringExtra("trx_log_id"));
        amount_tv.setText(""+getIntent().getStringExtra("crypto_val")+" "+getIntent().getStringExtra("crypto_code"));
        credit_users_wallets.setText(""+getIntent().getStringExtra("credit_wallet_value")+" "+getIntent().getStringExtra("crypto_code"));
        trx_fees.setText(""+getIntent().getStringExtra("trx_fees")+" "+getIntent().getStringExtra("crypto_code"));
        trx_timestamp.setText(""+getIntent().getStringExtra("trx_timestamp"));
        trx_status.setText(""+getIntent().getStringExtra("trx_status"));

        String content=getIntent().getStringExtra("type")+" "+getIntent().getStringExtra("crypto_code")+" "+getIntent().getStringExtra("crypto_val")+" to \n"+getIntent().getStringExtra("credit_address");

        trx_type.setText(content);


    }
}
