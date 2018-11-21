package com.BFMe.BFMBuyer.main.adapter.home;

import android.view.View;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.recycle.base.ItemViewDelegate;
import com.BFMe.BFMBuyer.base.recycle.base.ViewHolder;
import com.BFMe.BFMBuyer.main.bean.BaseTypeBean;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/11/8 11:43
 */
class HotProductsTitleItemDelegate implements ItemViewDelegate<BaseTypeBean> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_look_more;
    }

    @Override
    public boolean isForViewType(BaseTypeBean item, int position) {
        return item.getType().equals("HotProductsTitle");
    }

    @Override
    public void convert(ViewHolder holder, BaseTypeBean baseTypeBean, int position) {
        String data = (String) baseTypeBean.getData();
        holder.getView(R.id.tv_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allProductsOnClickListener.allProductsOnClick();
            }
        });
        holder.setText(R.id.tv_title,data);
    }

    private AllProductsOnClickListener allProductsOnClickListener;

    interface AllProductsOnClickListener{
        void allProductsOnClick();
    }

    void setAllProductsOnClickListener(AllProductsOnClickListener allProductsOnClickListener) {
        this.allProductsOnClickListener = allProductsOnClickListener;
    }
}
