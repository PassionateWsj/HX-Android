package com.intfocus.hx.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.view.MotionEvent;
import android.view.View;

import com.intfocus.hx.constant.Colors;

import java.util.ArrayList;
import java.util.List;

/**
 * 曲线图
 */
public class CustomCurveChart extends View {

    // 坐标单位
    private String[] xLabel;
    private String[] yLabel;
    // 曲线数据
    private List<Float[]> dataList;
    private int[] colorList;
    // 默认边距
    private int margin = 60;
    // 距离左边偏移量
    private int marginX = 90;
    // 原点坐标
    private int xPoint;
    private int yPoint;
    // X,Y轴的单位长度
    private int xScale;
    private int yScale;
    // 画笔
    private Paint paintAxes;
    private Paint paintCoordinate;
    private Paint paintTable;
    private Paint paintCurve;
    private Paint paintRectF;
    private Paint paintValue;
    private Paint paint_broken;
    private Paint paint_circle;

    private int defauteolor = 0x73737373;

    private int[] lineColors = new int[]{0xFF71A3ED, 0xFF9F7CC7, 0xFFF56341};


    private ArrayList<Integer> xpoints = new ArrayList<>();
    private int selectItem;

    public CustomCurveChart(Context context, String[] xLabel, String[] yLabel,
                            List<Float[]> dataList, int[] colorList) {
        super(context);
        this.xLabel = xLabel;
        this.yLabel = yLabel;
        this.dataList = dataList;
        this.colorList = colorList;
        selectItem = xLabel.length - 1;
        init();
    }

    public CustomCurveChart(Context context) {
        super(context);
    }

    /**
     * 初始化数据值和画笔
     */
    public void init() {
        paintAxes = new Paint();
        paintAxes.setStyle(Paint.Style.STROKE);
        paintAxes.setAntiAlias(true);
        paintAxes.setDither(true);
        paintAxes.setAntiAlias(true);
        paintAxes.setColor(defauteolor);
        paintAxes.setTextSize(dipToPx(12));

        paintCoordinate = new Paint();
        paintCoordinate.setStyle(Paint.Style.STROKE);
        paintCoordinate.setDither(true);
        paintCoordinate.setAntiAlias(true);
        paintCoordinate.setColor(defauteolor);
        paintCoordinate.setTextSize(dipToPx(12));

        paintTable = new Paint();
        paintTable.setStyle(Paint.Style.STROKE);
        paintTable.setAntiAlias(true);
        paintTable.setDither(true);
        paintTable.setColor(defauteolor);
        paintTable.setStrokeWidth(2);

        paintCurve = new Paint();
        paintCurve.setStyle(Paint.Style.STROKE);
        paintCurve.setDither(true);
        paintCurve.setAntiAlias(true);
        paintCurve.setStrokeWidth(3);
        PathEffect pathEffect = new CornerPathEffect(25);
        paintCurve.setPathEffect(pathEffect);

        paintRectF = new Paint();
        paintRectF.setStyle(Paint.Style.FILL);
        paintRectF.setDither(true);
        paintRectF.setAntiAlias(true);
        paintRectF.setStrokeWidth(3);

        paintValue = new Paint();
        paintValue.setStyle(Paint.Style.STROKE);
        paintValue.setAntiAlias(true);
        paintValue.setDither(true);
        paintValue.setColor(defauteolor);
        paintValue.setTextAlign(Paint.Align.CENTER);
        paintValue.setTextSize(15);

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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(0xffffffff);
        drawAxesLine(canvas, paintAxes);
        drawCoordinate(canvas, paintCoordinate);
        for (int i = 0; i < dataList.size(); i++) {
            drawCurve(canvas, paintCurve, dataList.get(i), lineColors[i]);
        }
    }

    /**
     * 绘制坐标轴
     */
    private void drawAxesLine(Canvas canvas, Paint paint) {
        // X
        canvas.drawLine(0, getHeight() - margin, this.getWidth(), getHeight() - margin, paint);
    }

    /**
     * 绘制刻度
     */
    private void drawCoordinate(Canvas canvas, Paint paint) {
        // X轴坐标
        for (int i = 0; i <= (xLabel.length - 1); i++) {
            paint.setTextAlign(Paint.Align.CENTER);
            int startX = xPoint + i * xScale;
            paint.setColor(getRelativeColor(i));
            canvas.drawText(xLabel[i], startX, this.getHeight() - margin / 6, paint);
            xpoints.add(startX);
        }

        // Y轴坐标
        for (int i = 1; i <= (yLabel.length - 1); i++) {
            paintAxes.setTextAlign(Paint.Align.LEFT);
            Float ylabel = Float.parseFloat(yLabel[i]);
            float startY = toY(ylabel.intValue());
            int offsetX;
            switch (yLabel[i].length()) {
                case 1:
                    offsetX = 28;
                    break;

                case 2:
                    offsetX = 20;
                    break;

                case 3:
                    offsetX = 12;
                    break;

                case 4:
                    offsetX = 5;
                    break;

                default:
                    offsetX = 0;
                    break;
            }
            int offsetY;
            if (i == 0) {
                offsetY = 0;
            } else {
                offsetY = margin / 5;
            }
            // x默认是字符串的左边在屏幕的位置，y默认是字符串是字符串的baseline在屏幕上的位置
            canvas.drawText(yLabel[i], margin / 4 + offsetX, startY + offsetY, paintAxes);
        }
    }

    /**
     * 获取线段相应颜色
     *
     * @param index
     * @return
     */
    private int getRelativeColor(int index) {
        int color;
        switch (colorList[index]) {
            case 1:
            case 2:
                color = Colors.INSTANCE.getColorsRGY()[0];
                break;

            case 3:
            case 4:
                color = Colors.INSTANCE.getColorsRGY()[1];
                break;

            case 5:
            case 6:
                color = Colors.INSTANCE.getColorsRGY()[2];
                break;
            default:
                color = defauteolor;
        }
        return color;
    }

    /**
     * 绘制曲线
     */
    private void drawCurve(Canvas canvas, Paint paint, Float[] data, int drawColor) {
        paint.setColor(drawColor);
        Path path = new Path();
        int xsize = xLabel.length;
        int dsize = data.length;
        int loopingcont = xsize > dsize ? dsize : xsize;
        for (int i = 0; i < loopingcont; i++) {
            float yPoint;

            if (i == 0) {
                yPoint = toY(data[0]);
                path.moveTo(xPoint, yPoint);
            } else {
                yPoint = toY(data[i]);
                path.lineTo(xPoint + i * xScale, yPoint);
            }

            if (i == xLabel.length - 1) {
                path.lineTo(xPoint + i * xScale, yPoint);
            }
        }
        canvas.drawPath(path, paint);

        for (int i = 0; i < loopingcont; i++) {
            float yPoint;
            if (i == 0)
                yPoint = toY(data[0]);
            else
                yPoint = toY(data[i]);

            if (i == selectItem) {
                paint_circle.setColor(drawColor & 64);
                canvas.drawCircle(xPoint + i * xScale, yPoint, dipToPx(8f), paint_circle);
                paint_circle.setColor(drawColor);
                canvas.drawCircle(xPoint + i * xScale, yPoint, dipToPx(6f), paint_circle);
                canvas.drawCircle(xPoint + i * xScale, yPoint, dipToPx(4f), paint_broken);
            }
        }
    }

    /**
     * 数据按比例转坐标
     */
    private float toY(float num) {
        float y;
        try {
            float a = num / 100.0f;
            y = yPoint - a * yScale;
        } catch (Exception e) {
            return 0;
        }
        return y;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.getParent().requestDisallowInterceptTouchEvent(true);
        //一旦底层View收到touch的action后调用这个方法那么父层View就不会再调用onInterceptTouchEvent了，也无法截获以后的action
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                onActionUpEvent(event);
                this.getParent().requestDisallowInterceptTouchEvent(false);
                break;
            case MotionEvent.ACTION_CANCEL:
                this.getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return true;
    }

    private void onActionUpEvent(MotionEvent event) {
        boolean isValidTouch = validateTouch(event.getX(), event.getY());
        if (isValidTouch) {
            invalidate();
        }
    }

    //是否是有效的触摸范围
    private boolean validateTouch(float x, float y) {
        //曲线触摸区域
        for (int i = 0; i < xpoints.size(); i++) {
            // dipToPx(8)乘以2为了适当增大触摸面积
            if (x > (xpoints.get(i) - dipToPx(8) * 2) && x < (xpoints.get(i) + dipToPx(8) * 2)) {
                selectItem = i;
                if (listener != null)
                    listener.onPointClick(selectItem);
                return true;
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

    public interface PointClickListener {
        void onPointClick(int index);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        xPoint = margin + marginX;
        yPoint = getHeight() - margin;
        xScale = (getWidth() - 2 * margin - marginX) / (xLabel.length - 1);
        yScale = (getHeight() - 2 * margin) / yLabel.length;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = (int) (sizeWidth * 0.5);
        setMeasuredDimension(sizeWidth, sizeHeight);
    }
}
