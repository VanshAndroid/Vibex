package com.crescentek.cryptocurrency.activity.login_signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crescentek.cryptocurrency.R;
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
import java.util.Map;

/**
 * Created by R.Android on 19-07-2018.
 */

public class SignInConfirmation extends BaseActivity implements ApiRequestListener{



    private TextView headerText_tv,email_txt_tv,resend_email;
    private ImageView back_iv;
    private Button continue_btn;
    private String TOKEN="";


    Map<String, String> mapobject,mapRest;
    Map<String, String> mapheader;
    String token;
    UserSessionManager sessionManager;
    com.d.OtpView otp_view;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(SignInConfirmation.this);
        setContentView(R.layout.sign_in_confirmation);

        mapobject = new HashMap<String, String>();
        mapheader=new HashMap<String, String>();
        mapRest=new HashMap<String, String>();
        sessionManager=new UserSessionManager(SignInConfirmation.this);
        TOKEN=sessionManager.getValues(UserSessionManager.KEY_TOKEN);
        initViews();

        System.out.println("TOKEN-->"+ sessionManager.getValues(sessionManager.KEY_TOKEN));
        Log.d("TOKEN>>>", sessionManager.getValues(sessionManager.KEY_TOKEN));

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( ConnectivityReceiver.isConnected())
                {
                    if(otp_view.getText().toString().trim().length()==4)
                    {
                        String pin=otp_view.getText().toString();
                        showCustomProgrssDialog(SignInConfirmation.this);

                        mapobject = signatureobject(pin);
                        mapheader=headerObject();
                        Log.d("MapObj>>>>",mapobject.toString()+":::"+mapheader.toString());
                        new PostRequest(SignInConfirmation.this,mapobject,mapheader,SignInConfirmation.this, NetworkUtility.EMAIL_ACTIVATION,"EmailActivation");
                    }
                    else {
                        showAlertDialog("Please Input 4 digits pin");

                    }
                }
                else {
                    showAlertDialog(getResources().getString(R.string.dlg_nointernet));
                }



            }
        });

        resend_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( ConnectivityReceiver.isConnected())
                {
                    showCustomProgrssDialog(SignInConfirmation.this);
                    ConnectivityReceiver.isConnected();
                    Log.d("RESENDURL>>",NetworkUtility.RESEND_EMAIL);
                    new GetRequest(SignInConfirmation.this,SignInConfirmation.this, "ResendEmail",NetworkUtility.RESEND_EMAIL);
                }
                else {
                    showAlertDialog(getResources().getString(R.string.dlg_nointernet));
                }



            }
        });

    }

    public void initViews()
    {
        otp_view=findViewById(R.id.otp_view);
        resend_email=findViewById(R.id.resend_email);
        continue_btn=findViewById(R.id.continue_btn);

        headerText_tv=findViewById(R.id.headerText_tv);
        email_txt_tv=findViewById(R.id.email_txt_tv);
        back_iv=findViewById(R.id.back_iv);
        headerText_tv.setText("Sign in Confirmation");
        email_txt_tv.setText("Please enter the 4-digit code that was sent to your"+" "+sessionManager.getValues(UserSessionManager.KEY_EMAIL));

    }

    private Map<String,String> postData() {

        return mapRest;
    }

    private Map<String,String> signatureobject(String pin) {

        mapobject.put("code",pin);
        Log.d("mapobject>>>",pin);

        return mapobject;
    }

    private Map<String,String> headerObject() {

        mapheader.put("token",TOKEN);
        Log.d("token>>>",TOKEN);
        return mapheader;
    }

    @Override
    public void onSuccess(String result, String type) throws JSONException {

        Log.d("SignInConfirmation>>>",result);
        hideCustomProgrssDialog();

        switch (type)
        {
            case "EmailActivation":
                {
                    try{
                        sessionManager.setValues(UserSessionManager.KEY_TOKEN,"");
                        JSONObject jObj=new JSONObject(result);
                        String status=jObj.optString("status");
                        String message=jObj.optString("message");
                        if(status.equalsIgnoreCase("true")) {

                            JSONObject jObjData=jObj.optJSONObject("data");
                            String isEmailVery=jObjData.optString("isEmailVery");
                            String isPhoneVery=jObjData.optString("isPhoneVery");
                            String isBvnVerified=jObjData.optString("isBvnVerified");
                            String isPinSet=jObjData.optString("isPinSet");
                            String isDocUploaded=jObjData.optString("isDocUploaded");
                            String isProfileUploaded=jObjData.optString("isProfileUploaded");
                            String isTwoFASet=jObjData.optString("isTwoFASet");
                            String user_level=jObjData.optString("user_level");
                            String user_level_id=jObjData.optString("user_level_id");
                            String token=jObjData.optString("token");

                            sessionManager.setValues(UserSessionManager.KEY_EMAIL_VERIFY,isEmailVery);
                            sessionManager.setValues(UserSessionManager.KEY_PHONE_VERIFY,isPhoneVery);
                            sessionManager.setValues(UserSessionManager.KEY_BVN_VERIFY,isBvnVerified);
                            sessionManager.setValues(UserSessionManager.KEY_T_PIN_VERIFY,isPinSet);
                            sessionManager.setValues(UserSessionManager.KEY_DOC_UPLOADED,isDocUploaded);
                            sessionManager.setValues(UserSessionManager.KEY_USER_LEVEL,user_level);
                            sessionManager.setValues(UserSessionManager.KEY_USER_LEVEL_ID,user_level_id);
                            sessionManager.setValues(UserSessionManager.KEY_TOKEN,token);
                            sessionManager.setValues(UserSessionManager.KEY_TWO_FACTER,isTwoFASet);
                            Intent intent=new Intent(getApplicationContext(),CreatePinActivity.class);
                            startActivity(intent);
                            finish();

                        }
                        else {
                            showAlertDialog(message);
                        }
                    }
                    catch (Exception e) {
                        Log.d("Exception",e.getMessage());
                    }
                }
                break;
            case "ResendEmail":
                {
                    Log.d("ResponseMessage>>>",result);
                    JSONObject jsonObject=new JSONObject(result);
                    String status=jsonObject.optString("status");
                    String message=jsonObject.optString("message");
                    if(status.equalsIgnoreCase("true")){

                        showAlertDialog(message);
                    }else {
                        showAlertDialog(message);
                    }

                }
                break;
        }


    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {
        Log.d("Response_error>>>",responseMessage);
        showAlertDialog(responseMessage);
        hideCustomProgrssDialog();
    }


}
