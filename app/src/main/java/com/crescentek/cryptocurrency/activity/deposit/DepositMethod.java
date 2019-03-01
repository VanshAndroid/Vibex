package com.crescentek.cryptocurrency.activity.deposit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.utility.BaseActivity;

/**
 * Created by R.Android on 11-07-2018.
 */

public class DepositMethod extends BaseActivity {

    private ImageView back_iv;
    private TextView headerText_tv;
    private LinearLayout paystack,gtbank;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(DepositMethod.this);
        setContentView(R.layout.deposit_activity);

        back_iv=findViewById(R.id.back_iv);
        headerText_tv=findViewById(R.id.headerText_tv);
        paystack=findViewById(R.id.paystack);
        gtbank=findViewById(R.id.gtbank);
        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        headerText_tv.setText("Deposit Method");


        paystack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getApplicationContext(),"Implement Payment Getway ",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),AmountDeposit.class));
                finish();

            }
        });

        gtbank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),AmountDeposit.class));
                finish();

            }
        });

    }
}
