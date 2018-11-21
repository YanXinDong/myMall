package com.BFMe.BFMBuyer.main.adapter.ugc;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.CheckBox;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.recycle.base.ItemViewDelegate;
import com.BFMe.BFMBuyer.base.recycle.base.ViewHolder;
import com.BFMe.BFMBuyer.main.model.TopicListItemModel;
import com.BFMe.BFMBuyer.main.presenter.TopicListItemOnClickListener;
import com.BFMe.BFMBuyer.ugc.bean.TopicList;
import com.BFMe.BFMBuyer.utils.UiUtils;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/9/14 11:37
 */
public class UGCTopicListAdapter implements ItemViewDelegate<TopicList.TopicsListBean> {

    private Activity activity;
    private TopicListItemOnClickListener topicListItemOnClickListener;

    public UGCTopicListAdapter(Activity activity) {
        this.activity = activity;
        topicListItemOnClickListener = new TopicListItemModel(activity);
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_ucg_recommend_topic_list;
    }

    @Override
    public boolean isForViewType(TopicList.TopicsListBean item, int position) {
        return true;
    }

    @Override
    public void convert(ViewHolder holder, TopicList.TopicsListBean data, int position) {
        Drawable drawable = activity.getResources().getDrawable(R.drawable.collect_select);
        drawable.setBounds(0, 0, UiUtils.dip2px(18), UiUtils.dip2px(18));
        CheckBox cb_collect = holder.getView(R.id.cb_collect);
        cb_collect.setCompoundDrawables(drawable, null, null, null);

        holder.setImageGlide(R.id.iv_icon,data.getImageUrl());
        holder.setText(R.id.tv_info,data.getContent());
        holder.setImageError(R.id.civ_head_icon,data.getUserImage());
        holder.setText(R.id.tv_user_name,data.getUserName());
        if (data.getIsPrase() == 1) {
            cb_collect.setChecked(true);
        } else {
            cb_collect.setChecked(false);
        }

//        cb_collect.setText(data.getParseCount() + "");
        
        setListener(holder,data);
    }

    private void setListener(ViewHolder holder, final TopicList.TopicsListBean data) {
        holder.setOnClickListener(R.id.rl_recommend_topic, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topicListItemOnClickListener.detailsOnClick(data.getId());
            }
        });

        holder.setOnClickListener(R.id.civ_head_icon, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topicListItemOnClickListener.userTopicOnClick(data.getEncryptUserId());
            }
        });

        holder.setOnClickListener(R.id.cb_collect, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topicListItemOnClickListener.topicLikeOnClick(v,data);
            }
        });
    }

}
