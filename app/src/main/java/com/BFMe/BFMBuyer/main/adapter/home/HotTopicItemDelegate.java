package com.BFMe.BFMBuyer.main.adapter.home;

import android.view.View;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.recycle.base.ItemViewDelegate;
import com.BFMe.BFMBuyer.base.recycle.base.ViewHolder;
import com.BFMe.BFMBuyer.main.bean.BaseTypeBean;
import com.BFMe.BFMBuyer.main.bean.HomeData;

/**
 * Description：话题分类item
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/8/15 11:40
 */
public class HotTopicItemDelegate implements ItemViewDelegate<BaseTypeBean> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_image_165_210;
    }

    @Override
    public boolean isForViewType(BaseTypeBean item, int position) {
        return item.getType().equals("HotTopic");
    }

    @Override
    public void convert(final ViewHolder holder, BaseTypeBean baseTypeBean, int position) {
        final HomeData.HomeDataBean.CategoryBean categoryBean = (HomeData.HomeDataBean.CategoryBean) baseTypeBean.getData();
        holder.setImageGlide(R.id.iv_icon, categoryBean.getImageUrl());
        holder.setOnClickListener(R.id.iv_icon, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topicCategoryOnItemClickListener.onItemClick(String.valueOf(categoryBean.getTopicId()));
            }
        });
    }

    private TopicCategoryOnItemClickListener topicCategoryOnItemClickListener;
    public interface TopicCategoryOnItemClickListener{
        void onItemClick(String tpoicId);
    }

    public void setTopicCategoryOnItemClickListener(TopicCategoryOnItemClickListener topicCategoryOnItemClickListener) {
        this.topicCategoryOnItemClickListener = topicCategoryOnItemClickListener;
    }
}
