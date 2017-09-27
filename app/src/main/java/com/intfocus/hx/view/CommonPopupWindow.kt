package com.intfocus.hx.view

import android.app.Activity
import android.graphics.drawable.BitmapDrawable
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import com.intfocus.hx.R

/**
 * Created by CANC on 2017/8/1.
 */
class CommonPopupWindow : PopupWindow() {
    lateinit var conentView: View
    lateinit var tvBtn1: TextView
    lateinit var tvBtn2: TextView
    lateinit var minePop: PopupWindow

    /**
     *
     */
    open fun showPopupWindow(activity: Activity, str1: String, colorId1: Int, str2: String, colorId2: Int, lisenter: ButtonLisenter) {
        val inflater = LayoutInflater.from(activity)
        conentView = inflater.inflate(R.layout.popup_common, null)
        tvBtn1 = conentView.findViewById(R.id.tv_btn1) as TextView
        tvBtn2 = conentView.findViewById(R.id.tv_btn2) as TextView
        tvBtn1.text = str1
        tvBtn2.text = str2
        tvBtn1.setTextColor(ContextCompat.getColor(activity, colorId1))
        tvBtn2.setTextColor(ContextCompat.getColor(activity, colorId2))
        //
        tvBtn1.setOnClickListener {
            lisenter.btn1Click()
            minePop.dismiss()
        }
        tvBtn2.setOnClickListener {
            lisenter.btn2Click()
            minePop.dismiss()
        }
        minePop = PopupWindow(conentView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        minePop.isFocusable = true// 取得焦点
        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
        minePop.setBackgroundDrawable(BitmapDrawable())
        val params = activity.window.attributes
        params.alpha = 0.7f
        activity.window.attributes = params
        //点击外部消失
        minePop.isOutsideTouchable = true
        //设置可以点击
        minePop.isTouchable = true
        //进入退出的动画
        minePop.animationStyle = R.style.anim_popup_bottombar
        minePop.showAtLocation(conentView, Gravity.BOTTOM, 0, 0)
        minePop.setOnDismissListener {
            val paramsa = activity.window.attributes
            paramsa.alpha = 1f
            activity.window.attributes = paramsa
        }

    }

    interface ButtonLisenter {

        fun btn1Click()

        fun btn2Click()
    }

}
