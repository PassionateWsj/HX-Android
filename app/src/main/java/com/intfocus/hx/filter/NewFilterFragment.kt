package com.intfocus.hx.filter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.intfocus.hx.R
import com.intfocus.hx.dashboard.mine.adapter.NewFilterAdapter
import com.intfocus.hx.data.response.filter.MenuItem
import com.intfocus.hx.view.MyLinearLayoutManager

/**
 * Created by CANC on 2017/8/8.
 * 筛选界面
 */
class NewFilterFragment(menuDatas: ArrayList<MenuItem>, myLisenter: NewFilterFragmentListener) : Fragment(), NewFilterAdapter.FilterMenuListener {

    lateinit var adapter: NewFilterAdapter
    var datas = menuDatas
    lateinit var mView: View
    lateinit var recyclerView: RecyclerView
    var lisenter = myLisenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater!!.inflate(R.layout.fragment_filter, container, false)
        initView()
        return mView
    }

    fun initView() {
        recyclerView = mView.findViewById(R.id.recycler_view) as RecyclerView
        adapter = NewFilterAdapter(context, datas, this)
        var layoutManager = MyLinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        adapter.update()
    }

    override fun itemClick(position: Int) {
        for (i in 0..datas.size - 1) {
            if (i == position) {
                datas!![position].arrorDirection = true
            } else {
                datas!![i].arrorDirection = false
            }
        }
        adapter.setData(datas)
        lisenter.itemClick(position, datas)
    }

    interface NewFilterFragmentListener {
        fun itemClick(position: Int, menuDatas: ArrayList<MenuItem>)
    }
}
