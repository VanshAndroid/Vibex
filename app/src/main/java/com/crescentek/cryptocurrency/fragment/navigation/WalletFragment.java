package com.crescentek.cryptocurrency.fragment.navigation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.crescentek.cryptocurrency.R;

/**
 * Created by R.Android on 20-06-2018.
 */

public class WalletFragment extends Fragment {

    private FrameLayout continue_fram;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_wallets, container, false);
        continue_fram=view.findViewById(R.id.continue_fram);
        continue_fram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AccountsFragment accountsFragment = new AccountsFragment();
                Bundle args = new Bundle();
                accountsFragment.setArguments(args);
                getFragmentManager().beginTransaction().replace(R.id.container_body, accountsFragment).commit();

            }
        });

        return view;
    }
}
