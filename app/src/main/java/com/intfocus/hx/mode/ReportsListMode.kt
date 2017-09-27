package com.intfocus.hx.mode

import android.content.Context
import com.intfocus.hx.dashboard.report.mode.ReportListPageRequest
import com.intfocus.hx.data.response.home.ReportListResult
import com.intfocus.hx.net.ApiException
import com.intfocus.hx.net.CodeHandledSubscriber
import com.intfocus.hx.net.RetrofitUtil
import com.intfocus.hx.util.URLs.kGroupId
import com.intfocus.hx.util.URLs.kRoleId
import com.zbl.lib.baseframe.core.AbstractMode
import org.greenrobot.eventbus.EventBus

/**
 * 主页 - 报表 Model
 * Created by liuruilin on 2017/6/15.
 */
class ReportsListMode(var ctx: Context) : AbstractMode() {
    var mUserSP = ctx.getSharedPreferences("UserBean", Context.MODE_PRIVATE)

    override fun requestData() {
        RetrofitUtil.getHttpService(ctx).getReportList(mUserSP.getString(kGroupId, "0"), mUserSP.getString(kRoleId, "0"))
                .compose(RetrofitUtil.CommonOptions<ReportListResult>())
                .subscribe(object : CodeHandledSubscriber<ReportListResult>() {

                    override fun onBusinessNext(data: ReportListResult?) {
                        val result1 = ReportListPageRequest(true, 200)
                        result1.categroy_list = data!!.data
                        EventBus.getDefault().post(result1)
                    }

                    override fun onError(apiException: ApiException?) {
                        val result1 = ReportListPageRequest(false, -1)
                        EventBus.getDefault().post(result1)
                    }

                    override fun onCompleted() {
                    }
                })
    }
}
