package com.intfocus.hx.dashboard.mine.bean

import java.io.File

/**
 * Created by liuruilin on 2017/7/1.
 */
class IssueCommitInfo {
    var user_num = ""
    var issue_content = ""
    var app_version = ""
    var platform = ""
    var platform_version = ""
    var image_list : MutableList<File> = mutableListOf()
}
