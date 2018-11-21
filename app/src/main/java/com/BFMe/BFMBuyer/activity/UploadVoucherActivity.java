package com.BFMe.BFMBuyer.activity;

/**
 * Created by BFMe.miao on 2018/2/28.
 */

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.javaBean.BankInfoBean;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.javaBean.UploadingImageBean;
import com.BFMe.BFMBuyer.ugc.activity.PhotoPickerActivity;
import com.BFMe.BFMBuyer.utils.CacheUtils;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.ImageUtils;
import com.BFMe.BFMBuyer.utils.PictureUtil;
import com.BFMe.BFMBuyer.utils.TimeUtils;
import com.BFMe.BFMBuyer.utils.UiUtils;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.BFMe.BFMBuyer.utils.xpermission.listener.XPermissionsListener;
import com.BFMe.BFMBuyer.utils.xpermissions.XPermissions;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.zhy.http.okhttp.OkHttpUtils.post;

/**
 * 上传转账凭证
 */
public class UploadVoucherActivity extends IBaseActivity implements View.OnClickListener {

    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private TextView tv_order_number;
    private TextView tv_bank_name;
    private TextView tv_order_money;
    private TextView tv_bank_account;
    private TextView tv_bank_account_name;
    private ImageView mIvUpload;
    private String imageString;
    private String orderId;
    private String totleMoney;
    private String userId;

    @Override
    public int initLayout() {
        return R.layout.activity_uploadvoucher;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        XPermissions.init(this);
        initView();
        initData();
        getBankInfo();

    }


    private void initView() {
        setBackButtonVisibility(View.VISIBLE);
        tv_title.setText(getResources().getString(R.string.upload_voucher));
        tv_order_number = (TextView) findViewById(R.id.tv_order_number);//订单编号
        tv_order_money = (TextView) findViewById(R.id.tv_order_money);//订单总金额
        tv_bank_name = (TextView) findViewById(R.id.tv_bank_name);//银行的名字
        tv_bank_account = (TextView) findViewById(R.id.tv_bank_account);//银行账号
        tv_bank_account_name = (TextView) findViewById(R.id.tv_bank_account_name);//银行账号名
        mIvUpload = (ImageView) findViewById(R.id.iv_upload_voucher);
        Button btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
        mIvUpload.setOnClickListener(this);

    }

    private void initData() {
        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");
        totleMoney = intent.getStringExtra("money");
        tv_order_number.setText(orderId);
        tv_order_money.setText(totleMoney);

        userId = CacheUtils.getString(this, GlobalContent.USER);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit:
                if(TextUtils.isEmpty(imageString)){
                    showToast("请上传支付凭证！");
                    return;
                }
                uploadVoucherImage();
                break;
            case R.id.iv_upload_voucher://上传图片
                choosePicture();
                break;

        }
    }

    private void uploadVoucherImage() {
            Map<String, String> params = new HashMap<>();
            params.put("userId", userId);
            File file1_1 = new File(imageString);
            if (!file1_1.exists()) {
                showToast(getString(R.string.file_does_not_exist));
                return;
            }
            post()//请求方式
                    .addFile("mFile", "01.jpg", file1_1)
                    .url(GlobalContent.GLOBAL_IMGGE_URL)
                    .params(params)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(okhttp3.Request request, Exception e) {

                        }

                        @Override
                        public void onResponse(String response) {
                            Logger.e("response" + response);
                            UploadingImageBean uploadingImageBean = new Gson().fromJson(response, UploadingImageBean.class);
                            String path = uploadingImageBean.getFiles().get(0).getPath();
                            uploadVoucher(path);

                        }
                    });


    }


    private void setDate(BankInfoBean bankInfoBean) {
        tv_bank_name.setText(bankInfoBean.getData().getBankName());
        tv_bank_account.setText(bankInfoBean.getData().getAccount());
        tv_bank_account_name.setText(bankInfoBean.getData().getAccountName());
    }

    /**
     * 获取银行信息
     * GlobalContent.GLOBAL_BANK_INFO
     */
    private void getBankInfo() {
        showProgress();
        OkHttpUtils
                .get()
                .url(GlobalContent.GLOBAL_BANK_INFO)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                        dismissProgress();
                        Logger.e("银行信息 error==="+e);
                    }

                    @Override
                    public void onResponse(String response) {
                        dismissProgress();
                        Gson gson = new Gson();
                        Logger.e("银行信息==="+response);
                        RootBean rootBean = gson.fromJson(response, RootBean.class);
                        if (Integer.parseInt(rootBean.ErrCode) == 0) {
                            String data = rootBean.Data;
                            BankInfoBean bankInfoBean = new Gson().fromJson(data, BankInfoBean.class);
                            setDate(bankInfoBean);
                        }
                    }

                });
    }

    /**
     * 上传支付凭证
     * GlobalContent.GLOBSAL_UPLOAD_VOUCHER
     * @param path
     */
    private void uploadVoucher(String path) {
        showProgress();
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        params.put("orderId", orderId);
        params.put("payVoucherUrl",path);
        Logger.e("上传支付凭证请求参数==="+params);
        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBSAL_UPLOAD_VOUCHER)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                        dismissProgress();
                        showToast(getResources().getString(R.string.upload_voucher_error));
                    }

                    @Override
                    public void onResponse(String response) {
                        dismissProgress();
                        Gson gson = new Gson();
                        Logger.e("上传支付凭证==="+response);
                        RootBean rootBean = gson.fromJson(response, RootBean.class);
                        if (Integer.parseInt(rootBean.ErrCode) == 0) {
                            showToast(getResources().getString(R.string.upload_voucher_success));
                        }else {
                            showToast(getResources().getString(R.string.upload_voucher_error));

                        }
                    }

                });
    }

    //-------------------------相机相册相关-----------------------------------
    private void choosePicture() {
        View v = LayoutInflater.from(this).inflate(R.layout.layou_choose_picture, null, false);
        final PopupWindow popupWindow = new PopupWindow(v, WindowManager.LayoutParams.MATCH_PARENT,
                UiUtils.px2dp(1800), true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setAnimationStyle(R.style.AddressDialogAnim);
        popupWindow.setHeight(UiUtils.dip2px(200));
        popupWindow.showAtLocation(findViewById(R.id.ll_uploading_voucher), Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, 0);
        Button btn1 = (Button) v.findViewById(R.id.btn1);
        Button btn2 = (Button) v.findViewById(R.id.btn2);
        Button btn3 = (Button) v.findViewById(R.id.btn3);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //从相册选择
                Intent intent = new Intent(UploadVoucherActivity.this, PhotoPickerActivity.class);
                intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN,1);
                intent.putExtra("isFrom", true);
                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                UploadVoucherActivity.this.overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
                popupWindow.dismiss();
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


    private void skipCamera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File oriPhotoFile = null;
        try {
            oriPhotoFile = createOriImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            imgUriOri = Uri.fromFile(oriPhotoFile);
        } else {
            imgUriOri = FileProvider.getUriForFile(this,"com.BFMe.BFMBuyer.fileprovider", oriPhotoFile);
        }
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUriOri);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:  //相机拍照
                if (resultCode != RESULT_OK) {
                    showToast(getString(R.string.operation_cancel));
                    return;
                } else {

                    ArrayList<String> list = new ArrayList<>();
                    try {
                        list.add(ImageUtils.getImage(PictureUtil
                                .getSmallBitmap(imgPathOri), TimeUtils.getCurTimeMills() + ""));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    imageString = list.get(0);

                    Glide.with(this).load(imageString).into(mIvUpload);
                }
                break;
            case 2://相册选择的
                if (data == null) {
                    showToast(getString(R.string.operation_cancel));
                    return;
                } else {
                    ArrayList<String> result = data.getStringArrayListExtra("picker_result");
                    if(result.size()>0){
                        imageString = result.get(0);
                        Glide
                                .with(mContext)
                                .load(imageString)
                                .into(mIvUpload);
                    }
                }
                break;
            default:
                break;
        }
    }
    /***************************7.0适配图片裁剪***************start*********************************************************/


    //原图像 路径
    private static String imgPathOri;
    //原图像 URI
    private Uri imgUriOri;

    /**
     * 创建原图像保存的文件
     * @return
     * @throws IOException
     */
    private File createOriImageFile() throws IOException {
        String imgNameOri = "HomePic_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File pictureDirOri = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/OriPicture");
        if (!pictureDirOri.exists()) {
            pictureDirOri.mkdirs();
        }
        File image = File.createTempFile(
                imgNameOri,         /* prefix */
                ".jpg",             /* suffix */
                pictureDirOri       /* directory */
        );
        imgPathOri = image.getAbsolutePath();
        return image;
    }
    /******************************************end*********************************************************/

}
