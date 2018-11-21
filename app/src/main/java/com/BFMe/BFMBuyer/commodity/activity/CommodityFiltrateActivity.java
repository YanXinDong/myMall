package com.BFMe.BFMBuyer.commodity.activity;

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
import com.BFMe.BFMBuyer.commodity.adapter.CommodityFiltrateAdapter;
import com.BFMe.BFMBuyer.javaBean.SearchCommodityBean;
import com.BFMe.BFMBuyer.utils.UiUtils;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.BFMe.BFMBuyer.R.id.rv;

/**
 * 商品筛选页面
 */
public class CommodityFiltrateActivity extends IBaseActivity {

    @BindView(rv)
    RecyclerView RVContent;
    private CommodityFiltrateAdapter mAdapter;
    private SearchCommodityBean.DataBean.FacetResultsBean mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    private void initData() {
        isBottomToTop = true;
        String commodityResults = getIntent().getStringExtra("CommodityFiltrate");
        mData = mGson.fromJson(commodityResults, SearchCommodityBean.DataBean.FacetResultsBean.class);
        setAdapter();

    }

    private void setAdapter() {
        mAdapter = new CommodityFiltrateAdapter(this, mData);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setSpanSizeLookup(new SectionedSpanSizeLookup(mAdapter, gridLayoutManager));
        SectionedGridDivider mDivider = new SectionedGridDivider(this, UiUtils.dip2px(1), Color.parseColor("#F0F0F0"));

        RVContent.addItemDecoration(mDivider);
        RVContent.setLayoutManager(gridLayoutManager);
        RVContent.setAdapter(mAdapter);
    }

    @OnClick(R.id.btn_reset)
    void reset() {

        Intent intent = new Intent();
        intent.putExtra("brand", "");
        intent.putExtra("country", "");
        intent.putExtra("classify", "");
        intent.putExtra("maxPrice", "");
        intent.putExtra("minPrice", "");
        setResult(1004, intent);

        finish();
        bottomexitAnim();
    }

    @OnClick(R.id.btn_affirm)
    void affirm() {
        String  brand = "";//品牌
        String country = "";//国家
        String classify = "";//分类

        List<SearchCommodityBean.DataBean.FacetResultsBean.BnfBean> bnf = mData.getBnf();
        List<SearchCommodityBean.DataBean.FacetResultsBean.CtryfBean> ctryf = mData.getCtryf();
        List<SearchCommodityBean.DataBean.FacetResultsBean.TcnfBean> tcnf = mData.getTcnf();

        for (int i = 0; i < bnf.size(); i++) {
            SearchCommodityBean.DataBean.FacetResultsBean.BnfBean data = bnf.get(i);
            if (data.isSelected()) {
                brand = brand + data.getName() + ",";
            }
        }

        for (int i = 0; i < ctryf.size(); i++) {
            SearchCommodityBean.DataBean.FacetResultsBean.CtryfBean data = ctryf.get(i);
            if (data.isSelected()) {
                country = country + data.getName() + ",";
            }
        }

        for (int i = 0; i < tcnf.size(); i++) {
            SearchCommodityBean.DataBean.FacetResultsBean.TcnfBean data = tcnf.get(i);
            if (data.isSelected()) {
                classify = classify + data.getName() + ",";
            }
        }

        Map<String, String> price = getPrices.getPrice();
        Intent intent = new Intent();
        intent.putExtra("brand", brand);
        intent.putExtra("country", country);
        intent.putExtra("classify", classify);

        String maxPrice = price.get("maxPrice");
        intent.putExtra("maxPrice",maxPrice );

        String minPrice = price.get("minPrice");
        if(!maxPrice.isEmpty() && minPrice.isEmpty()) {
            intent.putExtra("minPrice","0" );
        }else {
            intent.putExtra("minPrice", minPrice);
        }
        setResult(1003, intent);

        finish();
        bottomexitAnim();
    }

    private GetPrices getPrices;

    public interface GetPrices{
        Map<String,String> getPrice();
    }

    public void setGetPrices(GetPrices getPrices) {
        this.getPrices = getPrices;
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
