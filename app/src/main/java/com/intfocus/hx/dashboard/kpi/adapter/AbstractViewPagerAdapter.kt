package com.intfocus.hx.dashboard.kpi.adapter

import android.support.v4.view.PagerAdapter
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup

/**
 * Created by CANC on 2017/7/28.
 */

abstract class AbstractViewPagerAdapter<T>(protected var mData: List<T>) : PagerAdapter() {
    private val mViews: SparseArray<View> = SparseArray<View>(mData.size)

    override fun getCount(): Int {
        return mData.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var view: View? = mViews.get(position)
        if (view == null) {
            view = newView(position)
            mViews.put(position, view)
        }
        container.addView(view)
        return view
    }

    abstract fun newView(position: Int): View

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(mViews.get(position))
    }

    fun getItem(position: Int): T {
        return mData[position]
    }
}
