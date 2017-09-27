package com.intfocus.hx.data.response.filter

import com.google.gson.annotations.SerializedName
import com.intfocus.hx.view.addressselector.CityInterface
import java.util.*

/**
 * Created by CANC on 2017/8/3.
 */
class MenuItem : CityInterface {
    var id: String? = null
    var name: String? = null
    /**
     * type : single_choices
     * category : 板块
     * rank_index : 0
     * server_param : plate_id
     */
    var type: String? = null
    var category: String? = null
    var rank_index: Int = 0
    var server_param: String? = null

    @SerializedName("data")
    var data: ArrayList<MenuItem>? = null

    var arrorDirection: Boolean = false//记录是否点击了 false未点击 true已点击

    constructor(id: String?, name: String?) {
        this.id = id
        this.name = name
    }

    override fun getCityName(): String? {
        return name
    }

}
