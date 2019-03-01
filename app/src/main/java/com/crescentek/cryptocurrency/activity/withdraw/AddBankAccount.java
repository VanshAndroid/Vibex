package com.crescentek.cryptocurrency.activity.withdraw;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.activity.login_signup.HomeActivity;
import com.crescentek.cryptocurrency.interfaces.ApiRequestListener;
import com.crescentek.cryptocurrency.model.Bank;
import com.crescentek.cryptocurrency.network.GetRequest;
import com.crescentek.cryptocurrency.network.PostRequest;
import com.crescentek.cryptocurrency.utility.BaseActivity;
import com.crescentek.cryptocurrency.utility.ConnectivityReceiver;
import com.crescentek.cryptocurrency.utility.NetworkUtility;
import com.crescentek.cryptocurrency.utility.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by R.Android on 11-07-2018.
 */

public class AddBankAccount extends BaseActivity implements ApiRequestListener{


    //Spinner spinner;
    private TextView name_tv,account_tv,next_tv;
    private EditText name_ed,account_ed;
    View name_view,account_view;
    private ImageView back_iv;
    private TextView headerText_tv;
    private TextView bank_spinner;

    List<Bank> bankList;
    ArrayList bankNameList;
    BankAdapter myAdapter;

    Map<String,String> mapPost;
    Map<String,String> mapHeader;

    UserSessionManager sessionManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(AddBankAccount.this);
        setContentView(R.layout.add_bank_account);

        mapPost=new HashMap<String, String>();
        mapHeader=new HashMap<String, String>();
        sessionManager=new UserSessionManager(getApplicationContext());

        bankList=new ArrayList<Bank>();
        bankNameList=new ArrayList();

        bank_spinner = findViewById(R.id.bank_spinner);
        name_tv=findViewById(R.id.name_tv);
        account_tv=findViewById(R.id.account_tv);
        name_ed=findViewById(R.id.name_ed);
        account_ed=findViewById(R.id.account_ed);
        name_view=findViewById(R.id.name_view);
        account_view=findViewById(R.id.account_view);
        back_iv=findViewById(R.id.back_iv);
        headerText_tv=findViewById(R.id.headerText_tv);
        next_tv=findViewById(R.id.next_tv);

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        headerText_tv.setText("Add bank beneficiary");

        bank_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(bankList.size()>0)
                {
                    bankListDialog(bankList);
                }
                else {
                    Toast.makeText(getApplicationContext(),"No Bank Found",Toast.LENGTH_LONG).show();
                }


            }
        });

        name_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(name_ed.getText().toString().length()<=1)
                {
                    name_tv.setTextColor(getResources().getColor(R.color.dark_gray));
                    name_view.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                }
                else {
                    name_tv.setTextColor(getResources().getColor(R.color.sky));
                    name_view.setBackgroundColor(getResources().getColor(R.color.sky));
                }

            }
        });

        account_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(account_ed.getText().toString().length()<=1)
                {
                    account_tv.setTextColor(getResources().getColor(R.color.dark_gray));
                    //name_ed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_email_dark_gray_24dp, 0);
                    account_view.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                }
                else {
                    account_tv.setTextColor(getResources().getColor(R.color.sky));
                    //name_ed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_email_sky_blue_24dp, 0);
                    account_view.setBackgroundColor(getResources().getColor(R.color.sky));
                }

            }
        });


       next_tv.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(bank_spinner.getText().toString().trim().length()<1)
               {
                   showAlertDialog("Please Select Bank");
               }
               else if(name_ed.getText().toString().trim().length()<1)
               {
                   name_ed.setError("Name Can't be blank");
               }
               else if(account_ed.getText().toString().trim().length()<1)
               {
                   account_ed.setError("Bank Account can't be blank");
               }
               else {
                   generateAlert();
               }
           }
       });


        getBankList();

    }

    public void getBankList()
    {

        if(ConnectivityReceiver.isConnected())
        {
            ConnectivityReceiver.isConnected();
            showCustomProgrssDialog(AddBankAccount.this);
            new GetRequest(AddBankAccount.this,AddBankAccount.this,"BANK_LIST", NetworkUtility.BANK_LIST);
        }
        else {
            showAlertDialog(getResources().getString(R.string.dlg_nointernet));
        }


    }

    public void generateAlert()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddBankAccount.this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog dialog = dialogBuilder.create();

        final TextView alert_title=(TextView)dialogView.findViewById(R.id.alert_title);
        final TextView alert_text=(TextView)dialogView.findViewById(R.id.alert_text);
        final TextView negative_txt=(TextView)dialogView.findViewById(R.id.negative_txt);
        final TextView positive_txt=(TextView)dialogView.findViewById(R.id.positive_txt);

        negative_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        positive_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

         if(ConnectivityReceiver.isConnected())
          {
              showCustomProgrssDialog(AddBankAccount.this);
              mapPost=postData();
              mapHeader=postHeader();
              Log.d("mapPost>>>",mapPost.toString());
              new PostRequest(AddBankAccount.this,mapPost,mapHeader,AddBankAccount.this,NetworkUtility.ADD_BANK_ACCOUNT,"ADD_BANK_ACCOUNT");
          }
          else {
              showAlertDialog(getResources().getString(R.string.dlg_nointernet));
          }




            }
        });

        dialog.show();

    }

    public Map<String,String> postData()
    {
        mapPost.put("bank_name",bank_spinner.getText().toString().trim());
        mapPost.put("bank_account",account_ed.getText().toString().trim());
        mapPost.put("bank_account_name",name_ed.getText().toString().trim());

        return mapPost;
    }

    public Map<String,String> postHeader()
    {
        mapHeader.put("token",sessionManager.getValues(UserSessionManager.KEY_TOKEN));

        return mapHeader;
    }

    @Override
    public void onSuccess(String result, String type) throws JSONException {

        hideCustomProgrssDialog();
        Log.d("WalletResponse>>>",result);

        switch (type)
        {
            case "BANK_LIST":
                {
                    JSONObject jObj=new JSONObject(result);
                    String status=jObj.optString("status");
                    if(status.equalsIgnoreCase("true"))
                    {
                        JSONArray jsonArray=jObj.optJSONArray("data");
                        if(jsonArray!=null)
                        {
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                Bank bank=new Bank();
                                JSONObject jOb=jsonArray.optJSONObject(i);
                                bank.setId(jOb.optString("id"));
                                bank.setBankcode(jOb.optString("bankcode"));
                                bank.setBank(jOb.optString("bank"));
                                bank.setCurrency_id(jOb.optString("currency_id"));

                                bankList.add(bank);
                            }

                        }
                    }

                }
                break;
            case "ADD_BANK_ACCOUNT":
                {
                    try {

                        JSONObject jObj=new JSONObject(result);
                        String status=jObj.optString("status");
                        String message=jObj.optString("message");
                        if (status.equalsIgnoreCase("true"))
                        {
                            //showAlertDialog(message);
                            boolean alertStaus=showAlertDialogWithType(message);
                            if(alertStaus)
                            {
                                Intent intent=new Intent(getApplicationContext(), HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                            }
                        }else {
                            showAlertDialog(message);
                        }

                    }
                    catch (Exception e){

                    }

                }
                break;

        }

    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

    }




    public void bankListDialog(final List<Bank> bankListData)
    {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddBankAccount.this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_banks, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog dialog = dialogBuilder.create();

        final ListView bank_list=(ListView) dialogView.findViewById(R.id.bank_list);

        bank_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                bank_spinner.setText(bankListData.get(position).getBank());

                dialog.dismiss();

            }
        });

        myAdapter=new BankAdapter(getApplicationContext(),bankListData);
        bank_list.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();

        dialog.show();

    }


    class BankAdapter extends ArrayAdapter<String> {

        Context myContext;
        LayoutInflater inflater;
        List<Bank> bankListA;

        public BankAdapter(Context activity, List<Bank> bankListA) {
            super(activity, R.layout.single_list_item_bank, R.id.bank_tv);

            myContext = activity;
            inflater = LayoutInflater.from(activity);
            this.bankListA = bankListA;

        }

        @Override
        public int getCount() {
            return bankListA.size();
        }


        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            final ViewHolder holder;
            if (view == null) {

                holder = new ViewHolder();
                view = inflater.inflate(R.layout.single_list_item_bank, null);
                holder.bank_tv = (TextView) view.findViewById(R.id.bank_tv);
                holder.bank_code_tv = (TextView) view.findViewById(R.id.bank_code_tv);

                view.setTag(holder);
            } else {

                holder = (ViewHolder) view.getTag();
            }

            holder.bank_tv.setText("" + bankListA.get(position).getBank());
            holder.bank_code_tv.setText("" + bankListA.get(position).getBankcode());


            return view;
        }


        class ViewHolder {
            public TextView bank_tv, bank_code_tv;

        }
    }

}
