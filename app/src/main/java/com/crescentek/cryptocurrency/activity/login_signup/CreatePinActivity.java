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

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crescentek.cryptocurrency.R;
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
 * Created by R.Android on 18-06-2018.
 */

public class CreatePinActivity extends BaseActivity implements ApiRequestListener{

    private TextView headerText_tv;
    private ImageView back_iv;

    private Map<String,String> mapobject,mapheader;
    UserSessionManager sessionManager;
    private Button continue_btn;

    com.d.OtpView otp_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(CreatePinActivity.this);
        setContentView(R.layout.activity_pin);

        initView();

        mapobject =new HashMap<String, String>();
        mapheader=new HashMap<String, String>();
        sessionManager=new UserSessionManager(CreatePinActivity.this);

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ConnectivityReceiver.isConnected())
                {
                    if(otp_view.getText().toString().trim().length()==4)
                    {
                        String pin=otp_view.getText().toString();
                        showCustomProgrssDialog(CreatePinActivity.this);
                        ConnectivityReceiver.isConnected();
                        mapobject = signatureobject(pin);
                        mapheader=headerObject();
                        Log.d("ApiData>>>>",mapobject.toString()+"Header"+""+mapheader.toString()+">>>>"+NetworkUtility.TRANSACTION_PIN);
                        new PostRequest(CreatePinActivity.this,mapobject,mapheader,CreatePinActivity.this,NetworkUtility.TRANSACTION_PIN,"Create Transaction pin");
                    }
                    else {

                        showAlertDialog("Transaction pin should be 4 Digits.");
                    }
                }
                else {

                    showAlertDialog(getResources().getString(R.string.dlg_nointernet));

                }

            }
        });


    }


    public void initView()
    {

        continue_btn=findViewById(R.id.continue_btn);
        otp_view=findViewById(R.id.otp_view);
        headerText_tv=findViewById(R.id.headerText_tv);
        back_iv=findViewById(R.id.back_iv);
        headerText_tv.setText(R.string.create_pin);


    }

    public Map<String,String> signatureobject(String pin) {

        mapobject.put("pin",pin);
        Log.d("pin>>>",pin);

        return mapobject;
    }

    public Map<String,String> headerObject() {

        mapheader.put("token",sessionManager.getValues(UserSessionManager.KEY_TOKEN));
        Log.d(">>>token>>>",sessionManager.getValues(UserSessionManager.KEY_TOKEN));

        return mapheader;
    }

    @Override
    public void onSuccess(String result, String type) throws JSONException {

        Log.d("TransactionResponse>>>",result);
        hideCustomProgrssDialog();

        try {
            JSONObject jObj=new JSONObject(result);
            String status=jObj.optString("status");
            String message=jObj.optString("message");
            if(status.equalsIgnoreCase("true"))
            {
                JSONObject jObjData=jObj.optJSONObject("data");
                String isEmailVery=jObjData.optString("isEmailVery");
                String isPhoneVery=jObjData.optString("isPhoneVery");
                String isBvnVerified=jObjData.optString("isBvnVerified");
                String isPinSet=jObjData.optString("isPinSet");
                String isDocUploaded=jObjData.optString("isDocUploaded");
                String user_level=jObjData.optString("user_level");
                String user_level_id=jObjData.optString("user_level_id");

                sessionManager.setValues(UserSessionManager.KEY_EMAIL_VERIFY,isEmailVery);
                sessionManager.setValues(UserSessionManager.KEY_PHONE_VERIFY,isPhoneVery);
                sessionManager.setValues(UserSessionManager.KEY_BVN_VERIFY,isBvnVerified);
                sessionManager.setValues(UserSessionManager.KEY_T_PIN_VERIFY,isPinSet);
                sessionManager.setValues(UserSessionManager.KEY_DOC_UPLOADED,isDocUploaded);
                sessionManager.setValues(UserSessionManager.KEY_USER_LEVEL,user_level);
                sessionManager.setValues(UserSessionManager.KEY_USER_LEVEL_ID,user_level_id);
                Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                //startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            }
        }
        catch (Exception e) {

        }

    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

        Log.d("Response>>>",responseMessage);
        hideCustomProgrssDialog();

    }



}
