package com.crescentek.cryptocurrency.fragment;

/**
 * Created by R.Android on 29/07/15.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.adapter.NavigationDrawerAdapter;
import com.crescentek.cryptocurrency.fragment.home.HomeCardFragment_1;
import com.crescentek.cryptocurrency.interfaces.ApiRequestListener;
import com.crescentek.cryptocurrency.model.NavDrawerItem;
import com.crescentek.cryptocurrency.network.GetRequest;
import com.crescentek.cryptocurrency.utility.ConnectivityReceiver;
import com.crescentek.cryptocurrency.utility.NetworkUtility;
import com.crescentek.cryptocurrency.utility.UserSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentDrawer extends Fragment implements ApiRequestListener {

    private static String TAG = FragmentDrawer.class.getSimpleName();

    private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationDrawerAdapter adapter;
    private View containerView;
    private static String[] titles = null;
    private FragmentDrawerListener drawerListener;
    ImageView ic_close;

    private TextView name_tv,email_tv;

    private TextView currency_val_tv;

    UserSessionManager sessionManager;

    public FragmentDrawer() {

    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    public static List<NavDrawerItem> getData() {
        List<NavDrawerItem> data = new ArrayList<>();

        for (int i = 0; i < titles.length; i++) {
            NavDrawerItem navItem = new NavDrawerItem();
            navItem.setTitle(titles[i]);
            data.add(navItem);
        }
        return data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // drawer labels
        titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        sessionManager=new UserSessionManager(getActivity());

        name_tv=layout.findViewById(R.id.name_tv);

        email_tv=layout.findViewById(R.id.email_tv);

        currency_val_tv=layout.findViewById(R.id.currency_val_tv);

        email_tv.setText(sessionManager.getValues(UserSessionManager.KEY_EMAIL));

        //Log.d("FirstName>>>",sessionManager.getValues(UserSessionManager.KEY_FIRST_NAME));

        if(sessionManager.getValues(UserSessionManager.KEY_FIRST_NAME)!=null)
        {
            name_tv.setText(sessionManager.getValues(UserSessionManager.KEY_FIRST_NAME));
        }

        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);

        adapter = new NavigationDrawerAdapter(getActivity(), getData());

        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {

            @Override
            public void onClick(View view, int position) {
                drawerListener.onDrawerItemSelected(view, position);
                mDrawerLayout.closeDrawer(containerView);
            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        callRateApi();

        return layout;
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }


    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }

    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }


    public void callRateApi()
    {
        if(ConnectivityReceiver.isConnected())
        {
            if(sessionManager.getValues(UserSessionManager.CRYPTO_ID)!=null)
            {
                new GetRequest(getActivity(),FragmentDrawer.this,"Rate", NetworkUtility.GET_RATE+"="+sessionManager.getValues(UserSessionManager.CRYPTO_ID));
            }else {
                new GetRequest(getActivity(),FragmentDrawer.this,"Rate", NetworkUtility.GET_RATE+"="+"1");
            }

        }

        else{
            Toast.makeText(getActivity(),getResources().getString(R.string.dlg_nointernet),Toast.LENGTH_LONG).show();

        }

    }


    @Override
    public void onSuccess(String result, String type) throws JSONException {

        Log.d("GetRateResponse>>>>",result);
        JSONObject jObj=new JSONObject(result);
        String status=jObj.optString("status");
        if(status.equalsIgnoreCase("true"))
        {
            JSONObject jOb=jObj.optJSONObject("data");
            String crypto_rate=jOb.optString("crypto_rate");
            String currency_code=jOb.optString("currency_code");
            currency_val_tv.setText("="+currency_code+" "+crypto_rate);
        }
    }

    @Override
    public void onFailure(int responseCode, String responseMessage) {

        Log.d("ErrorResponse>>>>",responseMessage);
    }


}
