package com.BFMe.BFMBuyer.ugc.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.ugc.bean.OfficialBean;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：官方推荐
 * Created by : 奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/7/14 11:00
 */

public class OfficialAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TOPIC = 1;//推荐话题
    private static final int PRODUCT = 2;//推荐商品

    private List<OfficialBean.DataBean> mData = new ArrayList<>();

    public OfficialAdapter(Context context, List<OfficialBean.DataBean> data) {
        if (data != null && data.size() > 0) {
            mData.clear();
            mData.addAll(data);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case TOPIC:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_offical_one, parent, false);
                holder = new OfficialViewHolder1(view, parent.getContext());
                break;
            case PRODUCT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_offical_two, parent, false);
                holder = new OfficialViewHolder2(view, parent.getContext());
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TOPIC:
                OfficialViewHolder1 viewHolder1 = (OfficialViewHolder1) holder;
                viewHolder1.setData();
                break;
            case PRODUCT:
                OfficialViewHolder2 viewHolder2 = (OfficialViewHolder2) holder;
                viewHolder2.setData();
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        int type = mData.get(position).getType();
        if (type == 1) {
            return TOPIC;
        } else if (type == 2) {
            return PRODUCT;
        } else {
            return TOPIC;
        }
    }

    public void cleanData() {
        mData.clear();
    }

    public void addData(List<OfficialBean.DataBean> data) {
        addData(0, data);
    }

    public void addData(int position, List<OfficialBean.DataBean> data) {
        if (data != null && data.size() > 0) {
            mData.addAll(position, data);
            notifyDataSetChanged();
        }
    }

    private class OfficialViewHolder1 extends RecyclerView.ViewHolder {
        private TextView tvTime;
        private ImageView ivPicOfficial;
        private TextView tvTitleOfficial;
        private RelativeLayout rl_item;
        private Context mContext;

        private OfficialViewHolder1(View itemView, Context context) {
            super(itemView);
            mContext = context;
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            ivPicOfficial = (ImageView) itemView.findViewById(R.id.iv_pic_official);
            tvTitleOfficial = (TextView) itemView.findViewById(R.id.tv_title_official);
            rl_item = (RelativeLayout) itemView.findViewById(R.id.rl_item);
        }

        public void setData() {
            OfficialBean.DataBean item = mData.get(getAdapterPosition()-1);
            tvTime.setText(item.getPushDate());
            if (item.getMessageCenter().get(0) != null) {
                final OfficialBean.DataBean.MessageCenterBean centerBean = item.getMessageCenter().get(0);
                tvTitleOfficial.setText(centerBean.getTitle());
                Glide
                        .with(mContext)
                        .load(centerBean.getImagePath())
                        .into(ivPicOfficial);

                rl_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.topicItemClick((long) centerBean.getId());
                    }
                });
            }
        }
    }

    private class OfficialViewHolder2 extends RecyclerView.ViewHolder {
        private TextView tvTime;
        private RelativeLayout rlLayout;
        private ImageView ivPicOfficial1;
        private ImageView ivPicOfficial2;
        private TextView tvTitleOfficial;
        private RecyclerView rvItemProduct;
        private Context mContext;

        OfficialViewHolder2(View itemView, Context context) {
            super(itemView);
            mContext = context;
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            rlLayout = (RelativeLayout) itemView.findViewById(R.id.rl_layout);
            ivPicOfficial1 = (ImageView) itemView.findViewById(R.id.iv_pic_official1);
            ivPicOfficial2 = (ImageView) itemView.findViewById(R.id.iv_pic_official2);
            tvTitleOfficial = (TextView) itemView.findViewById(R.id.tv_title_official);
            rvItemProduct = (RecyclerView) itemView.findViewById(R.id.rv_item_product);

        }

        public void setData() {
            OfficialBean.DataBean item = mData.get(getAdapterPosition() - 1);
            tvTime.setText(item.getPushDate());
            if (item.getMessageCenter().get(0) != null) {
                final OfficialBean.DataBean.MessageCenterBean centerBean = item.getMessageCenter().get(0);
                tvTitleOfficial.setText(centerBean.getTitle());
                Glide
                        .with(mContext)
                        .load(centerBean.getImagePath())
                        .into(ivPicOfficial1);
                Glide
                        .with(mContext)
                        .load(centerBean.getImagePathTwo())
                        .into(ivPicOfficial2);

                rlLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.commodityItemClick(centerBean.getId() + "");
                    }
                });
            }


            OfficialProductAdapter adapter = new OfficialProductAdapter(mContext, item.getMessageCenter());
            rvItemProduct.setAdapter(adapter);
            rvItemProduct.setLayoutManager(new LinearLayoutManager(mContext));

            adapter.setOnItemClickListener(new OfficialProductAdapter.OnItemClickListener() {
                @Override
                public void itemClick(String commodityId) {
                    onItemClickListener.commodityItemClick(commodityId);
                }
            });
        }
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void commodityItemClick(String commodityId);

        void topicItemClick(long id);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
