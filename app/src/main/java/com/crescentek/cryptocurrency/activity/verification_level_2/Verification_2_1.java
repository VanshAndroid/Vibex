package com.crescentek.cryptocurrency.activity.verification_level_2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.utility.BaseActivity;

/**
 * Created by R.Android on 26-07-2018.
 */

public class Verification_2_1 extends BaseActivity {

    private TextView next_tv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiantYellow(Verification_2_1.this);
        setContentView(R.layout.verification_2_1);

        initView();

        next_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),Verification_2_2.class));

            }
        });

    }

    public void initView()
    {
        next_tv=findViewById(R.id.next_tv);
    }
}
