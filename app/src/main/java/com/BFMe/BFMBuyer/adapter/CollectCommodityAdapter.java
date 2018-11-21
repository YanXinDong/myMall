package com.BFMe.BFMBuyer.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.javaBean.CollectCommodityBean;
import com.BFMe.BFMBuyer.utils.Utils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：喜欢的商品adapter
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/5/22 10:40
 */
public class CollectCommodityAdapter extends BaseAdapter{

    private List<CollectCommodityBean.ProductsBean> mProducts = new ArrayList<>();
    public CollectCommodityAdapter(List<CollectCommodityBean.ProductsBean> products) {
        if(products != null && products.size() > 0) {
            mProducts.clear();
            mProducts.addAll(products);
        }
    }

    @Override
    public int getCount() {
        return mProducts.size();
    }

    @Override
    public Object getItem(int position) {
        return mProducts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_collect_commodity,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_new_price = (TextView) convertView.findViewById(R.id.tv_new_price);
            viewHolder.tv_old_price = (TextView) convertView.findViewById(R.id.tv_old_price);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CollectCommodityBean.ProductsBean item = (CollectCommodityBean.ProductsBean) getItem(position);
        Glide
                .with(parent.getContext())
                .load(item.getImagePath())
                .placeholder(R.drawable.zhanwei1)
                .error(R.drawable.zhanwei1)
                .into(viewHolder.iv_icon);

        viewHolder.tv_name.setText(item.getProductName());
        viewHolder.tv_new_price.setText(parent.getContext().getResources().getString(R.string.money_type)+Utils.doubleSave2(item.getMarketPrice()));
        viewHolder.tv_old_price.setText(parent.getContext().getResources().getString(R.string.money_type)+Utils.doubleSave2(item.getMinSalePrice()));
        viewHolder.tv_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        return convertView;
    }


    class ViewHolder{
        private ImageView iv_icon;
        private TextView tv_name;
        private TextView tv_new_price;
        private TextView tv_old_price;
    }

    /**
     * 添加数据
     *
     * @param datas 喜欢的商品列表数据
     * @param position 添加数据的下标
     */
    public void addData(int position, List<CollectCommodityBean.ProductsBean> datas) {
        if (datas != null && datas.size() > 0) {
            mProducts.addAll(position, datas);
            notifyDataSetChanged();
        }
    }


    public void addData(List<CollectCommodityBean.ProductsBean> datas) {
        addData(0, datas);
    }

    /**
     * 清楚数据
     */
    public void cleanData() {
        mProducts.clear();
        notifyDataSetChanged();
    }
}
