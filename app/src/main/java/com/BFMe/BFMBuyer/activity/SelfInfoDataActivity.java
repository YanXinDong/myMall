package com.BFMe.BFMBuyer.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.javaBean.IdentityInfoBean;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.javaBean.UploadingImageBean;
import com.BFMe.BFMBuyer.ugc.bean.PersonalCenterInfo;
import com.BFMe.BFMBuyer.utils.CacheUtils;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.ImageUtils;
import com.BFMe.BFMBuyer.utils.StringUtils;
import com.BFMe.BFMBuyer.utils.TimeUtils;
import com.BFMe.BFMBuyer.utils.UiUtils;
import com.BFMe.BFMBuyer.utils.UriUtils;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.BFMe.BFMBuyer.utils.xpermission.listener.XPermissionsListener;
import com.BFMe.BFMBuyer.utils.xpermissions.XPermissions;
import com.BFMe.BFMBuyer.view.GlideCircleTransform;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

import static com.zhy.http.okhttp.OkHttpUtils.post;

/**
 * Description：个人信息页面
 * Created by :奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2016/9/14 17:28
 */
public class SelfInfoDataActivity extends IBaseActivity implements View.OnClickListener {
    private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private static final int NICK_CODE = 4;//昵称
    private static final int SIGN_CODE = 5;//签名

    private RelativeLayout rlHead;
    private ImageView ivHead;
    private RelativeLayout rlNick;
    private TextView ivNickname;
    private String userId;
    private RelativeLayout rlEditInfodata;
    private TextView tvSignname;
    private RelativeLayout rlMima;
    private RelativeLayout rlSettingPhone;
    private TextView tvSettingPhone;
    private TextView tv_identity_hint;
    private String crop_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        XPermissions.init(this);
        getUserPhone();
        getInfoFromNet();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserNameAndCardNo();
    }

    /**
     * 获取身份认证信息
     */
    private void getUserNameAndCardNo() {
        Logger.e("UserId   " + userId);
        OkHttpUtils
                .post()
                .addParams("UserId", userId)
                .url(GlobalContent.GET_USER_NAME_AND_CARD_NO)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("获取身份认证信息==" + response);
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (rootBean.ErrCode.equals("0")) {
                            IdentityInfoBean identityInfoBean = mGson.fromJson(rootBean.Data, IdentityInfoBean.class);
                            IdentityInfoBean.DataBean data = identityInfoBean.getData();
                            if (StringUtils.isSpace(data.getCardNo()) || StringUtils.isSpace(data.getRealName())) {
                                tv_identity_hint.setText(getString(R.string.no_authentication));
                                tv_identity_hint.setTextColor(getResources().getColor(R.color.color_black_ff666666));
                            } else {
                                String cardNo = data.getCardNo();
                                cardNo = cardNo.substring(0, 2) + "********" + cardNo.substring(cardNo.length()-2,cardNo.length());
                                tv_identity_hint.setText(cardNo);
                                tv_identity_hint.setTextColor(getResources().getColor(R.color.colorZhu));
                            }
                        } else {
                            showToast(rootBean.ResponseMsg);
                        }
                    }
                });
    }


    /**
     * 初始化布局
     */
    private void initView() {
        setBackButtonVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.personage_info));

        userId = CacheUtils.getString(this, GlobalContent.USER);
        rlHead = (RelativeLayout) findViewById(R.id.rl_head);
        ivHead = (ImageView) findViewById(R.id.iv_head);
        rlNick = (RelativeLayout) findViewById(R.id.rl_nick);
        ivNickname = (TextView) findViewById(R.id.iv_nickname);
        rlEditInfodata = (RelativeLayout) findViewById(R.id.rl_edit_infodata);
        tvSignname = (TextView) findViewById(R.id.tv_signname);


        rlMima = (RelativeLayout) findViewById(R.id.rl_mima);
        rlSettingPhone = (RelativeLayout) findViewById(R.id.rl_setting_phone);
        tvSettingPhone = (TextView) findViewById(R.id.tv_setting_phone);
        tv_identity_hint = (TextView) findViewById(R.id.tv_identity_hint);

        rlHead.setOnClickListener(this);
        rlNick.setOnClickListener(this);
        rlMima.setOnClickListener(this);
        rlSettingPhone.setOnClickListener(this);
        rlEditInfodata.setOnClickListener(this);

        findViewById(R.id.rl_identity_authentication).setOnClickListener(this);
    }

    /**
     * 获取用户信息
     */
    public void getInfoFromNet() {
        OkHttpUtils
                .post()
                .url(GlobalContent.POST_PERSONAL_CENTER)
                .addParams("userid", mUserId)
                .addParams("searchUserId", mUserId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (rootBean.ErrCode.equals("0")) {
                            PersonalCenterInfo userInfo2 = new Gson().fromJson(rootBean.Data, PersonalCenterInfo.class);
                            Glide.with(getApplicationContext())
                                    .load(userInfo2.getImageUrl()).transform(new GlideCircleTransform(SelfInfoDataActivity.this))
                                    .error(R.drawable.zhanwei3).placeholder(R.drawable.zhanwei3)
                                    .into(ivHead);
                            ivNickname.setText(userInfo2.getUserName());
                            tvSignname.setText(userInfo2.getIntroduceMyself());
                        }
                    }
                });
    }

    /**
     * 获取当前用户的手机号
     */
    private void getUserPhone() {

        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBAL_GET_PHONE)
                .addParams("UserId", userId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        RootBean rootBean = new Gson().fromJson(response, RootBean.class);
                        Logger.e("response" + response);
                        if (Integer.parseInt(rootBean.ErrCode) == 0) {
                            String data = rootBean.Data;
                            if(data.length()==11){
                                data = data.substring(0, 2) + "****" + data.substring(9);
                            }
                            data = data.substring(0, 2) + "****" + data.substring(data.length()-2,data.length());
                            tvSettingPhone.setText(data);
                        }
                    }
                });
    }

    @Override
    public int initLayout() {
        return R.layout.activity_selfdata;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_head:
                choosePicture();
                break;
            case R.id.rl_nick:
                Intent intentNick = new Intent(SelfInfoDataActivity.this, ChangeNickActivity.class);
                startActivityForResult(intentNick, NICK_CODE);
                startAnim();
                break;
            case R.id.rl_setting_phone:
                startActivity(new Intent(SelfInfoDataActivity.this, ChangeTelephoneActivity.class));
                startAnim();
                break;
            case R.id.rl_mima:
                startActivity(new Intent(SelfInfoDataActivity.this, ChangePasswordActivity.class));
                startAnim();
                break;
            case R.id.rl_edit_infodata:
                Intent intent = new Intent(SelfInfoDataActivity.this, EditSignActivity.class);
                intent.putExtra("Content", tvSignname.getText().toString());
                intent.putExtra("IS_FROM_SIGN", true);
                startActivityForResult(intent, SIGN_CODE);
                startAnim();
                break;

            case R.id.rl_identity_authentication://身份认证
                startActivity(new Intent(SelfInfoDataActivity.this, EditIdentityActivity.class));
                startAnim();
                break;

        }
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
        popupWindow.showAtLocation(findViewById(R.id.ll_selfdata), Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, 0);
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
                                    skipPicture();
                                    popupWindow.dismiss();
                                } else {
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
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intent.setAction(Intent.ACTION_PICK);
        } else {
            intent.setAction(Intent.ACTION_GET_CONTENT);
//            intent.setAction(Intent.ACTION_PICK);
        }
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }


    /**
     * 拍照
     */
    public void skipCamera() {
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
            imgUriOri = FileProvider.getUriForFile(this, "com.BFMe.BFMBuyer.fileprovider", oriPhotoFile);
        }
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUriOri);
        startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
    }

    /**
     * 返回数据的回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.e("data:" + data + "requestCode:" + requestCode + "resultCode:" + resultCode);
        switch (requestCode) {
            case PHOTO_REQUEST_CAREMA:  //相机拍照
                addPicToGallery(imgPathOri);
                cropPhoto(imgUriOri);
                break;
            case PHOTO_REQUEST_GALLERY://从相册选择头像
                if (data != null) {
                    Uri imgUriSel = data.getData();
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                        //打开相册会返回一个经过图像选择器安全化的Uri，直接放入裁剪程序会不识别，抛出[暂不支持此类型：华为7.0]
                        //formatUri会返回根据Uri解析出的真实路径
                        String imgPathSel = UriUtils.formatUri(this, imgUriSel);
                        //根据真实路径转成File,然后通过应用程序重新安全化，再放入裁剪程序中才可以识别
                        cropPhoto(FileProvider.getUriForFile(this, "com.BFMe.BFMBuyer.fileprovider", new File(imgPathSel)));

                    } else {
                        cropPhoto(imgUriSel);
                    }
                }
                break;

            case PHOTO_REQUEST_CUT:

                addPicToGallery(imgPathCrop);
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(imgPathCrop, bmOptions);

                // Determine how much to scale down the image
                int scaleFactor = Math.min(1, 1);

                // Decode the image file into a Bitmap sized to fill the View
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                bmOptions.inPurgeable = true;

                Bitmap bitmap = BitmapFactory.decodeFile(imgPathCrop, bmOptions);
                try {
                    String imageSrc = ImageUtils.getImage(bitmap, TimeUtils.getCurTimeMills() + "");
                    multiFileUpload(imageSrc);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case NICK_CODE:
                if (data != null) {
                    ivNickname.setText(data.getStringExtra("NICK"));
                }
                break;
            case SIGN_CODE:
                if (data != null) {
                    tvSignname.setText(data.getStringExtra("SIGN"));
                }
                break;
            default:
                break;
        }
    }


    /**
     * 头像上传
     *
     * @param imageSrc
     */
    public void multiFileUpload(final String imageSrc) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        File file1_1 = new File(imageSrc);
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
                        multiFileUpload(imageSrc);
                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("response" + response);
                        UploadingImageBean uploadingImageBean = new Gson().fromJson(response, UploadingImageBean.class);
                        String path = uploadingImageBean.getFiles().get(0).getPath();
                        ChangeUserLogo(path);
                        ChangeIMUserLogo(path);

                        Glide.with(getApplicationContext())
                                .load(path).transform(new GlideCircleTransform(SelfInfoDataActivity.this))
                                .error(R.drawable.zhanwei3).placeholder(R.drawable.zhanwei3)
                                .into(ivHead);
                    }
                });
    }

    /**
     * 同步更新云信的用户头像
     *
     * @param path
     */
    private void ChangeIMUserLogo(String path) {
        Map<UserInfoFieldEnum, Object> fields = new HashMap<>(1);
        fields.put(UserInfoFieldEnum.AVATAR, path);
        NIMClient.getService(UserService.class).updateUserInfo(fields)
                .setCallback(new RequestCallbackWrapper<Void>() {
                    @Override
                    public void onResult(int i, Void aVoid, Throwable throwable) {
                        Logger.e("头像更新成功");
                    }
                });
    }

    /**
     * 更新头像
     *
     * @param path 上传图片返回的图片路径
     */
    private void ChangeUserLogo(final String path) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", mUserId);
        params.put("Logo", path);
        OkHttpUtils
                .post()
                .url(GlobalContent.GLABAL_LOGO_URL)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        RootBean rootBean = new Gson().fromJson(response, RootBean.class);
                        Logger.e("返回值：" + rootBean.ErrCode);
                    }
                });
    }


    /***************************
     * 7.0适配图片裁剪***************start
     *********************************************************/

    //原图像 路径
    private static String imgPathOri;
    //裁剪图像 路径
    private static String imgPathCrop;
    //原图像 URI
    private Uri imgUriOri;
    //裁剪图像 URI
    private Uri imgUriCrop;

    /**
     * 创建原图像保存的文件
     *
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

    /**
     * 把图像添加进系统相册
     *
     * @param imgPath 图像路径
     */
    private void addPicToGallery(String imgPath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imgPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

    private void setWindowAlpha(float alpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        getWindow().setAttributes(lp);
    }

    /**
     * 裁剪图片
     *
     * @param uri 需要 裁剪图像的Uri
     */
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        File cropPhotoFile = null;
        try {
            cropPhotoFile = createCropImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (cropPhotoFile != null) {

            //7.0 安全机制下不允许保存裁剪后的图片
            //所以仅仅将File Uri传入MediaStore.EXTRA_OUTPUT来保存裁剪后的图像
            imgUriCrop = Uri.fromFile(cropPhotoFile);

            intent.setDataAndType(uri, "image/*");
            intent.putExtra("crop", true);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 300);
            intent.putExtra("outputY", 300);
            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUriCrop);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivityForResult(intent, PHOTO_REQUEST_CUT);

        }
    }

    /**
     * 创建裁剪图像保存的文件
     *
     * @return
     * @throws IOException
     */
    private File createCropImageFile() throws IOException {
        String imgNameCrop = "HomePic_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File pictureDirCrop = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/CropPicture");
        if (!pictureDirCrop.exists()) {
            pictureDirCrop.mkdirs();
        }
        File image = File.createTempFile(
                imgNameCrop,         /* prefix */
                ".jpg",             /* suffix */
                pictureDirCrop      /* directory */
        );
        imgPathCrop = image.getAbsolutePath();
        return image;
    }


    /******************************************end*********************************************************/

}
