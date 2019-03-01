package com.crescentek.cryptocurrency.activity.login_signup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.interfaces.ApiRequestListener;
import com.crescentek.cryptocurrency.network.GetRequest;
import com.crescentek.cryptocurrency.utility.BaseActivity;
import com.crescentek.cryptocurrency.utility.ConnectivityReceiver;
import com.crescentek.cryptocurrency.utility.NetworkUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by R.Android on 09-10-2018.
 */

public class PrivacyPolicy extends BaseActivity implements ApiRequestListener{


    private ImageView back_iv;
    private TextView headerText_tv,privacy_policies_tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(PrivacyPolicy.this);
        setContentView(R.layout.privacy_policies);

        back_iv=findViewById(R.id.back_iv);
        headerText_tv=findViewById(R.id.headerText_tv);
        privacy_policies_tv=findViewById(R.id.privacy_policies_tv);

        headerText_tv.setText("Privacy and Policy");

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        callPrivacyPolicy();


    }

    public void callPrivacyPolicy()
    {
        if(ConnectivityReceiver.isConnected())
        {
            showCustomProgrssDialog(PrivacyPolicy.this);
            new GetRequest(PrivacyPolicy.this,PrivacyPolicy.this,"TermCondition", NetworkUtility.PRIVACY_POLICY);

        }
        else {
            showAlertDialog(getResources().getString(R.string.dlg_nointernet));
        }

    }


    @Override
    public void onSuccess(String result, String type) throws JSONException {

        hideCustomProgrssDialog();
        Log.d("TermCondition>>>",result);

        try {
            JSONObject jObj=new JSONObject(result);
            String status=jObj.optString("status");
            if(status.equalsIgnoreCase("true"))
            {
                JSONArray jsonArray=jObj.optJSONArray("data");
                if(jsonArray!=null)
                {
                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jObject=jsonArray.optJSONObject(i);
                        String blog_content=jObject.optString("blog_content");
                        privacy_policies_tv.setText(blog_content);

                    }
                }
            }
        }
        catch (Exception e) {

        }

    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

    }
}
