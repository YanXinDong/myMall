package com.BFMe.BFMBuyer.ugc.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.ugc.bean.UserPublishTopics;
import com.BFMe.BFMBuyer.utils.UiUtils;
import com.BFMe.BFMBuyer.view.CircularImageView;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.Glide.with;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/6/20 11:49
 */
public class TopicListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<UserPublishTopics.TopicsListBean> mTopicsList = new ArrayList<>();
//    private float itemWidth;//获取瀑布流需要的item宽度

    public TopicListAdapter(List<UserPublishTopics.TopicsListBean> topicsList) {
        if(topicsList != null && topicsList.size() > 0) {
            mTopicsList.clear();
            mTopicsList.addAll(topicsList);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TopicListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ucg_recommend_topic_list, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TopicListViewHolder topicListViewHolder = (TopicListViewHolder) holder;
        topicListViewHolder.setData();
    }

    @Override
    public int getItemCount() {
        return mTopicsList.size();
    }

    public void cleanData() {
        mTopicsList.clear();
    }

    public void addData(List<UserPublishTopics.TopicsListBean> mTopicsList) {
        addData(0,mTopicsList);
    }

    public void addData(int position,List<UserPublishTopics.TopicsListBean> topicsList) {
        if (topicsList != null && topicsList.size() > 0) {
            mTopicsList.addAll(position, topicsList);
        }
    }
    private class TopicListViewHolder extends RecyclerView.ViewHolder {
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
            UserPublishTopics.TopicsListBean topicsList = mTopicsList.get(getLayoutPosition() - 1);
            intiView(topicsList);
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
                    topicListOnClickListener.detailsOnClick(topicsList.getId());
                }
            });

            //使用点击事件监听手动设置CheckBox的状态改变，防止刷新时提示信息出现的错误
            cb_collect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean checked = cb_collect.isChecked();
                    cb_collect.setChecked(checked);
//                    if (topicsList.getIsPrase() == 1) {//之前赞过
//                        if (checked) {
//                            cb_collect.setText(topicsList.getParseCount() + "");
//                        } else {
//                            cb_collect.setText(topicsList.getParseCount() - 1 + "");
//                        }
//                    } else {
//                        if (checked) {
//                            cb_collect.setText(topicsList.getParseCount() + 1 + "");
//                        } else {
//                            cb_collect.setText(topicsList.getParseCount() + "");
//                        }
//                    }

                    topicListOnClickListener.topicLikeOnClick(topicsList.getId());
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
//            float scale = itemWidth / topicsList.getImageWidth();
//
//            ViewGroup.LayoutParams layoutParams = iv_icon.getLayoutParams();
//            layoutParams.width = (int) itemWidth;
//            layoutParams.height = (int) (scale * topicsList.getImageHeight());
//
//            iv_icon.setLayoutParams(layoutParams);

        }
    }

    private TopicListOnClickListener topicListOnClickListener;
    public interface TopicListOnClickListener{

        void detailsOnClick(long topicId);

        void topicLikeOnClick(long topicId);
    }

    public void setTopicListOnClickListener(TopicListOnClickListener topicListOnClickListener) {
        this.topicListOnClickListener = topicListOnClickListener;
    }
}
