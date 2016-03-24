package de.hddesign.androidutils.androidutils.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import de.hddesign.androidutils.androidutils.R;

public class AspectRatioLayout extends RelativeLayout {

    private float aspectRatio;
    private int width;
    private int height;
    private int w;
    private int h;
    private int measuredWidth;
    private int measuredHeight;

    public float setAspectRatio(int width, int height) {
        this.aspectRatio = (float) width / (float) height;
        this.requestLayout();
        return aspectRatio;
    }

    public AspectRatioLayout(Context context) {
        super(context);
    }

    public AspectRatioLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AspectRatioLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AspectRatioLayout);
        width = a.getInt(R.styleable.AspectRatioLayout_aspectRatioWidth, 16);
        height = a.getInt(R.styleable.AspectRatioLayout_aspectRatioHeight, 9);
        aspectRatio = (float) width / (float) height;
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = widthMode == MeasureSpec.UNSPECIFIED ? Integer.MAX_VALUE : MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = heightMode == MeasureSpec.UNSPECIFIED ? Integer.MAX_VALUE : MeasureSpec.getSize(heightMeasureSpec);

        if (heightMode == MeasureSpec.EXACTLY && widthMode == MeasureSpec.EXACTLY) {
        /*
         * Possibility 1: Both width and height fixed
         */
            measuredWidth = widthSize;
            measuredHeight = heightSize;
        } else if (heightMode == MeasureSpec.EXACTLY) {
        /*
         * Possibility 2: Width dynamic, height fixed
         */
            measuredWidth = (int) Math.min(widthSize, Math.ceil(heightSize * aspectRatio));
            measuredHeight = (int) Math.ceil(measuredWidth / aspectRatio);
            measuredHeight = MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY);
            measuredWidth = MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY);
        } else if (widthMode == MeasureSpec.EXACTLY) {
        /*
         * Possibility 3: Width fixed, height dynamic
         */
            measuredHeight = (int) Math.min(heightSize, Math.ceil(widthSize / aspectRatio));
            measuredWidth = (int) Math.ceil(measuredHeight * aspectRatio);
            measuredHeight = MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY);
            measuredWidth = MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY);
        } else {
        /*
         * Possibility 4: Both width and height dynamic
         */
            if (widthSize > heightSize * aspectRatio) {
                measuredHeight = heightSize;
                measuredWidth = (int) Math.ceil(measuredHeight * aspectRatio);
                measuredWidth = MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY);
            } else {
                measuredWidth = widthSize;
                measuredHeight = (int) Math.ceil(measuredWidth / aspectRatio);
                measuredHeight = MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY);
            }
        }
        super.onMeasure(measuredWidth, measuredHeight);
    }
}
