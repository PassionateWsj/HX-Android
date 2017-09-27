package com.intfocus.hx.subject.table

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.intfocus.hx.R
import com.intfocus.hx.subject.table.bean.Head
import com.intfocus.hx.subject.table.bean.MainData
import com.intfocus.hx.util.Utils

import android.text.TextUtils.TruncateAt.MIDDLE

/**
 * Created by CANC on 2017/4/6.
 */

class TableContentItemAdapter(private val context: Context, private var heads: List<Head>?, private var mainData: List<MainData>?, private var rowHeight: Int//1行,2行,3行
                              , var listener: TableContentItemAdapter.ContentItemListener) : RecyclerView.Adapter<TableContentItemAdapter.TableHeadHolder>() {
    var inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableContentItemAdapter.TableHeadHolder {
        val contentView = inflater.inflate(R.layout.item_table_main_item, parent, false)
        return TableContentItemAdapter.TableHeadHolder(contentView)
    }

    fun setData(heads: List<Head>, mainData: List<MainData>, rowHeight: Int) {
        this.heads = heads
        this.mainData = mainData
        this.rowHeight = rowHeight
        this.notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: TableContentItemAdapter.TableHeadHolder, position: Int) {
        if (!heads!![position].isShow || heads!![position].isKeyColumn) {
            holder.tvMain.visibility = View.GONE
        } else {
            holder.tvMain.text = mainData!![position].value
            holder.tvMain.ellipsize = MIDDLE
            holder.tvMain.maxLines = rowHeight
            holder.tvMain.visibility = View.VISIBLE
            holder.tvMain.layoutParams.height = Utils.dpToPx(context, (25 * rowHeight).toFloat())
            holder.tvMain.setOnClickListener { listener.itemClick(position) }
        }
    }

    override fun getItemCount(): Int {
        return if (mainData == null) 0 else mainData!!.size
    }

    class TableHeadHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvMain = itemView.findViewById(R.id.tv_main) as TextView
    }

    interface ContentItemListener {
    fun itemClick(position: Int)
}
}
