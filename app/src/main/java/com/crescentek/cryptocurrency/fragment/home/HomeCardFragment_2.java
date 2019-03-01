package com.crescentek.cryptocurrency.fragment.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.interfaces.ApiRequestListener;
import com.crescentek.cryptocurrency.model.HomeContent;
import com.crescentek.cryptocurrency.network.GetRequest;
import com.crescentek.cryptocurrency.utility.BaseActivity;
import com.crescentek.cryptocurrency.utility.ConnectivityReceiver;
import com.crescentek.cryptocurrency.utility.NetworkUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by R.Android on 21-06-2018.
 */

public class HomeCardFragment_2 extends Fragment implements ApiRequestListener{

    List<HomeContent> homeContentList;

    LinearLayout list_recycled_parts;

    LayoutInflater inflater;

    BaseActivity baseActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_card_container_2,container,false);
        this.inflater = inflater;

        baseActivity=new BaseActivity(){};

        homeContentList=new ArrayList<HomeContent>();

        list_recycled_parts = (LinearLayout)view.findViewById(R.id.list_recycled_parts);

        callHomeContent();

        return view;
    }

    public void callHomeContent()
    {
        if(ConnectivityReceiver.isConnected())
        {
            new GetRequest(getActivity(),HomeCardFragment_2.this,"HOME_CONTENT", NetworkUtility.HOME_CONTENT);
        }
        else{

            baseActivity.showAlertDialog(getResources().getString(R.string.dlg_nointernet));
        }


    }

    @Override
    public void onSuccess(String result, String type) throws JSONException {

        Log.d("HomeResponse>>>",result);
        JSONObject jsonObject=new JSONObject(result);
        String status=jsonObject.optString("status");
        if (status.equalsIgnoreCase("true"))
        {
            JSONArray data=jsonObject.optJSONArray("data");
            if(data!=null)
            {
                for(int i=0;i<data.length();i++)
                {
                    JSONObject jObj=data.optJSONObject(i);
                    HomeContent homeContent=new HomeContent();
                    homeContent.setBlog_content_tag(jObj.optString("blog_content_tag"));
                    homeContent.setBlog_content_subject(jObj.optString("blog_content_subject"));
                    homeContent.setBlog_content_pic(jObj.optString("blog_content_pic"));
                    homeContent.setBlog_content(jObj.optString("blog_content"));

                    homeContentList.add(homeContent);
                }

                if(homeContentList.size()>0){
                    populateView(homeContentList);
                }

            }
        }

    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

    }

    public void populateView(List<HomeContent> listHomeData){

        for (int i = 0; i<listHomeData.size(); i++){

            HomeContent homeContent = listHomeData.get(i);

            View view = inflater.inflate(R.layout.list_item_home, null);
            TextView blog_content_subject = (TextView) view.findViewById(R.id.blog_content_subject);
            TextView blog_content=view.findViewById(R.id.blog_content);
            LinearLayout item_views=view.findViewById(R.id.item_views);
            blog_content_subject.setText(homeContent.getBlog_content_subject());
            blog_content.setText(homeContent.getBlog_content());

            list_recycled_parts.addView(view);
        }

    }

}
