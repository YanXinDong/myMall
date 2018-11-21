package com.BFMe.BFMBuyer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.adapter.SubjectListAdapter;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.javaBean.SubjectListBean;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.BFMe.BFMBuyer.view.MyGridView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.netease.nim.uikit.common.ui.ptr.ILoadingLayout;
import com.netease.nim.uikit.common.ui.ptr.PullToRefreshBase;
import com.netease.nim.uikit.common.ui.ptr.PullToRefreshScrollView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description ：专题列表
 * Created by :奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2016/9/14 17:25
 */
public class SubjectListActivity extends IBaseActivity {

    private MyGridView gvProduct;

    private int firstTopic;
    private Intent intent;
    private String id;

    private PullToRefreshScrollView pull_refresh_scrollview;

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
    //private int pageSize = 10;//一页的数据
    private SubjectListAdapter adapter;
    private List<SubjectListBean.ProductsBean> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pull_refresh_scrollview = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
        initData();

        setBackButtonVisibility(View.VISIBLE);
        setChartButtonVisibility(View.VISIBLE);

        ImageView ivPicture = (ImageView) findViewById(R.id.iv_picture);
        gvProduct = (MyGridView) findViewById(R.id.gv_product);
        initIndicator();
        //首页轮播图传过来的数据
        if (firstTopic == 11) {
            String imageUrl3 = intent.getStringExtra("imageUrl");
            tv_title.setVisibility(View.INVISIBLE);
            Glide.with(this).load(imageUrl3).error(R.drawable.zhanwei2).centerCrop().into(ivPicture);
            // 根据id访问服务器获取专题列表
            GetTopicProduct(10, 1);
        }
        setListener();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_subject_list;
    }

    /**
     * 监听
     */
    private void setListener() {

        pull_refresh_scrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                pageNo = 1;
                state = STATE_REFREH;
                GetTopicProduct(10, pageNo);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                pageNo += 1;
                state = STATE_MORE;
                GetTopicProduct(10, pageNo);
            }

        });

    }


    /**
     * 根据id访问服务器获取专题列表
     */
    private void GetTopicProduct(int pageSize, int pageNo) {
        showProgress();

        Map<String, String> map = new HashMap<>();
        map.put("pageSize", pageSize + "");
        map.put("pageNo", pageNo + "");
        map.put("TopicId", id + "");

        Logger.e("专题列表map=="+map.toString());
        OkHttpUtils
                .get()
                .url(GlobalContent.GLOBAL_TOPIC_PTODUCT)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                        dismissProgress();
                        pull_refresh_scrollview.onRefreshComplete();
                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("专题列表=="+response);
                        dismissProgress();
                        RootBean rootBean = new Gson().fromJson(response, RootBean.class);
                        if (rootBean.ErrCode.equals("0")) {
                            String data = rootBean.Data;
                            SubjectListBean subjectListBean = new Gson().fromJson(data, SubjectListBean.class);
                            products = subjectListBean.getProducts();
                            showShopCommodity();
                        } else {
                            showToast(rootBean.ResponseMsg);
                        }
                    }
                });
    }

    /**
     * 专题列表数据的初始化
     */
    private void initData() {
        intent = getIntent();
        firstTopic = intent.getIntExtra("firstTopic", 1);
        id = intent.getStringExtra("id");

    }

    /**
     * 显示店铺内商品列表
     *
     * @param
     */
    private void showShopCommodity() {
        switch (state) {
            case STATE_NORMAL:
                adapter = new SubjectListAdapter(products, SubjectListActivity.this);
                gvProduct.setAdapter(adapter);
                break;

            case STATE_REFREH:
                if (adapter != null) {
                    adapter.cleanData();
                    adapter.addData(products);
                } else {
                    adapter = new SubjectListAdapter(products, SubjectListActivity.this);
                    gvProduct.setAdapter(adapter);
                }

                pull_refresh_scrollview.onRefreshComplete();
                showToast(getString(R.string.refresh_succeed));
                break;

            case STATE_MORE:
                if (adapter != null) {
                    adapter.addData(adapter.getCount(), products);
                    pull_refresh_scrollview.onRefreshComplete();
                } else {
                    adapter = new SubjectListAdapter(products, SubjectListActivity.this);
                    gvProduct.setAdapter(adapter);
                }
                showToast(getString(R.string.more_succeed));
                break;

        }
    }

    private void initIndicator() {
        ILoadingLayout startLabels = pull_refresh_scrollview
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        ILoadingLayout endLabels = pull_refresh_scrollview.getLoadingLayoutProxy(
                false, true);
        endLabels.setPullLabel("上拉加载更多...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在加载更多...");// 刷新时
        endLabels.setReleaseLabel("放开加载更多...");// 下来达到一定距离时，显示的提示
    }

}
