package com.crescentek.cryptocurrency.activity.verification_level_2;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.utility.BaseActivity;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


/**
 * Created by R.Android on 27-07-2018.
 */

public class DocumentType extends BaseActivity {


    private LinearLayout bvn_layout,driving_layout,passport_layout,other_layout;
    String type;
    private ImageView back_iv;
    private TextView headerText_tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(DocumentType.this);
        setContentView(R.layout.document_type);

        initView();

        type=getIntent().getStringExtra("type");

        bvn_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(),DocumentUploadingView.class);
                intent.putExtra("kyc_type","BVN");
                startActivity(intent);

                //startActivity(new Intent(getApplicationContext(),DocumentUploadingView.class));

            }
        });
        driving_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(),DocumentUploadingView.class);
                intent.putExtra("kyc_type","Driving License");
                startActivity(intent);

                //startActivity(new Intent(getApplicationContext(),DocumentUploadingView.class));

            }
        });
        passport_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(),DocumentUploadingView.class);
                intent.putExtra("kyc_type","Passport");
                startActivity(intent);

            }
        });
        other_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(),DocumentUploadingView.class);
                intent.putExtra("kyc_type","Utility Bill");
                startActivity(intent);

            }
        });

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    public void initView()
    {
        bvn_layout=findViewById(R.id.bvn_layout);
        driving_layout=findViewById(R.id.driving_layout);
        passport_layout=findViewById(R.id.passport_layout);
        other_layout=findViewById(R.id.other_layout);
        back_iv=findViewById(R.id.back_iv);
        headerText_tv=findViewById(R.id.headerText_tv);


        headerText_tv.setText("Document Type");
    }


}
