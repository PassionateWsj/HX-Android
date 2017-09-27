package com.intfocus.hx.filter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.*
import android.view.ViewGroup.LayoutParams
import android.widget.ImageView
import com.intfocus.hx.R
import com.intfocus.hx.data.response.filter.MenuItem


/**
 * Created by CANC on 2017/8/9.
 * 筛选专用dialogfragment,支持所有数据深度
 */
class MyFilterDialogFragment(mDatas: ArrayList<MenuItem>, lisenter: FilterLisenter) : DialogFragment(), NewFilterFragment.NewFilterFragmentListener {
    lateinit var mView: View
    lateinit var ivClose: ImageView
    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager
    var currentPosition: Int? = 0
    lateinit var adapter: FragmentAdapter
    var titleList = ArrayList<String>()
    var fragments = ArrayList<Fragment>()
    var datas: ArrayList<MenuItem>? = mDatas
    var lisenter: FilterLisenter? = lisenter
    var selectedDatas = ArrayList<MenuItem>()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        //放置位置
        dialog.window.setGravity(Gravity.LEFT)
        dialog.window.setGravity(Gravity.BOTTOM)
        //设置布局
        var mView = LayoutInflater.from(activity).inflate(R.layout.dialog_fragment_filter, null)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        dialog.window.setWindowAnimations(R.style.anim_popup_bottombar)
        this.mView = mView
        ivClose = mView.findViewById(R.id.iv_close) as ImageView
        tabLayout = mView.findViewById(R.id.tab_layout) as TabLayout
        viewPager = mView.findViewById(R.id.view_pager) as ViewPager
        tabLayout.setBackgroundColor(ContextCompat.getColor(activity, R.color.co10_syr))
        tabLayout.setSelectedTabIndicatorHeight(3)
        tabLayout.setTabTextColors(ContextCompat.getColor(activity, R.color.co4_syr), ContextCompat.getColor(activity, R.color.co1_syr))
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(activity, R.color.co1_syr))
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                currentPosition = position
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
        ivClose.setOnClickListener { this.dismiss() }
        var currentStr = "请选择"
        datas!!
                .filter { it.arrorDirection }
                .forEach { currentStr = it.name!! }
        titleList.add(currentStr)
        fragments.add(NewFilterFragment(datas!!, this))
        adapter = FragmentAdapter(childFragmentManager, fragments, titleList)
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
        viewPager.currentItem = currentPosition!!
        return mView
    }

    override fun itemClick(position: Int, menuDatas: ArrayList<MenuItem>) {
        /**
         * 还有下一节点，继续添加新界面,没有下一节点，调用完成方法
         */
        if (menuDatas!![position].data != null && menuDatas!![position].data!!.size > 0) {
            titleList[currentPosition!!] = menuDatas[position].name!!
            var menuItem = MenuItem(menuDatas[position].id, menuDatas[position].name)
            if (selectedDatas.size >= currentPosition!!) {
                selectedDatas.add(menuItem)
            } else {
                selectedDatas[currentPosition!!] = menuItem
            }
            //用于返回上级后的点击列表处理
            var i = titleList.size - 1
            while (i > currentPosition!!) {
                titleList.removeAt(i)
                fragments.removeAt(i)
                selectedDatas.removeAt(i)
                i--
            }
            titleList.add("请选择")
            currentPosition = currentPosition!! + 1
            for (data in menuDatas!![position].data!!) {
                data.arrorDirection = false
            }
            fragments.add(NewFilterFragment(menuDatas!![position].data!!, this))
            if (adapter == null) {
                adapter = FragmentAdapter(childFragmentManager, fragments, titleList)
                viewPager.adapter = adapter
            } else {
                adapter.updateFragments(fragments)
                adapter.updateTitles(titleList)
            }
            tabLayout.setupWithViewPager(viewPager)
            viewPager.currentItem = currentPosition!!
        } else {
            titleList[currentPosition!!] = menuDatas!![position].name!!
            var menuItem = MenuItem(menuDatas[position].id, menuDatas[position].name)
            if (selectedDatas.size >= currentPosition!!) {
                selectedDatas.add(menuItem)
            } else {
                selectedDatas[currentPosition!!] = menuItem
            }
            if (adapter == null) {
                adapter = FragmentAdapter(childFragmentManager, fragments, titleList)
                viewPager.adapter = adapter
            } else {
                adapter.updateFragments(fragments)
                adapter.updateTitles(titleList)
            }
            tabLayout.setupWithViewPager(viewPager)
            viewPager.currentItem = currentPosition!!
            lisenter!!.complete(selectedDatas)
            this.dismiss()
        }
    }

    override fun onResume() {
        val params = dialog.window!!.attributes
        params.width = LayoutParams.MATCH_PARENT
        params.height = LayoutParams.WRAP_CONTENT
        dialog.window!!.attributes = params
        super.onResume()
    }

    interface FilterLisenter {
        fun complete(data: ArrayList<MenuItem>)
    }
}
