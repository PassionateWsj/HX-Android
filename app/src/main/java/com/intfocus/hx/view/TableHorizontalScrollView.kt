package com.intfocus.hx.view

import android.content.Context
import android.util.AttributeSet
import android.widget.HorizontalScrollView

/**
 * 说明 自定义水平滚动视图，解决ScrollView在API23以下没有滚动监听事件问题
 * 作者 郭翰林
 * 创建时间 2017/3/31.
 */

class TableHorizontalScrollView: HorizontalScrollView {

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    interface onScrollChangeListener {
        fun onScrollChanged(scrollView: HorizontalScrollView, x: Int, y: Int)
    }

    /**
     * 设置监听
     * @param onScrollChangeListener
     */
    fun setOnScrollChangeListener(onScrollChangeListener: TableHorizontalScrollView.onScrollChangeListener) {
        Companion.onScrollChangeListener = onScrollChangeListener
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        //回调
        if (Companion.onScrollChangeListener != null) {
            Companion.onScrollChangeListener!!.onScrollChanged(this, l, t)
        }
    }

    companion object {
        private var onScrollChangeListener: onScrollChangeListener? = null
    }
}
