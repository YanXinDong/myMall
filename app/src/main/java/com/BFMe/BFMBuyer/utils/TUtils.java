package com.BFMe.BFMBuyer.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by huangjunhui on 2017/1/18.9:36
 *
 * 自定义toast 实现toast时间
 */
public class TUtils {
    private static Toast toast;

    public static void showToast(Context context, String msg){
        if(toast == null){
            toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        }
        toast.setText(msg);
        toast.show();
    }
}
