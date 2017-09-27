package com.intfocus.hx.dashboard.mine.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.intfocus.hx.R
import com.intfocus.hx.subject.template_v2.entity.MDetalUnitEntity

/**
 * Created by CANC on 2017/7/24.
 */
class TableTitleAdapter(val context: Context,
                        private var datas: List<MDetalUnitEntity>?,
                        var listener: NoticeItemListener) : RecyclerView.Adapter<TableTitleAdapter.NoticeMenuHolder>() {

    var inflater = LayoutInflater.from(context)

    fun setData(data: List<MDetalUnitEntity>?) {
        this.datas = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeMenuHolder {
        val contentView = inflater.inflate(R.layout.item_table_title, parent, false)
        return NoticeMenuHolder(contentView)
    }

    override fun onBindViewHolder(holder: NoticeMenuHolder, position: Int) {
        if (datas != null) {
            var noticeMenuData = datas!![position]
            holder.tvText.text = noticeMenuData.type
            if (noticeMenuData.isCheck) {
                holder.viewLine.visibility = View.VISIBLE
            } else {
                holder.viewLine.visibility = View.INVISIBLE
            }
            holder.llTitle.setOnClickListener { listener.itemClick(position) }
        }
    }

    override fun getItemCount(): Int {
        return if (datas == null) 0 else datas!!.size
    }

    class NoticeMenuHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var llTitle = itemView.findViewById(R.id.ll_title) as LinearLayout
        var tvText = itemView.findViewById(R.id.tv_text) as TextView
        var viewLine = itemView.findViewById(R.id.view_line)
    }

    interface NoticeItemListener {
        fun itemClick(position: Int)
    }

}
