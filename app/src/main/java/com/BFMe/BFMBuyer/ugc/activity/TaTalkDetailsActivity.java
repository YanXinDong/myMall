package com.BFMe.BFMBuyer.ugc.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.activity.LoginActivity;
import com.BFMe.BFMBuyer.base.NoTitleBaseActivity;
import com.BFMe.BFMBuyer.commodity.activity.ProducetDetailsActivity;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.share.SelectShareCategoryActivity;
import com.BFMe.BFMBuyer.ugc.adapter.TaTalkDetailsAdapter;
import com.BFMe.BFMBuyer.ugc.bean.TopicDetails;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.ScreenUtils;
import com.BFMe.BFMBuyer.utils.UiUtils;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.mob.MobSDK;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Request;

import static com.zhy.http.okhttp.OkHttpUtils.post;

/**
 * Ta说详情
 */
public class TaTalkDetailsActivity extends NoTitleBaseActivity implements View.OnClickListener {

    @BindView(R.id.rv_ta_talk_details)
    RecyclerView taTalkDetails;
    @BindView(R.id.et_add_comment)
    EditText et_add_comment;
    @BindView(R.id.rl_comment)
    RelativeLayout rl_comment;
    @BindView(R.id.rl_title)
    RelativeLayout RLTitle;

    private TaTalkDetailsAdapter mAdapter;
    private long mTopicId;//话题ID
    private float mScreenWidth;
    /**
     * 正常状态
     */
    private static final int STATE_NORMAL = 0;
    /**
     * 刷新状态
     */
    private static final int STATE_REFRESH = 1;

    private int state = STATE_NORMAL;//默认为正常状态

    //改变title透明度的高度
    private int rollHeight = UiUtils.dip2px(230);
    private String shareTitle;
    private String shareContent;
    private String shareImageUrl;
    private String shareUrl;
    @OnClick(R.id.iv_back)
    void back(){
        finish();
        exitAnim();
    }

    //分享至微信或朋友圈
    @OnClick(R.id.iv_title_right)
    void share(){
        MobSDK.init(this,"1b462d46b8900","514cfad75c1832fd14fb3b7ce97cba9d");
        Intent intent = new Intent(this, SelectShareCategoryActivity.class);
        intent.putExtra("shareTitle",shareTitle);
        intent.putExtra("shareContent",shareContent);
        intent.putExtra("shareImageUrl",shareImageUrl);
        intent.putExtra("shareUrl",shareUrl);
        Logger.e("shareTitle==="+shareTitle);
        Logger.e("shareContent==="+shareContent);
        Logger.e("shareImageUrl==="+shareImageUrl);
        Logger.e("shareUrl==="+shareUrl);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.anim_no);
    }
    @BindView(R.id.tv_title)
    TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isTranslucentStatusBar = false;
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    private void initData() {
        isShare = true;

        //h5页面跳转app接收传值
        Intent intent = getIntent();
        String action = intent.getAction();
        if (Intent.ACTION_VIEW.equals(action)) {
            Uri uri = intent.getData();
            if (uri != null) {
                mTopicId = Long.parseLong(uri.getQueryParameter("Id"));
            }
        } else {
            mTopicId = intent.getLongExtra(GlobalContent.TOPIC_ID, 0);
        }

        //获取屏幕宽度
        mScreenWidth = ScreenUtils.getScreenWidth(mContext);

        getNetData();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        state = STATE_REFRESH;
        getNetData();
    }

    private void getNetData() {
        showProgress();
        Map<String, String> params = new HashMap<>();
        if (isLogin) {
            params.put("userId", mUserId);
        }
        params.put("topicId", mTopicId + "");
        Logger.e("params=="+params.toString());
        post()
                .params(params)
                .url(GlobalContent.POST_SEARCH_UGC_TOPIC_DETIALS)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("talkDetails" + response);
                        dismissProgress();

                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (rootBean.ErrCode.equals("0")) {
                            TopicDetails topicDetails = mGson.fromJson(rootBean.Data, TopicDetails.class);
                            setData(topicDetails);
                        }
                    }
                });
    }

    private void setData(TopicDetails topicDetails) {
        tv_title.setText(topicDetails.getTitle());
        setShareInfo(topicDetails);

        if (topicDetails.getState() != 1) {//审核中与审核未通过
            rl_comment.setVisibility(View.GONE);
        }
        List<TopicDetails.TopicPhotosBean> topicPhotos = topicDetails.getTopicPhotos();

        //根据图片的宽度转换图片的宽高以适应屏幕
        for (int i = 0; i < topicPhotos.size(); i++) {
            float scale = mScreenWidth / topicPhotos.get(i).getImageWidth();
            topicPhotos.get(i).setImageWidth((int) mScreenWidth);
            topicPhotos.get(i).setImageHeight((int) (scale * topicPhotos.get(i).getImageHeight()));
        }

        switch (state) {
            case STATE_NORMAL:
                taTalkDetails.setLayoutManager(new LinearLayoutManager(TaTalkDetailsActivity.this));
                mAdapter = new TaTalkDetailsAdapter(this,topicDetails);
                taTalkDetails.setAdapter(mAdapter);
                break;
            case STATE_REFRESH://刷新
                if (mAdapter != null) {
                    mAdapter.clearData();
                    mAdapter.addData(topicDetails);
                }
                break;
        }

        setOnClickListener();

    }

    private void setShareInfo(TopicDetails topicDetails) {
        shareTitle = topicDetails.getTitle();
        shareContent = topicDetails.getContent();
        shareUrl = topicDetails.getShareLink();
        if (topicDetails.getTopicPhotos() != null && topicDetails.getTopicPhotos().size() > 0) {
            shareImageUrl = topicDetails.getTopicPhotos().get(0).getImageUrl();
        }
    }

    private void setOnClickListener() {
        mAdapter.setTaTalkOnClickListener(new TaTalkDetailsAdapter.TaTalkOnClickListener() {
            @Override
            public void userTopicOnClick(String userId) {
                Intent intent = new Intent(TaTalkDetailsActivity.this, UserInfoActivity.class);
                intent.putExtra(GlobalContent.USER_INFO_ID, userId);
                startActivity(intent);
                startAnim();
            }

            @Override
            public void userAttentionOnClick(View v, String concernUserId) {
                if (isLogin) {
                    CheckBox checkBox = (CheckBox) v;
                    userConcern(checkBox, concernUserId);
                } else {
                    startActivity(new Intent(TaTalkDetailsActivity.this, LoginActivity.class));
                    startAnim();
                }
            }

            @Override
            public void relevanceCommodityOnClick(String productId) {
                Intent intent = new Intent(TaTalkDetailsActivity.this, ProducetDetailsActivity.class);
                intent.putExtra("productId", productId);
                startActivity(intent);
                startAnim();
            }

            @Override
            public void topicLikeOnClick() {
                if (isLogin) {
                    paresTopic(mTopicId);
                } else {
                    startActivity(new Intent(TaTalkDetailsActivity.this, LoginActivity.class));
                    startAnim();
                }
            }

            @Override
            public void commentPraiseOnClick(String commentId) {
                if (isLogin) {
                    commentPraise(commentId);
                } else {
                    startActivity(new Intent(TaTalkDetailsActivity.this, LoginActivity.class));
                    startAnim();
                }
            }

            @Override
            public void allCommentOnClick() {
                Intent intent = new Intent(TaTalkDetailsActivity.this, AllCommentActivity.class);
                intent.putExtra(GlobalContent.TOPIC_ID, mTopicId);
                startActivity(intent);
                startAnim();
            }
        });

        taTalkDetails.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int mDistanceY;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //滑动的距离
                mDistanceY += dy;
                //当滑动的距离 <= view_bg宽度的时候，改变view_bg背景色的透明度，达到渐变的效果
                if (mDistanceY <= rollHeight) {
                    float scale = (float) mDistanceY / rollHeight;
                    float alpha = scale * 255;
                    RLTitle.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));

                    if(mDistanceY >= UiUtils.dip2px(115)) {
                        tv_title.setVisibility(View.VISIBLE);
                    }else {
                        tv_title.setVisibility(View.GONE);
                    }
                } else {
                    //上述虽然判断了滑动距离与view_bg宽度相等的情况，但是实际测试时发现，view_bg的背景色
                    RLTitle.setBackgroundResource(R.color.white);
                    tv_title.setVisibility(View.VISIBLE);
                }

                boolean aDefault = isDefault(taTalkDetails);//是否处于默认状态
                if(aDefault) {
                    //banner背景色设置为全透明
                    RLTitle.setBackgroundResource(R.color.transparent);
                    tv_title.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 是否是默认状态
     * @param recyclerView
     */
    private boolean isDefault(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        //获取可视的第一个view
        View topView = layoutManager.getChildAt(0);
        //获取与该view的顶部的偏移量用于判断是否是初始位置
        return topView.getTop() >= UiUtils.dip2px(0);
    }

    /**
     * 点赞评论
     *
     * @param commentId 评论ID
     */
    private void commentPraise(String commentId) {
        OkHttpUtils
                .post()
                .addParams("userId", mUserId)
                .addParams("topicCommentsId", commentId)
                .url(GlobalContent.POST_PARSE_TOPIC_COMMENTS)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("评论点赞" + response);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_btn_send:
                if (isLogin) {
                    proofreadComment();
                } else {
                    startActivity(new Intent(TaTalkDetailsActivity.this, LoginActivity.class));
                    overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
                }
                break;
        }
    }

    /**
     * 校验数据
     */
    private void proofreadComment() {
        String comment = et_add_comment.getText().toString();
        if (TextUtils.isEmpty(comment)) {
            showToast(getString(R.string.comment_content_no_empty));
        } else {
            publishTopicComment(comment);
        }
    }

    /**
     * 提交评论
     *
     * @param comment 评论内容
     */
    private void publishTopicComment(String comment) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", mUserId);
        params.put("topicId", mTopicId + "");
        params.put("content", comment);
        OkHttpUtils
                .post()
                .params(params)
                .url(GlobalContent.POST_PUBLISH_TOPIC_COMMENT)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (rootBean.ErrCode.equals("0")) {
                            showToast(getString(R.string.comment_succeed));
                            et_add_comment.setText("");
                            Intent intent = new Intent(TaTalkDetailsActivity.this, AllCommentActivity.class);
                            intent.putExtra(GlobalContent.TOPIC_ID, mTopicId);
                            startActivity(intent);
                            startAnim();
                        } else {
                            showToast(rootBean.ResponseMsg);
                        }
                    }
                });
    }

    /**
     * 话题点赞
     *
     * @param topicId 话题id
     */
    private void paresTopic(long topicId) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", mUserId);
        params.put("topicId", topicId + "");
        post()
                .params(params)
                .url(GlobalContent.POST_USER_PARES_TOPIC)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("点赞" + response);
                    }
                });
    }

    /**
     * 关注用户
     *
     * @param checkBox      关注按钮
     * @param concernUserId 关注的用户id
     */
    private void userConcern(final CheckBox checkBox, String concernUserId) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", mUserId);
        params.put("concernUserId", concernUserId);
        post()
                .params(params)
                .url(GlobalContent.POST_USER_CONCERN)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("关注" + response);
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (!rootBean.ErrCode.equals("0")) {
                            checkBox.setChecked(false);
                            showToast(rootBean.ResponseMsg);
                        }
                    }
                });
    }

    private void initView() {

        findViewById(R.id.img_btn_send).setOnClickListener(this);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_ta_talk_details;
    }

}
