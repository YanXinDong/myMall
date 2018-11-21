package com.BFMe.BFMBuyer.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.javaBean.AccountBean;
import com.BFMe.BFMBuyer.javaBean.AddressListBean;
import com.BFMe.BFMBuyer.javaBean.DeductibleAmountBean;
import com.BFMe.BFMBuyer.javaBean.LimitDetailsBean;
import com.BFMe.BFMBuyer.javaBean.LimitProductFreightAmountBean;
import com.BFMe.BFMBuyer.javaBean.OrdersBean;
import com.BFMe.BFMBuyer.javaBean.PayBean;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.javaBean.WXBFMWechatBean;
import com.BFMe.BFMBuyer.utils.CacheUtils;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.Utils;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zhy.http.okhttp.OkHttpUtils.post;

/**
 * 立即购买——确认订单
 */
public class ImmediatelBuyActivity extends IBaseActivity implements View.OnClickListener {

    private String totalSkuid;
    private TextView tvAddressName;
    private TextView tvAddressPhone;
    private TextView tvAccountAddress;
    private TextView tvAccountJifen;
    private TextView tvHejiPrice;
    private Button btnCartAccaount;
    private int addressId;
    private TextView tvShopName;
    private ImageView ivAccomuntProduct;
    private TextView tvAccountDes;
    private TextView tvItemPrice;
    private EditText etInputInfo;
    private TextView tvAccountYunfei;
    private TextView tvAccountGuanshui;
    private TextView tvShopPrice;
    private String userId;

    private int cartItemId;//购物车id
    private TextView tv_add_address;
    private TextView tvItemNumber;

   /* private ImageView iv_reduce;//减号
    private TextView tv_buy_number;//数量
    private ImageView iv_add;//加号*/

    private CheckBox cb_isintegral;//是否使用积分
    private double mIntegralPerMoney;//积分抵扣的钱
    private double mUserIntegrals;//用户可用积分
    private double totalPrice;//总金额
    private double noTaxPrice;//不带税费  运费的总价格
    private int number = 1;
    private AccountBean mAccountBean;
    private String limitId;
    private double marketPrice;
    private boolean isLimitBuy;
    private RelativeLayout rl_layout;
    private boolean isAddress;//是否有收获地址
    private RelativeLayout rlEditAddress;

    private TextView tvTotalYunfei;
    private TextView tvTotalShuifei;
    private TextView tvTotalPrice1;

    private RelativeLayout rl_exchange_code;
    private EditText et_exchange_code;
    private TextView tv_official_hint;

    private boolean mIsExchangeCode;//是否需要兑换码
    private String mVeriCode;//兑换码
    private TextView tv_color_size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {

        //设置为按钮不可用  防止数据未加载的时候点击按钮
        btnNoClickable();
        rlEditAddress.setClickable(false);

        Intent intent = getIntent();
        totalSkuid = intent.getStringExtra("skuId");
        tvItemNumber.setText(intent.getStringExtra("productNumber"));
        limitId = intent.getStringExtra("LimitId");
        isLimitBuy = intent.getBooleanExtra("IsLimitBuy", false);
        mIsExchangeCode = intent.getBooleanExtra("IsExchangeCode", false);
        number = intent.getIntExtra("Count", 0);
        String colorName = intent.getStringExtra("colorName");
        String sizeName = intent.getStringExtra("sizeName");

        if (colorName != null || sizeName != null) {
            tv_color_size.setText(colorName + "  " + sizeName);
        }

        userId = CacheUtils.getString(this, GlobalContent.USER);

        if (mIsExchangeCode) {
            rl_exchange_code.setVisibility(View.VISIBLE);
            tv_official_hint.setVisibility(View.VISIBLE);

            btnCartAccaount.setClickable(false);
            btnCartAccaount.setBackgroundResource(R.color.app_body_bg);

        } else {
            rl_exchange_code.setVisibility(View.GONE);
            tv_official_hint.setVisibility(View.GONE);
        }
        setListener();
    }

    private void setListener() {
        //兑换码
        et_exchange_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mVeriCode = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 获取用户可用积分
     */
    private void getIntegral() {
        Map<String, String> params = new HashMap<>();
        params.put("UserId", userId);
        params.put("Amount", noTaxPrice + "");
        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBAL_GET_INTEGRAL)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                        getIntegral();
                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("用户可用积分" + response);
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        String responseMsg = rootBean.ResponseMsg;
                        if (responseMsg.equals("success")) {
                            DeductibleAmountBean deductibleAmountBean = mGson.fromJson(rootBean.Data, DeductibleAmountBean.class);
                            //积分抵扣的钱数
                            mIntegralPerMoney = deductibleAmountBean.getDeductibleAmount().getIntegralPerMoney();
                            //可用积分
                            mUserIntegrals = deductibleAmountBean.getDeductibleAmount().getUserIntegrals();
                            String integral = String.format(getResources().getString(R.string.integral_deduction_¥), Utils.doubleSave2(mIntegralPerMoney));
                            tvAccountJifen.setText(integral);
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //获取购物车地址
        getAddressId();

        //是否是限时购商品
        if(isLimitBuy) {
            //限时购商品不能使用积分
            rl_layout.setVisibility(View.GONE);
        }
        indentList();

        getWXService();
    }

    private void getLimitFreights(int productId) {
        Map<String, String> params = new HashMap<>();
        params.put("ProductId", productId + "");
        params.put("ProductCount", number + "");
        params.put("AddressId", addressId + "");
        OkHttpUtils
                .get()
                .url(GlobalContent.GLOBSAL_GET_LIMIT_PRODUCT_FREIGHT)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                        Logger.e("onError==" + e.toString());
                    }

                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        RootBean rootBean = gson.fromJson(response, RootBean.class);
                        if (rootBean.ErrCode.equals("0")) {
                            LimitProductFreightAmountBean limitProductFreightAmountBean = gson.fromJson(rootBean.Data, LimitProductFreightAmountBean.class);
                            double freight = limitProductFreightAmountBean.getData().getFreight();
                            double tax = limitProductFreightAmountBean.getData().getTax();
                            tvAccountYunfei.setText(getString(R.string.money_type) + Utils.doubleSave2(freight));//运费
                            //总税费
                            tvTotalShuifei.setText(getString(R.string.money_type) + Utils.doubleSave2(tax));
                            //总运费
                            tvTotalYunfei.setText(getString(R.string.money_type) + Utils.doubleSave2(freight));

                            totalPrice = number * marketPrice + freight + tax;
                            setTotalPrice();//合计
                            Logger.e("totalPrice==" + totalPrice);
                        }
                    }
                });
    }

    /**
     * 获取微信客服
     */
    private void getWXService() {
        OkHttpUtils
                .get()
                .url(GlobalContent.GET_BFM_WECHAT)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                        Logger.e("获取微信客服onError==" + e.toString());
                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("获取微信客服onResponse==" + response);
                        Gson gson = new Gson();
                        RootBean rootBean = gson.fromJson(response, RootBean.class);
                        if (0 == Integer.parseInt(rootBean.ErrCode)) {
                            WXBFMWechatBean wxbfmWechatBean = gson.fromJson(rootBean.Data, WXBFMWechatBean.class);
                            tv_official_hint.setText(String.format(getString(R.string.bfme_wx_hint),wxbfmWechatBean.getWX()));
                        }
                    }
                });
    }

    /**
     * 立即购买
     */
    private void commitOrder() {
        showProgress(getResources().getString(R.string.is_submit));
        if (isLimitBuy) {
            limitCommit();
        } else {
            commit();
        }
    }

    /**
     * 限时购商品提交订单
     */
    private void limitCommit() {
        Map<String, String> params = new HashMap<>();
        params.put("UserId", userId);
        params.put("SkuId", totalSkuid);
        params.put("RecieveAddressId", addressId + "");
        params.put("LimitId", limitId);

        if (cb_isintegral.isChecked()) {
            params.put("Integral", Math.round(mUserIntegrals) + "");
        } else {
            params.put("Integral", "0");
        }
        params.put("PlatformType", "2");
        params.put("Count", number + "");

        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBAL_ORDER_SUBMIT_LIMITBUY)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                        Logger.e("限时购提交订单onError==" + e.toString());
                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("限时购提交订单onResponse==" + response);
                        Gson gson = new Gson();
                        RootBean rootBean = gson.fromJson(response, RootBean.class);
                        if (Integer.parseInt(rootBean.ErrCode) == 0) {//成功

                            //跳转到支付页面
                            Intent intent = new Intent(ImmediatelBuyActivity.this, ChoosePayActivity.class);
                            intent.putExtra("orderId", rootBean.Data);
                            intent.putExtra("HasHSCode", mAccountBean.isHasHSCode());
                            //是否使用积分
                            if (cb_isintegral.isChecked()) {
                                double temp = totalPrice - mIntegralPerMoney;//总金额减去积分抵扣的钱
                                BigDecimal b = new BigDecimal(temp);
                                double price = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                intent.putExtra("totalPrice", Utils.doubleSave2(price));
                            } else {
                                intent.putExtra("totalPrice", Utils.doubleSave2(totalPrice));
                            }
                            startActivity(intent);
                            finish();
                            startAnim();
                        } else {
                           showToast(rootBean.ResponseMsg);
                        }
                    }
                });
    }

    /**
     * 普通商品提交订单
     */
    private void commit() {
        Map<String, String> params = new HashMap<>();
        OrdersBean ordersBean = new OrdersBean();
        ordersBean.setPlatformType(2);//平台类型
        ordersBean.setRecieveAddressId(addressId);//收货地址id
        ordersBean.setLimitId(limitId);
        ordersBean.setUserId(userId);//用户id

        if (cb_isintegral.isChecked()) {
            ordersBean.setIntergal(Math.round(mUserIntegrals));//积分数量
        } else {
            ordersBean.setIntergal(0);//积分数量
        }

        List<OrdersBean.OrdersData> orders = new ArrayList<>();
        OrdersBean.OrdersData ordersData = new OrdersBean.OrdersData();
        int[] castId = {cartItemId};
        ordersData.setCartItemIds(castId);//购物车id
        ordersData.setRemark(etInputInfo.getText().toString());//备注
        orders.add(ordersData);

        ordersBean.setOrders(orders);//订单列表

        String ordersJson = new Gson().toJson(ordersBean);
        params.put("orders", ordersJson);

        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBAL_CART_COMMIT)
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
                        resolveJson(response);
                    }
                });
    }

    /**
     * 解析提交订单后的json数据
     *
     * @param response
     */
    private void resolveJson(String response) {
        RootBean rootBean = mGson.fromJson(response, RootBean.class);
        if (Integer.parseInt(rootBean.ErrCode) == 0) {//成功

            //跳转到支付页面
            Intent intent = new Intent(this, ChoosePayActivity.class);
            PayBean payBean = mGson.fromJson(rootBean.Data, PayBean.class);
            intent.putExtra("orderId", payBean.getData().get(0) + "");
            intent.putExtra("HasHSCode", mAccountBean.isHasHSCode());
            //是否使用积分
            if (cb_isintegral.isChecked()) {
                double temp = totalPrice - mIntegralPerMoney;//总金额减去积分抵扣的钱
                BigDecimal b = new BigDecimal(temp);
                double price = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                intent.putExtra("totalPrice", Utils.doubleSave2(price));
            } else {
                intent.putExtra("totalPrice", Utils.doubleSave2(totalPrice));
            }
            startActivity(intent);
            finish();
            startAnim();
        } else {
            showToast(rootBean.ResponseMsg);
        }
    }

    /**
     * 获取收货地址
     */
    public void getAddressId() {
        showProgress();
        Map<String, String> params = new HashMap<>();
        params.put("userId", mUserId);
        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBAL_GET_ADDRESS)
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
                        String data = rootBean.Data;
                        Logger.e("收货地址onResponse" + response);

                        if (Integer.parseInt(rootBean.ErrCode) == 0) {
                            AddressListBean addressListBean = mGson.fromJson(data, AddressListBean.class);
                            ArrayList<AddressListBean.addresses> addressList = addressListBean.Address;
                            rlEditAddress.setClickable(true);

                            if (addressList != null && addressList.size() > 0) {
                                isAddress = true;//有收货地址
                                tv_add_address.setVisibility(View.GONE);
                                tvAddressName.setVisibility(View.VISIBLE);
                                tvAddressPhone.setVisibility(View.VISIBLE);
                                tvAccountAddress.setVisibility(View.VISIBLE);
                                for (int i = 0; i < addressList.size(); i++) {
                                    if (addressList.get(i).IsDefault) {
                                        addressId = Integer.parseInt(addressList.get(i).Id);
                                        tvAddressName.setText(addressList.get(i).ShipTo);
                                        tvAddressPhone.setText(addressList.get(i).Phone);
                                        tvAccountAddress.setText(addressList.get(i).RegionFullName + addressList.get(i).Address);
                                    }
                                }

                                if(isLimitBuy) {
                                    //获取限时购商品数据
                                    getLimitData();
                                }
                            } else {
                                isAddress = false;//没有收获地址
                                tv_add_address.setVisibility(View.VISIBLE);
                                tvAddressName.setVisibility(View.GONE);
                                tvAddressPhone.setVisibility(View.GONE);
                                tvAccountAddress.setVisibility(View.GONE);
                            }
                        }
                    }
                });
    }

    /**
     * 获取限时抢购 的价格
     */
    private void getLimitData() {
        Map<String, String> params = new HashMap<>();
        params.put("LimitId", limitId);
        params.put("userId", mUserId);
        OkHttpUtils
                .get()
                .url(GlobalContent.GLOBSAL_LIMIT_PRODUCT_DETAILS)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("限时购商品信息==" + response);
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (0 == Integer.parseInt(rootBean.ErrCode)) {
                            //按钮可用
                            btnYesClickable();
                            String data = rootBean.Data;
                            LimitDetailsBean limitDetailsBean = new Gson().fromJson(data, LimitDetailsBean.class);
                            parseJsonLimitData(limitDetailsBean);
                            getLimitFreights(limitDetailsBean.getData().getProductId());
                        }
                    }
                });
    }

    private void parseJsonLimitData(LimitDetailsBean limitDetailsBean) {
        LimitDetailsBean.DataBean data = limitDetailsBean.getData();
        limitId = data.getId() + "";

        marketPrice = data.getMarketPrice();
        tvShopName.setText(data.getShopName());//店铺名称
        Glide//商品缩略图
                .with(getApplicationContext())
                .load(data.getImagePath())
                .placeholder(R.drawable.zhanwei1)
                .crossFade()
                .into(ivAccomuntProduct);
        tvAccountDes.setText(data.getProductName());//商品名称
        tvItemPrice.setText(getString(R.string.money_type) + Utils.doubleSave2(marketPrice));//商品价格
        tvItemNumber.setText("*" + number);//商品数量

        tvAccountYunfei.setText(data.getFreightStr());//运费
        tvAccountGuanshui.setText(getString(R.string.money_type) + Utils.doubleSave2(data.getTax()));//税费

        noTaxPrice = marketPrice * number;
        //总商品金额
        tvTotalPrice1.setText(getString(R.string.money_type) + Utils.doubleSave2(noTaxPrice));
    }

    /**
     * 结算界面获取购物车列表
     */
    private void indentList() {
        showProgress();
        Map<String, String> params = new HashMap<>();
        params.put("UserId", mUserId);
        params.put("SkuIds", totalSkuid);
        OkHttpUtils
                .get()
                .url(GlobalContent.GLOBAl_COMMIT_ACCOUNT)
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
                        Logger.e("结算页面数据" + response);
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (!rootBean.ErrCode.equals("0")) {
                            return;
                        }
                        String data = rootBean.Data;
                        mAccountBean = mGson.fromJson(data, AccountBean.class);
                        if (mAccountBean.getCartItems() != null && mAccountBean.getCartItems().size() > 0) {
                            //按钮可用
                            btnYesClickable();
                            cartItemId = mAccountBean.getCartItems().get(0).getCartItems().get(0).getCartItemId();
                            addressId = mAccountBean.getAddressId();
                            //数据的设置
                            parseJsonData(mAccountBean.getCartItems().get(0));
                            //获取用户可用积分
                            getIntegral();
                        }
                    }
                });
    }

    /**
     * json数据的解析设置，根据json数据动态添加布局
     *
     * @param accountBean
     */
    private void parseJsonData(AccountBean.CartItemsDatas accountBean) {
        tvShopName.setText(accountBean.getShopName());//店铺名称
        Glide//商品缩略图
                .with(getApplicationContext())
                .load(accountBean.getCartItems().get(0).getImgUrl())
                .placeholder(R.drawable.zhanwei1)
                .crossFade()
                .into(ivAccomuntProduct);

        tvAccountDes.setText(accountBean.getCartItems().get(0).getName());//商品名称
        tvItemPrice.setText(getString(R.string.money_type) + Utils.doubleSave2(accountBean.getCartItems().get(0).getPrice()));//商品价格

        tvItemNumber.setText("*" + accountBean.getCartItems().get(0).getCount());//商品数量

        number = accountBean.getCartItems().get(0).getCount();
        //商品单价大于商家规定的金额  免运费
        setShopPrice(accountBean);

        //税费
        tvAccountGuanshui.setText(getString(R.string.money_type) + Utils.doubleSave2(accountBean.getTaxation()));

        //合计
        setTotalPrice();
        //不带税费  运费的总价格
        if (isLimitBuy) {
            noTaxPrice = marketPrice * accountBean.getCartItems().get(0).getCount();
        } else {
            noTaxPrice = accountBean.getCartItems().get(0).getPrice() * accountBean.getCartItems().get(0).getCount();
        }

        //总商品金额
        tvTotalPrice1.setText(getString(R.string.money_type) + Utils.doubleSave2(noTaxPrice));
        //总税费
        tvTotalShuifei.setText(getString(R.string.money_type) + Utils.doubleSave2(accountBean.getTaxation()));
        //总运费
        tvTotalYunfei.setText(getString(R.string.money_type) + Utils.doubleSave2(accountBean.getFreight()));
        tv_color_size.setText(accountBean.getCartItems().get(0).getColor() + "  " + accountBean.getCartItems().get(0).getSize());
        if (noTaxPrice < 0.02) {
            rl_layout.setVisibility(View.GONE);
        }
    }

    /**
     * 设置本店合计金额
     *
     * @param accountBean
     */
    private void setShopPrice(AccountBean.CartItemsDatas accountBean) {

        if (accountBean.getCartItems().get(0).getPrice() > accountBean.getFreeFreight()
                && accountBean.getFreeFreight() != 0) {

            tvAccountYunfei.setText(getString(R.string.pinkage));
            //本店合计
            if (isLimitBuy) {
                totalPrice = number
                        * marketPrice
                        + accountBean.getTaxation();
            } else {
                totalPrice = number
                        * accountBean.getCartItems().get(0).getPrice()
                        + accountBean.getTaxation();
            }

            tvShopPrice.setText("￥" + Utils.doubleSave2(totalPrice));
        } else {

            double temp = 0.00;
            if (accountBean.getFreight() == temp) {
                tvAccountYunfei.setText(getString(R.string.pinkage));
            } else {
                tvAccountYunfei.setText(getString(R.string.money_type) + Utils.doubleSave2(accountBean.getFreight()));
            }
            Logger.e("运费" + accountBean.getFreight());
            //本店合计
            if (isLimitBuy) {
                totalPrice = number
                        * marketPrice
                        + accountBean.getTaxation()
                        + accountBean.getFreight();
            } else {
                totalPrice = number
                        * accountBean.getCartItems().get(0).getPrice()
                        + accountBean.getTaxation()
                        + accountBean.getFreight();
            }

            tvShopPrice.setText(getString(R.string.money_type) + Utils.doubleSave2(totalPrice));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_edit_address://地址管理
                startActivity(new Intent(this, AddressMangerActivity.class));
                startAnim();
                break;
            case R.id.btn_cart_accaount://提交订单
                //效验
                if (Utils.isFastDoubleClick()) {
                    return;
                } else {
                    if (isAddress) {//是否有地址
                        if (mIsExchangeCode) {//是否需要兑换码
                            //效验兑换码
                            efficacyExchangeCode();
                        } else {
                            commitOrder();
                            rlEditAddress.setClickable(false);
                            btnNoClickable();
                        }

                    } else {//填写收货地址
                        showAddAddressDialog();
                    }
                }

                break;
            case R.id.cb_isintegral://是否使用积分
                setTotalPrice();
                break;
            default:
                break;
        }
    }

    private void setTotalPrice() {
        if (cb_isintegral.isChecked()) {
            double temp = totalPrice - mIntegralPerMoney;//总金额减去积分抵扣的钱
            BigDecimal b = new BigDecimal(temp);
            double price = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            tvHejiPrice.setText(getString(R.string.money_type) + price);
        } else {
            tvHejiPrice.setText(getString(R.string.money_type) + totalPrice);
        }
    }

    /**
     * 效验验证码
     */
    private void efficacyExchangeCode() {

        if (null == mVeriCode || mVeriCode.isEmpty()) {
            showToast(getString(R.string.input_code_hint));
            rlEditAddress.setClickable(true);
            btnYesClickable();
            return;
        }

        showProgress();
        Map<String, String> params = new HashMap<>();
        params.put("VeriCode", mVeriCode);
        params.put("SkuId", totalSkuid);
        post()
                .params(params)
                .url(GlobalContent.CHECK_LIMIT_BUY_CODE)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                        dismissProgress();
                        Logger.e("效验兑换码onError" + e.toString());
                        rlEditAddress.setClickable(true);
                        btnYesClickable();
                    }

                    @Override
                    public void onResponse(String response) {
                        dismissProgress();
                        Logger.e("效验兑换码onResponse" + response);
                        rlEditAddress.setClickable(true);
                        btnYesClickable();

                        Gson gson = new Gson();
                        RootBean rootBean = gson.fromJson(response, RootBean.class);
                        if (0 == Integer.parseInt(rootBean.ErrCode)) {
                            commitOrder();
                        } else {
                            showToast(rootBean.ResponseMsg);
                        }
                    }
                });
    }

    /**
     * 显示需要添加收获地址的dialog
     */
    private void showAddAddressDialog() {
        AlertDialog.Builder addAddressDialog =
                new AlertDialog.Builder(ImmediatelBuyActivity.this);
        addAddressDialog.setMessage(getResources().getString(R.string.add_address_hint2));
        addAddressDialog.setPositiveButton(getResources().getString(R.string.commit),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(ImmediatelBuyActivity.this, AddressMangerActivity.class));
                        startAnim();
                    }
                });
        //显示对话框
        addAddressDialog.setCancelable(false);
        addAddressDialog.show();
    }

    /**
     * 初始化布局
     */
    private void initView() {

        setBackButtonVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.affirm_order));
        //地址栏
        rlEditAddress = (RelativeLayout) findViewById(R.id.rl_edit_address);
        //收货人姓名
        tvAddressName = (TextView) findViewById(R.id.tv_address_name);
        //收货人手机号
        tvAddressPhone = (TextView) findViewById(R.id.tv_address_phone);
        //收货人地址
        tvAccountAddress = (TextView) findViewById(R.id.tv_account_address);
        //店铺名称
        tvShopName = (TextView) findViewById(R.id.tv_shop_name);
        //商品图标
        ivAccomuntProduct = (ImageView) findViewById(R.id.iv_accomunt_product);
        //商品描述
        tvAccountDes = (TextView) findViewById(R.id.tv_account_des);
        //隐藏税费
        RelativeLayout rlTax = (RelativeLayout) findViewById(R.id.rl_tax);
        rlTax.setVisibility(View.GONE);
        //商品价格
        tvItemPrice = (TextView) findViewById(R.id.tv_item_price);
        //商品数量
        tvItemNumber = (TextView) findViewById(R.id.tv_item_number);
        tvItemNumber.setVisibility(View.VISIBLE);
        //备注
        etInputInfo = (EditText) findViewById(R.id.et_input_info);
        //运费
        tvAccountYunfei = (TextView) findViewById(R.id.tv_account_yunfei);
        //关税
        tvAccountGuanshui = (TextView) findViewById(R.id.tv_account_guanshui);
        //本店合计
        tvShopPrice = (TextView) findViewById(R.id.tv_shop_price);

        //总运费
        tvTotalYunfei = (TextView) findViewById(R.id.tv_total_yunfei);
        //总税费
        tvTotalShuifei = (TextView) findViewById(R.id.tv_total_shuifei);
        //总商品金额
        tvTotalPrice1 = (TextView) findViewById(R.id.tv_total_price);

        //克扣积分
        tvAccountJifen = (TextView) findViewById(R.id.tv_account_jifen);
        //合计
        tvHejiPrice = (TextView) findViewById(R.id.tv_heji_price);
        //提交订单到支付
        btnCartAccaount = (Button) findViewById(R.id.btn_cart_accaount);
        //添加收货地址
        tv_add_address = (TextView) findViewById(R.id.tv_add_address);
        //是否使用积分
        cb_isintegral = (CheckBox) findViewById(R.id.cb_isintegral);

        rl_layout = (RelativeLayout) findViewById(R.id.rl_layout);

        rl_exchange_code = (RelativeLayout) findViewById(R.id.rl_exchange_code);
        et_exchange_code = (EditText) findViewById(R.id.et_exchange_code);
        tv_official_hint = (TextView) findViewById(R.id.tv_official_hint);
        tv_color_size = (TextView) findViewById(R.id.tv_color_size);

        rlEditAddress.setOnClickListener(this);
        btnCartAccaount.setOnClickListener(this);
        cb_isintegral.setOnClickListener(this);

    }

    @Override
    public int initLayout() {
        return R.layout.activity_immediate_buy;
    }

    //设置为按钮不可用  防止数据未加载的时候点击按钮
    private void btnNoClickable() {
        btnCartAccaount.setClickable(false);
        btnCartAccaount.setBackgroundResource(R.color.btn_no_clickable_bg);
        btnCartAccaount.setTextColor(getResources().getColor(R.color.btn_no_clickable_text));
    }

    //设置为按钮可用  数据加载完成
    private void btnYesClickable() {
        btnCartAccaount.setClickable(true);
        btnCartAccaount.setBackgroundResource(R.color.btn_text_price);
        btnCartAccaount.setTextColor(getResources().getColor(R.color.btn_yes_clickable_text));
    }
}
