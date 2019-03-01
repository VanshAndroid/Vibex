package com.crescentek.cryptocurrency.fragment.exchange;

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
import com.crescentek.cryptocurrency.activity.login_signup.HomeActivity;
import com.crescentek.cryptocurrency.interfaces.ApiRequestListener;
import com.crescentek.cryptocurrency.model.BuySell;
import com.crescentek.cryptocurrency.network.PostRequest;
import com.crescentek.cryptocurrency.utility.BaseActivity;
import com.crescentek.cryptocurrency.utility.ConnectivityReceiver;
import com.crescentek.cryptocurrency.utility.NetworkUtility;
import com.crescentek.cryptocurrency.utility.UserSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by R.Android on 09-10-2018.
 */

public class CancelOrderBook extends BaseActivity implements ApiRequestListener {

    Bundle bundle;
    ArrayList<BuySell> buySellList;
    int position;

    private ImageView back_iv;
    private TextView headerText_tv,cryptoRate,crypto_val,status,order_timestamp,trx_type,crypto_closing_traded,crypto_closing_balance,fees;
    private Button cancel_btn;

    private Map<String,String> mapObject;
    private Map<String,String> mapHeader;

    UserSessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(CancelOrderBook.this);
        setContentView(R.layout.exchange_order_details);

        mapObject=new HashMap<String, String>();
        mapHeader=new HashMap<String, String>();
        sessionManager=new UserSessionManager(getApplicationContext());

        initViews();

        bundle = getIntent().getExtras();
        buySellList= (ArrayList<BuySell>) bundle.getSerializable("buySellList");
        position=Integer.parseInt(getIntent().getStringExtra("position"));

        setValues();

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callCancelApi();

            }
        });

    }

    public void callCancelApi()
    {

        if(ConnectivityReceiver.isConnected())
        {
            ConnectivityReceiver.isConnected();
            showCustomProgrssDialog(CancelOrderBook.this);
            mapObject=postData();
            mapHeader=postToken();
            Log.d("NetworkUrl>>>",NetworkUtility.CANCEL_ORDERBOOK+""+buySellList.get(position).getType()+"/"+buySellList.get(position).getId());
            new PostRequest(CancelOrderBook.this,mapObject,mapHeader,CancelOrderBook.this, NetworkUtility.CANCEL_ORDERBOOK+""+buySellList.get(position).getType()+"/"+buySellList.get(position).getId(),"CANCELORDERBOOK");
        }

        else{

            showAlertDialog(getResources().getString(R.string.dlg_nointernet));

        }




    }

    public Map<String,String> postData()
    {
        //mapObject.put("","");
        return mapObject;
    }

    public Map<String,String> postToken()
    {

        mapHeader.put("token",sessionManager.getValues(UserSessionManager.KEY_TOKEN));

        return mapHeader;
    }

    public void setValues()
    {
        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        headerText_tv.setText("Order Details");
        cryptoRate.setText("Buy at "+buySellList.get(position).getCurrency_code()+" "+buySellList.get(position).getCryptoRate());
        crypto_val.setText(buySellList.get(position).getCrypto_code()+" "+buySellList.get(position).getCrypto_val());
        status.setText(buySellList.get(position).getStatus());
        order_timestamp.setText(buySellList.get(position).getOrder_timestamp());
        trx_type.setText(buySellList.get(position).getTrx_type());
        crypto_closing_traded.setText(buySellList.get(position).getCrypto_closing_traded());
        crypto_closing_balance.setText(buySellList.get(position).getCrypto_closing_balance());
        fees.setText(buySellList.get(position).getTrx_fees());

    }

    public void initViews()
    {
        back_iv=findViewById(R.id.back_iv);
        headerText_tv=findViewById(R.id.headerText_tv);
        cryptoRate=findViewById(R.id.cryptoRate);
        crypto_val=findViewById(R.id.crypto_val);
        status=findViewById(R.id.status);
        order_timestamp=findViewById(R.id.order_timestamp);
        trx_type=findViewById(R.id.trx_type);
        crypto_closing_traded=findViewById(R.id.crypto_closing_traded);
        crypto_closing_balance=findViewById(R.id.crypto_closing_balance);
        fees=findViewById(R.id.fees);
        cancel_btn=findViewById(R.id.cancel_btn);

    }

    @Override
    public void onSuccess(String result, String type) throws JSONException {

        /*{"message":"Sell Order cancelled successfully","result":1,"status":"true"}*/

        hideCustomProgrssDialog();
        Log.d("OrderBookDelete>>>",result);
        try {
            JSONObject jObj=new JSONObject(result);
            String status=jObj.optString("status");
            String message=jObj.optString("message");
            if(status.equalsIgnoreCase("true"))
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
        catch (Exception e) {
            Log.d("Exception>>>",e.getMessage());
        }

    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

        hideCustomProgrssDialog();
        Log.d("OrderBooKDelete>>>",responseMessage);

    }
}
