package com.BFMe.BFMBuyer.ugc.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.search.adapter.RecentViewedAdapter;
import com.BFMe.BFMBuyer.base.NoTitleBaseActivity;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.javaBean.SearchShopBean;
import com.BFMe.BFMBuyer.model.Model;
import com.BFMe.BFMBuyer.ugc.adapter.BoundProductSearchAdapter;
import com.BFMe.BFMBuyer.ugc.bean.BoundProductBean;
import com.BFMe.BFMBuyer.utils.CacheUtils;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.Utils;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

import static com.BFMe.BFMBuyer.R.id.et_search_circle;

/**
 * 关联商品
 */
public class BoundProductActivity extends NoTitleBaseActivity implements View.OnClickListener {
    private EditText etSearchCircle;
    private TextView tvSearchCancel;
    private RelativeLayout rlLayout2;
    private RecyclerView rvRecentlyViewed;
    private TextView tvNoSearch;
    private Button btnClearRecently;
    private XRecyclerView rv_result;
    private RelativeLayout rv_layout_search;
    private ImageView ivNoCreate;
    private ImageView ivNoResult;
    private static final int STATE_NORMAL = 0;
    private static final int STATE_MORE = 1;

    private int state = STATE_NORMAL;//默认为正常状态
    private int pageNo = 1;//记载的页数
    private int pageSize = 15;//每页的条数
    private boolean isSearchOrBound;
    private BoundProductSearchAdapter adapter;
    private ArrayList<BoundProductBean.OrdersListBean> searchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        setRecentViewed();
        setListener();
    }

    /**
     * 设置最近浏览的数据
     */
    private void setRecentViewed() {
        ArrayList<String> historySearch = Model.getInstance(this)
                .getHistorySearchDAO()
                .seleteHistorySearch();
        if (historySearch.size() > 0) {
            rvRecentlyViewed.setVisibility(View.VISIBLE);
            tvNoSearch.setVisibility(View.GONE);
        } else {
            rvRecentlyViewed.setVisibility(View.GONE);
            tvNoSearch.setVisibility(View.VISIBLE);
        }
        ArrayList<String> viewedList = new ArrayList<>();
        for (int i = historySearch.size() - 1; i >= 0; i--) {
            viewedList.add(historySearch.get(i));
        }
        RecentViewedAdapter recentViewidAdapter = new RecentViewedAdapter(this, viewedList);
        rvRecentlyViewed.setAdapter(recentViewidAdapter);
        rvRecentlyViewed.setLayoutManager(new LinearLayoutManager(this));
        recentViewidAdapter.setOnRecentViewedClickListener(new RecentViewedAdapter.OnRecentViewedClickListener() {
            @Override
            public void OnRecentViewed(String content) {
                etSearchCircle.setText(content);
                state = STATE_NORMAL;
                jumpSearchResult(content.trim());
            }
        });
    }

    private void jumpSearchResult(String content) {
        isSearchOrBound = true;
        if (TextUtils.isEmpty(content)) {
            showToast(getString(R.string.search_empt_hint));
        } else {
            //如果已经存在 则不再添加至数据库
            ArrayList<String> historySearch = Model.getInstance(this)
                    .getHistorySearchDAO()
                    .seleteHistorySearch();
            for (int i = 0; i < historySearch.size(); i++) {
                if (content.equals(historySearch.get(i))) {
                    Model.getInstance(this).getHistorySearchDAO().deleteExistSearch(content);
                }
            }
            Model.getInstance(this).getHistorySearchDAO().addHistorySearch(content);
            getDataFromSearch(content);
        }
    }

    /**
     * 搜索的结果
     */
    private void getDataFromSearch(String content) {
        showProgress();
        Map<String, String> params = new HashMap<>();
        params.put("pageno", pageNo + "");
        params.put("pagesize", pageSize + "");
        params.put("QueryKey", content + "");
        OkHttpUtils
                .get()
                .url(GlobalContent.GLOBAL_SEARCH)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                        Logger.e("搜索结果错误" + e);
                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("搜索的数据" + response);
                        RootBean rootBean = new Gson().fromJson(response, RootBean.class);
                        if (Integer.parseInt(rootBean.ErrCode) == 0) {
                            dismissProgress();
                            searchList = new ArrayList<>();
                            SearchShopBean mSearchShopBean = new Gson().fromJson(rootBean.Data, SearchShopBean.class);
                            List<SearchShopBean.ProductsBean> products = mSearchShopBean.getProducts();
                            for (int i = 0; i < products.size(); i++) {
                                BoundProductBean.OrdersListBean ordersListBean = new BoundProductBean.OrdersListBean();
                                ordersListBean.setProductName(products.get(i).getProductName());
                                ordersListBean.setImageUrl(products.get(i).getImagePath());
                                ordersListBean.setSalePrice(Utils.doubleSave2(products.get(i).getMinSalePrice()));
                                ordersListBean.setId(products.get(i).getId());
                                ordersListBean.setShopName(products.get(i).getShopName());
                                searchList.add(ordersListBean);
                            }
                            setData(searchList);
                        }
                    }
                });
    }

    /**
     * 获取关联的订单列表
     */
    private void getOrderListFromNet() {
        showProgress();
        Map<String, String> map = new HashMap<>();
        String userId = CacheUtils.getString(this, GlobalContent.USER);
        map.put("userId", userId);
        map.put("pageSize", pageSize + "");
        map.put("pageNo", pageNo + "");
        OkHttpUtils
                .post()
                .url(GlobalContent.GET_UGC_ORDER_LIST)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Logger.e("获取关联订单错误日志中" + e);
                    }

                    @Override
                    public void onResponse(String response) {
                        dismissProgress();
                        Logger.e("获取关联订单数据：" + response);
                        RootBean rootBean = new Gson().fromJson(response, RootBean.class);
                        if ("0".equals(rootBean.ErrCode)) {
                            BoundProductBean boundProductBean = new Gson().fromJson(rootBean.Data, BoundProductBean.class);
                            setData(boundProductBean.getOrdersList());
                        }
                    }
                });
    }

    private void setData(List<BoundProductBean.OrdersListBean> ordersList) {
        ivNoCreate.setVisibility(View.GONE);
        ivNoResult.setVisibility(View.GONE);
        rv_result.setVisibility(View.VISIBLE);
        rv_layout_search.setVisibility(View.GONE);
        btnClearRecently.setVisibility(View.GONE);
        switch (state) {
            case STATE_NORMAL:
                adapter = new BoundProductSearchAdapter(this, ordersList);
                rv_result.setAdapter(adapter);
                rv_result.setLayoutManager(new LinearLayoutManager(this));
                if (ordersList.size() <= 0) {
                    if (isSearchOrBound) {
                        ivNoCreate.setVisibility(View.VISIBLE);
                        ivNoResult.setVisibility(View.GONE);
                    } else {
                        ivNoCreate.setVisibility(View.GONE);
                        ivNoResult.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case STATE_MORE:
                if (ordersList.size() < pageSize) {
                    adapter.addData(ordersList);
                    rv_result.loadMoreComplete();
                    showToast(getString(R.string.no_more_data1));
                } else {
                    adapter.addData(adapter.getItemCount(), ordersList);
                    rv_result.loadMoreComplete();
                }
                break;
        }
        adapter.setOnSeleteClickListener(new BoundProductSearchAdapter.OnSeleteClickListener() {
            @Override
            public void onSeleteClickListener(int position) {
                Intent intent = new Intent();
                intent.putExtra("BOUND_PRODUCT_NAME", adapter.getList().get(position).getProductName());
                intent.putExtra("BOUND_PRODUCT_PRICE", adapter.getList().get(position).getSalePrice());
                intent.putExtra("BOUND_PRODUCT_ID", adapter.getList().get(position).getId());
                intent.putExtra("BOUND_PRODUCT_IMG", adapter.getList().get(position).getImageUrl());
                intent.putExtra("BOUND_PRODUCT_SHOP_NAME", adapter.getList().get(position).getShopName());
                setResult(3, intent);
                finish();
                exitAnim();
            }
        });
    }

    /**
     * 监听
     */
    private void setListener() {
        etSearchCircle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    /*隐藏软键盘*/
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputMethodManager.isActive()) {
                        inputMethodManager.hideSoftInputFromWindow(BoundProductActivity.this.getCurrentFocus().getWindowToken(), 0);
                    }
                    String mContent = etSearchCircle.getText().toString();
                    state = STATE_NORMAL;
                    jumpSearchResult(mContent.trim());
                }
                return false;
            }
        });

        rv_result.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                pageNo += 1;
                state = STATE_MORE;
                if (isSearchOrBound) {
                    jumpSearchResult(etSearchCircle.getText().toString().trim());
                } else {
                    getOrderListFromNet();
                }
            }
        });
    }

    private void initView() {
        etSearchCircle = (EditText) findViewById(et_search_circle);
        tvSearchCancel = (TextView) findViewById(R.id.tv_search_cancel);
        rlLayout2 = (RelativeLayout) findViewById(R.id.rl_layout2);
        rvRecentlyViewed = (RecyclerView) findViewById(R.id.rv_recently_viewed);
        tvNoSearch = (TextView) findViewById(R.id.tv_no_search);
        btnClearRecently = (Button) findViewById(R.id.btn_clear_recently);
        rv_result = (XRecyclerView) findViewById(R.id.rv_result);
        rv_layout_search = (RelativeLayout) findViewById(R.id.rv_layout_search);
        ivNoCreate = (ImageView) findViewById(R.id.iv_no_create);
        ivNoResult = (ImageView) findViewById(R.id.iv_no_result);
        rv_result.setPullRefreshEnabled(false);

        tvSearchCancel.setOnClickListener(this);
        rlLayout2.setOnClickListener(this);
        btnClearRecently.setOnClickListener(this);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_bound_product;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_search_cancel:
                finish();
                exitAnim();
                break;
            case R.id.rl_layout2:
                state = STATE_NORMAL;
                isSearchOrBound = false;
                pageNo = 1;
                getOrderListFromNet();
                break;
            case R.id.btn_clear_recently:
                Model.getInstance(this).getHistorySearchDAO().deleteHistorySearch();
                setRecentViewed();
                break;
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    // 获取InputMethodManager，隐藏软键盘
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
