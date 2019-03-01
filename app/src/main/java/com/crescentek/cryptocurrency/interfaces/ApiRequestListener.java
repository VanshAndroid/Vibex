package com.crescentek.cryptocurrency.interfaces;

import org.json.JSONException;

/**
 * Created by R.Android on 19-10-2016.
 */
public interface ApiRequestListener {
    public void onSuccess(String result, String type) throws JSONException;
    public void onFailure(int responseCode, String responseMessage);

}
