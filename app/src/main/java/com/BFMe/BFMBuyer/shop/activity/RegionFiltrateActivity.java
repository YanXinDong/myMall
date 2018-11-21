package com.BFMe.BFMBuyer.shop.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.shop.adapter.RegionFiltrateAdapter;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.javaBean.RegionFiltrateBean;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.main.bean.BaseTypeBean;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * 店铺列表地区选择页面
 */
public class RegionFiltrateActivity extends IBaseActivity {

    private RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vw_bg.setVisibility(View.GONE);
        setBackButtonVisibility(View.VISIBLE);
        setBackButtonImageResource(R.drawable.icon_group);
        rv = (RecyclerView)findViewById(R.id.rv);
        isBottomToTop = true;
        initData();
    }

    private void initData() {
        String titleName = getIntent().getStringExtra("TitleName");
        tv_title.setText(titleName);

        getNetData();
    }

    private void getNetData() {
        OkHttpUtils
                .get()
                .url(GlobalContent.GET_REGION_FILTRATE)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (rootBean.ErrCode.equals("0")) {
                            Logger.e("店铺地区列表=="+rootBean.Data);
                            disposeData(mGson.fromJson(rootBean.Data, RegionFiltrateBean.class).getAreaData());
                        } else {
                            showToast(rootBean.ResponseMsg);
                        }
                    }
                });
    }

    private void disposeData(RegionFiltrateBean.AreaDataBean areaData) {
        List<RegionFiltrateBean.AreaDataBean.ConuntryBean> country = areaData.getConuntry();
        List<RegionFiltrateBean.AreaDataBean.IntercontinentalBean> intercontinental = areaData.getIntercontinental();

        List<BaseTypeBean> baseTypeData = new ArrayList<>();

        baseTypeData.add(new BaseTypeBean("title",getString(R.string.continent_pavilion)));

        for(int i = 0; i < intercontinental.size(); i++) {
            baseTypeData.add(new BaseTypeBean("content",intercontinental.get(i)));
        }

        baseTypeData.add(new BaseTypeBean("title",getString(R.string.country_region)));

        for(int i = 0; i < country.size(); i++) {
            baseTypeData.add(new BaseTypeBean("content",country.get(i)));
        }


        setAdapter(baseTypeData);
    }

    private void setAdapter(List<BaseTypeBean> baseTypeData) {
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        rv.setAdapter(new RegionFiltrateAdapter(mContext,baseTypeData));
    }

    @Override
    public int initLayout() {
        return R.layout.activity_rv;
    }
}
