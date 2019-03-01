package com.crescentek.cryptocurrency.model;

/**
 * Created by R.Android on 04-09-2018.
 */

public class Wallet {

    private String crypto_value="";
    private String currency_value="";
    private String crypto_name="";
    private String crypto_id="";
    private String currency_code="";
    private String crypto_code="";

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    public String getCrypto_code() {
        return crypto_code;
    }

    public void setCrypto_code(String crypto_code) {
        this.crypto_code = crypto_code;
    }

    public String getCrypto_value() {
        return crypto_value;
    }

    public void setCrypto_value(String crypto_value) {
        this.crypto_value = crypto_value;
    }

    public String getCurrency_value() {
        return currency_value;
    }

    public void setCurrency_value(String currency_value) {
        this.currency_value = currency_value;
    }

    public String getCrypto_name() {
        return crypto_name;
    }

    public void setCrypto_name(String crypto_name) {
        this.crypto_name = crypto_name;
    }

    public String getCrypto_id() {
        return crypto_id;
    }

    public void setCrypto_id(String crypto_id) {
        this.crypto_id = crypto_id;
    }


}
