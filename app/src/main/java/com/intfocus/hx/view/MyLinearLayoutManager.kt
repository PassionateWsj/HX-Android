package com.intfocus.hx.view

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

/**
 * Created by CANC on 2017/7/28.
 */

class MyLinearLayoutManager(context: Context) : LinearLayoutManager(context) {

    private var mRecycler: RecyclerView.Recycler? = null

    override fun onMeasure(recycler: RecyclerView.Recycler?, state: RecyclerView.State?, widthSpec: Int, heightSpec: Int) {
        super.onMeasure(recycler, state, widthSpec, heightSpec)
        mRecycler = recycler
    }

    val scrollY: Int
        get() {
            var scrollY = paddingTop
            val firstVisibleItemPosition = findFirstVisibleItemPosition()

            if (firstVisibleItemPosition >= 0 && firstVisibleItemPosition < itemCount) {
                for (i in 0..firstVisibleItemPosition - 1) {
                    val view = mRecycler!!.getViewForPosition(i) ?: continue
                    if (view.measuredHeight <= 0) {
                        measureChildWithMargins(view, 0, 0)
                    }
                    val lp = view.layoutParams as RecyclerView.LayoutParams
                    scrollY += lp.topMargin
                    scrollY += getDecoratedMeasuredHeight(view)
                    scrollY += lp.bottomMargin
                    mRecycler!!.recycleView(view)
                }

                val firstVisibleItem = findViewByPosition(firstVisibleItemPosition)
                val firstVisibleItemLayoutParams = firstVisibleItem.layoutParams as RecyclerView.LayoutParams
                scrollY += firstVisibleItemLayoutParams.topMargin
                scrollY -= getDecoratedTop(firstVisibleItem)
            }

            return scrollY
        }
}

