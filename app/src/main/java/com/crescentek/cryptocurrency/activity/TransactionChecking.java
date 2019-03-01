package com.crescentek.cryptocurrency.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.activity.settings.MobileNumberVerificationActivity;
import com.crescentek.cryptocurrency.activity.settings.VerifyPersonalDetailActivity;
import com.crescentek.cryptocurrency.activity.verification_level_2.DocumentType;
import com.crescentek.cryptocurrency.fragment.navigation.SettingFragment;
import com.crescentek.cryptocurrency.interfaces.ApiRequestListener;
import com.crescentek.cryptocurrency.network.GetRequest;
import com.crescentek.cryptocurrency.utility.BaseActivity;
import com.crescentek.cryptocurrency.utility.ConnectivityReceiver;
import com.crescentek.cryptocurrency.utility.NetworkUtility;
import com.crescentek.cryptocurrency.utility.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by R.Android on 10-10-2018.
 */

public class TransactionChecking extends BaseActivity implements ApiRequestListener{

    ImageView back_iv;
    TextView content_tv;
    private Button btn_done;
    UserSessionManager sessionManager;
    String phone,phonecode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checking_transaction);

        sessionManager=new UserSessionManager(TransactionChecking.this);

        initViews();

        callProfileApi();

        if(sessionManager.getValues(UserSessionManager.KEY_LEVEL_NAME).equalsIgnoreCase("Level0"))
        {
            content_tv.setText(R.string.verify_mobile);
        }
        else {
            content_tv.setText(R.string.verify_identity);
        }

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if(sessionManager.getValues(UserSessionManager.KEY_LEVEL_NAME).equalsIgnoreCase("Level0"))
                    {
                        Intent intent = new Intent(getApplicationContext(), MobileNumberVerificationActivity.class);
                        intent.putExtra("mobile", phone);
                        intent.putExtra("phonecode", phonecode);
                        startActivity(intent);
                    }
                    if(sessionManager.getValues(UserSessionManager.KEY_LEVEL_NAME).equalsIgnoreCase("Level1"))
                    {
                        startActivity(new Intent(getApplicationContext(), DocumentType.class));
                    }
                    else {

                    }

            }
        });


    }

    public void initViews()
    {
        back_iv=findViewById(R.id.back_iv);
        content_tv=findViewById(R.id.content_tv);
        btn_done=findViewById(R.id.btn_done);
    }

    public void callProfileApi()
    {
        if(ConnectivityReceiver.isConnected())
        {
            new GetRequest(TransactionChecking.this,TransactionChecking.this,"PROFILE_DETAILS", NetworkUtility.PROFILE_DETAILS);
        }
        else {
            showAlertDialog(getResources().getString(R.string.dlg_nointernet));
        }

    }


    @Override
    public void onSuccess(String result, String type) throws JSONException {

        Log.d("PROFILEResponse>>>",result);
        {
            Log.d("PROFILE_DETAILS>>>",result);
            JSONObject jObj=new JSONObject(result);
            String status=jObj.optString("status");
            String message=jObj.optString("message");
            if(status.equalsIgnoreCase("success"))
            {
                JSONArray jsonArray=jObj.optJSONArray("data");
                if(jsonArray!=null)
                {
                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jOb=jsonArray.optJSONObject(i);
                        phone=jOb.optString("phone");
                        phonecode=jOb.optString("phonecode");

                        sessionManager.setValues(UserSessionManager.KEY_PHONE_CODE,phonecode);
                        sessionManager.setValues(UserSessionManager.KEY_PHONE,phone);

                    }
                }
            }

        }

    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

    }
}
