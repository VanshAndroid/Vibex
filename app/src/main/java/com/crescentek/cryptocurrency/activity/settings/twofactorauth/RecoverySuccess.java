package com.crescentek.cryptocurrency.activity.settings.twofactorauth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.activity.login_signup.HomeActivity;
import com.crescentek.cryptocurrency.utility.BaseActivity;
import com.crescentek.cryptocurrency.utility.UserSessionManager;

public class RecoverySuccess extends BaseActivity {

    UserSessionManager sessionManager;
    Button done_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(RecoverySuccess.this);
        setContentView(R.layout.recovery_success);

        sessionManager = new UserSessionManager(this);
        done_btn=findViewById(R.id.btn_done);

        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.setValues(UserSessionManager.KEY_VERIFICATION_LEVEL2,"Level -2(Verify)");
                Intent intent=new Intent(RecoverySuccess.this, HomeActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                intent.putExtra("position","6");
                startActivity(intent);
                finish();

            }
        });
    }
}
