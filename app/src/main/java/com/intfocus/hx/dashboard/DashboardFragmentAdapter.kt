package com.intfocus.hx.dashboard

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup
import com.intfocus.hx.dashboard.kpi.HomeFragment

import com.intfocus.hx.dashboard.mine.UserFragment
import com.intfocus.hx.dashboard.report.ReportFragment
import com.intfocus.hx.dashboard.work_box.WorkBoxFragment

/**
 * Created by liuruilin on 2017/3/23.
 */

class DashboardFragmentAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    private val PAGER_COUNT = 4
    private var mMeterFragment = WorkBoxFragment()
    private var mAnalysisFragment = WorkBoxFragment()
    private var mAppFragment = WorkBoxFragment()
    private var mMessageFragment = WorkBoxFragment()

    override fun getCount(): Int {
        return PAGER_COUNT
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
    }

    override fun getItem(position: Int): Fragment {
        when (position) {
            DashboardActivity.PAGE_KPI -> return mMeterFragment
            DashboardActivity.PAGE_REPORTS -> return mAnalysisFragment
            DashboardActivity.PAGE_APP -> return mAppFragment
            DashboardActivity.PAGE_MINE -> return mMessageFragment
        }
        return mMeterFragment
    }
}
