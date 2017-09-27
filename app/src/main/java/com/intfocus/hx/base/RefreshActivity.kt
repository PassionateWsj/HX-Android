package com.intfocus.hx.base

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.intfocus.hx.R
import com.intfocus.hx.util.LoadingUtils
import com.intfocus.hx.util.ToastUtils
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout

/**
 * Created by CANC on 2017/7/31.
 */
open abstract class RefreshActivity : BaseActivity() {

    lateinit var mActivity: Activity
    var loadingDialog: Dialog? = null
    lateinit var refreshLayout: TwinklingRefreshLayout
    lateinit var recyclerView: RecyclerView
    //错误界面
    lateinit var llError: LinearLayout
    lateinit var tvErrorMsg: TextView
    lateinit var ivError: ImageView
    lateinit var llRetry: LinearLayout
    //是否是空数据
    var isEmpty: Boolean? = true
    //页码信息
    var totalPage: Int? = 0
    var page: Int? = 1
    var pageSize: Int = 10
    //是否需要清空数据
    var isRefresh: Boolean? = true
    //是否最后一页
    var isLasePage: Boolean? = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = this
    }

    fun setRefreshLayout() {
        refreshLayout = findViewById(R.id.refresh_layout) as TwinklingRefreshLayout
        recyclerView = findViewById(R.id.recycler_view) as RecyclerView
        llError = findViewById(R.id.ll_empty) as LinearLayout
        tvErrorMsg = findViewById(R.id.tv_errorMsg) as TextView
        ivError = findViewById(R.id.iv_error) as ImageView
        llRetry = findViewById(R.id.ll_retry) as LinearLayout

        refreshLayout.setOnRefreshListener(object : RefreshListenerAdapter() {
            override fun onRefresh(refreshLayout: TwinklingRefreshLayout?) {
                super.onRefresh(refreshLayout)
                isRefresh = true
                page = 1
                getData(false)
            }

            override fun onLoadMore(refreshLayout: TwinklingRefreshLayout?) {
                super.onLoadMore(refreshLayout)
                if (isLasePage!!) {
                    ToastUtils.show(mActivity, "已经是最后一页")
                    refreshLayout!!.finishRefreshing()
                    refreshLayout.finishLoadmore()
                    return
                }
                page = page!! + 1
                isRefresh = false
                getData(false)
            }
        })
    }

    /**
     * 获取数据
     * @param isShowDialog
     */
    protected abstract fun getData(isShowDialog: Boolean)

    protected fun showLoading() {
        loadingDialog = LoadingUtils.createLoadingDialog(mActivity)
        loadingDialog!!.show()
    }

    protected fun dismissLoading() {
        if (loadingDialog != null) {
            loadingDialog!!.dismiss()
        }
    }

}
