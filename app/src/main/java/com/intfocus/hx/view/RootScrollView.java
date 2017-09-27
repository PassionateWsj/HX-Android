package com.intfocus.hx.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 *
 * Created by zbaoliang on 17-6-15.
 */
public class RootScrollView extends ScrollView {
    private OnScrollListener onScrollListener;

    public RootScrollView(Context context) {
        super(context);
    }

    public RootScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollListener != null) {
            onScrollListener.onScroll(t);
        }
    }

    /**
     * 滚动的回调接口
     */
    public interface OnScrollListener {
        /**
         * 返回ScrollView滑动的Y方向距离
         *
         * @param scrollY
         */
        void onScroll(int scrollY);
    }
}
