package com.intfocus.hx.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.intfocus.hx.R;

/**
 * 可以设置布局的宽高比 Created by higgses on 14-5-18.
 */
public class WidthHeightLinearLayou extends LinearLayout {
	/**
	 * 控件宽带比重
	 */
	private float widthWeight;
	/**
	 * 控件高度比重
	 */
	private float heightWeight;
	/**
	 * 比重计算后额外的宽度
	 */
	private float extractWidth;
	/**
	 * 比重计算后额外的宽度
	 */
	private float extractHeight;

	public WidthHeightLinearLayou(Context context) {
		super(context);
	}

	public WidthHeightLinearLayou(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public WidthHeightLinearLayou(Context context, AttributeSet attrs,
                                  int defStyle) {
		super(context, attrs, defStyle);
	}

	private void init(Context context, AttributeSet attrs) {
		TypedArray array = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.WidthHeightFrameLayout, 0, 0);
		try {
			widthWeight = array.getFloat(
					R.styleable.WidthHeightFrameLayout_width_weight, 0);
			extractWidth = array.getDimensionPixelSize(
					R.styleable.WidthHeightFrameLayout_extract_width, 0);
			heightWeight = array.getFloat(
					R.styleable.WidthHeightFrameLayout_height_weight, 0);
			extractHeight = array.getDimensionPixelSize(
					R.styleable.WidthHeightFrameLayout_extract_height, 0);
		} catch (Exception e) {

		} finally {
			array.recycle();
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		measure(MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY),
				MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY));
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		if (widthWeight != 0 && heightWeight != 0) {
			heightSize = (int) (widthSize * heightWeight / widthWeight);
		}
		widthSize += extractWidth;
		heightSize += extractHeight;
		super.onMeasure(
				MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY),
				MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY));
	}

	public float getWidthWeight() {
		return widthWeight;
	}

	public void setWidthWeight(float widthWeight) {
		this.widthWeight = widthWeight;
		invalidate();
		requestLayout();
	}

	public float getHeightWeight() {
		return heightWeight;
	}

	public void setHeightWeight(float heightWeight) {
		this.heightWeight = heightWeight;
		invalidate();
		requestLayout();
	}
}
