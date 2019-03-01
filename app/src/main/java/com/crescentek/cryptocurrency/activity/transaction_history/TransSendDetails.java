package com.crescentek.cryptocurrency.activity.transaction_history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.utility.BaseActivity;

public class TransSendDetails extends BaseActivity {

    private TextView title_tv,trx_ref,crypto_tv,credit_users_wallets,trx_fees,trx_timestamp,trx_status;
    private TextView headerText_tv;
    private ImageView back_iv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(TransSendDetails.this);
        setContentView(R.layout.trans_send_details);
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
        title_tv=findViewById(R.id.title_tv);
        trx_ref=findViewById(R.id.trx_ref);
        crypto_tv=findViewById(R.id.crypto_tv);
        credit_users_wallets=findViewById(R.id.credit_users_wallets);
        trx_fees=findViewById(R.id.trx_fees);
        trx_timestamp=findViewById(R.id.trx_timestamp);
        trx_status=findViewById(R.id.trx_status);
        setValues();

    }

    private void setValues() {

        headerText_tv.setText("Summary");

        trx_ref.setText(getIntent().getStringExtra("trx_log_id"));
        crypto_tv.setText(""+getIntent().getStringExtra("crypto_val")+" "+getIntent().getStringExtra("crypto_code"));
        credit_users_wallets.setText(""+getIntent().getStringExtra("credit_wallet_value")+" "+getIntent().getStringExtra("crypto_code"));
        trx_fees.setText(""+getIntent().getStringExtra("trx_fees")+" "+getIntent().getStringExtra("crypto_code"));
        trx_timestamp.setText(""+getIntent().getStringExtra("trx_timestamp"));
        trx_status.setText(""+getIntent().getStringExtra("trx_status"));

        String content=getIntent().getStringExtra("send")+" "+getIntent().getStringExtra("crypto_code")+" "+getIntent().getStringExtra("crypto_val")+" to \n"+getIntent().getStringExtra("credit_address");
        title_tv.setText(content);

    }

}
