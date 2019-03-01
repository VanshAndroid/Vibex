package com.crescentek.cryptocurrency.activity.withdraw;

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
import com.crescentek.cryptocurrency.activity.buysell.BuySellPinTransactionActivity;
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
 * Created by R.Android on 22-10-2018.
 */

public class TransactionPin extends BaseActivity implements ApiRequestListener{

    TextView headerText_tv;

    Button btn_proceed;
    ImageView back_iv;
    Map<String, String> mapobject,mapheader;
    UserSessionManager sessionManager;

    com.d.OtpView otp_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(TransactionPin.this);
        setContentView(R.layout.transaction_pin);


        sessionManager=new UserSessionManager(getApplicationContext());
        initAllViews();

        headerText_tv = findViewById(R.id.headerText_tv);
        headerText_tv.setText("Transaction Pin");



        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                        showCustomProgrssDialog(TransactionPin.this);

                        mapobject=new HashMap<String, String>();
                        mapheader=new HashMap<String, String>();
                        mapobject = postRequest(otp_view.getText().toString());
                        mapheader=headerObject();

                        new PostRequest(TransactionPin.this, mapobject, mapheader, TransactionPin.this, NetworkUtility.FUND_WITHDRAW, "FUND_WITHDRAW");
                    }
                    else {
                        showAlertDialog(getResources().getString(R.string.dlg_nointernet));
                    }

                }

                else {

                }

            }
        });


    }

    public void initAllViews()
    {
        otp_view=findViewById(R.id.otp_view);
        back_iv=findViewById(R.id.back_iv);
        btn_proceed = findViewById(R.id.btn_proceed);
    }

    public Map<String,String> postRequest(String pin)
    {
        mapobject.put("trx_ref","Withdraw");
        mapobject.put("users_bank_id",getIntent().getStringExtra("users_bank_id"));
        mapobject.put("currency_value",getIntent().getStringExtra("currency_value"));
        mapobject.put("transact_pin",pin);

        return mapobject;
    }
    public Map<String,String> headerObject()
    {
        mapheader.put("token",sessionManager.getValues(UserSessionManager.KEY_TOKEN));
        return mapheader;
    }

    @Override
    public void onSuccess(String result, String type) throws JSONException {

        hideCustomProgrssDialog();
        Log.d("TransactionResponse>>>",result);

        try {
            JSONObject jsonObject=new JSONObject(result);
            String status=jsonObject.optString("status");
            String message=jsonObject.optString("message");
            if(status.equalsIgnoreCase("true")){

                JSONObject jObj=jsonObject.optJSONObject("data");
                String fees=jObj.optString("fees");
                String amount=jObj.optString("amount");
                String withdrable_amount=jObj.optString("withdrable_amount");
                String bank_account=jObj.optString("bank_account");

                Intent intent=new Intent(getApplicationContext(),WithdrawSuccess.class);
                intent.putExtra("withdrable_amount",withdrable_amount);
                intent.putExtra("fees",fees);
                intent.putExtra("amount",amount);
                intent.putExtra("bank_account",bank_account);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        }
        catch (Exception e) {

        }


    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

        hideCustomProgrssDialog();
        Log.d("TransactionResponse>>>",responseMessage);

    }


}
