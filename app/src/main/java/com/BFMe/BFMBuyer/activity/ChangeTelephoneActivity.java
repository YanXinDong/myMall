package com.BFMe.BFMBuyer.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.utils.CacheUtils;
import com.BFMe.BFMBuyer.utils.EncryptCode;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * Description：修改手机号
 * Created by :奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2016/9/18 16:45
 */
public class ChangeTelephoneActivity extends IBaseActivity implements View.OnClickListener {
    private ImageView ivJiantou1;
    private ImageView ivChange2;
    private ImageView ivJiantou2;
    private ImageView ivChange3;
    private TextView tvSuccess;
    private TextView tvPhonenumber;
    private LinearLayout llNewphonenumber;
    private EditText etNewphonenumber;
    private LinearLayout llYangzhengma;
    private EditText etYanzhengma;
    private TextView tvGetyanzhengma;
    private Button btnConfirmpwd1;
    private String phoneNumber;
    private Button btnConfirmpwd2;
    private Button btnConfirmpwd3;
    private TextView tvGetnewyanzhengma;

    //倒计时数字
    private int seconds = 60;
    private int seconds2 = 60;


    // 接受端
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
                        tvGetyanzhengma.setText(String.format(string,seconds));


                        handler.sendEmptyMessageDelayed(1, 1000);
                    } else {
                        //移除所有的消息
                        handler.removeCallbacksAndMessages(null);
                        seconds = 60;
                        tvGetyanzhengma.setText(getCode);
                        tvGetyanzhengma.setTextColor(getResources().getColor(R.color.light_blue));
                        tvGetyanzhengma.setEnabled(true);
                    }
                    break;
                case 2:
                    if (seconds2 > 1) {
                        seconds2--;
                        tvGetyanzhengma.setText(String.format(string,seconds2));

                        handler.sendEmptyMessageDelayed(2, 1000);
                    } else {
                        //移除所有的消息
                        handler.removeCallbacksAndMessages(null);
                        seconds2 = 60;
                        tvGetnewyanzhengma.setText(getCode);
                        tvGetnewyanzhengma.setTextColor(getResources().getColor(R.color.light_blue));
                        tvGetnewyanzhengma.setEnabled(true);
                    }

                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取当前登陆的用户userid
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //根据userid获取用户信息
        getUserPhone();
    }

    /**
     * 获取当前用户的手机号
     */
    private void getUserPhone() {

        OkHttpUtils
                .get()
                .url(GlobalContent.GLOBAL_GET_PHONE)
                .addParams("userId", mUserId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        phoneNumber = rootBean.Data;
                        tvPhonenumber.setText(String.format(getResources().getString(R.string.current_phonenumber_number),phoneNumber));
                    }
                });
    }

    private void initView() {

        setBackButtonVisibility(View.VISIBLE);
        tv_title.setText(getResources().getString(R.string.replace_phone_number));

        ivJiantou1 = (ImageView) findViewById(R.id.iv_jiantou1);
        ivChange2 = (ImageView) findViewById(R.id.iv_change2);
        ivJiantou2 = (ImageView) findViewById(R.id.iv_jiantou2);
        ivChange3 = (ImageView) findViewById(R.id.iv_change3);
        tvSuccess = (TextView) findViewById(R.id.tv_success);
        tvPhonenumber = (TextView) findViewById(R.id.tv_phonenumber);
        llNewphonenumber = (LinearLayout) findViewById(R.id.ll_newphonenumber);
        etNewphonenumber = (EditText) findViewById(R.id.et_newphonenumber);
        llYangzhengma = (LinearLayout) findViewById(R.id.ll_yangzhengma);
        etYanzhengma = (EditText) findViewById(R.id.et_yanzhengma);
        tvGetyanzhengma = (TextView) findViewById(R.id.tv_getyanzhengma);
        tvGetnewyanzhengma = (TextView) findViewById(R.id.tv_getnewyanzhengma);

        btnConfirmpwd1 = (Button) findViewById(R.id.btn_confirmpwd1);
        btnConfirmpwd2 = (Button) findViewById(R.id.btn_confirmpwd2);
        btnConfirmpwd3 = (Button) findViewById(R.id.btn_confirmpwd3);

        tvGetyanzhengma.setOnClickListener(this);
        tvGetnewyanzhengma.setOnClickListener(this);
        btnConfirmpwd1.setOnClickListener(this);
        btnConfirmpwd2.setOnClickListener(this);
        btnConfirmpwd3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_getyanzhengma://获取验证
                tvGetyanzhengma.setText(String.format(getResources().getString(R.string.code_hint),seconds));
                tvGetyanzhengma.setTextColor(getResources().getColor(R.color.gray));
                tvGetyanzhengma.setEnabled(false);
                handler.sendEmptyMessageDelayed(1, 1000);
                getYanZhengMa();

                break;
            case R.id.tv_getnewyanzhengma:
                tvGetnewyanzhengma.setText(String.format(getResources().getString(R.string.code_hint),seconds));
                tvGetnewyanzhengma.setTextColor(getResources().getColor(R.color.gray));
                tvGetnewyanzhengma.setEnabled(false);
                handler.sendEmptyMessageDelayed(2, 1000);

                getNewYanZhengMa();//获取新手机号的验证码

                break;
            case R.id.btn_confirmpwd1:

                yanzhengPhone();//验证当前手机
                break;
            case R.id.btn_confirmpwd2:

                yanzhengNewPhone();//验证新手机
                break;
            case R.id.btn_confirmpwd3:
                //返回个人中心
                finish();
                break;
        }
    }

    /**
     * 验证当前手机
     */
    private void yanzhengPhone() {
        Map<String, String> params = new HashMap<>();
        try {
            params.put("Mobile", EncryptCode.encrypt(phoneNumber));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String code = etYanzhengma.getText().toString();
        CacheUtils.putString(ChangeTelephoneActivity.this, GlobalContent.CURRCODE, code);
        params.put("CheckCode", code);
        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBAL_YANZHENG_PHONE)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        //返回值  是否成功
                        String errCode = rootBean.ErrCode;
                        if ("0".equals(errCode)) {
                            showToast(getResources().getString(R.string.verify_succeed));
                            btnConfirmpwd1.setVisibility(View.GONE);
                            btnConfirmpwd2.setVisibility(View.VISIBLE);
                            tvPhonenumber.setVisibility(View.GONE);
                            llNewphonenumber.setVisibility(View.VISIBLE);
                            tvGetyanzhengma.setVisibility(View.GONE);
                            etYanzhengma.setText("");
                            tvGetnewyanzhengma.setVisibility(View.VISIBLE);
                            ivJiantou1.setImageResource(R.drawable.arrow_a);
                            ivChange2.setImageResource(R.drawable.jindu_b);

                        } else {
                            showToast(getResources().getString(R.string.verify_failure));
                        }
                    }
                });
    }

    /**
     * 验证新手机
     */
    private void yanzhengNewPhone() {
        Map<String, String> params = new HashMap<>();
        String number = etNewphonenumber.getText().toString();
        isPhone(number);
        String code = etYanzhengma.getText().toString();

        try {
            params.put("UserId", mUserId);
            params.put("NewCheckCode", code);
            params.put("NewMobile", EncryptCode.encrypt(number));
            params.put("CurrMobile", EncryptCode.encrypt(phoneNumber));
        } catch (Exception e) {
            e.printStackTrace();
        }
        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBAL_YANZHENG_NEW_PHONE)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        //返回值  是否成功
                        String errCode = rootBean.ErrCode;
                        if ("0".equals(errCode)) {
                            showToast(getResources().getString(R.string.new_phone_verify_succeed));
                            setShowView();
                        } else {
                            showToast(rootBean.ResponseMsg);
                        }
                    }
                });
    }

    private void setShowView() {
        btnConfirmpwd1.setVisibility(View.GONE);
        btnConfirmpwd2.setVisibility(View.GONE);
        llNewphonenumber.setVisibility(View.GONE);
        llYangzhengma.setVisibility(View.GONE);
        tvSuccess.setVisibility(View.VISIBLE);
        tvPhonenumber.setVisibility(View.VISIBLE);
        tvGetnewyanzhengma.setVisibility(View.GONE);
        tvGetyanzhengma.setVisibility(View.GONE);
        ivJiantou1.setImageResource(R.drawable.arrow_a);
        ivChange2.setImageResource(R.drawable.jindu_b);
        ivJiantou2.setImageResource(R.drawable.arrow_a);
        ivChange3.setImageResource(R.drawable.jindu_c);

        btnConfirmpwd3.setVisibility(View.VISIBLE);
    }

    /**
     * 获取当前手机的验证码
     */
    private void getYanZhengMa() {
        Map<String, String> params = new HashMap<>();
        params.put("UserId", mUserId);
        try {
            params.put("CurrMobile", EncryptCode.encrypt(phoneNumber));
        } catch (Exception e) {
            e.printStackTrace();
        }
        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBAL_GET_PHONE_CODE)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        //返回值  是否成功
                        String errCode = rootBean.ErrCode;
                        if (Integer.parseInt(errCode) == 0) {
                            showToast(getResources().getString(R.string.code_send_succeed));
                        } else {
                            showToast(rootBean.ResponseMsg);
                        }
                    }
                });
    }

    /**
     * 获取新手机号的验证码
     */
    private void getNewYanZhengMa() {
        Map<String, String> params = new HashMap<>();
        String newphoneNumber = etNewphonenumber.getText().toString();
        isPhone(newphoneNumber);
        String currcode = CacheUtils.getString(ChangeTelephoneActivity.this, GlobalContent.CURRCODE);
        try {
            params.put("UserId", mUserId);
            params.put("CurrMobile", EncryptCode.encrypt(phoneNumber));
            params.put("NewMobile", EncryptCode.encrypt(newphoneNumber));
            params.put("CurrCheckCode", currcode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBAL_GET_NEW_PHONE_CODE)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        //返回值  是否成功
                        String errCode = rootBean.ErrCode;
                        if (Integer.parseInt(errCode) == 0) {
                            showToast(getResources().getString(R.string.code_send_succeed));
                        } else {
                            showToast(rootBean.ResponseMsg);
                        }
                    }
                });

    }


    /**
     * 效验手机号码
     */
    private boolean isPhone(String phoneNumber) {

        //判断是否为空
        if (TextUtils.isEmpty(phoneNumber)) {
            showToast(getResources().getString(R.string.input_phone_number));
            return false;
        }
//        else {
//            //判断是否是手机号
//            Pattern compile = Pattern.compile(GlobalContent.PHONE);
//            Matcher matcher = compile.matcher(phoneNumber);
//            if (!matcher.find()) {
//                showToast(getResources().getString(R.string.input_phone_number_hint));
//                return false;
//
//            } else {
//                return true;
//            }
//        }
            return true;
    }

    /**
     * 点击屏幕空白区域隐藏软键盘（方法1）
     * <p>在onTouch中处理，未获焦点则隐藏</p>
     * <p>参照以下注释代码</p>
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
    public int initLayout() {
        return R.layout.activity_change_telephone;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
