package com.crescentek.cryptocurrency.model;

/**
 * Created by R.Android on 02-10-2018.
 */

public class AllBuyOrder {

    private String id;
    private String cryptoRate;
    private String crypto_val;
    private String crypto_code;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCryptoRate() {
        return cryptoRate;
    }

    public void setCryptoRate(String cryptoRate) {
        this.cryptoRate = cryptoRate;
    }

    public String getCrypto_val() {
        return crypto_val;
    }

    public void setCrypto_val(String crypto_val) {
        this.crypto_val = crypto_val;
    }

    public String getCrypto_code() {
        return crypto_code;
    }

    public void setCrypto_code(String crypto_code) {
        this.crypto_code = crypto_code;
    }
}
