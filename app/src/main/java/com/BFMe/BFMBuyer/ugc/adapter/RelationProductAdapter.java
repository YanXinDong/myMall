package com.BFMe.BFMBuyer.ugc.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.ugc.bean.BoundProductBean;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：关联的商品
 * Created by : 奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/6/8 15:37
 */
public class RelationProductAdapter extends BaseAdapter {
    private Context mContext;
    private List<BoundProductBean.OrdersListBean> mList = new ArrayList<>();

    public RelationProductAdapter(Context context, List<BoundProductBean.OrdersListBean> boundList) {
        this.mContext = context;
        if (boundList != null && boundList.size() > 0) {
            mList.clear();
            mList.addAll(boundList);
        }

    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        RelationProductViewHolder holder = null;
        if (convertView == null) {
            holder = new RelationProductViewHolder();
            convertView = View.inflate(mContext, R.layout.item_relation_product, null);
            holder.ivRelationProduct = (ImageView) convertView.findViewById(R.id.iv_relation_product);
            holder.tvRelationProductName = (TextView) convertView.findViewById(R.id.tv_relation_product_name);
            holder.tvRelationProductPrice = (TextView) convertView.findViewById(R.id.tv_relation_product_price);
            holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
            holder.tv_product_shopname = (TextView) convertView.findViewById(R.id.tv_product_shopname);

            convertView.setTag(holder);
        } else {
            holder = (RelationProductViewHolder) convertView.getTag();
        }
        holder.tv_product_shopname.setText(mList.get(position).getShopName());
        String productName = mList.get(position).getProductName();

        productName = productName.replace("<em>", "");
        productName = productName.replace("</em>", "");

        holder.tvRelationProductName.setText(productName);
        holder.tvRelationProductPrice.setText(parent.getContext().getResources().getString(R.string.money_type) + mList.get(position).getSalePrice());
        Glide
                .with(mContext)
                .load(mList.get(position).getImageUrl())
                .into(holder.ivRelationProduct);
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteClickListener.onDeleteClick(position);
            }
        });
        return convertView;
    }

    class RelationProductViewHolder {
        private ImageView ivRelationProduct;
        private TextView tvRelationProductName;
        private TextView tvRelationProductPrice;
        private TextView tv_product_shopname;
        private ImageView iv_delete;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public OnDeleteClickListener onDeleteClickListener;

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public void deleteData(int position) {
        mList.remove(position);
        notifyDataSetChanged();
    }
}
