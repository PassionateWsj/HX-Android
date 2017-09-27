package com.intfocus.hx.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.intfocus.hx.R;
import com.intfocus.hx.util.DisplayUtil;

/**
 * 排序按钮
 * Created by zbaoliang on 17-5-16.
 */
public class SortCheckBox extends View {
    private Context ctx;
    private Paint paint;

    private int sort_upicon = 0;
    private int sort_downicon = 0;
    private int sort_noneicon = 0;

    public interface CheckedState {
        int sort_upicon = 0;
        int sort_downicon = 1;
        int sort_noneicon = 2;
    }

    private int checkedState = CheckedState.sort_noneicon;

    private float drawablePadding = 0;
    /**
     * 左0,右1
     */
    private int sort_placeicon = 1;
    private String text;
    private int textSize;
    private int textColor;

    private boolean ischecked;

    private OnClickListener mClickListener;
    private float textWidth;
    private float textHeight;

    private PointF originP;
    private Bitmap mBitmap;

    private SortViewSizeListener sizeListener;

    public void setText(String text) {
        this.text = text;
    }

    public SortCheckBox(Context context) {
        super(context);
        ctx = context;
        initPaint();
    }

    public SortCheckBox(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        ctx = context;
        init(context, attrs);
    }

    public SortCheckBox(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ctx = context;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initAttrs(attrs);
        initPaint();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(textColor);
        paint.setTextSize(textSize - 10);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.LEFT);

        mBitmap = BitmapFactory.decodeResource(getResources(), sort_noneicon);
    }

    private void initAttrs(AttributeSet attrs) {
        int[] attrsArray = new int[]{
                android.R.attr.textColor,
                android.R.attr.textSize,
                android.R.attr.text,
                android.R.attr.drawablePadding,

                R.attr.sort_upicon,
                R.attr.sort_downicon,
                R.attr.sort_noneicon,
                R.attr.sort_placeicon,
                R.attr.checked,
        };
        TypedArray array = ctx.getTheme().obtainStyledAttributes(attrs,
                attrsArray, 0, 0);
        textColor = array.getColor(0, Color.BLACK);
        textSize = array.getDimensionPixelSize(1, 20);
        textSize = DisplayUtil.sp2px(getContext(), textSize);
        text = array.getString(2);
        drawablePadding = array.getDimensionPixelSize(3, 0);
        sort_upicon = array.getResourceId(4, 0);
        sort_downicon = array.getResourceId(5, 0);
        sort_noneicon = array.getResourceId(6, 0);
        sort_placeicon = array.getInt(7, 0);
        ischecked = array.getBoolean(8, false);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        textWidth = rect.width();
        textHeight = rect.height();

        int bitmapW = mBitmap.getWidth();
        int width = (int) (textWidth + bitmapW + drawablePadding + getPaddingLeft() + getPaddingRight());
        setMeasuredDimension(width, sizeHeight);
        originP = new PointF(getPaddingLeft(), sizeHeight / 2 - textHeight / 2);
        if (sizeListener != null) {
            sizeListener.onSortViewSize(width, getTag());
        }
    }

    /**
     * 重置状态
     */
    public void reset() {
        ischecked = false;
        checkedState = CheckedState.sort_noneicon;
        mBitmap = BitmapFactory.decodeResource(getResources(), sort_noneicon);
        invalidate();
    }

    /**
     * 设置选中状态
     *
     * @param ischecked
     */
    public void setChecked(Boolean ischecked) {
        this.ischecked = ischecked;
        if (mClickListener != null)
            mClickListener.onClick(this);

        if (ischecked) {
            mBitmap.recycle();
            checkedState = CheckedState.sort_downicon;
            mBitmap = BitmapFactory.decodeResource(getResources(), sort_downicon);
        } else {
            mBitmap.recycle();
            checkedState = CheckedState.sort_upicon;
            mBitmap = BitmapFactory.decodeResource(getResources(), sort_upicon);
        }
        invalidate();
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
        mClickListener = l;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (x + getLeft() < getRight() && y + getTop() < getBottom()) {
                    if (ischecked)
                        ischecked = false;
                    else
                        ischecked = true;

                    setChecked(ischecked);
                }
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(text, originP.x, originP.y + textHeight - drawablePadding / 2, paint);
        float left = originP.x + textWidth + drawablePadding;
        float top = getHeight() / 2 - mBitmap.getHeight() / 2;
        canvas.drawBitmap(mBitmap, left, top, paint);
    }

    /**
     * 获取当前选中状态
     *
     * @return
     */
    public int getCheckedState() {
        return checkedState;
    }

    public interface SortViewSizeListener {
        void onSortViewSize(int w, Object tag);
    }

    public void setOnSortViewSizeListener(SortViewSizeListener sizeListener) {
        this.sizeListener = sizeListener;
    }
}
