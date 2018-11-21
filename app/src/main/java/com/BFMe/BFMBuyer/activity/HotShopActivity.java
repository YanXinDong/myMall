package com.BFMe.BFMBuyer.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.adapter.hotshop.PopularShopsAdapter;
import com.BFMe.BFMBuyer.base.IBaseActivity;

import butterknife.BindView;

public class HotShopActivity extends IBaseActivity {

    @BindView(R.id.rv_hot_shop)
    RecyclerView rv_hot_shop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    private void initData() {
        rv_hot_shop.setLayoutManager(new LinearLayoutManager(mContext));
        rv_hot_shop.setAdapter(new PopularShopsAdapter(mContext));
    }

    private void initView() {
        tv_title.setText("热门店铺");
    }

    @Override
    public int initLayout() {
        return R.layout.activity_hot_shop1;
    }
}
