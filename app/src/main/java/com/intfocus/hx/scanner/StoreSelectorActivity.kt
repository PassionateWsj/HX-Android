package com.intfocus.hx.scanner

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import com.intfocus.hx.R
import com.intfocus.hx.base.BaseActivity
import com.intfocus.hx.data.response.scanner.StoreItem
import com.intfocus.hx.scanner.adapter.SelectorListAdapter
import com.intfocus.hx.scanner.presenter.StoreSelectorPresenter
import com.intfocus.hx.scanner.view.StoreSelectorView
import com.intfocus.hx.util.URLs
import kotlinx.android.synthetic.main.activity_store_selector.*

class StoreSelectorActivity : BaseActivity(), StoreSelectorView {
    companion object {
        val RESULT_CODE_CHOOSE = 2
    }

    lateinit var presenter: StoreSelectorPresenter
    lateinit var adapter: SelectorListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_selector)

        initAdapter()
        initData()
        initListener()
    }

    private fun initAdapter() {
        store_item_select.text = intent.getStringExtra(URLs.kStore)
        adapter = SelectorListAdapter(this)
        listStores.adapter = adapter
    }


    private fun initData() {
        presenter = StoreSelectorPresenter(this, this)
        presenter.loadData()
    }

    private fun initListener() {
        // 隐藏软键盘
        ll_store_selector_container.setOnTouchListener { v, event ->
            hideKeyboard()
            false
        }
        //　item 点击监听
        listStores.setOnItemClickListener { parent, view, position, id ->
            var intent = Intent()
            intent.putExtra(URLs.kStore, adapter.getSelectItem(position).name)
            intent.putExtra(URLs.kStoreIds, adapter.getSelectItem(position).id)
            setResult(RESULT_CODE_CHOOSE, intent)
            finish()
        }
        // 根据搜索内容 请求数据
        tv_store_selector_search.setOnClickListener {
            hideKeyboard()
            presenter.loadData(storeSearchView.text.toString())
        }
        store_item_select.setOnClickListener { finish() }
    }

    /**
     * 获取数据成功
     */
    override fun onResultSuccess(data: MutableList<StoreItem>?) {
        adapter.setData(data)
    }

    /**
     * 获取数据失败
     */
    override fun onResultFailure(e: Throwable?) {

    }

    /**
     * 返回监听
     */
    fun backPress() {
        hideKeyboard()
        finish()
    }

    /**
     * 隐藏软件盘
     */
    fun hideKeyboard() {
        val imm = getSystemService(
                Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.currentFocus
                .windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}
