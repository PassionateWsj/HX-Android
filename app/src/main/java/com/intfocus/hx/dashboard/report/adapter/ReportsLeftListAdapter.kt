package com.intfocus.hx.dashboard.report.adapter

import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.intfocus.hx.R
import com.intfocus.hx.dashboard.report.mode.CategoryBean

/**
 * Created by Liurl on 2017/6/17.
 */
class ReportsLeftListAdapter(var ctx: android.content.Context,
                             var datas: List<CategoryBean>?,
                             var listener: ReportsLeftListAdapter.ReportLeftListListener) : BaseAdapter() {

    var mInflater: LayoutInflater = from(ctx)
    var currentPosition = 0

    override fun getCount(): Int {
        return datas!!.size
    }

    override fun getItem(position: Int): Any {
        return datas!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, v: View?, parent: ViewGroup): View {
        var convertView = v
        val viewTag: ReportsLeftListAdapter.ItemViewTag

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_reports_left_lv, null)

            viewTag = ItemViewTag(convertView!!.findViewById(R.id.rl_reports_category_item) as LinearLayout,
                    convertView.findViewById(R.id.tv_reports_category_name) as TextView,
                    convertView.findViewById(R.id.iv_reports_left_sign) as ImageView
            )
            convertView.tag = viewTag
        } else {
            viewTag = convertView.tag as ReportsLeftListAdapter.ItemViewTag
        }

        viewTag.tvCategoryName.text = datas!![position].category
        if (position == currentPosition) {
            viewTag.ivCategorySign.visibility = View.VISIBLE
            viewTag.rlCategory.setBackgroundColor(ContextCompat.getColor(ctx, R.color.color10))
            viewTag.tvCategoryName.setTextColor(ContextCompat.getColor(ctx, R.color.co1_syr))
            viewTag.tvCategoryName.paint.isFakeBoldText = true
        } else {
            viewTag.ivCategorySign.visibility = View.INVISIBLE
            viewTag.rlCategory.setBackgroundColor(ContextCompat.getColor(ctx, R.color.color8))
            viewTag.tvCategoryName.setTextColor(ContextCompat.getColor(ctx, R.color.col_report_left_text))
            viewTag.tvCategoryName.paint.isFakeBoldText = false
        }
        viewTag.rlCategory.setOnClickListener { listener.reportLeftItemClick(viewTag.ivCategorySign, position) }

        return convertView
    }

    inner class ItemViewTag(var rlCategory: LinearLayout,
                            var tvCategoryName: TextView,
                            var ivCategorySign: ImageView)

    interface ReportLeftListListener {
        fun reportLeftItemClick(sign: ImageView, position: Int)
    }

    fun refreshListItemState(position: Int) {
        currentPosition = position
        notifyDataSetInvalidated()
    }
}
