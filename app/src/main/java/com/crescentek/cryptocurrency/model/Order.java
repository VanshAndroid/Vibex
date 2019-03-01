package com.crescentek.cryptocurrency.model;

import android.widget.TextView;

/**
 * Created by R.Android on 09-07-2018.
 */

public class Order {

    private String price;
    private String amount;
    private String price_1;
    private String amount_1;


    public Order(String price, String amount, String price_1, String amount_1) {
        this.price = price;
        this.amount = amount;
        this.price_1 = price_1;
        this.amount_1 = amount_1;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPrice_1() {
        return price_1;
    }

    public void setPrice_1(String price_1) {
        this.price_1 = price_1;
    }

    public String getAmount_1() {
        return amount_1;
    }

    public void setAmount_1(String amount_1) {
        this.amount_1 = amount_1;
    }
}
