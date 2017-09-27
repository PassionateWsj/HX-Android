package com.intfocus.hx.filter

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.amap.api.location.AMapLocation
import com.intfocus.hx.R
import com.intfocus.hx.dashboard.mine.adapter.FilterMenuAdapter
import com.intfocus.hx.data.response.filter.MenuItem
import com.intfocus.hx.data.response.filter.MenuResult
import com.intfocus.hx.net.ApiException
import com.intfocus.hx.net.CodeHandledSubscriber
import com.intfocus.hx.net.RetrofitUtil
import com.intfocus.hx.service.LocationService
import com.intfocus.hx.util.LogUtil
import com.intfocus.hx.util.ToastUtils
import com.intfocus.hx.view.addressselector.FilterPopupWindow
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by CANC on 2017/8/3.
 */
class FilterActivity : FragmentActivity(), FilterMenuAdapter.FilterMenuListener, FilterPopupWindow.MenuLisenter, MyFilterDialogFragment.FilterLisenter {

    lateinit var mActivity: FragmentActivity
    /**
     * 地址选择
     */
    lateinit var locationDatas: ArrayList<MenuItem>
    /**
     * 菜单
     */
    var currentPosition: Int? = 0//当前展开的menu
    lateinit var menuDatas: ArrayList<MenuItem>
    lateinit var menuAdpter: FilterMenuAdapter
    var filterPopupWindow: FilterPopupWindow? = null
    lateinit var viewLine: View
    lateinit var viewBg: View

    lateinit var filterRecyclerView: RecyclerView
    lateinit var tvAddressFilter: TextView
    lateinit var tvLocationAddress: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        EventBus.getDefault().register(this)
        mActivity = this
        initView()
        startService(Intent(this, LocationService::class.java))
        getMenuData()
    }

    fun initView() {
        locationDatas = ArrayList()
        menuDatas = ArrayList()
        viewLine = findViewById(R.id.view_line)
        viewBg = findViewById(R.id.view_bg)
        tvAddressFilter = findViewById(R.id.tv_address_filter) as TextView
        tvLocationAddress = findViewById(R.id.tv_location_address) as TextView
        filterRecyclerView = findViewById(R.id.filter_recycler_view) as RecyclerView


        val mLayoutManager = LinearLayoutManager(mActivity)
        mLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        filterRecyclerView.layoutManager = mLayoutManager
        menuAdpter = FilterMenuAdapter(mActivity, menuDatas, this)
        filterRecyclerView.adapter = menuAdpter

        tvAddressFilter.setOnClickListener {
            showDialogFragment()
        }
    }


    fun showDialogFragment() {
        val mFragTransaction = supportFragmentManager.beginTransaction()
        val fragment = supportFragmentManager.findFragmentByTag("dialogFragment")
        if (fragment != null) {
            //为了不重复显示dialog，在显示对话框之前移除正在显示的对话框
            mFragTransaction.remove(fragment)
        }
        val dialogFragment = MyFilterDialogFragment(locationDatas, this)
        dialogFragment!!.show(mFragTransaction!!, "dialogFragment")//显示一个Fragment并且给该Fragment添加一个Tag，可通过findFragmentByTag找到该Fragment

    }

    fun initMenuPopup(position: Int) {
        filterPopupWindow = FilterPopupWindow(this, menuDatas!![position].data!!, this)
        filterPopupWindow!!.init()
    }

    private fun showMenuPop(position: Int) {
        if (filterPopupWindow == null) {
            initMenuPopup(position)
        } else {
            filterPopupWindow!!.upDateDatas(menuDatas!![position].data!!)
        }
        viewBg.visibility = View.VISIBLE
        filterPopupWindow!!.showAsDropDown(viewLine)
        filterPopupWindow!!.setOnDismissListener {
            for (menu in menuDatas) {
                menu.arrorDirection = false
            }
            menuAdpter.setData(menuDatas)
            viewBg.visibility = View.GONE
        }
    }


    /**
     * 获取筛选菜单数据
     */
    fun getMenuData() {
        RetrofitUtil.getHttpService(applicationContext).filterMenu
                .compose(RetrofitUtil.CommonOptions<MenuResult>())
                .subscribe(object : CodeHandledSubscriber<MenuResult>() {
                    override fun onError(apiException: ApiException?) {
                        ToastUtils.show(mActivity, apiException!!.displayMessage)
                    }

                    override fun onCompleted() {
                    }

                    override fun onBusinessNext(data: MenuResult?) {
                        for (menu in data!!.data) {
                            if ("location".equals(menu.type)) {
                                locationDatas = menu.data!!
                            }
                            if ("faster_select".equals(menu.type)) {
                                menuDatas = menu.data!!
                                menuAdpter.setData(menuDatas)
                            }
                        }
                    }
                })
    }

    /**
     * 点击普通筛选栏
     */
    override fun itemClick(position: Int) {
        //标记点击位置
        menuDatas!![position].arrorDirection = true
        menuAdpter.setData(menuDatas)
        currentPosition = position
        showMenuPop(position)
    }


    override fun menuItemClick(position: Int) {
        for (menuItem in menuDatas!![currentPosition!!].data!!) {
            menuItem.arrorDirection = false
        }
        //标记点击位置
        menuDatas!![currentPosition!!].data!![position].arrorDirection = true
        filterPopupWindow!!.dismiss()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun Location(location: AMapLocation) {
        if (location != null && location.errorCode == 0) {
            tvLocationAddress.text = location.address
        } else {
            tvLocationAddress.text = "定位失败"
        }
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        stopService(Intent(this, LocationService::class.java))
        super.onDestroy()
    }

    override fun complete(menuItems: ArrayList<MenuItem>) {
        var addressStr: String? = ""
        for (menuItem in menuItems) {
            addressStr += menuItem.name
        }
        tvLocationAddress.text = addressStr
        LogUtil.d("complete", menuItems.size.toString())
    }

}
