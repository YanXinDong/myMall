package com.BFMe.BFMBuyer.ugc.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.ugc.activity.TaTalkDetailsActivity;
import com.BFMe.BFMBuyer.ugc.adapter.TopicListAdapter;
import com.BFMe.BFMBuyer.ugc.bean.UserPublishTopics;
import com.BFMe.BFMBuyer.utils.CacheUtils;
import com.BFMe.BFMBuyer.utils.Config;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

import static com.BFMe.BFMBuyer.R.id.rv_user_info;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/6/20 11:24
 */
public abstract class BaseTopicFragment extends Fragment{

    private String mUserId;
    private boolean isLogin;//是否登陆
    private Gson mGson = new Gson();
    private Context mContext;
    XRecyclerView xRVTopicList;

    /**
     * 正常状态
     */
    private static final int STATE_NORMAL = 0;
    /**
     * 刷新状态
     */
    private static final int STATE_REFRESH = 1;
    /**
     * 加载更多状态
     */
    private static final int STATE_MORE = 2;

    private int state = STATE_NORMAL;//默认为正常状态
    //加载的页数
    private int pageNo = 1;
    //一页的数据
    private int pageSize = 10;
    private List<UserPublishTopics.TopicsListBean> mTopicsList;
    private TopicListAdapter mTopicListAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        isLogin = CacheUtils.getBoolean(mContext, GlobalContent.ISLOGIN);
        mUserId = CacheUtils.getString(mContext, GlobalContent.USER);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.activity_user_info, container, false);
        xRVTopicList = (XRecyclerView) inflate.findViewById(rv_user_info);
        xRVTopicList.setFootViewText(getString(R.string.load), getString(R.string.no_more_data));
        return inflate;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            //加载第一页
            pageNo = 1;
            //默认正常状态
            state = STATE_NORMAL;
            getNetData();
        }
    }

    private void getNetData() {
        showProgress();
        Map<String, String> params = new HashMap<>();
        if (isLogin) {
            params.put("userId", mUserId);
        }
        params.put("searchUserId", mUserId);
        params.put("pageSize", pageSize + "");
        params.put("pageNo", pageNo + "");
        params.put("topicState", getTopicState());
        OkHttpUtils
                .post()
                .params(params)
                .url(GlobalContent.POST_USER_PUBLISH_TOPICS)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("TAG", "BaseTopicFragment==" + e.toString());
                    }

                    @Override
                    public void onResponse(String response) {
                        dismissProgress();
                        Logger.e("BaseTopicFragment==" + response);
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (rootBean.ErrCode.equals("0")) {
                            UserPublishTopics userPublishTopics = mGson.fromJson(rootBean.Data, UserPublishTopics.class);
                            mTopicsList = userPublishTopics.getTopicsList();
                            showData();
                        }
                    }
                });
    }

    private void showData() {
        switch (state) {
            case STATE_NORMAL:
                xRVTopicList.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                mTopicListAdapter = new TopicListAdapter(mTopicsList);
                xRVTopicList.setAdapter(mTopicListAdapter);
                if (mTopicsList.size() < 10) {
                    xRVTopicList.setNoMore(true);
                }
                break;
            case STATE_REFRESH:
                state = STATE_NORMAL;
                xRVTopicList.refreshComplete();
                if (mTopicsList.size() < 10) {
                    xRVTopicList.setNoMore(true);
                }

                if (mTopicListAdapter != null) {
                    mTopicListAdapter.cleanData();
                    mTopicListAdapter.addData(mTopicsList);
                    mTopicListAdapter.notifyDataSetChanged();
                }

                break;
            case STATE_MORE:
                state = STATE_NORMAL;
                xRVTopicList.loadMoreComplete();

                if (mTopicListAdapter != null) {
                    if (mTopicsList.size() < 10) {
                        xRVTopicList.setNoMore(true);
                        mTopicListAdapter.addData(mTopicListAdapter.getItemCount(), mTopicsList);
                    } else {
                        mTopicListAdapter.addData(mTopicListAdapter.getItemCount(), mTopicsList);
                    }
                    mTopicListAdapter.notifyItemChanged(mTopicListAdapter.getItemCount());
                }
                break;

        }

        setListener();
    }

    private void setListener() {
        xRVTopicList.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                state = STATE_REFRESH;
                getNetData();
            }

            @Override
            public void onLoadMore() {
                pageNo += 1;
                state = STATE_MORE;
                getNetData();
            }
        });

        mTopicListAdapter.setTopicListOnClickListener(new TopicListAdapter.TopicListOnClickListener() {
            @Override
            public void detailsOnClick(long topicId) {
                Intent intent = new Intent(mContext, TaTalkDetailsActivity.class);
                intent.putExtra(GlobalContent.TOPIC_ID, topicId);
                mContext.startActivity(intent);
                startAnim();
            }

            @Override
            public void topicLikeOnClick(long topicId) {
                paresTopic(topicId);
            }
        });
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


    public abstract String getTopicState();

    protected void showProgress() {
        Config.getInstance().showProgress(mContext);
    }
    protected void showProgress(String hint){
        Config.getInstance().showProgress(mContext,hint);
    }

    protected void dismissProgress() {
        Config.getInstance().dismissProgress();
    }
    //启动Activity动画
    protected void startAnim() {
        getActivity().overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
    }

    //关闭Activity动画
    protected void exitAnim() {
        getActivity().overridePendingTransition(R.anim.enter_back, R.anim.exit_back);
    }
}
