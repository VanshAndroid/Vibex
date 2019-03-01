package com.crescentek.cryptocurrency.activity.verification_level_2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.utility.BaseActivity;

/**
 * Created by R.Android on 27-07-2018.
 */

public class UploadDocumentActivity extends BaseActivity {

    private ImageView back_iv;
    private TextView headerText_tv,take_photo_tv,select_file_tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(UploadDocumentActivity.this);
        setContentView(R.layout.upload_document);

        initView();

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        take_photo_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(),DocumentType.class);
                intent.putExtra("type","camera");
                startActivity(intent);

            }
        });

        select_file_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(),DocumentType.class);
                intent.putExtra("type","files");
                startActivity(intent);

            }
        });


    }

    public void initView()
    {
        back_iv=findViewById(R.id.back_iv);
        headerText_tv=findViewById(R.id.headerText_tv);
        take_photo_tv=findViewById(R.id.take_photo_tv);
        select_file_tv=findViewById(R.id.select_file_tv);

        headerText_tv.setText("Upload Document");

    }

}
