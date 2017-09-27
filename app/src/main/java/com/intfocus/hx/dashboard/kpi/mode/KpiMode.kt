package com.intfocus.hx.dashboard.kpi.mode

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.intfocus.hx.dashboard.kpi.bean.Message
import com.intfocus.hx.dashboard.kpi.bean.KpiRequest
import com.intfocus.hx.dashboard.kpi.bean.KpiResultData
import com.intfocus.hx.dashboard.kpi.bean.MsgRequest
import com.intfocus.hx.dashboard.kpi.bean.MsgResultData
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
 * Created by liuruilin on 2017/6/21.
 */
class KpiMode(var ctx: Context) : AbstractMode() {
    lateinit var urlString: String
    var result: String? = null
    var messageUrlString = ""
    var mNoticeListSP = ctx.getSharedPreferences("KpiData", Context.MODE_PRIVATE)
    var mUserSP = ctx.getSharedPreferences("UserBean", Context.MODE_PRIVATE)
    var gson = Gson()

    fun getUrl(): String {
        var url = String.format(K.kKPIApiDataPath, K.kBaseUrl,
                mUserSP.getString(URLs.kGroupId, "0"), mUserSP.getString(URLs.kRoleId, "0"))
        return url
    }

    fun getMessageUrl(): String {
        val url = String.format(K.kMessageDataMobilePath, K.kBaseUrl, mUserSP.getString(URLs.kRoleId, "0"),
                mUserSP.getString(URLs.kGroupId, "0"),
                mUserSP.getString("user_id", "0"))
        return url
    }

    override fun requestData() {
        Thread(Runnable {
            urlString = getUrl()
            if (!urlString.isEmpty()) {
                val response = HttpUtil.httpGet(ctx, urlString, HashMap<String, String>())
                result = response["body"]
                if (StringUtil.isEmpty(result)) {
                    val result1 = KpiRequest(false, 400, "返回数据为空")
                    EventBus.getDefault().post(result1)
                    return@Runnable
                }
                analysisData(result)
            } else {
                val result1 = KpiRequest(false, 400, "请求链接为空")
                EventBus.getDefault().post(result1)
                return@Runnable
            }
        }).start()
//        requestMessage()
        //请求信息
//        getMessage()
    }

    /**
     * 解析数据
     * @param result
     */
    private fun analysisData(result: String?): KpiRequest {
        var errormsg = "请求失败"
        try {
            val jsonObject = JSONObject(result)
            if (jsonObject.has("code")) {
                val code = jsonObject.getInt("code")
                if (code != 200) {
                    if (jsonObject.has("message")) {
                        errormsg = jsonObject.getString("message")
                    }
                    val result1 = KpiRequest(false, code, errormsg)
                    EventBus.getDefault().post(result1)
                    return result1
                }
            } else {
                val result1 = KpiRequest(false, 404, "未找到服务器")
                EventBus.getDefault().post(result1)
                return result1
            }

            mNoticeListSP.edit().putString("KpiData", jsonObject.toString()).commit()
            var mKpiData = gson.fromJson(jsonObject.toString().replace("null", "\"\""), KpiResultData::class.java)
            if (jsonObject.has("message")) {
                errormsg = jsonObject.getString("message")
            }
            val result1 = KpiRequest(true, 200, errormsg)
            result1.kpi_data = mKpiData
            EventBus.getDefault().post(result1)
            return result1
        } catch (e: JSONException) {
            e.printStackTrace()
            val result1 = KpiRequest(false, -1, "解析错误")
            EventBus.getDefault().post(result1)
        }
        val result1 = KpiRequest(false, 0, "失败")
        EventBus.getDefault().post(result1)
        return result1
    }

    /**

     * 获取公告栏消息
     * @return message
     */
    fun getMessage(): Array<String>? {
        var message: Array<String>
        messageUrlString = getMessageUrl()
        if (messageUrlString.isEmpty()) {
            return null
        }

        val gson = Gson()
        val response = HttpUtil.httpGet(ctx, messageUrlString, HashMap<String, String>())

        if (response["code"].equals("200")) {
            val returnData = JsonParser().parse(response["body"]).asJsonObject
            val mMessage = gson.fromJson(returnData, Message::class.java)

            val firstMessage = mMessage.data[0].title
            val secondMessage = mMessage.data[mMessage.data.size - 1].title

            message = arrayOf(firstMessage, secondMessage)
        } else {
            message = arrayOf("暂无数据", "暂无数据")
        }

        return message
    }

    fun requestMessage() {
        Thread(Runnable {
            messageUrlString = getMessageUrl()
            if (!messageUrlString.isEmpty()) {
                val response = HttpUtil.httpGet(ctx, messageUrlString, HashMap<String, String>())
                result = response["body"]
                if (StringUtil.isEmpty(result)) {
                    val result1 = KpiRequest(false, 400, "空数据")
                    EventBus.getDefault().post(result1)
                    return@Runnable
                }
                analysisMsgData(result)
            } else {
                val result1 = KpiRequest(false, 400, "请求链接为空")
                EventBus.getDefault().post(result1)
                return@Runnable
            }
        }).start()
    }

    /**
     * 解析数据
     * @param result
     */
    private fun analysisMsgData(result: String?): MsgRequest {
        var errormsg = "请求失败"
        try {
            val jsonObject = JSONObject(result)
            if (jsonObject.has("code")) {
                val code = jsonObject.getInt("code")
                if (code != 200) {
                    if (jsonObject.has("message")) {
                        errormsg = jsonObject.getString("message")
                    }
                    val result1 = MsgRequest(false, code, errormsg)
                    EventBus.getDefault().post(result1)
                    return result1
                }
            } else {
                val result1 = MsgRequest(false, 404, "未找到服务器")
                EventBus.getDefault().post(result1)
                return result1
            }
            var mMsgResultData = gson.fromJson(jsonObject.toString().replace("null", "\"\""), MsgResultData::class.java)
            if (jsonObject.has("message")) {
                errormsg = jsonObject.getString("message")
            }
            val result1 = MsgRequest(true, 200, errormsg)
            result1.msgData = mMsgResultData
            EventBus.getDefault().post(result1)
            return result1
        } catch (e: JSONException) {
            e.printStackTrace()
            val result1 = KpiRequest(false, -1, "解析错误")
            EventBus.getDefault().post(result1)
        }
        val result1 = MsgRequest(false, 0, "失败")
        EventBus.getDefault().post(result1)
        return result1
    }

}
