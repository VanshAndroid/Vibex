package com.crescentek.cryptocurrency.activity.deposit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.activity.login_signup.HomeActivity;
import com.crescentek.cryptocurrency.utility.BaseActivity;

/**
 * Created by R.Android on 26-09-2018.
 */

public class DepositSuccess extends BaseActivity {

    private TextView deposit_amt,referance,trans_fee,wallet_deposit,total_paid_amt;
    private Button continue_btn;
    private ImageView back_iv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStatusBarGradiant(DepositSuccess.this);
        setContentView(R.layout.deposit_success);

        deposit_amt=findViewById(R.id.deposit_amt);
        referance=findViewById(R.id.referance);
        continue_btn=findViewById(R.id.continue_btn);
        back_iv=findViewById(R.id.back_iv);
        trans_fee=findViewById(R.id.trans_fee);
        wallet_deposit=findViewById(R.id.wallet_deposit);
        total_paid_amt=findViewById(R.id.total_paid_amt);

        deposit_amt.setText(getIntent().getStringExtra("wallet")+" added in Fiat Wallet");
        trans_fee.setText(getIntent().getStringExtra("fees"));
        referance.setText(getIntent().getStringExtra("trx_ref"));
        wallet_deposit.setText(getIntent().getStringExtra("wallet"));
        total_paid_amt.setText(getIntent().getStringExtra("amount"));

        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }
        });

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }
        });

    }
}
