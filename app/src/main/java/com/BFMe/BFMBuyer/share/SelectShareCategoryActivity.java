package com.BFMe.BFMBuyer.share;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.recycle.CommonAdapter;
import com.BFMe.BFMBuyer.base.recycle.MultiItemTypeAdapter;
import com.BFMe.BFMBuyer.base.recycle.base.ViewHolder;
import com.BFMe.BFMBuyer.javaBean.ShareBean;
import com.BFMe.BFMBuyer.utils.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 分享类型选择页面
 */
public class SelectShareCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rv_select_share_category;
    private List<ShareBean> icons ;
    private String shareTitle;
    private String shareContent;
    private String shareImageUrl;
    private String shareImagePath;
    private String shareUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_share_category);

        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        shareTitle = intent.getStringExtra("shareTitle");
        shareContent = intent.getStringExtra("shareContent");
        shareImageUrl = intent.getStringExtra("shareImageUrl");
        shareImagePath = intent.getStringExtra("shareImagePath");
        shareUrl = intent.getStringExtra("shareUrl");

        icons = new ArrayList<>();
        icons.add(new ShareBean(R.drawable.ssdk_oks_classic_wechat,"微信好友",ShareType.WXSESSION));
        icons.add(new ShareBean(R.drawable.ssdk_oks_classic_wechatmoments,"微信朋友圈", ShareType.WXTIMELINE));
        icons.add(new ShareBean(R.drawable.ssdk_oks_classic_sinaweibo,"新浪微博", ShareType.SINAWEIBO));

        setAdapter();
    }

    private void setAdapter() {
        rv_select_share_category.setLayoutManager(new GridLayoutManager(this,3));
        CommonAdapter<ShareBean> adapter = new CommonAdapter<ShareBean>(this, R.layout.item_share_category, icons) {
            @Override
            protected void convert(ViewHolder holder, ShareBean data, int position) {
                holder.setImageGlide(R.id.iv_icon, data.getIcon());
                holder.setText(R.id.tv_name, data.getName());
            }
        };
        rv_select_share_category.setAdapter(adapter);

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Logger.e("icons"+icons.get(position).getName());
                ShareHelp shareHelp = new ShareHelp(SelectShareCategoryActivity.this);
                shareHelp.start(shareTitle,shareContent,shareImageUrl,shareImagePath,shareUrl,icons.get(position).getType());
                finish();
                overridePendingTransition(0, R.anim.slide_out_to_bottom);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void initView() {
        rv_select_share_category = (RecyclerView)findViewById(R.id.rv_select_share_category);
        findViewById(R.id.activity_select_share_category).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_select_share_category:
                finish();
                overridePendingTransition(0, R.anim.slide_out_to_bottom);
                break;
        }
    }
}
