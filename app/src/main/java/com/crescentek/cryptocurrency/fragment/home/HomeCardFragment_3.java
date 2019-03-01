package com.crescentek.cryptocurrency.fragment.home;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.activity.verification_level_2.DocumentType;

public class HomeCardFragment_3 extends android.support.v4.app.Fragment {

    String status;
    TextView title_tv,body_txt;
    Button btn_upload;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_card_container_1_1,container,false);

        status=getArguments().getString("status");
        title_tv=view.findViewById(R.id.title_tv);
        body_txt=view.findViewById(R.id.body_txt);
        btn_upload=view.findViewById(R.id.btn_upload);

        if(status.equalsIgnoreCase("Pending"))
        {
            title_tv.setText("Identity under review");
            body_txt.setText("Thank you for providing the required documentation. We are now busy reviewing your information. Due to high volume of new customers, this can take up to five business days.");
            btn_upload.setVisibility(View.GONE);

        }
        else {
            title_tv.setText("Document Rejected");
            body_txt.setText("Your document is rejected. \nPlease upload Again.");

            btn_upload.setVisibility(View.VISIBLE);
        }

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),DocumentType.class));
            }
        });


        return view;
    }
}
