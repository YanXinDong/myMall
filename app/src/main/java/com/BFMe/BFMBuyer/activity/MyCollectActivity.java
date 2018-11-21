package com.BFMe.BFMBuyer.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.fragment.collect.CollectFragmentFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 我的收藏
 */
public class MyCollectActivity extends IBaseActivity {
    private List<String> titleNames = new ArrayList<>();

    @BindView(R.id.tl_my_collect)
    TabLayout tl_my_collect;
    @BindView(R.id.vp_my_collect)
    ViewPager vp_my_collect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        titleNames.add(getResources().getString(R.string.collect_product));
        titleNames.add(getResources().getString(R.string.attention_shop));

        vp_my_collect.setAdapter(new MyAdapter(getSupportFragmentManager()));
        tl_my_collect.setupWithViewPager(vp_my_collect);

    }

    private class MyAdapter extends FragmentPagerAdapter{

        private MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleNames.get(position);
        }
        @Override
        public Fragment getItem(int position) {
            return CollectFragmentFactory.getFragment(position);
        }

        @Override
        public int getCount() {
            return titleNames.size();
        }
    }
    private void initView() {
        tv_title.setText(getString(R.string.my_collect));
        setBackButtonVisibility(View.VISIBLE);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_my_collect;
    }
}
