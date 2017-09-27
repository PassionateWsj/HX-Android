package com.intfocus.hx.dashboard.mine.bean

/**
 * Created by liuruilin on 2017/6/12.
 */
class NoticeListDataBean {
    /**
     * id : 42
     * type : 1
     * title : 123
     * content : 1232
     * readed : 0
     * time : 2017-06-12 05:47:55 UTC
     */

    var id: Int = 0
    var type: Int = 0
    var title: String? = null
    var abstracts: String? = null
    var see: Boolean = false
    var time: String? = null

    constructor(id: Int, type: Int, title: String?, abstracts: String?, see: Boolean, time: String?) {
        this.id = id
        this.type = type
        this.title = title
        this.abstracts = abstracts
        this.see = see
        this.time = time
    }
}
