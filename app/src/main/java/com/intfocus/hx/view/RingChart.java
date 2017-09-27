package com.intfocus.hx.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.intfocus.hx.R;
import com.intfocus.hx.util.DisplayUtil;

import java.text.DecimalFormat;

/**
 * 环形经度表
 * Created by zbaoliang on 17-4-29.
 */
public class RingChart extends View {

    // 画实心圆的画笔
    private Paint mCirclePaint;
    // 画圆环的画笔
    private Paint mRingDefaultPaint;
    // 已用环的画笔
    private Paint mUsePaint;
    // 画圆环的画笔
    private Paint mRingPaint;
    // 画白线的画笔
    private Paint mLinePaint;
    // 画字体的画笔
    private Paint mTextPaint;
    // 圆形颜色
    private int mCircleColor;
    // 圆环颜色
    private int mRingColor;
    // 半径
    private float mRadius;
    // 圆环半径
    private float mRingRadius;
    // 圆环宽度
    private float mStrokeWidth;
    // 圆心x坐标
    private int mXCenter;
    // 圆心y坐标
    private int mYCenter;
    // 字的长度
    private float mTxtWidth;
    // 字的高度
    private float mTxtHeight;
    // 总进度
    private double mTotalProgress = 100;
    // 当前进度
    private double mProgress;
    // 实际展示总进度
    private double mShowProgress;
    // 单位
    private String unit;


    private int viewWidth = 100;
    private int viewHeight = 100;

    private Context mContext;

    private Handler circleHandler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                double temp = (Double) msg.obj;
                setProgress(temp);
            }
        }
    };

    public RingChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        // 获取自定义的属性
        initAttrs(context, attrs);
        initVariable();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.RingChart, 0, 0);
        mStrokeWidth = typeArray.getDimension(R.styleable.RingChart_strokeWidth, 10);
        mRadius = typeArray.getDimension(R.styleable.RingChart_radius, 80);
        mRingRadius = mRadius + mStrokeWidth / 2;

        mCircleColor = typeArray.getColor(R.styleable.RingChart_circleColor, 0xFFFFFFFF);
        mRingColor = typeArray.getColor(R.styleable.RingChart_ringColor, 0xFFFFFFFF);
    }

    private void initVariable() {
        //画圆画笔设置
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);//防锯齿
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setStyle(Paint.Style.FILL);

        //“使用”字画笔设置
        mUsePaint = new Paint();
        mUsePaint.setAntiAlias(true);
        mUsePaint.setStyle(Paint.Style.FILL);
        mUsePaint.setColor(getResources().getColor(R.color.argb_font_94));
        mUsePaint.setTextSize(DisplayUtil.sp2px(mContext, 20));

        //圆环画笔设置
        mRingDefaultPaint = new Paint();
        mRingDefaultPaint.setAntiAlias(true);
        mRingDefaultPaint.setColor(getResources().getColor(R.color.default_ring_color));
        mRingDefaultPaint.setStyle(Paint.Style.STROKE);
        mRingDefaultPaint.setStrokeWidth(mStrokeWidth);

        //已使用多少圆环画笔设置
        mRingPaint = new Paint();
        mRingPaint.setAntiAlias(true);
        mRingPaint.setColor(mRingColor);
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setStrokeWidth(mStrokeWidth);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(getResources().getColor(R.color.argb_unitcolor));
        mTextPaint.setTextSize(DisplayUtil.sp2px(mContext, 36));

        mLinePaint = new Paint();
        mLinePaint.setColor(Color.WHITE);


        //获取字体高度
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        mTxtHeight = (int) Math.ceil(fm.descent - fm.ascent);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        mXCenter = viewWidth / 2;
        mYCenter = viewHeight / 2;
        //画圆
        canvas.drawCircle(mXCenter, mYCenter, mRadius, mCirclePaint);

        float left = mXCenter - mRingRadius;
        float top = mYCenter - mRingRadius;
        float right = mRingRadius * 2 + (mXCenter - mRingRadius);
        float bottom = mRingRadius * 2 + (mYCenter - mRingRadius);
        RectF oval = new RectF(left, top, right, bottom);

        //画整圆弧
        canvas.drawArc(oval, -90, 360, false, mRingDefaultPaint);
        //已使用多少圆弧
        canvas.drawArc(oval, -90, (float) ((mProgress / mTotalProgress) * 360), false, mRingPaint);
        String txt = "0";
        if (mProgress != 0) {
            //文字绘制
            DecimalFormat df = new DecimalFormat("#.00");
            float f = (float) ((mProgress / mTotalProgress) * 100);
            txt = df.format(f);
            //文字的长度
            mTxtWidth = mTextPaint.measureText(txt, 0, txt.length());
        }
        canvas.drawText(txt, mXCenter - mTxtWidth / 2, mYCenter + mTxtHeight / 6, mTextPaint);

        if (!TextUtils.isEmpty(unit)) {
            Rect _pb = new Rect();
            mUsePaint.getTextBounds(unit, 0, unit.length(), _pb);
            float perX = mXCenter - mUsePaint.measureText(unit) / 2;
            float perY = bottom - mStrokeWidth * 2 - _pb.height() / 2;
            canvas.drawText(unit, perX, perY, mUsePaint);
        }
    }

    /**
     * 设置当前进度
     *
     * @param progress
     */
    public void setProgress(double progress) {
        mProgress = progress;
        postInvalidate();
    }

    /**
     * 实际展示总进度
     *
     * @param progress
     */
    public void setShowProgress(double progress) {
        mShowProgress = progress;
        new Thread(new CircleThread()).start();
    }

    /**
     * 设置单位
     *
     * @param unit
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setMaxProgress(double maxProgress) {
        this.mTotalProgress = maxProgress;
    }


    private class CircleThread implements Runnable {

        double m = mTotalProgress / 50;
        double i = 0;

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Message msg = new Message();
                    msg.what = 1;
                    i += m;
                    if (i <= mShowProgress) {
                        msg.obj = i;
                        circleHandler.sendMessage(msg);
                    } else {
                        i = mShowProgress;
                        msg.obj = i;
                        circleHandler.sendMessage(msg);
                        return;
                    }
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;

        //        int ll = (int) ((mRadius + mStrokeWidth) * 2);
        if (viewWidth >= viewHeight) {
//            if (viewHeight - ll < 0) {
            mRadius = (viewHeight - (mStrokeWidth) * 2) / 2;
            mRingRadius = mRadius + mStrokeWidth / 2;
//            }
        } else if (viewWidth < viewHeight) {
//            if (viewWidth - mRadius * 2 < 0) {
            mRadius = viewWidth - mStrokeWidth * 2;
            mRingRadius = mRadius + mStrokeWidth / 2;
//            }
        }
    }

/*    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        viewWidth = measure(widthMeasureSpec, true);
        viewHeight = measure(heightMeasureSpec, false);
        setMeasuredDimension(viewWidth, viewHeight);
    }
    private int measure(int measureSpec, boolean isWidth) {
        int result;
        int size = MeasureSpec.getSize(measureSpec);
        int mode = MeasureSpec.getMode(measureSpec);
        int padding = isWidth ? getPaddingLeft() + getPaddingRight() : getPaddingTop() + getPaddingBottom();
        if (mode == MeasureSpec.EXACTLY) {//表示用户设定了大小
            result = size;
        } else {
            result = isWidth ? getSuggestedMinimumWidth() : getSuggestedMinimumHeight();
            result += padding;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.max(result, size);
            }
        }
        return result;
    }*/
}
