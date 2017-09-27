package com.intfocus.hx.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import org.greenrobot.eventbus.EventBus

/**
 * Created by CANC on 2017/8/7.
 * 使用EventBus传输定位信息
 */
class LocationService : Service(), AMapLocationListener {
    var locationClient: AMapLocationClient? = null
    var locationOption: AMapLocationClientOption? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        //初始化client
        locationClient = AMapLocationClient(this.applicationContext)
        locationOption = defaultOption
        //设置定位参数
        locationClient!!.setLocationOption(locationOption)
        // 设置定位监听
        locationClient!!.setLocationListener(this)
        // 启动定位
        locationClient!!.startLocation()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (locationClient != null) {
            locationClient!!.unRegisterLocationListener(this)
            locationClient!!.stopLocation()
        }
    }

    override fun onLocationChanged(location: AMapLocation?) {
        EventBus.getDefault().post(location)
        locationClient!!.stopLocation()
    }

    /**
     * 默认的定位参数

     * @author hongming.wang
     * *
     * @since 2.8.0
     */
    val defaultOption: AMapLocationClientOption
        get() {
            val mOption = AMapLocationClientOption()
            //可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
            mOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
            //可选，设置是否gps优先，只在高精度模式下有效。默认关闭
            mOption.isGpsFirst = false
            //可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
            mOption.httpTimeOut = 30000
            //可选，设置定位间隔。默认为2秒
            mOption.interval = 2000
            //可选，设置是否返回逆地理地址信息。默认是true
            mOption.isNeedAddress = true
            //可选，设置是否单次定位。默认是false
            mOption.isOnceLocation = true
            //可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
            mOption.isOnceLocationLatest = false
            //可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
            AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP)
            //可选，设置是否使用传感器。默认是false
            mOption.isSensorEnable = false
            //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
            mOption.isWifiScan = true
            //可选，设置是否使用缓存定位，默认为true
            mOption.isLocationCacheEnable = true
            return mOption
        }
}
