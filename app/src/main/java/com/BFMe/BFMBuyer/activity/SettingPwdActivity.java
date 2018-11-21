package com.BFMe.BFMBuyer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.javaBean.UserInfo;
import com.BFMe.BFMBuyer.utils.EncryptCode;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:密码设置页面
 * Copyright  : Copyright (c) 2016
 * Company    : 白富美(北京)网络科技有限公司
 * Author     : 王  可
 * Date       : 2016/7/14 11:20
 */
public class SettingPwdActivity extends IBaseActivity implements View.OnClickListener {

    private EditText etAgainPwd;
    private EditText etRegistPwd;
    private String registPwd;
    private String phoneNumber;
    private String code;
    private Button btnRegistFinish;
    private int option;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        //获取手机号和验证码
        Intent intent = getIntent();
        phoneNumber = intent.getStringExtra("phoneNumber");
        code = intent.getStringExtra("code");
        option = intent.getIntExtra(GlobalContent.SETTING_PASSWORD_SIGN, GlobalContent.REQUEST_CODE_1);

        if (option == GlobalContent.REQUEST_CODE_1) {
            tv_title.setText(getString(R.string.setting_pwd));
            etRegistPwd.setHint(getString(R.string.input_login_pwd));
            etAgainPwd.setHint(getString(R.string.input_again_pwd));
            btnRegistFinish.setText(getString(R.string.register_finish));
        } else {
            tv_title.setText(getString(R.string.reset_pwd));
            etRegistPwd.setHint(getString(R.string.input_login_pwd));
            etAgainPwd.setHint(getString(R.string.input_again_pwd));
            btnRegistFinish.setText(getString(R.string.commit));
        }
    }

    @Override
    public int initLayout() {
        return R.layout.activity_setting_pwd;
    }

    /**
     * 初始化数据
     */
    private void initView() {
        setBackButtonVisibility(View.VISIBLE);

        etAgainPwd = (EditText) findViewById(R.id.et_again_pwd);
        etRegistPwd = (EditText) findViewById(R.id.et_regist_pwd);
        btnRegistFinish = (Button) findViewById(R.id.btn_regist_finish);

        btnRegistFinish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_regist_finish:

                registPwd = etRegistPwd.getText().toString();
                String againPwd = etAgainPwd.getText().toString().trim();

                if (registPwd.isEmpty() || againPwd.isEmpty()) {
                    showToast(getString(R.string.pwd_empt_hint));
                    return;
                }
                if (registPwd.length() < 6 || registPwd.length() > 20) {
                    showToast(getString(R.string.pwd_illegal));
                    return;
                }
                if (registPwd.equals(againPwd)) {
                    if (option == GlobalContent.REQUEST_CODE_1) {
                        //注册
                        finishRegist();
                    } else {
                        //重置密码
                        resetPWD();
                    }

                    finish();//关闭当前页面
                } else {
                    showToast(getString(R.string.pwd_inconformity));
                }
                break;
            default:
                break;
        }
    }

    /**
     * 重置密码的完成
     */
    private void resetPWD() {

        String password = null;
        String number = null;
        try {
            password = EncryptCode.encrypt(registPwd);
            number = EncryptCode.encrypt(phoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, String> map = new HashMap<>();
        map.put("CheckCode", code);
        map.put("NewPassword", password);
        map.put("Mobile", number);
        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBAL_REGIST_BUYER_PWD)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        RootBean rootBean = new Gson().fromJson(response, RootBean.class);
                        String errCode = rootBean.ErrCode;//返回值  是否成功
                        String responseMsg = rootBean.ResponseMsg;//返回消息  异常

                        if (Integer.parseInt(errCode) == 0) {
                            showToast(getString(R.string.pwd_reset_succeed_hint));
                        } else {
                            Toast.makeText(SettingPwdActivity.this, responseMsg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    /**
     * 设置密码
     */
    private void finishRegist() {
        String password = null;
        String number = null;
        registPwd = etRegistPwd.getText().toString();
        try {
            password = EncryptCode.encrypt(registPwd);
            number = EncryptCode.encrypt(phoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("Mobile", number);
        map.put("CheckCode", code);
        map.put("Password", password);
        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBAL_REGIST)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        RootBean rootBean = new Gson().fromJson(response, RootBean.class);
                        String errCode = rootBean.ErrCode;//返回值  是否成功
                        String responseMsg = rootBean.ResponseMsg;//返回消息  异常
                        UserInfo userInfo = new Gson().fromJson(rootBean.Data, UserInfo.class);

                        if (Integer.parseInt(errCode) == 0) {
                            showToast(getString(R.string.regist_succeed_hint));
                        } else {
                            Toast.makeText(SettingPwdActivity.this, responseMsg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
