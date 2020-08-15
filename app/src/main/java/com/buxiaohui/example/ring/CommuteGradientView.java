/*
 * Copyright (C) 2020 Baidu, Inc. All Rights Reserved.
 */
package com.buxiaohui.example.ring;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;

public class CommuteGradientView extends View {
    public CommuteGradientView(Context context) {
        super(context);
    }

    public CommuteGradientView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CommuteGradientView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private LinearGradient mLinearGradient;
    private Paint mPaint;

    private int mDx;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPaint == null) {
            mPaint = new Paint();
        }
        if (mLinearGradient == null) {
            mLinearGradient =
                    new LinearGradient(mDx - getMeasuredWidth() / 3, getMeasuredHeight(), getMeasuredWidth() / 3,
                            getMeasuredHeight(),
                            new int[] {Color.parseColor("#CFCFCF"),  Color.parseColor("#EDEDED"), Color.parseColor("#CFCFCF")}, null,
                            LinearGradient.TileMode.CLAMP);
        }

        Matrix matrix = new Matrix();
        matrix.setTranslate(mDx, 0);
        mLinearGradient.setLocalMatrix(matrix);
        mPaint.setShader(mLinearGradient);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        ValueAnimator animator = ValueAnimator.ofInt(0, getMeasuredWidth());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mDx = (Integer) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.setRepeatMode(ValueAnimator.REVERSE);
        // animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setDuration(1_000);
        animator.start();
    }
}
