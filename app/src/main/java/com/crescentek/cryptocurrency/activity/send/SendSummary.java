package com.crescentek.cryptocurrency.activity.send;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.activity.buysell.BuySellSummary;
import com.crescentek.cryptocurrency.comman.WalletRecord;
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

public class SendSummary extends BaseActivity implements ApiRequestListener,WalletListener{

    Button btn_proceed;
    private TextView btc_tv,ngn_tv,trans_fee,current_wallet_ngn,headerText_tv,crypto_bought_tv,tv_total_amt;
    UserSessionManager sessionManager;
    ImageView back_iv;

    Map<String, String> mapObject,mapHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(SendSummary.this);
        setContentView(R.layout.activity_summary);
        new WalletRecord(SendSummary.this,SendSummary.this);
        sessionManager=new UserSessionManager(getApplicationContext());
        mapObject=new HashMap<String, String>();
        mapHeader=new HashMap<String, String>();

        initViews();
        callFeesApi();

        headerText_tv.setText("Summary");
        crypto_bought_tv.setText(getIntent().getStringExtra("crypto_code") +" Being Send");
        ngn_tv.setText(getIntent().getStringExtra("crypto_code")+" "+getIntent().getStringExtra("crypto"));

        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(SendSummary.this,SendPinActivity.class);
                intent.putExtra("contact",getIntent().getStringExtra("contact"));
                intent.putExtra("amount",getIntent().getStringExtra("crypto"));
                intent.putExtra("cryptoId",getIntent().getStringExtra("cryptoId"));
                startActivity(intent);

            }
        });
        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void initViews()
    {
        btc_tv=findViewById(R.id.crypto_tv);
        ngn_tv=findViewById(R.id.totalamount_tv);
        trans_fee=findViewById(R.id.trans_fee);
        current_wallet_ngn=findViewById(R.id.current_wallet_balance);
        btn_proceed = findViewById(R.id.btn_proceed);
        headerText_tv=findViewById(R.id.headerText_tv);
        back_iv=findViewById(R.id.back_iv);
        crypto_bought_tv=findViewById(R.id.crypto_bought_tv);
        tv_total_amt=findViewById(R.id.tv_total_amt);
        tv_total_amt.setText("Total Crypto to be Paid");

    }

    public void callFeesApi()
    {

        if(ConnectivityReceiver.isConnected())
        {
            showCustomProgrssDialog(SendSummary.this);
            ConnectivityReceiver.isConnected();
            mapHeader=addHeader();
            mapObject=postData();
            new PostRequest(SendSummary.this,mapObject,mapHeader,SendSummary.this, NetworkUtility.TRANSACTION_FEE,"transaction_fee");
        }
        else {
            showAlertDialog(getResources().getString(R.string.dlg_nointernet));
        }


    }

    public Map<String,String> postData(){

        mapObject.put("type","cryptoSend");
        mapObject.put("amount",getIntent().getStringExtra("crypto"));
        mapObject.put("cryptoId",""+getIntent().getStringExtra("cryptoId"));
        mapObject.put("contact",getIntent().getStringExtra("contact"));
        Log.d("mapObject>>>",mapObject.toString());

        return mapObject;
    }
    public Map<String,String> addHeader()
    {
        mapHeader.put("token",sessionManager.getValues(UserSessionManager.KEY_TOKEN));
        Log.d("token>>>",sessionManager.getValues(UserSessionManager.KEY_TOKEN));
        return mapHeader;
    }

    @Override
    public void onSuccess(String result, String type) throws JSONException {

        hideCustomProgrssDialog();
        Log.d("Response>>>",result);
        try {
            JSONObject jObj=new JSONObject(result);
            String status=jObj.optString("status");
            if(status.equalsIgnoreCase("true"))
            {
                trans_fee.setText(jObj.optString("fees")+" "+jObj.optString("code"));
                String fees=jObj.optString("fees");
                double feesd=Double.parseDouble(fees);
                double totalAmt=Double.parseDouble(getIntent().getStringExtra("crypto"));
                double sendAmt=totalAmt-feesd;
                btc_tv.setText(sendAmt+" "+getIntent().getStringExtra("crypto_code"));

            }
        }catch (Exception e)
        {

        }

    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

    }

    @Override
    public void walletList(List<WalletN> walletList) throws Exception {

    }

    @Override
    public void cryptoList(List<CryptoM> cryptoList) throws Exception {

        for(int i=0;i<cryptoList.size();i++)
        {
            if(cryptoList.get(i).getCrypto_code().equalsIgnoreCase(getIntent().getStringExtra("crypto_code")))
            {
                current_wallet_ngn.setText(cryptoList.get(i).getCrypto_code()+" "+cryptoList.get(i).getCrypto_value());
            }
        }

    }

    @Override
    public void curreencyList(List<CurreencyM> curreencyList) throws Exception {

    }
}
