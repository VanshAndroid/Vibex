package com.crescentek.cryptocurrency.activity.QrCode;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.activity.send.SendBitcoin_1;
import com.crescentek.cryptocurrency.activity.send.SendBitcoin_2;
import com.crescentek.cryptocurrency.utility.BaseActivity;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView.OnQRCodeReadListener;

public class QrcodeActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback, OnQRCodeReadListener {

  private static final int MY_PERMISSION_REQUEST_CAMERA = 0;
  private ViewGroup mainLayout;
  private TextView resultTextView;
  private QRCodeReaderView qrCodeReaderView;
  private CheckBox flashlightCheckBox;
  private CheckBox enableDecodingCheckBox;
  private PointsOverlayView pointsOverlayView;

  private ImageView back_iv;
  private TextView headerText_tv;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setStatusBarGradiant(QrcodeActivity.this);
    setContentView(R.layout.activity_qr_code);

    mainLayout = (ViewGroup) findViewById(R.id.main_layout);
    back_iv=findViewById(R.id.back_iv);
    headerText_tv=findViewById(R.id.headerText_tv);

    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        == PackageManager.PERMISSION_GRANTED) {
      initQRCodeReaderView();
    } else {
      requestCameraPermission();
    }

    back_iv.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });

    headerText_tv.setText("Scan Qr Code");
  }

  @Override protected void onResume() {
    super.onResume();

    if (qrCodeReaderView != null) {
      qrCodeReaderView.startCamera();
    }
  }

  @Override protected void onPause() {
    super.onPause();

    if (qrCodeReaderView != null) {
      qrCodeReaderView.stopCamera();
    }
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    if (requestCode != MY_PERMISSION_REQUEST_CAMERA) {
      return;
    }

    if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      Snackbar.make(mainLayout, "Camera permission was granted.", Snackbar.LENGTH_SHORT).show();
      initQRCodeReaderView();
    } else {
      Snackbar.make(mainLayout, "Camera permission request was denied.", Snackbar.LENGTH_SHORT)
          .show();
    }
  }


  @Override public void onQRCodeRead(String text, PointF[] points) {

    Log.d("ScannedData>>>",text);

    resultTextView.setText(text);

    String cryptoAddress=text.substring(0,text.indexOf("CryptoId"));
    String cryptoId=text.substring(text.indexOf("CryptoId=")+9,text.indexOf("CryptoCode"));
    String cryptoCode=text.substring(text.indexOf("CryptoCode=")+11,text.indexOf("CurrencyCode"));
    String currencyCode=text.substring(text.indexOf("CurrencyCode=")+13,text.length());

    Intent intent=new Intent(QrcodeActivity.this, SendBitcoin_2.class);
    intent.putExtra("contact",""+cryptoAddress);
    intent.putExtra("amount","");
    intent.putExtra("cryptoid",cryptoId);
    intent.putExtra("local_currency_code",currencyCode);
    intent.putExtra("crypto_code",cryptoCode);
    startActivity(intent);
    finish();
    //Toast.makeText(getApplicationContext(),""+text,Toast.LENGTH_LONG).show();
    pointsOverlayView.setPoints(points);
  }

  private void requestCameraPermission() {
    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
      Snackbar.make(mainLayout, "Camera access is required to display the camera preview.",
          Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
        @Override public void onClick(View view) {
          ActivityCompat.requestPermissions(QrcodeActivity.this, new String[] {
              Manifest.permission.CAMERA
          }, MY_PERMISSION_REQUEST_CAMERA);
        }
      }).show();
    } else {
      Snackbar.make(mainLayout, "Permission is not available. Requesting camera permission.",
          Snackbar.LENGTH_SHORT).show();
      ActivityCompat.requestPermissions(this, new String[] {
          Manifest.permission.CAMERA
      }, MY_PERMISSION_REQUEST_CAMERA);
    }
  }

  private void initQRCodeReaderView() {
    View content = getLayoutInflater().inflate(R.layout.qr_content, mainLayout, true);

    qrCodeReaderView = (QRCodeReaderView) content.findViewById(R.id.qrdecoderview);
    resultTextView = (TextView) content.findViewById(R.id.result_text_view);
    flashlightCheckBox = (CheckBox) content.findViewById(R.id.flashlight_checkbox);
    enableDecodingCheckBox = (CheckBox) content.findViewById(R.id.enable_decoding_checkbox);
    pointsOverlayView = (PointsOverlayView) content.findViewById(R.id.points_overlay_view);

    qrCodeReaderView.setAutofocusInterval(2000L);
    qrCodeReaderView.setOnQRCodeReadListener(this);
    qrCodeReaderView.setBackCamera();
    flashlightCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        qrCodeReaderView.setTorchEnabled(isChecked);
      }
    });
    enableDecodingCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        qrCodeReaderView.setQRDecodingEnabled(isChecked);
      }
    });
    qrCodeReaderView.startCamera();
  }
}