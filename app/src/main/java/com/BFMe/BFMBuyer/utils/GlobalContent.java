package com.BFMe.BFMBuyer.utils;

/**
 * Description:数据的初始化
 * Copyright  : Copyright (c) 2016
 * Company    : 白富美(北京)网络科技有限公司
 * Author     :
 * Date       : 2016/7/12 12:13
 */
public class GlobalContent {

    /**
     * 网络访问的URL
     */
//    private static final String GLOBAL_URl                         = "http://www.baifomi.com/restbuyer/api/v1/";//深圳正式环境url地址
//     private static final String GLOBAL_URl                      = "http://qc.api.baifomi.com/buyer/api/v1/";//url地址
//    private static final String GLOBAL_URl                       = "http://192.168.1.50:9009/api/v1/";//内网url地址
//    private static final String GLOBAL_URl                       = "http://192.168.1.50:7788/api/v1/";//香港地址
    private static final String GLOBAL_URl = getGlobalUrl();

    private static String getGlobalUrl() {
        if(LocalUtils.isLocalMainland()){
            return "http://www.baifomi.com/restbuyer/api/v1/";//正式地址
//            return "http://qc.api.baifomi.com/buyer/api/v1/";
        }
        return "http://192.168.1.50:7788/api/v1/";

    }

    public static final String GLOBAL_VISION_CHECKED               = GLOBAL_URl + "version/Version_check?system=1";//版本更新
    public static final String GLOBAL_GET_CODE                     = GLOBAL_URl + "Account/SendCheckCode"; //买家注册发送注册码
    public static final String GLOBAL_FORGET_PWD_GET_CODE          = GLOBAL_URl + "Authorize/GetMobileCheckCode"; //买家忘记密码发送注册码
    public static final String GLOBAL_REGIST_BUYER_PWD             = GLOBAL_URl + "Account/ResetBuyerPassword";//买家忘记密码 重置密码
    public static final String GLOBAL_REGIST                       = GLOBAL_URl + "Account/BuyerRegister";//买家注册
    public static final String GLOBAL_LOGIN                        = GLOBAL_URl + "Authorize/NativeLogin";  //登录
    public static final String GLOBAL_LOGIN_THREE                  = GLOBAL_URl + "Authorize/QuickLogin";  //三方登录

    public static final String GLOBAl_ADVERST                      = GLOBAL_URl + "Misc/GetAppHomeCarousel";//获取图片轮播
    public static final String GLOBAL_SUBJECT_LIST                 = GLOBAL_URl + "Topic/GetTopicItems";//首页专题列表
    public static final String GLOBSL_LIMIT_ITEMS                  = GLOBAL_URl + "LimitBuy/GetLimitBuyItems"; //限时抢购
    public static final String GLOBAL_HOT_SHOP                     = GLOBAL_URl + "Misc/GetHotShops"; //热门店铺
    public static final String GLOBAL_HOT_SORT                     = GLOBAL_URl + "Misc/GetHotCategory"; //热门分类
    public static final String GLOBAL_TOPIC_PTODUCT                = GLOBAL_URl + "Topic/GetTopicProduct";//获取一个专题的商品包含的商品
    public static final String GLOBSAL_HOT_SELL_PRODUCT            = GLOBAL_URl + "Misc/GetHotProducts";//获取热销商品数据
    public static final String GLOBSAL_COMMENT                     = GLOBAL_URl + "ProductComment/GetShotProductComments"; //获取商品评价
    public static final String GLOBSAL_PRODUCT_DETAILS             = GLOBAL_URl + "Product/ProductDetail";//获取商品详情
    public static final String GLOBSAL_PRODUCT_SKU                 = GLOBAL_URl + "Product/GetSkuInfo";//获取商品sku信息
    public static final String GLOBSAL_LIMIT_PRODUCT_DETAILS       = GLOBAL_URl + "Product/LimitProductDetail";//获取限时购商品详情
    public static final String GLOBSAL_GET_LIMIT_PRODUCT_FREIGHT   = GLOBAL_URl + "/Product/GetLimitProductFreightAmount";//获取限时购商品运费关税
    public static final String GET_ALL_SEARCH_KEY_WORDS            = GLOBAL_URl + "Search/GetAllSearchKeywords";//获取大家都在搜
    public static final String GLOBAL_COLLECT_PRODUCT              = GLOBAL_URl + "CollectProduct/CollectProduct";//收藏商品
    public static final String GLOBAL_CANCEL_COLLECT_PRODUCT       = GLOBAL_URl + "CollectProduct/CancelCollectProduct";//取消收藏商品

    public static final String GET_SHOP_CONTRY_AREA                = GLOBAL_URl + "shop/GetShopContryArea";   //获取店铺推荐区域和洲际信息
    public static final String GET_COUNTRY_AREA                    = GLOBAL_URl + "CountryArea/GetList";   //获取店铺区域信息
    public static final String GLOBSAL_SHOP_INFO                   = GLOBAL_URl + "Shop/GetShopBasicInfo";   //  根据店铺id，获取店铺信息
    public static final String GLOBSAL_SHOP_COUNTRY_INFO           = GLOBAL_URl + "Shop/GetShopByCountryId";   //  根据国家id，获取这个国家的店铺信息
    public static final String GLOBSAL_SHOP_LIST_INTERL            = GLOBAL_URl + "shop/GetShopByInterlId";   //  根据洲际id，获取这个洲际的店铺信息
    public static final String GLOBSAL_SHOP_PRODUCTS               = GLOBAL_URl + "Product/GetShopProducts";//获取店铺内所有的商品
    public static final String GLOBSAL_ADD_CART                    = GLOBAL_URl + "Cart/AddToCart"; //添加到购物车
    public static final String GLOBSAL_GETCSRT_LIST                = GLOBAL_URl + "Cart/GetCartItems";//获取购物车列表
    public static final String GLOBSAL_CHANGE_NUMBER               = GLOBAL_URl + "Cart/UpdateCartItems";//更改购物车内商品数量

    public static final String GLOBAl_DELETE_CART                  = GLOBAL_URl + "Cart/DeleteCartItems";//根据sukid删除购物车
    public static final String GLOBAl_COMMIT_ACCOUNT               = GLOBAL_URl + "Cart/GetSubmitCartItems";// 结算界面获取购物车列表

    public static final String GLOBAl_ADD_ADDRESS                  = GLOBAL_URl + "ShippingAddress/AddAdress";// 追加收货地址
    public static final String GLOBAl_EDIT_ADDRESS                 = GLOBAL_URl + "ShippingAddress/EditAdress";//编辑收货地址
    public static final String GLOBAL_DEFAULT_ADDRESS              = GLOBAL_URl + "ShippingAddress/SetDefaultAdress";//设置为默认地址
    public static final String GLOBAl_GET_SETTED_ADDRESS           = GLOBAL_URl + "ShippingAddress/GetAddressById";//根据地址id生成地址信息
    public static final String GLOBAL_DELETE_ADDRESS               = GLOBAL_URl + "ShippingAddress/DeleteAddress";//删除收货地址
    public static final String GLOBSL_GET_ORDER                    = GLOBAL_URl + "UserOrder/OrderList";//获取订单列表
    public static final String GLOBAL_LOSE_ORDER                   = GLOBAL_URl + "OrderSubmit/CancelOrder";//取消订单
    public static final String GLOBAL_GET_ADDRESS                  = GLOBAL_URl + "ShippingAddress/GetAddressForOrder";//获取收货地址
    public static final String GLOBAL_CART_COMMIT                  = GLOBAL_URl + "Order/OrderSubmit";//购物车提交订单
    public static final String GLOBAL_ORDER_SUBMIT_LIMITBUY        = GLOBAL_URl + "OrderSubmit/LimitBuy";//限时购提交订单
    public static final String GLOBAL_COMMIT_COMMENT               = GLOBAL_URl + "ProductComment/SubmitComment";//提交评价数据
    public static final String GLOBAL_ORDER_DEATIL                 = GLOBAL_URl + "Order/OrderDetail";//获取订单详情
    public static final String GLOBAL_ADD_CART                     = GLOBAL_URl + "Cart/FastBuyAddToCart";//模拟加入购物车
    public static final String GLOBAL_ATTENTION                    = GLOBAL_URl + "CollectShop/GetCollectShops";//关注的店铺
    public static final String GLOBAL_COMMIT_NEW                   = GLOBAL_URl + "Order/OrderSubmit";
    public static final String GLOBAL_CONFIRM_ORDER                = GLOBAL_URl + "OrderSubmit/ConfirmOrder";//确认订单

    public static final String GLOBAL_GET_INTEGRAL_MY              = GLOBAL_URl + "Integral/GetIntegralDetail";// 我的界面获取用户可用积分
    public static final String GLOBAL_GET_INTEGRAL                 = GLOBAL_URl + "Order/GetDeductibleAmount";// 结算界面获取用户可用积分
    public static final String GLOBAL_GET_INTEGRALDETAIL           = GLOBAL_URl + "Integral/GetIntegral";// 获取用 户积分明细
    /**
     * activity的数据传递
     */
    public static final String SETTING_PASSWORD_SIGN               = "opinion";//密码设置的一个标记
    public static final int REQUEST_CODE_1                         = 1; //登录的activity跳转
    public static final int REQUEST_CODE_2                         = 2;  //忘记密码的activity跳转

    public static final String GLOBAL_IMGGE_URL                    = "http://img.baifomi.com/upload/api/UploadPhone";//图片上传地址深圳
//      public static final String GLOBAL_IMGGE_URL                    = "http://img.baifomi.com/upload_qc/api/UploadPhone";//图片上传地址深圳
//    public static final String GLOBAL_IMGGE_URL                    = "http://192.168.1.50:8888/upload/api/UploadPhone";//本地图片上传地址

    public static final String GLOBAL_GET_REFUND_PRODUCTS          = GLOBAL_URl + "Refund/GetRefundProductsInfo";//退款商品信息
    public static final String GLOBAL_GET_REFUND_REASON            = GLOBAL_URl + "Refund/GetRefundReason";//退款原因
    public static final String GLOBAL_GET_REFUND                   = GLOBAL_URl + "Refund/SubmitRefundApply";//退款

    public static final String GLOBAL_GO_PAY                       = GLOBAL_URl + "Pay/GetPayInfo";// 我的订单页面去支付接口
    public static final String GLOBAL_PAY_STATE                    = GLOBAL_URl + "pay/GetPayState";// 我的订单页面去支付接口
    public static final String GLOBAL_GET_EXPRESS                  = GLOBAL_URl + "Account/GetExpressData";// 获取物流数据
    public static final String GLOBAL_COMPLAINT                    = GLOBAL_URl + "Order/OrderComplaint";// 维权

    /**
     * 首页的专题列表
     */
    public static final String GLOBSAL_PROMPTLY_BUY                = GLOBAL_URl + "Cart/FastBuyAddToCart"; //立即购买接口
    public static final String GLOBSAL_PRODUCT_IMG_TEXT            = GLOBAL_URl + "Product/GetProductDesc";//根据商品id 获取商品图文详情
    public static final String GLOBAL_HELP                         = GLOBAL_URl + "HelpCenter/GetHelpCenterNav";//帮助中心
    public static final String GLOBAL_HELP_ARTICLE                 = GLOBAL_URl + "HelpCenter/GetHelpCenterArticle";//帮助中心文章
    public static final String GLOBAL_SORT                         = GLOBAL_URl + "Category/GetCategories";//分类
    public static final String GLOBAL_SEARCH                       = GLOBAL_URl + "Search/ProductSearch";//搜索
    public static final String GLOBAL_SHOP_SORT                    = GLOBAL_URl + "Product/GetShopProducts";//搜索店铺内商品

    public static final String GLOBAL_GET_PHONE                    = GLOBAL_URl + "Account/GetMobile";//获取当前用户的手机号
    public static final String GLOBAL_GET_SHOP_LIST                = GLOBAL_URl + "ProductList/List";//获取商品列表
    public static final String GLOBAL_CANCEL_ATTENTION_SHOP        = GLOBAL_URl + "CollectShop/CancelCollectShop";//关注店铺
    public static final String GLOBAL_ATTENTION_SHOP               = GLOBAL_URl + "CollectShop/CollectShop";//关注店铺

    public static final String GLOBAL_GET_PHONE_CODE               = GLOBAL_URl + "Account/GetCurrentCellPhoneCheckCode";//获取当前手机验证码
    public static final String GLOBAL_YANZHENG_PHONE               = GLOBAL_URl + "Account/CheckCurrentCellPhoneCheckCode";//验证当前手机验证码
    public static final String GLOBAL_GET_NEW_PHONE_CODE           = GLOBAL_URl + "Account/GetNewCellPhoneCheckCode";//获取新手机号验证码
    public static final String GLOBAL_YANZHENG_NEW_PHONE           = GLOBAL_URl + "Account/ReplaceCellPhoneNumber";//验证新手机号

    public static final String GLABAL_CHNAGE_NICK                  = GLOBAL_URl + "Account/ModifyBuyerNick"; //  修改用户名
    public static final String GLABAL_CHNAGE_EMAIL                 = GLOBAL_URl + "Account/ModifyBuyerEmail"; //  修改邮箱
    public static final String GLABAL_CHNAGE_PWD                   = GLOBAL_URl + "Account/ModifyBuyerPassword"; //  修改密码

    public static final String GLABAL_GET_USERINFO                 = GLOBAL_URl + "BuyerInfo/GetBuyerInfo";//获取用户信息
    public static final String GLABAL_LOGO_URL                     = GLOBAL_URl+"Account/ModifyBuyerLogo";//更改头像


    public static final String GLABAL_GET_REFUND_LIST              = GLOBAL_URl+"Refund/GetRefundList";//获取退款列表
    public static final String GLABAL_GET_REFUND_DETAIL            = GLOBAL_URl+"Refund/GetRefundDetail";//获取退款详情

    public static final String GLOBAL_LOGINIM                      = GLOBAL_URl + "Im/GetIMInfo";  //登录环信
    public static final String GLOBAL_FORGETPWDCHECKCODE           = GLOBAL_URl + "Authorize/CheckForgetPwdCheckCode";  //忘记密码验证手机号
    public static final String GLOBAL_CHECKQUICKREGIST             = GLOBAL_URl + "Authorize/CheckQuickRegist";  //注册验证手机号


    public static final String GET_BFM_WECHAT                      = GLOBAL_URl + "Misc/GetBFMWechat";  //获取微信客服
    public static final String CHECK_LIMIT_BUY_CODE                = GLOBAL_URl + "Order/CheckLimitBuyCode";  //效验验证码是否正确
    public static final String DELAY_THE_GOODS                     = GLOBAL_URl + "UserOrder/MembeConfirmLatterOrder";  //延期收货
    public static final String COLLECT_PRODUCTS                     = GLOBAL_URl + "CollectProduct/GetCollectProducts";  //获取喜欢的商品列表

    /**
     * UGC
     */

    //ugc首页banner
    public static final String GET_UGC_HOME_BANNER                  = GLOBAL_URl + "UGCTopic/GetUGCHomeBanner";
    //热门话题分类
    public static final String GET_UGC_HOT_TOPIC_CATEGORT           = GLOBAL_URl + "UGCTopic/GetUGCHotTopicCategory";
    //推荐话题
    public static final String POST_SEARCH_UGC_TOPIC_LIST            = GLOBAL_URl + "UGCTopic/SearchUGCTopicsList";
    //话题详情
    public static final String POST_SEARCH_UGC_TOPIC_DETIALS         = GLOBAL_URl + "UGCTopic/SearchUGCTopicsDetials";
    //话题点赞
    public static final String POST_USER_PARES_TOPIC                 = GLOBAL_URl + "UGCTopic/UserParseTopic";
    //关注用户
    public static final String POST_USER_CONCERN                     = GLOBAL_URl + "UGCTopic/UserConcern";
    //提交评价
    public static final String POST_PUBLISH_TOPIC_COMMENT            = GLOBAL_URl + "UGCTopic/PublishTopicComment";
    //点赞评论
    public static final String POST_PARSE_TOPIC_COMMENTS             = GLOBAL_URl + "UGCUserTopic/UserParseTopicComments";
    //全部评论
    public static final String POST_TOPIC_COMMENT_LIST               = GLOBAL_URl + "UGCTopic/SearchTopicCommentList";
    //个人说活主页详情
    public static final String POST_PERSONAL_CENTER                  = GLOBAL_URl + "UGCUserTopic/SearchUGCPersonalCenter";
    //用户发布的话题列表
    public static final String POST_USER_PUBLISH_TOPICS              = GLOBAL_URl + "UGCUserTopic/SearchUGCUserPublishTopics";
    //获取发布话题时用户订单信息
    public static final String GET_UGC_ORDER_LIST                   = GLOBAL_URl + "UGCTopic/SearchUGCOrderList";
    //发布UGC话题
    public static final String PUBLISH_UGC_TOPIC                     = GLOBAL_URl + "UGCTopic/PublishUGCTopic";
    //子话题头部信息
    public static final String POST_PUBLISH_UGC_SUB_TOPIC             = GLOBAL_URl + "UGCUserTopic/SearchUGCSubTopic";
    //子话题下话题列表
    public static final String POST_PUBLISH_UGC_SUB_TOPIC_LIST        = GLOBAL_URl + "UGCUserTopic/SearchSubTopicsList";
    //订阅子话题
    public static final String POST_SUBSCRIBE_TOPIC_CATEGORY          = GLOBAL_URl + "UGCUserTopic/SubscribeTopicCategory";
    //我的评论列表
    public static final String POST_USER_COMMENT_LIST                = GLOBAL_URl + "UGCUserTopic/SearchUserCommentList";
    //获取我的关注的人列表
    public static final String GET_USER_FOCUS_LIST                   = GLOBAL_URl + "UGCUserTopic/SearchUserFocus";
    //获取我的粉丝列表
    public static final String GET_USER_FANS_LIST                    = GLOBAL_URl + "UGCUserTopic/SearchUserFans";
    //提交反馈信息
    public static final String POST_USER_FEED_BACK                   = GLOBAL_URl + "UserFeedBack/SubmitUserFeedBack";
    //更改个性签名
    public static final String POST_USER_BUYER_INFO                  = GLOBAL_URl + "Account/ModifyBuyerInfo";
    //签到
    public static final String GET_USER_ATTENDANCE                   = GLOBAL_URl + "BuyerInfo/UserAttendance";


    /**
     * 推送
     */
    //消息中心中-查询用户是否有未读消息，配合小红点使用
    public static final String POST_GET_USER_READ_MESSAGE           = GLOBAL_URl + "UserMessage/GetUserReadMessage";
    //消息中心中-绑定用户极光注册Id
    public static final String POST_BIND_JPUSH_USER_REGISTRATIONID  = GLOBAL_URl + "UserMessage/BindJPushUserRegistrationId";
    //消息中心中-获取消息中主页内容
    public static final String POST_PUSH_MAIN_MESSAGE               = GLOBAL_URl + "UserMessage/GetPushMainMessage";
    //获取最新的官方推荐消息
    public static final String POST_PUSH_RECOMMEN_MESSAGE           = GLOBAL_URl + "UserMessage/GetPushRecommenMessage";
    //物流推送通知
    public static final String GET_PUSH_EXPRESS_NOTICE               = GLOBAL_URl + "UserMessage/GetPushExpressNotice";
    //话题精选
    public static final String GET_PUSH_CONCERN_MESSAGE              = GLOBAL_URl + "UserMessage/GetPushConcernMessage";
    //新的话题评论
    public static final String GET_PUSH_NEW_COMMENTS                 = GLOBAL_URl + "UserMessage/GetUGCNewComments";

    /**
     * 新版首页
     */
    //首页
    public static final String POST_HOME_DATA                        = GLOBAL_URl + "Index/HomeData";
    //店铺首页
    public static final String GET_SHOP_HOME_PAGE                   = GLOBAL_URl + "Index/ShopHomePage";
    //获取店铺地区列表
    public static final String GET_REGION_FILTRATE                   = GLOBAL_URl + "Index/GetAreaList";
    //获取店铺品牌列表
    public static final String GET_BRAND_LIST                          = GLOBAL_URl + "Index/GetBrandList";
    //获取店铺列表
    public static final String POST_SHOP_LIST                          = GLOBAL_URl + "Index/GetShopList";
    //店铺筛选条件
    public static final String GET_SHOP_SCREEN                       = GLOBAL_URl + "Index/GetShopScreenList";
    //获取分类主页的一级分类列表
    public static final String GET_HOME_CATEGORIES                   = GLOBAL_URl + "Category/GetHomeCategories";
    //根据一级分类Id获取二级和三级分类列表
    public static final String GET_SUB_CATEGORIES_BY_ID              = GLOBAL_URl + "Category/GetSubCategoriesById";
    //获取搜索提示列表（话题，店铺，商品）
    public static final String POST_SEARCH_TIPS                       = GLOBAL_URl + "SearchNew/GetSerachTips";
    //根据关键字查询话题列表
    public static final String POST_UGC_TOPIC_BY_KEYWORDS             = GLOBAL_URl + "UGCUserTopic/SearchUGCTopicsByKeywords";
    //查询商品列表
    public static final String POST_SEARCH_PRODUCTS                  = GLOBAL_URl + "SearchNew/SearchProducts";

    //存储注册过userID key
    public static final String USER                                = "userid";
    public static final String ISLOGIN                             = "islogin";
    public static final String APP_ID                              = "wx3888d4577f479bba";
    public static final String SORT_ID                             = "sort_id";//标记分类id
    //存储当前手机验证码
    public static final String CURRCODE                            ="currCode";


    public static final String ORDER_STATUS                        = "order_status";//订单状态
    public static final String COMMODITY_URL                       = "commodity_url";//商品url
    public static final String ORDER_ID                            = "order_id";//订单id
    public static final String ORDER_ITEM_ID                       = "order_item_id";//子订单ID
    public static final String ORDER_INFO                          = "order_info";//订单信息

    public static final String REFUND_ID                           = "refund_id";//退货id
    public static final String IS_SHOP                             = "is_shop";
    public static final String SHOP_ID                             = "shop_id";

    public static final String limitdjs                            = "LIMITDJS";
    public static final String ISONE                               = "isone";
    public static final String CART_NUMBER                         = "cart_number";
    public static final String IS_HONGKONG                         = "is_hongkong";//是否是香港用户

    /**
     * UGC跳转
     */
    public static final String TOPIC_ID                             = "topicId";
    public static final String TOPICS_ID                             = "topicsId";
    public static final String USER_INFO_ID                         = "userInfoId";

    /**
     * 获取用户真实姓名和身份证号码
     */
    public static final String GET_USER_NAME_AND_CARD_NO                  = GLOBAL_URl + "BuyerInfo/GetUserNameAndCardNo";
    //修改用户真实姓名和身份证号码
    public static final String POST_UPDATE_NAME_AND_CARD_NO               = GLOBAL_URl + "BuyerInfo/UpdateNameAndCardNo";
    //获取南沙合捷保税仓的提示信息
    public static final String GET_HS_CODE_TITLE                          = GLOBAL_URl + "Order/GetHSCodeTitle";
    //获取首页热销商品
    public static final String GET_HOME_HOT_PRODUCTS                      = GLOBAL_URl + "Misc/GetHomeHotProducts";
    //当前用户是否可以购买此商品
    public static final String GET_IF_I_CAN_BUY                           = GLOBAL_URl + "BuyerInfo/CheckUserBuyLimtmaketProduct";

    /**
     * 迭代
     */

    public static final String GET_SEARCH_PRODUCTS_BY_THREECATEGORY       = GLOBAL_URl + "SearchNew/SearchProductsByThreeCategory";


    public static final String POST_EXPRESS_INFORMATION                   = GLOBAL_URl + "Refund/UserConfirmRefundGood";

    public static final String GLOBAL_BANK_INFO                           = GLOBAL_URl +"Order/GetPayBankInfo";

    public static final String GLOBSAL_UPLOAD_VOUCHER                     = GLOBAL_URl+"Order/UploadOrderPayVoucher";//上传支付凭证


    public static final String GLOBAL_ONEKEY_BINDER                       =GLOBAL_URl+"Authorize/BindUser";//绑定第三方账号

    public static final String GLOBAL_GET_CODE_THREE                      = GLOBAL_URl+"Authorize/GetQuickLoginMobileCheckCode";


}
