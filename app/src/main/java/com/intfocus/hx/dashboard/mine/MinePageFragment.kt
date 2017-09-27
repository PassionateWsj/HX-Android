package com.intfocus.hx.dashboard.mine

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.intfocus.hx.R
import com.intfocus.hx.base.BaseFragment
import com.intfocus.hx.dashboard.mine.adapter.MinePageVPAdapter
import com.intfocus.hx.util.Utils
import kotlinx.android.synthetic.main.fragment_mine.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

class MinePageFragment : BaseFragment(), ViewPager.OnPageChangeListener {
    lateinit var mViewPagerAdapter: MinePageVPAdapter
    var fragmentList = ArrayList<Fragment>()
    val titleList = ArrayList<String>()
    val FIRST_PAGE_INDEX: Int = 0

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_mine, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViewPager()
    }

    fun initViewPager() {
        fragmentList.add(NoticeFragment())
        fragmentList.add(DataCollegeFragment())
        fragmentList.add(UserFragment())

        titleList.add("公告预警")
        titleList.add("数据学院")
        titleList.add("个人信息")

        mViewPagerAdapter = MinePageVPAdapter(childFragmentManager, fragmentList, titleList)
        mViewPagerAdapter.switchTo(FIRST_PAGE_INDEX)

        vp_message.adapter = mViewPagerAdapter
        vp_message.addOnPageChangeListener(this)
        vp_message.currentItem = 0
        vp_message.offscreenPageLimit = 2
        tab_layout.setSelectedTabIndicatorColor(ContextCompat.getColor(activity, R.color.color11))
        tab_layout.setTabTextColors(ContextCompat.getColor(activity, R.color.color4), ContextCompat.getColor(activity, R.color.color6))
        tab_layout.setBackgroundColor(ContextCompat.getColor(activity, R.color.color10))
        tab_layout.setupWithViewPager(vp_message)
        tab_layout.post({ Utils.setIndicator(tab_layout, 25, 25) })
        vp_message.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    line_orange.visibility=View.VISIBLE
                    line_green.visibility=View.INVISIBLE
                    line_blue.visibility=View.INVISIBLE
//                    tab_layout.setSelectedTabIndicatorColor(ContextCompat.getColor(activity, R.color.color11))
                } else if (position == 1) {
                    line_orange.visibility=View.INVISIBLE
                    line_green.visibility=View.VISIBLE
                    line_blue.visibility=View.INVISIBLE
//                    tab_layout.setSelectedTabIndicatorColor(ContextCompat.getColor(activity, R.color.color2))
                } else {
                    line_orange.visibility=View.INVISIBLE
                    line_green.visibility=View.INVISIBLE
                    line_blue.visibility=View.VISIBLE
//                    tab_layout.setSelectedTabIndicatorColor(ContextCompat.getColor(activity, R.color.color1))
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    //重写ViewPager页面切换的处理方法
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {
        mViewPagerAdapter.switchTo(position)
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun setTabUpdatedState() {

    }
}

