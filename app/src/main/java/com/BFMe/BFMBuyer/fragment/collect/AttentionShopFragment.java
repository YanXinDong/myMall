package com.BFMe.BFMBuyer.fragment.collect;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.adapter.AttentionShopAdapter;
import com.BFMe.BFMBuyer.base.BaseFragment;
import com.BFMe.BFMBuyer.javaBean.AttentionBean;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.google.gson.Gson;
import com.netease.nim.uikit.common.ui.ptr.PullToRefreshBase;
import com.netease.nim.uikit.common.ui.ptr.PullToRefreshListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import static com.zhy.http.okhttp.OkHttpUtils.post;

/**
 * Description：关注的店铺
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/6/12 15:58
 */
public class AttentionShopFragment extends BaseFragment {

    private PullToRefreshListView lv_attention;
    private ImageView view_empty_hint;
    /**
     * 正常状态
     */
    private static final int STATE_NORMAL = 0;
    /**
     * 刷新状态
     */
    private static final int STATE_REFREH = 1;
    /**
     * 加载更多状态
     */
    private static final int STATE_MORE = 2;

    private int state = STATE_NORMAL;//默认为正常状态

    private int pageNo = 1;//记载的页数
    private int pageSize = 10;//一页的数据
    private AttentionShopAdapter mAdapter;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            getNetData();
        }
    }

    private void getNetData() {
        showProgress();

        Map<String, String> params = new HashMap<>();
        params.put("userId", mUserId);
        params.put("pageNo", pageNo + "");
        params.put("pageSize", pageSize + "");
        post()
                .params(params)
                .url(GlobalContent.GLOBAL_ATTENTION)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                        dismissProgress();
                    }

                    @Override
                    public void onResponse(String response) {
                        dismissProgress();
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (rootBean.ErrCode.equals("0")) {
                            AttentionBean attentionBean = mGson.fromJson(rootBean.Data, AttentionBean.class);

                            showData(attentionBean);
                        } else {
                            showToast(rootBean.ResponseMsg);
                        }
                    }
                });
    }

    private void showData(AttentionBean attentionBean) {

        switch (state) {
            //正常状态
            case STATE_NORMAL:
                mAdapter = new AttentionShopAdapter(attentionBean.getShops(),mActivity);
                lv_attention.setAdapter(mAdapter);
                break;

            //刷新状态
            case STATE_REFREH:
                if (mAdapter != null) {
                    //把原来的清除
                    mAdapter.cleanData();
                    //适配器重新添加数据
                    mAdapter.addData(attentionBean.getShops());
                    showToast(getString(R.string.refresh_succeed));
                } else {
                    mAdapter = new AttentionShopAdapter(attentionBean.getShops(), mActivity);
                    lv_attention.setAdapter(mAdapter);

                }
                //关闭刷新显示
                lv_attention.onRefreshComplete();
                break;

            //加载更多状态
            case STATE_MORE:
                if (mAdapter != null) {
                    if (attentionBean.getShops().size() < 10) {

                        showToast(getString(R.string.no_more_data1));
                        //关闭刷新显示
                        lv_attention.onRefreshComplete();
                        return;
                    }
                    //在原来的基础上添加数据
                    mAdapter.addData(mAdapter.getCount(), attentionBean.getShops());
                    showToast(getString(R.string.more_succeed));
                } else {
                    mAdapter = new AttentionShopAdapter(attentionBean.getShops(), mActivity);
                    lv_attention.setAdapter(mAdapter);

                }
                lv_attention.onRefreshComplete();//隐藏上拉加载
                break;
        }

        if (state != STATE_MORE) {
            if (attentionBean.getShops() == null || attentionBean.getShops().size() <= 0) {
                view_empty_hint.setVisibility(View.VISIBLE);
            }
        }
        setListener();
    }

    /**
     * 设置监听
     */
    private void setListener() {

        //刷新跟加载更多的监听
        lv_attention.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            //下拉刷新
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //加载第一页
                pageNo = 1;
                //更改状态　为刷新状态
                state = STATE_REFREH;
                getNetData();

            }

            //上拉加载更多
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

                //加载第一页
                pageNo += 1;
                //更改状态　为加载更多状态
                state = STATE_MORE;
                getNetData();
            }
        });

        //取消关注
        mAdapter.setOnClickListenerDelete(new AttentionShopAdapter.OnClickListenerDelete() {

            @Override
            public void onDeleteClick(String shopId) {

                Map<String, String> params = new HashMap<>();
                params.put("userId", mUserId);
                params.put("shopId", shopId);

                OkHttpUtils
                        .post()
                        .params(params)
                        .url(GlobalContent.GLOBAL_CANCEL_ATTENTION_SHOP)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(okhttp3.Request request, Exception e) {

                            }

                            @Override
                            public void onResponse(String response) {
                                RootBean bean = new Gson().fromJson(response, RootBean.class);
                                if (bean.ErrCode.equals("0")) {
                                    //加载第一页
                                    pageNo = 1;
                                    //更改状态　为刷新状态
                                    state = STATE_REFREH;
                                    getNetData();
                                } else {
                                    Logger.e(  "取消失败");
                                }
                            }
                        });
            }
        });
    }

    @Override
    public View initView(LayoutInflater inflater) {
        View inflate = inflater.inflate(R.layout.activity_attentionshop, null, false);
        lv_attention = (PullToRefreshListView) inflate.findViewById(R.id.lv_attention);
        view_empty_hint = (ImageView) inflate.findViewById(R.id.view_empty_hint);

        return inflate;
    }
}
