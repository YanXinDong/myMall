package com.BFMe.BFMBuyer.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.javaBean.AccountBean;
import com.BFMe.BFMBuyer.javaBean.AddressBean;
import com.BFMe.BFMBuyer.javaBean.DeductibleAmountBean;
import com.BFMe.BFMBuyer.javaBean.OrdersBean;
import com.BFMe.BFMBuyer.javaBean.PayBean;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.utils.CacheUtils;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.Utils;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.bumptech.glide.Glide;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 购物车结算——确认订单
 */
public class AccountActivity extends IBaseActivity implements View.OnClickListener {

    private String totalSkuid;
    private TextView tvAddressName;
    private TextView tvAddressPhone;
    private TextView tvAccountAddress;
    private TextView tv_add_address;
    private ImageButton ibAddressManager;
    private LinearLayout llAccountProduct;
    private TextView tvAccountJifen;
    private TextView tvHejiPrice;
    private Button btnCartAccaount;
    private TextView tvTotalYunfei;
    private TextView tvTotalShuifei;
    private TextView tvTotalPrice;

    private CheckBox cb_isintegral;//是否使用积分
    private double mIntegralPerMoney;//积分抵扣的钱
    private double mUserIntegrals;//用户可用积分

    private int addressId;
    private String userId;
    private ArrayList<AccountBean.CartItemsDatas> mCartItems;
    //商品价格总价
    private float totalPrice = 0;
    //合计
    private float total = 0;
    private boolean isAddress;//是否有收获地址

    private RelativeLayout rlEditAddress;
    private TextView tv_color_size;
    private HashMap<Integer, String> mEditMap = new HashMap<>();
    private boolean hasHSCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //重置之前的数据
        llAccountProduct.removeAllViews();
        totalPrice = 0;
        total = 0;
        showProgress();
        //获取购物车列表
        indentList();
    }

    /**
     * 结算界面获取购物车列表
     */
    private void initData() {

        //设置为按钮不可用  防止数据未加载的时候点击按钮
        btnNoClickable();
        rlEditAddress.setClickable(false);

        Intent intent = getIntent();
        totalSkuid = intent.getStringExtra("totalSkuid");
        userId = CacheUtils.getString(this, GlobalContent.USER);
    }

    //设置为按钮不可用  防止数据未加载的时候点击按钮
    private void btnNoClickable() {
        btnCartAccaount.setClickable(false);
        btnCartAccaount.setBackgroundResource(R.color.btn_no_clickable_bg);
        btnCartAccaount.setTextColor(getResources().getColor(R.color.btn_no_clickable_text));
    }

    //设置为按钮不可用  防止数据未加载的时候点击按钮
    private void btnYesClickable() {
        btnCartAccaount.setClickable(true);
        btnCartAccaount.setBackgroundResource(R.color.btn_text_price);
        btnCartAccaount.setTextColor(getResources().getColor(R.color.btn_yes_clickable_text));
    }

    /**
     * 获取购物车列表
     */
    private void indentList() {
        OkHttpUtils
                .get()
                .url(GlobalContent.GLOBAl_COMMIT_ACCOUNT)
                .addParams("UserId",userId)
                .addParams("SkuIds",totalSkuid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);

                        if (Integer.parseInt(rootBean.ErrCode) == 0) {
                            dismissProgress();
                            //按钮可用
                            btnYesClickable();
                            AccountBean accountBean = mGson.fromJson(rootBean.Data, AccountBean.class);
                            hasHSCode = accountBean.isHasHSCode();
                            addressId = accountBean.getAddressId();
                            //获取地址
                            getAddressId(addressId);
                            //数据的设置
                            parseJsonData(accountBean);
                            //获取用户可用积分
                            getIntegral();
                        }
                    }
                });
    }

    /**
     * 获取用户可用积分
     */
    private void getIntegral() {

        Map<String, String> params = new HashMap<>();
        params.put("UserId",userId);
        params.put("Amount",totalPrice + "");
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

    /**
     * 地址的设置
     *
     * @param addressId
     */
    private void getAddressId(final int addressId) {
        Map<String, String> params = new HashMap<>();
        params.put("AddressId",addressId +"");
        OkHttpUtils
                .get()
                .url(GlobalContent.GLOBAl_GET_SETTED_ADDRESS)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        rlEditAddress.setClickable(true);
                        if (Integer.parseInt(rootBean.ErrCode) == 0) {
                            isAddress = true;//有收货地址
                            tv_add_address.setVisibility(View.GONE);
                            tvAddressName.setVisibility(View.VISIBLE);
                            tvAddressPhone.setVisibility(View.VISIBLE);
                            tvAccountAddress.setVisibility(View.VISIBLE);

                            String data = rootBean.Data;
                            AddressBean addressBean = mGson.fromJson(data, AddressBean.class);
                            AddressBean.addresses address = addressBean.Address;
                            //给地址设置值
                            tvAddressName.setText(address.ShipTo);
                            tvAddressPhone.setText(address.Phone);
                            tvAccountAddress.setText(address.RegionFullName + address.Address);
                        } else {
                            isAddress = false;//没有收获地址
                            tv_add_address.setVisibility(View.VISIBLE);
                            tvAddressName.setVisibility(View.GONE);
                            tvAddressPhone.setVisibility(View.GONE);
                            tvAccountAddress.setVisibility(View.GONE);
                        }
                    }
                });

    }

    /**
     * json数据的解析设置，根据json数据动态添加布局
     */
    private void parseJsonData(AccountBean accountBean) {
        //设置默认接口的网络访问，编辑地址的网络访问。
        mCartItems = (ArrayList<AccountBean.CartItemsDatas>) accountBean.getCartItems();
        //添加店铺
        float totalShuiFei = 0;
        float totalYunFei = 0;
        for (int i = 0; i < mCartItems.size(); i++) {
            View view = View.inflate(this, R.layout.confirm_acconunt_product, null);
            //店铺名称
            TextView tvShopName = (TextView) view.findViewById(R.id.tv_shop_name);
            //子item名称
            LinearLayout llAccountItem = (LinearLayout) view.findViewById(R.id.ll_account_item);
            EditText etInputInfo = (EditText) view.findViewById(R.id.et_input_info);
            TextView tvAccountYunfei = (TextView) view.findViewById(R.id.tv_account_yunfei);
            TextView tvAccountGuanshui = (TextView) view.findViewById(R.id.tv_account_guanshui);
            TextView tvShopPrice = (TextView) view.findViewById(R.id.tv_shop_price);


            SpannableString spannableString = new SpannableString(getResources().getString(R.string.import_remark_content));
            AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(15, true);
            spannableString.setSpan(absoluteSizeSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            etInputInfo.setHint(new SpannableString(spannableString));
            etInputInfo.addTextChangedListener(new MyTextWatcher(i));

            AccountBean.CartItemsDatas cartItemsDatas = mCartItems.get(i);
            tvShopName.setText(cartItemsDatas.getShopName());

            AccountBean.CartItemsDatas datas = mCartItems.get(i);
            ArrayList<AccountBean.CartItemsDatas.CartItems> itemList = (ArrayList<AccountBean.CartItemsDatas.CartItems>) datas.getCartItems();
            float price = 0;

            for (int j = 0; j < itemList.size(); j++) {
                View inflate = View.inflate(this, R.layout.confirm_account_product_item, null);
                //item的图片
                ImageView ivAccomuntProduct = (ImageView) inflate.findViewById(R.id.iv_accomunt_product);
                //商品描述
                TextView tvAccountDes = (TextView) inflate.findViewById(R.id.tv_account_des);
                //价格
                TextView tvItemPrice = (TextView) inflate.findViewById(R.id.tv_item_price);
                //数量
                TextView tvItemNumber = (TextView) inflate.findViewById(R.id.tv_item_number);
                //税费
                TextView tvItemShuifei = (TextView) inflate.findViewById(R.id.tv_item_shuifei);
                tv_color_size = (TextView) inflate.findViewById(R.id.tv_color_size);
                AccountBean.CartItemsDatas.CartItems info = itemList.get(j);

                Glide//商品缩略图
                        .with(getApplicationContext())
                        .load(info.getImgUrl())
                        .into(ivAccomuntProduct);
                tvAccountDes.setText(info.getName());//商品名称
                tvItemNumber.setText("*" + info.getCount());//商品数量
                tvItemPrice.setText(getResources().getString(R.string.money_type) + Utils.doubleSave2(info.getPrice()));//商品单价

                String product_tax = getResources().getString(R.string.product_tax_¥);
                tvItemShuifei.setText(String.format(product_tax,Utils.doubleSave2(info.getTaxation())));//商品单税
                tv_color_size.setText(info.getColor() + "  " + info.getSize());
                llAccountItem.addView(inflate);
                //计算店铺里面的全部商品的价格
                price += info.getPrice() * info.getCount();
            }

            double yunfei = datas.getFreight();
            if (price > datas.getFreeFreight() && datas.getFreeFreight() != 0) {
                //运费
                tvAccountYunfei.setText(getResources().getString(R.string.pinkage));
                yunfei = 0;
            } else {
                //运费
                tvAccountYunfei.setText(getResources().getString(R.string.money_type) + Utils.doubleSave2(yunfei));
            }
            //本店合计
            tvShopPrice.setText(getResources().getString(R.string.money_type) + Utils.doubleSave2(price + yunfei + datas.getTaxation()));
            //关税
            tvAccountGuanshui.setText(getResources().getString(R.string.money_type) + Utils.doubleSave2(datas.getTaxation()));

            llAccountProduct.addView(view);

            //总商品价格
            totalPrice += price;
            //总税费
            totalShuiFei += Float.parseFloat(String.valueOf(datas.getTaxation()));
            //总运费
            totalYunFei += Float.parseFloat(String.valueOf(yunfei));
        }
        //总商品金额
        tvTotalPrice.setText(getResources().getString(R.string.money_type) + Utils.doubleSave2(totalPrice));
        //总税费
        tvTotalShuifei.setText(getResources().getString(R.string.money_type) + Utils.doubleSave2(totalShuiFei));
        //总运费
        tvTotalYunfei.setText(getResources().getString(R.string.money_type) + Utils.doubleSave2(totalYunFei));


        //合计
        total = totalPrice + totalYunFei + totalShuiFei;
        setTotalPrice();
    }

    class MyTextWatcher implements TextWatcher{
        private int mPosition;
         MyTextWatcher(int position) {
             mPosition = position;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(s != null) {
                SaveEdit(mPosition,s.toString());
            }
        }
    }

    public void SaveEdit(int position, String string) {
        //回调处理edittext内容，使用map的好处在于：position确定的情况下，string改变，只会动态改变string内容
        mEditMap.put(position,string);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_edit_address:
                Intent intent = new Intent(this, AddressMangerActivity.class);
                intent.putExtra("addressId", addressId + "");
                startActivity(intent);
                startAnim();
                break;
            case R.id.btn_cart_accaount:
                Logger.e("备注信息=="+mEditMap.toString());
                //效验
                if (Utils.isFastDoubleClick()) {
                    return;
                } else {
                    showProgress(getResources().getString(R.string.is_submit));
                    if (isAddress) {
                        commintOrder();
                    } else {
                        showAddAddressDialog();
                    }
                    rlEditAddress.setClickable(false);
                    btnNoClickable();
                }

                break;
            case R.id.cb_isintegral:
                setTotalPrice();
                break;
            default:
                break;
        }
    }

    /**
     * 显示需要添加收获地址的dialog
     */
    private void showAddAddressDialog() {
        AlertDialog.Builder addAddressDialog =
                new AlertDialog.Builder(AccountActivity.this);
        addAddressDialog.setMessage(getResources().getString(R.string.add_address_hint2));
        addAddressDialog.setPositiveButton(getResources().getString(R.string.commit),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(AccountActivity.this, AddressMangerActivity.class));
                    }
                });
        //显示对话框
        addAddressDialog.setCancelable(false);
        addAddressDialog.show();
    }

    /**
     * 是否使用积分的点击事件监听
     */
    private void setTotalPrice() {
        if (cb_isintegral.isChecked()) {
            double temp = total - mIntegralPerMoney;//总金额减去积分抵扣的钱
            BigDecimal b = new BigDecimal(temp);
            double price = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            tvHejiPrice.setText(getResources().getString(R.string.money_type) + Utils.doubleSave2(price));
        } else {
            tvHejiPrice.setText(getResources().getString(R.string.money_type) + Utils.doubleSave2(total));
        }
    }

    /**
     * 调用提交购物车接口
     */
    private void commintOrder() {
        Map<String, String> params = new HashMap<>();

        OrdersBean ordersBean = new OrdersBean();
        ordersBean.setPlatformType(2);//平台类型
        ordersBean.setRecieveAddressId(addressId);//收货地址id
        ordersBean.setUserId(userId);//用户id
        if (cb_isintegral.isChecked()) {
            ordersBean.setIntergal(Math.round(mUserIntegrals));//积分数量
        } else {
            ordersBean.setIntergal(0);//积分数量
        }
        List<OrdersBean.OrdersData> orders = new ArrayList<>();

        for (int i = 0; i < mCartItems.size(); i++) {
            OrdersBean.OrdersData ordersData = new OrdersBean.OrdersData();
            int cartId[] = new int[mCartItems.get(i).getCartItems().size()];
            for (int j = 0; j < mCartItems.get(i).getCartItems().size(); j++) {
                int cartItemId = mCartItems.get(i).getCartItems().get(j).getCartItemId();
                cartId[j] = cartItemId;
                ordersData.setCartItemIds(cartId);//购物车id
                ordersData.setRemark(mEditMap.get(i));//备注
            }
            orders.add(ordersData);
        }
        ordersBean.setOrders(orders);//订单列表
        String ordersJson = mGson.toJson(ordersBean);
        params.put("orders", ordersJson);
        Logger.e("params=="+ordersJson);
        Logger.e("params=="+params);
        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBAL_CART_COMMIT)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {

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
        Logger.e("提交订单=="+response);
        RootBean rootBean = mGson.fromJson(response, RootBean.class);
        if (Integer.parseInt(rootBean.ErrCode) == 0) {//成功
            PayBean payBean = mGson.fromJson(rootBean.Data, PayBean.class);

            if (payBean.getData().size() > 1) {//订单数大于一个跳转到待支付页面
                //跳转到待支付页面
                Intent intentpay = new Intent(this, MyOrderActivity.class);
                intentpay.putExtra("flag", 1);
                startActivity(intentpay);

            } else {
                //跳转到支付页面
                Intent intent = new Intent(this, ChoosePayActivity.class);
                intent.putExtra("orderId", payBean.getData().get(0) + "");
                intent.putExtra("HasHSCode", hasHSCode);
                //是否使用积分
                if (cb_isintegral.isChecked()) {
                    double temp = total - mIntegralPerMoney;//总金额减去积分抵扣的钱
                    BigDecimal b = new BigDecimal(temp);
                    double price = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    intent.putExtra("totalPrice", Utils.doubleSave2(price));
                } else {
                    intent.putExtra("totalPrice", Utils.doubleSave2(total));
                }
                startActivity(intent);
            }

            finish();
            startAnim();
        } else {
            showToast(rootBean.ResponseMsg);
        }
    }

    private void initView() {

        setBackButtonVisibility(View.VISIBLE);
        tv_title.setText(getResources().getString(R.string.affirm_order));
        //地址栏
        rlEditAddress = (RelativeLayout) findViewById(R.id.rl_edit_address);
        //收货人姓名
        tvAddressName = (TextView) findViewById(R.id.tv_address_name);
        //收货人手机号
        tvAddressPhone = (TextView) findViewById(R.id.tv_address_phone);
        //收货人地址
        tvAccountAddress = (TextView) findViewById(R.id.tv_account_address);
        tv_add_address = (TextView) findViewById(R.id.tv_add_address);
        //地址设置
        ibAddressManager = (ImageButton) findViewById(R.id.ib_address_manager);
        //店铺以及对应的商品
        llAccountProduct = (LinearLayout) findViewById(R.id.ll_account_product);
        //提交订单对应积分
        tvAccountJifen = (TextView) findViewById(R.id.tv_account_jifen);
        //积分
        cb_isintegral = (CheckBox) findViewById(R.id.cb_isintegral);
        //总运费
        tvTotalYunfei = (TextView) findViewById(R.id.tv_total_yunfei);
        //总税费
        tvTotalShuifei = (TextView) findViewById(R.id.tv_total_shuifei);
        //总商品金额
        tvTotalPrice = (TextView) findViewById(R.id.tv_total_price);
        //结算的合计计算价格
        tvHejiPrice = (TextView) findViewById(R.id.tv_heji_price);
        //提交订单
        btnCartAccaount = (Button) findViewById(R.id.btn_cart_accaount);

        ibAddressManager.setOnClickListener(this);
        rlEditAddress.setOnClickListener(this);
        btnCartAccaount.setOnClickListener(this);
        cb_isintegral.setOnClickListener(this);
    }

    /**
     * 点击屏幕空白区域隐藏软键盘（方法1）
     * <p>在onTouch中处理，未获焦点则隐藏</p >
     * <p>参照以下注释代码</p >
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_account;
    }
}
