package com.intfocus.hx.dashboard.work_box

import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.intfocus.hx.R
import com.intfocus.hx.base.BaseModeFragment
import com.intfocus.hx.login.LoginActivity
import com.intfocus.hx.util.HttpUtil
import com.intfocus.hx.util.ToastUtils
import com.zbl.lib.baseframe.core.Subject
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.fragment_work_box.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by liuruilin on 2017/7/28.
 */
class WorkBoxFragment: BaseModeFragment<WorkBoxMode>(), SwipeRefreshLayout.OnRefreshListener {
    var rootView : View? = null
    var datas: List<WorkBoxItem>? = null

    override fun setSubject(): Subject {
        return WorkBoxMode(ctx)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        EventBus.getDefault().register(this)
        if (rootView == null) {
            rootView = inflater!!.inflate(R.layout.fragment_work_box, container, false)
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
    fun initView(request: WorkBoxRequest) {
        if (request.isSuccess) {
            datas = request.workBoxDatas
            gv_work_box.adapter = WorkBoxAdapter(ctx, datas)
            tv_logout.setOnClickListener { showLogoutPopupWindow(ctx) }
        }
        swipe_container.isRefreshing = false
    }

    /**
     * 退出登录选择窗
     */
    internal fun showLogoutPopupWindow(ctx: Context) {
        val contentView = LayoutInflater.from(ctx).inflate(R.layout.popup_logout, null)
        //设置弹出框的宽度和高度
        var popupWindow = PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        popupWindow.isFocusable = true// 取得焦点
        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
        popupWindow.setBackgroundDrawable(BitmapDrawable())
        //点击外部消失
        popupWindow.isOutsideTouchable = true
        //设置可以点击
        popupWindow.isTouchable = true
        popupWindow.showAtLocation(activity.toolBar, Gravity.BOTTOM, 0, 0)
        popupWindow.animationStyle = R.anim.popup_bottombar_in

        contentView.findViewById(R.id.rl_logout_confirm).setOnClickListener {
            // 确认退出
            val intent = Intent()
            intent.setClass(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            activity.finish()
        }
        contentView.findViewById(R.id.rl_cancel).setOnClickListener {
            // 取消
            popupWindow.dismiss()
        }
        contentView.findViewById(R.id.rl_popup_logout_background).setOnClickListener {
            // 点击背景半透明区域
            popupWindow.dismiss()
        }
    }
}
