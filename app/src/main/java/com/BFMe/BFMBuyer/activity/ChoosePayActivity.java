package com.BFMe.BFMBuyer.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.javaBean.CartBean;
import com.BFMe.BFMBuyer.javaBean.GoPayBean;
import com.BFMe.BFMBuyer.javaBean.HSCodeTileBean;
import com.BFMe.BFMBuyer.javaBean.PayResult;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.main.MainActivity;
import com.BFMe.BFMBuyer.utils.CacheUtils;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.LocalUtils;
import com.BFMe.BFMBuyer.utils.Utils;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.BFMe.BFMBuyer.wxapi.WXPayEntryActivity;
import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import okhttp3.Request;

import static com.zhy.http.okhttp.OkHttpUtils.post;

/**
 * 选择支付方式页面
 */
public class ChoosePayActivity extends IBaseActivity implements View.OnClickListener {

    private CheckBox cbCheckZhifubao;
    private CheckBox cbCheckWeixin;
    private IWXAPI api;
    private GoPayBean goPayBean;
    private TextView tv_zhifu_totalPrice;
    private TextView tv_hs_code;
    private Gson gson;
    private String orderId;//订单编号

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
            /**
             对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
             */
            String resultInfo = payResult.getResult();// 同步返回需要验证的信息
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为9000则代表支付成功
            if (TextUtils.equals(resultStatus, "9000")) {
                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                showLoadDialog(getResources().getString(R.string.wait_pay_result));
                getOrderState();
            } else if(TextUtils.equals(resultStatus, "6001")) {
                showToast(getResources().getString(R.string.deal_cancel));
                goToOrder(1);
            }else{
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                showLoadDialog(getResources().getString(R.string.wait_pay_result));
                getOrderState();
            }
        };
    };
    private ProgressDialog progressDialog;
    private CheckBox cbCheckOffline;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (gson == null) {
            gson = new Gson();
        }
        initView();
        initData();

        //把应用注册到微信
        api = WXAPIFactory.createWXAPI(this, GlobalContent.APP_ID);
        api.registerApp(GlobalContent.APP_ID);
    }

    /**
     * 初始化数据
     */
    private void initData() {

        //获取最新的购物车信息
        getNetShoppingCartNumber();
        isPay = true;
        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");
        boolean isHasHSCode = intent.getBooleanExtra("HasHSCode",false);
        //订单金额
        tv_zhifu_totalPrice.setText(getString(R.string.money_type) + intent.getStringExtra("totalPrice"));

        if(isHasHSCode) {//是否包含南沙保税仓的商品
            getHSCodeTitle();
        }
    }

    private void getHSCodeTitle() {
        OkHttpUtils
                .get()
                .url(GlobalContent.GET_HS_CODE_TITLE)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("保税仓提示=="+response);
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if(rootBean.ErrCode.equals("0")) {
                            HSCodeTileBean data = mGson.fromJson(rootBean.Data, HSCodeTileBean.class);
                            tv_hs_code.setVisibility(View.VISIBLE);
                            tv_hs_code.setText(data.getTitle());
                        }
                    }
                });
    }

    /**
     * 获取购物车商品数量
     */
    private void getNetShoppingCartNumber() {
        String userId = CacheUtils.getString(this, GlobalContent.USER);
        OkHttpUtils
                .get()
                .url(GlobalContent.GLOBSAL_GETCSRT_LIST)
                .addParams("UserId",userId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        RootBean rootBean = new Gson().fromJson(response, RootBean.class);

                        if(rootBean.ErrCode.equals("0")) {
                            CartBean cartBean = new Gson().fromJson(rootBean.Data, CartBean.class);
                            getCartNumber(cartBean);
                        }
                    }
                });
    }

    /**
     * 计算购物车商品数量
     * @param cartBean
     */
    private void getCartNumber(CartBean cartBean) {
        List<CartBean.CartItemsBean> cartItems = cartBean.getCartItems();
        int number = 0;
        for(int i = 0; i < cartItems.size(); i++) {
            for(int j = 0; j < cartItems.get(i).getCartItems().size(); j++) {
                number ++;
            }
        }
        CacheUtils.putString(ChoosePayActivity.this, GlobalContent.CART_NUMBER, String.valueOf(number));
        sendCartNumberChangeBroadcast();
    }

    /**
     * 发送购物车商品数量改变的广播
     */
    private void sendCartNumberChangeBroadcast() {
        Intent intent = new Intent("com.BFMe.BFMBuyer.SHOPPING_CART_NUMBER");
        sendBroadcast(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goToOrder(1);
                break;
            case R.id.btn_pay:
                if (Utils.isFastDoubleClick()) {
                    return;
                }else {
                    goPay();
                }
                break;
            default:
                break;
        }
    }

    private void goPay() {
        showLoadDialog(getResources().getString(R.string.get_order_info));
        //判断是微信支付还是支付宝支付
        if (cbCheckZhifubao.isChecked()) {
            //支付宝支付
            getPayInfo("2");
        }else if (cbCheckWeixin.isChecked()) {
            //微信支付
            getPayInfo("1");
        }else if(cbCheckOffline.isChecked()) {
            startActivity(new Intent(this,UploadVoucherActivity.class).putExtra("orderId",orderId).putExtra("money",tv_zhifu_totalPrice.getText().toString().trim()));
            dissLoadDialog();
            finish();
            startAnim();
        }else {
            showToast(getResources().getString(R.string.select_pay_way));
            dissLoadDialog();
        }
    }

    /**
     * 显示进度条对话框
     * @param message 内容
     */
    private void showLoadDialog(String message){
        if(progressDialog == null) {
            progressDialog = new ProgressDialog(ChoosePayActivity.this);
        }
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    /**
     * 取消进度条对话框
     */
    private void dissLoadDialog(){
        if(progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
    /**
     * 获取支付信息
     * @param type
     */
    private void getPayInfo(final String type) {
        post()
                .url(GlobalContent.GLOBAL_GO_PAY)
                .addParams("OrderId",orderId)
                .addParams("Type",type)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                    }

                    @Override
                    public void onResponse(String response) {
                        dissLoadDialog();
                        RootBean rootBean = gson.fromJson(response, RootBean.class);
                        if(rootBean.ErrCode.equals("0")) {
                            if(type.equals("1")) {//微信支付
                                goPayBean = gson.fromJson(rootBean.Data, GoPayBean.class);
                                WeinXinPay();
                            }else if(type.equals("2")) {//支付宝支付
                                aliPay(rootBean.Data);
                            }
                        }else {
                            Toast.makeText(ChoosePayActivity.this, rootBean.ResponseMsg, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private int temp;
    /**
     * 获取订单状态
     */
    private void getOrderState() {

        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBAL_PAY_STATE)
                .addParams("OrderId",orderId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        getOrderState();
                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e( "获取订单状态=="+response);
                        RootBean rootBean = gson.fromJson(response, RootBean.class);
                        if(rootBean.ErrCode.equals("0")) {
                            try {
                                JSONObject jsonObject = new JSONObject(rootBean.Data);
                                String orderStatus = jsonObject.getString("OrderStatus");
                                if("2".equals(orderStatus)) {
                                    dissLoadDialog();
                                    showSucceedDialog();
                                    temp = 0;
                                }else {
                                    if(temp >= 10) {
                                        dissLoadDialog();
                                        showDefeatedDialog();
                                    }else {
                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }finally {
                                            getOrderState();
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }finally {
                                temp ++;
                            }
                        }else {
                            Toast.makeText(ChoosePayActivity.this, rootBean.ResponseMsg, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    /**
     * 支付宝支付
     * @param data
     */
    private void aliPay(String data) {
        final String orderInfo = data;   // 订单信息

        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(ChoosePayActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo,true);

                Message msg = new Message();
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 微信支付
     */
    private void WeinXinPay() {

        if (!isWXAppInstalledAndSupported(api)) {
            showToast(getResources().getString(R.string.is_WXApp_Installed));
            return;
        }

        if (goPayBean != null) {
            PayReq payReq = new PayReq();
            payReq.appId = GlobalContent.APP_ID;
            payReq.partnerId = goPayBean.getPayInfo().getPartnerid();
            payReq.prepayId = goPayBean.getPayInfo().getPrepayid();
            payReq.packageValue = "Sign=WXPay";
            payReq.nonceStr = goPayBean.getPayInfo().getNoncestr();
            payReq.timeStamp = goPayBean.getPayInfo().getTimestamp();
            payReq.sign = goPayBean.getPayInfo().getSign();

            //发起请求
            api.sendReq(payReq);
        }
    }

    /**
     * 判断支付是否成功
     */
    @Override
    protected void onRestart() {
        super.onRestart();

        if(WXPayEntryActivity.getIsPay()) {
            //判断是否支付成功
            int isPaySucceed = WXPayEntryActivity.getErrCode();
            if (isPaySucceed == 0) {
                showSucceedDialog();
            } else if(isPaySucceed == -2) {
                showToast(getResources().getString(R.string.deal_cancel));
                goToOrder(1);
            }else{
                showDefeatedDialog();
            }
        }

    }

    /**
     * 支付失败的对话框
     */
    private void showDefeatedDialog() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        AlertDialog.Builder payDialog = new AlertDialog.Builder(this);
        payDialog.setView(R.layout.dialog_pay_defeated);
        payDialog.setCancelable(false);
        payDialog.setPositiveButton(getResources().getString(R.string.back_home),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        //跳转到首页
                        CacheUtils.putBoolean(ChoosePayActivity.this,"IsFromCar",true);
                        Intent intent = new Intent(ChoosePayActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        startAnim();
                    }
                });

        payDialog.setNegativeButton(getResources().getString(R.string.anew_pay),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        temp = 0;
                        goPay();
                    }
                });

        payDialog.show();
    }

    /**
     * 支付成功的对话框
     */
    private void showSucceedDialog() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        AlertDialog.Builder payDialog =
                new AlertDialog.Builder(ChoosePayActivity.this);
        payDialog.setView(R.layout.dialog_pay_succeed);
        payDialog.setCancelable(false);
        payDialog.setPositiveButton(getResources().getString(R.string.look_order),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //跳转至待发货界面
                        goToOrder(2);
                    }
                });
        payDialog.setNegativeButton(getResources().getString(R.string.back_home),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CacheUtils.putBoolean(ChoosePayActivity.this,"IsFromCar",true);
                        Intent intent = new Intent(ChoosePayActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        // 显示
        payDialog.show();
    }

    /**
     * 判断是否安装微信客户端
     *
     * @param api IWXAPI
     * @return 返回ture or false
     */
    private static boolean isWXAppInstalledAndSupported(IWXAPI api) {

        return api.isWXAppInstalled() && api.isWXAppSupportAPI();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            goToOrder(1);
            return true;
        }
        return super.onKeyDown(keyCode, event);
        
    }

    @Override
    public int initLayout() {
        return R.layout.activity_choose_pay;
    }

    /**
     * 跳转到订单页面
     * @param state 状态
     */
    private void goToOrder(int state) {
        //跳转至待发货界面
        Intent intentsend = new Intent(ChoosePayActivity.this, MyOrderActivity.class);
        intentsend.putExtra("flag", state);
        startActivity(intentsend);
        finish();
        startAnim();
    }

    /**
     * 初始化布局
     */
    private void initView() {
        setBackButtonVisibility(View.VISIBLE);
        tv_title.setText(getResources().getString(R.string.select_pay_way));

        cbCheckZhifubao = (CheckBox) findViewById(R.id.cb_check_zhifubao);
        cbCheckWeixin = (CheckBox) findViewById(R.id.cb_check_weixin);
        Button btnPay = (Button) findViewById(R.id.btn_pay);
        tv_zhifu_totalPrice = (TextView) findViewById(R.id.tv_zhifu_totalPrice);
        tv_hs_code = (TextView)findViewById(R.id.tv_hs_code);
        cbCheckOffline = (CheckBox) findViewById(R.id.cb_check_offline);
        RelativeLayout rl_offline = (RelativeLayout) findViewById(R.id.rl_offline);
        RelativeLayout rl_check_weixin = (RelativeLayout) findViewById(R.id.rl_check_weixin);
        RelativeLayout rl_check_zhifubao = (RelativeLayout) findViewById(R.id.rl_check_zhifubao);


        if(!LocalUtils.isLocalMainland()){//如果不是大陆地区就让他使用香港服务器
            rl_offline.setVisibility(View.VISIBLE);
            rl_check_weixin.setVisibility(View.GONE);
            rl_check_zhifubao.setVisibility(View.GONE);

            cbCheckZhifubao.setChecked(false);
            cbCheckWeixin.setChecked(false);
            cbCheckOffline.setChecked(true);
        }


        btnPay.setOnClickListener(this);

        cbCheckWeixin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbCheckZhifubao.setChecked(false);
                }
            }
        });
        cbCheckZhifubao.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbCheckWeixin.setChecked(false);
                }
            }
        });

        findViewById(R.id.rl_check_weixin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbCheckWeixin.setChecked(true);
            }
        });
        findViewById(R.id.rl_check_zhifubao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbCheckZhifubao.setChecked(true);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
