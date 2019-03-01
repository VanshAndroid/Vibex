package com.crescentek.cryptocurrency.fragment.promotions;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.interfaces.ApiRequestListener;
import com.crescentek.cryptocurrency.network.GetRequest;
import com.crescentek.cryptocurrency.utility.BaseActivity;
import com.crescentek.cryptocurrency.utility.NetworkUtility;
import com.crescentek.cryptocurrency.utility.UserSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by R.Android on 31-07-2018.
 */

public class InviteFriends extends Fragment{


    private Button invite_btn;
    private TextView invitationCode_tv;
    BaseActivity baseActivity;
    UserSessionManager sessionManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.invite_friends,container,false);

        sessionManager=new UserSessionManager(getActivity());
        baseActivity=new BaseActivity() {};

        invite_btn=view.findViewById(R.id.invite_btn);
        invitationCode_tv=view.findViewById(R.id.invitationCode_tv);

        invitationCode_tv.setText(sessionManager.getValues(UserSessionManager.KEY_REFERAL_CODE));
        invite_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Vibex");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Bitcoin when you buy or sell NGN 5000.00 (exchange excluded), using https://www.vibex.com/invite/"+invitationCode_tv.getText().toString());
                startActivity(Intent.createChooser(sharingIntent, "Send Invite to"));

            }
        });

        //getrefferalCode();

        return view;
    }

    /*public void getrefferalCode()
    {
        baseActivity.showCustomProgrssDialog(getActivity());
        new GetRequest(getActivity(),InviteFriends.this,"Referal", NetworkUtility.REFFERAL_CODE);
    }

    @Override
    public void onSuccess(String result, String type) throws JSONException {

        baseActivity.hideCustomProgrssDialog();
        Log.d("ReferaResponse>>>",result);
        try {
            JSONObject jObj=new JSONObject(result);
            String status=jObj.optString("status");
            if(status.equalsIgnoreCase("true"))
            {
                JSONObject data=jObj.optJSONObject("data");
                String referral_code=data.optString("referral_code");
                invitationCode_tv.setText(referral_code);
            }
        }catch (Exception e)
        {
            Log.d("ErrorResponse>>>",e.getMessage());
        }

    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

    }*/
}
