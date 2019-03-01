package com.crescentek.cryptocurrency.activity.Exchangebuysell;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.activity.buysell.BuyBitCoin;
import com.crescentek.cryptocurrency.activity.buysell.BuySellSummary;
import com.crescentek.cryptocurrency.activity.login_signup.HomeActivity;
import com.crescentek.cryptocurrency.comman.WalletRecord;
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

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by R.Android on 21-09-2018.
 */

public class ExchangeBuyFragment extends Fragment implements ApiRequestListener,WalletListener {

    private EditText amount_ed,edPriceBitcoin;
    View view;
    private TextView requiredFund,walletBalance,estimated_fee;
    private Button exchange_buy;

    UserSessionManager sessionManager;
    Map<String, String> mapObject,mapHeader,buyObject;
    BaseActivity baseActivity;
    TextView required_bal_cod,available_bal_cod,required_fund_currency;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.exchange_buy,container,false);

        initViews();
        new WalletRecord(getActivity(),ExchangeBuyFragment.this);
        sessionManager=new UserSessionManager(getActivity());
        baseActivity=new BaseActivity() {};
        mapObject=new HashMap<String, String>();
        buyObject=new HashMap<String, String>();
        mapHeader=new HashMap<String, String>();

        amount_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(amount_ed.getText().toString().length()<1)
                {
                    //amount_ed.setError("Crypto Required");
                }
                else if(edPriceBitcoin.getText().toString().length()<1)
                {
                    //edPriceBitcoin.setError("Price per Crypto");
                }
                else {
                    double btcVal=Double.parseDouble(amount_ed.getText().toString());
                    double perBtcPrice=Double.parseDouble(edPriceBitcoin.getText().toString());
                    double total=btcVal*perBtcPrice;
                    requiredFund.setText(""+total);
                    callFeesApi();

                }

            }
        });

        edPriceBitcoin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                    if(amount_ed.getText().toString().length()<1)
                    {
                        amount_ed.setError("Crypto Required");
                    }
                    else if(edPriceBitcoin.getText().toString().length()<1)
                    {
                        edPriceBitcoin.setError("Price per Crypto");
                    }
                    else {
                        double btcVal=Double.parseDouble(amount_ed.getText().toString());
                        double perBtcPrice=Double.parseDouble(edPriceBitcoin.getText().toString());
                        double total=btcVal*perBtcPrice;
                        requiredFund.setText(""+total);
                        callFeesApi();

                    }

            }
        });



        exchange_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(amount_ed.getText().toString().length()<1)
                {
                    amount_ed.setError("Crypto Required");
                }
                else if(edPriceBitcoin.getText().toString().length()<1)
                {
                    edPriceBitcoin.setError("Price per Crypto");
                }else {

                    if(ConnectivityReceiver.isConnected())
                    {


                        /*buyObject.put("orderType","buy");
                        buyObject.put("cryptoId",sessionManager.getValues(UserSessionManager.CRYPTO_ID));
                        buyObject.put("cryptoVal",""+amount_ed.getText().toString());
                        buyObject.put("currencyVal",""+edPriceBitcoin.getText().toString());
*/

                        Intent intent=new Intent(getActivity(),ExchangePinActivity.class);
                        intent.putExtra("orderType","buy");
                        intent.putExtra("cryptoId",sessionManager.getValues(UserSessionManager.CRYPTO_ID));
                        intent.putExtra("cryptoVal",amount_ed.getText().toString());
                        intent.putExtra("currencyVal",edPriceBitcoin.getText().toString());
                        startActivity(intent);



                        /*baseActivity.showCustomProgrssDialog(getActivity());
                        ConnectivityReceiver.isConnected();
                        mapHeader=addHeader();
                        buyObject=postbuyData();
                        Log.d("MapVal>>>",buyObject.toString());
                        new PostRequest(getActivity(),buyObject,mapHeader,ExchangeBuyFragment.this,NetworkUtility.ORDER_EXCHANGE,"Transaction");*/
                    }
                    else {
                        baseActivity.showAlertDialog(getResources().getString(R.string.dlg_nointernet));
                    }

                }
            }
        });


        return view;
    }

    public void initViews()
    {
        amount_ed=view.findViewById(R.id.amount_ed);
        edPriceBitcoin=view.findViewById(R.id.edPriceBitcoin);
        requiredFund=view.findViewById(R.id.requiredFund);
        walletBalance=view.findViewById(R.id.walletBalance);
        estimated_fee=view.findViewById(R.id.estimated_fee);
        exchange_buy=view.findViewById(R.id.exchange_buy);
        required_bal_cod=view.findViewById(R.id.required_bal_cod);
        available_bal_cod=view.findViewById(R.id.available_bal_cod);
        required_fund_currency=view.findViewById(R.id.required_fund_currency);



    }

    public void callFeesApi()
    {

        if(ConnectivityReceiver.isConnected())
        {
            ConnectivityReceiver.isConnected();
            mapHeader=addHeader();
            mapObject=postData();
            new PostRequest(getActivity(),mapObject,mapHeader,ExchangeBuyFragment.this, NetworkUtility.TRANSACTION_FEE,"transaction_fee");

        }
        else {
            baseActivity.showAlertDialog(getResources().getString(R.string.dlg_nointernet));
        }

    }
    public Map<String,String> postData(){

        mapObject.put("type","exchangeBuy");
        mapObject.put("amount",amount_ed.getText().toString());
        mapObject.put("rate",edPriceBitcoin.getText().toString());
        mapObject.put("cryptoId",""+sessionManager.getValues(UserSessionManager.CRYPTO_ID));

        Log.d("MapObj>>>",mapObject.toString());
        return mapObject;
    }
    public Map<String,String> postbuyData(){

        buyObject.put("orderType","buy");
        buyObject.put("cryptoId",sessionManager.getValues(UserSessionManager.CRYPTO_ID));
        buyObject.put("cryptoVal",""+amount_ed.getText().toString());
        buyObject.put("currencyVal",""+edPriceBitcoin.getText().toString());

        return buyObject;
    }
    public Map<String,String> addHeader()
    {
        mapHeader.put("token",sessionManager.getValues(UserSessionManager.KEY_TOKEN));
        Log.d("token>>>",sessionManager.getValues(UserSessionManager.KEY_TOKEN));
        return mapHeader;
    }

    @Override
    public void onSuccess(String result, String type) throws JSONException {

        switch (type)
        {
            case "transaction_fee":
                {
                    Log.d("T_Response>>>",result);
                    JSONObject jObj=new JSONObject(result);
                    String status=jObj.optString("status");
                    if(status.equalsIgnoreCase("true"))
                    {
                        String fees=jObj.optString("fees");
                        estimated_fee.setText(fees);
                    }
                }
                break;

            case "Transaction":
                {
                    Log.d("TransactionResponse>>>",result);
                    baseActivity.hideCustomProgrssDialog();
                    JSONObject jsonObject=new JSONObject(result);
                    String status=jsonObject.optString("status");
                    String message=jsonObject.optString("message");
                    if (status.equalsIgnoreCase("true"))
                    {
                        Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(getActivity(), HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
                    }
                }
                break;
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

    }

    @Override
    public void curreencyList(List<CurreencyM> curreencyList) throws Exception {

        required_bal_cod.setText(curreencyList.get(0).getCurrency_code());
        available_bal_cod.setText(curreencyList.get(0).getCurrency_code());
        required_fund_currency.setText(curreencyList.get(0).getCurrency_code());
        walletBalance.setText(curreencyList.get(0).getCurrency_value());

    }
}
