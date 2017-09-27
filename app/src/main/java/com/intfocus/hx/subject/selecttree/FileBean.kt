package com.intfocus.hx.subject.selecttree

class FileBean(@TreeNodeId
               private val _id: Int, @TreeNodePid
               private val parentId: Int, @TreeNodeLabel
               private val name: String) {
    private val length: Long = 0
    private val desc: String? = null

}
