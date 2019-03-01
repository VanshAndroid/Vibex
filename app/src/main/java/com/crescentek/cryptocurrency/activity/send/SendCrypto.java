package com.crescentek.cryptocurrency.activity.send;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.adapter.SendCryptoAdapter;
import com.crescentek.cryptocurrency.comman.WalletRecord;
import com.crescentek.cryptocurrency.interfaces.WalletListener;
import com.crescentek.cryptocurrency.model.Contact;
import com.crescentek.cryptocurrency.model.CryptoM;
import com.crescentek.cryptocurrency.model.CurreencyM;
import com.crescentek.cryptocurrency.model.WalletN;
import com.crescentek.cryptocurrency.utility.BaseActivity;
import com.crescentek.cryptocurrency.utility.RecyclerTouchListener;
import com.crescentek.cryptocurrency.utility.UserSessionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by R.Android on 12-07-2018.
 */

public class SendCrypto extends BaseActivity implements WalletListener{

    private ImageView back_iv,info;
    private TextView headerText_tv;
    private LinearLayout bitcoin_layout,ethereum_layout;
    List<CryptoM> cryptoList;
    RecyclerView crypto_recycler;

    SendCryptoAdapter adapter;
    UserSessionManager sessionManager;
    String local_currency;
    List<Contact> contactVOList,contactFilterList;
    boolean contactList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(SendCrypto.this);
        setContentView(R.layout.send_bitcoin);
        showCustomProgrssDialog(SendCrypto.this);
        new WalletRecord(SendCrypto.this,SendCrypto.this);
        bitcoin_layout=findViewById(R.id.bitcoin_layout);
        ethereum_layout=findViewById(R.id.ethereum_layout);
        back_iv=findViewById(R.id.back_iv);
        info=findViewById(R.id.info);
        headerText_tv=findViewById(R.id.headerText_tv);
        crypto_recycler =findViewById(R.id.crypto_list);


        headerText_tv.setText("What would you like to send");
        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        requestRequiredPermission();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SendCrypto.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        crypto_recycler.setLayoutManager(linearLayoutManager);

        crypto_recycler.addOnItemTouchListener(new RecyclerTouchListener(SendCrypto.this, crypto_recycler, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {


                Log.d("crypto_name_id",cryptoList.get(position).getCrypto_name()+">>>>"+cryptoList.get(position).getCrypto_id());


                    Intent intent=new Intent(getApplicationContext(),SendBitcoin_1.class);
                    intent.putExtra("cryptoName",cryptoList.get(position).getCrypto_name());
                    intent.putExtra("cryptoid",cryptoList.get(position).getCrypto_id());
                    intent.putExtra("local_currency_code",local_currency);
                    intent.putExtra("crypto_code",cryptoList.get(position).getCrypto_code());
                    startActivity(intent);
                }


            @Override
            public void onLongClick(View view, int position) {

                Log.d("Position>>",position+"");

            }
        }));

        //getAllContacts();


    }



    private void requestRequiredPermission() {

        int hasREAD_CONTACTSPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        int hasWRITE_CONTACTSPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS);


        List<String> permissions = new ArrayList<String>();
        if (hasREAD_CONTACTSPermission != PackageManager.PERMISSION_GRANTED)
        {
            Log.e("Splash Request", "prepare fine permission");
            permissions.add(Manifest.permission.READ_CONTACTS);
        } else {
            Log.e("Splash Request", "READ_CONTACTS permission granted");
        }
        if (hasWRITE_CONTACTSPermission != PackageManager.PERMISSION_GRANTED) {
            Log.e("Splash Request", "prepare Storage permission");
            permissions.add(Manifest.permission.WRITE_CONTACTS);
        } else {
            Log.e("Splash Request", "WRITE_CONTACTS permission granted");
        }

        if (!permissions.isEmpty()) {
            Log.e("Splash Request", "Requesting Permission..." + permissions.size());
            ActivityCompat.requestPermissions(this, permissions.toArray(new String[permissions.size()]), 1);
        } else {

        }
    }

    @Override
    public void walletList(List<WalletN> walletList) throws Exception {

        hideCustomProgrssDialog();

    }

    @Override
    public void cryptoList(List<CryptoM> cryptoList) throws Exception {

        this.cryptoList =cryptoList;
        adapter=new SendCryptoAdapter(cryptoList);
        crypto_recycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void curreencyList(List<CurreencyM> curreencyList) throws Exception {

        local_currency=curreencyList.get(0).getCurrency_code();

    }




    private boolean getAllContacts() {
        contactVOList = new ArrayList();
        Contact contactVO;

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    contactVO = new Contact();
                    contactVO.setContactName(name);

                    Cursor phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id},
                            null);
                    if (phoneCursor.moveToNext()) {
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contactVO.setContactNumber(phoneNumber);
                    }

                    phoneCursor.close();

                    Cursor emailCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (emailCursor.moveToNext()) {
                        String emailId = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    }
                    contactVOList.add(contactVO);
                    sessionManager.setContact(UserSessionManager.KEY_CONTACT_DATA,contactVOList);
                    return true;
                }
            }

            //setContact_Recycler(contactVOList);
        }
        return false;
    }

}
