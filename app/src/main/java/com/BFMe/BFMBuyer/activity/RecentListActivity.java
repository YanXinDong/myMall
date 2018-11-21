package com.BFMe.BFMBuyer.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.adapter.RecentListAdapter;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.javaBean.PushMainList;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.ugc.activity.LogisticsActivity;
import com.BFMe.BFMBuyer.ugc.activity.NewsCommentActivity;
import com.BFMe.BFMBuyer.ugc.activity.OfficialActivity;
import com.BFMe.BFMBuyer.ugc.activity.TopicSelectActivity;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.session.activity.P2PMessageActivity;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Request;

/**
 * 消息列表
 */
public class RecentListActivity extends IBaseActivity {

    private RecyclerView rv_recent;
    private RecentListAdapter adapter;
    private IMRecriver imRecriver;
    private List<PushMainList.DataBean> mPushData;
    private List<RecentContact> mRecents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rv_recent = (RecyclerView) findViewById(R.id.rv_recent);
        //返回按钮
        setBackButtonVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.message));

        //获取消息数
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.BFMe.BFMBuyer.ACTION.RECEIVE_MSG");
        imRecriver = new IMRecriver();
        registerReceiver(imRecriver, intentFilter);

    }


    private class IMRecriver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
//            此处加一是因为 返回的消息数从0开始的  1条即为0
            initData();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_recent_list;
    }

    private void initData() {
        getIMList();
        getNetPushList();
    }

    /**
     * 获取推送的消息列表
     */
    private void getNetPushList() {
        OkHttpUtils
                .post()
                .url(GlobalContent.POST_PUSH_MAIN_MESSAGE)
                .addParams("userId",mUserId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("消息中心推送列表=="+response);
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if(rootBean.ErrCode.equals("0")) {
                            PushMainList pushMainList = mGson.fromJson(rootBean.Data, PushMainList.class);
                            mPushData = pushMainList.getData();
                            verifyData();
                        }
                    }
                });
    }

    private void getIMList() {
        //获取最近联系人列表
        NIMClient.getService(MsgService.class).queryRecentContacts()
                .setCallback(new RequestCallbackWrapper<List<RecentContact>>() {
                    @Override
                    public void onResult(int code, List<RecentContact> recents, Throwable e) {
                        // recents参数即为最近联系人列表（最近会话列表）
                        mRecents = recents;
                        verifyData();
                    }
                });
    }

    private void verifyData() {
        if(mRecents != null && mPushData != null) {
            showData();
        }
    }

    private void showData() {
        adapter = new RecentListAdapter(RecentListActivity.this, mRecents,mPushData);
        rv_recent.setAdapter(adapter);
        rv_recent.setLayoutManager(new LinearLayoutManager(RecentListActivity.this, LinearLayoutManager.VERTICAL, false));
        setListener();
    }

    private void setListener() {
        adapter.setOnItemClickListener(new RecentListAdapter.OnItemClickListener() {
            @Override
            public void RecentOnClickListener(String s) {//进入点对点了解页面
                //  P2PMessageActivity.IMFlag = false;
                P2PMessageActivity.shopDetailImg ="";
                P2PMessageActivity.shopDetailDes = "";
                P2PMessageActivity.shopDetailPrice = "";
                NimUIKit.startChatting(RecentListActivity.this, s, SessionTypeEnum.P2P, null, null);
                overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
            }

            @Override
            public void CommentOnClickListener() {
                startActivity(new Intent(RecentListActivity.this, NewsCommentActivity.class));
                startAnim();
            }

            @Override
            public void PushOnClick(int position, String title) {
                jumpIntent(position, title);
            }
        });
        adapter.setOnLongClickListener(new RecentListAdapter.OnLongClickListener() {
            @Override
            public void OnLongClickListener(int position, RecentContact s) {
                //删除该条对话
                DeleteDialog(position, s);
            }
        });
    }

    private void jumpIntent(int position, String title) {
        Intent intent = new Intent();
        switch (position) {
            case 0:
                intent.setClass(this, OfficialActivity.class);
                break;
            case 1:
                intent.setClass(this, TopicSelectActivity.class);
                break;
            default:
                intent.setClass(this, LogisticsActivity.class);
                break;
        }
        intent.putExtra("TITLE", title);
        startActivity(intent);
        startAnim();
    }


    private void DeleteDialog(final int position, final RecentContact s) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(RecentListActivity.this);
        builder.setMessage(getString(R.string.delete_message));
        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                NIMClient.getService(MsgService.class).deleteRecentContact(s);
                adapter.deleteItem(position);
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(imRecriver);
    }

}
