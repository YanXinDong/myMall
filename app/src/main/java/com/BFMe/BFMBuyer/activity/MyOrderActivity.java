package com.BFMe.BFMBuyer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.fragment.orderfragment.OrderFragmentFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的订单Activity
 */
public class MyOrderActivity extends IBaseActivity  {
    private List<String> titleNames = new ArrayList<>();
    private TabLayout indicator;
    private ViewPager viewPager;
    private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_my_order;
    }

    private void initData() {
        Intent intent = getIntent();
        flag = intent.getIntExtra("flag", 0);

        titleNames.add(getResources().getString(R.string.all));
        titleNames.add(getResources().getString(R.string.wait_pay));
        titleNames.add(getResources().getString(R.string.wait_send_product));
        titleNames.add(getResources().getString(R.string.wait_accept_product));
        titleNames.add(getResources().getString(R.string.wait_evaluate));

        MyOrderAdapter orderAdapter = new MyOrderAdapter(getSupportFragmentManager());
        viewPager.setAdapter(orderAdapter);
        viewPager.setCurrentItem(flag);
        indicator.setVisibility(View.VISIBLE);
        indicator.setupWithViewPager(viewPager);

    }


    /**
     * 初始化布局
     */
    private void initView() {
        setBackButtonVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.my_order));
        indicator = (TabLayout) findViewById(R.id.indicator);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

    }

    class MyOrderAdapter extends FragmentPagerAdapter {

        @Override
        public CharSequence getPageTitle(int position) {
            return titleNames.get(position);
        }

        public MyOrderAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return OrderFragmentFactory.getFragment(position);
        }

        @Override
        public int getCount() {
            return titleNames.size();
        }
    }

}
