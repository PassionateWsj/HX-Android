package com.intfocus.hx.dashboard

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.View
import com.google.gson.Gson
import com.intfocus.hx.R
import com.intfocus.hx.YHApplication
import com.intfocus.hx.bean.DashboardItemBean
import com.intfocus.hx.dashboard.kpi.bean.NoticeBoardRequest
import com.intfocus.hx.dashboard.mine.bean.PushMessageBean
import com.intfocus.hx.data.response.scanner.StoreItem
import com.intfocus.hx.data.response.scanner.StoreListResult
import com.intfocus.hx.db.OrmDBHelper
import com.intfocus.hx.net.ApiException
import com.intfocus.hx.net.CodeHandledSubscriber
import com.intfocus.hx.net.RetrofitUtil
import com.intfocus.hx.scanner.BarCodeScannerActivity
import com.intfocus.hx.subject.WebApplicationActivity
import com.intfocus.hx.util.ActionLogUtil
import com.intfocus.hx.util.RxBusUtil
import com.intfocus.hx.util.ToastUtils
import com.intfocus.hx.util.URLs
import com.intfocus.hx.view.NoScrollViewPager
import com.intfocus.hx.view.TabView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONObject
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import sumimakito.android.advtextswitcher.AdvTextSwitcher
import java.sql.SQLException

class DashboardActivity : FragmentActivity(), ViewPager.OnPageChangeListener, AdvTextSwitcher.Callback {
    private var mDashboardFragmentAdapter: DashboardFragmentAdapter? = null
    private var mSharedPreferences: SharedPreferences? = null
    private var mTabView: Array<TabView>? = null
    private var userID: Int = 0
    private var mApp: YHApplication? = null
    private var mViewPager: NoScrollViewPager? = null
    private var mTabKPI: TabView? = null
    private var mTabAnalysis: TabView? = null
    private var mTabAPP: TabView? = null
    private var mTabMessage: TabView? = null
    private var mContext: Context? = null
    private var mAppContext: Context? = null
    private var mGson: Gson? = null
    lateinit var mUserSP: SharedPreferences
    private var storeList: List<StoreItem>? = null

    private var objectTypeName = arrayOf("生意概况", "报表", "工具箱")

    companion object {
        val PAGE_KPI = 0
        val PAGE_REPORTS = 1
        val PAGE_APP = 2
        val PAGE_MINE = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        EventBus.getDefault().register(this)
        mApp = this.application as YHApplication
        mAppContext = mApp!!.appContext
        mContext = this
        mGson = Gson()
        mSharedPreferences = getSharedPreferences("DashboardPreferences", Context.MODE_PRIVATE)
        mUserSP = getSharedPreferences("UserBean", Context.MODE_PRIVATE)
        mDashboardFragmentAdapter = DashboardFragmentAdapter(supportFragmentManager)
        mViewPager = findViewById(R.id.content_view) as NoScrollViewPager
        initTabView()
        initViewPaper(mDashboardFragmentAdapter!!)
        getStoreList()

        var intent = intent
        if (intent.hasExtra("msgData")) {
            handlePushMessage(intent.getBundleExtra("msgData").getString("message"))
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    /*
     * 推送消息处理
     */
    fun handlePushMessage(message: String) {
        var pushMessage = mGson!!.fromJson(message, PushMessageBean::class.java)
        pushMessage.body_title = intent.getBundleExtra("msgData").getString("message_body_title")
        pushMessage.body_text = intent.getBundleExtra("msgData").getString("message_body_text")
        pushMessage.new_msg = true
        pushMessage.user_id = userID
        var personDao = OrmDBHelper.getInstance(this).pushMessageDao
        //  RxJava异步存储推送过来的数据
        Observable.create(Observable.OnSubscribe<PushMessageBean> {
            try {
                personDao.createIfNotExists(pushMessage)
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { }

        // RxBus通知消息界面 ShowPushMessageActivity 更新数据
        RxBusUtil.getInstance().post("UpDatePushMessage")
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("温馨提示")
                .setMessage(String.format("确认退出【%s】？", resources.getString(R.string.app_name)))
                .setPositiveButton("确认") { _, _ ->
                    mApp!!.setCurrentActivity(null)
                    finish()
                    System.exit(0)
                }
                .setNegativeButton("取消") { _, _ ->
                    // 返回DashboardActivity
                }
        builder.show()
    }

    fun startBarCodeActivity(v: View) {
        if (ContextCompat.checkSelfPermission(this@DashboardActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            val builder = AlertDialog.Builder(this@DashboardActivity)
            builder.setTitle("温馨提示")
                    .setMessage("相机权限获取失败，是否到本应用的设置界面设置权限")
                    .setPositiveButton("确认") { _, _ -> goToAppSetting() }
                    .setNegativeButton("取消") { _, _ ->
                        // 返回DashboardActivity
                    }
            builder.show()
            return
        } else if (storeList == null) {
            val builder = AlertDialog.Builder(this@DashboardActivity)
            builder.setTitle("温馨提示")
                    .setMessage("抱歉, 您没有扫码权限")
                    .setPositiveButton("确认") { _, _ -> }
            builder.show()
            return
        } else {
            val barCodeScannerIntent = Intent(mContext, BarCodeScannerActivity::class.java)
            mContext!!.startActivity(barCodeScannerIntent)

            var logParams = JSONObject()
            logParams.put(URLs.kAction, "点击/扫一扫")
            ActionLogUtil.actionLog(mAppContext, logParams)
        }
    }

    /*
     * 跳转系统设置页面
     */
    private fun goToAppSetting() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    private fun initTabView() {
        mTabKPI = findViewById(R.id.tab_kpi) as TabView
        mTabAnalysis = findViewById(R.id.tab_analysis) as TabView
        mTabAPP = findViewById(R.id.tab_app) as TabView
        mTabMessage = findViewById(R.id.tab_message) as TabView
        mTabView = arrayOf<TabView>(mTabKPI!!, mTabAnalysis!!, mTabAPP!!, mTabMessage!!)

        mTabKPI!!.setOnClickListener(mTabChangeListener)
        mTabAnalysis!!.setOnClickListener(mTabChangeListener)
        mTabAPP!!.setOnClickListener(mTabChangeListener)
        mTabMessage!!.setOnClickListener(mTabChangeListener)
    }

    /**
     * @param dashboardFragmentAdapter
     */
    private fun initViewPaper(dashboardFragmentAdapter: DashboardFragmentAdapter) {
        mViewPager!!.adapter = dashboardFragmentAdapter
        mViewPager!!.offscreenPageLimit = 4
        mViewPager!!.currentItem = mSharedPreferences!!.getInt("LastTab", 0)
        mTabView!![mViewPager!!.currentItem].setActive(true)
        mViewPager!!.addOnPageChangeListener(this)
    }

    /*
     * Tab 栏按钮监听事件
     */
    private val mTabChangeListener = View.OnClickListener { v ->
        when (v.id) {
            R.id.tab_kpi -> mViewPager!!.currentItem = PAGE_KPI
            R.id.tab_analysis -> mViewPager!!.currentItem = PAGE_REPORTS
            R.id.tab_app -> mViewPager!!.currentItem = PAGE_APP
            R.id.tab_message -> mViewPager!!.currentItem = PAGE_MINE
            else -> {
            }
        }
        refreshTabView()
    }

    //重写ViewPager页面切换的处理方法
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {}

    override fun onPageScrollStateChanged(state: Int) {
        if (state == 2) {
            when (mViewPager!!.currentItem) {
                PAGE_KPI -> mTabKPI!!.setActive(true)

                PAGE_REPORTS -> mTabAnalysis!!.setActive(true)

                PAGE_APP -> mTabAPP!!.setActive(true)

                PAGE_MINE -> mTabMessage!!.setActive(true)
            }
        }
        refreshTabView()
        mSharedPreferences!!.edit().putInt("LastTab", mViewPager!!.currentItem).commit()
    }

    /*
     * 公告点击事件
     */
    override fun onItemClick(position: Int) {
        mViewPager!!.currentItem = PAGE_MINE
        refreshTabView()
    }

    /*
     * 刷新 TabView 高亮状态
     */
    private fun refreshTabView() {
        mTabView!![mViewPager!!.currentItem].setActive(true)
        for (i in mTabView!!.indices) {
            if (i != mViewPager!!.currentItem) {
                mTabView!![i].setActive(false)
            }
        }
    }

    fun getStoreList() {
        RetrofitUtil.getHttpService(this).getStoreList(mUserSP.getString("user_num", "0"))
                .compose(RetrofitUtil.CommonOptions<StoreListResult>())
                .subscribe(object : CodeHandledSubscriber<StoreListResult>() {
                    override fun onCompleted() {
                    }

                    override fun onError(apiException: ApiException?) {
                    }

                    override fun onBusinessNext(data: StoreListResult) {
                        if (data.data != null && data.data!!.size > 0) {
                            storeList = data.data
                        }
                    }
                })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNoticeItemEvent(click: NoticeBoardRequest) {
        mViewPager!!.currentItem = PAGE_MINE
        refreshTabView()
    }

    /*
     * 图表点击事件统一处理方法
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(items: DashboardItemBean?) {
        if (items != null) {
            val link = items.link
            val bannerName = items.bannerName + ""
            pageLink(bannerName, link)
        } else {
            ToastUtils.show(this, "没有指定链接")
        }
    }

    /*
     * 页面跳转事件
     */
    fun pageLink(mBannerName: String, link: String) {
        when (mBannerName) {
            "券核销" -> {
                if (ContextCompat.checkSelfPermission(this@DashboardActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    val builder = AlertDialog.Builder(this@DashboardActivity)
                    builder.setTitle("温馨提示")
                            .setMessage("相机权限获取失败，是否到本应用的设置界面设置权限")
                            .setPositiveButton("确认") { _, _ -> goToAppSetting() }
                            .setNegativeButton("取消") { _, _ ->
                                // 返回DashboardActivity
                            }
                    builder.show()
                    return
                } else {
                    val barCodeScannerIntent = Intent(mContext, BarCodeScannerActivity::class.java)
                    mContext!!.startActivity(barCodeScannerIntent)
                }
            }

            else -> {
                val intent = Intent(this, WebApplicationActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                intent.putExtra(URLs.kBannerName, mBannerName)
                intent.putExtra(URLs.kLink, link)
                startActivity(intent)
            }
        }
    }
}
