package com.BFMe.BFMBuyer.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.javaBean.ShopListBean;
import com.BFMe.BFMBuyer.utils.Utils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/1.
 * <p/>
 * 分类商品列表展示adapter
 */
public class MoreStorShopAdapter extends BaseAdapter {

    private Context mContext;
    private List<ShopListBean.ProductsBean> mProducts = new ArrayList<>();
    //判断是否是店铺内列表（暂时取消）
    private boolean mIsShop;
    public MoreStorShopAdapter(Context context, List<ShopListBean.ProductsBean> products) {
        mContext = context;
        if (products != null && products.size() > 0) {
            mProducts.clear();
            mProducts.addAll(products);

        }
    }

    public MoreStorShopAdapter(Context context, List<ShopListBean.ProductsBean> products, boolean isShop) {
        mContext = context;
        if (products != null && products.size() > 0) {
            mProducts.clear();
            mProducts.addAll(products);

        }
        mIsShop = isShop;
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

        ViewHodler viewHodler = null;
        if (convertView == null) {
            viewHodler = new ViewHodler();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item_subject, parent, false);

            viewHodler.iv_product_icon = (ImageView) convertView.findViewById(R.id.iv_product_icon);
            viewHodler.tv_product_name = (TextView) convertView.findViewById(R.id.tv_product_name);
            viewHodler.tv_price_left = (TextView) convertView.findViewById(R.id.tv_price_left);
            viewHodler.tv_price_right = (TextView) convertView.findViewById(R.id.tv_price_right);

            viewHodler.iv_country_inco = (ImageView) convertView.findViewById(R.id.iv_country_inco);
            viewHodler.tv_country_text = (TextView) convertView.findViewById(R.id.tv_country_text);

            viewHodler.tv_fax = (TextView) convertView.findViewById(R.id.tv_fax);
            viewHodler.tv_free_right = (ImageView) convertView.findViewById(R.id.iv_free_right);

            convertView.setTag(viewHodler);

        } else {

            viewHodler = (ViewHodler) convertView.getTag();

        }

        viewHodler.tv_product_name.setText(mProducts.get(position).getProductName());//商品名称
        //设置删除线
        viewHodler.tv_price_right.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        viewHodler.tv_price_left.setText(parent.getContext().getResources().getString(R.string.money_type) + Utils.doubleSave2(mProducts.get(position).getMinSalePrice()));//最小销售价

        viewHodler.tv_price_right.setText(parent.getContext().getResources().getString(R.string.money_type) + Utils.doubleSave2(mProducts.get(position).getMarketPrice()));//市场价

        Glide//缩略图
                .with(mContext)
                .load(mProducts.get(position).getImagePath())
                .placeholder(R.drawable.zhanwei4)
                .dontAnimate()
                .crossFade()
                .into(viewHodler.iv_product_icon);
        Glide//国家图标
                .with(mContext)
                .load(mProducts.get(position).getCountryLogo())
                .placeholder(R.drawable.location_icon)
                .error(R.drawable.location_icon)
                .crossFade()
                .into(viewHodler.iv_country_inco);

        //国家名称
        viewHodler.tv_country_text.setText(mProducts.get(position).getCountry());

        return convertView;
    }

    class ViewHodler {

        ImageView iv_product_icon;//缩略图
        TextView tv_product_name;//商品名称
        TextView tv_price_left;//最小销售价
        TextView tv_price_right;//市场价
        ImageView iv_country_inco;//国家图标
        TextView tv_country_text;//国家名称

        TextView tv_fax;//运费
        ImageView tv_free_right;//包邮
    }

    /**
     * 获取所有的数据
     */
    public List<ShopListBean.ProductsBean> getDatas(){

        return mProducts;
    }
    /**
     * 添加数据
     *
     * @param datas
     * @param position
     */
    public void addData(int position, List<ShopListBean.ProductsBean> datas) {
        if (datas != null && datas.size() > 0) {
            mProducts.addAll(position, datas);
            notifyDataSetChanged();
        }
    }


    public void addData(List<ShopListBean.ProductsBean> datas) {
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
