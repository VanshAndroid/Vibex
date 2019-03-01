package com.crescentek.cryptocurrency.utility;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.crescentek.cryptocurrency.activity.login_signup.LoginSignUpActivity;
import com.crescentek.cryptocurrency.model.Contact;
import com.crescentek.cryptocurrency.model.CryptoM;
import com.crescentek.cryptocurrency.model.CurreencyM;
import com.crescentek.cryptocurrency.model.WalletN;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


public class UserSessionManager {


    public static final String KEY_EMAIL = "email";
    public static final String KEY_FIRST_NAME = "first_name";
    public static final String KEY_LAST_NAME = "last_name";
    public static final String KEY_PHONE = "phone_number";
    public static final String KEY_COUNTRY = "country";
    public static final String KEY_VERIFICATION_LEVEL = "verification_level";
    public static final String KEY_VERIFICATION_LEVEL2 = "verification_level2";
    public static final String KEY_DOB="date_of_birth";
    public static final String KEY_BVN_NUMBER="bvn_number";

    public static final String KEY_USER_ID ="user_id";
    public static final String KEY_TOKEN="token";
    public static final String KEY_TOKEN_TEMP="token_temp";
    public static final String KEY_EMAIL_VERIFY="verify_email";
    public static final String KEY_PHONE_VERIFY="verify_phone";
    public static final String KEY_BVN_VERIFY="bvn_verify";
    public static final String KEY_T_PIN_VERIFY="transaction_pin";
    public static final String KEY_DOC_UPLOADED="doc_uploaded";
    public static final String KEY_TWO_FACTER="two_factor";
    public static final String KEY_USER_LEVEL="user_level";
    public static final String KEY_USER_LEVEL_ID="user_level_id";
    public static final String KEY_TOKEN_PHONE="token_phone";
    public static final String CRYPTO_ID="crypto_id";
    public static final String CURRENCY_CODE ="currency_code";
    public static final String CRYPTO_CODE ="crypto_code";
    public static final String BTC_WALLET="btc";
    public static final String NGN_WALLET="ngn";

    public static final String KEY_CAN_SEND="can_send";
    public static final String KEY_CAN_SELL="can_sell";
    public static final String KEY_CAN_BUY="can_buy";
    public static final String KEY_CAN_DEPOSIT="can_deposit";
    public static final String KEY_CAN_WITHDRAW="can_withdraw";
    public static final String KEY_CAN_TRADE="can_trade";
    public static final String KEY_LEVEL_NAME="level_name";
    public static final String KEY_PHONE_CODE="phonecode";
    public static final String KEY_CAN_WITHDRAW_LIMIT="can_withdraw_limit";
    public static final String KEY_CAN_DEPOSIT_LIMIT="can_deposit_limit";
    public static final String KEY_CRYPTO_WALLET="Key_Crypto_Wallet";
    public static final String KEY_CURRENCY_WALLET="Key_Crypto_Wallet";
    public static final String KEY_WALLET_DATA="key_wallet_data";
    public static final String KEY_CONTACT_DATA="contact_list";
    public static final String KEY_CONTACT_CODE="key_ontact_code";
    public static final String KEY_REFERAL_CODE="key_referal_code";



    public static final String[] KEY_FIELD = {KEY_EMAIL, KEY_FIRST_NAME,KEY_LAST_NAME,KEY_PHONE,
            KEY_COUNTRY, KEY_VERIFICATION_LEVEL,KEY_BVN_NUMBER,KEY_DOB, KEY_USER_ID,KEY_TOKEN,KEY_EMAIL_VERIFY,
            KEY_PHONE_VERIFY,KEY_BVN_VERIFY,KEY_T_PIN_VERIFY,KEY_DOC_UPLOADED,KEY_USER_LEVEL,KEY_USER_LEVEL_ID,KEY_TOKEN_TEMP,
            KEY_TOKEN_PHONE,KEY_TWO_FACTER,BTC_WALLET,NGN_WALLET,CRYPTO_CODE,KEY_CAN_SEND,KEY_CAN_SELL,KEY_CAN_BUY,KEY_CAN_DEPOSIT,
            KEY_CAN_WITHDRAW,KEY_CAN_TRADE,KEY_LEVEL_NAME,KEY_PHONE_CODE,KEY_CRYPTO_WALLET,KEY_CURRENCY_WALLET,KEY_WALLET_DATA,
            KEY_REFERAL_CODE,KEY_CONTACT_DATA
    };


    // Sharedpref file name
    private static final String PREFER_NAME = "Vibex";

    SharedPreferences pref;
    // Editor reference for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;



    // Constructor
    public UserSessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();

    }


    /**
     * Clear session details
     */
    public void logoutUser() {

        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, LoginSignUpActivity.class);

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);

    }

    public void clearUser() {

        // Clearing all user data from Shared Preferences
        editor.clear ();
        editor.commit();

    }



    // Set Dept Values
    public void setValues(String TAG, String deptFrom) {

        //Dept from
        editor.putString(TAG, deptFrom);

        // commit changes
        editor.commit();
    }


    public String getValues(String TAG){
        return pref.getString(TAG, null);
    }

    // Set Dept Values
    public void setBooleanValue(String TAG, boolean value) {

        //Dept from
        editor.putBoolean(TAG, value);

        // commit changes
        editor.commit();
    }

    public boolean getBooleanValue(String TAG) {

        return pref.getBoolean(TAG, false);
    }

    //--------------------- Crypto------------------------------------

    public <T> void setCrypto(String key, List<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        setValues(key, json);
        //editor.putString(key, json);


    }

    public List<CryptoM> getCrypto(String key) {
        if (pref != null) {

            Gson gson = new Gson();
            List<CryptoM> cryptoList;

            String string = pref.getString(key, null);
            Type type = new TypeToken<List<CryptoM>>() {
            }.getType();
            cryptoList = gson.fromJson(string, type);
            return cryptoList;
        }
        return null;
    }

    //--------------Currency-------------------------

    public <T> void setCurrency(String key, List<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        setValues(key, json);
    }

    public List<CurreencyM> getCurrency(String key) {
        if (pref != null) {

            Gson gson = new Gson();
            List<CurreencyM> curreencyList;

            String string = pref.getString(key, null);
            Type type = new TypeToken<List<CurreencyM>>() {
            }.getType();
            curreencyList = gson.fromJson(string, type);
            return curreencyList;
        }
        return null;
    }

    //--------------------- Wallet ALL Data---------------------

    public <T> void setWallet(String key, List<T> list) {
        Log.d("WalletSizeInPrefence",list.size()+"");
        Gson gson = new Gson();
        String json = gson.toJson(list);
        Log.d("WalletSizeInString",json+"");
        setValues(key, json);
    }

    public List<WalletN> getWallet(String key) {
        if (pref != null) {

            Gson gson = new Gson();
            List<WalletN> walletList;

            String string = pref.getString(key, null);
            Type type = new TypeToken<List<WalletN>>() {
            }.getType();
            walletList = gson.fromJson(string, type);
            return walletList;
        }
        return null;
    }



    public <T> void setContact(String key, List<T> list) {
        Log.d("WalletSizeInPrefence",list.size()+"");
        Gson gson = new Gson();
        String json = gson.toJson(list);
        Log.d("WalletSizeInString",json+"");
        setValues(key, json);
    }

    public List<Contact> getContact(String key) {
        if (pref != null) {

            Gson gson = new Gson();
            List<Contact> contactList;

            String string = pref.getString(key, null);
            Type type = new TypeToken<List<Contact>>() {
            }.getType();
            contactList = gson.fromJson(string, type);
            return contactList;
        }
        return null;
    }



}