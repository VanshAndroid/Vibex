package com.crescentek.cryptocurrency.fragment.navigation;

import android.app.Activity;
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

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.adapter.AccountAdapter;
import com.crescentek.cryptocurrency.comman.WalletRecord;
import com.crescentek.cryptocurrency.interfaces.WalletListener;
import com.crescentek.cryptocurrency.model.CryptoM;
import com.crescentek.cryptocurrency.model.CurreencyM;
import com.crescentek.cryptocurrency.model.WalletN;
import com.crescentek.cryptocurrency.utility.UserSessionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by R.Android on 17-07-2018.
 */

public class AccountsFragment extends Fragment implements WalletListener {

    RecyclerView recycler_view;
    private List<WalletN> walletNList;
    AccountAdapter mAdapter;
    UserSessionManager sessionManager;
    Activity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.accounts,container,false);

        new WalletRecord(getActivity(),AccountsFragment.this);
        sessionManager=new UserSessionManager(getActivity());
       /* walletNList=new ArrayList<WalletN>();
        walletNList=sessionManager.getWallet(UserSessionManager.KEY_WALLET_DATA);
        Log.d("walletNList>>>",""+walletNList.size());*/
        recycler_view=view.findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
       /* mAdapter=new AccountAdapter(this.walletNList,getActivity());
        recycler_view.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();*/


        return view;

    }

    @Override
    public void walletList(List<WalletN> walletList) throws Exception {

        mAdapter=new AccountAdapter(walletList,getActivity());
        recycler_view.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void cryptoList(List<CryptoM> cryptoList) throws Exception {

    }

    @Override
    public void curreencyList(List<CurreencyM> curreencyList) throws Exception {

    }


}
