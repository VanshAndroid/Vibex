package com.crescentek.cryptocurrency.model;

/**
 * Created by R.Android on 03-10-2018.
 */

public class PriceAlertModel {

    private String amount;

    private String amount_more;

    private String amount_less;

    private String created;

    private String users_alerts_id;

    private String alert_type;

    private String currency_code;

    public String getAmount ()
    {
        return amount;
    }

    public void setAmount (String amount)
    {
        this.amount = amount;
    }

    public String getAmount_more ()
    {
        return amount_more;
    }

    public void setAmount_more (String amount_more)
    {
        this.amount_more = amount_more;
    }

    public String getAmount_less ()
    {
        return amount_less;
    }

    public void setAmount_less (String amount_less)
    {
        this.amount_less = amount_less;
    }

    public String getCreated ()
    {
        return created;
    }

    public void setCreated (String created)
    {
        this.created = created;
    }

    public String getUsers_alerts_id ()
    {
        return users_alerts_id;
    }

    public void setUsers_alerts_id (String users_alerts_id)
    {
        this.users_alerts_id = users_alerts_id;
    }

    public String getAlert_type ()
    {
        return alert_type;
    }

    public void setAlert_type (String alert_type)
    {
        this.alert_type = alert_type;
    }

    public String getCurrency_code ()
    {
        return currency_code;
    }

    public void setCurrency_code (String currency_code)
    {
        this.currency_code = currency_code;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [amount = "+amount+", amount_more = "+amount_more+", amount_less = "+amount_less+", created = "+created+", users_alerts_id = "+users_alerts_id+", alert_type = "+alert_type+", currency_code = "+currency_code+"]";
    }

}
