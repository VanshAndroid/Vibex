package com.crescentek.cryptocurrency.comman;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.interfaces.ApiRequestListener;
import com.crescentek.cryptocurrency.interfaces.WalletListener;
import com.crescentek.cryptocurrency.model.CryptoM;
import com.crescentek.cryptocurrency.model.CurreencyM;
import com.crescentek.cryptocurrency.model.Wallet;
import com.crescentek.cryptocurrency.model.WalletN;
import com.crescentek.cryptocurrency.network.GetRequest;
import com.crescentek.cryptocurrency.utility.ConnectivityReceiver;
import com.crescentek.cryptocurrency.utility.NetworkUtility;
import com.crescentek.cryptocurrency.utility.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by R.Android on 21-09-2018.
 */

public class WalletRecord implements ApiRequestListener{

    public List<WalletN> walletNList;
    Activity activity;
    List<CryptoM> cryptoMList;
    List<CurreencyM> curreencyMList;
    UserSessionManager sessionManager;
    WalletListener walletListener;

    public WalletRecord(Activity activity,WalletListener walletListener)
    {
        this.activity=activity;
        this.walletListener=walletListener;
        sessionManager=new UserSessionManager(this.activity);
        if(ConnectivityReceiver.isConnected())
        {
            new GetRequest(activity,WalletRecord.this,"wallet", NetworkUtility.WALLET);
        }
        else {
            Toast.makeText(activity,"Please Check Internet Connection",Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void onSuccess(String result, String type) throws JSONException {

        Log.d("WalletData>>",result);
        try {
            walletNList=new ArrayList<WalletN>();
            cryptoMList=new ArrayList<CryptoM>();
            curreencyMList=new ArrayList<CurreencyM>();

            JSONObject jObj=new JSONObject(result);
            String status=jObj.optString("status");
            if(status.equalsIgnoreCase("success"))
            {
                JSONArray jsonArr=jObj.optJSONArray("result");
                if(jsonArr!=null)
                {
                    for(int i=0;i<jsonArr.length();i++)
                    {
                        JSONObject object=jsonArr.getJSONObject(i);
                        WalletN walletN=new WalletN();
                        CurreencyM curreencyM=new CurreencyM();
                        CryptoM crypto=new CryptoM();

                        walletN.setCrypto_value(object.optString("crypto_value"));
                        walletN.setCurrency_value(object.optString("currency_value"));
                        walletN.setCrypto_name(object.optString("crypto_name"));
                        walletN.setCrypto_id(object.optString("crypto_id"));
                        walletN.setCurrency_id(object.optString("currency_id"));
                        walletN.setCurrency_code(object.optString("currency_code"));
                        walletN.setCrypto_code(object.optString("crypto_code"));
                        walletN.setCrypto_addr(object.optString("crypto_addr"));
                        walletN.setAddr_label(object.optString("addr_label"));
                        walletN.setCurrency_logo(object.optString("currency_logo"));
                        walletN.setCrypto_logo(object.optString("crypto_logo"));

                        if(object.optString("currency_code").equalsIgnoreCase("null"))
                        {
                            crypto.setCrypto_id(object.optString("crypto_id"));
                            crypto.setCrypto_code(object.optString("crypto_code"));
                            crypto.setCrypto_name(object.optString("crypto_name"));
                            crypto.setCrypto_value(object.optString("crypto_value"));
                            crypto.setCrypto_addr(object.optString("crypto_addr"));
                            crypto.setCrypto_logo(object.optString("crypto_logo"));
                            cryptoMList.add(crypto);

                        }
                        if(object.optString("crypto_code").equalsIgnoreCase("null")) {

                            curreencyM.setCurrency_id(object.optString("currency_id"));
                            curreencyM.setCurrency_code(object.optString("currency_code"));
                            curreencyM.setCurrency_value(object.optString("currency_value"));
                            curreencyM.setCrypto_addr(object.optString("addr_label"));
                            curreencyM.setCrypto_addr(object.optString("crypto_addr"));
                            curreencyM.setCurrency_logo(object.optString("currency_logo"));

                            curreencyMList.add(curreencyM);

                        }

                        walletNList.add(walletN);

                    }
                    walletListener.cryptoList(cryptoMList);
                    walletListener.walletList(walletNList);
                    walletListener.curreencyList(curreencyMList);


                }

            }
        }
        catch (Exception e)
        {
            Log.d("WalletException>>>",e.getMessage());
        }

    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

        Log.d("WalletData>>",responseMessage);
    }

}
