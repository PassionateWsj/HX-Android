package com.intfocus.hx.net;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.intfocus.hx.util.K;
import com.intfocus.hx.util.Utils;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by CANC on 2017/8/10.
 * 用于统一添加请求参数
 * 基本请求参数: user_num, user_device_id, app_version
 */

public class BaseParamsIntercepter implements Interceptor {
    private SharedPreferences mUserSP;

    public BaseParamsIntercepter(Context ctx) {
        mUserSP = ctx.getSharedPreferences("UserBean", Context.MODE_PRIVATE);
    }

    /**
     * @param chain
     * @return
     * @throws IOException
     */
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oriRequest = chain.request();
        //提取api_path
        String apiPath = oriRequest.url().toString().replace(K.kBaseUrl, "");
        if (apiPath.contains("?")) {
            apiPath = apiPath.substring(0, apiPath.indexOf("?"));
        }

        //根据规则加密生成api_token
        String apiToken = Utils.getApiToken(apiPath);

        String userNum = mUserSP.getString("user_num", "0");
        String userDeviceId = mUserSP.getString("user_device_id", "0");
        String appVision = mUserSP.getString("app_version", "0");

        //把api_token添加进url中
        HttpUrl.Builder authorizedUrlBuilder = oriRequest.url()
                .newBuilder()
                .scheme(oriRequest.url().scheme())
                .host(oriRequest.url().host())
                .addQueryParameter(K.API_TOKEN, apiToken)
                .addQueryParameter("_user_num", userNum)
                .addQueryParameter("_user_device_id", userDeviceId)
                .addQueryParameter("_app_version", appVision);

        // 新的请求--添加参数
        Request newRequest = oriRequest.newBuilder()
                .method(oriRequest.method(), oriRequest.body())
                .url(authorizedUrlBuilder.build())
                .build();

        Log.i("apiPath", apiPath);
        Log.i("apiToken", apiToken);
        okhttp3.Response response = chain.proceed(newRequest);
        return response;
    }
}
