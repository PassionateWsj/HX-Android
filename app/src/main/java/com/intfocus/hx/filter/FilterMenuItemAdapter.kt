package com.intfocus.hx.dashboard.mine.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.intfocus.hx.R
import com.intfocus.hx.data.response.filter.MenuItem


/**
 * Created by CANC on 2017/8/3.
 */
class FilterMenuItemAdapter(val context: Context,
                            var menuDatas: List<MenuItem>?,
                            var lisenter: FilterMenuItemListener) : RecyclerView.Adapter<FilterMenuItemAdapter.FilterMenuItemHolder>() {
    var inflater = LayoutInflater.from(context)!!
    fun setData(data: List<MenuItem>?) {
        this.menuDatas = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterMenuItemHolder {
        val contentView = inflater.inflate(R.layout.item_filter_menu_item, parent, false)
        return FilterMenuItemHolder(contentView)
    }

    override fun onBindViewHolder(holder: FilterMenuItemHolder, position: Int) {
        holder.contentView.setBackgroundResource(R.drawable.recycler_bg)
        if (menuDatas != null) {
            holder.tvFilterName.text = menuDatas!![position].name
            if (menuDatas!![position].arrorDirection!!) {
                holder.ivFilter.visibility = View.VISIBLE
                holder.tvFilterName.setTextColor(ContextCompat.getColor(context, R.color.co1_syr))
            } else {
                holder.ivFilter.visibility = View.GONE
                holder.tvFilterName.setTextColor(ContextCompat.getColor(context, R.color.co3_syr))
            }
            holder.rlFilter.setOnClickListener {
                lisenter.menuItemClick(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (menuDatas == null) 0 else menuDatas!!.size
    }

    class FilterMenuItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var contentView = itemView
        var rlFilter = itemView.findViewById(R.id.rl_filter) as RelativeLayout
        var tvFilterName = itemView.findViewById(R.id.tv_filter_name) as TextView
        var ivFilter = itemView.findViewById(R.id.iv_filter) as ImageView
    }

    interface FilterMenuItemListener {
        fun menuItemClick(position: Int)
    }
}
