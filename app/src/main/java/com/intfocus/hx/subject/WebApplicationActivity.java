package com.intfocus.hx.subject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.intfocus.hx.CommentActivity;
import com.intfocus.hx.R;
import com.intfocus.hx.base.BaseActivity;
import com.intfocus.hx.subject.selecttree.SelectItems;
import com.intfocus.hx.util.ActionLogUtil;
import com.intfocus.hx.util.ApiHelper;
import com.intfocus.hx.util.FileUtil;
import com.intfocus.hx.util.ImageUtil;
import com.intfocus.hx.util.K;
import com.intfocus.hx.util.LogUtil;
import com.intfocus.hx.util.ToastColor;
import com.intfocus.hx.util.ToastUtils;
import com.intfocus.hx.util.URLs;
import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnErrorOccurredListener;
import com.joanzapata.pdfview.listener.OnLoadCompleteListener;
import com.joanzapata.pdfview.listener.OnPageChangeListener;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import static android.webkit.WebView.enableSlowWholeDocumentDraw;
import static java.lang.String.format;

public class WebApplicationActivity extends BaseActivity implements OnPageChangeListener, OnLoadCompleteListener, OnErrorOccurredListener {
    @ViewInject(R.id.ll_shaixuan)
    LinearLayout llShaixuan;
    @ViewInject(R.id.ll_copylink)
    LinearLayout llCopyLinkl;

    private Boolean isInnerLink, isSupportSearch = false;
    private String templateID, reportID;
    private PDFView mPDFView;
    private File pdfFile;
    private String bannerName, link;
    private int objectID, objectType;
    private String groupID;
    private String userNum;
    private RelativeLayout bannerView;
    private Context mContext;
    private int loadCount = 0;
    private TextView mTitle;
    private boolean reportDataState;
    private ImageView iv_BannerBack;
    private TextView tv_BannerBack;
    private ImageView iv_BannerSetting;
    private Intent mSourceIntent;
    private Boolean isFromActivityResult = false;

    /* 请求识别码 */
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_CAMERA_RESULT = 0xa0;

    @Override
    @SuppressLint("SetJavaScriptEnabled")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
         * 判断当前设备版本，5.0 以 上 Android 系统使用才 enableSlowWholeDocumentDraw();
		 */
        if (Build.VERSION.SDK_INT > 20) {
            enableSlowWholeDocumentDraw();
        }
        setContentView(R.layout.activity_web_app);

        mContext = this;

        groupID = mUserSP.getString(URLs.kGroupId, "-2");
        userNum = mUserSP.getString(URLs.kUserNum, "no-set");

        iv_BannerBack = (ImageView) findViewById(R.id.iv_banner_back);
        tv_BannerBack = (TextView) findViewById(R.id.tv_banner_back);
        iv_BannerSetting = (ImageView) findViewById(R.id.iv_banner_setting);
        mWebView = (WebView) findViewById(R.id.browser);
        initActiongBar();
        initWebAppWebView();

        mWebView.setWebChromeClient(new WebApplicationActivity.MyWebChromeClient());
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                LogUtil.d("onPageStarted", String.format("%s - %s", URLs.timestamp(), url));
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                animLoading.setVisibility(View.GONE);
                isWeiXinShared = true;
                LogUtil.d("onPageFinished", String.format("%s - %s", URLs.timestamp(), url));

                // 报表缓存列表:是否把报表标题存储
                if (reportDataState && url.contains("report_" + reportID)) {
                    try {
                        SharedPreferences sp = getSharedPreferences("subjectCache", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        String cache = sp.getString("cache", "");
                        JSONObject json;
                        if (cache.equals("")) {
                            json = new JSONObject();
                            json.put("0", bannerName);
                        } else {
                            boolean isAdd = true;
                            json = new JSONObject(cache);
                            Iterator<String> it = json.keys();
                            while (it.hasNext()) {
                                String key = it.next();
                                if (json.getString(key).equals(bannerName)) {
                                    isAdd = false;
                                }
                            }
                            if (isAdd) {
                                json.put("" + json.length(), bannerName);
                            }
                        }
                        editor.putString("cache", json.toString());
                        editor.commit();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                LogUtil.d("onReceivedError",
                        String.format("errorCode: %d, description: %s, url: %s", errorCode, description,
                                failingUrl));
            }
        });

        mWebView.requestFocus();
        mWebView.setVisibility(View.VISIBLE);
        mWebView.addJavascriptInterface(new WebApplicationActivity.JavaScriptInterface(), URLs.kJSInterfaceName);
        animLoading.setVisibility(View.VISIBLE);
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                loadHtml();
            }
        });
        isWeiXinShared = false;
    }


    public android.webkit.WebView initWebAppWebView() {
        animLoading = (RelativeLayout) findViewById(R.id.anim_loading);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDefaultTextEncodingName("utf-8");

        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        mWebView.getSettings().setAppCachePath(appCachePath);
        mWebView.getSettings().setAllowFileAccess(true);

        mWebView.getSettings().setAppCacheEnabled(true);

        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setDrawingCacheEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                LogUtil.d("onPageStarted", String.format("%s - %s", URLs.timestamp(), url));
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                animLoading.setVisibility(View.GONE);
                isWeiXinShared = true;
                LogUtil.d("onPageFinished", String.format("%s - %s", URLs.timestamp(), url));
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                LogUtil.d("onReceivedError",
                        String.format("errorCode: %d, description: %s, url: %s", errorCode, description,
                                failingUrl));
            }
        });

        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return false;
            }
        });
        setWebViewLongListener(false);
        return mWebView;
    }

    public class MyWebChromeClient extends WebChromeClient {
        // Android 5.0 以上
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            if (mUploadMessage1 != null) {
                mUploadMessage1 = null;
            }
            Log.i("FileType1", fileChooserParams.toString());
            mUploadMessage1 = filePathCallback;
            showOptions();
            return true;
        }

        //Android 4.0 以下
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            mUploadMessage = uploadMsg;
            showOptions();
        }

        // Android 4.0 - 4.4.4
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            mUploadMessage = uploadMsg;
            showOptions();
        }
    }

    private void initActiongBar() {
        bannerView = (RelativeLayout) findViewById(R.id.rl_action_bar);
        mTitle = (TextView) findViewById(R.id.tv_banner_title);

		/*
         * Intent Data || JSON Data
         */
        Intent intent = getIntent();
        link = intent.getStringExtra(URLs.kLink);
        bannerName = intent.getStringExtra(URLs.kBannerName);
        objectID = intent.getIntExtra(URLs.kObjectId, -1);
        objectType = intent.getIntExtra(URLs.kObjectType, -1);

        isInnerLink = link.indexOf("template") > 0 && link.indexOf("group") > 0;
        mTitle.setText(bannerName);

        if (link.toLowerCase().endsWith(".pdf")) {
            mPDFView = (PDFView) findViewById(R.id.pdfview);
            mPDFView.setVisibility(View.INVISIBLE);
        }
        iv_BannerSetting.setVisibility(View.VISIBLE);
        if (intent.getBooleanExtra("hideBannerSetting", false))
            iv_BannerSetting.setVisibility(View.INVISIBLE);
    }

    /*
     * 标题栏点击设置按钮显示下拉菜单
     */
    public void launchDropMenuActivity(View v) {
        showComplaintsPopWindow(v);
    }

    /**
     * 显示菜单
     *
     * @param clickView
     */
    void showComplaintsPopWindow(View clickView) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_menu_v2, null);
        x.view().inject(this, contentView);
        if (!isInnerLink) {
            llCopyLinkl.setVisibility(View.VISIBLE);
        }
        if (isSupportSearch) {
            llShaixuan.setVisibility(View.GONE);
        }
        //设置弹出框的宽度和高度
        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);// 取得焦点
        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        popupWindow.setOutsideTouchable(true);
        //设置可以点击
        popupWindow.setTouchable(true);
        //进入退出的动画
//        popupWindow.setAnimationStyle(R.style.AnimationPopupwindow);
        contentView.findViewById(R.id.ll_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 分享
                actionShare2Weixin(view);
                popupWindow.dismiss();
            }
        });
        contentView.findViewById(R.id.ll_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 评论
                actionLaunchCommentActivity(view);
                popupWindow.dismiss();
            }
        });
//        contentView.findViewById(R.id.ll_shaixuan).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // 筛选
//                actionLaunchReportSelectorActivity(view);
////                WidgetUtil.showToastShort(mAppContext, "暂无筛选功能");
//                popupWindow.dismiss();
//            }
//        });
        contentView.findViewById(R.id.ll_copylink).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 拷贝外部链接
                actionCopyLink(view);
                popupWindow.dismiss();
            }
        });
        contentView.findViewById(R.id.ll_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animLoading.setVisibility(View.VISIBLE);
                popupWindow.dismiss();
                // 刷新
                refresh(view);
            }
        });
        popupWindow.showAsDropDown(clickView);
    }

    public void onResume() {
        mMyApp.setCurrentActivity(this);
        super.onResume();
    }

    protected void displayBannerTitleAndSearchIcon() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String selectedItem = FileUtil.reportSelectedItem(WebApplicationActivity.this, groupID, templateID, reportID);
                if (selectedItem == null || selectedItem.length() == 0) {
                    SelectItems items = FileUtil.reportSearchItems(WebApplicationActivity.this, groupID, templateID, reportID);
                    String firstName = "";
                    String secondName = "";
                    String thirdName = "";
                    if (items.getData().size() != 0) {
                        firstName = items.getData().get(0).getTitles();

                        if (items.getData().get(0).getInfos().size() != 0) {
                            secondName = "·" + items.getData().get(0).getInfos().get(0).getTitles();

                            if (items.getData().get(0).getInfos().get(0).getInfos().size() != 0) {
                                thirdName = "·" + items.getData().get(0).getInfos().get(0).getInfos().get(0).getTitles();
                            }
                        }
                    }

                    selectedItem = firstName + secondName + thirdName;
                } else {
                    selectedItem = selectedItem.replace("||", "·");
                }

                if (selectedItem.equals("")) {
                    selectedItem = bannerName;
                }
                TextView mTitle = (TextView) findViewById(R.id.bannerTitle);
                mTitle.setText(selectedItem);
            }
        });
    }

    /**
     * PDFView OnPageChangeListener CallBack
     *
     * @param page      the new page displayed, starting from 1
     * @param pageCount the total page count, starting from 1
     */
    public void onPageChanged(int page, int pageCount) {
        Log.i("onPageChanged", format("%s %d / %d", bannerName, page, pageCount));
    }

    public void loadComplete(int nbPages) {
        Log.d("loadComplete", "load pdf done");
    }

    public void errorOccured(String errorType, String errorMessage) {
        String htmlPath = String.format("%s/loading/%s.html", sharedPath, "500"),
                outputPath = String.format("%s/loading/%s.html", sharedPath, "500.output");

        if (!(new File(htmlPath)).exists()) {
            toast(String.format("链接打开失败: %s", link));
            return;
        }

        mWebView.setVisibility(View.VISIBLE);
        mPDFView.setVisibility(View.INVISIBLE);

        String htmlContent = FileUtil.readFile(htmlPath);
        htmlContent = htmlContent.replace("$exception_type$", errorType);
        htmlContent = htmlContent.replace("$exception_message$", errorMessage);
        htmlContent = htmlContent.replace("$visit_url$", link);
        try {
            FileUtil.writeFile(outputPath, htmlContent);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Message message = mHandlerWithAPI.obtainMessage();
        message.what = 200;
        message.obj = outputPath;

        mHandlerWithAPI.sendMessage(message);
    }

    private void loadHtml() {
        WebSettings webSettings = mWebView.getSettings();
        if (isInnerLink) {
            // format: /mobile/v1/group/:group_id/template/:template_id/report/:report_id
            // deprecated
            // format: /mobile/report/:report_id/group/:group_id
            templateID = TextUtils.split(link, "/")[6];
            reportID = TextUtils.split(link, "/")[8];
            String urlPath = format(link.replace("%@", "%d"), groupID);
            urlString = String.format("%s%s", K.kBaseUrl, urlPath);
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

            /**
             * 内部报表具有筛选功能时
             *   - 如果用户已选择，则 banner 显示该选项名称
             *   - 未设置时，默认显示筛选项列表中第一个
             *
             *  初次加载时，判断筛选功能的条件还未生效
             *  此处仅在第二次及以后才会生效
             */
            isSupportSearch = FileUtil.reportIsSupportSearch(mAppContext, groupID, templateID, reportID);
            if (isSupportSearch) {
                displayBannerTitleAndSearchIcon();
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    reportDataState = ApiHelper.reportData(mAppContext, groupID, templateID, reportID);
                    String jsFileName = "";

                    // 模板 4 的 groupID 为 0
                    if (Integer.valueOf(templateID) == 4) {
                        jsFileName = String.format("group_%s_template_%s_report_%s.js", "0", templateID, reportID);
                    } else {
                        jsFileName = String.format("group_%s_template_%s_report_%s.js", groupID, templateID, reportID);
                    }
                    String javascriptPath = String.format("%s/assets/javascripts/%s", sharedPath, jsFileName);
                    if (new File(javascriptPath).exists()) {
                        new Thread(mRunnableForDetecting).start();
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                builder.setTitle("温馨提示")
                                        .setMessage("报表数据下载失败,不再加载网页")
                                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                finish();
                                            }
                                        });
                                builder.show();
                            }
                        });
                    }
                }
            }).start();
        } else {
            urlString = link;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (urlString.toLowerCase().endsWith(".pdf")) {
                        new Thread(mRunnableForPDF).start();
                    } else {
                        /*
                         * 外部链接传参: user_num, timestamp
                         */
                        String appendParams = String.format("user_num=%s&timestamp=%s", userNum, URLs.timestamp());
                        String splitString = urlString.contains("?") ? "&" : "?";
                        urlString = String.format("%s%s%s", urlString, splitString, appendParams);
                        mWebView.loadUrl(urlString);
                        Log.i("OutLink", urlString);
                    }
                }
            });
        }
    }

    private final Handler mHandlerForPDF = new Handler() {
        public void handleMessage(Message message) {
            //Log.i("PDF", pdfFile.getAbsolutePath());
            if (pdfFile.exists()) {
                mPDFView.fromFile(pdfFile)
                        .defaultPage(1)
                        .showMinimap(true)
                        .enableSwipe(true)
                        .swipeVertical(true)
                        .onLoad(WebApplicationActivity.this)
                        .onPageChange(WebApplicationActivity.this)
                        .onErrorOccured(WebApplicationActivity.this)
                        .load();
                mWebView.setVisibility(View.INVISIBLE);
                mPDFView.setVisibility(View.VISIBLE);
            } else {
                toast("加载PDF失败");
            }
        }
    };

    private final Runnable mRunnableForPDF = new Runnable() {
        @Override
        public void run() {
            String outputPath = String.format("%s/%s/%s.pdf", FileUtil.basePath(mAppContext), K.kCachedDirName, URLs.MD5(urlString));
            pdfFile = new File(outputPath);
            ApiHelper.downloadFile(mAppContext, urlString, pdfFile);

            Message message = mHandlerForPDF.obtainMessage();
            mHandlerForPDF.sendMessage(message);
        }
    };

    /*
     * 内部报表具有筛选功能时，调用筛选项界面
     */
    public void actionLaunchReportSelectorActivity(View v) {
        if (isSupportSearch) {
            String selectedItemPath = String.format("%s.selected_item", FileUtil.reportJavaScriptDataPath(WebApplicationActivity.this, groupID, templateID, reportID));
            String searchItemsPath = String.format("%s.search_items", FileUtil.reportJavaScriptDataPath(WebApplicationActivity.this, groupID, templateID, reportID));
            Intent intent = new Intent(mContext, SelectorTreeActivity.class);
            intent.putExtra("searchItemsPath", searchItemsPath);
            intent.putExtra("selectedItemPath", selectedItemPath);
            mContext.startActivity(intent);
        } else {
            toast("该报表暂不支持筛选");
        }
    }

    /*
     * 拷贝链接
     */
    public void actionCopyLink(View v) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboardManager.setText(link);
        toast("链接已拷贝", ToastColor.SUCCESS);
    }

    /*
     * 分享截图至微信
     */
    public void actionShare2Weixin(View v) {
        if (link.toLowerCase().endsWith(".pdf")) {
            toast("暂不支持 PDF 分享");
            return;
        }

        if (!isWeiXinShared) {
            toast("网页加载完成,才能使用分享功能");
            return;
        }

        Bitmap bmpScrennShot = ImageUtil.takeScreenShot(WebApplicationActivity.this);
        if (bmpScrennShot == null) {
            ToastUtils.INSTANCE.show(this, "截图失败");
        }
        UMImage image = new UMImage(this, bmpScrennShot);
        new ShareAction(this)
                .withText("截图分享")
                .setPlatform(SHARE_MEDIA.WEIXIN)
                .setDisplayList(SHARE_MEDIA.WEIXIN)
                .withMedia(image)
                .setCallback(umShareListener)
                .open();
        /*
         * 用户行为记录, 单独异常处理，不可影响用户体验
         */
        try {
            logParams = new JSONObject();
            logParams.put("action", "分享");
            ActionLogUtil.actionLog(mContext, logParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Log.d("throw", "throw:" + " 分享取消了");
        }
    };

    /*
     * 评论
     */
    public void actionLaunchCommentActivity(View v) {
        Intent intent = new Intent(mContext, CommentActivity.class);
        intent.putExtra(URLs.kBannerName, bannerName);
        intent.putExtra(URLs.kObjectId, objectID);
        intent.putExtra(URLs.kObjectType, objectType);
        mContext.startActivity(intent);
    }

    /*
     * 返回
     */
    public void dismissActivity(View v) {
        WebApplicationActivity.this.onBackPressed();
    }

    @Override
    public void onBackPressed() {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("温馨提示")
                    .setMessage("退出当前页面?")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 不进行任何操作
                        }
                    });
            builder.show();
    }

    public void refresh(View v) {
        if (isOffline) {
            mTitle.setText(bannerName + "(离线)");
        }
        animLoading.setVisibility(View.VISIBLE);
        new WebApplicationActivity.refreshTask().execute();
    }

    private class refreshTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            /*
             *  浏览器刷新时，删除响应头文件，相当于无缓存刷新
             */
            if (isInnerLink) {
                String urlKey;
                if (urlString != null && !urlString.isEmpty()) {
                    urlKey = urlString.contains("?") ? TextUtils.split(urlString, "?")[0] : urlString;
                    ApiHelper.clearResponseHeader(urlKey, assetsPath);
                }
                urlKey = String.format(K.kReportDataAPIPath, K.kBaseUrl, groupID, templateID, reportID);
                ApiHelper.clearResponseHeader(urlKey, FileUtil.sharedPath(mAppContext));
                boolean reportDataState = ApiHelper.reportData(mAppContext, groupID, templateID, reportID);
                if (reportDataState) {
                    new Thread(mRunnableForDetecting).start();
                } else {
                    showWebViewExceptionForWithoutNetwork();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loadHtml();
        }
    }

    private class JavaScriptInterface extends JavaScriptBase {

        @JavascriptInterface
        public void showSource(String html) {
            String htmlFilePath = Environment.getExternalStorageDirectory() + "/" + "content.html";
            try {
                FileUtil.writeFile(htmlFilePath, html);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /*
         * JS 接口，暴露给JS的方法使用@JavascriptInterface装饰
         */
        @JavascriptInterface
        public void storeTabIndex(final String pageName, final int tabIndex) {
            try {
                String filePath = FileUtil.dirPath(mAppContext, K.kConfigDirName, K.kTabIndexConfigFileName);

                JSONObject config = new JSONObject();
                if ((new File(filePath).exists())) {
                    String fileContent = FileUtil.readFile(filePath);
                    config = new JSONObject(fileContent);
                }
                config.put(pageName, tabIndex);

                FileUtil.writeFile(filePath, config.toString());
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }

        @JavascriptInterface
        public int restoreTabIndex(final String pageName) {
            int tabIndex = 0;
            try {
                String filePath = FileUtil.dirPath(mAppContext, K.kConfigDirName, K.kTabIndexConfigFileName);

                JSONObject config = new JSONObject();
                if ((new File(filePath).exists())) {
                    String fileContent = FileUtil.readFile(filePath);
                    config = new JSONObject(fileContent);
                }
                tabIndex = config.getInt(pageName);
            } catch (JSONException e) {
                //e.printStackTrace();
            }

            return tabIndex < 0 ? 0 : tabIndex;
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
                logParams.put(URLs.kObjTitle, String.format("主题页面/%s/%s", bannerName, ex));
                ActionLogUtil.actionLog(mContext, logParams);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @JavascriptInterface
        public void reportSearchItems(final String arrayString) {
            try {
                String searchItemsPath = String.format("%s.search_items", FileUtil.reportJavaScriptDataPath(WebApplicationActivity.this, groupID, templateID, reportID));
                FileUtil.writeFile(searchItemsPath, arrayString);

                /**
                 *  判断筛选的条件: arrayString 数组不为空
                 *  报表第一次加载时，此处为判断筛选功能的关键点
                 */
                if (!arrayString.equals("{\"data\":[],\"max_deep\":0}")) {
                    isSupportSearch = true;
                    displayBannerTitleAndSearchIcon();
                } else {
                    isSupportSearch = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @JavascriptInterface
        public void setBannerTitle(final String bannerTitle) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!bannerTitle.equals("")) {
                        mTitle.setText(bannerTitle);
                    } else {
                        mTitle.setText("");
                    }
                }
            });
        }

        @JavascriptInterface
        public String reportSelectedItem() {
            String item = "";
            String selectedItemPath = String.format("%s.selected_item", FileUtil.reportJavaScriptDataPath(WebApplicationActivity.this, groupID, templateID, reportID));
            if (new File(selectedItemPath).exists()) {
                item = FileUtil.readFile(selectedItemPath);
            }
            return item;
        }

        @JavascriptInterface
        public void refreshBrowser() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    animLoading.setVisibility(View.VISIBLE);
                    loadHtml();
                }
            });
        }

        @JavascriptInterface
        public void toggleShowBanner(final String state) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    bannerView.setVisibility(state.equals("show") ? View.VISIBLE : View.GONE);
                }
            });
        }

        @JavascriptInterface
        public String getLocation() {
            SharedPreferences mUserSP = mAppContext.getSharedPreferences("UserBean", Context.MODE_PRIVATE);
            return mUserSP.getString("location", "0,0");
        }

        @JavascriptInterface
        public void toggleShowBannerBack(final String state) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    iv_BannerBack.setVisibility(state.equals("show") ? View.VISIBLE : View.GONE);
                    tv_BannerBack.setVisibility(state.equals("show") ? View.VISIBLE : View.GONE);
                }
            });
        }

            @JavascriptInterface
            public void toggleShowBannerMenu(final String state) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iv_BannerSetting.setVisibility(state.equals("show") ? View.VISIBLE : View.GONE);
                    }
                });
        }

        @JavascriptInterface
        public void saveParam(String isSave, int local) {
        }

        @JavascriptInterface
        public void goBack(String info) {
            mWebView.goBack();
        }

        @JavascriptInterface
        public void closeSubjectView() {
            finish();
        }

        @JavascriptInterface
        public void showAlert(final String title, final String content) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(WebApplicationActivity.this);
                    builder.setTitle(title)
                            .setMessage(content)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.show();
                }
            });
        }
    }

    public void showOptions() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setOnCancelListener(new WebApplicationActivity.DialogOnCancelListener());

        alertDialog.setTitle("请选择操作");
        // gallery, camera.
        String[] options = {"相册", "拍照"};

        alertDialog.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            try {
                                mSourceIntent = ImageUtil.choosePicture();
                                startActivityForResult(mSourceIntent, CODE_RESULT_REQUEST);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(WebApplicationActivity.this,
                                        "请去\"设置\"中开启本应用的图片媒体访问权限",
                                        Toast.LENGTH_SHORT).show();
                                restoreUploadMsg();
                            }

                        } else {
                            try {
                                getCameraCapture();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(WebApplicationActivity.this,
                                        "相机调用失败, 请尝试从相册上传图片。",
                                        Toast.LENGTH_SHORT).show();
                                restoreUploadMsg();
                            }
                        }
                    }
                }
        );

        alertDialog.show();
    }

    private class DialogOnCancelListener implements DialogInterface.OnCancelListener {
        @Override
        public void onCancel(DialogInterface dialogInterface) {
            restoreUploadMsg();
        }
    }

    private void restoreUploadMsg() {
        if (mUploadMessage != null) {
            mUploadMessage.onReceiveValue(null);
            mUploadMessage = null;

        } else if (mUploadMessage1 != null) {
            mUploadMessage1.onReceiveValue(null);
            mUploadMessage1 = null;
        }
    }

    /*
     * 启动拍照并获取图片
     */
    private void getCameraCapture() {
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        /*
         * 需要调用裁剪图片功能，无法读取内部存储，故使用 SD 卡先存储图片
         */
        if (FileUtil.hasSdcard()) {
            Uri imageUri;
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                imageUri = FileProvider.getUriForFile(this, "com.intfocus.shengyiplus.fileprovider", new File(Environment.getExternalStorageDirectory(), "upload.jpg"));
                intentFromCapture.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intentFromCapture.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            } else {
                imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "upload.jpg"));
            }
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        }

        startActivityForResult(intentFromCapture, CODE_CAMERA_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        isFromActivityResult = true;
        super.onActivityResult(requestCode, resultCode, intent);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, intent);
        Log.e("uploadImg", resultCode + "");
        if (resultCode != Activity.RESULT_OK) {
            if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(null);
            }

            if (mUploadMessage1 != null) {         // for android 5.0+
                mUploadMessage1.onReceiveValue(null);
            }
            return;
        }
        switch (requestCode) {
            case CODE_CAMERA_RESULT:
                try {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        if (mUploadMessage == null) {
                            return;
                        }

                        File cameraFile = new File(Environment.getExternalStorageDirectory(), "upload.jpg");

                        Uri uri = Uri.fromFile(cameraFile);

                        mUploadMessage.onReceiveValue(uri);

                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if (mUploadMessage1 == null) {        // for android 5.0+
                            return;
                        }

                        File cameraFile = new File(Environment.getExternalStorageDirectory(), "upload.jpg");

                        Uri uri = Uri.fromFile(cameraFile);
                        mUploadMessage1.onReceiveValue(new Uri[]{uri});
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case CODE_RESULT_REQUEST: {
                try {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        if (mUploadMessage == null) {
                            return;
                        }

                        String sourcePath = ImageUtil.retrievePath(this, mSourceIntent, intent);

                        if (TextUtils.isEmpty(sourcePath) || !new File(sourcePath).exists()) {
                            Log.e("uploadImg", "sourcePath empty or not exists.");
                            break;
                        }

                        Uri uri = Uri.fromFile(new File(sourcePath));
                        mUploadMessage.onReceiveValue(uri);

                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if (mUploadMessage1 == null) {        // for android 5.0+
                            return;
                        }

                        String sourcePath = ImageUtil.retrievePath(this, mSourceIntent, intent);

                        if (TextUtils.isEmpty(sourcePath) || !new File(sourcePath).exists()) {
                            Log.e("uploadImg", "sourcePath empty or not exists.");
                            break;
                        }

                        Uri uri = Uri.fromFile(new File(sourcePath));
                        mUploadMessage1.onReceiveValue(new Uri[]{uri});
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }
}
