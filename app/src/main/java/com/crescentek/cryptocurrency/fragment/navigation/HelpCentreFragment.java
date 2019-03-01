package com.crescentek.cryptocurrency.fragment.navigation;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.activity.help.ContactSupportActivity;
import com.crescentek.cryptocurrency.activity.help.SecurityInfoActivity;
import com.crescentek.cryptocurrency.interfaces.ApiRequestListener;
import com.crescentek.cryptocurrency.model.HomeContent;
import com.crescentek.cryptocurrency.model.Questions;
import com.crescentek.cryptocurrency.model.Topics;
import com.crescentek.cryptocurrency.network.GetRequest;
import com.crescentek.cryptocurrency.utility.BaseActivity;
import com.crescentek.cryptocurrency.utility.ConnectivityReceiver;
import com.crescentek.cryptocurrency.utility.NetworkUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by R.Android on 30-07-2018.
 */

public class HelpCentreFragment extends Fragment implements ApiRequestListener{

    private Button contact_btn;
    BaseActivity baseActivity;
    private List<Topics> topicsList;
    private List<Questions> questionsList;
    private LinearLayout list_topics,list_questions;

    LayoutInflater inflater;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.help_center_fragment,container,false);

        this.inflater=inflater;

        baseActivity=new BaseActivity();

        topicsList=new ArrayList<Topics>();

        questionsList=new ArrayList<Questions>();

        //two_factor_layout=view.findViewById(R.id.two_factor_layout);

        contact_btn=view.findViewById(R.id.contact_btn);

        list_topics=view.findViewById(R.id.list_topics);

        list_questions=view.findViewById(R.id.list_questions);

        /*two_factor_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), SecurityInfoActivity.class));

            }
        });*/

        contact_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), ContactSupportActivity.class));

            }
        });

        callApiForFAQ();

        return view;
    }

    public void callApiForFAQ()
    {
        if(ConnectivityReceiver.isConnected())
        {
            ConnectivityReceiver.isConnected();
            baseActivity.showCustomProgrssDialog(getActivity());
            new GetRequest(getActivity(),HelpCentreFragment.this,"HELP_CENTER", NetworkUtility.HELP_CENTER);
        }

        else{

            baseActivity.showAlertDialog(getResources().getString(R.string.dlg_nointernet));

        }


    }

    @Override
    public void onSuccess(String result, String type) throws JSONException {

        baseActivity.hideCustomProgrssDialog();
        Log.d("HelpResponse>>>",result);
        try {
            JSONObject jsonObject=new JSONObject(result);
            String status=jsonObject.optString("status");
            if (status.equalsIgnoreCase("true"))
            {
                JSONObject jObj=jsonObject.optJSONObject("data");
                JSONArray topicsArray=jObj.optJSONArray("topics");
                JSONArray questionsArray=jObj.optJSONArray("questions");
                if(topicsArray!=null)
                {
                    for(int i=0;i<topicsArray.length();i++)
                    {
                        Topics topics=new Topics();
                        JSONObject jOb=topicsArray.optJSONObject(i);
                        topics.setBlog_content_tag(jOb.optString("blog_content_tag"));
                        topics.setBlog_content_subject(jOb.optString("blog_content_subject"));
                        topics.setBlog_content_pic(jOb.optString("blog_content_pic"));
                        topics.setBlog_content(jOb.optString("blog_content"));
                        topicsList.add(topics);
                    }
                    if(topicsList.size()>0){
                        populateViewTopics(topicsList);
                    }
                }
                if(questionsArray!=null)
                {

                    for(int i=0;i<questionsArray.length();i++)
                    {
                        Questions questions=new Questions();
                        JSONObject jOb=questionsArray.optJSONObject(i);
                        questions.setBlog_content_tag(jOb.optString("blog_content_tag"));
                        questions.setBlog_content_subject(jOb.optString("blog_content_subject"));
                        questions.setBlog_content_pic(jOb.optString("blog_content_pic"));
                        questions.setBlog_content(jOb.optString("blog_content"));
                        questionsList.add(questions);
                    }

                    if(questionsList.size()>0){
                        populateViewQuestions(questionsList);
                    }

                }

            }
        }
        catch (Exception e) {
            Log.d("ErrorResponse>>>",e.getMessage());
        }

    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

        baseActivity.hideCustomProgrssDialog();
        Log.d("HelpResponse>>>",responseMessage);

    }


    public void populateViewTopics(List<Topics> listHomeData){

        for (int i = 0; i<listHomeData.size(); i++){

            Topics topicsContent = listHomeData.get(i);

            View view = inflater.inflate(R.layout.list_item_help, null);
            ImageView topic_icon = (ImageView) view.findViewById(R.id.topic_icon);
            TextView topic_subject=(TextView) view.findViewById(R.id.topic_subject);
            topic_subject.setText(topicsContent.getBlog_content_subject());

            list_topics.addView(view);
        }

    }

    public void populateViewQuestions(List<Questions> listHomeData){

        for (int i = 0; i<listHomeData.size(); i++){

            Questions questionContent = listHomeData.get(i);

            View view = inflater.inflate(R.layout.list_item_help, null);
            ImageView topic_icon = (ImageView) view.findViewById(R.id.topic_icon);
            TextView topic_subject=(TextView) view.findViewById(R.id.topic_subject);
            topic_subject.setText(questionContent.getBlog_content_subject());

            list_questions.addView(view);
        }

    }
}
