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
import android.widget.Toast;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.interfaces.ApiRequestListener;
import com.crescentek.cryptocurrency.network.GetRequest;
import com.crescentek.cryptocurrency.utility.BaseActivity;
import com.crescentek.cryptocurrency.utility.ConnectivityReceiver;
import com.crescentek.cryptocurrency.utility.NetworkUtility;
import com.crescentek.cryptocurrency.utility.UserSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

/**
 * Created by R.Android on 01-08-2018.
 */

public class SellBitCoin extends BaseActivity implements ApiRequestListener{

    private ImageView back_iv;
    private TextView headerText_tv,avilabeCryptoCode_tv,crypto_wallet_bal,currency_code_tv,crypto_code_tv,crypto_val_tv;
    private EditText currency_val;
    private Button sell_btn;
    String crypto_code,currency_code;
    double crypto_rate,value;
    UserSessionManager sessionManager;
    Spinner spinner;
    String[] crypto_localCurrency;
    ImageView logo_iv;

    boolean checkVal=false;
    double local_amt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(SellBitCoin.this);
        setContentView(R.layout.sell_bitcoin);
        sessionManager=new UserSessionManager(SellBitCoin.this);

        initViews();

        crypto_localCurrency=new String [] {getIntent().getStringExtra("localCurrancy"),getIntent().getStringExtra("cryptoTag")};
        crypto_code_tv.setVisibility(View.INVISIBLE);
        crypto_val_tv.setVisibility(View.INVISIBLE);

        headerText_tv.setText("Sell Bitcoin");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,crypto_localCurrency);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        currency_code_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.performClick();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position==0)
                {
                    checkVal=true;
                }else {
                    checkVal=false;
                }

                currency_code_tv.setText(crypto_localCurrency[position]);
                currency_val.setText("");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        crypto_wallet_bal.setText(getIntent().getStringExtra("CryptoVal"));
        avilabeCryptoCode_tv.setText("Abailable "+getIntent().getStringExtra("cryptoTag")+" ");
        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sell_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(currency_val.getText().toString().trim().length()>0) {

                    double wallet_curr = Double.parseDouble(sessionManager.getValues(UserSessionManager.BTC_WALLET));
                    if (checkVal) {
                        local_amt = Double.parseDouble(String.valueOf(value));
                    } else {
                        local_amt = Double.parseDouble(currency_val.getText().toString().trim());
                    }
                    Log.d("Values>>>",wallet_curr+":::"+local_amt);

                    if(wallet_curr>local_amt)
                    {
                        if(currency_val.getText().toString().trim().length()>0) {

                            Log.d("Currency_Check>>>",""+getIntent().getStringExtra("localCurrancy")+":::"+crypto_code_tv.getText().toString()+":::"+currency_code_tv.getText().toString());

                            if(getIntent().getStringExtra("localCurrancy").equalsIgnoreCase(crypto_code_tv.getText().toString())){

                                Intent intent=new Intent(getApplicationContext(),BuySellSummary.class);
                                Log.d("crypto_value>>>>>>",currency_val.getText().toString());
                                intent.putExtra("crypto_value",currency_val.getText().toString());

                                Log.d("currency_payable>>>>>>",crypto_val_tv.getText().toString());
                                intent.putExtra("currency_payable",crypto_val_tv.getText().toString());

                                intent.putExtra("type","cryptoSell");

                                Log.d("crypto_code>>>>>>",crypto_code);
                                intent.putExtra("crypto_code",crypto_code);

                                Log.d("crypto_id>>>>>>",getIntent().getStringExtra("crypto_id"));
                                intent.putExtra("crypto_id",getIntent().getStringExtra("crypto_id"));
                                startActivity(intent);

                            }
                            else {

                                Intent intent=new Intent(getApplicationContext(),BuySellSummary.class);
                                Log.d("crypto_value>>>>>>",crypto_val_tv.getText().toString());
                                intent.putExtra("crypto_value",crypto_val_tv.getText().toString());

                                Log.d("currency_payable>>>>>>",currency_val.getText().toString());
                                intent.putExtra("currency_payable",currency_val.getText().toString());

                                intent.putExtra("type","cryptoSell");

                                Log.d("crypto_code>>>>>>",crypto_code);
                                intent.putExtra("crypto_code",crypto_code);

                                Log.d("crypto_id>>>>>>",getIntent().getStringExtra("crypto_id"));
                                intent.putExtra("crypto_id",getIntent().getStringExtra("crypto_id"));
                                startActivity(intent);
                            }


                        }
                        else {
                            currency_val.setError("Enter Amount");
                        }
                    }
                    else {
                        showAlertDialog("Insuffcient balance");
                    }


                }
                else {
                    currency_val.setError("Enter Amount");
                }


            }
        });

        currency_val.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(currency_val.getText().toString().length()>0) {

                    if(ConnectivityReceiver.isConnected())
                    {
                        crypto_code_tv.setVisibility(View.VISIBLE);
                        crypto_val_tv.setVisibility(View.VISIBLE);
                        new GetRequest(SellBitCoin.this, SellBitCoin.this, "getrate", NetworkUtility.GET_RATE+"="+getIntent().getStringExtra("crypto_id"));
                    }
                    else {
                        showAlertDialog(getResources().getString(R.string.dlg_nointernet));
                    }

                }
                else {
                    crypto_code_tv.setVisibility(View.INVISIBLE);
                    crypto_val_tv.setVisibility(View.INVISIBLE);
                }

            }
        });

        if(getIntent().getStringExtra("crypto_id").equalsIgnoreCase("1"))
        {
            logo_iv.setImageResource(R.drawable.bit);
        }
        else {
            logo_iv.setImageResource(R.drawable.ethereum);
        }

    }

    public void initViews()
    {
        avilabeCryptoCode_tv=findViewById(R.id.avilabeCryptoCode_tv);
        crypto_wallet_bal=findViewById(R.id.crypto_wallet_bal);
        currency_code_tv=findViewById(R.id.currency_code_tv);
        currency_val=findViewById(R.id.currency_val);
        crypto_code_tv=findViewById(R.id.crypto_code_tv);
        crypto_val_tv=findViewById(R.id.crypto_val_tv);
        back_iv=findViewById(R.id.back_iv);
        headerText_tv=findViewById(R.id.headerText_tv);
        sell_btn=findViewById(R.id.sell_btn);
        spinner=findViewById(R.id.spinner);
        logo_iv=findViewById(R.id.logo_iv);

    }


    @Override
    public void onSuccess(String result, String type) throws JSONException {

        try {
            Log.d("Rate>>>",result);
            JSONObject jsonObject = new JSONObject(result);
            boolean status = jsonObject.optBoolean("status");
            if (status){

                JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                crypto_rate = jsonObjectData.optDouble("crypto_sell_rate");
                crypto_code=jsonObjectData.optString("crypto_code");
                currency_code=jsonObjectData.optString("currency_code");

                DecimalFormat df = new DecimalFormat();
                df.setMaximumFractionDigits(11);

                Log.d("ValueCheck",getIntent().getStringExtra("cryptoTag")+":::"+crypto_code_tv.getText().toString());

                if(getIntent().getStringExtra("cryptoTag").equalsIgnoreCase(currency_code_tv.getText().toString()))
                {
                    value = Double.parseDouble(currency_val.getText().toString())*crypto_rate;
                    Log.d(">rate Ca: ",""+value);
                    crypto_code_tv.setText(""+currency_code);
                    crypto_val_tv.setVisibility(View.VISIBLE);
                    crypto_code_tv.setVisibility(View.VISIBLE);
                    crypto_val_tv.setText(" "+df.format(value));
                }
                else {
                    value = Double.parseDouble(currency_val.getText().toString())/crypto_rate;
                    Log.d(">rate Ca: ",""+value);
                    crypto_code_tv.setText(""+crypto_code);
                    crypto_val_tv.setVisibility(View.VISIBLE);
                    crypto_code_tv.setVisibility(View.VISIBLE);
                    crypto_val_tv.setText(" "+df.format(value));
                }

            }


        }catch (Exception e){
            Log.e(">>Error",e.toString());
            crypto_val_tv.setVisibility(View.INVISIBLE);
            crypto_code_tv.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

    }
}
