package com.intfocus.hx.dashboard.old_kpi

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.intfocus.hx.R
import com.intfocus.hx.base.BaseModeFragment
import com.intfocus.hx.dashboard.kpi.bean.MererEntity
import com.intfocus.hx.dashboard.kpi.bean.MeterClickEventEntity
import com.intfocus.hx.constant.Colors
import com.zbl.lib.baseframe.core.Subject
import kotlinx.android.synthetic.main.fragment_number.*
import org.greenrobot.eventbus.EventBus
import java.text.DecimalFormat

/**
 * 纯数字文本表
 */
class NumberFragment : BaseModeFragment<Subject>() {
    private var entity: MererEntity? = null
    private val colors = Colors.colorsRGY
    private var rootView: View? = null

    internal var df = DecimalFormat("#.00")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            entity = arguments.getSerializable("Entity") as MererEntity
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if (rootView == null) {
            rootView = inflater!!.inflate(R.layout.fragment_number, container, false)
            Handler().postDelayed({ init() }, 100)
        }
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun init() {
        tv_title_vpitem!!.text = entity!!.title
        val number = entity!!.data!!.high_light!!.number!!
        if (number == 0.123456789) {
            //            tv_title.setTextSize(20);
            tv_number_vpitem!!.visibility = View.GONE
//            ll_vp!!.visibility = View.GONE
            tv_vpitem_unit!!.visibility = View.GONE
        } else {
            tv_number_vpitem!!.text = formatNumber(df.format(number).toString())
        }

        tv_vpitem_unit!!.text = entity!!.unit

//        val high_light = entity!!.data!!.high_light
//        if (high_light!!.compare != 0.0) {//显示百分比
//            val compare = ((high_light.number!! - high_light.compare) / high_light.compare * 100).toFloat()
//            if (high_light.number!! - high_light.compare > 0) {//上箭头
//                tv_compare_vpitem!!.text = "+" + df.format(compare.toDouble()) + "%"
//            } else {
//                tv_compare_vpitem!!.text = "" + df.format(compare.toDouble()) + "%"
//            }
//        }
//
//        if (high_light.arrow >= 0) {
//            img_vpitem!!.visibility = View.VISIBLE
//            tv_compare_vpitem!!.setTextColor(colors[high_light.arrow!!])
//            tv_number_vpitem.setTextColor(colors[high_light.arrow!!])
//        } else {
//            img_vpitem!!.visibility = View.GONE
//            tv_number_vpitem.setTextColor(Color.BLACK)
//        }
//
//        img_vpitem.setCursorState(high_light.arrow!!)

        rootView!!.setOnClickListener { EventBus.getDefault().post(MeterClickEventEntity(entity!!)) }
    }

    companion object {

        fun newInstance(entity: MererEntity): NumberFragment {

            val fragment = NumberFragment()
            val args = Bundle()
            args.putSerializable("Entity", entity)
            fragment.arguments = args
            return fragment
        }
    }
}
