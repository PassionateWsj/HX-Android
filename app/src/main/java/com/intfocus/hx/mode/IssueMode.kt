package com.intfocus.hx.mode

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Environment
import com.google.gson.Gson
import com.intfocus.hx.dashboard.mine.bean.IssueCommitInfo
import com.intfocus.hx.dashboard.mine.bean.IssueCommitRequest
import com.intfocus.hx.dashboard.mine.bean.IssueListBean
import com.intfocus.hx.dashboard.mine.bean.IssueListRequest
import com.intfocus.hx.util.*
import com.zbl.lib.baseframe.core.AbstractMode
import com.zbl.lib.baseframe.utils.StringUtil
import okhttp3.*
import org.greenrobot.eventbus.EventBus
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.util.*


/**
 * Created by liuruilin on 2017/6/11.
 */
class IssueMode(var ctx: Context) : AbstractMode() {
    lateinit var urlString: String
    var result: String? = null
    val mIssueSP: SharedPreferences = ctx.getSharedPreferences("IssueList", Context.MODE_PRIVATE)
    var gson = Gson()
    var fileList: MutableList<File> = mutableListOf()

    fun getUrl(): String {
        var url = "http://development.shengyiplus.com/api/v1/user/123456/page/1/limit/10/problems"
        return url
    }

    override fun requestData() {
        Thread(Runnable {
            urlString = getUrl()
            if (!urlString.isEmpty()) {
                val response = HttpUtil.httpGet(ctx, urlString, HashMap<String, String>())
                result = response["body"]
                if (StringUtil.isEmpty(result)) {
                    val result1 = IssueListRequest(false, 400)
                    EventBus.getDefault().post(result1)
                    return@Runnable
                }
                analysisData(result)
            } else {
                val result1 = IssueListRequest(false, 400)
                EventBus.getDefault().post(result1)
                return@Runnable
            }
        }).start()
    }

    /**
     * 解析数据
     * @param result
     */
    private fun analysisData(result: String?): IssueListRequest {
        try {
            val jsonObject = JSONObject(result)
            if (jsonObject.has("code")) {
                val code = jsonObject.getInt("code")
                if (code != 200) {
                    val result1 = IssueListRequest(false, code)
                    EventBus.getDefault().post(result1)
                    return result1
                }
            }

            var resultStr = jsonObject.toString()
            mIssueSP.edit().putString("IssueList", resultStr).commit()
            var issueListBean = gson.fromJson(resultStr, IssueListBean::class.java)
            val result1 = IssueListRequest(true, 200)
            result1.issueList = issueListBean
            EventBus.getDefault().post(result1)
            return result1
        } catch (e: JSONException) {
            e.printStackTrace()
            val result1 = IssueListRequest(false, -1)
            EventBus.getDefault().post(result1)
        }

        val result1 = IssueListRequest(false, 0)
        EventBus.getDefault().post(result1)
        return result1
    }

    fun addUploadImg(bmp: Bitmap) {
        if (fileList.size <= 3) {
            var str = Environment.getExternalStorageDirectory().toString() + "/" + "image1.png"
            if (File(str).exists()) {
                File(str).delete()
            }
            fileList.add(FileUtil.saveImage(str, bmp))
        } else {
            ToastUtils.show(ctx, "仅可上传3张图片")
        }
    }

    fun setUploadImg(imgFile: File) {
        fileList.add(imgFile)
    }

    /**
    params:
    {
    title: 标题-可选,
    content: 反馈内容-必填,
    user_num: 用户编号-可选,
    app_version: 应用版本-可选,
    platform: 系统名称-可选,
    platform_version: 系统版本-可选,
    images: [
    multipart/form-data
    ]
    }
     */
    fun commitIssue2(issueInfo: IssueCommitInfo) {
        var mOkHttpClient = OkHttpClient()

        val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("api_token", URLs.MD5(K.ANDROID_API_KEY + K.KFeedBack + K.ANDROID_API_KEY))
                .addFormDataPart("content", issueInfo.issue_content)
                .addFormDataPart("title", "生意人问题反馈")
                .addFormDataPart("user_num", issueInfo.user_num)
                .addFormDataPart("app_version", issueInfo.app_version)
                .addFormDataPart("platform", issueInfo.platform)
                .addFormDataPart("platform_version", issueInfo.platform_version)

        if (!fileList.isEmpty()) {
            for ((i, file) in fileList.withIndex()) {
                requestBody.addFormDataPart("image" + i, file.name, RequestBody.create(MediaType.parse("image/*"), file))
            }
        }

        val request = Request.Builder()
                .url(K.kBaseUrl + K.KFeedBack)
                .post(requestBody.build())
                .build()

        mOkHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                var request = IssueCommitRequest(false, "提交失败")
                EventBus.getDefault().post(request)

                var logParams = JSONObject()
                logParams.put(URLs.kAction, "点击/问题反馈/失败")
                ActionLogUtil.actionLog(ctx, logParams)
            }

            override fun onResponse(call: Call?, response: Response?) {
                if (response!!.isSuccessful) {
                    var request = IssueCommitRequest(true, "提交成功")
                    EventBus.getDefault().post(request)

                    var logParams = JSONObject()
                    logParams.put(URLs.kAction, "点击/问题反馈/成功")
                    ActionLogUtil.actionLog(ctx, logParams)
                } else {
                    var request = IssueCommitRequest(false, "提交失败")
                    EventBus.getDefault().post(request)

                    var logParams = JSONObject()
                    logParams.put(URLs.kAction, "点击/问题反馈/失败")
                    ActionLogUtil.actionLog(ctx, logParams)
                }
            }
        })

    }
}
