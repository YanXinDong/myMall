package com.BFMe.BFMBuyer.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.io.File;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/2/23 17:38
 */
public class AppUtils {
    private AppUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }
    /**
     * 获取打开App的意图
     *
     * @param context     上下文
     * @param packageName 包名
     * @return intent
     */
    private static Intent getLaunchAppIntent(Context context, String packageName) {
        return context.getPackageManager().getLaunchIntentForPackage(packageName);
    }


    /**
     * 判断App是否安装
     *
     * @param context     上下文
     * @param packageName 包名
     * @return {@code true}: 已安装<br>{@code false}: 未安装
     */
    public static boolean isInstallApp(Context context, String packageName) {
        return !StringUtils.isSpace(packageName) && getLaunchAppIntent(context, packageName) != null;
    }

    /**
     * 打开App
     *
     * @param context     上下文
     * @param packageName 包名
     */
    public static void launchApp(Context context, String packageName) {
        if (StringUtils.isSpace(packageName)) return;
        context.startActivity(getLaunchAppIntent(context, packageName));
    }

    /**
     * 安装App（支持7.0）
     *@param activity    activity
     * @param file      文件
     * @param authority 7.0及以上安装需要传入清单文件中的{@code <provider>}的authorities属性
     *                  <br>参看https://developer.android.com/reference/android/support/v4/content/FileProvider.html
     */
    public static void installApp(final Activity activity,final File file, final String authority) {
        if (!FileUtils.isFileExists(file)) return;
        activity.startActivity(IntentUtils.getInstallAppIntent(activity,file, authority));
    }
}
