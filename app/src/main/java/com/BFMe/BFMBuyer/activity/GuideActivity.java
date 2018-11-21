package com.BFMe.BFMBuyer.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.main.MainActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Description:引导页面
 * Copyright  : Copyright (c) 2016
 * Company    : 白富美(北京)网络科技有限公司
 * Author     : 王  可
 * Date       : 2016/7/12 10:00
 */
public class GuideActivity extends Activity {
    private int[] icons = {R.drawable.lead01, R.drawable.lead02, R.drawable.lead03, R.drawable.lead04};
    private ViewPager viewPager;
    private ArrayList<ImageView> mList;
    private TextView btnHome;
    private TextView btnHomeHongKong;
    private TextView btn_tipe;
    private LinearLayout ll_selector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        hideBottomUIMenu();
        viewPager = (ViewPager) this.findViewById(R.id.vp_pager);
        btnHome = (TextView) findViewById(R.id.btn_start_mainland);
        btnHomeHongKong = (TextView) findViewById(R.id.btn_start_hongkong);
        ll_selector = (LinearLayout) findViewById(R.id.ll_selector);
        //初始化viewpager的数据
        initView();
        GuideAdapter adapter = new GuideAdapter();
        viewPager.setAdapter(adapter);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                btnHome.setBackgroundResource(R.drawable.btn_bg_white_frame_blue);
//                btnHomeHongKong.setBackgroundResource(R.drawable.btn_bg_white_frame_gray);
//                CacheUtils.putBoolean(GuideActivity.this,GlobalContent.IS_HONGKONG,false);
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                finish();

            }
        });
        btnHomeHongKong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                btnHome.setBackgroundResource(R.drawable.btn_bg_white_frame_gray);
//                btnHomeHongKong.setBackgroundResource(R.drawable.btn_bg_white_frame_blue);
//                CacheUtils.putBoolean(GuideActivity.this,GlobalContent.IS_HONGKONG,true);
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                finish();
            }
        });
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * 初始化viewpager的数据
     */
    private void initView() {
        ImageView view;
        mList = new ArrayList<>();
        for (int i = 0; i < icons.length; i++) {
            view = new ImageView(this);
//            view.setBackgroundResource(icons[i]); //只有设置了背景才能填充满屏幕
            Glide.with(this).load(icons[i]).skipMemoryCache(true).into(view);
            mList.add(view);
        }
        //对viewpager的滑动事件进行监听
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == icons.length - 1) {
                    btnHome.setVisibility(View.VISIBLE);
//                    btnHomeHongKong.setVisibility(View.VISIBLE);
//                    ll_selector.setVisibility(View.VISIBLE);
                } else {
                    btnHome.setVisibility(View.GONE);
//                    btnHomeHongKong.setVisibility(View.GONE);
//                    ll_selector.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mList.get(position));
            return mList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((ImageView) object);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mList.clear();
        mList = null;
        viewPager = null;
        icons = null;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        mList.clear();
        mList = null;
        viewPager = null;
        icons = null;
    }
}
