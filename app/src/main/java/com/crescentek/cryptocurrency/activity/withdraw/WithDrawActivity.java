package com.crescentek.cryptocurrency.activity.withdraw;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.activity.TransactionChecking;
import com.crescentek.cryptocurrency.activity.VerifyTransBVNChecking;
import com.crescentek.cryptocurrency.activity.verification_level_2.DocumentType;
import com.crescentek.cryptocurrency.adapter.UserBankListAdapter;
import com.crescentek.cryptocurrency.fragment.exchange.CancelOrderBook;
import com.crescentek.cryptocurrency.interfaces.ApiRequestListener;
import com.crescentek.cryptocurrency.model.BankList;
import com.crescentek.cryptocurrency.network.GetRequest;
import com.crescentek.cryptocurrency.utility.ConnectivityReceiver;
import com.crescentek.cryptocurrency.utility.NetworkUtility;
import com.crescentek.cryptocurrency.utility.RecyclerTouchListener;
import com.crescentek.cryptocurrency.utility.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by R.Android on 10-07-2018.
 */

public class WithDrawActivity extends Activity implements ApiRequestListener{

    private LinearLayout withDrawView;
    private LinearLayout add_bank;
    private RecyclerView recycler_view_bank;
    List<BankList> bankLists;
    UserBankListAdapter mAdapter;
    UserSessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_up_anim,R.anim.slide_down_anim);
        setContentView(R.layout.withdraw_activity);

        sessionManager=new UserSessionManager(WithDrawActivity.this);

        withDrawView=findViewById(R.id.withDrawView);
        add_bank=findViewById(R.id.add_bank);
        recycler_view_bank=findViewById(R.id.recycler_view_bank);

        bankLists=new ArrayList<BankList>();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view_bank.setLayoutManager(linearLayoutManager);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                withDrawView.setBackgroundColor(Color.parseColor("#80000000"));
            }
        }, 800);


        add_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sessionManager.getValues(UserSessionManager.KEY_COUNTRY).equalsIgnoreCase("NIGERIA"))
                {
                    if(sessionManager.getValues(UserSessionManager.KEY_BVN_VERIFY).equalsIgnoreCase("true"))
                    {
                        startActivity(new Intent(getApplicationContext(),AddBankAccount.class));
                        finish();
                    }
                    else if(sessionManager.getValues(UserSessionManager.KEY_USER_LEVEL).equalsIgnoreCase("Level0")){
                        startActivity(new Intent(getApplicationContext(), TransactionChecking.class));
                    }
                    else {
                        startActivity(new Intent(getApplicationContext(), VerifyTransBVNChecking.class));
                    }
                }
                else {
                    if(sessionManager.getValues(UserSessionManager.KEY_USER_LEVEL).equalsIgnoreCase("Level0"))
                    {
                        startActivity(new Intent(getApplicationContext(), TransactionChecking.class));
                    }
                    else if(sessionManager.getValues(UserSessionManager.KEY_USER_LEVEL).equalsIgnoreCase("Level1"))
                    {
                        startActivity(new Intent(getApplicationContext(),VerifyDocument.class));
                    }
                    else {
                        startActivity(new Intent(getApplicationContext(),AddBankAccount.class));
                        finish();
                    }


                }



            }
        });


        recycler_view_bank.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recycler_view_bank, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                BankList bankList=bankLists.get(position);

                Intent intent=new Intent(getApplicationContext(),WithdrawAmount.class);
                intent.putExtra("users_bank_id",bankList.getUsers_bank_id());
                intent.putExtra("bank_name",bankList.getBank_name());
                intent.putExtra("bank_account",bankList.getBank_account());
                intent.putExtra("bank_account_name",bankList.getBank_account_name());
                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {


            }
        }));

        callBankApi();

    }

    public void callBankApi()
    {
        if(ConnectivityReceiver.isConnected())
        {
            new GetRequest(WithDrawActivity.this,WithDrawActivity.this,"BANK_LIST_USER", NetworkUtility.BANK_LIST_USER);
        }
        else {
           Toast.makeText(getApplicationContext(),""+getResources().getString(R.string.dlg_nointernet),Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onSuccess(String result, String type) throws JSONException {

        Log.d("BankListResponse>>>",result);

        try {
            JSONObject jsonObj=new JSONObject(result);
            String status=jsonObj.optString("status");
            if(status.equalsIgnoreCase("true"))
            {
                JSONArray jsonArr=jsonObj.optJSONArray("data");
                if(jsonArr!=null)
                {
                    for (int i=0;i<jsonArr.length();i++)
                    {
                        JSONObject jObj=jsonArr.optJSONObject(i);
                        BankList bankList=new BankList();
                        bankList.setUsers_bank_id(jObj.optString("users_bank_id"));
                        bankList.setBank_name(jObj.optString("bank_name"));
                        bankList.setBank_account(jObj.optString("bank_account"));
                        bankList.setBank_account_name(jObj.optString("bank_account_name"));
                        bankList.setCurrency_id(jObj.optString("currency_id"));
                        bankLists.add(bankList);

                    }

                    mAdapter = new UserBankListAdapter(bankLists);
                    recycler_view_bank.setAdapter(mAdapter);
                }
            }else {

            }
        }
        catch (Exception e) {

        }

    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

        Log.d("BankListResponse>>>",responseMessage);

    }
}
