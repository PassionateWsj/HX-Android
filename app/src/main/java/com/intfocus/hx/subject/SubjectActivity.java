package com.intfocus.hx.subject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.intfocus.hx.CommentActivity;
import com.intfocus.hx.R;
import com.intfocus.hx.base.BaseActivity;
import com.intfocus.hx.dashboard.mine.adapter.FilterMenuAdapter;
import com.intfocus.hx.data.response.filter.Menu;
import com.intfocus.hx.data.response.filter.MenuItem;
import com.intfocus.hx.data.response.filter.MenuResult;
import com.intfocus.hx.filter.MyFilterDialogFragment;
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
import com.intfocus.hx.view.addressselector.FilterPopupWindow;
import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnErrorOccurredListener;
import com.joanzapata.pdfview.listener.OnLoadCompleteListener;
import com.joanzapata.pdfview.listener.OnPageChangeListener;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.webkit.WebView.enableSlowWholeDocumentDraw;
import static java.lang.String.format;

public class SubjectActivity extends BaseActivity implements OnPageChangeListener, OnLoadCompleteListener, OnErrorOccurredListener
        , FilterMenuAdapter.FilterMenuListener, FilterPopupWindow.MenuLisenter, MyFilterDialogFragment.FilterLisenter {
    @ViewInject(R.id.ll_shaixuan)
    LinearLayout llShaixuan;
    @ViewInject(R.id.ll_copylink)
    LinearLayout llCopyLinkl;

    private Boolean isInnerLink = false;
    private String templateID, reportID;
    private PDFView mPDFView;
    private File pdfFile;
    private String bannerName, link;
    private String groupID;
    private int objectType, objectID;
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

    /**
     * 筛选
     *
     * @param savedInstanceState
     */
    private LinearLayout llFilter;
    private RelativeLayout rlAddressFilter;
    private TextView tvLocationAddress;
    private TextView tvAddressFilter;
    private RecyclerView filterRecyclerView;
    private View viewLine;
    /**
     * 地址选择
     */
    private List<MenuItem> locationDatas;
    /**
     * 菜单
     */
    private int currentPosition = 0;//当前展开的menu
    private List<MenuItem> menuDatas;
    private FilterMenuAdapter menuAdpter;
    private FilterPopupWindow filterPopupWindow;

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
        setContentView(R.layout.activity_subject);

        mContext = this;
        groupID = mUserSP.getString(URLs.kGroupId, "-2");
        userNum = mUserSP.getString(URLs.kUserNum, "not-set");

        iv_BannerBack = (ImageView) findViewById(R.id.iv_banner_back);
        tv_BannerBack = (TextView) findViewById(R.id.tv_banner_back);
        iv_BannerSetting = (ImageView) findViewById(R.id.iv_banner_setting);
        mWebView = (WebView) findViewById(R.id.browser);
        llFilter = (LinearLayout) findViewById(R.id.ll_filter);
        rlAddressFilter = (RelativeLayout) findViewById(R.id.rl_address_filter);
        tvLocationAddress = (TextView) findViewById(R.id.tv_location_address);
        tvAddressFilter = (TextView) findViewById(R.id.tv_address_filter);
        filterRecyclerView = (RecyclerView) findViewById(R.id.filter_recycler_view);
        viewLine = findViewById(R.id.view_line);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        filterRecyclerView.setLayoutManager(mLayoutManager);
        menuAdpter = new FilterMenuAdapter(mContext, menuDatas, this);
        filterRecyclerView.setAdapter(menuAdpter);
        tvAddressFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogFragment();
            }
        });

        initActiongBar();
        initSubWebView();

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
                //是否有筛选数据，有就显示出来
                if (locationDatas != null && locationDatas.size() > 0) {
                    rlAddressFilter.setVisibility(View.VISIBLE);
                    LogUtil.d("location", locationDatas.size() + "");
                } else {
                    rlAddressFilter.setVisibility(View.GONE);
                }
                if (menuDatas != null && menuDatas.size() > 0) {
                    LogUtil.d("faster_select", menuDatas.size() + "");
                    filterRecyclerView.setVisibility(View.VISIBLE);
                    viewLine.setVisibility(View.VISIBLE);
                    menuAdpter.setData(menuDatas);
                } else {
                    filterRecyclerView.setVisibility(View.GONE);
                    viewLine.setVisibility(View.GONE);
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
        mWebView.addJavascriptInterface(new JavaScriptInterface(), URLs.kJSInterfaceName);
        animLoading.setVisibility(View.VISIBLE);

        mWebView.post(new Runnable() {
            @Override
            public void run() {
                loadHtml();
            }
        });

        isWeiXinShared = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isOffline) {
                            mTitle.setText(bannerName + "(离线)");
                        }
                    }
                });
            }
        }).start();

        mMyApp.setCurrentActivity(this);
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
        super.onResume();
    }

    protected void displayBannerTitleAndSearchIcon() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String selectedItem = FileUtil.reportSelectedItem(SubjectActivity.this, groupID, templateID, reportID);
                if (selectedItem == null || selectedItem.length() == 0) {
                    SelectItems items = FileUtil.reportSearchItems(SubjectActivity.this, groupID, templateID, reportID);
                    String firstName = "";
                    String secondName = "";
                    String thirdName = "";
                    if (items != null && items.getData().size() != 0) {
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // 横屏时隐藏标题栏、导航栏
        checkInterfaceOrientation(newConfig);
    }

    /*
     * 横屏 or 竖屏
     */
    private void checkInterfaceOrientation(Configuration config) {
        Boolean isLandscape = (config.orientation == Configuration.ORIENTATION_LANDSCAPE);

        bannerView.setVisibility(isLandscape ? View.GONE : View.VISIBLE);
        if (isLandscape) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(lp);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            WindowManager.LayoutParams attr = getWindow().getAttributes();
            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attr);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    private void loadHtml() {
        WebSettings webSettings = mWebView.getSettings();
        if (isInnerLink) {
            // format: /mobile/v1/group/:group_id/template/:template_id/report/:report_id
            // deprecated
            // format: /mobile/report/:report_id/group/:group_id
            templateID = TextUtils.split(link, "/")[6];
            reportID = TextUtils.split(link, "/")[8];
            String urlPath = format(link.replace("%@", "%s"), groupID);
            urlString = String.format("%s%s", K.kBaseUrl, urlPath);
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);


            new Thread(new Runnable() {
                @Override
                public void run() {
                    reportDataState = ApiHelper.reportData(mAppContext, String.format("%s", groupID), templateID, reportID);
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
                                                SubjectActivity.this.finish();
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
//            Log.i("PDF", pdfFile.getAbsolutePath());
            if (pdfFile.exists()) {
                mPDFView.fromFile(pdfFile)
                        .defaultPage(1)
                        .showMinimap(true)
                        .enableSwipe(true)
                        .swipeVertical(true)
                        .onLoad(SubjectActivity.this)
                        .onPageChange(SubjectActivity.this)
                        .onErrorOccured(SubjectActivity.this)
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
     * 拷贝链接
     */
    public void actionCopyLink(View v) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboardManager.setText(link);
        ToastUtils.INSTANCE.show(mContext, "链接已拷贝", ToastColor.SUCCESS);
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

        Bitmap bmpScrennShot = ImageUtil.takeScreenShot(SubjectActivity.this);
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
        SubjectActivity.this.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (isInnerLink) {
            finish();
        } else {
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
    }

    public void refresh(View v) {
        if (isOffline) {
            mTitle.setText(bannerName + "(离线)");
        }
        animLoading.setVisibility(View.VISIBLE);
        new refreshTask().execute();
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
            Log.i("SubjectSearch", "1");
//            Log.i("SubjectSearch", arrayString + "1");
            String str = arrayString;
        }

        @JavascriptInterface
        public void reportSearchItemsV2(final String arrayString) {
            Log.i("SubjectSearch", "2");
//            Log.i("SubjectSearch", arrayString + "2");
            if (!TextUtils.isEmpty(arrayString)) {
                MenuResult msg = new Gson().fromJson(arrayString, MenuResult.class);
                if (msg != null && msg.getData() != null && msg.getData().size() > 0) {
                    for (Menu menu : msg.getData()) {
                        if ("location".equals(menu.getType())) {
                            locationDatas = menu.getData();
                            String selectedItemPath = String.format("%s.selected_item", FileUtil.reportJavaScriptDataPath(SubjectActivity.this, groupID, templateID, reportID));
                            if (!new File(selectedItemPath).exists()) {
                                if (locationDatas != null) {
                                    tvLocationAddress.setText(menu.getCurrent_location().getDisplay());
                                }
                            }
                        }
                        if ("faster_select".equals(menu.getType())) {
                            menuDatas = menu.getData();
                        }
                    }
                }
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
            String selectedItemPath = String.format("%s.selected_item", FileUtil.reportJavaScriptDataPath(SubjectActivity.this, groupID, templateID, reportID));
            if (new File(selectedItemPath).exists()) {
                item = FileUtil.readFile(selectedItemPath);
                final String filterText = item;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvLocationAddress.setText(filterText);
                    }
                });
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
            String location = ApiHelper.getLocation(mAppContext);
            return location;
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(SubjectActivity.this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, intent);
    }

    private void showDialogFragment() {
        FragmentTransaction mFragTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("dialogFragment");
        if (fragment != null) {
            //为了不重复显示dialog，在显示对话框之前移除正在显示的对话框
            mFragTransaction.remove(fragment);
        }
        MyFilterDialogFragment dialogFragment = new MyFilterDialogFragment((ArrayList<MenuItem>) locationDatas, this);
        dialogFragment.show(mFragTransaction, "dialogFragment"); //显示一个Fragment并且给该Fragment添加一个Tag，可通过findFragmentByTag找到该Fragment
    }

    /**
     * 点击普通筛选栏
     */
    @Override
    public void itemClick(int position) {
        //标记点击位置
        menuDatas.get(position).setArrorDirection(true);
        menuAdpter.setData(menuDatas);
        currentPosition = position;
        showMenuPop(position);
    }

    private void showMenuPop(int position) {
        if (filterPopupWindow == null) {
            initMenuPopup(position);
        } else {
            filterPopupWindow.upDateDatas(menuDatas.get(position).getData());
        }
//        viewBg.visibility = View.VISIBLE
        filterPopupWindow.showAsDropDown(viewLine);
        filterPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                for (MenuItem menuItem : menuDatas) {
                    menuItem.setArrorDirection(false);
                }
                menuAdpter.setData(menuDatas);
            }
        });
    }

    private void initMenuPopup(int position) {
        filterPopupWindow = new FilterPopupWindow(this, menuDatas.get(position).getData(), this);
        filterPopupWindow.init();
    }

    /**
     * 普通排序列表点击
     *
     * @param position
     */
    @Override
    public void menuItemClick(int position) {
        for (MenuItem menuItem : menuDatas.get(currentPosition).getData()) {
            menuItem.setArrorDirection(false);
        }

        //标记点击位置
        menuDatas.get(currentPosition).getData().get(position).setArrorDirection(true);
        filterPopupWindow.dismiss();
    }

    @Override
    public void complete(@NotNull ArrayList<MenuItem> data) {
        try {
            String addStr = "";
            for (int i = 0; i < data.size(); i++) {
                addStr += data.get(i).getName() + "||";
            }

            addStr = addStr.substring(0, addStr.length() - 2);
            String selectedItemPath = String.format("%s.selected_item", FileUtil.reportJavaScriptDataPath(SubjectActivity.this, groupID, templateID, reportID));
            FileUtil.writeFile(selectedItemPath, addStr);

            animLoading.setVisibility(View.VISIBLE);
            mWebView.post(new Runnable() {
                @Override
                public void run() {
                    loadHtml();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
