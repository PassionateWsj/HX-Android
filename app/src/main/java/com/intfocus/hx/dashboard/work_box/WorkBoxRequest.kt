package com.intfocus.hx.dashboard.work_box

/**
 * Created by liuruilin on 2017/7/28.
 */
class WorkBoxRequest(var isSuccess: Boolean,var stateCode: Int) {
    var workBoxDatas: List<WorkBoxItem>? = null
}
