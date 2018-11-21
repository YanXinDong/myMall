package com.BFMe.BFMBuyer.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.NoTitleBaseActivity;
import com.BFMe.BFMBuyer.javaBean.CartBean;
import com.BFMe.BFMBuyer.javaBean.ImInfoBean;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.javaBean.UserInfo;
import com.BFMe.BFMBuyer.main.MainActivity;
import com.BFMe.BFMBuyer.main.presenter.MyTextWatcher;
import com.BFMe.BFMBuyer.utils.CacheUtils;
import com.BFMe.BFMBuyer.utils.EncryptCode;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.MyCache;
import com.BFMe.BFMBuyer.utils.UiUtils;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.mob.MobSDK;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import okhttp3.Request;

/**
 * Description:登录页面
 * Copyright  : Copyright (c) 2016
 * Company    : 白富美(北京)网络科技有限公司
 * Author     : 王  可
 * Date       : 2016/7/13 10:07
 */
public class LoginActivity extends NoTitleBaseActivity implements View.OnClickListener {

    private EditText etNumber; //密码
    private EditText etPassword;  //手机号
    private TextView tvForgetPwd;     //忘记密码
    private Button btnLogin;      //登录
    private TextView tv_regist;        //注册
    private String IMUSERID = "imuserid";
    private String IMPASSWORD = "impassword";

    private boolean isOne;
    private String mNumber = "";
    private String mPassword = "";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private TextView mTvTipe;
    private TextView mTvMainLandUserLine;
    private TextView mTvHongKongUserLine;
    private TextView mTvMainLandUser;
    private TextView mTvHongKongUser;
    private boolean isMainLand = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        translucentStatusBar();
        MobSDK.init(this);
        //证书的拷贝
        isOne = getIntent().getBooleanExtra(GlobalContent.ISONE, false);
        EncryptCode.copyDb("BFMeBuyer.der.cer", this);
        initView();
        initListener();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initListener() {
        etNumber.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void myAfterTextChanged(Editable s) {
                mNumber = s.toString().trim();
                ValidationData();
            }
        });
        etPassword.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void myAfterTextChanged(Editable s) {
                mPassword = s.toString().trim();
                ValidationData();
            }
        });
    }

    private void ValidationData() {
        if (!mNumber.isEmpty() && !mPassword.isEmpty()) {
            btnLogin.setBackgroundResource(R.color.blue_035de9);
        } else {
            btnLogin.setBackgroundResource(R.color.color_aaaaaa_content_text);
        }
    }

    /**
     * 初始化组件
     */
    private void initView() {

        etNumber = (EditText) findViewById(R.id.et_number);
        etPassword = (EditText) findViewById(R.id.et_password);
        tvForgetPwd = (TextView) findViewById(R.id.tv_forget_pwd);
        btnLogin = (Button) findViewById(R.id.btn_login);
        tv_regist = (TextView) findViewById(R.id.tv_regist);
        ImageView iv_delete = (ImageView) findViewById(R.id.iv_delete);
        mTvTipe = (TextView) findViewById(R.id.tv_tipe);
        mTvMainLandUser = (TextView) findViewById(R.id.tv_mainland_user);
        mTvHongKongUser = (TextView) findViewById(R.id.tv_hongkong_user);
        mTvMainLandUserLine = (TextView) findViewById(R.id.tv_mainland_user_line);
        mTvHongKongUserLine = (TextView) findViewById(R.id.tv_hongkong_user_line);


        TextView mTvWeiBo = (TextView) findViewById(R.id.weibo_login);
        TextView mTvWeChat = (TextView) findViewById(R.id.weixin_login);
        Drawable drawable = getResources().getDrawable(R.drawable.ssdk_oks_classic_wechat);
        drawable.setBounds(0, 0, UiUtils.dip2px(40), UiUtils.dip2px(40));
        mTvWeChat.setCompoundDrawables(drawable, null, null, null);
        drawable = getResources().getDrawable(R.drawable.ssdk_oks_classic_sinaweibo);
        drawable.setBounds(0, 0, UiUtils.dip2px(40), UiUtils.dip2px(40));
        mTvWeiBo.setCompoundDrawables(drawable, null, null, null);

        mTvWeiBo.setOnClickListener(this);
        mTvWeChat.setOnClickListener(this);
        iv_delete.setOnClickListener(this);
        tv_regist.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        tvForgetPwd.setOnClickListener(this);
        mTvMainLandUser.setOnClickListener(this);
        mTvHongKongUser.setOnClickListener(this);
    }

    /**
     * 点击的事件处理
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_regist://注册
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                startAnim();
                finish();
                break;
            case R.id.btn_login://登陆

                login1();
                break;
            case R.id.tv_forget_pwd://忘记密码

                Intent intentForget = new Intent(this, RegistActivity.class);
                intentForget.putExtra(GlobalContent.SETTING_PASSWORD_SIGN, GlobalContent.REQUEST_CODE_2);
                startActivity(intentForget);
                startAnim();
                break;
            case R.id.iv_delete://跳过登录
                if (isOne) {
                    CacheUtils.putBoolean(LoginActivity.this, "IsFromCar", true);
                    Intent intentLogin = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intentLogin);
                }
                finish();
                exitAnim();
                break;
            case R.id.weibo_login://微博登录
                Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                sanfangLogin(weibo,"Himall.Plugin.OAuth.Weibo");
                break;
            case R.id.weixin_login://微信登录
                Platform weixin = ShareSDK.getPlatform(Wechat.NAME);
                sanfangLogin(weixin,"Himall.Plugin.OAuth.WeiXin");
                break;
            default:
                break;
        }
    }

    private void sanfangLogin(Platform platform, final String outhid) {
        if (platform.isAuthValid()) {
            platform.removeAccount(true);
        }
        if(platform.isClientValid()){
            platform.removeAccount(true);
        }
        platform.setPlatformActionListener(new PlatformActionListener() {

            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                arg2.printStackTrace();
                Logger.e("三方登录===" + arg2);
                arg0.removeAccount(true);
            }

            @Override
            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                arg0.getDb().exportData();
                Iterator ite = arg2.entrySet().iterator();
                String headimgurl = "";
                while (ite.hasNext()) {
                    Map.Entry entry = (Map.Entry) ite.next();
                    String key = (String) entry.getKey();
                    Object value = entry.getValue();
                    if (key.equals("headimgurl")) {
                        headimgurl = (String) value;
                    }
                    if(key.equals("icon")){
                        headimgurl = (String) value;
                    }
                }
                Logger.e("三方登录===" + arg0.getDb().exportData());
                PlatformDb db = arg0.getDb();
                String encrypt = null;
                try {
                    encrypt = EncryptCode.encrypt(db.getUserId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sanfangLogin(db.getUserName(), db.getUserId(), headimgurl, encrypt, outhid);
            }

            @Override
            public void onCancel(Platform arg0, int arg1) {

            }
        });
        platform.SSOSetting(false); // true表示不使用SSO方式授权
        platform.showUser(null);//执行登录，登录后在回调里面获取用户资料
    }


    /**
     * 三方登录到自己服务器
     *
     * @param nick
     * @param opendid
     * @param img
     * @param sign   Himall.Plugin.OAuth.WeiXin
     * @param oauthid nick=BFME&opendid=ohtsls7aVEEpSVTE3UV1yOzguOoA&img=&oauthid=Himall.Plugin.OAuth.WeiXin&sign=oxGKjM3Np+I5p83SPGDJDfIgFre3C4iKGhXL2n/oWdQh/2kdNCR3PLzLsNC4pgzzkvq0vJf2lurktyi7tTmFJiA3cjxnWQPCgWbvLoQ2Vyxl2lkFrGGuXWRZsdhOQMDFG6pVUkdU1Lb0fh5oVFaM8mwc3kuhzvOrAcdy6fyO1UyYl+C1CRpgoTOEpP7+70p5Y8yUSA9r7ZFIgG1tLgM/w7kqGqgLZABQkfVSvW0i+WXzq4T4T8QDXShCHcCQgpyMBGlE8A0mFnQ64BiMObLJvAjZ1koxFLmfY9DjGNDXBbQyaZ+hNYUo83lEZYpP3Kqd1MCWH8VGvVBRcNInyU8mWg==
     */
    private void sanfangLogin(String nick, final String opendid, final String img, final String sign, final String oauthid) {
        Map<String, String> map = new HashMap<>();
        map.put("opendid", opendid);
        map.put("img", img);
        map.put("sign", sign);
        map.put("oauthid", oauthid);
        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBAL_LOGIN_THREE)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        showToast("联网失败");
                    }

                    @Override
                    public void onResponse(String response) {
                        if (getErrorCode(response).equals("0")) {
                            parserLogin(response);
                        } else if (getErrorCode(response).equals("100005")) {//未绑定用户
                            Intent intent = new Intent(LoginActivity.this, OnekeyBinderActivity.class);
                            intent.putExtra("opendid", opendid);
                            intent.putExtra("sign", sign);
                            intent.putExtra("img", img);
                            intent.putExtra("oauthid", oauthid);
                            startActivity(intent);
                            startAnim();
                        }

                    }
                });

    }

    private String getErrorCode(String response) {
        RootBean rootBean = new Gson().fromJson(response, RootBean.class);
        return rootBean.ErrCode;
    }

    private void parserLogin(String response) {
        RootBean rootBean = new Gson().fromJson(response, RootBean.class);
        String errCode = rootBean.ErrCode;//返回值  是否成功
        Logger.e("三方登录==="+response);
        if (Integer.parseInt(errCode) == 0) {
            UserInfo userInfo = new Gson().fromJson(rootBean.Data, UserInfo.class);
            CacheUtils.putString(LoginActivity.this, GlobalContent.USER, userInfo.getId());
            getNetShoppingCartNumber();
            bindJPushUserRegistrationId();
            loginIM();
        } else {
            Toast.makeText(LoginActivity.this, rootBean.ResponseMsg, Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 访问网络进行登录
     */
    private void login1() {
        //获取手机号和密码
        String number = etNumber.getText().toString().trim();
        String phone = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(number) || TextUtils.isEmpty(phone)) {
            showToast(getString(R.string.login_empt_hint));
            return;
        }
        //对手机号和密码进行加密
        String encryptNumber = null;
        String encryptPhone = null;
        try {
            encryptNumber = EncryptCode.encrypt(number);
            encryptPhone = EncryptCode.encrypt(phone);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, String> map = new HashMap<>();
        map.put("UserName", encryptNumber);
        map.put("Password", encryptPhone);
        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBAL_LOGIN)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        showToast("联网失败");
                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("本地登录===" + response);
                        parserLogin(response);
                    }
                });
    }

    /**
     * 绑定用户极光注册Id
     */
    private void bindJPushUserRegistrationId() {
        Map<String, String> params = new HashMap<>();
        params.put("userId", CacheUtils.getString(LoginActivity.this, GlobalContent.USER));
        params.put("registrationId", JPushInterface.getRegistrationID(LoginActivity.this));
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
        CacheUtils.putString(LoginActivity.this, GlobalContent.CART_NUMBER, String.valueOf(number));
        sendCartNumberChangeBroadcast();
    }

    /**
     * 发送购物车商品数量改变的广播
     */
    private void sendCartNumberChangeBroadcast() {
        Intent intent = new Intent("com.BFMe.BFMBuyer.SHOPPING_CART_NUMBER");
        sendBroadcast(intent);
    }

    /**
     * 获取云信用户名和密码
     */
    private void loginIM() {
        String userId = CacheUtils.getString(LoginActivity.this, GlobalContent.USER);
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
                        CacheUtils.putString(LoginActivity.this, IMUSERID, loginInfo.getAccount());
                        CacheUtils.putString(LoginActivity.this, IMPASSWORD, loginInfo.getToken());
                        //存储一个boolear值判断下次是否还登陆
                        CacheUtils.putBoolean(LoginActivity.this, GlobalContent.ISLOGIN, true);
                        NimUIKit.setAccount(loginInfo.getAccount());

                        if (isOne) {
                            Intent intentLogin = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intentLogin);
                        }
                        finish();
                        startAnim();
                    }

                    @Override
                    public void onFailed(int i) {
                        Logger.e("云信登录失败" + i);
                        //推出云信账号
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

    @Override
    public int initLayout() {
        return R.layout.activity_login;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isOne) {
                CacheUtils.putBoolean(LoginActivity.this, "IsFromCar", true);
                Intent intentLogin = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intentLogin);
            }
            finish();
            exitAnim();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Login Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}

