package com.intfocus.hx.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtil {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String kUserAgent = "User-Agent";
    public static final String kContentType = "Content-Type";
    public static final String kFailedToConnectTo = "failed to connect to";
    public static final String kUnauthorized = "unauthorized";
    public static final String kUnableToResolveHost = "unable to resolve host";
    public static final String kApplicationJson = "application/json";
    public static final String kAccept = "Accept";

    /**
     * ִ执行一个HTTP GET请求，返回请求响应的HTML
     *
     * @param urlString 请求的URL地址
     * @return 返回请求响应的HTML
     */
    //@throws UnsupportedEncodingException
    public static Map<String, String> httpGet(Context ctx, String urlString, Map<String, String> headers) {
        LogUtil.d("GET", urlString);
        SharedPreferences mUserSP = ctx.getSharedPreferences("UserBean", Context.MODE_PRIVATE);
        String userNum = mUserSP.getString("user_num", "0");
        String userDeviceId = mUserSP.getString("user_device_id", "0");
        String appVision = mUserSP.getString("app_version", "0");

        Map<String, String> retMap = new HashMap<>();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();

        okhttp3.Request.Builder builder = new Request.Builder();

        builder.url(urlString).addHeader(kUserAgent, HttpUtil.webViewUserAgent());

        if (headers.containsKey(URLs.kETag)) {
            builder = builder.addHeader("IF-None-Match", headers.get(URLs.kETag));
        }
        if (headers.containsKey(URLs.kLastModified)) {
            builder = builder.addHeader("If-Modified-Since", headers.get(URLs.kLastModified));
        }
        Response response;
        Request baseRequest = builder.build();

        //提取api_path
        String apiPath = baseRequest.url().toString().replace(K.kBaseUrl, "");
        if (apiPath.contains("?")) {
            apiPath = apiPath.substring(0, apiPath.indexOf("?"));
        }

        //根据规则加密生成api_token
        String apiToken = Utils.getApiToken(apiPath);

        HttpUrl.Builder httpUrl = baseRequest.url().newBuilder()
                .addQueryParameter(K.API_TOKEN, apiToken)
                .addQueryParameter("_user_num", userNum)
                .addQueryParameter("_user_device_id", userDeviceId)
                .addQueryParameter("_app_version", appVision);

        // 新的请求--添加参数
        Request newRequest = baseRequest.newBuilder()
                .method(baseRequest.method(), baseRequest.body())
                .url(httpUrl.build())
                .build();

        try {
            response = client.newCall(newRequest).execute();
            Headers responseHeaders = response.headers();
            boolean isJSON = false;
            for (int i = 0, len = responseHeaders.size(); i < len; i++) {
                retMap.put(responseHeaders.name(i), responseHeaders.value(i));
                // Log.i("HEADER", String.format("Key : %s, Value: %s", responseHeaders.name(i), responseHeaders.value(i)));
                isJSON = responseHeaders.name(i).equalsIgnoreCase(kContentType) && responseHeaders.value(i).contains(kApplicationJson);
            }
            retMap.put(URLs.kCode, String.format("%d", response.code()));
            retMap.put(URLs.kBody, response.body().string());
//            LogUtil.d("BODY", retMap.get("body"));

            if (isJSON) {
                LogUtil.d("code", retMap.get("code"));
                LogUtil.d("responseBody", retMap.get("body"));
            }
        } catch (UnknownHostException e) {
            // 400: Unable to resolve host "yonghui.idata.mobi": No address associated with hostname
            if (e != null && e.getMessage() != null) {
                LogUtil.d("UnknownHostException2", e.getMessage());
            }
            retMap.put(URLs.kCode, "400");
            retMap.put(URLs.kBody, "{\"info\": \"请检查网络环境！\"}");
        } catch (Exception e) {
            // Default Response
            retMap.put(URLs.kCode, "400");
            retMap.put(URLs.kBody, "{\"info\": \"请检查网络环境！\"}");

            if (e != null && e.getMessage() != null) {
                String errorMessage = e.getMessage().toLowerCase();
                LogUtil.d("Exception", errorMessage);
                if (errorMessage.contains(kUnableToResolveHost) || errorMessage.contains(kFailedToConnectTo)) {
                    retMap.put(URLs.kCode, "400");
                    retMap.put(URLs.kBody, "{\"info\": \"请检查网络环境！\"}");
                } else if (errorMessage.contains(kUnauthorized)) {
                    retMap.put(URLs.kCode, "401");
                    retMap.put(URLs.kBody, "{\"info\": \"用户名或密码错误\"}");
                }
            }
        }
        return retMap;
    }

    /**
     * ִ执行一个HTTP POST请求，返回请求响应的HTML
     */
    //@throws UnsupportedEncodingException
    //@throws JSONException
    public static Map<String, String> httpPost(String urlString, Map params) {
        LogUtil.d("POST", urlString);
        Map<String, String> retMap = new HashMap<>();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();
        Request request;
        Response response;
        Request.Builder requestBuilder = new Request.Builder();
        if (params != null) {
            try {
                Iterator iter = params.entrySet().iterator();
                JSONObject holder = new JSONObject();

                while (iter.hasNext()) {
                    Map.Entry pairs = (Map.Entry) iter.next();
                    String key = (String) pairs.getKey();

                    if (pairs.getValue() instanceof Map) {
                        Map m = (Map) pairs.getValue();

                        JSONObject data = new JSONObject();
                        for (Object o : m.entrySet()) {
                            Map.Entry pairs2 = (Map.Entry) o;
                            data.put((String) pairs2.getKey(), pairs2.getValue());
                            holder.put(key, data);
                        }
                    } else {
                        holder.put(key, pairs.getValue());
                    }
                }
                requestBuilder.post(RequestBody.create(JSON, holder.toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            request = requestBuilder
                    .url(urlString)
                    .addHeader(kAccept, kApplicationJson)
                    .addHeader(kContentType, kApplicationJson)
                    .addHeader(kUserAgent, HttpUtil.webViewUserAgent())
                    .build();
            response = client.newCall(request).execute();

            Headers responseHeaders = response.headers();
            int headerSize = responseHeaders.size();
            for (int i = 0; i < headerSize; i++) {
                retMap.put(responseHeaders.name(i), responseHeaders.value(i));
            }

            retMap.put(URLs.kCode, String.format("%d", response.code()));
            retMap.put(URLs.kBody, response.body().string());

            LogUtil.d("code", retMap.get("code"));
            LogUtil.d("responseBody", retMap.get("body"));
        } catch (UnknownHostException e) {
            if (e != null && e.getMessage() != null) {
                LogUtil.d("UnknownHostException", e.getMessage());
            }
            retMap.put(URLs.kCode, "400");
            retMap.put(URLs.kBody, "{\"info\": \"请检查网络环境！\"}");
        } catch (Exception e) {
            // Default Response
            retMap.put(URLs.kCode, "400");
            retMap.put(URLs.kBody, "{\"info\": \"请检查网络环境！\"}");

            if (e != null && e.getMessage() != null) {
                String errorMessage = e.getMessage().toLowerCase();
                LogUtil.d("Exception", errorMessage);
                if (errorMessage.contains(kUnableToResolveHost) || errorMessage.contains(kFailedToConnectTo)) {
                    retMap.put(URLs.kCode, "400");
                    retMap.put(URLs.kBody, "{\"info\": \"请检查网络环境！\"}");
                } else if (errorMessage.contains(kUnauthorized)) {
                    retMap.put(URLs.kCode, "401");
                    retMap.put(URLs.kBody, "{\"info\": \"用户名或密码错误\"}");
                }
            }
        }
        return retMap;
    }


    /**
     * ִ执行一个HTTP POST请求，返回请求响应的HTML
     */
    public static Map<String, String> httpPost(String urlString, JSONObject params) {
        LogUtil.d("POST2", urlString);
        Map<String, String> retMap = new HashMap<>();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .build();
        Request request;
        Response response;
        Request.Builder requestBuilder = new Request.Builder();

        if (params != null) {
            requestBuilder.post(RequestBody.create(JSON, params.toString()));
            LogUtil.d("PARAM", params.toString());
        }
        try {
            request = requestBuilder
                    .url(urlString)
                    .addHeader(kAccept, kApplicationJson)
                    .addHeader(kContentType, kApplicationJson)
                    .addHeader(kUserAgent, HttpUtil.webViewUserAgent())
                    .build();
            response = client.newCall(request).execute();
            Headers responseHeaders = response.headers();
            for (int i = 0, headerSize = responseHeaders.size(); i < headerSize; i++) {
                retMap.put(responseHeaders.name(i), responseHeaders.value(i));
                LogUtil.d("HEADER", String.format("Key : %s, Value: %s", responseHeaders.name(i),
                        responseHeaders.value(i)));
            }
            retMap.put(URLs.kCode, String.format("%d", response.code()));
            retMap.put("body", response.body().string());

            LogUtil.d("code", retMap.get("code"));
            LogUtil.d("responseBody", retMap.get("body"));
        } catch (UnknownHostException e) {
            if (e != null && e.getMessage() != null) {
                LogUtil.d("UnknownHostException2", e.getMessage());
            }
            retMap.put(URLs.kCode, "400");
            retMap.put(URLs.kBody, "{\"info\": \"请检查网络环境！\"}");
        } catch (Exception e) {
            retMap.put(URLs.kCode, "400");
            retMap.put(URLs.kBody, "{\"info\": \"请检查网络环境！\"}");

            if (e != null && e.getMessage() != null) {
                String errorMessage = e.getMessage().toLowerCase();
                LogUtil.d("Exception2", errorMessage);
                if (errorMessage.contains(kUnableToResolveHost) || errorMessage.contains("failed to connect to")) {
                    retMap.put(URLs.kCode, "400");
                    retMap.put(URLs.kBody, "{\"info\": \"请检查网络环境！\"}");
                } else if (errorMessage.contains(kUnauthorized)) {
                    retMap.put(URLs.kCode, "401");
                    retMap.put(URLs.kBody, "{\"info\": \"用户名或密码错误\"}");
                }
            }
        }
        return retMap;
    }

    /**
     * ִ执行一个HTTP POST请求，上传文件
     */
    public static Map<String, String> httpPostFile(String urlString, String fileType, String fileKey, String filePath) {
        Map<String, String> retMap = new HashMap<>();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();

        Request request;
        Response response;
        Request.Builder requestBuilder = new Request.Builder();
        try {
            File file = new File(filePath);
            RequestBody fileBody = RequestBody.create(MediaType.parse(fileType), file);
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            builder.addFormDataPart(fileKey, file.getName(), fileBody);
            MultipartBody requestBody = builder.build();

            request = requestBuilder
                    .url(urlString)
                    .post(requestBody)
                    .build();
            response = client.newCall(request).execute();

            retMap.put(URLs.kCode, String.format("%d", response.code()));
            retMap.put("body", response.body().string());
        } catch (UnknownHostException e) {
            if (e != null && e.getMessage() != null) {
                LogUtil.d("UnknownHostException2", e.getMessage());
            }
            retMap.put(URLs.kCode, "400");
            retMap.put(URLs.kBody, "{\"info\": \"请检查网络环境！\"}");
        } catch (Exception e) {
            retMap.put(URLs.kCode, "400");
            retMap.put(URLs.kBody, "{\"info\": \"请检查网络环境！\"}");
        }
        return retMap;
    }

    public static String urlToFileName(String urlString) {
        String path = "default";
        try {
            urlString = urlString.replace(K.kBaseUrl, "");
            URI uri = new URI(urlString);
            path = uri.getPath().replace("/", "_");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.format("%s.html", path);
    }

    private static String webViewUserAgent() {
        String userAgent = System.getProperty("http.agent");
        if (userAgent == null) {
            userAgent = "Mozilla/5.0 (Linux; U; Android 4.3; en-us; HTC One - 4.3 - API 18 - 1080x1920 Build/JLS36G) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30 default-by-hand";
        }

        return userAgent;
    }

    /**
     * Check if there is any connectivity
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isAvailable());
    }

    public static class DownloadAssetsTask extends AsyncTask<String, Integer, String> {
        private final Context context;
        private PowerManager.WakeLock mWakeLock;
        private final String assetFilename;
        private final boolean isInAssets;

        public DownloadAssetsTask(Context context, String assetFilename, boolean isInAssets) {
            this.context = context;
            this.assetFilename = assetFilename;
            this.isInAssets = isInAssets;
        }

        @Override
        protected String doInBackground(String... params) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();
                input = connection.getInputStream();
                output = new FileOutputStream(params[1]);

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                LogUtil.d("Exception", e.toString());
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }
                if (connection != null)
                    connection.disconnect();
            }
            FileUtil.checkAssets(context, assetFilename, isInAssets);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();

            if (result != null) {
                ToastUtils.INSTANCE.show(context, String.format("静态资源更新失败(%s)", result));
            }
        }
    }

    /**
     * 检测服务器端静态文件是否更新
     * to do
     */
    public static void checkAssetsUpdated(final Context context) {
        checkAssetUpdated(context, URLs.kLoading, false);
        checkAssetUpdated(context, URLs.kFonts, true);
        checkAssetUpdated(context, URLs.kImages, true);
        checkAssetUpdated(context, URLs.kIcons, true);
        checkAssetUpdated(context, URLs.kStylesheets, true);
        checkAssetUpdated(context, URLs.kJavaScripts, true);
    }

    public static void checkAssetUpdated(Context context, String assetName, boolean isInAssets) {
        SharedPreferences mAssetsSP = context.getSharedPreferences("AssetsMD5", Context.MODE_PRIVATE);
        boolean isShouldUpdateAssets = false;
        String sharedPath = FileUtil.sharedPath(context);
        String assetZipPath = String.format("%s/%s.zip", sharedPath, assetName);

        String localKeyName = String.format("local_%s_md5", assetName);
        String keyName = String.format("%s_md5", assetName);
        isShouldUpdateAssets = !mAssetsSP.getString(localKeyName, "0").equals(mAssetsSP.getString(keyName, "0"));
        if (!isShouldUpdateAssets) {
            return;
        }

        LogUtil.d("checkAssetUpdated", String.format("%s: %s != %s", assetZipPath, mAssetsSP.getString(localKeyName, ""), mAssetsSP.getString(keyName, "")));
        // execute this when the downloader must be fired
        final HttpUtil.DownloadAssetsTask downloadTask = new DownloadAssetsTask(context, assetName, isInAssets);

        // AsyncTask并行下载
        downloadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, String.format(K.kDownloadAssetsAPIPath, K.kBaseUrl, assetName), assetZipPath);
    }

    /*
     * Zip 档下载
     */
    public static Map<String, String> downloadZip(String urlString, String outputPath, Map<String, String> headers) {
        Map<String, String> response = new HashMap<>();
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", HttpUtil.webViewUserAgent());
            connection.setConnectTimeout(5 * 1000);
            connection.setReadTimeout(10 * 1000);
            if (headers.containsKey(URLs.kETag)) {
                connection.setRequestProperty("IF-None-Match", headers.get(URLs.kETag));
            }
            if (headers.containsKey(URLs.kLastModified)) {
                connection.setRequestProperty("If-Modified-Since", headers.get(URLs.kLastModified));
            }

            connection.connect();
            response.put(URLs.kCode, String.format("%d", connection.getResponseCode()));
            Map<String, List<String>> map = connection.getHeaderFields();
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                response.put(entry.getKey(), entry.getValue().get(0));
            }
            Log.i("DownloadZIP", String.format("%d - %s - %s", connection.getResponseCode(), urlString, response.toString()));

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return response;
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();
            input = connection.getInputStream();
            output = new FileOutputStream(outputPath);

            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                output.write(data, 0, count);
            }
        } catch (Exception e) {
            LogUtil.d("Exception", e.toString());
            response.put(URLs.kCode, "400");
            return response;
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }
        return response;
    }

    /*
     * 判断当前网络状态是否为 WiFi
     */
    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }

        return false;
    }
}
