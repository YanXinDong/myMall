package com.BFMe.BFMBuyer.ugc.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.activity.LoginActivity;
import com.BFMe.BFMBuyer.base.NoTitleBaseActivity;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.ugc.bean.SubTopic;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.bumptech.glide.Glide;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import butterknife.OnClick;
import okhttp3.Request;

/**
 * 话题分类信息页面
 */
public class TopicCategoryActivity extends NoTitleBaseActivity {

    private String mTopicCategoryId;

    private ImageView iv_topic_icon;
    private TextView tv_topic_info;
    private TextView tv_subscription;
    private TextView tv_participation;
    private CheckBox cb_subscription;
    private SubTopic.TopicsListBean topicsInfo;

    @OnClick(R.id.ib_finish)
    void close(){
        finish();
        bottomexitAnim();
    }

    @OnClick(R.id.cb_subscription)
    void subscription(){
        if (isLogin) {
            boolean checked = cb_subscription.isChecked();
            if (checked) {
                cb_subscription.setText(getString(R.string.subscription_ture));
            } else {
                cb_subscription.setText(getString(R.string.subscription));
            }
            if (topicsInfo.getIsSubscribe() == 1) {//之前订阅过
                if (checked) {
                    tv_subscription.setText(getString(R.string.subscription)+ " " + topicsInfo.getSubscribeCount());
                } else {
                    tv_subscription.setText(getString(R.string.subscription)+ " "  + (topicsInfo.getSubscribeCount() - 1));
                }
            } else {
                if (checked) {
                    tv_subscription.setText(getString(R.string.subscription) + " " + (topicsInfo.getSubscribeCount() + 1));
                } else {
                    tv_subscription.setText(getString(R.string.subscription) + " " + topicsInfo.getSubscribeCount());
                }
            }

            subscriptionTopic(topicsInfo.getId());
        } else {
            cb_subscription.setChecked(false);
            cb_subscription.setText(getString(R.string.subscription));
            startActivity(new Intent(TopicCategoryActivity.this, LoginActivity.class));
            startAnim();
        }
    }

    /**
     * 订阅子话题
     *
     * @param topicCategoryId 子话题ID
     */
    private void subscriptionTopic(long topicCategoryId) {
        OkHttpUtils
                .post()
                .addParams("userId", mUserId)
                .addParams("topicCategoryId", topicCategoryId + "")
                .url(GlobalContent.POST_SUBSCRIBE_TOPIC_CATEGORY)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("订阅子话题" + response);
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBottomUIMenu();

        initView();
        initData();
    }

    private void initView() {
        iv_topic_icon = (ImageView) findViewById(R.id.iv_topic_icon);
        tv_topic_info = (TextView) findViewById(R.id.tv_topic_info);
        tv_subscription = (TextView) findViewById(R.id.tv_subscription);
        tv_participation = (TextView) findViewById(R.id.tv_participation);
        cb_subscription = (CheckBox) findViewById(R.id.cb_subscription);

    }

    private void initData() {
        mTopicCategoryId = getIntent().getStringExtra(GlobalContent.TOPICS_ID);
        getSubTopic();
    }

    private void getSubTopic() {
        Map<String, String> params = new HashMap<>();
        if (isLogin) {
            params.put("userId", mUserId);
        }
        params.put("topicCategoryId", mTopicCategoryId);
        OkHttpUtils
                .post()
                .params(params)
                .url(GlobalContent.POST_PUBLISH_UGC_SUB_TOPIC)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("子话题头部信息==" + response);
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (rootBean.ErrCode.equals("0")) {
                            showData(mGson.fromJson(rootBean.Data, SubTopic.class));
                        } else {
                            showToast(rootBean.ResponseMsg);
                        }
                    }
                });
    }

    private void showData(SubTopic subTopic) {
        topicsInfo = subTopic.getTopicsList();
        Glide
                .with(mContext)
                .load(topicsInfo.getImageUrl())
                .into(iv_topic_icon);
        tv_topic_info.setText(topicsInfo.getContent());
        tv_subscription.setText(getString(R.string.subscription) + " "+ topicsInfo.getSubscribeCount());
        tv_participation.setText("参与 "+ topicsInfo.getParticipateCount());
        if(topicsInfo.getIsSubscribe() == 1) {
            cb_subscription.setChecked(true);
            cb_subscription.setText(getString(R.string.subscription_ture));
        }else {
            cb_subscription.setChecked(false);
            cb_subscription.setText(getString(R.string.subscription));
        }
    }

    @Override
    public int initLayout() {
        return R.layout.activity_topic_category;
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
}
