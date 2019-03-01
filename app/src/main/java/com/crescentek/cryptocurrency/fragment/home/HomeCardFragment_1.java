package com.crescentek.cryptocurrency.fragment.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.activity.TransactionChecking;
import com.crescentek.cryptocurrency.activity.buysell.BuyCrypto;
import com.crescentek.cryptocurrency.activity.buysell.SellCrypto;
import com.crescentek.cryptocurrency.interfaces.ApiRequestListener;
import com.crescentek.cryptocurrency.interfaces.CurencyRateInfo;
import com.crescentek.cryptocurrency.network.GetRequest;
import com.crescentek.cryptocurrency.utility.BaseActivity;
import com.crescentek.cryptocurrency.utility.ConnectivityReceiver;
import com.crescentek.cryptocurrency.utility.NetworkUtility;
import com.crescentek.cryptocurrency.utility.UserSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by R.Android on 20-06-2018.
 */

public class HomeCardFragment_1 extends Fragment implements ApiRequestListener{

    View view;
    UserSessionManager sessionManager;

    private TextView crypto_rate_val,sell,buy;
    BaseActivity baseActivity;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_card_container_1, container, false);

        baseActivity=new BaseActivity(){};

        sessionManager=new UserSessionManager(getActivity());

        crypto_rate_val=view.findViewById(R.id.crypto_rate);

        sell=view.findViewById(R.id.sell);

        buy=view.findViewById(R.id.buy);

        callRateApi();

        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sessionManager.getValues(UserSessionManager.KEY_CAN_SELL).equalsIgnoreCase("1"))
                {
                    startActivity(new Intent(getActivity(), SellCrypto.class));
                }else {
                    startActivity(new Intent(getActivity(), TransactionChecking.class));
                }

            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sessionManager.getValues(UserSessionManager.KEY_CAN_BUY).equalsIgnoreCase("1"))
                {
                    startActivity(new Intent(getActivity(), BuyCrypto.class));
                }else {
                    startActivity(new Intent(getActivity(), TransactionChecking.class));
                }


            }
        });

        return view;
    }

    public void callRateApi()
    {
        Handler handler = new Handler();
        Runnable  runnable = new Runnable() {
            @Override
            public void run() {

                if(ConnectivityReceiver.isConnected())
                {
                    new GetRequest(getActivity(),HomeCardFragment_1.this,"Rate", NetworkUtility.GET_RATE+"="+sessionManager.getValues(UserSessionManager.CRYPTO_ID));
                }

                else{
                    baseActivity.showAlertDialog(getResources().getString(R.string.dlg_nointernet));
                }
            }
        };

        handler.postDelayed(runnable,1000);

    }

    @Override
    public void onSuccess(String result, String type) throws JSONException {

        Log.d("Response>>>>",result);
        JSONObject jObj=new JSONObject(result);
        String status=jObj.optString("status");
        if(status.equalsIgnoreCase("true"))
        {
            JSONObject jOb=jObj.optJSONObject("data");
            String crypto_rate=jOb.optString("crypto_rate");
            String currency_code=jOb.optString("currency_code");
            crypto_rate_val.setText(currency_code+" "+crypto_rate);

            //curencyRateInfo.setCurencyRate(currency_code,crypto_rate);
        }


    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

    }

}
