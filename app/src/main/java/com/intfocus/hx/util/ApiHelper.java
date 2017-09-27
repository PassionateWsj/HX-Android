package com.intfocus.hx.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.intfocus.hx.constant.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class ApiHelper {
    /*
     * 用户登录验证
     * params: {device: {name, platform, os, os_version, uuid}}
     */
//    public static String authentication(Context context, String username, String password) {
//        String responseState = "success", urlString = String.format(K.kUserAuthenticateAPIPath, K.kBaseUrl, "android", username, password);
//        SharedPreferences mUserSP = context.getApplicationContext().getSharedPreferences("UserBean", MODE_PRIVATE);
//        try {
//            JSONObject device = new JSONObject();
//            device.put("name", android.os.Build.MODEL);
//            device.put("platform", "android");
//            device.put("os", android.os.Build.MODEL);
//            device.put("os_version", Build.VERSION.RELEASE);
//            device.put("uuid", OpenUDID_manager.getOpenUDID());
//
//            JSONObject params = new JSONObject();
//            params.put("device", device);
//            params.put("coordinate", mUserSP.getString("location", "0,0"));
//            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
//            params.put(K.kAppVersion, String.format("a%s", packageInfo.versionName));
//
//            mUserSP.edit().putString(K.kAppVersion, String.format("a%s", packageInfo.versionName)).commit();
//            mUserSP.edit().putString("os_version", "android" + Build.VERSION.RELEASE).commit();
//            mUserSP.edit().putString("device_info", android.os.Build.MODEL).commit();
//
//            Log.i("DeviceParams", params.toString());
//
//            Map<String, String> response = HttpUtil.httpPost(urlString, params);
//            String userConfigPath = String.format("%s/%s", FileUtil.basePath(context), K.kUserConfigFileName);
//            JSONObject userJSON = FileUtil.readConfigFile(userConfigPath);
//            userJSON.put(URLs.kPassword, password);
//            userJSON.put(URLs.kIsLogin, response.get(URLs.kCode).equals("200"));
//
//            if (response.get(URLs.kCode).equals("400")) {
//                return "请检查网络环境";
//            } else if (response.get(URLs.kCode).equals("401")) {
//                return new JSONObject(response.get(URLs.kBody)).getString(kInfo);
//            } else if (response.get(URLs.kCode).equals("408")) {
//                return "连接超时";
//            } else if (!response.get(URLs.kCode).equals("200")) {
//                return response.get(URLs.kBody);
//            }
//            // FileUtil.dirPath 需要优先写入登录用户信息
//            JSONObject responseJSON = new JSONObject(response.get(URLs.kBody));
//            userJSON = ApiHelper.mergeJson(userJSON, responseJSON);
//            FileUtil.writeFile(userConfigPath, userJSON.toString());
//
//            String settingsConfigPath = FileUtil.dirPath(context, K.kConfigDirName, K.kSettingConfigFileName);
//            if ((new File(settingsConfigPath)).exists()) {
//                JSONObject settingJSON = FileUtil.readConfigFile(settingsConfigPath);
//                userJSON.put(URLs.kUseGesturePassword, settingJSON.has(URLs.kUseGesturePassword) ? settingJSON.getBoolean(URLs.kUseGesturePassword) : false);
//                userJSON.put(URLs.kGesturePassword, settingJSON.has(URLs.kGesturePassword) ? settingJSON.getString(URLs.kGesturePassword) : "");
//            } else {
//                userJSON.put(URLs.kUseGesturePassword, false);
//                userJSON.put(URLs.kGesturePassword, "");
//            }
//
//            JSONObject assetsJSON = userJSON.getJSONObject(URLs.kAssets);
//            userJSON.put(kFontsMd5, assetsJSON.getString(kFontsMd5));
//            userJSON.put(kImagesMd5, assetsJSON.getString(kImagesMd5));
//            userJSON.put(kIconsMd5, assetsJSON.getString(kIconsMd5));
//            userJSON.put(K.kStylesheetsMd5, assetsJSON.getString(K.kStylesheetsMd5));
//            userJSON.put(K.kJavaScriptsMd5, assetsJSON.getString(K.kJavaScriptsMd5));
//
//            FileUtil.writeFile(userConfigPath, userJSON.toString());
//            mUserSP.edit().putString(kUserName, userJSON.getString(URLs.kUserName)).commit();
//            mUserSP.edit().putInt(kGroupId, userJSON.getInt(kGroupId)).commit();
//            mUserSP.edit().putInt(kRoleId, userJSON.getInt(kRoleId)).commit();
//            mUserSP.edit().putInt(kUserId, userJSON.getInt(kUserId)).commit();
//            mUserSP.edit().putString(URLs.kRoleName, userJSON.getString(URLs.kRoleName)).commit();
//            mUserSP.edit().putString(URLs.kGroupName, userJSON.getString(URLs.kGroupName)).commit();
//            mUserSP.edit().putString(kUserNum, userJSON.getString(kUserNum)).commit();
//            mUserSP.edit().putInt(kUserDeviceId, userJSON.getInt(K.kUserDeviceId)).commit();
//            mUserSP.edit().putString(kCurrentUIVersion, "v2").commit();
//
//            if (response.get(URLs.kCode).equals("200")) {
//                // 第三方消息推送，设备标识
//                ActionLogUtil.pushDeviceToken(context, userJSON.getString("device_uuid"));
//
//                FileUtil.writeFile(settingsConfigPath, userJSON.toString());
//            } else {
//                responseState = responseJSON.getString(kInfo);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            responseState = e.getMessage();
//        }
//        return responseState;
//    }

    /*
     *  获取报表网页数据
     */
    public static boolean reportData(Context context, String groupID, String templateID, String reportID) {
//        String urlString = String.format(K.kReportDataAPIPath, K.kBaseUrl, groupID, templateID, reportID);
        // %s/api/v1.1/report/data?api_token=%s&group_id=%s&template_id=%s&report_id=%s&disposition=zip
        String urlString = String.format(K.KReportZipData, K.kBaseUrl, URLs.MD5(K.ANDROID_API_KEY + K.KReportBaseApi  + K.ANDROID_API_KEY), groupID, templateID, reportID);
        String assetsPath = FileUtil.sharedPath(context);
        String headerPath = String.format("%s/%s", assetsPath, K.kCachedHeaderConfigFileName);
        File headerFile = new File(headerPath);
        if (headerFile.exists()) {
            headerFile.delete();
        }
        Map<String, String> headers = ApiHelper.checkResponseHeader(urlString, assetsPath);
        String jsFileName = String.format("group_%s_template_%s_report_%s.js", groupID, templateID, reportID);
        String cachedZipPath = FileUtil.dirPath(context, K.kCachedDirName, String.format("%s.zip", jsFileName));


        Map<String, String> response = HttpUtil.downloadZip(urlString, cachedZipPath, headers);

        //添加code字段是否存在。原因:网络不好的情况下response为{}
        if (!response.containsKey(URLs.kCode)) {
            return false;
        }

        String codeStatus = response.get(URLs.kCode);

        switch (codeStatus) {
            case "200":

            case "201":
                break;
            case "304":
                break;
            default:
                return false;
        }

        try {
            //获取的内容为attachment; filename="group_%s_template_%s_report_%s.js.zip"
            String contentDis = response.get("Content-Disposition");

            //获取的内容为 group_%s_template_%s_report_%s.js.zip
            String subContentDis = contentDis.substring(contentDis.indexOf("\"") + 1, contentDis.lastIndexOf("\""));

            jsFileName = subContentDis.replace(".zip", "");
            String javascriptPath = String.format("%s/assets/javascripts/%s", assetsPath, jsFileName);

            ApiHelper.storeResponseHeader(urlString, assetsPath, response);

            InputStream zipStream = new FileInputStream(cachedZipPath);
            FileUtil.unZip(zipStream, FileUtil.dirPath(context, K.kCachedDirName), true);
            zipStream.close();
            String jsFilePath = FileUtil.dirPath(context, K.kCachedDirName, jsFileName);
            File jsFile = new File(jsFilePath);
            if (jsFile.exists()) {
                FileUtil.copyFile(jsFilePath, javascriptPath);
                jsFile.delete();
            }
            new File(cachedZipPath).delete();

            String searchItemsPath = String.format("%s.search_items", javascriptPath);
            File searchItemsFile = new File(searchItemsPath);
            if (searchItemsFile.exists()) {
                searchItemsFile.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
            deleteHeadersFile(assetsPath);
            return false;
        }
        return true;
    }

    /*
     *  获取报表 JSON 数据
     */
    public static boolean reportJsonData(Context context, String groupID, String templateID, String reportID) {
        String urlString = String.format(K.kReportJsonDataAPIPath, K.kBaseUrl, groupID, templateID, reportID);
        String assetsPath = FileUtil.sharedPath(context);
        Map<String, String> headers = ApiHelper.checkResponseHeader(urlString, assetsPath);
        String jsonFileName = String.format("group_%s_template_%s_report_%s.json", groupID, templateID, reportID);
        String cachedZipPath = FileUtil.dirPath(context, K.kCachedDirName, String.format("%s.zip", jsonFileName));
        Map<String, String> response = HttpUtil.downloadZip(urlString, cachedZipPath, headers);
        String jsonFilePath = FileUtil.dirPath(context, K.kCachedDirName, jsonFileName);

        //添加code字段是否存在。原因:网络不好的情况下response为{}
        if (!response.containsKey(URLs.kCode)) {
            return false;
        }

        String codeStatus = response.get(URLs.kCode);
        switch (codeStatus) {
            case "200":

            case "201":
                break;
            case "304":
                if (new File(jsonFilePath).exists()) {
                    return true;
                }
                break;
            default:
                return false;
        }

        try {
            ApiHelper.storeResponseHeader(urlString, assetsPath, response);

            InputStream zipStream = new FileInputStream(cachedZipPath);
            FileUtil.unZip(zipStream, FileUtil.dirPath(context, K.kCachedDirName), true);
            zipStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            deleteHeadersFile(assetsPath);
            return false;
        }
        return true;
    }


    public static void deleteHeadersFile(String assetsPath) {
        String headersFilePath = String.format("%s/%s", assetsPath, K.kCachedHeaderConfigFileName);
        if ((new File(headersFilePath)).exists()) {
            new File(headersFilePath).delete();
        }
    }

    public static Map<String, String> httpGetWithHeader(Context ctx, String urlString, String assetsPath, String relativeAssetsPath) {
        Map<String, String> retMap = new HashMap<>();

        String urlKey = urlString.contains("?") ? TextUtils.split(urlString, "?")[0] : urlString;

        try {

            Map<String, String> headers = new HashMap<>();
            Map<String, String> response = HttpUtil.httpGet(ctx, urlKey, headers);

            String statusCode = response.get(URLs.kCode);
            retMap.put(URLs.kCode, statusCode);

            String htmlName = HttpUtil.urlToFileName(urlString);
            String htmlPath = String.format("%s/%s", assetsPath, htmlName);
            retMap.put("path", htmlPath);

            if (statusCode.equals("200")) {
                ApiHelper.storeResponseHeader(urlKey, assetsPath, response);

                String htmlContent = response.get(URLs.kBody);

                htmlContent = htmlContent.replace("/javascripts/", String.format("%s/javascripts/", relativeAssetsPath));
                htmlContent = htmlContent.replace("/stylesheets/", String.format("%s/stylesheets/", relativeAssetsPath));
                htmlContent = htmlContent.replace("/images/", String.format("%s/images/", relativeAssetsPath));
                FileUtil.writeFile(htmlPath, htmlContent);
            } else {
                retMap.put(URLs.kCode, statusCode);
            }
        } catch (Exception e) {
            retMap.put(URLs.kCode, "500");
            e.printStackTrace();
        }
        return retMap;
    }

    /*
     * 缓存文件中，清除指定链接的内容
     *
     * @param 链接
     * @param 缓存头文件相对文件夹
     */
    public static void clearResponseHeader(String urlKey, String assetsPath) {
        String headersFilePath = String.format("%s/%s", assetsPath, K.kCachedHeaderConfigFileName);
        if (!(new File(headersFilePath)).exists()) {
            return;
        }

        JSONObject headersJSON = FileUtil.readConfigFile(headersFilePath);
        if (headersJSON.has(urlKey)) {
            try {
                headersJSON.remove(urlKey);
                Log.i("clearResponseHeader", String.format("%s[%s]", headersFilePath, urlKey));

                FileUtil.writeFile(headersFilePath, headersJSON.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从缓存头文件中，获取指定链接的ETag/Last-Modified
     *
     * @param urlKey     链接
     * @param assetsPath 缓存头文件相对文件夹
     */
    public static Map<String, String> checkResponseHeader(String urlKey, String assetsPath) {
        Map<String, String> headers = new HashMap<>();

        try {
            JSONObject headersJSON = new JSONObject();

            String headersFilePath = String.format("%s/%s", assetsPath, K.kCachedHeaderConfigFileName);
            if ((new File(headersFilePath)).exists()) {
                headersJSON = FileUtil.readConfigFile(headersFilePath);
            }

            JSONObject headerJSON;
            if (headersJSON.has(urlKey)) {
                headerJSON = (JSONObject) headersJSON.get(urlKey);

                if (headerJSON.has(URLs.kETag)) {
                    headers.put(URLs.kETag, headerJSON.getString(URLs.kETag));
                }
                if (headerJSON.has(URLs.kLastModified)) {
                    headers.put(URLs.kLastModified, headerJSON.getString(URLs.kLastModified));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return headers;
    }

    /**
     * 把服务器响应的ETag/Last-Modified存入本地
     *
     * @param urlKey     链接
     * @param assetsPath 缓存头文件相对文件夹
     * @param response   服务器响应的ETag/Last-Modifiede
     */
    public static void storeResponseHeader(String urlKey, String assetsPath, Map<String, String> response) {
        try {
            JSONObject headersJSON = new JSONObject();

            String headersFilePath = String.format("%s/%s", assetsPath, K.kCachedHeaderConfigFileName);
            if ((new File(headersFilePath)).exists()) {
                headersJSON = FileUtil.readConfigFile(headersFilePath);
            }

            JSONObject headerJSON = new JSONObject();

            if (response.containsKey(URLs.kETag)) {
                headerJSON.put(URLs.kETag, response.get(URLs.kETag));
            }
            if (response.containsKey(URLs.kLastModified)) {
                headerJSON.put(URLs.kLastModified, response.get(URLs.kLastModified));
            }

            headersJSON.put(urlKey, headerJSON);
            FileUtil.writeFile(headersFilePath, headersJSON.toString());
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 合并两个JSONObject
     *
     * @param obj   JSONObject
     * @param other JSONObject
     * @return 合并后的JSONObject
     */
    public static JSONObject mergeJson(JSONObject obj, JSONObject other) {
        try {
            Iterator it = other.keys();
            while (it.hasNext()) {
                String key = (String) it.next();
                obj.put(key, other.get(key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }

    /**
     * 下载文件
     *
     * @param context    上下文
     * @param urlString  下载链接
     * @param outputFile 写入本地文件路径
     */
    public static void downloadFile(Context context, String urlString, File outputFile) {
        try {
            URL url = new URL(urlString);
            String headerPath = String.format("%s/%s/%s", FileUtil.basePath(context), K.kCachedDirName, K.kCachedHeaderConfigFileName);

            File cachePath = new File(String.format("%s/%s", FileUtil.basePath(context), K.kCachedDirName));
            if (!cachePath.exists()) {
                cachePath.mkdirs();
            }
            JSONObject headerJSON = new JSONObject();
            if ((new File(headerPath)).exists()) {
                headerJSON = FileUtil.readConfigFile(headerPath);
            }

            URLConnection conn = url.openConnection();
            String etag = conn.getHeaderField(URLs.kETag);

            boolean isDownloaded = outputFile.exists() && headerJSON.has(urlString) && etag != null && !etag.isEmpty() && headerJSON.getString(urlString).equals(etag);

            if (isDownloaded) {
                Log.i("downloadFile", "exist - " + outputFile.getAbsolutePath());
            } else {
                InputStream in = url.openStream();
                FileOutputStream fos = new FileOutputStream(outputFile);

                int length;
                byte[] buffer = new byte[1024];// buffer for portion of data from connection
                while ((length = in.read(buffer)) > -1) {
                    fos.write(buffer, 0, length);
                }
                fos.close();
                in.close();

                if (etag != null && !etag.isEmpty()) {
                    headerJSON.put(urlString, etag);
                    FileUtil.writeFile(headerPath, headerJSON.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    /**
//     * 二维码扫描
//     *
//     * @param groupID  群组ID
//     * @param roleID   角色ID
//     * @param userNum  用户编号
//     * @param storeID  门店ID
//     * @param codeInfo 条形码信息
//     * @param codeType 条形码或二维码
//     */
//    public static Map<String, String> barCodeScan(String groupID, String roleID, String userNum, String storeID, String codeInfo, String codeType) {
//        try {
//            JSONObject params = new JSONObject();
//            params.put(URLs.kCodeInfo, codeInfo);
//            params.put(URLs.kCodeType, codeType);
//
//            String urlString = String.format(K.kBarCodeScanAPIPath, K.kBaseUrl, groupID, roleID, userNum, storeID, codeInfo, codeType);
//            // Map<String, String> response = HttpUtil.httpPost(urlString, params);
//
//            return (Map<String, String>) HttpUtil.httpGet(urlString, new HashMap());
//        } catch (JSONException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    public static String getLocation(Context context) {
        try {
            SharedPreferences mUserSP = context.getSharedPreferences("UserBean", MODE_PRIVATE);

            String locationInfo = "";
            String locationProvider = "";
            //获取地理位置管理器
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            //获取所有可用的位置提供器
            List<String> providers = locationManager.getProviders(true);

            if (providers.contains(LocationManager.GPS_PROVIDER) && locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
                //如果是GPS
                locationProvider = LocationManager.GPS_PROVIDER;
            } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
                //如果是Network
                locationProvider = LocationManager.NETWORK_PROVIDER;
            } else {
                return null;
            }

            //获取Location
            Location location;

            location = locationManager.getLastKnownLocation(locationProvider);
            if (location != null) {
                locationInfo = String.format("%.6f", location.getLongitude()) + "," + String.format("%.6f", location.getLatitude());
            }

            mUserSP.edit().putString("coordinate", locationInfo).commit();
            return locationInfo;
        } catch (SecurityException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void getAMapLocation(final Context ctx) {
        //初始化client
        AMapLocationClient locationClient = new AMapLocationClient(ctx);
        locationClient.setApiKey(Constants.GAODE_MAP_APP_KEY);
        AMapLocationClientOption locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation location) {
                if (null != location) {
                    StringBuffer sb = new StringBuffer();
                    //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                    if (location.getErrorCode() == 0) {
                        SharedPreferences mUserSP = ctx.getSharedPreferences("UserBean", Context.MODE_PRIVATE);
                        mUserSP.edit().putString("location",
                                String.format("%.6f", location.getLongitude()) + ","
                                        + String.format("%.6f", location.getLatitude())).commit();

                        sb.append("经    度    : " + location.getLongitude() + "\n");
                        sb.append("纬    度    : " + location.getLatitude() + "\n");
                    } else {
                        //定位失败
                        sb.append("错误码:" + location.getErrorCode() + "\n");
                        sb.append("错误信息:" + location.getErrorInfo() + "\n");
                        sb.append("错误描述:" + location.getLocationDetail() + "\n");
                    }

                    //解析定位结果
                    String result = sb.toString();
                    Log.i("testlog", result);
                } else {
                    Log.i("testlog", "定位失败，loc is null");
                }
            }
        });
        locationClient.startLocation();
    }

    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    public static AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(60000 * 10);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    public static String checkApiToken(String url) {
        return URLs.MD5(K.ANDROID_API_KEY + url + K.ANDROID_API_KEY );
    }
}
