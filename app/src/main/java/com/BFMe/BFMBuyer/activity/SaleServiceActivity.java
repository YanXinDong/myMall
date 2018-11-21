package com.BFMe.BFMBuyer.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.javaBean.RefundListBean;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.utils.CacheUtils;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.Utils;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.netease.nim.uikit.common.ui.ptr.PullToRefreshBase;
import com.netease.nim.uikit.common.ui.ptr.PullToRefreshListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：售后服务
 * Created by :奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2016/9/19 18:28
 */
public class SaleServiceActivity extends IBaseActivity  {

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

    private PullToRefreshListView listview;
    private String mUserId;//用户id
    private int mPageNo = 1;
    private int mPageSize = 10;

    private Gson gson;
    private RefundListBean mRefundListBean;
    private MyAdapter mMyAdapter;
    private ImageView view_empty_hint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
        setListener();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_saleservice;
    }

    /**
     * 监听
     */
    private void setListener() {

        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPageNo = 1;
                state = STATE_REFREH;
                getDataFromNet();

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

                mPageNo += 1;
                state = STATE_MORE;
                getDataFromNet();
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SaleServiceActivity.this, RefundDetailsActivity.class);
                RefundListBean.ModelsBean item = (RefundListBean.ModelsBean) mMyAdapter.getItem(position - 1);
                intent.putExtra(GlobalContent.REFUND_ID, item.getId() + "");//退货id
                startActivity(intent);
                startAnim();
            }
        });
    }


    private void initData() {
        gson = new Gson();
        mUserId = CacheUtils.getString(this, GlobalContent.USER);
        getDataFromNet();
    }

    private void getDataFromNet() {
        showProgress();
        OkHttpUtils
                .get()
                .url(GlobalContent.GLABAL_GET_REFUND_LIST
                        + "?UserId=" + mUserId
                        + "&PageNo=" + mPageNo
                        + "&PageSize=" + mPageSize
                )
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                    }

                    @Override
                    public void onResponse(String response) {
                        dismissProgress();
                        Logger.e("退款列表数据"+response);
                        RootBean rootBean = gson.fromJson(response, RootBean.class);
                        if (Integer.parseInt(rootBean.ErrCode) == 0) {
                            mRefundListBean = gson.fromJson(rootBean.Data, RefundListBean.class);
                            showReundListData(mRefundListBean.getModels());
                        }else {
                            showToast(rootBean.ResponseMsg);
                        }
                    }
                });
    }

    private void showReundListData(List<RefundListBean.ModelsBean> models) {
        switch (state) {
            case STATE_NORMAL:
                mMyAdapter = new MyAdapter(models);
                listview.setAdapter(mMyAdapter);
                if (mMyAdapter.getCount() == 0) {
                    view_empty_hint.setVisibility(View.VISIBLE);
                }
                break;

            case STATE_REFREH:
                if (mMyAdapter != null) {
                    mMyAdapter.cleanData();
                    mMyAdapter.addData(mRefundListBean.getModels());
                    showToast(getString(R.string.refresh_succeed));
                } else {
                    mMyAdapter = new MyAdapter(models);
                    listview.setAdapter(mMyAdapter);
                    if (mMyAdapter.getCount() == 0) {
                        view_empty_hint.setVisibility(View.VISIBLE);
                    }
                }
                listview.onRefreshComplete();
                break;

            case STATE_MORE:
                if (mMyAdapter != null) {
                    if (mMyAdapter.getCount() >= mRefundListBean.getTotal()) {
                        showToast(getString(R.string.no_more_data1));
                        listview.onRefreshComplete();
                        return;
                    }
                    mMyAdapter.addData(mMyAdapter.getCount(), mRefundListBean.getModels());
                    showToast(getString(R.string.more_succeed));
                } else {
                    mMyAdapter = new MyAdapter(models);
                    listview.setAdapter(mMyAdapter);
                    if (mMyAdapter.getCount() == 0) {
                        view_empty_hint.setVisibility(View.VISIBLE);
                    }
                }
                listview.onRefreshComplete();
                break;
        }
    }


    class MyAdapter extends BaseAdapter {

        private List<RefundListBean.ModelsBean> mModels = new ArrayList<>();

        private MyAdapter(List<RefundListBean.ModelsBean> models) {
            if (models != null && models.size() > 0) {
                mModels.clear();
                mModels.addAll(models);
            }
        }

        @Override
        public int getCount() {
            return mModels.size();
        }

        @Override
        public Object getItem(int position) {
            return mModels.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHodler viewHodler = null;
            if (convertView == null) {
                viewHodler = new ViewHodler();
                convertView = LayoutInflater.from(SaleServiceActivity.this).inflate(R.layout.item_sale_service, parent, false);

                viewHodler.tv_shop_name = (TextView) convertView.findViewById(R.id.tv_shop_name);
                viewHodler.tv_refund_status = (TextView) convertView.findViewById(R.id.tv_refund_status);
                viewHodler.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                viewHodler.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                viewHodler.tv_norms = (TextView) convertView.findViewById(R.id.tv_norms);
                viewHodler.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
                viewHodler.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
                convertView.setTag(viewHodler);

            } else {
                viewHodler = (ViewHodler) convertView.getTag();
            }

            RefundListBean.ModelsBean item = (RefundListBean.ModelsBean) getItem(position);

            viewHodler.tv_shop_name.setText(item.getShopName());
            viewHodler.tv_refund_status.setText(item.getRefundStatus());
            switch (item.getRefundStatus()){
                case "待商家审核":
                case "待商家收货":
                    viewHodler.tv_refund_status.setTextColor(Color.parseColor("#4990E2"));
                        break;
                case "待买家寄货":
                case "待平台确认":
                    viewHodler.tv_refund_status.setTextColor(Color.parseColor("#F6A623"));
                    break;
                case "商家拒绝":
                    viewHodler.tv_refund_status.setTextColor(Color.parseColor("#F55A5F"));
                    break;
                case "退款成功":
                    viewHodler.tv_refund_status.setTextColor(Color.parseColor("#64B709"));
                    break;
                default:
                    viewHodler.tv_refund_status.setTextColor(Color.parseColor("#333333"));
                    break;
            }
            Glide
                    .with(parent.getContext())
                    .load(item.getOrderItemInfo().getThumbnailsUrl())
                    .placeholder(R.drawable.zhanwei1)
                    .error(R.drawable.zhanwei1)
                    .into(viewHodler.iv_icon);
            viewHodler.tv_name.setText(item.getOrderItemInfo().getProductName());
            viewHodler.tv_norms.setText(item.getOrderItemInfo().getColor()+" "+item.getOrderItemInfo().getSize());
            if(item.getOrderItemInfo().getReturnQuantity() == 0) {
                viewHodler.tv_number.setText(getString(R.string.only_refund));
            }else {
                viewHodler.tv_number.setText("x"+item.getOrderItemInfo().getReturnQuantity());
            }
            viewHodler.tv_price.setText(String.format(getString(R.string.total_$),Utils.doubleSave2(item.getAmount())));

            return convertView;
        }

        public void cleanData() {
            mModels.clear();
            notifyDataSetChanged();
        }

        /**
         * 添加数据
         *
         * @param datas
         * @param position
         */
        public void addData(int position, List<RefundListBean.ModelsBean> datas) {
            if (datas != null && datas.size() > 0) {
                mModels.addAll(position, datas);
                notifyDataSetChanged();
            }
        }


        public void addData(List<RefundListBean.ModelsBean> datas) {
            addData(0, datas);
        }

        class ViewHodler {

            private TextView tv_shop_name; //订单的店铺名称
            private TextView tv_refund_status;  //订单状态
            private ImageView iv_icon;//商品图片
            private TextView tv_name;//商品名称
            private TextView tv_norms;//商品规格
            private TextView tv_number; //商品个数
            private TextView tv_price;//总价格
        }
    }

    private void initView() {
        view_empty_hint = (ImageView) findViewById(R.id.view_empty_hint);
        setBackButtonVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.after_sales_service));
        listview = (PullToRefreshListView) findViewById(R.id.lv_saleservice);
    }
}
