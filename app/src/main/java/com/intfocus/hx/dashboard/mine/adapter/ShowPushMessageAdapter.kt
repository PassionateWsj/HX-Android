package com.intfocus.hx.dashboard.mine.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.intfocus.hx.R
import com.intfocus.hx.dashboard.mine.bean.PushMessageBean
import com.zbl.lib.baseframe.utils.ToastUtil

/**
 * ****************************************************
 * author: JamesWong
 * created on: 17/08/01 下午3:38
 * e-mail: PassionateWsj@outlook.com
 * name: 推送消息适配器
 * desc: 显示当前用户推送消息的适配器
 * ****************************************************
 */
class ShowPushMessageAdapter(val mContext: Context, val listener: OnPushMessageListener) : RecyclerView.Adapter<ShowPushMessageAdapter.MessageHolder>() {
    /**
     * 推送消息 bean 集合
     */
    var mData = mutableListOf<PushMessageBean>()

    override fun onBindViewHolder(holder: MessageHolder?, position: Int) {
        // 加载数据
        if (mData.size > 0) {

            holder!!.tvPushMsgTitle.text = mData[position].title
            holder.tvPushMsgContent.text = mData[position].body_text
            holder.tvPushMsgTime.text = mData[position].debug_timestamp.substring(0,19)

            holder.llPushMsgItem.setOnClickListener {
                listener.onItemClick(position)
            }
        } else {
            ToastUtil.showToast(mContext, "暂无消息")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MessageHolder {
        return MessageHolder(LayoutInflater.from(mContext).inflate(R.layout.item_push_msg_list, parent, false))
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun setData(data: List<PushMessageBean>) {
        mData.clear()
        mData.addAll(data)
        notifyDataSetChanged()
    }

    class MessageHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val llPushMsgItem = itemView!!.findViewById(R.id.ll_push_msg_item) as LinearLayout
        val tvPushMsgTitle = itemView!!.findViewById(R.id.tv_push_msg_title) as TextView
        val tvPushMsgContent = itemView!!.findViewById(R.id.tv_push_msg_content) as TextView
        val tvPushMsgTime = itemView!!.findViewById(R.id.tv_push_msg_time) as TextView
    }

    interface OnPushMessageListener {
        fun onItemClick(position: Int)
    }
}
