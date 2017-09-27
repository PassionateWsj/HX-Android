package com.intfocus.hx.dashboard.mine.bean

/**
 * Created by CANC on 2017/7/24.
 */
class NoticeMenuBean {
    /**
     * 公告预警菜单使用
     *
     */
    var code: Int = 0
    var isSelected: Boolean = false

    constructor(code: Int, isSelected: Boolean) {
        this.code = code
        this.isSelected = isSelected
    }
}
