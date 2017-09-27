package com.intfocus.hx.bean

/**
 * Created by liuruilin on 2017/7/14.
 */
class PushMessage {
    /**{
     *  "msg_id":"ul42751150157385157100",
     *  "display_type":"notification",
     *  "random_min":0,
     *  "body":{
     *      "ticker":"消息中心(7851)",
     *      "title":"消息中心(7851)",
     *      "text":"野竹分青霭，飞泉挂碧峰",
     *      "sound":"default",
     *      "play_sound":"true",
     *      "play_lights":"true",
     *      "play_vibrate":"true",
     *      "after_open":"go_custom",
     *      "custom":{
     *          "type":"report",
     *          "title":"第二集群销售额",
     *          "url":"/mobile/v2/group/%@/template/4/report/8",
     *          "obj_id":8,
     *          "obj_type":1,
     *          "debug_timestamp":"2017-08-01 15:50:51 +0800"
     *      }
     *  }
     *}
     */
    var type: String? = null
    var title: String? = null
    var url: String? = null
    var obj_id: Int = 0
    var obj_type: Int = 0
    var debug_timestamp: String? = null
    var body_title: String? = null
    var body_text: String? = null
    var is_new_msg:Boolean = true

}

