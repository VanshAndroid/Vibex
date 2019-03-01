package com.crescentek.cryptocurrency.fragment.exchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.activity.Exchangebuysell.ExchangeBuySell;
import com.crescentek.cryptocurrency.activity.TransCheckingExchange;
import com.crescentek.cryptocurrency.activity.TransactionChecking;
import com.crescentek.cryptocurrency.adapter.ExchangeBuySellAdaptor;
import com.crescentek.cryptocurrency.interfaces.ApiRequestListener;
import com.crescentek.cryptocurrency.model.BuySell;
import com.crescentek.cryptocurrency.network.GetRequest;
import com.crescentek.cryptocurrency.utility.BaseActivity;
import com.crescentek.cryptocurrency.utility.ConnectivityReceiver;
import com.crescentek.cryptocurrency.utility.NetworkUtility;
import com.crescentek.cryptocurrency.utility.RecyclerTouchListener;
import com.crescentek.cryptocurrency.utility.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by R.Android on 09-07-2018.
 */

public class My_Orders_Fragment extends Fragment implements ApiRequestListener{


    private LinearLayout learn_more_layout,my_order_layout;
    BaseActivity baseActivity;

    public ArrayList<BuySell> buySellList;

    private RecyclerView recycler_view_buy;
    ExchangeBuySellAdaptor exchangeSellAdaptor;

    UserSessionManager sessionManager;

    private ImageView exchange_buy_sell;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.my_orders_fragments,container,false);

        buySellList =new ArrayList<BuySell>();
        baseActivity=new BaseActivity() {};
        sessionManager=new UserSessionManager(getActivity());
        learn_more_layout=view.findViewById(R.id.learn_more_layout);
        my_order_layout=view.findViewById(R.id.my_order_layout);
        recycler_view_buy=view.findViewById(R.id.recycler_view_buy);
        exchange_buy_sell=view.findViewById(R.id.exchange_buy_sell);

        learn_more_layout.setVisibility(View.GONE);
        my_order_layout.setVisibility(View.VISIBLE);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view_buy.setLayoutManager(linearLayoutManager);


        recycler_view_buy.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recycler_view_buy, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Log.d("Position>>",position+"");
                Intent intent=new Intent(getActivity(),CancelOrderBook.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("buySellList", (Serializable) buySellList);
                intent.putExtras(bundle);
                intent.putExtra("position",position+"");
                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {

                Log.d("Position>>",position+"");

            }
        }));



        callOrderBuySellAPi();


        exchange_buy_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(sessionManager.getValues(UserSessionManager.KEY_CAN_TRADE).equalsIgnoreCase("1"))
                {
                    Intent intent=new Intent(getActivity(),ExchangeBuySell.class);
                    intent.putExtra("type","sell");
                    startActivity(intent);
                    //finish();
                }else {
                    startActivity(new Intent(getActivity(), TransCheckingExchange.class));
                }

                /*if(sessionManager.getValues(UserSessionManager.KEY_CAN_TRADE).equalsIgnoreCase("0"))
                {
                    Intent intent=new Intent(getActivity(),ExchangeBuySell.class);
                    intent.putExtra("type","buy");
                    startActivity(intent);

                }else {
                    startActivity(new Intent(getActivity(), TransactionChecking.class));
                }*/

            }
        });

        return view;
    }

    public void callOrderBuySellAPi()
    {

        if(ConnectivityReceiver.isConnected())
        {
            baseActivity.showCustomProgrssDialog(getActivity());
            Log.d("Exchange_Url>>>",NetworkUtility.ORDER_BUY_SELL_TRADE+"/"+sessionManager.getValues(UserSessionManager.CRYPTO_ID));
            new GetRequest(getActivity(),My_Orders_Fragment.this,"buy_sell_exchange", NetworkUtility.ORDER_BUY_SELL_TRADE+"/"+sessionManager.getValues(UserSessionManager.CRYPTO_ID));
        }

        else{

            baseActivity.showAlertDialog(getResources().getString(R.string.dlg_nointernet));

        }

    }

    @Override
    public void onSuccess(String result, String type) throws JSONException {

        baseActivity.hideCustomProgrssDialog();
        Log.d("Exchange_Response>>>",result);

        try {
            JSONObject jsonObject=new JSONObject(result);
            String status=jsonObject.optString("status");
            String message=jsonObject.optString("message");
            if(status.equalsIgnoreCase("true"))
            {

                JSONArray jsonArray=jsonObject.optJSONArray("result");
                if(jsonArray!=null)
                {
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        BuySell buySell=new BuySell();
                        JSONObject jObj=jsonArray.optJSONObject(i);
                        buySell.setId(jObj.optString("id"));
                        buySell.setCryptoRate(jObj.optString("cryptoRate"));
                        buySell.setTrx_fees(jObj.optString("trx_fees"));
                        buySell.setCrypto_val(jObj.optString("crypto_val"));
                        buySell.setOrder_timestamp(jObj.optString("order_timestamp"));
                        buySell.setCrypto_code(jObj.optString("crypto_code"));
                        buySell.setCurrency_code(jObj.optString("currency_code"));
                        buySell.setTrx_type(jObj.optString("trx_type"));
                        buySell.setCrypto_closing_balance(jObj.optString("crypto_closing_balance"));
                        buySell.setCrypto_closing_traded(jObj.optString("crypto_closing_traded"));
                        buySell.setStatus(jObj.optString("status"));
                        buySell.setCredit_market_rate(jObj.optString("credit_market_rate"));
                        buySell.setType(jObj.optString("type"));

                        buySellList.add(buySell);

                    }
                    exchangeSellAdaptor=new ExchangeBuySellAdaptor(buySellList);
                    recycler_view_buy.setAdapter(exchangeSellAdaptor);

                }
                else {
                    Toast.makeText(getActivity(),"No Data Found",Toast.LENGTH_LONG).show();
                }

            }
            else {
                baseActivity.showAlertDialog(message);
            }

        }catch (Exception e) {

        }

    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

        baseActivity.hideCustomProgrssDialog();
        Log.d("Error_Response>>>",responseMessage);

    }


}
