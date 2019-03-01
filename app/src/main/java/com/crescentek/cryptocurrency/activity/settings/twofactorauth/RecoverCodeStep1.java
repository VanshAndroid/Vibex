package com.crescentek.cryptocurrency.activity.settings.twofactorauth;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.utility.BaseActivity;

public class RecoverCodeStep1 extends BaseActivity {

    TextView headerText_tv, next_step_tv;
    ImageView back_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(RecoverCodeStep1.this);
        setContentView(R.layout.recovery_code_step1);

        initAll();
        headerText_tv.setText("Recovery code");
        headerText_tv.setGravity(Gravity.CENTER);
        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        next_step_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecoverCodeStep1.this, RecoveryCodeStep2.class));

                Intent intent=new Intent(getApplicationContext(),RecoveryCodeStep2.class);
                intent.putExtra("secret",getIntent().getStringExtra("secret"));
                startActivity(intent);
            }
        });

    }

    public void initAll(){
        next_step_tv = findViewById(R.id.next_step_tv);
        headerText_tv = findViewById(R.id.headerText_tv);
        back_iv = findViewById(R.id.back_iv);
    }
}
