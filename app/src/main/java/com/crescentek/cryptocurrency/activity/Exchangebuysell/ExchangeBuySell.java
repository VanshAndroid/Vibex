package com.crescentek.cryptocurrency.activity.Exchangebuysell;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.fragment.home.HomeCardFragment_1;
import com.crescentek.cryptocurrency.utility.BaseActivity;

/**
 * Created by R.Android on 13-07-2018.
 */

public class ExchangeBuySell extends BaseActivity {

    ImageView back_iv;
    TextView headerText_tv,buy,sell;
    private String type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(ExchangeBuySell.this);
        setContentView(R.layout.exchange_buy_sell);
        type=getIntent().getExtras().getString("type");
        back_iv=findViewById(R.id.back_iv);
        headerText_tv=findViewById(R.id.headerText_tv);
        buy=findViewById(R.id.buy);
        sell=findViewById(R.id.sell);

        if (type.equalsIgnoreCase("buy"))
        {
            buy.setBackgroundResource(R.drawable.rounded_box_blue);
            sell.setBackgroundResource(R.drawable.rounded_box_white);
            buy.setTextColor(Color.parseColor("#FFFFFF"));
            sell.setTextColor(Color.parseColor("#316DF8"));

            ExchangeBuyFragment exchangeBuyFragment =new ExchangeBuyFragment();
            FragmentManager manager=getSupportFragmentManager();
            FragmentTransaction transaction=manager.beginTransaction();
            transaction.replace(R.id.exchange_container_layout, exchangeBuyFragment, "buy");
            transaction.commit();
        }
        else {
            buy.setBackgroundResource(R.drawable.rounded_box_white);
            sell.setBackgroundResource(R.drawable.rounded_box_blue);
            buy.setTextColor(Color.parseColor("#316DF8"));
            sell.setTextColor(Color.parseColor("#FFFFFF"));

            ExchangeSellFragment exchangeSellFragment =new ExchangeSellFragment();
            FragmentManager manager=getSupportFragmentManager();
            FragmentTransaction transaction=manager.beginTransaction();
            transaction.replace(R.id.exchange_container_layout, exchangeSellFragment, "sell");
            transaction.commit();

        }

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        headerText_tv.setText("Place limit order");

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buy.setBackgroundResource(R.drawable.rounded_box_blue);
                sell.setBackgroundResource(R.drawable.rounded_box_white);
                buy.setTextColor(Color.parseColor("#FFFFFF"));
                sell.setTextColor(Color.parseColor("#316DF8"));
                //amount_ed.setHint("Amount to buy (BTC)");

                ExchangeBuyFragment exchangeBuyFragment =new ExchangeBuyFragment();
                FragmentManager manager=getSupportFragmentManager();
                FragmentTransaction transaction=manager.beginTransaction();
                transaction.replace(R.id.exchange_container_layout, exchangeBuyFragment, "buy");
                transaction.commit();


            }
        });
        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buy.setBackgroundResource(R.drawable.rounded_box_white);
                sell.setBackgroundResource(R.drawable.rounded_box_blue);
                buy.setTextColor(Color.parseColor("#316DF8"));
                sell.setTextColor(Color.parseColor("#FFFFFF"));
                //amount_ed.setHint("Amount to sell (BTC)");

                ExchangeSellFragment exchangeSellFragment =new ExchangeSellFragment();
                FragmentManager manager=getSupportFragmentManager();
                FragmentTransaction transaction=manager.beginTransaction();
                transaction.replace(R.id.exchange_container_layout, exchangeSellFragment, "sell");
                transaction.commit();
            }
        });

    }
}
