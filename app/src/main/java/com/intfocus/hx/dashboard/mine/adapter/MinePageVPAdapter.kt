package com.intfocus.hx.dashboard.mine.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by liuruilin on 2017/6/6.
 */
class MinePageVPAdapter(fragmentManager: FragmentManager, val mFragmentList: ArrayList<Fragment>, val titleList: ArrayList<String>) : FragmentPagerAdapter(fragmentManager) {
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

    override fun getPageTitle(position: Int): CharSequence {
        return titleList[position]
    }
}
