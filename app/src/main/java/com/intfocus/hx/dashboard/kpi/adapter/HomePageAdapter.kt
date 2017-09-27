package com.intfocus.hx.dashboard.mine.adapter

import android.content.Context
import android.graphics.Typeface
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.LinearLayout
import android.widget.TextView
import com.intfocus.hx.R
import com.intfocus.hx.bean.DashboardItemBean
import com.intfocus.hx.dashboard.kpi.adapter.MyViewPagerAdapter
import com.intfocus.hx.dashboard.kpi.bean.HomeBean
import com.intfocus.hx.dashboard.kpi.bean.NoticeBoardRequest
import com.intfocus.hx.dashboard.mine.bean.InstituteDataBean
import com.intfocus.hx.view.AutoScrollViewPager
import com.yonghui.homemetrics.utils.Utils
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * Created by CANC on 2017/6/12.
 */
class HomePageAdapter(val context: Context,
                      private var datas: List<HomeBean>?,
                      var listener: HomePageListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //当前显示的公告位置
    var i = 0
    var UNKNOWN: Int = -1   //未知样式
    var VIEW_PAGER: Int = UNKNOWN + 1//轮播图
    var TEXT_SWITCHER: Int = VIEW_PAGER + 1//跳动文字
    var OPERATIONAL_WARNING: Int = TEXT_SWITCHER + 1//经营预警
    var BUSINESS_OVERVIEW: Int = OPERATIONAL_WARNING + 1//生意概况
    var HOME_BOTTOM: Int = BUSINESS_OVERVIEW + 1// Bottom

    var inflater = LayoutInflater.from(context)
    fun setData(data: List<HomeBean>?) {
        this.datas = data
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return getMyItemViewType(datas!![position].index!!)
    }

    private fun getMyItemViewType(itemType: Int): Int {
        if (VIEW_PAGER == itemType) {
            return VIEW_PAGER
        } else if (TEXT_SWITCHER == itemType) {
            return TEXT_SWITCHER
        } else if (BUSINESS_OVERVIEW == itemType) {
            return BUSINESS_OVERVIEW
        } else if (OPERATIONAL_WARNING == itemType) {
            return OPERATIONAL_WARNING
        } else if (HOME_BOTTOM == itemType) {
            return HOME_BOTTOM
        } else {
            return UNKNOWN
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val contentView: View
        val viewHolder: RecyclerView.ViewHolder
        if (viewType == VIEW_PAGER) {
            contentView = inflater.inflate(R.layout.item_home_viewpager, parent, false)
            viewHolder = ViewPagerHolder(contentView)
        } else if (viewType == TEXT_SWITCHER) {
            contentView = inflater.inflate(R.layout.item_home_text_switcher, parent, false)
            viewHolder = TextSwitcherHolder(contentView)
        } else if (viewType == OPERATIONAL_WARNING) {
            contentView = inflater.inflate(R.layout.item_home_operational_waring_recyclerview, parent, false)
            viewHolder = OperationalWarningHolder(contentView)
        } else if (viewType == BUSINESS_OVERVIEW) {
            contentView = inflater.inflate(R.layout.item_home_business_overview_recycler_view, parent, false)
            viewHolder = BusinessOverviewHolder(contentView)
        } else if (viewType == HOME_BOTTOM) {
            contentView = inflater.inflate(R.layout.item_home_bottom, parent, false)
            viewHolder = HomeBottomHolder(contentView)
        } else {
            contentView = inflater.inflate(R.layout.item_home_unknow, parent, false)
            viewHolder = UNKnowHolder(contentView)
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return if (datas == null) 0 else datas!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        var homeData = datas!![position]
        when (holder) {
            is ViewPagerHolder -> {
                if (homeData != null && homeData.data!!.size > 0) {
                    var views: MutableList<View>? = ArrayList()
                    views!!.clear()
                    val mTypeface = Typeface.createFromAsset(context.assets, "ALTGOT2N.TTF")
                    for (themeItem in homeData!!.data!!.iterator()) {
                        val contentView = View.inflate(context, R.layout.fragment_number_one, null)
                        var tv_number_one_number = contentView.findViewById(R.id.tv_number_one_number) as TextView
                        var tv_number_one_title = contentView.findViewById(R.id.tv_number_one_title) as TextView
                        var tv_number_one_unit = contentView.findViewById(R.id.tv_number_one_unit) as TextView
                        var tv_number_one_sub_title = contentView.findViewById(R.id.tv_number_one_sub_title) as TextView
                        var tv_number_one_sub = contentView.findViewById(R.id.tv_number_one_sub) as TextView
                        var rl_kpi_number_one = contentView.findViewById(R.id.rl_kpi_number_one) as LinearLayout

                        tv_number_one_title.text = themeItem!!.memo2
                        var number = themeItem.data!!.high_light!!.number
                        tv_number_one_number.text = formatNumber(number)
                        tv_number_one_number.typeface = mTypeface
                        tv_number_one_unit.text = "(" + themeItem.unit + ")"
                        tv_number_one_sub_title.text = themeItem.memo1
                        tv_number_one_sub.text = themeItem.data!!.high_light!!.compare
                        rl_kpi_number_one.setOnClickListener {
//                            EventBus.getDefault().post(DashboardItemBean(themeItem.target_url!!, themeItem.report_title!!, themeItem.id!!, 1))
                        }
                        views!!.add(contentView)
                    }
                    var myViewPagerAdapter = MyViewPagerAdapter(views!!, context)
                    holder.viewPager.currentItem = views.size - Integer.MAX_VALUE / 2 % views.size + Integer.MAX_VALUE / 2
                    holder.viewPager.adapter = myViewPagerAdapter
                    holder.layoutDot.removeAllViews()
                    if (views != null && views.size > 0) {
                        holder.viewPager.startAutoScroll()
                        for (i in views.indices) {
                            val point = View(context)
                            point.setBackgroundResource(R.drawable.point_background)
                            val size = Utils.dipDimensionInteger(context, 8.0f)
                            val param = LinearLayout.LayoutParams(size, size)
                            param.rightMargin = size / 2
                            point.layoutParams = param
                            point.isEnabled = i == 0
                            holder.layoutDot.addView(point)
                            //图片数量少于等于1，隐藏圆点
                            if (views.size <= 1) {
                                point.visibility = View.GONE
                            } else {
                                point.visibility = View.VISIBLE
                            }
                        }
                    } else {
                        holder.layoutDot.removeAllViews()
                        holder.viewPager.stopAutoScroll()
                    }
                    holder.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                        override fun onPageScrolled(i: Int, v: Float, i1: Int) {

                        }

                        override fun onPageSelected(i: Int) {
                            val realPosition = myViewPagerAdapter.getRealPosition(i)
                            for (i in views.indices) {
                                if (i == realPosition) {
                                    holder.layoutDot.getChildAt(i).setEnabled(false)
                                } else {
                                    holder.layoutDot.getChildAt(i).setEnabled(true)
                                }
                            }
                        }

                        override fun onPageScrollStateChanged(i: Int) {

                        }
                    })
                }
            }
            is TextSwitcherHolder -> {
                holder.tvNotice.text = ""
                val itemsTLAP1 = homeData.data
                if (itemsTLAP1 != null && itemsTLAP1.size > 0) {
                    val translateAnimation = TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f)
                    translateAnimation.duration = 3000
                    translateAnimation.repeatCount = 10000
                    translateAnimation.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(animation: Animation) {
                            if (i < itemsTLAP1.size) {
                                holder.tvNotice.text = itemsTLAP1[i].title
                            } else {
                                i = 0
                                holder.tvNotice.text = itemsTLAP1[i].title
                            }
                            i++
                        }

                        override fun onAnimationEnd(animation: Animation) {}

                        override fun onAnimationRepeat(animation: Animation) {
                            if (i < itemsTLAP1.size) {
                                holder.tvNotice.text = itemsTLAP1[i].title
                            } else {
                                i = 0
                                holder.tvNotice.text = itemsTLAP1[i].title
                            }
                            i++
                        }
                    })
                    holder.tvNotice.startAnimation(translateAnimation)
                    holder.tvNotice.setOnClickListener {
                        EventBus.getDefault().post(NoticeBoardRequest(true))
                    }
                }
            }
            is OperationalWarningHolder -> {
                var operationalWarningAdapter = OperationalWarningAdapter(context, homeData.data)
                holder.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                holder.recyclerView.adapter = operationalWarningAdapter
            }
            is BusinessOverviewTitleHolder -> {
            }
            is BusinessOverviewHolder -> {
                var businessOverViewAdapter = BusinessOverViewAdapter(context, homeData.data)
                holder.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                holder.recyclerView.adapter = businessOverViewAdapter
            }
            is UNKnowHolder -> {
            }
        }
    }

    /**
     * 轮播图
     */
    class ViewPagerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var viewPager = itemView.findViewById(R.id.vp_icons) as AutoScrollViewPager
        var layoutDot = itemView.findViewById(R.id.layout_dot) as LinearLayout
    }

    /**
     * 跳动文字
     */
    class TextSwitcherHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvNotice = itemView.findViewById(R.id.tv_notice) as TextView

    }

    /**
     *经营预警Title
     */
    class OperationalWarningTitleHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    /**
     *经营预警
     */
    class OperationalWarningHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var recyclerView = itemView.findViewById(R.id.recycler_view) as RecyclerView
    }

    /**
     *生意概况TItle
     */
    class BusinessOverviewTitleHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    /**
     *生意概况
     */
    class BusinessOverviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var recyclerView = itemView.findViewById(R.id.recycler_view) as RecyclerView
    }

    class HomeBottomHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class UNKnowHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface HomePageListener {
        fun itemClick(instituteDataBean: InstituteDataBean)
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
