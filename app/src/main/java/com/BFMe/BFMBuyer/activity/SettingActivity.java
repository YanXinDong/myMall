package com.BFMe.BFMBuyer.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.application.MyApplication;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.ugc.activity.PushSettingActivity;
import com.BFMe.BFMBuyer.utils.CacheUtils;
import com.BFMe.BFMBuyer.utils.DataCleanManager;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.MyCache;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Request;

/**
 * Description ：设置页面
 * Created by :奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2016/9/12 18:35
 */
public class SettingActivity extends IBaseActivity implements View.OnClickListener {

    private TextView tv_clear_cache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }



    @Override
    public int initLayout() {
        return R.layout.activity_setting;
    }


    /**
     * 初始化布局
     */
    private void initView() {

        setBackButtonVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.setting));
        //退出登录
        Button btn_setting_out = (Button) findViewById(R.id.btn_setting_out);
        tv_clear_cache = (TextView) findViewById(R.id.tv_clear_cache);
        try {
            String totalCacheSize = DataCleanManager.getTotalCacheSize(this);
            tv_clear_cache.setText(totalCacheSize);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //個人資料
        LinearLayout ll_setting_data = (LinearLayout) findViewById(R.id.ll_setting_data);

        //我的收货地址
        LinearLayout ll_address = (LinearLayout) findViewById(R.id.ll_address);

        //消息推送设置
        LinearLayout ll_push_setting = (LinearLayout) findViewById(R.id.ll_push_setting);

        //清除缓存
        LinearLayout ll_clear_cache = (LinearLayout) findViewById(R.id.ll_clear_cache);

        //关于BFME
        LinearLayout ll_setting_bfme = (LinearLayout) findViewById(R.id.ll_setting_bfme);

        btn_setting_out.setOnClickListener(this);
        ll_setting_data.setOnClickListener(this);
        ll_address.setOnClickListener(this);
        ll_push_setting.setOnClickListener(this);
        ll_clear_cache.setOnClickListener(this);
        ll_setting_bfme.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_address:
                startActivity(new Intent(this, AddressMangerActivity.class));
                startAnim();
                break;
            case R.id.ll_push_setting:
                startActivity(new Intent(this, PushSettingActivity.class));
                startAnim();
                break;
            case R.id.ll_setting_data:
                Intent intent = new Intent(this, SelfInfoDataActivity.class);
                startActivity(intent);
                startAnim();
                break;
            case R.id.ll_setting_bfme:
                startActivity(new Intent(SettingActivity.this, AboutBFMeActivity.class));
                startAnim();
                break;
            case R.id.ll_clear_cache:
                clearCache();
                break;
            case R.id.btn_setting_out:
                bindJPushUserRegistrationId();
                exit();
                break;
        }
    }

    /**
     * 解绑用户极光注册Id
     */
    private void bindJPushUserRegistrationId() {
        Map<String, String> params = new HashMap<>();
        params.put("userId",CacheUtils.getString(SettingActivity.this, GlobalContent.USER));
        params.put("registrationId", JPushInterface.getRegistrationID(SettingActivity.this));
        params.put("type","2");//1：绑定操作、2：解绑操作
        OkHttpUtils
                .post()
                .url(GlobalContent.POST_BIND_JPUSH_USER_REGISTRATIONID)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("解绑userId与激光注册ID"+response);
                    }
                });
    }

    /**
     * TODO 清除缓存
     */
    private void clearCache() {
        if(tv_clear_cache.getText().toString().equals("0.00k")){
            showToast("暂无缓存文件需要清理！");
            return;
        }
        new AlertDialog.Builder(this).setTitle("是否清除所有缓存文件！")
                .setNegativeButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        DataCleanManager.clearAllCache(SettingActivity.this);
                        tv_clear_cache.setText("0.00k");
                        dialog.dismiss();
                    }
                }).setPositiveButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    private void exit() {
        showToast(getString(R.string.exit_login));
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(GlobalContent.ISONE, true);
        startActivity(intent);

        CacheUtils.clear(SettingActivity.this);
        MyApplication.getInstance().exit();
        //推出云信账号
        NIMClient.getService(AuthService.class).logout();
        MyCache.clear();
        finish();
        startAnim();
    }
}
