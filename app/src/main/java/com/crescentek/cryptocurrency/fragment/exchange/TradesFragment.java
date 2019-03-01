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
import com.crescentek.cryptocurrency.adapter.TrandsAdapter;
import com.crescentek.cryptocurrency.interfaces.ApiRequestListener;
import com.crescentek.cryptocurrency.model.Trades;
import com.crescentek.cryptocurrency.network.GetRequest;
import com.crescentek.cryptocurrency.utility.BaseActivity;
import com.crescentek.cryptocurrency.utility.ConnectivityReceiver;
import com.crescentek.cryptocurrency.utility.NetworkUtility;
import com.crescentek.cryptocurrency.utility.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by R.Android on 04-07-2018.
 */

public class TradesFragment extends Fragment implements ApiRequestListener{

    private RecyclerView recycler_view;
    TrandsAdapter mAdapter;
    private List<Trades> tradesList;
    BaseActivity baseActivity;
    UserSessionManager sessionManager;
    ImageView exchange_sell_buy;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.trandes_fragment,container,false);
        recycler_view=view.findViewById(R.id.recycler_view);
        exchange_sell_buy=view.findViewById(R.id.exchange_sell_buy);

        baseActivity=new BaseActivity() {};
        sessionManager=new UserSessionManager(getActivity());
        tradesList =new ArrayList<Trades>();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(linearLayoutManager);


        callTradeApi();

        exchange_sell_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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

        //prepareMovieData();

        return view;
    }

    public void callTradeApi()
    {

        if(ConnectivityReceiver.isConnected())
        {
            baseActivity.showCustomProgrssDialog(getActivity());
            ConnectivityReceiver.isConnected();
            new GetRequest(getActivity(),TradesFragment.this,"Trade", NetworkUtility.ORDER_TRADES+sessionManager.getValues(UserSessionManager.CRYPTO_ID));
        }

        else{

            baseActivity.showAlertDialog(getResources().getString(R.string.dlg_nointernet));

        }



    }


    @Override
    public void onSuccess(String result, String type) throws JSONException {

        baseActivity.hideCustomProgrssDialog();
        Log.d("TradesResponse>>>",result);
        switch (type)
        {
            case "Trade":
                {
                    JSONObject jsonObject=new JSONObject(result);
                    String status=jsonObject.optString("status");
                    String message=jsonObject.optString("message");
                    if(status.equalsIgnoreCase("true"))
                    {
                        JSONArray resultArr=jsonObject.optJSONArray("result");
                        if(resultArr!=null)
                        {
                            for (int i=0;i<resultArr.length();i++)
                            {
                                JSONObject jObj=resultArr.optJSONObject(i);
                                Trades trades=new Trades();
                                trades.setExchange_trades_id(jObj.optString("exchange_trades_id"));
                                trades.setAmount(jObj.optString("amount"));

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                                try {
                                    Date date = sdf.parse(jObj.optString("time"));
                                    int year=date.getYear()+1900;
                                    int month=date.getMonth()+1;
                                    int dateN=date.getDate();
                                    Log.d("Date>>>",""+dateN+" - "+month+" - "+year);
                                    trades.setTime(""+dateN+"-"+month+"-"+year);
                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }


                                trades.setTotalCrypto(jObj.optString("totalCrypto"));
                                trades.setPrice(jObj.optString("price"));
                                tradesList.add(trades);

                            }
                            mAdapter = new TrandsAdapter(tradesList);
                            recycler_view.setAdapter(mAdapter);
                        }
                        else {
                            baseActivity.showAlertDialog(message);
                        }
                    }
                }
                break;
            case "":
                {

                }
                break;
        }


    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

    }
}
