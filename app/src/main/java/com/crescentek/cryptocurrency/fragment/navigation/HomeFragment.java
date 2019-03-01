package com.crescentek.cryptocurrency.fragment.navigation;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.activity.QrCode.QrcodeActivity;
import com.crescentek.cryptocurrency.activity.TransactionChecking;
import com.crescentek.cryptocurrency.activity.buysell.BuyCrypto;
import com.crescentek.cryptocurrency.activity.login_signup.HomeActivity;
import com.crescentek.cryptocurrency.activity.login_signup.MoreActivity;
import com.crescentek.cryptocurrency.activity.buysell.BuyBitCoin;
import com.crescentek.cryptocurrency.activity.deposit.DepositMethod;
import com.crescentek.cryptocurrency.activity.send.SendCrypto;
import com.crescentek.cryptocurrency.adapter.CryptoAdapter;
import com.crescentek.cryptocurrency.comman.WalletRecord;
import com.crescentek.cryptocurrency.fragment.home.HomeCardFragment_1;
import com.crescentek.cryptocurrency.fragment.home.HomeCardFragment_2;
import com.crescentek.cryptocurrency.fragment.home.HomeCardFragment_3;
import com.crescentek.cryptocurrency.interfaces.ApiRequestListener;
import com.crescentek.cryptocurrency.interfaces.WalletListener;
import com.crescentek.cryptocurrency.model.CryptoM;
import com.crescentek.cryptocurrency.model.CurreencyM;
import com.crescentek.cryptocurrency.model.Wallet;
import com.crescentek.cryptocurrency.model.WalletN;
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


@SuppressLint("ValidFragment")
public class HomeFragment extends Fragment implements ApiRequestListener,WalletListener {

    private LinearLayout moreOption,send,buy,deposit,social_sharing;
    private FrameLayout menu_container;
    private ScrollView scroll_view;
    private BaseActivity baseActivity;
    private TextView btc_tv,ngn_tv;
    ArrayList<Wallet> walletList;
    private ImageView scanQr_iv;
    UserSessionManager sessionManager;

    Spinner spinner;
    TextView crypto_tv_n;
    List<CryptoM> cryptoList;
    List<CurreencyM> curreencyList;

    int intentValue = 1;

    public HomeFragment(int i) {

        intentValue = i;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setStatusBarGradiant(getActivity());
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        baseActivity=new HomeActivity();
        sessionManager=new UserSessionManager(getActivity());
        walletList=new ArrayList<Wallet>();
        moreOption=view.findViewById(R.id.moreOption);
        send=view.findViewById(R.id.send);
        buy=view.findViewById(R.id.buy);
        deposit=view.findViewById(R.id.deposit);
        btc_tv=view.findViewById(R.id.crypto_tv);
        ngn_tv=view.findViewById(R.id.totalamount_tv);
        scanQr_iv=view.findViewById(R.id.scanQr_iv);
        menu_container=view.findViewById(R.id.menu_container);
        scroll_view=view.findViewById(R.id.scroll_view);
        spinner=view.findViewById(R.id.spinner);
        social_sharing=view.findViewById(R.id.social_sharing);

        menu_container.setVisibility(View.GONE);
        scroll_view.setVisibility(View.VISIBLE);
        crypto_tv_n=view.findViewById(R.id.crypto_tv_n);


        if(intentValue == 1){
            baseActivity.showCustomProgrssDialog(getActivity());
            callWalletAPIafterDelay();
        }else {

            new WalletRecord(getActivity(),HomeFragment.this);
            calluserLevelApiAndWallet();
            loadFragments();
        }

        spinner.setVisibility(View.INVISIBLE);

        crypto_tv_n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                spinner.performClick();
            }
        });

        //ngn_tv.setText();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                crypto_tv_n.setText(cryptoList.get(position).getCrypto_code()+" "+cryptoList.get(position).getCrypto_value());
                spinner.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        moreOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),MoreActivity.class));
            }
        });

        deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sessionManager.getValues(UserSessionManager.KEY_CAN_DEPOSIT).equalsIgnoreCase("1"))
                {
                    startActivity(new Intent(getActivity(), DepositMethod.class));
                }else {
                    startActivity(new Intent(getActivity(), TransactionChecking.class));
                }

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sessionManager.getValues(UserSessionManager.KEY_CAN_SELL).equalsIgnoreCase("1"))
                {
                    startActivity(new Intent(getActivity(), SendCrypto.class));
                }else {
                    startActivity(new Intent(getActivity(), TransactionChecking.class));
                }

            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sessionManager.getValues(UserSessionManager.KEY_CAN_BUY).equalsIgnoreCase("1"))
                {
                    //startActivity(new Intent(getActivity(), BuyBitCoin.class));

                    startActivity(new Intent(getActivity(), BuyCrypto.class));
                }else {
                    startActivity(new Intent(getActivity(), TransactionChecking.class));
                }

            }
        });

        scanQr_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), QrcodeActivity.class));
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


        social_sharing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Vibex");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Bitcoin when you buy or sell NGN 5000.00 (exchange excluded), using https://www.vibex.com/invite/"+sessionManager.getValues(UserSessionManager.KEY_REFERAL_CODE));
                startActivity(Intent.createChooser(sharingIntent, "Send Invite to"));

            }
        });

    }

    public void loadFragments()
    {

        HomeCardFragment_1 homeCardFragment_1 =new HomeCardFragment_1();
        FragmentManager manager=getActivity().getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.replace(R.id.container_layout, homeCardFragment_1, "homeCardFragment_1");
        transaction.commit();

        HomeCardFragment_2 homeCardFragment_2 =new HomeCardFragment_2();
        FragmentManager manager_2=getActivity().getSupportFragmentManager();
        FragmentTransaction transaction_2=manager_2.beginTransaction();
        transaction_2.replace(R.id.container_layout_2, homeCardFragment_2, "homeCardFragment_2");
        transaction_2.commit();


    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.gradient_theme);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }


    public void calluserLevelApiAndWallet()
    {
        baseActivity=new HomeActivity();
        if (ConnectivityReceiver.isConnected())
        {
            baseActivity=new HomeActivity();
            baseActivity.showCustomProgrssDialog(getActivity());
            new GetRequest(getActivity(),HomeFragment.this,"level_details", NetworkUtility.LEVEL_DETAILS);
            new GetRequest(getActivity(),HomeFragment.this,"wallet", NetworkUtility.WALLET);
            new GetRequest(getActivity(),HomeFragment.this,"Referal", NetworkUtility.REFFERAL_CODE);
        }
        else {
            baseActivity.showAlertDialog(getResources().getString(R.string.dlg_nointernet));
        }

    }
    public void callHomeContent()
    {
        new GetRequest(getActivity(),HomeFragment.this,"HOME_CONTENT", NetworkUtility.HOME_CONTENT);
    }


    @Override
    public void onSuccess(String result, String type) throws JSONException {

        Log.d("Response>>>",result);
        baseActivity.hideCustomProgrssDialog();
        switch (type)
        {
            case "level_details":
                {
                    try{

                        Log.d("UserLevelResponse>>>",result);
                        JSONObject jObj=new JSONObject(result);
                        JSONObject jObject=jObj.optJSONObject("result");
                        String users_id=jObject.optString("users_id");
                        String users_level_id=jObject.optString("users_level_id");
                        String level_name=jObject.optString("level_name");
                        String withdraw_limit_type=jObject.optString("withdraw_limit_type");
                        String can_withdraw=jObject.optString("can_withdraw");
                        String can_withdraw_limit=jObject.optString("can_withdraw_limit");
                        String buy_limit_type=jObject.optString("buy_limit_type");
                        String can_buy=jObject.optString("can_buy");
                        String can_buy_limit=jObject.optString("can_buy_limit");
                        String sell_limit_type=jObject.optString("sell_limit_type");
                        String can_sell=jObject.optString("can_sell");
                        String can_send=jObject.optString("can_send");
                        String can_sell_limit=jObject.optString("can_sell_limit");
                        String trade_limit_type=jObject.optString("trade_limit_type");
                        String can_trade=jObject.optString("can_trade");
                        String can_trade_limit=jObject.optString("can_trade_limit");

                        String can_deposit=jObject.optString("can_deposit");
                        String can_deposit_limit=jObject.optString("can_deposit_limit");
                        String deposit_limit_type=jObject.optString("deposit_limit_type");
                        String isDocUploaded=jObject.optString("isDocUploaded");
                        String kycStatus=jObject.optString("kycStatus");
                        String isEmailVery=jObject.optString("isEmailVery");
                        String isPhoneVery=jObject.optString("isPhoneVery");
                        String isBvnVerified=jObject.optString("isBvnVerified");

                        String isPinSet=jObject.optString("isPinSet");
                        String isTwoFASet=jObject.optString("isTwoFASet");
                        String isProfileUploaded=jObject.optString("isProfileUploaded");
                        String firstname=jObject.optString("firstname");
                        String lastname=jObject.optString("lastname");
                        String email=jObject.optString("email");

                        String id=jObject.optString("id");
                        String phone=jObject.optString("phone");
                        String country=jObject.optString("country");


                        sessionManager.setValues(UserSessionManager.KEY_FIRST_NAME,firstname);
                        sessionManager.setValues(UserSessionManager.KEY_LAST_NAME,lastname);
                        sessionManager.setValues(UserSessionManager.KEY_CAN_SEND,can_send);
                        sessionManager.setValues(UserSessionManager.KEY_CAN_SELL,can_sell);
                        sessionManager.setValues(UserSessionManager.KEY_CAN_BUY,can_buy);
                        sessionManager.setValues(UserSessionManager.KEY_CAN_DEPOSIT,can_deposit);
                        sessionManager.setValues(UserSessionManager.KEY_CAN_WITHDRAW,can_withdraw);
                        sessionManager.setValues(UserSessionManager.KEY_CAN_TRADE,can_trade);
                        sessionManager.setValues(UserSessionManager.KEY_LEVEL_NAME,level_name);
                        sessionManager.setValues(UserSessionManager.KEY_CAN_WITHDRAW_LIMIT,can_withdraw_limit);
                        sessionManager.setValues(UserSessionManager.KEY_CAN_DEPOSIT_LIMIT,can_deposit_limit);
                        sessionManager.setValues(UserSessionManager.KEY_COUNTRY,country);
                        sessionManager.setValues(UserSessionManager.KEY_BVN_NUMBER,isBvnVerified);

                        if(isDocUploaded.equalsIgnoreCase("true")){

                            if(kycStatus.equalsIgnoreCase("Pending"))
                            {
                                Bundle bundle=new Bundle();
                                bundle.putString("status","pending");
                                HomeCardFragment_3 homeCardFragment_3 =new HomeCardFragment_3();
                                FragmentManager manager_3=getActivity().getSupportFragmentManager();
                                FragmentTransaction transaction_3=manager_3.beginTransaction();
                                homeCardFragment_3.setArguments(bundle);
                                transaction_3.replace(R.id.container_doc_status, homeCardFragment_3, "homeCardFragment_3");
                                transaction_3.commit();
                            }
                            if (kycStatus.equalsIgnoreCase("Rejected")){

                                Bundle bundle=new Bundle();
                                bundle.putString("status","rejected");
                                HomeCardFragment_3 homeCardFragment_3 =new HomeCardFragment_3();
                                FragmentManager manager_3=getActivity().getSupportFragmentManager();
                                FragmentTransaction transaction_3=manager_3.beginTransaction();
                                homeCardFragment_3.setArguments(bundle);
                                transaction_3.replace(R.id.container_doc_status, homeCardFragment_3, "homeCardFragment_3");
                                transaction_3.commit();

                            }
                            else {

                            }

                        }
                        else {

                        }

                    }catch (Exception e){

                    }
                }
                break;

            case "wallet":
                {
                    Log.d("WalletResponse>>>",result);
                    JSONObject jObject=new JSONObject(result);
                    String status=jObject.optString("status");
                    String message=jObject.optString("message");
                    if(status.equalsIgnoreCase("success"))
                    {
                        JSONArray jArr=jObject.optJSONArray("result");
                        if(jArr!=null)
                        {
                            for(int i=0;i<jArr.length();i++)
                            {
                                JSONObject jObj=jArr.getJSONObject(i);
                                Wallet wallet=new Wallet();
                                wallet.setCrypto_value(jObj.optString("crypto_value"));
                                wallet.setCurrency_value(jObj.optString("currency_value"));
                                wallet.setCrypto_name(jObj.optString("crypto_name"));
                                wallet.setCrypto_id(jObj.optString("crypto_id"));
                                wallet.setCurrency_code(jObj.optString("currency_code"));
                                wallet.setCrypto_code(jObj.optString("crypto_code"));

                                walletList.add(wallet);
                            }

                            for (int i=0;i<walletList.size();i++)
                            {
                                if(walletList.get(i).getCrypto_code().equalsIgnoreCase("BTC"))
                                {
                                    Log.d("BTC-->>",walletList.get(i).getCrypto_code()+" "+walletList.get(i).getCrypto_value());
                                    //btc_tv.setText(walletList.get(i).getCrypto_code()+" "+walletList.get(i).getCrypto_value());
                                    sessionManager.setValues(UserSessionManager.BTC_WALLET,walletList.get(i).getCrypto_value());
                                    sessionManager.setValues(UserSessionManager.CRYPTO_ID,walletList.get(i).getCrypto_id());
                                    sessionManager.setValues(UserSessionManager.CRYPTO_CODE,walletList.get(i).getCrypto_code());

                                }
                                if(walletList.get(i).getCurrency_code().equalsIgnoreCase("NGN"))
                                {
                                    Log.d("NGN-->>",walletList.get(i).getCurrency_code()+" "+walletList.get(i).getCurrency_value());
                                    //ngn_tv.setText(walletList.get(i).getCurrency_code()+" "+walletList.get(i).getCurrency_value());
                                    sessionManager.setValues(UserSessionManager.NGN_WALLET,walletList.get(i).getCurrency_value());
                                    sessionManager.setValues(UserSessionManager.CURRENCY_CODE,walletList.get(i).getCurrency_code());
                                }
                                if(walletList.get(i).getCurrency_code().equalsIgnoreCase("INR"))
                                {
                                    Log.d("INR-->>",walletList.get(i).getCurrency_code()+" "+walletList.get(i).getCurrency_value());
                                    //ngn_tv.setText(walletList.get(i).getCurrency_code()+" "+walletList.get(i).getCurrency_value());
                                    sessionManager.setValues(UserSessionManager.NGN_WALLET,walletList.get(i).getCurrency_value());
                                    sessionManager.setValues(UserSessionManager.CURRENCY_CODE,walletList.get(i).getCurrency_code());
                                }
                                else {

                                }
                            }

                        }
                    }
                    else {
                        baseActivity.showAlertDialog(message);
                    }
                }
                break;

            case "HOME_CONTENT":
                {
                    Log.d("HomeResponse>>>",result);
                    JSONObject jsonObject=new JSONObject(result);
                    String status=jsonObject.optString("status");
                    if (status.equalsIgnoreCase("true"))
                    {
                        JSONArray data=jsonObject.optJSONArray("data");
                        if(data!=null)
                        {
                            for(int i=0;i<data.length();i++)
                            {
                                JSONObject jObj=data.optJSONObject(i);
                                String blog_content_tag=jObj.optString("blog_content_tag");
                                String blog_content_subject=jObj.optString("blog_content_subject");
                                String blog_content_pic=jObj.optString("blog_content_pic");
                                String blog_content=jObj.optString("blog_content_pic");

                            }
                        }
                    }
                }
                break;

            case "Referal":
                {
                    Log.d("ReferalResponse>>>",result);
                    try {
                        JSONObject jObj=new JSONObject(result);
                        String status=jObj.optString("status");
                        if(status.equalsIgnoreCase("true"))
                        {
                            JSONObject data=jObj.optJSONObject("data");
                            String referral_code=data.optString("referral_code");
                            sessionManager.setValues(UserSessionManager.KEY_REFERAL_CODE,referral_code);
                        }
                    }catch (Exception e)
                    {
                        Log.d("ErrorResponse>>>",e.getMessage());
                    }

                }
                break;

        }

    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

        Log.d("ErrorResponse>>>",""+responseMessage);
        baseActivity.hideCustomProgrssDialog();


    }

    @Override
    public void walletList(List<WalletN> walletList) throws Exception {

        Log.d("walletListSize>>>",walletList.size()+"");
        sessionManager.setWallet(UserSessionManager.KEY_WALLET_DATA,walletList);

        List<WalletN> walletListTest=sessionManager.getWallet(UserSessionManager.KEY_WALLET_DATA);
        Log.d("walletListTest>>>",walletListTest.size()+"   abc");
        //getValue();
    }

    @Override
    public void cryptoList(List<CryptoM> cryptoList) throws Exception {

        sessionManager.setCrypto(UserSessionManager.KEY_CRYPTO_WALLET,cryptoList);
        Log.d("cryptoListSize>>>",cryptoList.size()+"");
        getCrypto();

    }

    @Override
    public void curreencyList(List<CurreencyM> curreencyList) throws Exception {

        sessionManager.setCurrency(UserSessionManager.KEY_CURRENCY_WALLET,curreencyList);
        Log.d("curreencyListSize>>>",curreencyList.size()+"");
        setCurrancy();

    }

    public void getCrypto()
    {
        cryptoList=sessionManager.getCrypto(UserSessionManager.KEY_CRYPTO_WALLET);
        CryptoAdapter adapter = new CryptoAdapter(getActivity(),R.layout.list_item_crypto, R.id.title, cryptoList);
        spinner.setAdapter(adapter);

    }
    public void setCurrancy()
    {
        curreencyList=sessionManager.getCurrency(UserSessionManager.KEY_CURRENCY_WALLET);
        ngn_tv.setText(curreencyList.get(0).getCurrency_code()+" "+curreencyList.get(0).getCurrency_value());

    }



    public  void callWalletAPIafterDelay(){

        // baseActivity.showToastMesseage(getActivity(), "WalletAPI");

        Handler handler = new Handler();
        Runnable  runnable = new Runnable() {
            @Override
            public void run() {

                new WalletRecord(getActivity(),HomeFragment.this);

                callALLAPIafterDelay();

            }
        };

        handler.postDelayed(runnable,2000);

    }

    public  void callALLAPIafterDelay(){


        Handler handler = new Handler();
        Runnable  runnable = new Runnable() {
            @Override
            public void run() {
                // do the task
                //baseActivity.showToastMesseage(getActivity(), "---");
                try {

                    baseActivity.hideCustomProgrssDialog();
                    calluserLevelApiAndWallet();
                    loadFragments();

                }catch (Exception e)
                {

                }finally {


                }



            }
        };

        handler.postDelayed(runnable,1000);

    }

}
