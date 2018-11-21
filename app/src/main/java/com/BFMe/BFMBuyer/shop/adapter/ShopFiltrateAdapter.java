package com.BFMe.BFMBuyer.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.recycle.SectionAdapter;
import com.BFMe.BFMBuyer.base.recycle.base.ViewHolder;
import com.BFMe.BFMBuyer.javaBean.ShopFiltrateBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：店铺筛选条件adapter
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/9/11 14:56
 */
public class ShopFiltrateAdapter extends SectionAdapter {

    private LayoutInflater mLayoutInflater;
    private List<String> title;
    private ShopFiltrateBean.DataBean mData;

    public ShopFiltrateAdapter(Context context, ShopFiltrateBean.DataBean data) {
        mData = data;
        title = new ArrayList<>();
        title.add(context.getString(R.string.shop_type));
        title.add(context.getString(R.string.country_region));
        mLayoutInflater = LayoutInflater.from(context);

        setupPosition();//调用父类方法从新计算数据
    }

    @Override
    protected boolean hasHeader() {
        return false;
    }

    @Override
    protected int getSectionCount() {
        return title.size();
    }

    @Override
    protected int getItemCountForSection(int section) {
        if (section == 0) {
            return mData.getShopTypes().size();
        } else {
            return mData.getAreasList().size();
        }
    }

    @Override
    protected ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected ViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        View inflate = mLayoutInflater.inflate(R.layout.item_shop_filtrate_head, parent, false);
        return new ViewHolder(parent.getContext(), inflate);
    }

    @Override
    protected ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View inflate = mLayoutInflater.inflate(R.layout.item_shop_filtrate_body, parent, false);
        return new ViewHolder(parent.getContext(), inflate);
    }

    @Override
    protected void onBindHeaderViewHolder(ViewHolder holder) {

    }

    @Override
    protected void onBindSectionHeaderViewHolder(ViewHolder holder, int section) {
        holder.setText(R.id.tv_title, title.get(section));
    }

    @Override
    protected void onBindItemViewHolder(final ViewHolder holder, final int section, final int position) {
        final List<ShopFiltrateBean.DataBean.ShopTypesBean> shopTypes = mData.getShopTypes();
        final List<ShopFiltrateBean.DataBean.AreasListBean> areasList = mData.getAreasList();
        if (section == 0) {
            ShopFiltrateBean.DataBean.ShopTypesBean shopData = shopTypes.get(position);
            holder.setText(R.id.tv_content, shopData.getShopTypeName());
            if (shopData.isSelected()) {
                holder.setBackgroundRes(R.id.tv_content, R.color.blue_CDDFFB);
            } else {
                holder.setBackgroundRes(R.id.tv_content, R.color.translucence_gray_fafafafa);
            }
        } else {
            ShopFiltrateBean.DataBean.AreasListBean areasData = areasList.get(position);

            holder.setText(R.id.tv_content, areasData.getAreaName());
            if (areasData.isSelected()) {
                holder.setBackgroundRes(R.id.tv_content, R.color.blue_CDDFFB);
            } else {
                holder.setBackgroundRes(R.id.tv_content, R.color.translucence_gray_fafafafa);
            }
        }

        holder.setOnClickListener(R.id.tv_content, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (section == 0) {
                    ShopFiltrateBean.DataBean.ShopTypesBean shopData = shopTypes.get(position);
                    if (shopData.isSelected()) {
                        shopData.setSelected(false);
                    } else {
                        if (validationMaxSelected()) {
                            Toast.makeText(holder.getContext(), "最多五个", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        shopData.setSelected(true);
                    }
                } else {
                    ShopFiltrateBean.DataBean.AreasListBean areasData = areasList.get(position);
                    if (areasData.isSelected()) {
                        areasData.setSelected(false);
                    } else {
                        if (validationMaxSelected()) {
                            Toast.makeText(holder.getContext(), "最多五个", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        areasData.setSelected(true);
                    }
                }

                notifyItemChanged(holder.getAdapterPosition());
            }
        });
    }

    private boolean validationMaxSelected() {

        List<ShopFiltrateBean.DataBean.ShopTypesBean> shopTypes = mData.getShopTypes();
        List<ShopFiltrateBean.DataBean.AreasListBean> areasList = mData.getAreasList();
        int number = 0;
        for (int i = 0; i < shopTypes.size(); i++) {
            if (shopTypes.get(i).isSelected()) {
                number++;
            }
        }
        for (int i = 0; i < areasList.size(); i++) {
            if (areasList.get(i).isSelected()) {
                number++;
            }
        }

        return number >= 5;
    }
}