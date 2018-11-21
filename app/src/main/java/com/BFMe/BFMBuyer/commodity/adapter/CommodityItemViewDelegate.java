package com.BFMe.BFMBuyer.commodity.adapter;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.application.MyApplication;
import com.BFMe.BFMBuyer.base.recycle.base.ItemViewDelegate;
import com.BFMe.BFMBuyer.base.recycle.base.ViewHolder;
import com.BFMe.BFMBuyer.javaBean.SearchCommodityBean;
import com.BFMe.BFMBuyer.utils.Utils;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/9/18 20:04
 */
public class CommodityItemViewDelegate implements ItemViewDelegate<SearchCommodityBean.DataBean.ProductsBean> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_search_result;
    }

    @Override
    public boolean isForViewType(SearchCommodityBean.DataBean.ProductsBean item, int position) {
        return true;
    }

    @Override
    public void convert(ViewHolder holder, final SearchCommodityBean.DataBean.ProductsBean data, int position) {
        holder.setImageGlide(R.id.iv_product_icon, data.getImagePath());
        holder.setText(R.id.tv_product_name, data.getProductName());
        holder.setText(R.id.tv_product_price, MyApplication.getContext().getResources().getString(R.string.money_type)+Utils.doubleSave2(data.getMinSalePrice()));
        holder.setImageGlide(R.id.iv_address, data.getCountryLogo());
    }
}
