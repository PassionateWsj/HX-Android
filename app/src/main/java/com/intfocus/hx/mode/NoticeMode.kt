package com.intfocus.hx.mode

import android.content.Context
import com.google.gson.Gson
import com.intfocus.hx.dashboard.mine.bean.NoticeListBean
import com.intfocus.hx.dashboard.mine.bean.NoticeListRquest
import com.intfocus.hx.util.HttpUtil
import com.intfocus.hx.util.K
import com.intfocus.hx.util.URLs
import com.zbl.lib.baseframe.core.AbstractMode
import com.zbl.lib.baseframe.utils.StringUtil
import org.greenrobot.eventbus.EventBus
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 * Created by liuruilin on 2017/6/12.
 */
class NoticeMode(var ctx: Context) : AbstractMode() {
    lateinit var urlString: String
    var result: String? = null
    var mUserSP = ctx.getSharedPreferences("UserBean", Context.MODE_PRIVATE)
    var mNoticeListBean: NoticeListBean? = null
    var page = 1
    var gson = Gson()
    var typeStr: String? = null
    var errorMsg: String? = "未知异常"

    fun getUrl(): String {
        var url = String.format(K.kNoticeListPath, K.kBaseUrl,
                mUserSP.getString(URLs.kUserNum, ""), typeStr, page, 10.toString())
        return url
    }

    fun requestData(page: Int, typeStr: String) {
        this.page = page
        this.typeStr = typeStr
        requestData()
    }

    override fun requestData() {
        Thread(Runnable {
            urlString = getUrl()
            if (!urlString.isEmpty()) {
                val response = HttpUtil.httpGet(ctx, urlString, HashMap<String, String>())
                result = response["body"]
                if (StringUtil.isEmpty(result)) {
                    val result1 = NoticeListRquest(true, 400, "数据为空")
                    result1.noticeListBean = mNoticeListBean
                    EventBus.getDefault().post(result1)
                    return@Runnable
                }
                analysisData(result)
            } else {
                val result1 = NoticeListRquest(true, 400, "请求链接为空")
                result1.noticeListBean = mNoticeListBean
                EventBus.getDefault().post(result1)
                return@Runnable
            }
        }).start()
    }

    /**
     * 解析数据
     * @param result
     */
    private fun analysisData(result: String?): NoticeListRquest {
        try {
            val jsonObject = JSONObject(result)
            if (jsonObject.has("code")) {
                val code = jsonObject.getInt("code")
                if (code != 200) {
                    if (jsonObject.has("message")) {
                        errorMsg = jsonObject.getString("message")
                    }
                    val result1 = NoticeListRquest(true, code, errorMsg!!)
                    result1.noticeListBean = mNoticeListBean
                    EventBus.getDefault().post(result1)
                    return result1
                }
            } else {
                val result1 = NoticeListRquest(true, 404, errorMsg!!)
                result1.noticeListBean = mNoticeListBean
                EventBus.getDefault().post(result1)
                return result1
            }

            var mNoticeList = gson.fromJson(jsonObject.toString(), NoticeListBean::class.java)
            val result1 = NoticeListRquest(true, 200, errorMsg!!)
            result1.noticeListBean = mNoticeList
            EventBus.getDefault().post(result1)
            return result1
        } catch (e: JSONException) {
            e.printStackTrace()
            val result1 = NoticeListRquest(true, -1, "解析出错")
            result1.noticeListBean = mNoticeListBean
            EventBus.getDefault().post(result1)
        }

        val result1 = NoticeListRquest(true, 0, errorMsg!!)
        result1.noticeListBean = mNoticeListBean
        EventBus.getDefault().post(result1)
        return result1
    }
}
