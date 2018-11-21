package com.BFMe.BFMBuyer.fragment.commentfragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.activity.CommentProductsActivity;
import com.BFMe.BFMBuyer.adapter.CommentAdapter;
import com.BFMe.BFMBuyer.javaBean.CommentBean;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.google.gson.Gson;
import com.netease.nim.uikit.common.ui.ptr.PullToRefreshBase;
import com.netease.nim.uikit.common.ui.ptr.PullToRefreshListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

/**
 * Description：评论的基类
 * Created by : 奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/5/22 17:21
 */

public abstract class BaseCommentFragment extends Fragment {
    private Dialog mDialog;
    private Context context;
    private PullToRefreshListView lvList;
    private ImageView zhanweiRabit;
    private String productId;
    private int pageNo = 1;
    private int pageSize = 10;
    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE = 2;

    private int state = STATE_NORMAL;//默认为正常状态
    private CommentAdapter commentAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        CommentProductsActivity activity = (CommentProductsActivity) getActivity();
        productId = activity.getProductId();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(context, R.layout.lisview_conment, null);
        lvList = (PullToRefreshListView) view.findViewById(R.id.lv_list);
        zhanweiRabit = (ImageView) view.findViewById(R.id.zhanwei_rabit);
        return view;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            pageNo = 1;
            state = STATE_NORMAL;
            getDataFromNet();
        }
    }
    private void setListener() {

        lvList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageNo = 1;
                state = STATE_REFREH;
                getDataFromNet();
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageNo += 1;
                state = STATE_MORE;
               getDataFromNet();
            }
        });
    }
    private void getDataFromNet() {
        showProgress(context,getString(R.string.load));
        Map<String, String> map = new HashMap<>();
        map.put("ProductId", productId);
        map.put("PageNo", pageNo + "");
        map.put("PageSize", pageSize + "");
        map.put("Level", getPosition() + "");
        OkHttpUtils
                .get()
                .url(GlobalContent.GLOBSAL_COMMENT)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        RootBean rootBean = gson.fromJson(response, RootBean.class);
                        Logger.e(response);
                        if ("0".equals(rootBean.ErrCode)) {
                            dismissProgress();
                            CommentBean commentBean = gson.fromJson(rootBean.Data, CommentBean.class);
                            showShopCommodity(commentBean);
                        }
                    }
                });
    }
    private void showShopCommodity(CommentBean commentBean) {
        ArrayList<CommentBean.CommentData> commentList = commentBean.Comments;
        if(state != STATE_MORE &&commentBean.Comments.size()<=0){
            zhanweiRabit.setVisibility(View.VISIBLE);
            lvList.setVisibility(View.GONE);
        }
        switch (state) {
            //正常状态
            case STATE_NORMAL:
                commentAdapter = new CommentAdapter(context, commentList);
                lvList.setAdapter(commentAdapter);
                break;
            case STATE_REFREH:
                if(commentAdapter !=null){
                    commentAdapter.cleanData();
                    commentAdapter.addData(commentList);
                }else{
                    commentAdapter = new CommentAdapter(context, commentList);
                    lvList.setAdapter(commentAdapter);
                }
                lvList.onRefreshComplete();
                Toast.makeText(context, getString(R.string.refresh_succeed), Toast.LENGTH_SHORT).show();
                break;

            case STATE_MORE:
                if(commentAdapter.getCount()>=commentBean.Total){
                    Toast.makeText(context, getString(R.string.no_more_data1), Toast.LENGTH_SHORT).show();
                    lvList.onRefreshComplete();
                   return;
                }
                if(commentAdapter !=null){
                    commentAdapter.addData(commentAdapter.getCount(), commentList);
                }else {
                    commentAdapter = new CommentAdapter(context, commentList);
                    lvList.setAdapter(commentAdapter);
                }
                lvList.onRefreshComplete();
                Toast.makeText(context, getString(R.string.more_succeed), Toast.LENGTH_SHORT).show();
                break;
        }
        setListener();
    }
    public abstract int getPosition();

    public void showProgress(Context context, String hint) {
        if (mDialog == null) {
            mDialog = new Dialog(context, R.style.Dialog);
        }
        View view = View.inflate(context, R.layout.progressbar, null);
        TextView textView = (TextView) view.findViewById(R.id.pb_text);
        textView.setText(hint);
        mDialog.setContentView(view);
        mDialog.show();
        mDialog.setCancelable(false);
    }

    public void dismissProgress() {
        if (null != mDialog && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
