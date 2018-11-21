package com.BFMe.BFMBuyer.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.javaBean.RefundDetailsBean;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.utils.CacheUtils;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.UiUtils;
import com.BFMe.BFMBuyer.utils.Utils;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.session.activity.P2PMessageActivity;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

/**
 * 退款详情页
 */
public class RefundDetailsActivity extends IBaseActivity implements View.OnClickListener {

    private TextView productName;//商品名称
    private TextView saleType;//售后类型
    private TextView salenum;//数量
    private TextView returnReason;//原因
    private TextView refundMoney;//金额
    private TextView payFee;//手续费
    private TextView handler;//总计
    private TextView returnIntro;//说明
    private TextView replyBuyerStatus;//卖家意见
    private ImageView iv_product_refund;//产品缩略图

    private String mUserId;
    private String mRefundId;
    private Gson gson;

    private TextView tv_refund_status;
    private Button btn_contact_the_merchant;
    private TextView tv_time;
    private TextView tv_explain;

    private Button btn_add_express_information;
    private View include_express_info;
    private EditText et_company_name;
    private EditText et_company_number;
    private Button btn_finish;

    private String mSellerIMID;
    private String mShopName;
    private RefundDetailsBean refundDetailsBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();

    }

    @Override
    public int initLayout() {
        return R.layout.activity_refund_details;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        gson = new Gson();
        mUserId = CacheUtils.getString(this, GlobalContent.USER);
        mRefundId = getIntent().getStringExtra(GlobalContent.REFUND_ID);

        getDataFromNet();
    }

    private void getDataFromNet() {
        OkHttpUtils
                .get()
                .url(GlobalContent.GLABAL_GET_REFUND_DETAIL
                        + "?UserId=" + mUserId
                        + "&RefundId=" + mRefundId
                )
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("退款详情" + response);
                        RootBean rootBean = gson.fromJson(response, RootBean.class);
                        if (Integer.parseInt(rootBean.ErrCode) == 0) {
                            refundDetailsBean = gson.fromJson(rootBean.Data, RefundDetailsBean.class);
                            showRefundDetailsData(refundDetailsBean);
                        }
                    }
                });
    }

    /**
     * 显示退款详情数据
     *
     * @param detailsDatas
     */
    private void showRefundDetailsData(RefundDetailsBean detailsDatas) {
        mSellerIMID = detailsDatas.getSellerIMID();
        mShopName = detailsDatas.getShopName();

        setRefundStatus(detailsDatas);
        productName.setText(detailsDatas.getOrderItemInfo().getProductName());//商品名称
        saleType.setText(detailsDatas.getRefundModeDesc());//售后类型
        salenum.setText("*" + detailsDatas.getOrderItemInfo().getReturnQuantity());//退货数量
        returnReason.setText(detailsDatas.getReasonTypeDesc());//退款原因
        refundMoney.setText(Utils.doubleSave2(detailsDatas.getAmount()) + "元");//退款金额

        payFee.setText(Utils.doubleSave2(detailsDatas.getServiceFee()) + "元");//支付平台手续费

        if (detailsDatas.isBuyerBear()) {//是否买家承担服务费
            //买家承担手续费
            handler.setText(Utils.doubleSave2(detailsDatas.getAmount() - detailsDatas.getServiceFee()) + "元");//总计
        } else {
            //卖家承担手续费
            handler.setText(Utils.doubleSave2(detailsDatas.getAmount()) + "元");//总计
        }

        returnIntro.setText(detailsDatas.getReason());//退款说明
        Glide.with(getApplicationContext())
                .load(detailsDatas.getOrderItemInfo().getThumbnailsUrl())
                .placeholder(R.drawable.zhanwei1)
                .into(iv_product_refund);//订单商品缩略图
        replyBuyerStatus.setText(detailsDatas.getSellerRemark());//卖家意见;


    }

    private void setRefundStatus(RefundDetailsBean detailsDatas) {
        RelativeLayout.LayoutParams lpStatus = (RelativeLayout.LayoutParams) tv_refund_status.getLayoutParams();
        RelativeLayout.LayoutParams lpTime = (RelativeLayout.LayoutParams) tv_time.getLayoutParams();
        RelativeLayout.LayoutParams lpExplain = (RelativeLayout.LayoutParams) tv_explain.getLayoutParams();

        Drawable countDown = getResources().getDrawable(R.drawable.icon_count_down);
        Drawable sandGlass = getResources().getDrawable(R.drawable.icon_sand_glass);

        countDown.setBounds(0, 0, countDown.getMinimumWidth(), countDown.getMinimumHeight());
        sandGlass.setBounds(0, 0, countDown.getMinimumWidth(), countDown.getMinimumHeight());

//        detailsDatas.setRefundStatus("待买家寄货");

        switch (detailsDatas.getRefundStatus()) {
            case "待商家审核":
                btn_contact_the_merchant.setVisibility(View.VISIBLE);
                tv_time.setVisibility(View.VISIBLE);
                tv_explain.setVisibility(View.GONE);

                lpStatus.setMargins(UiUtils.dip2px(15), UiUtils.dip2px(15), UiUtils.dip2px(0), UiUtils.dip2px(10));
                lpTime.setMargins(UiUtils.dip2px(15), UiUtils.dip2px(0), UiUtils.dip2px(0), UiUtils.dip2px(15));

                tv_refund_status.setTextColor(getResources().getColor(R.color.black));
                tv_time.setTextColor(getResources().getColor(R.color.search_text_color));

                tv_time.setCompoundDrawables(sandGlass, null, null, null);

                tv_refund_status.setText(R.string.merchants_in_the_audit);
                tv_time.setText(detailsDatas.getHandleDate());
                break;
            case "待买家寄货":
                btn_contact_the_merchant.setVisibility(View.VISIBLE);
                tv_time.setVisibility(View.GONE);
                tv_explain.setVisibility(View.VISIBLE);
                btn_add_express_information.setVisibility(View.VISIBLE);

                lpStatus.setMargins(UiUtils.dip2px(15), UiUtils.dip2px(15), UiUtils.dip2px(0), UiUtils.dip2px(10));
                lpTime.setMargins(UiUtils.dip2px(15), UiUtils.dip2px(0), UiUtils.dip2px(0), UiUtils.dip2px(5));
                lpExplain.setMargins(UiUtils.dip2px(15), UiUtils.dip2px(0), UiUtils.dip2px(15), UiUtils.dip2px(15));

                tv_refund_status.setTextColor(getResources().getColor(R.color.black));

                tv_refund_status.setText(R.string.send_the_goods_back);
                tv_explain.setText(detailsDatas.getNote());

                break;
            case "待商家收货":
                btn_contact_the_merchant.setVisibility(View.VISIBLE);
                tv_time.setVisibility(View.GONE);
                tv_explain.setVisibility(View.VISIBLE);
                include_express_info.setVisibility(View.VISIBLE);
                btn_finish.setVisibility(View.GONE);

                lpStatus.setMargins(UiUtils.dip2px(15), UiUtils.dip2px(15), UiUtils.dip2px(0), UiUtils.dip2px(10));
                lpTime.setMargins(UiUtils.dip2px(15), UiUtils.dip2px(0), UiUtils.dip2px(0), UiUtils.dip2px(5));
                lpExplain.setMargins(UiUtils.dip2px(15), UiUtils.dip2px(0), UiUtils.dip2px(15), UiUtils.dip2px(15));

                tv_refund_status.setTextColor(getResources().getColor(R.color.black));

                et_company_name.setText(detailsDatas.getExpressCompanyName());
                et_company_number.setText(detailsDatas.getShipOrderNumber());
                tv_refund_status.setText(R.string.businesses_receiving);
                tv_explain.setText(detailsDatas.getNote());

                break;
            case "商家拒绝":
                btn_contact_the_merchant.setVisibility(View.VISIBLE);
                tv_time.setVisibility(View.GONE);
                tv_explain.setVisibility(View.GONE);

                lpStatus.setMargins(UiUtils.dip2px(15), UiUtils.dip2px(15), UiUtils.dip2px(0), UiUtils.dip2px(15));

                tv_refund_status.setTextColor(getResources().getColor(R.color.search_text_color));

                tv_refund_status.setText(R.string.refuse_to);

                break;
            case "待平台确认":
                btn_contact_the_merchant.setVisibility(View.GONE);
                tv_time.setVisibility(View.VISIBLE);
                tv_explain.setVisibility(View.VISIBLE);

                lpStatus.setMargins(UiUtils.dip2px(15), UiUtils.dip2px(15), UiUtils.dip2px(0), UiUtils.dip2px(10));
                lpTime.setMargins(UiUtils.dip2px(15), UiUtils.dip2px(0), UiUtils.dip2px(0), UiUtils.dip2px(5));
                lpExplain.setMargins(UiUtils.dip2px(15), UiUtils.dip2px(0), UiUtils.dip2px(15), UiUtils.dip2px(15));

                tv_refund_status.setTextColor(getResources().getColor(R.color.black));
                tv_time.setTextColor(getResources().getColor(R.color.black));

                tv_time.setCompoundDrawables(countDown, null, null, null);

                tv_refund_status.setText(R.string.terrace_audit);
                tv_time.setText(detailsDatas.getHandleDate());
                tv_explain.setText(detailsDatas.getNote());

                break;
            case "退款成功":
                btn_contact_the_merchant.setVisibility(View.GONE);
                tv_time.setVisibility(View.GONE);
                tv_explain.setVisibility(View.VISIBLE);

                lpStatus.setMargins(UiUtils.dip2px(15), UiUtils.dip2px(15), UiUtils.dip2px(0), UiUtils.dip2px(10));
                lpExplain.setMargins(UiUtils.dip2px(15), UiUtils.dip2px(0), UiUtils.dip2px(15), UiUtils.dip2px(15));

                tv_refund_status.setTextColor(getResources().getColor(R.color.black));

                tv_refund_status.setText(R.string.refund_success);
                tv_explain.setText(detailsDatas.getNote());

                break;

        }

        tv_refund_status.setLayoutParams(lpStatus);
        tv_time.setLayoutParams(lpTime);
        tv_explain.setLayoutParams(lpExplain);

    }

    /**
     * 初始化布局
     */
    private void initView() {
        setBackButtonVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.refund_details));

        productName = (TextView) findViewById(R.id.productName);
        saleType = (TextView) findViewById(R.id.saleType);
        salenum = (TextView) findViewById(R.id.salenum);
        returnReason = (TextView) findViewById(R.id.returnReason);
        refundMoney = (TextView) findViewById(R.id.refundMoney);
        payFee = (TextView) findViewById(R.id.payFee);
        handler = (TextView) findViewById(R.id.handler);
        returnIntro = (TextView) findViewById(R.id.returnIntro);
        replyBuyerStatus = (TextView) findViewById(R.id.replyBuyerStatus);
        iv_product_refund = (ImageView) findViewById(R.id.iv_product_refund);

        tv_refund_status = (TextView) findViewById(R.id.tv_refund_status);
        btn_contact_the_merchant = (Button) findViewById(R.id.btn_contact_the_merchant);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_explain = (TextView) findViewById(R.id.tv_explain);

        btn_add_express_information = (Button) findViewById(R.id.btn_add_express_information);
        include_express_info = findViewById(R.id.include_express_info);
        et_company_name = (EditText) findViewById(R.id.et_company_name);
        et_company_number = (EditText) findViewById(R.id.et_company_number);
        btn_finish = (Button) findViewById(R.id.btn_finish);

        et_company_name.setFocusable(false);
        et_company_number.setFocusable(false);
        btn_finish.setText(R.string.submit);

        btn_finish.setOnClickListener(this);
        btn_contact_the_merchant.setOnClickListener(this);
        btn_add_express_information.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_contact_the_merchant://联系商家
                P2PMessageActivity.IMFlag = true;
                P2PMessageActivity.IMUserName = mShopName;
                P2PMessageActivity.shopDetailImg =refundDetailsBean.getOrderItemInfo().getThumbnailsUrl();
                P2PMessageActivity.shopDetailDes = refundDetailsBean.getOrderItemInfo().getProductName();
                P2PMessageActivity.shopDetailPrice = refundDetailsBean.getAmount()+"";
                NimUIKit.startChatting(this, mSellerIMID, SessionTypeEnum.P2P, null, null);

                break;
            case R.id.btn_add_express_information://填写快递信息
                startActivityForResult(new Intent(this, AddExpressInformationActivity.class), 1001);
                startAnim();
                break;
            case R.id.btn_finish:
                userConfirmRefundGood();
                break;

        }
    }

    private void userConfirmRefundGood() {
        Map<String, String> params = new HashMap<>();
        params.put("userId", mUserId);
        params.put("orderRefundId", mRefundId);
        params.put("expressCompanyName", et_company_name.getText().toString());
        params.put("shipOrderNumber", et_company_number.getText().toString());
        OkHttpUtils
                .post()
                .params(params)
                .url(GlobalContent.POST_EXPRESS_INFORMATION)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("买家退货提交退货物流信息==" + response);
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (rootBean.ErrCode.equals("0")) {
                            btn_finish.setText(R.string.logistics_information_submit_hint);
                            btn_finish.setBackgroundResource(R.color.color_aaaaaa_content_text);
                            btn_finish.setEnabled(false);
                        } else {
                            showToast(rootBean.ResponseMsg);
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == 1002) {
            btn_add_express_information.setVisibility(View.GONE);
            include_express_info.setVisibility(View.VISIBLE);

            String companyName = data.getStringExtra("companyName");
            String companyNumber = data.getStringExtra("companyNumber");

            et_company_name.setText(companyName);
            et_company_number.setText(companyNumber);

        }
    }
}
