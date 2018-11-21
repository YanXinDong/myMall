package com.BFMe.BFMBuyer.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.activity.EditSignActivity;
import com.BFMe.BFMBuyer.activity.GradeActivity;
import com.BFMe.BFMBuyer.activity.HelpActivity;
import com.BFMe.BFMBuyer.activity.LoginActivity;
import com.BFMe.BFMBuyer.activity.MyCollectActivity;
import com.BFMe.BFMBuyer.activity.MyOrderActivity;
import com.BFMe.BFMBuyer.activity.PromotionActivity;
import com.BFMe.BFMBuyer.activity.RecentListActivity;
import com.BFMe.BFMBuyer.activity.SaleServiceActivity;
import com.BFMe.BFMBuyer.activity.SelfInfoDataActivity;
import com.BFMe.BFMBuyer.activity.SettingActivity;
import com.BFMe.BFMBuyer.activity.ShoppingCartActivity;
import com.BFMe.BFMBuyer.base.BaseFragment;
import com.BFMe.BFMBuyer.javaBean.MyGradeBean;
import com.BFMe.BFMBuyer.javaBean.ReadBean;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.share.SelectShareCategoryActivity;
import com.BFMe.BFMBuyer.ugc.activity.AttentionAndFansActivity;
import com.BFMe.BFMBuyer.ugc.activity.MyCommentActivity;
import com.BFMe.BFMBuyer.ugc.activity.UserInfoActivity;
import com.BFMe.BFMBuyer.ugc.bean.PersonalCenterInfo;
import com.BFMe.BFMBuyer.utils.AppUtils;
import com.BFMe.BFMBuyer.utils.CacheUtils;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.ImageUtils;
import com.BFMe.BFMBuyer.utils.TimeUtils;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.BFMe.BFMBuyer.view.GlideCircleTransform;
import com.bumptech.glide.Glide;
import com.mob.MobSDK;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.session.activity.P2PMessageActivity;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

/**
 * wode的fragment
 */
public class MyFragment extends BaseFragment implements View.OnClickListener {

    private TextView tvGrade;
    private TextView tvUserName;
    private ImageView ivLogin;
    private View is_remind;
    private TextView tv_attention;
    private TextView tv_fans;
    private TextView tv_praise;
    private CheckBox cb_sign_in;
    private TextView tv_cart_number;

    private IMRecriver imRecriver;
    private PersonalCenterInfo personalCenterInfo;
    private MyGradeBean myGradeBean;
    protected String mCartNumber;//购物车商品数量
    private ShoppingCartNumberReceiver shoppingCartNumberReceiver;

    @Override
    protected void initData() {
        super.initData();
        //获取消息数
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.BFMe.BFMBuyer.ACTION.RECEIVE_MSG");
        imRecriver = new IMRecriver();
        getActivity().registerReceiver(imRecriver, intentFilter);

    }

    @Override
    public void onResume() {
        super.onResume();
        mUserId = CacheUtils.getString(mActivity, GlobalContent.USER);
        isLogin = CacheUtils.getBoolean(mActivity, GlobalContent.ISLOGIN);

        //注册购物车商品数量变化的广播接收器
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.BFMe.BFMBuyer.SHOPPING_CART_NUMBER");
        shoppingCartNumberReceiver = new ShoppingCartNumberReceiver();
        mActivity.registerReceiver(shoppingCartNumberReceiver, intentFilter);

        //获取未读消息数 此处返回数是从1开始
        if (NIMClient.getService(MsgService.class).getTotalUnreadCount() > 0) {
            is_remind.setVisibility(View.VISIBLE);
        } else {
            is_remind.setVisibility(View.GONE);
        }
        if (isLogin) {
            //积分
            setGradeData();
            //用户名
            setUserInfoData();
            //获取是否有未读推送
            getNetPush();
            getCartNumber();
        } else {
            tvUserName.setText(getString(R.string.please_login));
            Glide
                    .with(getActivity())
                    .load(R.drawable.zhanwei3)
                    .transform(new GlideCircleTransform(mActivity))
                    .into(ivLogin);
            tv_attention.setText("--");
            tv_fans.setText("--");
            tv_praise.setText("--");
            cb_sign_in.setChecked(false);
            tv_cart_number.setVisibility(View.GONE);
        }
    }

    private void getCartNumber() {
        mCartNumber = CacheUtils.getString(mActivity, GlobalContent.CART_NUMBER);

        if (isLogin && !mCartNumber.equals("0")) {
            tv_cart_number.setVisibility(View.VISIBLE);
            tv_cart_number.setText(mCartNumber);
        } else {
            tv_cart_number.setVisibility(View.GONE);
        }
    }

    /**
     * 购物车商品数量变化的广播接收器
     */
    private class ShoppingCartNumberReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            getCartNumber();
        }
    }

    /**
     * 获取是否有推送信息
     */
    private void getNetPush() {
        OkHttpUtils
                .post()
                .url(GlobalContent.POST_GET_USER_READ_MESSAGE)
                .addParams("userId", mUserId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("是否有消息未读" + response);
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (rootBean.ErrCode.equals("0")) {
                            ReadBean readBean = mGson.fromJson(rootBean.Data, ReadBean.class);
                            if (readBean.getRead() == 1) {//未读
                                is_remind.setVisibility(View.VISIBLE);
                            } else {
                                is_remind.setVisibility(View.GONE);
                            }
                        }
                    }
                });
    }

    /**
     * 获取用户信息
     */
    private void setUserInfoData() {
        Map<String, String> params = new HashMap<>();
        if (isLogin) {
            params.put("userId", mUserId);
        }
        params.put("searchUserId", mUserId);
        OkHttpUtils
                .post()
                .params(params)
                .url(GlobalContent.POST_PERSONAL_CENTER)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("获取用户信息" + response);
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (rootBean.ErrCode.equals("0")&&!rootBean.Data.equals("")) {
                            personalCenterInfo = mGson.fromJson(rootBean.Data, PersonalCenterInfo.class);
                            Glide
                                    .with(getActivity())
                                    .load(personalCenterInfo.getImageUrl())
                                    .transform(new GlideCircleTransform(mActivity))
                                    .error(R.drawable.zhanwei3)
                                    .placeholder(R.drawable.zhanwei3)
                                    .into(ivLogin);
                            tvUserName.setText(personalCenterInfo.getUserName());
                            tv_attention.setText(String.valueOf(personalCenterInfo.getFocusUserCount()));
                            tv_fans.setText(String.valueOf(personalCenterInfo.getFans()));
                            tv_praise.setText(String.valueOf(personalCenterInfo.getParseCount()));
                        }

                    }
                });
    }

    /**
     * 获取积分
     */
    private void setGradeData() {
        OkHttpUtils
                .post()
                .addParams("userid", mUserId)
                .url(GlobalContent.GLOBAL_GET_INTEGRAL_MY)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e(response);
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (rootBean.ErrCode.equals("0")) {
                            myGradeBean = mGson.fromJson(rootBean.Data, MyGradeBean.class);
                            tvGrade.setText("积分:" + myGradeBean.getIntegral());
                            CacheUtils.putString(mActivity, "myGrade", getString(R.string.grade_total1) + myGradeBean.getIntegral());
                            if (myGradeBean.getIsAttendance() == 1) {
                                cb_sign_in.setChecked(true);
                            } else {
                                cb_sign_in.setChecked(false);
                            }
                        }
                    }
                });
    }

    /**
     * 签到
     */
    private void setSign() {
        OkHttpUtils
                .get()
                .addParams("userid", mUserId)
                .url(GlobalContent.GET_USER_ATTENDANCE)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e(response);
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (rootBean.ErrCode.equals("0")) {
                            showToast(getString(R.string.operation_succeed));
                            cb_sign_in.setChecked(true);
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (!isLogin) {//未登录是的点击事件处理
            if(id == R.id.cb_sign_in) {//签到按钮不可用
                cb_sign_in.setChecked(false);
            }

            if (id == R.id.rl_up_shop || id == R.id.rl_help) {
                id = v.getId();
            } else {
                id = -1;
            }

        }
        switch (id) {
            case R.id.ib_remind://消息提醒
                mActivity.startActivity(new Intent(mActivity, RecentListActivity.class));
                startAnim();
                break;
            case R.id.ib_setting:
                //跳转到设置页面
                mActivity.startActivity(new Intent(mActivity, SettingActivity.class));
                startAnim();
                break;
            case R.id.btn_my_home://我的个人主页
                Intent intentUser = new Intent(mActivity, UserInfoActivity.class);
                intentUser.putExtra(GlobalContent.USER_INFO_ID, mUserId);
                mActivity.startActivity(intentUser);
                startAnim();
                break;
            case R.id.rl_my_order://跳转至全部订单
                mActivity.startActivity(new Intent(mActivity, MyOrderActivity.class));
                startAnim();
                break;
            case R.id.ll_wait_pay://跳转至待付款界面
                Intent intentevaluate = new Intent(mActivity, MyOrderActivity.class);
                intentevaluate.putExtra("flag", 1);
                mActivity.startActivity(intentevaluate);
                startAnim();
                break;
            case R.id.ll_wait_sendproduct://跳转至待发货界面
                Intent intentsend = new Intent(mActivity, MyOrderActivity.class);
                intentsend.putExtra("flag", 2);
                mActivity.startActivity(intentsend);
                startAnim();
                break;
            case R.id.ll_wait_acceptproduct://跳转至待收货界面
                Intent intentaccept = new Intent(mActivity, MyOrderActivity.class);
                intentaccept.putExtra("flag", 3);
                mActivity.startActivity(intentaccept);
                startAnim();
                break;
            case R.id.ll_wait_evaluate://跳转至待评价界面
                Intent intentpay = new Intent(mActivity, MyOrderActivity.class);
                intentpay.putExtra("flag", 4);
                mActivity.startActivity(intentpay);
                startAnim();
                break;
            case R.id.ll_sale_service://收货服务
                startActivity(new Intent(mActivity, SaleServiceActivity.class));
                startAnim();
                break;

            case R.id.iv_login://个人资料
                mActivity.startActivity(new Intent(mActivity, SelfInfoDataActivity.class));
                startAnim();
                break;
            case R.id.ll_my_collect://我的收藏
                mActivity.startActivity(new Intent(mActivity, MyCollectActivity.class));
                startAnim();
                break;
            case R.id.ll_my_integral://我的积分
                mActivity.startActivity(new Intent(mActivity, GradeActivity.class));
                startAnim();
                break;
            case R.id.ll_my_comment://我的评论
                mActivity.startActivity(new Intent(mActivity, MyCommentActivity.class));
                mActivity.overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
                break;
            case R.id.ll_complaint://投诉反馈
                Intent intent = new Intent(mActivity, EditSignActivity.class);
                intent.putExtra("IS_FROM_SIGN", false);
                startActivity(intent);
                startAnim();
                break;
            case R.id.ll_service://在线客服
                P2PMessageActivity.IMFlag = false;
                P2PMessageActivity.shopDetailImg ="";
                P2PMessageActivity.shopDetailDes = "";
                P2PMessageActivity.shopDetailPrice = "";
                NimUIKit.startChatting(mActivity, "bfme_0885315b19514590907912533", SessionTypeEnum.P2P, null, null);
                startAnim();
                break;

            case R.id.rl_up_shop://我要开店
                //跳转至开店页面
                startSeller();
                break;
            case R.id.rl_help://帮助中心
                startActivity(new Intent(mActivity, HelpActivity.class));
                startAnim();
                break;
            case R.id.rl_invite://邀请好友
                shareToWachat();
                break;
            case R.id.ll_attention://关注
                Intent attentionIntent = new Intent(mActivity, AttentionAndFansActivity.class);
                attentionIntent.putExtra("IsAttentionOrFans", true);
                startActivity(attentionIntent);
                startAnim();
                break;
            case R.id.ll_fans://粉丝
                Intent fansIntent = new Intent(mActivity, AttentionAndFansActivity.class);
                fansIntent.putExtra("IsAttentionOrFans", false);
                startActivity(fansIntent);
                startAnim();
                break;
            case R.id.cb_sign_in:
                setGradeData();
                if (myGradeBean.getIsAttendance() == 0) {
                    setSign();
                } else {
                    showToast(getString(R.string.sign_in_hint));
                }
                break;
            case R.id.ib_shopping_cart:
                startActivity(new Intent(mActivity, ShoppingCartActivity.class));
                startAnim();
                break;
            default:
                //跳转到登录页面
                Intent intentCart = new Intent(mActivity, LoginActivity.class);
                startActivity(intentCart);
                startAnim();
                break;
        }
    }

    private void startSeller() {
        boolean installApp = AppUtils.isInstallApp(mActivity, "com.BFMe.BFMSeller");
        if (installApp) {
            AppUtils.launchApp(mActivity, "com.BFMe.BFMSeller");
        } else {
            Toast.makeText(mActivity, getString(R.string.download_hint), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mActivity, PromotionActivity.class);
            intent.putExtra("url", "http://m.baifomi.com/pages/download_seller.html");
            mActivity.startActivity(intent);
            startAnim();
        }
    }

    private class IMRecriver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
//            此处加一是因为 返回的消息数从0开始的  1条即为0
            is_remind.setVisibility(View.VISIBLE);
        }
    }

    private void shareToWachat() {
        if (personalCenterInfo == null) {
            return;
        }
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.launcher);

        MobSDK.init(mActivity, "1b462d46b8900", "514cfad75c1832fd14fb3b7ce97cba9d");
        Intent intent = new Intent(mActivity, SelectShareCategoryActivity.class);
        intent.putExtra("shareTitle", "Buy For Me");
        intent.putExtra("shareContent", personalCenterInfo.getUserName() + getString(R.string.share_app_hint));
        try {
            intent.putExtra("shareImagePath", ImageUtils.getImage(bitmap, TimeUtils.getCurTimeMills() + ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        intent.putExtra("shareUrl", "http://m.baifomi.com/pages/download_buyer.html");
        startActivity(intent);
        bottomStartAnim();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(imRecriver);
    }

    @Override
    public View initView(LayoutInflater inflater) {
        View view = View.inflate(mActivity, R.layout.fragment_my, null);
        //提醒
        ImageButton ibRemind = (ImageButton) view.findViewById(R.id.ib_remind);
        is_remind = view.findViewById(R.id.is_remind);
        //设置
        ImageButton ibSetting = (ImageButton) view.findViewById(R.id.ib_setting);
        //已经登录的图标
        ivLogin = (ImageView) view.findViewById(R.id.iv_login);
        //用户名称
        tvUserName = (TextView) view.findViewById(R.id.tv_user_name);
        //用户积分
        tvGrade = (TextView) view.findViewById(R.id.tv_grade);
        tv_attention = (TextView) view.findViewById(R.id.tv_attention);
        tv_fans = (TextView) view.findViewById(R.id.tv_fans);
        tv_praise = (TextView) view.findViewById(R.id.tv_praise);
        cb_sign_in = (CheckBox) view.findViewById(R.id.cb_sign_in);
        tv_cart_number = (TextView) view.findViewById(R.id.tv_cart_number);

        //全部订单
        RelativeLayout rl_my_order = (RelativeLayout) view.findViewById(R.id.rl_my_order);
        //等待付款
        LinearLayout llWaitPay = (LinearLayout) view.findViewById(R.id.ll_wait_pay);
        //代发货
        LinearLayout llWaitSendproduct = (LinearLayout) view.findViewById(R.id.ll_wait_sendproduct);
        //待收货
        LinearLayout llWaitAcceptproduct = (LinearLayout) view.findViewById(R.id.ll_wait_acceptproduct);
        //待评价
        LinearLayout ll_wait_evaluate = (LinearLayout) view.findViewById(R.id.ll_wait_evaluate);
        //售后服务
        LinearLayout llSaleService = (LinearLayout) view.findViewById(R.id.ll_sale_service);

        LinearLayout ll_my_collect = (LinearLayout) view.findViewById(R.id.ll_my_collect);//我的收藏
        LinearLayout ll_my_integral = (LinearLayout) view.findViewById(R.id.ll_my_integral);//我的积分
        LinearLayout ll_my_comment = (LinearLayout) view.findViewById(R.id.ll_my_comment);//我的评论
        LinearLayout ll_complaint = (LinearLayout) view.findViewById(R.id.ll_complaint);//投诉反馈
        LinearLayout ll_service = (LinearLayout) view.findViewById(R.id.ll_service);//在线客服

        RelativeLayout rl_up_shop = (RelativeLayout) view.findViewById(R.id.rl_up_shop);//在线客服
        RelativeLayout rl_help = (RelativeLayout) view.findViewById(R.id.rl_help);//帮助中心
        RelativeLayout rl_invite = (RelativeLayout) view.findViewById(R.id.rl_invite);//邀请好友


        LinearLayout llAttention = (LinearLayout) view.findViewById(R.id.ll_attention);
        LinearLayout llFans = (LinearLayout) view.findViewById(R.id.ll_fans);

        ImageButton ib_shopping_cart = (ImageButton) view.findViewById(R.id.ib_shopping_cart);
        view.findViewById(R.id.btn_my_home).setOnClickListener(this);

        rl_my_order.setOnClickListener(this);
        ll_wait_evaluate.setOnClickListener(this);
        ibSetting.setOnClickListener(this);
        ibRemind.setOnClickListener(this);
        ivLogin.setOnClickListener(this);
        tvGrade.setOnClickListener(this);
        llWaitPay.setOnClickListener(this);
        llWaitSendproduct.setOnClickListener(this);
        llWaitAcceptproduct.setOnClickListener(this);
        llSaleService.setOnClickListener(this);

        ll_my_collect.setOnClickListener(this);
        ll_my_integral.setOnClickListener(this);
        ll_my_comment.setOnClickListener(this);
        ll_complaint.setOnClickListener(this);
        ll_service.setOnClickListener(this);

        rl_up_shop.setOnClickListener(this);
        rl_help.setOnClickListener(this);
        rl_invite.setOnClickListener(this);
        llAttention.setOnClickListener(this);
        llFans.setOnClickListener(this);
        cb_sign_in.setOnClickListener(this);
        ib_shopping_cart.setOnClickListener(this);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity.unregisterReceiver(shoppingCartNumberReceiver);
    }
}
