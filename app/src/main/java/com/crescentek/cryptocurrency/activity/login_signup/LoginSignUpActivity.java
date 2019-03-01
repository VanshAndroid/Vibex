package com.crescentek.cryptocurrency.activity.login_signup;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.crescentek.cryptocurrency.utility.BaseActivity;
import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.utility.ConnectivityReceiver;
import com.crescentek.cryptocurrency.utility.UserSessionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by R.Android on 25-07-2018.
 */

public class LoginSignUpActivity extends BaseActivity {

    private Button login_btn,signup_btn;
    UserSessionManager sessionManager;
    private String EMAIL_VERIFY="",T_PIN_VERIFY="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarBlue(LoginSignUpActivity.this);
        setContentView(R.layout.login_signup);

        requestRequiredPermission();

        sessionManager=new UserSessionManager(LoginSignUpActivity.this);

        login_btn=findViewById(R.id.login_btn);
        signup_btn=findViewById(R.id.signup_btn);
        EMAIL_VERIFY=sessionManager.getValues(UserSessionManager.KEY_EMAIL_VERIFY);
        T_PIN_VERIFY=sessionManager.getValues(UserSessionManager.KEY_T_PIN_VERIFY);
        Log.d("Validation>>","checkEmail>>>"+EMAIL_VERIFY+"   EmailId>>>"+sessionManager.getValues(UserSessionManager.KEY_EMAIL));


        if(EMAIL_VERIFY!=null)
        {
            if(EMAIL_VERIFY.equalsIgnoreCase("true")&&T_PIN_VERIFY.equalsIgnoreCase("true")) {
                Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            else if(EMAIL_VERIFY.equalsIgnoreCase("true")&&T_PIN_VERIFY.equalsIgnoreCase("false")){
                Intent intent=new Intent(getApplicationContext(),CreatePinActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            else {
                Log.d("Validation","Validation not Matched");
            }

        }


        login_btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(new Intent(getApplicationContext(),LoginActivity.class));
              }
        });

        signup_btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
             }
        });


    }

    private void requestRequiredPermission() {

        int hasREAD_CONTACTSPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        int hasWRITE_CONTACTSPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS);
        int hasACCESS_FINE_LOCATIONPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int hasACCESS_COARSE_LOCATIONPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int hasCAMERAPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int hasREAD_EXTERNAL_STORAGEPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int hasWRITE_EXTERNAL_STORAGEPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);


        List<String> permissions = new ArrayList<String>();
        if (hasREAD_CONTACTSPermission != PackageManager.PERMISSION_GRANTED)
        {
            Log.e("Splash Request", "prepare fine permission");
            permissions.add(Manifest.permission.READ_CONTACTS);
        } else {
            Log.e("Splash Request", "READ_CONTACTS permission granted");
        }
        if (hasWRITE_CONTACTSPermission != PackageManager.PERMISSION_GRANTED) {
            Log.e("Splash Request", "prepare Storage permission");
            permissions.add(Manifest.permission.WRITE_CONTACTS);
        } else {
            Log.e("Splash Request", "WRITE_CONTACTS permission granted");
        }
        if (hasACCESS_FINE_LOCATIONPermission != PackageManager.PERMISSION_GRANTED) {
            Log.e("Splash Request", "prepare Storage permission");
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            Log.e("Splash Request", "ACCESS_FINE_LOCATION permission granted");
        }
        if (hasACCESS_COARSE_LOCATIONPermission != PackageManager.PERMISSION_GRANTED) {
            Log.e("Splash Request", "prepare Storage permission");
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        } else {
            Log.e("Splash Request", "ACCESS_COARSE_LOCATION permission granted");
        }
        if (hasCAMERAPermission != PackageManager.PERMISSION_GRANTED) {
            Log.e("Splash Request", "prepare Storage permission");
            permissions.add(Manifest.permission.CAMERA);
        } else {
            Log.e("Splash Request", "CAMERA permission granted");
        }
        if (hasREAD_EXTERNAL_STORAGEPermission != PackageManager.PERMISSION_GRANTED) {
            Log.e("Splash Request", "prepare Storage permission");
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        } else {
            Log.e("Splash Request", "READ_EXTERNAL_STORAGE permission granted");
        }
        if (hasWRITE_EXTERNAL_STORAGEPermission != PackageManager.PERMISSION_GRANTED) {
            Log.e("Splash Request", "prepare Storage permission");
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else {
            Log.e("Splash Request", "READ_EXTERNAL_STORAGE permission granted");
        }

        if (!permissions.isEmpty()) {
            Log.e("Splash Request", "Requesting Permission..." + permissions.size());
            ActivityCompat.requestPermissions(this, permissions.toArray(new String[permissions.size()]), 1);
        } else {

        }
    }
}
