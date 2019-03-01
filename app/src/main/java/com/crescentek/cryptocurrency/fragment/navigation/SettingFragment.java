package com.crescentek.cryptocurrency.fragment.navigation;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.crescentek.cryptocurrency.activity.login_signup.SignUpActivity;
import com.crescentek.cryptocurrency.activity.settings.MobileNumberVerificationActivity;
import com.crescentek.cryptocurrency.activity.settings.twofactorauth.TwoFactorAuthentication;
import com.crescentek.cryptocurrency.activity.verification_level_2.Verification_2_1;
import com.crescentek.cryptocurrency.interfaces.ApiRequestListener;
import com.crescentek.cryptocurrency.model.CountryM;
import com.crescentek.cryptocurrency.network.GetRequest;
import com.crescentek.cryptocurrency.network.PostRequest;
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
 * Created by R.Android on 21-06-2018.
 */

public class SettingFragment extends Fragment implements ApiRequestListener {

    private Button save_btn;
    private LinearLayout telephone, email_layout, country_layout, notification, verification_level, two_factor_auth;
    private EditText name_ed, lname_ed, email_ed, notification_ed;
    private TextView signout_tv, verification_tv, tel_tv, two_factor_auth_tv, county_tv;
    private ImageView edit_iv;
    private View view;
    UserSessionManager sessionManager;
    Context context;

    private List<CountryM> areaList,area_List,areaList_copy;
    private Map<String, String> mapobject, mapheader;

    ProgressDialog progressDialog;

    android.app.AlertDialog dialog;
    ListView country_list;
    EditText search_ed;

    String country_id="";

    String verify_phone = "";

    String phone = "";

    String phonecode = "";

    String searchData="";

    Animation animZoomOut;

    LinearLayout linear_main;

    CountryAdapter mAdapter;
    private TextView fname_hint_tv, lname_hint_tv1, mobile_hint_tv, email_hint_tv, country_hint_tv, language_hint_tv, verification_hint_tv, two_factor_auth_hint_tv;;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_settings, container, false);

        initView();

        callCountryApi();

        callProfileAPi();

        area_List = new ArrayList<CountryM>();
        areaList_copy = new ArrayList<CountryM>();

        county_tv.setEnabled(false);

        mapobject = new HashMap<String, String>();
        mapheader = new HashMap<String, String>();
        sessionManager = new UserSessionManager(getActivity());


        signout_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to signout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                sessionManager.logoutUser();

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
        });

        animZoomOut = AnimationUtils.loadAnimation(getActivity(),
                R.anim.fade_in);

        edit_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                linear_main.startAnimation(animZoomOut);

                Toast.makeText(getActivity(), "Edit Your Profile", Toast.LENGTH_LONG).show();
                save_btn.setVisibility(View.VISIBLE);
                name_ed.setEnabled(true);
                lname_ed.setEnabled(true);
                county_tv.setClickable(true);
                county_tv.setEnabled(true);
                signout_tv.setVisibility(View.GONE);
                tel_tv.setEnabled(true);
                edit_iv.setVisibility(View.INVISIBLE);

                /*fname_hint_tv,lname_hint_tv1,mobile_hint_tv,email_hint_tv,country_hint_tv,language_hint_tv,verification_hint_tv,two_factor_auth_hint_tv;*/
                fname_hint_tv.setTextColor(Color.parseColor("#2877fd"));
                lname_hint_tv1.setTextColor(Color.parseColor("#2877fd"));
                //mobile_hint_tv.setTextColor(Color.parseColor("#2877fd"));
                country_hint_tv.setTextColor(Color.parseColor("#2877fd"));
                language_hint_tv.setTextColor(Color.parseColor("#2877fd"));
                verification_hint_tv.setTextColor(Color.parseColor("#2877fd"));
                two_factor_auth_hint_tv.setTextColor(Color.parseColor("#2877fd"));


            }
        });

        two_factor_auth_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TwoFactorAuthentication.class));
            }
        });


        //---------------------------------------- Listener ----------------------------------------------

        //county_tv.setFocusable(false);

        county_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                generateCountryDialog();
            }
        });


        tel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (verify_phone.equalsIgnoreCase("No")) {
                    Intent intent = new Intent(getActivity(), MobileNumberVerificationActivity.class);
                    intent.putExtra("mobile", phone);
                    intent.putExtra("phonecode", phonecode);
                    startActivity(intent);
                } else {

                    Intent intent = new Intent(getActivity(), MobileNumberVerificationActivity.class);
                    intent.putExtra("mobile", phone);
                    intent.putExtra("phonecode", phonecode);
                    startActivity(intent);
                }

            }
        });


        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (name_ed.getText().toString().trim().length() < 0) {
                    name_ed.setError("First Name Can't be blank");
                } else if (lname_ed.getText().toString().trim().length() < 0) {
                    lname_ed.setError("Last Name Can't be blank");
                } else if (tel_tv.getText().toString().length() < 1) {
                    tel_tv.setError("Tap To Set Mobile Number ");
                } else {
                    String countrySelected = county_tv.getText().toString().trim();
                    for (int i = 0; i < areaList.size(); i++) {
                        if (countrySelected.equalsIgnoreCase(areaList.get(i).getCountry())) {
                            String countryId = areaList.get(i).getCountry_id();
                            Log.d("CountryNameId>>>", areaList.get(i).getCountry() + " " + areaList.get(i).getCountry_id());
                            ConnectivityReceiver.isConnected();
                            mapobject = signatureobject(countryId);
                            mapheader = headerObject();
                            Log.d("mapObjectSave>>>", mapobject.toString());

                            if(ConnectivityReceiver.isConnected())
                            {
                                progressDialog = new ProgressDialog(getActivity());
                                progressDialog.setMessage("Please Wait...");
                                progressDialog.setCanceledOnTouchOutside(false);
                                progressDialog.show();
                                new PostRequest(getActivity(), mapobject, mapheader, SettingFragment.this, NetworkUtility.PERSONAL_DETAILS, "personalDetails");
                            }
                            else{

                                Toast.makeText(getActivity(),""+getResources().getString(R.string.dlg_nointernet),Toast.LENGTH_LONG).show();

                            }

                        }
                    }
                }
            }
        });


        verification_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (verification_tv.getText().toString().equalsIgnoreCase("Level0")) {

                    if (verify_phone.equalsIgnoreCase("No")) {
                        Intent intent = new Intent(getActivity(), MobileNumberVerificationActivity.class);
                        intent.putExtra("mobile", phone);
                        intent.putExtra("phonecode", phonecode);
                        startActivity(intent);
                    } else {

                        Intent intent = new Intent(getActivity(), MobileNumberVerificationActivity.class);
                        intent.putExtra("mobile", phone);
                        intent.putExtra("phonecode", phonecode);
                        startActivity(intent);
                    }

                } else if (verification_tv.getText().toString().equalsIgnoreCase("Level1")) {

                    startActivity(new Intent(getActivity(), Verification_2_1.class));

                } else {

                }


            }
        });

        return view;
    }

    public void initView() {

        save_btn = view.findViewById(R.id.save_btn);
        telephone = view.findViewById(R.id.telephone);
        email_layout = view.findViewById(R.id.email_layout);
        country_layout = view.findViewById(R.id.country_layout);
        notification = view.findViewById(R.id.notification);
        verification_level = view.findViewById(R.id.verification_level);
        two_factor_auth = view.findViewById(R.id.two_factor_auth);
        signout_tv = view.findViewById(R.id.signout_tv);
        edit_iv = view.findViewById(R.id.edit_iv);
        name_ed = view.findViewById(R.id.name_ed);
        lname_ed = view.findViewById(R.id.lname_ed);
        tel_tv = view.findViewById(R.id.tel_tv);
        email_ed = view.findViewById(R.id.email_ed);
        county_tv = view.findViewById(R.id.county_ed);
        notification_ed = view.findViewById(R.id.notification_ed);
        verification_tv = view.findViewById(R.id.verification_tv);
        two_factor_auth_tv = view.findViewById(R.id.two_factor_auth_tv);

        fname_hint_tv = view.findViewById(R.id.fname_hint_tv);
        lname_hint_tv1 = view.findViewById(R.id.lname_hint_tv1);
        mobile_hint_tv = view.findViewById(R.id.mobile_hint_tv);
        email_hint_tv = view.findViewById(R.id.email_hint_tv);
        country_hint_tv = view.findViewById(R.id.country_hint_tv);
        language_hint_tv = view.findViewById(R.id.language_hint_tv);
        verification_hint_tv = view.findViewById(R.id.verification_hint_tv);
        two_factor_auth_hint_tv = view.findViewById(R.id.two_factor_auth_hint_tv);
        linear_main=view.findViewById(R.id.linear_main);

        save_btn.setVisibility(View.GONE);


    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void callProfileAPi() {


        if(ConnectivityReceiver.isConnected())
        {
            ConnectivityReceiver.isConnected();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            new GetRequest(getActivity(), SettingFragment.this, "PROFILE_DETAILS", NetworkUtility.PROFILE_DETAILS);
        }

        else{

            Toast.makeText(getActivity(),""+getResources().getString(R.string.dlg_nointernet),Toast.LENGTH_LONG).show();
            //showAlertDialog();

        }



    }

    public void callCountryApi() {


        if(ConnectivityReceiver.isConnected())
        {
            ConnectivityReceiver.isConnected();
            areaList = new ArrayList<CountryM>();
            new GetRequest(getActivity(), SettingFragment.this, "country", NetworkUtility.GET_COUNTRY);
        }

        else{

            Toast.makeText(getActivity(),""+getResources().getString(R.string.dlg_nointernet),Toast.LENGTH_LONG).show();
        }



    }


    private Map<String, String> signatureobject(String country_id) {

        mapobject.put("firstname", name_ed.getText().toString());
        mapobject.put("lastname", lname_ed.getText().toString());
        mapobject.put("phone", tel_tv.getText().toString().trim());
        mapobject.put("twoFA", two_factor_auth_tv.getText().toString());
        mapobject.put("country", country_id);

        return mapobject;
    }

    private Map<String, String> headerObject() {

        mapheader.put("token", sessionManager.getValues(UserSessionManager.KEY_TOKEN));

        return mapheader;
    }

    @Override
    public void onSuccess(String result, String type) throws JSONException {

        switch (type) {
            case "country": {
                Log.d("CountryM>>", result);
                JSONObject jObject = new JSONObject(result);
                String status = jObject.optString("status");
                if (status.equalsIgnoreCase("success")) {
                    JSONArray jsonArray = jObject.optJSONArray("data");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            CountryM country = new CountryM();
                            JSONObject jobj = jsonArray.getJSONObject(i);
                            country.setCountry_id(jobj.optString("country_id"));
                            country.setCountry(jobj.optString("country"));
                            country.setPhonecode(jobj.optString("phonecode"));
                            country.setCurrency(jobj.optString("currency"));
                            country.setCurrency_code(jobj.optString("currency_code"));
                            country.setCurrency_symbol(jobj.optString("currency_symbol"));
                            areaList.add(country);
                        }

                    }
                }
            }
            break;
            case "personalDetails": {
                progressDialog.dismiss();
                Log.d("personalDetails>>", result);
                JSONObject jObject = new JSONObject(result);
                String status = jObject.optString("status");
                String message = jObject.optString("message");
                if (status.equalsIgnoreCase("true")) {
                    JSONObject object = jObject.optJSONObject("data");
                    sessionManager.setValues(UserSessionManager.KEY_EMAIL_VERIFY, object.optString("isEmailVery"));
                    sessionManager.setValues(UserSessionManager.KEY_PHONE_VERIFY, object.optString("isPhoneVery"));
                    sessionManager.setValues(UserSessionManager.KEY_BVN_VERIFY, object.optString("isBvnVerified"));
                    sessionManager.setValues(UserSessionManager.KEY_T_PIN_VERIFY, object.optString("isPinSet"));
                    sessionManager.setValues(UserSessionManager.KEY_DOC_UPLOADED, object.optString("isDocUploaded"));
                    sessionManager.setValues(UserSessionManager.KEY_TWO_FACTER, object.optString("isTwoFASet"));
                    sessionManager.setValues(UserSessionManager.KEY_USER_LEVEL, object.optString("user_level"));
                    sessionManager.setValues(UserSessionManager.KEY_USER_LEVEL_ID, object.optString("user_level_id"));

                    sessionManager.setValues(UserSessionManager.KEY_FIRST_NAME, object.optString("firstname"));
                    sessionManager.setValues(UserSessionManager.KEY_LAST_NAME, object.optString("lastname"));

                    name_ed.setText(sessionManager.getValues(UserSessionManager.KEY_FIRST_NAME));
                    lname_ed.setText(sessionManager.getValues(UserSessionManager.KEY_LAST_NAME));
                    save_btn.setVisibility(View.GONE);
                    name_ed.setEnabled(false);
                    //lname_ed.setEnabled(false);
                    county_tv.setClickable(false);
                    county_tv.setEnabled(false);
                    edit_iv.setVisibility(View.VISIBLE);
                    signout_tv.setVisibility(View.VISIBLE);


                    fname_hint_tv.setTextColor(Color.parseColor("#676767"));
                    lname_hint_tv1.setTextColor(Color.parseColor("#676767"));
                    //mobile_hint_tv.setTextColor(Color.parseColor("#676767"));
                    country_hint_tv.setTextColor(Color.parseColor("#676767"));
                    language_hint_tv.setTextColor(Color.parseColor("#676767"));
                    verification_hint_tv.setTextColor(Color.parseColor("#676767"));
                    two_factor_auth_hint_tv.setTextColor(Color.parseColor("#676767"));

                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                }

            }

            break;

            case "PROFILE_DETAILS": {
                Log.d("PROFILE_DETAILS>>>", result);
                progressDialog.dismiss();
                JSONObject jObj = new JSONObject(result);
                String status = jObj.optString("status");
                String message = jObj.optString("message");
                if (status.equalsIgnoreCase("success")) {
                    JSONArray jsonArray = jObj.optJSONArray("data");
                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jOb = jsonArray.optJSONObject(i);
                            String users_id = jOb.optString("users_id");
                            String firstname = jOb.optString("firstname");
                            String lastname = jOb.optString("lastname");

                            String users_level_id = jOb.optString("users_level_id");
                            String email = jOb.optString("email");
                            phone = jOb.optString("phone");
                            phonecode = jOb.optString("phonecode");


                            String currency = jOb.optString("currency");
                            String currency_code = jOb.optString("currency_code");

                            String bvn = jOb.optString("bvn");
                            String country = jOb.optString("country");
                            String verify_status = jOb.optString("verify_status");

                            String photo = jOb.optString("photo");
                            String twoFA = jOb.optString("twoFA");
                            String verify_email = jOb.optString("verify_email");

                            verify_phone = jOb.optString("verify_phone");
                            String transact_pin = jOb.optString("transact_pin");
                            String verify_pin = jOb.optString("verify_pin");
                            String level_name = jOb.optString("level_name");

                            county_tv.setText(country);
                            county_tv.setClickable(false);
                            name_ed.setText(firstname);
                            name_ed.setEnabled(false);
                            lname_ed.setText(lastname);
                            lname_ed.setEnabled(false);
                            if (verify_phone.equalsIgnoreCase("yes")) {
                                tel_tv.setText(phone);
                                tel_tv.setEnabled(false);
                                tel_tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_verify, 0);
                            } else {
                                tel_tv.setText(phone);
                                tel_tv.setEnabled(true);
                                tel_tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_not_verify, 0);
                            }

                            email_ed.setText(email);
                            email_ed.setEnabled(false);
                            for (int j = 0; j < areaList.size(); j++) {
                                if (areaList.get(j).getCountry_id().equalsIgnoreCase(country)) {
                                    county_tv.setText(areaList.get(j).getCountry());
                                    county_tv.setClickable(false);
                                }
                            }

                            verification_tv.setText(level_name);
                            //verification_tv.setEnabled(false);
                            if (twoFA.equalsIgnoreCase("none")) {
                                two_factor_auth_tv.setText("Disable");
                                sessionManager.setValues(UserSessionManager.KEY_TWO_FACTER, "Enable");
                            }
                            if (twoFA.equalsIgnoreCase("Google")) {
                                two_factor_auth_tv.setText("Enable");
                                sessionManager.setValues(UserSessionManager.KEY_TWO_FACTER, "Disable");
                            }
                              /*  else {
                                    two_factor_auth_tv.setText("Enable");
                                    sessionManager.setValues(UserSessionManager.KEY_TWO_FACTER,two_factor_auth_tv.getText().toString());
                                }*/

                        }
                    }
                }

            }
            break;

        }

    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

    }

    public void generateCountryDialog(){

        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(getActivity());
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
        mAdapter=new CountryAdapter(getActivity(),areaList);
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
            final CountryAdapter.ViewHolder holder;
            if (view == null) {

                holder = new CountryAdapter.ViewHolder();
                view = inflater.inflate(R.layout.list_item_country, null);
                holder.country_name = (TextView) view.findViewById(R.id.country_name);
                holder.country_code = (TextView) view.findViewById(R.id.country_code);
                holder.customLayout=(LinearLayout) view.findViewById(R.id.customLayout);

                view.setTag(holder);
            } else {

                holder = (CountryAdapter.ViewHolder) view.getTag();
            }

            holder.country_name.setText("" + mCountryList.get(position).getCountry());
            holder.country_code.setText("" + mCountryList.get(position).getPhonecode());

            holder.customLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d("CountryAdapter>>>",mCountryListSearch.get(position).getPhonecode()+" "+mCountryListSearch.get(position).getCountry());

                    country_id=mCountryListSearch.get(position).getCountry_id();
                    county_tv.setText(mCountryListSearch.get(position).getCountry());
                    country_id=mCountryListSearch.get(position).getPhonecode();
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
                    mAdapter = new CountryAdapter(getActivity(), mCountryListSearch);
                    country_list.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    notifyDataSetChanged();  // notifies the data with new filtered values
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                    ArrayList<CountryM> FilteredArrList = new ArrayList<>();

                    if (mCountryList == null) {
                        mCountryList = new ArrayList<CountryM>(mCountryListSearch); // saves the original data in mOriginalValues
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
