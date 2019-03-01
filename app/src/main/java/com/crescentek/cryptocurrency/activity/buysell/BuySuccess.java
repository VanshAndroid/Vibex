package com.crescentek.cryptocurrency.activity.buysell;

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
 * Created by R.Android on 01-08-2018.
 */

public class BuySuccess extends BaseActivity {

    private ImageView back_iv;
    private Button continue_btn;
    private TextView cryptCodeAndVal,locaclCodeAndVal;
    private TextView referance,trans_fee,wallet,total_paid_amt,crypto_tv,total_currency;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_success);
        back_iv=findViewById(R.id.back_iv);
        continue_btn=findViewById(R.id.continue_btn);
        referance=findViewById(R.id.referance);
        trans_fee=findViewById(R.id.trans_fee);
        crypto_tv=findViewById(R.id.crypto_tv);
        wallet=findViewById(R.id.wallet);
        total_paid_amt=findViewById(R.id.total_paid_amt);
        cryptCodeAndVal=findViewById(R.id.cryptCodeAndVal);
        locaclCodeAndVal=findViewById(R.id.locaclCodeAndVal);
        total_currency=findViewById(R.id.total_currency);

        if(getIntent().getStringExtra("type").equalsIgnoreCase("buy"))
        {
            cryptCodeAndVal.setText(getIntent().getStringExtra("crypto_Credited")+" "+getIntent().getStringExtra("cryptoCode")+" bought at "+getIntent().getStringExtra("currVal")+" "+getIntent().getStringExtra("currCode"));
            referance.setText(getIntent().getStringExtra("trx_ref"));
            trans_fee.setText(getIntent().getStringExtra("fees")+" "+getIntent().getStringExtra("currCode"));
            wallet.setText(getIntent().getStringExtra("crypto_Credited")+" "+getIntent().getStringExtra("cryptoCode"));
            total_paid_amt.setText(getIntent().getStringExtra("currVal")+" "+getIntent().getStringExtra("currCode"));
        }
        else {
            crypto_tv.setText("Crypto debited       ");
            total_currency.setText("Currency recived    ");
            cryptCodeAndVal.setText(getIntent().getStringExtra("cryptoVal")+" "+getIntent().getStringExtra("cryptoCode")+" Sold at "+getIntent().getStringExtra("currVal")+" "+getIntent().getStringExtra("currCode"));
            referance.setText(""+getIntent().getStringExtra("trx_ref"));
            trans_fee.setText(""+getIntent().getStringExtra("fees")+" "+getIntent().getStringExtra("currCode"));
            wallet.setText(""+getIntent().getStringExtra("cryptoVal")+" "+getIntent().getStringExtra("cryptoCode"));
            total_paid_amt.setText(""+getIntent().getStringExtra("currVal")+" "+getIntent().getStringExtra("currCode"));
        }

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

    }
}
