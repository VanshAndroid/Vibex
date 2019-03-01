package com.crescentek.cryptocurrency.activity.buysell;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.activity.send.SendBitcoin_2;
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

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by R.Android on 01-08-2018.
 */

public class BuyBitCoin extends BaseActivity implements ApiRequestListener {

    private Button buy_btn;
    private ImageView back_iv,logo_iv;
    private TextView headerText_tv,available_btc,local_currency,crypto_code_tv,crypto_code_val,currencyCode;
    private EditText edAmount;
    String crypto_code,currency_code;
    double crypto_rate,value;
    UserSessionManager sessionManager;
    String[] crypto_localCurrency ;
    Spinner spinner;
    boolean checkVal=false;
    double local_amt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(BuyBitCoin.this);
        setContentView(R.layout.buybitcoin);
        sessionManager=new UserSessionManager(BuyBitCoin.this);

        Log.d("Value_Check>>>",""+getIntent().getStringExtra("localCurrancy")+"  "+getIntent().getStringExtra("cryptoTag"));
        crypto_localCurrency=new String [] {getIntent().getStringExtra("localCurrancy"),getIntent().getStringExtra("cryptoTag")};
        initViews();

        crypto_code_val.setVisibility(View.INVISIBLE);
        crypto_code_tv.setVisibility(View.INVISIBLE);
        headerText_tv.setText("Buy "+getIntent().getStringExtra("cryptoName"));
        available_btc.setText(sessionManager.getValues(UserSessionManager.NGN_WALLET));

        if(getIntent().getStringExtra("crypto_id").equalsIgnoreCase("1"))
        {
            logo_iv.setImageResource(R.drawable.bit);
        }
        else {
            logo_iv.setImageResource(R.drawable.ethereum);
        }


        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,crypto_localCurrency);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);


        local_currency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                spinner.performClick();

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                Log.d("Crurrency>>>",crypto_localCurrency[position]);
                if(position==0)
                {
                    checkVal=false;
                }
                else {
                    checkVal=true;
                }

                local_currency.setText(crypto_localCurrency[position]);
                edAmount.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        currencyCode.setText("Available "+sessionManager.getValues(UserSessionManager.CURRENCY_CODE)+" ");

        buy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edAmount.getText().toString().trim().length()>0) {

                    double wallet_curr = Double.parseDouble(sessionManager.getValues(UserSessionManager.NGN_WALLET));
                    if (checkVal){
                        local_amt = Double.parseDouble(String.valueOf(value));
                    }else {
                        local_amt = Double.parseDouble(edAmount.getText().toString().trim());
                    }

                    if (wallet_curr > local_amt) {
                        if (local_amt > 0) {

                            if(getIntent().getStringExtra("localCurrancy").equalsIgnoreCase(local_currency.getText().toString()))
                            {
                                Intent intent=new Intent(getApplicationContext(),BuySellSummary.class);
                                Log.d("crypto_value>>>>>>",crypto_code_val.getText().toString());
                                intent.putExtra("crypto_value",crypto_code_val.getText().toString());

                                Log.d("currency_payable>>>>>>",edAmount.getText().toString());
                                intent.putExtra("currency_payable",edAmount.getText().toString());

                                intent.putExtra("type","cryptoBuy");

                                Log.d("crypto_code>>>>>>",crypto_code);
                                intent.putExtra("crypto_code",crypto_code);

                                Log.d("crypto_id>>>>>>",getIntent().getStringExtra("crypto_id"));
                                intent.putExtra("crypto_id",getIntent().getStringExtra("crypto_id"));

                                startActivity(intent);

                            }
                            else {

                                Intent intent=new Intent(getApplicationContext(),BuySellSummary.class);

                                Log.d("crypto_value>>>>>>",edAmount.getText().toString());
                                intent.putExtra("crypto_value",edAmount.getText().toString());

                                Log.d("currency_payable>>>>>>",crypto_code_val.getText().toString());
                                intent.putExtra("currency_payable",crypto_code_val.getText().toString());

                                intent.putExtra("type","cryptoBuy");

                                Log.d("crypto_code>>>>>>",crypto_code);
                                intent.putExtra("crypto_code",crypto_code);

                                Log.d("crypto_id>>>>>>",getIntent().getStringExtra("crypto_id"));
                                intent.putExtra("crypto_id",getIntent().getStringExtra("crypto_id"));

                                startActivity(intent);
                            }

                        } else {
                            showAlertDialog("Enter Amount");
                        }

                    } else {
                        showAlertDialog("Insuffcient balance");
                    }

                }
                else {
                    edAmount.setError("Enter Amount");
                }

            }
        });

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

                if(edAmount.getText().toString().length()>0) {

                    if(ConnectivityReceiver.isConnected())
                    {
                        crypto_code_val.setVisibility(View.VISIBLE);
                        crypto_code_tv.setVisibility(View.VISIBLE);
                        new GetRequest(BuyBitCoin.this, BuyBitCoin.this, "getrate", NetworkUtility.GET_RATE+"="+getIntent().getStringExtra("crypto_id"));
                    }
                    else {
                        showAlertDialog(getResources().getString(R.string.dlg_nointernet));
                    }

                }
                else {
                    crypto_code_val.setVisibility(View.INVISIBLE);
                    crypto_code_tv.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    public void initViews()
    {
        buy_btn=findViewById(R.id.buy_btn);
        back_iv=findViewById(R.id.back_iv);
        headerText_tv=findViewById(R.id.headerText_tv);
        available_btc=findViewById(R.id.available_btc);
        local_currency=findViewById(R.id.local_currency);
        crypto_code_tv=findViewById(R.id.crypto_code);
        crypto_code_val=findViewById(R.id.crypto_code_val);
        currencyCode=findViewById(R.id.currencyCode);
        edAmount=findViewById(R.id.edAmount);
        logo_iv=findViewById(R.id.logo_iv);
        spinner=findViewById(R.id.spinner);
    }

    @Override
    public void onSuccess(String result, String type) throws JSONException {

        try {

            Log.d("Rate>>>",result);
            JSONObject jsonObject = new JSONObject(result);
            boolean status = jsonObject.optBoolean("status");
            if (status){

                JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                crypto_rate = jsonObjectData.optDouble("crypto_rate");
                crypto_code=jsonObjectData.optString("crypto_code");
                currency_code=jsonObjectData.optString("currency_code");



                DecimalFormat df = new DecimalFormat();
                df.setMaximumFractionDigits(11);

                Log.d("ValueCheck",getIntent().getStringExtra("cryptoTag")+":::"+crypto_code_tv.getText().toString());
                if(getIntent().getStringExtra("cryptoTag").equalsIgnoreCase(local_currency.getText().toString()))
                {

                    value = Double.parseDouble(edAmount.getText().toString())*crypto_rate;
                    Log.d(">rate Ca: ",""+value);
                    crypto_code_tv.setText(""+currency_code);
                    crypto_code_val.setVisibility(View.VISIBLE);
                    crypto_code_tv.setVisibility(View.VISIBLE);
                    crypto_code_val.setText(" "+df.format(value));
                }
                else {

                    value = Double.parseDouble(edAmount.getText().toString())/crypto_rate;
                    crypto_code_tv.setText(""+crypto_code);
                    Log.d(">rate Ca: ",""+value);
                    crypto_code_val.setVisibility(View.VISIBLE);
                    crypto_code_tv.setVisibility(View.VISIBLE);
                    crypto_code_val.setText(" "+df.format(value));
                }


            }

        }catch (Exception e){
            Log.e(">>Error",e.toString());
            crypto_code_val.setVisibility(View.INVISIBLE);
            crypto_code_tv.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

    }


}
