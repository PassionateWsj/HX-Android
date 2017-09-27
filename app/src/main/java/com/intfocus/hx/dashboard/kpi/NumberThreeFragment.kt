package com.intfocus.hx.dashboard.kpi

import android.app.Fragment
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.intfocus.hx.R
import com.intfocus.hx.constant.Colors
import com.intfocus.hx.dashboard.kpi.bean.KpiGroupItem
import kotlinx.android.synthetic.main.fragment_number_three.*
import java.text.DecimalFormat

/**
 * Created by liuruilin on 2017/6/21.
 */
class NumberThreeFragment : Fragment() {
    var datas: KpiGroupItem? = null
    private var rootView: View? = null
    internal var df = DecimalFormat("#.00")
    private val colors = Colors.colorsRGY

    companion object {
        fun newInstance(datas: KpiGroupItem): NumberThreeFragment {
            val fragment = NumberThreeFragment()
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
        rootView = inflater!!.inflate(R.layout.fragment_number_three, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Handler().postDelayed({ init() }, 100)
        super.onActivityCreated(savedInstanceState)
    }

    fun init() {
        if (rootView != null) {
            tv_number_three_title.text = datas!!.title
            var number = datas!!.data!!.high_light!!.number
            tv_number_three_main.text = formatNumber(number)
            tv_number_three_unit.text = datas!!.unit
            tv_number_three_compare.text = datas!!.data!!.high_light!!.compare
            tv_number_three_sub.text = datas!!.memo1
//            rl_number_three_compare.setBackgroundColor(Colors.colorsRGY[datas!!.data!!.high_light!!.arrow])
//            rl_kpi_number_three.setOnClickListener {
//                EventBus.getDefault().post(datas)
//            }
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
