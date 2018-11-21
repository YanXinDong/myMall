package com.BFMe.BFMBuyer.ugc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.ugc.bean.HotTopic;
import com.BFMe.BFMBuyer.utils.UiUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/7/21 15:45
 */
public class AllTopicSortAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<HotTopic.HotTopicBean> mHotTopic = new ArrayList<>();
    public AllTopicSortAdapter(List<HotTopic.HotTopicBean> hotTopic) {
        if( hotTopic != null && hotTopic.size() > 0) {
            mHotTopic.clear();
            mHotTopic.addAll(hotTopic);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ucg_hot_topic_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setData();
    }

    @Override
    public int getItemCount() {
        return mHotTopic.size();
    }

    public void clearData() {
        mHotTopic.clear();
    }

    public void addData(List<HotTopic.HotTopicBean> data) {
        addData(0,data);
    }
    public void addData(int position ,List<HotTopic.HotTopicBean> data) {
        if( data != null && data.size() > 0) {
            mHotTopic.addAll(position,data);
            notifyDataSetChanged();
        }
    }
    private class ViewHolder extends RecyclerView.ViewHolder{
        private RelativeLayout rl_hot_topic;
        private ImageView iv_hot_topic_icon;
        private TextView tv_topic_name;
        private TextView tv_topic_number;
        private Context mContext;
        private ViewHolder(View itemView) {
            super(itemView);
            rl_hot_topic = (RelativeLayout) itemView.findViewById(R.id.rl_hot_topic);
            iv_hot_topic_icon = (ImageView) itemView.findViewById(R.id.iv_hot_topic_icon);
            tv_topic_name = (TextView) itemView.findViewById(R.id.tv_topic_name);
            tv_topic_number = (TextView) itemView.findViewById(R.id.tv_topic_number);
            mContext = itemView.getContext();
        }

        public void setData() {
            final HotTopic.HotTopicBean hotTopic = mHotTopic.get(getLayoutPosition() - 1);

            Glide
                    .with(mContext)
                    .load(hotTopic.getImageUrl())
                    .override(UiUtils.dip2px(51),UiUtils.dip2px(65))
                    .into(iv_hot_topic_icon);
            tv_topic_name.setText(hotTopic.getTitle());
            tv_topic_number.setText("共" + hotTopic.getParticipateCount() + "人参与");
            rl_hot_topic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.itemClick(hotTopic.getTitle(),hotTopic.getTopicId());
                }
            });
        }
    }

    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        void itemClick(String title, long id);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
