package com.BFMe.BFMBuyer.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.javaBean.HelpArticleBean;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

public class HelpDetailsActivity extends IBaseActivity {

    private ImageView iv_help_data_left;
    private TextView tv_help_data_title;
    private WebView wv_help_content;
    private ProgressBar pb_help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_help_details;
    }

    private void initData() {
        //获取传递过来的数据
        int help_id = getIntent().getIntExtra("help_id", 0);

        OkHttpUtils
                .get()
                .url(GlobalContent.GLOBAL_HELP_ARTICLE)
                .addParams("id",help_id+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        //解析返回的数据
                        RootBean rootBean = mGson.fromJson(response,RootBean.class);
                        String data = rootBean.Data;
                        if(rootBean.ErrCode.equals("0")){
                            //解析Data
                            HelpArticleBean helpArticleBean = mGson.fromJson(data, HelpArticleBean.class);
                            showContent(helpArticleBean);
                        }

                    }
                });
    }

    /**
     * 显示内容
     * @param helpArticleBean
     */
    private void showContent(HelpArticleBean helpArticleBean) {
        //设置title
        tv_title.setText(helpArticleBean.getTitle());

        //显示内容
        if(helpArticleBean.isIsRelease()) {

            showWebView(helpArticleBean);

        }
    }

    private void showWebView(HelpArticleBean helpArticleBean) {
        String url = "<style type=\"text/css\">img {width:100% !important;height:auto;}</style>";
        //支持javascript
        wv_help_content.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        wv_help_content.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        wv_help_content.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        wv_help_content.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        wv_help_content.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wv_help_content.getSettings().setLoadWithOverviewMode(true);


        //webView加载html代码
        wv_help_content.setWebViewClient(new WebViewClient());
        wv_help_content.getSettings().setDefaultTextEncodingName("utf-8");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            wv_help_content.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            wv_help_content.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
        wv_help_content.loadData(getHtmlData(url + helpArticleBean.getContent()), "text/html; charset=utf-8", "utf-8");
        wv_help_content.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activity和Webview根据加载程度决定进度条的进度大小
                // 当加载到100%的时候 进度条自动消失
                pb_help.setProgress(progress * 100);
                if (progress == 100) {
                    pb_help.setVisibility(View.GONE);
                }
            }
        });
    }

    private String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }
    private void initView() {
        setBackButtonVisibility(View.VISIBLE);
        wv_help_content = (WebView)findViewById(R.id.wv_help_content);
        pb_help = (ProgressBar)findViewById(R.id.pb_help);

    }
}
