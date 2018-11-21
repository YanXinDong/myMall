package com.BFMe.BFMBuyer.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.zhy.http.okhttp.OkHttpUtils;

/**
 * Description：配置程序
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2016/11/11 12:19
 */
public class Config {

    private Dialog mDialog;

    private Activity mActivity;
    /**
     * 获取单利实例
     */
    private static Config INSTANCE = new Config();

    public static Config getInstance() {
        if(null ==INSTANCE){
            INSTANCE = new Config();
        }
        return INSTANCE;
    }

    public void setActivityState(Activity activity) {
        // 设置App只能竖屏显示
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mActivity = activity;
    }

    public void showProgress(Context context) {
        if(mDialog == null) {
            mDialog = new Dialog(context, R.style.Dialog);
        }
        View view =View.inflate(context, R.layout.progressbar, null);
        mDialog.setContentView(view);
        mDialog.show();
        mDialog.setCancelable(false);
        mDialog.setOnKeyListener(onKeyListener);
    }

    public void showProgress(Context context,String hint) {
        if(mDialog == null) {
            mDialog = new Dialog(context, R.style.Dialog);
        }
        View view =View.inflate(context, R.layout.progressbar, null);
        TextView textView = (TextView) view.findViewById(R.id.pb_text);
        textView.setText(hint);
        mDialog.setContentView(view);
        mDialog.show();
        mDialog.setCancelable(false);
        mDialog.setOnKeyListener(onKeyListener);
    }
    /**
     * 监听显示dialog时的返回键
     */
    private DialogInterface.OnKeyListener onKeyListener = new DialogInterface.OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                dismissProgress();
                //可以取消同一个tag的
                OkHttpUtils instance = OkHttpUtils.getInstance();
                instance.cancelTag(mActivity);

                if(null != mActivity && Utils.isMainActivityTop(mActivity)) {
                    mActivity.finish();
                }
            }
            return false;
        }
    };
    public void dismissProgress() {

        if ( null  != mDialog && mDialog.isShowing() ) {
            mDialog.dismiss();
            mDialog.setOnKeyListener(null);
            mActivity = null;
            mDialog = null;
        }
    }
}
