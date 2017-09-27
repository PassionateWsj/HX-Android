package com.intfocus.hx.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import com.intfocus.hx.constant.Colors;

/**
 * 仪表盘箭头
 * Created by zbaoliang on 17-4-29.
 */
public class MeterCursor extends android.support.v7.widget.AppCompatImageView {

    private int[] colors = Colors.INSTANCE.getColorsRGY();

    private int state = 0;

    private Path pathLine = new Path();
    Paint paint = new Paint();

    public MeterCursor(Context context) {
        super(context);
        init();
    }

    public MeterCursor(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MeterCursor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint.setStyle(Paint.Style.FILL);
    }

    public void setCursorState(int state) {
        this.state = state;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(colors[state]);
        pathLine.reset();
        int h = canvas.getHeight();
        int w = canvas.getWidth();
        if (state == 0)
            return;
        if (state < 3) {//上箭头
            pathLine.moveTo(0, h);
            pathLine.lineTo(w, h);
            pathLine.lineTo(w / 2, h % 8);
        } else {
            pathLine.moveTo(0, 0);
            pathLine.lineTo(w, 0);
            pathLine.lineTo(w / 2, h - h % 8);
        }
        canvas.drawPath(pathLine, paint);
    }
}
