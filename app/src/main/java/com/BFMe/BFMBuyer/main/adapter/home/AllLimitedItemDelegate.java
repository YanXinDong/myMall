package com.BFMe.BFMBuyer.main.adapter.home;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.recycle.base.ItemViewDelegate;
import com.BFMe.BFMBuyer.base.recycle.base.ViewHolder;
import com.BFMe.BFMBuyer.main.bean.BaseTypeBean;

/**
 * Description：限时购查看更多
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/8/15 11:46
 */
public class AllLimitedItemDelegate implements ItemViewDelegate<BaseTypeBean> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_look_more;
    }

    @Override
    public boolean isForViewType(BaseTypeBean item, int position) {
        return item.getType().equals("AllLimited");
    }

    @Override
    public void convert(final ViewHolder holder, BaseTypeBean baseTypeBean, int position) {

    }
}
