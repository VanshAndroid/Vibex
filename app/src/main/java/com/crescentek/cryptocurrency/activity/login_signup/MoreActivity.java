package com.crescentek.cryptocurrency.activity.login_signup;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.activity.QrCode.QrcodeActivity;
import com.crescentek.cryptocurrency.activity.Exchangebuysell.ExchangeBuySell;
import com.crescentek.cryptocurrency.activity.TransCheckingExchange;
import com.crescentek.cryptocurrency.activity.TransactionChecking;
import com.crescentek.cryptocurrency.activity.buysell.BuyBitCoin;
import com.crescentek.cryptocurrency.activity.buysell.BuyCrypto;
import com.crescentek.cryptocurrency.activity.buysell.SellBitCoin;
import com.crescentek.cryptocurrency.activity.buysell.SellCrypto;
import com.crescentek.cryptocurrency.activity.deposit.DepositMethod;
import com.crescentek.cryptocurrency.activity.withdraw.WithDrawActivity;
import com.crescentek.cryptocurrency.utility.BaseActivity;
import com.crescentek.cryptocurrency.utility.UserSessionManager;

/**
 * Created by R.Android on 10-07-2018.
 */

public class MoreActivity extends Activity {

    LinearLayout menuView,withDraw,deposit,buy,sell,scanQrCode,exchange_buy,exchange_sell,reciveBitcoin;
    UserSessionManager sessionManager;

    BaseActivity baseActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseActivity= new BaseActivity();
        baseActivity.setStatusTransparent(MoreActivity.this);
        overridePendingTransition(R.anim.slide_up_anim,R.anim.slide_down_anim);
        setContentView(R.layout.more_menu_layout);
        sessionManager=new UserSessionManager(getApplicationContext());
        menuView=findViewById(R.id.menuView);
        withDraw=findViewById(R.id.withDraw);
        deposit=findViewById(R.id.deposit);
        buy=findViewById(R.id.buy);
        sell=findViewById(R.id.sell);
        scanQrCode=findViewById(R.id.scanQrCode);
        exchange_buy=findViewById(R.id.exchange_buy);
        exchange_sell=findViewById(R.id.exchange_sell);
        reciveBitcoin=findViewById(R.id.reciveBitcoin);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                menuView.setBackgroundColor(Color.parseColor("#80000000"));
            }
        }, 800);

        menuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        withDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sessionManager.getValues(UserSessionManager.KEY_CAN_WITHDRAW).equalsIgnoreCase("1"))
                {
                    menuView.setBackgroundColor(Color.parseColor("#00000000"));
                    startActivity(new Intent(getApplicationContext(),WithDrawActivity.class));
                    finish();
                }else {
                    startActivity(new Intent(getApplicationContext(), TransactionChecking.class));
                }

            }
        });

        deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sessionManager.getValues(UserSessionManager.KEY_CAN_DEPOSIT).equalsIgnoreCase("1"))
                {
                    startActivity(new Intent(getApplicationContext(), DepositMethod.class));
                    finish();
                }else {
                    startActivity(new Intent(getApplicationContext(), TransactionChecking.class));
                }

            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sessionManager.getValues(UserSessionManager.KEY_CAN_BUY).equalsIgnoreCase("1"))
                {
                    startActivity(new Intent(getApplicationContext(), BuyCrypto.class));
                }else {
                    startActivity(new Intent(getApplicationContext(), TransactionChecking.class));
                }

            }
        });

        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sessionManager.getValues(UserSessionManager.KEY_CAN_SELL).equalsIgnoreCase("1"))
                {
                    startActivity(new Intent(getApplicationContext(), SellCrypto.class));
                    finish();
                }else {
                    startActivity(new Intent(getApplicationContext(), TransactionChecking.class));
                }

            }
        });

        scanQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), QrcodeActivity.class));
                finish();
            }
        });

        exchange_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sessionManager.getValues(UserSessionManager.KEY_CAN_TRADE).equalsIgnoreCase("1"))
                {
                    Intent intent=new Intent(getApplicationContext(),ExchangeBuySell.class);
                    intent.putExtra("type","buy");
                    startActivity(intent);
                    finish();
                }else {
                    startActivity(new Intent(getApplicationContext(), TransCheckingExchange.class));
                }

            }
        });

        exchange_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sessionManager.getValues(UserSessionManager.KEY_CAN_TRADE).equalsIgnoreCase("1"))
                {
                    Intent intent=new Intent(getApplicationContext(),ExchangeBuySell.class);
                    intent.putExtra("type","sell");
                    startActivity(intent);
                    finish();
                }else {
                    startActivity(new Intent(getApplicationContext(), TransCheckingExchange.class));
                }


            }
        });

        reciveBitcoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),ReciveBitCoin.class));
                finish();
            }
        });

    }
}
