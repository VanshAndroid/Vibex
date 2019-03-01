package com.crescentek.cryptocurrency.utility;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import com.chootdev.csnackbar.Align;
import com.chootdev.csnackbar.Duration;
import com.chootdev.csnackbar.Type;
import com.crescentek.cryptocurrency.R;

public class BaseActivity extends AppCompatActivity implements BaseView, ConnectivityReceiver.ConnectivityReceiverListener {

    protected ProgressDialog progressDialog;
    protected Dialog customProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public BaseActivity()
    {

    }



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.gradient_theme);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            //window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiantYellow(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.yellow_for_base_activity);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            //window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiantSky(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.sky_for_base_activity);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            //window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiantBlue(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.blue_for_base_activity);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            //window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarBlue(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.blue_for_home);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            //window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusTransparent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.transparent_for_more);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            //window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }



    protected void showProgressDialog(String message) {
        if (progressDialog == null || !progressDialog.isShowing()) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(message);
            progressDialog.setCanceledOnTouchOutside(false);
            if (!isFinishing()) {
                progressDialog.show();
            }
        }
    }

    protected void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            if (!isFinishing()) {
                progressDialog.dismiss();
            }
        }
    }

    public void showAlertDialog(final String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.app_name);
        if (message != null) {
            builder.setMessage(Html.fromHtml(message));
        } else {
            builder.setMessage("");
        }
        builder.setPositiveButton(android.R.string.ok, null);

        if (!isFinishing()) {
            builder.show();
        }
    }

    public boolean showAlertDialogWithType(final String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.app_name);
        if (message != null) {
            builder.setMessage(Html.fromHtml(message));
        } else {
            builder.setMessage("");
        }
        builder.setPositiveButton(android.R.string.ok, null);

        if (!isFinishing()) {
            builder.show();

            return true;
        }

        return false;
    }

    public void showCustomProgrssDialog(final Context activity){

        customProgressDialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
        customProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customProgressDialog.setCancelable(false);
        customProgressDialog.setContentView(R.layout.dialog_custom_progressdialog);

        if(customProgressDialog.isShowing()){

        }else {
            customProgressDialog.show();
        }

    }
    public void hideCustomProgrssDialog(){
        if(customProgressDialog != null){
            customProgressDialog.dismiss();
        }
        else {
        }
    }

    @Override
    public void showProgress(String message) {
        showProgressDialog(message);
    }

    @Override
    public void hideProgress() {
        hideProgressDialog();
    }

    @Override
    public void showError(String message) {
        showAlertDialog(message);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        Log.d("Res: ",""+isConnected);
        showSnack(isConnected);
       // Toast.makeText(this,"--"+isConnected,Toast.LENGTH_SHORT).show();
    }

    // Showing the status in Snackbar
    protected void showSnack(boolean isConnected) {
        String message;
        if (isConnected) {

        } else {
            message = "Sorry! Not connected to Internet";
            com.chootdev.csnackbar.Snackbar.with(this,null)
                    .type(Type.ERROR)
                    .message(message)
                    .duration(Duration.LONG)
                    .fillParent(true)
                    .textAlign(Align.CENTER)
                    .show();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }

}
