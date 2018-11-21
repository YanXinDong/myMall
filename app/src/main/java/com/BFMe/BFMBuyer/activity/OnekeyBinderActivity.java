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
import android.widget.TextView;
import android.widget.Toast;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.javaBean.CartBean;
import com.BFMe.BFMBuyer.javaBean.ImInfoBean;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.javaBean.UserInfo;
import com.BFMe.BFMBuyer.main.MainActivity;
import com.BFMe.BFMBuyer.utils.CacheUtils;
import com.BFMe.BFMBuyer.utils.EncryptCode;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.MyCache;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.google.gson.Gson;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Request;

/**
 * Created by BFMe.miao on 2018/3/20.
 * 一键绑定
 */

public class OnekeyBinderActivity extends IBaseActivity implements View.OnClickListener {

    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.editText)
    EditText etEditText;
    @BindView(R.id.btn_getcode)
    Button btnGetCode;
    @BindView(R.id.iv_agree)
    ImageView ivAgree;
    @BindView(R.id.tv_regist_agree)
    TextView tvRegisterAgree;
    @BindView(R.id.btn_register)
    Button btnRegister;

    private String phoneNumber;
    private String IMUSERID = "imuserid";
    private String IMPASSWORD = "impassword";
    private boolean isChecked;//是否被选中

    @Override
    public int initLayout() {
        return R.layout.activity_onekeybinder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setBackButtonVisibility(View.VISIBLE);
        tv_title.setText(getResources().getString(R.string.onekey_binder_title));
        btnGetCode.setOnClickListener(this);
        ivAgree.setOnClickListener(this);
        tvRegisterAgree.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_getcode:
                getCode();
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
            case R.id.tv_regist_agree:
                //跳转到注册协议页面
                Intent intentAgree = new Intent(this, UserRegistAgreeActivity.class);
                startActivity(intentAgree);
                startAnim();
                break;
            case R.id.btn_register:
                Intent intent = getIntent();
                String opendid = intent.getStringExtra("opendid");
                String sign = intent.getStringExtra("sign");
                String img = intent.getStringExtra("img");
                String oauthid = intent.getStringExtra("oauthid");
                String code = etEditText.getText().toString();
                if(isPhone()){
                    onekeyBinder(phoneNumber,code,opendid,sign,img,oauthid);
                }

                break;
        }
    }

    private void onekeyBinder(String phoneNumber, String code, String opendid, String sign, String img, String oauthid) {
        Map<String, String> map = new HashMap<>();
        map.put("opendid", opendid);
        map.put("img", img);
        map.put("sign", sign);
        map.put("oauthid", oauthid);
        map.put("phone", phoneNumber);
        map.put("checkcode", code);
        Logger.e("一键绑定请求==="+map.toString());

        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBAL_ONEKEY_BINDER)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                        showToast("网络错误,请重试。。。");
                    }

                    @Override
                    public void onResponse(String response) {
                        RootBean rootBean = new Gson().fromJson(response, RootBean.class);
                        String errCode = rootBean.ErrCode;//返回值  是否成功
                        Logger.e("一键绑定==="+response);
                        if (Integer.parseInt(errCode) == 0) {
                            UserInfo userInfo = new Gson().fromJson(rootBean.Data, UserInfo.class);
                            CacheUtils.putString(OnekeyBinderActivity.this, GlobalContent.USER, userInfo.getId());
                            getNetShoppingCartNumber();
                            bindJPushUserRegistrationId();
                            loginIM();
                        } else {
                            Toast.makeText(OnekeyBinderActivity.this, rootBean.ResponseMsg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });




    }
    /**
     * 发送购物车商品数量改变的广播
     */
    private void sendCartNumberChangeBroadcast() {
        Intent intent = new Intent("com.BFMe.BFMBuyer.SHOPPING_CART_NUMBER");
        sendBroadcast(intent);
    }

    private void getCode() {
        //效验手机号
        if (isPhone()) {
            getNetCode();
            btnGetCode.setText(String.format(getResources().getString(R.string.code_hint),seconds));
            btnGetCode.setTextColor(getResources().getColor(R.color.gray));
            btnGetCode.setEnabled(false);

            handler.sendEmptyMessageDelayed(1, 1000);
        }
    }

    /**
     * 服务器发送验证码
     */
    private void getNetCode() {
        String encryptNumber = null;
        try {
            encryptNumber = EncryptCode.encrypt(phoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, String> map = new HashMap<>();
        map.put("mobile",encryptNumber );
        Logger.e("服务器发送验证码"+map.toString());
        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBAL_GET_CODE_THREE)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                        showToast("网络错误,请重试。。。");
                        Logger.e("服务器发送验证码"+e.toString());
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
        return true;
    }

    //倒计时数字
    private int seconds = 60;
    //创建handler
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:
                    if (seconds > 1) {
                        seconds--;
                        btnGetCode.setText(String.format(getResources().getString(R.string.code_hint),seconds));
                        handler.sendEmptyMessageDelayed(1, 1000);
                    } else {
                        //移除所有的消息
                        handler.removeCallbacksAndMessages(null);
                        seconds = 60;
                        btnGetCode.setText(getString(R.string.get_code));
                        btnGetCode.setTextColor(getResources().getColor(R.color.light_blue));
                        btnGetCode.setEnabled(true);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    private void getNetShoppingCartNumber() {
        String userId = CacheUtils.getString(this, GlobalContent.USER);
        OkHttpUtils
                .get()
                .url(GlobalContent.GLOBSAL_GETCSRT_LIST)
                .addParams("UserId", userId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);

                        if (rootBean.ErrCode.equals("0")) {
                            CartBean cartBean = mGson.fromJson(rootBean.Data, CartBean.class);
                            getCartNumber(cartBean);
                        }
                    }
                });
    }

    private void getCartNumber(CartBean cartBean) {
        List<CartBean.CartItemsBean> cartItems = cartBean.getCartItems();
        int number = 0;
        for (int i = 0; i < cartItems.size(); i++) {
            for (int j = 0; j < cartItems.get(i).getCartItems().size(); j++) {
                number++;
            }
        }
        CacheUtils.putString(OnekeyBinderActivity.this, GlobalContent.CART_NUMBER, String.valueOf(number));
        sendCartNumberChangeBroadcast();
    }

    /**
     * 绑定用户极光注册Id
     */
    private void bindJPushUserRegistrationId() {
        Map<String, String> params = new HashMap<>();
        params.put("userId", CacheUtils.getString(OnekeyBinderActivity.this, GlobalContent.USER));
        params.put("registrationId", JPushInterface.getRegistrationID(OnekeyBinderActivity.this));
        params.put("type", "1");//1：绑定操作、2：解绑操作
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
                        Logger.e("绑定userId与激光注册ID" + response);
                    }
                });
    }


    /**
     * 获取云信用户名和密码
     */
    private void loginIM() {
        String userId = CacheUtils.getString(OnekeyBinderActivity.this, GlobalContent.USER);
        Map<String, String> map = new HashMap<>();
        map.put("UserId", userId);
        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBAL_LOGINIM)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                    }
                    @Override
                    public void onResponse(String response) {
                        Logger.e("获取云信账号===" + response);
                        RootBean rootBean = new Gson().fromJson(response, RootBean.class);
                        ImInfoBean imInfoBean = new Gson().fromJson(rootBean.Data, ImInfoBean.class);
                        Logger.e("云信账号密码===" + imInfoBean.getIMInfo().getIMUserId() + "       " + imInfoBean.getIMInfo().getIMPassWord());
                        loginIm(imInfoBean.getIMInfo().getIMUserId(), imInfoBean.getIMInfo().getIMPassWord());
                    }
                });

    }

    /**
     * 登录事件响应函数
     *
     * @param imUserId
     * @param imPassWord
     */
    private void loginIm(final String imUserId, final String imPassWord) {
        LoginInfo info = new LoginInfo(imUserId, imPassWord); // config...
        RequestCallback<LoginInfo> callback =
                new RequestCallback<LoginInfo>() {
                    @Override
                    public void onSuccess(LoginInfo loginInfo) {
                        Logger.e("云信登录成功===");
                        MyCache.setAccount(imPassWord);
                        CacheUtils.putString(OnekeyBinderActivity.this, IMUSERID, loginInfo.getAccount());
                        CacheUtils.putString(OnekeyBinderActivity.this, IMPASSWORD, loginInfo.getToken());
                        //存储一个boolear值判断下次是否还登陆
                        CacheUtils.putBoolean(OnekeyBinderActivity.this, GlobalContent.ISLOGIN, true);
                        NimUIKit.setAccount(loginInfo.getAccount());
                        Intent intentLogin = new Intent(OnekeyBinderActivity.this, MainActivity.class);
                        startActivity(intentLogin);
                        finish();
                        startAnim();
                    }

                    @Override
                    public void onFailed(int i) {
                        Logger.e("云信登录失败" + i);
                        NIMClient.getService(AuthService.class).logout();
                        loginIm(imUserId, imPassWord);

                    }

                    @Override
                    public void onException(Throwable throwable) {
                        Logger.e("云信登录异常===");
                        NIMClient.getService(AuthService.class).logout();

                    }
                };
        NIMClient.getService(AuthService.class).login(info)
                .setCallback(callback);
    }

}
