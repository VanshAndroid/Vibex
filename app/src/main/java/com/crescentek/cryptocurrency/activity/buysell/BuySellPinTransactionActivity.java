package com.crescentek.cryptocurrency.activity.buysell;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.crescentek.cryptocurrency.activity.login_signup.HomeActivity;
import com.crescentek.cryptocurrency.activity.send.SendPinActivity;
import com.crescentek.cryptocurrency.fragment.home.HomeCardFragment_1;
import com.crescentek.cryptocurrency.interfaces.ApiRequestListener;
import com.crescentek.cryptocurrency.interfaces.WalletListener;
import com.crescentek.cryptocurrency.model.CryptoM;
import com.crescentek.cryptocurrency.model.CurreencyM;
import com.crescentek.cryptocurrency.model.WalletN;
import com.crescentek.cryptocurrency.network.GetRequest;
import com.crescentek.cryptocurrency.network.PostRequest;
import com.crescentek.cryptocurrency.utility.BaseActivity;
import com.crescentek.cryptocurrency.utility.ConnectivityReceiver;
import com.crescentek.cryptocurrency.utility.NetworkUtility;
import com.crescentek.cryptocurrency.utility.UserSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by R.Android on 19-09-2018.
 */

public class BuySellPinTransactionActivity extends BaseActivity implements ApiRequestListener,WalletListener {

    TextView headerText_tv;
    Button btn_proceed;
    ImageView back_iv;
    Map<String, String> mapobject,mapheader;
    UserSessionManager sessionManager;
    com.d.OtpView otp_view;
    String crypto_rate;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(BuySellPinTransactionActivity.this);
        setContentView(R.layout.transaction_pin);

        sessionManager=new UserSessionManager(getApplicationContext());
        initAllViews();

        headerText_tv = findViewById(R.id.headerText_tv);
        headerText_tv.setText("PIN");

        callRateApi();

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
                        showCustomProgrssDialog(BuySellPinTransactionActivity.this);
                        ConnectivityReceiver.isConnected();
                        mapobject=new HashMap<String, String>();
                        mapheader=new HashMap<String, String>();
                        mapobject = postRequest(otp_view.getText().toString());
                        mapheader=headerObject();
                        if(getIntent().getStringExtra("type").equalsIgnoreCase("cryptoBuy")) {
                            new PostRequest(BuySellPinTransactionActivity.this, mapobject, mapheader, BuySellPinTransactionActivity.this, NetworkUtility.BUY_CRYPTO, "BuyCrypto");
                        }
                        else {
                            new PostRequest(BuySellPinTransactionActivity.this, mapobject, mapheader, BuySellPinTransactionActivity.this, NetworkUtility.SELL_CRYPTO, "SellCrypto");
                        }

                    }
                    else {
                        showAlertDialog(getResources().getString(R.string.dlg_nointernet));
                    }

                }


            }
        });

    }

    public void callRateApi()
    {

        if(ConnectivityReceiver.isConnected())
        {
            new GetRequest(BuySellPinTransactionActivity.this,BuySellPinTransactionActivity.this,"Rate", NetworkUtility.GET_RATE+"="+getIntent().getStringExtra("crypto_id"));
        }
        else{
            showAlertDialog(getResources().getString(R.string.dlg_nointernet));
        }

    }

    public Map<String,String> postRequest(String pin)
    {
        mapobject.put("crypto_id",getIntent().getStringExtra("crypto_id"));
        mapobject.put("crypto_value",getIntent().getStringExtra("crypto_value"));
        mapobject.put("transact_pin",pin);
        mapobject.put("rate",crypto_rate);
        Log.d("mapObject>>>",mapobject.toString());

        return mapobject;
    }
    public Map<String,String> headerObject()
    {
        mapheader.put("token",sessionManager.getValues(UserSessionManager.KEY_TOKEN));
        return mapheader;
    }


    public void initAllViews()
    {

        back_iv=findViewById(R.id.back_iv);
        btn_proceed = findViewById(R.id.btn_proceed);
        otp_view=findViewById(R.id.otp_view);
    }

    @Override
    public void onSuccess(String result, String type) throws JSONException {

        Log.d("ResponseTransaction>>>",result);
        hideCustomProgrssDialog();
        switch (type)
        {
            case "Rate":
                {
                    Log.d("Response>>>>",result);
                    JSONObject jObj=new JSONObject(result);
                    String status=jObj.optString("status");
                    if(status.equalsIgnoreCase("true"))
                    {
                        JSONObject jOb=jObj.optJSONObject("data");
                        crypto_rate=jOb.optString("crypto_rate");
                        Log.d("crypto_rate>>>",crypto_rate);
                        //String currency_code=jOb.optString("currency_code");
                    }
                }
                break;

            case "BuyCrypto":
                {
                    try {
                        Log.d("BuyCrypto>>>",result);
                        JSONObject jObj=new JSONObject(result);
                        boolean status=jObj.optBoolean("status");
                        String message=jObj.optString("message");
                        if(status)
                        {
                            String currCode=jObj.optString("currCode");
                            String cryptoCode=jObj.optString("cryptoCode");
                            String cryptoVal=jObj.optString("cryptoVal");
                            String currVal=jObj.optString("currVal");
                            String fees=jObj.optString("fees");
                            String credit_market_rate=jObj.optString("credit_market_rate");
                            String trx_ref=jObj.optString("trx_ref");
                            String crypto_Credited=jObj.optString("final_crypto");

                            double fee=Double.parseDouble(fees);
                            double credit_rate=Double.parseDouble(credit_market_rate);
                            double trans_fees=fee*credit_rate;

                            Intent intent=new Intent(BuySellPinTransactionActivity.this, BuySuccess.class);
                            intent.putExtra("currCode",currCode);
                            intent.putExtra("cryptoCode",cryptoCode);
                            intent.putExtra("cryptoVal",cryptoVal);
                            intent.putExtra("currVal",currVal);
                            intent.putExtra("fees",""+trans_fees);
                            intent.putExtra("type","buy");
                            intent.putExtra("trx_ref",trx_ref);
                            intent.putExtra("crypto_Credited",crypto_Credited);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                        }

                    }catch (Exception e)
                    {

                    }
                }
                break;
            case "SellCrypto":
                {
                    try {
                        Log.d("SellCrypto>>>",result);
                        JSONObject jObj=new JSONObject(result);
                        boolean status=jObj.optBoolean("status");
                        String message=jObj.optString("message");
                        if(status)
                        {
                            String currCode=jObj.optString("currCode");
                            String cryptoCode=jObj.optString("cryptoCode");
                            String cryptoVal=jObj.optString("cryptoVal");
                            String currVal=jObj.optString("currVal");
                            String fees=jObj.optString("fees");
                            String credit_market_rate=jObj.optString("credit_market_rate");
                            String trx_ref=jObj.optString("trx_ref");

                            double fee=Double.parseDouble(fees);
                            double credit_rate=Double.parseDouble(credit_market_rate);
                            double trans_fees=fee*credit_rate;

                            Intent intent=new Intent(BuySellPinTransactionActivity.this, BuySuccess.class);
                            intent.putExtra("currCode",currCode);
                            intent.putExtra("cryptoCode",cryptoCode);
                            intent.putExtra("cryptoVal",cryptoVal);
                            intent.putExtra("currVal",currVal);
                            intent.putExtra("fees",""+trans_fees);
                            intent.putExtra("type","sell");
                            intent.putExtra("trx_ref",trx_ref);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                        }

                    }catch (Exception e)
                    {

                    }
                }
                break;

        }


    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

        hideCustomProgrssDialog();
        Toast.makeText(getApplicationContext(),responseMessage,Toast.LENGTH_LONG).show();
    }


    @Override
    public void walletList(List<WalletN> walletList) throws Exception {

    }

    @Override
    public void cryptoList(List<CryptoM> cryptoList) throws Exception {

    }

    @Override
    public void curreencyList(List<CurreencyM> curreencyList) throws Exception {

    }
}
