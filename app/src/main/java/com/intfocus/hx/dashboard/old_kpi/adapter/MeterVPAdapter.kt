package com.intfocus.hx.dashboard.old_kpi.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter

import com.intfocus.hx.dashboard.kpi.bean.MererEntity
import com.intfocus.hx.dashboard.old_kpi.BarFragment_Bottom
import com.intfocus.hx.dashboard.old_kpi.CurveLineFragment
import com.intfocus.hx.dashboard.old_kpi.NumberFragment
import com.intfocus.hx.dashboard.old_kpi.RingFragment

import java.util.ArrayList

/**
 * 实时数据适配器
 * Created by zbaoliang on 17-4-28.
 */
class MeterVPAdapter(internal var ctx: Context, fm: FragmentManager, internal var topDatas: ArrayList<MererEntity>?) : FragmentStatePagerAdapter(fm) {

    override fun getCount(): Int {
        if (topDatas == null)
            return 0
        else
            return topDatas!!.size
    }

    override fun getItem(position: Int): Fragment? {
        val entity = topDatas!![position]
        var fragment: Fragment
        when (entity.dashboard_type) {
            "line"//折线图
            -> fragment = CurveLineFragment.newInstance(entity)

            "bar"//柱状图
            -> fragment = BarFragment_Bottom.newInstance(entity)

            "ring"//环形图
            -> fragment = RingFragment.newInstance(entity)

            else//纯文本
            -> fragment = NumberFragment.newInstance(entity)
        }

        return fragment
    }

    override fun getItemPosition(`object`: Any?): Int {
        // 返回发生改变，让系统重新加载（强制刷新）
        // 系统默认返回的是     POSITION_UNCHANGED，未改变
        return PagerAdapter.POSITION_NONE
    }

}

