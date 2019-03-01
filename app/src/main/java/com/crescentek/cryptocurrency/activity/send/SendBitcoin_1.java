package com.crescentek.cryptocurrency.activity.send;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.activity.QrCode.QrcodeActivity;
import com.crescentek.cryptocurrency.adapter.AllContactsAdapter;
import com.crescentek.cryptocurrency.interfaces.ApiRequestListener;
import com.crescentek.cryptocurrency.model.Contact;
import com.crescentek.cryptocurrency.network.PostRequest;
import com.crescentek.cryptocurrency.utility.BaseActivity;
import com.crescentek.cryptocurrency.utility.ConnectivityReceiver;
import com.crescentek.cryptocurrency.utility.NetworkUtility;
import com.crescentek.cryptocurrency.utility.RecyclerTouchListener;
import com.crescentek.cryptocurrency.utility.UserSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by R.Android on 02-08-2018.
 */

public class SendBitcoin_1 extends BaseActivity implements ApiRequestListener{

    private FrameLayout next_view;
    private ImageView back_iv,scanQr_iv,cancel_iv,contact_iv;
    private TextView headerText_tv,contact_address,contact_hint;
    private EditText email_ed;
    LinearLayout address_not_layout,address_send_layout;

    private RecyclerView recycler_view;

    List<Contact> contactVOList,contactFilterList;
    private Map<String,String> mapobject,mapheader;
    UserSessionManager sessionManager;
    String contactValue="";
    String amount="";
    String cryptoName,cryptoId;
    FrameLayout sendBtn;

    AllContactsAdapter contactAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(SendBitcoin_1.this);
        setContentView(R.layout.send_bitcoin_1);
        cryptoName=getIntent().getStringExtra("cryptoName");
        cryptoId=getIntent().getStringExtra("cryptoid");
        requestRequiredPermission();
        mapobject=new HashMap<String, String>();
        mapheader=new HashMap<String, String>();
        sessionManager=new UserSessionManager(SendBitcoin_1.this);
        contactFilterList=new ArrayList<>();
        //contactFilterList=new ArrayList<>();

        initViews();

        sendBtn.setVisibility(View.GONE);
        cancel_iv.setVisibility(View.VISIBLE);

        if(getIntent().getStringExtra("address")!=null)
        {
            contactValue=getIntent().getStringExtra("address");
            if(getIntent().getStringExtra("amount")!=null)
            {
                amount=getIntent().getStringExtra("amount");
            }
            callcheckUserApi(getIntent().getStringExtra("address"));
        }

        headerText_tv.setText("Send "+cryptoName);
        contact_hint.setText("CONTACT");
        contact_iv.setVisibility(View.VISIBLE);
        recycler_view=findViewById(R.id.recycler_view);
        address_not_layout.setVisibility(View.GONE);
        address_send_layout.setVisibility(View.GONE);

        //------------------------------------Listener---------------------------------

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                contactValue = email_ed.getText().toString();

                Log.d(">>>>====Contact=====->>",contactValue);

                callcheckUserApi(email_ed.getText().toString());

            }
        });


        scanQr_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), QrcodeActivity.class));

            }
        });


        email_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(email_ed.getText().toString().length()<=9){

                    sendBtn.setVisibility(View.GONE);
                    cancel_iv.setVisibility(View.VISIBLE);
                }
                else{
                    sendBtn.setVisibility(View.VISIBLE);
                    cancel_iv.setVisibility(View.GONE);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

                String address_phone=email_ed.getText().toString();
                if(address_phone.contains("@")||address_phone.contains("[0-9]+"))
                {
                    recycler_view.setVisibility(View.GONE);
                    address_not_layout.setVisibility(View.VISIBLE);
                    if(address_phone.contains("."))
                    {
                        address_send_layout.setVisibility(View.VISIBLE);
                        address_not_layout.setVisibility(View.GONE);
                        contact_hint.setText("SEND TO EMAIL ADDRESS");
                        contact_iv.setImageResource(R.drawable.ic_email_sky_blue_24dp);
                        contact_address.setText(address_phone);
                        if(email_ed.getText().toString().length()>=9)
                        {
                            sendBtn.setVisibility(View.GONE);
                            cancel_iv.setVisibility(View.VISIBLE);
                        }
                        else {
                            sendBtn.setVisibility(View.VISIBLE);
                            cancel_iv.setVisibility(View.GONE);
                        }

                    }

                }
                else {

                    if(contactAdapter !=null){

                        contactAdapter.getFilter().filter(address_phone);

                    }


                }
            }
        });

        address_send_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                contactValue=email_ed.getText().toString().trim();

                Log.d(">>>>---Contact---->>",contactValue);

                callcheckUserApi(email_ed.getText().toString().trim());

            }
        });

        cancel_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email_ed.setText("");
                setContact_Recycler(contactVOList);
            }
        });

       // getAllContacts();


        String value = sessionManager.getValues(UserSessionManager.KEY_CONTACT_CODE);
        if(value != null && value.equalsIgnoreCase("1")){

            // check load from local contact
            loadContactsFromLocal();

        }else {

            // reload contact list

            sessionManager.setValues(UserSessionManager.KEY_CONTACT_CODE,"1");
            refreshContactList();
        }



    }


    public void callcheckUserApiMethod(Contact contact){

        Log.d("Number>>>",""+contact.getContactNumber());

        contactValue=contact.getContactNumber().replace(" ","");
        callcheckUserApi(contact.getContactNumber().replace(" ",""));

    }

    public void initViews()
    {
        address_not_layout=findViewById(R.id.address_not_layout);
        address_send_layout=findViewById(R.id.address_send_layout);
        contact_address=findViewById(R.id.contact_address);
        next_view=findViewById(R.id.next_view);
        back_iv=findViewById(R.id.back_iv);
        scanQr_iv=findViewById(R.id.scanQr_iv);
        headerText_tv=findViewById(R.id.headerText_tv);
        cancel_iv=findViewById(R.id.cancel_iv);
        email_ed=findViewById(R.id.email_ed);
        contact_hint=findViewById(R.id.contact_hint);
        contact_iv=findViewById(R.id.contact_iv);
        sendBtn=findViewById(R.id.sendBtn);
    }

    private void getAllContacts() {
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
                }
            }

            setContact_Recycler(contactVOList);
        }
    }

    public void setContact_Recycler(List<Contact> contactList)
    {

        contactVOList=contactList;
        contactAdapter = new AllContactsAdapter(contactList, getApplicationContext(), this);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(SendBitcoin_1.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.setAdapter(contactAdapter);
        contactAdapter.notifyDataSetChanged();
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


    public void callcheckUserApi(String contact)
    {

        if(ConnectivityReceiver.isConnected())
        {
            showCustomProgrssDialog(SendBitcoin_1.this);
            ConnectivityReceiver.isConnected();
            mapobject = signatureobject(contact);
            mapheader=headerObject();

            new PostRequest(SendBitcoin_1.this,mapobject,mapheader,SendBitcoin_1.this, NetworkUtility.CHECK_USER,"checkUser");
        }
        else {
            showAlertDialog(getResources().getString(R.string.dlg_nointernet));
        }



    }
    private Map<String,String> signatureobject(String contact) {

        mapobject.put("contact",contact);
        //mapobject.put("cryptoId",sessionManager.getValues(UserSessionManager.CRYPTO_ID));
        mapobject.put("cryptoId",cryptoId);

        return mapobject;
    }

    private Map<String,String> headerObject() {

        mapheader.put("token",sessionManager.getValues(UserSessionManager.KEY_TOKEN));

        return mapheader;
    }

    @Override
    public void onSuccess(String result, String type) throws JSONException {
        Log.d("Response>>>",result+"\n==="+contactValue);
        hideCustomProgrssDialog();
        try {
            JSONObject obj=new JSONObject(result);
            String status=obj.optString("status");
            String message=obj.optString("message");
            if(status.equalsIgnoreCase("true"))
            {
                Intent intent=new Intent(getApplicationContext(),SendBitcoin_2.class);
                intent.putExtra("contact",contactValue);
                intent.putExtra("amount",amount);
                intent.putExtra("cryptoid",getIntent().getStringExtra("cryptoid"));
                intent.putExtra("local_currency_code",getIntent().getStringExtra("local_currency_code"));
                intent.putExtra("crypto_code",getIntent().getStringExtra("crypto_code"));
                startActivity(intent);
            }else{
                showAlertDialog(message);
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

    }








    //===========Fetch All contact //02-11-2018

    private class GetAllContactTask extends AsyncTask<String, Void, String> {

        List<Contact> listPhoneBook;

        @Override
        protected void onPreExecute() {

            Toast.makeText(SendBitcoin_1.this,"Fetching...",Toast.LENGTH_LONG).show();
            String value = sessionManager.getValues(UserSessionManager.KEY_CONTACT_CODE);
            if(value == null){
                // check load from local contact

                showCustomProgrssDialog(SendBitcoin_1.this);

            }else {
                // reload contact list
            }

        }

        @Override
        protected String doInBackground(String... params) {

            listPhoneBook = getAllPhoneContacts();
            if(listPhoneBook != null) {
                sessionManager.setContact(UserSessionManager.KEY_CONTACT_DATA, listPhoneBook);
            }

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            Toast.makeText(SendBitcoin_1.this,"Fetching Completed",Toast.LENGTH_LONG).show();
            hideCustomProgrssDialog();
            setContact_Recycler(listPhoneBook);

        }


    }


    public void loadContactsFromLocal() {

        List<Contact> listContact = sessionManager.getContact(UserSessionManager.KEY_CONTACT_DATA);

        setContact_Recycler(listContact);

       // refreshContactList();


    }

    private List<Contact> getAllPhoneContacts() {
        List<Contact> listContact = new ArrayList();
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
                    listContact.add(contactVO);
                }
            }


        }
        return listContact;
    }


    public void refreshContactList(){

        Handler handler = new Handler();
        Runnable  runnable = new Runnable() {
            @Override
            public void run() {
                // do the task
                new GetAllContactTask().execute();
            }
        };

        handler.postDelayed(runnable,500);
    }
}
