package com.BFMe.BFMBuyer.shop.adapter;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.recycle.base.ItemViewDelegate;
import com.BFMe.BFMBuyer.base.recycle.base.ViewHolder;
import com.BFMe.BFMBuyer.main.bean.BaseTypeBean;

/**
 * Description：店铺地区列表title
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/9/7 16:01
 */
public class RegionTitleItemDelegate implements ItemViewDelegate<BaseTypeBean> {
    @Override
    public int getItemViewLayoutId() {

        return R.layout.item_title_360_35_bg_f0f0f0;
    }

    @Override
    public boolean isForViewType(BaseTypeBean item, int position) {
        return item.getType().equals("title");
    }

    @Override
    public void convert(ViewHolder holder, BaseTypeBean baseTypeBean, int position) {
        String title = (String) baseTypeBean.getData();
        holder.setText(R.id.tv_title,title);
    }
}
