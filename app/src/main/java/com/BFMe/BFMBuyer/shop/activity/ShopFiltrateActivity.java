package com.BFMe.BFMBuyer.shop.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.base.recycle.SectionedGridDivider;
import com.BFMe.BFMBuyer.base.recycle.SectionedSpanSizeLookup;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.javaBean.ShopFiltrateBean;
import com.BFMe.BFMBuyer.shop.adapter.ShopFiltrateAdapter;
import com.BFMe.BFMBuyer.utils.CacheUtils;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.UiUtils;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Request;

import static com.BFMe.BFMBuyer.R.id.rv;

/**
 * 店铺列表筛选条件页
 */
public class ShopFiltrateActivity extends IBaseActivity {

    @BindView(rv)
    RecyclerView RVContent;
    private ShopFiltrateAdapter mAdapter;
    private ShopFiltrateBean.DataBean mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        initData();
    }

    private void initData() {
        isBottomToTop = true;

        String shopFiltrate = CacheUtils.getString(this, "shopFiltrate");
        if (shopFiltrate.isEmpty()) {
            getNetData();
        } else {
            mData = mGson.fromJson(shopFiltrate, ShopFiltrateBean.DataBean.class);
            setAdapter();
        }
    }

    private void getNetData() {
        OkHttpUtils
                .get()
                .url(GlobalContent.GET_SHOP_SCREEN)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("店铺筛选条件==" + response);
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (rootBean.ErrCode.equals("0")) {
                            disposeData(mGson.fromJson(rootBean.Data, ShopFiltrateBean.class).getData());
                        } else {
                            showToast(rootBean.ResponseMsg);
                        }
                    }
                });
    }

    private void disposeData(ShopFiltrateBean.DataBean data) {
        mData = data;

        setAdapter();

    }

    private void setAdapter() {
        mAdapter = new ShopFiltrateAdapter(this, mData);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setSpanSizeLookup(new SectionedSpanSizeLookup(mAdapter, gridLayoutManager));
        SectionedGridDivider mDivider = new SectionedGridDivider(this, UiUtils.dip2px(1), Color.parseColor("#F0F0F0"));

        RVContent.addItemDecoration(mDivider);
        RVContent.setLayoutManager(gridLayoutManager);
        RVContent.setAdapter(mAdapter);
    }

    @OnClick(R.id.btn_reset)
    void reset() {
        List<ShopFiltrateBean.DataBean.ShopTypesBean> shopTypes = mData.getShopTypes();
        List<ShopFiltrateBean.DataBean.AreasListBean> areasList = mData.getAreasList();
        for (int i = 0; i < shopTypes.size(); i++) {
            shopTypes.get(i).setSelected(false);
        }
        for (int i = 0; i < areasList.size(); i++) {
            areasList.get(i).setSelected(false);
        }
        mAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.btn_affirm)
    void affirm() {
        String shopTypes = "";
        String areasTypes = "";
        List<ShopFiltrateBean.DataBean.ShopTypesBean> shopList = mData.getShopTypes();
        List<ShopFiltrateBean.DataBean.AreasListBean> areasList = mData.getAreasList();

        for (int i = 0; i < shopList.size(); i++) {
            ShopFiltrateBean.DataBean.ShopTypesBean data = shopList.get(i);
            if (data.isSelected()) {
                shopTypes = shopTypes + data.getValue() + ",";
            }
        }

        for (int i = 0; i < areasList.size(); i++) {
            ShopFiltrateBean.DataBean.AreasListBean data = areasList.get(i);
            if (data.isSelected()) {
                areasTypes = areasTypes + data.getAreaId() + ",";
            }
        }
        Intent intent = new Intent();
        intent.putExtra("shopTypes", shopTypes);
        intent.putExtra("areasTypes", areasTypes);
        setResult(1002, intent);

        CacheUtils.putString(this, "shopFiltrate", mGson.toJson(mData));
        finish();
        bottomexitAnim();
    }


    private void initView() {
        vw_bg.setVisibility(View.GONE);
        setBackButtonVisibility(View.VISIBLE);
        setBackButtonImageResource(R.drawable.icon_group);
        tv_title.setText(R.string.filtrate);
    }

    @Override
    public int initLayout() {
        return R.layout.rv_bg_ffffff_pd_10;
    }
}
