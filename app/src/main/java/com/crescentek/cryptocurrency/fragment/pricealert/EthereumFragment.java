package com.crescentek.cryptocurrency.fragment.pricealert;

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

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.activity.alert.CreateAlert;
import com.crescentek.cryptocurrency.activity.alert.EditPriceAlert;
import com.crescentek.cryptocurrency.adapter.PriceAlertAdapter;
import com.crescentek.cryptocurrency.comman.WalletRecord;
import com.crescentek.cryptocurrency.fragment.navigation.HomeFragment;
import com.crescentek.cryptocurrency.interfaces.ApiRequestListener;
import com.crescentek.cryptocurrency.interfaces.WalletListener;
import com.crescentek.cryptocurrency.model.CryptoM;
import com.crescentek.cryptocurrency.model.CurreencyM;
import com.crescentek.cryptocurrency.model.PriceAlertModel;
import com.crescentek.cryptocurrency.model.WalletN;
import com.crescentek.cryptocurrency.network.GetRequest;
import com.crescentek.cryptocurrency.utility.BaseActivity;
import com.crescentek.cryptocurrency.utility.ConnectivityReceiver;
import com.crescentek.cryptocurrency.utility.NetworkUtility;
import com.crescentek.cryptocurrency.utility.RecyclerTouchListener;
import com.crescentek.cryptocurrency.utility.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by R.Android on 13-07-2018.
 */

public class EthereumFragment extends Fragment implements ApiRequestListener,WalletListener{


    private ImageView createAlert;
    View include;
    private RecyclerView recycler_view_price_alert;
    BaseActivity baseActivity;
    UserSessionManager sessionManager;
    ArrayList<PriceAlertModel> priceAlertList;
    PriceAlertAdapter adapter;


    String cryptoTag="ETH";
    String crypto_id;
    List<CryptoM> mCryptoList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.price_alert_etherum,container,false);

        new WalletRecord(getActivity(),EthereumFragment.this);

        createAlert=view.findViewById(R.id.createAlert);

        recycler_view_price_alert=view.findViewById(R.id.recycler_view_price_alert);

        //include=view.findViewById(R.id.headerView);

        baseActivity=new BaseActivity() {};

        sessionManager=new UserSessionManager(getActivity());

        priceAlertList=new ArrayList<PriceAlertModel>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view_price_alert.setLayoutManager(linearLayoutManager);

        recycler_view_price_alert.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recycler_view_price_alert, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Log.d("Position>>",position+"");
                Intent intent=new Intent(getActivity(),EditPriceAlert.class);
                intent.putExtra("amount",priceAlertList.get(position).getAmount());
                intent.putExtra("alert_type",priceAlertList.get(position).getAlert_type());
                intent.putExtra("amount_more",priceAlertList.get(position).getAmount_more());
                intent.putExtra("alert_id",priceAlertList.get(position).getUsers_alerts_id());
                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {

                Log.d("Position>>",position+"");

            }
        }));

        createAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), CreateAlert.class));

            }
        });


        //callEthereumAlertDetails();

        return view;
    }

    public void callEthereumAlertDetails(String crypto_id)
    {

        if(ConnectivityReceiver.isConnected())
        {
            baseActivity.showCustomProgrssDialog(getActivity());
            ConnectivityReceiver.isConnected();
            new GetRequest(getActivity(),EthereumFragment.this,"SERVICE_ALERT_LIST", NetworkUtility.SERVICE_ALERT_LIST+crypto_id);
        }

        else{

            baseActivity.showAlertDialog(getResources().getString(R.string.dlg_nointernet));

        }



    }

    @Override
    public void onSuccess(String result, String type) throws JSONException {
        baseActivity.hideCustomProgrssDialog();
        Log.d("ResponseAlert>>>",result);
        switch (type)
        {
            case "SERVICE_ALERT_LIST":
            {
                JSONObject jObject=new JSONObject(result);
                String status=jObject.optString("status");
                String message=jObject.optString("message");
                if (status.equalsIgnoreCase("true"))
                {
                    JSONArray resultArray=jObject.optJSONArray("result");
                    if(resultArray!=null)
                    {
                        for(int i=0;i<resultArray.length();i++)
                        {
                            JSONObject jObj=resultArray.optJSONObject(i);
                            PriceAlertModel priceAlert=new PriceAlertModel();
                            priceAlert.setAmount(jObj.optString("amount"));
                            priceAlert.setUsers_alerts_id(jObj.optString("users_alerts_id"));
                            priceAlert.setAlert_type(jObj.optString("alert_type"));
                            priceAlert.setCurrency_code(jObj.optString("currency_code"));
                            priceAlert.setCreated(jObj.optString("created"));
                            priceAlert.setAmount_less(jObj.optString("amount_less"));
                            priceAlert.setAmount_more(jObj.optString("amount_more"));

                            priceAlertList.add(priceAlert);
                        }
                        adapter=new PriceAlertAdapter(priceAlertList);
                        recycler_view_price_alert.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }
                else {
                    baseActivity.showAlertDialog(message);
                }
            }
            break;
            case "":
                break;
        }
    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

    }



    @Override
    public void walletList(List<WalletN> walletList) throws Exception {

    }

    @Override
    public void cryptoList(List<CryptoM> cryptoList) throws Exception {

        mCryptoList=cryptoList;
        Log.d("mCryptoList",mCryptoList.size()+"");
        for (int i=0;i<cryptoList.size();i++)
        {
            if(cryptoList.get(i).getCrypto_code().equalsIgnoreCase(cryptoTag))
            {
                crypto_id=cryptoList.get(i).getCrypto_id();
                Log.d("crypto_id>>>",crypto_id);
            }
        }
        callEthereumAlertDetails(crypto_id);

    }

    @Override
    public void curreencyList(List<CurreencyM> curreencyList) throws Exception {

    }
}
