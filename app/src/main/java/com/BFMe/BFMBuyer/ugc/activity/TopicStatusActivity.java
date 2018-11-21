package com.BFMe.BFMBuyer.ugc.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.ugc.fragment.TopicFragmentFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.BFMe.BFMBuyer.R.id.tl_my_collect;
import static com.BFMe.BFMBuyer.R.id.vp_my_collect;

/**
 * 用户个人主页话题审核状态页面
 */
public class TopicStatusActivity extends IBaseActivity {
    private List<String> titleNames;

    @BindView(tl_my_collect)
    TabLayout TLTopicStatus;
    @BindView(vp_my_collect)
    ViewPager VPTopicStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        titleNames = new ArrayList<>();
        titleNames.add(getString(R.string.all));
        titleNames.add(getString(R.string.already_passed));
        titleNames.add(getString(R.string.under_review));
        titleNames.add(getString(R.string.not_pass));

        VPTopicStatus.setAdapter(new MyAdapter(getSupportFragmentManager()));
        TLTopicStatus.setupWithViewPager(VPTopicStatus);
    }

    private class MyAdapter extends FragmentPagerAdapter {

        private MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleNames.get(position);
        }
        @Override
        public Fragment getItem(int position) {
            return TopicFragmentFactory.getFragment(position);
        }

        @Override
        public int getCount() {
            return titleNames.size();
        }
    }

    private void initView() {
        setBackButtonVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.my_topic));
    }

    @Override
    public int initLayout() {
        return R.layout.activity_my_collect;
    }
}
