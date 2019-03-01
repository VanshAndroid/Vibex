package com.crescentek.cryptocurrency.activity.transaction_history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.utility.BaseActivity;

public class TranswithdrawDetails extends BaseActivity {

    private TextView title_tv,trx_ref,amount_tv,debit_user_bal,trx_fees,trx_timestamp,trx_status;
    private TextView headerText_tv;
    ImageView back_iv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStatusBarGradiant(TranswithdrawDetails.this);
        setContentView(R.layout.trans_wihtdraw_details);
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
        amount_tv=findViewById(R.id.amount_tv);
        debit_user_bal=findViewById(R.id.debit_user_bal);
        trx_fees=findViewById(R.id.trx_fees);
        trx_timestamp=findViewById(R.id.trx_timestamp);
        trx_status=findViewById(R.id.trx_status);

        setValues();
    }


    private void setValues() {

        headerText_tv.setText("Summary");
        title_tv.setText(""+getIntent().getStringExtra("type"));
        trx_fees.setText(""+getIntent().getStringExtra("trx_fees")+" "+getIntent().getStringExtra("currency_code"));
        trx_ref.setText(getIntent().getStringExtra("trx_log_id"));
        double debit_balance=Double.parseDouble(getIntent().getStringExtra("debit_value"));
        double trx_fees=Double.parseDouble(getIntent().getStringExtra("trx_fees"));
        double amount=debit_balance + trx_fees;
        amount_tv.setText(""+amount+" "+getIntent().getStringExtra("currency_code"));
        debit_user_bal.setText(""+getIntent().getStringExtra("debit_value")+" "+getIntent().getStringExtra("currency_code"));
        trx_timestamp.setText(""+getIntent().getStringExtra("trx_timestamp"));
        trx_status.setText(""+getIntent().getStringExtra("trx_status"));

    }
}
