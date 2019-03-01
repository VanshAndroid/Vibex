package com.crescentek.cryptocurrency.activity.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.activity.login_signup.HomeActivity;
import com.crescentek.cryptocurrency.utility.BaseActivity;
import com.crescentek.cryptocurrency.utility.UserSessionManager;

/**
 * Created by R.Android on 22-06-2018.
 */

public class MobVerifiedActivity extends BaseActivity {

    private ImageView back_iv;
    private TextView headerText_tv,next_step_tv;
    UserSessionManager sessionManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(MobVerifiedActivity.this);
        setContentView(R.layout.mob_verified_activity);

        sessionManager=new UserSessionManager(MobVerifiedActivity.this);
        initView();

        headerText_tv.setText("Mobile Number Verified");
        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        next_step_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MobVerifiedActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

    }

    public void initView()
    {
        headerText_tv=findViewById(R.id.headerText_tv);
        next_step_tv=findViewById(R.id.next_step_tv);
        back_iv=findViewById(R.id.back_iv);

    }
}
