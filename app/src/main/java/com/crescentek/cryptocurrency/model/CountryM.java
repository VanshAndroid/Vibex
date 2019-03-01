package com.crescentek.cryptocurrency.model;

/**
 * Created by R.Android on 14-08-2018.
 */

public class CountryM {

    public String country_id="";
    public String country="";
    public String phonecode="";
    public String currency="";
    public String currency_code="";
    public String currency_symbol="";

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhonecode() {
        return phonecode;
    }

    public void setPhonecode(String phonecode) {
        this.phonecode = phonecode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    public String getCurrency_symbol() {
        return currency_symbol;
    }

    public void setCurrency_symbol(String currency_symbol) {
        this.currency_symbol = currency_symbol;
    }


    public CountryM(String country_id, String country, String phonecode, String currency, String currency_code, String currency_symbol) {
        this.country_id = country_id;
        this.country = country;
        this.phonecode = phonecode;
        this.currency = currency;
        this.currency_code = currency_code;
        this.currency_symbol = currency_symbol;
    }

    public CountryM()
    {

    }
}
