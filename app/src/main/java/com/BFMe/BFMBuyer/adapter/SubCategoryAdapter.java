package com.BFMe.BFMBuyer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.recycle.SectionAdapter;
import com.BFMe.BFMBuyer.base.recycle.base.ViewHolder;
import com.BFMe.BFMBuyer.commodity.onclick.SubCategoryItemOnClick;
import com.BFMe.BFMBuyer.javaBean.SubCategoryData;

import java.util.List;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/9/12 19:49
 */
public class SubCategoryAdapter extends SectionAdapter {

    private LayoutInflater mLayoutInflater;
    private String mImgPath;
    private List<SubCategoryData.CategoryBean> category;

    public SubCategoryAdapter(Context context, SubCategoryData data) {
        mLayoutInflater = LayoutInflater.from(context);
        mImgPath = data.getImgPath();
        this.category = data.getCategory();

        setupPosition();
    }

    @Override
    protected boolean hasHeader() {
        return true;
    }

    @Override
    protected int getSectionCount() {
        return category.size();
    }

    @Override
    protected int getItemCountForSection(int section) {
        return category.get(section).getSubCategory().size();
    }

    @Override
    protected ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        View inflate = mLayoutInflater.inflate(R.layout.item_image_360_96, parent, false);
        return new ViewHolder(parent.getContext(), inflate);
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
        holder.setImageGlide(R.id.iv_icon, mImgPath);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(ViewHolder holder, int section) {
        holder.setText(R.id.tv_title, category.get(section).getName());
    }

    @Override
    protected void onBindItemViewHolder(ViewHolder holder, int section, int position) {
        SubCategoryData.CategoryBean.SubCategoryBean data = category.get(section).getSubCategory().get(position);
        holder.setText(R.id.tv_content, data.getName());

        holder.setOnClickListener(R.id.tv_content, new SubCategoryItemOnClick(holder.getContext(),data.getName(),data.getId()));
    }


}
