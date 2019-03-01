package com.crescentek.cryptocurrency.network;

import android.app.Activity;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crescentek.cryptocurrency.interfaces.ApiRequestListener;
import com.crescentek.cryptocurrency.utility.NetworkUtility;
import com.crescentek.cryptocurrency.utility.UserSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;




public class GetRequest {
    private String username;
    private String passord, type;
    private Activity act;
    Map<String, String> mapHeader;
    private ApiRequestListener apiRequestListener;
    private String networkurl;
    private JSONObject jsonObject = null;
    UserSessionManager sessionManager;

    public GetRequest(Activity act, ApiRequestListener apiRequestListener, String type, String netnetworkUrl) {
        this.act = act;
        this.apiRequestListener = apiRequestListener;
        this.type = type;
        this.networkurl = netnetworkUrl;
        sessionManager=new UserSessionManager(act);
        login();
    }

    private void login() {

        Log.d("url","url"+networkurl);
        StringRequest stringrequest = new StringRequest(Request.Method.GET, networkurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       System.out.println("Response" +networkurl+":"+ response);
                        try {
                            apiRequestListener.onSuccess(response, type);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("response_Main","response" +response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        if (response != null) {
                            int code = response.statusCode;
                            String errorMsg = new String(response.data);
                            try {
                                jsonObject=new JSONObject(errorMsg);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String msg=jsonObject.optString("message");
                            apiRequestListener.onFailure(code, msg);
                        } else {
                            String errorMsg = error.getMessage();

                            apiRequestListener.onFailure(0, errorMsg);
                        }


                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String,String> params = new HashMap<String, String>();
                params.put("token",sessionManager.getValues(UserSessionManager.KEY_TOKEN));
                Log.d("Token>>>>",params.toString());
                return params;
            }
        };

        stringrequest.setRetryPolicy(new DefaultRetryPolicy(
                600000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestqueue = Volley.newRequestQueue(act);
        requestqueue.add(stringrequest);

    }

}
