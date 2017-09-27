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
import com.intfocus.hx.util.Utils


/**
 * Created by CANC on 2017/8/3.
 */
class FilterMenuAdapter(val context: Context,
                        var menuDatas: List<MenuItem>?,
                        var lisenter: FilterMenuListener) : RecyclerView.Adapter<FilterMenuAdapter.FilterMenuHolder>() {
    var inflater = LayoutInflater.from(context)!!
    fun setData(data: List<MenuItem>?) {
        this.menuDatas = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterMenuHolder {
        val contentView = inflater.inflate(R.layout.item_filter_menu, parent, false)
        return FilterMenuHolder(contentView)
    }

    override fun onBindViewHolder(holder: FilterMenuHolder, position: Int) {
        holder.contentView.setBackgroundResource(R.drawable.recycler_bg)
        if (menuDatas != null) {
            holder.rlFilter.layoutParams.width = Utils.getScreenWidth(context) / 4
            holder.tvFilterName.text = menuDatas!![position].category
            holder.tvFilterName.text = menuDatas!![position].category
            if (menuDatas!![position].arrorDirection!!) {
                holder.ivArrow.setImageResource(R.drawable.ic_arrow_up)
                holder.tvFilterName.setTextColor(ContextCompat.getColor(context, R.color.co1_syr))
            } else {
                holder.ivArrow.setImageResource(R.drawable.ic_arrow_down)
                holder.tvFilterName.setTextColor(ContextCompat.getColor(context, R.color.co3_syr))
            }
            holder.rlFilter.setOnClickListener {
                lisenter.itemClick(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (menuDatas == null) 0 else menuDatas!!.size
    }

    class FilterMenuHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var contentView = itemView
        var rlFilter = itemView.findViewById(R.id.rl_filter) as RelativeLayout
        var tvFilterName = itemView.findViewById(R.id.tv_filter_name) as TextView
        var ivArrow = itemView.findViewById(R.id.iv_arrow) as ImageView
    }

    interface FilterMenuListener {
        fun itemClick(position: Int)
    }
}
