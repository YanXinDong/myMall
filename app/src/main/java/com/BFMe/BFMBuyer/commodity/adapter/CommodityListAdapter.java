package com.BFMe.BFMBuyer.commodity.adapter;

import com.BFMe.BFMBuyer.base.recycle.MultiItemTypeAdapter;
import com.BFMe.BFMBuyer.commodity.activity.CommodityListActivity;
import com.BFMe.BFMBuyer.javaBean.SearchCommodityBean;

import java.util.List;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/9/18 20:01
 */
public class CommodityListAdapter extends MultiItemTypeAdapter<SearchCommodityBean.DataBean.ProductsBean> {

    private List<SearchCommodityBean.DataBean.ProductsBean> mDatas;
    public CommodityListAdapter(CommodityListActivity context, List<SearchCommodityBean.DataBean.ProductsBean> datas) {
        super(context, datas);
        mDatas = datas;
        addItemViewDelegate(new CommodityItemViewDelegate());
    }

    public void cleanData() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    public void addData(List<SearchCommodityBean.DataBean.ProductsBean> datas) {
        addData(0,datas);
    }

    public void addData(int position,List<SearchCommodityBean.DataBean.ProductsBean> datas) {
        if(datas != null && datas.size() > 0) {
            mDatas.addAll(position,datas);
            notifyItemRangeInserted(position+1,datas.size());
        }
    }
}
