package com.intfocus.hx.dashboard.kpi.bean

import java.io.Serializable

/**
 * Created by liuruilin on 2017/6/21.
 */
class KpiGroup: Serializable {
    var group_name: String? = null
    var data: List<KpiGroupItem>? = null
}
