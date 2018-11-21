package com.BFMe.BFMBuyer.commodity.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.PopupWindow;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.base.recycle.MultiItemTypeAdapter;
import com.BFMe.BFMBuyer.commodity.adapter.CommodityListAdapter;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.javaBean.SearchCommodityBean;
import com.BFMe.BFMBuyer.javaBean.SearchConditionBean;
import com.BFMe.BFMBuyer.search.adapter.SearchConditionAdapter;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.UiUtils;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.BFMe.BFMBuyer.view.MyDividerGridItemDecoration;
import com.BFMe.BFMBuyer.view.SFPopupWindow;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Request;

public class CommodityListActivity extends IBaseActivity {

    @BindView(R.id.xrv_content)
    XRecyclerView XRVContent;

    @BindView(R.id.cb_rank)
    CheckBox cb_rank;

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


    //加载的页数
    private int pageNo = 1;

    //商品排序条件：0：PI（付费商家）倒序
    //              1：销量倒序
    //              2：价格升序
    //              3：价格倒序
    //              4：评论数倒序   (去除)
    //              5：上架时间倒序
    //              6：以收藏量来倒序
    private int mCommoditySort = 0;
    private String mBrandQuery;//品牌名
    private String mThirdCategoryQuery;//三级分类名称
    private String mCountryQuery;//国家名
    private String minPrice;//筛选最小价格
    private String maxPrice;//筛选最大价格
    private String initClassify;//初始分类名称

    private List<SearchConditionBean> mRankCommodityData = new ArrayList<>();
    private SearchCommodityBean.DataBean.FacetResultsBean mCommodityResults;
    private CommodityListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    private void initData() {
        mRankCommodityData.add(new SearchConditionBean(getString(R.string.recommend), true));
        mRankCommodityData.add(new SearchConditionBean(getString(R.string.sales_volume), false));
        mRankCommodityData.add(new SearchConditionBean(getString(R.string.new_product), false));
        mRankCommodityData.add(new SearchConditionBean(getString(R.string.popularity), false));
        mRankCommodityData.add(new SearchConditionBean(getString(R.string.price_low_to_tall), false));
        mRankCommodityData.add(new SearchConditionBean(getString(R.string.price_tall_to_low), false));

        Intent intent = getIntent();
        mThirdCategoryQuery = intent.getStringExtra("subCategoryName");
        initClassify = mThirdCategoryQuery;
        tv_title.setText(mThirdCategoryQuery);

        getNetData();
    }

    private void getNetData() {
        Map<String, String> params = new HashMap<>();
        params.put("PageSize", "10");
        params.put("PageNo", String.valueOf(pageNo));
        params.put("Sort", String.valueOf(mCommoditySort));

        if (mBrandQuery != null && !mBrandQuery.isEmpty()) {
            params.put("BrandQuery", mBrandQuery);
        }
        if (mThirdCategoryQuery != null && !mThirdCategoryQuery.isEmpty()) {
            params.put("ThirdCategoryQuery", mThirdCategoryQuery);
        }
        if (mCountryQuery != null && !mCountryQuery.isEmpty()) {
            params.put("CountryQuery", mCountryQuery);
        }
        if (minPrice != null && !minPrice.isEmpty()) {
            params.put("minPrice", minPrice);
        }
        if (maxPrice != null && !maxPrice.isEmpty()) {
            params.put("maxPrice", maxPrice);
        }

        Logger.e("商品列表请求参数==" + params.toString());
        OkHttpUtils
                .post()
                .params(params)
                .url(GlobalContent.POST_SEARCH_PRODUCTS)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        dismissProgress();
                        Logger.e("商品列表===" + response);
                        parseJson(response);
                    }
                });
    }

    private void parseJson(String response) {
        RootBean rootBean = mGson.fromJson(response, RootBean.class);
        if (rootBean.ErrCode.equals("0")) {
            SearchCommodityBean searchCommodityBean = mGson.fromJson(rootBean.Data, SearchCommodityBean.class);
            SearchCommodityBean.DataBean data = searchCommodityBean.getData();
            mCommodityResults = data.getFacetResults();
            showData(data.getProducts());
        } else {
            showToast(rootBean.ResponseMsg);
        }
    }

    private void showData(List<SearchCommodityBean.DataBean.ProductsBean> datas) {
        switch (state) {
            case STATE_NORMAL:
                XRVContent.setLayoutManager(new GridLayoutManager(this,2));
                XRVContent.addItemDecoration(new MyDividerGridItemDecoration(this, UiUtils.dip2px(10),UiUtils.dip2px(10), Color.parseColor("#FFFFFF")));
                mAdapter = new CommodityListAdapter(this,datas);
                XRVContent.setAdapter(mAdapter);
                if (datas.size() < 10) {
                    XRVContent.setNoMore(true);
                }
                setListener(datas);
                break;
            case STATE_REFRESH:
                state = STATE_NORMAL;
                XRVContent.refreshComplete();
                if (datas.size() < 10) {
                    XRVContent.setNoMore(true);
                }

                if (mAdapter != null) {
                    mAdapter.cleanData();
                    mAdapter.addData(datas);
                }

                break;
            case STATE_MORE:
                state = STATE_NORMAL;
                XRVContent.loadMoreComplete();
                if (mAdapter != null) {
                    if (datas.size() < 10) {
                        XRVContent.setNoMore(true);
                        mAdapter.addData(mAdapter.getItemCount(), datas);
                    } else {
                        mAdapter.addData(mAdapter.getItemCount(), datas);
                    }
                }
                break;

        }
    }

    private void setListener(final List<SearchCommodityBean.DataBean.ProductsBean> datas) {
        XRVContent.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }

            @Override
            public void onLoadMore() {
                pageNo += 1;
                state = STATE_MORE;
                getNetData();
            }
        });
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                SearchCommodityBean.DataBean.ProductsBean productsBean = datas.get(position - 1);
                Intent intent = new Intent(CommodityListActivity.this,ProducetDetailsActivity.class);
                intent.putExtra("productId",productsBean.getId());
                startActivity(intent);
                startAnim();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }

    /**
     * 刷新数据
     */
    private void refreshData() {
        pageNo = 1;
        state = STATE_REFRESH;
        getNetData();
    }

    @OnClick(R.id.cb_rank)
    void rank(){//排序
        showPP();
    }

    /**
     * 显示一个popupWindow
     */
    private void showPP() {
        View v = LayoutInflater.from(this).inflate(R.layout.pure_rv, null, false);
        SFPopupWindow popupWindow = new SFPopupWindow(v, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT, true);
        popupWindow.setAnimationStyle(R.style.AddressDialogAnim);
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框。不知道是什么原因
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击窗口外边窗口消失
        popupWindow.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        popupWindow.setFocusable(true);

        popupWindow.showAsDropDown(cb_rank);
        //添加pop窗口关闭事件
        popupWindow.setOnDismissListener(new poponDismissListener());
        initPPView(v, popupWindow);
    }
    private void initPPView(View v, PopupWindow popupWindow) {
        RecyclerView rv_mContent = (RecyclerView) v.findViewById(R.id.rv_content);
        rv_mContent.setLayoutManager(new LinearLayoutManager(this));
        SearchConditionAdapter searchConditionAdapter = new SearchConditionAdapter();
        rv_mContent.setAdapter(searchConditionAdapter);
        initPPData(searchConditionAdapter, popupWindow);
    }

    private void initPPData(SearchConditionAdapter adapter, final PopupWindow popupWindow) {
        adapter.setData(mRankCommodityData);
        adapter.setOnItemClickListener(new SearchConditionAdapter.OnItemClickListener() {
            @Override
            public void itemClick(int position) {
                for(int i = 0; i < mRankCommodityData.size(); i++) {
                    if (position == i) {
                        cb_rank.setText(mRankCommodityData.get(i).getTitle());

                        switch (position){
                            case 0:
                                mCommoditySort = i;
                                break;
                            case 1:
                                mCommoditySort = i;
                                break;
                            case 2:
                                mCommoditySort = 5;
                                break;
                            case 3:
                                mCommoditySort = 6;
                                break;
                            case 4:
                                mCommoditySort = 2;
                                break;
                            case 5:
                                mCommoditySort = 3;
                                break;
                        }

                        mRankCommodityData.get(i).setSelect(true);
                    } else {
                        mRankCommodityData.get(i).setSelect(false);
                    }
                }

                refreshData();
                popupWindow.dismiss();
            }
        });
    }

    /**
     * popWin关闭的事件
     */
    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            cb_rank.setChecked(false);
        }

    }

    @OnClick(R.id.btn_filtrate)
    void filtrate(){//筛选
        Intent intent = new Intent(this, CommodityFiltrateActivity.class);
        intent.putExtra("CommodityFiltrate",mGson.toJson(mCommodityResults));
        startActivityForResult(intent,1001);
        bottomStartAnim();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1001) {
            if(resultCode == 1003) {

                String brand = data.getStringExtra("brand");
                String classify = data.getStringExtra("classify");
                String country = data.getStringExtra("country");
                String maxPrice = data.getStringExtra("maxPrice");
                String minPrice = data.getStringExtra("minPrice");

                if(!brand.isEmpty()) {
                    mBrandQuery = brand ;//品牌名
                }
                if(!classify.isEmpty()) {
                    mThirdCategoryQuery = classify;//三级分类名称
                }
                if(!country.isEmpty()) {
                    mCountryQuery = country;//国家名
                }
                if(!maxPrice.isEmpty()) {
                    this.maxPrice = maxPrice;//最高价格
                }
                if(!minPrice.isEmpty()) {
                    this.minPrice = minPrice;//最小价格
                }
            }

            if(resultCode == 1004) {
                mBrandQuery = data.getStringExtra("brand");//品牌名
                mThirdCategoryQuery = initClassify;//三级分类名称(恢复初始条件)
                mCountryQuery = data.getStringExtra("country");//国家名
                maxPrice = data.getStringExtra("maxPrice");//国家名
                minPrice = data.getStringExtra("minPrice");//国家名
            }
            refreshData();
        }
    }

    private void initView() {
        setBackButtonVisibility(View.VISIBLE);
        XRVContent.setFootViewText(getString(R.string.load), getString(R.string.no_more_data));
    }

    @Override
    public int initLayout() {
        return R.layout.activity_commodity_list;
    }
}
