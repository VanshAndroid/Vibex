package com.crescentek.cryptocurrency.activity.send;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.utility.BaseActivity;

/**
 * Created by R.Android on 03-08-2018.
 */

public class SendBitcoin_3 extends BaseActivity {

    ImageView next_iv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(SendBitcoin_3.this);
        setContentView(R.layout.send_bitcoin_3);

        next_iv=findViewById(R.id.next_iv);
        next_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),SendSummary.class));

            }
        });

    }
}
