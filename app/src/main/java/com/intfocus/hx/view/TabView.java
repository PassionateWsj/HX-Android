package com.intfocus.hx.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.intfocus.hx.R;
import com.intfocus.hx.util.DisplayUtil;


/**
 * Created by wiky on 1/10/16.
 */
public class TabView extends LinearLayout {
    private boolean mActive;
    private Drawable mDrawable;
    private Drawable mActiveDrawable;
    private ImageView mImageView;
    private TextView mTextView;

    public TabView(Context context) {
        this(context, null);
    }

    public TabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initialize(context, attrs);
    }

    private void initialize(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabView);
        mActive = a.getBoolean(R.styleable.TabView_active, false);
        mDrawable = a.getDrawable(R.styleable.TabView_src);
        mActiveDrawable = a.getDrawable(R.styleable.TabView_active_src);
        String mText = a.getString(R.styleable.TabView_text);
        int srcHeight = a.getDimensionPixelOffset(R.styleable.TabView_src_height, DisplayUtil.dip2px(context, 28));
        a.recycle();

        setOrientation(VERTICAL);
        mImageView = new ImageView(context);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, srcHeight);
        layoutParams.topMargin = 5;
        addView(mImageView, layoutParams);
        mTextView = new TextView(context);
        mTextView.setText(mText);
        mTextView.setTextSize(10);
        mTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.bottomMargin = 6;
        addView(mTextView, layoutParams);

        update();
    }

    private void update() {
        if (mActive) {
            mImageView.setImageDrawable(mActiveDrawable);
            mTextView.setTextColor(getResources().getColor(R.color.co1_syr));
        }
        else {
            mImageView.setImageDrawable(mDrawable);
            mTextView.setTextColor(Color.BLACK);
        }
    }

    public void setActive(boolean active) {
        mActive = active;
        update();
    }
}
