package com.intfocus.hx.dashboard.kpi.bean

import java.io.Serializable

/**
 * Created by CANC on 2017/7/28.
 */
class MsgResultData : Serializable {
    var code: Int = 0
    var message: String? = null
    var data: List<KpiGroupItem>? = null
}
