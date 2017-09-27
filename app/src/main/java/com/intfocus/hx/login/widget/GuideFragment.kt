package com.intfocus.hx.login.widget

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.intfocus.hx.R
import com.intfocus.hx.login.LoginActivity

/**
 * ****************************************************
 * author: JamesWong
 * created on: 17/08/07 下午1:52
 * e-mail: PassionateWsj@outlook.com
 * name:
 * desc:
 * ****************************************************
 */
class GuideFragment : Fragment() {
    private var mSharedPreferences: SharedPreferences? = null
    val imgPage = intArrayOf(R.drawable.text_gaikuan,
            R.drawable.text_baobiao,
            R.drawable.text_gongju)
    val imgScreenshot = intArrayOf(R.drawable.pic_phone1,
            R.drawable.pic_phone2,
            R.drawable.pic_phone3)


    private lateinit var rlGuideDescribe: RelativeLayout
    private lateinit var ivGuideScreenshot: ImageView
    private lateinit var ivGuideText: ImageView
    private lateinit var llGuidePage: LinearLayout
    private lateinit var btnGuideEnter: Button

    var index: Int = 0
    var totalCount: Int = 0

    fun newInstance(index: Int, totalCount: Int): GuideFragment {
        val args = Bundle()
        args.putInt("index", index)
        args.putInt("totalCount", totalCount)
        val fragment = GuideFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        index = arguments.getInt("index")
        totalCount = arguments.getInt("totalCount")
        mSharedPreferences = activity.getSharedPreferences("SettingPreference", Context.MODE_PRIVATE)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.fragment_guide, container, false)
        rlGuideDescribe = view.findViewById(R.id.rl_guide_describe) as RelativeLayout
        ivGuideScreenshot = view.findViewById(R.id.iv_guide_screenshot) as ImageView
        ivGuideText = view.findViewById(R.id.iv_guide_text) as ImageView
        llGuidePage = view.findViewById(R.id.ll_guide_page) as LinearLayout
        btnGuideEnter = view.findViewById(R.id.btn_guide_enter) as Button
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ivGuideText.setImageDrawable(activity.resources.getDrawable(imgPage[index]))
        ivGuideScreenshot.setImageDrawable(activity.resources.getDrawable(imgScreenshot[index]))
        for (i in 0..llGuidePage.childCount - 1) {
            if (i == index) {
                llGuidePage.getChildAt(i).background = activity.resources.getDrawable(R.drawable.icon_paging1)
            } else {
                llGuidePage.getChildAt(i).background = activity.resources.getDrawable(R.drawable.icon_paging2)
            }
        }
        if (index == totalCount - 1) {
            llGuidePage.visibility = View.GONE
            btnGuideEnter.visibility = View.VISIBLE
        } else {
            llGuidePage.visibility = View.VISIBLE
            btnGuideEnter.visibility = View.GONE
        }
        btnGuideEnter.setOnClickListener { startLoginActivity() }
    }

    private fun startLoginActivity() {
        val packageInfo = activity.packageManager.getPackageInfo(activity.packageName, 0)
        mSharedPreferences!!.edit().putInt("Version", packageInfo.versionCode).commit()
        var intent = Intent(activity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        activity.finish()
    }

}
