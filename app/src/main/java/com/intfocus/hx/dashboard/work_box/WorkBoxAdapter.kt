package com.intfocus.hx.dashboard.work_box

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.intfocus.hx.R
import com.intfocus.hx.bean.DashboardItemBean
import com.zbl.lib.baseframe.utils.PhoneUtil
import org.greenrobot.eventbus.EventBus
import org.xutils.x

/**
 * Created by liuruilin on 2017/7/28.
 */
class WorkBoxAdapter(var ctx: Context, var datas: List<WorkBoxItem>?) : BaseAdapter() {
    var mInflater: LayoutInflater = LayoutInflater.from(ctx)
    var laryoutParams = AbsListView.LayoutParams(PhoneUtil.getScreenWidth(ctx) / 3, PhoneUtil.getScreenWidth(ctx) / 3)

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
        val viewTag: WorkBoxAdapter.ItemViewTag

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_work_box, null)

            // construct an item tag
            viewTag = ItemViewTag(convertView!!.findViewById(R.id.ll_work_box_item) as RelativeLayout,
                    convertView.findViewById(R.id.iv_work_box_item_img) as ImageView,
                    convertView.findViewById(R.id.tv_work_box_item_name) as TextView)
            convertView.tag = viewTag

            if (convertView.layoutParams == null)
                convertView.layoutParams = laryoutParams
            else
                convertView.layoutParams.height = PhoneUtil.getScreenWidth(ctx) / 3
                convertView.layoutParams.width = PhoneUtil.getScreenWidth(ctx) / 3

        } else {
                viewTag = convertView.tag as WorkBoxAdapter.ItemViewTag
        }

        viewTag.mName.text = datas!![position].name
        x.image().bind(viewTag.mIcon, datas!![position].icon_link)
        viewTag.rlItem.setOnClickListener {
            EventBus.getDefault().post(DashboardItemBean(datas!![position].link_path!!, datas!![position].name!!))
        }

        return convertView
    }

    internal inner class ItemViewTag(var rlItem: RelativeLayout, var mIcon: ImageView, var mName: TextView)
}
