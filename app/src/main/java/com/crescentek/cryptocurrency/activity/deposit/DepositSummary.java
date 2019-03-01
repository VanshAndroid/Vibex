package com.crescentek.cryptocurrency.activity.deposit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.activity.buysell.BuySellSummary;
import com.crescentek.cryptocurrency.comman.WalletRecord;
import com.crescentek.cryptocurrency.fragment.navigation.AccountsFragment;
import com.crescentek.cryptocurrency.interfaces.ApiRequestListener;
import com.crescentek.cryptocurrency.interfaces.WalletListener;
import com.crescentek.cryptocurrency.model.CryptoM;
import com.crescentek.cryptocurrency.model.CurreencyM;
import com.crescentek.cryptocurrency.model.WalletN;
import com.crescentek.cryptocurrency.network.PostRequest;
import com.crescentek.cryptocurrency.paystack.MainActivityPayStack;
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
 * Created by R.Android on 26-09-2018.
 */

public class DepositSummary extends BaseActivity implements ApiRequestListener,WalletListener{

    TextView totalamount_tv,trans_fee,current_wallet_balance,headerText_tv;
    Button btn_proceed;
    UserSessionManager sessionManager;
    Map<String, String> mapObject,mapHeader;
    ImageView back_iv;

    private List<WalletN> walletNList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(DepositSummary.this);
        setContentView(R.layout.deposit_summary);

        initViews();
        new WalletRecord(DepositSummary.this,DepositSummary.this);

        sessionManager=new UserSessionManager(DepositSummary.this);
        mapObject=new HashMap<String, String>();
        mapHeader=new HashMap<String, String>();
        callFeesApi();
        headerText_tv.setText("Summary");
        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), MainActivityPayStack.class).putExtra("iAmount", getIntent().getStringExtra("iAmount")));

            }
        });

    }

    public void initViews()
    {
        totalamount_tv=findViewById(R.id.totalamount_tv);
        trans_fee=findViewById(R.id.trans_fee);
        current_wallet_balance=findViewById(R.id.current_wallet_balance);
        btn_proceed=findViewById(R.id.btn_proceed);
        headerText_tv=findViewById(R.id.headerText_tv);
        back_iv=findViewById(R.id.back_iv);

        totalamount_tv.setText(getIntent().getStringExtra("iAmount")+" NGN");
    }


    public void callFeesApi()
    {

        if(ConnectivityReceiver.isConnected())
        {
            showCustomProgrssDialog(DepositSummary.this);
            ConnectivityReceiver.isConnected();
            mapHeader=addHeader();
            mapObject=postData();
            new PostRequest(DepositSummary.this,mapObject,mapHeader,DepositSummary.this, NetworkUtility.TRANSACTION_FEE,"transaction_fee");
        }
        else {
            showAlertDialog(getResources().getString(R.string.dlg_nointernet));
        }




    }

    public Map<String,String> postData(){

        mapObject.put("type","deposit");
        mapObject.put("amount",getIntent().getStringExtra("iAmount"));
        mapObject.put("cryptoId",""+sessionManager.getValues(UserSessionManager.CRYPTO_ID));

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
        Log.d("Fees>>>",result);

        Log.d("JsonResponse>>>",result);
        try {
            JSONObject jObj=new JSONObject(result);
            String status=jObj.optString("status");
            if(status.equalsIgnoreCase("true")){

                String fees=jObj.optString("fees");
                String code=jObj.optString("code");

                trans_fee.setText(fees+" "+code);

            }
        }
        catch (Exception e)
        {

        }

    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

    }

    @Override
    public void walletList(List<WalletN> walletList) throws Exception {

        walletNList=walletList;
        try {
            for (int i=0;i<walletNList.size();i++)
            {
                if(!walletNList.get(i).getCurrency_code().equalsIgnoreCase("null"))
                {
                    Log.d("Values>>>",""+walletNList.get(i).getCurrency_value()+" "+walletNList.get(i).getCurrency_code());
                    current_wallet_balance.setText(walletNList.get(i).getCurrency_value()+" "+walletNList.get(i).getCurrency_code());
                }
            }
        }
        catch (Exception e)
        {
            Log.e("Exception",e.getMessage());
        }
        Log.d("WalletListSize",this.walletNList.size()+"");

    }

    @Override
    public void cryptoList(List<CryptoM> cryptoList) throws Exception {

    }

    @Override
    public void curreencyList(List<CurreencyM> curreencyList) throws Exception {

    }
}
