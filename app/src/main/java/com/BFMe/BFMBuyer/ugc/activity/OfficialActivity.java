package com.BFMe.BFMBuyer.ugc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.commodity.activity.ProducetDetailsActivity;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.ugc.adapter.OfficialAdapter;
import com.BFMe.BFMBuyer.ugc.bean.OfficialBean;
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
 * 官方推荐
 */
public class OfficialActivity extends IBaseActivity {
    @BindView(R.id.rv_official)
    XRecyclerView rv_official;
    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFRESH = 1;
    private static final int STATE_MORE = 2;
    private int state = STATE_NORMAL;
    private int pageNo = 1;
    private OfficialAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        getNetData();
    }

    private void getNetData() {
        Map<String, String> params = new HashMap<>();
        params.put("userId",mUserId);
        params.put("pageSize","10");
        params.put("pageNo",pageNo+"");
        OkHttpUtils
                .post()
                .url(GlobalContent.POST_PUSH_RECOMMEN_MESSAGE)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("官方推荐=="+response);
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if(rootBean.ErrCode.equals("0")) {
                            OfficialBean officialBean = mGson.fromJson(rootBean.Data, OfficialBean.class);
                            showData(officialBean.getData());
                        }
                    }
                });
    }

    private void showData(List<OfficialBean.DataBean> data) {

        switch (state) {
            case STATE_NORMAL:
                rv_official.setLayoutManager(new LinearLayoutManager(this));
                mAdapter = new OfficialAdapter(this,data);
                rv_official.setAdapter(mAdapter);
                setListener();
                if (data.size() < 10) {
                    rv_official.setNoMore(true);
                }
                break;
            case STATE_REFRESH:
                state = STATE_NORMAL;
                rv_official.refreshComplete();
                if (data.size() < 10) {
                    rv_official.setNoMore(true);
                }

                if (mAdapter != null) {
                    mAdapter.cleanData();
                    mAdapter.addData(data);
                }

                break;
            case STATE_MORE:
                state = STATE_NORMAL;
                rv_official.loadMoreComplete();

                if (mAdapter != null) {
                    if (data.size() < 10) {
                        rv_official.setNoMore(true);
                    }
                    mAdapter.addData(mAdapter.getItemCount(), data);
                }
                break;

        }
    }

    private void setListener() {
        rv_official.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                state = STATE_REFRESH;
                getNetData();

            }

            @Override
            public void onLoadMore() {
                pageNo += 1;
                state = STATE_MORE;
                getNetData();
            }
        });

        mAdapter.setOnItemClickListener(new OfficialAdapter.OnItemClickListener() {
            @Override
            public void commodityItemClick(String commodityId) {
                Intent intent = new Intent(OfficialActivity.this, ProducetDetailsActivity.class);
                intent.putExtra("productId", commodityId);
                startActivity(intent);
                startAnim();
            }

            @Override
            public void topicItemClick(long id) {
                Intent intent = new Intent(OfficialActivity.this, TaTalkDetailsActivity.class);
                intent.putExtra(GlobalContent.TOPIC_ID, id);
                startActivity(intent);
                startAnim();
            }
        });

    }

    private void initView() {
        setBackButtonVisibility(View.VISIBLE);
        String title = getIntent().getStringExtra("TITLE");
        tv_title.setText(title);
        rv_official.setFootViewText(getString(R.string.load), getString(R.string.no_more_data));
    }

    @Override
    public int initLayout() {
        return R.layout.activity_official;
    }
}
