package com.crescentek.cryptocurrency.activity.settings.twofactorauth;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.interfaces.ApiRequestListener;
import com.crescentek.cryptocurrency.network.GetRequest;
import com.crescentek.cryptocurrency.utility.BaseActivity;
import com.crescentek.cryptocurrency.utility.ConnectivityReceiver;
import com.crescentek.cryptocurrency.utility.NetworkUtility;

import org.json.JSONException;
import org.json.JSONObject;

public class RecoveryCodeStep3 extends BaseActivity implements ApiRequestListener{
    TextView headerText_tv, next_step_tv;
    ImageView back_iv;

    com.d.OtpView otp_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(RecoveryCodeStep3.this);
        setContentView(R.layout.recovery_code_step3);

        initAll();
        headerText_tv.setText("Setup two-factor authentication");
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

                String otpCode=otp_view.getText().toString();
                Log.d("Six_Digit_Pin>>>",otpCode);


                if(ConnectivityReceiver.isConnected())
                {
                    if (otpCode.length()==6) {
                        showCustomProgrssDialog(RecoveryCodeStep3.this);
                        new GetRequest(RecoveryCodeStep3.this,RecoveryCodeStep3.this,"Google Auth", NetworkUtility.VERIFY_2FA+""+otpCode);

                    }else {
                        showAlertDialog("Pin should be 6 digits");
                    }
                }
                else {
                    showAlertDialog(getResources().getString(R.string.dlg_nointernet));
                }

            }
        });

    }

    public void initAll(){
        next_step_tv = findViewById(R.id.next_step_tv);
        headerText_tv = findViewById(R.id.headerText_tv);
        back_iv = findViewById(R.id.back_iv);
        otp_view=findViewById(R.id.otp_view);

    }

    @Override
    public void onSuccess(String result, String type) throws JSONException {

        hideCustomProgrssDialog();
        Log.d("Response>>>",result);
        try {

            JSONObject jObj=new JSONObject(result);
            String verified=jObj.optString("verified");
            String status=jObj.optString("status");
            String message=jObj.optString("message");
            if(status.equalsIgnoreCase("true"))
            {
                if(verified.equalsIgnoreCase("true"))
                {
                    startActivity(new Intent(RecoveryCodeStep3.this, RecoverySuccess.class));
                    finish();
                }
            }
            else {
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
            }

        }catch (Exception e) {

        }

    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

    }


}
