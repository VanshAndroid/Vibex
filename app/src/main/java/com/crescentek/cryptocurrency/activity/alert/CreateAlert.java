package com.crescentek.cryptocurrency.activity.alert;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatRadioButton;
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
import com.crescentek.cryptocurrency.activity.buysell.SellBitCoin;
import com.crescentek.cryptocurrency.activity.login_signup.HomeActivity;
import com.crescentek.cryptocurrency.adapter.CryptoAdapter;
import com.crescentek.cryptocurrency.comman.WalletRecord;
import com.crescentek.cryptocurrency.fragment.navigation.HomeFragment;
import com.crescentek.cryptocurrency.fragment.navigation.TransactionsFragment;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by R.Android on 06-08-2018.
 */

public class CreateAlert extends BaseActivity implements ApiRequestListener,WalletListener{

    private Button create_alert_btn;
    private ImageView back_iv;
    private TextView headerText_tv,rate_tv,crypto_txt;
    Map<String,String> mapObj,mapHeader;
    UserSessionManager sessionManager;
    EditText amt_ed;
    Spinner crypto_spinner;
    ImageView crypto_icon;
    String crpto_id;
    List<CryptoM> cryptoList;
    TextView currency_tv;

    AppCompatRadioButton once_off, every_time, more_than, less_than;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(CreateAlert.this);
        setContentView(R.layout.create_alert);

        new WalletRecord(CreateAlert.this,CreateAlert.this);

        mapObj=new HashMap<String, String>();
        mapHeader=new HashMap<String, String>();
        sessionManager=new UserSessionManager(CreateAlert.this);
        create_alert_btn=findViewById(R.id.create_alert_btn);
        back_iv=findViewById(R.id.back_iv);
        headerText_tv=findViewById(R.id.headerText_tv);
        once_off=findViewById(R.id.once_off);
        every_time=findViewById(R.id.every_time);
        more_than=findViewById(R.id.more_than);
        less_than=findViewById(R.id.less_than);
        amt_ed=findViewById(R.id.amt_ed);
        crypto_spinner = (Spinner) findViewById(R.id.crypto_spinner);
        rate_tv=(TextView) findViewById(R.id.rate_tv);
        crypto_icon=findViewById(R.id.crypto_icon);
        crypto_txt=findViewById(R.id.crypto_txt);
        currency_tv=findViewById(R.id.currency_tv);
        once_off = (AppCompatRadioButton)findViewById(R.id.once_off);
        every_time = (AppCompatRadioButton)findViewById(R.id.every_time);
        more_than = (AppCompatRadioButton)findViewById(R.id.more_than);
        less_than = (AppCompatRadioButton)findViewById(R.id.less_than);

        headerText_tv.setText("Create an alert");


        /*ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CreateAlert.this,
                R.array.crypto_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        crypto_spinner.setAdapter(adapter);*/

        create_alert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(amt_ed.getText().toString().trim().length()<1)
                {
                    amt_ed.setError("Amount Required");
                }
                else {

                    if(ConnectivityReceiver.isConnected())
                    {
                        Log.d("CreateAlert>>>",once_off.isChecked()+" "+every_time.isChecked()+" "+more_than.isChecked()+" "+less_than.isChecked());
                        showCustomProgrssDialog(CreateAlert.this);
                        mapObj=postData();
                        mapHeader=mapHeader();
                        Log.d("BodyData>>>",mapObj.toString());
                        new PostRequest(CreateAlert.this,mapObj,mapHeader,CreateAlert.this, NetworkUtility.SERVICE_ALERT,"SERVICE_ALERT");
                    }
                    else {
                        showAlertDialog(getResources().getString(R.string.dlg_nointernet));
                    }

                }

            }
        });

        crypto_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position==0)
                {
                    //Toast.makeText(getApplicationContext(),"0",Toast.LENGTH_LONG).show();
                    crypto_icon.setImageDrawable(getResources().getDrawable(R.drawable.bit));
                    crpto_id=cryptoList.get(position).getCrypto_id();
                    crypto_txt.setText("When the price of 1 "+cryptoList.get(position).getCrypto_name()+" is");

                }
                else if(position==1) {
                    //Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();
                    crypto_icon.setImageDrawable(getResources().getDrawable(R.drawable.ethereum_icon));
                    crpto_id=cryptoList.get(position).getCrypto_id();
                    crypto_txt.setText("When the price of 1 "+cryptoList.get(position).getCrypto_name()+" is");
                }
                else {
                    crypto_icon.setImageDrawable(getResources().getDrawable(R.drawable.icon_no_image));
                    crypto_txt.setText("When the price of 1 "+cryptoList.get(position).getCrypto_name()+" is");
                    crpto_id=cryptoList.get(position).getCrypto_id();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getCryptoRate();

    }

    public void getCryptoRate()
    {
        if(ConnectivityReceiver.isConnected())
        {
            showCustomProgrssDialog(CreateAlert.this);
            ConnectivityReceiver.isConnected();
            new GetRequest(CreateAlert.this, CreateAlert.this, "getrate", NetworkUtility.GET_BTC_RATE);
        }
        else {

            showAlertDialog(getResources().getString(R.string.dlg_nointernet));

        }

    }

    public Map<String,String> postData()
    {
        if(every_time.isChecked()){
            mapObj.put("alert_type","Indefinitely");
        }else {
            mapObj.put("alert_type","Once");
        }
        if(more_than.isChecked()){
            mapObj.put("amount_more","1");
        }else {
            mapObj.put("amount_less","1");
        }

        mapObj.put("amount",""+amt_ed.getText().toString().trim());
        mapObj.put("crypto_id",crpto_id);

        return mapObj;
    }
    public Map<String,String> mapHeader()
    {
        mapHeader.put("token",sessionManager.getValues(UserSessionManager.KEY_TOKEN));

        return mapHeader;
    }

    @Override
    public void onSuccess(String result, String type) throws JSONException {

        hideCustomProgrssDialog();
        Log.d("ResponseAlert>>>",result);

        switch (type)
        {
            case "getrate":
                {
                    try {

                        Log.d("Rate>>>",result);
                        JSONObject jsonObject = new JSONObject(result);
                        boolean status = jsonObject.optBoolean("status");
                        if(status)
                        {
                            JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                            String crypto_rate = jsonObjectData.optString("crypto_rate");
                            String crypto_code=jsonObjectData.optString("crypto_code");
                            String currency_code=jsonObjectData.optString("currency_code");

                            rate_tv.setText("Current price "+currency_code+" "+crypto_rate);
                            currency_tv.setText(currency_code);
                        }

                    }
                    catch (Exception e) {

                    }

                }
                break;
            case "SERVICE_ALERT":
                {
                    JSONObject jObj=new JSONObject(result);
                    String status=jObj.optString("status");
                    String message=jObj.optString("message");
                    if(status.equalsIgnoreCase("success"))
                    {
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

                        Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        showAlertDialog(message);
                    }

                }
                break;

        }

    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

        hideCustomProgrssDialog();
        Toast.makeText(getApplicationContext(),"Response Code"+responseCode+"  \n"+responseMessage,Toast.LENGTH_LONG).show();

    }

    @Override
    public void walletList(List<WalletN> walletList) throws Exception {

    }

    @Override
    public void cryptoList(List<CryptoM> cryptoList) throws Exception {

        String showCrypto="Crypto";
        this.cryptoList=cryptoList;
        CryptoAdapter adapter = new CryptoAdapter(CreateAlert.this,R.layout.list_item_crypto, R.id.title, cryptoList,showCrypto);
        crypto_spinner.setAdapter(adapter);
    }

    @Override
    public void curreencyList(List<CurreencyM> curreencyList) throws Exception {

    }
}
