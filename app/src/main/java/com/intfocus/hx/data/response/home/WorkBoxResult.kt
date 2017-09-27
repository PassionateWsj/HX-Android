package com.intfocus.hx.data.response.home

import com.google.gson.annotations.SerializedName
import com.intfocus.hx.dashboard.work_box.WorkBoxItem
import com.intfocus.hx.data.response.BaseResult

/**
 * Created by liuruilin on 2017/8/11.
 */
class WorkBoxResult: BaseResult() {
    @SerializedName("data")
    var data: List<WorkBoxItem>? = null
}
