package com.crescentek.cryptocurrency.fragment.exchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.activity.Exchangebuysell.ExchangeBuySell;
import com.crescentek.cryptocurrency.activity.TransCheckingExchange;
import com.crescentek.cryptocurrency.activity.TransactionChecking;
import com.crescentek.cryptocurrency.adapter.Order_Book_Adapter;
import com.crescentek.cryptocurrency.interfaces.ApiRequestListener;
import com.crescentek.cryptocurrency.model.AllBuyOrder;
import com.crescentek.cryptocurrency.model.AllSellOrder;
import com.crescentek.cryptocurrency.model.Order;
import com.crescentek.cryptocurrency.network.GetRequest;
import com.crescentek.cryptocurrency.utility.BaseActivity;
import com.crescentek.cryptocurrency.utility.ConnectivityReceiver;
import com.crescentek.cryptocurrency.utility.NetworkUtility;
import com.crescentek.cryptocurrency.utility.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by R.Android on 09-07-2018.
 */

public class OrderFragment extends Fragment implements ApiRequestListener{

    private RecyclerView recycler_view;
    private ArrayList<Order> orderList=new ArrayList<Order>();
    Order_Book_Adapter mAdapter;
    BaseActivity baseActivity;
    UserSessionManager sessionManager;
    List<AllSellOrder> allSellOrders;
    List<AllBuyOrder> allBuyOrders;
    ImageView exchange_sell_buy;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.odrer_book_fragment,container,false);

        baseActivity=new BaseActivity() {};
        sessionManager=new UserSessionManager(getActivity());
        allSellOrders=new ArrayList<AllSellOrder>();
        allBuyOrders=new ArrayList<AllBuyOrder>();
        recycler_view=view.findViewById(R.id.recycler_view_order_book);
        exchange_sell_buy=view.findViewById(R.id.exchange_sell_buy);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(linearLayoutManager);

        callAllOrderList();


        exchange_sell_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if(sessionManager.getValues(UserSessionManager.KEY_CAN_TRADE).equalsIgnoreCase("0"))
                {
                    Intent intent=new Intent(getActivity(),ExchangeBuySell.class);
                    intent.putExtra("type","buy");
                    startActivity(intent);

                }else {
                    startActivity(new Intent(getActivity(), TransactionChecking.class));
                }*/

                if(sessionManager.getValues(UserSessionManager.KEY_CAN_TRADE).equalsIgnoreCase("1"))
                {
                    Intent intent=new Intent(getActivity(),ExchangeBuySell.class);
                    intent.putExtra("type","sell");
                    startActivity(intent);
                    //finish();
                }else {
                    startActivity(new Intent(getActivity(), TransCheckingExchange.class));
                }

            }
        });

        return view;
    }

    public void callAllOrderList()
    {
        if(ConnectivityReceiver.isConnected())
        {
            baseActivity.showCustomProgrssDialog(getActivity());
            new GetRequest(getActivity(),OrderFragment.this,"ORDER_ALL_LIST", NetworkUtility.ORDER_ALL_LIST+sessionManager.getValues(UserSessionManager.CRYPTO_ID));

        }

        else{

            baseActivity.showAlertDialog(getResources().getString(R.string.dlg_nointernet));

        }


    }

    @Override
    public void onSuccess(String result, String type) throws JSONException {

        baseActivity.hideCustomProgrssDialog();
        Log.d("ORDER_ALL_LIST>>>",result);
        try {

            JSONObject jObj=new JSONObject(result);
            String status=jObj.optString("status");
            if (status.equalsIgnoreCase("true"))
            {
                JSONObject jsonObject=jObj.optJSONObject("result");
                JSONArray sellArr=jsonObject.optJSONArray("sell");
                JSONArray buyArr=jsonObject.optJSONArray("buy");
                if (sellArr!=null)
                {
                    for(int i=0;i<sellArr.length();i++)
                    {
                        JSONObject jOb=sellArr.getJSONObject(i);
                        AllSellOrder allSellOrder=new AllSellOrder();
                        allSellOrder.setId(jOb.optString("id"));
                        allSellOrder.setCryptoRate(jOb.optString("cryptoRate"));
                        allSellOrder.setCrypto_val(jOb.optString("crypto_val"));
                        allSellOrder.setCrypto_code(jOb.optString("crypto_code"));
                        allSellOrders.add(allSellOrder);
                    }
                }
                if (buyArr!=null)
                {
                    for (int i=0;i<buyArr.length();i++)
                    {
                        JSONObject jOb=buyArr.getJSONObject(i);
                        AllBuyOrder allBuyOrder=new AllBuyOrder();
                        allBuyOrder.setId(jOb.optString("id"));
                        allBuyOrder.setCryptoRate(jOb.optString("cryptoRate"));
                        allBuyOrder.setCrypto_val(jOb.optString("crypto_val"));
                        allBuyOrder.setCrypto_code(jOb.optString("crypto_code"));
                        allBuyOrders.add(allBuyOrder);
                    }
                }

                mAdapter = new Order_Book_Adapter(allSellOrders,allBuyOrders);
                recycler_view.setAdapter(mAdapter);

            }

        }catch (Exception e)
        {
            Log.d("Exception>>>",e.getMessage());
        }


    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

        baseActivity.hideCustomProgrssDialog();
        Log.d("ErrorResponse>>>",responseMessage);

    }
}
