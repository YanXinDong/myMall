package com.BFMe.BFMBuyer.main.adapter.home;

import android.view.View;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.recycle.base.ItemViewDelegate;
import com.BFMe.BFMBuyer.base.recycle.base.ViewHolder;
import com.BFMe.BFMBuyer.main.bean.BaseTypeBean;

/**
 * Created by BFMe.miao on 2018/2/28.
 */

public class AllLimitedItemDelegate2  implements ItemViewDelegate<BaseTypeBean> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_button_black_340_44;
    }

    @Override
    public boolean isForViewType(BaseTypeBean item, int position) {
        return item.getType().equals("AllLimited2");
    }

    @Override
    public void convert(final ViewHolder holder, BaseTypeBean baseTypeBean, int position) {
        holder.setOnClickListener(R.id.btn_look_all, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allLimitedOnClickListener.allLimitedOnClick();
            }
        });
    }

    private AllLimitedOnClickListener2 allLimitedOnClickListener;
    public interface AllLimitedOnClickListener2{
        void allLimitedOnClick();
    }

    public void setAllLimitedOnClickListener(AllLimitedOnClickListener2 allLimitedOnClickListener) {
        this.allLimitedOnClickListener = allLimitedOnClickListener;
    }
}
