package com.BFMe.BFMBuyer.application;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.activity.RecentListActivity;
import com.BFMe.BFMBuyer.utils.CacheUtils;
import com.BFMe.BFMBuyer.utils.DemoCache;
import com.BFMe.BFMBuyer.utils.MyCache;
import com.BFMe.BFMBuyer.utils.SystemUtil;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.netease.nim.uikit.ImageLoaderKit;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.cache.FriendDataCache;
import com.netease.nim.uikit.cache.NimUserInfoCache;
import com.netease.nim.uikit.cache.TeamDataCache;
import com.netease.nim.uikit.contact.ContactProvider;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.zhy.autolayout.config.AutoLayoutConifg;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import cn.jpush.android.api.JPushInterface;

/**
 * Description: 应用的初始化
 * Copyright  : Copyright (c) 2016
 * Company    :   白富美(北京)网络科技有限公司
 * Author     : 王  可
 * Date       : 2016/7/4 16:38
 */
public class MyApplication extends MultiDexApplication implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "Debug_BFMe";
    private static Handler handler;
    private static int mainThreadId;
    private static Context context;
    private static Handler mainHandler;
    private String IMPASSWORD = "impassword";
    private String IMUSERID = "imuserid";

    /**
     * 获取应用常用参数
     */
    @Override
    public void onCreate() {
        super.onCreate();

        AutoLayoutConifg.getInstance().useDeviceSize();

        context = getApplicationContext();
        mainHandler = new Handler();

        setIM();

        Logger.init(TAG)
                .methodCount(3)//方法栈打印个数
                .hideThreadInfo();//隐藏线程信息
//        Logger.init(TAG).logLevel(LogLevel.NONE);

        initJPush();
    }

    private void initJPush() {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    private void setIM() {
        // SDK初始化（启动后台服务，若已经存在用户登录信息， SDK 将完成自动登录）
        NIMClient.init(this, loginInfo(), options());
        if (inMainProcess()) {
            // 注意：以下操作必须在主进程中进行
            // 1、UI相关初始化操作
            // 2、相关Service调用
            // 初始化UIKit模块
            initUIKit();
        }
    }

    private void initUIKit() {
        // 初始化，需要传入用户信息提供者
        NimUIKit.init(this, infoProvider, contactProvider);

    }


    private UserInfoProvider infoProvider = new UserInfoProvider() {
        @Override
        public UserInfo getUserInfo(String account) {
            UserInfo user = NimUserInfoCache.getInstance().getUserInfo(account);
            if (user == null) {
                NimUserInfoCache.getInstance().getUserInfoFromRemote(account, null);
            }

            return user;
        }

        @Override
        public int getDefaultIconResId() {
            return R.drawable.avatar_def;
        }

        @Override
        public Bitmap getTeamIcon(String teamId) {
            /**
             * 注意：这里最好从缓存里拿，如果读取本地头像可能导致UI进程阻塞，导致通知栏提醒延时弹出。
             */
            // 从内存缓存中查找群头像
            Team team = TeamDataCache.getInstance().getTeamById(teamId);
            if (team != null) {
                Bitmap bm = ImageLoaderKit.getNotificationBitmapFromCache(team.getIcon());
                if (bm != null) {
                    return bm;
                }
            }

            // 默认图
            Drawable drawable = getResources().getDrawable(R.drawable.nim_avatar_group);
            if (drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable) drawable).getBitmap();
            }

            return null;
        }

        @Override
        public Bitmap getAvatarForMessageNotifier(String account) {
            /**
             * 注意：这里最好从缓存里拿，如果读取本地头像可能导致UI进程阻塞，导致通知栏提醒延时弹出。
             */
            UserInfo user = getUserInfo(account);
            return (user != null) ? ImageLoaderKit.getNotificationBitmapFromCache(user.getAvatar()) : null;
        }

        @Override
        public String getDisplayNameForMessageNotifier(String account, String sessionId, SessionTypeEnum sessionType) {
            String nick = null;
            if (sessionType == SessionTypeEnum.P2P) {
                nick = NimUserInfoCache.getInstance().getAlias(account);
            } else if (sessionType == SessionTypeEnum.Team) {
                nick = TeamDataCache.getInstance().getTeamNick(sessionId, account);
                if (TextUtils.isEmpty(nick)) {
                    nick = NimUserInfoCache.getInstance().getAlias(account);
                }
            }
            // 返回null，交给sdk处理。如果对方有设置nick，sdk会显示nick
            if (TextUtils.isEmpty(nick)) {
                return null;
            }

            return nick;
        }

    };
    private ContactProvider contactProvider = new ContactProvider() {
        @Override
        public List<UserInfoProvider.UserInfo> getUserInfoOfMyFriends() {
            List<NimUserInfo> nimUsers = NimUserInfoCache.getInstance().getAllUsersOfMyFriend();
            List<UserInfoProvider.UserInfo> users = new ArrayList<>(nimUsers.size());
            if (!nimUsers.isEmpty()) {
                users.addAll(nimUsers);
            }

            return users;
        }

        @Override
        public int getMyFriendsCount() {
            return FriendDataCache.getInstance().getMyFriendCounts();
        }

        @Override
        public String getUserDisplayName(String account) {
            return NimUserInfoCache.getInstance().getUserDisplayName(account);
        }
    };


    /**
     * 获取全局对象
     */
    public static Context getContext() {
        return context;
    }

    /**
     * 获取主线程的handler
     */
    public static Handler getHandler() {
        return mainHandler;
    }

    /**
     * 系统的异常捕获
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        //杀死进程，退出
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }


    private static MyApplication instance;


    public synchronized static MyApplication getInstance() {
        if (null == instance) {
            instance = new MyApplication();
        }
        return instance;
    }

    //退出登录Activity集合
    Stack<WeakReference<Activity>> mList = new Stack<>();
    // add Activity
    public void addActivity(Activity activity) {
        mList.add(new WeakReference<Activity>(activity));
    }

    public void exit() {
        try {
            for(int i = 0; i < mList.size(); i++) {
                WeakReference<Activity> activity = mList.get(i);
                Activity activity1 = activity.get();
                if(activity1!=null){
                    activity1.finish();
                }
                mList.remove(activity);
                i--;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //回到首页的activity集合
    Stack<WeakReference<Activity>> activityStack = new Stack<>();

    // add Activity
    public void addActivityHome(Activity activity) {

        activityStack.add(new WeakReference<Activity>(activity));
    }

    public void exitHome() {
        try {
            for(int i = 0; i < activityStack.size(); i++) {
                WeakReference<Activity> activity = activityStack.get(i);
                Activity activity1 = activity.get();
                if (activity1 != null){
                    activity1.finish();
                }
                activityStack.remove(i);
                i--;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    // 如果已经存在用户登录信息，返回LoginInfo，否则返回null即可
    private LoginInfo loginInfo() {
        String account = CacheUtils.getString(this, IMUSERID);
        String token = CacheUtils.getString(this, IMPASSWORD);

        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            DemoCache.setAccount(account.toLowerCase());
            return new LoginInfo(account, token);
        } else {
            return null;
        }
    }

    // 如果返回值为null，则全部使用默认参数。
    private SDKOptions options() {
        SDKOptions options = new SDKOptions();

        // 如果将新消息通知提醒托管给 SDK 完成，需要添加以下配置。否则无需设置。
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        config.notificationEntrance = RecentListActivity.class; // 点击通知栏跳转到该Activity
        config.notificationSmallIconId = R.mipmap.launcher;
        // 呼吸灯配置
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;
        // 通知铃声的uri字符串
        config.notificationSound = "android.resource://com.netease.nim.demo/raw/msg";
        options.statusBarNotificationConfig = config;

        // 配置保存图片，文件，log 等数据的目录
        // 如果 options 中没有设置这个值，SDK 会使用下面代码示例中的位置作为 SDK 的数据目录。
        // 该目录目前包含 log, file, image, audio, video, thumb 这6个目录。
        // 如果第三方 APP 需要缓存清理功能， 清理这个目录下面个子目录的内容即可。
        String sdkPath = Environment.getExternalStorageDirectory() + "/" + getPackageName() + "/nim";
        options.sdkStorageRootPath = sdkPath;

        // 配置是否需要预下载附件缩略图，默认为 true
        options.preloadAttach = true;

        // 配置附件缩略图的尺寸大小。表示向服务器请求缩略图文件的大小
        // 该值一般应根据屏幕尺寸来确定， 默认值为 Screen.width / 2
        options.thumbnailSize = 480;

        // 用户资料提供者, 目前主要用于提供用户资料，用于新消息通知栏中显示消息来源的头像和昵称
        options.userInfoProvider = new UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String account) {
                return null;
            }

            @Override
            public int getDefaultIconResId() {
                return R.drawable.avatar_def;
            }

            @Override
            public Bitmap getTeamIcon(String tid) {
                return null;
            }

            @Override
            public Bitmap getAvatarForMessageNotifier(String account) {
                return null;
            }

            @Override
            public String getDisplayNameForMessageNotifier(String account, String sessionId,
                                                           SessionTypeEnum sessionType) {
                return null;
            }
        };
        return options;
    }

    public boolean inMainProcess() {
        String packageName = getPackageName();
        String processName = SystemUtil.getProcessName(this);
        return packageName.equals(processName);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        CacheUtils.clear(this);
        MyApplication.getInstance().exit();
        NIMClient.getService(AuthService.class).logout();
        MyCache.clear();
        System.exit(0);
    }
}
