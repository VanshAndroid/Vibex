package com.crescentek.cryptocurrency.activity.login_signup;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.comman.WalletRecord;
import com.crescentek.cryptocurrency.interfaces.WalletListener;
import com.crescentek.cryptocurrency.model.CryptoM;
import com.crescentek.cryptocurrency.model.CurreencyM;
import com.crescentek.cryptocurrency.model.WalletN;
import com.crescentek.cryptocurrency.utility.BaseActivity;

import net.glxn.qrgen.android.QRCode;

import java.util.List;

/**
 * Created by R.Android on 18-07-2018.
 */

public class ReciveBitCoin extends BaseActivity implements WalletListener {

    private ImageView back_iv;
    private TextView headerText_tv;
    private EditText email_ed;
    private ImageView qrcode_iv;
    String url="";
    String currencyCode="";
    String crypto_id="";
    String crypto_code="";
    //String url="2N57avJF1PfHfjJFJzbPDqvkjD8BHLD3rCV";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(ReciveBitCoin.this);
        setContentView(R.layout.recive_bitcoin);

        new WalletRecord(ReciveBitCoin.this,ReciveBitCoin.this);
        email_ed=findViewById(R.id.email_ed);
        qrcode_iv=findViewById(R.id.qrcode_iv);

        back_iv=findViewById(R.id.back_iv);
        headerText_tv=findViewById(R.id.headerText_tv);

//        generateDefaultCode(url);

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        headerText_tv.setText("Recive BitCoin");

        email_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String valueAmt=email_ed.getText().toString().trim();
                if(valueAmt.length()>0)
                {
                    String newValAmt=url+"("+valueAmt+")";
                    generateDefaultCode(newValAmt);
                }
                else {
                    generateDefaultCode(url);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    public void generateDefaultCode(String value)
    {
        //VCard abhay = new VCard(value);
        Bitmap myBitmap = QRCode.from(value).withSize(250, 250).bitmap();
        qrcode_iv.setImageBitmap(myBitmap);
    }

    @Override
    public void walletList(List<WalletN> walletList) throws Exception {

    }

    @Override
    public void cryptoList(List<CryptoM> cryptoList) throws Exception {

        url=cryptoList.get(0).getCrypto_addr();
        crypto_id=cryptoList.get(0).getCrypto_id();
        crypto_code=cryptoList.get(0).getCrypto_code();

    }

    @Override
    public void curreencyList(List<CurreencyM> curreencyList) throws Exception {

        currencyCode=curreencyList.get(0).getCurrency_code();

        String Qrurl=this.url+"CryptoId="+crypto_id+"CryptoCode="+crypto_code+"CurrencyCode="+currencyCode;
        Log.d("CryptoAddress>>>",Qrurl);
        generateDefaultCode(Qrurl);

        /*String data="2N2XuTrBeMWXHnhA2nbnoYQ7pPvPHw5VzsXCryptoId=1CryptoCode=BTCCurrencyCode=NGN";
        String token=data.substring(0,data.indexOf("CryptoId")-1);
        System.out.println(token);*/


    }
}
