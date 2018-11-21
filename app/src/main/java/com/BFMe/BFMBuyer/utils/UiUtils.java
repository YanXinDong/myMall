package com.BFMe.BFMBuyer.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.view.WindowManager;

import com.BFMe.BFMBuyer.application.MyApplication;

/**
 * Description:UI操作的封装
 * Copyright  : Copyright (c) 2016
 * Company    : 白富美(北京)网络科技有限公司
 * Author     : 王  可
 * Date       : 2016/7/12 12:13
 */
public class UiUtils {


    // 设置屏幕背景变暗
    public static void setScreenBgDarken(Activity activity) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 0.5f;
        lp.dimAmount = 0.5f;
        activity.getWindow().setAttributes(lp);
    }
    // 设置屏幕背景变亮
    public static void setScreenBgLight(Activity activity) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 1.0f;
        lp.dimAmount = 1.0f;
        activity.getWindow().setAttributes(lp);
    }

    /**
     * 获取sd卡的路径
     */
    public static String getSDcardPath() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        return path;
    }

    /**
     * 判断内存卡是否挂载
     */
    public static boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取手机屏幕的宽
     */
    public static int getScreenWidth(Context ctx) {
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }

    /**
     * 获取手机屏幕的高
     */
    public static int getScreenHeight(Context ctx) {
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }


    /**
     * dp到px
     * @param dp
     * @return
     */
    public static int dip2px(int dp) {
        return (int) (MyApplication.getContext().getResources().getDisplayMetrics().density
                * dp + 0.5);
    }

    /**
     * px到Dp
     * @param px
     * @return
     */
    public static int px2dp(int px) {
        return (int) (px / MyApplication.getContext().getResources().getDisplayMetrics().density);
    }

    /**
     * sp转px
     *
     * @param context 上下文
     * @param spValue sp值
     * @return px值
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
