package com.crescentek.cryptocurrency.activity.Exchangebuysell;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class ExchangePinActivity extends BaseActivity implements ApiRequestListener {

    TextView headerText_tv;
    Button btn_proceed;
    Map<String, String> buyObject, mapHeader;
    UserSessionManager sessionManager;
    ImageView back_iv;
    String crypro_id="";
    com.d.OtpView otp_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(ExchangePinActivity.this);
        setContentView(R.layout.activity_exchange_pin);
        sessionManager=new UserSessionManager(getApplicationContext());
        initViews();
        headerText_tv.setText("PIN");
        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(otp_view.getText().toString().length()==4)
                {
                    if(ConnectivityReceiver.isConnected())
                    {

                        Log.d("Pin>>>>",otp_view.getText().toString());
                        showCustomProgrssDialog(ExchangePinActivity.this);
                        buyObject =new HashMap<String, String>();
                        mapHeader =new HashMap<String, String>();
                        buyObject = signatureobject(otp_view.getText().toString());
                        Log.d("mapObj>>>", buyObject.toString()+"\n  "+NetworkUtility.ORDER_EXCHANGE);

                        mapHeader =headerObject();
                        new PostRequest(ExchangePinActivity.this,buyObject,mapHeader,ExchangePinActivity.this, NetworkUtility.ORDER_EXCHANGE,"Transaction");
                    }
                    else {
                        showAlertDialog(getResources().getString(R.string.dlg_nointernet));
                    }
                }

                else {

                    showAlertDialog("Four Digit Pin Required ! ");
                }

            }
        });

    }

    private Map<String,String> headerObject() {

        mapHeader.put("token",sessionManager.getValues(UserSessionManager.KEY_TOKEN));
        return mapHeader;
    }

    private Map<String,String> signatureobject(String pin) {

        buyObject.put("orderType",getIntent().getStringExtra("orderType"));
        buyObject.put("cryptoId",getIntent().getStringExtra("cryptoId"));
        buyObject.put("cryptoVal",getIntent().getStringExtra("cryptoVal"));
        buyObject.put("currencyVal",getIntent().getStringExtra("currencyVal"));
        buyObject.put("transact_pin",pin);

        return buyObject;
    }

    private void initViews() {

        otp_view=findViewById(R.id.otp_view);
        back_iv=(ImageView) findViewById(R.id.back_iv);
        btn_proceed = findViewById(R.id.btn_proceed);
        headerText_tv = findViewById(R.id.headerText_tv);
    }

    @Override
    public void onSuccess(String result, String type) throws JSONException {

        switch (type)
        {
            case "Transaction":
            {
                Log.d("TransactionResponse>>>",result);
                hideCustomProgrssDialog();
                JSONObject jsonObject=new JSONObject(result);
                String status=jsonObject.optString("status");
                String message=jsonObject.optString("message");
                if (status.equalsIgnoreCase("true"))
                {
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(getApplicationContext(), HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                else {

                    showAlertDialog(message);
                }
            }
            break;

        }

    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

        hideCustomProgrssDialog();
        showAlertDialog(responseMessage+"  "+responseCode);

    }
}
