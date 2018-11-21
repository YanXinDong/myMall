package com.BFMe.BFMBuyer.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.javaBean.RefundProductsBean;
import com.BFMe.BFMBuyer.javaBean.RefundReasonBean;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.javaBean.UploadingImageBean;
import com.BFMe.BFMBuyer.utils.CacheUtils;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.OperateStringUtlis;
import com.BFMe.BFMBuyer.utils.SystemUtil;
import com.BFMe.BFMBuyer.utils.UiUtils;
import com.BFMe.BFMBuyer.utils.Utils;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.BFMe.BFMBuyer.utils.xpermission.listener.XPermissionsListener;
import com.BFMe.BFMBuyer.utils.xpermissions.XPermissions;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

import static com.BFMe.BFMBuyer.R.id.rl_refund_type;

/**
 * 退款页面
 */
public class ApplyFordrawbackActivity extends Activity implements View.OnClickListener {

    private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private File tempFile;

    private ImageView iv_order_icon;//商品缩略图
    private TextView tv_order_new_price;
    private TextView tv_order_old_price;
    private TextView tv_order_commodity_name;
    private TextView tv_order_size_color;
    private TextView tv_order_num;

    private TextView tv_text_type;//退款类型

    private TextView tv_text;//退款原因

    private TextView num;//退款数量

    private TextView tv_return_all_price;//退款金额提示
    private EditText ed_input_money;//退款金额

    private TextView tv_procedure;//手续费
    private TextView tv_return_sum;//退款总计
    private EditText intro;//退款说明

    private ImageView iv_voucher1;
    private ImageView iv_voucher2;
    private ImageView iv_voucher3;

    private String mUserId;//用户id
    private String mOrderId;//订单ID
    private String mOrderItemId;//子订单id

    private Gson gson;
    private RefundProductsBean mRefundProductsBean;//退款商品信息
    private RefundReasonBean mRefundReasonBean;//退款原因类
    private int mRefundType = 1;//退款类型  1== 退款（无需退货） 2== 退货
    private int mRefundReason = 1;//退款原因
    private int mRefundNum = 1;//退款数量
    private boolean mSellerBear = false;//是否卖家承担手续费

    private double mTotalPrice;//退款总金额
    private double mServiceFee = 0.00;//转换后的具体退款手续费
    private RelativeLayout checkNum;
    private ImageView imageView;
    private Dialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_fordrawback);
        SystemUtil.setWindowStatusBarColor(this, R.color.colorZhu);
        initView();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mUserId = CacheUtils.getString(this, GlobalContent.USER);
        mOrderId = getIntent().getStringExtra(GlobalContent.ORDER_ID);
        mOrderItemId = getIntent().getStringExtra(GlobalContent.ORDER_ITEM_ID);

        gson = new Gson();
        //获取退款商品
        getNetRefundData();
        //获取退款原因
        getNetRefundCause(1);//默认是退款
    }

    /**
     * 获取退款原因
     */
    private void getNetRefundCause(int type) { //+ "?type=" + "2" 退款是1 退货是2

        Map<String, String> params = new HashMap<>();
        params.put("UserId", mUserId);
        params.put("type", type + "");

        OkHttpUtils
                .get()
                .params(params)
                .url(GlobalContent.GLOBAL_GET_REFUND_REASON)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e(  "退款原因：" + response);
                        mRefundReasonBean = gson.fromJson(gson.fromJson(response, RootBean.class).Data, RefundReasonBean.class);
                    }
                });
    }

    /**
     * 获取退款商品信息
     */
    private void getNetRefundData() {

        Map<String, String> params = new HashMap<>();
        params.put("UserId", OperateStringUtlis.getUerIdBack(mUserId));
        params.put("OrderId", mOrderId);
        params.put("OrderItemId", mOrderItemId);

        OkHttpUtils
                .get()
                .params(params)
                .url(GlobalContent.GLOBAL_GET_REFUND_PRODUCTS)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e(  "退款商品信息" + response);
                        RootBean rootBean = gson.fromJson(response, RootBean.class);
                        if (Integer.parseInt(rootBean.ErrCode) == 0) {
                            mRefundProductsBean = gson.fromJson(rootBean.Data, RefundProductsBean.class);
                            showRedundProducts();
                        }
                    }
                });

    }

    /**
     * 显示退款商品详情
     */
    private void showRedundProducts() {
        RefundProductsBean.OrderItemBean orderItem = mRefundProductsBean.getOrderItem();
        Glide
                .with(getApplicationContext())
                .load(orderItem.getImgPath())
                .placeholder(R.drawable.zhanwei1)
                .error(R.drawable.zhanwei1)
                .into(iv_order_icon);
        tv_order_new_price.setText(getResources().getString(R.string.money_type) + Utils.doubleSave2(orderItem.getSalePrice()));
        tv_order_old_price.setText(getResources().getString(R.string.money_type) + Utils.doubleSave2(orderItem.getCostPrice()));
        tv_order_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        tv_order_commodity_name.setText(orderItem.getProductName());
        tv_order_size_color.setText(orderItem.getColor() + " " + orderItem.getSize());
        tv_order_num.setText("x" + orderItem.getQuantity());

        mRefundNum = mRefundProductsBean.getMacRefundGoodCount();

        String refundMaxMoney = getResources().getString(R.string.refund_max_money);
        String maxRefundAmount = Utils.doubleSave2(mRefundProductsBean.getMaxRefundAmount());
        tv_return_all_price.setText(String.format(refundMaxMoney, maxRefundAmount));

        //四舍五入保留两位
        double serviceFee = mRefundProductsBean.getServiceFee() * 0.01;
        mServiceFee = mRefundProductsBean.getMaxRefundAmount() * serviceFee;
        tv_procedure.setText(getString(R.string.money_type) + Utils.doubleSave2(mServiceFee));//支付平台手续费
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_back://返回按钮
                finish();
                break;
            case rl_refund_type://退款类型
                choosePicture(rl_refund_type, getResources().getString(R.string.refund_type_select));
                break;
            case R.id.rl_refund_cause://退款原因
                choosePicture(R.id.rl_refund_cause, getResources().getString(R.string.refund_cause_select));
                break;
            case R.id.checkNum://退款数量
                choosePicture(R.id.checkNum, getResources().getString(R.string.refund_number_select));
                break;
            case R.id.iv_voucher1:
                imageView = iv_voucher1;
                choosePicture();
                break;
            case R.id.iv_voucher2:
                imageView = iv_voucher2;
                choosePicture();
                break;
            case R.id.iv_voucher3:
                imageView = iv_voucher3;
                choosePicture();
                break;
            case R.id.bt_submit://提交按钮
                //效验请求数据
                if (TextUtils.isEmpty(tv_text_type.getText())) {
                    String string = getResources().getString(R.string.select_refund_type);
                    Toast.makeText(ApplyFordrawbackActivity.this, string, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(tv_text.getText())) {
                    String string = getResources().getString(R.string.select_refund_cause);
                    Toast.makeText(ApplyFordrawbackActivity.this, string, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(ed_input_money.getText())) {
                    String string = getResources().getString(R.string.select_refund_price);
                    Toast.makeText(ApplyFordrawbackActivity.this, string, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(intro.getText())) {
                    String string = getResources().getString(R.string.select_refund_explain);
                    Toast.makeText(ApplyFordrawbackActivity.this, string, Toast.LENGTH_SHORT).show();
                    return;
                }
                refund();
                break;
        }
    }

    /**
     * 退款
     */
    private void refund() {
        Map<String, String> params = new HashMap<>();
        params.put("UserId", OperateStringUtlis.getUerIdBack(mUserId));//用户id
        params.put("RefundType", mRefundType + "");//退款类型
        params.put("ReturnQuantity", mRefundNum + "");//退货数量
        params.put("Amount", mTotalPrice + "");//退款金额
        params.put("ReasonType", mRefundReason + "");//退款理由
        params.put("Reason", intro.getText().toString());//退说明
        params.put("OrderId", mOrderId);//订单id
        params.put("OrderItemId", mOrderItemId);//子订单ID
        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBAL_GET_REFUND)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        RootBean rootBean = gson.fromJson(response, RootBean.class);
                        if (Integer.parseInt(rootBean.ErrCode) == 0) {
                            String string = getResources().getString(R.string.refund_succeed_hint);
                            Toast.makeText(ApplyFordrawbackActivity.this, string, Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ApplyFordrawbackActivity.this, rootBean.ResponseMsg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * 条件选择器
     *
     * @param btn 按钮
     * @param str title
     */
    private void choosePicture(final int btn, String str) {

        final ArrayList<String> datas = initConditionData(btn);

        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                if (btn == R.id.rl_refund_type) {//选择退款类型
                    tv_text_type.setText(datas.get(options1));
                    getNetRefundCause(options1 + 1);//默认是退款
                    mRefundType = options1 + 1;
                    if (mRefundType == 1) {
                        checkNum.setVisibility(View.GONE);
                    } else {
                        checkNum.setVisibility(View.VISIBLE);
                    }
                } else if (btn == R.id.rl_refund_cause) {//选择退款原因
                    RefundReasonBean.ReasonBean reasonBean = mRefundReasonBean.getReason().get(options1);

                    tv_text.setText(reasonBean.getReasonDesc());
                    mRefundReason = reasonBean.getReasonId();
                    mSellerBear = reasonBean.isSellerBear();
                } else if (btn == R.id.checkNum) {
                    num.setText(datas.get(options1));
                    mRefundNum = Integer.parseInt(datas.get(options1));
                }

                String refundPrice = ed_input_money.getText().toString();

                if (!TextUtils.isEmpty(refundPrice)) {
                    totalRefundPrice(refundPrice);
                }
            }
        })
                .setSubmitText(getResources().getString(R.string.commit))//确定按钮文字
                .setCancelText(getResources().getString(R.string.cancel))//取消按钮文字
                .setTitleText(str)//标题
                .setSubCalSize(14)//确定和取消文字大小
                .setTitleSize(16)//标题文字大小
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.argb(255, 19, 43, 131))//确定按钮文字颜色
                .setCancelColor(Color.argb(255, 19, 43, 131))//取消按钮文字颜色
                .setContentTextSize(16)//滚轮文字大小
                .build();
        pvOptions.setPicker(datas);
        pvOptions.show();

    }

    /**
     * 初始化选择条件数据
     *
     * @param btn 选择按钮
     * @return 条件数据
     */
    @NonNull
    private ArrayList<String> initConditionData(int btn) {
        ArrayList<String> datas = new ArrayList<>();

        if (btn == R.id.rl_refund_type) {//选择退款类型
            String refundType1 = getResources().getString(R.string.refund_type1);
            String refundType2 = getResources().getString(R.string.refund_type2);
            datas.add(refundType1);
            datas.add(refundType2);
        } else if (btn == R.id.rl_refund_cause) {//选择退款原因
            for (int i = 0; i < mRefundReasonBean.getReason().size(); i++) {
                datas.add(mRefundReasonBean.getReason().get(i).getReasonDesc());
            }
        } else if (btn == R.id.checkNum) {
            datas.add("0");
            for (int i = 0; i < mRefundProductsBean.getMacRefundGoodCount(); i++) {
                datas.add(String.valueOf(i + 1));
            }
        }
        return datas;
    }

    /**
     * 退款金额输入框监听
     */
    private void setListener() {
        //文本框变化的监听
        ed_input_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (TextUtils.isEmpty(s)) {
                    return;
                }
                if (Double.parseDouble(s.toString()) > mRefundProductsBean.getMaxRefundAmount()) {//输入的金额大于最大金额
                    showPriceDialog();
                    return;
                }
                totalRefundPrice(s.toString());
            }
        });
    }

    /**
     * 合计退款金额
     *
     * @param refundPrice 输入的退款金额
     */
    private void totalRefundPrice(String refundPrice) {
        if (mSellerBear) {//卖家承担运费
            mTotalPrice = Double.parseDouble(refundPrice);
            tv_return_sum.setText(getString(R.string.money_type) + Utils.doubleSave2(mTotalPrice));

        } else {
            //总计
            //四舍五入保留两位
            double temp2 = Double.parseDouble(refundPrice);
            //总计
            mTotalPrice = temp2 - mServiceFee;
            tv_return_sum.setText(getString(R.string.money_type) + Utils.doubleSave2(mTotalPrice));
        }
    }

    /**
     * 显示金额提示框
     */
    private void showPriceDialog() {
        String string = getResources().getString(R.string.refund_price_hint);
        AlertDialog.Builder priceDialog =
                new AlertDialog.Builder(ApplyFordrawbackActivity.this);
        priceDialog.setMessage(string);
        priceDialog.setPositiveButton(getResources().getString(R.string.commit),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ed_input_money.setText("");
                    }
                });
        //显示对话框
        priceDialog.setCancelable(false);
        priceDialog.show();
    }

    /**
     * 照片的选择
     */
    private void choosePicture() {
        View view = LayoutInflater.from(this).inflate(R.layout.layou_choose_picture, null, false);
        final PopupWindow popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                UiUtils.px2dp(1800), true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setAnimationStyle(R.style.AddressDialogAnim);
        popupWindow.setHeight(UiUtils.dip2px(200));
        popupWindow.showAtLocation(findViewById(R.id.draw), Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, 0);
        Button btn1 = (Button) view.findViewById(R.id.btn1);
        Button btn2 = (Button) view.findViewById(R.id.btn2);
        Button btn3 = (Button) view.findViewById(R.id.btn3);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //从相册选择
                XPermissions.requestPermissions().setRequestCode(203).setShouldShow(true).setPermissions(
                        new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE})
                        .setOnXPermissionsListener(new XPermissionsListener() {
                            @Override
                            public void onXPermissions(int requestCode, int resultCode) {
                                if (resultCode == XPermissions.PERMISSION_SUCCESS) {
                                    //权限申请成功，可以继续往下走
                                    skipPicture();
                                    popupWindow.dismiss();
                                } else {
                                    //权限申请失败，此时应该关闭界面或者退出程序
                                    popupWindow.dismiss();
                                }
                            }
                        }).builder();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //拍照选择

                XPermissions
                        .requestPermissions()
                        .setRequestCode(203)
                        .setShouldShow(true)
                        .setPermissions(new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        })
                        .setOnXPermissionsListener(new XPermissionsListener() {
                            @Override
                            public void onXPermissions(int requestCode, int resultCode) {
                                if (resultCode == XPermissions.PERMISSION_SUCCESS) {
                                    //权限申请成功，可以继续往下走
                                    skipCamera();
                                    popupWindow.dismiss();
                                } else {
                                    popupWindow.dismiss();
                                }
                            }
                        }).builder();

            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消
                popupWindow.dismiss();
            }
        });
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        XPermissions.handlerPermissionResult(requestCode, permissions, grantResults);
    }

    /**
     * 相册
     */
    private void skipPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    /**
     * 拍照
     */
    public void skipCamera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 判断存储卡是否可以用，可用进行存储
        if (UiUtils.hasSdcard()) {
            tempFile = new File(UiUtils.getSDcardPath(), System.currentTimeMillis() + ".jpg");
            Uri uri = Uri.fromFile(tempFile);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        } else {
            String string = getResources().getString(R.string.memory_card_is_not_available);
            Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
            return;
        }
        startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
    }

    /**
     * 返回数据的回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PHOTO_REQUEST_CAREMA:  //相机拍照

                if (TextUtils.isEmpty(tempFile.getAbsolutePath())) {
                    return;
                } else {
                    Glide.with(getApplicationContext())
                            .load(tempFile.getAbsolutePath())
                            .error(R.drawable.zhanwei1)
                            .placeholder(R.drawable.zhanwei3)
                            .into(imageView);

                    File file = new File(tempFile.getAbsolutePath());
                    if (!file.exists()) {
                        String string = getResources().getString(R.string.file_does_not_exist);
                        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    multiFileUpload(file);
                }
                break;
            case PHOTO_REQUEST_GALLERY://从相册选择头像
                if (data == null) {
                    return;
                } else {
                    Uri uri = data.getData();
                    Glide.with(getApplicationContext())
                            .load(uri)
                            .error(R.drawable.zhanwei1)
                            .placeholder(R.drawable.zhanwei3)
                            .into(imageView);

                    File file = null;
                    try {
                        file = new File(new URI(uri.toString()));
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    multiFileUpload(file);
                }

                break;
            default:
                break;
        }
    }

    /**
     * 头像上传
     *
     * @param file 头像文件
     */
    public void multiFileUpload(final File file) {
        showProgress(this,getResources().getString(R.string.be_uploading));
        Map<String, String> params = new HashMap<>();
        params.put("userId", mUserId);

        OkHttpUtils.post()//请求方式
                .addFile("mFile", "01.jpg", file)
                .url(GlobalContent.GLOBAL_IMGGE_URL)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                        dismissProgress();
                        multiFileUpload(file);
                    }

                    @Override
                    public void onResponse(String response) {
                        dismissProgress();
                        Logger.e(  "response" + response);
                        UploadingImageBean uploadingImageBean = new Gson().fromJson(response, UploadingImageBean.class);
                        String path = uploadingImageBean.getFiles().get(0).getPath();
                    }
                });
    }

    /**
     * 初始化布局
     */
    private void initView() {

        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);//返回按钮
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getResources().getString(R.string.apply_refund));

        findViewById(R.id.rl_commodity_info).setBackgroundColor(Color.WHITE);
        iv_order_icon = (ImageView) findViewById(R.id.iv_order_icon);
        tv_order_new_price = (TextView) findViewById(R.id.tv_order_new_price);
        tv_order_old_price = (TextView) findViewById(R.id.tv_order_old_price);
        tv_order_commodity_name = (TextView) findViewById(R.id.tv_order_commodity_name);
        tv_order_size_color = (TextView) findViewById(R.id.tv_order_size_color);
        tv_order_num = (TextView) findViewById(R.id.tv_order_num);

        RelativeLayout rl_refund_type = (RelativeLayout) findViewById(R.id.rl_refund_type);
        rl_refund_type.setOnClickListener(this);
        tv_text_type = (TextView) findViewById(R.id.tv_text_type);

        RelativeLayout rl_refund_cause = (RelativeLayout) findViewById(R.id.rl_refund_cause);//退款原因
        rl_refund_cause.setOnClickListener(this);
        tv_text = (TextView) findViewById(R.id.tv_text);//退款原因

        //退款数量
        checkNum = (RelativeLayout) findViewById(R.id.checkNum);
        num = (TextView) findViewById(R.id.num);//退款数量

        tv_return_all_price = (TextView) findViewById(R.id.tv_return_all_price);

        ed_input_money = (EditText) findViewById(R.id.ed_input_money);//退款金额
        tv_procedure = (TextView) findViewById(R.id.tv_procedure);//手续费
        tv_return_sum = (TextView) findViewById(R.id.tv_return_sum);//退款总计
        intro = (EditText) findViewById(R.id.intro);//退款说明

        iv_voucher1 = (ImageView) findViewById(R.id.iv_voucher1);
        iv_voucher2 = (ImageView) findViewById(R.id.iv_voucher2);
        iv_voucher3 = (ImageView) findViewById(R.id.iv_voucher3);
        iv_voucher1.setOnClickListener(this);
        iv_voucher2.setOnClickListener(this);
        iv_voucher3.setOnClickListener(this);

        Button bt_submit = (Button) findViewById(R.id.bt_submit);//提交按钮


        iv_back.setOnClickListener(this);
        checkNum.setOnClickListener(this);
        bt_submit.setOnClickListener(this);

        setListener();

    }

    public void showProgress(Context context, String hint) {
        if(mDialog == null) {
            mDialog = new Dialog(context, R.style.Dialog);
        }
        View view =View.inflate(context, R.layout.progressbar, null);
        TextView textView = (TextView) view.findViewById(R.id.pb_text);
        textView.setText(hint);
        mDialog.setContentView(view);
        mDialog.show();
        mDialog.setCancelable(false);
    }

    public void dismissProgress() {

        if ( null  != mDialog && mDialog.isShowing() ) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    /**
     * 点击屏幕空白区域隐藏软键盘（方法2）
     * <p>根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘</p>
     * <p>需重写dispatchTouchEvent</p>
     * <p>参照以下注释代码</p>
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    // 获取InputMethodManager，隐藏软键盘
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
