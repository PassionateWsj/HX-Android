package com.intfocus.hx.listen

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

/**
 * Created by liuruilin on 2017/6/14.
 */
abstract class EndLessOnScrollListener(var llManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {
    //当前页，从0开始
    var currentPage = 0

    //已经加载出来的Item的数量
    var totalItemCount: Int = 0


    //主要用来存储上一个totalItemCount
    var previousTotal = 0

    //在屏幕上可见的item数量
    var visibleItemCount: Int = 0

    //在屏幕可见的Item中的第一个
    var firstVisibleItem: Int = 0

    //是否正在上拉数据
    var loading = true

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        visibleItemCount = recyclerView!!.childCount
        totalItemCount = llManager.itemCount
        firstVisibleItem = llManager.findFirstVisibleItemPosition()
        if (loading) {
            if (totalItemCount > previousTotal) {
                //说明数据已经加载结束
                loading = false
                previousTotal = totalItemCount
            }
        }
        //这里需要好好理解
        if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem) {
            currentPage++
            onLoadMore(currentPage)
            loading = true
        }
    }

    /**
     * 提供一个抽象方法，在Activity中监听到这个EndLessOnScrollListener
     * 并且实现这个方法
     */
    abstract fun onLoadMore(currentPage: Int)
}
