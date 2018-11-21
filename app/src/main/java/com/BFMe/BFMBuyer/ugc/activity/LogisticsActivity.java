package com.BFMe.BFMBuyer.ugc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.activity.OrderDetailActivity;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.ugc.adapter.LogisticsAdapter;
import com.BFMe.BFMBuyer.ugc.bean.PushExpressNoticeBean;
import com.BFMe.BFMBuyer.utils.CacheUtils;
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
 * 物流通知
 */
public class LogisticsActivity extends IBaseActivity {

    @BindView(R.id.rv_logistics)
    XRecyclerView rv_logistics;
    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE = 2;
    private int state = STATE_NORMAL;
    private int pageNo = 1;
    private LogisticsAdapter adapter;

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
        String userId = CacheUtils.getString(this, GlobalContent.USER);
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("pageSize", 10 + "");
        map.put("pageNo", pageNo + "");
        OkHttpUtils
                .post()
                .url(GlobalContent.GET_PUSH_EXPRESS_NOTICE)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Logger.e("推送物流通知异常:" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if ("0".equals(rootBean.ErrCode)) {
                            Logger.e("物流推送数据：" + rootBean.Data);
                            PushExpressNoticeBean pushExpressNotice = mGson.fromJson(rootBean.Data, PushExpressNoticeBean.class);
                            List<PushExpressNoticeBean.DataBean> pushExpressData = pushExpressNotice.getData();
                            setData(pushExpressData);
                        }
                    }
                });
    }

    private void setData(List<PushExpressNoticeBean.DataBean> pushExpressData) {
        switch (state) {
            case STATE_NORMAL:
                adapter = new LogisticsAdapter(this, pushExpressData);
                rv_logistics.setAdapter(adapter);
                rv_logistics.setLayoutManager(new LinearLayoutManager(this));
                break;

            case STATE_REFREH:
                if (adapter != null) {
                    adapter.cleanData();
                    adapter.addData(pushExpressData);
                } else {
                    rv_logistics.setLayoutManager(new LinearLayoutManager(this));
                    adapter = new LogisticsAdapter(this, pushExpressData);
                    rv_logistics.setAdapter(adapter);
                }
                rv_logistics.refreshComplete();
                break;

            case STATE_MORE:
                if (adapter != null) {
                    if (pushExpressData.size() < 10) {
                        rv_logistics.setNoMore(true);
                    } else {
                        adapter.addData(adapter.getItemCount(), pushExpressData);
                        rv_logistics.loadMoreComplete();
                    }
                } else {
                    rv_logistics.setLayoutManager(new LinearLayoutManager(this));
                    adapter = new LogisticsAdapter(this, pushExpressData);
                    rv_logistics.setAdapter(adapter);
                }
                break;
        }

        adapter.setOnPushExpressClickListener(new LogisticsAdapter.OnPushExpressClickListener() {
            @Override
            public void onPushExpress(String orderId, String isComment, String orderStateDesc) {
                Intent intent = new Intent(LogisticsActivity.this, OrderDetailActivity.class);
                intent.putExtra(GlobalContent.ORDER_ID, orderId);
                intent.putExtra(GlobalContent.ORDER_STATUS, orderStateDesc);
                intent.putExtra("ORDER_COMMENT_INFO", isComment);
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
        rv_logistics.setLoadingListener(new XRecyclerView.LoadingListener() {
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
        return R.layout.activity_logistics;
    }
}
