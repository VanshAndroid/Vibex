package com.crescentek.cryptocurrency.model;

/**
 * Created by R.Android on 04-07-2018.
 */

public class Trades {

    private String amount;

    private String time;

    private String price;

    private String exchange_trades_id;

    private String totalCrypto;

    public String getAmount ()
    {
        return amount;
    }

    public void setAmount (String amount)
    {
        this.amount = amount;
    }

    public String getTime ()
    {
        return time;
    }

    public void setTime (String time)
    {
        this.time = time;
    }

    public String getPrice ()
    {
        return price;
    }

    public void setPrice (String price)
    {
        this.price = price;
    }

    public String getExchange_trades_id ()
    {
        return exchange_trades_id;
    }

    public void setExchange_trades_id (String exchange_trades_id)
    {
        this.exchange_trades_id = exchange_trades_id;
    }

    public String getTotalCrypto ()
    {
        return totalCrypto;
    }

    public void setTotalCrypto (String totalCrypto)
    {
        this.totalCrypto = totalCrypto;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [amount = "+amount+", time = "+time+", price = "+price+", exchange_trades_id = "+exchange_trades_id+", totalCrypto = "+totalCrypto+"]";
    }
}
