package com.BFMe.BFMBuyer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.utils.CacheUtils;
import com.BFMe.BFMBuyer.utils.EncryptCode;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:注册页面
 * Copyright  : Copyright (c) 2016
 * Company    : 白富美(北京)网络科技有限公司
 * Author     : 王  可
 * Date       : 2016/7/13 15:09
 */
public class RegistActivity extends IBaseActivity implements View.OnClickListener {

    private EditText etPhone;
    private EditText edCode;
    private ImageView ivAgree;
    private Button btnGetcode;
    private String phoneNumber;
    private int opinion;

    private LinearLayout ll_register;

    //倒计时数字
    private int seconds = 60;
    //创建handler
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String string = getResources().getString(R.string.code_hint);
            String getCode = getResources().getString(R.string.get_code);

            switch (msg.what) {
                case 1:
                    if (seconds > 1) {
                        seconds--;
                        btnGetcode.setText(String.format(string,seconds));

                        handler.sendEmptyMessageDelayed(1, 1000);
                    } else {
                        //移除所有的消息
                        handler.removeCallbacksAndMessages(null);
                        seconds = 60;
                        btnGetcode.setText(getCode);
                        btnGetcode.setTextColor(getResources().getColor(R.color.light_blue));
                        btnGetcode.setEnabled(true);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        //获取activity回传的数据
        Intent intent = getIntent();
        opinion = intent.getIntExtra(GlobalContent.SETTING_PASSWORD_SIGN, GlobalContent.REQUEST_CODE_1);
        if (opinion == GlobalContent.REQUEST_CODE_1) {
            tv_title.setText(getString(R.string.regist));

            //注册的时候显示条款
            ll_register.setVisibility(View.VISIBLE);
        } else {
            tv_title.setText(getString(R.string.forget_pwd));

            //忘记密码时隐藏条款
            ll_register.setVisibility(View.GONE);

        }
    }

    private void initView() {
        setBackButtonVisibility(View.VISIBLE);
        etPhone = (EditText) findViewById(R.id.et_phone);
        edCode = (EditText) findViewById(R.id.editText);
        ivAgree = (ImageView) findViewById(R.id.iv_agree);
        Button btnRegistNext = (Button) findViewById(R.id.btn_regist_next);
        btnGetcode = (Button) findViewById(R.id.btn_getcode);
        TextView tvRegistAgree = (TextView) findViewById(R.id.tv_regist_agree);
        ll_register = (LinearLayout) findViewById(R.id.ll_register);


        btnRegistNext.setOnClickListener(this);
        btnGetcode.setOnClickListener(this);
        tvRegistAgree.setOnClickListener(this);
        ivAgree.setOnClickListener(this);
    }

    /**
     * 点击事件的处理
     */
    private boolean isChecked;//是否被选中

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_getcode:

                //效验手机号
                if (isPhone()) {
                    getCode();
                    btnGetcode.setText(String.format(getResources().getString(R.string.code_hint),seconds));
                    btnGetcode.setTextColor(getResources().getColor(R.color.gray));
                    btnGetcode.setEnabled(false);

                    handler.sendEmptyMessageDelayed(1, 1000);
                }
                break;
            case R.id.btn_regist_next:

                //效验手机号
                if (isPhone()) {

                    //再次获取手机号码和验证码
                    phoneNumber = etPhone.getText().toString().trim();
                    String code = edCode.getText().toString().trim();

                    if (!TextUtils.isEmpty(code) && !TextUtils.isEmpty(phoneNumber)) {
                        //判断是注册还是忘记密码
                        if (opinion == GlobalContent.REQUEST_CODE_1) {//注册
                            settingPWD(code);
                        } else {//忘记密码
                            resetPWD(code);
                        }

                    } else {
                        showToast(getString(R.string.write_verification_coder));
                    }
                }

                break;

            case R.id.tv_regist_agree:
                //跳转到注册协议页面
                Intent intentAgree = new Intent(this, UserRegistAgreeActivity.class);
                startActivity(intentAgree);
                startAnim();
                break;

            case R.id.iv_agree:
                //改变选中的状态
                if (isChecked) {
                    ivAgree.setImageResource(R.drawable.checkbox_off);
                    isChecked = false;
                } else {
                    ivAgree.setImageResource(R.drawable.checkbox_on);
                    isChecked = true;
                }
                break;
            default:
                break;
        }
    }

    /**
     * 验证当前手机
     */
    private void yanzhengPhone(final String code) {
        Map<String, String> map = new HashMap<>();
        try {
            map.put("Mobile", EncryptCode.encrypt(phoneNumber));
        } catch (Exception e) {
            e.printStackTrace();
        }
        CacheUtils.putString(RegistActivity.this, GlobalContent.CURRCODE, code);
        map.put("CheckCode", code);
        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBAL_CHECKQUICKREGIST)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        RootBean rootBean = new Gson().fromJson(response, RootBean.class);
                        String errCode = rootBean.ErrCode;
                        if ("0".equals(errCode)) {
                            //判断是注册还是忘记密码
                            if (opinion == GlobalContent.REQUEST_CODE_1) {//注册
                                skipPWD(code);
                            } else {
                                resetPWD(code);
                            }
                        } else {
                            Toast.makeText(RegistActivity.this, rootBean.ResponseMsg, Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
    }

    /**
     * 重置密码 验证当前手机
     */
    private void yanzhengPhone2(final String code) {
        Map<String, String> map = new HashMap<>();
        try {
            map.put("Mobile", EncryptCode.encrypt(phoneNumber));
        } catch (Exception e) {
            e.printStackTrace();
        }
        CacheUtils.putString(RegistActivity.this, GlobalContent.CURRCODE, code);
        map.put("CheckCode", code);
        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBAL_FORGETPWDCHECKCODE)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        RootBean rootBean = new Gson().fromJson(response, RootBean.class);
                        String errCode = rootBean.ErrCode;
                        if ("0".equals(errCode)) {
                            skipPWD(code);
                        } else {
                            showToast(rootBean.ResponseMsg);
                            return;
                        }
                    }
                });
    }

    /**
     * 忘记密码 重置密码
     *
     * @param code
     */
    private void resetPWD(String code) {
        yanzhengPhone2(code);
    }

    /**
     * 跳转到密码设置页面
     *
     * @param code
     */
    private void skipPWD(String code) {

        //进行下一步，密码设置
        Intent intent = new Intent(this, SettingPwdActivity.class);
        intent.putExtra(GlobalContent.SETTING_PASSWORD_SIGN, opinion);
        intent.putExtra("phoneNumber", phoneNumber);
        intent.putExtra("code", code);
        startActivity(intent);
        finish();
        startAnim();
    }

    /**
     * 注册设置密码
     *
     * @param code
     */
    private void settingPWD(String code) {

        if (isChecked) {
            yanzhengPhone(code);
        } else {
            showToast(getString(R.string.regist_agreement_hint));
            return;
        }
    }

    /**
     * 效验手机号码
     */
    private boolean isPhone() {

        //获取手机号码
        phoneNumber = etPhone.getText().toString().trim();

        //判断是否为空
        if (TextUtils.isEmpty(phoneNumber)) {
            showToast(getString(R.string.input_phone_number));
            return false;
        }
//        else {
//            //判断是否是手机号
//            Pattern compile = Pattern.compile(GlobalContent.PHONE);
//            Matcher matcher = compile.matcher(phoneNumber);
//            if (!matcher.find()) {
//                showToast(getString(R.string.input_phone_number_hint));
//                return false;
//
//            } else {
//                return true;
//            }
//        }
        return true;
    }

    /**
     * 请求服务器获取验证码
     */
    private void getCode() {

        if (opinion == GlobalContent.REQUEST_CODE_1) {//注册
            registerCode();
        } else {//忘记密码
            forgetPWDCode();
        }
    }

    /**
     * 忘记密码 获取验证码
     */
    private void forgetPWDCode() {
        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBAL_FORGET_PWD_GET_CODE)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                        showToast("网络错误,请重试。。。");
                    }

                    @Override
                    public void onResponse(String response) {
                        //验证码是否发送成功
                        isSendCode(response);
                    }
                });
    }

    /**
     * 验证码  是否发送成功
     *
     * @param response
     */
    private void isSendCode(String response) {

        RootBean rootBean = new Gson().fromJson(response, RootBean.class);
        String errCode = rootBean.ErrCode;//返回值  是否成功
        String responseMsg = rootBean.ResponseMsg;//返回消息  异常

        if (Integer.parseInt(errCode) == 0) {
            showToast(getString(R.string.code_send_succeed));
        } else {
            showToast(responseMsg);
        }

    }

    /**
     * 注册获取验证码
     */
    private void registerCode() {

        Map<String, String> map = new HashMap<>();
        map.put("Contract", phoneNumber);
        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBAL_GET_CODE)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                        showToast("网络错误,请重试。。。");
                    }

                    @Override
                    public void onResponse(String response) {
                        //验证码是否发送成功
                        isSendCode(response);
                    }
                });
    }

    /**
     * actvity销毁时移除所有消息  防止内存泄漏
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_regist;
    }
}
