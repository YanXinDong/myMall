package com.BFMe.BFMBuyer.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.Utils;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Description：微信帮助类
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/2/14 12:10
 */
public class WXShareHelp {

    /**
     * 网页类型的WX分享
     * @param context 上下文
     * @param url 网页链接
     * @param title 网页标题
     * @param describe 网页描述
     * @param bmp 网页缩略图
     * @param scene 分享到WX会话、朋友群、收藏
     */
    public void startShare(Context context,String url,String title,String describe,Bitmap bmp,int scene){
        Logger.e("startShare");
        IWXAPI api = registerIWXAPI(context);

        //初始化一个WXWebpageObject对象，填写url
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;

        //用WXWebpageObject对象初始化一个WXMediaMessage对象，填写标题，描述
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = describe;

//        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
//        bmp.recycle();
//        msg.thumbData = Utils.bmpToByteArray(bmp, true);
        msg.thumbData = Utils.bitmap2Bytes(bmp, Bitmap.CompressFormat.PNG);

        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");//transaction字段用于唯一标识一个请求
        req.message = msg;
        req.scene = scene;
        Logger.e("sendReq");
        api.sendReq(req);
        Logger.e("end");
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    @NonNull
    private IWXAPI registerIWXAPI(Context context) {
        IWXAPI api = WXAPIFactory.createWXAPI(context, GlobalContent.APP_ID,true);
        api.registerApp(GlobalContent.APP_ID);
        return api;
    }
}
