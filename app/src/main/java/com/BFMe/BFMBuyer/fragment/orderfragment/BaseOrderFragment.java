package com.BFMe.BFMBuyer.fragment.orderfragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.activity.ChoosePayActivity;
import com.BFMe.BFMBuyer.activity.CommentDetailActivity;
import com.BFMe.BFMBuyer.activity.OrderDetailActivity;
import com.BFMe.BFMBuyer.commodity.activity.ProducetDetailsActivity;
import com.BFMe.BFMBuyer.activity.ShoppingCartActivity;
import com.BFMe.BFMBuyer.adapter.OrderAdapter;
import com.BFMe.BFMBuyer.javaBean.CartBean;
import com.BFMe.BFMBuyer.javaBean.OrderBean;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.utils.CacheUtils;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.OperateStringUtlis;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.google.gson.Gson;
import com.netease.nim.uikit.common.ui.ptr.PullToRefreshBase;
import com.netease.nim.uikit.common.ui.ptr.PullToRefreshListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * Description:   订单的基类
 */
public abstract class BaseOrderFragment extends Fragment {

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
    //加载的页数
    private int pageno = 1;
    //一页的数据
    private int pagesize = 10;

    public Context ctx;
    private List<OrderBean.OrdersBean> ordersList;
    private PullToRefreshListView lvOrderList;

    private ImageView view_empty_hint;
    private OrderAdapter orderAdapter = null;
    private Gson gson;

    private String mOrderId;

    private Dialog mDialog;
    private String mUserId;

    private int mAddCartNumber;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = getActivity();
        gson = new Gson();

        mUserId = CacheUtils.getString(ctx, GlobalContent.USER);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.order_listview, null, false);
        lvOrderList = (PullToRefreshListView) view.findViewById(R.id.lv_order_List);
        view_empty_hint = (ImageView) view.findViewById(R.id.view_empty_hint);

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            //加载第一页
            pageno = 1;
            //默认正常状态
            state = STATE_NORMAL;
            getNetOrderData();
        }
    }

    /**
     * 刷新监听
     */
    private void setListener() {
        lvOrderList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            //下拉刷新
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                refresh();
            }

            //上拉加载更多
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //加载第下一页
                pageno += 1;
                //更改状态　为加载更多状态
                state = STATE_MORE;
                getNetOrderData();

            }
        });

        //订单列表的监听
        if (orderAdapter != null) {
            orderAdapter.setOnCheckBoxClickListener(new OrderAdapter.OnOrderItemClickListener() {

                //item的点击事件监听
                @Override
                public void onOrderItemClick(View view, OrderBean.OrdersBean orderData) {
                    Intent intent = new Intent(ctx, OrderDetailActivity.class);
                    intent.putExtra(GlobalContent.ORDER_ID, orderData.getId());
                    intent.putExtra(GlobalContent.ORDER_STATUS, orderData.getOrderStatus());
                    intent.putExtra("ORDER_COMMENT_INFO", orderData.getOrderCommentInfo());
                    getActivity().startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
                }

                /**
                 * 取消订单 再次购买 延期收货的点击事件监听
                 * @param view
                 * @param orderData
                 */
                @Override
                public void onCancelOrderClick(View view, OrderBean.OrdersBean orderData) {
                    cancelOrBuyAgain(orderData);
                }

                /**
                 * 去支付  确认收货   评论
                 * @param view
                 * @param orderData
                 */
                @Override
                public void onpayAndCommentClick(View view, OrderBean.OrdersBean orderData) {
                    payAndComment(orderData);
                }
            });
        }

    }

    /**
     * 取消订单  再次购买  延期收货  按钮
     * @param orderData 订单信息
     */
    private void cancelOrBuyAgain(OrderBean.OrdersBean orderData) {

        if(orderData.getOrderStatus().equals("WaitPay")) {
            showNormalDialog(orderData);
            return;
        }

        if(orderData.getOrderStatus().equals("Finish")) {
            buyAgain(orderData);
            return;
        }

        if(orderData.getOrderStatus().equals("WaitReceiving")) {
            delayTheGoods(orderData.getId());
        }
    }

    /**
     * 延期收货
     * @param id 订单id
     */
    private void delayTheGoods(String id) {
        showProgress(ctx,"请稍后...");
        Map<String, String> params = new HashMap<>();
        params.put("userId",mUserId);
        params.put("orderId",id);
        OkHttpUtils
                .post()
                .params(params)
                .url(GlobalContent.DELAY_THE_GOODS)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        dismissProgress();
                        Logger.e(  "延期收货=="+response);
                        RootBean rootBean = gson.fromJson(response, RootBean.class);
                        if(rootBean.ErrCode.equals("0")) {
                            Toast.makeText(ctx, getString(R.string.finish), Toast.LENGTH_SHORT).show();
                            refresh();
                        }else {
                            Toast.makeText(ctx, rootBean.ResponseMsg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * 跳转到商品详情 or  购物车
     * @param orderData 订单信息
     */
    private void buyAgain(OrderBean.OrdersBean orderData) {

        List<OrderBean.OrdersBean.ProductListBean> productList = orderData.getProductList();

        showProgress(ctx,getString(R.string.load));
        for(int i = 0; i < productList.size(); i++) {
          if(productList.get(i).isIsLimit() && productList.size() == 1) {
              Intent intent = new Intent(ctx,ProducetDetailsActivity.class);
              intent.putExtra("productId",productList.get(i).getProductId());
              intent.putExtra("ShopId",orderData.getShopId());
              ctx.startActivity(intent);
          }else {
              if(productList.get(i).isIsLimit()) {
                  mAddCartNumber ++;
                  return;
              }else {
                  addCart(productList.get(i),productList.size());
              }
          }
        }
    }

    private void addCart(OrderBean.OrdersBean.ProductListBean productListBean, final int size) {

        Map<String, String> params = new HashMap<>();
        params.put("SkuId",productListBean.getSkuId());
        params.put("Count",productListBean.getBuyNum()+"");
        params.put("UserId",mUserId);

        OkHttpUtils
                .post()
                .params(params)
                .url(GlobalContent.GLOBSAL_ADD_CART)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        mAddCartNumber ++;
                        RootBean rootBean = gson.fromJson(response, RootBean.class);
                        if(rootBean.ErrCode.equals("0") ) {
                            if(size == mAddCartNumber) {
                                getNetShoppingCartNumber();
                            }
                        }else {
                            Toast.makeText(ctx, rootBean.ResponseMsg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * 获取购物车商品数量
     */
    private void getNetShoppingCartNumber() {
        OkHttpUtils
                .get()
                .url(GlobalContent.GLOBSAL_GETCSRT_LIST)
                .addParams("UserId", mUserId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        RootBean rootBean = new Gson().fromJson(response, RootBean.class);
                        Logger.e(  "获取购物车商品数量=="+response);
                        if (rootBean.ErrCode.equals("0")) {
                            CartBean cartBean = new Gson().fromJson(rootBean.Data, CartBean.class);
                            getCartNumber(cartBean);
                            dismissProgress();
                            mAddCartNumber = 0;
                            ctx.startActivity(new Intent(ctx, ShoppingCartActivity.class));
                        }
                    }
                });
    }

    /**
     * 计算购物车商品数量
     *
     * @param cartBean
     */
    private void getCartNumber(CartBean cartBean) {
        List<CartBean.CartItemsBean> cartItems = cartBean.getCartItems();
        int number = 0;
        for (int i = 0; i < cartItems.size(); i++) {
            for (int j = 0; j < cartItems.get(i).getCartItems().size(); j++) {
                number++;
            }
        }
        CacheUtils.putString(ctx, GlobalContent.CART_NUMBER, String.valueOf(number));
        sendCartNumberChangeBroadcast();
    }

    /**
     * 发送购物车商品数量改变的广播
     */
    private void sendCartNumberChangeBroadcast() {
        Intent intent = new Intent("com.BFMe.BFMBuyer.SHOPPING_CART_NUMBER");
        ctx.sendBroadcast(intent);
    }

    private void showNormalDialog(final OrderBean.OrdersBean orderData) {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(ctx);
        normalDialog.setMessage(getString(R.string.cancel_order_hint));
        normalDialog.setPositiveButton(getString(R.string.commit),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelOrder(orderData);
                    }
                });
        normalDialog.setNegativeButton(getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();
    }

    /**
     * 去支付 评论  确认收货
     *
     * @param orderData 订单信息
     */
    private void payAndComment(OrderBean.OrdersBean orderData) {
        if (orderData.getOrderStatus().equals("WaitPay")) {//待付款

            commintOrder(orderData);
            return;
        }
        if (orderData.getOrderStatus().equals("Finish")) {//交易完成
            //跳转到评价页面
            mOrderId = orderData.getId();
            Logger.e(  "订单ID" + mOrderId);
            getCommentInfo();
            return;

        }
        if (orderData.getOrderStatus().equals("WaitReceiving")) {//待收货
            //确认收货
            ConfirmGoods(orderData);
        }
    }

    /**
     * 获取评价需要的信息
     */
    private void getCommentInfo() {
        //跳转
        Intent intentComm = new Intent(ctx, CommentDetailActivity.class);
        intentComm.putExtra(GlobalContent.ORDER_ID, mOrderId);//订单ID
        ctx.startActivity(intentComm);

    }

    /**
     * 确认收货
     *
     * @param orderData
     */
    private void ConfirmGoods(final OrderBean.OrdersBean orderData) {
        HashMap<String, String> map = new HashMap<>();
        map.put("UserId", OperateStringUtlis.getUerIdBack(CacheUtils.getString(ctx, GlobalContent.USER)));
        map.put("OrderId", orderData.getId());
        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBAL_CONFIRM_ORDER)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(ctx, "网络异常，请稍后。。", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        RootBean rootBean = gson.fromJson(response, RootBean.class);
                        String responseMsg = rootBean.ResponseMsg;
                        if ("success".equals(responseMsg)) {
                            Toast.makeText(ctx, getString(R.string.operation_succeed), Toast.LENGTH_SHORT).show();
                            refresh();
                        }
                    }
                });
    }

    /**
     * 跳转到选择支付页面
     *
     * @param orderData
     */
    private void commintOrder(OrderBean.OrdersBean orderData) {
        //跳转到支付页面
        Intent intent = new Intent(ctx, ChoosePayActivity.class);
        intent.putExtra("orderId", orderData.getId());
        intent.putExtra("totalPrice", orderData.getOrderTotalAmount());
        intent.putExtra("HasHSCode", orderData.isHasHSCode());
        ctx.startActivity(intent);
        getActivity().overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
    }

    /**
     * 取消订单
     *
     * @param orderData
     */
    private void cancelOrder(final OrderBean.OrdersBean orderData) {
        HashMap<String, String> map = new HashMap<>();
        map.put("UserId", OperateStringUtlis.getUerIdBack(CacheUtils.getString(ctx, GlobalContent.USER)));
        map.put("OrderId", orderData.getId());
        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBAL_LOSE_ORDER)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(ctx, "网络异常，请稍后。。", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onResponse(String response) {
                        RootBean rootBean = gson.fromJson(response, RootBean.class);
                        String responseMsg = rootBean.ResponseMsg;
                        if ("success".equals(responseMsg)) {
                            Toast.makeText(ctx, getString(R.string.operation_succeed), Toast.LENGTH_SHORT).show();
                            refresh();
                        }
                    }
                });
    }

    public void refresh() {
        //加载第一页
        pageno = 1;
        //更改状态　为刷新状态
        state = STATE_REFREH;
        getNetOrderData();
    }

    /**
     * 联网获取订单数据
     */
    public void getNetOrderData() {

        showProgress(ctx,getString(R.string.load));
        Map<String, String> params = new HashMap<>();
        params.put("userId", OperateStringUtlis.getUerIdBack(mUserId));
        params.put("pageSize", pagesize + "");
        params.put("pageNo", pageno + "");
        params.put("orderStatus", getPosition() + "");

        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBSL_GET_ORDER)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                        dismissProgress();
                    }

                    @Override
                    public void onResponse(String response) {
                        dismissProgress();
                        Logger.e("订单数据==" + response);
                        RootBean rootBean = gson.fromJson(response, RootBean.class);
                        if ("0".equals(rootBean.ErrCode)) {
                            String data = rootBean.Data;
                            OrderBean orderBean = gson.fromJson(data, OrderBean.class);
                            ordersList = orderBean.getOrders();
                            setadapter();
                            if (state != STATE_MORE) {
                                if (ordersList == null || ordersList.size() <= 0) {
                                    view_empty_hint.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                    }
                });
    }

    public abstract int getPosition();

    private void setadapter() {

        switch (state) {
            case STATE_NORMAL:

                orderAdapter = new OrderAdapter(ctx, ordersList, getPosition());
                lvOrderList.setAdapter(orderAdapter);

                break;

            case STATE_REFREH:

                if (orderAdapter != null) {
                    orderAdapter.cleanData();
                    orderAdapter.addData(ordersList);
                    orderAdapter.notifyDataSetChanged();
                    Toast.makeText(ctx, getString(R.string.refresh_succeed), Toast.LENGTH_SHORT).show();
                } else {
                    orderAdapter = new OrderAdapter(ctx, ordersList, getPosition());
                    lvOrderList.setAdapter(orderAdapter);
                }
                lvOrderList.onRefreshComplete();
                break;

            case STATE_MORE:
                if (orderAdapter != null) {
                    orderAdapter.addData(orderAdapter.getCount(), ordersList);
                    Toast.makeText(ctx, getString(R.string.more_succeed), Toast.LENGTH_SHORT).show();
                } else {
                    orderAdapter = new OrderAdapter(ctx, ordersList, getPosition());
                    lvOrderList.setAdapter(orderAdapter);
                }
                lvOrderList.onRefreshComplete();
                break;
        }
        setListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OrderFragmentFactory.hm.clear();
    }

    public void showProgress(Context context, String hint) {
        if(mDialog == null) {
            mDialog = new Dialog(context, R.style.Dialog);
        }
        View view =View.inflate(context, R.layout.progressbar, null);
        TextView textView = (TextView) view.findViewById(R.id.pb_text);
        textView.setText(hint);
        mDialog.setContentView(view);
        mDialog.show();
        mDialog.setCancelable(false);
    }

    public void dismissProgress() {

        if ( null  != mDialog && mDialog.isShowing() ) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
