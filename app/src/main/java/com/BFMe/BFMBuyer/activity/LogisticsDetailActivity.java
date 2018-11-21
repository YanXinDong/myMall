package com.BFMe.BFMBuyer.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.javaBean.LogisticsDetailBean;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.utils.CacheUtils;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物流详情页面
 */
public class LogisticsDetailActivity extends IBaseActivity {

    private ImageView iv_logistics_icon;//商品icon

    private RadioGroup rg_logistics_way;

    private TextView tv_logistics_status;//物流状态
    private TextView tv_logistics_way;//物流方式
    private TextView tv_logistics_id;//物流编号

    private ListView lv_logistic_info;//物流信息

    private String mOrderId;
    private String mUserId;
    private ArrayList<LogisticsDetailBean> logisticsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_logistics_detail;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mUserId = CacheUtils.getString(this, GlobalContent.USER);
        mOrderId = getIntent().getStringExtra(GlobalContent.ORDER_ID);

        getNetLogisticsData();
        setListener();
    }

    private void setListener() {
        rg_logistics_way.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb1:
                        showData(logisticsList.get(0));
                        break;
                    case R.id.rb2:
                        showData(logisticsList.get(1));
                        break;
                }
            }
        });
    }

    /**
     * 联网获取物流信息
     */
    private void getNetLogisticsData() {
        Map<String, String> params = new HashMap<>();
        params.put("OrderId", mOrderId);
        params.put("UserId", mUserId);
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
                        Logger.e("物流信息" + response);
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (Integer.parseInt(rootBean.ErrCode) == 0) {
                            logisticsList = mGson.fromJson(rootBean.Data, new TypeToken<ArrayList<LogisticsDetailBean>>() {
                            }.getType());
                            showLogisticsData();
//                            LogisticsDetailBean logisticsDetailBean = mGson.fromJson(rootBean.Data, LogisticsDetailBean.class);
//                            showLogisticsInfo(logisticsDetailBean);
                        }
                    }
                });
    }

    private void showLogisticsInfo(LogisticsDetailBean logisticsDetailBean) {
        Glide
                .with(getApplicationContext())
                .load(logisticsDetailBean.getProductImgPath())
                .placeholder(R.drawable.zhanwei1)
                .into(iv_logistics_icon);

        tv_logistics_status.setText(String.format(getString(R.string.logistics_state),logisticsDetailBean.getExpressStatus()));
        tv_logistics_way.setText(String.format(getString(R.string.logistics_source),logisticsDetailBean.getExpressSource()));
        tv_logistics_id.setText(String.format(getString(R.string.express_num),logisticsDetailBean.getExpressNum()));

        lv_logistic_info.setAdapter(new MyAdapter(logisticsDetailBean.getExpressInfo()));
    }

    /**
     * 显示物流信息
     */
    private void showLogisticsData() {
        if (logisticsList != null && logisticsList.size() > 0) {

            if (logisticsList.size() == 2 ) {
                rg_logistics_way.setVisibility(View.VISIBLE);//显示选项卡
                for(int i = 0; i < 2; i++) {
                    RadioButton childAt = (RadioButton) rg_logistics_way.getChildAt(i);
                    switch (logisticsList.get(i).getSendMethod()){
                        case 2://香港物流
                            childAt.setText("香港物流");
                            break;
                        case 5://合捷保税仓
                            childAt.setText("合捷保税仓");
                            break;
                        default://第三方
                            childAt.setText("第三方物流");
                            break;

                    }
                }
            } else {
                rg_logistics_way.setVisibility(View.GONE);
            }

            showData(logisticsList.get(0));
        }
    }

    private void showData(LogisticsDetailBean logisticsDetailBean) {
        Glide
                .with(getApplicationContext())
                .load(logisticsDetailBean.getProductImgPath())
                .placeholder(R.drawable.zhanwei1)
                .into(iv_logistics_icon);

        tv_logistics_status.setText(String.format(getString(R.string.logistics_state),logisticsDetailBean.getExpressStatus()));
        tv_logistics_way.setText(String.format(getString(R.string.logistics_source),logisticsDetailBean.getExpressSource()));
        tv_logistics_id.setText(String.format(getString(R.string.express_num),logisticsDetailBean.getExpressNum()));

        lv_logistic_info.setAdapter(new MyAdapter(logisticsDetailBean.getExpressInfo()));
    }

    /**
     * 物流信息列表 adapter
     */
    class MyAdapter extends BaseAdapter {

        private List<LogisticsDetailBean.ExpressInfoBean> mExpressInfo = new ArrayList<>();

        public MyAdapter(List<LogisticsDetailBean.ExpressInfoBean> expressInfo) {

            if (expressInfo != null && expressInfo.size() > 0) {
                mExpressInfo.clear();
                mExpressInfo.addAll(expressInfo);
            }
        }

        @Override
        public int getCount() {
            return mExpressInfo.size();

        }

        @Override
        public Object getItem(int position) {
            return mExpressInfo.get(position);
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
                convertView = LayoutInflater.from(LogisticsDetailActivity.this).inflate(R.layout.item_logistics, parent, false);
                viewHodler.iv_logistics_dian = (ImageView) convertView.findViewById(R.id.iv_logistics_dian);
                viewHodler.tv_logistics_info = (TextView) convertView.findViewById(R.id.tv_logistics_info);
                viewHodler.tv_logistics_time = (TextView) convertView.findViewById(R.id.tv_logistics_time);

                convertView.setTag(viewHodler);
            } else {
                viewHodler = (ViewHodler) convertView.getTag();
            }

            if (position == 0) {
                viewHodler.iv_logistics_dian.setImageResource(R.drawable.dian_a);
                viewHodler.tv_logistics_info.setTextColor(getResources().getColor(R.color.colorZhu));

            } else {
                viewHodler.iv_logistics_dian.setImageResource(R.drawable.dian_b);
                viewHodler.tv_logistics_info.setTextColor(getResources().getColor(R.color.font_black));
            }
            viewHodler.tv_logistics_info.setText(mExpressInfo.get(position).getContent());

            String time = mExpressInfo.get(position).getTime();

            viewHodler.tv_logistics_time.setText(time);

            return convertView;
        }

        class ViewHodler {

            private ImageView iv_logistics_dian;//圆点
            private TextView tv_logistics_info;//物流信息
            private TextView tv_logistics_time;//物流时间
        }
    }

    /**
     * 初始化布局
     */
    private void initView() {

        setBackButtonVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.look_logistics));

        rg_logistics_way = (RadioGroup) findViewById(R.id.rg_logistics_way);
        iv_logistics_icon = (ImageView) findViewById(R.id.iv_logistics_icon);

        tv_logistics_status = (TextView) findViewById(R.id.tv_logistics_status);
        tv_logistics_way = (TextView) findViewById(R.id.tv_logistics_way);
        tv_logistics_id = (TextView) findViewById(R.id.tv_logistics_id);

        lv_logistic_info = (ListView) findViewById(R.id.lv_logistic_info);
    }
}
