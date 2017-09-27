package com.intfocus.hx.dashboard.kpi

import android.app.Fragment
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.intfocus.hx.R
import com.intfocus.hx.constant.Colors
import com.intfocus.hx.dashboard.kpi.bean.KpiGroupItem
import kotlinx.android.synthetic.main.fragment_number_two.*
import org.greenrobot.eventbus.EventBus
import java.text.DecimalFormat

/**
 * Created by liuruilin on 2017/6/21.
 */
class NumberTwoFragment : Fragment() {
    var datas: KpiGroupItem? = null
    private var rootView: View? = null
    internal var df = DecimalFormat("#.00")
    private val colors = Colors.colorsRGY

    companion object {
        fun newInstance(datas: KpiGroupItem): NumberTwoFragment {
            val fragment = NumberTwoFragment()
            val args = Bundle()
            fragment.datas = datas
            args.putSerializable("Datas", datas)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            datas = arguments.getSerializable("Datas") as KpiGroupItem
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater!!.inflate(R.layout.fragment_number_two, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onResume() {
        Handler().postDelayed({ init() }, 100)
        super.onResume()
    }

    fun init() {
        if (rootView != null) {
            val mTypeface = Typeface.createFromAsset(activity.assets, "ALTGOT2N.TTF")
            tv_number_two_title.text = datas!!.title
            var number = datas!!.data!!.high_light!!.number
            tv_number_two_main.text = formatNumber(number)
            tv_number_two_main.setTextColor(colors[datas!!.data!!.high_light!!.arrow])
            tv_number_two_unit.text = datas!!.unit
            tv_number_two_compare.text = datas!!.data!!.high_light!!.compare
            tv_number_two_compare.setTextColor(colors[datas!!.data!!.high_light!!.arrow])
            tv_number_two_sub.text = datas!!.memo1

            ll_number_two_item.setOnClickListener {
                EventBus.getDefault().post(datas)
            }
        }
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
