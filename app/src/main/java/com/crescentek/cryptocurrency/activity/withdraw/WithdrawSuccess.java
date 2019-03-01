package com.crescentek.cryptocurrency.activity.withdraw;

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
 * Created by R.Android on 22-10-2018.
 */

public class WithdrawSuccess extends BaseActivity {

    private TextView withdraw_amt;
    private Button continue_btn;
    private ImageView back_iv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.withdraw_success);
        initViews();

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
        String mbankAccount=getIntent().getStringExtra("withdrable_amount");
        String successText="Withdrawn to bank account xxxx"+mbankAccount.substring(mbankAccount.length()-4)+" NGN "+getIntent().getStringExtra("withdrable_amount")+"\n\nFee Charged: NGN "+getIntent().getStringExtra("fees")+"\n\nTotal deducted from your wallet: NGN "+getIntent().getStringExtra("amount");

        withdraw_amt.setText(successText);

    }
    public void initViews()
    {
        withdraw_amt=findViewById(R.id.withdraw_amt);
        continue_btn=findViewById(R.id.continue_btn);
        back_iv=findViewById(R.id.back_iv);

    }

}
