package com.intfocus.hx.dashboard.kpi.bean

/**
 * Created by liuruilin on 2017/6/21.
 */
class KpiRequest(var isSuccess: Boolean, var state: Int, var msg: String) {
    var kpi_data: KpiResultData? = null
}
