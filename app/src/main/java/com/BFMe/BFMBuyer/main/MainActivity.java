package com.BFMe.BFMBuyer.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.activity.LoginActivity;
import com.BFMe.BFMBuyer.activity.ShoppingCartActivity;
import com.BFMe.BFMBuyer.base.BaseFragment;
import com.BFMe.BFMBuyer.search.activity.SearchActivity;
import com.BFMe.BFMBuyer.utils.CacheUtils;
import com.BFMe.BFMBuyer.utils.GlobalContent;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private View include_title;
    private RadioGroup rg_group;
    private int flag = 0;
    private List<BaseFragment> baseFragments;
    private TextView tv_logo;
    private TextView tv_title;
    private boolean isLogin;
    private long mExitTime;
    private TextView tv_cart_number;
    protected String mCartNumber;//购物车商品数量
    private ShoppingCartNumberReceiver shoppingCartNumberReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        translucentStatusBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);

        initView();
        initData();
    }

    private void initData() {

        //注册购物车商品数量变化的广播接收器
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.BFMe.BFMBuyer.SHOPPING_CART_NUMBER");
        shoppingCartNumberReceiver = new ShoppingCartNumberReceiver();
        registerReceiver(shoppingCartNumberReceiver, intentFilter);


        baseFragments = new ArrayList<>(5);
        baseFragments.add(new HomeFragment());
        baseFragments.add(new ShopFragment());
        baseFragments.add(new ClassifyFragment());
        baseFragments.add(new UGCFragment());
        baseFragments.add(new MyFragment());

        RadioButton home_btn = (RadioButton) rg_group.getChildAt(0);
        home_btn.setChecked(true);
    }

    /**
     * 购物车商品数量变化的广播接收器
     */
    private class ShoppingCartNumberReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            getCartNumber();
        }
    }

    private void getCartNumber() {
        mCartNumber = CacheUtils.getString(MainActivity.this, GlobalContent.CART_NUMBER);

        if (isLogin && !mCartNumber.equals("0")) {
            tv_cart_number.setVisibility(View.VISIBLE);
            tv_cart_number.setText(mCartNumber);
        } else {
            tv_cart_number.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isLogin = CacheUtils.getBoolean(this, GlobalContent.ISLOGIN);
        boolean isFromShoppingCart = CacheUtils.getBoolean(this, "IsFromCar");
        if (isFromShoppingCart) {
            CacheUtils.putBoolean(MainActivity.this, "IsFromCar", false);
            setOnClickShopListner(0);
        }

        getCartNumber();
    }

    private void setOnClickShopListner(int i) {
        RadioButton childAt = (RadioButton) rg_group.getChildAt(i);
        childAt.setChecked(true);
    }

    private void initView() {
        include_title = findViewById(R.id.include_title);
        rg_group = (RadioGroup)findViewById(R.id.rg_group);
        rg_group.setOnCheckedChangeListener(new MyOnCheckedChangeListener());

        tv_logo = (TextView)findViewById(R.id.tv_logo);
        tv_title = (TextView)findViewById(R.id.tv_title);
        ImageView iv_shopping_cart = (ImageView) findViewById(R.id.iv_shopping_cart);
        ImageView iv_search = (ImageView) findViewById(R.id.iv_search);
        tv_cart_number = (TextView)findViewById(R.id.tv_cart_number);

        iv_shopping_cart.setOnClickListener(this);
        iv_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.iv_shopping_cart:
                if (isLogin) {
                    startActivity(new Intent(this, ShoppingCartActivity.class));
                } else {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
                overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
                break;
            case R.id.iv_search:
                Intent intenSearch = new Intent(this, SearchActivity.class);
                startActivity(intenSearch);
                overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
                break;
        }
    }

    /**
     * 主页面底部按钮的改变监听
     */
    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        private String HOME_FRAGMENT = "home_fragment";
        private String SHOP_FRAGMENT = "shop_fragment";
        private String CLASSIF_FRAGMENT = "classif_fragment";
        private String MESSAGE_FRAGMENT = "message_fragment";
        private String ME_FRAGMENT = "me_fragment";

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment homeFragment = fm.findFragmentByTag(HOME_FRAGMENT);
            Fragment shopFragment = fm.findFragmentByTag(SHOP_FRAGMENT);
            Fragment classifyFragment = fm.findFragmentByTag(CLASSIF_FRAGMENT);
            Fragment messageFragment = fm.findFragmentByTag(MESSAGE_FRAGMENT);
            Fragment meFragment = fm.findFragmentByTag(ME_FRAGMENT);

            if (homeFragment != null) {
                ft.hide(homeFragment);
            }
            if (shopFragment != null) {
                ft.hide(shopFragment);
            }
            if (classifyFragment != null) {
                ft.hide(classifyFragment);
            }
            if (messageFragment != null) {
                ft.hide(messageFragment);
            }
            if (meFragment != null) {
                ft.hide(meFragment);
            }

            switch (checkedId) {
                case R.id.rb_home:
                    flag = 0;
                    if (homeFragment == null) {
                        homeFragment = baseFragments.get(0);
                        ft.add(R.id.fragment_main, homeFragment, HOME_FRAGMENT);
                    } else {
                        ft.show(homeFragment);
                    }
                    include_title.setVisibility(View.VISIBLE);
                    tv_logo.setVisibility(View.VISIBLE);
                    tv_title.setVisibility(View.GONE);
                    break;
                case R.id.rb_shop:
                    flag = 1;
                    if (shopFragment == null) {
                        shopFragment = baseFragments.get(1);
                        ft.add(R.id.fragment_main, shopFragment, SHOP_FRAGMENT);
                    } else {
                        ft.show(shopFragment);
                    }
                    include_title.setVisibility(View.VISIBLE);
                    tv_logo.setVisibility(View.GONE);
                    tv_title.setVisibility(View.VISIBLE);
                    tv_title.setText(getString(R.string.merchant));
                    break;
                case R.id.rb_sort:
                    flag = 2;
                    if (classifyFragment == null) {
                        classifyFragment = baseFragments.get(2);
                        ft.add(R.id.fragment_main, classifyFragment,
                                CLASSIF_FRAGMENT);
                    } else {
                        ft.show(classifyFragment);
                    }
                    include_title.setVisibility(View.VISIBLE);
                    tv_logo.setVisibility(View.GONE);
                    tv_title.setVisibility(View.VISIBLE);
                    tv_title.setText(getString(R.string.category));
                    break;
                case R.id.rb_message:
                    flag = 3;
                    if (messageFragment == null) {
                        messageFragment = baseFragments.get(3);
                        ft.add(R.id.fragment_main, messageFragment, MESSAGE_FRAGMENT);
                    } else {
                        ft.show(messageFragment);
                    }
                    include_title.setVisibility(View.VISIBLE);
                    tv_logo.setVisibility(View.GONE);
                    tv_title.setVisibility(View.VISIBLE);
                    tv_title.setText(getString(R.string.news));
                    break;
                case R.id.rb_my:
                        flag = 3;
                        if (meFragment == null) {
                            meFragment = baseFragments.get(4);
                            ft.add(R.id.fragment_main, meFragment, ME_FRAGMENT);
                        } else {
                            ft.show(meFragment);
                        }
                        include_title.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
            ft.commit();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        for (int i = 0; i < rg_group.getChildCount(); i++) {
            RadioButton mTab = (RadioButton) rg_group.getChildAt(i);
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentByTag((String) mTab.getTag());
            FragmentTransaction ft = fm.beginTransaction();
            if (fragment != null) {
                if (!mTab.isChecked()) {
                    ft.hide(fragment);
                }
            }
            ft.commit();
        }
    }

    protected void translucentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (flag == 0) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    Toast.makeText(this, getString(R.string.quit_procedure), Toast.LENGTH_SHORT).show();
                    mExitTime = System.currentTimeMillis();

                } else {
                    finish();
                }
                return true;
            }
            return super.onKeyDown(keyCode, event);
        } else {
            RadioButton childAt = (RadioButton) rg_group.getChildAt(0);
            childAt.setChecked(true);
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(shoppingCartNumberReceiver);
    }

}
