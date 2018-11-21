package com.BFMe.BFMBuyer.main.model;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.activity.LoginActivity;
import com.BFMe.BFMBuyer.main.presenter.TopicListItemOnClickListener;
import com.BFMe.BFMBuyer.ugc.activity.TaTalkDetailsActivity;
import com.BFMe.BFMBuyer.ugc.activity.UserInfoActivity;
import com.BFMe.BFMBuyer.ugc.bean.TopicList;
import com.BFMe.BFMBuyer.utils.CacheUtils;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

/**
 * Description：话题列表item的点击事件实现
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/9/14 14:49
 */
public class TopicListItemModel implements TopicListItemOnClickListener {

    private Activity activity;
    private boolean isLogin;//是否登陆
    private String mUserId;
    public TopicListItemModel(Activity activity) {
        this.activity = activity;
        isLogin = CacheUtils.getBoolean(activity, GlobalContent.ISLOGIN);
        mUserId = CacheUtils.getString(activity, GlobalContent.USER);
    }

    @Override
    public void detailsOnClick(long topicId) {
        Intent intent = new Intent(activity, TaTalkDetailsActivity.class);
        intent.putExtra(GlobalContent.TOPIC_ID, topicId);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
    }

    @Override
    public void userTopicOnClick(String userId) {
        Intent intent = new Intent(activity, UserInfoActivity.class);
        intent.putExtra(GlobalContent.USER_INFO_ID, userId);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
    }

    @Override
    public void topicLikeOnClick(View v, TopicList.TopicsListBean data) {
        CheckBox box = (CheckBox) v;
        boolean checked = box.isChecked();
        if (isLogin) {
            if(data.getIsPrase() == 1) {//之前赞过
                if (checked) {
                    box.setText(data.getParseCount()  + "");
                } else {
                    box.setText(data.getParseCount() -1 + "");
                }
            }else {
                if (checked) {
                    box.setText(data.getParseCount() + 1 + "");
                } else {
                    box.setText(data.getParseCount() + "");
                }
            }

            paresTopic(data.getId());
        } else {
            box.setChecked(false);
            activity.startActivity(new Intent(activity, LoginActivity.class));
            activity.overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
        }
    }

    /**
     * 话题点赞
     *
     * @param topicId 话题id
     */
    private void paresTopic(long topicId) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", mUserId);
        params.put("topicId", topicId + "");
        OkHttpUtils
                .post()
                .params(params)
                .url(GlobalContent.POST_USER_PARES_TOPIC)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("点赞" + response);
                    }
                });
    }
}
