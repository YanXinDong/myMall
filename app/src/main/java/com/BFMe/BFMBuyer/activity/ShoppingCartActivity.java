package com.BFMe.BFMBuyer.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.adapter.CartAdapter;
import com.BFMe.BFMBuyer.application.MyApplication;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.commodity.activity.ProducetDetailsActivity;
import com.BFMe.BFMBuyer.javaBean.CartBean;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.main.MainActivity;
import com.BFMe.BFMBuyer.shop.activity.ShopHomeActivity;
import com.BFMe.BFMBuyer.utils.CacheUtils;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 购物车
 */
public class ShoppingCartActivity extends IBaseActivity implements View.OnClickListener {

    private ExpandableListView lvList;
    private TextView tvCartPriceAll;
    private CheckBox cbCheckAll;
    private RelativeLayout rl_hint_empty_cart;//购物车无商品提示布局
    private LinearLayout ll_shopping_cart_buttom;
    private String userId;
    private CartAdapter mCartAdapter;
    private List<CartBean.CartItemsBean> mCartItems;

    private final String REFRESH = "refresh";
    private final String DEFAULT = "default";
    private String start = DEFAULT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        cbCheckAll.setChecked(false);
        initData();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_shopping_cart;
    }

    /**
     * 点击事件的监听
     */
    private void initListener() {
        if (mCartAdapter != null) {

            //编辑按钮的监听
            mCartAdapter.setOnClickListenerEdit(new CartAdapter.OnClickListenerEdit() {
                @Override
                public void onEditClick(int groupPosition) {
                    refresh(groupPosition);
                }

                @Override
                public void onRefresh(int groupPosition) {
                    start = REFRESH;//切换状态
                    initData();
                }
            });
            mCartAdapter.setOnClickListenerDelete(new CartAdapter.OnClickListenerDelete() {

                @Override
                public void onDeleteClick(String skuId, int groupPosition, int childPosition) {
                    showDeleteDialog(skuId, groupPosition, childPosition);
                }
            });
            //全选按钮的监听
            cbCheckAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<CartBean.CartItemsBean> cartCheckeditemses = mCartAdapter.returnDatas();
                    if (cartCheckeditemses != null && cartCheckeditemses.size() > 0) {

                        for (int i = 0; i < cartCheckeditemses.size(); i++) {
                            CartBean.CartItemsBean cartItemsBean = cartCheckeditemses.get(i);
                            cartItemsBean.setChecked(cbCheckAll.isChecked());//选中所有店铺

                            for (int j = 0; j < cartItemsBean.getCartItems().size(); j++) {
                                cartItemsBean.getCartItems().get(j).setChecked(cbCheckAll.isChecked());//选中所有商品
                            }
                        }
                    }
                    //显示价格
                    mCartAdapter.showTotalPrice();
                    //更新适配器
                    mCartAdapter.notifyDataSetChanged();
                }
            });

            mCartAdapter.setOnClickListenerCart(new CartAdapter.OnClickListenerCart() {
                @Override
                public void onShopClick(String shopId) {
                    Intent intentShop = new Intent(ShoppingCartActivity.this, ShopHomeActivity.class);
                    intentShop.putExtra("shopId", shopId);
                    startActivity(intentShop);
                    startAnim();
                }

                @Override
                public void onCommodityClick(String id, String shopId) {
                    Intent intent = new Intent(ShoppingCartActivity.this, ProducetDetailsActivity.class);
                    intent.putExtra("productId", id);
                    intent.putExtra("ShopId", shopId);
                    startActivity(intent);
                    startAnim();
                }
            });
        }
    }

    /**
     * 删除商品的对的话框
     *
     * @param skuId
     * @param groupPosition
     * @param childPosition
     */
    private void showDeleteDialog(final String skuId, final int groupPosition, final int childPosition) {
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(this);
        deleteDialog.setMessage(getString(R.string.delete_product_hint));
        deleteDialog.setCancelable(false);
        deleteDialog.setPositiveButton(getString(R.string.commit),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //删除
                        deleteCartproduct(skuId, groupPosition, childPosition);
                    }
                });

        deleteDialog.setNegativeButton(getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        deleteDialog.show();
    }

    //刷新方法
    private void refresh(int groupPosition) {
        lvList.collapseGroup(groupPosition);
        lvList.expandGroup(groupPosition);

        mCartAdapter.checkItemAll(groupPosition);

    }

    /**
     * 获取购物车列表:
     */
    private void initData() {

        showProgress();
        userId = CacheUtils.getString(this, GlobalContent.USER);
        OkHttpUtils
                .get()
                .url(GlobalContent.GLOBSAL_GETCSRT_LIST)
                .addParams("UserId",userId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                        Logger.e("购物车列表数据异常" + e.getMessage());
                        dismissProgress();
                    }

                    @Override
                    public void onResponse(String response) {
                        dismissProgress();
                        Logger.e("购物车列表数据" + response);
                        Gson gson = new Gson();
                        RootBean rootBean = gson.fromJson(response, RootBean.class);
                        String data = rootBean.Data;
                        if (Integer.parseInt(rootBean.ErrCode) == 0) {
                            CartBean cartBean = gson.fromJson(data, CartBean.class);
                            mCartItems = cartBean.getCartItems();
                            if (mCartItems == null || mCartItems.size() <= 0) {
                                //显示没有商品提示
                                rl_hint_empty_cart.setVisibility(View.VISIBLE);
                                ll_shopping_cart_buttom.setVisibility(View.GONE);
                            } else {
                                rl_hint_empty_cart.setVisibility(View.GONE);
                                ll_shopping_cart_buttom.setVisibility(View.VISIBLE);
                            }
                            parseCartDatas(cartBean);
                        }
                    }
                });
    }

    /**
     * 设置UI数据
     *
     * @param cartBean
     */
    private void parseCartDatas(CartBean cartBean) {

        if (start.equals(DEFAULT)) {//默认
            mCartAdapter = new CartAdapter(this, cartBean, tvCartPriceAll, cbCheckAll);
            lvList.setAdapter(mCartAdapter);
            //去除箭头
            lvList.setGroupIndicator(null);
            //默认全部展开
            for (int i = 0; i < mCartAdapter.getGroupCount(); i++) {
                lvList.expandGroup(i);
            }
            //不能点击收缩
            lvList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                @Override
                public boolean onGroupClick(ExpandableListView parent, View v,
                                            int groupPosition, long id) {
                    // TODO Auto-generated method stub
                    return true;
                }
            });
        } else if (start.equals(REFRESH)) {//更新
            //把原来的清除
            mCartAdapter.cleanData();
            //适配器重新添加数据
            mCartAdapter.addData(cartBean.getCartItems());
            for (int i = 0; i < cartBean.getCartItems().size(); i++) {
                refresh(i);
            }
            mCartAdapter.checkAll();
        }

        //监听方法
        initListener();
        //显示价格
        if (mCartAdapter != null) {
            mCartAdapter.showTotalPrice();
        }

        //计算购物车商品数量
        getCartNumber(cartBean);
    }

    /**
     * 计算购物车商品数量
     *
     * @param cartBean 购物车数据
     */
    private void getCartNumber(CartBean cartBean) {
        List<CartBean.CartItemsBean> cartItems = cartBean.getCartItems();
        int number = 0;
        for (int i = 0; i < cartItems.size(); i++) {
            for (int j = 0; j < cartItems.get(i).getCartItems().size(); j++) {
                number++;
            }
        }
        CacheUtils.putString(ShoppingCartActivity.this, GlobalContent.CART_NUMBER, String.valueOf(number));
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
     * 根据skuid删除购物车数据
     *
     * @param skuId
     * @param groupPosition
     * @param childPosition
     */
    private void deleteCartproduct(final String skuId, final int groupPosition, final int childPosition) {
        showProgress(getString(R.string.deleting));
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("UserId", userId);
        map.put("SkuId", skuId);
        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBAl_DELETE_CART)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                        dismissProgress();
                    }

                    @Override
                    public void onResponse(String response) {
                        dismissProgress();
                        Gson gson = new Gson();
                        RootBean rootBean = gson.fromJson(response, RootBean.class);
                        String responseMsg = rootBean.ResponseMsg;
                        if (responseMsg.equals("success")) {
                            start = REFRESH;//切换状态
                            //移除对应的选中集合数据
                            mCartAdapter.deleteCheckeds(groupPosition, childPosition);
                            initData();
                            showToast(getString(R.string.delete_succeed));
                        } else {
                            showToast(getString(R.string.delete_failure));
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //结算按钮
            case R.id.btn_cart_accaount:
                Intent intent = new Intent(this, AccountActivity.class);
                String skus = mCartAdapter.totalSkuid();
                if (TextUtils.isEmpty(skus)) {
                    Toast.makeText(ShoppingCartActivity.this, getString(R.string.select_product_empt_hint), Toast.LENGTH_SHORT).show();
                    return;
                }
                intent.putExtra("totalSkuid", skus);
                startActivity(intent);
                startAnim();
                break;
            case R.id.btn_empty_shopping:
                //去购物按钮
                CacheUtils.putBoolean(ShoppingCartActivity.this, "IsFromCar", true);
                Intent intentMainActivity = new Intent(ShoppingCartActivity.this, MainActivity.class);
                startActivity(intentMainActivity);
                MyApplication.getInstance().exitHome();
                finish();
                startAnim();
                break;
            default:
                break;
        }
    }

    /**
     * 初始化布局
     */
    private void initView() {
        setBackButtonVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.shopping_cart));
        lvList = (ExpandableListView) findViewById(R.id.lv_list);
        //全选
        cbCheckAll = (CheckBox) findViewById(R.id.cb_check_all);
        //总价
        tvCartPriceAll = (TextView) findViewById(R.id.tv_cart_price_all);
        //结算
        Button btnCartAccount = (Button) findViewById(R.id.btn_cart_accaount);
        //提示去购物的布局
        rl_hint_empty_cart = (RelativeLayout) findViewById(R.id.rl_hint_empty_cart);
        //去购物按钮
        Button btn_empty_shopping = (Button) findViewById(R.id.btn_empty_shopping);
        btn_empty_shopping.setOnClickListener(this);

        ll_shopping_cart_buttom = (LinearLayout) findViewById(R.id.ll_shopping_cart_buttom);

        btnCartAccount.setOnClickListener(this);
    }
}
