package de.hddesign.androidutils.androidutils.custom;


import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import de.hddesign.androidutils.androidutils.R;

public class RulerView extends View {
    private final Rect txtBounds;
    private float pxmm = 480 / 67.f;
    private float dpxmm = 0;
    private boolean imperialUnits;

    private Paint rulerPaint;
    private Paint textPaint;

    private int backgroundColor;
    private int indicatorColor;
    private int textColor;
    private float textSize;

    float lineHeight;

    public void setdpxmm(float dpxmm) {
        this.dpxmm = dpxmm;
    }

    public RulerView(Context context, AttributeSet attr) {
        super(context, attr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attr, R.styleable.RulerView, 0, 0);

        try {
            backgroundColor = a.getColor(R.styleable.RulerView_backgroundColor, Color.WHITE);
            indicatorColor = a.getColor(R.styleable.RulerView_rulerIndicatorColor, Color.BLACK);
            textColor = a.getColor(R.styleable.RulerView_textColor, Color.BLACK);
            textSize = a.getDimension(R.styleable.RulerView_textSize, 25);
            imperialUnits = a.getBoolean(R.styleable.RulerView_inch, false);
        } finally {
            a.recycle();
        }

        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();

        float xdpmm = metrics.xdpi / 25.4f;
        this.setdpxmm(xdpmm);

        rulerPaint = new Paint();
        textPaint = new Paint();

        txtBounds = new Rect();

        initPaints();
    }

    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        lineHeight = h;
    }

    public void onDraw(Canvas canvas) {
        float pxmm;
        int unitsep = 10;

        if (dpxmm == 0)
            pxmm = this.pxmm;
        else
            pxmm = dpxmm;


        if (imperialUnits) {
            pxmm *= 25.4f / 16;
            unitsep = 16;
        }

        canvas.drawColor(backgroundColor);

        for (int i = 1; ; ++i) {
            float xGap = i * pxmm;
            if (xGap > canvas.getWidth()) {
                break;
            }

            int size;

            if (imperialUnits) {
                int sizeHelper = (i * 10000 / unitsep);

                if (sizeHelper % unitsep == 0)
                    size = (int) lineHeight;
                else if (sizeHelper % 5000 == 0)
                    size = (int) (lineHeight / 1.33f);
                else if (sizeHelper % 2500 == 0)
                    size = (int) (lineHeight / 2);
                else if (sizeHelper % 1250 == 0)
                    size = (int) (lineHeight / 3);
                else
                    size = (int) (lineHeight / 6);

            } else
                size = (int) ((i % unitsep == 0) ? (lineHeight / 2) : (i % (unitsep / 2) == 0) ? (lineHeight / 3) : (lineHeight / 6));

            canvas.drawLine(xGap, lineHeight - size, xGap, lineHeight, rulerPaint);
        }

        for (int i = 1; i <= 18; ++i) {
            canvas.drawText("" + i, i * pxmm * unitsep - 10, 5 + txtBounds.height(), textPaint);
        }

        if (imperialUnits)
            canvas.drawText("inch", 10, 5 + txtBounds.height(), textPaint);
        else
            canvas.drawText("cm", 10, 5 + txtBounds.height(), textPaint);
    }

    private void initPaints() {
        rulerPaint.setStyle(Paint.Style.STROKE);
        rulerPaint.setStrokeWidth(0);
        rulerPaint.setAntiAlias(false);
        rulerPaint.setColor(indicatorColor);

        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setStrokeWidth(0);
        textPaint.setAntiAlias(true);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);

        textPaint.getTextBounds("0", 0, 1, txtBounds);
    }

    public boolean isImperialUnits() {
        return imperialUnits;
    }

    public void setImperialUnits(boolean imperialUnits) {
        this.imperialUnits = imperialUnits;
        this.invalidate();
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        initPaints();
        this.invalidate();
    }

    public int getIndicatorColor() {
        return indicatorColor;
    }

    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
        initPaints();
        this.invalidate();
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        initPaints();
        this.invalidate();
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        initPaints();
        this.invalidate();
    }
}