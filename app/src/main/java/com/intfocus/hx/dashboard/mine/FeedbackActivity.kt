package com.intfocus.hx.dashboard.mine

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import com.intfocus.hx.R
import com.intfocus.hx.dashboard.mine.adapter.FeedbackPageScreenshotAdapter
import com.intfocus.hx.dashboard.mine.bean.IssueCommitInfo
import com.intfocus.hx.dashboard.mine.bean.IssueCommitRequest
import com.intfocus.hx.mode.IssueMode
import com.intfocus.hx.util.*
import com.zbl.lib.baseframe.core.AbstractActivity
import com.zbl.lib.baseframe.core.Subject
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.filter.Filter
import kotlinx.android.synthetic.main.activity_feedback.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File


class FeedbackActivity : AbstractActivity<IssueMode>(), View.OnClickListener, FeedbackPageScreenshotAdapter.ScreenshotItemClickListener {
    override fun setLayoutRes(): Int {
        TODO("重写 BaseActivity 后, 需重写相关联 Activity 的 setLayoutRes")
    }

    override fun onCreateFinish(p0: Bundle?) {
    }

    companion object {
        val REQUEST_CODE_CHOOSE = 1
    }

    var mSelected: List<Uri>? = null
    val screenshotAdapter: FeedbackPageScreenshotAdapter = FeedbackPageScreenshotAdapter(this, this)

    lateinit var mUserSP: SharedPreferences
    lateinit var mProgressDialog: ProgressDialog

    val issueInfo = IssueCommitInfo()

    override fun setSubject(): Subject {
        return IssueMode(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        initData()
        initView()
        initAdapter()
    }


    private fun initData() {
        EventBus.getDefault().register(this)
        mUserSP = getSharedPreferences("UserBean", Context.MODE_PRIVATE)

    }

    private fun initView() {
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        rv_feedback_page_screenshot.layoutManager = linearLayoutManager
    }

    private fun initAdapter() {
        rv_feedback_page_screenshot.adapter = screenshotAdapter
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_feedback_banner_back -> {
                finish()
            }
            R.id.btn_feedback_submit -> {
                if (TextUtils.isEmpty(et_feedback_suggestion.text.toString()) && screenshotAdapter.getData().size == 0) {
                    ToastUtils.show(this, "请完善信息再提交")
                } else {
                    for (uri in screenshotAdapter.getData()) {
                        model.setUploadImg(File(ImageUtil.handleImageOnKitKat(uri, this)))
                    }
                    commitIssue()
                }
            }
        }
    }

    fun commitIssue() {
        mProgressDialog = ProgressDialog.show(this, "稍等", "正在提交...")
        issueInfo.issue_content = et_feedback_suggestion.text.toString()
        issueInfo.app_version = mUserSP.getString(K.kAppVersion, "2.0+")
        issueInfo.platform = mUserSP.getString("device_info", "android")
        issueInfo.platform_version = mUserSP.getString("os_version", "0")
        issueInfo.user_num = mUserSP.getString(URLs.kUserNum, "null")
        model.commitIssue2(issueInfo)
    }


    override fun addScreenshot(maxNum: Int) {
        Matisse.from(this)
                .choose(MimeType.allOf())
                .countable(true)
                .maxSelectable(maxNum)
                .addFilter(GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(
                        resources.getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE)

    }

    override fun delScreenshot(pos: Int) {
        screenshotAdapter.delScreenWithPos(pos)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data)
            screenshotAdapter.setData(mSelected)
            LogUtil.d("Matisse", "mSelected: " + mSelected)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showIssueCommitRequestDialog(request: IssueCommitRequest) {
        mProgressDialog.dismiss()
        if (request.isSuccess) {
            val builder = android.app.AlertDialog.Builder(this)
            builder.setTitle("温馨提示")
                    .setMessage("提交成功")
                    .setPositiveButton("确认") { _, _ ->
                        finish()
                    }
                    .setNegativeButton("取消") { _, _ ->
                        // 不进行任何操作
                    }
            builder.show()
        } else {
            val builder = android.app.AlertDialog.Builder(this)
            builder.setTitle("温馨提示")
                    .setMessage("提交失败, 是否重试?")
                    .setPositiveButton("确认") { _, _ ->
                        model.commitIssue2(issueInfo)
                    }
                    .setNegativeButton("取消") { _, _ ->
                        // 不进行任何操作
                    }
            builder.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}

