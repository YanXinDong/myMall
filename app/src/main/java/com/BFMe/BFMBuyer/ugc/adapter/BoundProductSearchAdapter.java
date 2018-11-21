package com.BFMe.BFMBuyer.ugc.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.application.MyApplication;
import com.BFMe.BFMBuyer.ugc.bean.BoundProductBean;
import com.BFMe.BFMBuyer.utils.UiUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：关联商品时的搜索
 * Created by : 奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/6/13 11:27
 */
public class BoundProductSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<BoundProductBean.OrdersListBean> mList = new ArrayList();

    public BoundProductSearchAdapter(Context context, List<BoundProductBean.OrdersListBean> list) {
        mContext = context;
        if (list != null && list.size() > 0) {
            mList.clear();
            mList.addAll(list);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_bound_product, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.setData(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public interface OnSeleteClickListener {
        void onSeleteClickListener(int position);
    }

    public OnSeleteClickListener onSeleteClickListener;

    public void setOnSeleteClickListener(OnSeleteClickListener onSeleteClickListener) {
        this.onSeleteClickListener = onSeleteClickListener;
    }

    public List<BoundProductBean.OrdersListBean> getList() {
        return mList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rlLayout;
        private ImageView ivRelevanceCommodity;
        private TextView tvRelevanceCommodityName;
        private TextView tvRelevanceCommodityPrice;
        private TextView tvBoundProductShop;

        public MyViewHolder(View itemView) {
            super(itemView);
            rlLayout = (RelativeLayout) itemView.findViewById(R.id.rl_layout);
            ivRelevanceCommodity = (ImageView) itemView.findViewById(R.id.iv_bound_product);
            tvRelevanceCommodityName = (TextView) itemView.findViewById(R.id.tv_bound_product_name);
            tvRelevanceCommodityPrice = (TextView) itemView.findViewById(R.id.tv_bound_product_price);
            tvBoundProductShop = (TextView) itemView.findViewById(R.id.tv_bound_product_shop);
        }

        public void setData(final int position) {
            tvBoundProductShop.setText(mList.get(position).getShopName());
            tvRelevanceCommodityPrice.setText(MyApplication.getContext().getResources().getString(R.string.money_type) + mList.get(position).getSalePrice());
            Glide
                    .with(mContext)
                    .load(mList.get(position).getImageUrl())
                    .into(ivRelevanceCommodity);
            rlLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSeleteClickListener.onSeleteClickListener(position);
                }
            });
            String productName = mList.get(position).getProductName();
            int tempStart = productName.indexOf("<em>");
            int tempEnd = productName.indexOf("</em>");

            productName = productName.replace("<em>", "");
            productName = productName.replace("</em>", "");

            //利用SpannableStringBuilder分批断设置字体颜色
            SpannableStringBuilder builder = new SpannableStringBuilder();
            builder.append(productName);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#ff151515"));
            AbsoluteSizeSpan typefaceSpan = new AbsoluteSizeSpan(UiUtils.sp2px(mContext, 13));
            StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);

            if (tempStart != -1 && tempEnd != -1) {
                builder.setSpan(colorSpan, tempStart, tempEnd - 4, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                builder.setSpan(typefaceSpan, tempStart, tempEnd - 4, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                builder.setSpan(styleSpan, tempStart, tempEnd - 4, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }
            tvRelevanceCommodityName.setText(builder);

        }
    }

    /**
     * 添加数据
     *
     * @param datas
     * @param position
     */
    public void addData(int position, List<BoundProductBean.OrdersListBean> datas) {
        if (datas != null && datas.size() > 0) {
            mList.addAll(position, datas);
            notifyDataSetChanged();
        }
    }


    public void addData(List<BoundProductBean.OrdersListBean> datas) {
        addData(0, datas);
    }

    /**
     * 清楚数据
     */
    public void cleanData() {
        mList.clear();
        notifyDataSetChanged();
    }
}
