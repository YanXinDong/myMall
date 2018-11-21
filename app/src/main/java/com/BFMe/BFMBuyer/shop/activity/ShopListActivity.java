package com.BFMe.BFMBuyer.shop.activity;

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
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.javaBean.SearchConditionBean;
import com.BFMe.BFMBuyer.javaBean.ShopListData;
import com.BFMe.BFMBuyer.search.adapter.SearchConditionAdapter;
import com.BFMe.BFMBuyer.shop.adapter.ShopListAdapter;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.UiUtils;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.BFMe.BFMBuyer.view.DividerGridItemDecoration;
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

/**
 * 店铺列表页
 */
public class ShopListActivity extends IBaseActivity {

    @BindView(R.id.ll_sort)
    LinearLayout LLSort;
    @BindView(R.id.cb_rank)
    CheckBox cbRank;
    @BindView(R.id.xrv_content)
    XRecyclerView XRVContent;

    private int mShopType;//|类型1、保障货源 2、保证进口 3、保障卖家
    private int mSort = 1;//排序条件 1：人气推荐，2：销量
    private String mAreaId;//国家地区id
    private String mIntercontinentalId;//洲际id
    private int mBrandId;//品牌id
    private String mShopMold;//店铺经营方式1:自营店铺,2:个人店铺,3:企业店铺

    private int pageNo = 1;
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
    private ShopListAdapter mAdapter;
    private List<SearchConditionBean> mTypeData = new ArrayList<>();
    private SFPopupWindow mPP;

    @OnClick(R.id.btn_filtrate)
    void showHintFiltrate() {
        startActivityForResult(new Intent(this, ShopFiltrateActivity.class), 1001);
        bottomStartAnim();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String titleName = getIntent().getStringExtra("TitleName");
        tv_title.setText(titleName);
        XRVContent.setFootViewText(getString(R.string.load), getString(R.string.no_more_data));
        initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1001 && resultCode ==1002) {
            mShopMold = data.getStringExtra("shopTypes");
            mAreaId = data.getStringExtra("areasTypes");
        }

        pageNo = 1;
        state = STATE_REFRESH;
        getNetData();
    }

    private void initData() {
        Intent intent = getIntent();
        mShopType = intent.getIntExtra("ShopType", 0);
        mAreaId = intent.getStringExtra("AreaId");
        mIntercontinentalId = intent.getStringExtra("IntercontinentalId");
        mBrandId = intent.getIntExtra("BrandId",0);

        mTypeData.add(new SearchConditionBean(getString(R.string.popularity_recommend), true));
        mTypeData.add(new SearchConditionBean(getString(R.string.sales_volume), false));

        getNetData();
    }

    private void getNetData() {
        showProgress();
        Map<String, String> params = new HashMap<>();
        params.put("pageSize", "10");
        params.put("pageNo", String.valueOf(pageNo));
        if (mShopType != 0) {
            params.put("ShopType", String.valueOf(mShopType));//|类型1、保障货源 2、保证进口 3、保障卖家
        }
        params.put("Sort", String.valueOf(mSort));//排序条件 1：人气推荐，2：销量
        if (mAreaId != null && !mAreaId.isEmpty()) {
            params.put("AreaId", String.valueOf(mAreaId));//国家地区id
        }
        if (mIntercontinentalId != null && !mIntercontinentalId.isEmpty()) {
            params.put("IntercontinentalId", String.valueOf(mIntercontinentalId));//洲际id
        }
        if (mBrandId != 0) {
            params.put("BrandId", String.valueOf(mBrandId));//品牌id
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
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (rootBean.ErrCode.equals("0")) {
                            showData(mGson.fromJson(rootBean.Data, ShopListData.class).getData());
                        } else {
                            showToast(rootBean.ResponseMsg);
                        }
                    }
                });
    }

    private void showData(List<ShopListData.DataBean> shopListData) {

        switch (state) {
            case STATE_NORMAL:
                setAdapter(shopListData);
                setListener();
                setNoMore(shopListData);
                break;
            case STATE_REFRESH:
                state = STATE_NORMAL;
                XRVContent.refreshComplete();
                if (mAdapter != null) {
                    mAdapter.clearData();
                    mAdapter.addData(shopListData);
                } else {
                    setAdapter(shopListData);
                }
                setNoMore(shopListData);
                break;
            case STATE_MORE:
                state = STATE_NORMAL;
                XRVContent.loadMoreComplete();
                if (mAdapter != null) {
                    mAdapter.addData(mAdapter.getItemCount(), shopListData);
                } else {
                    setAdapter(shopListData);
                }
                setNoMore(shopListData);
                break;

        }

    }

    private void setNoMore(List<ShopListData.DataBean> shopListData) {
        if (shopListData.size() < 5) {
            XRVContent.setNoMore(true);
        }
    }

    private void setAdapter(List<ShopListData.DataBean> shopListData) {
        XRVContent.setLayoutManager(new GridLayoutManager(mContext, 2));
        XRVContent.addItemDecoration(new MyDividerGridItemDecoration(this, UiUtils.dip2px(10),UiUtils.dip2px(10), Color.parseColor("#FFFFFF")));
        mAdapter = new ShopListAdapter(this, R.layout.item_shop_list, shopListData);
        XRVContent.addItemDecoration(new DividerGridItemDecoration(mContext));
        XRVContent.setAdapter(mAdapter);
    }

    private void setListener() {
        XRVContent.setLoadingListener(new XRecyclerView.LoadingListener() {
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

        cbRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbRank.isChecked()) {
                    showPP();
                } else {
                    mPP.dismiss();
                }
            }
        });
    }

    /**
     * 显示一个popupWindow
     */
    private void showPP() {
        View v = LayoutInflater.from(this).inflate(R.layout.pure_rv, null, false);
        mPP = new SFPopupWindow(v, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT, true);
        mPP.setAnimationStyle(R.style.AddressDialogAnim);
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框。不知道是什么原因
        mPP.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击窗口外边窗口消失
        mPP.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        mPP.setFocusable(true);
        //添加pop窗口关闭事件
        mPP.setOnDismissListener(new PoponDismissListener());
        initPPView(v);
        mPP.showAsDropDown(LLSort);

    }

    /**
     * 初始化view
     *
     * @param v
     */
    private void initPPView(View v) {
        RecyclerView rv_content = (RecyclerView) v.findViewById(R.id.rv_content);
        rv_content.setLayoutManager(new LinearLayoutManager(this));
        SearchConditionAdapter searchConditionAdapter = new SearchConditionAdapter();
        rv_content.setAdapter(searchConditionAdapter);
        initPPData(searchConditionAdapter);
    }

    /**
     * 初始化数据
     *
     * @param adapter
     */
    private void initPPData(SearchConditionAdapter adapter) {
        adapter.setData(mTypeData);

        adapter.setOnItemClickListener(new SearchConditionAdapter.OnItemClickListener() {
            @Override
            public void itemClick(int position) {
                for (int i = 0; i < mTypeData.size(); i++) {
                    if (position == i) {
                        cbRank.setText(mTypeData.get(i).getTitle());
                        mSort = i + 1;
                        mTypeData.get(i).setSelect(true);
                    } else {
                        mTypeData.get(i).setSelect(false);
                    }
                }
                refreshData();
                mPP.dismiss();
            }
        });
    }

    private class PoponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            cbRank.setChecked(false);
        }
    }

    private void refreshData() {
        pageNo = 1;
        state = STATE_REFRESH;
        getNetData();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_shop_list;
    }
}
