package com.intfocus.hx.login.bean

import com.google.gson.annotations.SerializedName
import com.intfocus.hx.data.response.BaseResult

/**
 * ****************************************************
 * author: JamesWong
 * created on: 17/08/11 上午09:23
 * e-mail: PassionateWsj@outlook.com
 * name:
 * desc:
 * ****************************************************
 */

class NewUser : BaseResult() {

    @SerializedName("data")
    var data: LoginResult? = null

    inner class LoginResult {
        /**
         * user_num : 13913778859
         * user_name : 王施君
         * user_pass : c4ca4238a0b923820dcc509a6f75849b
         * email :
         * mobile : 13913778859
         * user_id : 121801
         * status : true
         * gravatar :
         * group_id : 165
         * group_name : 大区(全部)门店(全部)商行(全部)
         * role_id : 99
         * role_name : 胜因团队
         * code : 200
         */

        var user_num: String? = null
        var user_name: String? = null
        var user_pass: String? = null
        var email: String? = null
        var mobile: String? = null
        var user_id: String? = null
        var status: String? = null
        var gravatar: String? = null
        var group_id: String? = null
        var group_name: String? = null
        var role_id: String? = null
        var role_name: String? = null
    }
}
