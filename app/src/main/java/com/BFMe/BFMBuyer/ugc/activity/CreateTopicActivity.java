package com.BFMe.BFMBuyer.ugc.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.javaBean.UploadingImageBean;
import com.BFMe.BFMBuyer.ugc.adapter.CreateTopicAdapter;
import com.BFMe.BFMBuyer.ugc.adapter.RelationProductAdapter;
import com.BFMe.BFMBuyer.ugc.bean.BoundProductBean;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.ImageUtils;
import com.BFMe.BFMBuyer.utils.PictureUtil;
import com.BFMe.BFMBuyer.utils.TimeUtils;
import com.BFMe.BFMBuyer.utils.UiUtils;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.BFMe.BFMBuyer.utils.xpermission.listener.XPermissionsListener;
import com.BFMe.BFMBuyer.utils.xpermissions.XPermissions;
import com.BFMe.BFMBuyer.view.MyListView;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

import static com.zhy.http.okhttp.OkHttpUtils.post;

/**
 * 发布话题
 */
public class CreateTopicActivity extends IBaseActivity implements View.OnClickListener {
    private EditText etContent;
    private TextView tvNumber;
    private GridView gvPic;
    private MyListView lvRelationTopic;
    private RelativeLayout rl_layout2;
    private Button btnCreateTopic;
    private TextView tv_sort_name;

    private CreateTopicAdapter createTopicAdapter;
    private Boolean isAddOrChangePic;//true 添加 false 更换
    private int addOrChangePic;//坐标
    private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PRODUCT_REQUEST = 3;// 关联商品
    private static final int ADD_TOPIC_SORT = 1001;// 添加分类
    private File tempFile;
    private List<BoundProductBean.OrdersListBean> boundList = new ArrayList<>();
    private List<UploadingImageBean.FilesBean> picBean;
    private ArrayList<String> list;
    private ArrayList picResultList;
    private String content;
    private RelationProductAdapter relationProductAdapter;
    private ArrayList<String> orderIdList;
    private ArrayList<String> picLIst;
    private String mTopicCategoryId = "0";
    private EditText et_topic_title;
    private String topicTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackButtonVisibility(View.VISIBLE);
        tv_title.setText("发布话题");
        XPermissions.init(this);

        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        picResultList = intent.getStringArrayListExtra("picker_result");
        String topicSortTitle =  intent.getStringExtra("topic_sort_title");
        mTopicCategoryId = String.valueOf(intent.getLongExtra("topic_sort_id", 0));
        Logger.e("话题分类title=="+topicSortTitle+"===话题分类id==="+mTopicCategoryId);

        if(topicSortTitle != null) {
            tv_sort_name.setText(topicSortTitle);
        }
        setEditTextListener();
        picResultList.add(R.drawable.image_adds);
        createTopicAdapter = new CreateTopicAdapter(this, picResultList);
        gvPic.setAdapter(createTopicAdapter);
        setGvPicData();
    }


    @Override
    protected void onResume() {
        super.onResume();
        relationProductAdapter = new RelationProductAdapter(this, boundList);
        lvRelationTopic.setAdapter(relationProductAdapter);
        relationProductAdapter.setOnDeleteClickListener(new RelationProductAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int position) {
                boundList.remove(position);
                relationProductAdapter.deleteData(position);
            }
        });
    }

    /**
     * 添加或更换图片
     */
    private void setGvPicData() {
        createTopicAdapter.setOnAddPicClickListener(new CreateTopicAdapter.OnAddPicClickListener() {
            @Override
            public void onAddPicClickListener(int position) {
                if (createTopicAdapter.getPicList().size() <= 5 && createTopicAdapter.getPicList().size() - 1 == position) {
                    //add
                    isAddOrChangePic = true;
                    addOrChangePic = position;
                    choosePicture();
                } else {
                    //change
                    isAddOrChangePic = false;
                    addOrChangePic = position;
                    changePicture();
                }

            }
        });
    }

    private void setEditTextListener() {
        etContent.addTextChangedListener(new TextWatcher() {
            CharSequence temp;
            int num = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                int number = num + s.length();
                tvNumber.setText(number + "");
                if (temp.length() > 1000) {
                    showToast(getString(R.string.number_max));
                    int selectionStart = etContent.getSelectionStart();
                    int selectionEnd = etContent.getSelectionEnd();
                    s.delete(selectionStart - 1, selectionEnd);
                }
            }
        });
    }


    @Override
    public int initLayout() {
        return R.layout.activity_create_topic;
    }

    private void initView() {
        etContent = (EditText) findViewById(R.id.et_content);
        tvNumber = (TextView) findViewById(R.id.tv_number);
        tv_sort_name = (TextView)findViewById(R.id.tv_sort_name);
        gvPic = (GridView) findViewById(R.id.gv_pic);
        lvRelationTopic = (MyListView) findViewById(R.id.lv_relation_topic);
        et_topic_title = (EditText) findViewById(R.id.et_topic_title);
        rl_layout2 = (RelativeLayout) findViewById(R.id.rl_layout2);
        
        btnCreateTopic = (Button) findViewById(R.id.btn_create_topic);
        btnCreateTopic.setOnClickListener(this);
        rl_layout2.setOnClickListener(this);
        findViewById(R.id.rl_topic_sort).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create_topic:
                multiFileUpload();
                break;
            case R.id.rl_layout2:
                if (relationProductAdapter.getCount() >= 3) {
                    showToast(getString(R.string.relation_product_max_hint));
                    return;
                }
                Intent intent = new Intent(this, BoundProductActivity.class);
                startActivityForResult(intent, PRODUCT_REQUEST);
                startAnim();
                break;
            case R.id.rl_topic_sort:
                Intent its = new Intent(this, AllTopicSortActivity.class);
                its.putExtra("Title",getString(R.string.add_classify));
                startActivityForResult(its, ADD_TOPIC_SORT);
                startAnim();
                break;
        }
    }

    /**
     * 提交发布的内容
     */
    private void setCreateTopic() {
        topicTitle = et_topic_title.getText().toString();
        if(TextUtils.isEmpty(topicTitle)){
            showToast(getString(R.string.title_empty_hint));
            return;
        }
        orderIdList = new ArrayList<>();
        picLIst = new ArrayList<>();
        for (int i = 0; i < boundList.size(); i++) {
            orderIdList.add(boundList.get(i).getId());
        }
        for (int i = 0; i < picBean.size(); i++) {
            picLIst.add(picBean.get(i).getPath());
        }
        createTopic();
    }

    private void createTopic() {
        content = etContent.getText().toString();
        Map<String, String> map = new HashMap<>();
        map.put("UserId", mUserId);
        map.put("TopicCategoryId", mTopicCategoryId);
        map.put("OrderId", new Gson().toJson(orderIdList));
        map.put("Content", content);
        map.put("Images", new Gson().toJson(picLIst));
        map.put("Title",topicTitle);
        Logger.e("map=="+map.toString());
        OkHttpUtils
                .post()
                .url(GlobalContent.PUBLISH_UGC_TOPIC)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        dismissProgress();
                        Logger.e("发布话题错误" + e);
                    }

                    @Override
                    public void onResponse(String response) {
                        dismissProgress();
                        Logger.e("发布话题" + response);
                        RootBean rootBean = new Gson().fromJson(response, RootBean.class);
                        if ("success".equals(rootBean.ResponseMsg)) {
                            showToast(getString(R.string.operation_succeed));
                            finish();
                            exitAnim();
                        } else {
                            showToast(rootBean.ResponseMsg);
                        }
                    }
                });
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
        popupWindow.showAtLocation(findViewById(R.id.rl_layout_base), Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, 0);
        Button btn1 = (Button) view.findViewById(R.id.btn1);
        Button btn2 = (Button) view.findViewById(R.id.btn2);
        Button btn3 = (Button) view.findViewById(R.id.btn3);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //从相册选择
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


    private void changePicture() {
        View view = LayoutInflater.from(this).inflate(R.layout.layou_choose_picture, null, false);
        final PopupWindow popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                UiUtils.px2dp(1800), true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setAnimationStyle(R.style.AddressDialogAnim);
        popupWindow.setHeight(UiUtils.dip2px(200));
        popupWindow.showAtLocation(findViewById(R.id.rl_layout_base), Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, 0);
        Button btn1 = (Button) view.findViewById(R.id.btn1);
        Button btn2 = (Button) view.findViewById(R.id.btn2);
        btn1.setText(getString(R.string.replace_the_picture));
        btn2.setText(getString(R.string.delete_the_picture));
        Button btn3 = (Button) view.findViewById(R.id.btn3);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
                popupWindow.dismiss();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTopicAdapter.deletePic(addOrChangePic);
                popupWindow.dismiss();
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
        Intent intent = new Intent(CreateTopicActivity.this, PhotoPickerActivity.class);
        if (isAddOrChangePic) {
            intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, 5 - createTopicAdapter.getPicList().size());
        } else {
            intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, 1);
        }
        intent.putExtra("isFrom", true);
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
        overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
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
            Toast.makeText(this, getString(R.string.memory_card_is_not_available), Toast.LENGTH_SHORT).show();
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
        Logger.e("data:" + data + "requestCode:" + requestCode + "resultCode:" + resultCode);
        switch (requestCode) {
            case PHOTO_REQUEST_CAREMA:  //相机拍照
                if (resultCode != RESULT_OK) {
                    showToast(getString(R.string.operation_cancel));
                    return;
                } else {
                    if (isAddOrChangePic) {
                        createTopicAdapter.addPic(addOrChangePic, tempFile.getAbsolutePath());
                    } else {
                        createTopicAdapter.changePic(addOrChangePic, tempFile.getAbsolutePath());
                    }
                    createTopicAdapter.notifyDataSetChanged();
                    setGvPic(tempFile.getAbsolutePath());
                }
                break;
            case PHOTO_REQUEST_GALLERY://从相册选择头像
                if (data == null) {
                    showToast(getString(R.string.operation_cancel));
                    return;
                } else {
                    ArrayList<String> result = data.getStringArrayListExtra("picker_result");

                    for (int i = 0; i < result.size(); i++) {

                        if (isAddOrChangePic) {
                            createTopicAdapter.addPic(addOrChangePic, result.get(i));
                            createTopicAdapter.notifyDataSetChanged();
                        } else {
                            createTopicAdapter.changePic(addOrChangePic, result.get(i));
                            createTopicAdapter.notifyDataSetChanged();
                        }
                    }
                }
                break;
            case PRODUCT_REQUEST:
                if (resultCode == 3 && data != null) {
                    String name = data.getStringExtra("BOUND_PRODUCT_NAME");
                    String price = data.getStringExtra("BOUND_PRODUCT_PRICE");
                    String img = data.getStringExtra("BOUND_PRODUCT_IMG");
                    String id = data.getStringExtra("BOUND_PRODUCT_ID");
                    String shop_name = data.getStringExtra("BOUND_PRODUCT_SHOP_NAME");
                    boundList.add(new BoundProductBean.OrdersListBean(id, img, name, price,shop_name));
                }
                break;
            case ADD_TOPIC_SORT:

                if (resultCode == 1001 && data != null) {
                    String topicSortTitle = data.getStringExtra("TOPIC_SORT_TITLE");
                    long topicSortId = data.getLongExtra("TOPIC_SORT_ID", 0);
                    mTopicCategoryId = String.valueOf(topicSortId);
                    tv_sort_name.setText(topicSortTitle);
                }
                break;

            default:
                break;
        }
    }

    /**
     * 压缩图片
     */
    private void setGvPic(final String pic) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (isAddOrChangePic) {
                        picResultList.add(ImageUtils.getImage(PictureUtil
                                .getSmallBitmap(pic), TimeUtils.getCurTimeMills() + ""));
                    } else {
                        picResultList.remove(addOrChangePic);
                        picResultList.add(addOrChangePic, ImageUtils.getImage(PictureUtil
                                .getSmallBitmap(pic), TimeUtils.getCurTimeMills() + ""));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void multiFileUpload() {
        if (createTopicAdapter.getPicList().size() <= 1) {
            showToast(getString(R.string.please_add_picture));
            return;
        }
        showProgress();
        list = new ArrayList<>();
        for (int i = 0; i < createTopicAdapter.getPicList().size() - 1; i++) {
            list.add(createTopicAdapter.getPicList().get(i).toString());
        }
        List<File> files = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            File file1_1 = new File(list.get(i));
            files.add(file1_1);
        }
        Map<String, String> params = new HashMap<>();
        params.put("userId", mUserId);
        post()
                .addFile("mFile", "01.jpg", files)
                .url(GlobalContent.GLOBAL_IMGGE_URL)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                        dismissProgress();
                        Logger.e("上传onError()" + e.toString());
                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("上传成功" + response);
                        UploadingImageBean uploadingImageBean = new Gson().fromJson(response, UploadingImageBean.class);
                        picBean = uploadingImageBean.getFiles();
                        setCreateTopic();
                    }
                });
    }
}
