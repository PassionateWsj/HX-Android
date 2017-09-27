package com.intfocus.hx.dashboard.mine.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.intfocus.hx.R
import com.intfocus.hx.dashboard.mine.bean.NoticeMenuBean

/**
 * Created by CANC on 2017/7/24.
 */
class NoticeMenuAdapter(val context: Context,
                        private var noticeMenuDatas: List<NoticeMenuBean>?,
                        var listener: NoticeItemListener) : RecyclerView.Adapter<NoticeMenuAdapter.NoticeMenuHolder>() {

    var inflater = LayoutInflater.from(context)

    fun setData(data: List<NoticeMenuBean>?) {
        this.noticeMenuDatas = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeMenuHolder {
        val contentView = inflater.inflate(R.layout.item_notice_menu, parent, false)
        return NoticeMenuHolder(contentView)
    }

    override fun onBindViewHolder(holder: NoticeMenuHolder, position: Int) {
        if (noticeMenuDatas != null) {
            holder.tvNoticeMenuName.text = getTypeStr(noticeMenuDatas!![position].code)
            holder.tvNoticeMenuName.setOnClickListener { listener.menuClick(noticeMenuDatas!![position]) }
            if (noticeMenuDatas!![position].isSelected) {
                holder.tvNoticeMenuName.setBackgroundResource(R.drawable.background_light_blue_radius_border_dark_blue)
                holder.tvNoticeMenuName.setTextColor(ContextCompat.getColor(context, R.color.color15))
            } else {
                holder.tvNoticeMenuName.setBackgroundResource(R.drawable.background_white_border_gray)
                holder.tvNoticeMenuName.setTextColor(ContextCompat.getColor(context, R.color.color3))
            }
        }
    }

    override fun getItemCount(): Int {
        return if (noticeMenuDatas == null) 0 else noticeMenuDatas!!.size
    }

    class NoticeMenuHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvNoticeMenuName = itemView.findViewById(R.id.tv_notice_menu_name) as TextView
    }

    interface NoticeItemListener {
        fun menuClick(noticeMenuBean: NoticeMenuBean)
    }

    fun getTypeStr(type: Int): String {
        when (type) {
            0 -> return "系统公告"
            1 -> return "业务公告"
            2 -> return "预警体系"
            3 -> return "报表评论"
        }
        return "未知公告"
    }
}
