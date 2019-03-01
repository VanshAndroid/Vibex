package com.crescentek.cryptocurrency.model;

import java.io.Serializable;

public class BuySell implements Serializable{

    private String credit_market_rate;

    private String crypto_closing_balance;

    private String cryptoRate;

    private String id;

    private String trx_fees;

    private String order_timestamp;

    private String status;

    private String trx_type;

    private String crypto_closing_traded;

    private String crypto_val;

    private String type;

    private String currency_code;

    private String crypto_code;

    public String getCredit_market_rate ()
    {
        return credit_market_rate;
    }

    public void setCredit_market_rate (String credit_market_rate)
    {
        this.credit_market_rate = credit_market_rate;
    }

    public String getCrypto_closing_balance ()
    {
        return crypto_closing_balance;
    }

    public void setCrypto_closing_balance (String crypto_closing_balance)
    {
        this.crypto_closing_balance = crypto_closing_balance;
    }

    public String getCryptoRate ()
    {
        return cryptoRate;
    }

    public void setCryptoRate (String cryptoRate)
    {
        this.cryptoRate = cryptoRate;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getTrx_fees ()
    {
        return trx_fees;
    }

    public void setTrx_fees (String trx_fees)
    {
        this.trx_fees = trx_fees;
    }

    public String getOrder_timestamp ()
    {
        return order_timestamp;
    }

    public void setOrder_timestamp (String order_timestamp)
    {
        this.order_timestamp = order_timestamp;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getTrx_type ()
    {
        return trx_type;
    }

    public void setTrx_type (String trx_type)
    {
        this.trx_type = trx_type;
    }

    public String getCrypto_closing_traded ()
    {
        return crypto_closing_traded;
    }

    public void setCrypto_closing_traded (String crypto_closing_traded)
    {
        this.crypto_closing_traded = crypto_closing_traded;
    }

    public String getCrypto_val ()
    {
        return crypto_val;
    }

    public void setCrypto_val (String crypto_val)
    {
        this.crypto_val = crypto_val;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    public String getCurrency_code ()
    {
        return currency_code;
    }

    public void setCurrency_code (String currency_code)
    {
        this.currency_code = currency_code;
    }

    public String getCrypto_code ()
    {
        return crypto_code;
    }

    public void setCrypto_code (String crypto_code)
    {
        this.crypto_code = crypto_code;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [credit_market_rate = "+credit_market_rate+", crypto_closing_balance = "+crypto_closing_balance+", cryptoRate = "+cryptoRate+", id = "+id+", trx_fees = "+trx_fees+", order_timestamp = "+order_timestamp+", status = "+status+", trx_type = "+trx_type+", crypto_closing_traded = "+crypto_closing_traded+", crypto_val = "+crypto_val+", type = "+type+", currency_code = "+currency_code+", crypto_code = "+crypto_code+"]";
    }
}
