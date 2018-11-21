package com.BFMe.BFMBuyer.share;

import android.content.Context;
import android.text.TextUtils;

import com.BFMe.BFMBuyer.utils.logger.Logger;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Description：分享封装类
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/9/28 14:46
 */
public class ShareHelp {


    private Context mContext;

    public ShareHelp(SelectShareCategoryActivity selectShareCategoryActivity) {
        mContext = selectShareCategoryActivity;
    }

    public void start(String shareTitle, String shareContent, String shareImageUrl, String shareImagePath, String shareUrl, ShareType type) {
        switch (type) {
            case WXSESSION://微信好友
            case WXTIMELINE://微信朋友圈
                startWX(shareTitle, shareContent, shareImageUrl, shareImagePath, shareUrl, type);
                break;
            case SINAWEIBO://新浪微博
                startSinaWeiBo(shareContent, shareImageUrl, shareImagePath, shareUrl);
                break;
        }
    }

    /**
     * 分享到新浪微博
     *
     * @param shareContent
     * @param shareImageUrl
     * @param shareUrl
     */
    private void startSinaWeiBo(String shareContent, String shareImageUrl, String shareImagePath, String shareUrl) {
        Platform.ShareParams sp = new Platform.ShareParams();
        if(shareContent != null && shareContent.length() >= 60) {
            //新浪微博只允许分享140字以内的字符串，为了不影响分享截取前60个字符并拼接链接
            shareContent = shareContent.substring(0,60) + "...";
        }
        sp.setText(shareContent + shareUrl);
        if (shareImagePath == null || shareImagePath.isEmpty()) {
            sp.setImageUrl(shareImageUrl);
        } else {
            sp.setImagePath(shareImagePath);
        }
        Logger.e("内容=="+shareContent + shareUrl);
        Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
        weibo.setPlatformActionListener(new MyPlatformActionListener()); // 设置分享事件回调
        // 执行图文分享
        weibo.share(sp);
    }

    private void startWX(String shareTitle,
                         String shareContent,
                         String shareImageUrl,
                         String shareImagePath,
                         String shareUrl,
                         ShareType type) {

        OnekeyShare oks = new OnekeyShare();
        if (TextUtils.isEmpty(shareTitle)) {
            oks.setTitle("Buy For Me");
        } else {
            oks.setTitle(shareTitle);
        }
        if (TextUtils.isEmpty(shareContent)) {
            oks.setText("Buy For Me");
        } else {
            oks.setText(shareContent);
        }

        if (shareImagePath == null || shareImagePath.isEmpty()) {
            oks.setImageUrl(shareImageUrl);
        } else {
            oks.setImagePath(shareImagePath);
        }
        oks.setUrl(shareUrl);

        Logger.e("shareTitle==" + shareTitle);
        Logger.e("shareContent==" + shareContent);
        Logger.e("shareImageUrl==" + shareImageUrl);
        Logger.e("shareUrl==" + shareUrl);
        switch (type) {
            case WXSESSION://微信好友
                oks.setPlatform(Wechat.NAME);
                break;
            case WXTIMELINE://微信朋友圈
                oks.setPlatform(WechatMoments.NAME);
                break;
        }
        oks.show(mContext);
    }

    private class MyPlatformActionListener implements PlatformActionListener {

        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            Logger.e("完成");
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            Logger.e("onError");
        }

        @Override
        public void onCancel(Platform platform, int i) {
            Logger.e("onCancel");
        }
    }
}
