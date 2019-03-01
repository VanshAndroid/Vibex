package com.crescentek.cryptocurrency.paystack;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.activity.deposit.DepositSuccess;
import com.crescentek.cryptocurrency.activity.deposit.MakeDeposit;
import com.crescentek.cryptocurrency.interfaces.ApiRequestListener;
import com.crescentek.cryptocurrency.network.PostRequest;
import com.crescentek.cryptocurrency.utility.BaseActivity;
import com.crescentek.cryptocurrency.utility.ConnectivityReceiver;
import com.crescentek.cryptocurrency.utility.NetworkUtility;
import com.crescentek.cryptocurrency.utility.UserSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.exceptions.ExpiredAccessCodeException;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;

public class MainActivityPayStack extends BaseActivity implements ApiRequestListener {


    String backend_url = "https://demopaystack.herokuapp.com";
    String paystack_public_key = "pk_test_6c5255038c89968ce5d4f972b4b962826edc4b65";

    EditText mEditCardNum;
    EditText mEditCVC;
    EditText mEditExpiryMonth;
    EditText mEditExpiryYear;

    TextView mTextError;
    TextView mTextBackendMessage;

    ProgressDialog dialog;
    private TextView mTextReference;
    private Charge charge;
    private Transaction transaction;

    private ImageView back_iv;
    private TextView headerText_tv;

    String sAmount = "";

    private Map<String,String> mapobject,mapheader;
    UserSessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(MainActivityPayStack.this);
        setContentView(R.layout.payment);

        mapobject=new HashMap<String,String>();
        mapheader=new HashMap<String,String>();
        sessionManager=new UserSessionManager(MainActivityPayStack.this);

        sAmount = getIntent().getStringExtra("iAmount");
        Log.d(">>Amount",sAmount);

        PaystackSdk.setPublicKey(paystack_public_key);

        mEditCardNum = findViewById(R.id.edit_card_number);
        mEditCVC = findViewById(R.id.edit_cvc);
        mEditExpiryMonth = findViewById(R.id.edit_expiry_month);
        mEditExpiryYear = findViewById(R.id.edit_expiry_year);
        back_iv=findViewById(R.id.back_iv);
        headerText_tv=findViewById(R.id.headerText_tv);
        headerText_tv.setText("Payment Page");
        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button mButtonPerformTransaction = findViewById(R.id.button_perform_transaction);
        Button mButtonPerformLocalTransaction = findViewById(R.id.button_perform_local_transaction);

        mTextError = findViewById(R.id.textview_error);
        mTextBackendMessage = findViewById(R.id.textview_backend_message);
        mTextReference = findViewById(R.id.textview_reference);

        //initialize sdk
        PaystackSdk.initialize(getApplicationContext());

        //set click listener
        mButtonPerformTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    startAFreshCharge(false);
                } catch (Exception e) {
                    MainActivityPayStack.this.mTextError.setText(String.format("An error occurred while charging card: %s %s", e.getClass().getSimpleName(), e.getMessage()));

                }
            }
        });
        mButtonPerformLocalTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    if(mEditCardNum.getText().toString().length()<1)
                    {
                        mEditCardNum.setError("Card number can't be blank");
                    }else if(mEditCVC.getText().toString().length()<2)
                    {
                        mEditCVC.setError("CVV number shold be 3 digit");
                    }
                    else if(mEditExpiryMonth.getText().toString().length()<1)
                    {
                        mEditExpiryMonth.setError("Month can't be blank");
                    }
                    else if(mEditExpiryYear.getText().toString().length()<1)
                    {
                        mEditExpiryYear.setError("Year can't be blank");
                    }
                    else {
                        startAFreshCharge(true);
                    }

                } catch (Exception e) {
                    MainActivityPayStack.this.mTextError.setText(String.format("An error occurred while charging card: %s %s", e.getClass().getSimpleName(), e.getMessage()));

                }
            }
        });
    }

    private void startAFreshCharge(boolean local) {
        // initialize the charge
        charge = new Charge();
        charge.setCard(loadCardFromForm());

        dialog = new ProgressDialog(MainActivityPayStack.this);
        dialog.setMessage("Performing transaction... please wait");
        dialog.show();

        if (local) {
            // Set transaction params directly in app (note that these params
            // are only used if an access_code is not set. In debug mode,
            // setting them after setting an access code would throw an exception

            // default 2000
            charge.setAmount(Integer.parseInt(sAmount));
            charge.setEmail("help@paystack.co");
            charge.setReference("" + Calendar.getInstance().getTimeInMillis());

            Log.d(">>REF>>","" + Calendar.getInstance().getTimeInMillis());

            try {
                charge.putCustomField("Charged From", "Android SDK");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            chargeCard();
        } else {
            // Perform transaction/initialize on our server to get an access code
            // documentation: https://developers.paystack.co/reference#initialize-a-transaction
            new fetchAccessCodeFromServer().execute(backend_url + "/new-access-code");
        }
    }

    /**
     * Method to validate the form, and set errors on the edittexts.
     */
    private Card loadCardFromForm() {
        //validate fields
        Card card;

        String cardNum = mEditCardNum.getText().toString().trim();

        //build card object with ONLY the number, update the other fields later
        card = new Card.Builder(cardNum, 0, 0, "").build();
        String cvc = mEditCVC.getText().toString().trim();
        //update the cvc field of the card
        card.setCvc(cvc);

        //validate expiry month;
        String sMonth = mEditExpiryMonth.getText().toString().trim();
        int month = 0;
        try {
            month = Integer.parseInt(sMonth);
        } catch (Exception ignored) {
        }

        card.setExpiryMonth(month);

        String sYear = mEditExpiryYear.getText().toString().trim();
        int year = 0;
        try {
            year = Integer.parseInt(sYear);
        } catch (Exception ignored) {
        }
        card.setExpiryYear(year);

        return card;
    }

    @Override
    public void onPause() {
        super.onPause();

        if ((dialog != null) && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = null;
    }

    private void chargeCard() {
        transaction = null;
        PaystackSdk.chargeCard(MainActivityPayStack.this, charge, new Paystack.TransactionCallback() {
            // This is called only after transaction is successful
            @Override
            public void onSuccess(Transaction transaction) {
                dismissDialog();

                MainActivityPayStack.this.transaction = transaction;
                mTextError.setText(" ");
                Toast.makeText(MainActivityPayStack.this, transaction.getReference(), Toast.LENGTH_LONG).show();
                calltransactionReferenceApi(transaction.getReference());

            }

            @Override
            public void beforeValidate(Transaction transaction) {
                MainActivityPayStack.this.transaction = transaction;
                Toast.makeText(MainActivityPayStack.this, transaction.getReference(), Toast.LENGTH_LONG).show();
                updateTextViews();
            }

            @Override
            public void onError(Throwable error, Transaction transaction) {
                // If an access code has expired, simply ask your server for a new one
                // and restart the charge instead of displaying error
                MainActivityPayStack.this.transaction = transaction;
                if (error instanceof ExpiredAccessCodeException) {
                    MainActivityPayStack.this.startAFreshCharge(false);
                    MainActivityPayStack.this.chargeCard();
                    return;
                }

                dismissDialog();

                if (transaction.getReference() != null) {
                    Toast.makeText(MainActivityPayStack.this, transaction.getReference() + " concluded with error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    mTextError.setText(String.format("%s  concluded with error: %s %s", transaction.getReference(), error.getClass().getSimpleName(), error.getMessage()));
                    new verifyOnServer().execute(transaction.getReference());
                } else {
                    Toast.makeText(MainActivityPayStack.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    mTextError.setText(String.format("Error: %s %s", error.getClass().getSimpleName(), error.getMessage()));
                }
                updateTextViews();
            }

        });
    }

    private void dismissDialog() {
        if ((dialog != null) && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void updateTextViews() {
        if (transaction.getReference() != null) {
            mTextReference.setText(String.format("Reference: %s", transaction.getReference()));
        } else {
            mTextReference.setText("No transaction");
        }
    }



    private boolean isEmpty(String s) {
        return s == null || s.length() < 1;
    }


    public void calltransactionReferenceApi(String referance)
    {

        mapobject=new HashMap<String, String>();
        mapheader=new HashMap<String, String>();
        showCustomProgrssDialog(MainActivityPayStack.this);
        ConnectivityReceiver.isConnected();
        mapobject = signatureobject();
        mapheader=headerObject();
        new PostRequest(MainActivityPayStack.this,mapobject,mapheader,MainActivityPayStack.this, NetworkUtility.TRANSACTION_REFERANCE+referance,"Referance");

    }

    private Map<String,String> signatureobject() {

        return mapobject;
    }

    private Map<String,String> headerObject() {

        mapheader.put("token",sessionManager.getValues(UserSessionManager.KEY_TOKEN));
        Log.d("token>>>",sessionManager.getValues(UserSessionManager.KEY_TOKEN));

        return mapheader;
    }

    @Override
    public void onSuccess(String result, String type) throws JSONException {

        Log.d("T_response>>>",result);
        hideCustomProgrssDialog();
        try {
            JSONObject job=new JSONObject(result);
            String status=job.optString("status");
            if(status.equalsIgnoreCase("true"))
            {

                double amount_d=Double.parseDouble(job.optString("amount"));
                double fees_d=Double.parseDouble(job.optString("fees"));
                double wallet_amount=amount_d-fees_d;

                Log.d("WalletDeposit>>>",wallet_amount+"");

                //Intent intent=new Intent(getApplicationContext(), MakeDeposit.class);
                Intent intent=new Intent(getApplicationContext(), DepositSuccess.class);
                intent.putExtra("amount",job.optString("amount"));
                intent.putExtra("fees",job.optString("fees"));
                intent.putExtra("trx_ref",job.optString("trx_ref"));
                intent.putExtra("wallet",wallet_amount+"");

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                //finish();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

    }

    private class fetchAccessCodeFromServer extends AsyncTask<String, Void, String> {
        private String error;

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                charge.setAccessCode(result);
                chargeCard();
            } else {
                MainActivityPayStack.this.mTextBackendMessage.setText(String.format("There was a problem getting a new access code form the backend: %s", error));
                dismissDialog();
            }
        }

        @Override
        protected String doInBackground(String... ac_url) {
            try {
                URL url = new URL(ac_url[0]);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                url.openStream()));

                String inputLine;
                inputLine = in.readLine();
                in.close();
                return inputLine;
            } catch (Exception e) {
                error = e.getClass().getSimpleName() + ": " + e.getMessage();
            }
            return null;
        }
    }

    private class verifyOnServer extends AsyncTask<String, Void, String> {
        private String reference;
        private String error;

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                MainActivityPayStack.this.mTextBackendMessage.setText(String.format("Gateway response: %s", result));

            } else {
                MainActivityPayStack.this.mTextBackendMessage.setText(String.format("There was a problem verifying %s on the backend: %s ", this.reference, error));
                dismissDialog();
            }
        }

        @Override
        protected String doInBackground(String... reference) {
            try {
                this.reference = reference[0];
                URL url = new URL(backend_url + "/verify/" + this.reference);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                url.openStream()));

                String inputLine;
                inputLine = in.readLine();
                in.close();
                return inputLine;
            } catch (Exception e) {
                error = e.getClass().getSimpleName() + ": " + e.getMessage();
            }
            return null;
        }
    }
}
