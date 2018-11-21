package com.BFMe.BFMBuyer.main.adapter.home;

import android.view.View;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.application.MyApplication;
import com.BFMe.BFMBuyer.base.recycle.base.ItemViewDelegate;
import com.BFMe.BFMBuyer.base.recycle.base.ViewHolder;
import com.BFMe.BFMBuyer.main.bean.BaseTypeBean;
import com.BFMe.BFMBuyer.main.bean.HotProductsBean;
import com.BFMe.BFMBuyer.utils.Utils;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/11/1 14:23
 */
public class HotProductsItemDelegate implements ItemViewDelegate<BaseTypeBean> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_search_result;
    }

    @Override
    public boolean isForViewType(BaseTypeBean item, int position) {
        return item.getType().equals("HotProducts");
    }

    @Override
    public void convert(ViewHolder holder, BaseTypeBean baseTypeBean, int position) {
        final HotProductsBean.DataBean data = (HotProductsBean.DataBean) baseTypeBean.getData();

        holder.setImageGlide(R.id.iv_product_icon, data.getImagePath());
        holder.setText(R.id.tv_product_name, data.getName());
        holder.setText(R.id.tv_product_price, MyApplication.getContext().getResources().getString(R.string.money_type)+ Utils.doubleSave2(data.getPrice()));
        holder.setImageGlide(R.id.iv_address, data.getCountryIcon());

        holder.setOnClickListener(R.id.rl_subjectproduct, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hotProductsItemOnClickListener.onHotProductsItemClick(data.getId());
            }
        });
    }

    private HotProductsItemOnClickListener hotProductsItemOnClickListener;
    public interface HotProductsItemOnClickListener{
        void onHotProductsItemClick(String productId);
    }

    public void setHotProductsItemOnClickListener(HotProductsItemOnClickListener hotProductsItemOnClickListener) {
        this.hotProductsItemOnClickListener = hotProductsItemOnClickListener;
    }
}
