package com.crescentek.cryptocurrency.activity.login_signup;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.crescentek.cryptocurrency.network.PostRequest;
import com.crescentek.cryptocurrency.utility.BaseActivity;
import com.crescentek.cryptocurrency.utility.ConnectivityReceiver;
import com.crescentek.cryptocurrency.utility.NetworkUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by R.Android on 05-10-2018.
 */

public class PasswordReset extends BaseActivity implements ApiRequestListener {

    TextView password_tv,password_tv1;
    EditText password_ed,password_ed1;
    Button reset_pwd;
    View password_view,password_view1;
    ImageView back_iv;
    TextView headerText_tv;

    Map<String,String> mapPost,mapHeader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(PasswordReset.this);
        setContentView(R.layout.password_reset);
        initViews();

        mapPost=new HashMap<String, String>();

        mapHeader=new HashMap<String, String>();

        headerText_tv.setText("Reset Password");

        password_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(password_ed.getText().toString().length()<=1)
                {
                    password_tv.setTextColor(getResources().getColor(R.color.dark_gray));
                    password_ed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_password_dark_gray_24dp, 0);
                    password_view.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                }
                else {

                    password_tv.setTextColor(getResources().getColor(R.color.sky));
                    password_ed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_password_blue_24dp, 0);
                    password_view.setBackgroundColor(getResources().getColor(R.color.sky));
                }

            }
        });

        password_ed1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(password_ed1.getText().toString().trim().length()>1)
                {
                    if (password_ed.getText().toString().trim().equals(password_ed1.getText().toString().trim()))
                    {
                        password_tv1.setTextColor(getResources().getColor(R.color.sky));
                        password_ed1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_password_blue_24dp, 0);
                        password_view1.setBackgroundColor(getResources().getColor(R.color.sky));
                    }
                    else {
                        password_tv1.setTextColor(getResources().getColor(R.color.dark_gray));
                        password_ed1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_password_dark_gray_24dp, 0);
                        password_view1.setBackgroundColor(getResources().getColor(R.color.dark_gray));
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

        reset_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(password_ed.getText().toString().trim().length()<1)
                {
                    password_ed.setError("Password can't be blank");
                }else if(password_ed1.getText().toString().trim().length()<1) {
                    password_ed1.setError("Password can't be blank");
                }
                else if(!password_ed.getText().toString().trim().equals(password_ed1.getText().toString().trim()))
                {
                    showAlertDialog("Password Not Matched!!!");
                }
                else {
                    //Toast.makeText(getApplicationContext(),"Okay",Toast.LENGTH_LONG).show();
                    callResetApi();

                }

            }
        });

    }

    public void initViews()
    {
        password_tv=findViewById(R.id.password_tv);
        password_tv1=findViewById(R.id.password_tv1);
        password_ed=findViewById(R.id.password_ed);
        password_ed1=findViewById(R.id.password_ed1);
        password_view=findViewById(R.id.password_view);
        password_view1=findViewById(R.id.password_view1);
        back_iv=findViewById(R.id.back_iv);
        headerText_tv=findViewById(R.id.headerText_tv);
        reset_pwd=findViewById(R.id.reset_pwd);
    }

    public void callResetApi()
    {
        if(ConnectivityReceiver.isConnected())
        {
            showCustomProgrssDialog(PasswordReset.this);
            ConnectivityReceiver.isConnected();
            mapPost=postData();
            mapHeader=tokenPost();
            Log.d("Values>>>",mapPost+"\n"+mapHeader+"\n"+NetworkUtility.PASSWORD_USER);
            new PostRequest(PasswordReset.this,mapPost,mapHeader,PasswordReset.this, NetworkUtility.PASSWORD_USER,"PASSWORD_USER");
        }
        else {
            showAlertDialog(getResources().getString(R.string.dlg_nointernet));
        }

    }

    public Map<String,String> postData()
    {
        mapPost.put("password",password_ed1.getText().toString().trim());
        return mapPost;
    }

    public Map<String,String> tokenPost()
    {
        mapHeader.put("token",getIntent().getStringExtra("token"));
        return mapHeader;
    }

    @Override
    public void onSuccess(String result, String type) throws JSONException {

        hideCustomProgrssDialog();
        Log.d("ResetPassword>>>",result);
        try {
            JSONObject jObj=new JSONObject(result);
            String status=jObj.optString("status");
            String message=jObj.optString("message");
            if(status.equalsIgnoreCase("true"))
            {
                boolean alertCheck=showAlertDialogWithType(message);
                if(alertCheck)
                {
                    boolean returnStatus=showAlertDialogWithType(message);
                    if(returnStatus)
                    {
                        Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                }
            }else {
                showAlertDialog(message);
            }
        }catch (Exception e)
        {

        }

    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

        hideCustomProgrssDialog();
        Log.d("ResetPassword>>>",responseMessage+"  "+responseCode);

    }
}
