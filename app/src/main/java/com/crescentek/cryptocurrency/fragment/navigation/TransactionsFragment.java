package com.crescentek.cryptocurrency.fragment.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.activity.transaction_history.TransDepositDetails;
import com.crescentek.cryptocurrency.activity.transaction_history.TransExchangeDetails;
import com.crescentek.cryptocurrency.activity.transaction_history.TransBuySellDetails;
import com.crescentek.cryptocurrency.activity.transaction_history.TransReciveDetails;
import com.crescentek.cryptocurrency.activity.transaction_history.TransSendDetails;
import com.crescentek.cryptocurrency.activity.transaction_history.TranswithdrawDetails;
import com.crescentek.cryptocurrency.adapter.TransactionAdapter;
import com.crescentek.cryptocurrency.interfaces.ApiRequestListener;
import com.crescentek.cryptocurrency.model.TransactionDetails;
import com.crescentek.cryptocurrency.network.GetRequest;
import com.crescentek.cryptocurrency.utility.BaseActivity;
import com.crescentek.cryptocurrency.utility.ConnectivityReceiver;
import com.crescentek.cryptocurrency.utility.NetworkUtility;
import com.crescentek.cryptocurrency.utility.RecyclerTouchListener;

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
 * Created by R.Android on 25-06-2018.
 */

public class TransactionsFragment extends Fragment implements ApiRequestListener{

    Spinner spinner;
    RecyclerView recyclerView;
    List<TransactionDetails> transactionList;
    TransactionAdapter transactionAdapter;

    int page=0;
    int total_count=0;
    //New Change
    Boolean isScrolling=false;
    int currentItems,totalItems,scrollOutItems;
    ProgressBar progress;
    TextView noData;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_transaction,container,false);

        transactionList=new ArrayList<TransactionDetails>();

        spinner = (Spinner) view.findViewById(R.id.month_spinner);

        recyclerView=view.findViewById(R.id.recyclerView);

        progress=view.findViewById(R.id.progress);

        noData=view.findViewById(R.id.noData);

        noData.setVisibility(View.GONE);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(TransactionsFragment.this.getActivity(),R.array.month_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        getTransactionRecord();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Log.d("TRX_TYPE>>>>",""+transactionList.get(position).getTrx_type());

                if(transactionList.get(position).getTrx_type().equalsIgnoreCase("Withdraw")) {

                    Intent intent=new Intent(getActivity(),TranswithdrawDetails.class);
                    intent.putExtra("type",transactionList.get(position).getTrx_type());
                    intent.putExtra("debit_value",transactionList.get(position).getDebit_users_wallets_value());
                    intent.putExtra("trx_fees",transactionList.get(position).getTrx_fees());
                    intent.putExtra("trx_timestamp",transactionList.get(position).getTrx_timestamp());
                    intent.putExtra("currency_code",transactionList.get(position).getCurrency_code());
                    intent.putExtra("trx_log_id",transactionList.get(position).getTrx_log_id());
                    intent.putExtra("trx_status",transactionList.get(position).getTrx_status());
                    startActivity(intent);

                }
                else if(transactionList.get(position).getTrx_type().equalsIgnoreCase("sell")){

                    Intent intent=new Intent(getActivity(),TransBuySellDetails.class);
                    intent.putExtra("type",transactionList.get(position).getTrx_type());
                    intent.putExtra("amount",transactionList.get(position).getAmount());
                    intent.putExtra("credit_market_rate",transactionList.get(position).getCredit_market_rate());
                    intent.putExtra("crypto_code",transactionList.get(position).getCrypto_code());
                    intent.putExtra("crypto_val",transactionList.get(position).getCrypto_val());
                    Log.d("CryptoVal>>>>>>>",transactionList.get(position).getCrypto_val());
                    intent.putExtra("trx_timestamp",transactionList.get(position).getTrx_timestamp());
                    intent.putExtra("currency_code",transactionList.get(position).getCurrency_code());
                    intent.putExtra("trx_log_id",transactionList.get(position).getTrx_log_id());
                    intent.putExtra("trx_status",transactionList.get(position).getTrx_status());
                    intent.putExtra("trx_fees",transactionList.get(position).getTrx_fees());
                    startActivity(intent);

                }
                else if(transactionList.get(position).getTrx_type().equalsIgnoreCase("buy")){

                    Intent intent=new Intent(getActivity(),TransBuySellDetails.class);
                    intent.putExtra("type",transactionList.get(position).getTrx_type());
                    intent.putExtra("amount",transactionList.get(position).getAmount());
                    intent.putExtra("credit_market_rate",transactionList.get(position).getCredit_market_rate());
                    intent.putExtra("crypto_code",transactionList.get(position).getCrypto_code());
                    intent.putExtra("crypto_val",transactionList.get(position).getCrypto_val());
                    intent.putExtra("trx_timestamp",transactionList.get(position).getTrx_timestamp());
                    intent.putExtra("currency_code",transactionList.get(position).getCurrency_code());
                    intent.putExtra("trx_log_id",transactionList.get(position).getTrx_log_id());
                    intent.putExtra("trx_status",transactionList.get(position).getTrx_status());
                    intent.putExtra("trx_fees",transactionList.get(position).getTrx_fees());
                    startActivity(intent);

                }
                else if(transactionList.get(position).getTrx_type().equalsIgnoreCase("Receive")){

                    Intent intent=new Intent(getActivity(),TransReciveDetails.class);
                    intent.putExtra("type",transactionList.get(position).getTrx_type());
                    intent.putExtra("crypto_val",transactionList.get(position).getCrypto_val());
                    intent.putExtra("crypto_code",transactionList.get(position).getCrypto_code());
                    intent.putExtra("credit_wallet_value",transactionList.get(position).getCredit_wallet_value());
                    intent.putExtra("trx_fees",transactionList.get(position).getTrx_fees());
                    intent.putExtra("trx_timestamp",transactionList.get(position).getTrx_timestamp());
                    intent.putExtra("trx_status",transactionList.get(position).getTrx_status());
                    intent.putExtra("trx_log_id",transactionList.get(position).getTrx_log_id());
                    intent.putExtra("credit_address",transactionList.get(position).getCredit_address());
                    startActivity(intent);

                }
                else if(transactionList.get(position).getTrx_type().equalsIgnoreCase("send")){

                    Intent intent=new Intent(getActivity(),TransSendDetails.class);
                    intent.putExtra("send",transactionList.get(position).getTrx_type());
                    intent.putExtra("crypto_val",transactionList.get(position).getCrypto_val());
                    intent.putExtra("crypto_code",transactionList.get(position).getCrypto_code());
                    intent.putExtra("credit_wallet_value",transactionList.get(position).getCredit_wallet_value());
                    intent.putExtra("trx_fees",transactionList.get(position).getTrx_fees());
                    intent.putExtra("trx_timestamp",transactionList.get(position).getTrx_timestamp());
                    intent.putExtra("trx_status",transactionList.get(position).getTrx_status());
                    intent.putExtra("trx_log_id",transactionList.get(position).getTrx_log_id());
                    intent.putExtra("credit_address",transactionList.get(position).getCredit_address());
                    startActivity(intent);

                }
                else if(transactionList.get(position).getTrx_type().equalsIgnoreCase("deposit")){

                    Intent intent=new Intent(getActivity(),TransDepositDetails.class);
                    intent.putExtra("deposit",transactionList.get(position).getTrx_type());
                    intent.putExtra("deposit_amount",transactionList.get(position).getDeposit_amount());
                    intent.putExtra("credit_users_wallets",transactionList.get(position).getCredit_users_wallets());
                    intent.putExtra("trx_fees",transactionList.get(position).getTrx_fees());
                    intent.putExtra("trx_status",transactionList.get(position).getTrx_status());
                    intent.putExtra("trx_timestamp",transactionList.get(position).getTrx_timestamp());
                    intent.putExtra("currency_code",transactionList.get(position).getCurrency_code());
                    intent.putExtra("trx_log_id",transactionList.get(position).getTrx_log_id());
                    startActivity(intent);
                }
                else if(transactionList.get(position).getTrx_type().equalsIgnoreCase("Exchange Sell")){

                    Intent intent=new Intent(getActivity(),TransExchangeDetails.class);
                    intent.putExtra("exchange",transactionList.get(position).getTrx_type());
                    intent.putExtra("amount",transactionList.get(position).getAmount());
                    intent.putExtra("trx_fees",transactionList.get(position).getTrx_fees());
                    intent.putExtra("trx_status",transactionList.get(position).getTrx_status());
                    intent.putExtra("crypto_closing_balance",transactionList.get(position).getCrypto_closing_balance());
                    intent.putExtra("crypto_closing_traded",transactionList.get(position).getCrypto_closing_traded());
                    intent.putExtra("trx_timestamp",transactionList.get(position).getTrx_timestamp());
                    intent.putExtra("currency_code",transactionList.get(position).getCurrency_code());
                    intent.putExtra("crypto_code",transactionList.get(position).getCrypto_code());
                    intent.putExtra("trx_log_id",transactionList.get(position).getTrx_log_id());
                    startActivity(intent);

                }
                else {

                    Intent intent=new Intent(getActivity(),TransExchangeDetails.class);
                    intent.putExtra("exchange",transactionList.get(position).getTrx_type());
                    intent.putExtra("amount",transactionList.get(position).getAmount());
                    intent.putExtra("trx_fees",transactionList.get(position).getTrx_fees());
                    intent.putExtra("trx_status",transactionList.get(position).getTrx_status());
                    intent.putExtra("crypto_closing_balance",transactionList.get(position).getCrypto_closing_balance());
                    intent.putExtra("crypto_closing_traded",transactionList.get(position).getCrypto_closing_traded());
                    intent.putExtra("trx_timestamp",transactionList.get(position).getTrx_timestamp());
                    intent.putExtra("currency_code",transactionList.get(position).getCurrency_code());
                    intent.putExtra("crypto_code",transactionList.get(position).getCrypto_code());
                    intent.putExtra("trx_log_id",transactionList.get(position).getTrx_log_id());
                    startActivity(intent);
                }


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){

                    isScrolling=true;

                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems=linearLayoutManager.getChildCount();
                totalItems=linearLayoutManager.getItemCount();
                scrollOutItems=linearLayoutManager.findFirstVisibleItemPosition();

                if(isScrolling && (currentItems+scrollOutItems==totalItems))
                {
                    isScrolling=false;
                    if(total_count<15){

                    }else {
                        page++;
                        fetchaData();
                    }

                }
            }
        });


        return view;
    }

    public void fetchaData(){
        progress.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getTransactionRecord();
            }
        }, 2000);

    }

    public void getTransactionRecord()
    {
        if(ConnectivityReceiver.isConnected())
        {
            ConnectivityReceiver.isConnected();

            String URL=NetworkUtility.TRANSACTION_RECORD+"?page="+page+"&per_page=15";

            Log.d("TransUrl>>>",URL);

            new GetRequest(getActivity(),TransactionsFragment.this,"TRANSACTION_RECORD", URL);
        }

        else{

            Toast.makeText(getActivity(),""+getResources().getString(R.string.dlg_nointernet),Toast.LENGTH_LONG).show();

        }


    }
    @Override
    public void onSuccess(String result, String type) throws JSONException {

        Log.d("TransactionHistory>>>",result);
        //baseActivity.hideCustomProgrssDialog();

        try {
            JSONObject jObj=new JSONObject(result);
            String status=jObj.optString("status");
            String count=jObj.optString("count");

            total_count=Integer.parseInt(count);

            if(status.equalsIgnoreCase("true"))
            {
                JSONArray jsonArr=jObj.optJSONArray("data");
                if (jsonArr!=null)
                {
                    for(int i=0;i<jsonArr.length();i++)
                    {

                        TransactionDetails details=new TransactionDetails();
                        JSONObject jObject=jsonArr.optJSONObject(i);
                        details.setTrx_log_id(jObject.optString("trx_log_id"));


                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                        try {
                            Date date = sdf.parse(jObject.optString("trx_timestamp"));
                            int year=date.getYear()+1900;
                            int month=date.getMonth()+1;
                            int dateN=date.getDate();
                            details.setTrx_timestamp(""+dateN+" - "+month+" - "+year);
                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        details.setTrx_type(jObject.optString("trx_type"));
                        details.setCurrency_id(jObject.optString("currency_id"));
                        details.setCrypto_id(jObject.optString("crypto_id"));
                        details.setCurrency_val(jObject.optString("currency_val"));
                        details.setCrypto_val(jObject.optString("crypto_val"));
                        details.setCredit_market_rate(jObject.optString("credit_market_rate"));
                        details.setTrx_status(jObject.optString("trx_status"));
                        details.setCurrency_code(jObject.optString("currency_code"));
                        details.setCrypto_code(jObject.optString("crypto_code"));
                        details.setDebit_users_wallets_value(jObject.optString("debit_users_wallets_value"));
                        details.setCredit_users_wallets(jObject.optString("credit_users_wallets"));
                        details.setTrx_fees(jObject.optString("trx_fees"));
                        details.setFullfill_type(jObject.optString("fullfill_type"));
                        details.setExchange_escrow_id(jObject.optString("exchange_escrow_id"));
                        details.setExercise_timestamp(jObject.optString("exercise_timestamp"));
                        details.setExercise_fiat_val(jObject.optString("exercise_fiat_val"));
                        details.setCrypto_closing_balance(jObject.optString("crypto_closing_balance"));
                        details.setFiat_closing_balance(jObject.optString("fiat_closing_balance"));
                        details.setCrypto_closing_traded(jObject.optString("crypto_closing_traded"));
                        details.setFiat_closing_traded(jObject.optString("fiat_closing_traded"));
                        details.setAmount(jObject.optString("amount"));
                        details.setDeposit_amount(jObject.optString("deposit_amount"));
                        details.setCredit_wallet_value(jObject.optString("credit_wallet_value"));
                        details.setCredit_address(jObject.optString("credit_address"));

                        transactionList.add(details);
                    }


                    transactionAdapter = new TransactionAdapter(transactionList);
                    recyclerView.setAdapter(transactionAdapter);
                    transactionAdapter.notifyDataSetChanged();
                    progress.setVisibility(View.GONE);
                    recyclerView.scrollToPosition(totalItems);


                }

            }else {
                progress.setVisibility(View.GONE);
                noData.setVisibility(View.VISIBLE);
            }
        }
        catch (Exception e) {

        }

    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

        Log.d("TransactionHistory>>>",responseMessage+" "+responseCode);

    }
}
