package com.BFMe.BFMBuyer.ugc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.activity.LoginActivity;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.ugc.adapter.AllCommentAdapter;
import com.BFMe.BFMBuyer.ugc.bean.CommentItem;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Request;

/**
 * 全部评论页面
 */
public class AllCommentActivity extends IBaseActivity {

    @BindView(R.id.rv_all_comment)
    XRecyclerView rv_all_comment;

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

    private int pageNo = 1;//记载的页数
    private int pageSize = 10;//一页的数据
    private long mTopicId;
    private AllCommentAdapter mAllCommentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        mTopicId = intent.getLongExtra(GlobalContent.TOPIC_ID, 0);

        getNetData();
    }

    private void getNetData() {
        showProgress();
        Map<String, String> params = new HashMap<>();
        if (isLogin) {
            params.put("userId", mUserId);
        } else {
            params.put("userId", " ");
        }
        params.put("pageSize", pageSize + "");
        params.put("pageNo", pageNo + "");
        params.put("topicId", mTopicId + "");
        Log.e("TAG", "params==" + params);
        OkHttpUtils
                .post()
                .params(params)
                .url(GlobalContent.POST_TOPIC_COMMENT_LIST)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("全部评论" + response);
                        dismissProgress();

                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (rootBean.ErrCode.equals("0")) {
                            Type type = new TypeToken<List<CommentItem>>() {
                            }.getType();
                            List<CommentItem> commentList = mGson.fromJson(rootBean.Data, type);
                            showData(commentList);
                        }
                    }
                });
    }

    private void showData(List<CommentItem> commentList) {
        switch (state) {
            case STATE_NORMAL:
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AllCommentActivity.this);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                rv_all_comment.setLayoutManager(linearLayoutManager);
                mAllCommentAdapter = new AllCommentAdapter(commentList);
                rv_all_comment.setAdapter(mAllCommentAdapter);

                if (commentList.size() < 10) {
                    rv_all_comment.setNoMore(true);
                }
                break;
            case STATE_REFRESH:
                state = STATE_NORMAL;
                rv_all_comment.refreshComplete();
                if (commentList.size() < 10) {
                    rv_all_comment.setNoMore(true);
                }

                if (mAllCommentAdapter != null) {
                    mAllCommentAdapter.cleanData();
                    mAllCommentAdapter.addData(commentList);
                    mAllCommentAdapter.notifyDataSetChanged();
                }

                break;
            case STATE_MORE:
                state = STATE_NORMAL;
                rv_all_comment.loadMoreComplete();
                if (mAllCommentAdapter != null) {

                    if (commentList.size() < 10) {
                        rv_all_comment.setNoMore(true);
                        mAllCommentAdapter.addData(mAllCommentAdapter.getItemCount(), commentList);
                    } else {
                        mAllCommentAdapter.addData(mAllCommentAdapter.getItemCount(), commentList);
                    }
                    mAllCommentAdapter.notifyItemChanged(mAllCommentAdapter.getItemCount());
                }

                break;

        }
        if (mAllCommentAdapter != null) {
            setListener();
        }
    }

    private void setListener() {
        rv_all_comment.setLoadingListener(new XRecyclerView.LoadingListener() {
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

        mAllCommentAdapter.setOnCommentItemClickListener(new AllCommentAdapter.OnCommentItemClickListener() {
            @Override
            public void praiseOnClick(View v,CommentItem item) {
                CheckBox box = (CheckBox) v;
                boolean checked = box.isChecked();
                if(isLogin) {

                    box.setChecked(checked);
                    if(item.getIsParse() == 1) {//之前赞过
                        if (checked) {
                            box.setText(item.getParseCount()  + "");
                        } else {
                            box.setText(item.getParseCount() -1 + "");
                        }
                    }else {
                        if (checked) {
                            box.setText(item.getParseCount() + 1 + "");
                        } else {
                            box.setText(item.getParseCount() + "");
                        }
                    }

                    commentPraise(item.getId());
                }else {
                    box.setChecked(false);
                    startActivity(new Intent(AllCommentActivity.this, LoginActivity.class));
                    startAnim();
                }
            }

            @Override
            public void userOnClick(String userId) {
                Intent intent = new Intent(AllCommentActivity.this, UserInfoActivity.class);
                intent.putExtra(GlobalContent.USER_INFO_ID, userId);
                startActivity(intent);
                startAnim();
            }
        });
    }

    /**
     * 点赞评论
     * @param commentId 评论ID
     */
    private void commentPraise(String commentId) {
        OkHttpUtils
                .post()
                .addParams("userId",mUserId)
                .addParams("topicCommentsId",commentId)
                .url(GlobalContent.POST_PARSE_TOPIC_COMMENTS)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("评论点赞"+response);
                    }
                });
    }

    private void initView() {
        tv_title.setText(getString(R.string.all_comment));
        setBackButtonVisibility(View.VISIBLE);
        rv_all_comment.setFootViewText(getString(R.string.load), getString(R.string.no_more_data));
    }

    @Override
    public int initLayout() {
        return R.layout.activity_all_comment;
    }
}
