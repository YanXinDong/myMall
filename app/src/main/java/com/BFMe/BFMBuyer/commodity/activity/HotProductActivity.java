package com.BFMe.BFMBuyer.commodity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.widget.RelativeLayout;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.main.adapter.home.HomeAdapter;
import com.BFMe.BFMBuyer.main.bean.BaseTypeBean;
import com.BFMe.BFMBuyer.main.bean.HotProductsBean;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.UiUtils;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.BFMe.BFMBuyer.view.DividerGridItemDecoration;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Request;

/**
 * 热销商品
 */
public class HotProductActivity extends IBaseActivity {

    @BindView(R.id.rv)
    XRecyclerView mHotProductXRV;

    private int mPageNo = 1;
    /**
     * 正常状态
     */
    private static final int STATE_NORMAL = 0;
    /**
     * 刷新状态
     */
    private static final int STATE_REFRESH = 1;

    /**
     * 加载更多状态
     */
    private static final int STATE_MORE = 2;

    private int state = STATE_NORMAL;//默认为正常状态
    private HomeAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    private void initData() {
        getHomeHotProducts();
    }

    private void getHomeHotProducts() {
        Map<String, String> params = new HashMap<>();
        params.put("PageNo", String.valueOf(mPageNo));
        params.put("PageSize", "10");
        OkHttpUtils
                .get()
                .params(params)
                .url(GlobalContent.GET_HOME_HOT_PRODUCTS)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("获取首页热销商品==" + response);
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (rootBean.ErrCode.equals("0")) {
                            HotProductsBean hotProductsBean = mGson.fromJson(rootBean.Data, HotProductsBean.class);
                            disposeHotProductsData(hotProductsBean.getData());

                        } else {
                            showToast(rootBean.ResponseMsg);
                        }
                    }
                });
    }

    private void disposeHotProductsData(List<HotProductsBean.DataBean> data) {
        List<BaseTypeBean> datas = new ArrayList<>();
        if(data != null && data.size() > 0) {
            for(int i = 0; i < data.size(); i++) {
                HotProductsBean.DataBean dataBean = data.get(i);
                datas.add(new BaseTypeBean("HotProducts",dataBean));
            }
        }
        setAdapter(datas);

    }

    private void setAdapter(List<BaseTypeBean> datas) {
        switch (state) {
            case STATE_NORMAL:
                mHotProductXRV.setLayoutManager(new GridLayoutManager(this, 2));
                mHotProductXRV.addItemDecoration(new DividerGridItemDecoration(this));
                mAdapter = new HomeAdapter(this, datas);
                mHotProductXRV.setAdapter(mAdapter);
                setListener();
                break;
            case STATE_REFRESH:
                state = STATE_NORMAL;
                mHotProductXRV.refreshComplete();

                if (mAdapter != null) {
                    mAdapter.cleanData();
                    mAdapter.addData(datas);
                }
                break;
            case STATE_MORE:
                state = STATE_NORMAL;
                mHotProductXRV.loadMoreComplete();
                if(datas.size() == 0) {
                    mHotProductXRV.setNoMore(true);
                    return;
                }
                if (mAdapter != null) {
                    mAdapter.addData(mAdapter.getItemCount(),datas);
                }
                break;
        }

    }

    private void setListener() {
        mHotProductXRV.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                state = STATE_REFRESH;
                getHomeHotProducts();
            }

            @Override
            public void onLoadMore() {
                state = STATE_MORE;
                mPageNo ++ ;
                getHomeHotProducts();
            }
        });
        mAdapter.setHomeItemOnClickListener(new HomeAdapter.HomeItemOnClickListener() {
            @Override
            public void bannerItemOnClick(int type, String url, String imageUrl) {

            }

            @Override
            public void topicCategoryOnItemClick(String topicCategoryId) {

            }

            @Override
            public void allTopicOnClick() {

            }

            @Override
            public void allLimitedOnClick() {

            }

            @Override
            public void onLimitedItemClick(String productId) {

            }

            @Override
            public void allProductsOnClick() {

            }

            @Override
            public void onHotProductsItemClick(String productId) {
                Intent intent = new Intent(HotProductActivity.this, ProducetDetailsActivity.class);
                intent.putExtra("productId", productId);
                startActivity(intent);
                startAnim();
            }
        });
    }

    private void initView() {
        tv_title.setText(getString(R.string.hot_products));
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mHotProductXRV.getLayoutParams();
        layoutParams.setMargins(UiUtils.dip2px(10), 0, 0, 0);
        mHotProductXRV.setFootViewText(getString(R.string.load), getString(R.string.no_more_data));
    }

    @Override
    public int initLayout() {
        return R.layout.activity_xrv;
    }
}
