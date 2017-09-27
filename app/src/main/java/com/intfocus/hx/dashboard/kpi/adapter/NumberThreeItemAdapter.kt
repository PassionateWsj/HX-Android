package com.intfocus.hx.dashboard.kpi.adapter

import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.intfocus.hx.R
import com.intfocus.hx.constant.Colors
import com.intfocus.hx.dashboard.kpi.bean.KpiGroupItem
import com.zbl.lib.baseframe.utils.PhoneUtil
import org.greenrobot.eventbus.EventBus

/**
 * Created by liuruilin on 2017/7/10.
 */
class NumberThreeItemAdapter(var ctx: Context, internal var itemDatas: List<KpiGroupItem>?) : RecyclerView.Adapter<NumberThreeItemAdapter.NumberThreeItemHolder>() {
    internal var viewWidth: Int = 0
    internal var viewHeight: Int = 0
    internal var view2width: Int = 0
    var inflater = LayoutInflater.from(ctx)
    private val colors = Colors.colorsRGY

    init {
        val sw = PhoneUtil.getScreenWidth(ctx)
        view2width = PhoneUtil.dip2px(ctx, 350.toFloat())
        viewWidth = sw / 2
        viewHeight = (viewWidth * 0.8).toInt()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberThreeItemHolder {
        val contentView = inflater.inflate(R.layout.fragment_number_three, parent, false)
        val holder = NumberThreeItemHolder(contentView)
        return holder
    }

    override fun onBindViewHolder(holder: NumberThreeItemHolder, position: Int) {
        holder.tv_number_three_title.text = itemDatas!![position].title
        val mTypeface = Typeface.createFromAsset(ctx.assets, "ALTGOT2N.TTF")
        var number = itemDatas!![position].data!!.high_light!!.number
        holder.tv_number_three_number.text = formatNumber(number)
        holder.tv_number_three_unit.text = itemDatas!![position].unit
        holder.tv_number_three_compare.text = itemDatas!![position].data!!.high_light!!.compare
        holder.tv_number_three_compare.typeface = mTypeface
        holder.tv_number_three_compare.setTextColor(colors[itemDatas!![position].data!!.high_light!!.arrow])
        holder.tv_number_three_sub.text = itemDatas!![position].memo1
        holder.tv_number_three_compare_text.text = itemDatas!![position].memo2
        holder.ll_number_three_item.setOnClickListener {
            EventBus.getDefault().post(itemDatas!![position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        if (itemDatas == null)
            return 0
        return itemDatas!!.size
    }

    inner class NumberThreeItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv_number_three_title = view.findViewById(R.id.tv_number_three_title) as TextView
        var tv_number_three_number = view.findViewById(R.id.tv_number_three_main) as TextView
        var tv_number_three_unit = view.findViewById(R.id.tv_number_three_unit) as TextView
        var tv_number_three_compare = view.findViewById(R.id.tv_number_three_compare) as TextView
        var tv_number_three_sub = view.findViewById(R.id.tv_number_three_sub) as TextView
        var tv_number_three_compare_text = view.findViewById(R.id.tv_number_three_compare_name) as TextView
        var ll_number_three_item = view.findViewById(R.id.ll_kpi_number_three) as LinearLayout
    }

    fun formatNumber(number: String): String {
        var number = number
        if (number.contains("")) {
            number = number.replace("0+?$".toRegex(), "")//去掉多余的0
            number = number.replace("[.]$".toRegex(), "")//如最后一位是.则去掉
        }
        return number
    }
}
