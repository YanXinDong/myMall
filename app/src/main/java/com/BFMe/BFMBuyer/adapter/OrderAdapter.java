package com.BFMe.BFMBuyer.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.javaBean.OrderBean;
import com.BFMe.BFMBuyer.utils.TimeUtils;
import com.BFMe.BFMBuyer.utils.Utils;
import com.BFMe.BFMBuyer.view.MyListView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.iwgang.countdownview.CountdownView;

import static com.BFMe.BFMBuyer.R.id.cv_pay_time;
import static com.BFMe.BFMBuyer.R.id.rl_btn;

/**
 * Description:  订单列表的adapter
 * Copyright  : Copyright (c) 2016
 * Company    : 白富美(北京)网络科技有限公司
 * Author     : 王  可
 * Date       : 2016/8/24 16:09
 */
public class OrderAdapter extends BaseAdapter {

    private Context ctx;
    private List<OrderBean.OrdersBean> ordersList;
    private int loaction;
    private Gson gson;
    private ArrayList<Long> times = new ArrayList<>();

    public OrderAdapter(Context ctx, List<OrderBean.OrdersBean> ordersList, int loaction) {

        this.ctx = ctx;
        this.ordersList = ordersList;
        this.loaction = loaction;
        if (gson == null) {
            gson = new Gson();
        }

        initCountdown();
    }

    private void initCountdown() {
        times.clear();//防止重复数据
        for (int i = 0; i < ordersList.size(); i++) {
            if (ordersList.get(i).getOverDate() != null) {
                long time = TimeUtils.getTime(ordersList.get(i).getOverDate());
                times.add(time);
            } else {
                times.add(0L);
            }
        }
    }

    @Override
    public int getCount() {
        return ordersList.size();
    }

    @Override
    public OrderBean.OrdersBean getItem(int position) {
        return ordersList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(ctx, R.layout.list_item_order, null);
            holder = new ViewHolder();

            holder.tvOrdeShopName = (TextView) convertView.findViewById(R.id.tv_orde_name);
            holder.tvOrderStatue = (TextView) convertView.findViewById(R.id.tv_order_statue);

            holder.lv_order_list = (MyListView) convertView.findViewById(R.id.lv_order_list);
            holder.tvAllPrice = (TextView) convertView.findViewById(R.id.tv_all_price);
            holder.btnOrderQuxiao = (Button) convertView.findViewById(R.id.btn_order_quxiao);
            holder.btnrderayPay = (Button) convertView.findViewById(R.id.btn_order_pay);

            holder.rl_btn = (RelativeLayout) convertView.findViewById(rl_btn);
            holder.tv_flag1 = (TextView) convertView.findViewById(R.id.tv_flag1);
            holder.tv_flag = (TextView) convertView.findViewById(R.id.tv_flag);

            holder.cv_pay_time = (CountdownView) convertView.findViewById(cv_pay_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        OrderBean.OrdersBean orderData = getItem(position);

        holder.lv_order_list.setAdapter(new OrderCommodityAdapter(orderData));

        holder.tvOrdeShopName.setText(orderData.getShopName());//商品名称
        holder.tvOrderStatue.setText(orderData.getOrderStatusDesc());//订单状态
        holder.tvAllPrice.setText(parent.getContext().getResources().getString(R.string.money_type) + orderData.getOrderTotalAmount());//订单总价格

        if (times.get(position) > 0) {
            holder.cv_pay_time.start(times.get(position));
            holder.cv_pay_time.setVisibility(View.VISIBLE);
            holder.tv_flag1.setVisibility(View.VISIBLE);
            holder.tv_flag.setVisibility(View.VISIBLE);
        } else {
            holder.cv_pay_time.setVisibility(View.GONE);
            holder.tv_flag1.setVisibility(View.GONE);
            holder.tv_flag.setVisibility(View.GONE);
        }

        switch (loaction) {//订单状态 以及按钮显示状态
            case 0:
                if (orderData.getOrderStatus().equals("WaitPay")) {
                    allBtnVisible(holder, "WaitPay");
                } else if (orderData.getOrderStatus().equals("Finish")) {
                    if (orderData.getOrderCommentInfo().equals("0")) {
                        allBtnVisible(holder, "Finish");
                    } else {
                        oneBtnVisible(holder, "Finish");
                    }

                } else if (orderData.getOrderStatus().equals("Close")) {
                    goneBtn(holder.rl_btn);
                } else if (orderData.getOrderStatus().equals("WaitReceiving")) {

                    allBtnVisible(holder, "WaitReceiving");
                    isDelayTheGoods(holder, orderData);
                } else if (orderData.getOrderStatus().equals("WaitDelivery")) {
                    goneBtn(holder.rl_btn);
                }

                break;
            case 1://待付款
                allBtnVisible(holder, "WaitPay");
                break;
            case 2://待发货
                goneBtn(holder.rl_btn);
                break;
            case 3://待收货

                allBtnVisible(holder, "WaitReceiving");
                isDelayTheGoods(holder, orderData);
                break;
            case 5://待评价
                allBtnVisible(holder, "Finish");
                break;

            default:
                break;
        }

        //监听
        // 取消订单  or  延期收货  再次购买
        holder.btnOrderQuxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOrderItemClickListener.onCancelOrderClick(v, ordersList.get(position));
            }
        });

        //支付  or  评价  确认收货
        holder.btnrderayPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOrderItemClickListener.onpayAndCommentClick(v, ordersList.get(position));
            }
        });

        //进入订单详情
        holder.lv_order_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int itemPosition, long id) {
                onOrderItemClickListener.onOrderItemClick(view, ordersList.get(position));
            }
        });
        return convertView;
    }

    /**
     * 是否已经延期收货
     *
     * @param holder    viewHolder
     * @param orderData 订单数据
     */
    private void isDelayTheGoods(ViewHolder holder, OrderBean.OrdersBean orderData) {
        if (orderData.isLaterWaitReceiving()) {
            //已延期收货 按钮置灰不可点击
            holder.btnOrderQuxiao.setBackgroundResource(R.drawable.shape_circle_range3);
            holder.btnOrderQuxiao.setTextColor(ctx.getResources().getColor(R.color.gray_btn));
            holder.btnOrderQuxiao.setEnabled(false);
            holder.btnOrderQuxiao.setText(ctx.getString(R.string.delay_the_goods_ture));
        } else {
            holder.btnOrderQuxiao.setBackgroundResource(R.drawable.shape_circle_range2);
            holder.btnOrderQuxiao.setTextColor(ctx.getResources().getColor(R.color.colorZhu));
            holder.btnOrderQuxiao.setEnabled(true);
            holder.btnOrderQuxiao.setText(ctx.getString(R.string.delay_the_goods));
        }
    }

    /**
     * 隐藏按钮栏
     *
     * @param rl_btn 按钮栏
     */
    private void goneBtn(RelativeLayout rl_btn) {
        rl_btn.setVisibility(View.GONE);
    }

    /**
     * 单按钮显示
     *
     * @param holder viewHolder
     * @param status 订单状态
     */
    private void oneBtnVisible(ViewHolder holder, String status) {
        holder.rl_btn.setVisibility(View.VISIBLE);
        holder.cv_pay_time.setVisibility(View.GONE);
        holder.tv_flag1.setVisibility(View.GONE);
        holder.tv_flag.setVisibility(View.GONE);
        if (status.equals("Finish")) {
            holder.btnrderayPay.setVisibility(View.GONE);
            holder.btnOrderQuxiao.setVisibility(View.VISIBLE);
            holder.btnOrderQuxiao.setText(ctx.getString(R.string.again_buy));

            holder.btnOrderQuxiao.setBackgroundResource(R.drawable.shape_circle_range2);
            holder.btnOrderQuxiao.setTextColor(ctx.getResources().getColor(R.color.colorZhu));
        }
    }

    /**
     * 双按钮显示
     *
     * @param holder viewHolder
     * @param status 订单状态
     */
    private void allBtnVisible(ViewHolder holder
            , String status) {
        holder.rl_btn.setVisibility(View.VISIBLE);
        holder.btnOrderQuxiao.setVisibility(View.VISIBLE);
        holder.btnrderayPay.setVisibility(View.VISIBLE);

        if (status.equals("WaitPay")) {
            holder.cv_pay_time.setVisibility(View.VISIBLE);
            holder.tv_flag1.setVisibility(View.VISIBLE);
            holder.tv_flag.setVisibility(View.VISIBLE);

            holder.btnOrderQuxiao.setBackgroundResource(R.drawable.shape_circle_range3);
            holder.btnOrderQuxiao.setTextColor(ctx.getResources().getColor(R.color.gray_btn));

            holder.btnrderayPay.setBackgroundResource(R.drawable.shape_circle_range1);
            holder.btnrderayPay.setTextColor(ctx.getResources().getColor(R.color.white));

            holder.btnOrderQuxiao.setText(ctx.getString(R.string.cancel_order));
            holder.btnrderayPay.setText(ctx.getString(R.string.go_pay));
        } else if (status.equals("Finish")) {
            holder.cv_pay_time.setVisibility(View.GONE);
            holder.tv_flag1.setVisibility(View.GONE);
            holder.tv_flag.setVisibility(View.GONE);

            holder.btnOrderQuxiao.setBackgroundResource(R.drawable.shape_circle_range2);
            holder.btnOrderQuxiao.setTextColor(ctx.getResources().getColor(R.color.colorZhu));

            holder.btnrderayPay.setBackgroundResource(R.drawable.shape_circle_range2);
            holder.btnrderayPay.setTextColor(ctx.getResources().getColor(R.color.colorZhu));

            holder.btnOrderQuxiao.setText(ctx.getString(R.string.again_buy));
            holder.btnrderayPay.setText(ctx.getString(R.string.go_comment));

        } else if (status.equals("WaitReceiving")) {
            holder.cv_pay_time.setVisibility(View.GONE);
            holder.tv_flag1.setVisibility(View.GONE);
            holder.tv_flag.setVisibility(View.GONE);

            holder.btnOrderQuxiao.setBackgroundResource(R.drawable.shape_circle_range2);
            holder.btnOrderQuxiao.setTextColor(ctx.getResources().getColor(R.color.colorZhu));

            holder.btnrderayPay.setBackgroundResource(R.drawable.shape_circle_range2);
            holder.btnrderayPay.setTextColor(ctx.getResources().getColor(R.color.colorZhu));

            holder.btnOrderQuxiao.setText(ctx.getString(R.string.delay_the_goods));
            holder.btnrderayPay.setText(ctx.getString(R.string.affirm_receiving));
        } else {
            holder.rl_btn.setVisibility(View.GONE);
        }
    }

    /**
     * 设置订单item的点击事件监听
     */
    public interface OnOrderItemClickListener {

        void onOrderItemClick(View view, OrderBean.OrdersBean orderData);//订单详情

        void onCancelOrderClick(View view, OrderBean.OrdersBean orderData);//取消订单按钮

        void onpayAndCommentClick(View view, OrderBean.OrdersBean orderData);//支付 确认收货 评论 按钮
    }

    private OnOrderItemClickListener onOrderItemClickListener;

    public void setOnCheckBoxClickListener(OnOrderItemClickListener onOrderItemClickListener) {
        this.onOrderItemClickListener = onOrderItemClickListener;
    }

    /**
     * 清除数据
     */
    public void cleanData() {
        ordersList.clear();
        notifyDataSetChanged();
    }

    /**
     * 添加数据
     *
     * @param datas
     */
    public void addData(List<OrderBean.OrdersBean> datas) {

        addData(0, datas);
    }

    public void addData(int position, List<OrderBean.OrdersBean> datas) {
        if (datas != null && datas.size() > 0) {
            ordersList.addAll(position, datas);
            initCountdown();
            notifyDataSetChanged();
        }
    }


    private static class ViewHolder {
        private TextView tvOrdeShopName; //订单的店铺名称
        private TextView tvOrderStatue;  //订单状态
        private MyListView lv_order_list;
        private TextView tvAllPrice;//总价格
        private Button btnOrderQuxiao;//取消订单
        private Button btnrderayPay;//去支付

        private RelativeLayout rl_btn;//按钮栏
        private TextView tv_flag1;
        private TextView tv_flag;
        private CountdownView cv_pay_time;//支付倒计时
    }

    /**
     * 订单列表内商品列表adapter
     */
    private class OrderCommodityAdapter extends BaseAdapter {

        private List<OrderBean.OrdersBean.ProductListBean> mProductList = new ArrayList<>();

        private OrderCommodityAdapter(OrderBean.OrdersBean orderData) {
            List<OrderBean.OrdersBean.ProductListBean> productList = orderData.getProductList();

            if (productList != null && productList.size() > 0) {
                mProductList.clear();
                mProductList.addAll(productList);
            }
        }

        @Override
        public int getCount() {
            return mProductList.size();
        }

        @Override
        public Object getItem(int position) {
            return mProductList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_commodity, parent, false);
                viewHolder = new ViewHolder();

                viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                viewHolder.tv_commodity_name = (TextView) convertView.findViewById(R.id.tv_commodity_name);
                viewHolder.tv_sale_price = (TextView) convertView.findViewById(R.id.tv_sale_price);
                viewHolder.tv_market_price = (TextView) convertView.findViewById(R.id.tv_market_price);
                viewHolder.tv_number = (TextView) convertView.findViewById(R.id.tv_number);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            OrderBean.OrdersBean.ProductListBean itemInfo = (OrderBean.OrdersBean.ProductListBean) getItem(position);

            Glide
                    .with(ctx)
                    .load(itemInfo.getImage())
                    .placeholder(R.drawable.zhanwei1)
                    .error(R.drawable.zhanwei1)
                    .into(viewHolder.iv_icon);
            if (itemInfo.isHSCode()) {//这个字段标示此商品是南沙保税仓发货，那么在商品名称后面就得给个”保税仓”这样的文字提示。
                viewHolder.tv_commodity_name.setText(itemInfo.getProductName()+"(保税仓)");
            } else {
                viewHolder.tv_commodity_name.setText(itemInfo.getProductName());
            }
            viewHolder.tv_sale_price.setText(parent.getContext().getResources().getString(R.string.money_type) + Utils.doubleSave2(itemInfo.getPrice()));
            viewHolder.tv_market_price.setText(parent.getContext().getResources().getString(R.string.money_type) + Utils.doubleSave2(itemInfo.getCostPrice()));
            viewHolder.tv_market_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            viewHolder.tv_number.setText("x" + itemInfo.getBuyNum());
            return convertView;
        }

        class ViewHolder {
            ImageView iv_icon;
            TextView tv_commodity_name;
            TextView tv_sale_price;
            TextView tv_market_price;
            TextView tv_number;

        }
    }
}
