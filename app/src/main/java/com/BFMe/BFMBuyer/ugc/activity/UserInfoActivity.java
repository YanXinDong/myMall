package com.BFMe.BFMBuyer.ugc.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.activity.LoginActivity;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.ugc.adapter.UserInfoAdapter;
import com.BFMe.BFMBuyer.ugc.bean.PersonalCenterInfo;
import com.BFMe.BFMBuyer.ugc.bean.UserPublishTopics;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Request;

import static com.zhy.http.okhttp.OkHttpUtils.post;

/**
 * 用户ugc信息主页
 */
public class UserInfoActivity extends IBaseActivity {

    @BindView(R.id.rv_user_info)
    XRecyclerView rv_user_info;

    //被查看的用户ID
    private String mSearchUserId;

    /**
     * 正常状态
     */
    private static final int STATE_NORMAL = 0;
    /**
     * 刷新状态
     */
    private static final int STATE_REFRESH = 1;
    /**
     * 局部刷新
     */
    private static final int STATE_ITEM_REFRESH = 3;
    /**
     * 加载更多状态
     */
    private static final int STATE_MORE = 2;

    private int state = STATE_NORMAL;//默认为正常状态

    private int pageNo = 1;//记载的页数
    private int pageSize = 10;//一页的数据
    private UserInfoAdapter mUserInfoAdapter;
    private PersonalCenterInfo mPersonalCenterInfo;

    //话题状态被查询话题的状态
    //（不包括已删除的）
    // 1：查询所有的话题
    // 2：查询审核通过的
    // 3：查询审核失败的
    // 4：查询正在审核的
    // 注释：进入别人的个人中心查询值为2的。
    // 进入自己的个人中心的就要分类查询。
    private String topicState = "2";
    private List<UserPublishTopics.TopicsListBean> mTopicsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    private void initData() {
        isShare = true;
        Intent intent = getIntent();
        String action = intent.getAction();
        if (Intent.ACTION_VIEW.equals(action)) {
            Uri uri = intent.getData();
            if (uri != null) {
                mSearchUserId = uri.getQueryParameter("Id");
            }
        } else {
            mSearchUserId = intent.getStringExtra(GlobalContent.USER_INFO_ID);
        }
        getNetData();
    }


    @Override
    protected void onResume() {
        super.onResume();
        setCartNumberVisibility(View.GONE);
    }

    private void getNetData() {
        showProgress();
        getPersonalCenter();
    }


    private void getPersonalCenter() {
        Map<String, String> params = new HashMap<>();
        if (isLogin) {
            params.put("userId", mUserId);
        }
        params.put("searchUserId", mSearchUserId);
        OkHttpUtils
                .post()
                .params(params)
                .url(GlobalContent.POST_PERSONAL_CENTER)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("个人话题主页信息==" + response);
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (rootBean.ErrCode.equals("0")) {
                            mPersonalCenterInfo = mGson.fromJson(rootBean.Data, PersonalCenterInfo.class);
                            if (mPersonalCenterInfo != null && state != STATE_ITEM_REFRESH) {
                                getUserPublishTopics();
                            }
                            validationData();
                        }

                    }
                });
    }

    private void getUserPublishTopics() {
        Map<String, String> params = new HashMap<>();
        if (isLogin) {
            params.put("userId", mUserId);
        }
        params.put("searchUserId", mSearchUserId);
        params.put("pageSize", pageSize + "");
        params.put("pageNo", pageNo + "");
        if (mPersonalCenterInfo.getIsOwn() == 1) {//是本人
            topicState = "1";
        } else {
            topicState = "2";
        }
        params.put("topicState", topicState);
        post()
                .params(params)
                .url(GlobalContent.POST_USER_PUBLISH_TOPICS)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("TAG", "个人话题主页话题列表==" + e.toString());
                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("个人话题主页话题列表==" + response);
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (rootBean.ErrCode.equals("0")) {
                            UserPublishTopics userPublishTopics = mGson.fromJson(rootBean.Data, UserPublishTopics.class);
                            mTopicsList = userPublishTopics.getTopicsList();
                            validationData();
                        }
                    }
                });
    }

    /**
     * 效验数据
     */
    private void validationData() {
        if (mPersonalCenterInfo != null && mTopicsList != null) {
            showData();
            dismissProgress();
        }
    }

    private void showData() {
        setShareInfo();

        tv_title.setText(mPersonalCenterInfo.getUserName());
        switch (state) {
            case STATE_NORMAL:
                rv_user_info.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                mUserInfoAdapter = new UserInfoAdapter(this,mPersonalCenterInfo, mTopicsList);
                rv_user_info.setAdapter(mUserInfoAdapter);
                if (mTopicsList.size() < 10) {
                    rv_user_info.setNoMore(true);
                }
                break;
            case STATE_REFRESH:
                state = STATE_NORMAL;
                rv_user_info.refreshComplete();
                if (mTopicsList.size() < 10) {
                    rv_user_info.setNoMore(true);
                }

                if (mUserInfoAdapter != null) {
                    mUserInfoAdapter.cleanData();
                    mUserInfoAdapter.addData(mPersonalCenterInfo, mTopicsList);
                }

                break;
            case STATE_ITEM_REFRESH:
                state = STATE_NORMAL;
                if (mUserInfoAdapter != null) {
                    mUserInfoAdapter.addHeadData(mPersonalCenterInfo);
                }
                break;
            case STATE_MORE:
                state = STATE_NORMAL;
                rv_user_info.loadMoreComplete();

                if (mUserInfoAdapter != null) {
                    if (mTopicsList.size() < 10) {
                        rv_user_info.setNoMore(true);
                        mUserInfoAdapter.addData(mUserInfoAdapter.getItemCount() - 2, mTopicsList);
                    } else {
                        mUserInfoAdapter.addData(mUserInfoAdapter.getItemCount() - 2, mTopicsList);
                    }
                }
                break;

        }

        setListener();

    }

    private void setShareInfo() {
        shareTitle = mPersonalCenterInfo.getUserName();
        shareContent = mPersonalCenterInfo.getIntroduceMyself();
        shareUrl = mPersonalCenterInfo.getUserShareLink();
        shareImageUrl = mPersonalCenterInfo.getImageUrl();
    }

    private void setListener() {
        mUserInfoAdapter.setUserCenterOnClickListener(new UserInfoAdapter.UserCenterOnClickListener() {
            @Override
            public void attentionOnClick(View v) {
                CheckBox attention = (CheckBox) v;
                if (isLogin) {
                    if (attention.isChecked()) {
                        attention.setText(getString(R.string.attention_ture));
                    } else {
                        attention.setText(getString(R.string.notice_total));
                    }
                    userConcern(attention);
                } else {
                    attention.setChecked(false);
                    attention.setText(getString(R.string.notice_total));
                    startActivity(new Intent(UserInfoActivity.this, LoginActivity.class));
                    overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
                }

            }

            @Override
            public void topicLikeOnClick(View v, UserPublishTopics.TopicsListBean topicInfo) {
                CheckBox box = (CheckBox) v;
//                boolean checked = box.isChecked();
                if (isLogin) {

//                    if (topicInfo.getIsPrase() == 1) {//之前赞过
//                        if (checked) {
//                            box.setText(topicInfo.getParseCount() + "");
//                        } else {
//                            box.setText(topicInfo.getParseCount() - 1 + "");
//                        }
//                    } else {
//                        if (checked) {
//                            box.setText(topicInfo.getParseCount() + 1 + "");
//                        } else {
//                            box.setText(topicInfo.getParseCount() + "");
//                        }
//                    }
                    paresTopic(topicInfo.getId());
                } else {
                    box.setChecked(false);
                    startActivity(new Intent(UserInfoActivity.this, LoginActivity.class));
                    startAnim();
                }
            }

            @Override
            public void detailsOnClick(long topicId) {
                Intent intent = new Intent(UserInfoActivity.this, TaTalkDetailsActivity.class);
                intent.putExtra(GlobalContent.TOPIC_ID, topicId);
                startActivity(intent);
                startAnim();
            }

            @Override
            public void sortTopicOnClick() {
                Intent intent = new Intent(UserInfoActivity.this, TopicStatusActivity.class);
                startActivity(intent);
                startAnim();
            }

            @Override
            public void showUserIcon(String imageUrl, View v) {
                if (Build.VERSION.SDK_INT < 21) {
                    Intent intent = new Intent(UserInfoActivity.this, UserIconActivity.class);
                    intent.putExtra("imageUrl",imageUrl);
                    startActivity(intent);
                } else {
                    UserIconActivity.start(UserInfoActivity.this, v,imageUrl);
                }
            }

        });

        rv_user_info.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                state = STATE_REFRESH;
                mPersonalCenterInfo = null;
                mTopicsList = null;
                getNetData();
            }

            @Override
            public void onLoadMore() {
                pageNo += 1;
                state = STATE_MORE;
                getUserPublishTopics();
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
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (rootBean.ErrCode.equals("0")) {
                            itemRefresh();
                        }
                    }
                });
    }

    /**
     * 刷新头部数据
     */
    private void itemRefresh() {
        state = STATE_ITEM_REFRESH;
        getPersonalCenter();
    }

    /**
     * 关注用户
     *
     * @param attention
     */
    private void userConcern(final CheckBox attention) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", mUserId);
        params.put("concernUserId", mSearchUserId);
        Log.e("TAG", "params==" + params);
        post()
                .params(params)
                .url(GlobalContent.POST_USER_CONCERN)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Logger.e("关注" + e.toString());
                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("关注" + response);
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (rootBean.ErrCode.equals("0")) {
                            itemRefresh();
                        } else {
                            attention.setText(getString(R.string.notice_total));
                            attention.setChecked(false);
                            showToast(rootBean.ResponseMsg);
                        }

                    }
                });
    }

    private void initView() {
        setBackButtonVisibility(View.VISIBLE);
        setChartButtonVisibility(View.VISIBLE);
        iv_title_right.setImageResource(R.drawable.icon_share);
        rv_user_info.setFootViewText(getString(R.string.load), getString(R.string.no_more_data));
    }

    @Override
    public int initLayout() {
        return R.layout.activity_user_info;
    }
}
