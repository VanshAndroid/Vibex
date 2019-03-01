package com.crescentek.cryptocurrency.interfaces;

import com.crescentek.cryptocurrency.model.CryptoM;
import com.crescentek.cryptocurrency.model.CurreencyM;
import com.crescentek.cryptocurrency.model.WalletN;

import java.util.List;

/**
 * Created by R.Android on 25-09-2018.
 */

public interface WalletListener {

    public void walletList(List<WalletN> walletList) throws Exception;
    public void cryptoList(List<CryptoM> cryptoList) throws Exception;
    public void curreencyList(List<CurreencyM> curreencyList) throws Exception;
}
