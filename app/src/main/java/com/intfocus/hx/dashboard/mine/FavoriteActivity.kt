package com.intfocus.hx.dashboard.mine

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.intfocus.hx.R
import com.intfocus.hx.base.RefreshActivity
import com.intfocus.hx.dashboard.mine.adapter.InstituteAdapter
import com.intfocus.hx.dashboard.mine.bean.InstituteDataBean
import com.intfocus.hx.data.request.RequestFavourite
import com.intfocus.hx.data.response.BaseResult
import com.intfocus.hx.data.response.article.ArticleResult
import com.intfocus.hx.net.ApiException
import com.intfocus.hx.net.CodeHandledSubscriber
import com.intfocus.hx.net.RetrofitUtil
import com.intfocus.hx.subject.WebApplicationActivity
import com.intfocus.hx.util.*
import com.intfocus.hx.view.CommonPopupWindow
import com.lcodecore.tkrefreshlayout.footer.LoadingView
import com.lcodecore.tkrefreshlayout.header.SinaRefreshView

class FavoriteActivity : RefreshActivity(), InstituteAdapter.NoticeItemListener {
    lateinit var ctx: Context
    lateinit var adapter: InstituteAdapter
    var datas: MutableList<InstituteDataBean>? = null
    lateinit var userNum: String
    lateinit var statusMap: MutableMap<String, String>
    lateinit var queryMap: MutableMap<String, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        ctx = this
        setRefreshLayout()
        userNum = mActivity.getSharedPreferences("UserBean", Context.MODE_PRIVATE).getString(URLs.kUserNum, "")
        init()
    }

    fun init() {
        statusMap = mutableMapOf()
        queryMap = mutableMapOf()
        val mLayoutManager = LinearLayoutManager(mActivity)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = mLayoutManager
        adapter = InstituteAdapter(mActivity, null, this)
        recyclerView.adapter = adapter
        var headerView = SinaRefreshView(mActivity)
        headerView.setArrowResource(R.drawable.loading_up)
        var bottomView = LoadingView(mActivity)
        refreshLayout.setHeaderView(headerView)
        refreshLayout.setBottomView(bottomView)
        getData(true)
    }

    /**
     * 获取数据
     */
    override fun getData(isShowDialog: Boolean) {
        if (!HttpUtil.isConnected(mActivity)) {
            finshRequest()
            isEmpty = datas == null || datas!!.size == 0
            ErrorUtils.viewProcessing(refreshLayout, llError, llRetry, "无更多文章了", tvErrorMsg, ivError,
                    isEmpty!!, false, R.drawable.pic_3, {
                getData(true)
            })
            ToastUtils.show(mActivity, "请检查网络链接")
            return
        }
        if (isShowDialog && (loadingDialog == null || !loadingDialog!!.isShowing)) {
            showLoading()
        }
        queryMap.put("user_num", userNum)
        queryMap.put("page", page.toString())
        queryMap.put("limit", pageSize.toString())
        RetrofitUtil.getHttpService(ctx).getMyFavouritedList(queryMap)
                .compose(RetrofitUtil.CommonOptions<ArticleResult>())
                .subscribe(object : CodeHandledSubscriber<ArticleResult>() {
                    override fun onCompleted() {
                        finshRequest()
                    }

                    override fun onError(apiException: ApiException) {
                        finshRequest()
                        ToastUtils.show(mActivity, apiException.displayMessage)
                    }

                    override fun onBusinessNext(data: ArticleResult) {
                        finshRequest()
                        totalPage = data.total_page
                        isLasePage = page == totalPage
                        if (datas == null) {
                            datas = ArrayList()
                        }
                        if (isRefresh!!) {
                            datas!!.clear()
                        }
                        datas!!.addAll(data.data)
                        adapter.setData(datas)
                        isEmpty = datas == null || datas!!.size == 0
                        ErrorUtils.viewProcessing(refreshLayout, llError, llRetry, "无更多文章了", tvErrorMsg, ivError,
                                isEmpty!!, true, R.drawable.pic_3, null)
                    }
                })
    }

    fun finshRequest() {
        refreshLayout.finishRefreshing()
        refreshLayout.finishLoadmore()
        dismissLoading()
    }


    /**
     * 操作收藏/取消收藏
     */
    fun ArticleOperating(articleId: String, status: String) {
        if (!HttpUtil.isConnected(mActivity)) {
            ToastUtils.show(mActivity, "请检查网络链接")
            return
        }
        showLoading()
        var body = RequestFavourite()
        body.user_num = userNum
        body.article_id = articleId
        body.favourite_status = status
        RetrofitUtil.getHttpService(ctx).articleOperating(body)
                .compose(RetrofitUtil.CommonOptions<BaseResult>())
                .subscribe(object : CodeHandledSubscriber<BaseResult>() {
                    override fun onCompleted() {
                    }

                    override fun onError(apiException: ApiException) {
                        dismissLoading()
                        ToastUtils.show(mActivity, apiException.displayMessage)
                    }

                    override fun onBusinessNext(data: BaseResult) {
                        getData(true)
                        ToastUtils.show(mActivity, data.message + "",ToastColor.SUCCESS)
                    }
                })
    }

    override fun itemClick(instituteDataBean: InstituteDataBean) {
        var intent = Intent(mActivity, WebApplicationActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        var link = String.format("%s/mobile/v2/user/%s/article/%s", K.kBaseUrl, mUserSP.getString(K.kUserId,"0").toString(), instituteDataBean!!.acticleId.toString())
        intent.putExtra(URLs.kBannerName, instituteDataBean!!.title.toString())
        intent.putExtra(URLs.kLink, link)
        startActivity(intent)
    }

    /**
     * 加入收藏
     */
    override fun addCollection(instituteDataBean: InstituteDataBean) {
        ArticleOperating(instituteDataBean.acticleId.toString(), "1")
    }

    /**
     * 取消收藏
     */
    override fun cancelCollection(instituteDataBean: InstituteDataBean) {
        CommonPopupWindow().showPopupWindow(mActivity, "取消收藏", R.color.co11_syr, "继续收藏", R.color.co3_syr,
                object : CommonPopupWindow.ButtonLisenter {
                    override fun btn1Click() {
                        ArticleOperating(instituteDataBean.acticleId.toString(), "2")
                    }

                    override fun btn2Click() {
                    }
                })
    }

}
