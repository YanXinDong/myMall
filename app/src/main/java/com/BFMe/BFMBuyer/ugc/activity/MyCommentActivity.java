package com.BFMe.BFMBuyer.ugc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.ugc.adapter.MyCommentAdapter;
import com.BFMe.BFMBuyer.ugc.bean.MyCommentItem;
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
 * 我的评论界面
 */
public class MyCommentActivity extends IBaseActivity {

    @BindView(R.id.rv_my_comment)
    XRecyclerView rv_my_comment;

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
    private MyCommentAdapter mMyCommentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        getNetData();
    }

    private void getNetData() {
        showProgress();
        Map<String, String> params = new HashMap<>();
        params.put("userId",mUserId);
        params.put("pageNo",pageNo+"");
        params.put("pageSize",pageSize+"");
        Log.e("TAG", "params=="+params);
        OkHttpUtils
                .post()
                .params(params)
                .url(GlobalContent.POST_USER_COMMENT_LIST)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        dismissProgress();
                        Logger.e("我的评论=="+response);
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if(rootBean.ErrCode.equals("0")) {
                            Type type = new TypeToken<List<MyCommentItem>>() {
                            }.getType();
                            List<MyCommentItem> myCommentItems = mGson.fromJson(rootBean.Data, type);
                            showData(myCommentItems);

                        }
                    }
                });

    }

    private void showData(List<MyCommentItem> myCommentItems) {
        switch (state) {
            case STATE_NORMAL:
                rv_my_comment.setLayoutManager(new LinearLayoutManager(this));
                mMyCommentAdapter = new MyCommentAdapter(myCommentItems);
                rv_my_comment.setAdapter(mMyCommentAdapter);
                if (myCommentItems.size() < 10) {
                    rv_my_comment.setNoMore(true);
                }
                break;
            case STATE_REFRESH:
                state = STATE_NORMAL;
                rv_my_comment.refreshComplete();
                if (myCommentItems.size() < 10) {
                    rv_my_comment.setNoMore(true);
                }

                if (mMyCommentAdapter != null) {
                    mMyCommentAdapter.cleanData();
                    mMyCommentAdapter.addData(myCommentItems);
                    mMyCommentAdapter.notifyDataSetChanged();
                }

                break;
            case STATE_MORE:
                state = STATE_NORMAL;
                rv_my_comment.loadMoreComplete();

                if (mMyCommentAdapter != null) {
                    if (myCommentItems.size() < 10) {
                        rv_my_comment.setNoMore(true);
                        mMyCommentAdapter.addData(mMyCommentAdapter.getItemCount(), myCommentItems);
                    } else {
                        mMyCommentAdapter.addData(mMyCommentAdapter.getItemCount(), myCommentItems);
                    }
                    mMyCommentAdapter.notifyItemChanged(mMyCommentAdapter.getItemCount());
                }
                break;

        }

        setListener();
        
    }

    private void setListener() {
        rv_my_comment.setLoadingListener(new XRecyclerView.LoadingListener() {
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

        mMyCommentAdapter.setOnItemClickListener(new MyCommentAdapter.OnItemClickListener() {
            @Override
            public void onClick(long topicId) {
                Intent intent = new Intent(MyCommentActivity.this, TaTalkDetailsActivity.class);
                intent.putExtra(GlobalContent.TOPIC_ID, topicId);
                startActivity(intent);
                startAnim();
            }
        });
    }

    private void initView() {
        tv_title.setText(getString(R.string.my_comment));
        setBackButtonVisibility(View.VISIBLE);
        rv_my_comment.setFootViewText(getString(R.string.load), getString(R.string.no_more_data));
    }

    @Override
    public int initLayout() {
        return R.layout.activity_my_comment;
    }
}
