package com.crescentek.cryptocurrency.model;

/**
 * Created by R.Android on 26-10-2018.
 */

public class CryptoM {

    private String  crypto_value;
    private String  crypto_name;
    private String  crypto_id;
    private String  crypto_code;
    private String  crypto_addr;
    private String  crypto_logo;

    public String getCrypto_logo() {
        return crypto_logo;
    }

    public void setCrypto_logo(String crypto_logo) {
        this.crypto_logo = crypto_logo;
    }

    public CryptoM() {
    }

    public CryptoM(String crypto_value, String crypto_name, String crypto_id, String crypto_code, String crypto_addr) {
        this.crypto_value = crypto_value;
        this.crypto_name = crypto_name;
        this.crypto_id = crypto_id;
        this.crypto_code = crypto_code;
        this.crypto_addr = crypto_addr;
    }

    public String getCrypto_value() {
        return crypto_value;
    }

    public void setCrypto_value(String crypto_value) {
        this.crypto_value = crypto_value;
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

    public String getCrypto_code() {
        return crypto_code;
    }

    public void setCrypto_code(String crypto_code) {
        this.crypto_code = crypto_code;
    }

    public String getCrypto_addr() {
        return crypto_addr;
    }

    public void setCrypto_addr(String crypto_addr) {
        this.crypto_addr = crypto_addr;
    }
}