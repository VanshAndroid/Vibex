package com.crescentek.cryptocurrency.fragment.navigation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crescentek.cryptocurrency.R;
import com.crescentek.cryptocurrency.fragment.pricealert.BitcoinPriceFragment;
import com.crescentek.cryptocurrency.fragment.pricealert.EthereumFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by R.Android on 13-07-2018.
 */

public class PriceAlertsFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.price_alert,container,false);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        adapter.addFragment(new BitcoinPriceFragment(), "Bitcoin");
        //adapter.addFragment(new EthereumFragment(), "Ethereum");
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);


        return view;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position)
            {
                case 0:
                    return mFragmentList.get(0);
                case 1:
                    return mFragmentList.get(1);
            }

            return null;
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
