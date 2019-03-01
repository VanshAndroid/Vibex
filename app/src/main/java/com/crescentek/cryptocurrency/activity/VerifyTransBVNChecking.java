package com.crescentek.cryptocurrency.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.activity.settings.VerifyPersonalDetailActivity;
import com.crescentek.cryptocurrency.utility.BaseActivity;

public class VerifyTransBVNChecking extends BaseActivity {

    TextView content_tv;
    Button btn_done;
    ImageView back_iv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checking_transaction);

        content_tv=findViewById(R.id.content_tv);
        btn_done=findViewById(R.id.btn_done);
        back_iv=findViewById(R.id.back_iv);

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        content_tv.setText(R.string.verify_bvn);

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(),VerifyPersonalDetailActivity.class));
            }
        });


    }
}
