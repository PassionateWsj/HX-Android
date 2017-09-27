package com.intfocus.hx.view.addressselector

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.intfocus.hx.R
import com.intfocus.hx.data.response.filter.MenuItem
import java.util.*

/**
 * Created by CANC on 2017/8/3.
 */
class AddressPopupWindow(activity: Activity, citydats: ArrayList<MenuItem>, lisenter: AddressLisenter) : PopupWindow() {
    lateinit var conentView: View
    lateinit var ivClose: ImageView
    lateinit var addressSelector: AddressSelector

    var mActicity: Activity? = activity
    var lisenter: AddressLisenter? = lisenter

    var currentCityDatas: ArrayList<MenuItem>? = null//当前选中省份下城市数据
    var currentCountyDatas: ArrayList<MenuItem>? = null//当前城市下区域数据

    var currentProvinceData: MenuItem? = null
    var currentCityData: MenuItem? = null
    var currentCountyData: MenuItem? = null
    var datas: ArrayList<MenuItem>? = citydats


    fun upDateDatas(citydats: ArrayList<MenuItem>) {
        this.datas = citydats
    }


    fun init() {
        val inflater = LayoutInflater.from(mActicity)
        conentView = inflater.inflate(R.layout.popup_address, null)
        addressSelector = conentView.findViewById(R.id.address_selector) as AddressSelector
        ivClose = conentView.findViewById(R.id.iv_close) as ImageView
        // 设置SelectPicPopupWindow的View
        this.contentView = conentView
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.width = LinearLayout.LayoutParams.MATCH_PARENT
        // 设置SelectPicPopupWindow弹出窗体的高
        this.height = LinearLayout.LayoutParams.WRAP_CONTENT
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.isFocusable = true
        this.isOutsideTouchable = true
        // 刷新状态
        this.update()
        // 实例化一个ColorDrawable颜色为半透明
        val dw = ColorDrawable(0)
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw)
        addressSelector.setTabAmount(3)
        addressSelector.setCities(datas)
        currentCityDatas = ArrayList<MenuItem>()
        currentCountyDatas = ArrayList<MenuItem>()
        ivClose.setOnClickListener { this.dismiss() }
        addressSelector.setOnItemClickListener { addressSelector, city, tabPosition ->
            when (tabPosition) {
                0 -> for (cityData in datas!!) {
                    if (cityData.cityName === city.cityName) {
                        currentProvinceData = cityData
                        currentCityDatas = cityData.data
                        addressSelector.setCities(currentCityDatas)
                    }
                }
                1 -> for (cityData in currentCityDatas!!) {
                    if (cityData.cityName === city.cityName) {
                        currentCityData = cityData
                        currentCountyDatas = cityData.data
                        addressSelector.setCities(currentCountyDatas)
                    }
                }
                2 -> for (cityData in currentCountyDatas!!) {
                    if (cityData.cityName === city.cityName) {
                        currentCountyData = cityData
                        lisenter!!.complete(currentProvinceData!!, currentCityData!!, currentCountyData!!)
                    }
                }
            }
        }

        addressSelector.setOnTabSelectedListener(object : AddressSelector.OnTabSelectedListener {
            override fun onTabSelected(addressSelector: AddressSelector, tab: AddressSelector.Tab) {
                when (tab.index) {
                    0 -> addressSelector.setCities(datas)
                    1 -> addressSelector.setCities(currentCityDatas)
                    2 -> addressSelector.setCities(currentCountyDatas)
                }
            }

            override fun onTabReselected(addressSelector: AddressSelector, tab: AddressSelector.Tab) {

            }
        })

    }

    interface AddressLisenter {
        fun complete(provinceData: MenuItem, cityData: MenuItem, countyData: MenuItem)
    }
}
