package com.BFMe.BFMBuyer.commodity.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.activity.ChooseSpecificationActivity;
import com.BFMe.BFMBuyer.activity.CommentProductsActivity;
import com.BFMe.BFMBuyer.activity.LoginActivity;
import com.BFMe.BFMBuyer.adapter.DetailIconAdapter;
import com.BFMe.BFMBuyer.application.MyApplication;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.base.recycle.CommonAdapter;
import com.BFMe.BFMBuyer.base.recycle.base.ViewHolder;
import com.BFMe.BFMBuyer.javaBean.IfICanBuyBean;
import com.BFMe.BFMBuyer.javaBean.LimitDetailsBean;
import com.BFMe.BFMBuyer.javaBean.ProducetImgTextBean;
import com.BFMe.BFMBuyer.javaBean.ProductCommentBean;
import com.BFMe.BFMBuyer.javaBean.ProductDetailBean;
import com.BFMe.BFMBuyer.javaBean.RecommendCommoditiesBean;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.javaBean.ShopDetailBean;
import com.BFMe.BFMBuyer.shop.activity.ShopHomeActivity;
import com.BFMe.BFMBuyer.utils.CacheUtils;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.LimitTimeUtils;
import com.BFMe.BFMBuyer.utils.StringUtils;
import com.BFMe.BFMBuyer.utils.TimeUtils;
import com.BFMe.BFMBuyer.utils.UiUtils;
import com.BFMe.BFMBuyer.utils.Utils;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.BFMe.BFMBuyer.view.GlideCircleTransform;
import com.BFMe.BFMBuyer.view.RatingBar;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.session.activity.P2PMessageActivity;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.iwgang.countdownview.CountdownView;
import okhttp3.Request;

/**
 * 商品详情页
 */
public class ProducetDetailsActivity extends IBaseActivity implements View.OnClickListener {

    private TextView tv_freight;//运费
    private TextView tvDetaliName;
    private TextView tvPriceNormal;
    private TextView tvPriceHight;
    private TextView tvFax;
    private TextView tvSendMethod;
    private ImageView ivUserPhoto;
    private TextView tvShopName;
    private Button ibNotice;
    private TextView tvLookNum;
    private ImageView ivSafeGoods;
    private ImageView ivBestGoods;
    private ImageView ivIdentity;
    private ImageView ivEnsureShop;
    private Button btnAddCart;
    private String productId;
    private String shopId;
    private ImageView ivUserPhoto1;
    private TextView tvTime;
    private TextView tvComment;
    private TextView tvUserName;
    private com.BFMe.BFMBuyer.view.RatingBar ll_start1;
    private TextView tvPingjia;
    private LinearLayout llComent;
    private String userId;
    private ShopDetailBean shopDetailBean;

    private WebView wb_producet_img_text;
    private boolean isAttention;
    private ViewPager vp_detail_icon;
    private LinearLayout ll_producet_dot;
    private TextView tv_no_evaluate;
    private CountdownView count_down;
    private LinearLayout ll_menu;
    private TextView tv_is_off;

    private ArrayList<ImageView> imageViews;
    private ProductDetailBean productDetailBean;
    private String marketPrice;
    private String minSalePrice1;
    private boolean isLimitBuy;

    private int isAddCart = 0;//0：加入购物车 1：立即购买 2：点击的选择颜色和规格
    private LimitDetailsBean limitDetailsBean;
    private Boolean isNetSuccess1 = false, isNetSuccess2 = false, isNetSuccess3 = false;
    private RelativeLayout rl_pingjia;
    private RelativeLayout rl_choose_color_size;
    private ImageView iv_relation;
    private TextView ibListArrow;
    private Button btnQuickBuy;
    private ImageView ivAttentionShop;
    private ImageView ivAttentionProduct;
    private Boolean isCollectProduct;
    private ImageView iv_bso;
    private TextView tv_bso;
    private int limitId;

    private RecyclerView rv_guess_you_like;
    private TextView tv_details_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化组件
        initView();

        Intent intent = getIntent();
        productId = intent.getStringExtra("productId");
        shopId = intent.getStringExtra("ShopId");
        //初始化数据
        initData();
        setListener();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_producet_details;
    }


    /**
     * 设置viewpager的监听
     */
    private void setListener() {

        vp_detail_icon.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < ll_producet_dot.getChildCount(); i++) {
                    ImageView childAt = (ImageView) ll_producet_dot.getChildAt(i);
                    if (i == position % imageViews.size()) {
                        childAt.setImageResource(R.drawable.active);
                    } else {
                        childAt.setImageResource(R.drawable.inactive);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        userId = CacheUtils.getString(this, GlobalContent.USER);

        //商品详情
        getProducetDatas();
        //获取商品评价
        getNetEvaluateData();
        //图文详情
        getNetImgTextData();
        //猜你喜欢
        getRecommendCommodities();
    }

    /**
     * 根据商品Id获取推荐商品
     */
    private void getRecommendCommodities() {
        Map<String, String> params = new HashMap<>();
        params.put("ProductId", productId);
        params.put("PageNo", "1");
        params.put("PageSize", "10");

        OkHttpUtils
                .get()
                .params(params)
                .url(GlobalContent.GET_SEARCH_PRODUCTS_BY_THREECATEGORY)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("猜你喜欢===" + response);
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (rootBean.ErrCode.equals("0")) {
                            RecommendCommoditiesBean data = mGson.fromJson(rootBean.Data, RecommendCommoditiesBean.class);
                            showRecommendCommoditiesData(data.getData().getProducts());
                        } else {
                            showToast(rootBean.ResponseMsg);
                        }
                    }
                });
    }

    private void showRecommendCommoditiesData(List<RecommendCommoditiesBean.DataBean.ProductsBean> products) {
        rv_guess_you_like.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        CommonAdapter<RecommendCommoditiesBean.DataBean.ProductsBean> adapter
                = new CommonAdapter<RecommendCommoditiesBean.DataBean.ProductsBean>
                (this, R.layout.item_recommend_commodities, products) {

            @Override
            protected void convert(ViewHolder holder, final RecommendCommoditiesBean.DataBean.ProductsBean data, int position) {
                holder.setImageGlide(R.id.iv_icon, data.getImagePath(), 1);
                holder.setText(R.id.tv_name, data.getProductName());
                holder.setText(R.id.tv_price, MyApplication.getContext().getResources().getString(R.string.money_type) + Utils.doubleSave2(data.getMinSalePrice()));

                holder.setOnClickListener(R.id.rl_product_info, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ProducetDetailsActivity.this, ProducetDetailsActivity.class);
                        intent.putExtra("productId", data.getId());
                        startActivity(intent);
                        startAnim();
                    }
                });
            }
        };
        rv_guess_you_like.setAdapter(adapter);
    }


    /**
     * 商品详情
     */
    private void getProducetDatas() {

        Logger.e("商品详情请求参数==" + productId+"   "+userId);

        showProgress();
        OkHttpUtils
                .get()
                .url(GlobalContent.GLOBSAL_PRODUCT_DETAILS)
                .addParams("ProductId", productId)
                .addParams("UserId", userId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("商品详情==" + response);
                        dismissProgress();
                        ParseData(response);
                        getNetShopData();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取店铺详情
        if (shopId != null) {
            getNetShopData();
        }
    }

    /**
     * 获取限时限购物品的详情
     */
    private void getLimitData() {
        //商品详情
        userId = CacheUtils.getString(this, GlobalContent.USER);

        Map<String, String> params = new HashMap<>();
        params.put("LimitId", String.valueOf(limitId));
        params.put("UserId", userId);
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
                        Logger.e("获取限时购详情onResponse==" + response);
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        String data = rootBean.Data;
                        if (0 == Integer.parseInt(rootBean.ErrCode)) {
                            isNetSuccess3 = true;
                            limitDetailsBean = new Gson().fromJson(data, LimitDetailsBean.class);
                            marketPrice = Utils.doubleSave2(limitDetailsBean.getData().getMarketPrice());
                            minSalePrice1 = Utils.doubleSave2(limitDetailsBean.getData().getMinSalePrice());

                            String startTime = limitDetailsBean.getData().getStartTime();
                            String endTime = limitDetailsBean.getData().getEndTime();
                            String newStartTime = TimeUtils.StringToDate(startTime, "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss");
                            String newEndTime = TimeUtils.StringToDate(endTime, "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss");
                            String time = LimitTimeUtils.getTime(newStartTime, newEndTime);
                            String timeCategory = time.substring(0, 1);
                            String countDown = time.substring(1);
                            if (timeCategory.equals("2")) {
                                count_down.start(Long.parseLong(countDown));
                            } else {
                                count_down.setVisibility(View.GONE);
                            }
                            tvPriceNormal.setText(MyApplication.getContext().getResources().getString(R.string.money_type) + marketPrice);
                            tvPriceHight.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                            tvPriceHight.setText(MyApplication.getContext().getResources().getString(R.string.money_type) + minSalePrice1);
                        }
                    }
                });
    }

    /**
     * 获取商品评价
     */
    private void getNetEvaluateData() {
        String urlcomm = GlobalContent.GLOBSAL_COMMENT + "?ProductId=" + productId + "&PageNo=1&PageSize=1&Level=0";
        OkHttpUtils
                .get()
                .url(urlcomm)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("商品评价===" + response);
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        String data = rootBean.Data;
                        ProductCommentBean productCommentBean = mGson.fromJson(data, ProductCommentBean.class);
                        setData(productCommentBean);
                    }
                });
    }

    private void setData(ProductCommentBean productCommentBean) {

        String total = productCommentBean.Total;
        if (total.equals("0")) {
            rl_pingjia.setVisibility(View.GONE);
        } else {
            rl_pingjia.setVisibility(View.VISIBLE);
            tvPingjia.setText(String.format(getString(R.string.comment_person_number), total));
        }

        ArrayList<ProductCommentBean.CommentsData> commentsList = productCommentBean.Comments;
        if (!commentsList.isEmpty()) {
            ProductCommentBean.CommentsData commentsData = commentsList.get(0);
            tvUserName.setText(commentsData.UserName);
            tvTime.setText(commentsData.ReviewDate);
            tvComment.setText(commentsData.ReviewContent);
            Glide
                    .with(getApplicationContext())
                    .load(commentsData.UserPhoto)
                    .transform(new GlideCircleTransform(ProducetDetailsActivity.this))
                    .placeholder(R.drawable.zhanwei3)
                    .error(R.drawable.zhanwei3)
                    .into(ivUserPhoto1);

            ll_start1.setStar(Float.parseFloat(commentsData.ReviewMark));
        } else {
            llComent.setVisibility(View.GONE);
            tv_no_evaluate.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取店铺详情
     */
    private void getNetShopData() {
        OkHttpUtils
                .get()
                .url(GlobalContent.GLOBSAL_SHOP_INFO)
                .addParams("shopId", shopId)
                .addParams("userId", userId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        RootBean rootBean = gson.fromJson(response, RootBean.class);
                        String data = rootBean.Data;
                        shopDetailBean = gson.fromJson(data, ShopDetailBean.class);
                        if (shopDetailBean != null) {
                            showShopData();
                        }
                    }
                });
    }

    /**
     * 显示店铺详情数据
     */
    private void showShopData() {
        Glide
                .with(getApplicationContext())
                .load(shopDetailBean.getLogo())
                .transform(new GlideCircleTransform(ProducetDetailsActivity.this))
                .placeholder(R.drawable.zwyj4)
                .into(ivUserPhoto);

        //店铺名称
        tvShopName.setText(shopDetailBean.getShopName());

        isAttention = shopDetailBean.isIsAttention();

        if (isLogin) {
            //关注ibNotice图标
            if (shopDetailBean.isIsAttention()) {
                //关注了
                ibNotice.setBackgroundResource(R.drawable.btn_bg_white_frame_gray);
                ibNotice.setText(getString(R.string.attention_ture));
                ibNotice.setTextColor(getResources().getColor(R.color.color_grey_999999));
            } else {
                //没关注
                ibNotice.setBackgroundResource(R.drawable.btn_bg_white_frame_blue);
                ibNotice.setText(getString(R.string.add_attention));
                ibNotice.setTextColor(getResources().getColor(R.color.blue_035de9));
            }
        } else {
            //没关注
            ibNotice.setBackgroundResource(R.drawable.btn_bg_white_frame_blue);
            ibNotice.setText(getString(R.string.add_attention));
            ibNotice.setTextColor(getResources().getColor(R.color.blue_035de9));
        }

        //关注数量
        tvLookNum.setText(shopDetailBean.getNumOfFavor());

        //100%正品
        if (shopDetailBean.isIsQualityGoods()) {
            ivSafeGoods.setImageResource(R.drawable.shopnner_100_lighting);
        } else {
            ivSafeGoods.setImageResource(R.drawable.shopnner_100_notlighting);
        }

        //最佳买手
        if (shopDetailBean.isIsBestSeller()) {
            ivBestGoods.setImageResource(R.drawable.shopnner_bestseller_lighting);
        } else {
            ivBestGoods.setImageResource(R.drawable.shopnner_bestseller_notlighting);
        }

        //身份验证
        if (shopDetailBean.isIsIdentityVerification()) {
            ivIdentity.setImageResource(R.drawable.shopnner_yanzheng_lighting);
        } else {
            ivIdentity.setImageResource(R.drawable.shopnner_yanzheng_notlighting);
        }

        //保障商家
        if (shopDetailBean.isIsGuaranteeSeller()) {
            ivEnsureShop.setImageResource(R.drawable.shopnner_baozhang_lighting);
        } else {
            ivEnsureShop.setImageResource(R.drawable.shopnner_baozhang_notlighting);
        }

        //正品货源保证
        if (shopDetailBean.isIsGuaranteeGoods()) {
            iv_bso.setVisibility(View.VISIBLE);
            tv_bso.setVisibility(View.VISIBLE);
        } else {
            iv_bso.setVisibility(View.GONE);
            tv_bso.setVisibility(View.GONE);
        }
    }

    /**
     * 联网获取图文详情
     */
    private void getNetImgTextData() {

        OkHttpUtils
                .get()
                .url(GlobalContent.GLOBSAL_PRODUCT_IMG_TEXT)
                .addParams("ProductId", productId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("图文详情===" + response);

                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if(rootBean.ErrCode.equals("0")){
                            ProducetImgTextBean producetImgTextBean = mGson.fromJson(rootBean.Data, ProducetImgTextBean.class);
                            showWebView(producetImgTextBean);
                        }

                    }
                });

    }

    private void showWebView(ProducetImgTextBean producetImgTextBean) {

        wb_producet_img_text.setWebViewClient(new WebViewClient());
        wb_producet_img_text.getSettings().setDefaultTextEncodingName("utf-8");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            wb_producet_img_text.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            wb_producet_img_text.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
        wb_producet_img_text.loadData(getHtmlData(producetImgTextBean.getProductDescription()), "text/html; charset=utf-8", "utf-8");


    }

    private String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

    /**
     * 商品详情解析json
     *
     * @param response
     */
    public void ParseData(String response) {

        RootBean rootBean = mGson.fromJson(response, RootBean.class);

        if (Integer.parseInt(rootBean.ErrCode) == 0) {
            isNetSuccess1 = true;
            if(!TextUtils.isEmpty(rootBean.Data)){
                showData(rootBean);
            }

            if (isLimitBuy) {//如果是限时购商品 加入购物车按钮禁用 收藏按钮隐藏 获取限时购价格
                ivAttentionProduct.setVisibility(View.GONE);
                btnAddCart.setVisibility(View.INVISIBLE);
                count_down.setVisibility(View.VISIBLE);
                getLimitData();
            }
        }
    }

    /**
     * 添加小圆点
     */
    private void createImageView() {
        imageViews = new ArrayList<>();
        for (int i = 0; i < productDetailBean.getProduct().getImagePath().size(); i++) {
            ImageView circle = new ImageView(ProducetDetailsActivity.this);
            if (i == 0) {
                circle.setImageResource(R.drawable.active);
            } else {
                circle.setImageResource(R.drawable.inactive);
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            if (i > 0) {
                params.leftMargin = UiUtils.px2dp(30);
            }
            circle.setLayoutParams(params);
            ll_producet_dot.addView(circle);
        }
    }

    /**
     * 显示数据
     *
     * @param rootBean
     */
    private void showData(RootBean rootBean) {
        String data = rootBean.Data;
        productDetailBean = new Gson().fromJson(data, ProductDetailBean.class);

        shopId = productDetailBean.getShopId();
        //价格
        String price = productDetailBean.getPrice();
        //税费
        String tax = Utils.doubleSave2(productDetailBean.getTax());
        //isLimitBuy 是否是限时购商品
        isLimitBuy = productDetailBean.isLimitBuy();
        limitId = productDetailBean.getLimitId();
        //是否官方自营
//        String isSellerAdminProdcut = productDetailBean.isIsSellerAdminProdcut();
        ProductDetailBean.ProductBean products = productDetailBean.getProduct();
        ProductDetailBean.SendMethodBean sendMethod = productDetailBean.getSendMethod();

        //是否已下架
        int isOff = productDetailBean.getIsOff();
        if (isOff == 1) {//出售中
            ll_menu.setVisibility(View.VISIBLE);
            tv_is_off.setVisibility(View.GONE);
        } else {//已下架
            ll_menu.setVisibility(View.GONE);
            tv_is_off.setVisibility(View.VISIBLE);
        }
        //分享字段赋值
        shareTitle = products.getProductName();
        shareContent = getString(R.string.share_product_content);
        if (products.getImagePath().size() > 0) {
            shareImageUrl = products.getImagePath().get(0);
        } else {
            shareImageUrl = "";
        }
        shareUrl = products.getShareLink();

        createImageView();
        //创建图片集合
        for (int i = 0; i < products.getImagePath().size(); i++) {
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageViews.add(imageView);
        }
        //显示商品icon
        vp_detail_icon.setAdapter(new DetailIconAdapter(ProducetDetailsActivity.this, products, imageViews));
        //字体加粗
        TextPaint tp = tvDetaliName.getPaint();
        tp.setFakeBoldText(true);
        tvDetaliName.setText(products.getProductName());
        if (!StringUtils.isSpace(products.getShortDescription())) {
            tv_details_message.setVisibility(View.VISIBLE);
            tv_details_message.setText(products.getShortDescription());
        } else {
            tv_details_message.setVisibility(View.GONE);
        }
        tvPriceNormal.setText(MyApplication.getContext().getResources().getString(R.string.money_type) + price);
        tvPriceHight.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tvPriceHight.setText(MyApplication.getContext().getResources().getString(R.string.money_type) + Utils.doubleSave2(products.getMarketPrice()));

        tvFax.setText(MyApplication.getContext().getResources().getString(R.string.money_type) +  tax);
        //运费
        tv_freight.setText(productDetailBean.getFreightStr());
        tvSendMethod.setText(getString(R.string.distribution_way) + sendMethod.getSendName());

        if (productDetailBean.getStockCount() == 0) {
            btnAddCart.setVisibility(View.INVISIBLE);
        }
        isCollectProduct = productDetailBean.getProduct().isIsFavorite();
        if (productDetailBean.getProduct().isIsFavorite()) {
            ivAttentionProduct.setBackgroundResource(R.drawable.attention_product1);
        } else {
            ivAttentionProduct.setBackgroundResource(R.drawable.attention_product0);
        }
    }


    /**
     * 选择规格和数量
     */
    private void setNumberAndSpecification() {
        Intent chooseIntent = new Intent(ProducetDetailsActivity.this, ChooseSpecificationActivity.class);
        chooseIntent.putExtra("isAddCart", isAddCart);
        chooseIntent.putExtra("productId", productId);
        Bundle bundle = new Bundle();
        bundle.putSerializable("productDetailBean", productDetailBean);
        bundle.putSerializable("limitDetailsBean", limitDetailsBean);
        chooseIntent.putExtras(bundle);
        startActivity(chooseIntent);
        overridePendingTransition(0, R.anim.dialog_enter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_list_arrow://商品评价
                Intent intent = new Intent(ProducetDetailsActivity.this, CommentProductsActivity.class);
                intent.putExtra("productId", productId);
                startActivity(intent);
                startAnim();
                break;
            case R.id.btn_shop:
            case R.id.iv_attention_shop:
            case R.id.iv_user_photo://店铺详情
                Intent intentPhoto = new Intent(ProducetDetailsActivity.this, ShopHomeActivity.class);
                if (shopId != null) {
                    intentPhoto.putExtra("shopId", shopId);
                }
                startActivity(intentPhoto);
                startAnim();
                break;
            case R.id.btn_add_cart:     //加入购物车
                isAddCart = 0;
                chooseColorAndSize();
                break;
            case R.id.btn_quick_buy://立即购买
                isAddCart = 1;
                chooseColorAndSize();
                break;
            case R.id.ib_notice://关注图标的监听

                if (isLogin) {
                    String url;
                    if (isAttention) {//此时是已经关注
                        url = GlobalContent.GLOBAL_CANCEL_ATTENTION_SHOP;
                    } else {
                        url = GlobalContent.GLOBAL_ATTENTION_SHOP;
                    }
                    isAttention = !isAttention;
                    cancelOrAttentionShop(url);
                } else {
                    startActivity(new Intent(ProducetDetailsActivity.this, LoginActivity.class));
                    startAnim();
                }
                break;
            case R.id.btn_relation_shop:
            case R.id.iv_relation:
                if (isLogin) {
                    if (shopDetailBean != null) {
                        P2PMessageActivity.IMFlag = true;
                        P2PMessageActivity.IMUserName = shopDetailBean.getShopName();
                        P2PMessageActivity.shopDetailImg =productDetailBean.getProduct().getImagePath().get(0);
                        P2PMessageActivity.shopDetailDes = productDetailBean.getProduct().getProductName();
                        P2PMessageActivity.shopDetailPrice = productDetailBean.getPrice()+"";
                        NimUIKit.startChatting(this, shopDetailBean.getSellerIMID(), SessionTypeEnum.P2P, null, null);
                    }
                } else {
                    startActivity(new Intent(ProducetDetailsActivity.this, LoginActivity.class));
                    startAnim();
                    overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
                }

                break;
            case R.id.rl_choose_color_size:
                isAddCart = 2;
                chooseColorAndSize();
                break;
            case R.id.iv_attention_product:

                if (isLogin) {
                    if (isCollectProduct) {
                        isCollectProduct = false;
                        cancelCollectProduct();
                    } else {
                        isCollectProduct = true;
                        setCollectProduct();
                    }
                } else {
                    startActivity(new Intent(mContext, LoginActivity.class));
                    startAnim();
                }

                break;
            default:
                break;
        }
    }

    /**
     * 取消收藏的商品
     */
    private void cancelCollectProduct() {
        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBAL_CANCEL_COLLECT_PRODUCT)
                .addParams("userId", userId)
                .addParams("productId", productId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                    }

                    @Override
                    public void onResponse(String response) {
                        ivAttentionProduct.setBackgroundResource(R.drawable.attention_product0);
                    }
                });
    }

    private void setCollectProduct() {
        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBAL_COLLECT_PRODUCT)
                .addParams("userId", userId)
                .addParams("productId", productId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                    }

                    @Override
                    public void onResponse(String response) {
                        ivAttentionProduct.setBackgroundResource(R.drawable.attention_product1);
                    }
                });

    }

    /**
     * 选择颜色和规格的跳转
     */
    private void chooseColorAndSize() {
        if (!isNetSuccess1) {
            return;
        }
        if (isLimitBuy && !isNetSuccess3) {
            return;
        }
        if (isLogin) {
            ifICanBuy();
        } else {
            startActivity(new Intent(ProducetDetailsActivity.this, LoginActivity.class));
            startAnim();
        }
    }

    //判断当前用户能否购买此商品
    private void ifICanBuy() {
        if (!isLimitBuy) {//非限时购商品不用判断
            setNumberAndSpecification();
            return;
        }
        showProgress();
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        params.put("productId", productId);
        params.put("limitmarketId", String.valueOf(limitId));

        Logger.e("params==" + params.toString());
        OkHttpUtils
                .get()
                .url(GlobalContent.GET_IF_I_CAN_BUY)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        dismissProgress();
                        Logger.e("当前用户是否能购买此商品==" + response);
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (rootBean.ErrCode.equals("0")) {
                            IfICanBuyBean ifICanBuyBean = mGson.fromJson(rootBean.Data, IfICanBuyBean.class);
                            if (ifICanBuyBean.isData()) {//当前用户可否购买此商品
                                setNumberAndSpecification();
                            } else {
                                showToast(ifICanBuyBean.getTips());
                            }
                        } else {
                            showToast(rootBean.ResponseMsg);
                        }
                    }
                });
    }

    /**
     * 更改后台关注数据
     *
     * @param url
     */
    private void cancelOrAttentionShop(String url) {
        HashMap<String, String> map = new HashMap<>();
        map.put("shopId", shopId);
        map.put("UserId", userId);
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
                        Logger.e("关注成功");
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if(rootBean.ErrCode.equals("0")) {
                            getNetShopData();
                        }
                    }
                });

    }


    /**
     * 初始化布局
     */

    private void initView() {

        setBackButtonVisibility(View.VISIBLE);
        setChartButtonVisibility(View.VISIBLE);
        iv_share.setVisibility(View.VISIBLE);

        //限时购倒计时
        count_down = (CountdownView) findViewById(R.id.count_down);
        //详情页商品图片
        vp_detail_icon = (ViewPager) findViewById(R.id.vp_detail_icon);
        //小圆点
        ll_producet_dot = (LinearLayout) findViewById(R.id.ll_producet_dot);

        //商品名称
        tvDetaliName = (TextView) findViewById(R.id.tv_detali_name);
        //商品价格
        tvPriceNormal = (TextView) findViewById(R.id.tv_price_normal);
        //商品高的价格
        tvPriceHight = (TextView) findViewById(R.id.tv_price_hight);
        //商品税费
        tvFax = (TextView) findViewById(R.id.tv_fax);
        //运费
        tv_freight = (TextView) findViewById(R.id.tv_freight);
        //商品配送方式
        tvSendMethod = (TextView) findViewById(R.id.tv_send_method);
        //用户图标
        ivUserPhoto = (ImageView) findViewById(R.id.iv_user_photo);
        //店铺名称
        tvShopName = (TextView) findViewById(R.id.tv_shop_name);
        //关注图标
        ibNotice = (Button) findViewById(R.id.ib_notice);
        //关注数量
        tvLookNum = (TextView) findViewById(R.id.tv_look_num);
        //100%正品
        ivSafeGoods = (ImageView) findViewById(R.id.iv_safe_goods);
        //最佳买手
        ivBestGoods = (ImageView) findViewById(R.id.iv_best_goods);
        //身份验证
        ivIdentity = (ImageView) findViewById(R.id.iv_identity);
        //保障商家
        ivEnsureShop = (ImageView) findViewById(R.id.iv_ensure_shop);
        //正品货源保证
        iv_bso = (ImageView) findViewById(R.id.iv_bso);
        tv_bso = (TextView) findViewById(R.id.tv_bso);

        //加入购物车
        btnAddCart = (Button) findViewById(R.id.btn_add_cart);
        //立即购买
        btnQuickBuy = (Button) findViewById(R.id.btn_quick_buy);

        //没有评价
        tv_no_evaluate = (TextView) findViewById(R.id.tv_no_evaluate);

        //第一个评论人头像
        ivUserPhoto1 = (ImageView) findViewById(R.id.iv_user_photo1);
        //第一个评论人名字
        tvUserName = (TextView) findViewById(R.id.tv_user_name);
        ll_start1 = (RatingBar) findViewById(R.id.ll_start1);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvComment = (TextView) findViewById(R.id.tv_comment);

        //商家评价人数
        tvPingjia = (TextView) findViewById(R.id.tv_pingjia);
        llComent = (LinearLayout) findViewById(R.id.ll_coment);

        //图文详情
        wb_producet_img_text = (WebView) findViewById(R.id.wb_producet_img_text);
        ibListArrow = (TextView) findViewById(R.id.ib_list_arrow);
        iv_relation = (ImageView) findViewById(R.id.iv_relation);
        rl_choose_color_size = (RelativeLayout) findViewById(R.id.rl_choose_color_size);
        rl_pingjia = (RelativeLayout) findViewById(R.id.rl_pingjia);

        //猜你喜欢
        rv_guess_you_like = (RecyclerView) findViewById(R.id.rv_guess_you_like);
        //商品详情广告词
        tv_details_message = (TextView) findViewById(R.id.tv_details_message);

        findViewById(R.id.btn_relation_shop).setOnClickListener(this);//联系卖家
        findViewById(R.id.btn_shop).setOnClickListener(this);//进店逛逛

        ivAttentionShop = (ImageView) findViewById(R.id.iv_attention_shop);
        ivAttentionProduct = (ImageView) findViewById(R.id.iv_attention_product);
        ll_menu = (LinearLayout) findViewById(R.id.ll_menu);
        tv_is_off = (TextView) findViewById(R.id.tv_is_off);

        iv_relation.setOnClickListener(this);
        ibListArrow.setOnClickListener(this);
        ivUserPhoto.setOnClickListener(this);
        btnAddCart.setOnClickListener(this);
        btnQuickBuy.setOnClickListener(this);
        ibNotice.setOnClickListener(this);
        rl_choose_color_size.setOnClickListener(this);
        ivAttentionProduct.setOnClickListener(this);
        ivAttentionShop.setOnClickListener(this);
    }
}
