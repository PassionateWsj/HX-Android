package com.intfocus.hx.login.bean

import com.google.gson.annotations.SerializedName
import com.intfocus.hx.data.response.BaseResult

/**
 * ****************************************************
 * author: JamesWong
 * created on: 17/08/11 上午10:52
 * e-mail: PassionateWsj@outlook.com
 * name:
 * desc:
 * ****************************************************
 */
class Device : BaseResult() {
    /**
     * {
     *    "message": "获取数据成功",
     *    "data": {
     *        "device_uuid": "25525ea79879905bf2f492d3f52sdfasfbc15f3e3ac409c",
     *        "device_state": true,
     *        "user_device_id": 11005
     *    },
     *    "code": 200
     * }
     */

    @SerializedName("data")
    var mResult: DeviceResult? = null

    class DeviceResult {

        /**
         * message : 获取数据成功
         * device_uuid : 25525ea79879905bf2f492d3f52sdfasfbc15f3e3ac409c
         * device_state : true
         * user_device_id : 10998
         * code : 200
         */

        var device_uuid: String? = null
        var device_state: Boolean = false
        var user_device_id: Int = 0
    }

}
