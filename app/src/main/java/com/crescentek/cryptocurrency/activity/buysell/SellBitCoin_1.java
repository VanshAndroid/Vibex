package com.crescentek.cryptocurrency.activity.buysell;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.utility.BaseActivity;

/**
 * Created by R.Android on 01-08-2018.
 */

public class SellBitCoin_1 extends BaseActivity {


    private ImageView back_iv;
    private Button continue_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(SellBitCoin_1.this);
        setContentView(R.layout.sell_bit_coin_1);
        back_iv=findViewById(R.id.back_iv);
        continue_btn=findViewById(R.id.continue_btn);

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SellBitCoin_Pin.class));

            }
        });

    }
}
