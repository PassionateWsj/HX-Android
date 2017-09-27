package com.intfocus.hx.filter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import com.intfocus.hx.R
import com.intfocus.hx.data.response.filter.MenuItem
import com.intfocus.hx.data.response.filter.MenuResult
import com.intfocus.hx.net.ApiException
import com.intfocus.hx.net.CodeHandledSubscriber
import com.intfocus.hx.net.RetrofitUtil
import com.intfocus.hx.util.LogUtil
import com.intfocus.hx.util.ToastUtils
import kotlinx.android.synthetic.main.dialog_fragment_filter.*

/**
 * Created by CANC on 2017/8/8.
 */
class NewFilterActivity : FragmentActivity(), NewFilterFragment.NewFilterFragmentListener {

    lateinit var mActivuty: FragmentActivity

    lateinit var adapter: FragmentAdapter
    var titleList = ArrayList<String>()
    var currentTitleList = ArrayList<String>()
    var fragments = ArrayList<Fragment>()
    var currentFragments = ArrayList<Fragment>()
    lateinit var locationDatas: ArrayList<MenuItem>
    var currentPosition: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_fragment_filter)
        mActivuty = this
        currentPosition = 0
        initView()
        getData()
    }

    fun getData() {
        RetrofitUtil.getHttpService(applicationContext).filterMenu
                .compose(RetrofitUtil.CommonOptions<MenuResult>())
                .subscribe(object : CodeHandledSubscriber<MenuResult>() {
                    override fun onError(apiException: ApiException?) {
                        ToastUtils.show(mActivuty, apiException!!.displayMessage)
                    }

                    override fun onCompleted() {
                    }

                    override fun onBusinessNext(data: MenuResult?) {
                        for (menu in data!!.data) {
                            if ("location".equals(menu.type)) {
                                locationDatas = menu.data!!
                            }
                        }
                        initData()
                    }
                })
    }

    fun initData() {
        titleList.add("请选择")
        currentTitleList.add("请选择")
        fragments.add(NewFilterFragment(locationDatas, this))
        currentFragments.add(NewFilterFragment(locationDatas, this))
        adapter = FragmentAdapter(supportFragmentManager, fragments, titleList)
        view_pager.adapter = adapter
        tab_layout.setupWithViewPager(view_pager)
        view_pager.currentItem = currentPosition!!
    }


    fun initView() {
        tab_layout.setBackgroundColor(ContextCompat.getColor(mActivuty, R.color.co10_syr))
        tab_layout.setSelectedTabIndicatorHeight(3)
        tab_layout.setTabTextColors(ContextCompat.getColor(mActivuty, R.color.co4_syr), ContextCompat.getColor(mActivuty, R.color.co1_syr))
        tab_layout.setSelectedTabIndicatorColor(ContextCompat.getColor(mActivuty, R.color.co1_syr))
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                currentPosition = position
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })

    }

    override fun itemClick(position: Int, datas: ArrayList<MenuItem>) {
        LogUtil.d("itemClick", position.toString())
        if (datas[position].data != null) {
            titleList[currentPosition!!] = datas[position].name!!
            //用于返回上级后的点击列表处理
            currentTitleList.clear()
            currentFragments.clear()
            for (i in 0..currentPosition!!) {
                currentTitleList.add(titleList[i])
                currentFragments.add(fragments[i])
            }
            var i = titleList.size - 1
            while (i > currentPosition!!) {
                titleList.removeAt(i)
                fragments.removeAt(i)
                i--
            }
            titleList.add("请选择")
            if (datas[position].data!! != null && datas[position].data!!.size > 0) {
                currentPosition = currentPosition!! + 1
                for (data in datas[position].data!!) {
                    data.arrorDirection = false
                }
                fragments.add(NewFilterFragment(datas[position].data!!, this))
                if (adapter == null) {
                    adapter = FragmentAdapter(supportFragmentManager, fragments, titleList)
                    view_pager.adapter = adapter
                } else {
                    adapter.updateFragments(fragments)
                    adapter.updateTitles(titleList)
                }
                tab_layout.setupWithViewPager(view_pager)
                view_pager.currentItem = currentPosition!!
            }
        } else {
            titleList[currentPosition!!] = datas[position].name!!
            if (adapter == null) {
                adapter = FragmentAdapter(supportFragmentManager, fragments, titleList)
                view_pager.adapter = adapter
            } else {
                adapter.updateFragments(fragments)
                adapter.updateTitles(titleList)
            }
            tab_layout.setupWithViewPager(view_pager)
            view_pager.currentItem = currentPosition!!
            ToastUtils.show(mActivuty, "没有下一级别")
        }
    }
}
