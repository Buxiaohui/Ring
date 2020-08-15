/*
 * Copyright (C) 2019 Baidu, Inc. All Rights Reserved.
 */
package com.buxiaohui.example.ring;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RoundImageView extends androidx.appcompat.widget.AppCompatImageView {

    private Path mClipPath;

    private float mRadio = 1;
    private float mRadius = 0;

    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RoundedImageView);
            mRadio = array.getFloat(R.styleable.RoundedImageView_roundedImageViewSizeRadio, mRadio);
            mRadius = array.getDimensionPixelSize(R.styleable.RoundedImageView_roundedImageViewCornerRadius,
                    (int) mRadius);
            array.recycle();
        }
    }

    public void setRadio(float radio) {
        mRadio = radio;
    }

    public void setRadius(float radius) {
        mRadius = radius;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mRadio <= 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = (int) (width / mRadio);
            super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mClipPath == null) {
            mClipPath = new Path();
            mClipPath.addRoundRect(new RectF(0, 0, getWidth(), getHeight()),
                    new float[] {mRadius, mRadius, mRadius, mRadius, mRadius, mRadius, mRadius, mRadius},
                    Path.Direction.CW);
        }
        canvas.clipPath(mClipPath);
        super.onDraw(canvas);
    }

    public static final class Size {
        public final float width;
        public final float height;

        public Size(float width, float height) {
            this.width = width;
            this.height = height;
        }
    }
}
