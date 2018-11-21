package com.BFMe.BFMBuyer.shop.adapter;

import android.app.Activity;
import android.view.View;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.recycle.CommonAdapter;
import com.BFMe.BFMBuyer.base.recycle.base.ViewHolder;
import com.BFMe.BFMBuyer.javaBean.ShopListData;
import com.BFMe.BFMBuyer.main.model.ShopListItemModel;
import com.BFMe.BFMBuyer.main.presenter.ShopListItemOnClickListener;

import java.util.List;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/9/8 15:43
 */
public class ShopListAdapter extends CommonAdapter<ShopListData.DataBean> {

    private ShopListItemOnClickListener shopListItemOnClickListener;
    private List<ShopListData.DataBean> mDatas;
    private Activity activity;
    public ShopListAdapter(Activity context, int layoutId, List<ShopListData.DataBean> datas) {
        super(context, layoutId, datas);
        activity = context;
        mDatas = datas;
        shopListItemOnClickListener = new ShopListItemModel(context);
    }

    @Override
    protected void convert(ViewHolder holder, final ShopListData.DataBean dataBean, int position) {
        holder.setImageGlideCircular(R.id.iv_shop_img,dataBean.getImageUrl());
        holder.setText(R.id.tv_shop_name,dataBean.getShopName());

        if(dataBean.getArea() == null || dataBean.getArea().isEmpty()) {
            holder.setText(R.id.tv_shop_county,activity.getString(R.string.rest));
        }else {
            holder.setText(R.id.tv_shop_county,dataBean.getArea());
        }

        if(dataBean.getDescription() == null || dataBean.getDescription().isEmpty()) {
            holder.setText(R.id.tv_shop_detail,activity.getString(R.string.shop_info_hint));
        }else {
            holder.setText(R.id.tv_shop_detail,dataBean.getDescription());
        }

        holder.setOnClickListener(R.id.rl_shop_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopListItemOnClickListener.onItemClick(dataBean.getId());
            }
        });
    }

    public void clearData() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    public void addData(List<ShopListData.DataBean> shopListData) {
        addData(0,shopListData);
    }

    public void addData(int position ,List<ShopListData.DataBean> shopListData) {
        if(shopListData != null && shopListData.size() > 0) {
            mDatas.addAll(position,shopListData);
            notifyDataSetChanged();
        }
    }
}
