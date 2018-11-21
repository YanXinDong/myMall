package com.BFMe.BFMBuyer.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.javaBean.CartBean;
import com.BFMe.BFMBuyer.javaBean.ProducetSUKBean;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.utils.CacheUtils;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.Utils;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.BFMe.BFMBuyer.view.GlideCircleTransform;
import com.BFMe.BFMBuyer.view.SlideLayout;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.BFMe.BFMBuyer.application.MyApplication.getContext;

/**
 * Description: 购物车的listview
 * Copyright  : Copyright (c) 2016
 * Company    : 白富美(北京)网络科技有限公司
 * Author     : 闫信董
 * Date       : 2016/8/10 14:03
 */
public class CartAdapter extends BaseExpandableListAdapter {

    private Context ctx;

    private ArrayList<CartBean.CartItemsBean> mCartItemsList = new ArrayList();
    //是否是编辑状态
    private HashMap<Integer, String> isEdit = new HashMap<>();

    private final String EDIT = "edit";//当前是编辑状态
    private final String DEFAULT = "default";//当前是默认状态
    private String state = DEFAULT;//默认为默认

    private TextView mTvCartPriceAll;//总价
    private CheckBox mCbCheckAll;

    //存贮商品sku信息的集合 key-- skuid  volley-- 最大库存
    private HashMap<String, String> mSKUInfo = new HashMap<>();
    //存储商品数量的结合  key-- skuid  volley-- 实时数量
    private HashMap<String, Integer> mCount = new HashMap<>();

    public CartAdapter(Context ctx, CartBean cartItemsDatas, TextView tvCartPriceAll, CheckBox cbCheckAll) {
        this.ctx = ctx;
        if (cartItemsDatas != null && cartItemsDatas.getCartItems().size() > 0) {
            mCartItemsList.clear();
            mCartItemsList.addAll(cartItemsDatas.getCartItems());
        }
        mTvCartPriceAll = tvCartPriceAll;
        mCbCheckAll = cbCheckAll;

    }

    //父item的数据大小
    @Override
    public int getGroupCount() {
        return mCartItemsList.size();
    }

    //子item的数据大小
    @Override
    public int getChildrenCount(int groupPosition) {
        return mCartItemsList.get(groupPosition).getCartItems().size();
    }

    //获取当前父item的数据
    @Override
    public Object getGroup(int groupPosition) {
        return mCartItemsList.get(groupPosition);
    }

    //获取当前子item须要关联的数据
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mCartItemsList.get(groupPosition).getCartItems().get(childPosition);
    }

    //父item的id
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //子item的id
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    //设置父item组件
    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(ctx, R.layout.list_item_shopping_cart_shop, null);
            holder = new ViewHolder();

            holder.rl_shopping_cart_shop = (RelativeLayout) convertView.findViewById(R.id.rl_shopping_cart_shop);
            holder.cbCheckhop = (CheckBox) convertView.findViewById(R.id.cb_check_shop);
            holder.ivUserCartPhoto = (ImageView) convertView.findViewById(R.id.iv_user_cart_photo);
            holder.tvCartShopName = (TextView) convertView.findViewById(R.id.tv_cart_shop_name);
            holder.tvEdit = (TextView) convertView.findViewById(R.id.tv_edit);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Glide//店铺的头像
                .with(ctx)
                .load(mCartItemsList.get(groupPosition).getShopLogo())
                .transform(new GlideCircleTransform(ctx))
                .placeholder(R.drawable.zhanwei3)
                .error(R.drawable.zwyj4)
                .into(holder.ivUserCartPhoto);
        //店铺名称
        holder.tvCartShopName.setText(mCartItemsList.get(groupPosition).getShopName());
        if (DEFAULT.equals(isEdit.get(groupPosition))) {
            //编辑按钮
            holder.tvEdit.setText(ctx.getString(R.string.edit));
        }

        //是否选中
        if (groupPosition < mCartItemsList.size()) {
            holder.cbCheckhop.setChecked(mCartItemsList.get(groupPosition).isChecked());
        }

        holder.rl_shopping_cart_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListenerCart.onShopClick(String.valueOf(mCartItemsList.get(groupPosition).getShopId()));
            }
        });
        //编辑按钮的点击事件监听
        holder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state.equals(EDIT)) {//此时是编辑状态
                    //改成默认状态
                    state = DEFAULT;
                    holder.tvEdit.setText(ctx.getString(R.string.edit));
                    //改变item的是否选中
                    changeItem(groupPosition, mCartItemsList.get(groupPosition).isChecked());
                } else {//此时是默认状态
                    //改成编辑状态
                    state = EDIT;
                    holder.tvEdit.setText(ctx.getString(R.string.finish));
                    //改变item的是否选中
                    changeItem(groupPosition, false);
                }
                isEdit.put(groupPosition, state);
                //显示价格
                showTotalPrice();
                //效验本item是否全部选中
                checkItemAll(groupPosition);
                //效验是否全选
                checkAll();
                onClickListenerEdit.onEditClick(groupPosition);
                notifyDataSetChanged();
            }
        });

        //选中按钮的监听
        holder.cbCheckhop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCartItemsList.get(groupPosition).setChecked(!mCartItemsList.get(groupPosition).isChecked());
                //改变item的是否选中
                changeItem(groupPosition, mCartItemsList.get(groupPosition).isChecked());
                onClickListenerEdit.onEditClick(groupPosition);
                //显示价格
                showTotalPrice();
                //效验是否全选
                checkAll();
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    /**
     * 效验item的选中
     *
     * @param groupPosition
     * @param isxuan
     */
    private void changeItem(int groupPosition, boolean isxuan) {
        for (int i = 0; i < mCartItemsList.get(groupPosition).getCartItems().size(); i++) {
            mCartItemsList.get(groupPosition).getCartItems().get(i).setChecked(isxuan);
        }
    }

    //设置子item组件
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        //给每个item加载自布局
        ViewHolder2 holder2 = null;
        if (convertView == null) {

            convertView = View.inflate(ctx, R.layout.list_item_shopping_cart_delete, null);
            holder2 = new ViewHolder2();

            holder2.tv_item_content = convertView.findViewById(R.id.tv_item_content);

            holder2.cbCheck = (CheckBox) convertView.findViewById(R.id.cb_check);
            holder2.ivCartIcon = (ImageView) convertView.findViewById(R.id.iv_cart_icon);
            holder2.tvCartName = (TextView) convertView.findViewById(R.id.tv_cart_name);
            holder2.tvCartSize = (TextView) convertView.findViewById(R.id.tv_cart_size);
            holder2.tvCartPrice = (TextView) convertView.findViewById(R.id.tv_cart_price);
            holder2.tvCartNumber = (TextView) convertView.findViewById(R.id.tv_cart_number);
            holder2.item_bg = convertView.findViewById(R.id.item_bg);

            holder2.tv_delete = (TextView) convertView.findViewById(R.id.tv_delete);
            holder2.rl_add_sub = (RelativeLayout) convertView.findViewById(R.id.rl_add_sub);
            holder2.tv_buy_number = (TextView) convertView.findViewById(R.id.tv_buy_number);
            holder2.iv_reduce = (ImageView) convertView.findViewById(R.id.iv_reduce);
            holder2.iv_add = (ImageView) convertView.findViewById(R.id.iv_add);

            holder2.tv_item_delete = (TextView) convertView.findViewById(R.id.tv_item_delete);
            convertView.setTag(holder2);
        } else {
            holder2 = (ViewHolder2) convertView.getTag();
        }
        final CartBean.CartItemsBean.CartSonItemsBean data = mCartItemsList.get(groupPosition).getCartItems().get(childPosition);

        Glide//商品缩略图
                .with(ctx)
                .load(data.getImgUrl())
                .centerCrop()
                .placeholder(R.drawable.zhanwei1)
                .error(R.drawable.zhanwei1)
                .into(holder2.ivCartIcon);
        holder2.tvCartName.setText(data.getName());//商品名称
        holder2.tvCartSize.setText(ctx.getString(R.string.color_size) + data.getColor() + "  " + data.getSize());//商品规格
        holder2.tvCartPrice.setText(parent.getContext().getResources().getString(R.string.money_type) + data.getPrice());//商品价格

        if (mCount.get(data.getSkuId()) != null) {
            holder2.tvCartNumber.setText("*" + mCount.get(data.getSkuId()));//商品数量
        } else {
            holder2.tvCartNumber.setText("*" + data.getCount());
        }
        holder2.tv_buy_number.setText(data.getCount() + "");

        //item的分割线
        if (childPosition == mCartItemsList.get(groupPosition).getCartItems().size() - 1) {
            holder2.item_bg.setVisibility(View.VISIBLE);
        } else {
            holder2.item_bg.setVisibility(View.GONE);
        }
        SlideLayout slideLayout = (SlideLayout) convertView;

        holder2.tv_item_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListenerCart.onCommodityClick(String.valueOf(data.getProductId())
                        , String.valueOf(mCartItemsList.get(groupPosition).getShopId()));
            }
        });
        //减号的点击事件监听
        final ViewHolder2 finalHolder = holder2;
        holder2.iv_reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCount.get(data.getSkuId()) == null) {
                    return;
                } else if (mCount.get(data.getSkuId()) <= 1) {
                    return;
                }

                Integer oldNumber = mCount.get(data.getSkuId());
                Integer newNumber = --oldNumber;

                mCount.put(data.getSkuId(), newNumber);
                finalHolder.tv_buy_number.setText(newNumber + "");
            }
        });

        //加号的点击事件监听
        final ViewHolder2 finalHolder1 = holder2;
        holder2.iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCount.get(data.getSkuId()) == null) {
                    return;
                }
                Integer oldNumber = mCount.get(data.getSkuId());
                Integer newNumber = ++oldNumber;
                //判断是否大于库存
                if (newNumber > Integer.parseInt(mSKUInfo.get(data.getSkuId()))) {
                    showSkuDialag();
                    return;
                }
                mCount.put(data.getSkuId(), newNumber);
                finalHolder1.tv_buy_number.setText(newNumber + "");
            }
        });

        //编辑时的删除按钮的点击事件监听
        holder2.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //改成默认状态
                state = DEFAULT;
                isEdit.put(groupPosition, state);
                onClickListenerDelete.onDeleteClick(data.getSkuId(), groupPosition, childPosition);

            }
        });
        //侧滑时的删除按钮点击事件监听
        holder2.tv_item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListenerDelete.onDeleteClick(data.getSkuId(), groupPosition, childPosition);
                SlideLayout slideLayout = (SlideLayout) v.getParent().getParent();
                slideLayout.closeMenu();//关闭当前打开的item
            }
        });
        //商品列表是否是选中
        holder2.cbCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.setChecked(!data.isChecked());
                //显示价格
                showTotalPrice();
                //效验本item是否全部选中
                checkItemAll(groupPosition);
                //效验是否选中
                checkAll();
                notifyDataSetChanged();
                onClickListenerEdit.onEditClick(groupPosition);
            }
        });
        //选中状态
        holder2.cbCheck.setChecked(data.isChecked());

        //判断是否是编辑状态
        if (EDIT.equals(isEdit.get(groupPosition))) {//编辑状态
            //如果是编辑状态的话 隐藏商品详情  显示编辑框
            holder2.tvCartName.setVisibility(View.GONE);
            holder2.tvCartSize.setVisibility(View.GONE);
            holder2.tvCartPrice.setVisibility(View.GONE);
            holder2.tvCartNumber.setVisibility(View.GONE);

            holder2.rl_add_sub.setVisibility(View.VISIBLE);
            holder2.tv_delete.setVisibility(View.VISIBLE);

            String number = holder2.tv_buy_number.getText().toString().trim();
            mCount.put(data.getSkuId(), Integer.parseInt(number));

            slideLayout.closeMenu();//关闭当前打开的item
            //获取sku信息
            getSKUInfo(data.getSkuId());

            slideLayout.setIsIntercept(new SlideLayout.IsIntercept() {
                @Override
                public boolean isIntercept() {
                    return true;
                }
            });
        } else {

            if (mCount.get(data.getSkuId()) != null) {
                //效验是否需要更改
                efficacyNumber(mCount.get(data.getSkuId()), data, groupPosition);
            }
            //默认状态 显示商品详情  隐藏编辑框
            holder2.tvCartName.setVisibility(View.VISIBLE);
            holder2.tvCartSize.setVisibility(View.VISIBLE);
            holder2.tvCartPrice.setVisibility(View.VISIBLE);
            holder2.tvCartNumber.setVisibility(View.VISIBLE);

            holder2.rl_add_sub.setVisibility(View.GONE);
            holder2.tv_delete.setVisibility(View.GONE);

            slideLayout.setIsIntercept(new SlideLayout.IsIntercept() {
                @Override
                public boolean isIntercept() {
                    return false;
                }
            });
        }

        //给所有的item设置监听
        slideLayout.setOnStateChangeListener(onStateChangeListener);

        return convertView;
    }

    /**
     * 显示大于库存的提示对话框
     */
    private void showSkuDialag() {
        AlertDialog.Builder skuDialog = new AlertDialog.Builder(ctx);
        skuDialog.setMessage(ctx.getString(R.string.sku_max_hint));
        skuDialog.setCancelable(false);
        skuDialog.setPositiveButton(ctx.getString(R.string.confirm),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        skuDialog.show();
    }

    /**
     * 获取商品的skuinfo
     *
     * @param skuId
     */
    private void getSKUInfo(final String skuId) {
        Logger.e("skuid"+skuId);
        OkHttpUtils
                .get()
                .url(GlobalContent.GLOBSAL_PRODUCT_SKU)
                .addParams("SkuId", skuId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                        Logger.e("商品库存信息数据失败" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("商品库存信息数据" + response);
                        Gson gson = new Gson();
                        RootBean rootBean = gson.fromJson(response, RootBean.class);
                        if (Integer.parseInt(rootBean.ErrCode) == 0) {
                            mSKUInfo.put(skuId, gson.fromJson(rootBean.Data, ProducetSUKBean.class).getStock());
                        }
                    }
                });
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return true;
    }

    /**
     * 效验数量是否需要更改
     *
     * @param newNumber
     * @param data
     * @param groupPosition
     */
    private void efficacyNumber(int newNumber, CartBean.CartItemsBean.CartSonItemsBean data, int groupPosition) {
        if (newNumber != data.getCount()) {
            //更改商品数量
            changeNumber(newNumber + "", data, groupPosition);
        }
    }

    /**
     * 更改商品的数量
     *
     * @param newNumber
     * @param data
     * @param groupPosition
     */
    private void changeNumber(String newNumber, CartBean.CartItemsBean.CartSonItemsBean data, final int groupPosition) {

        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBSAL_CHANGE_NUMBER)
                .addParams("UserId", CacheUtils.getString(ctx, GlobalContent.USER))
                .addParams("Count", newNumber)
                .addParams("SkuId", data.getSkuId())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                        Logger.e( "更改数量onError（）" + e.toString());
                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e( "更改数量onResponse()" + response);
                        RootBean rootBean = new Gson().fromJson(response, RootBean.class);
                        if (Integer.parseInt(rootBean.ErrCode) == 0) {
                            onClickListenerEdit.onRefresh(groupPosition);
                            mCount.clear();
                        }
                    }
                });
    }

    /**
     * 定义接口实现店铺跟商品的详情跳转
     */
    public interface OnClickListenerCart {
        void onShopClick(String shopId);

        void onCommodityClick(String id, String shopId);
    }

    private OnClickListenerCart onClickListenerCart;

    public void setOnClickListenerCart(OnClickListenerCart onClickListenerCart) {
        this.onClickListenerCart = onClickListenerCart;
    }

    /**
     * 定义接口实现删除
     */
    public interface OnClickListenerDelete {
        void onDeleteClick(String skuId, int groupPosition, int childPosition);
    }

    private OnClickListenerDelete onClickListenerDelete;

    public void setOnClickListenerDelete(OnClickListenerDelete onClickListenerDelete) {
        this.onClickListenerDelete = onClickListenerDelete;
    }

    /**
     * 定义接口实现编辑状态的切换
     */
    public interface OnClickListenerEdit {
        void onEditClick(int groupPosition);

        void onRefresh(int groupPosition);
    }

    private OnClickListenerEdit onClickListenerEdit;

    public void setOnClickListenerEdit(OnClickListenerEdit onClickListenerEdit) {
        this.onClickListenerEdit = onClickListenerEdit;
    }

    //滑动删除
    private SlideLayout openedLayout;
    private SlideLayout.OnStateChangeListener onStateChangeListener = new SlideLayout.OnStateChangeListener() {

        @Override
        public void onOpen(SlideLayout layout) {
            openedLayout = layout; //保存打开的item
        }

        @Override
        public void onClose(SlideLayout layout) {
            if (layout == openedLayout) {
                openedLayout = null; //将保存Item置空
            }
        }

        @Override
        public void onDown(SlideLayout layout) {
            if (openedLayout != null && layout != openedLayout) { //如果按下不是打开的item
                openedLayout.closeMenu(); //将其关闭
            }
        }

    };

    class ViewHolder {

        private RelativeLayout rl_shopping_cart_shop;//店铺
        private CheckBox cbCheckhop;//选中按钮
        private ImageView ivUserCartPhoto;//店铺头像
        private TextView tvCartShopName;//店铺名称
        private TextView tvEdit;//编辑按钮
    }

    private class ViewHolder2 {
        private CheckBox cbCheck;  //选择
        private ImageView ivCartIcon;    //购物车图标
        private TextView tvCartName;       //购物车宝贝名称
        private TextView tvCartSize;//商品规格，有的没有
        private TextView tvCartPrice;//商品价格
        private TextView tvCartNumber;//单个商品数量
        private View item_bg;

        private TextView tv_delete;  //侧滑删除
        private RelativeLayout rl_add_sub;//数量编辑
        private TextView tv_buy_number;//加减控件数量
        private ImageView iv_reduce;//减号控件
        private ImageView iv_add;//加号控件

        private View tv_item_content;
        private TextView tv_item_delete;
    }

    /**
     * 效验item全选状态
     *
     * @param groupPosition
     */
    public void checkItemAll(int groupPosition) {

        int number = 0;
        if (mCartItemsList != null
                && mCartItemsList.size() > 0
                && mCartItemsList.get(groupPosition).getCartItems() != null
                && mCartItemsList.get(groupPosition).getCartItems().size() > 0) {

            for (int i = 0; i < mCartItemsList.get(groupPosition).getCartItems().size(); i++) {

                CartBean.CartItemsBean.CartSonItemsBean cartSonItemsBean = mCartItemsList.get(groupPosition).getCartItems().get(i);

                if (cartSonItemsBean.isChecked()) {//选中
                    number += 1;
                } else {
                    mCartItemsList.get(groupPosition).setChecked(false);
                }
            }
            if (number == mCartItemsList.get(groupPosition).getCartItems().size()) {
                mCartItemsList.get(groupPosition).setChecked(true);
            }
        }
    }

    /**
     * 效验全选状态
     */
    public void checkAll() {

        int number = 0;
        if (mCartItemsList != null && mCartItemsList.size() > 0) {

            for (int i = 0; i < mCartItemsList.size(); i++) {
                CartBean.CartItemsBean cartItemsBean = mCartItemsList.get(i);
                if (cartItemsBean.isChecked()) {//选中
                    number += 1;
                } else {
                    mCbCheckAll.setChecked(false);
                }
            }
            if (number == mCartItemsList.size()) {
                mCbCheckAll.setChecked(true);
            }
        }
    }

    /**
     * 选中商品的skuid
     */
    public String totalSkuid() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mCartItemsList.size(); i++) {
            CartBean.CartItemsBean cartItemsBean = mCartItemsList.get(i);
            List<CartBean.CartItemsBean.CartSonItemsBean> cartItems = cartItemsBean.getCartItems();
            for (int j = 0; j < cartItems.size(); j++) {
                if (cartItems.get(j).isChecked()) {
                    String skuId = cartItems.get(j).getSkuId();
                    sb.append(skuId).append(",");
                }
            }
        }
        String s = sb.toString();
        if (TextUtils.isEmpty(s)) {
            return "";
        } else {
            return s.substring(0, s.length() - 1);
        }

    }

    /**
     * 显示商品价格
     */
    public void showTotalPrice() {
        mTvCartPriceAll.setText(getContext().getResources().getString(R.string.money_type) + Utils.doubleSave2(getTotalPrice()));
    }


    public double getTotalPrice() {
        double num = 0;

        if (mCartItemsList != null && mCartItemsList.size() > 0) {

            for (int i = 0; i < mCartItemsList.size(); i++) {
                CartBean.CartItemsBean cartItemsBean = mCartItemsList.get(i);

                if (i < mCartItemsList.size()) {

                    for (int j = 0; j < cartItemsBean.getCartItems().size(); j++) {

                        CartBean.CartItemsBean.CartSonItemsBean cartSonItemsBean = cartItemsBean.getCartItems().get(j);

                        if (cartSonItemsBean != null) {
                            //判断是否是选中
                            if (cartSonItemsBean.isChecked()) {
                                //选中
                                num = num + cartSonItemsBean.getPrice() * cartSonItemsBean.getCount();
                            }
                        }
                    }
                }
            }
        }
        return num;

    }

    /**
     * 添加数据
     *
     * @param datas
     * @param position
     */
    public void addData(int position, List<CartBean.CartItemsBean> datas) {
        if (datas != null && datas.size() > 0) {
            mCartItemsList.addAll(position, datas);
            notifyDataSetChanged();
        }
    }


    public void addData(List<CartBean.CartItemsBean> datas) {
        addData(0, datas);
    }

    /**
     * 清楚数据
     */
    public void cleanData() {
        mCartItemsList.clear();
        notifyDataSetChanged();
    }


    /**
     * 更新选中商品集合
     *
     * @param groupPosition
     * @param childPosition
     */
    public void deleteCheckeds(int groupPosition, int childPosition) {

        if (mCartItemsList != null && mCartItemsList.size() > 0) {
            //删除对应的数据
            mCartItemsList.get(groupPosition).getCartItems().remove(childPosition);
            //如果父结合没有数据  就移除父集合
            if (!(mCartItemsList.get(groupPosition).getCartItems().size() > 0)
                    || mCartItemsList.get(groupPosition).getCartItems() == null) {
                mCartItemsList.remove(groupPosition);
            }
        }
    }


    public ArrayList<CartBean.CartItemsBean> returnDatas() {
        return mCartItemsList;
    }
}
