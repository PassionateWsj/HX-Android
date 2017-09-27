package com.intfocus.hx.util

import android.content.Context
import com.daimajia.numberprogressbar.NumberProgressBar
import com.intfocus.hx.data.response.assets.AssetsResult
import com.intfocus.hx.net.ApiException
import com.intfocus.hx.net.CodeHandledSubscriber
import com.intfocus.hx.net.HttpService
import com.intfocus.hx.net.RetrofitUtil
import retrofit2.Retrofit
import rx.Observable
import rx.Observer
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.File

/**
 * ****************************************************
 * author: jameswong
 * created on: 17/08/29 上午10:35
 * e-mail: PassionateWsj@outlook.com
 * name:
 * desc:
 * ****************************************************
 */
object AssetsUpDateUtil {
    private var observable: Subscription? = null

    fun checkAssetsUpdate(ctx: Context, listener: OnCheckAssetsUpdateResultListener) {
        checkAssetsUpdate(ctx, null, listener)
    }

    fun checkAssetsUpdate(ctx: Context, progressBar: NumberProgressBar?, listener: OnCheckAssetsUpdateResultListener) {

        var sharedPath = String.format("%s/%s", FileUtil.basePath(ctx), K.kSharedDirName)
        LogUtil.d("hjjzz", "MainThread:::" + Thread.currentThread().name)
        RetrofitUtil.getHttpService(ctx).assetsMD5
                .compose(RetrofitUtil.CommonOptions<AssetsResult>())
                .subscribe(object : CodeHandledSubscriber<AssetsResult>() {
                    override fun onError(apiException: ApiException?) {

                    }

                    override fun onCompleted() {

                    }

                    override fun onBusinessNext(data: AssetsResult?) {
                        LogUtil.d("hjjzz", "getHttpService(ctx).assetsMD5:::" + Thread.currentThread().name)
                        val assetsMD5s = data!!.data!!
                        val mAssetsSP = ctx.getSharedPreferences("AssetsMD5", Context.MODE_PRIVATE)
                        val mAssetsSPEdit = mAssetsSP.edit()
                        val assetsNameArr = listOf(URLs.kFonts, URLs.kImages,
                                URLs.kIcons, URLs.kStylesheets,
                                URLs.kJavaScripts, URLs.kAdvertisement)
                        val assetsMD5sMap = HashMap<String, String>()
                        assetsMD5sMap.put(URLs.kFonts + "_md5", assetsMD5s.fonts_md5!!)
                        assetsMD5sMap.put(URLs.kImages + "_md5", assetsMD5s.images_md5!!)
                        assetsMD5sMap.put(URLs.kIcons + "_md5", assetsMD5s.icons_md5!!)
                        assetsMD5sMap.put(URLs.kJavaScripts + "_md5", assetsMD5s.javascripts_md5!!)
                        assetsMD5sMap.put(URLs.kStylesheets + "_md5", assetsMD5s.stylesheets_md5!!)
                        assetsMD5sMap.put(URLs.kAdvertisement + "_md5", assetsMD5s.advertisement_md5!!)

                        if (progressBar != null)
                            progressBar.progress += 10

                        observable = Observable.from(assetsNameArr)
                                .subscribeOn(Schedulers.io())
                                .map { assetName ->
                                    LogUtil.d("hjjzz", "unzip:::" + Thread.currentThread().name)
                                    if (!assetsMD5sMap[assetName + "_md5"].equals(mAssetsSP.getString(assetName + "_md5", ""))) {
                                        var fileUrl = K.kDownloadAssetsZip + "?api_token=d93c1a0dc03fe4ffad55a82febd1c94f&filename=" + assetName + ".zip"
                                        var response = Retrofit.Builder()
                                                .baseUrl(K.kBaseUrl)
                                                .build()
                                                .create(HttpService::class.java).downloadFileWithDynamicUrlSync(fileUrl).execute()
                                        var isWriteZipSuccess = FileUtil.writeResponseBodyToDisk(response!!.body()!!, sharedPath, assetName + ".zip")
                                        if (isWriteZipSuccess) {
                                            var isUnZipSuccess = FileUtil.unZipAssets(ctx, assetName)
                                            if (isUnZipSuccess) {
                                                mAssetsSPEdit.putString(assetName + "_md5", assetsMD5sMap[assetName + "_md5"]).commit()
                                                true
                                            }
                                            false
                                        }
                                        false
                                    }
                                    true
                                }
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(object : Observer<Boolean> {
                                    override fun onError(p0: Throwable?) {
                                        listener.onFailure()
                                    }

                                    override fun onCompleted() {
                                        LogUtil.d("hjjzz", "unzip:onCompleted:::" + Thread.currentThread().name)
                                        if (progressBar != null)
                                            progressBar.progress += 10
                                        listener.onResultSuccess()
                                    }

                                    override fun onNext(isCheckSuccess: Boolean?) {
                                        if (!isCheckSuccess!!)
                                            this.onError(kotlin.Throwable("解压出错"))
                                        if (progressBar != null)
                                            progressBar.progress += 10
                                    }
                                })
                    }
                })
    }

    /**
     * 取消订阅
     */
    fun unSubscribe() {
        if (observable != null && !observable!!.isUnsubscribed)
            observable!!.unsubscribe()
    }

    fun checkFirstSetup(ctx: Context, listener: OnCheckAssetsUpdateResultListener) {
        makeSureFolderExist(ctx, K.kSharedDirName)
        makeSureFolderExist(ctx, K.kCachedDirName)
        val mAssetsSP = ctx.getSharedPreferences("AssetsMD5", Context.MODE_PRIVATE)
        val mAssetsSPEdit = mAssetsSP.edit()
        Observable.just("assets", "loading")
                .subscribeOn(Schedulers.io())
                .map { assetsName ->
                    FileUtil.copyAssetFile(ctx, assetsName + ".zip", String.format("%s/%s/%s", FileUtil.basePath(ctx), K.kSharedDirName, assetsName + ".zip"))
                    var isUnZipSuccess = FileUtil.unZipAssets(ctx, assetsName)
                    if (isUnZipSuccess) {
                        val mD5 = FileUtil.MD5(File(String.format("%s/%s/%s", FileUtil.basePath(ctx), K.kSharedDirName, assetsName + ".zip")))
                        mAssetsSPEdit.putString(assetsName + "_md5", mD5).commit()
                        true
                    }
                    false
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Boolean> {
                    override fun onError(p0: Throwable?) {
                    }

                    override fun onNext(p0: Boolean?) {
                    }

                    override fun onCompleted() {
                        listener.onResultSuccess()
                    }

                })
    }

    private fun makeSureFolderExist(ctx: Context, folderName: String) {
        val cachedPath = String.format("%s/%s", FileUtil.basePath(ctx), folderName)
        FileUtil.makeSureFolderExist(cachedPath)
    }
}

interface OnCheckAssetsUpdateResultListener {
    fun onResultSuccess()
    fun onFailure()
}
