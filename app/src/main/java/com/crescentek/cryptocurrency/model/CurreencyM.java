package com.crescentek.cryptocurrency.model;

/**
 * Created by R.Android on 26-10-2018.
 */

public class CurreencyM {

    private String  currency_value;
    private String currency_id;
    private String currency_code;
    private String crypto_addr;
    private String currency_logo;

    public String getCurrency_logo() {
        return currency_logo;
    }

    public void setCurrency_logo(String currency_logo) {
        this.currency_logo = currency_logo;
    }

    public CurreencyM() {
    }

    public CurreencyM(String currency_value, String currency_id, String currency_code, String crypto_addr) {
        this.currency_value = currency_value;
        this.currency_id = currency_id;
        this.currency_code = currency_code;
        this.crypto_addr = crypto_addr;
    }



    public String getCurrency_value() {
        return currency_value;
    }

    public void setCurrency_value(String currency_value) {
        this.currency_value = currency_value;
    }

    public String getCurrency_id() {
        return currency_id;
    }

    public void setCurrency_id(String currency_id) {
        this.currency_id = currency_id;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    public String getCrypto_addr() {
        return crypto_addr;
    }

    public void setCrypto_addr(String crypto_addr) {
        this.crypto_addr = crypto_addr;
    }
}

/*{"crypto_value":4,"currency_value":3610700.39,"crypto_name":null,"crypto_id":null,"currency_id":1,
"currency_code":"NGN","crypto_code":null,"crypto_addr":"2Mw1i6d1ErqmAfm4drFvZKv3N7t5mDiwFB3","addr_label":null}*/
