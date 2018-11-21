package com.BFMe.BFMBuyer.main.adapter.home;

import android.view.View;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.application.MyApplication;
import com.BFMe.BFMBuyer.base.recycle.base.ItemViewDelegate;
import com.BFMe.BFMBuyer.base.recycle.base.ViewHolder;
import com.BFMe.BFMBuyer.main.bean.BaseTypeBean;
import com.BFMe.BFMBuyer.main.bean.HomeData;
import com.BFMe.BFMBuyer.utils.Utils;

/**
 * Description：限时购item
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/8/15 11:51
 */
public class LimitedItemDelegate implements ItemViewDelegate<BaseTypeBean>{
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_commodity;
    }

    @Override
    public boolean isForViewType(BaseTypeBean item, int position) {
        return item.getType().equals("Limited");
    }

    @Override
    public void convert(final ViewHolder holder, BaseTypeBean baseTypeBean, int position) {
        final HomeData.HomeDataBean.ProductBean data = (HomeData.HomeDataBean.ProductBean) baseTypeBean.getData();
        holder.setImageGlide(R.id.iv_icon,data.getProductImagePath());
        holder.setImageCircularGlide(R.id.iv_state,data.getCountryAreaImgPath());
        holder.setText(R.id.tv_name,data.getProductName());
        holder.setText(R.id.tv_price, MyApplication.getContext().getString(R.string.money_type)+Utils.doubleSave2(data.getMinSalePrice()));

        holder.setOnClickListener(R.id.rl_commodity_info, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limitedItemOnClickListener.onLimitedItemClick(String.valueOf(data.getProductId()));
            }
        });
    }

    private LimitedItemOnClickListener limitedItemOnClickListener;
    public interface LimitedItemOnClickListener{
        void onLimitedItemClick(String productId);
    }

    public LimitedItemOnClickListener getLimitedItemOnClickListener() {
        return limitedItemOnClickListener;
    }

    public void setLimitedItemOnClickListener(LimitedItemOnClickListener limitedItemOnClickListener) {
        this.limitedItemOnClickListener = limitedItemOnClickListener;
    }
}
