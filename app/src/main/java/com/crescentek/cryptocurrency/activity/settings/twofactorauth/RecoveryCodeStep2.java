package com.crescentek.cryptocurrency.activity.settings.twofactorauth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.utility.BaseActivity;

public class RecoveryCodeStep2 extends BaseActivity {

    TextView headerText_tv, next_step_tv,secret_code_tv;
    ImageView back_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(RecoveryCodeStep2.this);
        setContentView(R.layout.recovery_code_step2);

        initAll();

        secret_code_tv.setText(getIntent().getStringExtra("secret"));
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
                startActivity(new Intent(RecoveryCodeStep2.this, RecoveryCodeStep3.class));
            }
        });

        secret_code_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager)getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(secret_code_tv.getText());
                Toast.makeText(getApplicationContext(), "Copied", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void initAll(){
        next_step_tv = findViewById(R.id.next_step_tv);
        headerText_tv = findViewById(R.id.headerText_tv);
        secret_code_tv=findViewById(R.id.secret_code_tv);
        back_iv = findViewById(R.id.back_iv);
    }
}
