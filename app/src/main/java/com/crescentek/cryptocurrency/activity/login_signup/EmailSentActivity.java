package com.crescentek.cryptocurrency.activity.login_signup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.utility.BaseActivity;

/**
 * Created by R.Android on 26-07-2018.
 */

public class EmailSentActivity extends BaseActivity {


    private ImageView back_iv;
    private TextView headerText_tv,ok_tv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(EmailSentActivity.this);
        setContentView(R.layout.email_sent_activity);

        initView();
        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        headerText_tv.setText("Email Sent");

        ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    public void initView()
    {
        back_iv=findViewById(R.id.back_iv);
        headerText_tv=findViewById(R.id.headerText_tv);
        ok_tv=findViewById(R.id.ok_tv);
    }
}
