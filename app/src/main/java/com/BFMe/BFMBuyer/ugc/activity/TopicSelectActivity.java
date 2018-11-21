package com.BFMe.BFMBuyer.ugc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.ugc.adapter.TopicSelectAdapter;
import com.BFMe.BFMBuyer.ugc.bean.TopicSelectBean;
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

/**
 * 话题精选
 */
public class TopicSelectActivity extends IBaseActivity {
    @BindView(R.id.rv_topic_select)
    XRecyclerView rv_topic_select;
    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE = 2;
    private int state = STATE_NORMAL;
    private int pageNo = 1;
    private TopicSelectAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        setListener();
    }

    private void initData() {
       getDataFromNet();
    }

    private void getDataFromNet() {
        Map<String, String> map = new HashMap<>();
        map.put("userId", mUserId);
        map.put("pageSize", 10 + "");
        map.put("pageNo", pageNo + "");
        OkHttpUtils
                .post()
                .url(GlobalContent.GET_PUSH_CONCERN_MESSAGE)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Logger.e("话题精选通知异常:" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if ("0".equals(rootBean.ErrCode)) {
                            Logger.e("话题精选数据：" + rootBean.Data);
                            TopicSelectBean topicSelectBean = mGson.fromJson(rootBean.Data, TopicSelectBean.class);
                            List<TopicSelectBean.DataBean> topicSelectdata = topicSelectBean.getData();

                            setData(topicSelectdata);
                        }
                    }
                });
    }

    private void setData(List<TopicSelectBean.DataBean> topicSelectdata) {
        switch (state) {
            case STATE_NORMAL:
                adapter = new TopicSelectAdapter(this,topicSelectdata);
                rv_topic_select.setAdapter(adapter);
                rv_topic_select.setLayoutManager(new LinearLayoutManager(this));
                break;

            case STATE_REFREH:
                if (adapter != null) {
                    adapter.cleanData();
                    adapter.addData(topicSelectdata);
                } else {
                    rv_topic_select.setLayoutManager(new LinearLayoutManager(this));
                    adapter = new TopicSelectAdapter(this, topicSelectdata);
                    rv_topic_select.setAdapter(adapter);
                }
                rv_topic_select.refreshComplete();
                break;

            case STATE_MORE:
                if (adapter != null) {
                    if (topicSelectdata.size() < 10) {
                        rv_topic_select.setNoMore(true);
                    } else {
                        adapter.addData(adapter.getItemCount(), topicSelectdata);
                        rv_topic_select.loadMoreComplete();
                    }
                } else {
                    rv_topic_select.setLayoutManager(new LinearLayoutManager(this));
                    adapter = new TopicSelectAdapter(this, topicSelectdata);
                    rv_topic_select.setAdapter(adapter);
                }
                break;
        }
          adapter.setOnTopicSelectClickListener(new TopicSelectAdapter.OnTopicSelectClickListener() {
              @Override
              public void onTopicSelect(long topicId) {
                  Intent intent = new Intent(TopicSelectActivity.this, TaTalkDetailsActivity.class);
                  intent.putExtra(GlobalContent.TOPIC_ID, topicId);
                  startActivity(intent);
                  startAnim();
              }
          });
    }
    private void initView() {
        setBackButtonVisibility(View.VISIBLE);
        String title = getIntent().getStringExtra("TITLE");
        tv_title.setText(title);
    }

    private void setListener() {
        rv_topic_select.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                state = STATE_REFREH;
                getDataFromNet();
            }

            @Override
            public void onLoadMore() {
                pageNo += 1;
                state = STATE_MORE;
                getDataFromNet();
            }
        });

    }

    @Override
    public int initLayout() {
        return R.layout.activity_topic_select;
    }
}
