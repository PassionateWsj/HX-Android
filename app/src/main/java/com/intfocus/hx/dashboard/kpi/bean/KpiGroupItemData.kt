package com.intfocus.hx.dashboard.kpi.bean

import java.io.Serializable

/**
 * Created by liuruilin on 2017/6/21.
 */
class KpiGroupItemData: Serializable {
    var high_light: KpiGroupItemHighLight? = null
    var chart_data: List<*>? = null
}
