package de.hddesign.androidutils.androidutils.custom;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

import de.hddesign.androidutils.androidutils.R;


public class DPad extends View implements View.OnTouchListener, View.OnDragListener {

    private Point center;
    private float dpiFactor;

    private Paint topButtonPaint, bottomButtonPaint, rightButtonPaint, lefttButtonPaint, centerButtonPaint, topChevronPaint, bottomChevronPaint, rightChevronPaint, leftChevronPaint, centerTextPaint, backgroundPaint;

    private int primaryColor;
    private int backgroundColor;
    private int accentColor;
    private int windowColor;
    private int chevronColor;
    private float centerButtonRadius;
    private float centerButtonRadiusPx;
    private float buttonPadding;
    private float size;
    private float chevronSize;
    private float chevronStrokeWidth;
    private float centerTextSize;
    private float chevronPadding;

    private String centerText;

    private DPadTouchListener dPadTouchListener;

    private RectF arcRect;
    private Path chevronPath;

    private boolean initialized;

    private DPadCallback dPadCallback;

    public void setdPadCallback(DPadCallback dPadCallback) {
        this.dPadCallback = dPadCallback;
    }

    public interface DPadCallback {
        void layoutReady();
    }

    public void setdPadTouchListener(DPadTouchListener dPadTouchListener) {
        this.dPadTouchListener = dPadTouchListener;
    }

    public DPad(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.DPad, 0, 0);

        try {
            primaryColor = a.getColor(R.styleable.DPad_primaryColor, Color.rgb(0, 166, 255));
            accentColor = a.getColor(R.styleable.DPad_accentColor, Color.rgb(255, 89, 0));
            chevronColor = a.getColor(R.styleable.DPad_chevronColor, Color.rgb(0, 166, 255));
            backgroundColor = a.getColor(R.styleable.DPad_backgroundColor, Color.rgb(255, 255, 255));
            windowColor = a.getColor(R.styleable.DPad_windowColor, Color.argb(255, 250, 250, 250));
            centerButtonRadius = a.getDimension(R.styleable.DPad_centerButtonRadius, 150);
            buttonPadding = a.getDimension(R.styleable.DPad_buttonPadding, 5);
            size = a.getDimension(R.styleable.DPad_size, 0);
            chevronSize = a.getDimension(R.styleable.DPad_chevronSize, 25);
            chevronStrokeWidth = a.getDimension(R.styleable.DPad_chevronStrokeWidth, 8);
            chevronPadding = a.getDimension(R.styleable.DPad_chevronPadding, 20);
            centerTextSize = a.getDimension(R.styleable.DPad_textSize, 25);
            centerText = a.getString(R.styleable.DPad_centerText);
        } finally {
            a.recycle();
        }

        if (centerText == null)
            centerText = "please add\ncenterText";

        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        dpiFactor = (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);

        center = new Point();

        arcRect = new RectF();
        createPaints();

        this.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (!initialized) {
                    initialized = true;

                    if (size == 0)
                        size = getWidth();
                    initView();

                    if (dPadCallback != null)
                        dPadCallback.layoutReady();
                }
            }
        });

        this.setOnTouchListener(this);
    }

    private void initView() {
        center.set(((int) (size / 2)), ((int) (size / 2)));

        centerButtonRadiusPx = convertDpToPixel(centerButtonRadius);

        initPaints();

        float innerMargin = 1;

        float height = size - 2 * innerMargin;
        float l = (size - height) / 2;

        arcRect.set(l, innerMargin, l + height, innerMargin + height);

        chevronPath = new Path();
        chevronPath.moveTo(size / 2 - chevronSize, chevronSize + chevronPadding);
        chevronPath.lineTo(size / 2, 0 + chevronPadding);
        chevronPath.lineTo(size / 2 + chevronSize, chevronSize + chevronPadding);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (size <= 0)
            return;

        drawDPAD(canvas);
        drawDividerLines(canvas);
        drawSensorButton(canvas);
        drawDividerCircle(canvas);
        drawChevrons(canvas);

        drawMultiline(centerText, size / 2, size / 2, centerTextPaint, canvas);
    }

    private void drawChevrons(Canvas canvas) {
        canvas.drawPath(chevronPath, topChevronPaint);
        canvas.rotate(90, size / 2, size / 2);
        canvas.drawPath(chevronPath, rightChevronPaint);
        canvas.rotate(90, size / 2, size / 2);
        canvas.drawPath(chevronPath, bottomChevronPaint);
        canvas.rotate(90, size / 2, size / 2);
        canvas.drawPath(chevronPath, leftChevronPaint);
        canvas.rotate(90, size / 2, size / 2);
    }

    private void drawDPAD(Canvas canvas) {
        canvas.drawArc(arcRect, 225, 90, true, topButtonPaint);
        canvas.drawArc(arcRect, 315, 90, true, rightButtonPaint);
        canvas.drawArc(arcRect, 45, 90, true, bottomButtonPaint);
        canvas.drawArc(arcRect, 135, 90, true, lefttButtonPaint);
    }

    private void drawDividerLines(Canvas canvas) {
        canvas.rotate(45, size / 2, size / 2);
        canvas.drawLine(size / 2, size / 2, size / 2, 0, backgroundPaint);
        canvas.rotate(90, size / 2, size / 2);
        canvas.drawLine(size / 2, size / 2, size / 2, 0, backgroundPaint);
        canvas.rotate(90, size / 2, size / 2);
        canvas.drawLine(size / 2, size / 2, size / 2, 0, backgroundPaint);
        canvas.rotate(90, size / 2, size / 2);
        canvas.drawLine(size / 2, size / 2, size / 2, 0, backgroundPaint);
        canvas.rotate(45, size / 2, size / 2);
    }

    private void drawSensorButton(Canvas canvas) {
        canvas.drawCircle(size / 2, size / 2, centerButtonRadius - buttonPadding * 2, centerButtonPaint);
    }

    private void drawDividerCircle(Canvas canvas) {
        canvas.drawCircle(size / 2, size / 2, centerButtonRadius - buttonPadding * 2, backgroundPaint);
    }

    public void drawMultiline(String str, float x, float y, Paint paint, Canvas canvas) {
        int lines = 0;

        float paintHeight = -paint.ascent() + paint.descent();

        for (String line : str.split("\n"))
            lines++;

        y -= paintHeight * lines / 2;
        y += paintHeight / 1.2;

        for (String line : str.split("\n")) {
            canvas.drawText(line, x, y, paint);
            y += paintHeight;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Point a = new Point((int) event.getX(), (int) event.getY());

        if (distanceBetweenPoints(a, center) < ((size - 20) / 2)) {
            DPadButton touchedButton = calculateTouchedButton(a);

            if (dPadTouchListener != null) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    if (touchedButton != null) {
                        switch (touchedButton) {
                            case TOP:
                                dPadTouchListener.onTopButtonDown();
                                topButtonPaint.setColor(accentColor);
                                topChevronPaint.setColor(backgroundColor);
                                this.invalidate();
                                return true;
                            case BOTTOM:
                                dPadTouchListener.onBottomButtonDown();
                                bottomButtonPaint.setColor(accentColor);
                                bottomChevronPaint.setColor(backgroundColor);
                                this.invalidate();
                                return true;
                            case LEFT:
                                dPadTouchListener.onLeftButtonDown();
                                lefttButtonPaint.setColor(accentColor);
                                leftChevronPaint.setColor(backgroundColor);
                                this.invalidate();
                                return true;
                            case RIGHT:
                                dPadTouchListener.onRightButtonDown();
                                rightButtonPaint.setColor(accentColor);
                                rightChevronPaint.setColor(backgroundColor);
                                this.invalidate();
                                return true;
                            case SENSOR:
                                dPadTouchListener.onCenterButtonDown();
                                centerButtonPaint.setColor(accentColor);
                                centerTextPaint.setColor(backgroundColor);
                                this.invalidate();
                                return true;
                        }
                    }
                } else if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_OUTSIDE || event.getAction() == MotionEvent.ACTION_HOVER_EXIT) {
                    resetPaintColor();
                    dPadTouchListener.onDPadButtonUp();
                    this.invalidate();
                    return true;
                }
            }
        } else {
            if (dPadTouchListener != null)
                dPadTouchListener.onDPadButtonUp();

            resetPaintColor();
            this.invalidate();
            return true;
        }

        return true;
    }

    private void resetPaintColor() {
        topButtonPaint.setColor(backgroundColor);
        bottomButtonPaint.setColor(backgroundColor);
        rightButtonPaint.setColor(backgroundColor);
        lefttButtonPaint.setColor(backgroundColor);
        centerButtonPaint.setColor(backgroundColor);

        topChevronPaint.setColor(chevronColor);
        bottomChevronPaint.setColor(chevronColor);
        rightChevronPaint.setColor(chevronColor);
        leftChevronPaint.setColor(chevronColor);

        centerTextPaint.setColor(primaryColor);
    }

    private DPadButton calculateTouchedButton(Point a) {
        Point b = new Point(a.x, 0);

        if (distanceBetweenPoints(a, center) < centerButtonRadiusPx / 2)
            return DPadButton.SENSOR;
        else {
            double sideA = distanceBetweenPoints(b, center);
            double sideB = distanceBetweenPoints(a, center);
            double sideC = distanceBetweenPoints(a, b);

            double angleAlpha = Math.toDegrees(Math.acos(((sideA * sideA) - (sideC * sideC) - (sideB * sideB)) / (-2 * (sideB * sideC))));

            float originXFromCenter = a.x - (size / 2);
            float originYFromCenter = a.y - (size / 2);

            if (originXFromCenter < 0 && originYFromCenter > 0)
                angleAlpha += 90;

            if (originXFromCenter < 0 && originYFromCenter < 0)
                angleAlpha = 270 + (angleAlpha - 180);

            if (originXFromCenter > 0 && originYFromCenter < 0)
                angleAlpha = 270 + (180 - angleAlpha);

            if (originXFromCenter > 0 && originYFromCenter > 0)
                angleAlpha = 90 - angleAlpha;

            if (angleAlpha > 315 || angleAlpha < 45)
                return DPadButton.RIGHT;

            if (angleAlpha > 45 && angleAlpha < 135)
                return DPadButton.BOTTOM;

            if (angleAlpha > 135 && angleAlpha < 225)
                return DPadButton.LEFT;

            if (angleAlpha > 225 && angleAlpha < 315)
                return DPadButton.TOP;
        }

        return null;
    }

    public double distanceBetweenPoints(Point p1, Point p2) {
        return Math.sqrt(((p1.x - p2.x) * (p1.x - p2.x)) + ((p1.y - p2.y) * (p1.y - p2.y)));
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        return false;
    }

    public interface DPadTouchListener {
        void onCenterButtonDown();

        void onTopButtonDown();

        void onBottomButtonDown();

        void onRightButtonDown();

        void onLeftButtonDown();

        void onDPadButtonUp();
    }

    private float convertDpToPixel(float dp) {
        return dp * dpiFactor;
    }

    private void createPaints() {
        backgroundPaint = new Paint();
        topButtonPaint = new Paint();
        bottomButtonPaint = new Paint();
        lefttButtonPaint = new Paint();
        rightButtonPaint = new Paint();
        centerButtonPaint = new Paint();
        topChevronPaint = new Paint();
        bottomChevronPaint = new Paint();
        rightChevronPaint = new Paint();
        leftChevronPaint = new Paint();
        centerTextPaint = new Paint();
    }

    private void initPaints() {
        backgroundPaint.setColor(windowColor);
        backgroundPaint.setStrokeWidth(buttonPadding);
        backgroundPaint.setStyle(Style.STROKE);
        backgroundPaint.setAntiAlias(true);

        topButtonPaint.setColor(backgroundColor);
        topButtonPaint.setStyle(Style.FILL);
        topButtonPaint.setAntiAlias(true);

        bottomButtonPaint.setColor(backgroundColor);
        bottomButtonPaint.setStyle(Style.FILL);
        bottomButtonPaint.setAntiAlias(true);

        lefttButtonPaint.setColor(backgroundColor);
        lefttButtonPaint.setStyle(Style.FILL);
        lefttButtonPaint.setAntiAlias(true);

        rightButtonPaint.setColor(backgroundColor);
        rightButtonPaint.setStyle(Style.FILL);
        rightButtonPaint.setAntiAlias(true);

        centerButtonPaint.setColor(backgroundColor);
        centerButtonPaint.setStyle(Style.FILL);
        centerButtonPaint.setAntiAlias(true);

        topChevronPaint.setColor(chevronColor);
        topChevronPaint.setStyle(Style.STROKE);
        topChevronPaint.setStrokeWidth(chevronStrokeWidth);
        topChevronPaint.setAntiAlias(true);

        bottomChevronPaint.setColor(chevronColor);
        bottomChevronPaint.setStyle(Style.STROKE);
        bottomChevronPaint.setStrokeWidth(chevronStrokeWidth);
        bottomChevronPaint.setAntiAlias(true);

        rightChevronPaint.setColor(chevronColor);
        rightChevronPaint.setStyle(Style.STROKE);
        rightChevronPaint.setStrokeWidth(chevronStrokeWidth);
        rightChevronPaint.setAntiAlias(true);

        leftChevronPaint.setColor(chevronColor);
        leftChevronPaint.setStyle(Style.STROKE);
        leftChevronPaint.setStrokeWidth(chevronStrokeWidth);
        leftChevronPaint.setAntiAlias(true);

        centerTextPaint.setColor(primaryColor);
        centerTextPaint.setStyle(Style.STROKE);
        centerTextPaint.setTextSize(centerTextSize);
        centerTextPaint.setTextAlign(Paint.Align.CENTER);
        centerTextPaint.setAntiAlias(true);
    }

    public enum DPadButton {
        TOP,
        BOTTOM,
        LEFT,
        RIGHT,
        SENSOR;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int desiredWidth = (int) size;
        int desiredHeight = (int) size;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(desiredWidth, widthSize);
        } else {
            width = desiredWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(desiredHeight, heightSize);
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            height = width;
        } else {
            height = desiredHeight;
        }

        size = Math.min(width, height);

        setMeasuredDimension(width, height);
    }

    public void setPrimaryColor(int primaryColor) {
        this.primaryColor = primaryColor;
        this.initPaints();
        this.invalidate();
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        this.initPaints();
        this.invalidate();
    }

    public void setAccentColor(int accentColor) {
        this.accentColor = accentColor;
        this.initPaints();
        this.invalidate();
    }

    public void setWindowColor(int windowColor) {
        this.windowColor = windowColor;
        this.initPaints();
        this.invalidate();
    }

    public void setChevronColor(int chevronColor) {
        this.chevronColor = chevronColor;
        this.initPaints();
        this.invalidate();
    }

    public void setCenterButtonRadius(float centerButtonRadius) {
        this.centerButtonRadius = centerButtonRadius;
        this.initView();
        this.invalidate();
    }

    public void setButtonPadding(float buttonPadding) {
        this.buttonPadding = buttonPadding;
        this.initView();
        this.invalidate();
    }

    public void setChevronSize(float chevronSize) {
        this.chevronSize = chevronSize;
        this.initView();
        this.invalidate();
    }

    public void setChevronStrokeWidth(float chevronStrokeWidth) {
        this.chevronStrokeWidth = chevronStrokeWidth;
        this.initView();
        this.invalidate();
    }

    public void setChevronPadding(float chevronPadding) {
        this.chevronPadding = chevronPadding;
        this.initView();
        this.invalidate();
    }

    public void setCenterTextSize(float centerTextSize) {
        this.centerTextSize = centerTextSize;
        this.initView();
        this.invalidate();
    }

    public void setCenterText(String centerText) {
        this.centerText = centerText;
        this.initView();
        this.invalidate();
    }

    public int getPrimaryColor() {
        return primaryColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getAccentColor() {
        return accentColor;
    }

    public int getWindowColor() {
        return windowColor;
    }

    public int getChevronColor() {
        return chevronColor;
    }

    public float getChevronSize() {
        return chevronSize;
    }

    public float getChevronStrokeWidth() {
        return chevronStrokeWidth;
    }

    public float getChevronPadding() {
        return chevronPadding;
    }

    public float getCenterButtonRadius() {
        return centerButtonRadius;
    }

    public float getButtonPadding() {
        return buttonPadding;
    }

    public float getSize() {
        return size;
    }

    public String getCenterText() {
        return centerText;
    }

    public float getCenterTextSize() {
        return centerTextSize;
    }
}
