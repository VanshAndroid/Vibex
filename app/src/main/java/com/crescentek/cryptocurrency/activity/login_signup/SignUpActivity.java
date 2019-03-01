package com.crescentek.cryptocurrency.activity.login_signup;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.interfaces.ApiRequestListener;
import com.crescentek.cryptocurrency.model.CountryM;
import com.crescentek.cryptocurrency.network.GetRequest;
import com.crescentek.cryptocurrency.network.PostRequest;
import com.crescentek.cryptocurrency.utility.BaseActivity;
import com.crescentek.cryptocurrency.utility.ConnectivityReceiver;
import com.crescentek.cryptocurrency.utility.NetworkUtility;
import com.crescentek.cryptocurrency.utility.UserSessionManager;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.OnCountryPickerListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by R.Android on 15-06-2018.
 */

public class SignUpActivity extends BaseActivity implements ApiRequestListener{

    private TextView email_hint_tv,password_tv,headerText_tv,term_condition_tv,privacy_tv,first_hint_tv,lname_hint_tv1,country_hint_tv,mobile_hint_tv,mobile_code_tv;
    private EditText email_ed,password_ed,tel_ed,name_ed,lname_ed, country_ed;
    private View email_view,password_view,fname_view,lname_view,mobile_view,mobile_view1,country_view,re_password_view;
    private ImageView back_iv;
    private LinearLayout header_layout;
    private Button signup_btn;
    UserSessionManager userSessionManager;

    private TextView re_password_tv;
    private EditText re_password_ed;
    private ImageView re_password_visible;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Map<String, String> mapobject;

    private List<CountryM> areaList,areaList_copy;
    ArrayList<String> country_code_name;
    ArrayList<String> countryList;
    String country_id="";
    CountryAdapter mAdapter;
    ListView country_list;
    EditText search_ed;
    String searchData="";
    AlertDialog dialog;
    ImageView password_visible;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(SignUpActivity.this);
        setContentView(R.layout.signup_activity);

        userSessionManager=new UserSessionManager(SignUpActivity.this);
        mapobject = new HashMap<String, String>();
        areaList = new ArrayList<CountryM>();
        areaList_copy = new ArrayList<CountryM>();
        country_code_name = new ArrayList<String>();
        countryList = new ArrayList<String>();


        initViews();

        password_visible.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    password_ed.setInputType(InputType.TYPE_CLASS_TEXT);
                    password_visible.setImageResource(R.drawable.ic_blue_eye_24dp);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    password_ed.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password_visible.setImageResource(R.drawable.ic_gray_eye_24dp);
                }

                return true;
            }
        });

        re_password_visible.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    re_password_ed.setInputType(InputType.TYPE_CLASS_TEXT);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    re_password_ed.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }

                return true;
            }
        });

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        name_ed.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        lname_ed.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

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
                    first_hint_tv.setTextColor(getResources().getColor(R.color.dark_gray));
                    fname_view.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                }
                else {
                    first_hint_tv.setTextColor(getResources().getColor(R.color.sky));
                    fname_view.setBackgroundColor(getResources().getColor(R.color.sky));
                }

            }
        });

        lname_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(lname_ed.getText().toString().length()<=1)
                {
                    lname_hint_tv1.setTextColor(getResources().getColor(R.color.dark_gray));
                    lname_view.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                }
                else {
                    lname_hint_tv1.setTextColor(getResources().getColor(R.color.sky));
                    lname_view.setBackgroundColor(getResources().getColor(R.color.sky));
                }

            }
        });

        tel_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(tel_ed.getText().toString().length()<=1)
                {
                    mobile_hint_tv.setTextColor(getResources().getColor(R.color.dark_gray));
                    mobile_view.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                }
                else {
                    mobile_hint_tv.setTextColor(getResources().getColor(R.color.sky));
                    mobile_view.setBackgroundColor(getResources().getColor(R.color.sky));
                }

            }
        });

        email_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(email_ed.getText().toString().length()<=1)
                {
                    email_hint_tv.setTextColor(getResources().getColor(R.color.dark_gray));
                    email_ed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_email_dark_gray_24dp, 0);
                    email_view.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                }
                else {
                    email_hint_tv.setTextColor(getResources().getColor(R.color.sky));
                    email_ed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_email_sky_blue_24dp, 0);
                    email_view.setBackgroundColor(getResources().getColor(R.color.sky));
                }

            }
        });

        password_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(password_ed.getText().toString().length()<=1)
                {
                    password_tv.setTextColor(getResources().getColor(R.color.dark_gray));
                    //password_ed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_password_dark_gray_24dp, 0);
                    password_view.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                }
                else {
                    password_tv.setTextColor(getResources().getColor(R.color.sky));
                    //password_ed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_password_blue_24dp, 0);
                    password_view.setBackgroundColor(getResources().getColor(R.color.sky));

                }

            }

        });

        re_password_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(re_password_ed.getText().toString().length()<=1)
                {
                    re_password_tv.setTextColor(getResources().getColor(R.color.dark_gray));
                    //password_ed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_password_dark_gray_24dp, 0);
                    re_password_view.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                }
                else {
                    re_password_tv.setTextColor(getResources().getColor(R.color.sky));
                    //password_ed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_password_blue_24dp, 0);
                    re_password_view.setBackgroundColor(getResources().getColor(R.color.sky));

                }

            }

        });

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String password=password_ed.getText().toString().trim();
                String password_second=re_password_ed.getText().toString().trim();

                if(name_ed.getText().toString().trim().length()<1)
                {
                    name_ed.setError("First name can't be blank");
                }
                else if(lname_ed.getText().toString().trim().length()<1) {
                    lname_ed.setError("Last name can't be blank");
                }
                else if(country_ed.getText().toString().trim().length()<1) {
                    country_ed.setError("Select Country");
                }
                else if(tel_ed.getText().toString().trim().length()<1) {
                    tel_ed.setError("Mobile number can't be blank");
                }
                else if (!validEmail(email_ed.getText().toString().trim())) {
                    email_ed.setError("A valid email id needed");
                }
                else if(password_ed.getText().toString().trim().length()<1) {
                    password_ed.setError("Password Can't be blank");
                }
                else if(re_password_ed.getText().toString().trim().length()<1) {
                    re_password_ed.setError("Password Can't be blank");
                }
                else if (!password.equals(password_second)) {
                    showAlertDialog("Password Not Match !!! ");
                }

                else {

                    if(ConnectivityReceiver.isConnected())
                    {
                        //ConnectivityReceiver.isConnected();
                        showCustomProgrssDialog(SignUpActivity.this);
                        mapobject = signatureobject("", "");
                        Log.d("MapObj>>>",mapobject.toString());
                        new PostRequest(SignUpActivity.this,mapobject,SignUpActivity.this,NetworkUtility.REGISTRAION,"Registration");
                    }
                    else {
                        showAlertDialog(getResources().getString(R.string.dlg_nointernet));
                    }


                }

            }
        });


        country_ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                generateCountryDialog();

            }
        });


        term_condition_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(),TermsAndCondition.class);
                startActivity(intent);
            }
        });

        privacy_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),PrivacyPolicy.class));
            }
        });

        callCountryApi();

    }

    public void getCountryId()
    {
        String countrySelected= country_ed.getText().toString().trim();
        for (int i=0;i<areaList.size();i++) {
            if (countrySelected.equalsIgnoreCase(areaList.get(i).getCountry()))
            {
                String countryId=areaList.get(i).getPhonecode();
                country_id=areaList.get(i).getCountry_id();
                mobile_code_tv.setText("+"+countryId);

                Log.d("country_id",""+country_id);
            }
        }
    }

    public void callCountryApi()
    {

        if(ConnectivityReceiver.isConnected())
        {
            showCustomProgrssDialog(SignUpActivity.this);
            new GetRequest(SignUpActivity.this,SignUpActivity.this,"country", NetworkUtility.GET_COUNTRY);

        }
        else {
            showAlertDialog(getResources().getString(R.string.dlg_nointernet));
        }



    }

    public void initViews(){

        term_condition_tv=findViewById(R.id.term_condition_tv);
        privacy_tv=findViewById(R.id.privacy_tv);
        email_hint_tv=findViewById(R.id.email_hint_tv);
        password_tv=findViewById(R.id.password_tv);
        signup_btn=findViewById(R.id.signup_btn);

        email_ed=findViewById(R.id.email_ed);
        password_ed=findViewById(R.id.password_ed);

        email_view=findViewById(R.id.email_view);
        password_view=findViewById(R.id.password_view);

        back_iv=findViewById(R.id.back_iv);
        headerText_tv=findViewById(R.id.headerText_tv);
        header_layout = findViewById(R.id.header_layout);

        /*name_ed,lname_hint_tv1,lname_ed,country_hint_tv,country_ed,mobile_hint_tv*/
        first_hint_tv=findViewById(R.id.first_hint_tv);
        name_ed=findViewById(R.id.name_ed);
        lname_hint_tv1=findViewById(R.id.lname_hint_tv1);
        lname_ed=findViewById(R.id.lname_ed);
        country_hint_tv=findViewById(R.id.country_hint_tv);
        country_ed =findViewById(R.id.county_tv);
        mobile_hint_tv=findViewById(R.id.mobile_hint_tv);
        tel_ed=findViewById(R.id.tel_ed);
        fname_view=findViewById(R.id.fname_view);
        lname_view=findViewById(R.id.lname_view);
        mobile_view=findViewById(R.id.mobile_view);
        mobile_code_tv=findViewById(R.id.mobile_code_tv);
        mobile_view1=findViewById(R.id.mobile_view1);
        country_view=findViewById(R.id.county_view);


        password_visible=findViewById(R.id.password_visible);
        re_password_tv=findViewById(R.id.re_password_tv);
        re_password_ed=findViewById(R.id.re_password_ed);
        re_password_visible=findViewById(R.id.re_password_visible);
        re_password_view=findViewById(R.id.re_password_view);


        back_iv.setVisibility(View.GONE);
        headerText_tv.setText(R.string.sign_up_header_text);

    }

    private boolean validEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private Map<String,String> signatureobject(String s, String s1) {

        mapobject.put("firstname",name_ed.getText().toString().trim());
        mapobject.put("lastname",lname_ed.getText().toString().trim());
        mapobject.put("countryId",country_id);
        mapobject.put("phone",tel_ed.getText().toString().trim());
        mapobject.put("email",""+ email_ed.getText().toString());
        mapobject.put("password",""+password_ed.getText().toString());

        return mapobject;
    }

    @Override
    public void onSuccess(String result, String type) throws JSONException {

        Log.d("Reg_response>>>",result);
        hideCustomProgrssDialog();
        
        
        switch (type)
        {
            
            case "Registration":
                {

                    try {
                        JSONObject jObj=new JSONObject(result);
                        String status=jObj.optString("status");
                        String message=jObj.optString("message");

                        if(status.equalsIgnoreCase("true"))
                        {
                            JSONObject jObjData=jObj.optJSONObject("data");
                            String token=jObjData.optString("token");
                            String users_id=jObjData.optString("users_id");
                            userSessionManager.setValues(UserSessionManager.KEY_USER_ID,users_id);
                            userSessionManager.setValues(UserSessionManager.KEY_TOKEN,token);
                            userSessionManager.setValues(UserSessionManager.KEY_EMAIL,email_ed.getText().toString());
                            Intent intent=new Intent(getApplicationContext(),SignInConfirmation.class);
                            startActivity(intent);
                        }
                        else
                        {
                            showAlertDialog(message);
                        }

                    }
                    catch (Exception e) {
                        Log.d("Exception",""+e.getMessage());
                    }
                    
                }
                break;
            case "country":
                {
                    Log.d("CountryM>>",result);
                    JSONObject jObject=new JSONObject(result);
                    String status=jObject.optString("status");
                    if(status.equalsIgnoreCase("success"))
                    {
                        JSONArray jsonArray=jObject.optJSONArray("data");
                        if(jsonArray.length() >0)
                        {
                            for (int i=0;i<jsonArray.length();i++)
                            {
                                CountryM country=new CountryM();
                                JSONObject jobj = jsonArray.getJSONObject(i);
                                country.setCountry_id(jobj.optString("country_id"));
                                country.setCountry(jobj.optString("country"));
                                country.setPhonecode(jobj.optString("phonecode"));
                                country.setCurrency(jobj.optString("currency"));
                                country.setCurrency_code(jobj.optString("currency_code"));
                                country.setCurrency_symbol(jobj.optString("currency_symbol"));
                                country_code_name.add(jobj.optString("phonecode")+" "+jobj.optString("country"));
                                countryList.add("+"+jobj.optString("phonecode"));
                                areaList.add(country);
                                areaList_copy.add(country);
                            }
    
                        }
                    }
                }
                break;
            
        }

    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {
        Log.d("Error_response>>>",responseMessage);
        hideCustomProgrssDialog();

    }

    public void generateCountryDialog(){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SignUpActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.country_dialog, null);
        dialogBuilder.setView(dialogView);
        dialog = dialogBuilder.create();

        country_list=(ListView) dialogView.findViewById(R.id.country_list);
        search_ed=(EditText) dialogView.findViewById(R.id.search_ed);


        search_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (search_ed.getText().toString().length() == 0) {
                    areaList.clear();
                    areaList_copy.clear();
                    callCountryApi();
                    mAdapter.clear();
                } else {
                    searchData = search_ed.getText().toString().toLowerCase().trim();
                    Log.d("SearchData>>>>>", search_ed.getText().toString());
                    mAdapter.getFilter().filter(searchData.toString());
                    mAdapter.notifyDataSetChanged();

                }

            }
        });



        Log.d("stateList>>>",""+areaList.size());
        mAdapter=new CountryAdapter(getApplicationContext(),areaList);
        country_list.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        dialog.show();

    }



    class CountryAdapter extends ArrayAdapter<String> implements Filterable {

        Context myContext;
        LayoutInflater inflater;
        List<CountryM> mCountryList;
        List<CountryM> mCountryListSearch;


        public CountryAdapter(Context activity, List<CountryM> cityListAdapter) {
            super(activity, R.layout.list_item_country, R.id.country_name);

            myContext = activity;
            inflater = LayoutInflater.from(activity);
            this.mCountryList = cityListAdapter;
            mCountryListSearch=cityListAdapter;

        }

        @Override
        public int getCount() {
            return mCountryList.size();
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
                view = inflater.inflate(R.layout.list_item_country, null);
                holder.country_name = (TextView) view.findViewById(R.id.country_name);
                holder.country_code = (TextView) view.findViewById(R.id.country_code);
                holder.customLayout=(LinearLayout) view.findViewById(R.id.customLayout);

                view.setTag(holder);
            } else {

                holder = (ViewHolder) view.getTag();
            }

            holder.country_name.setText("" + mCountryList.get(position).getCountry());
            holder.country_code.setText("+" + mCountryList.get(position).getPhonecode());

            holder.customLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d("CountryAdapter>>>",mCountryListSearch.get(position).getPhonecode()+" "+mCountryListSearch.get(position).getCountry());

                    country_id=mCountryListSearch.get(position).getCountry_id();
                    country_ed.setText(mCountryListSearch.get(position).getCountry());
                    mobile_code_tv.setText("+"+mCountryListSearch.get(position).getPhonecode());
                    dialog.dismiss();

                }
            });


            return view;
        }

        @NonNull
        @Override
        public Filter getFilter() {

            Filter filter = new Filter() {
                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    mCountryListSearch = (ArrayList<CountryM>) results.values; // has the filtered values
                    mAdapter = new CountryAdapter(SignUpActivity.this, mCountryListSearch);
                    country_list.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    notifyDataSetChanged();
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();
                    ArrayList<CountryM> FilteredArrList = new ArrayList<>();

                    if (mCountryList == null) {
                        mCountryList = new ArrayList<CountryM>(mCountryListSearch);
                    }


                    if (constraint == null || constraint.length() == 0) {

                        // set the Original result to return
                        results.count = mCountryList.size();
                        results.values = mCountryList;
                    } else {
                        constraint = constraint.toString().toLowerCase();
                        mCountryList = areaList_copy;
                        for (int i = 0; i < mCountryList.size(); i++) {
                            String country = mCountryList.get(i).country;
                            String phonecode = mCountryList.get(i).phonecode;


                            if (country.toLowerCase().contains(constraint.toString()) || phonecode.toLowerCase().contains(constraint.toString()) ) {

                                FilteredArrList.add(new CountryM(mCountryList.get(i).country_id, mCountryList.get(i).country, mCountryList.get(i).phonecode, mCountryList.get(i).currency, mCountryList.get(i).currency_code,mCountryList.get(i).currency_symbol));
                            }
                        }
                        results.count = FilteredArrList.size();
                        results.values = FilteredArrList;
                        Log.d("Filter>>>", "" + FilteredArrList.size());
                    }
                    return results;
                }
            };

            return filter;
        }


        class ViewHolder {
            public TextView country_name,country_code;
            public LinearLayout customLayout;

        }
    }


}
