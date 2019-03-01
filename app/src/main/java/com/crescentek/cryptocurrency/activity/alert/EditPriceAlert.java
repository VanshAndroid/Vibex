package com.crescentek.cryptocurrency.activity.alert;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.activity.login_signup.HomeActivity;
import com.crescentek.cryptocurrency.interfaces.ApiRequestListener;
import com.crescentek.cryptocurrency.network.GetRequest;
import com.crescentek.cryptocurrency.network.PostRequest;
import com.crescentek.cryptocurrency.utility.BaseActivity;
import com.crescentek.cryptocurrency.utility.ConnectivityReceiver;
import com.crescentek.cryptocurrency.utility.NetworkUtility;
import com.crescentek.cryptocurrency.utility.UserSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by R.Android on 04-10-2018.
 */

public class EditPriceAlert extends BaseActivity implements ApiRequestListener{


    private Button save_btn;
    private ImageView back_iv;
    private TextView headerText_tv,rate_tv,delete_tv;
    Map<String,String> mapObj,mapHeader;
    UserSessionManager sessionManager;
    EditText amt_ed;
    Spinner crypto_spinner;
    AppCompatRadioButton once_off, every_time, more_than, less_than;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(EditPriceAlert.this);
        setContentView(R.layout.edit_price_alert);

        mapObj=new HashMap<String, String>();
        mapHeader=new HashMap<String, String>();
        sessionManager=new UserSessionManager(EditPriceAlert.this);
        save_btn=findViewById(R.id.save_btn);
        back_iv=findViewById(R.id.back_iv);
        headerText_tv=findViewById(R.id.headerText_tv);
        once_off=findViewById(R.id.once_off);
        every_time=findViewById(R.id.every_time);
        more_than=findViewById(R.id.more_than);
        less_than=findViewById(R.id.less_than);
        amt_ed=findViewById(R.id.amt_ed);
        delete_tv=findViewById(R.id.delete_tv);
        crypto_spinner = (Spinner) findViewById(R.id.crypto_spinner);
        rate_tv=(TextView) findViewById(R.id.rate_tv);

        once_off = (AppCompatRadioButton)findViewById(R.id.once_off);
        every_time = (AppCompatRadioButton)findViewById(R.id.every_time);
        more_than = (AppCompatRadioButton)findViewById(R.id.more_than);
        less_than = (AppCompatRadioButton)findViewById(R.id.less_than);

        headerText_tv.setText("Edit alert");
        amt_ed.setText(""+getIntent().getStringExtra("amount"));

        if(getIntent().getStringExtra("alert_type").equalsIgnoreCase("Once"))
        {
            once_off.setChecked(true);
        }
        else {
            every_time.setChecked(true);
        }
        if(getIntent().getStringExtra("amount_more").equalsIgnoreCase("1"))
        {
            more_than.setChecked(true);
        }
        else {
            less_than.setChecked(true);
        }


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(EditPriceAlert.this,
                R.array.crypto_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        crypto_spinner.setAdapter(adapter);

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(amt_ed.getText().toString().trim().length()<1)
                {
                    amt_ed.setError("Amount Required");
                }else {

                    if(ConnectivityReceiver.isConnected())
                    {
                        Log.d("CreateAlert>>>",once_off.isChecked()+" "+every_time.isChecked()+" "+more_than.isChecked()+" "+less_than.isChecked());
                        showCustomProgrssDialog(EditPriceAlert.this);
                        ConnectivityReceiver.isConnected();
                        mapObj=postData();
                        mapHeader=mapHeaderData();
                        Log.d("BodyData>>>",mapObj.toString());
                        new PostRequest(EditPriceAlert.this,mapObj,mapHeader,EditPriceAlert.this, NetworkUtility.SERVICE_ALERT+"/"+getIntent().getStringExtra("alert_id"),"SERVICE_ALERT");
                    }
                    else {
                        showAlertDialog(getResources().getString(R.string.dlg_nointernet));
                    }


                }

            }
        });

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        delete_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ConnectivityReceiver.isConnected())
                {
                    showCustomProgrssDialog(EditPriceAlert.this);
                    Log.d("DeleteUrl>>>",NetworkUtility.DELETE_SERVICE_ALERT+"/"+getIntent().getStringExtra("alert_id"));
                    new GetRequest(EditPriceAlert.this,EditPriceAlert.this,"DeleteAlert",NetworkUtility.DELETE_SERVICE_ALERT+""+getIntent().getStringExtra("alert_id"));
                }
                else {
                    showAlertDialog(getResources().getString(R.string.dlg_nointernet));
                }


            }
        });

        getCryptoRate();

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
        mapObj.put("amount",""+amt_ed.getText().toString());
        mapObj.put("crypto_id",""+sessionManager.getValues(UserSessionManager.CRYPTO_ID));

        return mapObj;
    }

    public void getCryptoRate()
    {

        if(ConnectivityReceiver.isConnected())
        {
            showCustomProgrssDialog(EditPriceAlert.this);
            new GetRequest(EditPriceAlert.this, EditPriceAlert.this, "getrate", NetworkUtility.GET_BTC_RATE);
        }
        else {
            showAlertDialog(getResources().getString(R.string.dlg_nointernet));
        }


    }

    public Map<String,String> mapHeaderData()
    {
        mapHeader.put("token",sessionManager.getValues(UserSessionManager.KEY_TOKEN));
        return mapHeader;
    }

    @Override
    public void onSuccess(String result, String type) throws JSONException {

        hideCustomProgrssDialog();
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
                    }

                }
                catch (Exception e) {

                }

            }
            break;
            case "SERVICE_ALERT":
            {
                try {
                    Log.d("SreviceAlert>>>",result);
                    JSONObject jObj=new JSONObject(result);
                    String status=jObj.optString("status");
                    String message=jObj.optString("message");
                    if(status.equalsIgnoreCase("true"))
                    {
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(getApplicationContext(), HomeActivity.class);
                        intent.putExtra("price_alert","3");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    else {
                        showAlertDialog(message);
                    }

                }catch (Exception e) {

                }

            }
            break;

            case "DeleteAlert":
                {
                    try {
                        Log.d("DeleteAlert>>>",result);
                        JSONObject jObj=new JSONObject(result);
                        String status=jObj.optString("status");
                        String message=jObj.optString("message");
                        if(status.equalsIgnoreCase("true"))
                        {
                            String resultStatus=jObj.optString("result");
                            if(resultStatus.equalsIgnoreCase("1"))
                            {
                                showAlert(message);
                            }
                        }
                        else {
                            showAlertDialog(message);
                        }

                        Log.d("DeleteResponse",result);
                    }
                    catch (Exception e) {

                    }
                }
            break;
        }

    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

        hideCustomProgrssDialog();
        Log.d("ErrorResponse>>>",responseMessage);

    }


    public void showAlert(final String message) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent=new Intent(getApplicationContext(), HomeActivity.class);
                        intent.putExtra("price_alert","3");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });

        android.support.v7.app.AlertDialog alert = builder.create();
        alert.show();
    }
}
