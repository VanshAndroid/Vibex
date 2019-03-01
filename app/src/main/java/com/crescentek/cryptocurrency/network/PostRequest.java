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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class PostRequest {
    private String username;
    private String passord, type;
    private Activity act;
    Map<String, String> mapobject;
    Map<String, String> mapobject_header;
    private ApiRequestListener apiRequestListener;
    private JSONObject jsonObject=null;
    private String networkurl="";


    public PostRequest(Activity act, Map<String, String> mapobject, ApiRequestListener apiRequestListener, String networkurl,String type) {
        //this.mapobject_header=new HashMap<String, String>();
        this.mapobject = mapobject;
        this.act = act;
        this.apiRequestListener = apiRequestListener;
        this.type = type;
        this.networkurl=networkurl;
        login();
    }

    public PostRequest(Activity act, Map<String, String> mapobject, Map<String, String> mapobject_header,ApiRequestListener apiRequestListener, String networkurl,String type) {

        this.mapobject_header=new HashMap<String, String>();
        this.mapobject = mapobject;
        this.mapobject_header=mapobject_header;
        this.act = act;
        this.apiRequestListener = apiRequestListener;
        this.type = type;
        this.networkurl=networkurl;
        Log.d("ApiAllValuesSend>>>","API>>>"+networkurl+">>>"+mapobject_header.toString()+"Body>>"+mapobject.toString());
        login();


    }

    private void login() {

        Log.d("URL","URL" +networkurl);
        Log.d("Object","" +mapobject);
        Log.d("header","" +mapobject_header);

        StringRequest stringrequest = new StringRequest(Request.Method.POST, networkurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("postsport","postsport" +response);
                        try {
                            apiRequestListener.onSuccess(response, type);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        if (response != null) {
                            int code = response.statusCode;
                            String errorMsg = new String(response.data);
                            Log.d("response","response" +errorMsg);
                            try {
                                jsonObject=new JSONObject(errorMsg);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                           apiRequestListener.onFailure(code, "Please Try again After Some time");
                        } else {
                            String errorMsg = error.getMessage();
                           apiRequestListener.onFailure(0, errorMsg);
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders()  throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                if (mapobject_header!=null)
                {
                    if(mapobject_header.size()>0) {
                        params = mapobject_header;
                        Log.d("header>>",""+params.size());
                    }
                }

                Log.d("header>>>",""+params);
                return params;
            }
            @Override
            public Map<String, String> getParams() throws AuthFailureError{

                Map<String, String> params = new HashMap<>();

                Log.d("body>>>",""+mapobject);
                return mapobject;
            }
        };

        stringrequest.setRetryPolicy(new DefaultRetryPolicy(600000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestqueue = Volley.newRequestQueue(act);
        requestqueue.add(stringrequest);

    }

}
