package com.crescentek.cryptocurrency.model;

/**
 * Created by R.Android on 08-10-2018.
 */

public class Bank {

    private String id="";
    private String bankcode="";
    private String bank="";
    private String currency_id="";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBankcode() {
        return bankcode;
    }

    public void setBankcode(String bankcode) {
        this.bankcode = bankcode;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getCurrency_id() {
        return currency_id;
    }

    public void setCurrency_id(String currency_id) {
        this.currency_id = currency_id;
    }
}
