package com.intfocus.hx.mode

import android.content.Context
import android.graphics.Bitmap
import com.google.gson.Gson
import com.intfocus.hx.dashboard.mine.bean.UserInfoRequest
import com.intfocus.hx.data.response.BaseResult
import com.intfocus.hx.data.response.mine_page.UserInfoResult
import com.intfocus.hx.net.ApiException
import com.intfocus.hx.net.CodeHandledSubscriber
import com.intfocus.hx.net.RetrofitUtil
import com.intfocus.hx.util.*
import com.intfocus.hx.util.K.kUserDeviceId
import com.intfocus.hx.util.K.kUserId
import com.zbl.lib.baseframe.core.AbstractMode
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by liuruilin on 2017/6/7.
 */
class UserInfoMode(var ctx: Context) : AbstractMode() {
    lateinit var urlString: String
    var result: String? = null
    var mUserSP = ctx.getSharedPreferences("UserBean", Context.MODE_PRIVATE)
    var gson = Gson()

    override fun requestData() {
        RetrofitUtil.getHttpService(ctx).getUserInfo(mUserSP.getString(URLs.kUserNum, ""))
                .compose(RetrofitUtil.CommonOptions<UserInfoResult>())
                .subscribe(object : CodeHandledSubscriber<UserInfoResult>() {
                    override fun onBusinessNext(data: UserInfoResult?) {
                        val result1 = UserInfoRequest(true, 200)
                        result1.userInfoBean = data!!.data
                        EventBus.getDefault().post(result1)
                    }

                    override fun onError(apiException: ApiException?) {
                    }

                    override fun onCompleted() {
                    }
                })
    }

    fun uplodeUserIcon(bitmap: Bitmap, imgPath: String) {
        Thread(Runnable {
            var format = SimpleDateFormat("yyyyMMddHHmmss")
            var date = Date(System.currentTimeMillis())
            File(imgPath).delete()
            var gravatarImgPath = FileUtil.dirPath(ctx, K.kConfigDirName, K.kAppCode + "_" + mUserSP.getString(URLs.kUserNum, "") + "_" + format.format(date) + ".jpg")
            FileUtil.saveImage(gravatarImgPath, bitmap)
            var requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), File(gravatarImgPath))
            var multiPartBody = MultipartBody.Part.createFormData("gravatar", mUserSP.getString(kUserId, "0") + "icon", requestBody)
            RetrofitUtil.getHttpService(ctx).userIconUpload(mUserSP.getString(kUserDeviceId, "0"), mUserSP.getString(URLs.kUserNum, "0"),multiPartBody)
                    .compose(RetrofitUtil.CommonOptions<BaseResult>())
                    .subscribe(object : CodeHandledSubscriber<BaseResult>() {
                        override fun onBusinessNext(data: BaseResult?) {
                            ToastUtils.show(ctx, "头像已上传",ToastColor.SUCCESS)
                        }

                        override fun onError(apiException: ApiException?) {
                            ToastUtils.show(ctx, apiException!!.displayMessage)

                        }

                        override fun onCompleted() {
                        }
                    })
        }).start()
    }

    fun modifiedUserConfig(isLogin: Boolean) {
        try {
            val configJSON = JSONObject()
            configJSON.put("is_login", isLogin)
            val userConfigPath = String.format("%s/%s", FileUtil.basePath(ctx), K.kUserConfigFileName)
            var userJSON = FileUtil.readConfigFile(userConfigPath)

            userJSON = ApiHelper.mergeJson(userJSON, configJSON)
            FileUtil.writeFile(userConfigPath, userJSON.toString())

            val settingsConfigPath = FileUtil.dirPath(ctx, K.kConfigDirName, K.kSettingConfigFileName)
            FileUtil.writeFile(settingsConfigPath, userJSON.toString())
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}
