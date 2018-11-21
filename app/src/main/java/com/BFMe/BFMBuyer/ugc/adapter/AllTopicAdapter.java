package com.BFMe.BFMBuyer.ugc.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.ugc.bean.SubTopic;
import com.BFMe.BFMBuyer.ugc.bean.SubTopicList;
import com.BFMe.BFMBuyer.utils.UiUtils;
import com.BFMe.BFMBuyer.view.CircularImageView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.Glide.with;

/**
 * Description：全部话题adapter
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/6/6 15:46
 */
public class AllTopicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TOPIC_INFO = 1; //用户信息
    private static final int TOPIC_LIST = 2; //话题列表
    private SubTopic mSubTopic = new SubTopic();
    private List<SubTopicList.TopicsListBean> mTopicsList = new ArrayList<>();
    private Context mContext;
//    private float itemWidth;//获取瀑布流需要的item宽度

    public AllTopicAdapter(Context context, SubTopic subTopic, List<SubTopicList.TopicsListBean> topicsList) {
        mContext = context;
        if(subTopic != null && topicsList != null ) {
            mSubTopic = subTopic;
            mTopicsList.clear();
            mTopicsList.addAll(topicsList);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) {
            return TOPIC_INFO;
        }else {
            return TOPIC_LIST;
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams
                && holder.getLayoutPosition() <= 1) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view;
        switch (viewType){
            case TOPIC_INFO:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topic_info, parent,false);
                viewHolder =  new TopicInfoViewHolder(view);
                break;
            case TOPIC_LIST:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ucg_recommend_topic_list, parent,false);
                viewHolder =  new TopicListViewHolder(view);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case TOPIC_INFO:
                TopicInfoViewHolder topicInfoViewHolder = (TopicInfoViewHolder) holder;
                topicInfoViewHolder.setData();
                break;
            case TOPIC_LIST:
                TopicListViewHolder topicListViewHolder = (TopicListViewHolder) holder;
                topicListViewHolder.setData();
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mTopicsList.size() + 1;
    }

    public void cleanData() {
        mSubTopic = null;
        mTopicsList.clear();
    }

    public void addData(SubTopic subTopic, List<SubTopicList.TopicsListBean> topicsList) {
        if (subTopic != null && topicsList != null && topicsList.size() > 0) {
            mSubTopic = subTopic;
            mTopicsList.addAll(topicsList);
            notifyDataSetChanged();
        }
    }

    public void addData(int position, List<SubTopicList.TopicsListBean> topicsList) {
        if (topicsList != null && topicsList.size() > 0) {
            mTopicsList.addAll(position, topicsList);
            notifyItemChanged(position);
        }
    }

    private class TopicInfoViewHolder extends RecyclerView.ViewHolder{
        private RelativeLayout rl_topic_title;
        private ImageView iv_topic_icon;
        private TextView tv_topic_info;
        private TextView tv_subscription;
        private TextView tv_participation;
        private CheckBox cb_subscription;
        private Context mContext;
        private TopicInfoViewHolder(View itemView) {
            super(itemView);
            rl_topic_title = (RelativeLayout) itemView.findViewById(R.id.rl_topic_title);
            iv_topic_icon = (ImageView) itemView.findViewById(R.id.iv_topic_icon);
            tv_topic_info = (TextView) itemView.findViewById(R.id.tv_topic_info);
            tv_subscription = (TextView) itemView.findViewById(R.id.tv_subscription);
            tv_participation = (TextView) itemView.findViewById(R.id.tv_participation);
            cb_subscription = (CheckBox) itemView.findViewById(R.id.cb_subscription);
            mContext = itemView.getContext();
        }

        public void setData() {
            final SubTopic.TopicsListBean topicsInfo = mSubTopic.getTopicsList();
            Glide
                    .with(mContext)
                    .load(topicsInfo.getImageUrl())
                    .into(iv_topic_icon);
            tv_topic_info.setText(topicsInfo.getContent());
            tv_subscription.setText(mContext.getString(R.string.subscription)+" "+topicsInfo.getSubscribeCount());
            tv_participation.setText(mContext.getString(R.string.participation)+" "+topicsInfo.getParticipateCount());
            if(topicsInfo.getIsSubscribe() == 1) {
                cb_subscription.setChecked(true);
                cb_subscription.setText(mContext.getString(R.string.subscription_ture));
            }else {
                cb_subscription.setChecked(false);
                cb_subscription.setText(mContext.getString(R.string.subscription));
            }


            cb_subscription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    subTopicOnClickListner.subscriptionOnClick(v,tv_subscription,topicsInfo);
                }
            });

            rl_topic_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    subTopicOnClickListner.topicTitleOnClick(topicsInfo.getId());
                }
            });
        }
    }

    private class TopicListViewHolder extends RecyclerView.ViewHolder{
        private RelativeLayout rl_recommend_topic;
        private ImageView iv_icon;
        private TextView tv_info;
        private CircularImageView civ_head_icon;
        private TextView tv_user_name;
        private CheckBox cb_collect;

        private Context mContext;
        private TopicListViewHolder(View itemView) {
            super(itemView);
            rl_recommend_topic = (RelativeLayout) itemView.findViewById(R.id.rl_recommend_topic);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            tv_info = (TextView) itemView.findViewById(R.id.tv_info);
            civ_head_icon = (CircularImageView) itemView.findViewById(R.id.civ_head_icon);
            tv_user_name = (TextView) itemView.findViewById(R.id.tv_user_name);
            cb_collect = (CheckBox) itemView.findViewById(R.id.cb_collect);
            mContext = itemView.getContext();
        }

        public void setData() {
            SubTopicList.TopicsListBean topicsList = mTopicsList.get(getLayoutPosition() - 2);

            Drawable drawable = mContext.getResources().getDrawable(R.drawable.collect_select);
            drawable.setBounds(0,0, UiUtils.dip2px(18),UiUtils.dip2px(18));
            cb_collect.setCompoundDrawables(drawable,null,null,null);
//
//            if (itemWidth == 0) {//获取瀑布流需要的item宽度
//                itemWidth = (ScreenUtils.getScreenWidth(mContext) - UiUtils.dip2px(5) * 4) / 2;
//            }
//            float scale = itemWidth / topicsList.getImageWidth();
//
//            ViewGroup.LayoutParams layoutParams = iv_icon.getLayoutParams();
//            layoutParams.width = (int) itemWidth;
//            layoutParams.height = (int) (scale * topicsList.getImageHeight());
//            iv_icon.setLayoutParams(layoutParams);

            with(mContext)
                    .load(topicsList.getImageUrl())
                    .centerCrop()
                    .dontTransform()
                    .into(iv_icon);
            tv_info.setText(topicsList.getContent());
            with(mContext)
                    .load(topicsList.getUserImage())
                    .centerCrop()
                    .dontTransform()
                    .into(civ_head_icon);
            tv_user_name.setText(topicsList.getUserName());

            cb_collect.setChecked(topicsList.getIsPrase() == 1);
//            cb_collect.setText(topicsList.getParseCount() + "");

            setListener(topicsList);
        }

        private void setListener(final SubTopicList.TopicsListBean topicsList) {
            rl_recommend_topic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    subTopicOnClickListner.detailsOnClick(topicsList.getId());
                }
            });

            civ_head_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    subTopicOnClickListner.userTopicOnClick(topicsList.getEncryptUserId());
                }
            });

            cb_collect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    subTopicOnClickListner.topicLikeOnClick(v,topicsList);

                }
            });
        }
    }

    private SubTopicOnClickListner subTopicOnClickListner;
    public interface SubTopicOnClickListner{
        void subscriptionOnClick(View v, TextView tv_subscription, SubTopic.TopicsListBean topicInfo);

        void detailsOnClick(long id);

        void userTopicOnClick(String encryptUserId);

        void topicLikeOnClick(View v,SubTopicList.TopicsListBean topicInfo);

        void topicTitleOnClick(long id);
    }

    public void setSubTopicOnClickListner(SubTopicOnClickListner subTopicOnClickListner) {
        this.subTopicOnClickListner = subTopicOnClickListner;
    }
}
