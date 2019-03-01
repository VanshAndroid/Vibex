package com.crescentek.cryptocurrency.activity.send;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.activity.buysell.BuySuccess;
import com.crescentek.cryptocurrency.interfaces.ApiRequestListener;
import com.crescentek.cryptocurrency.interfaces.WalletListener;
import com.crescentek.cryptocurrency.model.CryptoM;
import com.crescentek.cryptocurrency.model.CurreencyM;
import com.crescentek.cryptocurrency.model.WalletN;
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

public class SendPinActivity extends BaseActivity implements ApiRequestListener,WalletListener {

    TextView headerText_tv;
    Button btn_proceed;
    Map<String, String> mapobject,mapheader;
    UserSessionManager sessionManager;
    ImageView back_iv;
    String crypro_id="";
    com.d.OtpView otp_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(SendPinActivity.this);
        setContentView(R.layout.transaction_pin);
        sessionManager=new UserSessionManager(getApplicationContext());
        initAllViews();

        headerText_tv = findViewById(R.id.headerText_tv);
        headerText_tv.setText("PIN");



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

                        Log.d("Pin>>>>",otp_view.getText().toString());
                        showCustomProgrssDialog(SendPinActivity.this);
                        ConnectivityReceiver.isConnected();
                        mapobject=new HashMap<String, String>();
                        mapheader=new HashMap<String, String>();
                        mapobject = signatureobject(otp_view.getText().toString());
                        Log.d("mapObj>>>",mapobject.toString()+"\n  "+NetworkUtility.SEND_CRYPTO);

                        mapheader=headerObject();
                        new PostRequest(SendPinActivity.this,mapobject,mapheader,SendPinActivity.this, NetworkUtility.SEND_CRYPTO,"SendCrypto");
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

    public void initAllViews(){

        otp_view=findViewById(R.id.otp_view);
        back_iv=(ImageView) findViewById(R.id.back_iv);
        btn_proceed = findViewById(R.id.btn_proceed);

    }


    private Map<String,String> signatureobject(String pin) {

        mapobject.put("contact",""+ getIntent().getStringExtra("contact"));
        mapobject.put("cryptoId",""+getIntent().getStringExtra("cryptoId"));
        mapobject.put("amount",""+getIntent().getStringExtra("amount"));
        mapobject.put("transact_pin",pin);

        Log.d("mapobject>>>>",mapobject.toString());
        Log.d("amount>>>>",""+getIntent().getStringExtra("amount"));
        Log.d("transact_pin>>>>",pin);

        return mapobject;
    }

    private Map<String,String> headerObject() {

        mapheader.put("token",sessionManager.getValues(UserSessionManager.KEY_TOKEN));
        Log.d("token>>>>",sessionManager.getValues(UserSessionManager.KEY_TOKEN));
        return mapheader;
    }

    @Override
    public void onSuccess(String result, String type) throws JSONException {

        hideCustomProgrssDialog();
        try {

            Log.d("TransactionResponse>>>",result);
            JSONObject jObj=new JSONObject(result);
            String message=jObj.optString("message");
            String status=jObj.optString("status");
            if (status.equalsIgnoreCase("true"))
            {
                JSONObject jsonObject=jObj.optJSONObject("result");
                String users_id=jsonObject.optString("users_id");
                String currCode=jsonObject.optString("currCode");
                String cryptoCode=jsonObject.optString("cryptoCode");
                String fees=jsonObject.optString("fees");
                String cryptoVal=jsonObject.optString("cryptoVal");
                String statusC=jsonObject.optString("status");
                String contact=jsonObject.optString("contact");
                String trx_ref=jsonObject.optString("trx_ref");
                String cryptoSent=jsonObject.optString("cryptoSent");

                Intent intent=new Intent(SendPinActivity.this,SendSuccess.class);
                intent.putExtra("cryptoCode",cryptoCode);
                intent.putExtra("cryptoVal",cryptoVal);
                intent.putExtra("contact",contact);
                intent.putExtra("fees",fees);
                intent.putExtra("cryptoCode",cryptoCode);
                intent.putExtra("trx_ref",trx_ref);
                intent.putExtra("cryptoSent",cryptoSent);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
            else {
                showAlertDialog(message);
            }
        }
        catch (Exception e)
        {
            Log.d("Exception ",e.getMessage());
        }

    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

        hideCustomProgrssDialog();
        Log.d("responseMessage ",responseMessage);
    }



    @Override
    public void walletList(List<WalletN> walletList) throws Exception {

    }

    @Override
    public void cryptoList(List<CryptoM> cryptoList) throws Exception {

        for(int i=0;i<cryptoList.size();i++)
        {
            crypro_id=cryptoList.get(i).getCrypto_id();
        }
    }

    @Override
    public void curreencyList(List<CurreencyM> curreencyList) throws Exception {

    }



}
