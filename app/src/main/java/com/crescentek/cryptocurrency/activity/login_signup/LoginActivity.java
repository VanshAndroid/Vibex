package com.crescentek.cryptocurrency.activity.login_signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.interfaces.ApiRequestListener;
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
 * Created by R.Android on 26-07-2018.
 */

public class LoginActivity extends BaseActivity implements ApiRequestListener{

    private ImageView back_iv;
    private TextView headerText_tv,email_hint_tv,password_tv,forgot_tv;
    private EditText email_ed,password_ed;
    private View email_view,password_view;
    private Button login_btn;
    Map<String, String> mapobject;
    UserSessionManager sessionManager;
    ImageView password_visible;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(LoginActivity.this);
        setContentView(R.layout.login_activity);
        mapobject=new HashMap<String, String>();
        sessionManager=new UserSessionManager(LoginActivity.this);

        initView();

        password_visible.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    password_ed.setInputType(InputType.TYPE_CLASS_TEXT);
                    password_visible.setImageResource(R.drawable.ic_blue_eye_24dp);

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    password_ed.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password_visible.setImageResource(R.drawable.ic_gray_eye_24dp);

                }

                return true;
            }
        });

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        email_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(email_ed.getText().toString().length()<=1)
                {
                    email_hint_tv.setTextColor(getResources().getColor(R.color.dark_gray));
                    email_ed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_email_dark_gray_24dp, 0);
                    email_view.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                }
                else {

                    email_hint_tv.setTextColor(getResources().getColor(R.color.sky));
                    email_ed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_email_sky_blue_24dp, 0);
                    email_view.setBackgroundColor(getResources().getColor(R.color.sky));
                }

            }
        });

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
                    //password_ed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_password_dark_gray_24dp, 0);
                    password_view.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                }
                else {
                    password_tv.setTextColor(getResources().getColor(R.color.sky));
                    //password_ed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_password_blue_24dp, 0);
                    password_view.setBackgroundColor(getResources().getColor(R.color.sky));

                }

            }

        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validEmail(email_ed.getText().toString().trim())) {
                    email_ed.setError("A valid email id needed");
                }
                else if(password_ed.getText().toString().trim().length()<1) {
                    password_ed.setError("Password Can't be blank");
                }
                else {

                    if(ConnectivityReceiver.isConnected())
                    {

                        showCustomProgrssDialog(LoginActivity.this);
                        mapobject = signatureobject("", "");
                        new PostRequest(LoginActivity.this,mapobject,LoginActivity.this, NetworkUtility.LOGIN,"Login");
                    }
                    else {
                        showAlertDialog(getResources().getString(R.string.dlg_nointernet));
                    }



                }

            }
        });

        forgot_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),ForgotPassword.class));
                //startActivity(new Intent(getApplicationContext(),PasswordReset.class));
            }
        });

    }

    public void initView()
    {

        back_iv=findViewById(R.id.back_iv);
        headerText_tv=findViewById(R.id.headerText_tv);
        email_hint_tv=findViewById(R.id.email_hint_tv);
        password_tv=findViewById(R.id.password_tv);

        email_ed=findViewById(R.id.email_ed);
        password_ed=findViewById(R.id.password_ed);

        email_view=findViewById(R.id.email_view);
        password_view=findViewById(R.id.password_view);

        login_btn=findViewById(R.id.login_btn);
        forgot_tv=findViewById(R.id.forgot_tv);

        password_visible=findViewById(R.id.password_visible);

        headerText_tv.setText("Login");

    }

    private boolean validEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private Map<String,String> signatureobject(String s, String s1) {

        mapobject.put("email",""+ email_ed.getText().toString());
        mapobject.put("password",""+password_ed.getText().toString());

        return mapobject;
    }

    @Override
    public void onSuccess(String result, String type) throws JSONException {

        Log.d("Login_response>>>",result);
        hideCustomProgrssDialog();
        try {
            JSONObject jObj=new JSONObject(result);
            String status=jObj.optString("status");
            String message=jObj.optString("message");
            if(status.equalsIgnoreCase("Success")||status.equalsIgnoreCase("true"))
            {
                JSONObject jObjData=jObj.optJSONObject("data");
                String isEmailVery=jObjData.optString("isEmailVery");
                String isPhoneVery=jObjData.optString("isPhoneVery");
                String isBvnVerified=jObjData.optString("isBvnVerified");
                String isPinSet=jObjData.optString("isPinSet");
                String isDocUploaded=jObjData.optString("isDocUploaded");
                String isTwoFASet=jObjData.optString("isTwoFASet");
                String user_level=jObjData.optString("user_level");
                String user_level_id=jObjData.optString("user_level_id");
                String token=jObjData.optString("token");

                String firstname=jObjData.optString("firstname");
                String lastname=jObjData.optString("lastname");
                String email=jObjData.optString("email");
                String phone=jObjData.optString("phone");


                sessionManager.setValues(UserSessionManager.KEY_TOKEN,token);

                if(isEmailVery.equalsIgnoreCase("true")&&isPinSet.equalsIgnoreCase("true")&&isTwoFASet.equalsIgnoreCase("false"))
                {

                    sessionManager.setValues(UserSessionManager.KEY_EMAIL,email_ed.getText().toString());
                    sessionManager.setValues(UserSessionManager.KEY_EMAIL_VERIFY,isEmailVery);
                    sessionManager.setValues(UserSessionManager.KEY_PHONE_VERIFY,isPhoneVery);
                    sessionManager.setValues(UserSessionManager.KEY_BVN_VERIFY,isBvnVerified);
                    sessionManager.setValues(UserSessionManager.KEY_T_PIN_VERIFY,isPinSet);
                    sessionManager.setValues(UserSessionManager.KEY_DOC_UPLOADED,isDocUploaded);
                    sessionManager.setValues(UserSessionManager.KEY_TWO_FACTER,isTwoFASet);
                    sessionManager.setValues(UserSessionManager.KEY_USER_LEVEL,user_level);
                    sessionManager.setValues(UserSessionManager.KEY_USER_LEVEL_ID,user_level_id);
                    sessionManager.setValues(UserSessionManager.KEY_TOKEN,token);
                    sessionManager.setValues(UserSessionManager.KEY_FIRST_NAME,firstname);
                    sessionManager.setValues(UserSessionManager.KEY_LAST_NAME,lastname);
                    sessionManager.setValues(UserSessionManager.KEY_PHONE,phone);

                    Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                else if(isEmailVery.equalsIgnoreCase("true")&&isPinSet.equalsIgnoreCase("false")&&isTwoFASet.equalsIgnoreCase("false"))
                {
                    sessionManager.setValues(UserSessionManager.KEY_EMAIL,email_ed.getText().toString());
                    sessionManager.setValues(UserSessionManager.KEY_EMAIL_VERIFY,isEmailVery);
                    sessionManager.setValues(UserSessionManager.KEY_PHONE_VERIFY,isPhoneVery);
                    sessionManager.setValues(UserSessionManager.KEY_BVN_VERIFY,isBvnVerified);
                    sessionManager.setValues(UserSessionManager.KEY_T_PIN_VERIFY,isPinSet);
                    sessionManager.setValues(UserSessionManager.KEY_DOC_UPLOADED,isDocUploaded);
                    sessionManager.setValues(UserSessionManager.KEY_TWO_FACTER,isTwoFASet);
                    sessionManager.setValues(UserSessionManager.KEY_USER_LEVEL,user_level);
                    sessionManager.setValues(UserSessionManager.KEY_USER_LEVEL_ID,user_level_id);
                    sessionManager.setValues(UserSessionManager.KEY_TOKEN,token);
                    sessionManager.setValues(UserSessionManager.KEY_FIRST_NAME,firstname);
                    sessionManager.setValues(UserSessionManager.KEY_LAST_NAME,lastname);
                    sessionManager.setValues(UserSessionManager.KEY_PHONE,phone);

                    Intent intent=new Intent(getApplicationContext(),CreatePinActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
                else {
                        if(isTwoFASet.equalsIgnoreCase("true"))
                        {

                            Intent intent=new Intent(getApplicationContext(),TwoFaLogin.class);
                            intent.putExtra("KEY_EMAIL",email_ed.getText().toString());
                            intent.putExtra("KEY_EMAIL_VERIFY",isEmailVery);
                            intent.putExtra("KEY_PHONE_VERIFY",isPhoneVery);
                            intent.putExtra("KEY_BVN_VERIFY",isBvnVerified);
                            intent.putExtra("KEY_T_PIN_VERIFY",isPinSet);
                            intent.putExtra("KEY_DOC_UPLOADED",isDocUploaded);
                            intent.putExtra("KEY_TWO_FACTER",isTwoFASet);
                            intent.putExtra("KEY_USER_LEVEL",user_level);
                            intent.putExtra("KEY_USER_LEVEL_ID",user_level_id);
                            intent.putExtra("KEY_TOKEN",token);
                            intent.putExtra("KEY_FIRST_NAME",firstname);
                            intent.putExtra("KEY_LAST_NAME",lastname);
                            intent.putExtra("KEY_PHONE",phone);
                            startActivity(intent);

                        }
                        else {

                            sessionManager.setValues(UserSessionManager.KEY_EMAIL,email_ed.getText().toString());
                            sessionManager.setValues(UserSessionManager.KEY_EMAIL_VERIFY,isEmailVery);
                            sessionManager.setValues(UserSessionManager.KEY_PHONE_VERIFY,isPhoneVery);
                            sessionManager.setValues(UserSessionManager.KEY_BVN_VERIFY,isBvnVerified);
                            sessionManager.setValues(UserSessionManager.KEY_T_PIN_VERIFY,isPinSet);
                            sessionManager.setValues(UserSessionManager.KEY_DOC_UPLOADED,isDocUploaded);
                            sessionManager.setValues(UserSessionManager.KEY_TWO_FACTER,isTwoFASet);
                            sessionManager.setValues(UserSessionManager.KEY_USER_LEVEL,user_level);
                            sessionManager.setValues(UserSessionManager.KEY_USER_LEVEL_ID,user_level_id);
                            sessionManager.setValues(UserSessionManager.KEY_TOKEN,token);
                            sessionManager.setValues(UserSessionManager.KEY_FIRST_NAME,firstname);
                            sessionManager.setValues(UserSessionManager.KEY_LAST_NAME,lastname);
                            sessionManager.setValues(UserSessionManager.KEY_PHONE,phone);

                            Intent intent=new Intent(getApplicationContext(),SignInConfirmation.class);
                            startActivity(intent);
                        }

                }
            }
            else {
                showAlertDialog(message);
            }

        }catch (Exception e) {

        }

    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

        Log.d("Login_response>>>",responseMessage);
        hideCustomProgrssDialog();

    }
}
