package com.intfocus.hx.login.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import org.xutils.x

/**
 * Created by liuruilin on 2017/6/26.
 */
class GuidePageAdapter(var ctx: Context, var imageViews: List<ImageView>,var imageIDList: List<String>): PagerAdapter(){
    override fun getCount(): Int {
        return imageViews.size
    }

    /**
     * 判断当前分页是不是view
     * 由于ViewPager里面的分页可以填入Fragment

     * @param view
     * *
     * @param object
     * *
     * @return
     */
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    /**
     * 清理内存
     * 从第一页滑动到第二页，此时第一页的内存应该释放

     * @param container
     * *
     * @param position
     * *
     * @param object
     */
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(imageViews[position])//释放滑动过后的前一页
    }

    /**
     * 得到---->暂时是没有用的
     * @param object
     * *
     * @return
     */
    override fun getItemPosition(`object`: Any?): Int {
        return super.getItemPosition(`object`)
    }

    /**
     * 初始化分页

     * @param container
     * *
     * @param position
     * *
     * @return
     */
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = imageViews[position]
//        imageView.setImageResource(imageIDList[position])
        val viewLayoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        )
        x.image().bind(imageView, imageIDList[position])
        container.addView(imageView, viewLayoutParams)//设置图片的宽高
        return imageView
    }
}
