package com.BFMe.BFMBuyer.ugc.activity;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.ugc.adapter.FolderAdapter;
import com.BFMe.BFMBuyer.ugc.adapter.PhotoAdapter;
import com.BFMe.BFMBuyer.ugc.bean.Photo;
import com.BFMe.BFMBuyer.ugc.bean.PhotoFolder;
import com.BFMe.BFMBuyer.utils.ImageUtils;
import com.BFMe.BFMBuyer.utils.OtherUtils;
import com.BFMe.BFMBuyer.utils.PhotoUtils;
import com.BFMe.BFMBuyer.utils.PictureUtil;
import com.BFMe.BFMBuyer.utils.TimeUtils;
import com.BFMe.BFMBuyer.utils.xpermission.listener.XPermissionsListener;
import com.BFMe.BFMBuyer.utils.xpermissions.XPermissions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @Class: PhotoPickerActivity
 * @Description: 照片选择界面
 * @author: lling(www.liuling123.com)
 * @Date: 2015/11/4
 */
public class PhotoPickerActivity extends Activity implements PhotoAdapter.PhotoClickCallBack {


    public final static String KEY_RESULT = "picker_result";
    /**
     * 最大选择数量
     */
    public final static String EXTRA_MAX_MUN = "max_num";
    /**
     * 默认最大选择数量
     */
    public final static int DEFAULT_NUM = 9;

    private final static String ALL_PHOTO = "所有图片";
    private static final String TAG = PhotoPickerActivity.class.getSimpleName();
    /**
     * 最大选择数量，仅多选模式有用
     */
    private int mMaxNum;

    private GridView mGridView;
    private Map<String, PhotoFolder> mFolderMap;
    private List<Photo> mPhotoLists = new ArrayList<>();
    private ArrayList<String> mSelectList = new ArrayList<>();
    private PhotoAdapter mPhotoAdapter;
    private ProgressDialog mProgressDialog;
    private ListView mFolderListView;

    private TextView mPhotoNumTV;
    private TextView mPhotoNameTV;
    private TextView mCommitBtn;
    /**
     * 文件夹列表是否处于显示状态
     */
    boolean mIsFolderViewShow = false;
    /**
     * 文件夹列表是否被初始化，确保只被初始化一次
     */
    boolean mIsFolderViewInit = false;
    private boolean isFrom;
    private long mTopicCategoryId = 0;
    private String mTopicSortTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_picker);
        initView();
        XPermissions.init(this);
        requestPermission();

    }

    private void requestPermission() {
        XPermissions
                .requestPermissions()
                .setRequestCode(203)
                .setShouldShow(true)
                .setPermissions(new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                })
                .setOnXPermissionsListener(new XPermissionsListener() {
                    @Override
                    public void onXPermissions(int requestCode, int resultCode) {
                        if (resultCode == XPermissions.PERMISSION_SUCCESS) {
                            initIntentParams();
                            getPhotosTask.execute();
                        }else{
                            finish();
                        }
                    }
                }).builder();

    }

    private void initView() {
        mGridView = (GridView) findViewById(R.id.photo_gridview);
        mPhotoNumTV = (TextView) findViewById(R.id.photo_num);
        mPhotoNameTV = (TextView) findViewById(R.id.floder_name);
        findViewById(R.id.bottom_tab_bar).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //消费触摸事件，防止触摸底部tab栏也会选中图片
                return true;
            }
        });
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.enter_back, R.anim.exit_back);
            }
        });
    }

    /**
     * 初始化选项参数
     */
    private void initIntentParams() {
        Intent intent = getIntent();
        mTopicSortTitle =  intent.getStringExtra("topic_sort_title");
        mTopicCategoryId = intent.getLongExtra("topic_sort_id", 0);
        mMaxNum =intent.getIntExtra(EXTRA_MAX_MUN, DEFAULT_NUM);
        isFrom = intent.getBooleanExtra("isFrom", false);
        mCommitBtn = (TextView) findViewById(R.id.commit);
        mCommitBtn.setVisibility(View.VISIBLE);
        mCommitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectList.addAll(mPhotoAdapter.getmSelectedPhotos());
                getPhotosTask1.execute();

            }
        });
    }

    private ArrayList<String> picResultList = new ArrayList();
    /**
     * 获取照片的异步任务
     */
    private AsyncTask getPhotosTask1 = new AsyncTask() {
        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(PhotoPickerActivity.this, null, "loading...");
        }

        @Override
        protected Object doInBackground(Object[] params) {
            for (int i = 0; i < mSelectList.size(); i++) {
                try {
                    Log.d("选择的图片的路径是：",mSelectList.get(i).toString());
                    picResultList.add(ImageUtils.getImage(PictureUtil
                            .getSmallBitmap(mSelectList.get(i)), TimeUtils.getCurTimeMills() + ""));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            returnData();
            mProgressDialog.dismiss();
        }
    };

    private void getPhotosSuccess() {
        mProgressDialog.dismiss();
        mPhotoLists.addAll(mFolderMap.get(ALL_PHOTO).getPhotoList());

        mPhotoNumTV.setText(OtherUtils.formatResourceString(getApplicationContext(),
                R.string.photos_num, mPhotoLists.size()));

        mPhotoAdapter = new PhotoAdapter(this.getApplicationContext(), mPhotoLists);
        mPhotoAdapter.setSelectMode();
        mPhotoAdapter.setMaxNum(mMaxNum);
        mPhotoAdapter.setPhotoClickCallBack(this);
        mGridView.setAdapter(mPhotoAdapter);
        Set<String> keys = mFolderMap.keySet();
        final List<PhotoFolder> folders = new ArrayList<>();
        for (String key : keys) {
            if (ALL_PHOTO.equals(key)) {
                PhotoFolder folder = mFolderMap.get(key);
                folder.setIsSelected(true);
                folders.add(0, folder);
            } else {
                folders.add(mFolderMap.get(key));
            }
        }
        mPhotoNameTV.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                toggleFolderList(folders);
            }
        });

    }

    @Override
    public void onPhotoClick() {
        List<String> list = mPhotoAdapter.getmSelectedPhotos();
        if (list != null && list.size() > 0) {
            mCommitBtn.setEnabled(true);
            mCommitBtn.setText(OtherUtils.formatResourceString(getApplicationContext(),
                    R.string.commit_num, list.size(), mMaxNum));
        } else {
            mCommitBtn.setEnabled(false);
            mCommitBtn.setText(R.string.commit);
        }
    }

    /**
     * 返回选择图片的路径
     */
    private void returnData() {

        // 返回已选择的图片数据
        if (!isFrom) {
            Intent data = new Intent(this, CreateTopicActivity.class);
            data.putStringArrayListExtra(KEY_RESULT, picResultList);
            data.putExtra("topic_sort_title",mTopicSortTitle);
            data.putExtra("topic_sort_id",mTopicCategoryId);
            startActivity(data);

        } else {
            Intent data = new Intent();
            data.putStringArrayListExtra(KEY_RESULT, picResultList);
            setResult(2, data);
        }
        overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
        finish();

    }

    /**
     * 显示或者隐藏文件夹列表
     *
     * @param folders
     */
    private void toggleFolderList(final List<PhotoFolder> folders) {
        //初始化文件夹列表
        if (!mIsFolderViewInit) {
            ViewStub folderStub = (ViewStub) findViewById(R.id.floder_stub);
            folderStub.inflate();
            View dimLayout = findViewById(R.id.dim_layout);
            mFolderListView = (ListView) findViewById(R.id.listview_floder);
            final FolderAdapter adapter = new FolderAdapter(this, folders);
            mFolderListView.setAdapter(adapter);
            mFolderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    for (PhotoFolder folder : folders) {
                        folder.setIsSelected(false);
                    }
                    PhotoFolder folder = folders.get(position);
                    folder.setIsSelected(true);
                    adapter.notifyDataSetChanged();

                    mPhotoLists.clear();
                    mPhotoLists.addAll(folder.getPhotoList());
                    //这里重新设置adapter而不是直接notifyDataSetChanged，是让GridView返回顶部
                    mGridView.setAdapter(mPhotoAdapter);
                    mPhotoNumTV.setText(OtherUtils.formatResourceString(getApplicationContext(),
                            R.string.photos_num, mPhotoLists.size()));
                    mPhotoNameTV.setText(folder.getName());
                    toggle();
                }
            });
            dimLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (mIsFolderViewShow) {
                        toggle();
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            initAnimation(dimLayout);
            mIsFolderViewInit = true;
        }
        toggle();
    }

    /**
     * 弹出或者收起文件夹列表
     */
    private void toggle() {
        if (mIsFolderViewShow) {
            outAnimatorSet.start();
            mIsFolderViewShow = false;
        } else {
            inAnimatorSet.start();
            mIsFolderViewShow = true;
        }
    }


    /**
     * 初始化文件夹列表的显示隐藏动画
     */
    AnimatorSet inAnimatorSet = new AnimatorSet();
    AnimatorSet outAnimatorSet = new AnimatorSet();

    private void initAnimation(View dimLayout) {
        ObjectAnimator alphaInAnimator, alphaOutAnimator, transInAnimator, transOutAnimator;
        //获取actionBar的高
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        /**
         * 这里的高度是，屏幕高度减去上、下tab栏，并且上面留有一个tab栏的高度
         * 所以这里减去3个actionBarHeight的高度
         */
        int height = OtherUtils.getHeightInPx(this) - 3 * actionBarHeight;
        alphaInAnimator = ObjectAnimator.ofFloat(dimLayout, "alpha", 0f, 0.7f);
        alphaOutAnimator = ObjectAnimator.ofFloat(dimLayout, "alpha", 0.7f, 0f);
        transInAnimator = ObjectAnimator.ofFloat(mFolderListView, "translationY", height, 0);
        transOutAnimator = ObjectAnimator.ofFloat(mFolderListView, "translationY", 0, height);

        LinearInterpolator linearInterpolator = new LinearInterpolator();

        inAnimatorSet.play(transInAnimator).with(alphaInAnimator);
        inAnimatorSet.setDuration(300);
        inAnimatorSet.setInterpolator(linearInterpolator);
        outAnimatorSet.play(transOutAnimator).with(alphaOutAnimator);
        outAnimatorSet.setDuration(300);
        outAnimatorSet.setInterpolator(linearInterpolator);
    }


    /**
     * 获取照片的异步任务
     */
    private AsyncTask getPhotosTask = new AsyncTask() {
        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(PhotoPickerActivity.this, null, "loading...");
        }

        @Override
        protected Object doInBackground(Object[] params) {
            mFolderMap = PhotoUtils.getPhotos(
                    PhotoPickerActivity.this.getApplicationContext());
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            getPhotosSuccess();
        }
    };

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        XPermissions.handlerPermissionResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(R.anim.enter_back, R.anim.exit_back);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
