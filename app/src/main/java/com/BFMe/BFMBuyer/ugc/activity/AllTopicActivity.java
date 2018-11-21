package com.BFMe.BFMBuyer.ugc.activity;

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
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.activity.LoginActivity;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.ugc.adapter.AllTopicAdapter;
import com.BFMe.BFMBuyer.ugc.bean.SubTopic;
import com.BFMe.BFMBuyer.ugc.bean.SubTopicList;
import com.BFMe.BFMBuyer.utils.CacheUtils;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.ImageUtils;
import com.BFMe.BFMBuyer.utils.PictureUtil;
import com.BFMe.BFMBuyer.utils.TimeUtils;
import com.BFMe.BFMBuyer.utils.UiUtils;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.BFMe.BFMBuyer.utils.xpermission.listener.XPermissionsListener;
import com.BFMe.BFMBuyer.utils.xpermissions.XPermissions;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Request;

import static android.view.View.VISIBLE;

/**
 * 分类下全部话题列表
 */
public class AllTopicActivity extends IBaseActivity implements View.OnClickListener {
    @BindView(R.id.rv_all_topic)
    XRecyclerView rv_all_topic;
    private String mTopicCategoryId = "101";
    /**
     * 正常状态
     */
    private static final int STATE_NORMAL = 0;
    /**
     * 刷新状态
     */
    private static final int STATE_REFRESH = 1;
    /**
     * 加载更多状态
     */
    private static final int STATE_MORE = 2;

    private int state = STATE_NORMAL;//默认为正常状态

    private int pageNo = 1;//记载的页数
    private int pageSize = 10;//一页的数据
    private SubTopic mSubTopic;
    private List<SubTopicList.TopicsListBean> mTopicsList;
    private AllTopicAdapter mAllTopicAdapter;
    private File tempFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    private void initData() {
        XPermissions.init(this);
        isShare = true;
        //h5页面跳转app接收传值
        Intent intent = getIntent();
        String action = intent.getAction();
        if (Intent.ACTION_VIEW.equals(action)) {
            Uri uri = intent.getData();
            if (uri != null) {
                mTopicCategoryId = uri.getQueryParameter("Id");
            }
        } else {
            mTopicCategoryId = getIntent().getStringExtra(GlobalContent.TOPICS_ID);
        }
        getNetData();
    }

    private void getNetData() {
        showProgress();
        getSubTopic();
        getSubTopicList();
    }

    private void getSubTopicList() {
        Map<String, String> params = new HashMap<>();
        if (isLogin) {
            params.put("userId", mUserId);
        }
        params.put("pageNo", pageNo + "");
        params.put("pageSize", pageSize + "");
        params.put("topicCategoryId", mTopicCategoryId);
        OkHttpUtils
                .post()
                .params(params)
                .url(GlobalContent.POST_PUBLISH_UGC_SUB_TOPIC_LIST)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("子话题话题列表==" + response);
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (rootBean.ErrCode.equals("0")) {
                            SubTopicList subTopicList = mGson.fromJson(rootBean.Data, SubTopicList.class);
                            mTopicsList = subTopicList.getTopicsList();
                            validationData();
                        }
                    }
                });
    }

    private void getSubTopic() {
        Map<String, String> params = new HashMap<>();
        if (isLogin) {
            params.put("userId", mUserId);
        }
        params.put("topicCategoryId", mTopicCategoryId);
        OkHttpUtils
                .post()
                .params(params)
                .url(GlobalContent.POST_PUBLISH_UGC_SUB_TOPIC)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("子话题头部信息==" + response);
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (rootBean.ErrCode.equals("0")) {
                            mSubTopic = mGson.fromJson(rootBean.Data, SubTopic.class);
                            validationData();
                        }
                    }
                });
    }

    private void validationData() {
        if (mSubTopic != null && mTopicsList != null) {
            tv_title.setText(mSubTopic.getTopicsList().getTitle());
            showData();
            dismissProgress();
        }
    }

    private void showData() {

        if (mSubTopic != null) {
            shareImageUrl = mSubTopic.getTopicsList().getImageUrl();
            shareTitle = mSubTopic.getTopicsList().getTitle();
            shareContent = mSubTopic.getTopicsList().getContent();
            shareUrl = mSubTopic.getTopicsList().getShareLink();
        } else {
            shareImageUrl = "";
            shareTitle = "";
            shareContent = "";
            shareUrl = "";
        }


        switch (state) {
            case STATE_NORMAL:
                rv_all_topic.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                mAllTopicAdapter = new AllTopicAdapter(this,mSubTopic, mTopicsList);
                rv_all_topic.setAdapter(mAllTopicAdapter);
                if (mTopicsList.size() < 10) {
                    rv_all_topic.setNoMore(true);
                }
                break;
            case STATE_REFRESH:
                state = STATE_NORMAL;
                rv_all_topic.refreshComplete();
                if (mTopicsList.size() < 10) {
                    rv_all_topic.setNoMore(true);
                }

                if (mAllTopicAdapter != null) {
                    mAllTopicAdapter.cleanData();
                    mAllTopicAdapter.addData(mSubTopic, mTopicsList);
                }

                break;
            case STATE_MORE:
                state = STATE_NORMAL;
                rv_all_topic.loadMoreComplete();

                if (mAllTopicAdapter != null) {
                    if (mTopicsList.size() < 10) {
                        rv_all_topic.setNoMore(true);
                        mAllTopicAdapter.addData(mAllTopicAdapter.getItemCount() - 1, mTopicsList);
                    } else {
                        mAllTopicAdapter.addData(mAllTopicAdapter.getItemCount() - 1, mTopicsList);
                    }
                }
                break;

        }

        setListener();
    }

    private void setListener() {
        rv_all_topic.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                state = STATE_REFRESH;
                mSubTopic = null;
                mTopicsList = null;
                getNetData();
            }

            @Override
            public void onLoadMore() {
                pageNo += 1;
                state = STATE_MORE;
                getSubTopicList();
            }
        });

        mAllTopicAdapter.setSubTopicOnClickListner(new AllTopicAdapter.SubTopicOnClickListner() {
            @Override
            public void subscriptionOnClick(View v, TextView tv_subscription, SubTopic.TopicsListBean topicInfo) {
                CheckBox box = (CheckBox) v;
                if (isLogin) {
                    boolean checked = box.isChecked();
                    if (checked) {
                        box.setText(getString(R.string.subscription_ture));
                    } else {
                        box.setText(getString(R.string.subscription));
                    }
                    if (topicInfo.getIsSubscribe() == 1) {//之前订阅过
                        if (checked) {
                            tv_subscription.setText(getString(R.string.subscription) + topicInfo.getSubscribeCount());
                        } else {
                            tv_subscription.setText(getString(R.string.subscription) + (topicInfo.getSubscribeCount() - 1));
                        }
                    } else {
                        if (checked) {
                            tv_subscription.setText(getString(R.string.subscription) + (topicInfo.getSubscribeCount() + 1));
                        } else {
                            tv_subscription.setText(getString(R.string.subscription) + topicInfo.getSubscribeCount());
                        }
                    }

                    subscriptionTopic(topicInfo.getId());
                } else {
                    box.setChecked(false);
                    box.setText(getString(R.string.subscription));
                    startActivity(new Intent(AllTopicActivity.this, LoginActivity.class));
                    startAnim();
                }
            }

            @Override
            public void detailsOnClick(long topicId) {
                Intent intent = new Intent(AllTopicActivity.this, TaTalkDetailsActivity.class);
                intent.putExtra(GlobalContent.TOPIC_ID, topicId);
                startActivity(intent);
                startAnim();
            }

            @Override
            public void userTopicOnClick(String encryptUserId) {
                Intent intent = new Intent(AllTopicActivity.this, UserInfoActivity.class);
                intent.putExtra(GlobalContent.USER_INFO_ID, encryptUserId);
                startActivity(intent);
                startAnim();
            }

            @Override
            public void topicLikeOnClick(View v, SubTopicList.TopicsListBean topicInfo) {
                CheckBox box = (CheckBox) v;
                boolean checked = box.isChecked();
                if (isLogin) {

                    if (topicInfo.getIsPrase() == 1) {//之前赞过
                        if (checked) {
                            box.setText(topicInfo.getParseCount() + "");
                        } else {
                            box.setText(topicInfo.getParseCount() - 1 + "");
                        }
                    } else {
                        if (checked) {
                            box.setText(topicInfo.getParseCount() + 1 + "");
                        } else {
                            box.setText(topicInfo.getParseCount() + "");
                        }
                    }
                    paresTopic(topicInfo.getId());
                } else {
                    box.setChecked(false);
                    startActivity(new Intent(AllTopicActivity.this, LoginActivity.class));
                    startAnim();
                }
            }

            @Override
            public void topicTitleOnClick(long id) {//顶部话题分类信息
                Intent intent = new Intent(AllTopicActivity.this,TopicCategoryActivity.class);
                intent.putExtra(GlobalContent.TOPICS_ID,String.valueOf(id));
                startActivity(intent);
                bottomStartAnim();
            }
        });
    }

    /**
     * 订阅子话题
     *
     * @param topicCategoryId 子话题ID
     */
    private void subscriptionTopic(long topicCategoryId) {
        OkHttpUtils
                .post()
                .addParams("userId", mUserId)
                .addParams("topicCategoryId", topicCategoryId + "")
                .url(GlobalContent.POST_SUBSCRIBE_TOPIC_CATEGORY)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("订阅子话题" + response);
                    }
                });
    }

    /**
     * 话题点赞
     *
     * @param topicId 话题id
     */
    private void paresTopic(long topicId) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", mUserId);
        params.put("topicId", topicId + "");
        OkHttpUtils
                .post()
                .params(params)
                .url(GlobalContent.POST_USER_PARES_TOPIC)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("点赞" + response);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCartNumberVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_create:
                boolean isLogin = CacheUtils.getBoolean(this, GlobalContent.ISLOGIN);
                if (isLogin) {
                    choosePicture();
                } else {
                    startActivity(new Intent(this, LoginActivity.class));
                    overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
                }
                break;
        }
    }

    private void initView() {
        setBackButtonVisibility(VISIBLE);
        setChartButtonVisibility(View.VISIBLE);
        vw_bg.setVisibility(View.GONE);

        ImageButton ibCreate = (ImageButton) findViewById(R.id.ib_create);
        ibCreate.setOnClickListener(this);
        iv_title_right.setImageResource(R.drawable.icon_share);
        rv_all_topic.setFootViewText(getString(R.string.load), getString(R.string.no_more_data));
    }

    @Override
    public int initLayout() {
        return R.layout.activity_all_topic;
    }


    //-------------------------相机相册相关-----------------------------------
    private void choosePicture() {
        View v = LayoutInflater.from(this).inflate(R.layout.layou_choose_picture, null, false);
        final PopupWindow popupWindow = new PopupWindow(v, WindowManager.LayoutParams.MATCH_PARENT,
                UiUtils.px2dp(1800), true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setAnimationStyle(R.style.AddressDialogAnim);
        popupWindow.setHeight(UiUtils.dip2px(200));
        popupWindow.showAtLocation(AllTopicActivity.this.findViewById(R.id.activity_all_topic), Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, 0);
        Button btn1 = (Button) v.findViewById(R.id.btn1);
        Button btn2 = (Button) v.findViewById(R.id.btn2);
        Button btn3 = (Button) v.findViewById(R.id.btn3);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //从相册选择
                Intent intent = new Intent(AllTopicActivity.this, PhotoPickerActivity.class);
                intent.putExtra("topic_sort_title",mSubTopic.getTopicsList().getTitle());
                intent.putExtra("topic_sort_id",mSubTopic.getTopicsList().getId());
                intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, 4);
                startActivity(intent);
                overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
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
        Logger.e("data:" + data + "requestCode:" + requestCode + "resultCode:" + resultCode);
        switch (requestCode) {
            case 1:  //相机拍照
                if (resultCode != RESULT_OK) {
                    showToast(getResources().getString(R.string.operation_cancel));
                    return;
                } else {
                    Intent intent = new Intent(AllTopicActivity.this, CreateTopicActivity.class);
                    ArrayList<String> list = new ArrayList<>();
                    try {
                        list.add(ImageUtils.getImage(PictureUtil
                                .getSmallBitmap(imgPathOri), TimeUtils.getCurTimeMills() + ""));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    intent.putStringArrayListExtra("picker_result", list);
                    intent.putExtra("topic_sort_title",mSubTopic.getTopicsList().getTitle());
                    intent.putExtra("topic_sort_id",mSubTopic.getTopicsList().getId());
                    Logger.e("12121321321话题分类title=="+mSubTopic.getTopicsList().getTitle()+"===话题分类id==="+mSubTopic.getTopicsList().getId());
                    startActivity(intent);
                    overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
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
