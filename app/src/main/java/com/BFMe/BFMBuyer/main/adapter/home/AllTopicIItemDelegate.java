package com.BFMe.BFMBuyer.main.adapter.home;

import android.view.View;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.recycle.base.ItemViewDelegate;
import com.BFMe.BFMBuyer.base.recycle.base.ViewHolder;
import com.BFMe.BFMBuyer.main.bean.BaseTypeBean;

/**
 * Description：所有话题分类
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/8/15 11:42
 */
class AllTopicIItemDelegate implements ItemViewDelegate<BaseTypeBean> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_button_black_340_44;
    }

    @Override
    public boolean isForViewType(BaseTypeBean item, int position) {
        return item.getType().equals("AllTopic");
    }

    @Override
    public void convert(final ViewHolder holder, BaseTypeBean baseTypeBean, int position) {
        holder.setOnClickListener(R.id.btn_look_all, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allTopicCategoryOnClickListener.allTopicOnClick();
            }
        });
    }

    private AllTopicCategoryOnClickListener allTopicCategoryOnClickListener;
    interface AllTopicCategoryOnClickListener{
        void allTopicOnClick();
    }

    void setAllTopicCategoryOnClickListener(AllTopicCategoryOnClickListener allTopicCategoryOnClickListener) {
        this.allTopicCategoryOnClickListener = allTopicCategoryOnClickListener;
    }
}
