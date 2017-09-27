package com.intfocus.hx.data.response.scanner

import com.google.gson.annotations.SerializedName
import com.intfocus.hx.data.response.BaseResult

/**
 * Created by liuruilin on 2017/8/11.
 */
class StoreListResult: BaseResult() {
    @SerializedName("data")
    var data: List<StoreItem>? = null
}
