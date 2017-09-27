package com.intfocus.hx.dashboard.kpi.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.ViewGroup

/**
 * Created by liuruilin on 2017/6/23.
 */
class KpiStickAdapter(fragmentManager: FragmentManager, val mFragmentList: MutableList<Fragment>): FragmentStatePagerAdapter(fragmentManager) {

    var mCurrentFragment: Fragment? = null


    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun switchTo(position: Int) {
        mCurrentFragment = mFragmentList[position]
    }

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        return super.instantiateItem(container, position)
    }
}
