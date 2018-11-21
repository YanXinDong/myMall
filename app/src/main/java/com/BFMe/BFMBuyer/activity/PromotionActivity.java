package com.BFMe.BFMBuyer.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.commodity.activity.ProducetDetailsActivity;
import com.BFMe.BFMBuyer.shop.activity.ShopHomeActivity;
import com.BFMe.BFMBuyer.utils.ADFilterTool;
import com.BFMe.BFMBuyer.utils.AppUtils;
import com.BFMe.BFMBuyer.utils.UiUtils;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

/**
 * 图文促销活动详情页（非列表形式）
 */
public class PromotionActivity extends IBaseActivity {

    private WebView wv_content;
    private ProgressBar pb_promotion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        initData();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_promotion;
    }

    private void initData() {
        String url = getIntent().getStringExtra("url");
//        url = "http://m.baifomi.com/Page/223.html";
        if (url.equals("http://m.baifomi.com/pages/download_seller.html")) {
            tv_title.setText(getString(R.string.download));
        }
        showWebView(url);
    }

    private void showWebView(String url) {

        WebSettings settings = wv_content.getSettings();

        //支持javascript
        settings.setJavaScriptEnabled(true);
        //自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);

        wv_content.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activity和Webview根据加载程度决定进度条的进度大小
                // 当加载到100%的时候 进度条自动消失
                pb_promotion.setProgress(progress);
                if (progress == 100) {
                    pb_promotion.setVisibility(View.GONE);
                }
            }
        });

        wv_content.loadUrl(url);
        // 设置Web视图
        wv_content.setWebViewClient(new MyWebViewClient());
    }

    private boolean isClose;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String js = ADFilterTool.getClearAdDivJs(PromotionActivity.this);
            Log.v("adJs", js);
            if(null!=wv_content){
                wv_content.loadUrl(js);
            }

        }
    };

    // 监听 所有点击的链接，如果拦截到我们需要的，就跳转到相对应的页面。
    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (isClose) { //如果线程正在运行就不用重新开启一个线程了
                return;
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    isClose = true;
                    while (isClose) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessage(0x001);
                    }
                }
            }).start();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            isClose = false;
            handler.removeCallbacksAndMessages(null);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Logger.e("url=="+url);
            //这里进行url拦截
            if (url != null) {

                if (url.contains("http://www.baifomi.com/") || url.contains("http://m.baifomi.com/")) {

                    Intent intent = null;
                    if (url.contains("product") || url.contains("Product")) {
                        String id = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
                        intent = new Intent(PromotionActivity.this, ProducetDetailsActivity.class);
                        intent.putExtra("productId", id);

                    } else if (url.contains("shop") || url.contains("Shop")) {
                        String id = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
                        intent = new Intent(PromotionActivity.this, ShopHomeActivity.class);
                        intent.putExtra("shopId", id);

                    } else {
                        return super.shouldOverrideUrlLoading(view, url);
                    }

                    startActivity(intent);
                    startAnim();
                    return true;
                } else if (url.contains(".apk")) {
                    downLoadNewApp(url);
                } else {
                    return super.shouldOverrideUrlLoading(view, url);
                }
            }

            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    /**
     * 下载app
     *
     * @param url 下载卖家端的链接
     */
    public void downLoadNewApp(String url) {

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
                .url(url)
                .build()
                .execute(new FileCallBack(filePath, "bfmeBuyer.apk") {
                    @Override
                    public void inProgress(float progress) {
                        int percent = (int) (progress * 100);
                        dialog.setProgress(percent);//更新进度
                    }

                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                        //下载失败
                        showToast(getString(R.string.download_failure_hint));
                        dialog.cancel();
                    }

                    @Override
                    public void onResponse(File response) {
                        //安装apk
                        AppUtils.installApp(PromotionActivity.this,response,"com.BFMe.BFMBuyer.fileprovider");
                        dialog.cancel();
                    }
                });
    }

    private void initView() {
        setBackButtonVisibility(View.VISIBLE);
        wv_content = (WebView) findViewById(R.id.wv_content);
        pb_promotion = (ProgressBar) findViewById(R.id.pb_promotion);

    }

    @Override
    protected void onDestroy() {
        //销毁时销毁webView
        handler.removeCallbacksAndMessages(null);
        wv_content.destroy();
        wv_content = null;
        super.onDestroy();
    }
}
