package com.intfocus.hx.dashboard.kpi.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup

/**
 * Created by CANC on 2017/7/28.
 */

class MyViewPagerAdapter(private var views: List<View>, private val context: Context) : PagerAdapter() {
    var isInfiniteLoop = false
    fun setData(datas: List<View>) {
        this.views = views
        this.notifyDataSetChanged()
    }

    //获取真实的position
    fun getRealPosition(position: Int): Int {
        return if (isInfiniteLoop) position % views.size else position
    }

    override fun getCount(): Int {
        if (views == null || views.size == 0) {
            return 0
        } else {
            return if (isInfiniteLoop) Integer.MAX_VALUE else views.size
        }
    }

    override fun isViewFromObject(arg0: View, arg1: Any): Boolean {

        return arg0 === arg1
    }

    //展示的view
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        //获得展示的view
        val view = views[position]
        //添加到容器
        container.addView(view)
        //返回显示的view
        return view
    }

    //销毁view
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        //从容器中移除view
        container.removeView(`object` as View)
    }

}
