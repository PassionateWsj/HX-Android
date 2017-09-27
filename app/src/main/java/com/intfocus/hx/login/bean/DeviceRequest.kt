package com.intfocus.hx.login.bean

/**
 * ****************************************************
 * author: JamesWong
 * created on: 17/08/11 上午10:26
 * e-mail: PassionateWsj@outlook.com
 * name:
 * desc:
 * ****************************************************
 */
class DeviceRequest {

    /**
     * api_token : 75d2a2f04ed551ce1988c8d8f3b02241
     * user_num : 13913778859
     * device : {"uuid":"25525ea79879905bf2f492d3f52sdfasfbc15f3e3ac409c","os":"iPhonasdfe 6 (A1549/A1586)","name":"junjieasdfsdfasf的 iPhone💋","os_version":"9.2asdf.a1","platform":"ios"}
     * app_version : i1.3.0
     * ip : 223.104.5.206
     * browser : Mozilla/5.0 (iPhone; CPU iPhone OS 9_2_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Mobile/13D15
     */

    var api_token: String? = null
    var user_num: String? = null
    var device: DeviceBean? = null
    var app_version: String? = null
    var ip: String? = null
    var browser: String? = null


    class DeviceBean {
        /**
         * uuid : 25525ea79879905bf2f492d3f52sdfasfbc15f3e3ac409c
         * os : iPhonasdfe 6 (A1549/A1586)
         * name : junjieasdfsdfasf的 iPhone💋
         * os_version : 9.2asdf.a1
         * platform : ios
         */

        var uuid: String? = null
        var os: String? = null
        var name: String? = null
        var os_version: String? = null
        var platform: String? = null
    }
}
