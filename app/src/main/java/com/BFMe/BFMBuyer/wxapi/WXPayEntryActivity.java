package com.BFMe.BFMBuyer.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 微信支付返回结果页面
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;
    private static int errCode;//是否支付成功
    private static boolean isPay;//是否是支付

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, GlobalContent.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        isPay = true;
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
                errCode = resp.errCode;
            Logger.e( "支付=="+resp.errCode);
            finish();
        }
    }

    /**
     * 判断支付是否成功
     * @return
     */
    public static int getErrCode() {

        return errCode;
    }

    /**
     * 是否是支付返回的结果
     * @return
     */
    public static boolean getIsPay(){
        return isPay;
    }
}