package com.crescentek.cryptocurrency.activity.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.interfaces.ApiRequestListener;
import com.crescentek.cryptocurrency.model.CountryM;
import com.crescentek.cryptocurrency.network.GetRequest;
import com.crescentek.cryptocurrency.network.PostRequest;
import com.crescentek.cryptocurrency.utility.BaseActivity;
import com.crescentek.cryptocurrency.utility.ConnectivityReceiver;
import com.crescentek.cryptocurrency.utility.NetworkUtility;
import com.crescentek.cryptocurrency.utility.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by R.Android on 21-06-2018.
 */

public class MobileNumberVerificationActivity extends BaseActivity implements ApiRequestListener{


    private TextView headerText_tv;
    private ImageView back_iv;
    private EditText mob_no_ed;
    TextView country_code_ed;
    String mobile_no;
    private Button sendCode_btn;

    private List<CountryM> areaList;
    ArrayList<String> country_code_name;
    ArrayList<String> countryList;

    private Map<String,String> mapobject,mapheader;
    UserSessionManager sessionManager;

//mob_no_ed.setSelection(mob_no_ed.getText().length());
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(MobileNumberVerificationActivity.this);
        setContentView(R.layout.mob_verification_activity);
        areaList = new ArrayList<CountryM>();
        country_code_name = new ArrayList<String>();
        countryList = new ArrayList<String>();
        mapobject =new HashMap<String, String>();
        mapheader=new HashMap<String, String>();
        sessionManager=new UserSessionManager(MobileNumberVerificationActivity.this);

        initView();

        callCountryApi();

        mob_no_ed.setText(getIntent().getStringExtra("mobile"));
        mob_no_ed.setSelection(mob_no_ed.getText().length());

        country_code_ed.setText("+"+getIntent().getStringExtra("phonecode"));

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mob_no_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (mob_no_ed.getText().toString().length()<=9)
                {
                    sendCode_btn.setBackground(getResources().getDrawable(R.drawable.rounded_shaddow));
                    sendCode_btn.setEnabled(false);
                }else {
                    sendCode_btn.setBackground(getResources().getDrawable(R.drawable.rounded_yellow));
                    sendCode_btn.setEnabled(true);
                }

            }
        });

        sendCode_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(country_code_ed.getText().toString().trim().length()<1)
                {
                    country_code_ed.setError("Country code can't be blank");

                } else if(mob_no_ed.getText().toString().trim().length()<1) {

                    mob_no_ed.setError("Phone Number can't be blank");
                }
                else {

                    showCustomProgrssDialog(MobileNumberVerificationActivity.this);
                    ConnectivityReceiver.isConnected();
                    mapobject = signatureobject();
                    mapheader=headerObject();
                    new PostRequest(MobileNumberVerificationActivity.this,mapobject,mapheader,MobileNumberVerificationActivity.this,NetworkUtility.SEND_ACTIVATION_OTP,"SendOtp");
                }

            }
        });

        country_code_ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

    }

    public void callCountryApi()
    {
        showCustomProgrssDialog(MobileNumberVerificationActivity.this);
        new GetRequest(MobileNumberVerificationActivity.this,MobileNumberVerificationActivity.this,"country", NetworkUtility.GET_COUNTRY);
    }

    public void initView()
    {
        headerText_tv=findViewById(R.id.headerText_tv);
        back_iv=findViewById(R.id.back_iv);
        country_code_ed=findViewById(R.id.country_code_ed);
        mob_no_ed=findViewById(R.id.mob_no_ed);
        sendCode_btn= findViewById(R.id.sendCode_btn);
        headerText_tv.setText("Mobile Number");

        country_code_ed.setEnabled(true);


    }


    private Map<String,String> signatureobject() {

        //mapobject.put("phone",""+country_code_ed.getText().toString().trim()+""+mob_no_ed.getText().toString().trim());
        mapobject.put("phone",""+mob_no_ed.getText().toString().trim());

        return mapobject;
    }

    private Map<String,String> headerObject() {

        mapheader.put("token",sessionManager.getValues(UserSessionManager.KEY_TOKEN));

        return mapheader;
    }



    @Override
    public void onSuccess(String result, String type) throws JSONException {

        hideCustomProgrssDialog();
        Log.d("Response>>>",result);
        try {

            switch (type)
            {
                case "country":
                    {
                        Log.d("CountryM>>",result);
                        JSONObject jObject=new JSONObject(result);
                        String status=jObject.optString("status");
                        if(status.equalsIgnoreCase("success"))
                        {
                            JSONArray jsonArray=jObject.optJSONArray("data");
                            if(jsonArray.length()   >0)
                            {
                                for (int i=0;i<jsonArray.length();i++)
                                {
                                    CountryM country=new CountryM();
                                    JSONObject jobj = jsonArray.getJSONObject(i);
                                    country.setCountry_id(jobj.optString("country_id"));
                                    country.setCountry(jobj.optString("country"));
                                    country.setPhonecode(jobj.optString("phonecode"));
                                    country.setCurrency(jobj.optString("currency"));
                                    country.setCurrency_code(jobj.optString("currency_code"));
                                    country.setCurrency_symbol(jobj.optString("currency_symbol"));
                                    country_code_name.add(jobj.optString("phonecode")+" "+jobj.optString("country"));
                                    countryList.add("+"+jobj.optString("phonecode"));
                                    areaList.add(country);
                                }

                            }
                        }
                    }
                    break;
                case "SendOtp":
                    {
                        JSONObject jObject=new JSONObject(result);
                        String status=jObject.optString("status");
                        String message=jObject.optString("message");
                        if(status.equalsIgnoreCase("success"))
                        {
                            JSONObject data=jObject.optJSONObject("data");
                            String tokenM=data.optString("token");
                            String users_id=data.optString("users_id");
                            String code=data.optString("code");

                            sessionManager.setValues(UserSessionManager.KEY_USER_ID,users_id);
                            sessionManager.setValues(UserSessionManager.KEY_TOKEN_PHONE,tokenM);
                            sessionManager.setValues(UserSessionManager.KEY_PHONE,""+country_code_ed.getText().toString().trim()+""+mob_no_ed.getText().toString().trim());
                            Toast.makeText(getApplicationContext(),""+message,Toast.LENGTH_LONG).show();

                            Intent intent=new Intent(MobileNumberVerificationActivity.this,MobConfirmationActivity.class);
                            intent.putExtra("code",code);
                            intent.putExtra("phone",""+country_code_ed.getText().toString().trim()+""+mob_no_ed.getText().toString().trim());
                            intent.putExtra("country_code",country_code_ed.getText().toString().trim());
                            intent.putExtra("phone_1",mob_no_ed.getText().toString());
                            startActivity(intent);
                            finish();

                        }
                        else {
                            Toast.makeText(getApplicationContext(),""+message,Toast.LENGTH_LONG).show();
                        }
                    }
                    break;

            }

        }
        catch (Exception e)
        {

        }

    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

    }


    public void selectTitle(ArrayList<String> country_code) {

        final CharSequence[] items = country_code.toArray(new CharSequence[country_code.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Country Code");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                country_code_ed.setText(countryList.get(item));
                dialog.dismiss();
            }
        });

        builder.show();

    }
}
