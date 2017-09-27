package com.intfocus.hx.dashboard.mine.bean

/**
 * Created by liuruilin on 2017/6/12.
 */
class NoticeListBean {

    /**
     * total : 13
     * curr_page : 1
     * page_size : 4
     * total_page : 4
     * data : [{"id":24,"type":2,"title":"add test","abstracts":"
     *
     *\n\t测试草稿and发布\n<\/p>\n
     *
     *\n\t<br></br>\n<\/p>","time":"2017-06-13 10:20:13","see":true},{"id":8,"type":1,"title":"测试8","abstracts":"测试8测试8测试8测试8测试8测试8测试8测试8测试8测试8测试8测试8测试8测试8测试8测试8","time":"2017-06-08 11:55:36","see":true},{"id":6,"type":1,"title":"测试6","abstracts":"测试6测试6测试6测试6测试6测试6测试6测试6测试6","time":"2017-06-08 11:29:56","see":true},{"id":5,"type":2,"title":"测试5","abstracts":"测试5测试5测试5测试5测试5","time":"2017-06-08 11:29:18","see":true}]
     * code : 200
     */

    var total : Int = 0
    var curr_page : Int = 0
    var page_size : Int = 0
    var total_page : Int = 0
    var code : Int = 0
    var data : List<NoticeListDataBean> = listOf()
}
