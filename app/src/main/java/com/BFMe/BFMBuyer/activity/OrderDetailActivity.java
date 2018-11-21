package com.BFMe.BFMBuyer.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.commodity.activity.ProducetDetailsActivity;
import com.BFMe.BFMBuyer.javaBean.LogisticsDetailBean;
import com.BFMe.BFMBuyer.javaBean.OrderDetailBean;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.shop.activity.ShopHomeActivity;
import com.BFMe.BFMBuyer.utils.CacheUtils;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.Utils;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.BFMe.BFMBuyer.view.MyListView;
import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.session.activity.P2PMessageActivity;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.BFMe.BFMBuyer.R.id.tv_order_new_price;
import static com.BFMe.BFMBuyer.R.id.tv_order_old_price;

/**
 * 订单详情页面
 */
public class OrderDetailActivity extends IBaseActivity implements View.OnClickListener {

    //物流信息图片
    private ImageView iv_submit_order, iv_out_storehouse_order, iv_overseas_storehouse, iv_wait_recicve_goods, iv_finish;

    //订单状态
    private LinearLayout ll_order_info;
    //等待买家付款
    private TextView tv_order_waitepay;
    //物流信息跟踪
    private RelativeLayout ll_logistics_layout;
    private ImageView iv_arrow;
    private TextView tv_logistics_name;
    private TextView tv_logistics_time;
    private TextView tv_logistics_hint;
    private TextView tv_logistics_hj_hint;

    //收货人
    private TextView tv_order_receiving;
    //手机号
    private TextView tv_order_phone;
    //收货地址
    private TextView tv_order_address;
    //店铺信息栏
    private TextView tv_order_shopname;
    //备注
    private TextView tv_order_remarks;
    //商品总价
    private TextView tv_total;
    //运费
    private TextView tv_order_freight;
    //关税
    private TextView tv_order_tax;
    //积分抵扣
    private TextView tv_integral_deduction;
    //配送方式
    private TextView tv_order_distribution;
    //本店合计
    private TextView tv_total_num;
    //订单id
    private TextView tv_order_id;
    //创建时间
    private TextView tv_order_creat_time;
    //成交时间
    private TextView tv_finish_time;
    //支付方式
    private TextView tv_pay_way;

    private MyListView lv_order_info;//商品info
    private Button bt_right, bt_middle, bt_left;//底部按钮

    private String mOrderId;//订单id
    private String mOrderStatus;//订单状态
    private OrderDetailBean mOrderDetailBean;
    private String orderCommentInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_order_detail;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mOrderId = getIntent().getStringExtra(GlobalContent.ORDER_ID);//订单ID
        mOrderStatus = getIntent().getStringExtra(GlobalContent.ORDER_STATUS);//订单状态
        mUserId = CacheUtils.getString(this, GlobalContent.USER);
        orderCommentInfo = getIntent().getStringExtra("ORDER_COMMENT_INFO");
        //设置显示状态
        setShowStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        OrderItemId();
    }

    /**
     * 获取订单详情
     */
    private void OrderItemId() {
        showProgress();
        Map<String, String> params = new HashMap<>();
        params.put("UserId", mUserId);
        params.put("OrderId", mOrderId);
        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBAL_ORDER_DEATIL)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        dismissProgress();
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        Logger.e("订单详情" + rootBean.Data);
                        if (Integer.parseInt(rootBean.ErrCode) == 0) {
                            mOrderDetailBean = mGson.fromJson(rootBean.Data, OrderDetailBean.class);
                            //显示数据
                            showOrderDetail();
                        } else {
                            Toast.makeText(OrderDetailActivity.this, rootBean.ResponseMsg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * 显示订单详情数据
     */
    private void showOrderDetail() {
        //订单状态
        List<OrderDetailBean.ExpressInfoData> expressInfo = mOrderDetailBean.getExpressInfo();
        for (int i = 0; i < expressInfo.size(); i++) {

            if (expressInfo.get(i).isIsReady()) {
                switch (i) {
                    case 0:
                        break;
                    case 1:
                        iv_out_storehouse_order.setImageResource(R.drawable.tab_select_icon_b);
                        break;
                    case 2:
                        iv_overseas_storehouse.setImageResource(R.drawable.tab_select_icon_c);
                        break;
                    case 3:
                        iv_wait_recicve_goods.setImageResource(R.drawable.tab_select_icon_d);
                        break;
                    case 4:
                        iv_finish.setImageResource(R.drawable.tab_select_icon_e);
                        break;
                }
            }
        }

        //收货人
        tv_order_receiving.setText(String.format(getString(R.string.consignee), mOrderDetailBean.getShipTo()));
        //手机号
        tv_order_phone.setText(mOrderDetailBean.getCellPhone());
        //收货地址
        tv_order_address.setText(String.format(getString(R.string.shipping_address), mOrderDetailBean.getRegionFullName() + mOrderDetailBean.getAddress()));

        tv_order_shopname.setText(mOrderDetailBean.getShopName());//店铺名称

        //商品简介
        lv_order_info.setAdapter(new MyAdapter(mOrderDetailBean));

        //备注
        if (!TextUtils.isEmpty(mOrderDetailBean.getUserRemark())) {
            tv_order_remarks.setText(mOrderDetailBean.getUserRemark());
        }

        //商品总价
        tv_total.setText(getString(R.string.money_type) + Utils.doubleSave2(mOrderDetailBean.getProductTotalAmount()));
        //运费
        tv_order_freight.setText(getString(R.string.money_type) + Utils.doubleSave2(mOrderDetailBean.getFreight()));
        //关税
        tv_order_tax.setText(getString(R.string.money_type) + Utils.doubleSave2(mOrderDetailBean.getTax()));
        //积分抵扣
        tv_integral_deduction.setText(getString(R.string.money_type) + Utils.doubleSave2(mOrderDetailBean.getIntegralDiscount()));
        //配送方式
        tv_order_distribution.setText(mOrderDetailBean.getSendMethodName());
        //实付合计
        tv_total_num.setText(getString(R.string.money_type) + Utils.doubleSave2(mOrderDetailBean.getOrderTotalAmount()));

        //订单编号
        tv_order_id.setText(String.format(getString(R.string.order_id_$),mOrderDetailBean.getId()));
        //创建时间
        tv_order_creat_time.setText(String.format(getString(R.string.creat_time_$),mOrderDetailBean.getStrOrderDate()));
        //成交时间
        if (!TextUtils.isEmpty(mOrderDetailBean.getStrPayDate())) {
            tv_finish_time.setText(String.format(getString(R.string.finish_time_$),mOrderDetailBean.getStrPayDate()));
            tv_pay_way.setText(String.format(getString(R.string.pay_way_$),mOrderDetailBean.getPaymentTypeName()));
        } else {
            tv_finish_time.setVisibility(View.GONE);
            tv_pay_way.setVisibility(View.GONE);
        }


    }

    /**
     * 获取物流信息
     */
    private void getNetLogisticsData() {

        Map<String, String> params = new HashMap<>();
        params.put("OrderId", mOrderId);
        params.put("UserId", mUserId);
        Logger.e("物流信息params=="+params.toString());
        OkHttpUtils
                .get()
                .url(GlobalContent.GLOBAL_GET_EXPRESS)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("物流信息==" + response);
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (Integer.parseInt(rootBean.ErrCode) == 0) {
                            ArrayList<LogisticsDetailBean> logisticsList = mGson.fromJson(rootBean.Data, new TypeToken<ArrayList<LogisticsDetailBean>>() {
                            }.getType());
                            showLogisticsInfo(logisticsList);
//                            LogisticsDetailBean logisticsDetailBean = mGson.fromJson(rootBean.Data, LogisticsDetailBean.class);
//                            showLogisticsInfo(logisticsDetailBean);
                        }
                    }
                });
    }

    private void showLogisticsInfo(LogisticsDetailBean logisticsDetailBean) {
        List<LogisticsDetailBean.ExpressInfoBean> expressInfo = logisticsDetailBean.getExpressInfo();
        if (expressInfo != null && expressInfo.size() > 0) {
            tv_logistics_name.setText(expressInfo.get(0).getContent());

            String time = expressInfo.get(0).getTime();
            tv_logistics_time.setText(time);

        } else {
            tv_logistics_hint.setVisibility(View.VISIBLE);
            tv_logistics_hint.setText("暂无物流信息");
            ll_logistics_layout.setEnabled(false);
        }
    }

    private void showLogisticsInfo(ArrayList<LogisticsDetailBean> logisticsList) {
        if (logisticsList != null && logisticsList.size() > 0) {
            if (logisticsList.size() > 1) {//如果返回多物流信息，证明此订单包含南沙保税仓商品，给出提示。
                tv_logistics_hj_hint.setVisibility(View.VISIBLE);
            } else {
                tv_logistics_hj_hint.setVisibility(View.GONE);
            }
            LogisticsDetailBean logisticsDetailBean = logisticsList.get(0);
            List<LogisticsDetailBean.ExpressInfoBean> expressInfo = logisticsDetailBean.getExpressInfo();
            if (expressInfo != null && expressInfo.size() > 0) {
                tv_logistics_name.setText(expressInfo.get(0).getContent());

                String time = expressInfo.get(0).getTime();
                tv_logistics_time.setText(time);

            } else {
                tv_logistics_hint.setVisibility(View.VISIBLE);
                tv_logistics_hint.setText(getString(R.string.logistics_empt));
                ll_logistics_layout.setEnabled(false);
            }

        }

    }

    /**
     * 订单详情商品简介 列表
     */
    class MyAdapter extends BaseAdapter {

        private OrderDetailBean mOrderDetailBean;

        private MyAdapter(OrderDetailBean orderDetailBean) {
            mOrderDetailBean = orderDetailBean;
        }

        @Override
        public int getCount() {
            return mOrderDetailBean.getOrderItemInfo().size();
        }

        @Override
        public Object getItem(int position) {
            return mOrderDetailBean.getOrderItemInfo().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHodler viewHodler = null;
            if (convertView == null) {
                viewHodler = new ViewHodler();
                convertView = LayoutInflater.from(OrderDetailActivity.this).inflate(R.layout.item_order_detail, parent, false);

                viewHodler.rl_order_introduction = (RelativeLayout) convertView.findViewById(R.id.rl_order_introduction);
                viewHodler.tv_order_commodity_name = (TextView) convertView.findViewById(R.id.tv_order_commodity_name);

                viewHodler.tv_order_new_price = (TextView) convertView.findViewById(tv_order_new_price);
                viewHodler.tv_order_old_price = (TextView) convertView.findViewById(tv_order_old_price);
                viewHodler.tv_order_num = (TextView) convertView.findViewById(R.id.tv_order_num);
                viewHodler.tv_order_size_color = (TextView) convertView.findViewById(R.id.tv_order_size_color);

                viewHodler.iv_order_icon = (ImageView) convertView.findViewById(R.id.iv_order_icon);
                viewHodler.bt_refund = (Button) convertView.findViewById(R.id.bt_refund);
                convertView.setTag(viewHodler);
            } else {
                viewHodler = (ViewHodler) convertView.getTag();
            }

            final OrderDetailBean.OrderItemInfoBean item = (OrderDetailBean.OrderItemInfoBean) getItem(position);

            viewHodler.tv_order_commodity_name.setText(item.getProductName());//商品名称

            viewHodler.tv_order_new_price.setText(getString(R.string.money_type) + Utils.doubleSave2(item.getSalePrice()));
            viewHodler.tv_order_old_price.setText(getString(R.string.money_type) + Utils.doubleSave2(item.getCostPrice()));
            viewHodler.tv_order_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            viewHodler.tv_order_num.setText("x" + item.getQuantity());//购买数量
            viewHodler.tv_order_size_color.setText(item.getColor() + "  "
                    + item.getSize());
            //商品缩略图
            Glide.with(OrderDetailActivity.this)
                    .load(item.getThumbnailsUrl())
                    .placeholder(R.drawable.zhanwei1)
                    .centerCrop()
                    .into(viewHodler.iv_order_icon);

            if ("Close".equals(mOrderStatus) || "WaitPay".equals(mOrderStatus)) {//交易关闭 待付款
                viewHodler.bt_refund.setVisibility(View.GONE);
            }

            if (item.isHasRefundApply()) {//已申请退款
                viewHodler.bt_refund.setText(getString(R.string.already_refund));
                viewHodler.bt_refund.setEnabled(false);
                viewHodler.bt_refund.setBackgroundResource(R.drawable.shape_circle_range3);
                viewHodler.bt_refund.setTextColor(mContext.getResources().getColor(R.color.gray_btn));
            } else {
                viewHodler.bt_refund.setText(getString(R.string.apply_for_after_sales));
                viewHodler.bt_refund.setEnabled(true);
                viewHodler.bt_refund.setBackgroundResource(R.drawable.shape_circle_range2);
                viewHodler.bt_refund.setTextColor(mContext.getResources().getColor(R.color.colorZhu));
            }

            //商品简介点击事件监听
            viewHodler.rl_order_introduction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(OrderDetailActivity.this, ProducetDetailsActivity.class);
                    intent.putExtra("productId", item.getProductId());//商品id
                    intent.putExtra("ShopId", mOrderDetailBean.getShopId());//店铺id
                    startActivity(intent);
                    startAnim();
                }
            });
            //退款按钮的监听
            viewHodler.bt_refund.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(OrderDetailActivity.this, ApplyFordrawbackActivity.class);
                    intent.putExtra(GlobalContent.ORDER_ID, item.getOrderId());//订单id
                    intent.putExtra(GlobalContent.ORDER_ITEM_ID, item.getId());//子订单id
                    startActivity(intent);
                    startAnim();
                }
            });
            return convertView;
        }

        class ViewHodler {
            private RelativeLayout rl_order_introduction; //商品详情栏

            private ImageView iv_order_icon;//商品缩略图
            private TextView tv_order_commodity_name;//商品简介
            private TextView tv_order_new_price;
            private TextView tv_order_old_price;

            private TextView tv_order_num;//商品数量
            private TextView tv_order_size_color;//商品规格
            private Button bt_refund;//退款按钮
        }
    }

    /**
     * 设置显示状态
     */
    private void setShowStatus() {
        Logger.e("mOrderStatus==" + mOrderStatus);
        if ("WaitPay".equals(mOrderStatus)) {//待支付
            ll_order_info.setVisibility(View.GONE);
            ll_logistics_layout.setVisibility(View.GONE);
            tv_order_waitepay.setVisibility(View.VISIBLE);
            bt_right.setText(getString(R.string.payment));
            bt_middle.setText(getString(R.string.cancel_order));

        } else if ("WaitDelivery".equals(mOrderStatus)) {//待发货
            ll_order_info.setVisibility(View.VISIBLE);
            ll_logistics_layout.setVisibility(View.VISIBLE);
            tv_order_waitepay.setVisibility(View.GONE);
            bt_right.setVisibility(View.GONE);
            bt_middle.setVisibility(View.GONE);
            getNetLogisticsData();
        } else if ("WaitReceiving".equals(mOrderStatus)) {//待收货
            ll_order_info.setVisibility(View.VISIBLE);
            ll_logistics_layout.setVisibility(View.VISIBLE);
            tv_order_waitepay.setVisibility(View.GONE);
            iv_arrow.setVisibility(View.VISIBLE);
            bt_right.setText(getString(R.string.affirm_receiving));
            bt_middle.setVisibility(View.GONE);

            getNetLogisticsData();

        } else if ("Finish".equals(mOrderStatus)) {//交易完成
            ll_order_info.setVisibility(View.VISIBLE);
            ll_logistics_layout.setVisibility(View.VISIBLE);
            tv_order_waitepay.setVisibility(View.GONE);
            iv_arrow.setVisibility(View.VISIBLE);
            tv_title_right.setVisibility(View.VISIBLE);
            tv_title_right.setText(getString(R.string.complaint_one));
            Logger.e("orderCommentInfo===" + orderCommentInfo);
            if ("0".equals(orderCommentInfo)) {
                bt_right.setText(getString(R.string.immediate_evaluation));
                bt_middle.setVisibility(View.GONE);
            } else {
                bt_right.setVisibility(View.GONE);
                bt_middle.setVisibility(View.GONE);
            }
            getNetLogisticsData();

        } else if ("Close".equals(mOrderStatus)) {//交易关闭
            ll_order_info.setVisibility(View.GONE);
            ll_logistics_layout.setVisibility(View.GONE);
            tv_order_waitepay.setVisibility(View.GONE);
            bt_right.setVisibility(View.GONE);
            bt_middle.setVisibility(View.GONE);
        }

        bt_left.setText(getString(R.string.relation_seller));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_right:
                if ("WaitPay".equals(mOrderStatus)) {//待支付
                    //支付
                    commintOrder();
                } else if ("WaitReceiving".equals(mOrderStatus)) {//待收货
                    //确认收货
                    ConfirmGoods();
                } else if ("Finish".equals(mOrderStatus)) {//交易完成
                    //评论
                    //跳转到立即评价页面
                    Intent intentComm = new Intent(OrderDetailActivity.this, CommentDetailActivity.class);
                    if (mOrderDetailBean != null) {
                        intentComm.putExtra(GlobalContent.ORDER_ID, mOrderDetailBean.getId());//订单ID
                        startActivity(intentComm);
                        startAnim();
                    }
                }
                break;
            case R.id.bt_middle://取消订单
                if ("WaitPay".equals(mOrderStatus)) {//待支付

                    cancelOrder();

                }
                break;
            case R.id.bt_left://联系卖家
                if (mOrderDetailBean != null) {
                    P2PMessageActivity.IMFlag = true;
                    P2PMessageActivity.IMUserName = mOrderDetailBean.getShopName();
                    P2PMessageActivity.shopDetailImg =mOrderDetailBean.getOrderItemInfo().get(0).getThumbnailsUrl();
                    P2PMessageActivity.shopDetailDes = mOrderDetailBean.getOrderItemInfo().get(0).getProductName();
                    P2PMessageActivity.shopDetailPrice = mOrderDetailBean.getOrderItemInfo().get(0).getSalePrice()+"";
                    NimUIKit.startChatting(this, mOrderDetailBean.getSellerIMUser(), SessionTypeEnum.P2P, null, null);
                }
                break;
            case R.id.ll_logistics_layout://进入物流详情页
                //跳转到物流详情页面
                Intent intent = new Intent(OrderDetailActivity.this, LogisticsDetailActivity.class);
                if (mOrderDetailBean != null && !mOrderStatus.equals("WaitDelivery")) {
                    intent.putExtra(GlobalContent.ORDER_ID, mOrderId);
                    startActivity(intent);
                    startAnim();
                }

                break;

            case R.id.tv_title_right://投诉
                //跳转到投诉页面
                Intent complaint = new Intent(OrderDetailActivity.this, ComplaintActivity.class);
                complaint.putExtra(GlobalContent.ORDER_ID, mOrderId);
                startActivity(complaint);
                startAnim();
                break;

            case R.id.rl_shop_info://店铺信息栏
                Intent shopIntent = new Intent(OrderDetailActivity.this, ShopHomeActivity.class);
                shopIntent.putExtra("shopId", mOrderDetailBean.getShopId());
                startActivity(shopIntent);
                startAnim();
                break;
        }
    }

    /**
     * 确认收货
     */
    private void ConfirmGoods() {
        Map<String, String> params = new HashMap<>();
        params.put("UserId", mUserId);
        params.put("OrderId", mOrderId);
        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBAL_CONFIRM_ORDER)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                        showToast("网络错误，请稍后再试");
                    }

                    @Override
                    public void onResponse(String response) {
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        String responseMsg = rootBean.ResponseMsg;
                        if ("success".equals(responseMsg)) {
                            showToast(getString(R.string.affirm_receiving_succeed));
                            mOrderStatus = "Finish";
                            //设置显示状态
                            setShowStatus();
                            OrderItemId();
                        }
                    }
                });
    }

    /**
     * 跳转到选择支付页面
     */
    private void commintOrder() {
        //跳转到支付页面
        Intent intent = new Intent(OrderDetailActivity.this, ChoosePayActivity.class);
        intent.putExtra("orderId", mOrderId);
        intent.putExtra("totalPrice", Utils.doubleSave2(mOrderDetailBean.getOrderTotalAmount()));
        intent.putExtra("HasHSCode", mOrderDetailBean.isHasHSCode());
        startActivity(intent);
        startAnim();
    }

    /**
     * 取消订单
     */
    private void cancelOrder() {
        showProgress();
        Map<String, String> params = new HashMap<>();
        params.put("UserId", mUserId);
        params.put("OrderId", mOrderId);
        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBAL_LOSE_ORDER)
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
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        String responseMsg = rootBean.ResponseMsg;
                        if ("success".equals(responseMsg)) {
                            showToast(getString(R.string.order_cancel_one));
                            mOrderStatus = "Close";
                            //设置显示状态
                            setShowStatus();
                            OrderItemId();
                        }
                    }
                });
    }


    /**
     * 初始化布局
     */
    private void initView() {

        setBackButtonVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.order_detail));

        //物流信息
        iv_submit_order = (ImageView) findViewById(R.id.iv_submit_order);
        iv_out_storehouse_order = (ImageView) findViewById(R.id.iv_out_storehouse_order);
        iv_overseas_storehouse = (ImageView) findViewById(R.id.iv_overseas_storehouse);
        iv_wait_recicve_goods = (ImageView) findViewById(R.id.iv_wait_recicve_goods);
        iv_finish = (ImageView) findViewById(R.id.iv_finish);

        ll_logistics_layout = (RelativeLayout) findViewById(R.id.ll_logistics_layout);
        tv_logistics_name = (TextView) findViewById(R.id.tv_logistics_name);
        tv_logistics_time = (TextView) findViewById(R.id.tv_logistics_time);
        tv_logistics_hint = (TextView) findViewById(R.id.tv_logistics_hint);
        tv_logistics_hj_hint = (TextView) findViewById(R.id.tv_logistics_hj_hint);

        iv_arrow = (ImageView) findViewById(R.id.iv_arrow);

        ll_order_info = (LinearLayout) findViewById(R.id.ll_order_info);//订单状态
        tv_order_waitepay = (TextView) findViewById(R.id.tv_order_waitepay);//等待买家付款

        lv_order_info = (MyListView) findViewById(R.id.lv_order_info);//商品info

        tv_order_receiving = (TextView) findViewById(R.id.tv_order_receiving);//收货人
        tv_order_phone = (TextView) findViewById(R.id.tv_order_phone);//手机号
        tv_order_address = (TextView) findViewById(R.id.tv_order_address); //收货地址

        findViewById(R.id.rl_shop_info).setOnClickListener(this);
        tv_order_shopname = (TextView) findViewById(R.id.tv_order_shopname);
        tv_order_remarks = (TextView) findViewById(R.id.tv_order_remarks);//备注
        tv_total = (TextView) findViewById(R.id.tv_total);//商品总价
        tv_order_freight = (TextView) findViewById(R.id.tv_order_freight);//运费
        tv_order_tax = (TextView) findViewById(R.id.tv_order_tax);//关税
        tv_integral_deduction = (TextView) findViewById(R.id.tv_integral_deduction);//积分抵扣
        tv_order_distribution = (TextView) findViewById(R.id.tv_order_distribution);//配送方式
        tv_total_num = (TextView) findViewById(R.id.tv_total_num);//实付合计
        tv_order_id = (TextView) findViewById(R.id.tv_order_id);//订单id
        tv_order_creat_time = (TextView) findViewById(R.id.tv_order_creat_time);//创建时间
        tv_finish_time = (TextView) findViewById(R.id.tv_finish_time);//成交时间
        tv_pay_way = (TextView) findViewById(R.id.tv_pay_way);//支付方式

        bt_right = (Button) findViewById(R.id.bt_right);
        bt_middle = (Button) findViewById(R.id.bt_middle);
        bt_left = (Button) findViewById(R.id.bt_left);

        bt_right.setOnClickListener(this);
        bt_middle.setOnClickListener(this);
        bt_left.setOnClickListener(this);

        ll_logistics_layout.setOnClickListener(this);
        tv_title_right.setOnClickListener(this);

    }
}
