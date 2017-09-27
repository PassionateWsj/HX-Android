package com.intfocus.hx.data.response.home

import com.google.gson.annotations.SerializedName
import com.intfocus.hx.dashboard.kpi.bean.KpiGroupItem
import com.intfocus.hx.data.response.BaseResult

/**
 * Created by CANC on 2017/7/31.
 */
class HomeMsgResult : BaseResult() {
    @SerializedName("data")
    var data: List<KpiGroupItem>? = null
}
