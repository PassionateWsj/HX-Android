package com.intfocus.hx.dashboard.kpi

import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.intfocus.hx.R
import com.intfocus.hx.base.BaseModeFragment
import com.intfocus.hx.dashboard.kpi.bean.KpiGroupItem
import com.zbl.lib.baseframe.core.Subject
import kotlinx.android.synthetic.main.fragment_number_one.*
import org.greenrobot.eventbus.EventBus
import java.text.DecimalFormat


/**
 * Created by liuruilin on 2017/6/23.
 */
class NumberOneFragment : BaseModeFragment<Subject>() {
    var datas: KpiGroupItem? = null
    private var rootView: View? = null
    internal var df = DecimalFormat("#.00")

    companion object {
        fun newInstance(datas: KpiGroupItem): NumberOneFragment {
            val fragment = NumberOneFragment()
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
        rootView = inflater!!.inflate(R.layout.fragment_number_one, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Handler().postDelayed({ init() }, 100)
        super.onActivityCreated(savedInstanceState)
    }

    fun init() {
        val mTypeface = Typeface.createFromAsset(act.assets, "ALTGOT2N.TTF")
        tv_number_one_title.text = datas!!.memo2
        var number = datas!!.data!!.high_light!!.number
        tv_number_one_number.text = formatNumber(number)
        tv_number_one_number.typeface = mTypeface
        tv_number_one_unit.text = "(" + datas!!.unit + ")"
        tv_number_one_sub_title.text = datas!!.memo1
        tv_number_one_sub.text = datas!!.data!!.high_light!!.compare
        rl_kpi_number_one.setOnClickListener {
            EventBus.getDefault().post(datas)
        }
    }
}
