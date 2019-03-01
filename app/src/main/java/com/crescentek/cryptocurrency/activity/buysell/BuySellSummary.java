package com.crescentek.cryptocurrency.activity.buysell;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
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

/**
 * Created by R.Android on 19-09-2018.
 */

public class BuySellSummary extends BaseActivity implements ApiRequestListener,WalletListener{

    private TextView crypto_tv,totalamount_tv,trans_fee,current_wallet_balance,headerText_tv,crypto_bought_tv,tv_total_amt;
    private Button btn_proceed;
    private ImageView back_iv;
    Map<String, String> mapObject,mapHeader;
    UserSessionManager sessionManager;

    String CurrencyWalletValue,currency_code;
    String CryptoWalletValue,Crypto_code;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(BuySellSummary.this);
        setContentView(R.layout.activity_summary);
        new WalletRecord(BuySellSummary.this,BuySellSummary.this);
        sessionManager=new UserSessionManager(BuySellSummary.this);
        mapObject=new HashMap<String, String>();
        mapHeader=new HashMap<String, String>();

        initViews();
        callFeesApi();
        //setViewsValues();

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String crypto_code=getIntent().getStringExtra("crypto_code");
        crypto_tv.setText(getIntent().getStringExtra("crypto_value")+" "+getIntent().getStringExtra("crypto_code"));

        headerText_tv.setText("Summary");
        if(crypto_code.equalsIgnoreCase("null"))
        {

        }else {
            if(getIntent().getStringExtra("type").equalsIgnoreCase("cryptoSell"))
            {
                crypto_bought_tv.setText(crypto_code+" Sell");
            }
            else {
                crypto_bought_tv.setText(crypto_code+" Bought");
            }

        }

        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(BuySellSummary.this,BuySellPinTransactionActivity.class);
                intent.putExtra("crypto_value",getIntent().getStringExtra("crypto_value"));
                intent.putExtra("type",getIntent().getStringExtra("type"));
                intent.putExtra("crypto_id",getIntent().getStringExtra("crypto_id"));
                startActivity(intent);


            }
        });

    }

    public void initViews()
    {
        crypto_tv=findViewById(R.id.crypto_tv);
        totalamount_tv=findViewById(R.id.totalamount_tv);
        trans_fee=findViewById(R.id.trans_fee);
        current_wallet_balance=findViewById(R.id.current_wallet_balance);
        headerText_tv=findViewById(R.id.headerText_tv);
        btn_proceed=findViewById(R.id.btn_proceed);
        back_iv=findViewById(R.id.back_iv);
        headerText_tv=findViewById(R.id.headerText_tv);
        crypto_bought_tv=findViewById(R.id.crypto_bought_tv);
        tv_total_amt=findViewById(R.id.tv_total_amt);

    }


    public void callFeesApi()
    {
        if(ConnectivityReceiver.isConnected())
        {
            showCustomProgrssDialog(BuySellSummary.this);
            ConnectivityReceiver.isConnected();
            mapHeader=addHeader();
            mapObject=postData();
            Log.d("mapObject>>>",mapObject.toString());
            new PostRequest(BuySellSummary.this,mapObject,mapHeader,BuySellSummary.this, NetworkUtility.TRANSACTION_FEE,"transaction_fee");
        }
        else {
            showAlertDialog(getResources().getString(R.string.dlg_nointernet));
        }

    }

    public Map<String,String> postData(){

        mapObject.put("type",getIntent().getStringExtra("type"));
        mapObject.put("amount",getIntent().getStringExtra("crypto_value"));
        mapObject.put("cryptoId",""+getIntent().getStringExtra("crypto_id"));

        Log.d("Data>>>",getIntent().getStringExtra("type")+":::"+getIntent().getStringExtra("crypto_value")+":::");

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
            boolean status=jObj.optBoolean("status");
            if(status)
            {
                trans_fee.setText(jObj.optString("fees")+" "+jObj.optString("code"));
            }

        }catch (Exception e)
        {
            Log.d("Exception>>>",e.getMessage());
        }


    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

        hideCustomProgrssDialog();
        Log.d("Response>>>",responseMessage);


    }


    @Override
    public void walletList(List<WalletN> walletList) throws Exception {


    }

    @Override
    public void cryptoList(List<CryptoM> cryptoList) throws Exception {

        for (int i=0;i<cryptoList.size();i++)
        {
            if(cryptoList.get(i).getCrypto_id().equalsIgnoreCase(getIntent().getStringExtra("crypto_id")))
            {
                CryptoWalletValue=cryptoList.get(i).getCrypto_value();
                Crypto_code=cryptoList.get(i).getCrypto_code();

            }
        }

            current_wallet_balance.setText(CryptoWalletValue + " " + Crypto_code);


    }

    @Override
    public void curreencyList(List<CurreencyM> curreencyList) throws Exception {

        CurrencyWalletValue=curreencyList.get(0).getCurrency_value();
        currency_code=curreencyList.get(0).getCurrency_code();

        current_wallet_balance.setText(CurrencyWalletValue + " " + currency_code);
        totalamount_tv.setText(getIntent().getStringExtra("currency_payable")+" "+currency_code);

    }
}
