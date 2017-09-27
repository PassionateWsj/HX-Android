package com.intfocus.hx.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

/**
 * Created by CANC on 2017/4/19.
 */

public class MyHorizontalScrollView extends HorizontalScrollView {
    private MyScrollChangeListener listener;

    public MyHorizontalScrollView(Context context) {
        super(context);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setMyScrollChangeListener(MyScrollChangeListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (null != listener)
            listener.onscroll(this, l, t, oldl, oldt);
    }

    /**
     * 控制滑动速度
     */
    @Override
    public void fling(int velocityY) {
        super.fling(velocityY / 2);
    }

    public interface MyScrollChangeListener {
        void onscroll(MyHorizontalScrollView view, int l, int t, int oldl, int oldt);
    }
}
