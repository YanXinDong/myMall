package com.BFMe.BFMBuyer.search.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.NoTitleBaseActivity;
import com.BFMe.BFMBuyer.commodity.activity.CommodityFiltrateActivity;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.javaBean.SearchCommodityBean;
import com.BFMe.BFMBuyer.javaBean.SearchConditionBean;
import com.BFMe.BFMBuyer.javaBean.ShopListData;
import com.BFMe.BFMBuyer.search.adapter.SearchConditionAdapter;
import com.BFMe.BFMBuyer.search.adapter.SearchResultAdapter;
import com.BFMe.BFMBuyer.shop.activity.ShopFiltrateActivity;
import com.BFMe.BFMBuyer.ugc.bean.TopicList;
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

import okhttp3.Request;


public class SearchResultActivity extends NoTitleBaseActivity implements View.OnClickListener {

    private TextView tvSearchCircle;
    private XRecyclerView xrv_search_result_list;

    private String mContent;//搜索关键字

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

    private SearchResultAdapter mAdapter;
    private RelativeLayout rg_search_filtrate;
    private CheckBox cb_type;
    private CheckBox cb_rank;

    private List<SearchConditionBean> mTypeData = new ArrayList<>();
    private List<SearchConditionBean> mRankTopicData = new ArrayList<>();
    private List<SearchConditionBean> mRankShopData = new ArrayList<>();
    private List<SearchConditionBean> mRankCommodityData = new ArrayList<>();

    private SearchCommodityBean.DataBean.FacetResultsBean mCommodityResults;

    private int mViewId;
    private String mSearchSort;//查询类型 1:话题,2:店铺,3:商品
    private int mTopicSort = 1;//话题排序条件：1：按照创建时间倒序（最新）2：按照点按量倒序（人气推荐）

    private int mShopSort = 1;//店铺排序条件 1：人气推荐，2：销量
    private String mAreaId;//国家地区id
    private String mShopMold;//店铺经营方式1:自营店铺,2:个人店铺,3:企业店铺

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isTranslucentStatusBar = false;
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {

        mTypeData.add(new SearchConditionBean(getString(R.string.topic), false));
        mTypeData.add(new SearchConditionBean(getString(R.string.shop), false));
        mTypeData.add(new SearchConditionBean(getString(R.string.commodity), false));

        mRankTopicData.add(new SearchConditionBean(getString(R.string.newest), true));
        mRankTopicData.add(new SearchConditionBean(getString(R.string.popularity_recommend), false));

        mRankShopData.add(new SearchConditionBean(getString(R.string.popularity_recommend), true));
        mRankShopData.add(new SearchConditionBean(getString(R.string.sales_volume), false));

        mRankCommodityData.add(new SearchConditionBean(getString(R.string.recommend), true));
        mRankCommodityData.add(new SearchConditionBean(getString(R.string.sales_volume), false));
        mRankCommodityData.add(new SearchConditionBean(getString(R.string.new_product), false));
        mRankCommodityData.add(new SearchConditionBean(getString(R.string.popularity), false));
        mRankCommodityData.add(new SearchConditionBean(getString(R.string.price_low_to_tall), false));
        mRankCommodityData.add(new SearchConditionBean(getString(R.string.price_tall_to_low), false));

        mContent = getIntent().getStringExtra("Content");
        mSearchSort = getIntent().getStringExtra("searchSort");
        if (!TextUtils.isEmpty(mContent)) {
            tvSearchCircle.setText(mContent);

            getDatafromNet();
        }

    }

    /**
     * 联网获取数据
     */
    private void getDatafromNet() {
        showProgress();
        switch (mSearchSort){
            case "1":
                cb_type.setText(getString(R.string.topic));
                mTypeData.get(0).setSelect(true);
                SearchResultAdapter.STATE = 0;
                getNetTopicList();
                break;
            case "2":
                cb_type.setText(getString(R.string.shop));
                mTypeData.get(1).setSelect(true);
                SearchResultAdapter.STATE = 1;
                getNetShopList();
                break;
            case "3":
                cb_type.setText(getString(R.string.commodity));
                mTypeData.get(2).setSelect(true);
                SearchResultAdapter.STATE = 2;
                getNetCommodityList();
                break;
        }
    }

    private void getNetCommodityList() {
        Map<String, String> params = new HashMap<>();
        params.put("PageSize", "10");
        params.put("PageNo", String.valueOf(pageNo));
        params.put("QueryKey", mContent);
        params.put("Sort", String.valueOf(mCommoditySort));

        if(mBrandQuery != null && !mBrandQuery.isEmpty()) {
            params.put("BrandQuery", mBrandQuery);
        }
        if(mThirdCategoryQuery != null && !mThirdCategoryQuery.isEmpty()) {
            params.put("ThirdCategoryQuery", mThirdCategoryQuery);
        }
        if(mCountryQuery != null && !mCountryQuery.isEmpty()) {
            params.put("CountryQuery", mCountryQuery);
        }
        if(minPrice != null && !minPrice.isEmpty()) {
            params.put("minPrice", minPrice);
        }
        if(maxPrice != null && !maxPrice.isEmpty()) {
            params.put("maxPrice", maxPrice);
        }

        Logger.e("商品列表请求参数=="+params.toString());
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

    private void getNetShopList() {
        Map<String, String> params = new HashMap<>();
        params.put("PageSize", "10");
        params.put("PageNo", String.valueOf(pageNo));
        params.put("Sort", String.valueOf(mShopSort));//排序条件 1：人气推荐，2：销量
        params.put("keyWords", mContent);
        if (mAreaId != null && !mAreaId.isEmpty()) {
            params.put("AreaId", mAreaId);//国家地区id
        }
        if (mShopMold != null && !mShopMold.isEmpty()) {
            params.put("ShopMold", String.valueOf(mShopMold));//店铺经营方式1:自营店铺,2:个人店铺,3:企业店铺
        }

        Logger.e("店铺列表参数==" + params.toString());
        OkHttpUtils
                .post()
                .url(GlobalContent.POST_SHOP_LIST)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        dismissProgress();
                        Logger.e("店铺列表===" + response);
                        parseJson(response);
                    }
                });
    }

    private void getNetTopicList() {
        Map<String, String> params = new HashMap<>();
        params.put("pageSize", "10");
        params.put("pageNo", String.valueOf(pageNo));
        if (isLogin) {
            params.put("userId", mUserId);
        }
        params.put("keyWords", String.valueOf(mContent));
        params.put("sort", String.valueOf(mTopicSort));
        Logger.e("话题搜索参数==" + params.toString());
        OkHttpUtils
                .post()
                .params(params)
                .url(GlobalContent.POST_UGC_TOPIC_BY_KEYWORDS)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        dismissProgress();
                    }

                    @Override
                    public void onResponse(String response) {
                        dismissProgress();
                        Logger.e("话题搜索结果==" + response);
                        parseJson(response);
                    }
                });
    }


    /**
     * 解析数据
     *
     * @param response
     */
    private void parseJson(String response) {
        RootBean rootBean = mGson.fromJson(response, RootBean.class);
        if (Integer.parseInt(rootBean.ErrCode) == 0) {
            switch (mSearchSort){
                case "1":
                    showData(mGson.fromJson(rootBean.Data, TopicList.class).getTopicsList());
                    break;
                case "2":
                    showData(mGson.fromJson(rootBean.Data, ShopListData.class).getData());
                    break;
                case "3":
                    SearchCommodityBean searchCommodityBean = mGson.fromJson(rootBean.Data, SearchCommodityBean.class);
                    SearchCommodityBean.DataBean data = searchCommodityBean.getData();
                    mCommodityResults = data.getFacetResults();
                    showData(data.getProducts());
                    break;

            }
        } else {
            showToast(rootBean.ResponseMsg);
        }
    }

    private <T> void showData(List<T> datas) {
        switch (state) {
            case STATE_NORMAL:
                xrv_search_result_list.setLayoutManager(new GridLayoutManager(this,2));
                xrv_search_result_list.addItemDecoration(new MyDividerGridItemDecoration(this, UiUtils.dip2px(10),UiUtils.dip2px(10), Color.parseColor("#FFFFFF")));
                mAdapter = new SearchResultAdapter<>(this,datas);
                xrv_search_result_list.setAdapter(mAdapter);
                if (datas.size() < 10) {
                    xrv_search_result_list.setNoMore(true);
                }
                setListener();
                break;
            case STATE_REFRESH:
                state = STATE_NORMAL;
                xrv_search_result_list.refreshComplete();
                if (datas.size() < 10) {
                    xrv_search_result_list.setNoMore(true);
                }

                if (mAdapter != null) {
                    mAdapter.cleanData();
                    mAdapter.addData(datas);
                }

                break;
            case STATE_MORE:
                state = STATE_NORMAL;
                xrv_search_result_list.loadMoreComplete();
                if (mAdapter != null) {
                    if (datas.size() < 10) {
                        xrv_search_result_list.setNoMore(true);
                        mAdapter.addData(mAdapter.getItemCount(), datas);
                    } else {
                        mAdapter.addData(mAdapter.getItemCount(), datas);
                    }
                }
                break;

        }

    }

    private void setListener() {
        xrv_search_result_list.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }

            @Override
            public void onLoadMore() {
                pageNo += 1;
                state = STATE_MORE;
                getDatafromNet();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_search_circle:
            case R.id.iv_back:
                finish();
                exitAnim();
                break;
            case R.id.cb_type:
                mViewId = R.id.cb_type;
                showPP();
                break;
            case R.id.cb_rank:
                mViewId = R.id.cb_rank;
                showPP();
                break;
            case R.id.btn_filtrate:
                showFiltrate();
                break;
        }
    }

    /**
     * 分类型显示筛选页面
     * 话题没有筛选
     */
    private void showFiltrate() {
        Intent intent = null;
        switch (mSearchSort){
            case "2":
                intent = new Intent(this, ShopFiltrateActivity.class);
                startActivityForResult(intent,1001);
                bottomStartAnim();
                break;
            case "3":
                intent = new Intent(this, CommodityFiltrateActivity.class);
                intent.putExtra("CommodityFiltrate",mGson.toJson(mCommodityResults));
                startActivityForResult(intent,1001);
                bottomStartAnim();
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1001) {
            if(resultCode == 1002) {//店铺筛选返回的数据
                mShopMold = data.getStringExtra("shopTypes");
                mAreaId = data.getStringExtra("areasTypes");
            }else if(resultCode == 1003 || resultCode == 1004) {
                mBrandQuery = data.getStringExtra("brand");//品牌名
                mThirdCategoryQuery = data.getStringExtra("classify");//三级分类名称
                mCountryQuery = data.getStringExtra("country");//国家名
                maxPrice = data.getStringExtra("maxPrice");//国家名
                minPrice = data.getStringExtra("minPrice");//国家名
            }

            refreshData();
        }
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

        popupWindow.showAsDropDown(rg_search_filtrate);
        //添加pop窗口关闭事件
        popupWindow.setOnDismissListener(new poponDismissListener());
        initPPView(v, popupWindow);
    }

    private void initPPView(View v, PopupWindow popupWindow) {
        RecyclerView rv_mContent = (RecyclerView) v.findViewById(R.id.rv_content);
        rv_mContent.setLayoutManager(new LinearLayoutManager(this));
        SearchConditionAdapter searchConditionAdapter = new SearchConditionAdapter();
        rv_mContent.setAdapter(searchConditionAdapter);
        initPPData(searchConditionAdapter, v, popupWindow);
    }

    private void initPPData(SearchConditionAdapter adapter, View v, final PopupWindow popupWindow) {
        switch (mViewId) {
            case R.id.cb_type:
                adapter.setData(mTypeData);
                break;
            case R.id.cb_rank:
                switch (mSearchSort) {
                    case "1":
                        adapter.setData(mRankTopicData);
                        break;
                    case "2":
                        adapter.setData(mRankShopData);
                        break;
                    case "3":
                        adapter.setData(mRankCommodityData);
                        break;
                }
                break;
        }

        adapter.setOnItemClickListener(new SearchConditionAdapter.OnItemClickListener() {
            @Override
            public void itemClick(int position) {
                switch (mViewId) {
                    case R.id.cb_type:
                        switchoverLayout(position);
                        cb_rank.setText(getString(R.string.rank));
                        break;
                    case R.id.cb_rank:
                        switchoverSort(position);
                        break;
                }
                refreshData();
                popupWindow.dismiss();
            }
        });
    }

    /**
     * 切换排序条件
     * @param position 选中的类型
     */
    private void switchoverSort(int position) {
        switch (mSearchSort) {
            case "1":
                for(int i = 0; i < mRankTopicData.size(); i++) {
                    if (position == i) {
                        cb_rank.setText(mRankTopicData.get(i).getTitle());
                        mTopicSort = i + 1;
                        mRankTopicData.get(i).setSelect(true);
                    } else {
                        mRankTopicData.get(i).setSelect(false);
                    }
                }
                break;
            case "2":
                for(int i = 0; i < mRankShopData.size(); i++) {
                    if (position == i) {
                        cb_rank.setText(mRankShopData.get(i).getTitle());
                        mShopSort = i + 1;
                        mRankShopData.get(i).setSelect(true);
                    } else {
                        mRankShopData.get(i).setSelect(false);
                    }
                }
                break;
            case "3":
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
                break;
        }
    }

    /**
     * 切换搜索结果类型
     * @param position 选中的类型
     */
    private void switchoverLayout(int position) {
        for (int i = 0; i < mTypeData.size(); i++) {
            if (position == i) {
                cb_type.setText(mTypeData.get(i).getTitle());
                mSearchSort = String.valueOf(i + 1);
                mTypeData.get(i).setSelect(true);
            } else {
                mTypeData.get(i).setSelect(false);
            }
        }
    }

    /**
     * 刷新数据
     */
    private void refreshData() {
        pageNo = 1;
        state = STATE_REFRESH;
        getDatafromNet();
    }

    /**
     * popWin关闭的事件
     */
    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            cb_type.setChecked(false);
            cb_rank.setChecked(false);
        }

    }

    private void initView() {
        ImageView ivBack = (ImageView) findViewById(R.id.iv_back);
        tvSearchCircle = (TextView) findViewById(R.id.tv_search_circle);
        rg_search_filtrate = (RelativeLayout) findViewById(R.id.rg_search_filtrate);
        cb_type = (CheckBox) findViewById(R.id.cb_type);
        cb_rank = (CheckBox) findViewById(R.id.cb_rank);
        Button btn_filtrate = (Button) findViewById(R.id.btn_filtrate);

        xrv_search_result_list = (XRecyclerView) findViewById(R.id.xrv_search_result_list);
        xrv_search_result_list.setFootViewText(getString(R.string.load), getString(R.string.no_more_data));

        ivBack.setOnClickListener(this);
        tvSearchCircle.setOnClickListener(this);
        cb_type.setOnClickListener(this);
        cb_rank.setOnClickListener(this);
        btn_filtrate.setOnClickListener(this);

    }

    @Override
    public int initLayout() {
        return R.layout.activity_search_result;
    }


}
