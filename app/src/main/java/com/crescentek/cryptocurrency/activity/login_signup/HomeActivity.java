package com.crescentek.cryptocurrency.activity.login_signup;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.fragment.navigation.AccountsFragment;
import com.crescentek.cryptocurrency.fragment.navigation.ExchangeFragment;
import com.crescentek.cryptocurrency.fragment.FragmentDrawer;
import com.crescentek.cryptocurrency.fragment.navigation.HelpCentreFragment;
import com.crescentek.cryptocurrency.fragment.navigation.PriceAlertsFragment;
import com.crescentek.cryptocurrency.fragment.navigation.PromotionFragments;
import com.crescentek.cryptocurrency.fragment.navigation.TransactionsFragment;
import com.crescentek.cryptocurrency.fragment.navigation.HomeFragment;
import com.crescentek.cryptocurrency.fragment.navigation.SettingFragment;
import com.crescentek.cryptocurrency.fragment.navigation.WalletFragment;
import com.crescentek.cryptocurrency.utility.BaseActivity;

import java.util.List;


public class HomeActivity extends BaseActivity implements FragmentDrawer.FragmentDrawerListener {

    private static String TAG = HomeActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    int backPos=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(HomeActivity.this);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);


        String getValue=getIntent().getStringExtra("position");
        final String price_alert=getIntent().getStringExtra("price_alert");

        if(price_alert!=null&&price_alert.equalsIgnoreCase("3"))
        {
            displayView(3);
        }
        else {

            loadHomeFragment();
        }

    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        backPos=position;

        switch (position) {
            case 0:
                fragment = new HomeFragment(0);
                title = getString(R.string.title_home);

                break;
            case 1:
                fragment = new AccountsFragment();
                title = getString(R.string.title_Wallets);
                break;
            case 2:
                fragment = new TransactionsFragment();
                title = getString(R.string.title_Transaction);
                break;
            case 3:
                fragment = new PriceAlertsFragment();
                title = getString(R.string.title_price_alert);
                break;
            case 4:
                fragment = new PromotionFragments();
                title = getString(R.string.title_Promotions);
                break;
            case 5:
                fragment = new ExchangeFragment();
                title = getString(R.string.title_Exchange);
                break;
            case 6:
                fragment = new SettingFragment();
                title = getString(R.string.title_Settings);
                break;
            case 7:
                fragment = new HelpCentreFragment();
                title = getString(R.string.title_help_center);
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }


    public void loadHomeFragment(){
        Fragment fragment = new HomeFragment(1);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        getSupportActionBar().setTitle("");

    }

    public void callHomeFragment(Fragment fragment, String title){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        // set the toolbar title
        getSupportActionBar().setTitle(title);

    }

    @Override
    public void onBackPressed() {

        if(backPos==0)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            HomeActivity.this.finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        else {
            loadHomeFragment();
            backPos=0;
        }


    }


}

