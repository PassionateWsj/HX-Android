package com.intfocus.hx.dashboard.mine

import android.content.Context
import android.os.Bundle
import android.view.View
import com.intfocus.hx.R
import com.intfocus.hx.dashboard.mine.bean.NoticeContentRequest
import com.intfocus.hx.mode.NoticeContentMode
import com.intfocus.hx.util.ToastUtils
import com.zbl.lib.baseframe.core.AbstractActivity
import com.zbl.lib.baseframe.core.Subject
import com.zzhoujay.richtext.RichText
import kotlinx.android.synthetic.main.activity_notice_content.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class NoticeContentActivity : AbstractActivity<NoticeContentMode>() {
    lateinit var ctx: Context

    override fun setSubject(): Subject {
        ctx = this
        return NoticeContentMode(ctx)
    }

    override fun setLayoutRes(): Int {
        return R.layout.activity_notice_content
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice_content)
        EventBus.getDefault().register(this)
        var id = intent.getStringExtra("notice_id")
        model.requestData(id)
        onCreateFinish(savedInstanceState)
    }

    override fun onCreateFinish(p0: Bundle?) {
        supportActionBar!!.hide()
        print("Activity CreateFinish")
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun setData(result: NoticeContentRequest) {
        if (result.isSuccess) {
            RichText.from(result.noticeContent!!.content).into(tv_notice_content)
            tv_notice_content_title.text = result.noticeContent!!.title
            tv_notice_content_time.text = result.noticeContent!!.time
            tv_notice_content.text = result.noticeContent!!.content
        } else {
            ToastUtils.show(ctx, "文章加载失败, 请重试!")
        }
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    fun dismissActivity(v: View) {
        this.onBackPressed()
    }
}
