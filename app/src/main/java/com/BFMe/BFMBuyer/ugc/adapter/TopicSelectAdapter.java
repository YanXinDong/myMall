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
import com.BFMe.BFMBuyer.ugc.bean.TopicSelectBean;
import com.BFMe.BFMBuyer.view.GlideCircleTransform;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：话题精选
 * Created by : 奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/7/13 17:43
 */
public class TopicSelectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<TopicSelectBean.DataBean> mTopicSelectdata = new ArrayList<>();

    public TopicSelectAdapter(Context context, List<TopicSelectBean.DataBean> topicSelectdata) {
        this.mContext = context;
        if (topicSelectdata != null && topicSelectdata.size() != 0) {
            mTopicSelectdata.clear();
            mTopicSelectdata.addAll(topicSelectdata);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.itme_topic_select, parent, false);
        return new TopicSelectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        TopicSelectViewHolder topicSelectHolder = (TopicSelectViewHolder) holder;
        topicSelectHolder.tvTime.setText(mTopicSelectdata.get(position).getPushDate());
        topicSelectHolder.tvName.setText(mTopicSelectdata.get(position).getUserName());
        topicSelectHolder.tv_content.setText(mTopicSelectdata.get(position).getTitle());
        Glide
                .with(mContext)
                .load(mTopicSelectdata.get(position).getPhoto())
                .transform(new GlideCircleTransform(mContext))
                .placeholder(R.drawable.zhanwei3)
                .error(R.drawable.zhanwei3)
                .into(topicSelectHolder.ivHead);
        Glide
                .with(mContext)
                .load(mTopicSelectdata.get(position).getImagePath())
                .centerCrop()
                .placeholder(R.drawable.zhanwei2)
                .error(R.drawable.zhanwei2)
                .into(topicSelectHolder.ivPic);

        topicSelectHolder.rlLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTopicSelectClickListener.onTopicSelect(mTopicSelectdata.get(position).getTopicId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTopicSelectdata.size();
    }

    public void cleanData() {
        mTopicSelectdata.clear();
        notifyDataSetChanged();
    }

    public void addData(List<TopicSelectBean.DataBean> data) {
        addData(0, data);
    }

    public void addData(int position, List<TopicSelectBean.DataBean> data) {
        if (data != null && data.size() != 0) {
            mTopicSelectdata.addAll(position, data);
            notifyDataSetChanged();
        }
    }

    public interface OnTopicSelectClickListener {
        void onTopicSelect(long topicId);
    }

    private OnTopicSelectClickListener onTopicSelectClickListener;

    public void setOnTopicSelectClickListener(OnTopicSelectClickListener onTopicSelectClickListener) {
        this.onTopicSelectClickListener = onTopicSelectClickListener;
    }

    class TopicSelectViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rlLayout;
        private ImageView ivHead;
        private TextView tvName;
        private TextView tvTime;
        private TextView tv_content;
        private ImageView ivPic;

        public TopicSelectViewHolder(View itemView) {
            super(itemView);

            rlLayout = (RelativeLayout) itemView.findViewById(R.id.rv_layout_parent);
            ivHead = (ImageView) itemView.findViewById(R.id.iv_head);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            ivPic = (ImageView) itemView.findViewById(R.id.iv_pic);

        }
    }
}
