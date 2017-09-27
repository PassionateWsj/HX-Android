package com.intfocus.hx.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.intfocus.hx.util.K.kAppVersion;
import static com.intfocus.hx.util.K.kUserName;

/**
 * Created by liuruilin on 2017/8/1.
 */

public class ActionLogUtil {

    /**
     * 上传锁屏信息
     *
     * @param state    是否启用锁屏
     * @param deviceID 设备标识
     * @param password 锁屏密码
     *
     * "id": ,
     * "screen_lock_state": ,
     * "screen_lock_type": "",
     * "screen_lock": ""
     */
    public static void screenLock(String deviceID, String password, boolean state) {
        String urlString = String.format(K.kScreenLockAPIPath, K.kBaseUrl);

        Map<String, String> params = new HashMap<>();
        params.put("screen_lock_state", "1");
        params.put("screen_lock_type", "4位数字");
        params.put("screen_lock", password);
        params.put("api_token", ApiHelper.checkApiToken("/api/v1.1/device/screen_lock"));
        params.put("id", deviceID);
        HttpUtil.httpPost(urlString, params);
    }

    /**
     * 上传用户行为
     *
     * @param context 上下文
     * @param param   用户行为
     */
    public static void actionLog(final Context context, final JSONObject param) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SharedPreferences mUserSP = context.getApplicationContext().getSharedPreferences("UserBean", MODE_PRIVATE);

                    param.put(K.kUserId, mUserSP.getString(K.kUserId, ""));
                    param.put(URLs.kUserNum, mUserSP.getString(URLs.kUserNum, ""));
                    param.put(kUserName, mUserSP.getString(K.kUserName, ""));
                    param.put(K.kUserDeviceId, mUserSP.getString(K.kUserDeviceId, ""));

                    PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                    param.put(kAppVersion, String.format("a%s", packageInfo.versionName));
                    param.put("coordinate", mUserSP.getString("coordinate", ""));

                    JSONObject params = new JSONObject();
                    params.put("action_log", param);

                    JSONObject userParams = new JSONObject();
                    userParams.put(kUserName, mUserSP.getString(kUserName, ""));
                    userParams.put("user_pass", mUserSP.getString(URLs.kPassword, ""));
                    params.put("user", userParams);

                    params.put("api_token", ApiHelper.checkApiToken("/api/v1.1/device/logger"));

                    String urlString = String.format(K.kActionLog, K.kBaseUrl);
                    HttpUtil.httpPost(urlString, params);
                } catch (JSONException | PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 上传登录失败行为
     *
     * @param context 上下文
     * @param param   用户行为
     */
    public static void actionLoginLog(final Context context, final JSONObject param) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SharedPreferences mUserSP = context.getApplicationContext().getSharedPreferences("UserBean", MODE_PRIVATE);

                    PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                    param.put(kAppVersion, String.format("a%s", packageInfo.versionName));
                    param.put("coordinate", mUserSP.getString("coordinate", ""));

                    JSONObject params = new JSONObject();
                    params.put("action_log", param);

                    params.put("api_token", ApiHelper.checkApiToken("/api/v1.1/device/logger"));

                    String urlString = String.format(K.kActionLog, K.kBaseUrl);
                    HttpUtil.httpPost(urlString, params);
                } catch (JSONException | PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
