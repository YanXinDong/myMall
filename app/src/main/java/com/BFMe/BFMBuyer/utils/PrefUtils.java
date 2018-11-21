package com.BFMe.BFMBuyer.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Description: sp的封装
 * Copyright  : Copyright (c) 2016
 * Company    : 白富美(北京)网络科技有限公司
 * Author     : 王  可
 * Date       : 2016/7/11 10:13
 */
public class PrefUtils {

    private static SharedPreferences mPref;

    private static SharedPreferences getPreferences(Context ctx) {
        if (mPref == null) {//只初始化一次
            mPref = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }

        return mPref;
    }

    public static boolean getBoolean(Context ctx, String key, boolean defValue) {

        return getPreferences(ctx).getBoolean(key, defValue);
    }

    public static void putBoolean(Context ctx, String key, boolean value) {

        getPreferences(ctx).edit().putBoolean(key, value).commit();
    }

    public static String getString(Context ctx, String key, String defValue) {

        return getPreferences(ctx).getString(key, defValue);
    }

    public static void putString(Context ctx, String key, String value) {

        getPreferences(ctx).edit().putString(key, value).commit();
    }

    public static int getInt(Context ctx, String key, int defValue) {

        return getPreferences(ctx).getInt(key, defValue);
    }

    public static void putInt(Context ctx, String key, int value) {

        getPreferences(ctx).edit().putInt(key, value).commit();
    }

    public static void remove(Context ctx, String key) {

        getPreferences(ctx).edit().remove(key).commit();
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

}
