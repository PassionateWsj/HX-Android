package com.intfocus.hx.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;

import com.intfocus.hx.R;

import static android.animation.ObjectAnimator.ofFloat;

/**
 * 仪表盘箭头
 * Created by zbaoliang on 17-4-29.
 */
public class RateCursor extends android.support.v7.widget.AppCompatImageView {

    private int[] sources = new int[]{R.drawable.icon_redarrow,
            R.drawable.icon_yellowarrow, R.drawable.icon_greenarrow};
    boolean currAnim;
    private ObjectAnimator downAnimator;
    private ObjectAnimator upAnimator;

    public RateCursor(Context context) {
        super(context);
        init();
    }

    public RateCursor(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RateCursor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
//        setImageResource(sources[0]);
        downAnimator = ObjectAnimator.ofFloat(this, "rotation", 0, 180)
                .setDuration(500);
        upAnimator = ofFloat(this, "rotation", 180, 0)
                .setDuration(500);
    }

    public void setCursorState(int state, boolean isAnim) {
        if (state == -1 || state >= sources.length) {
            setImageResource(0);
            return;
        }

        setImageResource(sources[state]);

        if (currAnim != isAnim) {
            if (isAnim) {
                downAnimator.start();
            } else {
                upAnimator.start();
            }
        }
        currAnim = isAnim;
    }
}
