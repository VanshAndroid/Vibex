package com.crescentek.cryptocurrency.activity.withdraw;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.activity.verification_level_2.DocumentType;
import com.crescentek.cryptocurrency.utility.BaseActivity;

public class VerifyDocument extends BaseActivity  {

    TextView content_tv;
    Button btn_done;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checking_transaction);
        content_tv=findViewById(R.id.content_tv);
        btn_done=findViewById(R.id.btn_done);

        content_tv.setText(R.string.verify_doc);

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),DocumentType.class));
            }
        });
    }
}
