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
 * Created by R.Android on 26-07-2018.
 */

public class ForgotPassword extends BaseActivity implements ApiRequestListener{

    private TextView email_hint_tv,resetPwd_tv,headerText_tv;
    private EditText email_ed;
    private View email_view;
    private ImageView back_iv;
    Map<String,String> mapObj;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(ForgotPassword.this);
        setContentView(R.layout.forgot_pwd);
        mapObj=new HashMap<String,String>();

        initView();

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        email_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(email_ed.getText().toString().length()<=1)
                {
                    email_hint_tv.setTextColor(getResources().getColor(R.color.dark_gray));
                    email_ed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_email_dark_gray_24dp, 0);
                    email_view.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                }
                else {

                    email_hint_tv.setTextColor(getResources().getColor(R.color.sky));
                    email_ed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_email_sky_blue_24dp, 0);
                    email_view.setBackgroundColor(getResources().getColor(R.color.sky));
                }

            }
        });

        resetPwd_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!validEmail(email_ed.getText().toString().trim())) {
                    email_ed.setError("A valid email id needed");
                }
                else {
                    callForgetPassword();
                }

            }
        });

    }

    public void callForgetPassword()
    {
        if(ConnectivityReceiver.isConnected())
        {
            showCustomProgrssDialog(ForgotPassword.this);
            ConnectivityReceiver.isConnected();
            mapObj=getPostData();
            new PostRequest(ForgotPassword.this,mapObj,ForgotPassword.this, NetworkUtility.RESET_PASSWORD,"RESET_PASSWORD");
        }
        else {
            showAlertDialog(getResources().getString(R.string.dlg_nointernet));
        }


    }

    public Map<String,String> getPostData()
    {
        mapObj.put("email",email_ed.getText().toString().trim());

        return mapObj;
    }

    public void initView()
    {
        email_hint_tv=findViewById(R.id.email_hint_tv);
        email_ed=findViewById(R.id.email_ed);
        email_view=findViewById(R.id.email_view);
        resetPwd_tv=findViewById(R.id.resetPwd_tv);
        back_iv=findViewById(R.id.back_iv);
        headerText_tv=findViewById(R.id.headerText_tv);

        headerText_tv.setText("Forgot Password");
    }

    private boolean validEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onSuccess(String result, String type) throws JSONException {

        hideCustomProgrssDialog();
        Log.d("ResetPassword>>>",result);
        try {

            JSONObject jObj=new JSONObject(result);
            String status=jObj.optString("status");
            String message=jObj.optString("message");
            if(status.equalsIgnoreCase("true"))
            {
                JSONObject dataObj=jObj.optJSONObject("data");
                String token=dataObj.optString("token");

                Intent intent=new Intent(getApplicationContext(),ForgetPasswordOtp.class);
                intent.putExtra("token",token);
                intent.putExtra("email",email_ed.getText().toString().trim());
                startActivity(intent);

            }else {
                showAlertDialog(message);
            }

        }catch (Exception e)
        {
            Log.d("ResetPassword>>>",e.getMessage());
        }

    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

        hideCustomProgrssDialog();
        showAlertDialog(responseMessage);
        Log.d("ResetPassword>>>",responseMessage+"   "+responseCode);

    }
}
