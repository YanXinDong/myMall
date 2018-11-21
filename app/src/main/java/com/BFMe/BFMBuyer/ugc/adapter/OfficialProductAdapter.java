package com.BFMe.BFMBuyer.ugc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.ugc.bean.OfficialBean;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：官方推荐商品
 * Created by : 奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/7/13 17:43
 */
public class OfficialProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<OfficialBean.DataBean.MessageCenterBean> mMessageCenter = new ArrayList<>();
    public OfficialProductAdapter(Context context, List<OfficialBean.DataBean.MessageCenterBean> messageCenter) {
        this.mContext = context;
        if(messageCenter != null && messageCenter.size() > 1) {
            mMessageCenter.clear();
            mMessageCenter.addAll(messageCenter);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_official_product, parent, false);
        return new OfficialProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        OfficialProductViewHolder officialHolder = (OfficialProductViewHolder) holder;
        final OfficialBean.DataBean.MessageCenterBean item = mMessageCenter.get(position + 1);
        officialHolder.tvOfficialProduct.setText(item.getTitle());
        Glide
                .with(mContext)
                .load(item.getImagePath())
                .into(officialHolder.ivOfficialProduct);

        officialHolder.ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.itemClick(item.getId()+"");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMessageCenter.size() - 1;
    }

    private class OfficialProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivOfficialProduct;
        private TextView tvOfficialProduct;
        private LinearLayout ll_item;
        private  OfficialProductViewHolder(View itemView) {
            super(itemView);
            ivOfficialProduct = (ImageView) itemView.findViewById(R.id.iv_official_product);
            tvOfficialProduct = (TextView) itemView.findViewById(R.id.tv_official_product);
            ll_item = (LinearLayout) itemView.findViewById(R.id.ll_item);

        }
    }

    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        void itemClick(String commodityId);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
