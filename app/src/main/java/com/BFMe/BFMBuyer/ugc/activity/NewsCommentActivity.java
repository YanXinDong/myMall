package com.BFMe.BFMBuyer.ugc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.ugc.adapter.NewsCommentAdapter;
import com.BFMe.BFMBuyer.ugc.bean.NewsTopicCommentBean;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Request;

/**
 * 用户最新的被评论消息
 */
public class NewsCommentActivity extends IBaseActivity {

    @BindView(R.id.rv)
    XRecyclerView mRecyclerView;
    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE = 2;
    private int state = STATE_NORMAL;
    private int pageNo = 1;
    private NewsCommentAdapter newsCommentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
        setListener();
    }

    private void initData() {
        getNetData();
    }

    private void getNetData() {

        Map<String, String> map = new HashMap<>();
        map.put("userId", mUserId);
        map.put("pageSize", 10 + "");
        map.put("pageNo", pageNo + "");
        OkHttpUtils
                .post()
                .url(GlobalContent.GET_PUSH_NEW_COMMENTS)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Logger.e("新话题评论异常:" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if ("0".equals(rootBean.ErrCode)) {
                            Logger.e("新话题评论数据：" + rootBean.Data);
                            Type type = new TypeToken<List<NewsTopicCommentBean>>() {
                            }.getType();
                            List<NewsTopicCommentBean> newsTopicCommentBean = mGson.fromJson(rootBean.Data, type);
                            showData(newsTopicCommentBean);
                        }
                    }
                });

    }

    private void showData(List<NewsTopicCommentBean> newsTopicCommentBean) {
        switch (state) {
            case STATE_NORMAL:
                newsCommentAdapter = new NewsCommentAdapter(this, newsTopicCommentBean);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                mRecyclerView.setAdapter(newsCommentAdapter);
                break;

            case STATE_REFREH:
                if (newsCommentAdapter != null) {
                    newsCommentAdapter.cleanData();
                    newsCommentAdapter.addData(newsTopicCommentBean);
                } else {
                    newsCommentAdapter = new NewsCommentAdapter(this, newsTopicCommentBean);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                    mRecyclerView.setAdapter(newsCommentAdapter);
                }
                mRecyclerView.refreshComplete();
                break;

            case STATE_MORE:
                if (newsCommentAdapter != null) {
                    if (newsTopicCommentBean.size() < 10) {
                        mRecyclerView.setNoMore(true);
                    } else {
                        newsCommentAdapter.addData(newsCommentAdapter.getItemCount(), newsTopicCommentBean);
                        mRecyclerView.loadMoreComplete();
                    }
                } else {
                    newsCommentAdapter = new NewsCommentAdapter(this, newsTopicCommentBean);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                    mRecyclerView.setAdapter(newsCommentAdapter);
                }
                break;
        }
        newsCommentAdapter.setOnNewsTopicCommentClickListener(new NewsCommentAdapter.OnNewsTopicCommentClickListener() {
            @Override
            public void onNewsTopicComment(Long topicId) {
                Intent intent = new Intent(NewsCommentActivity.this, TaTalkDetailsActivity.class);
                intent.putExtra(GlobalContent.TOPIC_ID, topicId);
                startActivity(intent);
                startAnim();
            }
        });
        newsCommentAdapter.setTopicCommentListClickListener(new NewsCommentAdapter.OnTopicCommentListClickListener() {
            @Override
            public void onTopicCommentList(Long topicId) {
                Intent intent = new Intent(NewsCommentActivity.this, AllCommentActivity.class);
                intent.putExtra(GlobalContent.TOPIC_ID, topicId);
                startActivity(intent);
                startAnim();
            }
        });
    }

    private void setListener() {
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                state = STATE_REFREH;
                getNetData();
            }

            @Override
            public void onLoadMore() {
                pageNo += 1;
                state = STATE_MORE;
                getNetData();
            }
        });

    }

    private void initView() {
        //返回按钮
        setBackButtonVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.news_topic_comment));
    }

    @Override
    public int initLayout() {
        return R.layout.activity_xrv;
    }
}
