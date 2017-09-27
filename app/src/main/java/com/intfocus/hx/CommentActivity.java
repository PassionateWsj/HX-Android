package com.intfocus.hx;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.intfocus.hx.base.BaseActivity;
import com.intfocus.hx.data.request.CommentBody;
import com.intfocus.hx.data.response.BaseResult;
import com.intfocus.hx.net.ApiException;
import com.intfocus.hx.net.CodeHandledSubscriber;
import com.intfocus.hx.net.RetrofitUtil;
import com.intfocus.hx.util.ActionLogUtil;
import com.intfocus.hx.util.K;
import com.intfocus.hx.util.ToastColor;
import com.intfocus.hx.util.ToastUtils;
import com.intfocus.hx.util.URLs;

import org.json.JSONObject;

public class CommentActivity extends BaseActivity {

    private String bannerName;
    private String objectID;
    private String objectType;
    private int loadCount = 0;
    private FrameLayout mWebFrameLayout;

    @Override
    @SuppressLint("SetJavaScriptEnabled")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        TextView mTitle = (TextView) findViewById(R.id.bannerTitle);

        mWebFrameLayout = (FrameLayout) findViewById(R.id.browser);
        mWebView = new WebView(getApplicationContext());
        mWebFrameLayout.addView(mWebView, 0);

        initSubWebView();

        mWebView.requestFocus();
        mWebView.addJavascriptInterface(new JavaScriptInterface(), URLs.kJSInterfaceName);
        mWebView.loadUrl(urlStringForLoading);

        Intent intent = getIntent();
        bannerName = intent.getStringExtra(URLs.kBannerName);
        objectID = intent.getStringExtra(URLs.kObjectId);
        objectType = intent.getStringExtra(URLs.kObjectType);

        mTitle.setText(bannerName);
        urlString = String.format(K.kCommentMobilePath, K.kBaseUrl, URLs.currentUIVersion(mAppContext), objectID, objectType);

        new Thread(mRunnableForDetecting).start();
    }

    protected void onResume() {
        mMyApp.setCurrentActivity(this);
        super.onResume();
    }

    /*
     * 返回
     */
    public void dismissActivity(View v) {
        CommentActivity.this.onBackPressed();
    }

    private class JavaScriptInterface extends JavaScriptBase {
        /*
         * JS 接口，暴露给JS的方法使用@JavascriptInterface装饰
         */
        @JavascriptInterface
        public void writeComment(final String content) {
            CommentBody commentBody = new CommentBody();
            commentBody.setUser_num(mUserSP.getString(URLs.kUserNum, "0"));
            commentBody.setContent(content);
            commentBody.setObject_type(objectType);
            commentBody.setObject_id(objectID);
            commentBody.setObject_title(bannerName);

            RetrofitUtil.getHttpService(getApplicationContext()).submitComment(commentBody)
                    .compose(new RetrofitUtil.CommonOptions<BaseResult>())
                    .subscribe(new CodeHandledSubscriber<BaseResult>() {
                        @Override
                        public void onError(ApiException apiException) {
                            ToastUtils.INSTANCE.show(getApplicationContext(), apiException.getDisplayMessage());
                        }

                        @Override
                        public void onBusinessNext(BaseResult data) {
                            ToastUtils.INSTANCE.show(getApplicationContext(), data.getMessage(), ToastColor.SUCCESS);
                        }

                        @Override
                        public void onCompleted() {
                        }
                    });

            /*
             * 用户行为记录, 单独异常处理，不可影响用户体验
             */
            try {
                logParams = new JSONObject();
                logParams.put(URLs.kAction, "评论");
                logParams.put(URLs.kObjTitle, bannerName);
                ActionLogUtil.actionLog(mAppContext, logParams);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @JavascriptInterface
        public void jsException(final String ex) {
            /*
             * 用户行为记录, 单独异常处理，不可影响用户体验
             */
            try {
                logParams = new JSONObject();
                logParams.put(URLs.kAction, "JS异常");
                logParams.put("obj_id", objectID);
                logParams.put(URLs.kObjType, objectType);
                logParams.put(URLs.kObjTitle, String.format("评论页面/%s/%s", bannerName, ex));
                ActionLogUtil.actionLog(mAppContext, logParams);

                //点击两次还是有异常 异常报出
                if (loadCount < 2) {
                    showWebViewExceptionForWithoutNetwork();
                    loadCount++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
