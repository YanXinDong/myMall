package com.BFMe.BFMBuyer.main;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.activity.LoginActivity;
import com.BFMe.BFMBuyer.base.BaseFragment;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.main.adapter.ugc.UGCHomeAdapter;
import com.BFMe.BFMBuyer.ugc.activity.CreateTopicActivity;
import com.BFMe.BFMBuyer.ugc.activity.PhotoPickerActivity;
import com.BFMe.BFMBuyer.ugc.bean.TopicList;
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

import okhttp3.Request;

import static android.app.Activity.RESULT_OK;

/**
 * Description：UGCFragment
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/8/14 16:20
 */
public class UGCFragment extends BaseFragment implements View.OnClickListener {
    private XRecyclerView ugcRV;
    private ImageButton ib_add_topic;
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
    private UGCHomeAdapter mUGCAdapter;
    private List<TopicList.TopicsListBean> mTopicsList;
    private View view;
    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_ugc, null, false);
        ugcRV = (XRecyclerView) view.findViewById(R.id.rv_ugc);
        ib_add_topic = (ImageButton) view.findViewById(R.id.ib_add_topic);
        ib_add_topic.setOnClickListener(this);
        XPermissions.init(getActivity());
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ugcRV.getLayoutParams();
        layoutParams.setMargins(UiUtils.dip2px(5), 0, UiUtils.dip2px(5), 0);
        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        setListener();
        getNetTopicList();
    }

    private void getNetTopicList() {
        showProgress();
        Map<String, String> params = new HashMap<>();
        if (isLogin) {
            params.put("userId", mUserId);
        }
        params.put("pageSize", pageSize + "");
        params.put("pageNo", pageNo + "");

        OkHttpUtils.post()
                .params(params)
                .url(GlobalContent.POST_SEARCH_UGC_TOPIC_LIST)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("TAG", "Exception==" + e.toString());
                        dismissProgress();
                    }

                    @Override
                    public void onResponse(String response) {
                        dismissProgress();
                        Logger.e("TopicList" + response);
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (rootBean.ErrCode.equals("0")) {
                            TopicList topicList = mGson.fromJson(rootBean.Data, TopicList.class);
                            setAdapter(topicList.getTopicsList());
                        } else {
                            showToast(rootBean.ResponseMsg);
                        }
                    }
                });
    }

    private void setAdapter(List<TopicList.TopicsListBean> data) {

        mTopicsList = data;
        switch (state) {
            case STATE_NORMAL:
                ugcRV.setLayoutManager(new GridLayoutManager(getContext(), 2));
                mUGCAdapter = new UGCHomeAdapter(mActivity, data);
                ugcRV.setAdapter(mUGCAdapter);
                setNoMore();
                break;
            case STATE_REFRESH:
                state = STATE_NORMAL;
                ugcRV.refreshComplete();
                setNoMore();

                if (mUGCAdapter != null) {
                    mUGCAdapter.cleanData();
                    mUGCAdapter.addData(mTopicsList);
                }

                break;
            case STATE_MORE:
                state = STATE_NORMAL;
                ugcRV.loadMoreComplete();
                if (mUGCAdapter != null) {
                    setNoMore();
                    mUGCAdapter.addData(mUGCAdapter.getItemCount(), mTopicsList);
                }
                break;
        }

    }
    private void setNoMore() {
        if (mTopicsList.size() < 10) {
            ugcRV.setNoMore(true);
        }
    }

    private void setListener() {

        ugcRV.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                state = STATE_REFRESH;
                getNetTopicList();
            }

            @Override
            public void onLoadMore() {
                pageNo += 1;
                state = STATE_MORE;
                getNetTopicList();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_add_topic:
                boolean isLogin = CacheUtils.getBoolean(mActivity, GlobalContent.ISLOGIN);
                if(isLogin){
                    choosePicture();
                }else{
                    startActivity(new Intent(mActivity, LoginActivity.class));
                    mActivity.overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
                }
                break;
        }
    }

    //-------------------------相机相册相关-----------------------------------
    private void choosePicture() {
        View v = LayoutInflater.from(mActivity).inflate(R.layout.layou_choose_picture, null, false);
        final PopupWindow popupWindow = new PopupWindow(v, WindowManager.LayoutParams.MATCH_PARENT,
                UiUtils.px2dp(1800), true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setAnimationStyle(R.style.AddressDialogAnim);
        popupWindow.setHeight(UiUtils.dip2px(200));
        popupWindow.showAtLocation(view.findViewById(R.id.rl_layout_fragment), Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, 0);
        Button btn1 = (Button) v.findViewById(R.id.btn1);
        Button btn2 = (Button) v.findViewById(R.id.btn2);
        Button btn3 = (Button) v.findViewById(R.id.btn3);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //从相册选择
                Intent intent = new Intent(mActivity, PhotoPickerActivity.class);
                intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, 4);
                startActivity(intent);
                mActivity.overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
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
            imgUriOri = FileProvider.getUriForFile(getActivity(),"com.BFMe.BFMBuyer.fileprovider", oriPhotoFile);
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
                    showToast(getString(R.string.operation_cancel));
                    return;
                } else {
                    Intent intent = new Intent(mActivity, CreateTopicActivity.class);
                    ArrayList<String> list = new ArrayList<>();
                    try {
                        list.add(ImageUtils.getImage(PictureUtil
                                .getSmallBitmap(imgPathOri), TimeUtils.getCurTimeMills() + ""));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    intent.putStringArrayListExtra("picker_result", list);
                    startActivity(intent);
                    mActivity.overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
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
        File pictureDirOri = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/OriPicture");
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
