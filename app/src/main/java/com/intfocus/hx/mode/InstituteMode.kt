package com.intfocus.hx.mode

import android.content.Context
import com.google.gson.Gson
import com.intfocus.hx.dashboard.mine.bean.CollectionBean
import com.intfocus.hx.dashboard.mine.bean.CollectionRquest
import com.intfocus.hx.dashboard.mine.bean.InstittuteListBean
import com.intfocus.hx.dashboard.mine.bean.InstituteRquest
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
 * Created by CANC on 2017/7/25.
 */
class InstituteMode(var ctx: Context) : AbstractMode() {
    lateinit var urlString: String
    var result: String? = null
    var mUserSP = ctx.getSharedPreferences("UserBean", Context.MODE_PRIVATE)
    var mInstittuteListBean: InstittuteListBean? = null
    var mCollectionBean: CollectionBean? = null
    var page = 1
    var gson = Gson()
    var keyWorld: String? = ""
    var errorMsg: String? = "未知异常"

    /**
     * 获取列表URL
     */
    fun getUrl(): String {
        var url = String.format(K.KInstituteListPath, K.kBaseUrl,
                mUserSP.getString(URLs.kUserNum, ""), page, 10.toString(), keyWorld)
        return url
    }

    fun requestData(page: Int, keyWorld: String) {
        this.page = page
        this.keyWorld = keyWorld
        requestData()
    }

    override fun requestData() {
        Thread(Runnable {
            urlString = getUrl()
            if (!urlString.isEmpty()) {
                val response = HttpUtil.httpGet(ctx, urlString, HashMap<String, String>())
                result = response["body"]
                if (StringUtil.isEmpty(result)) {
                    val result1 = InstituteRquest(false, 400, errorMsg!!)
                    result1.instittuteListBean = mInstittuteListBean
                    EventBus.getDefault().post(result1)
                    return@Runnable
                }
                analysisData(result)
            } else {
                val result1 = InstituteRquest(false, 400, "请求链接为空")
                result1.instittuteListBean = mInstittuteListBean
                EventBus.getDefault().post(result1)
                return@Runnable
            }
        }).start()
    }

    /**
     * 解析数据
     * @param result
     */
    private fun analysisData(result: String?): InstituteRquest {
        try {
            val jsonObject = JSONObject(result)
            if (jsonObject.has("code")) {
                val code = jsonObject.getInt("code")
                if (code != 0) {
                    if (jsonObject.has("message")) {
                        errorMsg = jsonObject.getString("message")
                    }
                    val result1 = InstituteRquest(false, code, errorMsg!!)
                    result1.instittuteListBean = mInstittuteListBean
                    EventBus.getDefault().post(result1)
                    return result1
                }
            } else {
                val result1 = InstituteRquest(true, 404, errorMsg!!)
                result1.instittuteListBean = mInstittuteListBean
                EventBus.getDefault().post(result1)
                return result1
            }
            var mInstittuteListBean = gson.fromJson(jsonObject.toString(), InstittuteListBean::class.java)
            val result1 = InstituteRquest(true, 0, errorMsg!!)
            result1.instittuteListBean = mInstittuteListBean
            EventBus.getDefault().post(result1)
            return result1
        } catch (e: JSONException) {
            e.printStackTrace()
            val result1 = InstituteRquest(true, -1, "解析错误")
            result1.instittuteListBean = mInstittuteListBean
            EventBus.getDefault().post(result1)
        }

        val result1 = InstituteRquest(true, 0, errorMsg!!)
        result1.instittuteListBean = mInstittuteListBean
        EventBus.getDefault().post(result1)
        return result1
    }

    /**
     * 收藏/取消收藏
     */
    fun operatingCollection(articleId: String, favouriteStatus: String) {
        Thread(Runnable {
            var collectionUrl = String.format(K.KInstituteCollectionPath, K.kBaseUrl,
                    mUserSP.getString(URLs.kUserNum, ""), articleId, favouriteStatus)
            if (!collectionUrl.isEmpty()) {
                val response = HttpUtil.httpPost(collectionUrl, HashMap<String, String>())
                result = response["body"]
                if (StringUtil.isEmpty(result)) {
                    val result1 = InstituteRquest(false, 400, "数据为空")
                    result1.instittuteListBean = mInstittuteListBean
                    EventBus.getDefault().post(result1)
                    return@Runnable
                }
                analysisCollectionData(result)
            } else {
                val result1 = InstituteRquest(false, 400, "请求链接为空")
                result1.instittuteListBean = mInstittuteListBean
                EventBus.getDefault().post(result1)
                return@Runnable
            }
        }).start()
    }

    /**
     * 解析收藏数据
     * @param result
     */
    private fun analysisCollectionData(result: String?): CollectionRquest {
        try {
            val jsonObject = JSONObject(result)
            if (jsonObject.has("code")) {
                val code = jsonObject.getInt("code")
                if (code != 201) {
                    if (jsonObject.has("message")) {
                        errorMsg = jsonObject.getString("message")
                    }
                    var collectionBean = gson.fromJson(jsonObject.toString(), CollectionBean::class.java)
                    val result1 = CollectionRquest(false, code, errorMsg!!)
                    result1.collectionBean = collectionBean
                    EventBus.getDefault().post(result1)
                    return result1
                }
            } else {
                var collectionBean = gson.fromJson(jsonObject.toString(), CollectionBean::class.java)
                val result1 = CollectionRquest(false, 404, "找不到服务器啦")
                result1.collectionBean = collectionBean
                EventBus.getDefault().post(result1)
                return result1
            }
            var collectionBean = gson.fromJson(jsonObject.toString(), CollectionBean::class.java)
            val result1 = CollectionRquest(true, 0, "操作成功")
            result1.collectionBean = collectionBean
            EventBus.getDefault().post(result1)
            return result1
        } catch (e: JSONException) {
            e.printStackTrace()
            val result1 = CollectionRquest(false, -1, "解析错误")
            result1.collectionBean = mCollectionBean
            EventBus.getDefault().post(result1)
        }

        val result1 = CollectionRquest(true, 0, "操作成功")
        result1.collectionBean = mCollectionBean
        EventBus.getDefault().post(result1)
        return result1
    }
}
