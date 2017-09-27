package com.intfocus.hx.subject

import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.Toast
import com.google.gson.Gson
import com.intfocus.hx.R
import com.intfocus.hx.base.BaseActivity
import com.intfocus.hx.subject.selecttree.Bean
import com.intfocus.hx.subject.selecttree.SelectItems
import com.intfocus.hx.subject.selecttree.TreeListViewAdapter
import com.intfocus.hx.util.FileUtil
import kotlinx.android.synthetic.main.activity_selector_tree.*
import java.util.*

class SelectorTreeActivity : BaseActivity() {
    val mDatas = ArrayList<Bean>()
    lateinit var mAdapter: TreeListViewAdapter<*>
    var gson = Gson()
    var searchItemsPath = ""
    var selectedItemPath = ""
    lateinit var mTree: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selector_tree)

        var intent = intent
        searchItemsPath = intent.getStringExtra("searchItemsPath")
        selectedItemPath = intent.getStringExtra("selectedItemPath")
        mTree = findViewById(R.id.lv_selector_tree) as ListView
        initDatas(searchItemsPath)
    }

    fun initDatas(searchItemsPath: String) {
        Thread(Runnable {
            var searchItemsStr = FileUtil.readFile(searchItemsPath)
            var selectItems = gson.fromJson(searchItemsStr, SelectItems::class.java)
            var id = 1
            var first: Int
            var second: Int
            if (selectItems!=null) {
                for (selectItem in selectItems.data!!.iterator()) {
                    mDatas.add(Bean(id, 0, selectItem.titles))
                    first = id
                    id += 1
                    if (selectItem.infos != null) {
                        for (info in selectItem.infos!!.iterator()) {
                            mDatas.add(Bean(id, first, info.titles))
                            second = id
                            id += 1
                            if (info.infos != null) {
                                for (info in info.infos!!.iterator()) {
                                    mDatas.add(Bean(id, second, info.titles))
                                    id += 1
                                }
                            }
                        }
                    }
                }
            }

            runOnUiThread {
                mAdapter = SimpleTreeAdapter(mTree, this, mDatas, 10)
                anim_loading.visibility = View.GONE
                mAdapter.setOnTreeNodeClickListener { node, _ ->
                    if (node.isLeaf) {
                        var selectedName = node.name
                        if (node.parent != null) {
                            selectedName = node.parent.name + "||" + selectedName

                            if (node.parent.parent != null) {
                                selectedName = node.parent.parent.name + "||" + selectedName
                            }
                        }
                        Toast.makeText(applicationContext, selectedName,
                                Toast.LENGTH_SHORT).show()
                        FileUtil.writeFile(selectedItemPath, selectedName)
                        finish()
                    }
                }

                mTree.adapter = mAdapter
            }

        }).start()
    }

    /*
     * 返回
     */
    override fun dismissActivity(v: View) {
        this@SelectorTreeActivity.onBackPressed()
    }

    override fun onBackPressed() {
        finish()
    }
}
