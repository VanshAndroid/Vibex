package com.crescentek.cryptocurrency.activity.verification_level_2;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.utility.AndroidMultiPartEntity;
import com.crescentek.cryptocurrency.utility.BaseActivity;
import com.crescentek.cryptocurrency.utility.ConnectivityReceiver;
import com.crescentek.cryptocurrency.utility.NetworkUtility;
import com.crescentek.cryptocurrency.utility.UserSessionManager;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

/**
 * Created by R.Android on 30-07-2018.
 */

public class DocumentUploadingView extends BaseActivity {

    private ImageView imageViewDoc, imageViewDocBack,back_iv;
    private TextView headerText_tv,retakePhoto_tv;
    UserSessionManager sessionManager;
    private Button use_photo_btn;
    String imagePathFront="", imagePathBack = "";
    boolean isFrontSelect = true;
    String kyc_type="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(DocumentUploadingView.this);
        setContentView(R.layout.doument_upload_view);
        sessionManager=new UserSessionManager(DocumentUploadingView.this);

        initView();
        kyc_type=getIntent().getStringExtra("kyc_type");
        selectImage();

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        use_photo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(imagePathFront.equalsIgnoreCase(""))
                {

                }else {


                    if(ConnectivityReceiver.isConnected())
                    {
                        ConnectivityReceiver.isConnected();
                        showCustomProgrssDialog(DocumentUploadingView.this);
                        new UploadPhotoServer(imagePathFront, imagePathBack).execute();

                    }
                    else {
                        showAlertDialog(getResources().getString(R.string.dlg_nointernet));
                    }


                }


            }
        });

        retakePhoto_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        imageViewDocBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFrontSelect = false;
                selectImage();
            }
        });

        imageViewDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFrontSelect = true;
                selectImage();
            }
        });

    }

    public void initView()
    {
        imageViewDocBack=findViewById(R.id.imageViewDocBack);
        imageViewDoc=findViewById(R.id.imageViewDoc);
        back_iv=findViewById(R.id.back_iv);
        headerText_tv=findViewById(R.id.headerText_tv);
        use_photo_btn=findViewById(R.id.use_photo_btn);
        retakePhoto_tv=findViewById(R.id.retakePhoto_tv);

        headerText_tv.setText("National identity");
    }

    public void selectImage()
    {
        CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("Image>>>>",requestCode+"::::"+resultCode+">>>>"+isFrontSelect);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                if(isFrontSelect) {
                imageViewDoc.setImageURI(result.getUri());
                Uri uri=result.getUri();
                imagePathFront=uri.getPath();
                Log.d("imagePath FRONT ",imagePathFront);
                Toast.makeText(this, "Cropping successful: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
                }else {
                    imageViewDocBack.setImageURI(result.getUri());
                    Uri uri=result.getUri();
                    imagePathBack=uri.getPath();
                    Log.d("imagePath BACK: ",imagePathBack);
                    Toast.makeText(this, "Cropping successful: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }




    public class UploadPhotoServer extends AsyncTask<Void, Void, String> {

        long totalSize = 0;

        String p_image_path_front = "", p_image_path_back = "";

        public UploadPhotoServer(String p_image_path_front, String p_image_path_back){
            this.p_image_path_front = p_image_path_front;
            this.p_image_path_back = p_image_path_back;
        }

        @Override
        protected void onPreExecute() {

            Log.d(">>A","\n"+p_image_path_front);
            Log.d(">>B","\n"+p_image_path_back);
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(NetworkUtility.IMAGE_UPLOAD);
            Log.d("HostUrl>>>>",httppost.toString());
            httppost.addHeader("token",sessionManager.getValues(UserSessionManager.KEY_TOKEN));

            try {

                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {
                            @Override
                            public void transferred(long num) {
                                //publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                Log.d("ImagePath F>>>",p_image_path_front);
                Log.d("ImagePath B >>>",p_image_path_back);
                File photoFront = new File(p_image_path_front.replace("file://", ""));
                File photoBack = new File(p_image_path_front.replace("file://", ""));


                entity.addPart("frontDoc", new FileBody(photoFront));
                entity.addPart("backDoc", new FileBody(photoBack));
                entity.addPart("kyc_type",new StringBody(kyc_type));
                entity.addPart("kyc_name", new StringBody(kyc_type));


                totalSize = entity.getContentLength();
                httppost.setEntity(entity);
                Log.d("ENTITY>>>>",""+entity+"::::"+totalSize+"::::");
                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "+ statusCode;
                }
            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }
            Log.d("ErrorMsg",responseString);
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            //System.out.println("==========\n"+result);
            Log.d("Response=========",result);
            hideCustomProgrssDialog();

            try {
                JSONObject jsonObj = new JSONObject(result);
                String status=jsonObj.optString("status");
                String message=jsonObj.optString("message");
                if(status.equalsIgnoreCase("true")) {

                    Intent intent=new Intent(getApplicationContext(),UploadSuccess.class);
                    startActivity(intent);

                }
                else {
                    showAlertDialog(message);
                }


            } catch (Exception e) {
                // TODO: handle exception
            }

            super.onPostExecute(result);
        }

    }

}
