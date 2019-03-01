package com.crescentek.cryptocurrency.activity.send;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.activity.buysell.BuySuccess;
import com.crescentek.cryptocurrency.activity.login_signup.HomeActivity;
import com.crescentek.cryptocurrency.utility.BaseActivity;

/**
 * Created by R.Android on 03-08-2018.
 */

public class SendSuccess extends BaseActivity {

    Button continue_btn;
    private TextView cryptCodeAndVal;
    private ImageView back_iv;
    private TextView reference,trans_fee,total_crypto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStatusBarGradiant(SendSuccess.this);
        setContentView(R.layout.send_bitcoin_success);
        cryptCodeAndVal=findViewById(R.id.cryptCodeAndVal);
        continue_btn=findViewById(R.id.continue_btn);
        back_iv=findViewById(R.id.back_iv);
        reference=findViewById(R.id.referance);
        trans_fee=findViewById(R.id.trans_fee);
        total_crypto=findViewById(R.id.total_crypto);

        cryptCodeAndVal.setText(""+getIntent().getStringExtra("cryptoSent")+" "+getIntent().getStringExtra("cryptoCode")+" Sent to "+getIntent().getStringExtra("contact"));
        reference.setText(""+getIntent().getStringExtra("trx_ref"));
        trans_fee.setText(""+getIntent().getStringExtra("fees")+" "+getIntent().getStringExtra("cryptoCode"));
        total_crypto.setText(""+getIntent().getStringExtra("cryptoVal")+" "+getIntent().getStringExtra("cryptoCode"));

        continue_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent intent=new Intent(SendSuccess.this,HomeActivity.class);
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
                Intent intent=new Intent(SendSuccess.this,HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}
