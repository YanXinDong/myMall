package com.BFMe.BFMBuyer.shop.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.shop.adapter.BrandFiltrateAdapter;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.base.recycle.CommonAdapter;
import com.BFMe.BFMBuyer.base.recycle.MultiItemTypeAdapter;
import com.BFMe.BFMBuyer.base.recycle.base.ViewHolder;
import com.BFMe.BFMBuyer.javaBean.BrandFiltrateBean;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * 店铺品牌选择页
 */
public class BrandFiltrateActivity extends IBaseActivity {

    private RecyclerView rv_brand;
    private RecyclerView rv_menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vw_bg.setVisibility(View.GONE);
        setBackButtonVisibility(View.VISIBLE);
        setBackButtonImageResource(R.drawable.icon_group);
        rv_brand = (RecyclerView)findViewById(R.id.rv_brand);
        rv_menu = (RecyclerView)findViewById(R.id.rv_menu);
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
                .url(GlobalContent.GET_BRAND_LIST)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (rootBean.ErrCode.equals("0")) {
                            Logger.e("店铺品牌列表=="+rootBean.Data);
                            disposeData(mGson.fromJson(rootBean.Data, BrandFiltrateBean.class).getBrand());
                        } else {
                            showToast(rootBean.ResponseMsg);
                        }
                    }
                });
    }

    /**
     * 数据处理
     * @param brand gson解析后数据
     */
    private void disposeData(BrandFiltrateBean.BrandBean brand) {
        List<String> titleList = new ArrayList<>();
        List<BrandFiltrateBean.BrandBean.HotDataBean> contentList = new ArrayList<>();

        Map<String, List<BrandFiltrateBean.BrandBean.HotDataBean>> data = brand.getData();
        List<BrandFiltrateBean.BrandBean.HotDataBean> hotData = brand.getHotData();

        titleList.add("♔");
        for (BrandFiltrateBean.BrandBean.HotDataBean hot :hotData ){
            hot.setBrandChar(getString(R.string.hot_brand));
            contentList.add(hot);
        }

        int position = hotData.size();

        for (Map.Entry<String, List<BrandFiltrateBean.BrandBean.HotDataBean>> entry :data.entrySet()){
            List<BrandFiltrateBean.BrandBean.HotDataBean> value = entry.getValue();
            if(value !=null && !value.isEmpty()) {//如果集合为空就不用添加数据了
                titleList.add(entry.getKey());

                contentList.addAll(position,value);
                position += value.size();
            }
        }
        titleList.add("#");
        setAdapter(contentList,titleList);
    }

    private void setAdapter(final List<BrandFiltrateBean.BrandBean.HotDataBean> contentList, final List<String> titleList) {
        rv_brand.setLayoutManager(new LinearLayoutManager(mContext));
        BrandFiltrateAdapter adapter = new BrandFiltrateAdapter(mContext, R.layout.item_content_360_50_bg_ffffff,contentList);
        rv_brand.setAdapter(adapter);
        //利用自定义分割线给分类添加粘性头部
        StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(adapter);
        rv_brand.addItemDecoration(headersDecor);

        rv_menu.setLayoutManager(new LinearLayoutManager(mContext));
        CommonAdapter<String> commonAdapter = new CommonAdapter<String>(mContext,R.layout.item_brand_menu,titleList) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.tv_menu,s);
            }
        };
        rv_menu.setAdapter(commonAdapter);

        commonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                String s = titleList.get(position);
                for(int i = 0; i < contentList.size(); i++) {
                    if(s.equals(contentList.get(i).getBrandChar())) {
                        rv_brand.scrollToPosition(i);
                        Logger.e("position=="+position+"    联动position=="+i);
                        return;
                    }else if(s.equals("♔")) {
                        rv_brand.scrollToPosition(0);
                    }
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    public int initLayout() {
        return R.layout.activity_brand_filtrate;
    }
}
