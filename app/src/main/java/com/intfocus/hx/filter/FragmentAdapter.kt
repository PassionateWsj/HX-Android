package com.intfocus.hx.filter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.ViewGroup

/**
 * Created by CANC on 2017/8/9.
 */
class FragmentAdapter(fm: FragmentManager, var mFragments: List<Fragment>?, var mTitles: List<String>?) : FragmentStatePagerAdapter(fm) {

    fun updateTitles(titles: List<String>) {
        mTitles = titles
        this.notifyDataSetChanged()
    }

    fun updateFragments(fragments: List<Fragment>) {
        mFragments = fragments
        this.notifyDataSetChanged()
    }

    override fun getItem(position: Int): Fragment {
        return mFragments!![position]
    }

    override fun setPrimaryItem(container: ViewGroup?, position: Int, `object`: Any) {
        super.setPrimaryItem(container, position, `object`)
    }

    override fun getCount(): Int {
        return mFragments?.size ?: 0
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mTitles!![position]
    }

    override fun getItemPosition(`object`: Any?): Int {
        return POSITION_NONE
    }
}
