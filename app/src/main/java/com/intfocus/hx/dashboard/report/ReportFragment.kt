package com.intfocus.hx.dashboard.report

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.intfocus.hx.R
import com.intfocus.hx.base.BaseModeFragment
import com.intfocus.hx.dashboard.report.adapter.ReportsLeftListAdapter
import com.intfocus.hx.dashboard.report.adapter.ReportsRightRVAdapter
import com.intfocus.hx.dashboard.report.mode.CategoryBean
import com.intfocus.hx.dashboard.report.mode.ReportListPageRequest
import com.intfocus.hx.mode.ReportsListMode
import com.intfocus.hx.util.HttpUtil
import com.intfocus.hx.util.ToastUtils
import com.zbl.lib.baseframe.core.Subject
import kotlinx.android.synthetic.main.fragment_reports.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by liuruilin on 2017/6/15.
 */
class ReportFragment : BaseModeFragment<ReportsListMode>(), ReportsLeftListAdapter.ReportLeftListListener, SwipeRefreshLayout.OnRefreshListener {
    var rootView: View? = null
    var datas: List<CategoryBean>? = null
    lateinit var reportsRightAdapter: ReportsRightRVAdapter
    lateinit var reportsLeftAdapter: ReportsLeftListAdapter

    override fun setSubject(): Subject {
        return ReportsListMode(ctx)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        EventBus.getDefault().register(this)
        if (rootView == null) {
            rootView = inflater!!.inflate(R.layout.fragment_reports, container, false)
            model.requestData()
        }
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        initSwipeLayout()
        super.onActivityCreated(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

    fun initSwipeLayout() {
        swipe_container.setOnRefreshListener(this)
        swipe_container.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light)
        swipe_container.setDistanceToTriggerSync(300)// 设置手指在屏幕下拉多少距离会触发下拉刷新
        swipe_container.setSize(SwipeRefreshLayout.DEFAULT)
    }

    override fun onRefresh() {
        if (HttpUtil.isConnected(context)) {
            model.requestData()
        } else {
            swipe_container.isRefreshing = false
            ToastUtils.show(context, "请检查网络")
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun initView(requestReport: ReportListPageRequest) {
        if (requestReport.isSuccess) {
            datas = requestReport.categroy_list
            reportsLeftAdapter = ReportsLeftListAdapter(ctx, datas, this)
            ll_reports_category_list.adapter = reportsLeftAdapter
            val mLayoutManager = LinearLayoutManager(ctx)
            mLayoutManager.orientation = LinearLayoutManager.VERTICAL
            rv_reports_group_list.layoutManager = mLayoutManager
            reportsRightAdapter = ReportsRightRVAdapter(ctx, datas!![0].data)
            rv_reports_group_list.adapter = reportsRightAdapter
        }
        swipe_container.isRefreshing = false
    }

    override fun reportLeftItemClick(sign: ImageView, position: Int) {
        reportsRightAdapter.setData(datas!![position].data)
        reportsLeftAdapter.refreshListItemState(position)
    }
}
