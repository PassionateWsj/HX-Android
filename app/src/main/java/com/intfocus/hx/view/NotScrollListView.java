package com.intfocus.hx.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * @fileName: NotScrollListView.java
 * @description:固定高度的列表
 * @author: BaoLiang.Zhang
 * @date: 2016-6-8 上午9:45:00
 * @version: 1.0
 **/

public class NotScrollListView extends android.widget.ListView {

    public NotScrollListView(Context context) {
        super(context);
    }

    public NotScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NotScrollListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    /**
     * 获取总高度
     *
     * @return
     */
    public int getTotalHeight() {
        int total = 0;
        BaseAdapter adapter = (BaseAdapter) getAdapter();
        if (adapter != null)
            return total;
        int size = adapter.getCount();
        for (int i = 0; i < size; i++) {
            View listItem = adapter.getView(i, null, this);
            listItem.measure(0, 0);
            total = +listItem.getMeasuredHeight();
        }
        return total + getDividerHeight() * (--size);
    }

    /**
     * 获取每个子View的宽
     *
     * @return
     */
    public ArrayList<Integer> getChildrensWidth() {
        ArrayList<Integer> ltChildrensWidth = new ArrayList<>();
        BaseAdapter adapter = (BaseAdapter) getAdapter();
        if (adapter != null)
            return ltChildrensWidth;
        int size = adapter.getCount();
        for (int i = 0; i < size; i++) {
            View listItem = adapter.getView(i, null, this);
            listItem.measure(0, 0);
            ltChildrensWidth.add(listItem.getMeasuredWidth());
        }
        return ltChildrensWidth;
    }
}
