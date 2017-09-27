package com.intfocus.hx.view.addressselector

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.intfocus.hx.R
import com.intfocus.hx.dashboard.mine.adapter.FilterMenuItemAdapter
import com.intfocus.hx.data.response.filter.MenuItem
import java.util.*

/**
 * Created by CANC on 2017/8/3.
 */
class FilterPopupWindow(activity: Activity, citydats: ArrayList<MenuItem>, lisenter: MenuLisenter) : PopupWindow() {
    lateinit var conentView: View
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: FilterMenuItemAdapter
    var mActivity: Activity? = activity
    var lisenter: MenuLisenter? = lisenter

    var datas: ArrayList<MenuItem>? = citydats


    fun upDateDatas(citydats: ArrayList<MenuItem>) {
        adapter.setData(citydats)
    }


    fun init() {
        val inflater = LayoutInflater.from(mActivity)
        conentView = inflater.inflate(R.layout.popup_filter, null)
        recyclerView = conentView.findViewById(R.id.recycler_view) as RecyclerView

        val mLayoutManager = LinearLayoutManager(mActivity)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = mLayoutManager
        adapter = FilterMenuItemAdapter(mActivity!!, datas, lisenter!!)
        recyclerView.adapter = adapter


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
    }

    interface MenuLisenter : FilterMenuItemAdapter.FilterMenuItemListener {
        override fun menuItemClick(position: Int)
    }
}
