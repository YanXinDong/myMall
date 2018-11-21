package com.BFMe.BFMBuyer.ugc.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.ugc.adapter.AttentionAdapter;
import com.BFMe.BFMBuyer.ugc.adapter.FansAdapter;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.ugc.bean.FansBean;
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

import static com.zhy.http.okhttp.OkHttpUtils.post;


/**
 * 我的粉丝加关注二合一
 */
public class AttentionAndFansActivity extends IBaseActivity {

    private boolean isAttentionOrFans;//true from guanzhu ;false from fans
    private XRecyclerView recyclerview;
    private String url;
    private AttentionAdapter attentionAdapter;
    private FansAdapter fansAdapter;
    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE = 2;

    private int state = STATE_NORMAL;//默认为正常状态

    private int pageNo = 1;//记载的页数
    private List<FansBean.DataListBean> dataList;
    private int pageSize = 15;
    private boolean isAttention = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();


    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    private void initData() {
        if (isAttentionOrFans) {
            url = GlobalContent.GET_USER_FOCUS_LIST;//我的关注
        } else {
            url = GlobalContent.GET_USER_FANS_LIST;//我的粉丝
        }
        getAttentionOrFansList();
    }

    private void getAttentionOrFansList() {
        Map<String, String> map = new HashMap<>();
        map.put("userId", mUserId);
        map.put("pageSize", pageSize + "");
        map.put("pageNo", pageNo + "");
        OkHttpUtils
                .post()
                .url(url)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Logger.e("获取关注人或粉丝异常" + e);
                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e(response);
                        RootBean rootBean = new Gson().fromJson(response, RootBean.class);
                        if ("0".equals(rootBean.ErrCode)) {
                            FansBean fansBean = new Gson().fromJson(rootBean.Data, FansBean.class);
                            dataList = fansBean.getDataList();
                            showData();
                        }

                    }
                });
    }

    /**
     * 监听
     */
    private void setListener() {
        recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                state = STATE_REFREH;
                getAttentionOrFansList();
            }

            @Override
            public void onLoadMore() {
                pageNo += 1;
                state = STATE_MORE;
                getAttentionOrFansList();
            }
        });

        if (isAttentionOrFans) {
            attentionAdapter.setOnAttentionClickListener(new AttentionAdapter.OnAttentionClickListener() {
                @Override
                public void OnAttention(int position, boolean isAttention1, ImageView iv) {
                    if (attentionAdapter.getAttentionList().get(position).getAttention()) {//取消关注
                        cancleAttention(position, iv);
                    } else {//关注
                        userConcern(attentionAdapter.getAttentionList().get(position).getUserId());
                        iv.setImageResource(R.drawable.attention);
                        attentionAdapter.getAttentionList().get(position).setAttention(true);
                    }

                }
            });
        } else {
            fansAdapter.setOnFansClickListener(new FansAdapter.OnFansClickListener() {
                @Override
                public void onFans(int position, boolean isFans) {
                    userConcern(fansAdapter.getFanList().get(position).getUserId());
                }
            });
        }
    }

    private void showData() {
        switch (state) {
            //正常状态
            case STATE_NORMAL:
                setAdapter();
                break;
            case STATE_REFREH:
                if (attentionAdapter != null || fansAdapter != null) {
                    if (isAttentionOrFans) {
                        attentionAdapter.cleanData();
                        attentionAdapter.addData(0, dataList);
                    } else {
                        fansAdapter.cleanData();
                        fansAdapter.addData(0, dataList);
                    }
                    recyclerview.refreshComplete();
                } else {
                    setAdapter();
                }
                break;

            //加载更多状态
            case STATE_MORE:
                if (attentionAdapter != null || fansAdapter != null) {
                    if (isAttentionOrFans) {
                        if (dataList.size() < pageSize) {
                            recyclerview.setNoMore(true);
                        } else {
                            attentionAdapter.addData(attentionAdapter.getItemCount(), dataList);
                            recyclerview.loadMoreComplete();
                        }
                    } else {
                        if (dataList.size() < pageSize) {
                            recyclerview.setNoMore(true);
                        } else {
                            fansAdapter.addData(fansAdapter.getItemCount(), dataList);
                            recyclerview.loadMoreComplete();
                        }
                    }
                } else {
                    setAdapter();
                }
                break;
        }
        setListener();
    }

    private void setAdapter() {
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        if (isAttentionOrFans) {
            attentionAdapter = new AttentionAdapter(this, dataList);
            recyclerview.setAdapter(attentionAdapter);
        } else {
            fansAdapter = new FansAdapter(this, dataList);
            recyclerview.setAdapter(fansAdapter);
        }
    }

    private void cancleAttention(final int position, final ImageView iv) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AttentionAndFansActivity.this);
        builder.setMessage(getString(R.string.attention_hint));
        builder.setPositiveButton(getString(R.string.ture), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userConcern(attentionAdapter.getAttentionList().get(position).getUserId());
                iv.setImageResource(R.drawable.no_attention);
                attentionAdapter.getAttentionList().get(position).setAttention(false);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getString(R.string.regreted), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                return;
            }
        });
        builder.create().show();
    }

    private void userConcern(String concernUserId) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", mUserId);
        params.put("concernUserId", concernUserId);
        post()
                .params(params)
                .url(GlobalContent.POST_USER_CONCERN)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("关注" + response);
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        showToast(rootBean.ResponseMsg);
                    }
                });
    }

    private void initView() {
        setBackButtonVisibility(View.VISIBLE);
        recyclerview = (XRecyclerView) findViewById(R.id.recyclerview);
        isAttentionOrFans = getIntent().getBooleanExtra("IsAttentionOrFans", false);
        if (isAttentionOrFans) {
            tv_title.setText(getString(R.string.my_attention));
        } else {
            tv_title.setText(getString(R.string.my_fans));
        }
    }

    @Override
    public int initLayout() {
        return R.layout.activity_attention_and_fans;
    }
}
