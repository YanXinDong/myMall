package com.BFMe.BFMBuyer.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.utils.CacheUtils;
import com.BFMe.BFMBuyer.utils.Config;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.TUtils;
import com.google.gson.Gson;

public abstract class BaseFragment extends Fragment {

    public Activity mActivity;
    public View rootView;
    protected boolean isLogin;//是否登陆
    protected Gson mGson = new Gson();
    protected String mCartNumber;//购物车商品数量
    protected String mUserId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        mUserId = CacheUtils.getString(getContext(), GlobalContent.USER);
        isLogin = CacheUtils.getBoolean(mActivity, GlobalContent.ISLOGIN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        rootView = initView(inflater);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCartNumber = CacheUtils.getString(mActivity, GlobalContent.CART_NUMBER);
        initData();
    }

    protected  void initData(){

    };

    public abstract View initView(LayoutInflater inflater);

    protected void showToast(String text) {
        if (text != null && !text.trim().equals("")) {
//            Toast.makeText(mActivity, text, Toast.LENGTH_SHORT).show();
            TUtils.showToast(mActivity,text);
        }
    }

    protected void showProgress() {
        Config.getInstance().showProgress(mActivity);
    }
    protected void showProgress(String hint){
        Config.getInstance().showProgress(mActivity,hint);
    }

    protected void dismissProgress() {
        Config.getInstance().dismissProgress();
    }
    //启动Activity动画
    protected void startAnim() {
        mActivity.overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
    }

    //关闭Activity动画
    protected void exitAnim() {
        mActivity.overridePendingTransition(R.anim.enter_back, R.anim.exit_back);
    }

    /**
     * 底部启动Activity动画
     */
    protected void bottomStartAnim() {
        mActivity.overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.anim_no);
    }

    //底部关闭Activity动画
    protected void bottomexitAnim() {
        mActivity.overridePendingTransition(0, R.anim.slide_out_to_bottom);
    }
}

