package com.BFMe.BFMBuyer.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.NoTitleBaseActivity;
import com.BFMe.BFMBuyer.main.MainActivity;
import com.BFMe.BFMBuyer.utils.AppUtils;
import com.BFMe.BFMBuyer.utils.EncryptCode;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.PrefUtils;
import com.BFMe.BFMBuyer.utils.UiUtils;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.BFMe.BFMBuyer.utils.xpermission.listener.XPermissionsListener;
import com.BFMe.BFMBuyer.utils.xpermissions.XPermissions;
import com.bumptech.glide.Glide;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.io.File;

/**
 * Description: 版本更新页面
 * Copyright  : Copyright (c) 2016
 * Company    : 白富美(北京)网络科技有限公司
 * Author     : 王  可
 * Date       : 2016/7/12 10:50
 */
public class SplashActivity extends NoTitleBaseActivity {
    private static final int SHOW_UPDATE_DIALOG = 1;//升级弹窗
    private static final int ENTER_HOME = 2;//进入主页面
    private static final int NETWORK_ERROR = 3;//网络异常
    private String appUrl;
    private String appDesc;
    private int isUpgrade;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_UPDATE_DIALOG:
                    showAlertDialog();
                    break;
                case ENTER_HOME:
                    //进入主页面
                    enterLogin();
                    break;
                case NETWORK_ERROR:
                    //进入主页面
                    enterLogin();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBottomUIMenu();
        XPermissions.init(this);
        isUpData();
        initView();
        //证书的拷贝
        EncryptCode.copyDb("BFMeBuyer.der.cer", this);
    }

    private void initView() {
        ImageView iv_splash = (ImageView) findViewById(R.id.iv_splash);
        Glide.with(this).load(R.drawable.splash).skipMemoryCache(true).into(iv_splash);
    }


    @Override
    public int initLayout() {
        return R.layout.activity_splash;
    }


    /**
     * 根据服务器检测版本更新
     */
    public void isUpData() {
        OkHttpUtils
                .get()
                .url(GlobalContent.GLOBAL_VISION_CHECKED)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                        showToast("网络错误");
                        handler.sendEmptyMessageDelayed(NETWORK_ERROR, 2500);
                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("版本更新接口==="+response);
                        try {
                            JSONObject jsonRoot = new JSONObject(response);
                            String str = jsonRoot.getString("Data");
                            if (Integer.parseInt(jsonRoot.getString("ErrCode")) != 0) {
                                showToast("网络错误");
                                handler.sendEmptyMessageDelayed(NETWORK_ERROR, 2500);
                                return;
                            }

                            JSONObject jsonObject = new JSONObject(str);
                            String js = jsonObject.getString("Version");
                            JSONObject json = new JSONObject(js);
                            appUrl = json.getString("AppUrl");
                            appDesc = json.getString("UpgradeDesc");
                            isUpgrade = json.getInt("IsUpgrade");
                            String appVersion = json.getString("Version").replace(".", "");
                            Logger.e("appVersion" + appVersion + "isUpgrade" + isUpgrade + "appUrl" + appUrl);
                            if (getVersionCode() < Integer.parseInt(appVersion)) {
                                if (isUpgrade == 3) {//强制更新
                                    ForceUpdateDialog();
                                } else {
                                    handler.sendEmptyMessage(SHOW_UPDATE_DIALOG);
                                }

                            } else {
                                //没有更新,直接跳到主页面
                                handler.sendEmptyMessageDelayed(ENTER_HOME, 2500);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 强制更新APK版本
     */

    private void ForceUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.hint));
        builder.setMessage(appDesc);
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.update_now),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //立即下载
                        setPermissions();
                    }
                });

        builder.setNegativeButton(getString(R.string.out),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                });

        builder.show();

    }

    /**
     * 弹窗更新提示
     */
    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.hint));
        builder.setMessage(appDesc);
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.update_now),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setPermissions();
                    }
                });
        if (isUpgrade == 3) {
            builder.setNegativeButton(getString(R.string.out),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    });
        } else {
            builder.setNegativeButton(getString(R.string.talk_to_you_later),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            enterLogin();
                        }
                    });
        }

        //用户取消弹窗的监听
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                enterLogin();
            }
        });
        builder.show();
    }

    /**
     * 下载app
     */
    public void downLoadNewApp() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle(getString(R.string.is_download));
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//水平方向展示进度的进度条
        dialog.setCancelable(false);
        dialog.show();

        if (!UiUtils.hasSdcard()) {
            showToast(getString(R.string.memory_card_is_not_available));
            return;
        }
        String filePath = Environment.getExternalStorageDirectory().getAbsoluteFile().getPath();
        OkHttpUtils
                .get()
                .url(appUrl)
                .build()
                .execute(new FileCallBack(filePath, "bfmeBuyer.apk") {
                    @Override
                    public void inProgress(float progress) {
                        int percent = (int) (progress * 100);
                        dialog.setProgress(percent);
                    }

                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                        showToast(getString(R.string.download_failure_hint));
                        dialog.cancel();
                        showAlertDialog();
                    }

                    @Override
                    public void onResponse(File response) {
                        //安装apk
                        AppUtils.installApp(SplashActivity.this,response,"com.BFMe.BFMBuyer.fileprovider");
                    }
                });
    }


    /**
     * 当用户取消安装时, 就会回到此回调方法中, 在这里跳到主页面
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        enterLogin();
    }

    /**
     * 进入主页面
     */
    public void enterLogin() {
        boolean isFirst = PrefUtils.getBoolean(SplashActivity.this, "config", false);
        if (!isFirst) {
            PrefUtils.putBoolean(SplashActivity.this, "config", true);
            //第一次进来跳进引导页面
            Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
            startActivity(intent);
            finish();
            startAnim();
        } else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
            startAnim();
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        XPermissions.handlerPermissionResult(requestCode, permissions, grantResults);
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * 获取当前系统的版本号码
     */
    private int getVersionCode() {
        //获取包管理器
        PackageManager pm = getPackageManager();
        try {
            //获取包信息
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);//参1:当前app的包名;参2:标记,不获取额外信息的话传0
            int versionCode = packageInfo.versionCode;//版本号

            return versionCode;
        } catch (Exception e) {
            //没有找到包名的异常
            e.printStackTrace();
        }
        return 1;
    }

    private void setPermissions() {
        XPermissions
                .requestPermissions()
                .setRequestCode(203)
                .setShouldShow(false)
                .setPermissions(new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                })
                .setOnXPermissionsListener(new XPermissionsListener() {
                    @Override
                    public void onXPermissions(int requestCode, int resultCode) {
                        if (resultCode == XPermissions.PERMISSION_SUCCESS) {
                            downLoadNewApp();
                        } else {
                            showToast("您没有提供相应的权限!");
                            System.exit(0);
                        }
                    }
                }).builder();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
