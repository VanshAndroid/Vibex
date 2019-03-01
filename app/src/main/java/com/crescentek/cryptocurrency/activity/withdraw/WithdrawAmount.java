package com.crescentek.cryptocurrency.activity.withdraw;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.utility.BaseActivity;
import com.crescentek.cryptocurrency.utility.UserSessionManager;

/**
 * Created by R.Android on 22-10-2018.
 */

public class WithdrawAmount extends BaseActivity {

    EditText amount_ed;
    TextView account_number,account_holder,bank_name,wallet_bal,witdraw_limit;
    Button withdraw_btn;
    UserSessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(WithdrawAmount.this);
        setContentView(R.layout.withdraw_amt);

        sessionManager=new UserSessionManager(getApplicationContext());

        initViews();

        account_number.setText(getIntent().getStringExtra("bank_account"));

        account_holder.setText(getIntent().getStringExtra("bank_account_name"));

        bank_name.setText(getIntent().getStringExtra("bank_name"));

        wallet_bal.setText("Avilable Wallet Balance NGN "+sessionManager.getValues(UserSessionManager.NGN_WALLET));

        witdraw_limit.setText("NGN "+sessionManager.getValues(UserSessionManager.KEY_CAN_WITHDRAW_LIMIT));

        withdraw_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(amount_ed.getText().toString().length()<1)
                {
                    amount_ed.setError("Enter Amount !!!");
                }
                else {
                    Intent intent=new Intent(getApplicationContext(),TransactionPin.class);
                    intent.putExtra("users_bank_id",getIntent().getStringExtra("users_bank_id"));
                    intent.putExtra("currency_value",amount_ed.getText().toString().trim());
                    startActivity(intent);
                }

            }
        });


    }

    public void initViews()
    {
        account_number=findViewById(R.id.account_number);
        account_holder=findViewById(R.id.account_holder);
        bank_name=findViewById(R.id.bank_name);
        wallet_bal=findViewById(R.id.wallet_bal);
        witdraw_limit=findViewById(R.id.witdraw_limit);

        amount_ed=findViewById(R.id.amount_ed);
        withdraw_btn=findViewById(R.id.withdraw_btn);
    }

}
