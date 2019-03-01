package com.crescentek.cryptocurrency.activity.withdraw;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.utility.BaseActivity;

/**
 * Created by R.Android on 11-07-2018.
 */

public class PinCode extends BaseActivity {

    private ImageView back_iv;
    private TextView headerText_tv;
    private EditText pin1_ed,pin2_ed,pin3_ed,pin4_ed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(PinCode.this);
        setContentView(R.layout.pin_activity);

        back_iv=findViewById(R.id.back_iv);
        headerText_tv=findViewById(R.id.headerText_tv);

        pin1_ed=findViewById(R.id.pin1_ed);
        pin2_ed=findViewById(R.id.pin2_ed);
        pin3_ed=findViewById(R.id.pin3_ed);
        pin4_ed=findViewById(R.id.pin4_ed);

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        headerText_tv.setText("Enter Pin");

        pin2_ed.setEnabled(false);
        pin3_ed.setEnabled(false);
        pin4_ed.setEnabled(false);

        pin1_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                pin2_ed.requestFocus();
                pin2_ed.setEnabled(true);


            }
        });
        pin2_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                pin3_ed.requestFocus();
                pin3_ed.setEnabled(true);

            }
        });
        pin3_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                pin4_ed.requestFocus();
                pin4_ed.setEnabled(true);

            }
        });


        pin4_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                //startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            }
        });


    }
}
