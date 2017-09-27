package com.intfocus.hx.dashboard.old_kpi;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 瀑布流间距
 */
public class MarginDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public MarginDecoration(Context context) {
        space = 16;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(space, 0, space, space);
    }
}
