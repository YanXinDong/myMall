package com.BFMe.BFMBuyer.activity;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.adapter.CommentListAdapter;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.javaBean.OrderDetailBean;
import com.BFMe.BFMBuyer.javaBean.ProductComment;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.javaBean.UploadingImageBean;
import com.BFMe.BFMBuyer.utils.CacheUtils;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.ImageUtils;
import com.BFMe.BFMBuyer.utils.OperateStringUtlis;
import com.BFMe.BFMBuyer.utils.PictureUtil;
import com.BFMe.BFMBuyer.utils.TimeUtils;
import com.BFMe.BFMBuyer.utils.UiUtils;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.BFMe.BFMBuyer.utils.xpermission.listener.XPermissionsListener;
import com.BFMe.BFMBuyer.utils.xpermissions.XPermissions;
import com.BFMe.BFMBuyer.view.MyListView;
import com.BFMe.BFMBuyer.view.RatingBar;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 评价页面
 */
public class CommentDetailActivity extends IBaseActivity implements View.OnClickListener {
    private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果

    private RatingBar rbStart1;
    private RatingBar rbStart2;
    private RatingBar rbStart3;

    private int start1 = 5;
    private int start2 = 5;
    private int start3 = 5;

    private CheckBox cbCommOff;
    private Button btnCommit;
    private ImageView ivPicture1;
    private ImageView ivPicture2;
    private ImageView ivPicture3;

    private File tempFile;
    private int position;
    private String userId;
    private String id;//订单ID
    private ArrayList<String> pathList;  //上传图片的图片的详解

    private Gson gson;
    private UploadingImageBean mImageUrls;//上传图片返回的地址

    private MyListView lv_comment;
    private List<OrderDetailBean.OrderItemInfoBean> OrderItemInfo;
    private CommentListAdapter mCommentListAdapter;

    private HashMap<Integer, String> mContexts = new HashMap<>();
    private HashMap<Integer, Integer> mScores = new HashMap<>();
    private String mOrderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        XPermissions.init(this);
        initView();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {

        pathList = new ArrayList<>(3);
        gson = new Gson();

        userId = OperateStringUtlis.getUerIdBack(CacheUtils.getString(CommentDetailActivity.this, GlobalContent.USER));
        mOrderId = getIntent().getStringExtra(GlobalContent.ORDER_ID);
        getCommentInfo();
    }

    private void getCommentInfo() {
        showProgress();
        Map<String, String> params = new HashMap<>();
        params.put("UserId", userId);
        params.put("OrderId", mOrderId);
        OkHttpUtils
                .post()
                .params(params)
                .url(GlobalContent.GLOBAL_ORDER_DEATIL)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                        dismissProgress();
                    }

                    @Override
                    public void onResponse(String response) {
                        dismissProgress();
                        Logger.e(  "订单详情" + response);
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (Integer.parseInt(rootBean.ErrCode) == 0) {
                            OrderDetailBean orderDetailBean = mGson.fromJson(rootBean.Data, OrderDetailBean.class);
                            id = orderDetailBean.getId();
                            OrderItemInfo = orderDetailBean.getOrderItemInfo();
                            mCommentListAdapter = new CommentListAdapter(CommentDetailActivity.this, orderDetailBean.getOrderItemInfo());
                            lv_comment.setAdapter(mCommentListAdapter);
                            initListener();
                        } else {
                            Toast.makeText(CommentDetailActivity.this, rootBean.ResponseMsg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * 监听
     */
    private void initListener() {

        mCommentListAdapter.setOnClickListenerDelete(new CommentListAdapter.OnClickListenerUploading() {
            @Override
            public void onUploadingClick(int mPosition, int itemPosition, View view) {
                choosePicture(view);
                position = mPosition;
            }

            @Override
            public void onReturnView(View view1, View view2, View view3) {
                ivPicture1 = (ImageView) view1;
                ivPicture2 = (ImageView) view2;
                ivPicture3 = (ImageView) view3;
            }

            @Override
            public void onReturnDatas(HashMap<Integer, String> contexts, HashMap<Integer, Integer> scores) {
                mContexts = contexts;
                mScores = scores;
            }
        });

        rbStart1.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float ratingCount) {
                start1 = (int) ratingCount;
            }
        });
        rbStart2.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float ratingCount) {
                start2 = (int) ratingCount;
            }
        });
        rbStart3.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float ratingCount) {
                start3 = (int) ratingCount;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit:
                //效验
                for (int i = 0; i < OrderItemInfo.size(); i++) {
                    if (TextUtils.isEmpty(mContexts.get(i))) {
                        showToast(getResources().getString(R.string.input_comment_content));
                        return;
                    }
                }

                //上传图片
                showProgress(getResources().getString(R.string.is_submit));//显示进度条
                if (pathList != null && pathList.size() > 0) {
                    multiFileUpload();
                } else {
                    //提交商品评价的详情
                    submitCommit();
                }

                break;
            default:
                break;
        }
    }

    /**
     * 多文件上传
     */
    public void multiFileUpload() {

        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);

        if (pathList != null) {
            List<File> files = new ArrayList<>();
            for (int i = 0; i < pathList.size(); i++) {
                File file1_1 = new File(pathList.get(i));
                if (!file1_1.exists()) {
                    showToast(getResources().getString(R.string.file_does_not_exist));
                    return;
                } else {
                    files.add(file1_1);
                }
            }

            OkHttpUtils.post()//请求方式
                    .addFile("mFile", "01.jpg", files)//
                    .url(GlobalContent.GLOBAL_IMGGE_URL)
                    .params(params)//
                    .build()//
                    .execute(new StringCallback() {
                        @Override
                        public void onError(okhttp3.Request request, Exception e) {
                            Logger.e(  "上传onError()" + e.toString());
                        }

                        @Override
                        public void onResponse(String response) {
                            Logger.e(  "上传成功" + response);
                            mImageUrls = gson.fromJson(response, UploadingImageBean.class);
                            //提交商品评价的详情
                            submitCommit();
                        }
                    });
        }

    }

    /**
     * 提交评价
     */
    private void submitCommit() {
        Map<String, String> params = new HashMap<>();
        params.put("UserId", userId);
        params.put("OrderId", id);
        params.put("PackMark", start1 + "");//宝贝描述相符度
        params.put("DeliveryMark", start2 + "");//卖家发货速度评分
        params.put("ServiceMark", start3 + "");//卖家服务态度评分
        List<ProductComment.ProductCommentData> productCommentDatas = new ArrayList<>();

        for (int i = 0; i < OrderItemInfo.size(); i++) {
            ProductComment.ProductCommentData productComment = new ProductComment.ProductCommentData();
            productComment.setSubOrderId(OrderItemInfo.get(i).getId());//子订单ID
            productComment.setContent(mContexts.get(i));//评价内容
            productComment.setStar(mScores.get(i));//商品评分
            String urls = null;
            if (mImageUrls != null) {
                for (int j = 0; j < mImageUrls.getFiles().size(); j++) {
                    if(urls!=null){
                        if(j==mImageUrls.getFiles().size()-1){
                            urls = urls + mImageUrls.getFiles().get(j).getPath();
                        }else{
                            urls = urls + mImageUrls.getFiles().get(j).getPath() + ",";
                        }

                    }else{
                        if(j == mImageUrls.getFiles().size()-1){
                            urls = mImageUrls.getFiles().get(j).getPath();
                        }else{
                            urls = mImageUrls.getFiles().get(j).getPath() + ",";
                        }

                    }

                }
                productComment.setPics(urls);//图片地址
            } else {
                urls = "";
                productComment.setPics(urls);//图片地址
            }
            productCommentDatas.add(productComment);
            params.put("Productcomment", new Gson().toJson(productCommentDatas));
        }
        Logger.e(  "上传参数==" + params.toString());
        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBAL_COMMIT_COMMENT)
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
                        String responseMsg = rootBean.ResponseMsg;
                        if ("success".equals(responseMsg)) {
                            showToast(getResources().getString(R.string.comment_succeed));
                            finish();
                        } else {
                            showToast(responseMsg);
                        }
                    }
                });
    }


    /**
     * 照片的选择
     *
     * @param img
     */
    private void choosePicture(View img) {

        View view = LayoutInflater.from(this).inflate(R.layout.layou_choose_picture, null, false);
        final PopupWindow popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                UiUtils.px2dp(1800), true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setAnimationStyle(R.style.AddressDialogAnim);
        popupWindow.setHeight(UiUtils.dip2px(200));
        popupWindow.showAtLocation(findViewById(R.id.ll_comm), Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, 0);
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
            showToast(getResources().getString(R.string.memory_card_is_not_available));
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

        ImageView view = null;
        switch (requestCode) {
            case PHOTO_REQUEST_CAREMA:
                Logger.e(  "tupian0:" + tempFile.getAbsolutePath());
                Logger.e(  "position0:" + position);
                if (resultCode != RESULT_OK) {
                    // 取消照相后，删除已经创建的临时文件。
                    PictureUtil.deleteTempFile(tempFile.getAbsolutePath());
                    showToast(getResources().getString(R.string.operation_cancel));
                    return;
                } else {
                    if (position == 1) {
                        view = ivPicture1;
                        ivPicture2.setVisibility(View.VISIBLE);
                        ivPicture3.setVisibility(View.GONE);
                    }
                    if (position == 2) {
                        view = ivPicture2;
                        ivPicture2.setVisibility(View.VISIBLE);
                        ivPicture3.setVisibility(View.VISIBLE);
                    }
                    if (position == 3) {
                        view = ivPicture3;
                        ivPicture2.setVisibility(View.VISIBLE);
                        ivPicture3.setVisibility(View.VISIBLE);
                    }
                    // 添加到图库,这样可以在手机的图库程序中看到程序拍摄的照片
                    PictureUtil.galleryAddPic(this, tempFile.getAbsolutePath());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                pathList.add(ImageUtils.getImage(PictureUtil
                                        .getSmallBitmap(tempFile.getAbsolutePath()), TimeUtils.getCurTimeMills() + ""));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                    Logger.e(  "tupian3" + view.toString());
                    Glide.with(this).load(tempFile.getAbsolutePath()).placeholder(R.drawable.zhanwei1).into(view);
                }
                break;
            case PHOTO_REQUEST_GALLERY://相册回调
                Logger.e(  "position1:" + position);
                if (data == null) {
                    showToast(getResources().getString(R.string.operation_cancel));
                    return;
                } else {
                    if (position == 1) {
                        view = ivPicture1;
                        Logger.e(  "111111==");
                        ivPicture2.setVisibility(View.VISIBLE);
                        ivPicture3.setVisibility(View.GONE);
                    }
                    if (position == 2) {
                        view = ivPicture2;
                        ivPicture2.setVisibility(View.VISIBLE);
                        ivPicture3.setVisibility(View.VISIBLE);
                    }
                    if (position == 3) {
                        view = ivPicture3;
                        ivPicture2.setVisibility(View.VISIBLE);
                        ivPicture3.setVisibility(View.VISIBLE);
                    }

                    final Uri uri2 = data.getData();
                    Logger.e(  "tupian1:" + uri2);
                    Logger.e(  "tupian3" + view.toString());

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                pathList.add(ImageUtils.getImage(PictureUtil
                                        .getSmallBitmap(getRealFilePath(CommentDetailActivity.this, uri2)), TimeUtils.getCurTimeMills() + ""));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    Glide.with(getApplicationContext()).load(uri2).placeholder(R.drawable.zhanwei1).into(view);
                }


                break;
            default:
                break;
        }
    }

    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri)
            return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 初始化布局
     */
    private void initView() {

        setBackButtonVisibility(View.VISIBLE);
        tv_title.setText(getResources().getString(R.string.product_commnet));
        //其他星级评价
        rbStart1 = (RatingBar) findViewById(R.id.rb_start1);
        rbStart2 = (RatingBar) findViewById(R.id.rb_start2);
        rbStart3 = (RatingBar) findViewById(R.id.rb_start3);
        //匿名评价
        cbCommOff = (CheckBox) findViewById(R.id.cb_comm_off);
        //提交评价
        btnCommit = (Button) findViewById(R.id.btn_commit);
        //评价列表
        lv_comment = (MyListView) findViewById(R.id.lv_comment);


        btnCommit.setOnClickListener(this);
    }

    /**
     * 点击屏幕空白区域隐藏软键盘（方法1）
     * <p>在onTouch中处理，未获焦点则隐藏</p>
     * <p>参照以下注释代码</p>
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
        return R.layout.activity_comment_detail;
    }
}
