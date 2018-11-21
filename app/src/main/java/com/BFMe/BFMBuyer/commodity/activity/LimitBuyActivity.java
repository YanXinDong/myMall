package com.BFMe.BFMBuyer.commodity.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.commodity.adapter.LimitBuyListAdapter;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.javaBean.LimtBuyDataBean;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.netease.nim.uikit.common.ui.ptr.PullToRefreshBase;
import com.netease.nim.uikit.common.ui.ptr.PullToRefreshListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 限时购列表
 */
public class LimitBuyActivity extends IBaseActivity {

    private LimitBuyListAdapter adapter;
    private PullToRefreshListView pull_refresh_listView;
    /**
     * 正常状态
     */
    private static final int STATE_NORMAL = 0;
    /**
     * 刷新状态
     */
    private static final int STATE_REFREH = 1;
    /**
     * 加载更多状态
     */
    private static final int STATE_MORE = 2;

    private int state = STATE_NORMAL;//默认为正常状态

    private int pageNo = 1;//记载的页数
    private int pageSize = 10;//一页的数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pull_refresh_listView = (PullToRefreshListView) findViewById(R.id.pull_refresh_scrollview);
        setBackButtonVisibility(View.VISIBLE);
        setChartButtonVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.limit_buy));
        //限时抢购
        LimitBuy();
        setRefresh();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_limit_buy;
    }

    private void setRefresh() {
        pull_refresh_listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //加载第一页
                pageNo = 1;
                //更改状态　为刷新状态
                state = STATE_REFREH;

                LimitBuy();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //加载第一页
                pageNo += 1;
                //更改状态　为加载更多状态
                state = STATE_MORE;
                LimitBuy();
            }

        });

    }

    /**
     * 限时抢购
     */
    private void LimitBuy() {
        showProgress();
        Map<String, String> params = new HashMap<>();
        params.put("pageNo", pageNo + "");
        params.put("pageSize", pageSize + "");
        OkHttpUtils
                .get()
                .params(params)
                .url(GlobalContent.GLOBSL_LIMIT_ITEMS)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                        dismissProgress();
                    }

                    @Override
                    public void onResponse(String response) {
                        dismissProgress();
                        Logger.e(  "限时购列表===" + response);
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);

                        if (rootBean.ErrCode.equals("0")) {
                            String data = rootBean.Data;
                            LimtBuyDataBean limtBuyDataBean = mGson.fromJson(data, LimtBuyDataBean.class);
                            setRefreshData(limtBuyDataBean);
                        } else {
                            showToast(rootBean.ResponseMsg);
                        }
                    }
                });
    }

    private void setRefreshData(LimtBuyDataBean limtBuyDataBean) {
        List<LimtBuyDataBean.ProductsBean> productsList = limtBuyDataBean.getProducts();
        switch (state) {
            //正常状态
            case STATE_NORMAL:

                adapter = new LimitBuyListAdapter(productsList, LimitBuyActivity.this);

                pull_refresh_listView.setAdapter(adapter);
                pull_refresh_listView.onRefreshComplete();
                break;

            //刷新状态
            case STATE_REFREH:
                if (adapter != null) {
                    //把原来的清除
                    adapter.cleanData();
                    //适配器重新添加数据
                    adapter.addData(productsList);
                    //关闭刷新显示
                } else {
                    adapter = new LimitBuyListAdapter(productsList, LimitBuyActivity.this);
                    pull_refresh_listView.setAdapter(adapter);
                }

                pull_refresh_listView.onRefreshComplete();
                break;

            //加载更多状态
            case STATE_MORE:
                if (adapter != null) {
                    if (adapter.getCount() >= limtBuyDataBean.getTotal()) {
                        //关闭刷新显示
                        pull_refresh_listView.onRefreshComplete();
                        return;
                    }
                    //在原来的基础上添加数据
                    adapter.addData(adapter.getCount(), productsList);
                } else {
                    adapter = new LimitBuyListAdapter(productsList, LimitBuyActivity.this);
                    pull_refresh_listView.setAdapter(adapter);
                }
                pull_refresh_listView.onRefreshComplete();//隐藏上拉加载
                break;
        }
    }

}
