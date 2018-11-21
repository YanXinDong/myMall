package com.BFMe.BFMBuyer.fragment.collect;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.commodity.activity.ProducetDetailsActivity;
import com.BFMe.BFMBuyer.adapter.CollectCommodityAdapter;
import com.BFMe.BFMBuyer.base.BaseFragment;
import com.BFMe.BFMBuyer.javaBean.CollectCommodityBean;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.netease.nim.uikit.common.ui.ptr.PullToRefreshBase;
import com.netease.nim.uikit.common.ui.ptr.PullToRefreshListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

/**
 * Description：喜欢的商品
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/6/12 15:58
 */
public class CollectCommodityFragment extends BaseFragment {

    private PullToRefreshListView lv_collect_commodity;
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
    private CollectCommodityAdapter mCollectCommodityAdapter;

    @Override
    public View initView(LayoutInflater inflater) {
        View inflate = inflater.inflate(R.layout.activity_collect_commodity, null, false);
        lv_collect_commodity = (PullToRefreshListView)inflate.findViewById(R.id.lv_collect_commodity);
        view_empty_hint = (ImageView)inflate.findViewById(R.id.view_empty_hint);
        return inflate;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
//            //加载第一页
//            pageNo = 1;
//            //更改状态　为默认状态
//            state = STATE_NORMAL;
            getNetData();
        }
    }

    private void getNetData() {
        showProgress();
        Map<String, String> params = new HashMap<>();
        params.put("userId",mUserId);
        params.put("pageNo",pageNo+"");
        params.put("pageSize",pageSize+"");

        OkHttpUtils
                .post()
                .params(params)
                .url(GlobalContent.COLLECT_PRODUCTS)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        dismissProgress();
                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e(  "喜欢的商品=="+response);
                        dismissProgress();
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if(rootBean.ErrCode.equals("0")) {
                            showData(mGson.fromJson(rootBean.Data, CollectCommodityBean.class));
                        }else {
                            showToast(rootBean.ResponseMsg);
                        }
                    }
                });
    }


    private void showData(CollectCommodityBean collectCommodityBean) {

        switch (state) {
            //正常状态
            case STATE_NORMAL:
                mCollectCommodityAdapter = new CollectCommodityAdapter(collectCommodityBean.getProducts());
                lv_collect_commodity.setAdapter(mCollectCommodityAdapter);
                break;

            //刷新状态
            case STATE_REFREH:
                if(mCollectCommodityAdapter!=null){
                    //把原来的清除
                    mCollectCommodityAdapter.cleanData();
                    //适配器重新添加数据
                    mCollectCommodityAdapter.addData(collectCommodityBean.getProducts());
                    showToast(getString(R.string.refresh_succeed));
                }else{
                    mCollectCommodityAdapter = new CollectCommodityAdapter(collectCommodityBean.getProducts());
                    lv_collect_commodity.setAdapter(mCollectCommodityAdapter);

                }
                //关闭刷新显示
                lv_collect_commodity.onRefreshComplete();
                break;

            //加载更多状态
            case STATE_MORE:
                if(mCollectCommodityAdapter!=null){
                    if (mCollectCommodityAdapter.getCount() >= collectCommodityBean.getTotal()) {
                        showToast(getString(R.string.no_more_data1));
                        //关闭刷新显示
                        lv_collect_commodity.onRefreshComplete();
                        return;
                    }
                    //在原来的基础上添加数据
                    mCollectCommodityAdapter.addData(mCollectCommodityAdapter.getCount(),collectCommodityBean.getProducts());
                    showToast(getString(R.string.more_succeed));
                }else{
                    mCollectCommodityAdapter = new CollectCommodityAdapter(collectCommodityBean.getProducts());
                    lv_collect_commodity.setAdapter(mCollectCommodityAdapter);

                }
                lv_collect_commodity.onRefreshComplete();//隐藏上拉加载
                break;
        }

        if (state != STATE_MORE) {
            if (collectCommodityBean.getProducts() == null || collectCommodityBean.getProducts().size() <= 0) {
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
        lv_collect_commodity.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

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

        lv_collect_commodity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CollectCommodityBean.ProductsBean item = (CollectCommodityBean.ProductsBean) mCollectCommodityAdapter.getItem(position -1);
                Intent intent = new Intent(mActivity,ProducetDetailsActivity.class);
                intent.putExtra("productId",item.getProductId());
                intent.putExtra("ShopId",item.getShopId());
                startActivity(intent);
                startAnim();
            }
        });
    }
}
