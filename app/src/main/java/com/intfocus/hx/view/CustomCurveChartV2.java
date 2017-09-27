package com.intfocus.hx.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.intfocus.hx.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 曲线图
 * Created by zbaoliang on 17-5-10.
 */
public class CustomCurveChartV2 extends View implements ValueAnimator.AnimatorUpdateListener {

    private final String TAG = com.intfocus.hx.view.CustomCurveChart.class.getSimpleName();
    private String unit;
    /**
     * 坐标单位
     */
    private String[] xLabel;
    private String[] yLabel;
    /**
     * 曲线数据
     */
    private List<Float[]> dataList;
    private int[] colorList;
    /**
     * 默认边距
     */
    private int margin = 60;
    /**
     * 默认边距
     */
    private float padding = 24;

    /**
     * 柱状图宽度
     */
    private float barWidth = 50;
    /**
     * 原点坐标
     */
    private float xPoint;
    private float yPoint;
    /**
     * X,Y轴的单位长度
     */
    private float xScale;
    private float yScale;

    // 画笔
    /**
     * 轴、Y坐标
     */
    private Paint paintAxes;
    private Paint paintCoordinate;
    private Paint paintCurve;
    private Paint paint_broken;
    private Paint paint_circle;

    private int defauteolor = 0x73737373;
    private int blackColor = 0xff333333;
    private int barSelectColor = 0xff666666;
    private int textSize;

    public interface ChartStyle {
        int BAR = 1;
        int LINE = 2;
    }

    private int mChartStyle = ChartStyle.BAR;

    private int[] orderColors;

    //---------动画--------
    private float ratio = 1;
    private long animateTime = 1200;
    private ValueAnimator mVa;
    private Interpolator mInterpolator = new DecelerateInterpolator();//先加速 后减速

    private ArrayList<Float> xpoints = new ArrayList<>();
    private int selectItem;

    public CustomCurveChartV2(Context context) {
        super(context);
        init();
    }

    public CustomCurveChartV2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getBarSelectColor() {
        return barSelectColor;
    }

    public void setBarSelectColor(int barSelectColor) {
        this.barSelectColor = barSelectColor;
    }

    /**
     * 初始化数据值和画笔
     */
    public void init() {
        paintAxes = new Paint();
        paintAxes.setStyle(Paint.Style.STROKE);
        paintAxes.setAntiAlias(true);
        paintAxes.setDither(true);
        paintAxes.setColor(defauteolor);
        paintAxes.setTextSize(dipToPx(12));

        paintCoordinate = new Paint();
        paintCoordinate.setStyle(Paint.Style.STROKE);
        paintCoordinate.setDither(true);
        paintCoordinate.setAntiAlias(true);
        paintCoordinate.setColor(defauteolor);
        textSize = dipToPx(12);
        paintCoordinate.setTextSize(textSize);

        paintCurve = new Paint();
        paintCurve.setStyle(Paint.Style.STROKE);
        paintCurve.setDither(true);
        paintCurve.setAntiAlias(true);
        paintCurve.setStrokeWidth(4);

        paint_broken = new Paint();
        paint_broken.setAntiAlias(true);
        paint_broken.setStyle(Paint.Style.FILL);
        paint_broken.setStrokeWidth(2);
        paint_broken.setColor(0xffffffff);

        paint_circle = new Paint();
        paint_circle.setAntiAlias(true);
        paint_circle.setStyle(Paint.Style.FILL);
        paint_circle.setStrokeWidth(4);
        paint_circle.setStrokeCap(Paint.Cap.ROUND);

        orderColors = getResources().getIntArray(R.array.co_order);
        blackColor = getResources().getColor(R.color.co3);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawTopLine(canvas, paintAxes);
        drawAxesLine(canvas, paintAxes);
        drawCoordinate(canvas, paintCoordinate);

        for (int i = 0; i < dataList.size(); i++) {
            int color;
            if (orderColors == null || orderColors.length == 0)
                color = defauteolor;
            else
                color = orderColors[i];

            if (mChartStyle == ChartStyle.LINE)
                drawLine(canvas, paintCurve, dataList.get(i), color);
            else
                drawBAR(canvas, i, color);
        }
    }

    private void drawTopLine(Canvas canvas, Paint paintAxes) {
        canvas.drawLine(0, 0, this.getWidth(), 0, paintAxes);
    }

    /**
     * 绘制坐标轴
     */
    private void drawAxesLine(Canvas canvas, Paint paint) {
        // X
        canvas.drawLine(xPoint - barWidth, yPoint + 10, this.getWidth(), yPoint + 10, paint);
    }

    /**
     * 绘制刻度
     *
     * @param canvas
     * @param paint
     */
    private void drawCoordinate(Canvas canvas, Paint paint) {
        // X轴坐标
        paint.setTextAlign(Paint.Align.CENTER);

        int xlength = xLabel.length;
        for (int i = 0; i < xlength; i++) {

            if (i % 2 == 0) {
                continue;
            }

            float startX = 0;
            if (mChartStyle == ChartStyle.BAR)
                startX = barWidth / 2;
            startX += xPoint + i * xScale;

            int color;
            if (selectItem == i && mChartStyle == ChartStyle.BAR)
                color = blackColor;
            else {
                if (colorList == null)
                    color = defauteolor;
                else {
                    if (colorList == null || i > colorList.length - 1)
                        color = defauteolor;
                    else
                        color = getRelativeColor(colorList[i]);
                }
            }

            paint.setColor(color);
//            canvas.drawText(xLabel[i], startX, this.getHeight() - padding / 2 - textSize / 2, paint);
            canvas.drawText(xLabel[i], startX, yPoint + 40, paint);
            xpoints.add(startX);
        }

        // Y轴坐标
        paintAxes.setTextAlign(Paint.Align.LEFT);
        Rect rect = new Rect();
        String maxT = yLabel[yLabel.length - 1];
        paintAxes.getTextBounds(maxT, 0, maxT.length(), rect);
        float textw = rect.width();
        float textH = rect.height();
        int ylsize = yLabel.length;
        for (int i = 1; i < ylsize; i++) {
            Float ylabel = Float.parseFloat(yLabel[i]);
            float startY = toY(ylabel);
            canvas.drawText(yLabel[i], margin, startY + textH / 2, paintAxes);
        }
        float unitoffect = margin + padding;
        canvas.drawText(unit, unitoffect + textw / 2, margin + textH / 2, paintAxes);
    }

    /**
     * 获取线段相应颜色
     *
     * @param index
     * @return
     */
    private int getRelativeColor(int index) {
        if (orderColors == null || orderColors.length == 0 || orderColors.length < index - 1)
            return defauteolor;
        return orderColors[index];
    }

    /**
     * 绘制曲线
     */
    private void drawLine(Canvas canvas, Paint paint, Float[] data, int drawColor) {
        paint.setColor(drawColor);
        Path path = new Path();
        int xsize = xLabel.length;
        int dsize = data.length;
        int loopingcont = xsize > dsize ? dsize : xsize;
        for (int i = 0; i < loopingcont; i++) {
            float yPoint = toY(data[i]);
            if (i == 0) {
                path.moveTo(xPoint, yPoint);
            } else {
                path.lineTo(xPoint + i * xScale * ratio, yPoint);
            }

            if (i == xLabel.length - 1) {
                path.lineTo(xPoint + i * xScale * ratio, yPoint);
            }
        }
        canvas.drawPath(path, paint);

        if (ratio == 1) {
            for (int i = 0; i < loopingcont; i++) {
                float yPoint;
                if (i == 0)
                    yPoint = toY(data[0]);
                else
                    yPoint = toY(data[i]);

                if (i == selectItem) {
                    paint_circle.setColor(drawColor);
                    paint_circle.setAlpha(26);
                    canvas.drawCircle(xPoint + i * xScale, yPoint, dipToPx(6f), paint_circle);
                    paint_circle.setColor(drawColor);
                    canvas.drawCircle(xPoint + i * xScale, yPoint, dipToPx(3.6f), paint_circle);
                    canvas.drawCircle(xPoint + i * xScale, yPoint, dipToPx(2f), paint_broken);
                }
            }
        }
    }

    /**
     * 绘制柱状图
     *
     * @param canvas
     * @param drawColor
     */
    private void drawBAR(Canvas canvas, int dataIndex, int drawColor) {
        Float[] data = dataList.get(dataIndex);
        paintCurve.setStyle(Paint.Style.FILL);
        int xsize = xLabel.length;
        int dsize = data.length;
        int loopingcont = xsize > dsize ? dsize : xsize;
        float offset = barWidth / 2;
        float left;
        float top;
        float right;
        float bottom;
        for (int i = 0; i < loopingcont; i++) {
            float startX = xPoint + i * xScale;
            left = startX - offset + (dataIndex * barWidth);
            top = toY(data[i]) * (1 + (1 - ratio));

            right = startX + offset + (dataIndex * barWidth);
            bottom = yPoint;

            RectF rectF = new RectF(left, top, right, bottom);
            if (i == selectItem) {
                switch (dataIndex) {
                    case 0:
                        paintCurve.setColor(barSelectColor);
                        break;
                    case 1:
                        paintCurve.setColor(orderColors[dataIndex]);
                        break;
                }

            } else
                paintCurve.setColor(defauteolor);
            canvas.drawRect(rectF, paintCurve);
        }
    }

    /**
     * 数据按比例转坐标
     */
    private float toY(float num) {
        float y;
        try {
            y = yPoint - num * yScale;
            if (num < 0)
                y = Math.abs(y);
        } catch (Exception e) {
            return 0;
        }
        return y;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                onActionUpEvent(event);
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    private void onActionUpEvent(MotionEvent event) {
        boolean isValidTouch = validateTouch(event.getX(), event.getY());
        if (isValidTouch) {
            if (listener != null)
                listener.onPointClick(selectItem);
            invalidate();
        }
    }

    /**
     * 是否是有效的触摸范围
     *
     * @param x
     * @param y
     * @return
     */
    private boolean validateTouch(float x, float y) {
        //曲线触摸区域
        for (int i = 0; i < xpoints.size(); i++) {
            // dipToPx(8)乘以2为了适当增大触摸面积
            switch (mChartStyle) {
                case ChartStyle.LINE:
                    if (x > (xpoints.get(i) - dipToPx(8) * 2) && x < (xpoints.get(i) + dipToPx(8) * 2)) {
                        selectItem = i;
                        if (listener != null)
                            listener.onPointClick(selectItem);
                        return true;
                    }
                    break;

                case ChartStyle.BAR:
                    if (x > (xpoints.get(i) - barWidth) && x < (xpoints.get(i) + barWidth)) {

                        selectItem = i;
                        if (listener != null)
                            listener.onPointClick(selectItem);
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    /**
     * dip 转换成px
     *
     * @param dip
     * @return
     */
    private int dipToPx(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }

    private PointClickListener listener;

    public void setPointClickListener(PointClickListener listener) {
        this.listener = listener;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (dataList.size() > 0) {
            animateExcels();
        }
    }

    public void animateExcels() {
        if (mVa == null) {
            mVa = ValueAnimator.ofFloat(0, 1).setDuration(animateTime);
            mVa.addUpdateListener(this);
            mVa.setInterpolator(mInterpolator);
        }
        mVa.start();
    }

    /**
     * 设置默认颜色
     *
     * @param defauteolor
     */
    public void setDefauteolor(@ColorInt int defauteolor) {
        this.defauteolor = defauteolor;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        ratio = (float) animation.getAnimatedValue();
        postInvalidate();
    }

    public interface PointClickListener {
        void onPointClick(int index);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        onMeasureScale();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = (int) (sizeWidth * 0.5);
        setMeasuredDimension(sizeWidth, sizeHeight);
    }


    /**
     * 测量比例
     */
    private void onMeasureScale() {
        int textW = 0;
        int textH = 0;
        if (yLabel != null && yLabel.length != 0) {
            String lable = yLabel[yLabel.length - 1];
            Rect rect = new Rect();
            paintAxes.getTextBounds(lable, 0, lable.length(), rect);
            textW = rect.width();
            textH = rect.height();
        }

        xPoint = margin + padding + textW + barWidth;
        yPoint = getHeight() - margin - padding;
        xScale = (getWidth() - xPoint - margin * 2) / (xLabel.length - 1);
        yScale = (getHeight() - margin * 2 - padding * 2 - textH) / Float.valueOf(yLabel[yLabel.length - 1]);
    }

    public void setDefauteMargin(int defauteMargin) {
        this.margin = defauteMargin;
    }

    /**
     * 设置图标模式
     *
     * @param chartStyle
     */
    public void setCharStytle(int chartStyle) {
        mChartStyle = chartStyle;
    }

    /**
     * 设置条柱宽度
     *
     * @param barWidth
     */
    public void setBarWidth(int barWidth) {
        this.barWidth = barWidth;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setxLabel(String[] xLabel) {
        this.xLabel = xLabel;
    }

    public void setyLabel(String[] yLabel) {
        this.yLabel = yLabel;
    }

    public int setDataList(@NonNull List<Float[]> dataList) {
        this.dataList = dataList;
        if (dataList.size() > 0) {
            selectItem = dataList.get(0).length - 1;
        }
        return selectItem;
    }

    public void setSelectItem(int index) {
        selectItem = index;
    }

    public void setColorList(int[] colorList) {
        this.colorList = colorList;
    }
}
