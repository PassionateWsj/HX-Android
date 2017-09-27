package com.intfocus.hx.dashboard.kpi.adapter

import android.app.Fragment
import android.app.FragmentManager
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.intfocus.hx.R
import com.intfocus.hx.dashboard.kpi.NumberThreeFragment
import com.intfocus.hx.dashboard.kpi.NumberTwoFragment
import com.intfocus.hx.dashboard.kpi.bean.KpiGroupItem
import com.zbl.lib.baseframe.utils.PhoneUtil

/**
 * Created by liuruilin on 2017/6/22.
 */

class KpiGroupItemAdapter(var ctx: Context, internal var fm: FragmentManager, internal var itemDatas: List<KpiGroupItem>?) : RecyclerView.Adapter<KpiGroupItemAdapter.KpiGroupItemHolder>() {
    internal var viewWidth: Int = 0
    internal var viewHeight: Int = 0
    internal var view2width: Int = 0
    var inflater = LayoutInflater.from(ctx)

    init {
        val sw = PhoneUtil.getScreenWidth(ctx)
        view2width = PhoneUtil.dip2px(ctx, 350.toFloat())
        viewWidth = sw/2
        viewHeight = (viewWidth * 0.8).toInt()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KpiGroupItemHolder {
        val contentView = inflater.inflate(R.layout.item_kpi_group, parent, false)
//        val layout = FrameLayout(parent.context)
//        layout.id = View.generateViewId()
        val holder = KpiGroupItemHolder(contentView, viewType)
        return holder
    }

    override fun onBindViewHolder(holder: KpiGroupItemHolder, position: Int) {
        var lparams: FrameLayout.LayoutParams
        when (holder.datas.dashboard_type) {
            "number2" -> lparams = FrameLayout.LayoutParams(viewWidth, (viewHeight * 0.7).toInt())
            "number3" -> lparams = FrameLayout.LayoutParams(view2width, (viewHeight * 0.4).toInt())
            else -> lparams = FrameLayout.LayoutParams(viewWidth, viewHeight)
        }
        holder.layout.layoutParams = lparams
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        if (itemDatas == null)
            return 0
        return itemDatas!!.size
    }

    inner class KpiGroupItemHolder(view: View, viewType: Int) : RecyclerView.ViewHolder(view) {
        var layout: FrameLayout = view as FrameLayout
        var datas: KpiGroupItem = itemDatas!![viewType]

        init {
            var fragment: Fragment
            when (datas.dashboard_type) {
                "number2" -> fragment = NumberTwoFragment.newInstance(datas)
                "number3" -> fragment = NumberThreeFragment.newInstance(datas)
                else -> fragment = NumberThreeFragment.newInstance(datas)
            }
            val ft = fm.beginTransaction()
            ft.replace(view.id, fragment)
            ft.commitAllowingStateLoss()
        }
    }
}
