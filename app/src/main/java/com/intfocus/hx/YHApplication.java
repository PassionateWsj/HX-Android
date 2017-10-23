package com.intfocus.hx;

import android.app.ActivityManager;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.intfocus.hx.constant.Constants;
import com.intfocus.hx.dashboard.DashboardActivity;
import com.intfocus.hx.login.LoginActivity;
import com.intfocus.hx.screen_lock.ConfirmPassCodeActivity;
import com.intfocus.hx.util.FileUtil;
import com.intfocus.hx.util.URLs;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.OpenUDID.OpenUDID_manager;
import org.xutils.x;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.intfocus.hx.util.PrivateURLs.kWXAppId;
import static com.intfocus.hx.util.PrivateURLs.kWXAppSecret;

/**
 * Created by lijunjie on 16/1/15.
 */
public class YHApplication extends Application {
    /**
     * 缓存目录
     */
    public static String CACHEDIR;

    public static ExecutorService threadPool = Executors.newFixedThreadPool(2);

    private Context appContext;
    public static Context globalContext;
    SharedPreferences mSettingSP;
    SharedPreferences mUserSP;
    PackageInfo packageInfo;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        globalContext = getApplicationContext();
        mSettingSP = getSharedPreferences("SettingPreference", Context.MODE_PRIVATE);
        mUserSP = getSharedPreferences("UserBean", Context.MODE_PRIVATE);
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        /*
         * Bugly 异常上报
         */
        CrashReport.initCrashReport(getApplicationContext(), Constants.BUGLY_APP_ID, BuildConfig.DEBUG);

        /*
         * 友盟分享初始化
         */
        UMShareAPI.get(this);

        /*
         * 配置微信 appKey
         */
        PlatformConfig.setWeixin(kWXAppId, kWXAppSecret);

        initXutils();

        /*
         *  初始化 OpenUDID, 设备唯一化
         */
        OpenUDID_manager.sync(getApplicationContext());
//

        /*
         *  手机待机再激活时发送开屏广播
         */
        registerReceiver(broadcastScreenOnAndOff, new IntentFilter(Intent.ACTION_SCREEN_ON));

        /*
         *  监测内存泄漏
         */
//         refWatcher = LeakCanary.install(this);

        /*
         * 注册推送服务，每次调用register方法都会回调该接口
         */
//        PushAgent mPushAgent = PushAgent.getInstance(this);
//        mPushAgent.register(new IUmengRegisterCallback() {
//
//            @Override
//            public void onSuccess(String deviceToken) {
//                SharedPreferences mPushSP = getSharedPreferences("PushMessage", MODE_PRIVATE);
//                SharedPreferences.Editor mPushSPEdit = mPushSP.edit();
//
//                mPushSPEdit.putString(kPushDeviceToken, deviceToken).commit();
//            }
//
//            @Override
//            public void onFailure(String s, String s1) {
//                // 向服务器推送: 设备信息,
//            }
//        });
//        mPushAgent.setNotificationClickHandler(pushMessageHandler);
    }

    private void initXutils() {
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
    }

    final UmengNotificationClickHandler pushMessageHandler = new UmengNotificationClickHandler() {

        @Override
        public void dealWithCustomAction(Context context, UMessage uMessage) {
            super.dealWithCustomAction(context, uMessage);

            Intent intent = null;
            boolean isLogin = getApplicationContext().getSharedPreferences("UserBean", MODE_PRIVATE).getBoolean(URLs.kIsLogin, false);
            if (isLogin) {
                intent = new Intent(appContext, DashboardActivity.class);
            } else {
                intent = new Intent(appContext, LoginActivity.class);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle bundle = new Bundle();
            bundle.putString("message", uMessage.custom);
            bundle.putString("message_body_title", uMessage.title);
            bundle.putString("message_body_text", uMessage.text);
            intent.putExtra("msgData", bundle);
            startActivity(intent);
        }
    };

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /*
     * 程序终止时会执行以下代码
     */
    @Override
    public void onTerminate() {
//        PgyCrashManager.unregister(); // 解除注册蒲公英异常信息上传
        super.onTerminate();
    }

    public Context getAppContext() {
        return appContext;
    }

    /*
     *  手机待机再激活时接收解屏广播,进入解锁密码页
     */
    private final BroadcastReceiver broadcastScreenOnAndOff = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!intent.getAction().equals(Intent.ACTION_SCREEN_ON) || isBackground(appContext)) {
                Log.i("BroadcastReceiver", "return" + isBackground(appContext));
                return;
            }
            Log.i("BroadcastReceiver", "Screen On");
            String currentActivityName = ((YHApplication) context.getApplicationContext()).getCurrentActivity();
            if ((currentActivityName != null && !currentActivityName.trim().equals("ConfirmPassCodeActivity")) && // 当前活动的Activity非解锁界面
                    FileUtil.checkIsLocked(appContext)) { //应用处于登录状态，并且开启了密码锁
                intent = new Intent(appContext, ConfirmPassCodeActivity.class);
                intent.putExtra("is_from_login", true);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                appContext.startActivity(intent);
            }
        }
    };

    private String mCurrentActivity = null;

    public String getCurrentActivity() {
        return mCurrentActivity;
    }

    public void setCurrentActivity(Context context) {
        if (context == null) {
            mCurrentActivity = null;
            return;
        }
        String mActivity = context.toString();
        String mActivityName = mActivity.substring(mActivity.lastIndexOf(".") + 1, mActivity.indexOf("@"));
        mCurrentActivity = mActivityName;
        Log.i("activityName", mCurrentActivity);
    }

    /*
     * 判断应用当前是否处于后台
     * Android 4.4 以上版本 不适用 getRunningTasks() 方法
     */
    private boolean isBackground(Context context) {
        boolean isBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isBackground = false;
            }
        }

        return isBackground;
    }
}


