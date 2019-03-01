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

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.interfaces.ApiRequestListener;
import com.crescentek.cryptocurrency.network.PostRequest;
import com.crescentek.cryptocurrency.utility.BaseActivity;
import com.crescentek.cryptocurrency.utility.ConnectivityReceiver;
import com.crescentek.cryptocurrency.utility.NetworkUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by R.Android on 05-10-2018.
 */

public class ForgetPasswordOtp extends BaseActivity implements ApiRequestListener {

    EditText otpET1, otpET2, otpET3, otpET4;
    ImageView back_iv;
    TextView headerText_tv,email_txt_tv;
    Button continue_btn;

    Map<String,String> mapPost,mapHeader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(ForgetPasswordOtp.this);
        setContentView(R.layout.forget_pwd_otp);
        mapPost=new HashMap<String, String>();
        mapHeader=new HashMap<String, String>();

        initViews();

        otpET1.addTextChangedListener(new GenericTextWatcher(otpET1));
        otpET2.addTextChangedListener(new GenericTextWatcher(otpET2));
        otpET3.addTextChangedListener(new GenericTextWatcher(otpET3));
        otpET4.addTextChangedListener(new GenericTextWatcher(otpET4));

        headerText_tv.setText("Reset Password");
        String token=getIntent().getStringExtra("token");
        Log.d("VerifyToken",token);
        email_txt_tv.setText(getIntent().getStringExtra("email"));

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(otpET1.getText().toString().trim().length()<0)
                {
                    otpET1.setError("Code Missing");
                }
                else if (otpET2.getText().toString().trim().length()<0)
                {
                    otpET2.setError("Code Missing");
                }
                else if (otpET3.getText().toString().trim().length()<0)
                {
                    otpET3.setError("Code Missing");
                }
                else if (otpET4.getText().toString().trim().length()<0)
                {
                    otpET4.setError("Code Missing");
                }
                else {

                    String pin=otpET1.getText().toString()+""+otpET2.getText().toString()+""+otpET3.getText().toString()+""+otpET4.getText().toString();
                    callOtpResetApi(pin);

                }
            }
        });
    }

    public void initViews()
    {
        otpET1 = (EditText)findViewById(R.id.pin1_ed);
        otpET2 = (EditText)findViewById(R.id.pin2_ed);
        otpET3 = (EditText)findViewById(R.id.pin3_ed);
        otpET4 = (EditText)findViewById(R.id.pin4_ed);
        back_iv=findViewById(R.id.back_iv);
        headerText_tv=findViewById(R.id.headerText_tv);
        email_txt_tv=findViewById(R.id.email_txt_tv);
        continue_btn=findViewById(R.id.continue_btn);
    }

    public void callOtpResetApi(String pin)
    {

        if(ConnectivityReceiver.isConnected())
        {
            showCustomProgrssDialog(ForgetPasswordOtp.this);
            ConnectivityReceiver.isConnected();
            mapPost=postBody(pin);
            mapHeader=postHeader();

            Log.d("PostData>>>",mapPost+"   "+mapHeader+"\n"+NetworkUtility.VERIFY_CODE);
            new PostRequest(ForgetPasswordOtp.this,mapPost,mapHeader, ForgetPasswordOtp.this,NetworkUtility.VERIFY_CODE,"VERIFY_CODE");

        }
        else {
            showAlertDialog(getResources().getString(R.string.dlg_nointernet));
        }


    }
    public Map<String,String> postBody(String pin)
    {
        mapPost.put("code",pin);
        return mapPost;
    }

    public Map<String,String> postHeader()
    {
        mapHeader.put("token",getIntent().getStringExtra("token"));
        return mapHeader;
    }
    @Override
    public void onSuccess(String result, String type) throws JSONException {

        hideCustomProgrssDialog();
        Log.d("OtpResponse>>>",result);
        try {

            JSONObject jObj=new JSONObject(result);
            String status=jObj.optString("status");
            String message=jObj.optString("message");
            if(status.equalsIgnoreCase("true"))
            {
                boolean alertReturn=showAlertDialogWithType(message);
                String token=jObj.optString("token");
                if (alertReturn)
                {
                    Intent intent=new Intent(getApplicationContext(),PasswordReset.class);
                    intent.putExtra("token",token);
                    startActivity(intent);
                    finish();
                }


            }
            else {
                showAlertDialog(message);
            }

        }catch (Exception e) {

        }

    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

        hideCustomProgrssDialog();
        Log.d("OtpResponse>>>",responseMessage+"  "+responseCode);

    }


    public class GenericTextWatcher implements TextWatcher
    {
        private View view;
        private GenericTextWatcher(View view)
        {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            switch(view.getId())
            {

                case R.id.pin1_ed:
                    if(text.length()==1)
                        otpET2.requestFocus();
                    break;
                case R.id.pin2_ed:
                    if(text.length()==1)
                        otpET3.requestFocus();
                    break;
                case R.id.pin3_ed:
                    if(text.length()==1)
                        otpET4.requestFocus();
                    break;
                case R.id.pin4_ed:
                    break;

            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub

            String text = arg0.toString();
            switch(view.getId())
            {

                case R.id.pin2_ed:
                    if(text.length()==0)
                        otpET1.requestFocus();
                    break;
                case R.id.pin3_ed:
                    if(text.length()==0)
                        otpET2.requestFocus();
                    break;
                case R.id.pin4_ed:
                    if(text.length()==0)
                        otpET3.requestFocus();
                    break;

                case R.id.pin1_ed:
                    break;
            }

        }
    }



}
