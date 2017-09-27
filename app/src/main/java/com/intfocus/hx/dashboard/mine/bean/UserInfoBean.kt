package com.intfocus.hx.dashboard.mine.bean

/**
 * Created by liuruilin on 2017/6/7.
 */

class UserInfoBean {

    /**
     * user_num : 13162726850
     * user_name : 刘锐麟
     * login_duration : 50
     * browse_report_count : 6
     * surpass_percentage : 37.4
     * login_count : 1197
     * browse_count : 19
     * browse_distinct_count : 6
     * code : 200
     */

    var user_num: String? = null
    var user_name: String? = null
    var gravatar: String? = null
    var group_name: String? = null
    var role_name: String? = null
    var login_duration: String? = null
    var browse_report_count: String? = null
    var surpass_percentage: Double = 0.toDouble()
    var login_count: String? = null
    var browse_count: String? = null
    var coordinate_location: String? = null
    var browse_distinct_count: String? = null
    var code: Int = 0
}
