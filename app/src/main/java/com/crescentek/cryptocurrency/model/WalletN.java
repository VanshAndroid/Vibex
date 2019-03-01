package com.crescentek.cryptocurrency.model;

/**
 * Created by R.Android on 21-09-2018.
 */

public class WalletN {

    private String crypto_value="";
    private String currency_value="";
    private String crypto_name="";
    private String crypto_id="";
    private String currency_id="";
    private String currency_code="";
    private String crypto_code="";
    private String crypto_addr="";
    private String addr_label="";
    private String currency_logo="";
    private String crypto_logo="";


    public WalletN() {

    }

    public String getCurrency_logo() {
        return currency_logo;
    }

    public void setCurrency_logo(String currency_logo) {
        this.currency_logo = currency_logo;
    }

    public String getCrypto_logo() {
        return crypto_logo;
    }

    public void setCrypto_logo(String crypto_logo) {
        this.crypto_logo = crypto_logo;
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

    public String getAddr_label() {
        return addr_label;
    }

    public void setAddr_label(String addr_label) {
        this.addr_label = addr_label;
    }


    public WalletN(String crypto_value, String currency_value, String crypto_name, String crypto_id, String currency_id, String currency_code, String crypto_code, String crypto_addr, String addr_label) {
        this.crypto_value = crypto_value;
        this.currency_value = currency_value;
        this.crypto_name = crypto_name;
        this.crypto_id = crypto_id;
        this.currency_id = currency_id;
        this.currency_code = currency_code;
        this.crypto_code = crypto_code;
        this.crypto_addr = crypto_addr;
        this.addr_label = addr_label;
    }
}
