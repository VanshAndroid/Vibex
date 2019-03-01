package com.crescentek.cryptocurrency.activity.verification_level_2;

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
import com.crescentek.cryptocurrency.utility.UserSessionManager;

/**
 * Created by R.Android on 30-07-2018.
 */

public class UploadSuccess extends BaseActivity {

    private Button done_btn;
    private TextView headerText_tv;
    private ImageView back_iv;
    UserSessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(UploadSuccess.this);
        setContentView(R.layout.uploading_success);
        sessionManager=new UserSessionManager(UploadSuccess.this);

        initView();

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UploadSuccess.this, HomeActivity.class);
                //intent.putExtra("position","6");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(UploadSuccess.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

    }

    public void initView()
    {
        done_btn=findViewById(R.id.done_btn);
        headerText_tv=findViewById(R.id.headerText_tv);
        back_iv=findViewById(R.id.back_iv);
        headerText_tv.setText("Upload Document");

    }


}
