package com.BFMe.BFMBuyer.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.activity.LoginActivity;
import com.BFMe.BFMBuyer.activity.MyOrderActivity;
import com.BFMe.BFMBuyer.activity.ShoppingCartActivity;
import com.BFMe.BFMBuyer.application.MyApplication;
import com.BFMe.BFMBuyer.search.activity.SearchShopActivity;
import com.BFMe.BFMBuyer.share.SelectShareCategoryActivity;
import com.BFMe.BFMBuyer.utils.CacheUtils;
import com.BFMe.BFMBuyer.utils.Config;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.TUtils;
import com.google.gson.Gson;
import com.mob.MobSDK;
import com.zhy.http.okhttp.OkHttpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.BFMe.BFMBuyer.R.id.ll_content;

public abstract class IBaseActivity extends AppCompatActivity {

    @BindView(R.id.view_bg)
    View view_bg;

    @BindView(R.id.vw_bg)
    protected View vw_bg;

    @BindView(R.id.iv_back)
    ImageView iv_back;
    protected LinearLayout mLL_content;
    private ShoppingCartNumberReceiver shoppingCartNumberReceiver;
    protected String mUserId;

    @BindView(R.id.iv_share)
    protected ImageView iv_share;

    @OnClick(R.id.iv_share)
    void share() {
        shareToWachat();
    }

    //上下切换动画
    protected boolean isBottomToTop;

    @OnClick(R.id.iv_back)
    void goBack() {
        if (isPay) {
            //跳转至待发货界面
            Intent intent = new Intent(IBaseActivity.this, MyOrderActivity.class);
            intent.putExtra("flag", 1);
            startActivity(intent);
            finish();
            startAnim();
        } else if (isBottomToTop) {
            finish();
            bottomexitAnim();
        } else {
            finish();
            exitAnim();
        }

    }

    @BindView(R.id.iv_title_right)
    protected ImageView iv_title_right;

    @OnClick(R.id.iv_title_right)
    void goToChart() {
        if (isLogin) {
            if (isShare) {
                shareToWachat();
            } else {
                startActivity(new Intent(mContext, ShoppingCartActivity.class));
                startAnim();
            }
        } else {
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
            startAnim();
        }

    }

    @BindView(R.id.iv_logo)
    ImageView iv_logo;

    @BindView(R.id.tv_search_circle)
    TextView tv_search_circle;

    @BindView(R.id.tv_cart_number)
    TextView tv_cart_number;

    @OnClick(R.id.tv_search_circle)
    void goSearch() {
        Intent intenSearch = new Intent(mContext, SearchShopActivity.class);
        intenSearch.putExtra(GlobalContent.SHOP_ID, shopId);
        startActivity(intenSearch);
        startAnim();
    }

    public TextView tv_title;
    public TextView tv_title_right;
    protected String shopId;

    protected boolean isPay;//是否是支付页面

    protected Context mContext;
    protected boolean isLogin;//是否登陆
    protected boolean isShare;//是否分享

    protected Gson mGson = new Gson();
    protected String mCartNumber;//购物车商品数量
    protected boolean isTranslucentStatusBar = true;
    protected String shareTitle;
    protected String shareContent;
    protected String shareImageUrl;
    protected String shareUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isTranslucentStatusBar) {
            translucentStatusBar();
        }
        setContentView(R.layout.activity_ibase);
        //加入到集合里面  方便回到首页  关闭其他的activity
        MyApplication.getInstance().addActivityHome(IBaseActivity.this);
        mContext = this;
        Config.getInstance().setActivityState(this);

        mLL_content = (LinearLayout) findViewById(ll_content);
        mLL_content.addView(LayoutInflater.from(this).inflate(initLayout(), mLL_content, false));
        //必须放在布局加载之后
        ButterKnife.bind(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title_right = (TextView) findViewById(R.id.tv_title_right);

        //注册购物车商品数量变化的广播接收器
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.BFMe.BFMBuyer.SHOPPING_CART_NUMBER");
        shoppingCartNumberReceiver = new ShoppingCartNumberReceiver();
        registerReceiver(shoppingCartNumberReceiver, intentFilter);

        isLogin = CacheUtils.getBoolean(IBaseActivity.this, GlobalContent.ISLOGIN);
        mUserId = CacheUtils.getString(IBaseActivity.this, GlobalContent.USER);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mCartNumber = CacheUtils.getString(IBaseActivity.this, GlobalContent.CART_NUMBER);

        int visibility = iv_title_right.getVisibility();

        if (isLogin && !mCartNumber.equals("0") && visibility == View.VISIBLE) {
            tv_cart_number.setVisibility(View.VISIBLE);
            tv_cart_number.setText(mCartNumber);
        } else {
            tv_cart_number.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUserId = CacheUtils.getString(IBaseActivity.this, GlobalContent.USER);
        isLogin = CacheUtils.getBoolean(IBaseActivity.this, GlobalContent.ISLOGIN);
    }

    /**
     * 购物车商品数量变化的广播接收器
     */
    private class ShoppingCartNumberReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            mCartNumber = CacheUtils.getString(IBaseActivity.this, GlobalContent.CART_NUMBER);
            int visibility = iv_title_right.getVisibility();

            if (isLogin && !mCartNumber.equals("0") && visibility == View.VISIBLE) {
                tv_cart_number.setVisibility(View.VISIBLE);
                tv_cart_number.setText(mCartNumber);
            } else {
                tv_cart_number.setVisibility(View.GONE);
            }
        }
    }

    //title控件的显示隐藏
    public void setViewBGButtonVisibility(int visibility) {
        view_bg.setVisibility(visibility);
    }

    public void setCartNumberVisibility(int visibility) {
        tv_cart_number.setVisibility(visibility);
    }

    public void setChartButtonVisibility(int visibility) {
        iv_title_right.setVisibility(visibility);
    }

    public void setLogoButtonVisibility(int visibility) {
        iv_logo.setVisibility(visibility);
    }

    public void setSearchButtonVisibility(int visibility) {
        tv_search_circle.setVisibility(visibility);
    }

    public void setBackButtonVisibility(int visibility) {
        iv_back.setVisibility(visibility);
    }

    public void setBackButtonImageResource(int resId) {
        iv_back.setImageResource(resId);
    }

    protected void showToast(String text) {
        if (text != null && !text.trim().equals("")) {
//            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            TUtils.showToast(getApplicationContext(),text);
        }
    }

    protected void showProgress() {
        Config.getInstance().showProgress(mContext);
    }

    protected void showProgress(String hint) {
        Config.getInstance().showProgress(mContext, hint);
    }

    protected void dismissProgress() {
        Config.getInstance().dismissProgress();
    }

    /**
     * 点击屏幕空白区域隐藏软键盘（方法1）
     * <p>在onTouch中处理，未获焦点则隐藏</p >
     * <p>参照以下注释代码</p >
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(R.anim.enter_back, R.anim.exit_back);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //可以取消同一个tag的
        OkHttpUtils instance = OkHttpUtils.getInstance();
        instance.cancelTag(this);
        unregisterReceiver(shoppingCartNumberReceiver);
    }

    public abstract int initLayout();

    private void translucentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
    }

    //启动Activity动画
    protected void startAnim() {
        overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
    }

    //关闭Activity动画
    protected void exitAnim() {
        overridePendingTransition(R.anim.enter_back, R.anim.exit_back);
    }

    /**
     * 底部启动Activity动画
     */
    protected void bottomStartAnim() {
        overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.anim_no);
    }

    //底部关闭Activity动画
    protected void bottomexitAnim() {
        overridePendingTransition(0, R.anim.slide_out_to_bottom);
    }

    //分享至微信或朋友圈
    private void shareToWachat() {
        MobSDK.init(this,"1b462d46b8900","514cfad75c1832fd14fb3b7ce97cba9d");
        Intent intent = new Intent(this, SelectShareCategoryActivity.class);
        intent.putExtra("shareTitle",shareTitle);
        intent.putExtra("shareContent",shareContent);
        intent.putExtra("shareImageUrl",shareImageUrl);
        intent.putExtra("shareUrl",shareUrl);
        startActivity(intent);
        bottomStartAnim();
    }
}
