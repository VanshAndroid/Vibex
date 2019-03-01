package com.crescentek.cryptocurrency.activity.verification_level_2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.utility.BaseActivity;

/**
 * Created by R.Android on 26-07-2018.
 */

public class Verification_2_3 extends BaseActivity {


    private TextView done_tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiantBlue(Verification_2_3.this);
        setContentView(R.layout.verification_2_3);

        done_tv=findViewById(R.id.done_tv);

        done_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),UploadDocumentActivity.class));

            }
        });

    }
}
