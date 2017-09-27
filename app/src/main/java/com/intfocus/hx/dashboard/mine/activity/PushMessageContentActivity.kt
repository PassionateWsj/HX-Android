package com.intfocus.hx.dashboard.mine.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.intfocus.hx.R
import com.intfocus.hx.dashboard.mine.bean.PushMessageBean
import kotlinx.android.synthetic.main.activity_push_message_content.*

class PushMessageContentActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_push_message_content)

        initData()
    }

    private fun initData() {
        var bean = intent.getSerializableExtra("push_message_bean") as PushMessageBean
        tv_push_message_content_title.text = bean.title
        tv_push_message_content.text = bean.body_text

    }
    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.iv_back->{
                finish()
            }
        }
    }
}
