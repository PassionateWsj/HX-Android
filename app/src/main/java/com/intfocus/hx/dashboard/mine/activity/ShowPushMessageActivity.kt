package com.intfocus.hx.dashboard.mine.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import com.google.gson.Gson
import com.intfocus.hx.R
import com.intfocus.hx.bean.User
import com.intfocus.hx.dashboard.mine.adapter.ShowPushMessageAdapter
import com.intfocus.hx.dashboard.mine.bean.PushMessageBean
import com.intfocus.hx.dashboard.mine.presenter.PushMessagePresenter
import com.intfocus.hx.dashboard.mine.view.PushMessageView
import com.intfocus.hx.subject.SubjectActivity
import com.intfocus.hx.subject.TableActivity
import com.intfocus.hx.subject.WebApplicationActivity
import com.intfocus.hx.subject.metrics.HomeTricsActivity
import com.intfocus.hx.subject.template_v2.ModularTwo_Mode_Activity
import com.intfocus.hx.util.*
import kotlinx.android.synthetic.main.activity_show_push_message.*
import org.json.JSONException
import org.json.JSONObject
import rx.Subscription
import java.io.File

/**
 * ****************************************************
 * author: JamesWong
 * created on: 17/08/01 下午4:56
 * e-mail: PassionateWsj@outlook.com
 * name: 推送消息显示 Activity
 * desc: 根据 用户Id 显示推送消息的 activity
 * ****************************************************
 */

class ShowPushMessageActivity : AppCompatActivity(), PushMessageView, ShowPushMessageAdapter.OnPushMessageListener {
    /**
     * 当前用户id
     */
    var mUserID = 0
    /**
     * 显示消息的 adapter
     */
    val adapter = ShowPushMessageAdapter(this, this)
    /**
     * RxBus 接收推送消息的类，需要手动取消注册释放资源
     */
    lateinit var subscribe: Subscription
    /**
     * presenter 类
     */
    lateinit var presenter: PushMessagePresenter

    private var objectTypeName = arrayOf("生意概况", "报表", "工具箱")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_push_message)

        // 初始化数据
        initData()
        // 初始化适配器
        initAdapter()
    }

    private fun initData() {
        // 获取 User 信息
        val userConfigPath = String.format("%s/%s", FileUtil.basePath(this), K.kUserConfigFileName)
        if (File(userConfigPath).exists()) {
            val user = Gson()!!.fromJson(FileUtil.readConfigFile(userConfigPath).toString(), User::class.java)
            if (user!!.isIs_login) {
                mUserID = user!!.user_id
            }
        }
        // 请求数据
        presenter = PushMessagePresenter(applicationContext, this, mUserID)
        presenter.loadData()

        // RxBus接收到推送信息，处理数据列表更新
        subscribe = RxBusUtil.getInstance().toObservable(PushMessageBean::class.java)
                .subscribe { msg ->
                    if ("UpDatePushMessage".equals(msg)) presenter.loadData()
                }
    }

    private fun initAdapter() {
        val mLayoutManager = LinearLayoutManager(this)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_show_push_message.layoutManager = mLayoutManager
        rv_show_push_message.adapter = adapter
    }

    /**
     * 请求数据失败的回调方法
     */
    override fun onResultFailure() {
        ToastUtils.show(this, "没有找到数据")
    }

    /**
     * 请求数据成功的回调方法
     */
    override fun onResultSuccess(data: MutableList<PushMessageBean>?) {
        adapter.setData(data!!)
    }

    /**
     * 消息 item 的点击回调处理方法
     */
    override fun onItemClick(position: Int) {
        // 更新点击状态
        var pushMessageBean = adapter.mData[position]

        // 重新获取数据
        presenter.loadData()

        // 通知 adapter 刷新数据
        adapter.notifyDataSetChanged()

        // 点击 item 判断类型 进行页面跳转
        var intent = Intent(this, PushMessageContentActivity::class.java)
        intent.putExtra("push_message_bean", pushMessageBean)
        if ("report".equals(pushMessageBean.type)) {
            // 跳转到报表页面
            pageLink(pushMessageBean.title + "", pushMessageBean.url + "", 1, 1)
        } else {
            startActivity(intent)
        }
    }

    /*
     * 页面跳转事件
     */
    fun pageLink(mBannerName: String, link: String, objectId: Int, objectType: Int) {
        if (link.indexOf("template") > 0 && link.indexOf("group") > 0) {
            try {
                val groupID = getSharedPreferences("UserBean", Context.MODE_PRIVATE).getInt(URLs.kGroupId, 0)
                val reportID = TextUtils.split(link, "/")[8]
                var urlString: String
                val intent: Intent

                when {
                    link.indexOf("template/2") > 0 -> {
                        intent = Intent(this, SubjectActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                        intent.putExtra(URLs.kBannerName, mBannerName)
                        intent.putExtra(URLs.kLink, link)
                        intent.putExtra(URLs.kObjectId, objectId)
                        intent.putExtra(URLs.kObjectType, objectType)
                        intent.putExtra("groupID", groupID)
                        intent.putExtra("reportID", reportID)
                        startActivity(intent)
                    }
                    link.indexOf("template/4") > 0 -> {
                        intent = Intent(this, SubjectActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                        intent.putExtra(URLs.kBannerName, mBannerName)
                        intent.putExtra(URLs.kLink, link)
                        intent.putExtra(URLs.kObjectId, objectId)
                        intent.putExtra(URLs.kObjectType, objectType)
                        intent.putExtra("groupID", groupID)
                        intent.putExtra("reportID", reportID)
                        startActivity(intent)
                    }
                    link.indexOf("template/3") > 0 -> {
                        intent = Intent(this, HomeTricsActivity::class.java)
                        urlString = String.format("%s/api/v1/group/%s/template/%s/report/%s/json",
                                K.kBaseUrl, groupID, "3", reportID)
                        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                        intent.putExtra(URLs.kBannerName, mBannerName)
                        intent.putExtra(URLs.kObjectId, objectId)
                        intent.putExtra(URLs.kObjectType, objectType)
                        intent.putExtra("groupID", groupID)
                        intent.putExtra("reportID", reportID)
                        intent.putExtra("urlString", urlString)
                        startActivity(intent)
                    }
                    link.indexOf("template/5") > 0 -> {
                        intent = Intent(this, TableActivity::class.java)
                        urlString = String.format("%s/api/v1/group/%s/template/%s/report/%s/json",
                                K.kBaseUrl, groupID, "5", reportID)
                        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                        intent.putExtra(URLs.kBannerName, mBannerName)
                        intent.putExtra(URLs.kObjectId, objectId)
                        intent.putExtra(URLs.kObjectType, objectType)
                        intent.putExtra("groupID", groupID)
                        intent.putExtra("reportID", reportID)
                        intent.putExtra("urlString", urlString)
                        startActivity(intent)
                    }
                    link.indexOf("template/1") > 0 -> {
                        val intent = Intent(this, ModularTwo_Mode_Activity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                        intent.putExtra(URLs.kBannerName, mBannerName)
                        intent.putExtra(URLs.kObjectId, objectId)
                        intent.putExtra(URLs.kObjectType, objectType)
                        intent.putExtra("groupID", groupID)
                        intent.putExtra("reportID", reportID)
                        intent.putExtra(URLs.kLink, link)
                        startActivity(intent)
                    }
                    else -> showTemplateErrorDialog()
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            var logParams = JSONObject()
            logParams.put(URLs.kAction, "点击/" + objectTypeName[objectType - 1] + "/报表")
            logParams.put(URLs.kObjTitle, mBannerName)
            ActionLogUtil.actionLog(this, logParams)
        } else {
            val intent = Intent(this, WebApplicationActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            intent.putExtra(URLs.kBannerName, mBannerName)
            intent.putExtra(URLs.kLink, link)
            intent.putExtra(URLs.kObjectId, objectId)
            intent.putExtra(URLs.kObjectType, objectType)
            startActivity(intent)

            var logParams = JSONObject()
            logParams.put(URLs.kAction, "点击/生意概况/链接")
            logParams.put(URLs.kObjTitle, mBannerName)
            ActionLogUtil.actionLog(this, logParams)
        }
    }

    internal fun showTemplateErrorDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("温馨提示")
                .setMessage("当前版本暂不支持该模板, 请升级应用后查看")
                .setPositiveButton("前去升级") { _, _ ->
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(K.kPgyerUrl))
                    startActivity(browserIntent)
                }
                .setNegativeButton("稍后升级") { _, _ ->
                    // 返回 LoginActivity
                }
        builder.show()
    }

    fun back(v: View?) {
        finish()
    }

    /**
     * 释放资源
     */
    override fun onDestroy() {
        super.onDestroy()
        if (subscribe.isUnsubscribed)
            subscribe.unsubscribe()
    }
}
