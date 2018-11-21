package com.BFMe.BFMBuyer.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.adapter.SubCategoryAdapter;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.base.recycle.SectionedGridDivider;
import com.BFMe.BFMBuyer.base.recycle.SectionedSpanSizeLookup;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.javaBean.SubCategoryData;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.UiUtils;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import okhttp3.Request;

import static com.BFMe.BFMBuyer.R.id.rv;

/**
 * 二级分类页面
 */
public class SubCategoryActivity extends IBaseActivity {

    @BindView(rv)
    RecyclerView RVContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    private void initData() {
        isBottomToTop = true;
        Intent intent = getIntent();
        String title = intent.getStringExtra("Title");
        int categoryId = intent.getIntExtra("CategoryId", 0);
        tv_title.setText(title);

        getNetData(String.valueOf(categoryId));
    }

    private void getNetData(String id) {
        OkHttpUtils
                .get()
                .addParams("categoryId", id)
                .url(GlobalContent.GET_SUB_CATEGORIES_BY_ID)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("子分类=="+response);
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (rootBean.ErrCode.equals("0")) {
                            setAdapter(mGson.fromJson(rootBean.Data, SubCategoryData.class));
                        } else {
                            showToast(rootBean.ResponseMsg);
                        }
                    }
                });
    }

    private void setAdapter(SubCategoryData subCategoryData) {
        SubCategoryAdapter adapter = new SubCategoryAdapter(this,subCategoryData);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        gridLayoutManager.setSpanSizeLookup(new SectionedSpanSizeLookup(adapter,gridLayoutManager));
        SectionedGridDivider mDivider = new SectionedGridDivider(this, UiUtils.dip2px(1), Color.parseColor("#F0F0F0"));

        RVContent.addItemDecoration(mDivider);
        RVContent.setLayoutManager(gridLayoutManager);
        RVContent.setAdapter(adapter);
    }

    private void initView() {
        vw_bg.setVisibility(View.GONE);
        setBackButtonVisibility(View.VISIBLE);
        setBackButtonImageResource(R.drawable.icon_group);

        RVContent.setBackgroundResource(R.color.white);

        findViewById(R.id.ll_menu).setVisibility(View.GONE);
    }

    @Override
    public int initLayout() {
        return R.layout.rv_bg_ffffff_pd_10;
    }
}
