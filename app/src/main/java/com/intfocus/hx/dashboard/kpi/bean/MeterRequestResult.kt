package com.intfocus.hx.dashboard.kpi.bean

import java.util.ArrayList

/**
 * 仪表数据请求结果
 * Created by zbaoliang on 17-4-28.
 */
class MeterRequestResult(var isSuccess: Boolean, var stateCode: Int) {
    var topDatas: ArrayList<MererEntity>? = null
    var bodyDatas: ArrayList<MererEntity>? = null
}
