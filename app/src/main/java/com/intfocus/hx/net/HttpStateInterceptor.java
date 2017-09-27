package com.intfocus.hx.net;

import com.google.gson.Gson;
import com.intfocus.hx.data.response.BaseResult;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

import static com.alibaba.fastjson.util.IOUtils.UTF8;

/**
 * Created by CANC on 2017/8/11.
 * 处理系统特定Http 状·态码
 */
public class HttpStateInterceptor implements Interceptor {
    public static final int PARAMTER_ERROR = 400; //参数校验错误
    public static final int TOKEN_FAILED_CODE = 401; //未登录
    public static final int NO_PERMISSION_CODE = 403;// 无权限访问
    public static final int INTERNAL_ERROR = 500;// 服务器问题
    public static final int ERROR_502 = 502;// 服务器问题
    public static final int UN_FOUND = 404;// 服务器问题

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        if (response.code() == PARAMTER_ERROR || response.code() == TOKEN_FAILED_CODE
                || response.code() == NO_PERMISSION_CODE || response.code() == INTERNAL_ERROR
                || response.code() == UN_FOUND || response.code() == ERROR_502) {
            String msg = "";
            ResponseBody responseBody = response.body();
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();
            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            String bodyString = buffer.clone().readString(charset);
            try {
                BaseResult baseResult = new Gson().fromJson(bodyString, BaseResult.class);
                if (baseResult != null) {
                    msg = baseResult.getMessage();
                }
            } catch (Exception e) {
                msg = e.getMessage().toString();
            }
            HttpStateException interceptorExceptioin = new HttpStateException();
            interceptorExceptioin.setErrorCode(response.code());
            interceptorExceptioin.setMsg(msg);
            throw interceptorExceptioin;
        }
        return response;
    }
}
