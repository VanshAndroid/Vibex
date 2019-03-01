package com.crescentek.cryptocurrency.activity.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.activity.login_signup.HomeActivity;
import com.crescentek.cryptocurrency.interfaces.ApiRequestListener;
import com.crescentek.cryptocurrency.network.PostRequest;
import com.crescentek.cryptocurrency.utility.BaseActivity;
import com.crescentek.cryptocurrency.utility.ConnectivityReceiver;
import com.crescentek.cryptocurrency.utility.NetworkUtility;
import com.crescentek.cryptocurrency.utility.UserSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by R.Android on 22-06-2018.
 */

public class MobConfirmationActivity extends BaseActivity implements ApiRequestListener{

    private TextView submit_tv,headerText_tv,mobile_tv,resend_code;
    private ImageView back_iv;
    private EditText confirmation_ed;
    private String code="";

    private Map<String,String> mapobject,mapheader;
    UserSessionManager sessionManager;
    private Map<String,String> mapResend;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(MobConfirmationActivity.this);
        setContentView(R.layout.mob_confirmation_activity);

        submit_tv=findViewById(R.id.submit_tv);
        headerText_tv=findViewById(R.id.headerText_tv);
        back_iv=findViewById(R.id.back_iv);
        confirmation_ed=findViewById(R.id.confirmation_ed);
        mobile_tv=findViewById(R.id.mobile_tv);
        resend_code=findViewById(R.id.resend_code);

        mobile_tv.setText("Enter the confirmation code\nsent via SMS to\n "+getIntent().getStringExtra("phone"));

        mapobject =new HashMap<String, String>();
        mapheader=new HashMap<String, String>();
        mapResend=new HashMap<String, String>();
        sessionManager=new UserSessionManager(MobConfirmationActivity.this);

        headerText_tv.setText("Confirmation Code");
        code=getIntent().getStringExtra("code");
        confirmation_ed.setText(code);
        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        submit_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (confirmation_ed.getText().toString().trim().length()<1)
                {
                    confirmation_ed.setError("Confirmation code can't be blank");
                }
                else {

                    if(ConnectivityReceiver.isConnected())
                    {
                        showCustomProgrssDialog(MobConfirmationActivity.this);
                        ConnectivityReceiver.isConnected();
                        mapobject = signatureobject();
                        mapheader=headerObject();
                        new PostRequest(MobConfirmationActivity.this,mapobject,mapheader,MobConfirmationActivity.this, NetworkUtility.ACTIVATION_OTP,"Verify_Otp");
                    }
                    else {
                        showAlertDialog(getResources().getString(R.string.dlg_nointernet));
                    }



                }


            }
        });

        resend_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(ConnectivityReceiver.isConnected())
                {

                    mapResend=new HashMap<String, String>();
                    mapheader=new HashMap<String, String>();

                    showCustomProgrssDialog(MobConfirmationActivity.this);
                    ConnectivityReceiver.isConnected();
                    mapResend = resendData();
                    mapheader=headerObject();
                    new PostRequest(MobConfirmationActivity.this,mapobject,mapheader,MobConfirmationActivity.this, NetworkUtility.ACTIVATION_OTP,"Verify_Otp");
                }
                else {
                    showAlertDialog(getResources().getString(R.string.dlg_nointernet));
                }


            }
        });


    }


    private Map<String,String> resendData()
    {
        mapResend.put("phone",""+getIntent().getStringExtra("phone"));
        return mapResend;
    }

    private Map<String,String> signatureobject() {

        mapobject.put("code",""+confirmation_ed.getText().toString());

        return mapobject;
    }

    private Map<String,String> headerObject() {

        mapheader.put("token",sessionManager.getValues(UserSessionManager.KEY_TOKEN_PHONE));

        return mapheader;
    }

    @Override
    public void onSuccess(String result, String type) throws JSONException {

        hideCustomProgrssDialog();
        Log.d("Response>>>",result);
        try {
            JSONObject jObject=new JSONObject(result);
            String status=jObject.optString("status");
            String message=jObject.optString("message");
            if(status.equalsIgnoreCase("true"))
            {
                JSONObject jObj=jObject.optJSONObject("data");
                String isEmailVery=jObj.optString("isEmailVery");
                String isPhoneVery=jObj.optString("isPhoneVery");
                String isPinSet=jObj.optString("isPinSet");
                String isDocUploaded=jObj.optString("isDocUploaded");
                String isTwoFASet=jObj.optString("isTwoFASet");
                String user_level=jObj.optString("user_level");
                String user_level_id=jObj.optString("user_level_id");
                String token=jObj.optString("token");

                sessionManager.setValues(UserSessionManager.KEY_EMAIL_VERIFY,isEmailVery);
                sessionManager.setValues(UserSessionManager.KEY_PHONE_VERIFY,isPhoneVery);
                sessionManager.setValues(UserSessionManager.KEY_T_PIN_VERIFY,isPinSet);
                sessionManager.setValues(UserSessionManager.KEY_DOC_UPLOADED,isDocUploaded);
                sessionManager.setValues(UserSessionManager.KEY_USER_LEVEL,user_level);
                sessionManager.setValues(UserSessionManager.KEY_USER_LEVEL_ID,user_level_id);

                Intent intent=new Intent(getApplicationContext(),MobVerifiedActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }

        }catch (Exception e) {

        }

    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

        hideCustomProgrssDialog();
        Log.d("Response>>>",responseMessage+":::::"+responseCode);

    }
}
