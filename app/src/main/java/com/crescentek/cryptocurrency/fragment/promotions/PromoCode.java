package com.crescentek.cryptocurrency.fragment.promotions;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.activity.login_signup.SignInConfirmation;
import com.crescentek.cryptocurrency.interfaces.ApiRequestListener;
import com.crescentek.cryptocurrency.network.PostRequest;
import com.crescentek.cryptocurrency.utility.BaseActivity;
import com.crescentek.cryptocurrency.utility.ConnectivityReceiver;
import com.crescentek.cryptocurrency.utility.NetworkUtility;
import com.crescentek.cryptocurrency.utility.UserSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by R.Android on 31-07-2018.
 */

public class PromoCode extends Fragment implements ApiRequestListener{

    private EditText promo_ed;
    private Button apply_promo_btn;
    BaseActivity baseActivity;
    Map<String,String> mapobject,mapheader;
    UserSessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.promo_code,container,false);

        baseActivity=new BaseActivity() {};

        mapobject=new HashMap<String, String>();
        mapheader=new HashMap<String, String>();
        promo_ed=view.findViewById(R.id.promo_ed);
        sessionManager=new UserSessionManager(getActivity());
        apply_promo_btn=view.findViewById(R.id.apply_promo_btn);

        apply_promo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(promo_ed.getText().toString().trim().length()<1)
                {
                    promo_ed.setError("Promo Code Can't be blank");
                }
                else {

                    if(ConnectivityReceiver.isConnected())
                    {
                        baseActivity.showCustomProgrssDialog(getActivity());
                        ConnectivityReceiver.isConnected();
                        mapobject = signatureobject();
                        mapheader=headerObject();
                        new PostRequest(getActivity(),mapobject,mapheader,PromoCode.this, NetworkUtility.USERS_CLAIM,"EmailActivation");
                    }

                    else{

                        baseActivity.showAlertDialog(getResources().getString(R.string.dlg_nointernet));

                    }


                }

            }
        });

        return view;
    }

    public Map<String,String> signatureobject()
    {
        mapobject.put("code",""+promo_ed.getText().toString().trim());
        return mapobject;
    }
    public Map<String,String> headerObject()
    {
        mapheader.put("token",sessionManager.getValues(UserSessionManager.KEY_TOKEN));
        return mapheader;
    }

    @Override
    public void onSuccess(String result, String type) throws JSONException {

        baseActivity.hideCustomProgrssDialog();
        Log.d("PromoCodeRes>>>",result);
        try {
            JSONObject jObj=new JSONObject(result);
            JSONObject jsonObject=jObj.optJSONObject("result");
            String status=jsonObject.optString("status");
            String message=jsonObject.optString("message");
            if(status.equalsIgnoreCase("true"))
            {
                Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
            }
            else {
                baseActivity.showAlertDialog(message);
            }

        }catch (Exception e)
        {
            Log.d("ErrorRes>>>",e.getMessage());
        }

    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

    }
}
