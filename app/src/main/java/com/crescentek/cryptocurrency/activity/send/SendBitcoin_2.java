package com.crescentek.cryptocurrency.activity.send;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.adapter.CryptoAdapter;
import com.crescentek.cryptocurrency.comman.WalletRecord;
import com.crescentek.cryptocurrency.interfaces.ApiRequestListener;
import com.crescentek.cryptocurrency.interfaces.WalletListener;
import com.crescentek.cryptocurrency.model.CryptoM;
import com.crescentek.cryptocurrency.model.CurreencyM;
import com.crescentek.cryptocurrency.model.WalletN;
import com.crescentek.cryptocurrency.network.GetRequest;
import com.crescentek.cryptocurrency.utility.BaseActivity;
import com.crescentek.cryptocurrency.utility.ConnectivityReceiver;
import com.crescentek.cryptocurrency.utility.NetworkUtility;
import com.crescentek.cryptocurrency.utility.UserSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by R.Android on 02-08-2018.
 */

public class SendBitcoin_2 extends BaseActivity implements ApiRequestListener,WalletListener{


    private ImageView next_iv,back_iv;
    private TextView headerText_tv, tvCurencyRate,available_btc,crypto_code_tv;
    EditText edAmount;
    UserSessionManager sessionManager;
    double value=0.00,crypto_rate=0.00;
    String crypto_code,currency_code;
    String cryptoId="";
    TextView textView;
    Spinner spinner2, spiner_crypto_code_tv;
    List<CryptoM> cryptoList;
    String [] crytpo_currency;
    String CryptoWallet;
    String typeSend;
    double btc_amt;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(SendBitcoin_2.this);
        setContentView(R.layout.send_bitcoin_2);
        new WalletRecord(SendBitcoin_2.this,SendBitcoin_2.this);

        crytpo_currency=new String[]{getIntent().getStringExtra("crypto_code"),getIntent().getStringExtra("local_currency_code")};

        sessionManager=new UserSessionManager(getApplicationContext());
        headerText_tv=findViewById(R.id.headerText_tv);
        next_iv=findViewById(R.id.next_iv);
        back_iv=findViewById(R.id.back_iv);
        edAmount = findViewById(R.id.ngn_amount);
        tvCurencyRate = findViewById(R.id.btc_value);
        available_btc=findViewById(R.id.available_btc);
        crypto_code_tv=findViewById(R.id.crypto_code_tv);
        spinner2=findViewById(R.id.spinner2);
        spiner_crypto_code_tv=findViewById(R.id.spiner_crypto_code_tv);


        textView=findViewById(R.id.textView);

        cryptoId=getIntent().getStringExtra("cryptoid");

        crypto_code_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spiner_crypto_code_tv.performClick();
            }
        });

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,crytpo_currency);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner_crypto_code_tv.setAdapter(arrayAdapter);


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                spinner2.performClick();

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                Log.d("spinnerSeection>>>",""+cryptoList.get(position).getCrypto_code()+"::::"+cryptoList.get(position).getCrypto_value());
                //crypto_code
                textView.setText("Available "+getIntent().getStringExtra("crypto_code"));
                available_btc.setText(cryptoList.get(position).getCrypto_value());
                spinner2.setVisibility(View.INVISIBLE);
                cryptoId=cryptoList.get(position).getCrypto_id();
                getRate();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spiner_crypto_code_tv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                crypto_code_tv.setText(crytpo_currency[position]);
                spiner_crypto_code_tv.setVisibility(View.INVISIBLE);
                edAmount.setText("");

                getRate();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(getIntent().getStringExtra("amount")!=null||getIntent().getStringExtra("amount").length()>0)
        {
            edAmount.setText(getIntent().getStringExtra("amount"));
            getRate();
        }

        available_btc.setText(sessionManager.getValues(UserSessionManager.BTC_WALLET));
        headerText_tv.setText("Send to\n"+getIntent().getStringExtra("contact"));
        tvCurencyRate.setVisibility(View.INVISIBLE);

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        next_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double wallet_curr=Double.parseDouble(CryptoWallet);
                if(typeSend.equalsIgnoreCase("Crypto"))
                {
                    btc_amt=Double.parseDouble(edAmount.getText().toString().trim());
                }
                else {
                    btc_amt=Double.parseDouble(tvCurencyRate.getText().toString().trim().substring(4,tvCurencyRate.getText().toString().length()));
                }

                if (wallet_curr>btc_amt) {

                    if(typeSend.equalsIgnoreCase("Crypto")){

                        if(value>0) {
                            Intent intent = new Intent(getApplicationContext(), SendSummary.class);
                            intent.putExtra("crypto", "" + edAmount.getText().toString());
                            intent.putExtra("crypto_code", "" + crypto_code);
                            intent.putExtra("local_currency", "" + value);
                            intent.putExtra("currency_code", "" + currency_code);
                            intent.putExtra("contact", "" + getIntent().getStringExtra("contact"));
                            intent.putExtra("cryptoId",cryptoId);
                            startActivity(intent);
                        }
                        else {
                            showAlertDialog("Enter Amount");
                        }

                    }
                    else {

                        Intent intent = new Intent(getApplicationContext(), SendSummary.class);
                        intent.putExtra("crypto", "" + btc_amt);
                        intent.putExtra("crypto_code", "" + crypto_code);
                        intent.putExtra("local_currency", "" + value);
                        intent.putExtra("currency_code", "" + currency_code);
                        intent.putExtra("contact", "" + getIntent().getStringExtra("contact"));
                        intent.putExtra("cryptoId",cryptoId);
                        startActivity(intent);

                    }


                }
                else {
                    showAlertDialog("Insuffcient balance");
                }

            }
        });

        edAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                Log.d(">>Value>>",edAmount.getText().toString());
                if(edAmount.getText().toString().length()>0) {

                    if(ConnectivityReceiver.isConnected())
                    {
                        tvCurencyRate.setVisibility(View.INVISIBLE);
                        new GetRequest(SendBitcoin_2.this, SendBitcoin_2.this, "getrate", NetworkUtility.GET_RATE+"="+cryptoId);
                    }
                    else {
                        showAlertDialog(getResources().getString(R.string.dlg_nointernet));
                    }


                }
                else {
                    tvCurencyRate.setVisibility(View.INVISIBLE);
                }

            }
        });

    }

    public void getRate()
    {

        if(ConnectivityReceiver.isConnected())
        {
            tvCurencyRate.setVisibility(View.INVISIBLE);
            Log.d("RateUrl>>>",NetworkUtility.GET_RATE+"="+cryptoId);
            new GetRequest(SendBitcoin_2.this, SendBitcoin_2.this, "getrate", NetworkUtility.GET_RATE+"="+cryptoId);
        }
        else {
            showAlertDialog(getResources().getString(R.string.dlg_nointernet));
        }


    }

    @Override
    public void onSuccess(String result, String type) throws JSONException {

        try {

            Log.d("RateResponse>>>",result);
            JSONObject jsonObject = new JSONObject(result);
            boolean status = jsonObject.optBoolean("status");
            if (status){
                JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                crypto_rate = jsonObjectData.optDouble("crypto_rate");
                crypto_code=jsonObjectData.optString("crypto_code");
                currency_code=jsonObjectData.optString("currency_code");
                Log.d(">rate: ",""+crypto_rate);

                DecimalFormat df = new DecimalFormat();
                df.setMaximumFractionDigits(11);
                textView.setText("Available "+crypto_code+": ");

                if(getIntent().getStringExtra("crypto_code").equalsIgnoreCase(crypto_code_tv.getText().toString()))
                {
                    typeSend="Crypto";
                    value = Double.parseDouble(edAmount.getText().toString())*crypto_rate;
                    tvCurencyRate.setVisibility(View.VISIBLE);
                    tvCurencyRate.setText("-"+currency_code+" "+df.format(value));
                }
                else {
                    typeSend="Local";
                    value = Double.parseDouble(edAmount.getText().toString())/crypto_rate;
                    tvCurencyRate.setVisibility(View.VISIBLE);
                    tvCurencyRate.setText("-"+crypto_code+" "+df.format(value));
                }


            }

        }catch (Exception e){
            Log.e(">>Error",e.toString());
            tvCurencyRate.setVisibility(View.INVISIBLE);
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

        //available_btc
        for(int i=0;i<cryptoList.size();i++)
        {
            if(cryptoList.get(i).getCrypto_code().equalsIgnoreCase(getIntent().getStringExtra("crypto_code"))){
                available_btc.setText(cryptoList.get(i).getCrypto_value());
                CryptoWallet=cryptoList.get(i).getCrypto_value();
            }

        }

    }

    @Override
    public void curreencyList(List<CurreencyM> curreencyList) throws Exception {

    }
}
