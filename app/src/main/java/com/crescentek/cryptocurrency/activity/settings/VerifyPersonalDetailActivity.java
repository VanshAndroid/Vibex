package com.crescentek.cryptocurrency.activity.settings;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.interfaces.ApiRequestListener;
import com.crescentek.cryptocurrency.network.PostRequest;
import com.crescentek.cryptocurrency.utility.BaseActivity;
import com.crescentek.cryptocurrency.utility.ConnectivityReceiver;
import com.crescentek.cryptocurrency.utility.NetworkUtility;
import com.crescentek.cryptocurrency.utility.UserSessionManager;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.OnCountryPickerListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by R.Android on 27-06-2018.
 */

public class VerifyPersonalDetailActivity extends BaseActivity implements ApiRequestListener{

    private EditText fname_ed,lname_ed,county_ed,bvn_ed;
    private TextView headerText_tv,dob_tv,email_hint_tv2;
    private ImageView back_iv;
    private LinearLayout dob_layout;
    private Button next_step_btn;
    UserSessionManager sessionManager;

    private Map<String,String> mapobject,mapheader;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(VerifyPersonalDetailActivity.this);
        setContentView(R.layout.verify_personal_detail_activity);

        sessionManager=new UserSessionManager(VerifyPersonalDetailActivity.this);
        mapobject =new HashMap<String, String>();
        mapheader=new HashMap<String, String>();

        initView();

        next_step_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(fname_ed.getText().toString().trim().length()<1)
                {
                    fname_ed.setError("First name cant't be blank");
                }
                else if(lname_ed.getText().toString().trim().length()<1)
                {
                    lname_ed.setError("Last name cant't be blank");
                }
                else if(dob_tv.getText().toString().trim().length()<1)
                {
                    dob_tv.setError("Date Of birth Can't be blank");
                }
                else if(county_ed.getText().toString().trim().length()<1)
                {
                    county_ed.setError("Please select country");
                }
                else if(bvn_ed.getText().toString().trim().length()<10)
                {
                    bvn_ed.setError("Please Enter your BVN number");
                }
                else {

                    showCustomProgrssDialog(VerifyPersonalDetailActivity.this);
                    mapobject=signatureobject();
                    mapheader=headerObject();
                    new PostRequest(VerifyPersonalDetailActivity.this,mapobject,mapheader,VerifyPersonalDetailActivity.this,NetworkUtility.VERIFY_BVN,"VERIFY_BVN");

                    /*startActivity(new Intent(getApplicationContext(),VerifiedActivity.class));
                    finish();*/
                }

            }
        });



        county_ed.setFocusable(false);
        county_ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CountryPicker countryPicker =
                        new CountryPicker.Builder().with(VerifyPersonalDetailActivity.this)
                                .listener(new OnCountryPickerListener() {
                                    @Override public void onSelectCountry(Country country) {
                                        //DO something here
                                        county_ed.setText(country.getName());
                                        Drawable dr = getResources().getDrawable(country.getFlag());
                                        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
                                        // Scale it to 20 x 20
                                        Drawable dw = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 35, 35, true));
                                        county_ed.setCompoundDrawablesWithIntrinsicBounds(null, null, dw, null);

                                    }
                                })
                                .build();
                countryPicker.showDialog(VerifyPersonalDetailActivity.this.getSupportFragmentManager());
            }
        });


        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        headerText_tv.setText("Verify Personal Details");

        dob_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalender();
            }
        });
        dob_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openCalender();
            }
        });
        email_hint_tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalender();
            }
        });

    }

    public void initView()
    {
        headerText_tv=findViewById(R.id.headerText_tv);
        next_step_btn=findViewById(R.id.next_step_btn);
        back_iv=findViewById(R.id.back_iv);
        fname_ed=findViewById(R.id.fname_ed);
        lname_ed=findViewById(R.id.lname_ed);
        dob_tv=findViewById(R.id.dob_tv);
        email_hint_tv2=findViewById(R.id.email_hint_tv2);
        county_ed=findViewById(R.id.county_ed);
        bvn_ed=findViewById(R.id.bvn_ed);
        dob_layout=findViewById(R.id.dob_layout);

        //fname_ed.setEnabled(false);
        //lname_ed.setEnabled(false);
        fname_ed.setText(sessionManager.getValues(UserSessionManager.KEY_FIRST_NAME));
        lname_ed.setText(sessionManager.getValues(UserSessionManager.KEY_LAST_NAME));

        //dob_tv.setEnabled(false);
    }

    public void openCalender()
    {
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.datepicker, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dob_tv.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }


    private Map<String,String> signatureobject() {

        mapobject.put("bvn",""+bvn_ed.getText().toString());
        mapobject.put("dob",""+dob_tv.getText().toString());

        return mapobject;
    }

    private Map<String,String> headerObject() {

        mapheader.put("token",sessionManager.getValues(UserSessionManager.KEY_TOKEN));

        return mapheader;
    }


    @Override
    public void onSuccess(String result, String type) throws JSONException {

        Log.d("ResponseResult>>>",result);
        hideCustomProgrssDialog();
        try {

            JSONObject jsonObject=new JSONObject(result);
            String status = jsonObject.optString("status");
            String message=jsonObject.optString("message");
            if (status.equalsIgnoreCase("success"))
            {
                startActivity(new Intent(getApplicationContext(),VerifiedActivity.class));
                finish();
            }else {
                showAlertDialog(message);
            }
        }catch (Exception e){

        }

    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

    }


}
