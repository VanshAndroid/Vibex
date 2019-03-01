package com.crescentek.cryptocurrency.activity.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;


import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.activity.login_signup.HomeActivity;
import com.crescentek.cryptocurrency.utility.BaseActivity;

/**
 * Created by R.Android on 22-06-2018.
 */

public class VerifiedActivity extends BaseActivity {


    private Button done_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.verified_activity);
        done_btn=findViewById(R.id.done_btn);

        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(VerifiedActivity.this, HomeActivity.class);
                //intent.putExtra("position","6");
                startActivity(intent);

            }
        });

    }
}
