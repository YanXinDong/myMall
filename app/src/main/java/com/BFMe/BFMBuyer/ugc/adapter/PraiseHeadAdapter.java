package com.BFMe.BFMBuyer.ugc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.ugc.bean.TopicDetails;
import com.BFMe.BFMBuyer.view.CircularImageView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：点赞头像列表
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/6/7 18:19
 */
public class PraiseHeadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<TopicDetails.TopicparseuserlistBean> mTopicPraiseList = new ArrayList<>();
    public PraiseHeadAdapter(List<TopicDetails.TopicparseuserlistBean> topicPraiseList) {
        if(topicPraiseList != null && topicPraiseList.size() > 0) {
            mTopicPraiseList.clear();
            mTopicPraiseList.addAll(topicPraiseList);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_praise_head,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setData();
    }

    @Override
    public int getItemCount() {
        return mTopicPraiseList.size() > 8 ? 8 : mTopicPraiseList.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder{
        private RelativeLayout rl_praise;
        private CircularImageView clv_head;
        private Context mContext;
        private ViewHolder(View itemView) {
            super(itemView);
            rl_praise = (RelativeLayout) itemView.findViewById(R.id.rl_praise);
            clv_head = (CircularImageView) itemView.findViewById(R.id.clv_head);
            mContext = itemView.getContext();
        }

        public void setData() {
            final TopicDetails.TopicparseuserlistBean item = mTopicPraiseList.get(getLayoutPosition());

            Glide
                    .with(mContext)
                    .load(item.getUserPhoto())
                    .placeholder(R.drawable.zhanwei3)
                    .error(R.drawable.rabit)
                    .into(clv_head);
            rl_praise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(item.getEncryptUserId());
                }
            });
        }
    }

    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        void onClick(String userId);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
