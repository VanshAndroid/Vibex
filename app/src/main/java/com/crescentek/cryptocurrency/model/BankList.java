package com.crescentek.cryptocurrency.model;

/**
 * Created by R.Android on 16-10-2018.
 */

public class BankList {

    private String users_bank_id;
    private String bank_name;
    private String bank_account;
    private String bank_account_name;
    private String currency_id;


    public String getUsers_bank_id() {
        return users_bank_id;
    }

    public void setUsers_bank_id(String users_bank_id) {
        this.users_bank_id = users_bank_id;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getBank_account() {
        return bank_account;
    }

    public void setBank_account(String bank_account) {
        this.bank_account = bank_account;
    }

    public String getBank_account_name() {
        return bank_account_name;
    }

    public void setBank_account_name(String bank_account_name) {
        this.bank_account_name = bank_account_name;
    }

    public String getCurrency_id() {
        return currency_id;
    }

    public void setCurrency_id(String currency_id) {
        this.currency_id = currency_id;
    }
}
