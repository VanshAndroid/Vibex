package com.crescentek.cryptocurrency.activity.settings.twofactorauth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.activity.login_signup.HomeActivity;
import com.crescentek.cryptocurrency.activity.settings.MobileNumberVerificationActivity;
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

public class TwoFactorAuthentication extends BaseActivity implements ApiRequestListener{

    TextView headerText_tv, next_step_tv;
    ImageView back_iv;
    UserSessionManager sessionManager;
    Map<String,String> mapObj,mapHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(TwoFactorAuthentication.this);
        setContentView(R.layout.twofacter_authentication);

        initAll();

        mapObj=new HashMap<String, String>();
        mapHeader=new HashMap<String, String>();
        sessionManager=new UserSessionManager(getApplicationContext());
        headerText_tv.setText("Two-factor authentication");

        if(sessionManager.getValues(UserSessionManager.KEY_TWO_FACTER).equalsIgnoreCase("Disable"))
        {
            next_step_tv.setText("DISABLE");

        }
        if(sessionManager.getValues(UserSessionManager.KEY_TWO_FACTER).equalsIgnoreCase("Disable"))
        {
            next_step_tv.setText("DISABLE");

        }
        else {
            next_step_tv.setText("ENABLED");
        }

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        next_step_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String enableDisable=sessionManager.getValues(UserSessionManager.KEY_TWO_FACTER);
                Log.d("EnableDisable>>>",enableDisable);

                if(sessionManager.getValues(UserSessionManager.KEY_TWO_FACTER).equalsIgnoreCase("Disable"))
                {
                    startActivity(new Intent(getApplicationContext(),DisableTwoFactor.class));
                    /*next_step_tv.setText("DISABLE");
                    showCustomProgrssDialog(TwoFactorAuthentication.this);
                    mapObj=getPostData();
                    mapHeader=getToken();
                    //new GetRequest(TwoFactorAuthentication.this,TwoFactorAuthentication.this,"TWO_FACTOR_DISABLE", NetworkUtility.TWO_FACTOR_DISABLE);
                    new PostRequest(TwoFactorAuthentication.this,mapObj,mapHeader,TwoFactorAuthentication.this,NetworkUtility.TWO_FACTOR_DISABLE,"TWO_FACTOR_DISABLE");*/

                }
                else {
                    next_step_tv.setText("ENABLED");

                    if(ConnectivityReceiver.isConnected())
                    {
                        showCustomProgrssDialog(TwoFactorAuthentication.this);
                        new GetRequest(TwoFactorAuthentication.this,TwoFactorAuthentication.this,"TWO_FACTOR", NetworkUtility.TWO_FACTOR);
                    }
                    else {
                        showAlertDialog(getResources().getString(R.string.dlg_nointernet));
                    }


                }




            }
        });

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
    public void initAll(){
        next_step_tv = findViewById(R.id.next_step_tv);
        headerText_tv = findViewById(R.id.headerText_tv);
        back_iv = findViewById(R.id.back_iv);
    }

    @Override
    public void onSuccess(String result, String type) throws JSONException {

        Log.d("Result>>>>",result);
        hideCustomProgrssDialog();

        switch (type)
        {
            case "TWO_FACTOR":
                {
                    try {
                        JSONObject jObj=new JSONObject(result);
                        String status=jObj.optString("status");
                        String message=jObj.optString("message");
                        if (status.equalsIgnoreCase("true"))
                        {
                            String secret=jObj.optString("secret");
                            Intent intent=new Intent(getApplicationContext(),RecoverCodeStep1.class);
                            intent.putExtra("secret",secret);
                            startActivity(intent);
                        }else {
                            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG);
                        }

                    }catch (Exception e) {

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
}
