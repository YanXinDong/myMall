package com.BFMe.BFMBuyer.base;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.application.MyApplication;
import com.BFMe.BFMBuyer.utils.CacheUtils;
import com.BFMe.BFMBuyer.utils.Config;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;

import butterknife.ButterKnife;

public abstract class NoTitleBaseActivity extends AppCompatActivity {

    protected Context mContext;
    protected boolean isLogin;//是否登陆
    protected Gson mGson = new Gson();
    private LinearLayout ll_content;
    protected String mUserId;
    protected boolean isTranslucentStatusBar = true;
    protected boolean isShare;//是否分享

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isTranslucentStatusBar) {
            translucentStatusBar();
        }
        setContentView(R.layout.activity_no_title_base);
        //加入到集合里面  方便回到首页  关闭其他的activity
        MyApplication.getInstance().addActivityHome(NoTitleBaseActivity.this);
        mContext = this;
        Config.getInstance().setActivityState(this);

        ll_content = (LinearLayout) findViewById(R.id.activity_no_title_base);
        ll_content.addView(LayoutInflater.from(this).inflate(initLayout(), ll_content,false));
        //必须放在布局加载之后
        ButterKnife.bind(this);

        isLogin = CacheUtils.getBoolean(NoTitleBaseActivity.this, GlobalContent.ISLOGIN);
        mUserId = CacheUtils.getString(NoTitleBaseActivity.this, GlobalContent.USER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isLogin = CacheUtils.getBoolean(NoTitleBaseActivity.this, GlobalContent.ISLOGIN);
        mUserId = CacheUtils.getString(NoTitleBaseActivity.this, GlobalContent.USER);
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

    protected void showToast(String text) {
        if (text != null && !text.trim().equals("")) {
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        }
    }

    protected void showProgress() {
        Config.getInstance().showProgress(mContext);
    }
    protected void showProgress(String hint){
        Config.getInstance().showProgress(mContext,hint);
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
    }

    public abstract int initLayout();

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

}
