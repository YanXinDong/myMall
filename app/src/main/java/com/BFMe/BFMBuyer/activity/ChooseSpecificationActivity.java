package com.BFMe.BFMBuyer.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.adapter.TagAdapter;
import com.BFMe.BFMBuyer.javaBean.CartBean;
import com.BFMe.BFMBuyer.javaBean.LimitDetailsBean;
import com.BFMe.BFMBuyer.javaBean.ProducetSUKBean;
import com.BFMe.BFMBuyer.javaBean.ProductDetailBean;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.utils.CacheUtils;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.Utils;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.BFMe.BFMBuyer.view.flowlayout.FlowTagLayout;
import com.BFMe.BFMBuyer.view.flowlayout.OnTagSelectListener;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 选择购买数量和规格
 */
public class ChooseSpecificationActivity extends Activity implements View.OnClickListener {

    private ImageView ivFinish;
    private ImageView ivReduce;
    private TextView tvBuyNumber;
    private ImageView ivAdd;
    private Button btnConfirm;
    private int number = 1;


    private boolean isLimitBuy;
    private int retainCount;//限时购剩余的库存
    private int maxSaleCount;//限时购最大库存
    private int isAddCart;//0 == 加入购物车
    // 1 == 立即购买
    // 2 == 限时购购买

    private int maxSaleCount1;//普通商品库存
    private String userId;
    private String skuId;
    private String productId;
    private Boolean mHaveCode;

    private TextView tvTotal;
    private FlowTagLayout ftlSize;
    private FlowTagLayout ftlColor;
    private TagAdapter adapterSize;
    private TagAdapter adapterColor;
    private ProductDetailBean productDetailBean;
    private LimitDetailsBean limitDetailsBean;
    private TextView tvSku;
    private String colorName;
    private String sizeName;
    private Map<String, String> mapSize;
    private Map<String, String> mapColor;
    private List<String> colorSource;
    private List<String> sizeSource;
    private String marketPrice;//限时购的价格
    private TextView tv_size;
    private TextView tv_color;

    private LinearLayout llCartBuy;
    private Button btnAddCart;
    private Button btnQuickBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_choose_specification);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initView();
        initData();
    }


    private void initData() {
        userId = CacheUtils.getString(this, GlobalContent.USER);
        Intent intent = getIntent();
        productDetailBean = (ProductDetailBean) intent.getSerializableExtra("productDetailBean");
        limitDetailsBean = (LimitDetailsBean) intent.getSerializableExtra("limitDetailsBean");
        isLimitBuy = productDetailBean.isLimitBuy();
        isAddCart = intent.getIntExtra("isAddCart", 2);
        maxSaleCount1 = productDetailBean.getStockCount();
        productId = intent.getStringExtra("productId");
        skuId = productDetailBean.getSendMethod().getSKUId();
        if (isAddCart == 2) {
            btnConfirm.setVisibility(View.GONE);
            llCartBuy.setVisibility(View.VISIBLE);
        } else {
            llCartBuy.setVisibility(View.GONE);
            btnConfirm.setVisibility(View.VISIBLE);
        }
        if (isLimitBuy) {
            btnAddCart.setVisibility(View.GONE);//限时购 购物车不可用
            mHaveCode = limitDetailsBean.getData().isHaveCode();
            marketPrice = Utils.doubleSave2(limitDetailsBean.getData().getMarketPrice());
            maxSaleCount = limitDetailsBean.getData().getMaxSaleCount();
            retainCount = limitDetailsBean.getData().getLimitStock();
            tvSku.setText(String.format(getResources().getString(R.string.sku_number),retainCount));
            tvTotal.setText(marketPrice);
            if (retainCount == 0) {
                btnQuickBuy.setEnabled(false);
                btnQuickBuy.setBackgroundResource(R.drawable.shape_circle_range3);
                btnQuickBuy.setTextColor(getResources().getColor(R.color.gray));

                btnConfirm.setEnabled(false);
                btnConfirm.setBackgroundResource(R.color.app_body_bg);
            } else {
                btnQuickBuy.setEnabled(true);
                btnConfirm.setBackgroundResource(R.drawable.btnbg_blue);
                btnConfirm.setEnabled(true);
            }
        } else {
            if (maxSaleCount1 == 0) {
                btnQuickBuy.setEnabled(false);
                btnQuickBuy.setBackgroundResource(R.drawable.shape_circle_range3);
                btnQuickBuy.setTextColor(getResources().getColor(R.color.gray));

                btnAddCart.setEnabled(false);
                btnAddCart.setTextColor(getResources().getColor(R.color.gray));
                btnAddCart.setBackgroundResource(R.drawable.shape_circle_range3);

                btnConfirm.setEnabled(false);
                btnConfirm.setBackgroundResource(R.color.app_body_bg);
            } else {
                btnQuickBuy.setEnabled(true);
                btnAddCart.setEnabled(true);
                btnConfirm.setBackgroundResource(R.drawable.btnbg_blue);
                btnConfirm.setEnabled(true);
            }
            tvSku.setText(String.format(getResources().getString(R.string.sku_number),maxSaleCount1));
            tvTotal.setText(productDetailBean.getPrice());
        }

        initSizeAndColorData();
        setAdapter();
    }

    private void setAdapter() {


        //颜色
        adapterColor = new TagAdapter<>(this);

        ftlColor.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        ftlColor.setAdapter(adapterColor);

        ftlColor.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                if (selectedList != null && selectedList.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (int i : selectedList) {
                        sb.append(parent.getAdapter().getItem(i));
                    }
                    colorName = sb.toString();
                    if (!TextUtils.isEmpty(sizeName)) {
                        skuId = productDetailBean.getProduct().getId() + "_" + mapColor.get(colorName) + "_" + mapSize.get(sizeName) + "_" + "0";
                        getProducetSKU();
                    } else if (productDetailBean.getSize().size() == 0 && TextUtils.isEmpty(sizeName)) {
                        skuId = productDetailBean.getProduct().getId() + "_" + mapColor.get(colorName) + "_" + 0 + "_" + "0";
                        getProducetSKU();
                    }
                }
            }
        });

        adapterSize = new TagAdapter<>(this);
        ftlSize.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        ftlSize.setAdapter(adapterSize);

        ftlSize.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                if (selectedList != null && selectedList.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (int i : selectedList) {
                        sb.append(parent.getAdapter().getItem(i));
                    }
                    sizeName = sb.toString();
                    if (!TextUtils.isEmpty(colorName)) {
                        skuId = productDetailBean.getProduct().getId() + "_" + mapColor.get(colorName) + "_" + mapSize.get(sizeName) + "_" + "0";
                        getProducetSKU();
                    } else if (productDetailBean.getColor().size() == 0 && TextUtils.isEmpty(colorName)) {
                        skuId = productDetailBean.getProduct().getId() + "_" + 0 + "_" + mapSize.get(sizeName) + "_" + "0";
                        getProducetSKU();
                    }
                }
            }
        });

        adapterSize.onlyAddAll(sizeSource);
        adapterColor.onlyAddAll(colorSource);
    }

    private void initView() {
        ivFinish = (ImageView) findViewById(R.id.iv_finish);
        ivReduce = (ImageView) findViewById(R.id.iv_reduce);
        tvBuyNumber = (TextView) findViewById(R.id.tv_buy_number);
        ivAdd = (ImageView) findViewById(R.id.iv_add);
        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        tvSku = (TextView) findViewById(R.id.tv_sku);


        tvTotal = (TextView) findViewById(R.id.tv_total);
        ftlSize = (FlowTagLayout) findViewById(R.id.ftl_size);
        ftlColor = (FlowTagLayout) findViewById(R.id.ftl_color);
        tv_size = (TextView) findViewById(R.id.tv_size);
        tv_color = (TextView) findViewById(R.id.tv_color);


        llCartBuy = (LinearLayout) findViewById(R.id.ll_cart_buy);
        btnAddCart = (Button) findViewById(R.id.btn_add_cart);
        btnQuickBuy = (Button) findViewById(R.id.btn_quick_buy);


        ivFinish.setOnClickListener(this);
        ivReduce.setOnClickListener(this);
        ivAdd.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        btnAddCart.setOnClickListener(this);
        btnQuickBuy.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_finish:
                number = 1;
                finish();
                overridePendingTransition(R.anim.dialog_exit, 0);
                break;
            case R.id.iv_reduce:
                number = Integer.parseInt(tvBuyNumber.getText().toString().trim());
                if (number <= 1) {
                    return;
                }
                tvBuyNumber.setText(String.valueOf(--number));
                break;
            case R.id.iv_add:
                tvBuyNumber.setText(String.valueOf(++number));
                break;
            case R.id.btn_confirm:
                setConfirm();
                break;
            case R.id.btn_add_cart://加入购物车
                isAddCart = 0;
                setConfirm();
                break;
            case R.id.btn_quick_buy://立即购买
                isAddCart = 1;
                setConfirm();
                break;

        }
    }

    /**
     * 加入购物车或立即购买
     */
    private void setConfirm() {
        if (productDetailBean.getColor().size() > 0 && TextUtils.isEmpty(colorName)) {
            Toast.makeText(ChooseSpecificationActivity.this, getResources().getString(R.string.select_color_hint), Toast.LENGTH_SHORT).show();
            return;
        }
        if (productDetailBean.getSize().size() > 0 && TextUtils.isEmpty(sizeName)) {
            Toast.makeText(ChooseSpecificationActivity.this, getResources().getString(R.string.select_size_hint), Toast.LENGTH_SHORT).show();
            return;
        }
        if (isLimitBuy) {
            if (retainCount >= maxSaleCount) {
                if (number <= maxSaleCount) {
                    PromptlyBuy();
                } else {

                    Toast.makeText(ChooseSpecificationActivity.this, getResources().getString(R.string.sku_insufficient), Toast.LENGTH_SHORT).show();
                    number = 1;
                }
            } else {
                if (number <= retainCount) {
                    PromptlyBuy();
                } else {
                    Toast.makeText(ChooseSpecificationActivity.this, getResources().getString(R.string.sku_insufficient), Toast.LENGTH_SHORT).show();
                    number = 1;
                }
            }
        } else {
            if (number <= maxSaleCount1) {

                if (isAddCart == 0) {
                    //访问网络加入购物车
                    AddCart();
                } else {
                    //立即购买 添加到购物车
                    PromptlyBuy();
                }

            } else {

                int temp ;
                if (maxSaleCount1 == 0) {
                    temp = 1;
                } else {
                    temp = maxSaleCount1;
                }
                tvBuyNumber.setText(String.valueOf(temp));
                Toast.makeText(ChooseSpecificationActivity.this, getResources().getString(R.string.sku_insufficient), Toast.LENGTH_SHORT).show();
                number = temp;
            }
        }
    }

    /**
     * 立即购买
     */
    private void PromptlyBuy() {

        if (isLimitBuy) {
            Intent intentConfirm = new Intent(ChooseSpecificationActivity.this, ImmediatelBuyActivity.class);
            intentConfirm.putExtra("LimitId", productId);
            intentConfirm.putExtra("Count", number);
            intentConfirm.putExtra("skuId", skuId);
            intentConfirm.putExtra("IsLimitBuy", isLimitBuy);
            intentConfirm.putExtra("IsExchangeCode", mHaveCode);
            intentConfirm.putExtra("colorName", colorName);
            intentConfirm.putExtra("sizeName", sizeName);
            startActivity(intentConfirm);
            finish();
            overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
        } else {
            Map<String, String> params = new HashMap<>();
            params.put("SkuId", skuId);
            params.put("Count", number + "");
            params.put("UserId", userId);
            OkHttpUtils
                    .post()
                    .url(GlobalContent.GLOBSAL_PROMPTLY_BUY)
                    .params(params)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(okhttp3.Request request, Exception e) {

                        }

                        @Override
                        public void onResponse(String response) {
                            Logger.e("立即购买"+response);
                            Gson gson = new Gson();
                            RootBean rootBean = gson.fromJson(response, RootBean.class);
                            if (rootBean.ErrCode.equals("0")) {
                                getNetShoppingCartNumber();
                                Intent intentConfirm = new Intent(ChooseSpecificationActivity.this, ImmediatelBuyActivity.class);
                                intentConfirm.putExtra("LimitId", productId);
                                intentConfirm.putExtra("skuId", skuId);
                                intentConfirm.putExtra("IsLimitBuy", isLimitBuy);
                                intentConfirm.putExtra("IsExchangeCode", mHaveCode);
                                startActivity(intentConfirm);
                                finish();
                                overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
                            } else {
                                Toast.makeText(ChooseSpecificationActivity.this, rootBean.ResponseMsg, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    /**
     * 添加到购物车
     */
    private void AddCart() {
        Map<String, String> params = new HashMap<>();
        params.put("SkuId", skuId);
        params.put("Count", number + "");
        params.put("UserId", userId);
        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBSAL_ADD_CART)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        RootBean rootBean = new Gson().fromJson(response, RootBean.class);
                        if (Integer.parseInt(rootBean.ErrCode) == 0) {
                            //弹出对话框提示加入购物车成功
                            Toast.makeText(ChooseSpecificationActivity.this, getResources().getString(R.string.add_cart_succeed), Toast.LENGTH_SHORT).show();
                            getNetShoppingCartNumber();
                            finish();
                            overridePendingTransition(R.anim.dialog_exit, 0);
                        } else {
                            Toast.makeText(ChooseSpecificationActivity.this, getResources().getString(R.string.add_cart_failure), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * 获取购物车商品数量
     */
    private void getNetShoppingCartNumber() {
        String userId = CacheUtils.getString(this, GlobalContent.USER);
        OkHttpUtils
                .get()
                .url(GlobalContent.GLOBSAL_GETCSRT_LIST)
                .addParams("UserId", userId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        RootBean rootBean = new Gson().fromJson(response, RootBean.class);

                        if (rootBean.ErrCode.equals("0")) {
                            CartBean cartBean = new Gson().fromJson(rootBean.Data, CartBean.class);
                            getCartNumber(cartBean);
                        }
                    }
                });
    }

    /**
     * 计算购物车商品数量
     */
    private void getCartNumber(CartBean cartBean) {
        List<CartBean.CartItemsBean> cartItems = cartBean.getCartItems();
        int number = 0;
        for (int i = 0; i < cartItems.size(); i++) {
            for (int j = 0; j < cartItems.get(i).getCartItems().size(); j++) {
                number++;
            }
        }
        CacheUtils.putString(ChooseSpecificationActivity.this, GlobalContent.CART_NUMBER, String.valueOf(number));
        sendCartNumberChangeBroadcast();
    }

    /**
     * 发送购物车商品数量改变的广播
     */
    private void sendCartNumberChangeBroadcast() {
        Intent intent = new Intent("com.BFMe.BFMBuyer.SHOPPING_CART_NUMBER");
        sendBroadcast(intent);
    }


    /**
     * 初始化颜色和规格
     */
    private void initSizeAndColorData() {
        if (productDetailBean.getColor().size() <= 0) {
            tv_color.setVisibility(View.GONE);
        } else {
            tv_color.setVisibility(View.VISIBLE);
        }
        if (productDetailBean.getSize().size() <= 0) {
            tv_size.setVisibility(View.GONE);
        } else {
            tv_size.setVisibility(View.VISIBLE);
        }
        mapColor = new HashMap<>();
        colorSource = new ArrayList<>();

        for (int i = 0; i < productDetailBean.getColor().size(); i++) {
            colorSource.add(productDetailBean.getColor().get(i).getValue());
            mapColor.put(productDetailBean.getColor().get(i).getValue(), productDetailBean.getColor().get(i).getId());
        }

        mapSize = new HashMap<>();
        sizeSource = new ArrayList<>();
        for (int i = 0; i < productDetailBean.getSize().size(); i++) {
            sizeSource.add(productDetailBean.getSize().get(i).getValue());
            mapSize.put(productDetailBean.getSize().get(i).getValue(), productDetailBean.getSize().get(i).getId());
        }

        Logger.e("颜色=="+mapColor.toString());
        Logger.e("尺寸=="+mapSize.toString());

    }

    /**
     * 获取商品的库存信息
     */
    private void getProducetSKU() {
        Map<String, String> params = new HashMap<>();
        params.put("SkuId",skuId);
        Logger.e("SKUId=="+skuId);
        OkHttpUtils
                .get()
                .url(GlobalContent.GLOBSAL_PRODUCT_SKU)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                        getProducetSKU();
                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("SKU信息=="+response);
                        RootBean rootBean = new Gson().fromJson(response, RootBean.class);
                        if(Integer.parseInt(rootBean.ErrCode) == 0){
                           setSKUData(rootBean);
                        }else{
                            Toast.makeText(ChooseSpecificationActivity.this,rootBean.ResponseMsg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setSKUData(RootBean rootBean) {
        ProducetSUKBean producetSkuBean = new Gson().fromJson(rootBean.Data, ProducetSUKBean.class);
        maxSaleCount1 = Integer.parseInt(producetSkuBean.getStock());
        if (maxSaleCount1>0) {

            String salePrice = producetSkuBean.getSalePrice();
            tvTotal.setText(salePrice);
            tvSku.setText(String.format(getResources().getString(R.string.sku_number),maxSaleCount1));

            btnQuickBuy.setEnabled(true);
            btnQuickBuy.setTextColor(getResources().getColor(R.color.white));
            btnQuickBuy.setBackgroundResource(R.drawable.shape_circle_range1);

            btnAddCart.setEnabled(true);
            btnAddCart.setTextColor(getResources().getColor(R.color.colorZhu));
            btnAddCart.setBackgroundResource(R.drawable.shape_circle_range2);

            btnConfirm.setBackgroundResource(R.drawable.btnbg_blue);
            btnConfirm.setEnabled(true);
        } else {
            tvTotal.setText(productDetailBean.getPrice());
            tvSku.setText(String.format(getResources().getString(R.string.sku_number),0));
            btnConfirm.setEnabled(false);
            btnConfirm.setBackgroundResource(R.color.app_body_bg);

            btnQuickBuy.setEnabled(false);
            btnQuickBuy.setTextColor(getResources().getColor(R.color.gray));
            btnQuickBuy.setBackgroundResource(R.drawable.shape_circle_range3);

            btnAddCart.setEnabled(false);
            btnAddCart.setTextColor(getResources().getColor(R.color.gray));
            btnAddCart.setBackgroundResource(R.drawable.shape_circle_range3);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(R.anim.dialog_exit, 0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
