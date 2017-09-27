package com.intfocus.hx.net;

import android.annotation.SuppressLint;
import android.content.Context;

import com.intfocus.hx.YHApplication;
import com.intfocus.hx.util.HttpUtil;
import com.intfocus.hx.util.PrivateURLs;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by admin on 2016/6/28.
 */
public class RetrofitUtil {
    private static final int DEFAULT_TIME_OUT = 2 * 1000;
    private static final String DEFAULT_BASE_URL = PrivateURLs.kBaseUrl;
    private HttpService httpService;
    private Context ctx;

    private RetrofitUtil(Context ctx) {
        this.ctx = ctx;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DEFAULT_BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                 /*设置 自定义ResponseConverterFactory处理服务器返回错误码*/
                .addConverterFactory(ResponseConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                 /*设置 ScalarsConverterFactory返回纯String*/
//                .addConverterFactory(ScalarsConverterFactory.create())
                .client(getClientBuilder().build())
                .build();
        httpService = retrofit.create(HttpService.class);
    }

    public static HttpService getHttpService(Context ctx) {
        return getInstance(ctx).httpService;
    }

    public static RetrofitUtil getInstance(Context ctx) {
        return RetrofitHolder.retrofitUtil(ctx);
    }

    public OkHttpClient.Builder getClientBuilder() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.hostnameVerifier(new TrustAllHostnameVerifier());
        try {
            // 自定义一个信任所有证书的TrustManager，添加SSLSocketFactory的时候要用到
            final X509TrustManager trustAllCert =
                    new TrustAllManager();
            final SSLSocketFactory sslSocketFactory = new SSLSocketFactoryCompat(trustAllCert);
            clientBuilder.sslSocketFactory(sslSocketFactory, trustAllCert);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
//        clientBuilder.sslSocketFactory(createSSLSocketFactory());
        //Http状态码处理,针对已知状态码的处理
        clientBuilder.addInterceptor(new HttpStateInterceptor());
        clientBuilder.addInterceptor(new BaseParamsIntercepter(ctx));
        clientBuilder.addInterceptor(new NetworkInterceptor());
        clientBuilder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);
        return clientBuilder;
    }

    private static class RetrofitHolder {
        private static RetrofitUtil retrofitUtil(Context ctx) {
            return new RetrofitUtil(ctx);
        }
    }

    public static class CommonOptions<T> implements Observable.Transformer<T, T> {
        @Override
        public Observable<T> call(Observable<T> observable) {
            return observable
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    /*事件发送前执行，处理准备工作，在之后最近的subscribeOn指定的线程上执行*/
                    .doOnSubscribe(new Action0() {
                        @Override
                        public void call() {
                            if (!HttpUtil.isConnected(YHApplication.globalContext)) {
                                throw new NetStatusException(9000, "网络未连接");
                            }
                        }
                    })
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread());
                    /*捕获错误，发送一个默认值*/
//                    .onErrorReturn(new Func1<Throwable, T>() {
//                        @Override
//                        public T call(Throwable throwable) {
//                            return null;
//                        }
//                    });
        }
    }

    /**
     * 默认信任所有的证书
     * TODO 最好加上证书认证，主流App都有自己的证书
     *
     * @return
     */
    @SuppressLint("TrulyRandom")
    private static SSLSocketFactory createSSLSocketFactory() {

        SSLSocketFactory sSLSocketFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllManager()},
                    new SecureRandom());
            sSLSocketFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return sSLSocketFactory;
    }

    private static class TrustAllManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)

                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}
