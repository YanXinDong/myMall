package com.BFMe.BFMBuyer.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.commodity.activity.ProducetDetailsActivity;
import com.BFMe.BFMBuyer.javaBean.SubjectListBean;
import com.BFMe.BFMBuyer.utils.Utils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：专题列表
 * Created by :奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2016/9/14 16:38
 */
public class SubjectListAdapter extends BaseAdapter {
    private List<SubjectListBean.ProductsBean> mproducts = new ArrayList<>();
    private Activity mActivity;

    public SubjectListAdapter(List<SubjectListBean.ProductsBean> products, Activity activity) {
        this.mActivity = activity;
        this.mproducts = products;
    }

    @Override
    public int getCount() {
        return mproducts.size();
    }

    @Override
    public Object getItem(int position) {
        return mproducts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mActivity, R.layout.item_search_result, null);
            holder = new ViewHolder();
            holder.rl_subjectproduct = (RelativeLayout) convertView.findViewById(R.id.rl_subjectproduct);
            holder.ivProductIcon = (ImageView) convertView.findViewById(R.id.iv_product_icon);
            holder.tvProductName = (TextView) convertView.findViewById(R.id.tv_product_name);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_product_price);
            holder.ivAddress = (ImageView) convertView.findViewById(R.id.iv_address);
            holder.tv_IsSeckill = (TextView) convertView.findViewById(R.id.tv_IsSeckill);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //获取数据
        SubjectListBean.ProductsBean productsBean = mproducts.get(position);
        //设置图片
        Glide.with(mActivity).load(productsBean.getImage()).placeholder(R.drawable.zhanwei4).dontAnimate().into(holder.ivProductIcon);

        holder.tvPrice.setText(parent.getContext().getResources().getString(R.string.money_type) + Utils.doubleSave2(productsBean.getPrice()));
        holder.tvProductName.setText(productsBean.getName());

        Glide.with(mActivity).load(productsBean.getCountryImg()).into(holder.ivAddress);

        if (productsBean.isSeckill() == 1) {
            holder.tv_IsSeckill.setVisibility(View.VISIBLE);
        } else {
            holder.tv_IsSeckill.setVisibility(View.GONE);
        }

        holder.rl_subjectproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ProducetDetailsActivity.class);
                intent.putExtra("productId", mproducts.get(position).getId() + "");
                intent.putExtra("ShopId", mproducts.get(position).getShopId() + "");
                mActivity.startActivity(intent);
                mActivity.overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
            }
        });
        return convertView;
    }

    class ViewHolder {
        private ImageView ivProductIcon;
        private TextView tvProductName;
        private TextView tvPrice;
        private ImageView ivAddress;
        private RelativeLayout rl_subjectproduct;
        private TextView tv_IsSeckill;

    }


    /**
     * 添加数据
     *
     * @param datas
     * @param position
     */
    public void addData(int position, List<SubjectListBean.ProductsBean> datas) {
        if (datas != null && datas.size() > 0) {
            mproducts.addAll(position, datas);
            notifyDataSetChanged();
        }
    }


    public void addData(List<SubjectListBean.ProductsBean> datas) {
        addData(0, datas);
    }

    /**
     * 清楚数据
     */
    public void cleanData() {
        mproducts.clear();
        notifyDataSetChanged();
    }
}
