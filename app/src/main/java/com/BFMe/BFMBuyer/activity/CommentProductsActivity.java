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
import com.BFMe.BFMBuyer.fragment.commentfragment.CommentFragmentFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品评价
 */
public class CommentProductsActivity extends IBaseActivity {
    private List<String> titleNames = new ArrayList<>();
    private TabLayout tab;
    private ViewPager vpViewpager;
    private String productId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackButtonVisibility(View.VISIBLE);
        Intent intent = getIntent();
        productId = intent.getStringExtra("productId");
        tv_title.setText(getResources().getString(R.string.comment_on_commodity));
        tab = (TabLayout) findViewById(R.id.tab);
        vpViewpager = (ViewPager) findViewById(R.id.vp_viewpager);

        titleNames.add(getResources().getString(R.string.all));
        titleNames.add(getResources().getString(R.string.good_reputation));
        titleNames.add(getResources().getString(R.string.centre_reputation));
        titleNames.add(getResources().getString(R.string.bad_reputation));

        vpViewpager.setAdapter(new CommentFramentAdapter(getSupportFragmentManager()));
        vpViewpager.setCurrentItem(0);
        tab.setupWithViewPager(vpViewpager);
    }
    public String getProductId(){
           return productId;
    }
    @Override
    public int initLayout() {
        return R.layout.activity_comment_products;
    }

    private class CommentFramentAdapter extends FragmentPagerAdapter {
        public CommentFramentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleNames.get(position);
        }

        @Override
        public int getCount() {
            return titleNames.size();
        }


        @Override
        public Fragment getItem(int position) {
            return CommentFragmentFactory.getFragment(position);
        }
    }

}

