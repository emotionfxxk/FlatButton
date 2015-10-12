package com.minarc.flatbutton;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by sean on 10/10/15.
 */
public class FlatButton extends AppCompatButton {
    private boolean mIsRoundCorner;
    private float mCornerRadius;
    private int mStrokeColor, mSolidColor, mStrokeWidth, mNormalTextColor, mPressedTextColor;
    private final static float DEFAULT_PADDING = 16.0f;
    public FlatButton(Context context) {
        super(context);
        loadDefaultAttr(context);
        init();
    }
    public FlatButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadDefaultAttr(context);
        loadAttr(context, attrs);
        init();
    }
    public FlatButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadDefaultAttr(context);
        loadAttr(context, attrs);
        init();
    }
    private void loadDefaultAttr(Context context) {
        final float density = getResources().getDisplayMetrics().density;
        mIsRoundCorner = true;
        mCornerRadius = 3.0f * density; // default radius
        mStrokeWidth = (int) (0.75f * density); // default stroke width
        mNormalTextColor = mStrokeColor = mSolidColor =
                context.getResources().getColor(R.color.colorPrimary);
        mPressedTextColor = context.getResources().getColor(android.R.color.white);
    }
    private void loadAttr(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.FlatButton);
        final int count = a.getIndexCount();
        for (int i = 0; i < count; ++i) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.FlatButton_roundCorner:
                    mIsRoundCorner = a.getBoolean(attr, true);
                    break;
                case R.styleable.FlatButton_cornerRadius:
                    mCornerRadius = a.getDimension(attr, mCornerRadius);
                    break;
                case R.styleable.FlatButton_strokeWidth:
                    mStrokeWidth = a.getDimensionPixelSize(attr, mStrokeWidth);
                    break;
                case R.styleable.FlatButton_strokeColor:
                    mStrokeColor = a.getColor(attr,
                            context.getResources().getColor(R.color.colorPrimary));
                    break;
                case R.styleable.FlatButton_solidColor:
                    mSolidColor = a.getColor(attr,
                            context.getResources().getColor(R.color.colorPrimary));
                    break;
                case R.styleable.FlatButton_normalTextColor:
                    mNormalTextColor = a.getColor(attr, mNormalTextColor);
                    break;
                case R.styleable.FlatButton_pressedTextColor:
                    mPressedTextColor = a.getColor(attr, mPressedTextColor);
                    break;
            }
        }
        a.recycle();
    }
    private void init() {
        Log.d("SEAN", "mIsRoundCorner:" + mIsRoundCorner + ", strokeWidth:" + mStrokeWidth +
            "cornerRadius:" + mCornerRadius);
        // set padding
        final float density = getResources().getDisplayMetrics().density;
        int padding = (int) (density * DEFAULT_PADDING);
        setPadding(padding, 0, padding, 0);
        // create & set background drawable
        LayerDrawable bg_normal = (LayerDrawable) getResources().getDrawable(mIsRoundCorner ?
                R.drawable.flat_button_bg_normal : R.drawable.flat_button_rect_bg_normal);
        LayerDrawable bg_pressed = (LayerDrawable) getResources().getDrawable(mIsRoundCorner ?
                R.drawable.flat_button_bg_pressed : R.drawable.flat_button_rect_bg_pressed);
        GradientDrawable normalShape = (GradientDrawable) bg_normal.findDrawableByLayerId(R.id.item);
        GradientDrawable pressedShape = (GradientDrawable)bg_pressed.findDrawableByLayerId(R.id.item);
        normalShape.setStroke(mStrokeWidth, mStrokeColor);
        pressedShape.setStroke(mStrokeWidth, mStrokeColor);
        pressedShape.setColor(mSolidColor);
        if (mIsRoundCorner) {
            ((GradientDrawable) normalShape.mutate()).setCornerRadius(mCornerRadius);
            ((GradientDrawable) pressedShape.mutate()).setCornerRadius(mCornerRadius);
        }
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, bg_pressed);
        stateListDrawable.addState(new int[]{}, bg_normal);
        setBackground(stateListDrawable);

        // set text color
        ColorStateList colorStateList = new ColorStateList(
                new int[][] {new int[] {android.R.attr.state_pressed}, new int[] {}},
                new int[] {mPressedTextColor, mNormalTextColor}
        );
        setTextColor(colorStateList);
    }
}
