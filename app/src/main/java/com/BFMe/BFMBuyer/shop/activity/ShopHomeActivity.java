package com.BFMe.BFMBuyer.shop.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.activity.LoginActivity;
import com.BFMe.BFMBuyer.activity.ShoppingCartActivity;
import com.BFMe.BFMBuyer.base.NoTitleBaseActivity;
import com.BFMe.BFMBuyer.base.recycle.CommonAdapter;
import com.BFMe.BFMBuyer.base.recycle.base.ViewHolder;
import com.BFMe.BFMBuyer.commodity.activity.ProducetDetailsActivity;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.javaBean.ShopDetailBean;
import com.BFMe.BFMBuyer.javaBean.ShopListBean;
import com.BFMe.BFMBuyer.javaBean.StringBase;
import com.BFMe.BFMBuyer.search.activity.SearchShopActivity;
import com.BFMe.BFMBuyer.share.SelectShareCategoryActivity;
import com.BFMe.BFMBuyer.shop.adapter.ShopHomeListAdapter;
import com.BFMe.BFMBuyer.utils.CacheUtils;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.UiUtils;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.BFMe.BFMBuyer.view.GlideCircleTransform;
import com.bumptech.glide.Glide;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.mob.MobSDK;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.BFMe.BFMBuyer.R.id.iv_title_right;
import static com.BFMe.BFMBuyer.R.id.tv_attention;
import static com.bumptech.glide.Glide.with;

/**
 * 店铺页面
 */
public class ShopHomeActivity extends NoTitleBaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_banner)
    ImageView ivBanner;
    @BindView(R.id.iv_banner_hint)
    ImageView ivBannerHint;
    @BindView(R.id.iv_user_photo)
    ImageView ivUserPhoto;
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.cb_attention)
    CheckBox cbAttention;
    @BindView(R.id.tv_commodity)
    TextView tvCommodity;
    @BindView(R.id.tv_page_view)
    TextView tvPageView;
    @BindView(tv_attention)
    TextView tvAttention;
    @BindView(R.id.tv_shop_interduce)
    TextView tvShopInfo;
    @BindView(R.id.iv_quality_yes)
    ImageView ivQualityYes;
    @BindView(R.id.iv_bestbuyer_yes)
    ImageView ivBestbuyerYes;
    @BindView(R.id.iv_identity_yes)
    ImageView ivIdentityYes;
    @BindView(R.id.iv_ensure_yes)
    ImageView ivEnsureYes;
    @BindView(R.id.rb_recommend)
    RadioButton rbRecommend;
    @BindView(R.id.rb_new)
    RadioButton rbNew;
    @BindView(R.id.rb_hot)
    RadioButton rbHot;
    @BindView(R.id.rb_price)
    RadioButton rbPrice;
    @BindView(R.id.ll_content_s2)
    LinearLayout llContentS2;
    @BindView(R.id.tv_cart_number)
    TextView tvCartNumber;

    private ShopHomeListAdapter mAdapter;

    @OnClick(R.id.iv_back)
    void goBack() {
        finish();
        exitAnim();
    }

    @OnClick(iv_title_right)
    void goToChart() {
        if (isLogin) {
            startActivity(new Intent(this, ShoppingCartActivity.class));
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        startAnim();
    }

    @OnClick(R.id.tv_search_circle)
    void goSearch() {
        Intent intenSearch = new Intent(this, SearchShopActivity.class);
        intenSearch.putExtra(GlobalContent.SHOP_ID, mShopId);
        startActivity(intenSearch);
        startAnim();
    }

    @OnClick(R.id.iv_share)
    void share() {
        MobSDK.init(this, "1b462d46b8900", "514cfad75c1832fd14fb3b7ce97cba9d");
        Intent intent = new Intent(this, SelectShareCategoryActivity.class);
        intent.putExtra("shareTitle", shareTitle);
        intent.putExtra("shareContent", shareContent);
        intent.putExtra("shareImageUrl", shareImageUrl);
        intent.putExtra("shareUrl", shareUrl);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.anim_no);
    }


    @BindView(R.id.rv_shop_home_list)
    XRecyclerView mRVShopHomeList;

    private String shareTitle;
    private String shareContent;
    private String shareImageUrl;
    private String shareUrl;

    private String mShopId;
    private int pageNo = 1;//记载的页数
    private int pageSize = 10;//一页的数据
    private int searchType = 4;
    private ShopDetailBean.GuaranteeSellerBean mGuaranteeSeller;
    private boolean isAttention;//是否关注

    private Boolean isHighPrice = false;//此字段是控制价格排序是由高到低还是由低到高

    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFRESH = 1;
    private static final int STATE_MORE = 2;

    private int state = STATE_NORMAL;//默认为正常状态

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isTranslucentStatusBar = false;
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    private void initData() {

        mRVShopHomeList.setFootViewText(getString(R.string.load), getString(R.string.no_more_data));
        mRVShopHomeList.setPullRefreshEnabled(false);
        Intent intent = getIntent();
        mShopId = intent.getStringExtra("shopId");
        //获取店铺信息
        initShopInfo();
        getNetData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String mCartNumber = CacheUtils.getString(this, GlobalContent.CART_NUMBER);

        if (isLogin && !mCartNumber.equals("0")) {
            tvCartNumber.setVisibility(View.VISIBLE);
            tvCartNumber.setText(mCartNumber);
        } else {
            tvCartNumber.setVisibility(View.GONE);
        }
    }

    /**
     * 店铺信息
     */
    private void initShopInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("shopId", mShopId);
        params.put("userId", mUserId);
        OkHttpUtils
                .get()
                .url(GlobalContent.GLOBSAL_SHOP_INFO)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("店铺详情" + response);
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (Integer.parseInt(rootBean.ErrCode) == 0) {
                            String data = rootBean.Data;
                            ShopDetailBean shopDetailBean = mGson.fromJson(data, ShopDetailBean.class);
                            SettingShopDetail(shopDetailBean);
                        }else {
                            showToast(rootBean.ResponseMsg);
                        }
                    }
                });
    }

    /**
     * 设置数据
     */
    private void SettingShopDetail(ShopDetailBean shopDetailBean) {

        tvShopName.setText(shopDetailBean.getShopName()); //店铺名称
        shareTitle = shopDetailBean.getShopName();
        shareContent = shopDetailBean.getDescription();
        shareImageUrl = shopDetailBean.getLogo();
        shareUrl = shopDetailBean.getShareLink();
        mGuaranteeSeller = shopDetailBean.getGuaranteeSeller();

        if (isLogin) {
            if (shopDetailBean.isIsAttention()) {
                //关注了
                cbAttention.setText(getString(R.string.attention_ture));
            } else {
                //没关注
                cbAttention.setText(getString(R.string.add_attention));
            }
        } else {
            //没关注
            cbAttention.setText(getString(R.string.add_attention));
        }

        tvCommodity.setText(shopDetailBean.getProducCount() + "\n"+getResources().getString(R.string.product_total));//商品总数据
        tvAttention.setText(shopDetailBean.getNumOfFavor() + "\n"+getResources().getString(R.string.notice_total)); //关注总数
        tvPageView.setText(shopDetailBean.getThisMonthViewNum() + "\n"+getResources().getString(R.string.look_total)); //浏览总数

        if (TextUtils.isEmpty(shopDetailBean.getDescription())) {
            tvShopInfo.setText(getString(R.string.shop_description_empt_hint));
        } else {
            tvShopInfo.setText(shopDetailBean.getDescription());//店铺描述
        }

        //判断是否关注
        isAttention = shopDetailBean.isIsAttention();

        //100%正品
        if (shopDetailBean.isIsQualityGoods()) {
            ivQualityYes.setImageResource(R.drawable.shopnner_100_lighting);
        } else {
            ivQualityYes.setImageResource(R.drawable.shopnner_100_notlighting);
        }

        //最佳买手
        if (shopDetailBean.isIsBestSeller()) {
            ivBestbuyerYes.setImageResource(R.drawable.shopnner_bestseller_lighting);
        } else {
            ivBestbuyerYes.setImageResource(R.drawable.shopnner_bestseller_notlighting);
        }

        //身份验证
        if (shopDetailBean.isIsIdentityVerification()) {
            ivIdentityYes.setImageResource(R.drawable.shopnner_yanzheng_lighting);
        } else {
            ivIdentityYes.setImageResource(R.drawable.shopnner_yanzheng_notlighting);
        }

        //保障商家
        if (shopDetailBean.isIsGuaranteeSeller()) {
            ivEnsureYes.setImageResource(R.drawable.shopnner_baozhang_lighting);
        } else {
            ivEnsureYes.setImageResource(R.drawable.shopnner_baozhang_notlighting);
        }

        //正品货源保证
        if (shopDetailBean.isIsGuaranteeGoods()) {
            ivBannerHint.setBackgroundResource(R.drawable.shop_head_1);
        } else {
            ivBannerHint.setBackgroundResource(R.drawable.shop_head_2);
        }

        with(getApplicationContext())
                .load(shopDetailBean.getBaseLogo())
                .placeholder(R.drawable.shop_background)
                .error(R.drawable.shop_background)
                .centerCrop()
                .into(ivBanner);

        //商家头像
        Glide
                .with(getApplicationContext())
                .load(shopDetailBean.getLogo())
                .transform(new GlideCircleTransform(this))
                .placeholder(R.drawable.zwyj4)
                .into(ivUserPhoto);
    }

    private void initView() {
        rbRecommend.setOnClickListener(this);
        rbNew.setOnClickListener(this);
        rbHot.setOnClickListener(this);
        rbPrice.setOnClickListener(this);
        llContentS2.setOnClickListener(this);
        cbAttention.setOnClickListener(this);
    }

    /**
     * 联网获取店铺商品信息
     */
    private void getNetData() {
        showProgress();
        HashMap<String, String> map = new HashMap<>();
        map.put("ShopId", mShopId);
        map.put("PageNo", pageNo + "");
        map.put("PageSize", pageSize + "");
        map.put("Sort", searchType + "");
        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBSAL_SHOP_PRODUCTS)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                        dismissProgress();
                        Logger.e("店铺列表" + e);
                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("店铺列表" + response);
                        dismissProgress();
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (Integer.parseInt(rootBean.ErrCode) == 0) {
                            String data = rootBean.Data;
                            ShopListBean shopListBean = mGson.fromJson(data, ShopListBean.class);
                            //显示店铺商品列表
                            showShopCommodity(shopListBean);
                        }
                    }
                });
    }

    private void showShopCommodity(ShopListBean shopListBean) {
        List<ShopListBean.ProductsBean> products = shopListBean.getProducts();
        if(products.size()>=shopListBean.getTotal()){
            mRVShopHomeList.setNoMore(true);
        }
        switch (state) {
            //正常状态
            case STATE_NORMAL:
                mRVShopHomeList.setLayoutManager(new GridLayoutManager(this, 2));
                mAdapter = new ShopHomeListAdapter(this, R.layout.grid_item_subject, products);
                mRVShopHomeList.setAdapter(mAdapter);
                setListener();
                break;
            case STATE_REFRESH:
                mAdapter.cleanData();
                mAdapter.addData(shopListBean.getProducts());
                break;
            case STATE_MORE:
                state = STATE_NORMAL;
                mRVShopHomeList.loadMoreComplete();
                if(shopListBean.getProducts().size() == 0) {
                    mRVShopHomeList.setNoMore(true);
                    return;
                }
                if (mAdapter.getItemCount() >= shopListBean.getTotal()) {
                    showToast(getResources().getString(R.string.no_more_data1));
                    return;
                }
                mAdapter.addData(mAdapter.getItemCount(), shopListBean.getProducts());
                break;
        }


    }

    private void setListener() {
        mAdapter.setOnItemClickListener(new ShopHomeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String productId, String shopId) {
                Intent intent = new Intent(ShopHomeActivity.this, ProducetDetailsActivity.class);
                intent.putExtra("productId", productId);
                intent.putExtra("ShopId", shopId);
                startActivity(intent);
                startAnim();
            }
        });

        mRVShopHomeList.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                pageNo++;
                state = STATE_MORE;
                getNetData();
            }
        });
    }

    @Override
    public int initLayout() {
        return R.layout.activity_shop_home;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_recommend:
                rbPrice.setCompoundDrawables(null, null, showDown(), null);
                searchType = 4;
                refresh();
                break;
            case R.id.rb_new:
                rbPrice.setCompoundDrawables(null, null, showDown(), null);
                searchType = 5;
                refresh();
                break;
            case R.id.rb_hot:
                rbPrice.setCompoundDrawables(null, null, showDown(), null);
                searchType = 1;
                refresh();
                break;
            case R.id.rb_price:
                if (isHighPrice) {
                    searchType = 2;
                    rbPrice.setCompoundDrawables(null, null, showUpChecked(), null);
                    isHighPrice = false;
                } else {
                    searchType = 3;
                    rbPrice.setCompoundDrawables(null, null, showDownChecked(), null);
                    isHighPrice = true;
                }
                refresh();
                break;
            case R.id.ll_content_s2://四盏大灯。。。
                if (mGuaranteeSeller != null) {
                    showHintPPW();
                    UiUtils.setScreenBgDarken(this);//屏幕变暗
                }
                break;
            case R.id.cb_attention:
                attentionShop();
                break;

        }
    }

    private void attentionShop() {
        if (isLogin) {
            String url;
            if (isAttention) {//此时是已经关注
                cbAttention.setText(getString(R.string.add_attention));
                //取消关注
                url = GlobalContent.GLOBAL_CANCEL_ATTENTION_SHOP;
            } else {
                //关注店铺
                cbAttention.setText(getString(R.string.attention_ture));
                url = GlobalContent.GLOBAL_ATTENTION_SHOP;
            }
            isAttention = !isAttention;
            cancelOrAttentionShop(url);
        } else {
            cbAttention.setChecked(false);
            startActivity(new Intent(ShopHomeActivity.this, LoginActivity.class));
            startAnim();
        }

    }
    /**
     * 更改后台关注数据
     */
    private void cancelOrAttentionShop(String url) {
        HashMap<String, String> map = new HashMap<>();
        map.put("shopId", mShopId);
        map.put("UserId", mUserId);
        Logger.e("更改关注数据=="+map.toString());
        Logger.e("url=="+url);
        OkHttpUtils
                .post()
                .url(url)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("关注" + response);
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if(rootBean.ErrCode.equals("0")) {
                            initShopInfo();
                        }
                    }
                });
    }

    /**
     * 页面刷新
     */
    private void refresh() {
        pageNo = 1;
        state = STATE_NORMAL;
        getNetData();
    }

    private void showHintPPW() {

        View v = LayoutInflater.from(this).inflate(R.layout.hint_ppw, null, false);
        PopupWindow popupWindow = new PopupWindow(v, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.style.AddressDialogAnim);
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框。不知道是什么原因
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击窗口外边窗口消失
        popupWindow.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(findViewById(R.id.coor_layout), Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, 0);
        //添加pop窗口关闭事件
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                UiUtils.setScreenBgLight(ShopHomeActivity.this);//屏幕变亮
            }
        });
        initPPView(v, popupWindow);
    }

    private void initPPView(View v, final PopupWindow popupWindow) {
        RecyclerView rv_mContent = (RecyclerView) v.findViewById(R.id.rv_content);
        ImageButton ibBlack = (ImageButton) v.findViewById(R.id.ib_black);

        rv_mContent.setLayoutManager(new LinearLayoutManager(this));
        String hwjk = mGuaranteeSeller.getHWJK();
        String zymj = mGuaranteeSeller.getZYMJ();
        ShopDetailBean.GuaranteeSellerBean.SFYZBean sfyzs = mGuaranteeSeller.getSFYZ();
        String sfyz = sfyzs.getDefaultWords();

        if (null != sfyzs.getName() && !sfyzs.getName().isEmpty()) {//公司名称
            sfyz += getString(R.string.sfyz_company_name) + sfyzs.getName();
        }
        if (null != sfyzs.getAddress() && !sfyzs.getAddress().isEmpty()) {//地址
            sfyz += getString(R.string.sfyz_company_address) + sfyzs.getAddress();
        }
        if (null != sfyzs.getPhone() && !sfyzs.getPhone().isEmpty()) {//电话
            sfyz += getString(R.string.sfyz_company_phone) + sfyzs.getPhone();
        }
        if (null != sfyzs.getContacts() && !sfyzs.getContacts().isEmpty()) {//联系人
            sfyz += getString(R.string.sfyz_company_contacts) + sfyzs.getContacts();
        }

        String bzmj = mGuaranteeSeller.getBZMJ();

        List<StringBase> data = new ArrayList<>();
        data.add(new StringBase(getString(R.string.quality_yes), hwjk));
        data.add(new StringBase(getString(R.string.bestbuyer_yes), zymj));
        data.add(new StringBase(getString(R.string.identity_yes), sfyz));
        data.add(new StringBase(getString(R.string.ensure_yes), bzmj));

        CommonAdapter<StringBase> commonAdapter = new CommonAdapter<StringBase>(this, R.layout.item_shop_hint, data) {
            @Override
            protected void convert(ViewHolder holder, StringBase stringBase, int position) {
                holder.setText(R.id.tv_title, stringBase.getTitle());
                holder.setText(R.id.tv_content, stringBase.getContent());
            }
        };
        rv_mContent.setAdapter(commonAdapter);

        ibBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    private Drawable showDown() {
        Drawable drawable = getResources().getDrawable(R.drawable.down);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }

    private Drawable showDownChecked() {
        Drawable drawable = getResources().getDrawable(R.drawable.down_checked);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }

    private Drawable showUpChecked() {
        Drawable drawable = getResources().getDrawable(R.drawable.up_checked);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }
}
