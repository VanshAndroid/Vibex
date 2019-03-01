package com.crescentek.cryptocurrency.activity.deposit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.utility.BaseActivity;

/**
 * Created by R.Android on 12-07-2018.
 */

public class MakeDeposit extends BaseActivity {

    private ImageView back_iv;
    private TextView headerText_tv,deposit_tv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(MakeDeposit.this);
        setContentView(R.layout.make_deposit);

        back_iv=findViewById(R.id.back_iv);
        headerText_tv=findViewById(R.id.headerText_tv);
        deposit_tv=findViewById(R.id.deposit_tv);

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        headerText_tv.setText("Make a deposit");

        deposit_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),AwaitDeposit.class));

            }
        });


    }
}
