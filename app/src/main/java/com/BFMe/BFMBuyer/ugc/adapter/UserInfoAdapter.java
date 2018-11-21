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
import com.BFMe.BFMBuyer.ugc.bean.PersonalCenterInfo;
import com.BFMe.BFMBuyer.ugc.bean.UserPublishTopics;
import com.BFMe.BFMBuyer.utils.UiUtils;
import com.BFMe.BFMBuyer.view.CircularImageView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.Glide.with;

/**
 * Description：用户信息Adapter
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/6/9 14:24
 */
public class UserInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int USER_INFO = 1; //用户信息
    private static final int ADDS_TOPIC = 2; //发布的话题数
    private static final int TOPIC_LIST = 3;//话题列表

    private PersonalCenterInfo mPersonalCenterInfo;
    private List<UserPublishTopics.TopicsListBean> mTopicsList = new ArrayList<>();
//    private float itemWidth;//获取瀑布流需要的item宽度

    private Context mContext;
    public UserInfoAdapter(Context context, PersonalCenterInfo personalCenterInfo, List<UserPublishTopics.TopicsListBean> topicsList) {
        mPersonalCenterInfo = personalCenterInfo;
        mContext = context;
        if (topicsList != null && topicsList.size() > 0) {
            mTopicsList.clear();
            mTopicsList.addAll(topicsList);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return USER_INFO;
        } else if (position == 1) {
            return ADDS_TOPIC;
        } else {
            return TOPIC_LIST;
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams
                && holder.getLayoutPosition() < 3) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view;
        switch (viewType) {
            case USER_INFO:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_info_details, parent, false);
                viewHolder = new UserInfoViewHolder(view);
                break;
            case ADDS_TOPIC:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ucg_recommend_topic, parent, false);
                viewHolder = new AddTopicViewHolder(view);
                break;
            case TOPIC_LIST:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ucg_recommend_topic_list, parent, false);
                viewHolder = new TopicListViewHolder(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case USER_INFO:
                UserInfoViewHolder userInfoViewHolder = (UserInfoViewHolder) holder;
                userInfoViewHolder.setData();
                break;
            case ADDS_TOPIC:
                AddTopicViewHolder addTopicViewHolder = (AddTopicViewHolder) holder;
                addTopicViewHolder.setData();
                break;
            case TOPIC_LIST:
                TopicListViewHolder topicListViewHolder = (TopicListViewHolder) holder;
                topicListViewHolder.setData();
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mTopicsList.size() + 2;
    }

    public void cleanData() {
        mPersonalCenterInfo = null;
        mTopicsList.clear();
    }

    public void addData(PersonalCenterInfo personalCenterInfo, List<UserPublishTopics.TopicsListBean> topicsList) {
        if (personalCenterInfo != null && topicsList != null && topicsList.size() > 0) {
            mPersonalCenterInfo = personalCenterInfo;
            mTopicsList.addAll(topicsList);
            notifyDataSetChanged();
        }
    }

    public void addData(int position, List<UserPublishTopics.TopicsListBean> topicsList) {
        if (topicsList != null && topicsList.size() > 0) {
            mTopicsList.addAll(position, topicsList);
            notifyItemChanged(position);
        }
    }

    public void addHeadData(PersonalCenterInfo personalCenterInfo) {
        if(personalCenterInfo != null) {
            mPersonalCenterInfo = null;
            mPersonalCenterInfo = personalCenterInfo;
            notifyItemChanged(1);
        }
    }

    private class UserInfoViewHolder extends RecyclerView.ViewHolder {
        private CircularImageView civ_user_icon;
        private TextView tv_intro;
        private CheckBox cb_attention;
        private TextView tv_user_attention;
        private TextView tv_user_fans;
        private TextView tv_user_zan;
        private TextView tv_user_topic;
        private Context mContext;

        private UserInfoViewHolder(View itemView) {
            super(itemView);
            civ_user_icon = (CircularImageView) itemView.findViewById(R.id.civ_user_icon);
            cb_attention = (CheckBox) itemView.findViewById(R.id.cb_attention);
            tv_intro = (TextView) itemView.findViewById(R.id.tv_intro);
            tv_user_attention = (TextView) itemView.findViewById(R.id.tv_user_attention);
            tv_user_fans = (TextView) itemView.findViewById(R.id.tv_user_fans);
            tv_user_zan = (TextView) itemView.findViewById(R.id.tv_user_zan);
            tv_user_topic = (TextView) itemView.findViewById(R.id.tv_user_topic);

            mContext = itemView.getContext();
        }

        public void setData() {
            Glide.with(mContext)
                    .load(mPersonalCenterInfo.getImageUrl())
                    .placeholder(R.drawable.zhanwei3)
                    .error(R.drawable.rabit)
                    .into(civ_user_icon);
            tv_intro.setText(mPersonalCenterInfo.getIntroduceMyself());

            tv_user_zan.setText(mPersonalCenterInfo.getParseCount() + "");
            tv_user_topic.setText(mPersonalCenterInfo.getTopicCount() + "");
            tv_user_attention.setText(mPersonalCenterInfo.getFocusUserCount() + "");
            tv_user_fans.setText(mPersonalCenterInfo.getFans() + "");


            if(mPersonalCenterInfo.getIsOwn() == 1) {
                cb_attention.setVisibility(View.GONE);
            }else {
                cb_attention.setChecked(mPersonalCenterInfo.getIsFocus() == 1);
                if (cb_attention.isChecked()) {
                    cb_attention.setText(mContext.getString(R.string.attention_ture));
                } else {
                    cb_attention.setText(mContext.getString(R.string.notice_total));
                }
                cb_attention.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        userCenterOnClickListener.attentionOnClick(v);
                    }
                });
            }

            civ_user_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userCenterOnClickListener.showUserIcon(mPersonalCenterInfo.getImageUrl(),v);
                }
            });
        }
    }

    private class AddTopicViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rl_all_topic;
        private TextView tv_title;
        private ImageView iv_arrow;
        private AddTopicViewHolder(View itemView) {
            super(itemView);
            rl_all_topic = (RelativeLayout) itemView.findViewById(R.id.rl_all_topic);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            iv_arrow = (ImageView) itemView.findViewById(R.id.iv_arrow);
        }

        public void setData() {
            if(mPersonalCenterInfo.getIsOwn() == 1) {
                iv_arrow.setVisibility(View.VISIBLE);
                rl_all_topic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        userCenterOnClickListener.sortTopicOnClick();
                    }
                });
            }else {
                iv_arrow.setVisibility(View.GONE);
            }
            tv_title.setText("发布的话题" + "（" + mPersonalCenterInfo.getTopicCount() + "）");
        }
    }

    private class TopicListViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rl_recommend_topic;
        private ImageView iv_icon;
        private ImageView iv_start;
        private TextView tv_info;
        private CircularImageView civ_head_icon;
        private TextView tv_user_name;
        private CheckBox cb_collect;

        private Context mContext;

        private TopicListViewHolder(View itemView) {
            super(itemView);
            rl_recommend_topic = (RelativeLayout) itemView.findViewById(R.id.rl_recommend_topic);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            iv_start = (ImageView) itemView.findViewById(R.id.iv_start);
            tv_info = (TextView) itemView.findViewById(R.id.tv_info);
            civ_head_icon = (CircularImageView) itemView.findViewById(R.id.civ_head_icon);
            tv_user_name = (TextView) itemView.findViewById(R.id.tv_user_name);
            cb_collect = (CheckBox) itemView.findViewById(R.id.cb_collect);
            mContext = itemView.getContext();
        }

        public void setData() {
            UserPublishTopics.TopicsListBean topicsList = mTopicsList.get(getLayoutPosition() - 3);
            intiView(topicsList);
            
            with(mContext)
                    .load(topicsList.getImageUrl())
                    .centerCrop()
                    .dontTransform()
                    .into(iv_icon);
            if(topicsList.getState() == 1) {
                iv_start.setVisibility(View.GONE);
            }else {
                iv_start.setVisibility(View.VISIBLE);
                if(topicsList.getState() == 0) {
                    iv_start.setImageResource(R.drawable.icon_audit);
                }else {
                    iv_start.setImageResource(R.drawable.icon_audit_no);
                }
            }
            tv_info.setText(topicsList.getContent());
            with(mContext)
                    .load(topicsList.getUserImage())
                    .centerCrop()
                    .dontTransform()
                    .into(civ_head_icon);
            tv_user_name.setText(topicsList.getUserName());

            if (topicsList.getIsPrase() == 1) {
                cb_collect.setChecked(true);
            } else {
                cb_collect.setChecked(false);
            }
//            cb_collect.setText(topicsList.getParseCount() + "");

            setListener(topicsList);
        }

        private void setListener(final UserPublishTopics.TopicsListBean topicsList) {
            rl_recommend_topic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userCenterOnClickListener.detailsOnClick(topicsList.getId());
                }
            });

            //使用点击事件监听手动设置CheckBox的状态改变，防止刷新时提示信息出现的错误
            cb_collect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    userCenterOnClickListener.topicLikeOnClick(v,topicsList);
                }
            });

        }

        private void intiView(UserPublishTopics.TopicsListBean topicsList) {
            Drawable drawable = mContext.getResources().getDrawable(R.drawable.collect_select);
            drawable.setBounds(0, 0, UiUtils.dip2px(18), UiUtils.dip2px(18));
            cb_collect.setCompoundDrawables(drawable, null, null, null);

//            if (itemWidth == 0) {//获取瀑布流需要的item宽度
//                itemWidth = (ScreenUtils.getScreenWidth(mContext) - UiUtils.dip2px(5) * 4) / 2;
//            }
////            float scale = itemWidth / (topicsList.getImageWidth() == 0 ? itemWidth : topicsList.getImageWidth());
//            float scale = itemWidth / topicsList.getImageWidth();
//
//            ViewGroup.LayoutParams layoutParams = iv_icon.getLayoutParams();
//            layoutParams.width = (int) itemWidth;
////            layoutParams.height = (int) (scale * (topicsList.getImageHeight() == 0 ? itemWidth : topicsList.getImageHeight()));
//            layoutParams.height = (int) (scale * topicsList.getImageHeight());
//
//            iv_icon.setLayoutParams(layoutParams);

        }
    }

    private UserCenterOnClickListener userCenterOnClickListener;

    public interface UserCenterOnClickListener {
        void attentionOnClick(View v);

        void topicLikeOnClick(View v,UserPublishTopics.TopicsListBean topicInfo);

        void detailsOnClick(long topicId);

        void sortTopicOnClick();

        void showUserIcon(String imageUrl, View v);
    }

    public void setUserCenterOnClickListener(UserCenterOnClickListener userCenterOnClickListener) {
        this.userCenterOnClickListener = userCenterOnClickListener;
    }
}
