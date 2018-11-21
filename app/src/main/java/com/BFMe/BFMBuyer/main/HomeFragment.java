package com.BFMe.BFMBuyer.main;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.activity.PromotionActivity;
import com.BFMe.BFMBuyer.activity.SubjectListActivity;
import com.BFMe.BFMBuyer.base.BaseFragment;
import com.BFMe.BFMBuyer.commodity.activity.HotProductActivity;
import com.BFMe.BFMBuyer.commodity.activity.LimitBuyActivity;
import com.BFMe.BFMBuyer.commodity.activity.ProducetDetailsActivity;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.main.adapter.home.HomeAdapter;
import com.BFMe.BFMBuyer.main.bean.BaseTypeBean;
import com.BFMe.BFMBuyer.main.bean.HomeData;
import com.BFMe.BFMBuyer.main.bean.HotProductsBean;
import com.BFMe.BFMBuyer.ugc.activity.AllTopicActivity;
import com.BFMe.BFMBuyer.ugc.activity.AllTopicSortActivity;
import com.BFMe.BFMBuyer.ugc.activity.TaTalkDetailsActivity;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.UiUtils;
import com.BFMe.BFMBuyer.utils.Utils;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.BFMe.BFMBuyer.view.DividerGridItemDecoration;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * Description：首页Fragment
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/8/14 16:20
 */
public class HomeFragment extends BaseFragment {
    private XRecyclerView homeRV;
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
    private List<BaseTypeBean> mDdata;
    private List<BaseTypeBean> mHotProductsData = new ArrayList<>();
    private int mPageNo = 1;

    @Override
    public View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.activity_xrv, null, false);
        homeRV = (XRecyclerView) view.findViewById(R.id.rv);
        ((SimpleItemAnimator)homeRV.getItemAnimator()).setSupportsChangeAnimations(false);
        homeRV.getItemAnimator().setChangeDuration(0);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) homeRV.getLayoutParams();
        layoutParams.setMargins(UiUtils.dip2px(10), 0, 0, 0);
        homeRV.setFootViewText(getString(R.string.load), getString(R.string.no_more_data));
        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        getNetData();
    }

    private void getNetData() {
//        showProgress();
        OkHttpUtils
                .post()
                .url(GlobalContent.POST_HOME_DATA)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Logger.e("首页==onError()" + e.toString());
//                        dismissProgress();
                    }

                    @Override
                    public void onResponse(String response) {
//                        dismissProgress();
                        Logger.e("首页==" + response);
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (rootBean.ErrCode.equals("0")) {
                            HomeData homeData = mGson.fromJson(rootBean.Data, HomeData.class);
                            disposeData(homeData);
                        } else {
                            showToast(rootBean.ResponseMsg);
                        }
                    }
                });
    }

    private void disposeData(HomeData homeData) {
        List<HomeData.HomeDataBean.TopBannerBean> topBanner = homeData.getHomeData().getTopBanner();
        List<HomeData.HomeDataBean.CategoryBean> category = homeData.getHomeData().getCategory();
        List<HomeData.HomeDataBean.ProductBean> product = homeData.getHomeData().getProduct();
        List<HomeData.HomeDataBean.BottonBannerBean> bottonBanner = homeData.getHomeData().getBottonBanner();
        mDdata = new ArrayList<>();

        mDdata.add(new BaseTypeBean("Carousel", topBanner));

        for (int i = 0; i < category.size(); i++) {
            mDdata.add(new BaseTypeBean("HotTopic", category.get(i)));
        }

        mDdata.add(new BaseTypeBean("AllTopic", "查看全部话题分类按钮"));
        mDdata.add(new BaseTypeBean("AllLimited", "查看全部限时购按钮"));


        for (int i = 0; i < product.size(); i++) {
            mDdata.add(new BaseTypeBean("Limited", product.get(i)));
        }
        mDdata.add(new BaseTypeBean("AllLimited2", "查看全部限时购按钮"));

        mDdata.add(new BaseTypeBean("Banner", bottonBanner));
        mDdata.add(new BaseTypeBean("HotProductsTitle", getResources().getString(R.string.hot_products)));


        setAdapter();
        setListener();

        state = STATE_MORE;
        getHomeHotProducts();
    }

    private void getHomeHotProducts() {
        Map<String, String> params = new HashMap<>();
        params.put("PageNo", mPageNo+"");
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
                            setAdapter();
                        }
                    }
                });
    }

    private void disposeHotProductsData(List<HotProductsBean.DataBean> data) {
        mHotProductsData = new ArrayList<>();
        if(data != null && data.size() > 0) {
            //清除之前的数据
            mHotProductsData.clear();
            for(int i = 0; i < data.size(); i++) {
                HotProductsBean.DataBean dataBean = data.get(i);
                mHotProductsData.add(new BaseTypeBean("HotProducts",dataBean));
            }
        }
        setAdapter();
    }

    private void setAdapter() {
        switch (state) {
            case STATE_NORMAL:
                homeRV.setLayoutManager(new GridLayoutManager(getContext(), 2));
                homeRV.addItemDecoration(new DividerGridItemDecoration(getContext()));
                mAdapter = new HomeAdapter(getContext(), mDdata);
                homeRV.setAdapter(mAdapter);
                break;
            case STATE_REFRESH:
                state = STATE_NORMAL;
                homeRV.refreshComplete();

                if (mAdapter != null) {
                    mAdapter.cleanData();
                    mAdapter.addData(mDdata);
                }
                break;
            case STATE_MORE:
                state = STATE_NORMAL;
                homeRV.loadMoreComplete();
                if(mHotProductsData.size() == 0) {
                    homeRV.setNoMore(true);
                    return;
                }
                if (mAdapter != null) {
                    mAdapter.addData(mAdapter.getItemCount(),mHotProductsData);
                }
                break;
        }

    }

    private void setListener() {
        mAdapter.setHomeItemOnClickListener(new HomeAdapter.HomeItemOnClickListener() {
            @Override
            public void bannerItemOnClick(int type, String url, String imageUrl) {
                String string = url.substring(url.lastIndexOf("/") + 1);
                boolean b = Utils.isNumberByMatch(string);
                Logger.e("图片url==" + imageUrl);
                if (b) {
                    if (type == 1) {
                        Intent intent = new Intent(mActivity, SubjectListActivity.class);
                        intent.putExtra("imageUrl", imageUrl);
                        intent.putExtra("firstTopic", 11);
                        intent.putExtra("id", string);
                        mActivity.startActivity(intent);


                        startAnim();
                    } else {
                        Intent intent = new Intent(getContext(), TaTalkDetailsActivity.class);
                        intent.putExtra(GlobalContent.TOPIC_ID, Long.valueOf(string));
                        mActivity.startActivity(intent);
                        startAnim();
                    }

                } else {
                    Intent intent = new Intent(mActivity, PromotionActivity.class);
                    intent.putExtra("url", url);
                    mActivity.startActivity(intent);
                    startAnim();
                }
            }

            @Override
            public void topicCategoryOnItemClick(String topicCategoryId) {
                Intent intent = new Intent(mActivity, AllTopicActivity.class);
                intent.putExtra(GlobalContent.TOPICS_ID, topicCategoryId);
                mActivity.startActivity(intent);
                startAnim();
            }

            @Override
            public void allTopicOnClick() {
                Intent intent = new Intent(mActivity, AllTopicSortActivity.class);
                intent.putExtra("Title", getString(R.string.all_classify));
                mActivity.startActivity(intent);
                startAnim();
            }

            @Override
            public void allLimitedOnClick() {
                Intent intentMore = new Intent(mActivity, LimitBuyActivity.class);
                mActivity.startActivity(intentMore);
                startAnim();
            }

            @Override
            public void onLimitedItemClick(String productId) {
                Intent intent = new Intent(mActivity, ProducetDetailsActivity.class);
                intent.putExtra("productId", productId);
                mActivity.startActivity(intent);
                startAnim();
            }

            @Override
            public void allProductsOnClick() {
                Intent intent = new Intent(mActivity, HotProductActivity.class);
                mActivity.startActivity(intent);
                startAnim();
            }

            @Override
            public void onHotProductsItemClick(String productId) {
                Intent intent = new Intent(mActivity, ProducetDetailsActivity.class);
                intent.putExtra("productId", productId);
                mActivity.startActivity(intent);
                startAnim();
            }
        });

        homeRV.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPageNo = 1;
                state = STATE_REFRESH;
                getNetData();
            }

            @Override
            public void onLoadMore() {
                state = STATE_MORE;
                mPageNo ++ ;
                getHomeHotProducts();
            }
        });
    }
}
