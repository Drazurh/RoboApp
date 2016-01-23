package com.robodoot.roboapp;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Alex on 1/16/2016.
 */
public class BatteryView extends View {
    private float mCharge = 1.0f;
    private boolean mShowPercent;
    private float mTextHeight = 0.0f;
    private float mWidth = 0.0f;
    private float mHeight = 0.0f;
    private int mHighColor = Color.GREEN;
    private int mLowColor = Color.RED;

    // http://freevector.co/vector-icons/interface/empty-battery-2.html
    Bitmap mBatteryOutline;

    // event listener for when charge changes
    OnChargeChangedListener mListener;
    public interface OnChargeChangedListener {
        void onEvent();
    }
    public void setChargeChangedListener(OnChargeChangedListener eventListener) {
        mListener = eventListener;
    }

    // canvas objects

    // paint objects
    Paint mOutlinePaint;
    Paint mChargeBarPaint;
    Paint mPercentPaint;

    public BatteryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.BatteryView,
                0, 0);

        try {
            mShowPercent = a.getBoolean(R.styleable.BatteryView_batteryShowPercent, true);
        } finally {
            a.recycle();
        }

        init();
    }

    private void init() {
        mOutlinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOutlinePaint.setColor(Color.WHITE);

        mChargeBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mChargeBarPaint.setColor((int)new ArgbEvaluator().evaluate(mCharge, mLowColor, mHighColor));
        mChargeBarPaint.setStyle(Paint.Style.FILL);

        mPercentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPercentPaint.setColor(Color.BLACK);
        mPercentPaint.setTypeface(Typeface.DEFAULT);
        mPercentPaint.setTextSize(mTextHeight);
        //mPercentPaint.setTextScaleX(1.0f);
        mPercentPaint.setStyle(Paint.Style.FILL);
        mPercentPaint.setTextAlign(Paint.Align.CENTER);

        mBatteryOutline = BitmapFactory.decodeResource(this.getResources(), R.drawable.battery_outline);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // Account for padding
        float xpad = (float)(getPaddingLeft() + getPaddingRight());
        float ypad = (float)(getPaddingTop() + getPaddingBottom());

        float ww = (float)w - xpad;
        float hh = (float)h - ypad;

        mWidth = ww;
        mHeight = hh;
        mTextHeight = hh / 2.0f;
        mPercentPaint.setTextSize(mTextHeight);

        invalidate();
        requestLayout();
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        // Try for a width based on our minimum
//        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
//        int w = resolveSizeAndState(minw, widthMeasureSpec, 1);
//
//        // Whatever the width ends up being, ask for a height that would let the battery
//        // get as big as it can
//        int minh = MeasureSpec.getSize(w) - (int)mWidth + getPaddingBottom() + getPaddingTop();
//        int h = resolveSizeAndState(MeasureSpec.getSize(w) - (int)mWidth, heightMeasureSpec, 0);
//
//        setMeasuredDimension(w, h);
//        mWidth = w;
//        mHeight = h;
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float left = mWidth * 0.06f;
        float top = mHeight * 0.05f;
        canvas.drawRect(left, top, left + mWidth * mCharge * 0.82f, top +  mHeight * 0.85f, mChargeBarPaint);

        if (mShowPercent) {
            Paint.FontMetrics metric = mPercentPaint.getFontMetrics();
            int textHeight = (int) Math.ceil(metric.descent - metric.ascent);
            canvas.drawText((int) (mCharge * 100.0f) + "%", mWidth / 2.0f, mHeight / 2.0f + textHeight / 3, mPercentPaint);
        }

        canvas.drawBitmap(mBatteryOutline, null, new RectF(0, 0, mWidth, mHeight), mOutlinePaint);
    }

    public float getCharge() {
        return mCharge;
    }
    public void setCharge(float charge) {
        mCharge = Math.max(0.0f, Math.min(1.0f, charge));

        mChargeBarPaint.setColor((int) new ArgbEvaluator().evaluate(mCharge, mLowColor, mHighColor));

        invalidate();
        requestLayout();

        // invoke onEvent() of the charge event listener
        if (mListener != null)
            mListener.onEvent();
    }

    public boolean isShowPercent() {
        return mShowPercent;
    }
    public void setShowPercent(boolean showPercent) {
        mShowPercent = showPercent;
        invalidate();
        requestLayout();
    }

    public int getHighColor() {
        return mHighColor;
    }
    public void setHighColor(int highColor) {
        mHighColor = highColor;
        invalidate();
        requestLayout();
    }

    public int getLowColor() {
        return mLowColor;
    }
    public void setLowColor(int lowColor) {
        mLowColor = lowColor;
        invalidate();
        requestLayout();
    }
}