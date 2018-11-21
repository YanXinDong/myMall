package com.BFMe.BFMBuyer.commodity.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.commodity.activity.ProducetDetailsActivity;
import com.BFMe.BFMBuyer.javaBean.LimtBuyDataBean;
import com.BFMe.BFMBuyer.shop.activity.ShopHomeActivity;
import com.BFMe.BFMBuyer.utils.LimitTimeUtils;
import com.BFMe.BFMBuyer.utils.Utils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.iwgang.countdownview.CountdownView;
import cn.iwgang.countdownview.DynamicConfig;


/**
 * Description:  限时购商品列表
 * Copyright  : Copyright (c) 2016
 * Company    : 白富美(北京)网络科技有限公司
 * Author     : 王  可
 * Date       : 2016/7/22 15:16
 */
public class LimitBuyListAdapter extends BaseAdapter {
    private List<LimtBuyDataBean.ProductsBean> mProductsList = new ArrayList<>();
    private Activity ctx;
    private ArrayList<Long> times = new ArrayList<>();
    private List<String> flag = new ArrayList<>();


    public LimitBuyListAdapter(List<LimtBuyDataBean.ProductsBean> productsList, Activity ctx) {
        if(productsList != null && productsList.size() > 0) {
            mProductsList.clear();
            mProductsList.addAll(productsList);
        }
        initData();
        this.ctx = ctx;
    }

    /**
     * 初始化数据
     */
    private void initData() {

        times.clear();//防止重复数据
        for (int i = 0; i < mProductsList.size(); i++) {
            String time = LimitTimeUtils.getTime(mProductsList.get(i).getStartTime(), mProductsList.get(i).getEndTime());
            flag.add(time.substring(0, 1));
            String s2 = time.substring(1);
            times.add(Long.parseLong(s2));
        }
    }

    @Override
    public int getCount() {
        return mProductsList.size();
    }

    @Override
    public LimtBuyDataBean.ProductsBean getItem(int position) {
        return mProductsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_limit_buy,parent,false);

            viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.tv_hint = (TextView) convertView.findViewById(R.id.tv_hint);
            viewHolder.tv_time_hint = (TextView) convertView.findViewById(R.id.tv_time_hint);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_new_price = (TextView) convertView.findViewById(R.id.tv_new_price);
            viewHolder.tv_old_price = (TextView) convertView.findViewById(R.id.tv_old_price);
            viewHolder.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
            viewHolder.countdownView = (CountdownView) convertView.findViewById(R.id.countdownView);
            viewHolder.btn_go = (Button) convertView.findViewById(R.id.btn_go);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final LimtBuyDataBean.ProductsBean  item = getItem(position);
        String str = flag.get(position);
        setDaoJiShiUI(viewHolder, str, item,parent);
        viewHolder.countdownView.start(times.get(position));

        if (item.getImagePath().size() > 0) {
            Glide
                    .with(parent.getContext())
                    .load(item.getImagePath().get(0))
                    .placeholder(R.drawable.zhanwei1)
                    .error(R.drawable.zhanwei1)
                    .into(viewHolder.iv_icon);
        }
        viewHolder.tv_name.setText(item.getProductName());
        viewHolder.tv_new_price.setText(parent.getContext().getResources().getString(R.string.money_type) + Utils.doubleSave2(item.getPrice()));
        //设置删除线
        viewHolder.tv_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        viewHolder.tv_old_price.setText(parent.getContext().getResources().getString(R.string.money_type) + Utils.doubleSave2(item.getRecentMonthPrice()));

        viewHolder.btn_go.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    if(flag.get(position).equals("0") || item.getLimitStock() <= 0) {
                        intent = new Intent(ctx, ShopHomeActivity.class);
                        intent.putExtra("shopId", item.getShopId());
                        ctx.startActivity(intent);
                        ctx.overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
                    }else {
                        String productId = item.getProductId() + "";
                        intent = new Intent(ctx, ProducetDetailsActivity.class);
                        intent.putExtra("productId", productId);   //无法显示图片
                        intent.putExtra("ShopId", item.getShopId() + "");
                        intent.putExtra("Flag", true);
                        ctx.startActivity(intent);
                        ctx.overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
                    }

                }
            });

        return convertView;
    }

    private void setDaoJiShiUI(ViewHolder viewHolder, String str, final LimtBuyDataBean.ProductsBean item, ViewGroup parent) {

        if(str.equals("0")) {//已结束
            viewHolder.countdownView.setVisibility(View.GONE);
            viewHolder.tv_hint.setVisibility(View.GONE);
            viewHolder.tv_number.setVisibility(View.GONE);
            viewHolder.tv_time_hint.setText("已结束");
            viewHolder.tv_time_hint.setTextColor(ctx.getResources().getColor(R.color.color_black_333333));
            viewHolder.btn_go.setText("进店看看");

            viewHolder.btn_go.setBackgroundResource(R.drawable.shape_circle_range2);
            viewHolder.btn_go.setTextColor(ctx.getResources().getColor(R.color.colorZhu));
            viewHolder.btn_go.setEnabled(true);

        }else if (str.equals("1")) {//未开枪
            viewHolder.countdownView.setVisibility(View.VISIBLE);
            viewHolder.tv_hint.setVisibility(View.GONE);
            viewHolder.tv_number.setVisibility(View.VISIBLE);

            viewHolder.tv_time_hint.setText("距开抢");
            viewHolder.tv_time_hint.setTextColor(ctx.getResources().getColor(R.color.green_45AC59));
            DynamicConfig.Builder builder = new DynamicConfig.Builder();
            builder.setShowDay(true);
            builder.setShowHour(true);
            builder.setShowMinute(true);
            builder.setShowSecond(true);
            builder.setSuffixDay("天  ");
            builder.setSuffixHour(":");
            builder.setSuffixMinute(":");
            builder.setSuffixLRMargin(0);
            builder.setTimeTextColor(Color.parseColor("#45AC59"));
            builder.setSuffixTextColor(Color.parseColor("#45AC59"));
            builder.setSuffixTextSize(10);
            builder.setTimeTextSize(11);

            viewHolder.countdownView.dynamicShow(builder.build());

            viewHolder.btn_go.setText("还未开抢");
            viewHolder.btn_go.setBackgroundResource(R.drawable.shape_circle_range3);
            viewHolder.btn_go.setTextColor(ctx.getResources().getColor(R.color.gray_btn));
            viewHolder.btn_go.setEnabled(false);

            viewHolder.tv_number.setText("剩余" + item.getLimitStock() + "件");

        }else {
            viewHolder.countdownView.setVisibility(View.VISIBLE);
            viewHolder.tv_hint.setVisibility(View.GONE);
            viewHolder.tv_number.setVisibility(View.VISIBLE);

            viewHolder.tv_time_hint.setText("距结束");
            viewHolder.tv_time_hint.setTextColor(ctx.getResources().getColor(R.color.search_text_color));
            DynamicConfig.Builder builder = new DynamicConfig.Builder();
            builder.setShowDay(true);
            builder.setShowHour(true);
            builder.setShowMinute(true);
            builder.setShowSecond(true);
            builder.setSuffixDay("天  ");
            builder.setSuffixHour(":");
            builder.setSuffixMinute(":");
            builder.setSuffixLRMargin(0);
            builder.setTimeTextColor(Color.parseColor("#FFF55A5F"));
            builder.setSuffixTextColor(Color.parseColor("#FFF55A5F"));
            builder.setSuffixTextSize(10);
            builder.setTimeTextSize(11);

            viewHolder.countdownView.dynamicShow(builder.build());

            viewHolder.btn_go.setText("立即抢购");
            viewHolder.btn_go.setBackgroundResource(R.drawable.shape_circle_range2);
            viewHolder.btn_go.setTextColor(ctx.getResources().getColor(R.color.colorZhu));
            viewHolder.btn_go.setEnabled(true);
            viewHolder.tv_number.setText("剩余" + item.getLimitStock() + "件");
        }

        //已售罄的处理
        if (item.getLimitStock() <= 0) {
            viewHolder.countdownView.setVisibility(View.GONE);
            viewHolder.tv_hint.setVisibility(View.VISIBLE);
            viewHolder.tv_number.setVisibility(View.VISIBLE);
            viewHolder.tv_time_hint.setText("售罄");
            viewHolder.tv_time_hint.setTextColor(ctx.getResources().getColor(R.color.yellow_EDAD50));
            viewHolder.tv_number.setText("剩余" + item.getLimitStock() + "件");

            viewHolder.btn_go.setText("进店看看");

            viewHolder.btn_go.setBackgroundResource(R.drawable.shape_circle_range2);
            viewHolder.btn_go.setTextColor(ctx.getResources().getColor(R.color.colorZhu));
            viewHolder.btn_go.setEnabled(true);

        }

    }

    class ViewHolder {
        private ImageView iv_icon;
        private TextView tv_hint;
        private TextView tv_time_hint;
        private cn.iwgang.countdownview.CountdownView countdownView;
        private TextView tv_name;
        private TextView tv_new_price;
        private TextView tv_old_price;
        private TextView tv_number;
        private Button btn_go;

    }

    /**
     * 添加数据
     */
    public void addData(int position, List<LimtBuyDataBean.ProductsBean> productslist) {
        if (productslist != null && productslist.size() > 0) {
            mProductsList.addAll(position, productslist);
            initData();
            notifyDataSetChanged();
        }
    }

    public void addData(List<LimtBuyDataBean.ProductsBean> productslist) {
        addData(0, productslist);
    }

    public void cleanData() {
        mProductsList.clear();
        notifyDataSetChanged();
    }

}

