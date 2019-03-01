package com.crescentek.cryptocurrency.activity.settings.twofactorauth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.activity.login_signup.HomeActivity;
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

public class DisableTwoFactor extends BaseActivity implements ApiRequestListener {

    ImageView back_iv;
    TextView next_step_tv,headerText_tv;
    com.d.OtpView otp_view;
    Map<String,String> mapObj,mapHeader;
    UserSessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(DisableTwoFactor.this);
        setContentView(R.layout.disable_two_factor_disable);
        sessionManager=new UserSessionManager(DisableTwoFactor.this);

        mapObj=new HashMap<String, String>();
        mapHeader=new HashMap<String, String>();

        otp_view=findViewById(R.id.otp_view);
        next_step_tv=findViewById(R.id.next_step_tv);
        headerText_tv=findViewById(R.id.headerText_tv);
        back_iv=findViewById(R.id.back_iv);


        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        headerText_tv.setText("Two factor Disable");

        next_step_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ConnectivityReceiver.isConnected())
                {
                    if(otp_view.getText().toString().length()==6)
                    {
                        String pin=otp_view.getText().toString();
                        Log.d("OtpPin>>>",pin);
                        showCustomProgrssDialog(DisableTwoFactor.this);
                        new GetRequest(DisableTwoFactor.this,DisableTwoFactor.this,"Google Auth", NetworkUtility.VERIFY_2FA+""+pin);
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

    @Override
    public void onSuccess(String result, String type) throws JSONException {

        hideCustomProgrssDialog();
        Log.d("Response>>>",result);

        switch (type){

            case "Google Auth":
                {
                    try {

                        JSONObject jObj=new JSONObject(result);
                        String verified=jObj.optString("verified");
                        String status=jObj.optString("status");
                        String message=jObj.optString("message");
                        if(status.equalsIgnoreCase("true"))
                        {
                            callDisableApi();
                        }
                        else {
                            //Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                            showAlertDialog(message);
                        }

                    }catch (Exception e) {

                        Log.d("Error>>>>",e.getMessage());
                    }
                }
                break;
            case "TWO_FACTOR_DISABLE":
                {
                    Log.d("DisableResponse>>>>",result);
                    try{
                        JSONObject jObj=new JSONObject(result);
                        String status=jObj.optString("status");
                        String message=jObj.optString("message");
                        if(status.equalsIgnoreCase("true"))
                        {
                            boolean returnStatus=showAlertDialogWithType(message);
                            if(returnStatus)
                            {
                                sessionManager.setValues(UserSessionManager.KEY_TWO_FACTER,"false");
                                //sessionManager.getValues(UserSessionManager.KEY_TWO_FACTER);
                                Intent intent=new Intent(getApplicationContext(), HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }

                        }
                        else {
                            showAlertDialog(message);
                        }
                    }catch (Exception e)
                    {
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }



    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

    }




    public void callDisableApi(){

        if(ConnectivityReceiver.isConnected())
        {
            showCustomProgrssDialog(DisableTwoFactor.this);
            mapObj=getPostData();
            mapHeader=getToken();
            //new GetRequest(TwoFactorAuthentication.this,TwoFactorAuthentication.this,"TWO_FACTOR_DISABLE", NetworkUtility.TWO_FACTOR_DISABLE);
            new PostRequest(DisableTwoFactor.this,mapObj,mapHeader,DisableTwoFactor.this,NetworkUtility.TWO_FACTOR_DISABLE,"TWO_FACTOR_DISABLE");
        }
        else {
            showAlertDialog(getResources().getString(R.string.dlg_nointernet));
        }


    }

    public Map<String,String> getPostData()
    {
        mapObj.put("","");
        return mapObj;
    }
    public Map<String,String> getToken()
    {
        mapHeader.put("token",sessionManager.getValues(UserSessionManager.KEY_TOKEN));
        return mapHeader;
    }

}
