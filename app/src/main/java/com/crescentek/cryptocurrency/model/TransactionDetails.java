package com.crescentek.cryptocurrency.model;

/**
 * Created by R.Android on 11-10-2018.
 */

public class TransactionDetails {

    private String trx_log_id;
    private String trx_timestamp;
    private String trx_type;
    private String currency_id;
    private String crypto_id;
    private String currency_val;
    private String crypto_val;
    private String credit_market_rate;
    private String trx_status;
    private String currency_code;
    private String crypto_code;
    private String debit_users_wallets_value;
    private String credit_users_wallets;
    private String trx_fees;
    private String fullfill_type;
    private String exchange_escrow_id;
    private String exercise_timestamp;
    private String exercise_fiat_val;
    private String crypto_closing_balance;
    private String fiat_closing_balance;
    private String crypto_closing_traded;
    private String fiat_closing_traded;
    private String amount;
    private String deposit_amount;
    private String credit_wallet_value;
    private String credit_address;

    public String getCredit_address() {
        return credit_address;
    }

    public void setCredit_address(String credit_address) {
        this.credit_address = credit_address;
    }

    public String getCredit_wallet_value() {
        return credit_wallet_value;
    }

    public void setCredit_wallet_value(String credit_wallet_value) {
        this.credit_wallet_value = credit_wallet_value;
    }

    public String getDeposit_amount() {
        return deposit_amount;
    }

    public void setDeposit_amount(String deposit_amount) {
        this.deposit_amount = deposit_amount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTrx_log_id() {
        return trx_log_id;
    }

    public void setTrx_log_id(String trx_log_id) {
        this.trx_log_id = trx_log_id;
    }

    public String getTrx_timestamp() {
        return trx_timestamp;
    }

    public void setTrx_timestamp(String trx_timestamp) {
        this.trx_timestamp = trx_timestamp;
    }

    public String getTrx_type() {
        return trx_type;
    }

    public void setTrx_type(String trx_type) {
        this.trx_type = trx_type;
    }

    public String getCurrency_id() {
        return currency_id;
    }

    public void setCurrency_id(String currency_id) {
        this.currency_id = currency_id;
    }

    public String getCrypto_id() {
        return crypto_id;
    }

    public void setCrypto_id(String crypto_id) {
        this.crypto_id = crypto_id;
    }

    public String getCurrency_val() {
        return currency_val;
    }

    public void setCurrency_val(String currency_val) {
        this.currency_val = currency_val;
    }

    public String getCrypto_val() {
        return crypto_val;
    }

    public void setCrypto_val(String crypto_val) {
        this.crypto_val = crypto_val;
    }

    public String getCredit_market_rate() {
        return credit_market_rate;
    }

    public void setCredit_market_rate(String credit_market_rate) {
        this.credit_market_rate = credit_market_rate;
    }

    public String getTrx_status() {
        return trx_status;
    }

    public void setTrx_status(String trx_status) {
        this.trx_status = trx_status;
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

    public String getDebit_users_wallets_value() {
        return debit_users_wallets_value;
    }

    public void setDebit_users_wallets_value(String debit_users_wallets_value) {
        this.debit_users_wallets_value = debit_users_wallets_value;
    }

    public String getCredit_users_wallets() {
        return credit_users_wallets;
    }

    public void setCredit_users_wallets(String credit_users_wallets) {
        this.credit_users_wallets = credit_users_wallets;
    }

    public String getTrx_fees() {
        return trx_fees;
    }

    public void setTrx_fees(String trx_fees) {
        this.trx_fees = trx_fees;
    }

    public String getFullfill_type() {
        return fullfill_type;
    }

    public void setFullfill_type(String fullfill_type) {
        this.fullfill_type = fullfill_type;
    }

    public String getExchange_escrow_id() {
        return exchange_escrow_id;
    }

    public void setExchange_escrow_id(String exchange_escrow_id) {
        this.exchange_escrow_id = exchange_escrow_id;
    }

    public String getExercise_timestamp() {
        return exercise_timestamp;
    }

    public void setExercise_timestamp(String exercise_timestamp) {
        this.exercise_timestamp = exercise_timestamp;
    }

    public String getExercise_fiat_val() {
        return exercise_fiat_val;
    }

    public void setExercise_fiat_val(String exercise_fiat_val) {
        this.exercise_fiat_val = exercise_fiat_val;
    }

    public String getCrypto_closing_balance() {
        return crypto_closing_balance;
    }

    public void setCrypto_closing_balance(String crypto_closing_balance) {
        this.crypto_closing_balance = crypto_closing_balance;
    }

    public String getFiat_closing_balance() {
        return fiat_closing_balance;
    }

    public void setFiat_closing_balance(String fiat_closing_balance) {
        this.fiat_closing_balance = fiat_closing_balance;
    }

    public String getCrypto_closing_traded() {
        return crypto_closing_traded;
    }

    public void setCrypto_closing_traded(String crypto_closing_traded) {
        this.crypto_closing_traded = crypto_closing_traded;
    }

    public String getFiat_closing_traded() {
        return fiat_closing_traded;
    }

    public void setFiat_closing_traded(String fiat_closing_traded) {
        this.fiat_closing_traded = fiat_closing_traded;
    }
}
