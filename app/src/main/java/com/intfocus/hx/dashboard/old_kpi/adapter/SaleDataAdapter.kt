package com.intfocus.hx.dashboard.old_kpi.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

import com.intfocus.hx.dashboard.kpi.bean.MererEntity
import com.intfocus.hx.dashboard.old_kpi.BarFragment_Top
import com.intfocus.hx.dashboard.old_kpi.BrokenLineFragment
import com.intfocus.hx.dashboard.old_kpi.NumberFragment
import com.intfocus.hx.dashboard.old_kpi.RingFragment
import com.zbl.lib.baseframe.utils.PhoneUtil

import java.util.ArrayList
import java.util.Random

/**
 * 销售数据适配器
 * Created by zbaoliang on 17-4-28. nj
 */
class SaleDataAdapter(ctx: Context, internal var fm: FragmentManager, internal var bodyDatas: ArrayList<MererEntity>?) : RecyclerView.Adapter<SaleDataAdapter.SaleDataHolder>() {

    internal var ctx: Context? = null

    internal var viewWidth: Int = 0
    internal var viewHeigt: Int = 0

    init {
        val sw = PhoneUtil.getScreenWidth(ctx)
        viewWidth = sw / 2
        viewHeigt = (viewWidth * 0.8).toInt()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaleDataHolder {
        val layout = FrameLayout(parent.context)
        val random = Random()
        val id = random.nextInt(201505)
        layout.id = id
        val holder = SaleDataHolder(layout, viewType)
        return holder
    }

    override fun onBindViewHolder(holder: SaleDataHolder, position: Int) {

        var lparams: ViewGroup.LayoutParams
        when (holder.entity.dashboard_type) {
            "line", "bar" -> lparams = ViewGroup.LayoutParams(viewWidth, viewHeigt * 2)
            "ring" -> lparams = ViewGroup.LayoutParams(viewWidth, viewHeigt)
             else -> lparams = ViewGroup.LayoutParams(viewWidth, viewHeigt)
        }
        holder.layout.layoutParams = lparams
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        if (bodyDatas == null)
            return 0
        return bodyDatas!!.size
    }

    inner class SaleDataHolder(view: View, viewType: Int) : RecyclerView.ViewHolder(view) {

        var layout: FrameLayout
        var entity: MererEntity

        init {
            this.layout = view as FrameLayout
            entity = bodyDatas!![viewType]
            var fragment: Fragment
            when (entity.dashboard_type) {
                "line"//折线图
                -> fragment = BrokenLineFragment.newInstance(entity)

                "bar"//柱状图
                -> fragment = BarFragment_Top.newInstance(entity)

                "ring"//环形图
                -> fragment = RingFragment.newInstance(entity)

                else//纯文本
                -> fragment = NumberFragment.newInstance(entity)
            }
            val ft = fm.beginTransaction()
            ft.replace(view.getId(), fragment)
            ft.commit()
        }
    }
}
