package com.BFMe.BFMBuyer.main.presenter;

import android.view.View;

import com.BFMe.BFMBuyer.ugc.bean.TopicList;

/**
 * Description：话题列表item的点击事件接口
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/9/14 14:26
 */
public interface TopicListItemOnClickListener {
    void detailsOnClick(long topicId);

    void userTopicOnClick(String userId);

    void topicLikeOnClick(View v, TopicList.TopicsListBean topicInfo);
}
