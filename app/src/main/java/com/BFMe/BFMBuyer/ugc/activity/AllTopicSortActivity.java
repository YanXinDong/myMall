package com.BFMe.BFMBuyer.ugc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.ugc.adapter.AllTopicSortAdapter;
import com.BFMe.BFMBuyer.ugc.bean.HotTopic;
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
 * 获取全部话题分类
 */
public class AllTopicSortActivity extends IBaseActivity {

    @BindView(R.id.rv)
    XRecyclerView xrv;
    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFRESH = 1;
    private static final int STATE_MORE = 2;
    private int state = STATE_NORMAL;
    private int pageNo = 1;
    private AllTopicSortAdapter mAdapter;
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }


    private void initData() {
        Intent intent = getIntent();
        mTitle = intent.getStringExtra("Title");
        tv_title.setText(mTitle);
        getNethotTopic();
    }

    private void getNethotTopic() {
        Map<String, String> params = new HashMap<>();
        params.put("type","1");
        params.put("pageSize","10");
        params.put("pageNo",pageNo+"");
        OkHttpUtils
                .post()
                .url(GlobalContent.GET_UGC_HOT_TOPIC_CATEGORT)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("全部话题分类" + response);
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (rootBean.ErrCode.equals("0")) {
                            HotTopic hotTopic = mGson.fromJson(rootBean.Data, HotTopic.class);
                            showhotTopic(hotTopic.getHotTopic());
                        }
                    }
                });
    }

    private void showhotTopic(List<HotTopic.HotTopicBean> hotTopic) {
        switch (state) {
            case STATE_NORMAL:
                xrv.setLayoutManager(new LinearLayoutManager(this));
                mAdapter = new AllTopicSortAdapter(hotTopic);
                xrv.setAdapter(mAdapter);

                if (hotTopic.size() < 10) {
                    xrv.setNoMore(true);
                }
                break;
            case STATE_REFRESH:
                state = STATE_NORMAL;
                xrv.refreshComplete();
                if (hotTopic.size() < 10) {
                    xrv.setNoMore(true);
                }

                if (mAdapter != null) {
                    mAdapter.clearData();
                    mAdapter.addData(hotTopic);
                }

                break;
            case STATE_MORE:
                state = STATE_NORMAL;
                xrv.loadMoreComplete();

                if (mAdapter != null) {
                    if (hotTopic.size() < 10) {
                        xrv.setNoMore(true);
                    }
                    mAdapter.addData(mAdapter.getItemCount(), hotTopic);
                }
                break;

        }

        setListener();
    }

    private void setListener() {
        xrv.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                state = STATE_REFRESH;
                getNethotTopic();

            }

            @Override
            public void onLoadMore() {
                pageNo += 1;
                state = STATE_MORE;
                getNethotTopic();
            }
        });

        mAdapter.setOnItemClickListener(new AllTopicSortAdapter.OnItemClickListener() {
            @Override
            public void itemClick(String title, long id) {
                if(mTitle.equals(getString(R.string.all_classify))) {
                    Intent intent = new Intent(AllTopicSortActivity.this,AllTopicActivity.class);
                    intent.putExtra(GlobalContent.TOPICS_ID, id+"");
                    startActivity(intent);
                    startAnim();
                }else if(mTitle.equals(getString(R.string.add_classify))) {
                    Intent intent = new Intent();
                    intent.putExtra("TOPIC_SORT_TITLE", title);
                    intent.putExtra("TOPIC_SORT_ID", id);
                    setResult(1001, intent);
                    finish();
                    exitAnim();
                }
            }
        });
    }
    
    private void initView() {
        setBackButtonVisibility(View.VISIBLE);
        xrv.setFootViewText(getString(R.string.load), getString(R.string.no_more_data));
    }

    @Override
    public int initLayout() {
        return R.layout.activity_xrv;
    }
}
