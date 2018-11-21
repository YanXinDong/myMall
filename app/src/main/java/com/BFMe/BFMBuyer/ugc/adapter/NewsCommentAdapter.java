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
import com.BFMe.BFMBuyer.ugc.bean.NewsTopicCommentBean;
import com.BFMe.BFMBuyer.view.CircularImageView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/7/5 15:03
 */
public class NewsCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<NewsTopicCommentBean> mNewsList = new ArrayList<>();

    public NewsCommentAdapter(Context context, List<NewsTopicCommentBean> newsTopicCommentBean) {
        this.mContext = context;
        if (newsTopicCommentBean != null && newsTopicCommentBean.size() != 0) {
            mNewsList.clear();
            mNewsList.addAll(newsTopicCommentBean);
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_commnet, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setData(position);
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }


    public void addData(List<NewsTopicCommentBean>  data) {
        addData(0, data);
    }

    public void addData(int position, List<NewsTopicCommentBean>  data) {
        if (data != null && data.size() != 0) {
            mNewsList.addAll(position, data);
            notifyDataSetChanged();
        }
    }
    public void cleanData() {
        mNewsList.clear();
    }

    public interface OnNewsTopicCommentClickListener {
        void onNewsTopicComment(Long topicId);
    }

    private OnNewsTopicCommentClickListener onNewsTopicCommentClickListener;

    public void setOnNewsTopicCommentClickListener(OnNewsTopicCommentClickListener onNewsTopicCommentClickListener) {
        this.onNewsTopicCommentClickListener = onNewsTopicCommentClickListener;
    }

    public interface OnTopicCommentListClickListener {
        void onTopicCommentList(Long topicId);
    }

    private OnTopicCommentListClickListener onTopicCommentListClickListener;

    public void setTopicCommentListClickListener(OnTopicCommentListClickListener onTopicCommentListClickListener) {
        this.onTopicCommentListClickListener = onTopicCommentListClickListener;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private CircularImageView civHead;
        private TextView tvUserName;
        private ImageView ivIcon;
        private TextView tvTime;
        private RelativeLayout rlLayout;
        private TextView tvNewsContent;
        private ViewHolder(View itemView) {
            super(itemView);
            civHead = (CircularImageView) itemView.findViewById(R.id.civ_head);
            tvUserName = (TextView) itemView.findViewById(R.id.tv_user_name);
            ivIcon = (ImageView) itemView.findViewById(R.id.iv_icon);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            rlLayout = (RelativeLayout) itemView.findViewById(R.id.rl_layout);
            tvNewsContent = (TextView) itemView.findViewById(R.id.tv_news_content);


        }

        public void setData(final int position) {
            tvUserName.setText(mNewsList.get(position).getUserName());
            tvTime.setText(mNewsList.get(position).getCreateDate());
            tvNewsContent.setText(mNewsList.get(position).getCommentContent());
            Glide
                    .with(mContext)
                    .load(mNewsList.get(position).getPhoto())
                    .placeholder(R.drawable.zhanwei3)
                    .error(R.drawable.zhanwei3)
                    .into(civHead);
            Glide
                    .with(mContext)
                    .load(mNewsList.get(position).getImageUrl())
                    .error(R.drawable.zhanwei1)
                    .into(ivIcon);
            ivIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNewsTopicCommentClickListener.onNewsTopicComment(mNewsList.get(position).getId());
                }
            });
            tvNewsContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onTopicCommentListClickListener.onTopicCommentList(mNewsList.get(position).getId());
                }
            });
        }
    }
}
