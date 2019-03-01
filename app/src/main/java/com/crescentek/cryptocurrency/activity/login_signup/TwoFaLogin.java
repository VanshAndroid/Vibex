package com.crescentek.cryptocurrency.activity.login_signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.activity.send.SendPinActivity;
import com.crescentek.cryptocurrency.activity.settings.twofactorauth.RecoveryCodeStep3;
import com.crescentek.cryptocurrency.activity.settings.twofactorauth.RecoverySuccess;
import com.crescentek.cryptocurrency.fragment.navigation.HomeFragment;
import com.crescentek.cryptocurrency.interfaces.ApiRequestListener;
import com.crescentek.cryptocurrency.network.GetRequest;
import com.crescentek.cryptocurrency.network.PostRequest;
import com.crescentek.cryptocurrency.utility.BaseActivity;
import com.crescentek.cryptocurrency.utility.ConnectivityReceiver;
import com.crescentek.cryptocurrency.utility.NetworkUtility;
import com.crescentek.cryptocurrency.utility.UserSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by R.Android on 28-09-2018.
 */

public class TwoFaLogin extends BaseActivity implements ApiRequestListener {


    ImageView back_iv;
    TextView next_step_tv,headerText_tv;
    com.d.OtpView otp_view;

    UserSessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(TwoFaLogin.this);
        setContentView(R.layout.two_fa_login);
        sessionManager=new UserSessionManager(TwoFaLogin.this);

        initAllViews();

        headerText_tv.setText("Two factor authentication");

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        next_step_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ConnectivityReceiver.isConnected())
                {
                    if(otp_view.getText().toString().length()==6)
                    {
                        String pin=otp_view.getText().toString();
                        Log.d("OtpPin>>>",pin);
                        showCustomProgrssDialog(TwoFaLogin.this);
                        new GetRequest(TwoFaLogin.this,TwoFaLogin.this,"Google Auth", NetworkUtility.VERIFY_2FA+""+pin);
                    }
                    else {
                        showAlertDialog("Please Input 6 digits pin Code !!!");
                    }

                }
                else {
                    showAlertDialog(getResources().getString(R.string.dlg_nointernet));
                }

            }
        });


    }

    public void initAllViews(){

        otp_view=findViewById(R.id.otp_view);
        back_iv=findViewById(R.id.back_iv);
        headerText_tv=findViewById(R.id.headerText_tv);
        next_step_tv=findViewById(R.id.next_step_tv);

    }

    @Override
    public void onSuccess(String result, String type) throws JSONException {


        hideCustomProgrssDialog();
        Log.d("TFAResponse>>>",result);
        try {

            JSONObject jObj=new JSONObject(result);
            String verified=jObj.optString("verified");
            String status=jObj.optString("status");
            String message=jObj.optString("message");
            if(status.equalsIgnoreCase("true"))
            {

                    sessionManager.setValues(UserSessionManager.KEY_EMAIL,getIntent().getStringExtra("KEY_EMAIL"));
                    sessionManager.setValues(UserSessionManager.KEY_EMAIL_VERIFY,getIntent().getStringExtra("KEY_EMAIL_VERIFY"));
                    sessionManager.setValues(UserSessionManager.KEY_PHONE_VERIFY,getIntent().getStringExtra("KEY_PHONE_VERIFY"));
                    sessionManager.setValues(UserSessionManager.KEY_BVN_VERIFY,getIntent().getStringExtra("KEY_BVN_VERIFY"));
                    sessionManager.setValues(UserSessionManager.KEY_T_PIN_VERIFY,getIntent().getStringExtra("KEY_T_PIN_VERIFY"));
                    sessionManager.setValues(UserSessionManager.KEY_DOC_UPLOADED,getIntent().getStringExtra("KEY_DOC_UPLOADED"));
                    sessionManager.setValues(UserSessionManager.KEY_TWO_FACTER,getIntent().getStringExtra("KEY_TWO_FACTER"));
                    sessionManager.setValues(UserSessionManager.KEY_USER_LEVEL,getIntent().getStringExtra("KEY_USER_LEVEL"));
                    sessionManager.setValues(UserSessionManager.KEY_USER_LEVEL_ID,getIntent().getStringExtra("KEY_USER_LEVEL_ID"));
                    sessionManager.setValues(UserSessionManager.KEY_TOKEN,getIntent().getStringExtra("KEY_TOKEN"));
                    sessionManager.setValues(UserSessionManager.KEY_FIRST_NAME,getIntent().getStringExtra("KEY_FIRST_NAME"));
                    sessionManager.setValues(UserSessionManager.KEY_LAST_NAME,getIntent().getStringExtra("KEY_LAST_NAME"));
                    sessionManager.setValues(UserSessionManager.KEY_PHONE,getIntent().getStringExtra("KEY_PHONE"));

                    Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

            }
            else {
                //Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                showAlertDialog(message);
            }

        }catch (Exception e) {

        }

    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

        hideCustomProgrssDialog();

        Log.d("ErrorResponse>>>",responseMessage+"  ResponseCode : "+responseCode);

    }

}
