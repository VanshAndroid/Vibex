package com.crescentek.cryptocurrency.activity.deposit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.comman.WalletRecord;
import com.crescentek.cryptocurrency.interfaces.ApiRequestListener;
import com.crescentek.cryptocurrency.interfaces.WalletListener;
import com.crescentek.cryptocurrency.model.CryptoM;
import com.crescentek.cryptocurrency.model.CurreencyM;
import com.crescentek.cryptocurrency.model.WalletN;
import com.crescentek.cryptocurrency.network.GetRequest;
import com.crescentek.cryptocurrency.paystack.MainActivityPayStack;
import com.crescentek.cryptocurrency.utility.BaseActivity;
import com.crescentek.cryptocurrency.utility.ConnectivityReceiver;
import com.crescentek.cryptocurrency.utility.NetworkUtility;
import com.crescentek.cryptocurrency.utility.UserSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by R.Android on 11-07-2018.
 */

public class AmountDeposit extends BaseActivity implements ApiRequestListener {

    private ImageView back_iv;
    private TextView headerText_tv;
    private TextView sendCode_tv;
    EditText amount_ed;
    TextView local_currency;
    UserSessionManager sessionManager;
    TextView depositLimit;
    String remain_bal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(AmountDeposit.this);
        setContentView(R.layout.amount_deposit);

        //new WalletRecord(AmountDeposit.this,AmountDeposit.this);
        sessionManager=new UserSessionManager(getApplicationContext());
        back_iv=findViewById(R.id.back_iv);
        headerText_tv=findViewById(R.id.headerText_tv);
        sendCode_tv=findViewById(R.id.sendCode_tv);
        amount_ed = findViewById(R.id.amount_ed);
        local_currency=findViewById(R.id.local_currency);
        depositLimit=findViewById(R.id.depositLimit);

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        headerText_tv.setText("Enter Amount");

        callDepositLimitApi();

        sendCode_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             if(amount_ed.getText().toString().length()>0) {

                 int inputAmt=Integer.parseInt(amount_ed.getText().toString());
                 double depositLimitM=Double.parseDouble(remain_bal);
                 if (inputAmt<=depositLimitM) {

                     startActivity(new Intent(getApplicationContext(), MainActivityPayStack.class).putExtra("iAmount", amount_ed.getText().toString()));
                 }
                 else {
                     amount_ed.setError("Not more than "+remain_bal);
                 }
             }
             else {
                 amount_ed.setError("Please Enter Amount!!!");
             }
            }
        });

    }

    private void callDepositLimitApi() {

        if(ConnectivityReceiver.isConnected())
        {
            showCustomProgrssDialog(AmountDeposit.this);
            new GetRequest(AmountDeposit.this,AmountDeposit.this,"DepositLimit",NetworkUtility.TRX_LIMIT+"Deposit");
        }
        else {
            showAlertDialog(""+R.string.dlg_nointernet);
        }

    }


    @Override
    public void onSuccess(String result, String type) throws JSONException {

        hideCustomProgrssDialog();
        Log.d("Result>>>",result);
        JSONObject jsonObject=new JSONObject(result);
        String status=jsonObject.optString("status");
        if (status.equalsIgnoreCase("true"))
        {

            remain_bal=jsonObject.optString("remain_bal");
            depositLimit.setText(sessionManager.getValues(UserSessionManager.CURRENCY_CODE)+" "+remain_bal);
            local_currency.setText(sessionManager.getValues(UserSessionManager.CURRENCY_CODE));
        }

    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

    }
}
